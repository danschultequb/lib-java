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
        try (final Disposable criticalSection = mutex.criticalSection())
        {
            innerList.add(value);
        }
    }

    @Override
    public Result<Boolean> insert(int insertIndex, T value)
    {
        try (final Disposable criticalSection = mutex.criticalSection())
        {
            return innerList.insert(insertIndex, value);
        }
    }

    @Override
    public void addAll(T[] values)
    {
        try (final Disposable criticalSection = mutex.criticalSection())
        {
            innerList.addAll(values);
        }
    }

    @Override
    public void addAll(Iterator<T> values)
    {
        try (final Disposable criticalSection = mutex.criticalSection())
        {
            innerList.addAll(values);
        }
    }

    @Override
    public void addAll(Iterable<T> values)
    {
        try (final Disposable criticalSection = mutex.criticalSection())
        {
            innerList.addAll(values);
        }
    }

    @Override
    public boolean remove(T value)
    {
        try (final Disposable criticalSection = mutex.criticalSection())
        {
            return innerList.remove(value);
        }
    }

    @Override
    public T removeAt(int index)
    {
        try (final Disposable criticalSection = mutex.criticalSection())
        {
            return innerList.removeAt(index);
        }
    }

    @Override
    public T removeFirst()
    {
        try (final Disposable criticalSection = mutex.criticalSection())
        {
            return innerList.removeFirst();
        }
    }

    @Override
    public T removeFirst(Function1<T, Boolean> condition)
    {
        try (final Disposable criticalSection = mutex.criticalSection())
        {
            return innerList.removeFirst(condition);
        }
    }

    @Override
    public T removeLast()
    {
        try (final Disposable criticalSection = mutex.criticalSection())
        {
            return innerList.removeLast();
        }
    }

    @Override
    public void clear()
    {
        try (final Disposable criticalSection = mutex.criticalSection())
        {
            innerList.clear();
        }
    }

    @Override
    public boolean endsWith(T value)
    {
        try (final Disposable criticalSection = mutex.criticalSection())
        {
            return innerList.endsWith(value);
        }
    }

    @Override
    public boolean endsWith(Iterable<T> values)
    {
        try (final Disposable criticalSection = mutex.criticalSection())
        {
            return innerList.endsWith(values);
        }
    }

    @Override
    public void set(int index, T value)
    {
        try (final Disposable criticalSection = mutex.criticalSection())
        {
            innerList.set(index, value);
        }
    }

    @Override
    public void setFirst(T value)
    {
        try (final Disposable criticalSection = mutex.criticalSection())
        {
            innerList.setFirst(value);
        }
    }

    @Override
    public void setLast(T value)
    {
        try (final Disposable criticalSection = mutex.criticalSection())
        {
            innerList.setLast(value);
        }
    }

    @Override
    public T get(int index)
    {
        try (final Disposable criticalSection = mutex.criticalSection())
        {
            return innerList.get(index);
        }
    }

    @Override
    public int indexOf(Function1<T, Boolean> condition)
    {
        try (final Disposable criticalSection = mutex.criticalSection())
        {
            return innerList.indexOf(condition);
        }
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
