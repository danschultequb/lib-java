package qub;

/**
 * A array-based List data structure that can expand when it gets full.
 * @param <T> The type of element stored in this List.
 */
public class ArrayList<T> extends ListBase<T>
{
    private Array<T> array;
    private int elementCount;

    public ArrayList()
    {
        this(new Array<T>(0));
    }

    private ArrayList(Array<T> array)
    {
        this.array = array;
        this.elementCount = array.getCount();
    }

    private boolean inBounds(int index)
    {
        return 0 <= index && index < elementCount;
    }

    @Override
    public T get(int index)
    {
        return inBounds(index) ? array.get(index) : null;
    }

    @Override
    public void set(int index, T value)
    {
        if (inBounds(index))
        {
            array.set(index, value);
        }
    }

    @Override
    public void add(T value)
    {
        if (elementCount >= array.getCount())
        {
            final Array<T> newArray = new Array<T>((array.getCount() * 2) + 1);
            for (int i = 0; i < array.getCount(); ++i)
            {
                newArray.set(i, array.get(i));
            }
            array = newArray;
        }

        array.set(elementCount, value);
        ++elementCount;
    }

    @Override
    public T removeAt(int index)
    {
        T result = null;

        if (inBounds(index))
        {
            result = get(index);
            for (int i = index; i < getCount() - 1; ++i)
            {
                set(i, get(i + 1));
            }
            --elementCount;
        }

        return result;
    }

    @Override
    public T removeFirst()
    {
        return removeAt(0);
    }

    @Override
    public T removeFirst(Function1<T,Boolean> condition)
    {
        T result = null;

        final int firstMatchIndex = indexOf(condition);
        if (firstMatchIndex != -1)
        {
            result = removeAt(firstMatchIndex);
        }

        return result;
    }

    @Override
    public void clear()
    {
        elementCount = 0;
        array.setAll(null);
    }

    @Override
    public Iterator<T> iterate()
    {
        return array.iterate().take(elementCount);
    }

    /**
     * Get an Iterator that will iterate over the contents of this ArrayList in reverse.
     * @return An Iterator that will iterate over the contents of this ArrayList in reverse.
     */
    public Iterator<T> iterateReverse()
    {
        return array.iterateReverse().skip(array.getCount() - elementCount);
    }

    @Override
    public boolean any()
    {
        return elementCount > 0;
    }

    @Override
    public int getCount()
    {
        return elementCount;
    }

    public static <T> ArrayList<T> fromValues(T... values)
    {
        final Array<T> array = Array.fromValues(values);
        return new ArrayList<>(array);
    }

    public static <T> ArrayList<T> fromValues(Iterator<T> values)
    {
        final ArrayList<T> result = new ArrayList<>();
        result.addAll(values);
        return result;
    }

    public static <T> ArrayList<T> fromValues(Iterable<T> values)
    {
        final Array<T> array = Array.fromValues(values);
        return new ArrayList<>(array);
    }
}
