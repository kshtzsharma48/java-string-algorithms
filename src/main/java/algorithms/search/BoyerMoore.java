/*
 * Copyright (c) 2010 Steve Reed
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package algorithms.search;

import com.google.common.collect.AbstractIterator;
import static com.google.common.collect.Iterators.size;
import static java.lang.Math.max;
import static java.util.Arrays.fill;

import java.util.Iterator;

/**
 * This is an adaptation of Doyle Myers' original source, located at
 * http://www.aelvin.com/bmi.html. I've modified it so this object
 * tracks much less state. TODO : see how to property attribute work
 * <p/>
 * It's important for the user to realize the memory and performance implications when
 * choosing this specific algorithm. This algorithm creates an integer table for 64k
 * unicode characters in memory, which means that for short queries or short search text
 * a brute-force approach or another algorithm may be preferred.
 */
public class BoyerMoore implements StringFinder {

    private static final int ALPHABET_SIZE = 0x10000; // UNICODE alphabet size (64K chars)

    private static int[] preComputeBadCharTable(CharSequence sought) {
        int length = sought.length();
        int[] skip = new int[ALPHABET_SIZE];
        fill(skip, length);
        for (int j = 0; j < length - 1; j++) {
            skip[sought.charAt(j)] = length - j - 1;
        }
        return skip;
    }

    private static int[] preComputeGoodCharTable(CharSequence sought) {
        int length = sought.length();
        int[] result = new int[length + 1];
        int[] f = new int[length + 1];
        int j = length + 1;

        f[length] = j;

        for (int i = length; i > 0; i--) {
            while (j <= length && sought.charAt(i - 1) != sought.charAt(j - 1)) {
                if (result[j] == 0) {
                    result[j] = j - i;
                }
                j = f[j];
            }
            f[i - 1] = --j;
        }

        int p = f[0];
        for (j = 0; j <= length; ++j) {
            if (result[j] == 0) {
                result[j] = p;
            }
            if (j == p) {
                p = f[p];
            }
        }
        return result;
    }

    private final int[] badCharTable;
    private final int[] goodCharTable;
    private final CharSequence sought;
    private final int soughtLength;

    public BoyerMoore(final CharSequence sought) {
        this.sought = sought;
        soughtLength = sought.length();
        badCharTable = preComputeBadCharTable(sought);
        goodCharTable = preComputeGoodCharTable(sought);
    }

    @Override
    public int indexIn(CharSequence string) {
        return indexIn(string, 0);
    }

    @Override
    public int indexIn(CharSequence string, int startIndex) {
        Iterator<Integer> indexes = matches(string, startIndex);
        return indexes.hasNext() ? indexes.next() : -1;
    }

    @Override
    public boolean isPresentIn(CharSequence string) {
        return matches(string, 0).hasNext();
    }

    @Override
    public int countOccurencesIn(CharSequence string) {
        return size(matches(string, 0));
    }

    @Override
    public Iterator<Integer> matches(final CharSequence text, final int startIndex) {
        return new AbstractIterator<Integer>() {

            private int i = max(0, startIndex);

            @Override
            protected Integer computeNext() {
                int textLength = text.length();
                int lengthDiff = textLength - soughtLength;

                while (i <= lengthDiff) {
                    int j;
                    for (j = soughtLength - 1; j >= 0 && sought.charAt(j) == text.charAt(i + j); j--) {
                        // decrement j while matches
                    }

                    if (j < 0) {
                        int found = i;
                        i += goodCharTable[0];
                        return found;
                    } else {
                        i += max(
                                goodCharTable[j + 1], // suffix
                                badCharTable[text.charAt(i + j)] - soughtLength + j + 1 // bad char
                        );
                    }
                }

                return endOfData();
            }
        };
    }
}
