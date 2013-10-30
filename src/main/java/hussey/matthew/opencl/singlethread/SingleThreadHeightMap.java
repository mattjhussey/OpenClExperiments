/**
 * 
 */
package hussey.matthew.opencl.singlethread;

import hussey.matthew.opencl.HeightMap;
import hussey.matthew.opencl.Origin;
import hussey.matthew.opencl.VisibleCells;
import hussey.matthew.opencl.VisibleCellsFactory;

/**
 * @author matt
 *
 */
public final class SingleThreadHeightMap implements HeightMap {
	
	public SingleThreadHeightMap(final VisibleCellsFactory visibleCellsFactory) {
		this.visibleCellsFactory = visibleCellsFactory;
	}

	@Override
	public VisibleCells findCellsVisibleFrom(Origin origin) {
		final VisibleCells results = visibleCellsFactory.newVisibleCells();
		
		// Loop through each cell within the heightmap
		// Work each line from origin to cell
		// If cell can be seen from origin, add to VisibleCells
		
		return results;
	}
	
	private final VisibleCellsFactory visibleCellsFactory;
}
