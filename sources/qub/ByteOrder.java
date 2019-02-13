package qub;

/**
 * A set of functions that can convert between a sequence of bytes to various multi-byte primitive
 * types, such as shorts, ints, longs, floats, and doubles.
 */
public abstract class ByteOrder
{
    public static final LittleEndian LittleEndian = new LittleEndian();

    public static final BigEndian BigEndian = new BigEndian();

    public static final BigEndian Network = ByteOrder.BigEndian;

    public static final BigEndian Java = ByteOrder.BigEndian;

    /**
     * Convert the provided big-endian short to a little-endian short.
     * @param bigEndianShort The big-endian short to convert to a little-endian short.
     * @return The converted little-endian short.
     */
    public static short toLittleEndianShort(short bigEndianShort)
    {
        return (short)((bigEndianShort & 0xff00) >>> (1 * Bytes.bitCount) |
                       (bigEndianShort & 0x00ff) << (1 * Bytes.bitCount));
    }

    /**
     * Convert the provided big-endian integer to a little-endian integer.
     * @param bigEndianInteger The big-endian integer to convert to a little-endian integer.
     * @return The converted little-endian integer.
     */
    public static int toLittleEndianInteger(int bigEndianInteger)
    {
        return (bigEndianInteger & 0xff000000) >>> (3 * Bytes.bitCount) |
               (bigEndianInteger & 0x00ff0000) >>> (1 * Bytes.bitCount) |
               (bigEndianInteger & 0x0000ff00) << (1 * Bytes.bitCount) |
               (bigEndianInteger & 0x000000ff) << (3 * Bytes.bitCount);
    }

    /**
     * Convert the provided big-endian long to a little-endian long.
     * @param bigEndianLong The big-endian long to convert to a little-endian long.
     * @return The converted little-endian long.
     */
    public static long toLittleEndianLong(long bigEndianLong)
    {
        return (bigEndianLong & 0xff00000000000000L) >>> (7 * Bytes.bitCount) |
               (bigEndianLong & 0x00ff000000000000L) >>> (5 * Bytes.bitCount) |
               (bigEndianLong & 0x0000ff0000000000L) >>> (3 * Bytes.bitCount) |
               (bigEndianLong & 0x000000ff00000000L) >>> (1 * Bytes.bitCount) |
               (bigEndianLong & 0x00000000ff000000L) << (1 * Bytes.bitCount) |
               (bigEndianLong & 0x0000000000ff0000L) << (3 * Bytes.bitCount) |
               (bigEndianLong & 0x000000000000ff00L) << (5 * Bytes.bitCount) |
               (bigEndianLong & 0x00000000000000ffL) << (7 * Bytes.bitCount);
    }

    public abstract byte[] encodeShort(short value);

    public abstract byte[] encodeInteger(int value);

    public abstract byte[] encodeLong(long value);

    public byte[] encodeFloat(float value)
    {
        final int intValue = Float.floatToRawIntBits(value);
        final byte[] result = encodeInteger(intValue);

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(Floats.byteCount, result.length, "result.length");

        return result;
    }

    public byte[] encodeDouble(double value)
    {
        final long longValue = Double.doubleToRawLongBits(value);
        final byte[] result = encodeLong(longValue);

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(Doubles.byteCount, result.length, "result.length");

        return result;
    }

    public byte[] encodeShortArray(short[] values)
    {
        PreCondition.assertNotNull(values, "values");

        final byte[] result = new byte[Shorts.byteCount * values.length];
        for(int i = 0; i < values.length; ++i)
        {
            final byte[] valueBytes = encodeShort(values[i]);
            Array.copy(valueBytes, 0, result, i * Shorts.byteCount, Shorts.byteCount);
        }

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(Shorts.byteCount * values.length, result.length, "result.length");

        return result;
    }

    public byte[] encodeIntegerArray(int[] values)
    {
        PreCondition.assertNotNull(values, "values");

        final byte[] result = new byte[Integers.byteCount * values.length];
        for(int i = 0; i < values.length; ++i)
        {
            final byte[] valueBytes = encodeInteger(values[i]);
            Array.copy(valueBytes, 0, result, i * Integers.byteCount, Integers.byteCount);
        }

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(Integers.byteCount * values.length, result.length, "result.length");

        return result;
    }

    public byte[] encodeLongArray(long[] values)
    {
        PreCondition.assertNotNull(values, "values");

        final byte[] result = new byte[Longs.byteCount * values.length];
        for(int i = 0; i < values.length; ++i)
        {
            final byte[] valueBytes = encodeLong(values[i]);
            Array.copy(valueBytes, 0, result, i * Longs.byteCount, Longs.byteCount);
        }

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(Longs.byteCount * values.length, result.length, "result.length");

        return result;
    }

