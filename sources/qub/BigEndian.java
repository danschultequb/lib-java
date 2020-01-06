package qub;

public class BigEndian implements ByteOrder
{
    @Override
    public byte[] encodeShort(short value)
    {
        final byte[] result = new byte[]
        {
            (byte)(value >>> 1 * Bytes.bitCount),
            (byte)(value >>> 0 * Bytes.bitCount),
        };

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(Shorts.byteCount, result.length, "result.length");

        return result;
    }

    @Override
    public byte[] encodeInteger(int value)
    {
        final byte[] result = new byte[]
        {
            (byte)(value >>> 3 * Bytes.bitCount),
            (byte)(value >>> 2 * Bytes.bitCount),
            (byte)(value >>> 1 * Bytes.bitCount),
            (byte)(value >>> 0 * Bytes.bitCount),
        };

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(Integers.byteCount, result.length, "result.length");

        return result;
    }

    @Override
    public byte[] encodeLong(long value)
    {
        final byte[] result = new byte[]
        {
            (byte)(value >>> 7 * Bytes.bitCount),
            (byte)(value >>> 6 * Bytes.bitCount),
            (byte)(value >>> 5 * Bytes.bitCount),
            (byte)(value >>> 4 * Bytes.bitCount),
            (byte)(value >>> 3 * Bytes.bitCount),
            (byte)(value >>> 2 * Bytes.bitCount),
            (byte)(value >>> 1 * Bytes.bitCount),
            (byte)(value >>> 0 * Bytes.bitCount),
        };

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(Longs.byteCount, result.length, "result.length");

        return result;
    }

    @Override
    public short decodeAsShort(byte[] bytes)
    {
        PreCondition.assertNotNull(bytes, "bytes");
        PreCondition.assertEqual(Shorts.byteCount, bytes.length, "bytes.length");

        return (short)((Bytes.toUnsignedInt(bytes[0]) << (1 * Bytes.bitCount)) |
                       (Bytes.toUnsignedInt(bytes[1]) << (0 * Bytes.bitCount)));
    }

    @Override
    public short decodeAsShort(Indexable<Byte> bytes)
    {
        PreCondition.assertNotNull(bytes, "bytes");
        PreCondition.assertEqual(Shorts.byteCount, bytes.getCount(), "bytes.getCount()");

        return (short)((Bytes.toUnsignedInt(bytes.get(0)) << (1 * Bytes.bitCount)) |
                       (Bytes.toUnsignedInt(bytes.get(1)) << (0 * Bytes.bitCount)));
    }

    @Override
    public int decodeAsInteger(byte[] bytes)
    {
        PreCondition.assertNotNull(bytes, "bytes");
        PreCondition.assertEqual(Integers.byteCount, bytes.length, "bytes.length");

        return (Bytes.toUnsignedInt(bytes[0]) << (3 * Bytes.bitCount)) |
               (Bytes.toUnsignedInt(bytes[1]) << (2 * Bytes.bitCount)) |
               (Bytes.toUnsignedInt(bytes[2]) << (1 * Bytes.bitCount)) |
               (Bytes.toUnsignedInt(bytes[3]) << (0 * Bytes.bitCount));
    }

    @Override
    public int decodeAsInteger(Indexable<Byte> bytes)
    {
        PreCondition.assertNotNull(bytes, "bytes");
        PreCondition.assertEqual(Integers.byteCount, bytes.getCount(), "bytes.getCount()");

        return (Bytes.toUnsignedInt(bytes.get(0)) << (3 * Bytes.bitCount)) |
               (Bytes.toUnsignedInt(bytes.get(1)) << (2 * Bytes.bitCount)) |
               (Bytes.toUnsignedInt(bytes.get(2)) << (1 * Bytes.bitCount)) |
               (Bytes.toUnsignedInt(bytes.get(3)) << (0 * Bytes.bitCount));
    }

    @Override
    public long decodeAsLong(byte[] bytes)
    {
        PreCondition.assertNotNull(bytes, "bytes");
        PreCondition.assertEqual(Longs.byteCount, bytes.length, "bytes.length");

        return (Bytes.toUnsignedLong(bytes[0]) << (7 * Bytes.bitCount)) |
               (Bytes.toUnsignedLong(bytes[1]) << (6 * Bytes.bitCount)) |
               (Bytes.toUnsignedLong(bytes[2]) << (5 * Bytes.bitCount)) |
               (Bytes.toUnsignedLong(bytes[3]) << (4 * Bytes.bitCount)) |
               (Bytes.toUnsignedLong(bytes[4]) << (3 * Bytes.bitCount)) |
               (Bytes.toUnsignedLong(bytes[5]) << (2 * Bytes.bitCount)) |
               (Bytes.toUnsignedLong(bytes[6]) << (1 * Bytes.bitCount)) |
               (Bytes.toUnsignedLong(bytes[7]) << (0 * Bytes.bitCount));
    }

    @Override
    public long decodeAsLong(Indexable<Byte> bytes)
    {
        PreCondition.assertNotNull(bytes, "bytes");
        PreCondition.assertEqual(Longs.byteCount, bytes.getCount(), "bytes.getCount()");

        return (Bytes.toUnsignedLong(bytes.get(0)) << (7 * Bytes.bitCount)) |
               (Bytes.toUnsignedLong(bytes.get(1)) << (6 * Bytes.bitCount)) |
               (Bytes.toUnsignedLong(bytes.get(2)) << (5 * Bytes.bitCount)) |
               (Bytes.toUnsignedLong(bytes.get(3)) << (4 * Bytes.bitCount)) |
               (Bytes.toUnsignedLong(bytes.get(4)) << (3 * Bytes.bitCount)) |
               (Bytes.toUnsignedLong(bytes.get(5)) << (2 * Bytes.bitCount)) |
               (Bytes.toUnsignedLong(bytes.get(6)) << (1 * Bytes.bitCount)) |
               (Bytes.toUnsignedLong(bytes.get(7)) << (0 * Bytes.bitCount));
    }
}
