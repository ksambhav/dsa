package org.dsa.arrays;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

class NearestGreaterTest {

    @Test
    void nearestGreaterInLeft() {
        int[] result = NearestGreater.nearestGreaterInLeft(new int[]{10, 5, 7, 8, 2, 5, 3, 7, 54, 12});
        System.out.println(Arrays.toString(result));
    }
}