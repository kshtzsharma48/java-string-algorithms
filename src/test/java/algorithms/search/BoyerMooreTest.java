package algorithms.search;

import algorithms.TestHelp;
import static algorithms.TestHelp.logTiming;
import static algorithms.TestHelp.timeMultipleCalls;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Callable;

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
	public void findPassageInBeowulf() throws IOException {
		File beowulf = new File(getClass().getClassLoader().getResource("anonymous-beowulf-543.txt").getFile());
		String beowulfText = Files.toString(beowulf, Charsets.ISO_8859_1);
		final StringFinder beowulfFinder = searchApi.compileFinder("Beowulf");
        assertEquals(84, beowulfFinder.countOccurencesIn(beowulfText));
        final StringFinder internetWiretapFinder = searchApi.compileFinder("Internet Wiretap");
        assertEquals(4, internetWiretapFinder.indexIn(beowulfText));
        assertEquals(2, internetWiretapFinder.countOccurencesIn(beowulfText));
        final StringFinder stringFinder = searchApi.compileFinder("Beowulf finally accepted Hygd's offer of kingdom and hoard");
        assertEquals(146983, stringFinder.indexIn(beowulfText));
    }

    @Test
    public void benchmark() throws Exception {
        File beowulf = new File(getClass().getClassLoader().getResource("anonymous-beowulf-543.txt").getFile());
		final String beowulfText = Files.toString(beowulf, Charsets.ISO_8859_1);
        final String search = "Beowulf finally accepted Hygd's offer of kingdom and hoard";
        final StringFinder stringFinder = searchApi.compileFinder(search);
        TestHelp.Callback<Integer> callback = new TestHelp.Callback<java.lang.Integer>() {
            int garbage;
            @Override
            public void consume(Integer value) {
                garbage |= value;
            }
        };
        Callable<Integer> stringIndexOf = new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return beowulfText.indexOf(search);
            }
        };
        Callable<Integer> boyerMoore = new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return stringFinder.indexIn(beowulfText);
            }
        };
        logTiming("String.indexOf", timeMultipleCalls(stringIndexOf, 10000, callback));
        logTiming("Boyer-Moore   ", timeMultipleCalls(boyerMoore, 10000, callback));
        logTiming("String.indexOf", timeMultipleCalls(stringIndexOf, 10000, callback));
        logTiming("Boyre-Moore   ", timeMultipleCalls(boyerMoore, 10000, callback));
    }
}
