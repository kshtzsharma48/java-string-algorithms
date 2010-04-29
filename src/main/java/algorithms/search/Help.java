package algorithms.search;


class Help {

	static class LookupTable {
		long[] data = new long[1024];

		void set(char index) {
			data[index >> 6] |= (1L << index);
		}

		boolean get(char index) {
			return (data[index >> 6] & (1L << index)) != 0;
		}
	}

	static CharSequence reverse(final CharSequence string) {
		return new CharSequence() {
			@Override
			public int length() {
				return string.length();
			}

			@Override
			public char charAt(final int index) {
				return string.charAt(length() - index - 1);
			}

			@Override
			public CharSequence subSequence(final int start, final int end) {
				throw new UnsupportedOperationException();
			}
		};
	}
}
