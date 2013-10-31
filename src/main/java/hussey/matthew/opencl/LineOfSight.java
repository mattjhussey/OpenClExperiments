/**
 * 
 */
package hussey.matthew.opencl;

/**
 * @author matt
 *
 */
public interface LineOfSight {
	boolean canSee(int x0, int y0, int z0, int x1, int y1, int z1);
}
