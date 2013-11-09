/**
 * 
 */
package hussey.matthew.opencl.opencl;

import static org.jocl.CL.*;

import org.jocl.CL;
import org.jocl.Pointer;
import org.jocl.Sizeof;
import org.jocl.cl_command_queue;
import org.jocl.cl_context;
import org.jocl.cl_context_properties;
import org.jocl.cl_device_id;
import org.jocl.cl_kernel;
import org.jocl.cl_mem;
import org.jocl.cl_platform_id;
import org.jocl.cl_program;

import hussey.matthew.opencl.Cell;
import hussey.matthew.opencl.Grid;
import hussey.matthew.opencl.HeightMap;
import hussey.matthew.opencl.Origin;
import hussey.matthew.opencl.VisibleCells;

/**
 * @author matt
 *
 */
public class OpenCl implements HeightMap {
	
	public OpenCl(final Grid heights) {
		this.heights = heights;
	}

    //
    // The source code of the OpenCL program to execute
    //
    /*private static String programSource =
        "__kernel void "+
        "sampleKernel(__global char *heights, " +
        "             __global int *originX, " +
        "             __global int *originY, " +
        "             __global int *originZ, " +
        "		      __global char *c)"+
        "{"+
        "    int gid = get_global_id(0);"+
        "    c[gid] = 1;"+
        "}";*/
    

    private static String programSource =
        "__kernel void "+
        "sampleKernel(__global int *heights, " +
        "             __global int *originXArr, " +
        "             __global int *originYArr, " +
        "             __global int *originZArr, " +
        "             __global int *targetHeight, " +
        "             __global int *arrayWidth, " +
        "		      __global int *c)"+
        "{"+
        "    int gid = get_global_id(0);" +
        "    int x0 = originXArr[0];" +
        "    int y0 = originYArr[0];" +
        "    int z0 = originZArr[0];" +
        "    int width = arrayWidth[0];" +
        "    int x1 = gid % width;" +
        "    int y1 = gid / width;" +
        "    int z1 = targetHeight[0];" +
        "    int xTotalOffset = abs(x1 - x0);" +
        "    int yTotalOffset = abs(y1 - y0);" +
        "    float squares = xTotalOffset * xTotalOffset + yTotalOffset * yTotalOffset;" +
        "    float totalDistance = sqrt(squares);" +
        "    float rise = z1 - z0;" +
        "    float gradient = rise / totalDistance;" +
        "    int originX = x0;" +
        "    int originY = y0;" +
        "    bool steep = abs(y1 - y0) > abs(x1 - x0);" +
        "    if(steep)" +
        "    {" +
        "        int temp = x0;" +
        "        int x0 = y0;" +
        "        int y0 = temp;" +
        "        temp = x1;" +
        "        x1 = y1;" +
        "        y1 = temp;" +
        "    }" +
        "    int deltax = abs(x1 - x0);" +
        "    int deltay = abs(y1 - y0);" +
        "    int error = deltax / 2;" +
        "    int y = y0;" +
        "    int inc = x0 < x1 ? 1 : -1;" +
        "    int ystep = y0 < y1 ? 1 : -1;" +
        "    for(int x = x0; x != x1; x += inc)" +
        "    {" +
        "        int checkx = steep ? y : x;" +
        "        int checky = steep ? x : y;" +
        "        int rowOffset = checky * width + checkx;" +
        "        int z = heights[rowOffset];" +
        "        int xoffset = abs(originX - checkx);" +
        "        int yoffset = abs(originY - checky);" +
        "        float distSq = xoffset * xoffset + yoffset * yoffset;" +
        "        float distance = sqrt(distSq);" +
        "        float toClear = distance * gradient + z0;" +
        "        if(toClear < z)" +
        "        {" +
        "            c[gid] = 0;" +
        "            return;" +
        "        }" +
        "        error -= deltay;" +
        "        if(error < 0)" +
        "        {" +
        "            y += ystep;" +
        "            error += deltax;" +
        "        }" +
        "    }" +
        "    c[gid] = 1;" +
        "}";
    
