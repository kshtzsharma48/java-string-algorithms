package algorithms.search;

import com.google.common.base.Preconditions;
import com.google.common.collect.AbstractIterator;

import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

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

    private int longestPalindromeCenteredOnIndexInString(Integer index, CharSequence string, int minLength) {
        if ((minLength / 2) > index) {
            return -1;
        }
        if ((minLength / 2) > (string.length() - index)) {
            return -1;
        }

        SortedSet<Integer> lengths = new TreeSet<Integer>();

        boolean oddHit = true;
        boolean evenHit = true;

        for (int i = 1; isWithinBounds(i, index, string); i++) {

            // even length
            if (evenHit && string.charAt(index - i) != string.charAt(index + i - 1)) {
                evenHit = false;
                int length = (i - 1) * 2;
                if (length >= minLength) {
                    lengths.add(length);
                }
            }
            // odd length
            if (oddHit && string.charAt(index - i) != string.charAt(index + i)) {
                oddHit = false;
                int length = ((i - 1) * 2) + 1;
                if (length >= minLength) {
                    lengths.add(length);
                }
            }

            if (!(evenHit || oddHit)) {
                if (lengths.isEmpty()) {
                    return -1;
                } else {
                    return lengths.last();
                }
            }
        }

        int i = Math.min(index, string.length() - index - 1);
        i = (i * 2) + (i % 2);
        return i;
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
