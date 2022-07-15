package com.fgiannesini;

import org.jocl.*;

import java.util.Arrays;

public class OpenClFizzBuzz {
    private static final String programSource =
            """
                    __kernel void
                    computeFizzBuzz(__global const int *input,
                                 __global int *destination)
                    {
                        int gid = get_global_id(0);
                        int result = input[gid];
                        if (result % 3 == 0 && result % 5 == 0) {
                            result = -3;
                        } else if (result % 3 == 0) {
                            result = -1;
                        } else if (result % 5 == 0) {
                            result = -2;
                        }
                        destination[gid] = result;
                    }
                    """;

    public int[] run(int[] srcArray) {
        int inputSize = srcArray.length;

        CL.setExceptionsEnabled(true);

        try (
                Context context = Context.getInstance();
                CommandQueue commandQueue = CommandQueue.from(context);
                Program program = Program.from(context, programSource);
                Kernel kernel = Kernel.from(program, "computeFizzBuzz");
                MemObjectToRead memObjectsA = MemObjectToRead.memObjectToRead(context, srcArray);
                MemObjectToWrite memObjectsC = MemObjectToWrite.memObjectToWrite(context, inputSize)
        ) {
            kernel.addArgument(memObjectsA, 0);
            kernel.addArgument(memObjectsC, 1);

            commandQueue.executeKernel(kernel, inputSize);
            return commandQueue.readData(memObjectsC, inputSize);
        }
    }

}