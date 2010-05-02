package algorithms.search;

import com.google.common.collect.AbstractIterator;

import java.util.Iterator;

import static com.google.common.base.Preconditions.checkNotNull;


public class LongestPalindrome {

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
        
        final int length = longest;
        final int index = indexOfLongest;
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
                return toString().subSequence(start, end);
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

    private boolean isEvenLengthPalindrome(CharSequence string, Integer index, Integer length) {
        return (string.charAt(index - length) == string.charAt(index + length - 1));
    }

    private boolean isOddLengthPalindrome(CharSequence string, Integer index, Integer length) {
        return (string.charAt(index - length) == string.charAt(index + length));
    }

    private Iterable<Integer> lengthsToTry(final int index, final CharSequence string) {
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
