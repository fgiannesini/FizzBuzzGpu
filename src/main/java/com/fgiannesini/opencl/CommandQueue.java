package com.fgiannesini.opencl;

import org.jocl.CL;
import org.jocl.Pointer;
import org.jocl.Sizeof;
import org.jocl.cl_command_queue;

public record CommandQueue(cl_command_queue commandQueue) implements AutoCloseable {

    public static CommandQueue from(Context context) {
        return new CommandQueue(CL.clCreateCommandQueueWithProperties(context.context(), context.device(), null, null));
    }

    public void executeKernel(Kernel kernel, int size) {
        long[] global_work_size = new long[]{size};
        long[] local_work_size = new long[]{1};
        CL.clEnqueueNDRangeKernel(commandQueue, kernel.kernel(), 1, null, global_work_size, local_work_size, 0, null, null);
    }

    public int[] readData(MemObjectToWrite memObject, int destinationLength) {
        int[] destination = new int[destinationLength];
        long size = (long) Sizeof.cl_float * destinationLength;
        CL.clEnqueueReadBuffer(commandQueue, memObject.getMemObject(), CL.CL_TRUE, 0, size, Pointer.to(destination), 0, null, null);
        return destination;
    }

    @Override
    public void close() {
        CL.clReleaseCommandQueue(commandQueue);
    }
}
