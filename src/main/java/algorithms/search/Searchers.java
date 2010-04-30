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

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Searcher factory.
 */
public class Searchers {

	private abstract static class AbstractSearcher implements StringSearch {
		@Override
		public final int indexOf(final CharSequence sought, final CharSequence string) {
			checkNotNull(sought);
			checkNotNull(string);

			if (string.length() < sought.length()) {
				return -1;
			} else if (sought.length() == 0) {
				return 0;
			} else if (string.length() == 0) {
				return -1;
			}

			return doIndexOf(sought, string);
		}

		protected abstract int doIndexOf(CharSequence sought, CharSequence string);
	}

	private static class BoyerMooreSearcher extends AbstractSearcher {

		@Override
		public int doIndexOf(final CharSequence sought, final CharSequence string) {
			return compileFinder(sought).indexIn(string);
		}

		@Override
		public StringFinder compileFinder(final CharSequence sought) {
			return new BoyerMoore(sought);
		}
	}

	static StringSearch newBoyerMooreStringSearcher() {
		return new BoyerMooreSearcher();
	}
}
