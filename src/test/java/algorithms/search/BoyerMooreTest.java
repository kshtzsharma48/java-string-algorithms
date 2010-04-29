package algorithms.search;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests of the Boyer-Moore string searching algorithm.
 */
public class BoyerMooreTest {

	private StringSearch searchApi;

	@Before
	public void setup() {
		searchApi = Searchers.newBoyerMooreStringSearcher();
	}

	@Test
	public void findNeedleInHaystackLiterally() {
		assertEquals(-1, searchApi.indexOf("needle", "haystack"));
	}

	@Test
	public void findFooInFoobar() {
		assertEquals(0, searchApi.indexOf("foo", "foobar"));
	}

	@Test
	public void findBarInFoobar() {
		assertEquals(3, searchApi.indexOf("bar", "foobar"));
	}

	@Test
	public void findFooInLargerFoobar() {
		final String haystack = "The quick brown fox jumped over the lazy dog. Foo. The quick brown fox jumped over the lazy dog";
		assertEquals(46, searchApi.indexOf("Foo", haystack));
		assertEquals("Foo", haystack.substring(46, 49));
		assertEquals(46, searchApi.compileFinder("Foo").indexIn(haystack));
	}
}