    public byte[] encodeFloatArray(float[] values)
    {
        PreCondition.assertNotNull(values, "values");

        final byte[] result = new byte[Floats.byteCount * values.length];
        for(int i = 0; i < values.length; ++i)
        {
            final byte[] valueBytes = encodeFloat(values[i]);
            Array.copy(valueBytes, 0, result, i * Floats.byteCount, Floats.byteCount);
        }

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(Floats.byteCount * values.length, result.length, "result.length");

        return result;
    }

    public byte[] encodeDoubleArray(double[] values)
    {
        PreCondition.assertNotNull(values, "values");

        final byte[] result = new byte[Doubles.byteCount * values.length];
        for(int i = 0; i < values.length; ++i)
        {
            final byte[] valueBytes = encodeDouble(values[i]);
            Array.copy(valueBytes, 0, result, i * Doubles.byteCount, Doubles.byteCount);
        }

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(Doubles.byteCount * values.length, result.length, "result.length");

        return result;
    }

    public abstract short decodeAsShort(byte[] bytes);

    public abstract short decodeAsShort(Indexable<Byte> bytes);

    public abstract int decodeAsInteger(byte[] bytes);

    public abstract int decodeAsInteger(Indexable<Byte> bytes);

    public abstract long decodeAsLong(byte[] bytes);

    public abstract long decodeAsLong(Indexable<Byte> bytes);

    public float decodeAsFloat(byte[] bytes)
    {
        PreCondition.assertNotNull(bytes, "bytes");
        PreCondition.assertEqual(Floats.byteCount, bytes.length, "bytes.length");

        final int intBits = decodeAsInteger(bytes);
        final float result = Float.intBitsToFloat(intBits);

        return result;
    }

    public float decodeAsFloat(Indexable<Byte> bytes)
    {
        PreCondition.assertNotNull(bytes, "bytes");
        PreCondition.assertEqual(Floats.byteCount, bytes.getCount(), "bytes.getCount()");

        final int intBits = decodeAsInteger(bytes);
        final float result = Float.intBitsToFloat(intBits);

        return result;
    }

    public double decodeAsDouble(byte[] bytes)
    {
        PreCondition.assertNotNull(bytes, "bytes");
        PreCondition.assertEqual(Doubles.byteCount, bytes.length, "bytes.length");

        final long longBits = decodeAsLong(bytes);
        final double result = Double.longBitsToDouble(longBits);

        return result;
    }

    public double decodeAsDouble(Indexable<Byte> bytes)
    {
        PreCondition.assertNotNull(bytes, "bytes");
        PreCondition.assertEqual(Doubles.byteCount, bytes.getCount(), "bytes.getCount()");

        final long longBits = decodeAsLong(bytes);
        final double result = Double.longBitsToDouble(longBits);

        return result;
    }

    public short[] decodeAsShortArray(byte[] bytes)
    {
        PreCondition.assertNotNull(bytes, "bytes");
        PreCondition.assertEqual(0, bytes.length % Shorts.byteCount, "bytes.length % Shorts.byteCount");

        final short[] result = new short[bytes.length / Shorts.byteCount];
        for(int i = 0; i < result.length; ++i)
        {
            result[i] = decodeAsShort(Arrays.createFrom(bytes, i * Shorts.byteCount, Shorts.byteCount));
        }

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(bytes.length / Shorts.byteCount, result.length, "result.length");

        return result;
    }

    public short[] decodeAsShortArray(Indexable<Byte> bytes)
    {
        PreCondition.assertNotNull(bytes, "bytes");
        PreCondition.assertEqual(0, bytes.getCount() % Shorts.byteCount, "bytes.getCount() % Shorts.byteCount");

        final short[] result = new short[bytes.getCount() / Shorts.byteCount];
        for(int i = 0; i < result.length; ++i)
        {
            result[i] = decodeAsShort(bytes.getRange(i * Shorts.byteCount, Shorts.byteCount));
        }

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(bytes.getCount() / Shorts.byteCount, result.length, "result.length");

        return result;
    }

    public int[] decodeAsIntegerArray(byte[] bytes)
    {
        PreCondition.assertNotNull(bytes, "bytes");
        PreCondition.assertEqual(0, bytes.length % Integers.byteCount, "bytes.length % Integers.byteCount");

        final int[] result = new int[bytes.length / Integers.byteCount];
        for(int i = 0; i < result.length; ++i)
        {
            result[i] = decodeAsInteger(Arrays.createFrom(bytes, i * Integers.byteCount, Integers.byteCount));
        }

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(bytes.length / Integers.byteCount, result.length, "result.length");

        return result;
    }

