package com.fgiannesini;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

import java.util.random.RandomGenerator;

@Warmup(iterations = 2)
@Measurement(iterations = 2)
@Fork(value = 1)
public class Bench {

    private int[] data() {
        return RandomGenerator.getDefault().ints(8_000_000, 0, Integer.MAX_VALUE).toArray();
    }

    @Benchmark
    public void should_measure_fizz_buzz_sequential_scalar(Blackhole bh) {
        FizzBuzzScalar fizzBuzz = new FizzBuzzScalar();
        bh.consume(fizzBuzz.scalarSequentialFizzBuzz(data()));
    }

    @Benchmark
    public void should_measure_fizz_buzz_parallel_scalar(Blackhole bh) {
        FizzBuzzScalar fizzBuzz = new FizzBuzzScalar();
        bh.consume(fizzBuzz.scalarParallelFizzBuzz(data()));
    }

    @Benchmark
    public void should_measure_fizz_buzz_open_cl(Blackhole bh) {
        FizzBuzzOpenCl fizzBuzz = new FizzBuzzOpenCl();
        bh.consume(fizzBuzz.run(data()));
    }
}
