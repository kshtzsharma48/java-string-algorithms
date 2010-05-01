package algorithms.search;

import algorithms.TestHelp;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import static algorithms.TestHelp.timeAndLogMultipleCalls;
import static algorithms.TestHelp.timeMultipleCalls;

/**
 * Unit tests of the Boyer-Moore string searching algorithm.
 */
public class BoyerMooreTest extends StringSearchTestBase {

    @Override
    protected StringSearch createNewSearchApi() {
        return Searchers.newBoyerMooreStringSearcher();
    }

    @Test @Ignore
    public void benchmark() throws Exception {
        File beowulf = new File(getClass().getClassLoader().getResource("anonymous-beowulf-543.txt").getFile());
		final String beowulfText = Files.toString(beowulf, Charsets.UTF_8);
        final String search = "Beowulf finally accepted Hygd's offer of kingdom and hoard";
        final StringFinder stringFinder = searchApi().compileFinder(search);
        TestHelp.Callback<Integer> callback = new TestHelp.Callback<java.lang.Integer>() {
            int garbage;
            @Override
            public void consume(Integer value) {
                garbage |= value;
            }
        };
        Callable<Integer> stringIndexOf = new Callable<Integer>() {
            @SuppressWarnings({"RedundantStringConstructorCall"})
            @Override
            public Integer call() throws Exception {
                return new String(beowulfText).indexOf(search);
            }
        };
        Callable<Integer> boyerMoore = new Callable<Integer>() {
            @SuppressWarnings({"RedundantStringConstructorCall"})
            @Override
            public Integer call() throws Exception {
                return stringFinder.indexIn(new String(beowulfText));
            }
        };
        timeMultipleCalls(stringIndexOf, 100, callback, TimeUnit.DAYS);
        timeMultipleCalls(boyerMoore, 100, callback, TimeUnit.DAYS);
        System.gc();
        Thread.sleep(500L);
        System.gc();
        for (int i = 0; i < 10; i++) {
            timeAndLogMultipleCalls("String.indexOf", stringIndexOf, 10000, callback, TimeUnit.MILLISECONDS);
            timeAndLogMultipleCalls("  Boyter-Moore", boyerMoore, 10000, callback, TimeUnit.MILLISECONDS);
        }
    }
}
