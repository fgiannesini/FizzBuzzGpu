package com.fgiannesini.opencl;

import org.jocl.CL;
import org.jocl.Pointer;
import org.jocl.cl_mem;

public abstract class MemObject implements AutoCloseable {
    protected final cl_mem memObject;

    protected MemObject(cl_mem memObject) {
        this.memObject = memObject;
    }

    public Pointer getPointer() {
        return Pointer.to(memObject);
    }

    @Override
    public void close() {
        CL.clReleaseMemObject(memObject);
    }
}
