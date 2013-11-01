/**
 * 
 */
package hussey.matthew.opencl;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author matt
 *
 */
public class BresnhamsLineOfSightTest {
	public JUnitRuleMockery context = new JUnitRuleMockery();
	private final Grid grid = context.mock(Grid.class);
	private final LineOfSight testItem = new BresnhamsLineOfSight(grid);

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link hussey.matthew.opencl.BresnhamsLineOfSight#canSee(int, int, int, int, int, int)}.
	 */
	@Test
	public final void testCanSee() {
	}

}
