package org.dsa.concurrency;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConcurrentLogProcessor {

    private final BlockingQueue<String> logQueue;
    private final ExecutorService logWriterExecutor;
    private final ScheduledExecutorService flusherScheduler;
    private final String logFilePath;
    private final Lock fileLock;
    private volatile AtomicBoolean running = new AtomicBoolean(true); // To control workers and graceful shutdown
    private final int flushIntervalSeconds; // For periodic flushing
    private final int flushEntryThreshold; // For threshold-based flushing

    private PrintWriter printWriter; // Managed by the log writer thread

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    public ConcurrentLogProcessor(String logFilePath, int writerThreads, int flushIntervalSeconds, int flushEntryThreshold) throws IOException {
        this.logFilePath = logFilePath;
        this.logQueue = new LinkedBlockingQueue<>();
        this.logWriterExecutor = Executors.newFixedThreadPool(writerThreads);
        this.flusherScheduler = Executors.newSingleThreadScheduledExecutor();
        this.fileLock = new ReentrantLock();
        this.flushIntervalSeconds = flushIntervalSeconds;
        this.flushEntryThreshold = flushEntryThreshold;
        // Initialize PrintWriter once, within a try-with-resources to ensure closure at the end
        // However, for continuous writing, we initialize it and close on shutdown.
        // It's crucial that file access is always protected by `fileLock`.
        try {
            this.printWriter = new PrintWriter(new BufferedWriter(new FileWriter(logFilePath, true))); // true for append mode
        } catch (IOException e) {
            System.err.println("Error initializing log file writer: " + e.getMessage());
            throw e; // Re-throw to indicate a critical setup failure
        }


        // Start log writer tasks
        for (int i = 0; i < writerThreads; i++) {
            logWriterExecutor.submit(new LogWriterTask());
        }

        // Schedule periodic flushing
        flusherScheduler.scheduleAtFixedRate(this::flushLogs, flushIntervalSeconds, flushIntervalSeconds, TimeUnit.SECONDS);

        System.out.println("LogProcessor initialized. Logs will be written to: " + Paths.get(logFilePath).toAbsolutePath());
    }

    /**
     * Submits a log entry to be processed.
     * @param log The log message.
     */
    public void submitLogEntry(String log) {
        if (!running.get()) {
            System.err.println("LogProcessor is shutting down, refusing new log entry: " + log);
            return;
        }
        String timestampedLog = String.format("[%s] [%s] %s",
                Thread.currentThread().getName(),
                LocalDateTime.now().format(FORMATTER),
                log);
        try {
            logQueue.put(timestampedLog); // Using put for blocking if queue is full (though LinkedBlockingQueue is unbounded here)
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restore interrupt status
            System.err.println("Failed to submit log entry due to interruption: " + log);
        }
    }

    /**
     * Flushes any buffered logs to the file. This method is called periodically or manually.
     */
    private void flushLogs() {
        fileLock.lock();
        try {
            if (printWriter != null) {
                printWriter.flush();
                // System.out.println("Logs flushed periodically."); // For debugging
            }
        } finally {
            fileLock.unlock();
        }
    }

    /**
     * The task executed by worker threads to consume logs from the queue and write them to the file.
     */
    private class LogWriterTask implements Runnable {
        @Override
        public void run() {
            int writtenEntries = 0;
            while (running.get() || !logQueue.isEmpty()) { // Continue running while active or queue has items
                try {
                    String logEntry = logQueue.poll(100, TimeUnit.MILLISECONDS); // Poll with timeout
                    if (logEntry != null) {
                        fileLock.lock();
                        try {
                            if (printWriter != null) {
                                printWriter.println(logEntry);
                                writtenEntries++;
                                // Optional: Flush based on threshold
                                if (flushEntryThreshold > 0 && writtenEntries >= flushEntryThreshold) {
                                    printWriter.flush();
                                    writtenEntries = 0; // Reset counter
                                    // System.out.println("Logs flushed by entry threshold."); // For debugging
                                }
                            }
                        } finally {
                            fileLock.unlock();
                        }
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.out.println("LogWriterTask interrupted. Exiting.");
                    break;
                } catch (Exception e) {
                    System.err.println("Error in LogWriterTask: " + e.getMessage());
                }
            }
            System.out.println(Thread.currentThread().getName() + " shutting down.");
        }
    }

    /**
     * Initiates a graceful shutdown of the log processor.
     * Ensures all pending logs are written before exiting.
     */
    public void shutdown() {
        System.out.println("Initiating LogProcessor shutdown...");
        running.set(false); // Signal workers to stop accepting new tasks and process existing ones

        // Shutdown flusher scheduler immediately
        flusherScheduler.shutdown();
        try {
            if (!flusherScheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                flusherScheduler.shutdownNow();
                System.err.println("Flusher scheduler did not terminate cleanly.");
            }
        } catch (InterruptedException e) {
            flusherScheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }

        // Shutdown log writer executor
        logWriterExecutor.shutdown(); // Disable new tasks, but existing tasks will complete
        try {
            // Wait for existing tasks to terminate
            while (!logQueue.isEmpty()) {
                System.out.println("Waiting for " + logQueue.size() + " logs to be processed...");
                Thread.sleep(500); // Give workers time to process
            }
            if (!logWriterExecutor.awaitTermination(30, TimeUnit.SECONDS)) { // Give time for tasks to finish
                System.err.println("Log writer executor did not terminate cleanly within 30 seconds. Forcing shutdown.");
                logWriterExecutor.shutdownNow(); // Forcefully shut down
            }
        } catch (InterruptedException e) {
            logWriterExecutor.shutdownNow();
            Thread.currentThread().interrupt();
            System.err.println("Shutdown interrupted.");
        } finally {
            // Ensure final flush and close PrintWriter
            fileLock.lock();
            try {
                if (printWriter != null) {
                    System.out.println("Performing final flush and closing log file.");
                    printWriter.flush();
                    printWriter.close();
                    printWriter = null; // Mark as closed
                }
            } finally {
                fileLock.unlock();
            }
        }
        System.out.println("LogProcessor shutdown complete.");
    }

    public static void main(String[] args) {
        String testLogFile = "application.log";
        ConcurrentLogProcessor processor = null;

        try {
            processor = new ConcurrentLogProcessor(testLogFile, 2, 3, 10); // 2 writers, flush every 3 secs, or every 10 entries

            // Simulate multiple threads submitting logs
            ExecutorService submitters = Executors.newFixedThreadPool(5);
            for (int i = 0; i < 5; i++) {
                final int threadId = i;
                ConcurrentLogProcessor finalProcessor = processor;
                submitters.submit(() -> {
                    for (int j = 0; j < 20; j++) {
                        finalProcessor.submitLogEntry("Message " + j + " from submitter thread " + threadId);
                        try {
                            Thread.sleep((long) (Math.random() * 50)); // Simulate varied submission rates
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            break;
                        }
                    }
                });
            }

            // Also submit some logs from the main thread
            for (int i = 0; i < 5; i++) {
                processor.submitLogEntry("Main thread log message " + i);
                Thread.sleep(100);
            }


            submitters.shutdown();
            System.out.println("All submitters have finished queuing logs. Waiting for them to terminate...");
            if (!submitters.awaitTermination(15, TimeUnit.SECONDS)) {
                System.err.println("Submitters did not terminate cleanly.");
            }

            // Give some time for logs to be processed after submitters finish
            System.out.println("Giving a moment for final logs to be written...");
            Thread.sleep(5000); // Wait for 5 seconds

        } catch (IOException e) {
            System.err.println("Failed to initialize LogProcessor: " + e.getMessage());
            return; // Exit if setup fails
        } catch (InterruptedException e) {
            System.err.println("Main thread interrupted during sleep.");
            Thread.currentThread().interrupt();
        } finally {
            if (processor != null) {
                processor.shutdown();
            }
        }

        System.out.println("\n--- Check the '" + testLogFile + "' file for output. ---");
    }
}