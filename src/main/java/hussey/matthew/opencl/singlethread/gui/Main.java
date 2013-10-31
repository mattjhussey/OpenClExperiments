/**
 * 
 */
package hussey.matthew.opencl.singlethread.gui;

import hussey.matthew.opencl.Cell;
import hussey.matthew.opencl.Grid;
import hussey.matthew.opencl.HeightMap;
import hussey.matthew.opencl.LineOfSight;
import hussey.matthew.opencl.Origin;
import hussey.matthew.opencl.VisibleCells;
import hussey.matthew.opencl.multithreaded.MultipleThreadHeightMap;
import hussey.matthew.opencl.singlethread.SingleThreadHeightMap;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.JDialog;
import javax.swing.JFrame;

/**
 * @author matt
 *
 */
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		final int width = 500;
		final int height = 500;
		final List<Integer> heightList = new ArrayList<>();
		final int limit = width * height;
		final int maxHeight = 100;
		final Random rand = new Random(42);
		for(int h = 0; h != limit; ++h)
		{
			heightList.add(rand.nextInt(maxHeight));
		}
		
		final Grid heights = new Grid() {
			
			@Override
			public int width() {
				return width;
			}
			
			@Override
			public int height() {
				return height;
			}

			@Override
			public int at(int x, int y) {
				int index = x * width + y;
				return heightList.get(index);
			}
		};
		final LineOfSight lineOfSight = new LineOfSight() {
			
			@Override			
			public boolean canSee(int x0, int y0, int z0, int x1, int y1, int z1)
			{
				int xtotaloffset = Math.abs(x1 - x0);
				int ytotaloffset = Math.abs(y1 - y0);
				double totalDistance = Math.sqrt(xtotaloffset * xtotaloffset + ytotaloffset * ytotaloffset);
				double rise = z1 - z0;
				double gradient = rise / totalDistance;
				
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
					int xoffset = Math.abs(x0 - checkx);
					int yoffset = Math.abs(y0 - checky);
					double distance = Math.sqrt(xoffset * xoffset + yoffset * yoffset);
					double toClear = distance * gradient + z0;
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
		};
		//final HeightMap heightMap = new SingleThreadHeightMap(lineOfSight);
		final HeightMap heightMap = new MultipleThreadHeightMap(lineOfSight);
		final Origin origin = new Origin() {

			@Override
			public int x() {
				return 250;
			}

			@Override
			public int y() {
				return 250;
			}

			@Override
			public int z() {
				return heights.at(x(), y()) + 50;
			} };
		final VisibleCells visibleCells = new VisibleCells() {
			
			@Override
			public void displaySelf(Graphics graphics) {
				graphics.setColor(Color.RED);
				for(Cell cell: cells) {
					graphics.drawRect(cell.x(), cell.y(), 1, 1);
				}
			}

			@Override
			public void addCell(Cell cell) {
				lock.lock();
				try {
					cells.add(cell);
				} finally {
					lock.unlock();
				}
			}
			
			final Set<Cell> cells = new HashSet<>();
			final ReentrantLock lock = new ReentrantLock(true);
		};
		heightMap.findCellsVisibleFrom(origin, visibleCells, 90);
		
		JFrame frame = new JFrame() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 4818691693667630526L;

			public void paint(java.awt.Graphics g) {
				super.paint(g);
				visibleCells.displaySelf(g);
			}
		};
		
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JDialog.EXIT_ON_CLOSE);
	}

}
