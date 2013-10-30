/**
 * 
 */
package hussey.matthew.opencl.singlethread;

import hussey.matthew.opencl.Cell;
import hussey.matthew.opencl.Grid;
import hussey.matthew.opencl.HeightMap;
import hussey.matthew.opencl.Origin;
import hussey.matthew.opencl.VisibleCells;

/**
 * @author matt
 *
 */
public final class SingleThreadHeightMap implements HeightMap {
	
	public SingleThreadHeightMap(final Grid grid) {
		this.grid = grid;
	}

	@Override
	public void findCellsVisibleFrom(Origin origin, VisibleCells visibleCells) {
		
		for(int row = 0; row != grid.height(); ++row) {
			for(int column = 0; column != grid.width(); ++column) {
				// Work each line from origin to cell
				// If cell can be seen from origin, add to VisibleCells
				
				int x0 = origin.x();
				int y0 = origin.y();
				int x1 = column;
				int y1 = row;
				boolean steep = Math.abs(y1 - y0) > Math.abs(x1 - x0);
				if(steep) {
					int temp = x0;
					x0 = y0;
					y0 = temp;
					temp = x1;
					x1 = y1;
					y1 = temp;
				}
				
				int deltax = Math.abs(x1 - x0);
				int deltay = Math.abs(y1 - y0);
				int error = deltax / 2;
				int y = y0;
				
				int inc = x0 < x1 ? 1 : -1;
				int ystep = y0 < y1 ? 1 : -1;
				
				for(int x = x0; x != x1; x += inc) {
					final int plotx = x;
					final int ploty = y;
					if(steep)
					{
						// Check here
						visibleCells.addCell(new Cell() {							
							@Override
							public int y() {
								return plotx;
							}							
							@Override
							public int x() {
								return ploty;
							}
						});
					} else {
						// Check here
						visibleCells.addCell(new Cell() {							
							@Override
							public int y() {
								return ploty;
							}							
							@Override
							public int x() {
								return plotx;
							}
						});						
					}
					
					error -= deltay;
					
					if(error < 0) {
						y += ystep;
						error += deltax;
					}
				}
			}
		}
	}
	
	private final Grid grid;
}
