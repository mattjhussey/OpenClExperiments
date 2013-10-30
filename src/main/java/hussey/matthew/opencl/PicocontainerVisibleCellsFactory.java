/**
 * 
 */
package hussey.matthew.opencl;

import org.picocontainer.PicoContainer;

/**
 * @author matt
 *
 */
public class PicocontainerVisibleCellsFactory implements VisibleCellsFactory {
	
	public PicocontainerVisibleCellsFactory(final PicoContainer pico) {
		this.pico = pico;
	}

	@Override
	public VisibleCells newVisibleCells() {
		return (VisibleCells)pico.getComponentInstance(VisibleCells.class);
	}
	
	private final PicoContainer pico;

}
