package qub;

/**
 * A list of bytes. Internally this uses a primitive byte[] to store bytes, so it should be more
 * efficient than using a generic List<Byte>.
 */
public class ByteList implements List<Byte>
{
    private byte[] bytes;
    private int count;

    public ByteList()
    {
        this(64);
    }

    public ByteList(int capacity)
    {
        PreCondition.assertGreaterThanOrEqualTo(capacity, 0, "capacity");

        this.bytes = new byte[capacity];
    }

    public ByteList(byte... values)
    {
        this.bytes = new byte[values == null ? 0 : values.length];
        addAll(values);
    }

    public ByteList(int... values)
    {
        this.bytes = new byte[values == null ? 0 : values.length];
        addAll(values);
    }

    public static ByteList empty()
    {
        return new ByteList();
    }

    public static ByteList createFromBytes(byte... values)
    {
        return new ByteList(values);
    }

    public static ByteList createFromBytes(int... values)
    {
        return new ByteList(values);
    }

    @Override
    public int getCount()
    {
        return count;
    }

    public void insert(int insertIndex, int value)
    {
        PreCondition.assertIndexAccess(insertIndex, count + 1, "insertIndex");
        PreCondition.assertByte(value, "value");

        insert(insertIndex, (byte)value);
    }

    public void insert(int insertIndex, byte value)
    {
        PreCondition.assertIndexAccess(insertIndex, count + 1, "insertIndex");

        if (count == bytes.length)
        {
            final byte[] newBytes = new byte[(bytes.length * 2) + 1];
            if (1 <= insertIndex)
            {
                Array.copy(bytes, 0, newBytes, 0, insertIndex);
            }
            if (insertIndex <= count - 1)
            {
                Array.copy(bytes, insertIndex, newBytes, insertIndex + 1, count - insertIndex);
            }
            bytes = newBytes;
        }
        else if (insertIndex < count)
        {
            Array.shiftRight(bytes, insertIndex, count - insertIndex);
        }
        bytes[insertIndex] = value;
        ++count;
    }

    @Override
    public void insert(int insertIndex, Byte value)
    {
        PreCondition.assertIndexAccess(insertIndex, count + 1, "insertIndex");
        PreCondition.assertNotNull(value, "value");

        insert(insertIndex, value.byteValue());
    }

    public void add(byte value)
    {
        insert(count, value);
    }

    public void addAll(byte... values)
    {
        if (values != null && values.length > 0)
        {
            for (final byte value : values)
            {
                add(value);
            }
        }
    }

    public void add(int value)
    {
        PreCondition.assertByte(value, "value");

        add((byte)value);
    }

    public void addAll(int... values)
    {
        if (values != null && values.length > 0)
        {
            for (final int value : values)
            {
                add(value);
            }
        }
    }

    @Override
    public Byte removeAt(int index)
    {
        PreCondition.assertIndexAccess(index, count, "index");

        final byte result = bytes[index];
        if (index < count - 1)
        {
            Array.shiftLeft(bytes, index, count - index - 1);
        }
        --count;

        return result;
    }

    public void set(int index, byte value)
    {
        PreCondition.assertIndexAccess(index, count, "index");

        bytes[index] = value;
    }

    public void set(int index, int value)
    {
        PreCondition.assertIndexAccess(index, count, "index");
        PreCondition.assertByte(value, "value");

        set(index, (byte)value);
    }

    @Override
    public void set(int index, Byte value)
    {
        PreCondition.assertIndexAccess(index, count, "index");
        PreCondition.assertNotNull(value, "value");

        set(index, value.byteValue());
    }

    @Override
    public Byte get(int index)
    {
        PreCondition.assertIndexAccess(index, count, "index");

        return bytes[index];
    }

    @Override
    public Iterator<Byte> iterate()
    {
        return new ByteArrayIterator(bytes, 0, count);
    }

    @Override
    public boolean equals(Object rhs)
    {
        return Iterable.equals(this, rhs);
    }

    @Override
    public String toString()
    {
        return Iterable.toString(this);
    }

    /**
     * Get a byte[] representation of this ByteList.
     * @return The byte[].
     */
    public byte[] toByteArray()
    {
        return Array.clone(bytes, 0, count);
    }
}
