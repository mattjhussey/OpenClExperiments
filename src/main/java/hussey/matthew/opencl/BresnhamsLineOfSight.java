/**
 * 
 */
package hussey.matthew.opencl;

/**
 * @author matt
 *
 */
public class BresnhamsLineOfSight implements LineOfSight {
	
	public BresnhamsLineOfSight(final Grid heights) {
		this.heights = heights;
	}
	
	@Override			
	public boolean canSee(int x0, int y0, int z0, int x1, int y1, int z1)
	{
		int xtotaloffset = Math.abs(x1 - x0);
		int ytotaloffset = Math.abs(y1 - y0);
		float totalDistance = (float)Math.sqrt(xtotaloffset * xtotaloffset + ytotaloffset * ytotaloffset);
		float rise = z1 - z0;
		float gradient = rise / totalDistance;
		
		int originx = x0;
		int originy = y0;
		
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
			
			int checkx = steep ? y : x;
			int checky = steep ? x : y;
			
			int z = heights.at(checkx, checky);
			int xoffset = Math.abs(originx - checkx);
			int yoffset = Math.abs(originy - checky);
			float distance = (float)Math.sqrt(xoffset * xoffset + yoffset * yoffset);
			float toClear = distance * gradient + z0;
			if(toClear < z)
			{
				return false;
			}
			
			error -= deltay;
			
			if(error < 0) {
				y += ystep;
				error += deltax;
			}
		}
		
		return true;
	}
	
	final Grid heights;

}
