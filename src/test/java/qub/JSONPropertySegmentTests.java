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
    }

    private static void assertPropertySegment(String text, JSONQuotedString nameSegment, String name, JSONToken colonSegment, JSONSegment valueSegment, int afterEndIndex)
    {
        final JSONTokenizer tokenizer = new JSONTokenizer(text);
        final Iterable<JSONSegment> segments = ArrayList.fromValues(tokenizer).instanceOf(JSONSegment.class);
        final JSONPropertySegment propertySegment = new JSONPropertySegment(segments);

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
