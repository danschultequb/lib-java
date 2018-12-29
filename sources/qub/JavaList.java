package qub;

public class JavaList<T> implements List<T>
{
    private final java.util.List<T> list;

    protected JavaList(java.util.List<T> list)
    {
        this.list = list;
    }

    @Override
    public int getCount()
    {
        return list.size();
    }

    @Override
    public void add(T value)
    {
        list.add(value);
    }

    @Override
    public void insert(int insertIndex, T value)
    {
        validateInsertIndex(insertIndex);

        list.add(insertIndex, value);
    }

    @Override
    public void set(int index, T value)
    {
        validateAccessIndex(index);

        list.set(index, value);
    }

    @Override
    public T removeAt(int index)
    {
        validateAccessIndex(index);

        return list.remove(index);
    }

    @Override
    public void clear()
    {
        list.clear();
    }

    @Override
    public T get(int index)
    {
        validateAccessIndex(index);

        return list.get(index);
    }

    @Override
    public Iterator<T> iterate()
    {
        return JavaIteratorToIteratorAdapter.wrap(list.iterator());
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
     * Wrap the provided Java10 List as a Qub List.
     * @param list The Java10 List to wrap.
     * @param <T> The type contained by the List.
     * @return The Qub List that wraps the provided Java10 List, or null if a null Java10 List is
     * provided.
     */
    public static <T> List<T> wrap(java.util.List<T> list)
    {
        return list == null ? null : new JavaList<>(list);
    }
}
