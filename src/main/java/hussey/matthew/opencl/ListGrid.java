/**
 * 
 */
package hussey.matthew.opencl;

import java.util.List;

/**
 * @author matt
 *
 */
public class ListGrid implements Grid {
	
	public ListGrid(final List<Integer> values, final int width) {
		this.values = values;
		this.width = width;
	}
	
	@Override
	public int width() {
		return width;
	}
	
	@Override
	public int height() {
		return values.size() / width;
	}

	@Override
	public int at(int x, int y) {
		int index = y * width + x;
		return values.get(index);
	}
	
	final List<Integer> values;
	final int width;

}
