package com.fgiannesini;

import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class OpenClFizzBuzzTest {

    @Test
    void should_compute_fizz_buzz_on_gpu_using_opencl() {
        int n = 16;
        int[] srcArray = IntStream.range(0, n).toArray();
        int[] destArray = new OpenClFizzBuzz().run(srcArray);

        int[] expectedArray = IntStream.range(0, n).map(i -> i * 2).toArray();
        assertArrayEquals(expectedArray, destArray);
    }
}