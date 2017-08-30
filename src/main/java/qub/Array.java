package qub;

/**
 * A basic Array data structure that holds a fixed number of elements.
 * @param <T> The type of element contained by this Array.
 */
public class Array<T> implements Iterable<T>
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
}
