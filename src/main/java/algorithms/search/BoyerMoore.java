package algorithms.search;

import java.util.Map;

import com.google.common.collect.Maps;

/**
 * An implementation of the Boyer-Moore algorithm for searching strings.
 *
 * <a href="http://en.wikipedia.org/wiki/Boyer–Moore_string_search_algorithm">
 *  http://en.wikipedia.org/wiki/Boyer–Moore_string_search_algorithm
 * </a>
 */
class BoyerMoore implements StringFinder {

	// TODO : TurboBoyerMoore
	// TODO : BoyerMooreHorspool

	private static Map<Character, Integer> computeBadTable(final CharSequence sought) {
		final int lastIndex = sought.length() - 1;
		final Map<Character, Integer> table = Maps.newHashMapWithExpectedSize(sought.length());
		for (int i = lastIndex - 1; i >= 0; i--) {
			Character c = sought.charAt(i);
			if (!table.containsKey(c)) {
				table.put(c, lastIndex - i);
			}
		}
		return table;
	}

	private static int[] computePrefix(CharSequence string) {
		int size = string.length();
		int[] result = new int[size];
		result[0] = 0;
		for (int i = 1, k = 0; i < size; i++) {
			while (k > 0 && string.charAt(k) != string.charAt(i)) {
				k = result[k - 1];
			}
			if (string.charAt(k) == string.charAt(i)) {
				k++;
			}
			result[i] = k;
		}
		return result;
	}

	private static int[] prepareGoodSuffixHeuristic(CharSequence sought) {
		int size = sought.length();
		int[] result = new int[size + 1];
		CharSequence reversed = Help.reverse(sought);
		int[] prefix_normal = computePrefix(sought);
		int[] prefix_reversed = computePrefix(reversed);
		for (int i = 0; i <= size; i++) {
			result[i] = size - prefix_normal[size - 1];
		}
		for (int i = 0; i < size; i++) {
			int j = size - prefix_reversed[i];
			int k = i - prefix_reversed[i] + 1;
			if (result[j] > k) {
				result[j] = k;
			}
		}
		return result;
	}

	private final CharSequence sought;
	private final Map<Character, Integer> badCharacterShifts;
	private final int[] goodSuffixShifts;

	BoyerMoore(CharSequence sought) {
		this.sought = sought;
		this.badCharacterShifts = computeBadTable(sought);
		this.goodSuffixShifts = prepareGoodSuffixHeuristic(sought);
	}

	@Override
	public int indexIn(final CharSequence string) {
		assert string.length() > sought.length() : "Simple length heuristic should have been tested already.";
		assert string.length() != 0 : "Simple length heuristic should have been tested already.";
		assert sought.length() != 0 : "Simple length heuristic should have been tested already.";

		int needle_len = sought.length();
		int haystack_len = string.length();

		int s = 0;
		while (s <= (haystack_len - needle_len)) {
			int j = needle_len;
			while (j > 0 && sought.charAt(j - 1) == string.charAt(s + j - 1)) {
				j--;
			}

			if (j > 0) {
				Integer shift = badCharacterShifts.get(string.charAt(s + j - 1));
				int k = (shift == null) ? needle_len : shift;
				int m;
				if (k < j && (m = (j - k - 1)) > goodSuffixShifts[j]) {
					s += m;
				} else {
					s += goodSuffixShifts[j];
				}
			} else {
				return s;
			}
		}

		return -1;
	}
}
