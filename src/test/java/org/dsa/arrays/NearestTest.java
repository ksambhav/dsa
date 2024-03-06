package org.dsa.arrays;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

class NearestTest {

    @Test
    void nearestGreaterInLeft() {
        int[] result = Nearest.greaterInLeft(new int[]{10, 5, 7, 8, 2, 5, 3, 7, 0, 12});
        System.out.println(Arrays.toString(result));
    }

    @Test
    void nearestSmallerInLeft() {
        int[] result = Nearest.smallerInLeft((new int[]{1, 5, 3, 8, 2, 5, 3, 7, 0, 12}));
        System.out.println(Arrays.toString(result));
    }

    @Test
    void nearestSmallerInRight() {
        int[] result = Nearest.smallerInRight((new int[]{10, 5, 3, 8, 2, 5, 3, 7, 0, 12}));
        System.out.println(Arrays.toString(result));
    }

    @Test
    void greaterInRight() {
        int[] result = Nearest.greaterInRight((new int[]{1, 5, 3, 8, 2, 5, 3, 7, 0, 12}));
        System.out.println(Arrays.toString(result));
    }
}