/**
 * 
 */
package hussey.matthew.opencl;

import java.awt.Color;
import java.awt.Graphics;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author matt
 *
 */
public final class VisibleCells {
	
	public VisibleCells(final Color colour) {
		this.colour = colour;
	}

	public void displaySelf(Graphics graphics) {
		graphics.setColor(colour);
		for(Cell cell: cells) {
			graphics.drawRect(cell.x(), cell.y(), 1, 1);
		}
	}

	public void addCell(final int x, final int y) {
		lock.lock();
		try {
			cells.add(new Cell(x, y));
		} finally {
			lock.unlock();
		}
	}
	
	public void clear() {
		lock.lock();
		try {
			cells.clear();
		} finally {
			lock.unlock();
		}
	}
	
	final Color colour;
	final Set<Cell> cells = new HashSet<>();
	final ReentrantLock lock = new ReentrantLock(true);
}
