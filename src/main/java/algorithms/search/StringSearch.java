package algorithms.search;

/**
 * A string search algorithm.
 */
public interface StringSearch {

	StringFinder compileFinder(CharSequence sought);

	int indexOf(CharSequence sought, CharSequence string);
}
