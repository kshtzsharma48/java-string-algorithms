package algorithms.search;

import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;


public class UkkonenSuffixTreeTest extends StringSearchTestBase {

  private UkkonenSuffixTree suffixTreeFor(CharSequence string) {
    return UkkonenSuffixTree.forString(string);
  }

  @Test
  public void simpleIndexOf() {
    assertEquals(10, suffixTreeFor("The quick brown fox jumped over the lazy dog.").indexOf("brown fox"));
  }

  @Test
  public void emptyString() {
    assertEquals(-1, suffixTreeFor("").indexOf("A"));
  }

  @Test
  public void largerSubstring() {
    assertEquals(-1, suffixTreeFor("aaaa").indexOf("aaaaa"));
  }

  @Override
  @Ignore("Would fail due to unsupported operation of iterating over all matches (currently too costly)")
  public void findPassageInBeowulf1() throws IOException {
    // super.findPassageInBeowulf1();
  }

  @Override
  @Ignore("Would fail due to unsupported operation of iterating over all matches (currently too costly)")
  public void findPassageInBeowulf2() throws IOException {
    // super.findPassageInBeowulf2();
  }

  @Override
  protected StringSearch createNewSearchApi() {
    return Searchers.newUkkonenSuffixTreeStringSearcher();
  }
}
