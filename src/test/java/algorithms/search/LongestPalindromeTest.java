package algorithms.search;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.io.Files;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static algorithms.search.Help.reverse;
import static com.google.common.base.CharMatcher.WHITESPACE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;


public class LongestPalindromeTest {

    private LongestPalindrome palindrome;

    @Before
    public void setup() {
        palindrome = new LongestPalindrome();
    }

    @Test
    public void indices() {
        List<Integer> l = Lists.newArrayList(palindrome.indices("abcdefg"));
        assertEquals(ImmutableList.of(3, 4, 2, 5, 1, 6, 0), l);
        l = Lists.newArrayList(palindrome.indices("0123456789"));
        assertEquals(ImmutableList.of(5, 4, 6, 3, 7, 2, 8, 1, 9, 0), l);
    }

    @Test
    public void smallOddLengthPalindrome() {
        CharSequence longest = palindrome.findLongestPalindrome("Yabadabadoo!");
        assertEquals(7, longest.length());
        assertEquals('a', longest.charAt(0));
        assertEquals('b', longest.charAt(1));
        assertEquals('a', longest.charAt(2));
        assertEquals('d', longest.charAt(3));
        assertEquals('a', longest.charAt(4));
        assertEquals('b', longest.charAt(5));
        assertEquals('a', longest.charAt(6));
    }

    @Test
    public void smallEvenLengthPalindrome() {
        CharSequence longest = palindrome.findLongestPalindrome("I can't believe ABBA is the first four-letter palindrome I thought of.");
        assertEquals(6, longest.length());
        assertEquals(' ', longest.charAt(0));
        assertEquals('A', longest.charAt(1));
        assertEquals('B', longest.charAt(2));
        assertEquals('B', longest.charAt(3));
        assertEquals('A', longest.charAt(4));
        assertEquals(' ', longest.charAt(5));
    }

    @Test
    public void longestPalindromeInBeowulf() throws IOException {
        File beowulf = new File(getClass().getClassLoader().getResource("anonymous-beowulf-543.txt").getFile());
		String beowulfText = Files.toString(beowulf, Charsets.UTF_8);
        beowulfText = WHITESPACE.collapseFrom(beowulfText, ' ');
        assertEquals("of a fo", palindrome.findLongestPalindrome(beowulfText).toString());
    }

    @Test
    public void considersWhitespace() {
        String str = "satan oscillate my metallic sonatas";
        assertEquals("ata", palindrome.findLongestPalindrome(str).toString());
    }

    @Test
    public void somms() {
        String str = "satan oscillate my metallic sonatas";
        str = WHITESPACE.removeFrom(str);
        assertEquals(str, palindrome.findLongestPalindrome(str).toString());
    }

    @Test
    public void somms2() {
        String somms = "satan oscillate my metallic sonatas";
        String str = "My favorite album is " + somms + "! Not really but for this test, yeah.";
        str = WHITESPACE.removeFrom(str);
        assertEquals(WHITESPACE.removeFrom(somms), palindrome.findLongestPalindrome(str).toString());
    }

    @Test(expected = NullPointerException.class)
    public void nullString() {
        assertNull(palindrome.findLongestPalindrome(null));
    }

    @Test
    public void emptyString() {
        assertNull(palindrome.findLongestPalindrome(""));
    }

    @Test
    public void beginsWithOddLengthPalindrome() {
        assertEquals(5, palindrome.findLongestPalindrome("abcbadefghijklmnop").length());
        assertEquals("abcba", palindrome.findLongestPalindrome("abcbadefghijklmnop").toString());
    }

    @Test
    public void beginsWithEvenLengthPalindrome() {
        assertEquals("baab", palindrome.findLongestPalindrome("baabcdefghijklmnop").toString());
    }

    @Test
    public void endsWithOddLengthPalindrome() {
        assertEquals("abcba", palindrome.findLongestPalindrome(reverse("abcbadefghijklmnop")).toString());
    }

    @Test
    public void endsWithEvenLengthPalindrome() {
        assertEquals("baab", palindrome.findLongestPalindrome(reverse("baabcdefghijklmnop")).toString());
    }

    @Test
    public void itIsCaseSensitive() {
        assertEquals("oo", palindrome.findLongestPalindrome("Foof").toString());
    }

    @Test
    public void nullWhenNoPalindromes() {
        assertNull(palindrome.findLongestPalindrome("abcdefg hijklmnop qr stuv wx yz!?"));
    }
}
