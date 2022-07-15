package com.fgiannesini.opencl;

import org.jocl.*;

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
