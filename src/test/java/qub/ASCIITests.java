package qub;

import org.junit.Test;

import static org.junit.Assert.*;

public class ASCIITests
{
    @Test
    public void encodeCharacterWithNullOutput()
    {
        final ASCII ascii = new ASCII();
        final int encodedBytes = ascii.encode('a', null);
        assertEquals(1, encodedBytes);
    }

    @Test
    public void encodeCharacterWithEmptyOutput()
    {
        final ASCII ascii = new ASCII();
        final byte[] output = new byte[0];
        final int encodedBytes = ascii.encode('a', output);
        assertEquals(1, encodedBytes);
    }

    @Test
    public void encodeCharacterWithNonEmptyOutput()
    {
        final ASCII ascii = new ASCII();
        final byte[] output = new byte[10];
        final int encodedBytes = ascii.encode('a', output);
        assertEquals(1, encodedBytes);
        assertEquals(97, output[0]);
    }

    @Test
    public void encodeNullStringWithNullOutput()
    {
        final ASCII ascii = new ASCII();
        final int encodedBytes = ascii.encode(null, null);
        assertEquals(0, encodedBytes);
    }

    @Test
    public void encodeNullStringWithEmptyOutput()
    {
        final ASCII ascii = new ASCII();
        final byte[] output = new byte[0];
        final int encodedBytes = ascii.encode(null, output);
        assertEquals(0, encodedBytes);
    }

    @Test
    public void encodeNullStringWithNonEmptyOutput()
    {
        final ASCII ascii = new ASCII();
        final byte[] output = new byte[10];
        final int encodedBytes = ascii.encode(null, output);
        assertEquals(0, encodedBytes);
    }

    @Test
    public void encodeEmptyStringWithNullOutput()
    {
        final ASCII ascii = new ASCII();
        final int encodedBytes = ascii.encode("", null);
        assertEquals(0, encodedBytes);
    }

    @Test
    public void encodeEmptyStringWithEmptyOutput()
    {
        final ASCII ascii = new ASCII();
        final byte[] output = new byte[0];
        final int encodedBytes = ascii.encode("", output);
        assertEquals(0, encodedBytes);
    }

    @Test
    public void encodeEmptyStringWithNonEmptyOutput()
    {
        final ASCII ascii = new ASCII();
        final byte[] output = new byte[10];
        final int encodedBytes = ascii.encode("", output);
        assertEquals(0, encodedBytes);
    }

    @Test
    public void encodeNonEmptyStringWithNullOutput()
    {
        final ASCII ascii = new ASCII();
        final int encodedBytes = ascii.encode("abc", null);
        assertEquals(3, encodedBytes);
    }

    @Test
    public void encodeNonEmptyStringWithEmptyOutput()
    {
        final ASCII ascii = new ASCII();
        final byte[] output = new byte[0];
        final int encodedBytes = ascii.encode("abc", output);
        assertEquals(3, encodedBytes);
    }

    @Test
    public void encodeNonEmptyStringWithNonEmptyOutput()
    {
        final ASCII ascii = new ASCII();
        final byte[] output = new byte[10];
        final int encodedBytes = ascii.encode("abc", output);
        assertEquals(3, encodedBytes);
        assertArrayEquals(new byte[] { 97, 98, 99, 0, 0, 0, 0, 0, 0, 0 }, output);
    }

    @Test
    public void encodeNull()
    {
        final ASCII ascii = new ASCII();
        assertNull(ascii.encode(null));
    }

    @Test
    public void encodeEmpty()
    {
        final ASCII ascii = new ASCII();
        assertArrayEquals(new byte[0], ascii.encode(""));
    }

    @Test
    public void encodeNonEmpty()
    {
        final ASCII ascii = new ASCII();
        assertArrayEquals(new byte[] { 68, 97, 110 }, ascii.encode("Dan"));
    }

    @Test
    public void decodeNull()
    {
        final ASCII ascii = new ASCII();
        assertEquals(null, ascii.decode(null));
    }

    @Test
    public void decodeEmpty()
    {
        final ASCII ascii = new ASCII();
        assertArrayEquals(new char[0], ascii.decode(new byte[0]));
    }

    @Test
    public void decodeNonEmpty()
    {
        final ASCII ascii = new ASCII();
        assertArrayEquals(new char[] { 'a', 'b', 'c', 'd' }, ascii.decode(new byte[]{97 ,98, 99, 100}));
    }
}
