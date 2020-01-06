package qub;

/**
 * A set of functions that can convert between a sequence of bytes to various multi-byte primitive
 * types, such as shorts, ints, longs, floats, and doubles.
 */
public interface ByteOrder
{
    LittleEndian LittleEndian = new LittleEndian();

    BigEndian BigEndian = new BigEndian();

    BigEndian Network = ByteOrder.BigEndian;

    BigEndian Java = ByteOrder.BigEndian;

    /**
     * Convert the provided big-endian short to a little-endian short.
     * @param bigEndianShort The big-endian short to convert to a little-endian short.
     * @return The converted little-endian short.
     */
    static short toLittleEndianShort(short bigEndianShort)
    {
        return (short)((bigEndianShort & 0xff00) >>> (1 * Bytes.bitCount) |
                       (bigEndianShort & 0x00ff) << (1 * Bytes.bitCount));
    }

    /**
     * Convert the provided big-endian integer to a little-endian integer.
     * @param bigEndianInteger The big-endian integer to convert to a little-endian integer.
     * @return The converted little-endian integer.
     */
    static int toLittleEndianInteger(int bigEndianInteger)
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
    static long toLittleEndianLong(long bigEndianLong)
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

    byte[] encodeShort(short value);

    byte[] encodeInteger(int value);

    byte[] encodeLong(long value);

    default byte[] encodeFloat(float value)
    {
        final int intValue = java.lang.Float.floatToRawIntBits(value);
        final byte[] result = this.encodeInteger(intValue);

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(Floats.byteCount, result.length, "result.length");

        return result;
    }

    default byte[] encodeDouble(double value)
    {
        final long longValue = java.lang.Double.doubleToRawLongBits(value);
        final byte[] result = this.encodeLong(longValue);

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(Doubles.byteCount, result.length, "result.length");

        return result;
    }

    default byte[] encodeShortArray(short[] values)
    {
        PreCondition.assertNotNull(values, "values");

        final byte[] result = new byte[Shorts.byteCount * values.length];
        for(int i = 0; i < values.length; ++i)
        {
            final byte[] valueBytes = this.encodeShort(values[i]);
            Array.copy(valueBytes, 0, result, i * Shorts.byteCount, Shorts.byteCount);
        }

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(Shorts.byteCount * values.length, result.length, "result.length");

        return result;
    }

    default byte[] encodeIntegerArray(int[] values)
    {
        PreCondition.assertNotNull(values, "values");

        final byte[] result = new byte[Integers.byteCount * values.length];
        for(int i = 0; i < values.length; ++i)
        {
            final byte[] valueBytes = this.encodeInteger(values[i]);
            Array.copy(valueBytes, 0, result, i * Integers.byteCount, Integers.byteCount);
        }

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(Integers.byteCount * values.length, result.length, "result.length");

        return result;
    }

    default byte[] encodeLongArray(long[] values)
    {
        PreCondition.assertNotNull(values, "values");

        final byte[] result = new byte[Longs.byteCount * values.length];
        for(int i = 0; i < values.length; ++i)
        {
            final byte[] valueBytes = this.encodeLong(values[i]);
            Array.copy(valueBytes, 0, result, i * Longs.byteCount, Longs.byteCount);
        }

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(Longs.byteCount * values.length, result.length, "result.length");

        return result;
    }

    default byte[] encodeFloatArray(float[] values)
    {
        PreCondition.assertNotNull(values, "values");

        final byte[] result = new byte[Floats.byteCount * values.length];
        for(int i = 0; i < values.length; ++i)
        {
            final byte[] valueBytes = this.encodeFloat(values[i]);
            Array.copy(valueBytes, 0, result, i * Floats.byteCount, Floats.byteCount);
        }

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(Floats.byteCount * values.length, result.length, "result.length");

        return result;
    }

    default byte[] encodeDoubleArray(double[] values)
    {
        PreCondition.assertNotNull(values, "values");

        final byte[] result = new byte[Doubles.byteCount * values.length];
        for(int i = 0; i < values.length; ++i)
        {
            final byte[] valueBytes = this.encodeDouble(values[i]);
            Array.copy(valueBytes, 0, result, i * Doubles.byteCount, Doubles.byteCount);
        }

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(Doubles.byteCount * values.length, result.length, "result.length");

        return result;
    }

