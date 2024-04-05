package org.dsa.dp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

import static java.lang.Integer.max;

public class Knapsack {

    public static final Logger log = LoggerFactory.getLogger(Knapsack.class);


    /**
     * Find maximum value item that can be put in Knapsack
     *
     * @param weight      array of item's weight
     * @param value       array of item's value
     * @param maxCapacity capacity of knapsack
     * @return result
     */
    public static int dynamic(int[] weight, int[] value, int maxCapacity) {
        int n = weight.length;
        int[][] t = new int[n + 1][maxCapacity + 1];
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= maxCapacity; j++) {
                if (weight[i - 1] <= j) {
                    int valueIfChosen = t[i - 1][j - weight[i - 1]] + value[i - 1];
                    int valueIfNotChosen = t[i - 1][j];
                    t[i][j] = max(valueIfChosen, valueIfNotChosen);
                } else {
                    t[i][j] = t[i - 1][j];
                }
            }
        }
        return t[n][maxCapacity];
    }

    /**
     * The data set is traversed backwards
     */
    public static int recursive(int[] weight, int[] value, int capacity, int length) {
        if (length == 0 || capacity == 0) {
            log.info("Nothing to put or can go in bag");
            return 0;
        } else if (weight[length - 1] <= capacity) {
            return max(
                    value[length - 1] + recursive(weight, value, capacity - weight[length - 1], length - 1),
                    recursive(weight, value, capacity, length - 1)
            );
        } else {
            log.info("Not selecting as its overweight");
            return recursive(weight, value, capacity, length - 1);
        }
    }

    public static int recursiveWithMemo(int[] weight, int[] value, int capacity, int length) {
        int[][] cache = new int[length + 1][capacity + 1];
        for (int[] result : cache) {
            Arrays.fill(result, -1);
        }
        return maxProfit(weight, value, capacity, length, cache);
    }

    private static int maxProfit(int[] weight, int[] value, int capacity, int length, int[][] cache) {
        if (length == 0 || capacity == 0) {
            log.info("Nothing to put or can go in bag");
            return 0;
        } else if (cache[length][capacity] != -1) {
            log.info("From the memo");
            return cache[length][capacity];
        } else if (weight[length - 1] <= capacity) {
            int max = max(
                    value[length - 1] + maxProfit(weight, value, capacity - weight[length - 1], length - 1, cache),
                    maxProfit(weight, value, capacity, length - 1, cache)
            );
            cache[length][capacity] = max;
            return max;
        } else {
            log.info("Not selecting as its overweight");
            int maxProfit = maxProfit(weight, value, capacity, length - 1, cache);
            cache[length][capacity] = maxProfit;
            return maxProfit;
        }
    }

    static int knapsackUnbounded(int n, int w, int[] profit, int[] weight) {
        int[][] t = new int[n + 1][w + 1];
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= w; j++) {
                if (weight[i - 1] <= j) {
                    //item selected
                    t[i][i] = max(
                            profit[i - 1] + t[i][j - weight[i - 1]],
                            t[i - 1][j]
                    );
                } else {
                    t[i][j] = t[i - 1][j];
                }
            }
        }
        return t[n][w];
    }
}
