package algorithms.search;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import org.junit.Assert;
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
	public void findBanana() {
		assertEquals(5, searchApi.indexOf("banana", "bananbanana"));
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

	@Test
	public void computePrefixTest() {
		assertEquals(Arrays.toString(new int[]{0, 0, 0, 0}), Arrays.toString(BoyerMoore.computePrefix("help")));
		assertEquals(Arrays.toString(new int[]{0, 0, 1, 2}), Arrays.toString(BoyerMoore.computePrefix("baba")));
		assertEquals(Arrays.toString(new int[]{0, 0, 0, 1, 2, 0, 1, 2}), Arrays.toString(BoyerMoore.computePrefix("ANPANMAN")));
		assertEquals(Arrays.toString(new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}),
				  	    Arrays.toString(BoyerMoore.computePrefix("Internet Wiretap")));
	}

	@Test
	public void goodSuffixHeuristicTest() {
		assertEquals(Arrays.toString(new int[]{1, 8, 3, 6, 6, 6, 6, 6}), // 6, 6, 6, 6, 6, 6, 3, 3, 1
				  		 Arrays.toString(BoyerMoore.prepareGoodSuffixHeuristic("ANPANMAN")));
	}

	@Test
	public void findPassageInBeowulf() throws IOException {
		File beowulf = new File(getClass().getClassLoader().getResource("anonymous-beowulf-543.txt").getFile());
		String beowulfText = Files.toString(beowulf, Charsets.ISO_8859_1);
		System.out.println(beowulfText);
		System.out.println(beowulfText.length());
//		final StringFinder stringFinder = searchApi.compileFinder("Beowulf finally accepted Hygd's offer of kingdom and hoard");
//		final StringFinder stringFinder = searchApi.compileFinder("BEOWULF");
		final StringFinder internetWiretapFinder = searchApi.compileFinder("Internet Wiretap");
		assertEquals(4, internetWiretapFinder.indexIn(beowulfText));
	}
}
