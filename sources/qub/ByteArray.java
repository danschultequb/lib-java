package qub;

public class ByteArray extends IndexableBase<Byte> implements MutableIndexable<Byte>
{
    private final byte[] bytes;
    private final int startIndex;
    private final int length;

    public ByteArray(int byteCount)
    {
        PreCondition.assertGreaterThanOrEqualTo(byteCount, 0, "byteCount");

        this.bytes = new byte[byteCount];
        this.startIndex = 0;
        this.length = byteCount;
    }

    public ByteArray(byte[] bytes, int startIndex, int length)
    {
        PreCondition.assertNotNull(bytes, "byteArray");
        PreCondition.assertBetween(0, startIndex, bytes.length - 1, "startIndex");
        PreCondition.assertBetween(1, length, bytes.length - startIndex, "length");

        this.bytes = bytes;
        this.startIndex = startIndex;
        this.length = length;
    }

    @Override
    public int getCount()
    {
        return length;
    }

    public void set(int index, byte value)
    {
        PreCondition.assertBetween(0, index, getCount() - 1, "index");

        bytes[startIndex + index] = value;
    }

    @Override
    public void set(int index, Byte value)
    {
        PreCondition.assertBetween(0, index, getCount() - 1, "index");
        PreCondition.assertNotNull(value, "value");

        bytes[startIndex + index] = value;
    }

    @Override
    public Byte get(int index)
    {
        PreCondition.assertBetween(0, index, getCount() - 1, "index");

        return bytes[startIndex + index];
    }

    @Override
    public Iterator<Byte> iterate()
    {
        return Array.fromValues(bytes).iterate();
    }

    public ByteArray getRange(int startIndex)
    {
        PreCondition.assertBetween(0, startIndex, getCount() - 1, "startIndex");

        return getRange(startIndex, getCount() - startIndex);
    }

    public ByteArray getRange(int startIndex, int length)
    {
        PreCondition.assertBetween(0, startIndex, getCount() - 1, "startIndex");
        PreCondition.assertBetween(1, length, getCount() - startIndex, "length");

        ByteArray result;
        if (startIndex == 0 && this.length == length)
        {
            result = this;
        }
        else
        {
            result = new ByteArray(bytes, this.startIndex + startIndex, length);
        }

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(length, result.getCount(), "result.getCount()");

        return result;
    }

    public static ByteArray getRange(byte[] byteArray, int startIndex, int length)
    {
        PreCondition.assertNotNull(byteArray, "byteArray");
        PreCondition.assertBetween(0, startIndex, byteArray.length - 1, "startIndex");
        PreCondition.assertBetween(1, length, byteArray.length - startIndex, "length");

        return new ByteArray(byteArray, startIndex, length);
    }
}
