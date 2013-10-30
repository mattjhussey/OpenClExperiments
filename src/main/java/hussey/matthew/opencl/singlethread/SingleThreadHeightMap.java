/**
 * 
 */
package hussey.matthew.opencl.singlethread;

import hussey.matthew.opencl.HeightMap;
import hussey.matthew.opencl.Origin;
import hussey.matthew.opencl.VisibleCells;

/**
 * @author matt
 *
 */
public final class SingleThreadHeightMap implements HeightMap {

	@Override
	public void findCellsVisibleFrom(Origin origin, VisibleCells visibleCells) {
		
		// Loop through each cell within the heightmap
		// Work each line from origin to cell
		// If cell can be seen from origin, add to VisibleCells
	}
}
