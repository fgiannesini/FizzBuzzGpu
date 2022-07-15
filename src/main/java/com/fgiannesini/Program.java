package com.fgiannesini;

import org.jocl.CL;
import org.jocl.cl_program;

public record Program(cl_program program) implements AutoCloseable {

    public static Program from(Context context, String programSource) {
        cl_program program = CL.clCreateProgramWithSource(context.context(), 1, new String[]{programSource}, null, null);
        CL.clBuildProgram(program, 0, null, null, null, null);
        return new Program(program);
    }

    @Override
    public void close() {
        CL.clReleaseProgram(program);
    }
}
