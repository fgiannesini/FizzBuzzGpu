package com.fgiannesini;

import org.jocl.CL;
import org.jocl.Pointer;
import org.jocl.Sizeof;

public class MemObjectToRead extends MemObject {

    public static MemObjectToRead memObjectToRead(Context context, int[] src) {
        return new MemObjectToRead(context, src);
    }

    private MemObjectToRead(Context context, int[] src) {
        super(CL.clCreateBuffer(context.context(), CL.CL_MEM_READ_ONLY | CL.CL_MEM_COPY_HOST_PTR, (long) Sizeof.cl_int * src.length, Pointer.to(src), null));
    }

}
