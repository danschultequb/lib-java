package qub;

/**
 * A basic Array data structure that holds a fixed number of elements.
 * @param <T> The type of element contained by this Array.
 */
public class Array<T> extends IterableBase<T>
{
    private final Object[] data;

    /**
     * Create a new Array with the provided size.
     * @param count The number of elements in the Array.
     * @require 0 <= count
     * @promise getCount() == count
     */
    public Array(int count)
    {
        data = new Object[count];
    }

    /**
     * Get the number of elements in this Array.
     * @return The number of elements in this Array.
     * @promise 0 <= result
     */
    @Override
    public int getCount()
    {
        return data.length;
    }

    /**
     * Get the element at the provided index. If the provided index is outside of the bounds of this
     * Array, then null will be returned.
     * @param index The index of the element to return.
     * @return The element at the provided index, or null if the provided index is out of bounds.
     */
    public T get(int index)
    {
        return inBounds(index) ? (T)data[index] : null;
    }

    /**
     * Set the element at the provided index. If the provided index is outside of the bounds of this
     * Array, then nothing will happen.
     * @param index The index of the element to set.
     * @param value The value to set at the provided index.
     */
    public void set(int index, T value)
    {
        if (inBounds(index))
        {
            data[index] = value;
        }
    }

    /**
     * Set all of the elements of this Array to the provided value.
     * @param value The value to set all of the elements of this Array to.
     */
    public void setAll(T value)
    {
        java.util.Arrays.fill(data, value);
    }

    /**
     * Get whether or not the provided index is inside the bounds of this Array.
     * @param index The index to check.
     * @return Whether or not the provided index is inside the bounds of this Array.
     */
    private boolean inBounds(int index)
    {
        return 0 <= index && index < getCount();
    }

    @Override
    public Iterator<T> iterate()
    {
        return new ArrayIterator<>(this);
    }

    /**
     * Get an Iterator that will iterate over the contents of this Array in reverse.
     * @return An Iterator that will iterate over the contents of this Array in reverse.
     */
    public Iterator<T> iterateReverse() { return new ArrayIterator<>(this, getCount() - 1, -1); }

    @Override
    public boolean any()
    {
        return 1 <= getCount();
    }

    /**
     * Create an Array from the provided values.
     * @param values The values to initialize the array with.
     */
    public static <T> Array<T> fromValues(T... values)
    {
        final int length = values == null ? 0 : values.length;
        final Array<T> result = new Array<>(length);
        for (int i = 0; i < length; ++i)
        {
            result.set(i, values[i]);
        }
        return result;
    }

    /**
     * Create an Array from the provided values.
     * @param values The values to initialize the array with.
     */
    public static <T> Array<T> fromValues(Iterator<T> values)
    {
        return fromValues(ArrayList.fromValues(values));
    }

    /**
     * Create an Array from the provided values.
     * @param values The values to initialize the array with.
     */
    public static <T> Array<T> fromValues(Iterable<T> values)
    {
        final int length = values == null ? 0 : values.getCount();
        final Array<T> result = new Array<>(length);

        if (values != null && values.any())
        {
            int i = 0;
            for (final T value : values)
            {
                result.set(i, value);
                ++i;
            }
        }
        return result;
    }

    /**
     * Convert the provided byte Iterator into a byte array.
     * @param bytes The byte Iterator to convert to a byte array.
     * @return The byte array.
     */
    public static byte[] toByteArray(Iterator<Byte> bytes)
    {
        byte[] result;
        if (bytes == null)
        {
            result = null;
        }
        else if (!bytes.any())
        {
            result = new byte[0];
        }
        else
        {
            final ArrayList<Byte> list = new ArrayList<>();
            for (Byte value : bytes)
            {
                list.add(value);
            }

            result = toByteArray(list);
        }
        return result;
    }

    /**
     * Convert the provided byte Iterable into a byte array.
     * @param bytes The byte Iterable to convert to a byte array.
     * @return The byte array.
     */
    public static byte[] toByteArray(Iterable<Byte> bytes)
    {
        byte[] result;
        if (bytes == null)
        {
            result = null;
        }
        else if (!bytes.any())
        {
            result = new byte[0];
        }
        else
        {
            result = new byte[bytes.getCount()];
            int index = 0;
            for (Byte value : bytes)
            {
                result[index] = value.byteValue();
                ++index;
            }
        }
        return result;
    }
}
