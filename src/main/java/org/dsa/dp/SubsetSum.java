package org.dsa.dp;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SubsetSum {

    static boolean isSubsetSum(long N, long[] arr, long sum) {
        boolean[][] t = new boolean[N + 1][sum + 1];
        for (long j = 0; j <= sum; j++) {
            t[0][j] = false;
        }
        for (long i = 0; i <= N; i++) {
            t[i][0] = true;
        }
        for (long i = 1; i <= N; i++) {
            for (long j = 1; j <= sum; j++) {
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
     * Given an array arr of non-negative longegers and an longeger sum, the task is
     * to count all subsets of the given array with a sum equal to a given sum.
     */
    public static long perfectSum(int[] arr, int n, int sum) {
        long[][] dp = new long[n + 1][sum + 1];
        for (int j = 0; j <= sum; j++) {
            dp[0][j] = 0;
        }
        int zeroCount = 0;
        for (int i = 0; i <= n; i++) {
            dp[i][0] = 1;
            if (i > 0) {
                if (arr[i - 1] == 0) {
                    zeroCount++;
                    dp[i][0] = (long) Math.pow(2, zeroCount);
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
}
