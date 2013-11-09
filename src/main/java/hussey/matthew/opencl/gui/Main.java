/**
 * 
 */
package hussey.matthew.opencl.gui;

import hussey.matthew.opencl.BresnhamsLineOfSight;
import hussey.matthew.opencl.Cell;
import hussey.matthew.opencl.Grid;
import hussey.matthew.opencl.HeightMap;
import hussey.matthew.opencl.LineOfSight;
import hussey.matthew.opencl.ListGrid;
import hussey.matthew.opencl.Origin;
import hussey.matthew.opencl.VisibleCells;
import hussey.matthew.opencl.multithreaded.ByRowMultipleThreadHeightMap;
import hussey.matthew.opencl.multithreaded.MultipleThreadHeightMap;
import hussey.matthew.opencl.opencl.OpenCl;
import hussey.matthew.opencl.singlethread.SingleThreadHeightMap;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

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
		final int height = 400;
		final List<Integer> heightList = new ArrayList<>();
		final int limit = width * height;
		final int maxHeight = 100;
		final Random rand = new Random(42);
		for(int h = 0; h != limit; ++h)
		{
			heightList.add(rand.nextInt(maxHeight));
		}
		
		final Grid heights = new ListGrid(heightList, width);
		
		final LineOfSight lineOfSight = new BresnhamsLineOfSight(heights);
		
		final Origin origin = new Origin() {

			@Override
			public int x() {
				return width / 2 - 50;
			}

			@Override
			public int y() {
				return height / 2 - 100;
			}

			@Override
			public int z() {
				return heights.at(x(), y()) + 150;
			}
		};
		
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
			
			@Override
			public void clear() {
				lock.lock();
				try {
					cells.clear();
				} finally {
					lock.unlock();
				}
			}
			
			final Set<Cell> cells = new HashSet<>();
			final ReentrantLock lock = new ReentrantLock(true);
		};
		
		final JFrame frame = new JFrame();
		
		final JPanel drawPanel = new JPanel()
		{
			/**
			 * 
			 */
			private static final long serialVersionUID = 4818691693667630526L;

			public void paint(java.awt.Graphics g) {
				super.paint(g);
				visibleCells.displaySelf(g);
			}
		};
		final JComboBox<HeightMap> chooseProcess = new JComboBox<>();
		chooseProcess.addItem(new SingleThreadHeightMap(lineOfSight, width, height));
		chooseProcess.addItem(new MultipleThreadHeightMap(lineOfSight, width, height));
		chooseProcess.addItem(new ByRowMultipleThreadHeightMap(lineOfSight, width, height));
		chooseProcess.addItem(new OpenCl(heights));
		chooseProcess.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				final Object source = e.getSource();
				@SuppressWarnings("unchecked")
				final JComboBox<HeightMap> me = (JComboBox<HeightMap>)source;
				final HeightMap process = (HeightMap)me.getSelectedItem();
				visibleCells.clear();
				process.findCellsVisibleFrom(origin, visibleCells, 90);
				drawPanel.repaint();
			}
		});
		frame.add(chooseProcess, BorderLayout.NORTH);
		frame.add(drawPanel);
		
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JDialog.EXIT_ON_CLOSE);
	}

}
