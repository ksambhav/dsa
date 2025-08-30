package org.dsa.concurrency;

import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class BoundedBuffer {

    private final int maxSize;
    private final Lock lock = new ReentrantLock();
    private final LinkedList<String> buffer = new LinkedList<>();
    private final Condition bufferIsFull = lock.newCondition();
    private final Condition bufferIsEmpty = lock.newCondition();

    public BoundedBuffer(int maxSize) {
        this.maxSize = maxSize;
    }

    public static void main(String[] args) throws InterruptedException {
        final BoundedBuffer boundedBuffer = new BoundedBuffer(5);
        final AtomicInteger messageNo = new AtomicInteger(0);
        Runnable producer = () -> {
            for (int i = 0; i < 4; i++) {
                boundedBuffer.add("Message %d".formatted(messageNo.incrementAndGet()));
            }
        };
        Runnable consumer = () -> {
            for (int i = 0; i < 4; i++) {
                log.info("Polled {}", boundedBuffer.poll());
            }
        };
        try (ExecutorService executors = Executors.newVirtualThreadPerTaskExecutor()) {
            executors.submit(producer);
            executors.submit(consumer);
//            executors.shutdown();
//            executors.awaitTermination(3, TimeUnit.SECONDS);
        }
    }

    public void add(String message) {
        try {
            lock.lock();
            while (buffer.size() >= maxSize) {
                bufferIsFull.await();
            }
            Thread.sleep(200);
            buffer.add(message);
            bufferIsEmpty.signalAll();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            lock.unlock();
        }
    }

    public String poll() {
        try {
            lock.lock();
            while (buffer.isEmpty()) {
                bufferIsEmpty.await();
            }
            Thread.sleep(100);
            String message = buffer.poll();
            bufferIsFull.signalAll();
            return message;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        } finally {
            lock.unlock();
        }
    }
}
