/**
 * 
 */
package hussey.matthew.opencl.singlethread.gui;

import hussey.matthew.opencl.Cell;
import hussey.matthew.opencl.Grid;
import hussey.matthew.opencl.HeightMap;
import hussey.matthew.opencl.Origin;
import hussey.matthew.opencl.VisibleCells;
import hussey.matthew.opencl.singlethread.SingleThreadHeightMap;

import java.awt.Color;
import java.awt.Graphics;
import java.util.HashSet;
import java.util.Set;

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
		
		final Grid heights = new Grid() {
			
			@Override
			public int width() {
				return 200;
			}
			
			@Override
			public int height() {
				return 200;
			}
		};
		final HeightMap heightMap = new SingleThreadHeightMap(heights);
		final Origin origin = new Origin() {

			@Override
			public int x() {
				return 100;
			}

			@Override
			public int y() {
				return 100;
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
				cells.add(cell);
			}
			
			final Set<Cell> cells = new HashSet<>();
		};
		heightMap.findCellsVisibleFrom(origin, visibleCells);
		
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
