package org.dsa.dp;

import java.util.HashMap;
import java.util.Map;

/**
 * Have the function MinWindowSubstring(strArr) take the array of strings stored in strArr, which will contain only two strings,
 * the first parameter being the string N and the second parameter being a string K of some characters,
 * and your goal is to determine the smallest substring of N that contains all the characters in K.
 * For example: if strArr is ["aaabaaddae", "aed"] then the smallest substring of N that contains the characters a, e, and d is "dae" located at the end of the string.
 * So for this example your program should return the string dae.
 * <p>
 * Another example: if strArr is ["aabdccdbcacd", "aad"] then the smallest substring of N that contains all of the characters in K is "aabd"
 * which is located at the beginning of the string. Both parameters will be strings ranging in length from 1 to 50 characters and all of K's characters
 * will exist somewhere in the string N. Both strings will only contains lowercase alphabetic characters.
 * <a href="https://coderbyte.com/editor/Min%20Window%20Substring:Java">Min Window Substring</a>
 */
public class MinWindowSubstring {
    public static String MinWindowSubstring(String[] strArr) {
        String x = strArr[0];
        String y = strArr[1];
        int minLength = Integer.MAX_VALUE;
        Map<Character, Integer> demandMap = new HashMap<>();
        for (char c : y.toCharArray()) {
            demandMap.put(c, demandMap.getOrDefault(c, 0) + 1);
        }
        int count = demandMap.size();
        int i = 0;
        int start = 0;
        int end = 0;
        for (int j = 0; j < x.length(); j++) {
            Integer updatedDemand = demandMap.computeIfPresent(x.charAt(j), (c, n) -> n - 1);
            if (updatedDemand != null && updatedDemand == 0) {
                count--;
            }
            if (count == 0) {
                // found a solution
                while (count == 0) {
                    if (j - i + 1 < minLength) {
                        minLength = j - i + 1;
                        start = i;
                        end = j;
                    }
                    Integer updatedCount = demandMap.computeIfPresent(x.charAt(i), (c, n) -> n + 1);
                    if (updatedCount != null && updatedCount > 0) {
                        count++;
                    }
                    i++;
                }

            }
        }
        return x.substring(start, end + 1);
    }
}
