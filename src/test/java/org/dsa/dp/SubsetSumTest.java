package org.dsa.dp;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@Slf4j
class SubsetSumTest {


    @Test
    void testIsSubsetSum() {
        var subsetSum = SubsetSum.isSubsetSum(6,
                new int[]{3, 34, 4, 12, 5, 2},
                30);
        assertFalse(subsetSum);
    }

    @Test
    void perfectSum() {
        var result = SubsetSum.perfectSum(new int[]{9, 7, 0, 3, 9, 8, 6, 5, 7, 6}, 10, 31);
        log.info("{}", result);
        assertEquals(40, result);
    }

    @Test
    void isSubsetSum() {
    }

    @Test
    void minimumSubsetSumDifference() {
    }

    @Test
    void equalPartition() {
        int i = SubsetSum.equalPartition(45, new int[]{
                307, 781, 523, 494, 950, 899, 387, 329, 977, 510, 388, 203, 627,
                139, 959, 650, 459, 669, 182, 147, 429, 721, 175, 659, 15,
                415, 63, 740, 657, 555, 736, 316, 335, 258, 161, 636, 508,
                547, 965, 484, 56, 704, 38, 34, 843

        });
        Assertions.assertEquals(1, i);
    }
}