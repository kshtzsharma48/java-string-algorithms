package algorithms;

import algorithms.search.Searchers;
import algorithms.search.StringFinder;
import algorithms.search.StringSearch;
import algorithms.search.UkkonenSuffixTree;
import com.google.common.base.CharMatcher;
import com.google.common.base.Charsets;
import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.io.Files;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;


public class DemoTest {

    @Test
    public void useSearcher() {
        StringSearch search = Searchers.newKnuthMorrisPrattStringSearcher();
        assertEquals(-1, search.indexOf("chocolate", "peanut butter"));
        assertEquals(36, search.indexOf("lazy dog", "the quick brown fox jumped over the lazy dog."));
    }

    @Test @Ignore
    public void ukkonnenSuffixTreeSearchTest() throws IOException {
        File beowulf = new File(getClass().getClassLoader().getResource("anonymous-beowulf-543.txt").getFile());
        String beowulfText = Files.toString(beowulf, Charsets.ISO_8859_1);
        long before = System.currentTimeMillis();
        UkkonenSuffixTree tree = UkkonenSuffixTree.forString(beowulfText);
        System.out.format("Build suffix tree in %dms%n", System.currentTimeMillis() - before);
        for (int i = 0; i < 100; i++) {
            before = System.currentTimeMillis();
            int steveIndex = tree.indexOf("steve");
            int kihaIndex = tree.indexOf("kiha");
            int nygIndex = tree.indexOf("new york giants");
            int beowulfIndex = tree.indexOf("beowulf");
            System.out.format("s = %d, k = %d, n = %d, b = %d in %dms%n", steveIndex, kihaIndex, nygIndex, beowulfIndex, System.currentTimeMillis() - before);
        }
    }

    @Test @Ignore
    public void bogusMicrobenchmarkThatDoesNotTellYouAnythingButIsFunToRunNonetheless() {
        List<StringSearch> searchers = ImmutableList.of(
                Searchers.newBoyerMooreStringSearcher(),
                Searchers.newKnuthMorrisPrattStringSearcher(),
                // Searchers.newUkkonenSuffixTreeStringSearcher(),
                Searchers.newNaiveSearcher()
        );

        StringBuilder text = new StringBuilder();
        for (int i = 0; i < 100; i++) {
            text.append(toString());
            text.append("chocolate");
            text.append("peanut butter");
        }

        StringBuilder sought = new StringBuilder(toString());
        sought.append(toString());
        sought.append("the trick about these algorithms is they mostly perform well");
        sought.append(" when the search text is long so this search text is going to be pretty verbose. THAT IS ALL!");
        final String soughtTrimmed = CharMatcher.WHITESPACE.removeFrom(sought);

        text.append(soughtTrimmed);

        List<StringFinder> finders = Lists.transform(searchers, new Function<StringSearch, StringFinder>() {
            @Override
            public StringFinder apply(StringSearch from) {
                return from.compileFinder(soughtTrimmed);
            }
        });

        int r = 0;
        for (int i = 0; i < 3; i++) {
            for (StringFinder finder : finders) {
                try { TimeUnit.MILLISECONDS.sleep(50L); } catch (InterruptedException e) { }
                System.gc();

                long before = System.currentTimeMillis();
                for (int j = 0; j < 2000; j++) {
                    r += finder.indexIn(new StringBuilder(text));
                }
                long diff = System.currentTimeMillis() - before;
                System.out.format("%1$s took %2$,dms%n", finder.getClass().getSimpleName(), diff);
            }
        }
        if (r == 42) {
            System.out.println("Whoa");
        }
    }
}
