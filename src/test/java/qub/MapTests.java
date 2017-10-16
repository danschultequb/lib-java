package qub;

import org.junit.Test;

import static org.junit.Assert.*;

public abstract class MapTests
{
    protected abstract Map<Integer,Boolean> create();

    @Test
    public void constructor()
    {
        final Map<Integer,Boolean> map = create();
        assertEquals(0, map.getCount());
    }

    @Test
    public void getWithNullNonExistingKey()
    {
        final Map<Integer,Boolean> map = create();
        assertNull(map.get(null));
    }

    @Test
    public void getWithNonNullNonExistingKey()
    {
        final Map<Integer,Boolean> map = create();
        assertNull(map.get(20));
    }

    @Test
    public void getWithNullExistingKey()
    {
        final Map<Integer,Boolean> map = create();
        map.set(null, true);
        assertTrue(map.get(null));
    }

    @Test
    public void getWithNonNullExistingKey()
    {
        final Map<Integer,Boolean> map = create();
        map.set(100, false);
        assertFalse(map.get(100));
    }

    @Test
    public void setWithNullNonExistingKeyAndNullValue()
    {
        final Map<Integer,Boolean> map = create();
        map.set(null, null);
        assertEquals(1, map.getCount());
        assertNull(map.get(null));
    }

    @Test
    public void setWithNullNonExistingKeyAndNonNullValue()
    {
        final Map<Integer,Boolean> map = create();
        map.set(null, false);
        assertEquals(1, map.getCount());
        assertEquals(false, map.get(null));
    }

    @Test
    public void setWithNullExistingKeyAndNullValue()
    {
        final Map<Integer,Boolean> map = create();
        map.set(null, true);

        map.set(null, null);
        assertEquals(1, map.getCount());
        assertNull(map.get(null));
    }

    @Test
    public void setWithNullExistingKeyAndNonNullValue()
    {
        final Map<Integer,Boolean> map = create();
        map.set(null, true);

        map.set(null, false);
        assertEquals(1, map.getCount());
        assertEquals(false, map.get(null));
    }

    @Test
    public void setWithNonNullNonExistingKeyAndNullValue()
    {
        final Map<Integer,Boolean> map = create();
        map.set(12, null);
        assertEquals(1, map.getCount());
        assertNull(map.get(12));
    }

    @Test
    public void setWithNonNullNonExistingKeyAndNonNullValue()
    {
        final Map<Integer,Boolean> map = create();
        map.set(13, false);
        assertEquals(1, map.getCount());
        assertEquals(false, map.get(13));
    }

    @Test
    public void setWithNonNullExistingKeyAndNullValue()
    {
        final Map<Integer,Boolean> map = create();
        map.set(14, true);

        map.set(14, null);
        assertEquals(1, map.getCount());
        assertNull(map.get(14));
    }

    @Test
    public void setWithNonNullExistingKeyAndNonNullValue()
    {
        final Map<Integer,Boolean> map = create();
        map.set(15, true);

        map.set(15, false);
        assertEquals(1, map.getCount());
        assertEquals(false, map.get(15));
    }

    @Test
    public void removeWithNullNonExistingKey()
    {
        final Map<Integer,Boolean> map = create();
        assertFalse(map.remove(null));
    }

    @Test
    public void removeWithNullExistingKey()
    {
        final Map<Integer,Boolean> map = create();
        map.set(null, true);

        assertTrue(map.remove(null));
        assertEquals(0, map.getCount());
    }

    @Test
    public void removeWithNonNullNonExistingKey()
    {
        final Map<Integer,Boolean> map = create();
        assertFalse(map.remove(100));
    }

    @Test
    public void removeWithNonNullExistingKey()
    {
        final Map<Integer,Boolean> map = create();
        map.set(101, false);

        assertTrue(map.remove(101));
        assertEquals(0, map.getCount());
    }

    @Test
    public void getKeysWithEmptyMap()
    {
        final Map<Integer,Boolean> map = create();
        final Iterable<Integer> keys = map.getKeys();
        assertNotNull(keys);
        assertEquals(0, keys.getCount());
    }

    @Test
    public void getKeysWithNonEmptyMap()
    {
        final Map<Integer,Boolean> map = create();
        map.set(0, false);
        map.set(1, true);
        final Iterable<Integer> keys = map.getKeys();
        assertNotNull(keys);
        assertEquals(2, keys.getCount());
        assertArrayEquals(new int[] { 0, 1 }, Array.toIntArray(keys));
    }

    @Test
    public void getValuesWithEmptyMap()
    {
        final Map<Integer,Boolean> map = create();
        final Iterable<Boolean> values = map.getValues();
        assertNotNull(values);
        assertEquals(0, values.getCount());
    }

    @Test
    public void getValuesWithNonEmptyMap()
    {
        final Map<Integer,Boolean> map = create();
        map.set(0, false);
        map.set(1, true);
        map.set(2, false);
        final Iterable<Boolean> values = map.getValues();
        assertNotNull(values);
        assertEquals(3, values.getCount());
        assertArrayEquals(new boolean[] { false, true, false }, Array.toBooleanArray(values));
    }

    @Test
    public void iterateWithEmptyMap()
    {
        final Map<Integer,Boolean> map = create();
        final Iterator<MapEntry<Integer,Boolean>> entries = map.iterate();
        assertNotNull(entries);
        assertEquals(0, entries.getCount());
    }

    @Test
    public void iterateWithNonEmptyMap()
    {
        final Map<Integer,Boolean> map = create();
        map.set(50, false);
        final Iterator<MapEntry<Integer,Boolean>> entries = map.iterate();
        assertNotNull(entries);
        assertEquals(1, entries.getCount());

        final MapEntry<Integer,Boolean> entry = map.iterate().first();
        assertEquals(50, entry.getKey().longValue());
        assertEquals(false, entry.getValue());
    }
}