	@Override
	public void findCellsVisibleFrom(Origin origin, VisibleCells visibleCells, int height) {
        // Create input- and output data 
        int n = heights.height() * heights.width();
        int heightArray[] = new int[n];
        int originXArray[] = {origin.x()};
        int originYArray[] = {origin.y()};
        int originZArray[] = {origin.z()};
        int targetHeightArray[] = {height};
        int arrayWidth[] = {heights.width()};
        int dstArray[] = new int[n];
        for(int row = 0; row != heights.height(); ++row) {
        	int rowOffset = row * heights.width();
        	for(int column = 0; column != heights.width(); ++column) {
        		int offset = rowOffset + column;
        		int cellHeight = heights.at(column, row);
        		heightArray[offset] = cellHeight;
        	}
        }
        
        Pointer heightPtr = Pointer.to(heightArray);
        Pointer originXPtr = Pointer.to(originXArray);
        Pointer originYPtr = Pointer.to(originYArray);
        Pointer originZPtr = Pointer.to(originZArray);
        Pointer targetHeightPtr = Pointer.to(targetHeightArray);
        Pointer arrayWidthPtr = Pointer.to(arrayWidth);
        Pointer dst = Pointer.to(dstArray);

        // The platform, device type and device number
        // that will be used
        final int platformIndex = 0;
        final long deviceType = CL_DEVICE_TYPE_ALL;
        final int deviceIndex = 0;

        // Enable exceptions and subsequently omit error checks in this sample
        CL.setExceptionsEnabled(true);

        // Obtain the number of platforms
        int numPlatformsArray[] = new int[1];
        clGetPlatformIDs(0, null, numPlatformsArray);
        int numPlatforms = numPlatformsArray[0];

        // Obtain a platform ID
        cl_platform_id platforms[] = new cl_platform_id[numPlatforms];
        clGetPlatformIDs(platforms.length, platforms, null);
        cl_platform_id platform = platforms[platformIndex];

        // Initialize the context properties
        cl_context_properties contextProperties = new cl_context_properties();
        contextProperties.addProperty(CL_CONTEXT_PLATFORM, platform);
        
        // Obtain the number of devices for the platform
        int numDevicesArray[] = new int[1];
        clGetDeviceIDs(platform, deviceType, 0, null, numDevicesArray);
        int numDevices = numDevicesArray[0];
        
        // Obtain a device ID 
        cl_device_id devices[] = new cl_device_id[numDevices];
        clGetDeviceIDs(platform, deviceType, numDevices, devices, null);
        cl_device_id device = devices[deviceIndex];

        // Create a context for the selected device
        cl_context context = clCreateContext(
            contextProperties, 1, new cl_device_id[]{device}, 
            null, null, null);
        
        // Create a command-queue for the selected device
        cl_command_queue commandQueue = 
            clCreateCommandQueue(context, device, 0, null);

        // Allocate the memory objects for the input- and output data
        cl_mem memObjects[] = new cl_mem[7];
        memObjects[0] = clCreateBuffer(context, 
        		CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, 
                Sizeof.cl_int * n, heightPtr, null);
        memObjects[1] = clCreateBuffer(context, 
        		CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, 
                Sizeof.cl_int * 1, originXPtr, null);
        memObjects[2] = clCreateBuffer(context, 
        		CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, 
                Sizeof.cl_int * 1, originYPtr, null);
        memObjects[3] = clCreateBuffer(context, 
        		CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, 
                Sizeof.cl_int * 1, originZPtr, null);
        memObjects[4] = clCreateBuffer(context, 
        		CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, 
                Sizeof.cl_int * 1, targetHeightPtr, null);
        memObjects[5] = clCreateBuffer(context, 
        		CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, 
                Sizeof.cl_int * 1, arrayWidthPtr, null);
        memObjects[6] = clCreateBuffer(context, 
            CL_MEM_READ_WRITE, 
            Sizeof.cl_int * n, null, null);
        
        // Create the program from the source code
        cl_program program = clCreateProgramWithSource(context,
            1, new String[]{ programSource }, null, null);
        
        // Build the program
        clBuildProgram(program, 0, null, null, null, null);
        
        // Create the kernel
        cl_kernel kernel = clCreateKernel(program, "sampleKernel", null);
        
        // Set the arguments for the kernel
        int index = 0;
        for(cl_mem memObject: memObjects) {
            clSetKernelArg(kernel, index, Sizeof.cl_mem, Pointer.to(memObject));
            ++index;
        }
        
        // Set the work-item dimensions
        long global_work_size[] = new long[]{n};
        long local_work_size[] = new long[]{1};
        
        // Execute the kernel
        clEnqueueNDRangeKernel(commandQueue, kernel, 1, null,
            global_work_size, local_work_size, 0, null, null);
        
        // Read the output data
        clEnqueueReadBuffer(commandQueue, memObjects[6], CL_TRUE, 0,
            n * Sizeof.cl_int, dst, 0, null, null);
        
        // Release kernel, program, and memory objects
        for(cl_mem memObject: memObjects) {
            clReleaseMemObject(memObject);        	
        }
        clReleaseKernel(kernel);
        clReleaseProgram(program);
        clReleaseCommandQueue(commandQueue);
        clReleaseContext(context);
        
        // Verify the result        
        for(int row = 0; row != heights.height(); ++row) {
        	int rowOffset = row * heights.width();
        	final int frow = row;
        	for(int column = 0; column != heights.width(); ++column) {
        		final int fcolumn = column;
        		int cellIndex = rowOffset + column;
        		int result = dstArray[cellIndex];
        		boolean resultIsZero = result == 0;
        		boolean resultIsTrue = !resultIsZero;
        		if(resultIsTrue) {
        			visibleCells.addCell(new Cell() {
						@Override
						public int y() {
							return frow;
						}
						
						@Override
						public int x() {
							return fcolumn;
						}
					});
        		}
        	}
        }
        
        System.out.println("Done");
	}
	
	private final Grid heights;

}
