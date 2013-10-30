/**
 * 
 */
package hussey.matthew.opencl.singlethread.gui;

import hussey.matthew.opencl.HeightMap;
import hussey.matthew.opencl.Origin;
import hussey.matthew.opencl.VisibleCells;
import hussey.matthew.opencl.singlethread.SingleThreadHeightMap;

import java.awt.Graphics;

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
		
		final HeightMap heightMap = new SingleThreadHeightMap();
		final Origin origin = new Origin() { };
		final VisibleCells visibleCells = new VisibleCells() {
			
			@Override
			public void displaySelf(Graphics graphics) {
				
			}
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
