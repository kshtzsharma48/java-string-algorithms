package algorithms.search;

import static org.junit.Assert.assertNotNull;
import org.junit.Test;

/**
 * Unit tests of the Boyer-Moore string searching algorithm.
 */
public class BoyerMooreTest {

	@Test
	public void canInstantiateAlgorithmClass() {
		assertNotNull(new BoyerMoore());
	}
}
