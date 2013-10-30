/**
 * 
 */
package hussey.matthew.opencl.singlethread.gui;

import java.util.HashSet;
import java.util.Set;

import hussey.matthew.opencl.HeightMap;
import hussey.matthew.opencl.PicocontainerVisibleCellsFactory;
import hussey.matthew.opencl.Origin;
import hussey.matthew.opencl.SetVisibleCells;
import hussey.matthew.opencl.VisibleCells;
import hussey.matthew.opencl.VisibleCellsFactory;
import hussey.matthew.opencl.singlethread.SingleThreadHeightMap;

import javax.swing.JDialog;
import javax.swing.JFrame;

import org.picocontainer.MutablePicoContainer;
import org.picocontainer.Parameter;
import org.picocontainer.PicoContainer;
import org.picocontainer.defaults.ConstantParameter;
import org.picocontainer.defaults.DefaultPicoContainer;

/**
 * @author matt
 *
 */
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DefaultPicoContainer pico = new DefaultPicoContainer();
		pico.registerComponentImplementation(HeightMap.class, SingleThreadHeightMap.class);
		pico.registerComponentImplementation(VisibleCellsFactory.class, PicocontainerVisibleCellsFactory.class, new Parameter[] {new ConstantParameter(pico)});
		pico.registerComponentImplementation(VisibleCells.class, SetVisibleCells.class);
		pico.registerComponentImplementation(Set.class, HashSet.class);
		
		final HeightMap heightMap = (HeightMap)pico.getComponentInstance(HeightMap.class);
		final Origin origin = new Origin() { };
		final VisibleCells visibleCells = heightMap.findCellsVisibleFrom(origin);
		
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
