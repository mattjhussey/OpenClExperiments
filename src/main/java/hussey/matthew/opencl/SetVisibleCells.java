/**
 * 
 */
package hussey.matthew.opencl;

import java.awt.Graphics;
import java.util.Set;

/**
 * @author matt
 *
 */
public final class SetVisibleCells implements VisibleCells {
	
	public SetVisibleCells(final Set<Cell> cells) {
		this.cells = cells;
	}

	@Override
	public void addVisibleCell(Cell c) {
		cells.add(c);
	}

	@Override
	public void displaySelf(Graphics graphics) {
		for(Cell cell: cells) {
			cell.displaySelf(graphics);
		}
	}
	
	private final Set<Cell> cells;

}
