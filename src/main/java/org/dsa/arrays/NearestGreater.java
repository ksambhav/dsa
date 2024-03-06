package org.dsa.arrays;

import java.util.Stack;

public class NearestGreater {

    public static int[] nearestGreaterInLeft(int[] input) {
        int[] result = new int[input.length];
        Stack<Integer> stack = new Stack<>();
        result[0] = -1;
        stack.push(input[0]);
        for (int i = 1; i < input.length; i++) {
            if (stack.peek() > input[i]) {
                result[i] = stack.peek();
            } else {
                try {
                    while (stack.peek() <= input[i]) {
                        stack.pop();
                    }
                    result[i] = stack.peek();

                } catch (Exception e) {
                    result[i] = -1;
                }
            }
            stack.push(input[i]);
        }
        return result;
    }
}
