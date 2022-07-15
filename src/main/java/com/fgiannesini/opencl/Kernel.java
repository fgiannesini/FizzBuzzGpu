package com.fgiannesini.opencl;

import org.jocl.CL;
import org.jocl.Sizeof;
import org.jocl.cl_kernel;

public record Kernel(cl_kernel kernel) implements AutoCloseable {

    public static Kernel from(Program program, String kernelName) {
        cl_kernel kernel = CL.clCreateKernel(program.program(), kernelName, null);
        return new Kernel(kernel);
    }

    public void addArgument(MemObject memObject, int index) {
        CL.clSetKernelArg(kernel, index, Sizeof.cl_mem, memObject.getPointer());
    }

    @Override
    public void close() {
        CL.clReleaseKernel(kernel);
    }
}
