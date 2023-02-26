package qub;

/**
 * A list of bytes. Internally this uses a primitive byte[] to store bytes, so it should be more
 * efficient than using a generic {@link List}&lt;{@link Byte}&gt;.
 */
public class ByteList implements List<Byte>
{
    private byte[] bytes;
    private int count;

    private ByteList(int capacity)
    {
        PreCondition.assertGreaterThanOrEqualTo(capacity, 0, "capacity");

        this.bytes = new byte[capacity];
    }

    public static ByteList create()
    {
        return ByteList.create(64);
    }

    public static ByteList create(int capacity)
    {
        return new ByteList(capacity);
    }

    public int getCapacity()
    {
        return this.bytes.length;
    }

    @Override
    public boolean any()
    {
        return this.count > 0;
    }

    @Override
    public int getCount()
    {
        return this.count;
    }

    public ByteList insert(int insertIndex, int value)
    {
        PreCondition.assertIndexAccess(insertIndex, this.count + 1, "insertIndex");
        PreCondition.assertByte(value, "value");

        return this.insert(insertIndex, (byte)value);
    }

    public ByteList insert(int insertIndex, byte value)
    {
        PreCondition.assertIndexAccess(insertIndex, this.count + 1, "insertIndex");

        if (this.count == this.bytes.length)
        {
            final byte[] newBytes = new byte[(this.bytes.length * 2) + 1];
            if (1 <= insertIndex)
            {
                Array.copy(this.bytes, 0, newBytes, 0, insertIndex);
            }
            if (insertIndex <= this.count - 1)
            {
                Array.copy(this.bytes, insertIndex, newBytes, insertIndex + 1, this.count - insertIndex);
            }
            this.bytes = newBytes;
        }
        else if (insertIndex < this.count)
        {
            Array.shiftRight(this.bytes, insertIndex, this.count - insertIndex);
        }
        this.bytes[insertIndex] = value;
        this.count++;

        return this;
    }

    @Override
    public ByteList insert(int insertIndex, Byte value)
    {
        PreCondition.assertIndexAccess(insertIndex, this.count + 1, "insertIndex");
        PreCondition.assertNotNull(value, "value");

        return this.insert(insertIndex, value.byteValue());
    }

    public ByteList add(byte value)
    {
        return this.insert(this.count, value);
    }

    public ByteList addAll(byte[] values, int startIndex, int length)
    {
        PreCondition.assertNotNull(values, "values");
        PreCondition.assertStartIndex(startIndex, values.length);
        PreCondition.assertLength(length, startIndex, values.length);

        for (int i = 0; i < length; i++)
        {
            this.add(values[startIndex + i]);
        }

        return this;
    }

    public ByteList addAll(byte... values)
    {
        PreCondition.assertNotNull(values, "values");

        return this.addAll(values, 0, values.length);
    }

    @Override
    public ByteList addAll(Byte... values)
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

    @Override
    public ByteList addAll(Iterator<? extends Byte> values)
    {
        return (ByteList)List.super.addAll(values);
    }

    @Override
    public ByteList addAll(Iterable<? extends Byte> values)
    {
        return (ByteList)List.super.addAll(values);
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
        PreCondition.assertIndexAccess(index, this.count, "index");

        final byte result = this.bytes[index];
        if (index < this.count - 1)
        {
            Array.shiftLeft(this.bytes, index, this.count - index - 1);
        }
        this.count--;

        return result;
    }

    @Override
    public Result<ByteArray> removeFirst(int valuesToRemove)
    {
        PreCondition.assertGreaterThanOrEqualTo(valuesToRemove, 0, "valuesToRemove");

        return Result.create(() ->
        {
            final byte[] bytes = new byte[valuesToRemove];
            final int valuesRemoved = this.removeFirst(bytes).await();
            return ByteArray.create(bytes, 0, valuesRemoved);
        });
    }

    /**
     * Remove the first bytes from this {@link ByteList} and put them into the outputBytes array.
     * @param outputBytes The array to put the bytes into.
     * @return The number of bytes that were removed from this {@link ByteList}.
     * @exception EmptyException if this {@link ByteList} is empty.
     */
    public Result<Integer> removeFirst(byte[] outputBytes)
    {
        PreCondition.assertNotNull(outputBytes, "outputBytes");

        return this.removeFirst(outputBytes, 0, outputBytes.length);
    }

    /**
     * Remove the first bytes from this {@link ByteList} and put them into the outputBytes array.
     * @param outputBytes The array to put the bytes into.
     * @param startIndex The start index in the array to start putting the bytes to.
     * @param length The number of bytes to remove from the list and put into the array.
     * @return The number of bytes that were removed from this {@link ByteList}.
     * @exception EmptyException if this {@link ByteList} is empty.
     */
    public Result<Integer> removeFirst(byte[] outputBytes, int startIndex, int length)
    {
        PreCondition.assertNotNull(outputBytes, "outputBytes");
        PreCondition.assertStartIndex(startIndex, outputBytes.length);
        PreCondition.assertLength(length, startIndex, outputBytes.length);

        return Result.create(() ->
        {
            int result = 0;

            if (length >= 1)
            {
                if (!this.any())
                {
                    throw new EmptyException();
                }

                result = Math.minimum(this.count, length);
                Array.copy(this.bytes, 0, outputBytes, startIndex, result);
                if (result < this.count)
                {
                    Array.copy(this.bytes, result, this.bytes, 0, this.count - result);
                }
                this.count -= result;
            }

            PostCondition.assertGreaterThanOrEqualTo(result, 0, "result");

            return result;
        });
    }

    public ByteList set(int index, byte value)
    {
        PreCondition.assertIndexAccess(index, this.count, "index");

        this.bytes[index] = value;

        return this;
    }

    public ByteList set(int index, int value)
    {
        PreCondition.assertIndexAccess(index, this.count, "index");
        PreCondition.assertByte(value, "value");

        return this.set(index, (byte)value);
    }

    @Override
    public ByteList set(int index, Byte value)
    {
        PreCondition.assertIndexAccess(index, this.count, "index");
        PreCondition.assertNotNull(value, "value");

        return this.set(index, value.byteValue());
    }

    @Override
    public Byte get(int index)
    {
        PreCondition.assertIndexAccess(index, this.count, "index");

        return this.bytes[index];
    }

    @Override
    public Iterator<Byte> iterate()
    {
        return ByteArrayIterator.create(this.bytes, 0, this.count);
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
     * Get a byte[] representation of this {@link ByteList}.
     */
    public byte[] toByteArray()
    {
        return Arrays.clone(this.bytes, 0, this.count);
    }
}
