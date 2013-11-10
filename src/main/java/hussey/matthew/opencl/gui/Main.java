/**
 * 
 */
package hussey.matthew.opencl.gui;

import hussey.matthew.opencl.BresnhamsLineOfSight;
import hussey.matthew.opencl.HeightMap;
import hussey.matthew.opencl.LineOfSight;
import hussey.matthew.opencl.VisibleCells;
import hussey.matthew.opencl.multithreaded.ByRowMultipleThreadHeightMap;
import hussey.matthew.opencl.multithreaded.MultipleThreadHeightMap;
import hussey.matthew.opencl.opencl.OpenCl;
import hussey.matthew.opencl.singlethread.SingleThreadHeightMap;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * @author matt
 *
 */
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		final int width = 2000;
		final int height = 1600;
		final int originX = (width * 3)/ 4;
		final int originY = (height * 2) / 3;
		
		final int limit = width * height;
		final int[] heightArray = new int[limit];
		final int maxHeight = 100;
		final Random rand = new Random(42);
		for(int h = 0; h != limit; ++h)
		{	
			heightArray[h] = rand.nextInt(maxHeight);
		}
		
		final int originZ = heightArray[originY * width + originX] + 150;
		
		final LineOfSight lineOfSight = new BresnhamsLineOfSight(heightArray, width);
		
		final VisibleCells visibleCells = new VisibleCells();
		
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
		drawPanel.setPreferredSize(new Dimension(width, height));
		final JScrollPane scrollPane = new JScrollPane(drawPanel);
		scrollPane.setPreferredSize(new Dimension(800, 800));
		
		final JPanel upperPanel = new JPanel(new BorderLayout());
		final JLabel notes = new JLabel("SPACE");
		upperPanel.add(notes, BorderLayout.NORTH);
		final JComboBox<HeightMap> chooseProcess = new JComboBox<>();
		chooseProcess.addItem(new SingleThreadHeightMap(lineOfSight, width, height));
		chooseProcess.addItem(new MultipleThreadHeightMap(lineOfSight, width, height));
		chooseProcess.addItem(new ByRowMultipleThreadHeightMap(lineOfSight, width, height));
		chooseProcess.addItem(new OpenCl(heightArray, width));
		chooseProcess.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				final Object source = e.getSource();
				@SuppressWarnings("unchecked")
				final JComboBox<HeightMap> me = (JComboBox<HeightMap>)source;
				final HeightMap process = (HeightMap)me.getSelectedItem();
				visibleCells.clear();
				long before = System.currentTimeMillis();
				process.findCellsVisibleFrom(originX, originY, originZ, visibleCells, 90);
				long after = System.currentTimeMillis();
				long delta = after - before;
				notes.setText(String.format("%d ms", delta));
				drawPanel.repaint();
			}
		});
		upperPanel.add(chooseProcess);
		frame.add(upperPanel, BorderLayout.NORTH);
		frame.add(scrollPane);
		
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JDialog.EXIT_ON_CLOSE);
	}

}
