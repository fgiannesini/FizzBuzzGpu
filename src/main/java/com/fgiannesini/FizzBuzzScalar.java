package com.fgiannesini;

import java.util.Arrays;

public class FizzBuzzScalar {

    private static final int FIZZ = -1;
    private static final int BUZZ = -2;
    private static final int FIZZ_BUZZ = -3;

    public int[] scalarSequentialFizzBuzz(int[] values) {
        return Arrays.stream(values).map(this::isFizzBuzz).toArray();
    }

    public int[] scalarParallelFizzBuzz(int[] values) {
        return Arrays.stream(values)
                .parallel()
                .map(this::isFizzBuzz)
                .toArray();
    }

    private int isFizzBuzz(int result) {
        if (result % 3 == 0 && result % 5 == 0) {
            result = FIZZ_BUZZ;
        } else if (result % 3 == 0) {
            result = FIZZ;
        } else if (result % 5 == 0) {
            result = BUZZ;
        }
        return result;
    }
}
