/**
 * 
 */
package hussey.matthew.opencl;

/**
 * @author matt
 *
 */
public final class Cell {
	
	public Cell(final int x, final int y) {
		this.x = x;
		this.y = y;
	}
	
	public int x() {
		return x;
	}
	
	public int y() {
		return y;
	}
	
	private int x;
	private int y;
}
