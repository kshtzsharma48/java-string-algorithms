package algorithms.search;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests of the Boyer-Moore string searching algorithm.
 */
public class BoyerMooreTest {

	private StringSearch searchApi;

	@Before
	public void setup() {
		searchApi = new BoyerMoore();
	}

	@Test
	public void canInstantiateAlgorithmClass() {
		assertNotNull(searchApi);
	}

	@Test
	public void findNeedleInHaystackLiterally() {
		assertEquals(-1, searchApi.indexOf("haystack", "needle"));
	}

	@Test
	public void findFooInFoobar() {
		assertEquals(0, searchApi.indexOf("foobar", "foo"));
	}

	@Test
	public void findBarInFoobar() {
		assertEquals(3, searchApi.indexOf("foobar", "bar"));
	}

	@Test
	public void findFooInLargerFoobar() {
		final String haystack = "The quick brown fox jumped over the lazy dog. Foo. The quick brown fox jumped over the lazy dog";
		assertEquals(46, searchApi.indexOf(haystack, "Foo"));
		assertEquals("Foo", haystack.substring(46, 49));
	}
}
