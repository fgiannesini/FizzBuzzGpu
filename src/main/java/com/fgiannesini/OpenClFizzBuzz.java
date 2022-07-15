package com.fgiannesini;

import org.jocl.*;

import java.util.Arrays;

public class OpenClFizzBuzz {
    private static final String programSource =
            """
                    __kernel void
                    sampleKernel(__global const int *a,
                                 __global const int *b,
                                 __global int *c)
                    {
                        int gid = get_global_id(0);
                        c[gid] = a[gid] + b[gid];
                    }
                    """;

    public int[] run(int[] srcArrayA) {
        int n = srcArrayA.length;
        int[] srcArrayB = Arrays.copyOf(srcArrayA, n);

        CL.setExceptionsEnabled(true);

        try (
                Context context = Context.getInstance();
                CommandQueue commandQueue = CommandQueue.from(context);
                Program program = Program.from(context, programSource);
                Kernel kernel = Kernel.from(program, "sampleKernel");
                MemObjectToRead memObjectsA = MemObjectToRead.memObjectToRead(context, srcArrayA);
                MemObjectToRead memObjectsB = MemObjectToRead.memObjectToRead(context, srcArrayB);
                MemObjectToWrite memObjectsC = MemObjectToWrite.memObjectToWrite(context, n)
        ) {
            kernel.addArgument(memObjectsA, 0);
            kernel.addArgument(memObjectsB, 1);
            kernel.addArgument(memObjectsC, 2);

            commandQueue.executeKernel(kernel, n);
            return commandQueue.readData(memObjectsC, n);
        }
    }

}