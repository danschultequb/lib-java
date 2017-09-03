package qub;

import org.junit.Test;

import static org.junit.Assert.*;

public class ValueTests
{
    @Test
    public void constructorWithNoArguments()
    {
        final Value<Character> value = new Value<>();
        assertFalse(value.hasValue());
        assertNull(value.get());
    }

    @Test
    public void constructorWithNullArgument()
    {
        final Value<Character> value = new Value<>(null);
        assertTrue(value.hasValue());
        assertNull(value.get());
    }

    @Test
    public void constructorWithArgument()
    {
        final Value<Character> value = new Value<>('n');
        assertTrue(value.hasValue());
        assertEquals('n', value.get().charValue());
    }

    @Test
    public void setWithNull()
    {
        final Value<Character> value = new Value<>();
        value.set(null);
        assertTrue(value.hasValue());
        assertNull(value.get());
    }

    @Test
    public void setWithNonNull()
    {
        final Value<Character> value = new Value<>();
        value.set('z');
        assertTrue(value.hasValue());
        assertEquals('z', value.get().charValue());
    }

    @Test
    public void clear()
    {
        final Value<Character> value = new Value<>('v');
        value.clear();
        assertFalse(value.hasValue());
        assertNull(value.get());
    }
}
