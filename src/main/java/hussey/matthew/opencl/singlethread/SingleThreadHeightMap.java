/**
 * 
 */
package hussey.matthew.opencl.singlethread;

import hussey.matthew.opencl.Cell;
import hussey.matthew.opencl.HeightMap;
import hussey.matthew.opencl.LineOfSight;
import hussey.matthew.opencl.Origin;
import hussey.matthew.opencl.VisibleCells;

/**
 * @author matt
 *
 */
public final class SingleThreadHeightMap implements HeightMap {
	
	public SingleThreadHeightMap(final LineOfSight lineOfSight) {
		this.lineOfSight = lineOfSight;
	}

	@Override
	public void findCellsVisibleFrom(Origin origin, VisibleCells visibleCells, int height) {

		int x0 = origin.x();
		int y0 = origin.y();
		int z0 = origin.z();
		
		for(int row = 0; row != 500; ++row) {
			for(int column = 0; column != 500; ++column) {
				// Work each line from origin to cell
				// If cell can be seen from origin, add to VisibleCells
				
				int x1 = column;
				int y1 = row;
				int z1 = height;
				
				boolean canSee = lineOfSight.canSee(x0, y0, z0, x1, y1, z1);
				
				if(canSee)
				{
					final int x = column;
					final int y = row;
					// Check here
					visibleCells.addCell(new Cell() {							
						@Override
						public int y() {
							return y;
						}							
						@Override
						public int x() {
							return x;
						}
					});					
				}
			}
		}
	}
	
	private final LineOfSight lineOfSight;
}
