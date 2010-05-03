package algorithms.search;

import com.google.common.annotations.VisibleForTesting;
import static com.google.common.base.Preconditions.checkNotNull;
import com.google.common.collect.AbstractIterator;

import java.util.Iterator;


class LongestPalindrome {

    public CharSequence findLongestPalindrome(final CharSequence string) {
        checkNotNull(string);

        int longest = 0; // longest one so far
        int indexOfLongest = -1; // index of longest one
        for (Integer index : indices(string)) {
            if (longest / 2 > Math.min(string.length() - index, index)) {
                break;
            }

            boolean even = true;
            boolean odd = true;

            for (Integer length : lengthsToTry(index, string)) {
                even = even && isEvenLengthPalindrome(string, index, length);
                odd = odd && isOddLengthPalindrome(string, index, length);
                if (!even && !odd) {
                    break;
                }
                
                int l = -1;
                if (even) {
                    l = 2 * length;
                }
                if (odd) {
                    l = 2 * length + 1;
                }
                if (l > longest) {
                    indexOfLongest = index;
                    longest = l;
                }
            }
        }

        if (longest <= 1) {
            return null;
        }

        int start = indexOfLongest - (longest / 2);
        int end = start + longest;
        return string.subSequence(start, end);
    }

    private static boolean isEvenLengthPalindrome(CharSequence string, Integer index, Integer length) {
        return (string.charAt(index - length) == string.charAt(index + length - 1));
    }

    private static boolean isOddLengthPalindrome(CharSequence string, Integer index, Integer length) {
        return (string.charAt(index - length) == string.charAt(index + length));
    }

    private static Iterable<Integer> lengthsToTry(final int index, final CharSequence string) {
        final int stringLength = string.length();
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
                        int extent = index + i - 1 + (i % 2);
                        return extent >= stringLength;
                    }

                    private boolean wouldExtendPastBeginning(int i) {
                        return i > index;
                    }
                };
            }
        };
    }

    @VisibleForTesting
    static Iterable<Integer> indices(final CharSequence string) {
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
