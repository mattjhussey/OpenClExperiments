/**
 * 
 */
package hussey.matthew.opencl;

import java.awt.Graphics;

/**
 * @author matt
 *
 */
public interface VisibleCells {
	void addCell(Cell cell);
	void clear();
	void displaySelf(Graphics graphics);
}
