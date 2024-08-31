package org.dsa.dp;

/**
 * Common Sub-sequence
 */
public class Subsequence {

    /**
     * Function to find the length of longest common subsequence in two strings.
     */
    static int lcs(int x, int y, String s1, String s2) {
        // recursive approach
        /*if (x == 0 || y == 0) {
            return 0;
        } else if (s1.charAt(x - 1) == s2.charAt(y - 1)) {
            return 1 + lcs(x - 1, y - 1, s1, s2);
        } else {
            return Integer.max(
                    lcs(x, y - 1, s1, s2),
                    lcs(x - 1, y, s1, s2)
            );
        }*/
        // DP approach
        {
            int[][] dp = new int[x + 1][y + 1];
            for (int i = 1; i <= x; i++) {
                for (int j = 1; j <= y; j++) {
                    if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                        dp[i][j] = 1 + dp[i - 1][j - 1];
                    } else {
                        dp[i][j] = Integer.max(
                                dp[i][j - 1],
                                dp[i - 1][j]
                        );
                    }
                }
            }
            return dp[x][y];
        }
    }

    /**
     * Given two strings. The task is to find the length of the longest common substring.
     * <a><a href="https://www.geeksforgeeks.org/problems/longest-common-substring1452/1?itm_source=geeksforgeeks&itm_medium=article&itm_campaign=bottom_sticky_on_article">...</a></a>
     */
    int longestCommonSubstr(String S1, String S2, int n, int m) {
        int[][] dp = new int[n + 1][m + 1];
        int max = 0;
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                if (S1.charAt(i - 1) == S2.charAt(j - 1)) {
                    dp[i][j] = 1 + dp[i - 1][j - 1];
                    max = Integer.max(max, dp[i][j]);
                } else {
                    dp[i][j] = 0; // this is the only import change when compared to "sub-sequence" version
                }
            }
        }
        return max;
    }


    /**
     * Given two strings text1 and text2, return the length of their longest common subsequence. If there is no common subsequence, return 0.
     * <a>https://leetcode.com/problems/longest-common-subsequence/</a>
     */
    public int longestCommonSubsequence(String text1, String text2) {
//        return lcsRecursive(text1, text1.length(), text2, text2.length());
        int m = text1.length();
        int n = text2.length();
        int[][] dp = new int[m + 1][n + 1];
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (text1.charAt(i - 1) == text2.charAt(j - 1)) {
                    dp[i][j] = 1 + dp[i - 1][j - 1];
                } else {
                    dp[i][j] = Integer.max(dp[i][j - 1], dp[i - 1][j]);
                }
            }
        }
        return dp[m][n];
    }
/*
    private int lcsRecursive(String text1, int a, String text2, int b) {
        if (a <= 0 || b <= 0) {
            return 0;
        } else {
            if (text1.charAt(a - 1) == text2.charAt(b - 1)) {
                return 1 + lcsRecursive(text1, a - 1, text2, b - 1);
            } else {
                return Integer.max(
                        lcsRecursive(text1, a, text2, b - 1),
                        lcsRecursive(text1, a - 1, text2, b)
                );

            }
        }
    }*/

    /**
     * Given a string s, find the longest palindromic subsequence's length in s.
     * <a href="https://leetcode.com/problems/longest-palindromic-subsequence/description/">A subsequence is a sequence that can be derived from another sequence by deleting some or no elements without changing the order of the remaining elements. </a>
     */
    public int longestPalindromeSubseq(String s) {
        return longestCommonSubsequence(s, new StringBuilder(s).reverse().toString());
    }
}
