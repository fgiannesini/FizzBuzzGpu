package com.fgiannesini;

import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class OpenClFizzBuzzTest {

    @Test
    void should_compute_fizz_buzz_on_gpu_using_opencl() {
        int n = 10;
        float[] srcArray = new float[n];
        IntStream.range(0, n).forEach(i -> srcArray[i] = i);
        float[] destArray = new OpenClFizzBuzz().run(srcArray);

        float[] expectedArray = new float[n];
        IntStream.range(0, n).forEach(i -> expectedArray[i] = i * 2);
        assertArrayEquals(expectedArray, destArray);
    }
}