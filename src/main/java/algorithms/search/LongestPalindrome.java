package algorithms.search;

import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.common.collect.AbstractIterator;
import com.google.common.collect.Lists;

import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;

import static com.google.common.base.Preconditions.checkNotNull;


public class LongestPalindrome {

    public CharSequence findLongestPalindrome(final CharSequence string) {
        checkNotNull(string);

        int l = 0; // longest one so far
        int i = -1; // index of longest one
        for (Integer index : indices(string)) {
            if ((l / 2) > Math.min(string.length() - index, index)) {
                break;
            }
            int length = longestPalindromeCenteredOnIndexInString(index, string, l + 1);
            if (length > l) {
                i = index;
                l = length;
            }
        }
        final int length = l;
        final int index = i;
        return new CharSequence() {
            @Override
            public int length() {
                return length;
            }

            @Override
            public char charAt(int i) {
                int start = index - (length / 2);
                i += start;
                return string.charAt(i);
            }

            @Override
            public CharSequence subSequence(int start, int end) {
                throw new UnsupportedOperationException();
            }

            @Override
            public String toString() {
                StringBuilder builder = new StringBuilder();
                for (int i = 0; i < length(); i++) {
                    builder.append(charAt(i));
                }
                return builder.toString();
            }
        };
    }

    private int longestPalindromeCenteredOnIndexInString(final Integer index, final CharSequence string, int minLength) {
        if ((minLength / 2) > index) {
            return -1;
        }
        if ((minLength / 2) > (string.length() - index)) {
            return -1;
        }

        final AtomicInteger l = new AtomicInteger();
        SortedSet<Integer> lengths = new TreeSet<Integer>();
        Supplier<Integer> evenSupplier = new Supplier<Integer>() {
            boolean enabled = true;
            @Override
            public Integer get() {
                enabled = enabled && (string.charAt(index - l.get()) == string.charAt(index + l.get() - 1));
                return enabled
                        ? 2 * l.get()
                        : -1;
            }
        };
        Supplier<Integer> oddSupplier = new Supplier<Integer>() {
            boolean enabled = true;
            @Override
            public Integer get() {
                enabled = enabled && (string.charAt(index - l.get()) == string.charAt(index + l.get()));
                return enabled
                        ? 2 * l.get() + 1
                        : -1;
            }
        };

        for (Integer length : lengthsToTry(index, string)) {
            l.set(length);
            int e = evenSupplier.get();
            int o = oddSupplier.get();
            if (e == o && e == -1) {
                break;
            }
            lengths.add(e);
            lengths.add(o);
        }

        return lengths.isEmpty() ? -1 : lengths.last();
    }

    private Iterable<Integer> lengthsToTry(final int index, final CharSequence string) {
        return new Iterable<Integer>() {
            @Override
            public Iterator<Integer> iterator() {
                return new AbstractIterator<Integer>() {

                    private int i = 1;

                    @Override
                    protected Integer computeNext() {
                        if (wouldExtendPastBeginning(i)) {
                            return endOfData();
                        } else if (wouldExtendPastEnd(i)) {
                            return endOfData();
                        } else {
                            return i++;
                        }
                    }

                    private boolean wouldExtendPastEnd(int i) {
                        if (i % 2 == 0) {
                            // even
                            return index - 1 + i >= string.length();
                        } else {
                            // odd
                            return index + i >= string.length();
                        }
                    }

                    private boolean wouldExtendPastBeginning(int i) {
                        return index - i < 0;
                    }
                };
            }
        };
    }

    private boolean isWithinBounds(int distanceFromOddCenter, Integer index, CharSequence string) {
        boolean result = (index - distanceFromOddCenter) >= 0
                && (index + distanceFromOddCenter) < string.length();
        if (result) {
            return true;
        } else {
            return false;
        }
    }

    Iterable<Integer> indices(final CharSequence string) {
        return new Iterable<Integer>() {
            @Override
            public Iterator<Integer> iterator() {
                return new AbstractIterator<Integer>() {

                    int l = string.length(); // 5
                    int n = l / 2; // 2
                    int i = 0;
                    boolean left = ((l % 2) == 0); // false

                    @Override
                    protected Integer computeNext() {
                        if (i == l) {
                            return endOfData();
                        }

                        left = !left; // i=0, left=true, 2
                        return n + (((i++ + 1) / 2) * (left ? -1 : 1));
                    }
                };
            }
        };
    }
}
