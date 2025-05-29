package com.abbos.maang.ocp;

import org.junit.Test;

/**
 * @author Aliabbos Ashurov
 * @since 2025-05-29
 */
public class Blocks {

    @Test
    public void switchTest() {
        final int score1 = 8, score2 = 3;
        Integer myScore = 7;

        var goal = switch (myScore) {
            case score1, score2, 7 -> "good";
            case Integer i when i < 10 -> "better";
            case Integer i when i >= 10 -> "best";
            case null -> "nope";
            default -> {
                yield "unknown";
            }
        };
        System.out.println(goal);
    }
}
