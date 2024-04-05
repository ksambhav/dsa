package org.dsa.dp;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@Slf4j
class KnapsackTest {

    @Test
    void recursiveZeroOne() {
        int maxProfit = Knapsack.recursiveWithMemo(new int[]{4, 5, 1},
                new int[]{1, 2, 3},
                4,
                3
        );
        Assertions.assertEquals(3, maxProfit);
    }

    @Test
    void testDynamic() {
        int maxProfit = Knapsack.dynamic(new int[]{4, 5, 1},
                new int[]{1, 2, 3},
                4
        );
        Assertions.assertEquals(3, maxProfit);
    }
}