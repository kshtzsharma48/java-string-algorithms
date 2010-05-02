package algorithms.search;

import java.util.Arrays;

/**
 * Implementation of the Knuth-Morris-Pratt algorithm for finding substrings
 * in a larger string.
 * <p/>
 * See http://en.wikipedia.org/wiki/Knuth%E2%80%93Morris%E2%80%93Pratt_algorithm
 */
class KnuthMorrisPratt extends AbstractStringFinder {

    private static int[] computeTable(CharSequence string) {
        if (string.length() < 2) {
            // warning! this effectively becomes a poor implementation of the "naive" approach at this point
            return new int[] { -1 };
        }

        int[] table = new int[string.length()];
        table[0] = -1;
        table[1] = 0;
        for (int i = 2, test = 0; i < table.length;) {
            if (string.charAt(i - 1) == string.charAt(test)) {
                table[i] = test + 1;
                i += 1;
                test += 1;
            } else if (test > 0) {
                test = table[test];
            } else {
                table[i] = 0;
                i += 1;
            }
        }

        return table;
    }

    private final CharSequence sought;
    private final int[] table;

    public KnuthMorrisPratt(final CharSequence sought) {
        this.sought = sought;
        this.table = computeTable(sought);
    }

    @Override
    public int indexIn(CharSequence string, int startIndex) {
        int m = startIndex;
        int i = 0;
        int l = string.length();
        int maxM = l - sought.length();

        for (int j = m + i; m <= maxM && j < l; j = m + i) {
            if (sought.charAt(i) == string.charAt(j)) {
                i += 1;
                if (i == sought.length()) {
                    return m;
                }
            } else {
                int shift = table[i];
                m += i - shift;
                i = (shift > -1) ? shift : 0;
            }
        }

        return -1;
    }
}
