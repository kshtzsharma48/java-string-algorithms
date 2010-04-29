package algorithms.search;

import java.util.HashMap;
import java.util.Map;

/**
 * An implementation of the Boyer-Moore algorithm for searching strings.
 *
 * <a href="http://en.wikipedia.org/wiki/Boyer–Moore_string_search_algorithm">
 *  http://en.wikipedia.org/wiki/Boyer–Moore_string_search_algorithm
 * </a>
 */
public class BoyerMoore implements StringSearch {

	private static Map<Character, Integer> computeBadTable(final CharSequence search) {
		final int lastIndex = search.length() - 1;
		final Map<Character, Integer> table = new HashMap<Character, Integer>();
		for (int i = lastIndex - 1; i >= 0; i--) {
			if (!table.containsKey(search.charAt(i))) {
				table.put(search.charAt(i), lastIndex - i);
			}
		}
		return table;
	}

	@Override
	public int indexOf(final CharSequence haystack, final CharSequence needle) {
		int needle_len = needle.length();
		int haystack_len = haystack.length();

		if (haystack_len == 0) {
			return -1;
		}
		if (needle_len == 0) {
			return 0;
		}

		Map<Character, Integer> badcharacter = computeBadTable(needle);
		int[] goodsuffix = prepareGoodSuffixHeuristic(needle);

		int s = 0;
		while (s <= (haystack_len - needle_len)) {
			int j = needle_len;
			while (j > 0 && needle.charAt(j - 1) == haystack.charAt(s + j - 1)) {
				j--;
			}

			if (j > 0) {
				int k = badcharacter.containsKey(haystack.charAt(s + j - 1))
						  ? badcharacter.get(haystack.charAt(s + j - 1))
						  : needle_len;
				int m;
				if (k < j && (m = (j - k - 1)) > goodsuffix[j]) {
					s += m;
				} else {
					s += goodsuffix[j];
				}
			} else {
				return s;
			}
		}

		return -1;
	}

	private static int[] computePrefix(CharSequence str) {
		int size = str.length();
		int k;
		int[] result = new int[size];
		result[0] = 0;
		k = 0;
		for (int q = 1; q < size; q++) {
			while (k > 0 && str.charAt(k) != str.charAt(q)) {
				k = result[k - 1];
			}
			if (str.charAt(k) == str.charAt(q)) {
				k++;
			}
			result[q] = k;
		}
		return result;
	}

	private static int[] prepareGoodSuffixHeuristic(CharSequence normal) {
		int size = normal.length();
		int[] result = new int[size + 1];
		CharSequence reversed = new StringBuilder(normal).reverse();
		int[] prefix_normal = computePrefix(normal);
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
}
