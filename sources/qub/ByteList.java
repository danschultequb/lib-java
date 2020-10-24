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
    public boolean any()
    {
        return count > 0;
    }

    @Override
    public int getCount()
    {
        return count;
    }

    public ByteList insert(int insertIndex, int value)
    {
        PreCondition.assertIndexAccess(insertIndex, count + 1, "insertIndex");
        PreCondition.assertByte(value, "value");

        return this.insert(insertIndex, (byte)value);
    }

    public ByteList insert(int insertIndex, byte value)
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

        return this;
    }

    @Override
    public ByteList insert(int insertIndex, Byte value)
    {
        PreCondition.assertIndexAccess(insertIndex, count + 1, "insertIndex");
        PreCondition.assertNotNull(value, "value");

        return this.insert(insertIndex, value.byteValue());
    }

    public ByteList add(byte value)
    {
        return this.insert(count, value);
    }

    public ByteList addAll(byte... values)
    {
        if (values != null && values.length > 0)
        {
            for (final byte value : values)
            {
                this.add(value);
            }
        }
        return this;
    }

    public ByteList add(int value)
    {
        PreCondition.assertByte(value, "value");

        return this.add((byte)value);
    }

    public ByteList addAll(int... values)
    {
        if (values != null && values.length > 0)
        {
            for (final int value : values)
            {
                this.add(value);
            }
        }
        return this;
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

    /**
     * Remove the first bytes from this ByteList as a ByteArray.
     * @param valuesToRemove The number of bytes to remove from this ByteList.
     * @return The removed bytes.
     */
    public ByteArray removeFirstBytes(int valuesToRemove)
    {
        PreCondition.assertNotNullAndNotEmpty(this, "list");
        PreCondition.assertLength(valuesToRemove, 0, this.getCount());

        final byte[] bytes = new byte[valuesToRemove];
        this.removeFirstBytes(bytes);
        return ByteArray.create(bytes);
    }

    /**
     * Remove the first bytes from this ByteList and put them into the outputBytes array. There must
     * be enough bytes in this list to fill the provided array.
     * @param outputBytes The array to put the bytes into.
     */
    public void removeFirstBytes(byte[] outputBytes)
    {
        PreCondition.assertNotNullAndNotEmpty(outputBytes, "outputBytes");
        PreCondition.assertNotNullAndNotEmpty(this, "list");
        PreCondition.assertLength(outputBytes.length, 0, this.getCount());

        this.removeFirstBytes(outputBytes, 0, outputBytes.length);
    }

    /**
     * Remove the first bytes from this ByteList and put them into the outputBytes array.
     * @param outputBytes The array to put the bytes into.
     * @param startIndex The start index in the array to start putting the bytes to.
     * @param length The number of bytes to remove from the list and put into the array.
     */
    public void removeFirstBytes(byte[] outputBytes, int startIndex, int length)
    {
        PreCondition.assertNotNullAndNotEmpty(outputBytes, "outputBytes");
        PreCondition.assertStartIndex(startIndex, outputBytes.length);
        PreCondition.assertLength(length, startIndex, outputBytes.length);
        PreCondition.assertNotNullAndNotEmpty(this, "list");
        PreCondition.assertLength(length, 0, this.getCount());

        Array.copy(bytes, 0, outputBytes, startIndex, length);
        final int bytesInList = this.getCount();
        if (length < bytesInList)
        {
            Array.copy(bytes, length, this.bytes, 0, bytesInList - length);
        }
        count -= length;
    }

    @Override
    public Iterable<Byte> removeFirst(int valuesToRemove)
    {
        return removeFirstBytes(valuesToRemove);
    }

    public ByteList set(int index, byte value)
    {
        PreCondition.assertIndexAccess(index, count, "index");

        bytes[index] = value;

        return this;
    }

    public ByteList set(int index, int value)
    {
        PreCondition.assertIndexAccess(index, count, "index");
        PreCondition.assertByte(value, "value");

        return this.set(index, (byte)value);
    }

    @Override
    public ByteList set(int index, Byte value)
    {
        PreCondition.assertIndexAccess(index, count, "index");
        PreCondition.assertNotNull(value, "value");

        return this.set(index, value.byteValue());
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
        return this.count == 0 ? Iterator.create() : new ByteArrayIterator(this.bytes, 0, this.count);
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
        return Array.clone(this.bytes, 0, this.count);
    }
}
