package qub;

public class JavaList<T> extends ListBase<T>
{
    private final java.util.List<T> list;

    protected JavaList(java.util.List<T> list)
    {
        this.list = list;
    }

    private boolean inBounds(int index)
    {
        return 0 <= index && index < list.size();
    }

    @Override
    public void add(T value)
    {
        list.add(value);
    }

    @Override
    public void set(int index, T value)
    {
        if (inBounds(index))
        {
            list.set(index, value);
        }
    }

    @Override
    public T removeAt(int index)
    {
        T result = null;
        if (inBounds(index))
        {
            result = list.remove(index);
        }
        return result;
    }

    @Override
    public T removeFirst()
    {
        T result = null;
        if (!list.isEmpty())
        {
            result = list.remove(0);
        }
        return result;
    }

    @Override
    public T removeFirst(Function1<T, Boolean> condition)
    {
        final int removeIndex = indexOf(condition);
        return removeIndex < 0 ? null : removeAt(removeIndex);
    }

    @Override
    public void clear()
    {
        list.clear();
    }

    @Override
    public T get(int index)
    {
        T result = null;
        if (0 <= index && index < list.size())
        {
            result = list.get(index);
        }
        return result;
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

    @Override
    public Iterator<T> iterate()
    {
        return JavaIteratorToIteratorAdapter.wrap(list.iterator());
    }

    /**
     * Wrap the provided Java List as a Qub List.
     * @param list The Java List to wrap.
     * @param <T> The type contained by the List.
     * @return The Qub List that wraps the provided Java List, or null if a null Java List is
     * provided.
     */
    public static <T> List<T> wrap(java.util.List<T> list)
    {
        return list == null ? null : new JavaList<>(list);
    }
}
