package com.fgiannesini.opencl;

import org.jocl.CL;
import org.jocl.Sizeof;
import org.jocl.cl_context;
import org.jocl.cl_mem;

public class MemObjectToWrite extends MemObject {

    public static MemObjectToWrite memObjectToWrite(Context context, int length) {
        return new MemObjectToWrite(context.context(), length);
    }

    private MemObjectToWrite(cl_context context, int length) {
        super(CL.clCreateBuffer(context, CL.CL_MEM_READ_WRITE, (long) Sizeof.cl_int * length, null, null));
    }

    public cl_mem getMemObject() {
        return memObject;
    }

}
