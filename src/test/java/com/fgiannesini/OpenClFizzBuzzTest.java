package com.fgiannesini;

import org.junit.jupiter.api.Test;

import java.util.random.RandomGenerator;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class OpenClFizzBuzzTest {

    @Test
    void should_compute_fizz_buzz_on_gpu_using_opencl() {
        int[] destArray = new OpenClFizzBuzz().run(new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15});
        assertArrayEquals(new int[]{-3, 1, 2, -1, 4, -2, -1, 7, 8, -1, -2, 11, -1, 13, 14, -3}, destArray);
    }

    @Test
    void should_check_random_fizz_buzz() {
        OpenClFizzBuzz openClFizzBuzz = new OpenClFizzBuzz();
        FizzBuzzScalar fizzBuzzScalar = new FizzBuzzScalar();
        int[] input = RandomGenerator.getDefault().ints(8, 0, Integer.MAX_VALUE).toArray();
        assertArrayEquals(fizzBuzzScalar.scalarSequentialFizzBuzz(input), openClFizzBuzz.run(input));
    }
}