    short decodeAsShort(byte[] bytes);

    short decodeAsShort(Indexable<Byte> bytes);

    int decodeAsInteger(byte[] bytes);

    int decodeAsInteger(Indexable<Byte> bytes);

    long decodeAsLong(byte[] bytes);

    long decodeAsLong(Indexable<Byte> bytes);

    default float decodeAsFloat(byte[] bytes)
    {
        PreCondition.assertNotNull(bytes, "bytes");
        PreCondition.assertEqual(Floats.byteCount, bytes.length, "bytes.length");

        final int intBits = this.decodeAsInteger(bytes);
        final float result = Float.intBitsToFloat(intBits);

        return result;
    }

    default float decodeAsFloat(Indexable<Byte> bytes)
    {
        PreCondition.assertNotNull(bytes, "bytes");
        PreCondition.assertEqual(Floats.byteCount, bytes.getCount(), "bytes.getCount()");

        final int intBits = this.decodeAsInteger(bytes);
        final float result = Float.intBitsToFloat(intBits);

        return result;
    }

    default double decodeAsDouble(byte[] bytes)
    {
        PreCondition.assertNotNull(bytes, "bytes");
        PreCondition.assertEqual(Doubles.byteCount, bytes.length, "bytes.length");

        final long longBits = this.decodeAsLong(bytes);
        final double result = Double.longBitsToDouble(longBits);

        return result;
    }

    default double decodeAsDouble(Indexable<Byte> bytes)
    {
        PreCondition.assertNotNull(bytes, "bytes");
        PreCondition.assertEqual(Doubles.byteCount, bytes.getCount(), "bytes.getCount()");

        final long longBits = this.decodeAsLong(bytes);
        final double result = Double.longBitsToDouble(longBits);

        return result;
    }

    default short[] decodeAsShortArray(byte[] bytes)
    {
        PreCondition.assertNotNull(bytes, "bytes");
        PreCondition.assertEqual(0, bytes.length % Shorts.byteCount, "bytes.length % Shorts.byteCount");

        final short[] result = new short[bytes.length / Shorts.byteCount];
        for(int i = 0; i < result.length; ++i)
        {
            result[i] = this.decodeAsShort(ByteArray.create(bytes, i * Shorts.byteCount, Shorts.byteCount));
        }

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(bytes.length / Shorts.byteCount, result.length, "result.length");

        return result;
    }

    default short[] decodeAsShortArray(Indexable<Byte> bytes)
    {
        PreCondition.assertNotNull(bytes, "bytes");
        PreCondition.assertEqual(0, bytes.getCount() % Shorts.byteCount, "bytes.getCount() % Shorts.byteCount");

        final short[] result = new short[bytes.getCount() / Shorts.byteCount];
        for(int i = 0; i < result.length; ++i)
        {
            result[i] = this.decodeAsShort(bytes.getRange(i * Shorts.byteCount, Shorts.byteCount));
        }

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(bytes.getCount() / Shorts.byteCount, result.length, "result.length");

        return result;
    }

    default int[] decodeAsIntegerArray(byte[] bytes)
    {
        PreCondition.assertNotNull(bytes, "bytes");
        PreCondition.assertEqual(0, bytes.length % Integers.byteCount, "bytes.length % Integers.byteCount");

        final int[] result = new int[bytes.length / Integers.byteCount];
        for(int i = 0; i < result.length; ++i)
        {
            result[i] = this.decodeAsInteger(ByteArray.create(bytes, i * Integers.byteCount, Integers.byteCount));
        }

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(bytes.length / Integers.byteCount, result.length, "result.length");

        return result;
    }

