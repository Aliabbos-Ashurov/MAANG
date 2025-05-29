package com.abbos.maang.ocp;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Aliabbos Ashurov
 * @since 2025-05-29
 */
@RunWith(Enclosed.class)
public class Simple {

    @Test
    public void arguments() {
        var $num = (Integer) null;
        float value = 102.00f;
    }

    @Test
    public void starMoon() {
        int moon = 9, star = 2 + 2 * 3;
        float sun = star > 10 ? 1 : 3;

        double jupiter = (sun + moon) - 1.0f;
        int mars = --moon <= 8 ? 2 : 3;

        System.out.printf("Sun: %s, Jupiter: %s, Mars: %s%n", sun, jupiter, mars);
    }



    public static class MathFunctions {
        public static void addToInt(int x, int amountToAdd) {
            x = x + amountToAdd % -5;
        }

        @Test
        public void testAddToInt() {
            var a = 15;
            var b = 10;
            MathFunctions.addToInt(a, b);
            System.out.println(a);
        }
    }

    /**
     * <code>
     * Locale.of("uz","UZ")
     * </code>
     */
    @Test
    public void testProperties() {
        Locale fr = Locale.of("fr");
        Locale.setDefault(Locale.of("en", "US"));
        var b = ResourceBundle.getBundle("Penguin", fr);
        System.out.println(b.getString("name"));
        System.out.println(b.getString("age"));
    }

    @Test
    public void arrayMethodsTest() {
        int[] array = { 6, 9, 8 };
        System.out.println("B" + Arrays.binarySearch(array, 9));
        System.out.println("C" + Arrays.compare(array, new int[]{ 6, 9, 8 }));
        System.out.println("M" + Arrays.mismatch(array, new int[]{ 6, 9, 8 }));

    }

    /**
     * <code>
     * _ 0 0 0 0 0 0 1  =  1 <p>
     * _ 0 0 0 0 0 1 0  =  2 <p>
     * _ 0 0 0 0 0 1 1  =  3 <p>
     * _ 0 0 0 0 1 0 0  =  4 <p>
     * _ 0 0 0 0 1 0 1  =  5 <p>
     * _ 0 0 0 0 1 1 0  =  6 <p>
     * _ 0 0 0 0 1 1 1  =  7 <p>
     * _ 0 0 0 1 0 0 0  =  8 <p>
     * _ 0 0 0 1 0 0 1  =  9 <p>
     * _ 0 0 0 1 0 1 0   =  10 <p>
     * </code>
     */
    @Test
    public void testBi() {
        System.out.println(10 >>> 1);
    }
}

