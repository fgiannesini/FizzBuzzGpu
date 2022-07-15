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

    public record Context(cl_context context, cl_device_id device) implements AutoCloseable {
        public static Context getInstance() {
            cl_platform_id platform = getCl_platform_id();

            // Initialize the context properties
            cl_context_properties contextProperties = new cl_context_properties();
            contextProperties.addProperty(CL.CL_CONTEXT_PLATFORM, platform);

            cl_device_id device = getCl_device_id(platform);
            cl_context context = CL.clCreateContext(contextProperties, 1, new cl_device_id[]{device}, null, null, null);
            return new Context(context, device);
        }

        private static cl_device_id getCl_device_id(cl_platform_id platform) {
            // Obtain the number of devices for the platform
            int[] numDevicesArray = new int[1];
            final long deviceType = CL.CL_DEVICE_TYPE_ALL;
            CL.clGetDeviceIDs(platform, deviceType, 0, null, numDevicesArray);
            int numDevices = numDevicesArray[0];

            // Obtain a device ID
            cl_device_id[] devices = new cl_device_id[numDevices];
            CL.clGetDeviceIDs(platform, deviceType, numDevices, devices, null);
            return devices[0];
        }

        private static cl_platform_id getCl_platform_id() {
            // Obtain the number of platforms
            int[] numPlatformsArray = new int[1];
            CL.clGetPlatformIDs(0, null, numPlatformsArray);

            // Obtain a platform ID
            cl_platform_id[] platforms = new cl_platform_id[numPlatformsArray[0]];
            CL.clGetPlatformIDs(platforms.length, platforms, null);
            return platforms[0];
        }

        @Override
        public void close() {
            CL.clReleaseContext(context);
        }
    }

    public record CommandQueue(cl_command_queue commandQueue) implements AutoCloseable {

        public static CommandQueue from(Context context) {
            return new CommandQueue(CL.clCreateCommandQueue(context.context(), context.device(), 0, null));
        }

        public void executeKernel(Kernel kernel, int size) {
            long[] global_work_size = new long[]{size};
            long[] local_work_size = new long[]{1};
            CL.clEnqueueNDRangeKernel(commandQueue, kernel.kernel, 1, null, global_work_size, local_work_size, 0, null, null);
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

    public record Program(cl_program program) implements AutoCloseable {

        public static Program from(Context context, String programSource) {
            cl_program program = CL.clCreateProgramWithSource(context.context, 1, new String[]{programSource}, null, null);
            CL.clBuildProgram(program, 0, null, null, null, null);
            return new Program(program);
        }

        @Override
        public void close() {
            CL.clReleaseProgram(program);
        }
    }

    public record Kernel(cl_kernel kernel) implements AutoCloseable {

        public static Kernel from(Program program, String kernelName) {
            cl_kernel kernel = CL.clCreateKernel(program.program, kernelName, null);
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

    public static abstract class MemObject implements AutoCloseable {
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

    public static class MemObjectToRead extends MemObject {

        public static MemObjectToRead memObjectToRead(Context context, int[] src) {
            return new MemObjectToRead(context, src);
        }

        private MemObjectToRead(Context context, int[] src) {
            super(CL.clCreateBuffer(context.context, CL.CL_MEM_READ_ONLY | CL.CL_MEM_COPY_HOST_PTR, (long) Sizeof.cl_int * src.length, Pointer.to(src), null));
        }

    }

    public static class MemObjectToWrite extends MemObject {

        public static MemObjectToWrite memObjectToWrite(Context context, int length) {
            return new MemObjectToWrite(context.context, length);
        }

        private MemObjectToWrite(cl_context context, int length) {
            super(CL.clCreateBuffer(context, CL.CL_MEM_READ_WRITE, (long) Sizeof.cl_int * length, null, null));
        }

        public cl_mem getMemObject() {
            return memObject;
        }

    }


}