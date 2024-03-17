package org.dsa.dp;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SubsetSum {

    static boolean isSubsetSum(int N, int[] arr, int sum) {
        boolean[][] t = new boolean[N + 1][sum + 1];
        for (int j = 0; j <= sum; j++) {
            t[0][j] = false;
        }
        for (int i = 0; i <= N; i++) {
            t[i][0] = true;
        }
        for (int i = 1; i <= N; i++) {
            for (int j = 1; j <= sum; j++) {
                if (arr[i - 1] <= j) {
                    t[i][j] = t[i - 1][j - arr[i - 1]] || t[i - 1][j];
                } else {
                    t[i][j] = t[i - 1][j];
                }
            }
        }
        return t[N][sum];
    }

    /**
     * Given an array arr of non-negative integers and an integer sum, the task is
     * to count all subsets of the given array with a sum equal to a given sum.
     */
    public static int perfectSum(int[] arr, int n, int sum) {
        int[][] dp = new int[n + 1][sum + 1];
        for (int j = 0; j <= sum; j++) {
            dp[0][j] = 0;
        }
        int zeroCount = 0;
        for (int i = 0; i <= n; i++) {
            dp[i][0] = 1;
            if (i > 0) {
                if (arr[i - 1] == 0) {
                    zeroCount++;
                    dp[i][0] = (int) Math.pow(2, zeroCount);
                }
            }
        }
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= sum; j++) {
                if (arr[i - 1] <= j) {
                    dp[i][j] = dp[i - 1][j - arr[i - 1]] + dp[i - 1][j];
                } else {
                    dp[i][j] = dp[i - 1][j];
                }
            }
        }
        return dp[n][sum];
    }

    public static int minimumSubsetSumDifference() {
        return -1;
    }

    /**
     * Given an array arr[] of size N, check if it can be partitioned into
     * two parts such that the sum of elements in both parts is the same.
     */
    public static int equalPartition(int n, int[] arr) {
        boolean canBeSplit = false;
        int arraySum = 0;
        for (int k : arr) {
            arraySum += k;
        }
        if (arraySum % 2 == 0) {
            int sum = arraySum / 2;
            int[][] dp = new int[n + 1][sum + 1];
            for (int j = 0; j <= sum; j++) {
                dp[0][j] = 0;
            }
            int zeroCount = 0;
            for (int i = 0; i <= n; i++) {
                dp[i][0] = 1;
                if (i > 0) {
                    if (arr[i - 1] == 0) {
                        zeroCount++;
                        dp[i][0] = (int) Math.pow(2, zeroCount);
                    }
                }
            }
            for (int i = 1; i <= n; i++) {
                for (int j = 1; j <= sum; j++) {
                    if (arr[i - 1] <= j) {
                        dp[i][j] = dp[i - 1][j - arr[i - 1]] + dp[i - 1][j];
                    } else {
                        dp[i][j] = dp[i - 1][j];
                    }
                }
            }
            canBeSplit = dp[n][sum] > 0;
        }
        return canBeSplit ? 1 : 0;
    }
}
