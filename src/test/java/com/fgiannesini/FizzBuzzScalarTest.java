package com.fgiannesini;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class FizzBuzzScalarTest {

    @Test
    void should_check_fizz_buzz_sequential_scalar() {
        FizzBuzzScalar fizzBuzz = new FizzBuzzScalar();
        int[] results = fizzBuzz.scalarSequentialFizzBuzz(new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15});
        assertArrayEquals(new int[]{-3, 1, 2, -1, 4, -2, -1, 7, 8, -1, -2, 11, -1, 13, 14, -3}, results);
    }

    @Test
    void should_check_fizz_buzz_parallel_scalar() {
        FizzBuzzScalar fizzBuzz = new FizzBuzzScalar();
        int[] results = fizzBuzz.scalarParallelFizzBuzz(new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15});
        assertArrayEquals(new int[]{-3, 1, 2, -1, 4, -2, -1, 7, 8, -1, -2, 11, -1, 13, 14, -3}, results);
    }

}