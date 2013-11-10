/**
 * 
 */
package hussey.matthew.opencl;

/**
 * @author matt
 *
 */
public interface HeightMap {
	void findCellsVisibleFrom(int originX, int originY, int originZ, VisibleCells visibleCells, int height);
}
