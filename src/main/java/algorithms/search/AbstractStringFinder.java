package algorithms.search;

import com.google.common.collect.AbstractIterator;
import com.google.common.collect.Iterators;

import java.util.Iterator;

import static java.lang.Math.max;

/**
 * Base class for a string finder.
 */
public abstract class AbstractStringFinder implements StringFinder {

    @Override
    public int indexIn(CharSequence string) {
        return indexIn(string, 0);
    }

    @Override
    public boolean isPresentIn(CharSequence string) {
        return indexIn(string) != -1;
    }

    @Override
    public int countOccurencesIn(CharSequence string) {
        return Iterators.size(matches(string, 0));
    }

    @Override
    public Iterator<Integer> matches(final CharSequence text, final int startIndex) {
        return new AbstractIterator<Integer>() {

            private int i = max(0, startIndex);

            @Override
            protected Integer computeNext() {
                int result = AbstractStringFinder.this.indexIn(text, i);

                if (result == -1) {
                    return endOfData();
                } else {
                    i = result + 1;
                    return result;
                }
            }
        };
    }
}
