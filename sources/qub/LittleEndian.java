package qub;

public class LittleEndian extends ByteOrder
{
    @Override
    public byte[] encodeShort(short value)
    {
        final byte[] result = new byte[]
        {
            (byte)(value >>> 0 * Bytes.bitCount),
            (byte)(value >>> 1 * Bytes.bitCount),
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
            (byte)(value >>> 0 * Bytes.bitCount),
            (byte)(value >>> 1 * Bytes.bitCount),
            (byte)(value >>> 2 * Bytes.bitCount),
            (byte)(value >>> 3 * Bytes.bitCount),
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
            (byte)(value >>> 0 * Bytes.bitCount),
            (byte)(value >>> 1 * Bytes.bitCount),
            (byte)(value >>> 2 * Bytes.bitCount),
            (byte)(value >>> 3 * Bytes.bitCount),
            (byte)(value >>> 4 * Bytes.bitCount),
            (byte)(value >>> 5 * Bytes.bitCount),
            (byte)(value >>> 6 * Bytes.bitCount),
            (byte)(value >>> 7 * Bytes.bitCount),
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

        return (short)((Bytes.toUnsignedInt(bytes[0]) << (0 * Bytes.bitCount)) |
                       (Bytes.toUnsignedInt(bytes[1]) << (1 * Bytes.bitCount)));
    }

    @Override
    public short decodeAsShort(Indexable<Byte> bytes)
    {
        PreCondition.assertNotNull(bytes, "bytes");
        PreCondition.assertEqual(Shorts.byteCount, bytes.getCount(), "bytes.getCount()");

        return (short)((Bytes.toUnsignedInt(bytes.get(0)) << (0 * Bytes.bitCount)) | 
                       (Bytes.toUnsignedInt(bytes.get(1)) << (1 * Bytes.bitCount)));
    }

    @Override
    public int decodeAsInt(byte[] bytes)
    {
        PreCondition.assertNotNull(bytes, "bytes");
        PreCondition.assertEqual(Integers.byteCount, bytes.length, "bytes.length");

        final int result = (Bytes.toUnsignedInt(bytes[0]) <<  0) |
                               (Bytes.toUnsignedInt(bytes[1]) <<  8) |
                               (Bytes.toUnsignedInt(bytes[2]) << 16) |
                               (Bytes.toUnsignedInt(bytes[3]) << 24);

        return result;
    }

    @Override
    public int decodeAsInt(Indexable<Byte> bytes)
    {
        PreCondition.assertNotNull(bytes, "bytes");
        PreCondition.assertEqual(Integers.byteCount, bytes.getCount(), "bytes.getCount()");

        return (Bytes.toUnsignedInt(bytes.get(0)) <<  0) |
               (Bytes.toUnsignedInt(bytes.get(1)) <<  8) |
               (Bytes.toUnsignedInt(bytes.get(2)) << 16) |
               (Bytes.toUnsignedInt(bytes.get(3)) << 24);
    }

    @Override
    public long decodeAsLong(byte[] bytes)
    {
        PreCondition.assertNotNull(bytes, "bytes");
        PreCondition.assertEqual(Longs.byteCount, bytes.length, "bytes.length");

        return (Bytes.toUnsignedLong(bytes[0]) <<  0) |
               (Bytes.toUnsignedLong(bytes[1]) <<  8) |
               (Bytes.toUnsignedLong(bytes[2]) << 16) |
               (Bytes.toUnsignedLong(bytes[3]) << 24) |
               (Bytes.toUnsignedLong(bytes[4]) << 32) |
               (Bytes.toUnsignedLong(bytes[5]) << 40) |
               (Bytes.toUnsignedLong(bytes[6]) << 48) |
               (Bytes.toUnsignedLong(bytes[7]) << 56);
    }

    @Override
    public long decodeAsLong(Indexable<Byte> bytes)
    {
        PreCondition.assertNotNull(bytes, "bytes");
        PreCondition.assertEqual(Longs.byteCount, bytes.getCount(), "bytes.getCount()");

        return (Bytes.toUnsignedLong(bytes.get(0)) <<  0) |
               (Bytes.toUnsignedLong(bytes.get(1)) <<  8) |
               (Bytes.toUnsignedLong(bytes.get(2)) << 16) |
               (Bytes.toUnsignedLong(bytes.get(3)) << 24) |
               (Bytes.toUnsignedLong(bytes.get(4)) << 32) |
               (Bytes.toUnsignedLong(bytes.get(5)) << 40) |
               (Bytes.toUnsignedLong(bytes.get(6)) << 48) |
               (Bytes.toUnsignedLong(bytes.get(7)) << 56);
   }
}