    default int[] decodeAsIntegerArray(Indexable<Byte> bytes)
    {
        PreCondition.assertNotNull(bytes, "bytes");
        PreCondition.assertEqual(0, bytes.getCount() % Integers.byteCount, "bytes.getCount() % Integers.byteCount");

        final int[] result = new int[bytes.getCount() / Integers.byteCount];
        for(int i = 0; i < result.length; ++i)
        {
            result[i] = this.decodeAsInteger(bytes.getRange(i * Integers.byteCount, Integers.byteCount));
        }

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(bytes.getCount() / Integers.byteCount, result.length, "result.length");

        return result;
    }

    default long[] decodeAsLongArray(byte[] bytes)
    {
        PreCondition.assertNotNull(bytes, "bytes");
        PreCondition.assertEqual(0, bytes.length % Longs.byteCount, "bytes.length % Longs.byteCount");

        final long[] result = new long[bytes.length / Longs.byteCount];
        for(int i = 0; i < result.length; ++i)
        {
            result[i] = this.decodeAsLong(ByteArray.create(bytes, i * Longs.byteCount, Longs.byteCount));
        }

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(bytes.length / Longs.byteCount, result.length, "result.length");

        return result;
    }

    default long[] decodeAsLongArray(Indexable<Byte> bytes)
    {
        PreCondition.assertNotNull(bytes, "bytes");
        PreCondition.assertEqual(0, bytes.getCount() % Longs.byteCount, "bytes.getCount() % Longs.byteCount");

        final long[] result = new long[bytes.getCount() / Longs.byteCount];
        for(int i = 0; i < result.length; ++i)
        {
            result[i] = this.decodeAsLong(bytes.getRange(i * Longs.byteCount, Longs.byteCount));
        }

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(bytes.getCount() / Longs.byteCount, result.length, "result.length");

        return result;
    }

    default float[] decodeAsFloatArray(byte[] bytes)
    {
        PreCondition.assertNotNull(bytes, "bytes");
        PreCondition.assertEqual(0, bytes.length % Floats.byteCount, "bytes.length % Float.byteCount");

        final float[] result = new float[bytes.length / Floats.byteCount];
        for(int i = 0; i < result.length; ++i)
        {
            result[i] = this.decodeAsFloat(ByteArray.create(bytes, i * Floats.byteCount, Floats.byteCount));
        }

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(bytes.length / Floats.byteCount, result.length, "result.length");

        return result;
    }

    default float[] decodeAsFloatArray(Indexable<Byte> bytes)
    {
        PreCondition.assertNotNull(bytes, "bytes");
        PreCondition.assertEqual(0, bytes.getCount() % Floats.byteCount, "bytes.getCount() % Floats.byteCount");

        final float[] result = new float[bytes.getCount() / Floats.byteCount];
        for(int i = 0; i < result.length; ++i)
        {
            result[i] = this.decodeAsFloat(bytes.getRange(i * Floats.byteCount, Floats.byteCount));
        }

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(bytes.getCount() / Floats.byteCount, result.length, "result.length");

        return result;
    }

    default double[] decodeAsDoubleArray(byte[] bytes)
    {
        PreCondition.assertNotNull(bytes, "bytes");
        PreCondition.assertEqual(0, bytes.length % Doubles.byteCount, "bytes.length % Doubles.byteCount");

        final double[] result = new double[bytes.length / Doubles.byteCount];
        for(int i = 0; i < result.length; ++i)
        {
            result[i] = this.decodeAsDouble(ByteArray.create(bytes, i * Doubles.byteCount, Doubles.byteCount));
        }

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(bytes.length / Doubles.byteCount, result.length, "result.length");

        return result;
    }

    default double[] decodeAsDoubleArray(Indexable<Byte> bytes)
    {
        PreCondition.assertNotNull(bytes, "bytes");
        PreCondition.assertEqual(0, bytes.getCount() % Doubles.byteCount, "bytes.getCount() % Doubles.byteCount");

        final double[] result = new double[bytes.getCount() / Doubles.byteCount];
        for(int i = 0; i < result.length; ++i)
        {
            result[i] = this.decodeAsDouble(bytes.getRange(i * Doubles.byteCount, Doubles.byteCount));
        }

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(bytes.getCount() / Doubles.byteCount, result.length, "result.length");

        return result;
    }
}
