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