    public int[] decodeAsIntegerArray(Indexable<Byte> bytes)
    {
        PreCondition.assertNotNull(bytes, "bytes");
        PreCondition.assertEqual(0, bytes.getCount() % Integers.byteCount, "bytes.getCount() % Integers.byteCount");

        final int[] result = new int[bytes.getCount() / Integers.byteCount];
        for(int i = 0; i < result.length; ++i)
        {
            result[i] = decodeAsInteger(bytes.getRange(i * Integers.byteCount, Integers.byteCount));
        }

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(bytes.getCount() / Integers.byteCount, result.length, "result.length");

        return result;
    }

    public long[] decodeAsLongArray(byte[] bytes)
    {
        PreCondition.assertNotNull(bytes, "bytes");
        PreCondition.assertEqual(0, bytes.length % Longs.byteCount, "bytes.length % Longs.byteCount");

        final long[] result = new long[bytes.length / Longs.byteCount];
        for(int i = 0; i < result.length; ++i)
        {
            result[i] = decodeAsLong(Arrays.createFrom(bytes, i * Longs.byteCount, Longs.byteCount));
        }

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(bytes.length / Longs.byteCount, result.length, "result.length");

        return result;
    }

    public long[] decodeAsLongArray(Indexable<Byte> bytes)
    {
        PreCondition.assertNotNull(bytes, "bytes");
        PreCondition.assertEqual(0, bytes.getCount() % Longs.byteCount, "bytes.getCount() % Longs.byteCount");

        final long[] result = new long[bytes.getCount() / Longs.byteCount];
        for(int i = 0; i < result.length; ++i)
        {
            result[i] = decodeAsLong(bytes.getRange(i * Longs.byteCount, Longs.byteCount));
        }

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(bytes.getCount() / Longs.byteCount, result.length, "result.length");

        return result;
    }

    public float[] decodeAsFloatArray(byte[] bytes)
    {
        PreCondition.assertNotNull(bytes, "bytes");
        PreCondition.assertEqual(0, bytes.length % Floats.byteCount, "bytes.length % Float.byteCount");

        final float[] result = new float[bytes.length / Floats.byteCount];
        for(int i = 0; i < result.length; ++i)
        {
            result[i] = decodeAsFloat(Arrays.createFrom(bytes, i * Floats.byteCount, Floats.byteCount));
        }

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(bytes.length / Floats.byteCount, result.length, "result.length");

        return result;
    }

    public float[] decodeAsFloatArray(Indexable<Byte> bytes)
    {
        PreCondition.assertNotNull(bytes, "bytes");
        PreCondition.assertEqual(0, bytes.getCount() % Floats.byteCount, "bytes.getCount() % Floats.byteCount");

        final float[] result = new float[bytes.getCount() / Floats.byteCount];
        for(int i = 0; i < result.length; ++i)
        {
            result[i] = decodeAsFloat(bytes.getRange(i * Floats.byteCount, Floats.byteCount));
        }

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(bytes.getCount() / Floats.byteCount, result.length, "result.length");

        return result;
    }

    public double[] decodeAsDoubleArray(byte[] bytes)
    {
        PreCondition.assertNotNull(bytes, "bytes");
        PreCondition.assertEqual(0, bytes.length % Doubles.byteCount, "bytes.length % Doubles.byteCount");

        final double[] result = new double[bytes.length / Doubles.byteCount];
        for(int i = 0; i < result.length; ++i)
        {
            result[i] = decodeAsDouble(Arrays.createFrom(bytes, i * Doubles.byteCount, Doubles.byteCount));
        }

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(bytes.length / Doubles.byteCount, result.length, "result.length");

        return result;
    }

    public double[] decodeAsDoubleArray(Indexable<Byte> bytes)
    {
        PreCondition.assertNotNull(bytes, "bytes");
        PreCondition.assertEqual(0, bytes.getCount() % Doubles.byteCount, "bytes.getCount() % Doubles.byteCount");

        final double[] result = new double[bytes.getCount() / Doubles.byteCount];
        for(int i = 0; i < result.length; ++i)
        {
            result[i] = decodeAsDouble(bytes.getRange(i * Doubles.byteCount, Doubles.byteCount));
        }

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(bytes.getCount() / Doubles.byteCount, result.length, "result.length");

        return result;
    }
}
