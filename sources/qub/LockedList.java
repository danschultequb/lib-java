package qub;

public class LockedList<T> implements List<T>
{
    private final List<T> innerList;
    private final Mutex mutex;

    public LockedList(List<T> innerList)
    {
        this.innerList = innerList;
        this.mutex = new SpinMutex();
    }

    public static <T> LockedList<T> from(List<T> innerList)
    {
        return innerList == null ? null : new LockedList<T>(innerList);
    }

    @Override
    public void add(T value)
    {
        mutex.criticalSection(() -> innerList.add(value));
    }

    @Override
    public void insert(int insertIndex, T value)
    {
        mutex.criticalSection(() -> innerList.insert(insertIndex, value));
    }

    @Override
    public void addAll(T[] values)
    {
        mutex.criticalSection(() -> innerList.addAll(values));
    }

    @Override
    public void addAll(Iterator<T> values)
    {
        mutex.criticalSection(() -> innerList.addAll(values));
    }

    @Override
    public void addAll(Iterable<T> values)
    {
        mutex.criticalSection(() -> innerList.addAll(values));
    }

    @Override
    public boolean remove(T value)
    {
        return mutex.criticalSection(() -> innerList.remove(value));
    }

    @Override
    public T removeAt(int index)
    {
        return mutex.criticalSection(() -> innerList.removeAt(index));
    }

    @Override
    public T removeFirst()
    {
        return mutex.criticalSection(() -> innerList.removeFirst());
    }

    @Override
    public T removeFirst(Function1<T, Boolean> condition)
    {
        return mutex.criticalSection(() -> innerList.removeFirst(condition));
    }

    @Override
    public T removeLast()
    {
        return mutex.criticalSection(() -> innerList.removeLast());
    }

    @Override
    public void clear()
    {
        mutex.criticalSection(innerList::clear);
    }

    @Override
    public boolean endsWith(T value)
    {
        return mutex.criticalSection(() -> innerList.endsWith(value));
    }

    @Override
    public boolean endsWith(Iterable<T> values)
    {
        return mutex.criticalSection(() -> innerList.endsWith(values));
    }

    @Override
    public void set(int index, T value)
    {
        mutex.criticalSection(() -> innerList.set(index, value));
    }

    @Override
    public void setFirst(T value)
    {
        mutex.criticalSection(() -> innerList.setFirst(value));
    }

    @Override
    public void setLast(T value)
    {
        mutex.criticalSection(() -> innerList.setLast(value));
    }

    @Override
    public T get(int index)
    {
        return mutex.criticalSection(() -> innerList.get(index));
    }

    @Override
    public int indexOf(Function1<T, Boolean> condition)
    {
        return mutex.criticalSection(() -> innerList.indexOf(condition));
    }

    @Override
    public int indexOf(T value)
    {
        return mutex.criticalSection(() -> innerList.indexOf(value));
    }

    @Override
    public Iterator<T> iterate()
    {
        return mutex.criticalSection(innerList::iterate);
    }

    @Override
    public boolean any()
    {
        return mutex.criticalSection(innerList::any);
    }

    @Override
    public int getCount()
    {
        return mutex.criticalSection(innerList::getCount);
    }

    @Override
    public T first()
    {
        return mutex.criticalSection((Function0<T>)innerList::first);
    }

    @Override
    public T first(final Function1<T, Boolean> condition)
    {
        return mutex.criticalSection(() -> innerList.first(condition));
    }

    @Override
    public T last()
    {
        return mutex.criticalSection((Function0<T>)innerList::last);
    }

    @Override
    public T last(final Function1<T, Boolean> condition)
    {
        return mutex.criticalSection(() -> innerList.last(condition));
    }

    @Override
    public boolean contains(final T value)
    {
        return mutex.criticalSection(() -> innerList.contains(value));
    }

    @Override
    public boolean contains(final Function1<T, Boolean> condition)
    {
        return mutex.criticalSection(() -> innerList.contains(condition));
    }

    @Override
    public boolean equals(Object rhs)
    {
        return mutex.criticalSection(() -> innerList.equals(rhs));
    }

    @Override
    public boolean equals(final Iterable<T> rhs)
    {
        return mutex.criticalSection(() -> innerList.equals(rhs));
    }

    @Override
    public String toString()
    {
        return mutex.criticalSection((Function0<String>)innerList::toString);
    }

    @Override
    public java.util.Iterator<T> iterator()
    {
        return mutex.criticalSection(innerList::iterator);
    }
}
