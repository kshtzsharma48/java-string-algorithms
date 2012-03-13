package algorithms.search;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class HelpTest {

  @Test
  public void reverseTest() {
    assertEquals(3, Help.reverse("foo").length());
    assertEquals(10, Help.reverse(new StringBuilder("abcd").append("efg").append("hij")).length());
    assertEquals("oof", Help.reverse("foo").toString());
    CharSequence reversed = Help.reverse("0123456789");
    for (int i = 0; i < reversed.length(); i++) {
      assertEquals(String.valueOf(10 - i - 1).charAt(0), reversed.charAt(i));
    }
  }

  @Test
  public void lookupTableTest() {
    Help.LookupTable table = new Help.LookupTable();
    for (char c = Character.MIN_VALUE; c < Character.MAX_VALUE; c++) {
      assertFalse(table.get(c));
    }
    assertFalse(table.get(Character.MAX_VALUE));
    for (char c = Character.MIN_VALUE; c < Character.MAX_VALUE; c++) {
      assertFalse(table.get(c));
      table.set(c);
      assertTrue(table.get(c));
    }
    assertFalse(table.get(Character.MAX_VALUE));
    table.set(Character.MAX_VALUE);
    assertTrue(table.get(Character.MAX_VALUE));
  }
}
