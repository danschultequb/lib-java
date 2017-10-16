package qub;

import org.junit.Test;

import static org.junit.Assert.*;

public class ListMapTests extends MapTests
{
    @Override
    protected ListMap<Integer,Boolean> create()
    {
        return new ListMap<>();
    }

    @Test
    public void constructor()
    {
        final ListMap<Integer,Boolean> map = new ListMap<>();
        assertEquals(0, map.getCount());
    }
}
