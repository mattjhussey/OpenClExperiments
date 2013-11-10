/**
 * 
 */
package hussey.matthew.opencl.multithreaded;

import hussey.matthew.opencl.HeightMap;
import hussey.matthew.opencl.LineOfSight;
import hussey.matthew.opencl.VisibleCells;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author matt
 *
 */
public final class MultipleThreadHeightMap implements HeightMap {
	
	public MultipleThreadHeightMap(final LineOfSight lineOfSight, final int width, final int height) {
		this.lineOfSight = lineOfSight;
		this.width = width;
		this.height = height;
	}

	@Override
	public void findCellsVisibleFrom(int originX, int originY, int originZ, final VisibleCells visibleCells, int height) {

		final int x0 = originX;
		final int y0 = originY;
		final int z0 = originZ;
		
		final ExecutorService executor = Executors.newFixedThreadPool(4);
		
		for(int row = 0; row != this.height; ++row) {
			for(int column = 0; column != this.width; ++column) {
				// Work each line from origin to cell
				// If cell can be seen from origin, add to VisibleCells
				
				final int x1 = column;
				final int y1 = row;
				final int z1 = height;
				
				Runnable process = new Runnable() {

					@Override
					public void run() {
						
						boolean canSee = lineOfSight.canSee(x0, y0, z0, x1, y1, z1);
						
						if(canSee)
						{
							// Check here
							visibleCells.addCell(x1, y1);					
						}
					}
				};
				
				executor.execute(process);
			}
		}
		
		executor.shutdown();
		try {
			executor.awaitTermination(10000, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			executor.shutdownNow();
			e.printStackTrace();
		}
	}
	
	private final LineOfSight lineOfSight;
	private final int width;
	private final int height;
}
