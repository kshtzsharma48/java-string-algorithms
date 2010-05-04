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

import java.util.Iterator;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Searcher factory.
 */
public class Searchers {

    private abstract static class AbstractSearcher implements StringSearch {
		@Override
		public final int indexOf(final CharSequence substring, final CharSequence string) {
			checkNotNull(substring);
			checkNotNull(string);

			if (string.length() < substring.length()) {
				return -1;
			} else if (substring.length() == 0) {
				return 0;
			} else if (string.length() == 0) {
				return -1;
			}

			return compileFinder(substring).indexIn(string);
		}
	}

	private static class BoyerMooreSearcher extends AbstractSearcher {

		@Override
		public StringFinder compileFinder(final CharSequence sought) {
			return new BoyerMoore(sought);
		}
	}

    private static class KnuthMorrisPrattSearcher extends AbstractSearcher {

        @Override
        public StringFinder compileFinder(CharSequence sought) {
            return new KnuthMorrisPratt(sought);
        }
    }
    public static StringSearch newNaiveSearcher() {
        class NaiveSearcher extends AbstractSearcher {
            @Override
            public StringFinder compileFinder(final CharSequence sought) {
                class Naive extends AbstractStringFinder {
                    @Override
                    public int indexIn(CharSequence string, int startIndex) {
                        return sought.toString().indexOf(string.toString(), startIndex);
                    }
                }
                return new Naive();
            }
        }
        return new NaiveSearcher();
    }

	public static StringSearch newBoyerMooreStringSearcher() {
		return new BoyerMooreSearcher();
	}

    public static StringSearch newKnuthMorrisPrattStringSearcher() {
        return new KnuthMorrisPrattSearcher();
    }

    public static StringSearch newUkkonenSuffixTreeStringSearcher() {
        return new AbstractSearcher() {
            @Override
            public StringFinder compileFinder(final CharSequence sought) {
                class Ukkonen extends AbstractStringFinder {
                    @Override
                    public int indexIn(CharSequence string, int startIndex) {
                        return suffixTreeFor(string.subSequence(startIndex, string.length())).indexOf(sought);
                    }

                    @Override
                    public Iterator<Integer> allMatches(CharSequence text, int startIndex) {
                        throw new UnsupportedOperationException("Too costly on large strings!");
                    }
                }
                return new Ukkonen();
            }

            private UkkonenSuffixTree suffixTreeFor(CharSequence string) {
                return UkkonenSuffixTree.forString(string);
            }
        };
    }
}
