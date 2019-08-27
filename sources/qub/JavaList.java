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
        PreCondition.assertBetween(0, insertIndex, getCount(), "insertIndex");

        list.add(insertIndex, value);
    }

    @Override
    public void set(int index, T value)
    {
        PreCondition.assertIndexAccess(index, getCount());

        list.set(index, value);
    }

    @Override
    public T removeAt(int index)
    {
        PreCondition.assertIndexAccess(index, getCount());

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
        PreCondition.assertIndexAccess(index, getCount());

        return list.get(index);
    }

    @Override
    public Iterator<T> iterate()
    {
        return JavaIteratorToIteratorAdapter.create(list.iterator());
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
     * Wrap the provided Java List as a Qub List.
     * @param list The Java List to wrap.
     * @param <T> The type contained by the List.
     * @return The Qub List that wraps the provided Java List.
     */
    public static <T> List<T> wrap(java.util.List<T> list)
    {
        PreCondition.assertNotNull(list, "list");

        return new JavaList<>(list);
    }
}
