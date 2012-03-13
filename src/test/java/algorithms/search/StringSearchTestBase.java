package algorithms.search;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public abstract class StringSearchTestBase {

  private StringSearch searchApi;

  protected abstract StringSearch createNewSearchApi();

  protected StringSearch searchApi() {
    return searchApi;
  }

  protected CharSequence loadBeowulfText() throws IOException {
    File beowulf = new File(getClass().getClassLoader().getResource("anonymous-beowulf-543.txt").getFile());
    return Files.toString(beowulf, Charsets.ISO_8859_1);
  }

  @Before
  public void setup() {
    searchApi = createNewSearchApi();
  }

  @Test
  public void findEmptyString() {
    assertEquals(0, searchApi.indexOf("", "a"));
  }

  @Test
  public void findBanana() {
    assertEquals(5, searchApi.indexOf("banana", "bananbanana"));
  }

  @Test
  public void findNeedleInHaystackLiterally() {
    assertEquals(-1, searchApi.indexOf("needle", "haystack"));
  }

  @Test
  public void findFooInFoobar() {
    assertEquals(0, searchApi.indexOf("foo", "foobar"));
  }

  @Test
  public void findBarInFoobar() {
    assertEquals(3, searchApi.indexOf("bar", "foobar"));
  }

  @Test
  public void findFooInLargerFoobar() {
    final String haystack = "The quick brown fox jumped over the lazy dog. Foo. The quick brown fox jumped over the lazy dog";
    assertEquals(46, searchApi.indexOf("Foo", haystack));
    assertEquals("Foo", haystack.substring(46, 49));
    assertEquals(46, searchApi.compileFinder("Foo").indexIn(haystack));
  }

  @Test
  public void findPassageInBeowulf1() throws IOException {
    CharSequence beowulfText = loadBeowulfText();
    final StringFinder beowulfFinder = searchApi.compileFinder("Beowulf");
    assertEquals(84, beowulfFinder.countOccurencesIn(beowulfText));
  }

  @Test
  public void findPassageInBeowulf2() throws IOException {
    CharSequence beowulfText = loadBeowulfText();
    final StringFinder internetWiretapFinder = searchApi.compileFinder("Internet Wiretap");
    assertEquals(4, internetWiretapFinder.indexIn(beowulfText));
    assertEquals(2, internetWiretapFinder.countOccurencesIn(beowulfText));
  }

  @Test
  public void findPassageInBeowulf3() throws IOException {
    CharSequence beowulfText = loadBeowulfText();
    final StringFinder stringFinder = searchApi.compileFinder("Beowulf finally accepted Hygd's offer of kingdom and hoard");
    assertEquals(146983, stringFinder.indexIn(beowulfText));
  }

  @Test
  public void caseSensitivity() {
    final String text = "ALL CAPS TEXT";
    assertEquals(-1, searchApi.indexOf("a", text));
    assertEquals(-1, searchApi.indexOf("l", text));
    assertEquals(-1, searchApi.indexOf("c", text));
    assertEquals(-1, searchApi.indexOf("p", text));
    assertEquals(-1, searchApi.indexOf("s", text));
    assertEquals(-1, searchApi.indexOf("t", text));
    assertEquals(-1, searchApi.indexOf("e", text));
    assertEquals(-1, searchApi.indexOf("x", text));
    assertEquals(0, searchApi.indexOf("A", text));
    assertEquals(1, searchApi.indexOf("L", text));
    assertEquals(3, searchApi.indexOf(" ", text));
    assertEquals(4, searchApi.indexOf("C", text));
    assertEquals(6, searchApi.indexOf("P", text));
    assertEquals(7, searchApi.indexOf("S", text));
    assertEquals(9, searchApi.indexOf("T", text));
    assertEquals(10, searchApi.indexOf("E", text));
    assertEquals(11, searchApi.indexOf("X", text));
  }
}
