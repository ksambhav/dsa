package org.dsa.arrays;

import java.util.Stack;

public class Nearest {

    public static int[] greaterInRight(int[] input) {
        int[] result = new int[input.length];
        Stack<Integer> stack = new Stack<>();
        result[input.length - 1] = -1;
        stack.push(input[input.length - 1]);
        for (int i = input.length - 2; i > -1; i--) {
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

    public static int[] smallerInRight(int[] input) {
        int[] result = new int[input.length];
        Stack<Integer> stack = new Stack<>();
        result[input.length - 1] = -1;
        stack.push(input[input.length - 1]);
        for (int i = input.length - 2; i > -1; i--) {
            if (stack.peek() < input[i]) {
                result[i] = stack.peek();
            } else {
                try {
                    while (stack.peek() >= input[i]) {
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

    public static int[] smallerInLeft(int[] input) {
        int[] result = new int[input.length];
        Stack<Integer> stack = new Stack<>();
        result[0] = -1;
        stack.push(input[0]);
        for (int i = 1; i < input.length; i++) {
            if (stack.peek() < input[i]) {
                result[i] = stack.peek();
            } else {
                try {
                    while (stack.peek() >= input[i]) {
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

    public static int[] greaterInLeft(int[] input) {
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
