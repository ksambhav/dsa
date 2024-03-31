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
                    dp[i][j] = 0;
                }
            }
        }
        return max;
    }
}
