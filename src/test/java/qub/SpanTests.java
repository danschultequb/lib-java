package qub;

import org.junit.Test;

import static org.junit.Assert.*;

public class SpanTests
{
    @Test
    public void constructor()
    {
        final Span span = new Span(1, 2);
        assertEquals(1, span.getStartIndex());
        assertEquals(2, span.getLength());
        assertEquals("[1, 3)", span.toString());
    }

    private static void getAfterEndIndexTest(int startIndex, int length)
    {
        final Span span = new Span(startIndex, length);
        final int expectedAfterEndIndex = startIndex + length;
        final int actualAfterEndIndex = span.getAfterEndIndex();
        assertEquals(getTestName("getAfterEndIndex", startIndex, length), expectedAfterEndIndex, actualAfterEndIndex);
    }

    @Test
    public void getAfterEndIndex()
    {
        getAfterEndIndexTest(-10, -3);
        getAfterEndIndexTest(-10, 0);
        getAfterEndIndexTest(-10, 7);
        getAfterEndIndexTest(0, -4);
        getAfterEndIndexTest(0, 0);
        getAfterEndIndexTest(0, 3);
        getAfterEndIndexTest(1, -1);
        getAfterEndIndexTest(1, 0);
        getAfterEndIndexTest(1, 1);
    }

    private static void getEndIndexTest(int startIndex, int length)
    {
        final Span span = new Span(startIndex, length);
        final int expectedEndIndex = startIndex + length - (length <= 0 ? 0 : 1);
        final int actualEndIndex = span.getEndIndex();
        assertEquals(getTestName("getEndIndex", startIndex, length), expectedEndIndex, actualEndIndex);
    }

    @Test
    public void getEndIndex()
    {
        getEndIndexTest(-10, -3);
        getEndIndexTest(-10, 0);
        getEndIndexTest(-10, 1);
        getEndIndexTest(-10, 7);
        getEndIndexTest(0, -4);
        getEndIndexTest(0, 0);
        getEndIndexTest(0, 1);
        getEndIndexTest(0, 3);
        getEndIndexTest(1, -1);
        getEndIndexTest(1, 0);
        getEndIndexTest(1, 1);
        getEndIndexTest(1, 21);
    }

    private static String getTestName(String methodName, int startIndex, int length)
    {
        return "Span." + methodName + "() with " + startIndex + " and " + length;
    }
}
