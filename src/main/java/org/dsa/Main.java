package org.dsa.stack;

import lombok.extern.slf4j.Slf4j;

import java.util.Stack;

@Slf4j
public class Main {

    public static void main(String[] args) {
        Stack<Integer> stack = new Stack<>();
        stack.push(20);
        stack.push(20);
        stack.push(20);
        stack.push(30);
        log.info("{}", stack);
    }
}