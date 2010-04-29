package algorithms.search;

import java.util.Map;

import com.google.common.annotations.VisibleForTesting;
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

	@VisibleForTesting
	static int[] computePrefixOld(CharSequence string) {
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

	@VisibleForTesting
	static int[] computePrefix(CharSequence string) {
		int q, k;
		int[] result = new int[string.length()];
		result[0] = 0;
		k = 0;
		for (q = 1; q < string.length(); q++) {
			while (k > 0 && string.charAt(k) != string.charAt(q)) {
				k = result[k - 1];
			}
			if (string.charAt(k) == string.charAt(q)) {
				k++;
			}

			result[q] = k;
		}
		return result;
	}

	static int[] prepareGoodSuffixHeuristic(CharSequence sought) {
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
	private final int soughtLength;
	private final Map<Character, Integer> badCharacterShifts;
	private final int[] goodSuffixShifts;

	BoyerMoore(CharSequence sought) {
		this.sought = sought;
		this.soughtLength = sought.length();
		this.badCharacterShifts = computeBadTable(sought);
		this.goodSuffixShifts = prepareGoodSuffixHeuristic(sought);
	}

	@Override
	public int indexIn(final CharSequence string) {
		assert string.length() > sought.length() : "Simple length heuristic should have been tested already.";
		assert string.length() != 0 : "Simple length heuristic should have been tested already.";
		assert sought.length() != 0 : "Simple length heuristic should have been tested already.";

		int stringLength = string.length();
		int lengthDifference = stringLength - soughtLength;

		for (int index = 0, lastMatchIndex = soughtLength;
			  index <= lengthDifference; ) {

			// go from right to left until no chars to test (match found) or mismatch found
			for (lastMatchIndex = soughtLength;
				  lastMatchIndex > 0
							 && sought.charAt(lastMatchIndex - 1) == string.charAt(index + lastMatchIndex - 1);
				  lastMatchIndex--) {
				// continue
			}

			if (lastMatchIndex > 0) {
				int shift = getShift(string.charAt(index + lastMatchIndex - 1));
				int m = (lastMatchIndex - shift - 1);
				if (shift < lastMatchIndex && m > goodSuffixShifts[lastMatchIndex]) {
					index += m;
				} else {
					index += goodSuffixShifts[lastMatchIndex];
				}
			} else {
				return index;
			}
		}

		return -1;
	}

	private int getShift(Character character) {
		Integer shiftObjectValue = badCharacterShifts.get(character);
		int shift = (shiftObjectValue == null) ? soughtLength : shiftObjectValue;
		return shift;
	}
}
