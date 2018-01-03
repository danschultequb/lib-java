package qub;

import org.junit.Test;

import static org.junit.Assert.*;

public abstract class PathPatternTests
{
    protected abstract PathPattern parse(String text);

    @Test
    public void parse()
    {
        final PathPattern pattern = PathPattern.parse("test");
        assertTrue(pattern instanceof SimplePathPattern);
        assertEquals("test", pattern.toString());
    }

    @Test
    public void parseNull()
    {
        parseTest(null);
    }

    @Test
    public void parseEmpty()
    {
        parseTest("");
    }

    @Test
    public void parseNonEmptyLetters()
    {
        parseTest("abcd");
    }

    @Test
    public void parseBackslash()
    {
        parseTest("\\");
    }

    @Test
    public void parseForwardSlash()
    {
        parseTest("/");
    }

    @Test
    public void parseStar()
    {
        parseTest("*");
    }

    @Test
    public void parseStarFollowedByLetters()
    {
        parseTest("*abc");
    }

    @Test
    public void parseDoubleStar()
    {
        parseTest("**");
    }

    @Test
    public void parseDoubleStarFollowedByLetters()
    {
        parseTest("**test");
    }

    private void parseTest(String text)
    {
        final PathPattern pattern = parse(text);
        assertNotNull(pattern);
        assertEquals(text, pattern.toString());
        assertTrue(pattern.isMatch(text));
    }

    @Test
    public void isMatchWithNullPattern()
    {
        final PathPattern pattern = parse(null);

        assertTrue(pattern.isMatch((String)null));
        assertTrue(pattern.isMatch(""));
        assertFalse(pattern.isMatch("a"));

        assertTrue(pattern.isMatch((Path)null));
        assertTrue(pattern.isMatch(Path.parse("")));
        assertFalse(pattern.isMatch(Path.parse("a")));
    }

    @Test
    public void isMatchWithLettersPattern()
    {
        final PathPattern pattern = parse("abc");

        assertTrue(pattern.isMatch("abc"));
        assertTrue(pattern.isMatch(Path.parse("abc")));

        assertFalse(pattern.isMatch("ab"));
        assertFalse(pattern.isMatch("abcd"));
    }

    @Test
    public void isMatchWithForwardSlash()
    {
        final PathPattern pattern = parse("a/b");

        assertTrue(pattern.isMatch("a/b"));
        assertTrue(pattern.isMatch(Path.parse("a/b")));
        assertTrue(pattern.isMatch("a\\b"));
        assertTrue(pattern.isMatch(Path.parse("a\\b")));

        assertFalse(pattern.isMatch("a/b/"));
        assertFalse(pattern.isMatch("a\\b\\"));
    }

    @Test
    public void isMatchWithStar()
    {
        final PathPattern pattern = parse("*");

        assertTrue(pattern.isMatch(""));
        assertTrue(pattern.isMatch(".java"));
        assertTrue(pattern.isMatch("Test.java"));
        assertTrue(pattern.isMatch("..java"));
        assertTrue(pattern.isMatch("Test.jav"));
        assertTrue(pattern.isMatch("Test.javas"));

        assertFalse(pattern.isMatch("/"));
        assertFalse(pattern.isMatch("\\"));
    }

    @Test
    public void isMatchWithStarAndFileExtension()
    {
        final PathPattern pattern = parse("*.java");

        assertTrue(pattern.isMatch(".java"));
        assertTrue(pattern.isMatch("Test.java"));
        assertTrue(pattern.isMatch("..java"));

        assertFalse(pattern.isMatch(""));
        assertFalse(pattern.isMatch("Test.jav"));
        assertFalse(pattern.isMatch("Test.javas"));
        assertFalse(pattern.isMatch("/Test.java"));
        assertFalse(pattern.isMatch("\\Test.java"));
    }

    @Test
    public void isMatchWithDoubleStarAndFileExtension()
    {
        final PathPattern pattern = parse("sources/**.java");

        assertTrue(pattern.isMatch("sources/Test.java"));
        assertTrue(pattern.isMatch("sources/qub/Test.java"));
        assertTrue(pattern.isMatch("sources\\qub\\package\\Class.java"));

        assertFalse(pattern.isMatch("output/Test.java"));
    }

    @Test
    public void equals()
    {
        final PathPattern pattern = parse("a/b/c");

        assertFalse(pattern.equals((Object)null));
        assertFalse(pattern.equals((PathPattern)null));
        assertFalse(pattern.equals("path pattern"));
        assertFalse(pattern.equals(parse("a/b")));

        assertTrue(pattern.equals(pattern));
        assertTrue(pattern.equals(parse(pattern.toString())));
    }
}
