package qub;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class JSONDocumentTests
{
    @Test
    public void constructor()
    {
        constructorTest("");
        constructorTest("// hello");
        constructorTest("/* there */");
        constructorTest("\"fever\"", JSONToken.quotedString("\"fever\"", 0, true));
        constructorTest("20", JSONToken.number("20"));
        constructorTest("[0, 1, 2, 3, 4]", JSON.parseArray("[0, 1, 2, 3, 4]"));
        constructorTest("{}", JSON.parseObject("{}"));
    }

    private static void constructorTest(String text)
    {
        constructorTest(text, null);
    }

    private static void constructorTest(String text, JSONSegment expectedRoot)
    {
        final JSONDocument document = JSON.parse(text);
        assertEquals(expectedRoot, document.getRoot());
        assertEquals(text, document.toString());
        assertEquals(text.length(), document.getLength());
    }
}
