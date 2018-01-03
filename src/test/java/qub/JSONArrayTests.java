package qub;

import org.junit.Test;

import static org.junit.Assert.*;

public class JSONArrayTests
{
    @Test
    public void constructor()
    {
        constructorTest("[",
            JSONToken.leftSquareBracket(0),
            null,
            new JSONSegment[0]);
        constructorTest("[]",
            JSONToken.leftSquareBracket(0),
            JSONToken.rightSquareBracket(1),
            new JSONSegment[0]);
        constructorTest("[ ]",
            JSONToken.leftSquareBracket(0),
            JSONToken.rightSquareBracket(2),
            new JSONSegment[0]);
        constructorTest("[0]",
            JSONToken.leftSquareBracket(0),
            JSONToken.rightSquareBracket(2),
            new JSONSegment[]
            {
                JSONToken.number("0", 1)
            });
        constructorTest("[0,1]",
            JSONToken.leftSquareBracket(0),
            JSONToken.rightSquareBracket(4),
            new JSONSegment[]
                {
                    JSONToken.number("0", 1),
                    JSONToken.number("1", 3)
                });
        constructorTest("[,]",
            JSONToken.leftSquareBracket(0),
            JSONToken.rightSquareBracket(2),
            new JSONSegment[]
                {
                    null,
                    null
                });
        constructorTest("[[]]",
            JSONToken.leftSquareBracket(0),
            JSONToken.rightSquareBracket(3),
            new JSONSegment[]
                {
                   JSON.parseArray("[]", 1)
                });
    }

    private static void constructorTest(String text, JSONToken leftSquareBracket, JSONToken rightSquareBracket, JSONSegment[] elementSegments)
    {
        final JSONArray arraySegment = JSON.parseArray(text);
        assertNotNull(arraySegment);
        assertEquals(leftSquareBracket, arraySegment.getLeftSquareBracket());
        assertEquals(rightSquareBracket, arraySegment.getRightSquareBracket());
        assertEquals(text, arraySegment.toString());
        assertEquals(Array.fromValues(elementSegments), arraySegment.getElements());
        for (int i = 0; i < elementSegments.length; ++i)
        {
            assertEquals(elementSegments[i], arraySegment.getElement(i));
        }
        assertEquals(elementSegments.length, arraySegment.getElementCount());
        assertEquals(0, arraySegment.getStartIndex());
        assertEquals(text.length(), arraySegment.getAfterEndIndex());
    }
}
