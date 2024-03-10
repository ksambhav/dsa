package org.dsa.dp;

import lombok.extern.slf4j.Slf4j;
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
}