package qub;

import org.junit.Test;

import static org.junit.Assert.*;

public class JSONPropertySegmentTests
{
    @Test
    public void constructor()
    {
        assertPropertySegment("\"",
                JSONToken.quotedString("\"", 0, false),
                "",
                null,
                null,
                1);
        assertPropertySegment("\"\"",
                JSONToken.quotedString("\"\"", 0, true),
                "",
                null,
                null,
                2);

        assertPropertySegment("\"test",
                JSONToken.quotedString("\"test", 0, false),
                "test",
                null,
                null,
                5);
        assertPropertySegment("\"test\"",
                JSONToken.quotedString("\"test\"", 0, true),
                "test",
                null,
                null,
                6);

        assertPropertySegment("\"a\" ",
                JSONToken.quotedString("\"a\"", 0, true),
                "a",
                null,
                null,
                4);
        assertPropertySegment("\"a\":",
                JSONToken.quotedString("\"a\"", 0, true),
                "a",
                JSONToken.colon(3),
                null,
                4);
        assertPropertySegment("\"a\" :",
                JSONToken.quotedString("\"a\"", 0, true),
                "a",
                JSONToken.colon(4),
                null,
                5);

        assertPropertySegment("\"a\":\"b\"",
                JSONToken.quotedString("\"a\"", 0, true),
                "a",
                JSONToken.colon(3),
                JSONToken.quotedString("\"b\"", 4, true),
                7);
        assertPropertySegment("\"a\":  ",
                JSONToken.quotedString("\"a\"", 0, true),
                "a",
                JSONToken.colon(3),
                null,
                6);
        assertPropertySegment("\"a\":// comment",
                JSONToken.quotedString("\"a\"", 0, true),
                "a",
                JSONToken.colon(3),
                null,
                14);
        assertPropertySegment("\"a\":/* comment",
                JSONToken.quotedString("\"a\"", 0, true),
                "a",
                JSONToken.colon(3),
                null,
                14);
        assertPropertySegment("\"apples\":{}",
                JSONToken.quotedString("\"apples\"", 0, true),
                "apples",
                JSONToken.colon(8),
                JSON.parseObject("{}", 9),
                11);
    }

    @Test
    public void equalsTest()
    {
        final JSONPropertySegment propertySegment = JSON.parseProperty("\"a\":\"b\"");
        assertFalse(propertySegment.equals((Object)null));
        assertFalse(propertySegment.equals((JSONPropertySegment)null));

        assertFalse(propertySegment.equals((Object)"test"));

        assertTrue(propertySegment.equals(propertySegment));
        assertTrue(propertySegment.equals(JSON.parseProperty("\"a\":\"b\"")));
        assertFalse(propertySegment.equals(JSON.parseProperty("\"a\":50")));
    }

    private static void assertPropertySegment(String text, JSONQuotedString nameSegment, String name, JSONToken colonSegment, JSONSegment valueSegment, int afterEndIndex)
    {
        final JSONPropertySegment propertySegment = JSON.parseProperty(text);

        assertEquals(nameSegment, propertySegment.getNameSegment());
        assertEquals(name, propertySegment.getName());

        assertEquals(colonSegment, propertySegment.getColonSegment());

        assertEquals(valueSegment, propertySegment.getValueSegment());

        assertEquals(text, propertySegment.toString());

        assertEquals(nameSegment.getStartIndex(), propertySegment.getStartIndex());

        assertEquals(afterEndIndex, propertySegment.getAfterEndIndex());

        assertEquals(afterEndIndex - nameSegment.getStartIndex(), propertySegment.getLength());
    }
}
