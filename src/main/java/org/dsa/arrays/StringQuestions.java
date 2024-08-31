package org.dsa.arrays;

import java.util.HashMap;
import java.util.Map;

public class StringQuestions {
    public static int longestNonRepeatingSubstring() {
        return -1;
    }

    /**
     * <a href="https://www.geeksforgeeks.org/problems/longest-k-unique-characters-substring0853/1"></a>
     * Given a string you need to print the size of the longest possible substring that has <b>exactly</b> K unique characters. If there is no possible substring then print -1.
     */
    public static int longestkSubstr(String s, int k) {
        int longest = 0;
        Map<Character, Integer> map = new HashMap<>();
        int i = 0;
        for (int j = 0; j < s.length(); j++) {
            char c = s.charAt(j);
            map.put(c, map.getOrDefault(c, 0) + 1);
            if (map.size() == k) {
                // found an answer
                longest = Integer.max(longest, j - i + 1);
            } else if (map.size() > k) {
                // contract window from left
                while (map.size() != k) {
                    map.computeIfPresent(s.charAt(i), (character, integer) -> integer - 1);
                    if (map.get(s.charAt(i)) == 0) {
                        map.remove(s.charAt(i));
                    }
                    i++;
                }

            }
        }
        return longest == 0 ? -1 : longest;
    }

    public static void main(String[] args) {
        System.out.println(longestSubstrDistinctChars("geeksforgeeks"));
    }


    /**
     * Given a string S, find the length of the longest substring with all distinct characters.
     */
    static int longestSubstrDistinctChars(String S) {
        int longest = 0;
        int i = 0;
        Map<Character, Integer> map = new HashMap<>();
        for (int j = 0; j < S.length(); j++) {
            map.put(S.charAt(j), 1 + map.getOrDefault(S.charAt(j), 0));
            if (map.size() == j - i + 1) {
                // found an answer
                longest = Integer.max(longest, j - i + 1);
            } else if (map.size() < j - i + 1) {
                while (map.size() != j - i + 1) {
                    map.computeIfPresent(S.charAt(i), (character, integer) -> integer - 1);
                    if (map.get(S.charAt(i)) == 0) {
                        map.remove(S.charAt(i));
                    }
                    i++;
                }
            }
        }
        return longest;
    }
}
