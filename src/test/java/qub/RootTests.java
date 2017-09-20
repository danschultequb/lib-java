package qub;

import org.junit.Test;

import static org.junit.Assert.*;

public class RootTests
{
    @Test
    public void constructorWithStringPath()
    {
        final Root root = new Root(null, "/path/to/root/");
        assertEquals("/path/to/root/", root.toString());
    }
}
