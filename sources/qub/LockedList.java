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
    public Indexable<T> getRange(int startIndex, int length)
    {
        return IndexableBase.getRange(this, startIndex, length);
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
        try (final Disposable criticalSection = mutex.criticalSection())
        {
            return innerList.indexOf(value);
        }
    }

    @Override
    public Iterator<T> iterate()
    {
        try (final Disposable criticalSection = mutex.criticalSection())
        {
            return innerList.iterate();
        }
    }

    @Override
    public boolean any()
    {
        try (final Disposable criticalSection = mutex.criticalSection())
        {
            return innerList.any();
        }
    }

    @Override
    public int getCount()
    {
        try (final Disposable criticalSection = mutex.criticalSection())
        {
            return innerList.getCount();
        }
    }

    @Override
    public T first()
    {
        return mutex.criticalSection(new Function0<T>()
        {
            @Override
            public T run()
            {
                return innerList.first();
            }
        });
    }

    @Override
    public T first(final Function1<T, Boolean> condition)
    {
        return mutex.criticalSection(new Function0<T>()
        {
            @Override
            public T run()
            {
                return innerList.first(condition);
            }
        });
    }

    @Override
    public T last()
    {
        return mutex.criticalSection(new Function0<T>()
        {
            @Override
            public T run()
            {
                return innerList.last();
            }
        });
    }

    @Override
    public T last(final Function1<T, Boolean> condition)
    {
        return mutex.criticalSection(new Function0<T>()
        {
            @Override
            public T run()
            {
                return innerList.last(condition);
            }
        });
    }

    @Override
    public boolean contains(final T value)
    {
        return mutex.criticalSection(new Function0<Boolean>()
        {
            @Override
            public Boolean run()
            {
                return innerList.contains(value);
            }
        });
    }

    @Override
    public boolean contains(final Function1<T, Boolean> condition)
    {
        return mutex.criticalSection(new Function0<Boolean>()
        {
            @Override
            public Boolean run()
            {
                return innerList.contains(condition);
            }
        });
    }

    @Override
    public Indexable<T> take(final int toTake)
    {
        return ListBase.take(this, toTake);
    }

    @Override
    public Iterable<T> takeLast(int toTake)
    {
        return ListBase.takeLast(this, toTake);
    }

    @Override
    public Indexable<T> skip(final int toSkip)
    {
        return ListBase.skip(this, toSkip);
    }

    @Override
    public Iterable<T> skipFirst()
    {
        return ListBase.skipFirst(this);
    }

    @Override
    public Iterable<T> skipLast()
    {
        return ListBase.skipLast(this);
    }

    @Override
    public Iterable<T> skipLast(final int toSkip)
    {
        return ListBase.skipLast(this, toSkip);
    }

    @Override
    public Iterable<T> skipUntil(final Function1<T, Boolean> condition)
    {
        return ListBase.skipUntil(this, condition);
    }

    @Override
    public Iterable<T> where(Function1<T, Boolean> condition)
    {
        return IterableBase.where(this, condition);
    }

    @Override
    public <U> Indexable<U> map(Function1<T, U> conversion)
    {
        return ListBase.map(this, conversion);
    }

    @Override
    public <U> Iterable<U> instanceOf(Class<U> type)
    {
        return ListBase.instanceOf(this, type);
    }

    @Override
    public T minimum(Function2<T,T,Comparison> comparer)
    {
        return ListBase.minimum(this, comparer);
    }

    @Override
    public T maximum(Function2<T,T,Comparison> comparer)
    {
        return ListBase.maximum(this, comparer);
    }

    @Override
    public boolean equals(final Object rhs)
    {
        return mutex.criticalSection(new Function0<Boolean>()
        {
            @Override
            public Boolean run()
            {
                return innerList.equals(rhs);
            }
        });
    }

    @Override
    public boolean equals(final Iterable<T> rhs)
    {
        return mutex.criticalSection(new Function0<Boolean>()
        {
            @Override
            public Boolean run()
            {
                return innerList.equals(rhs);
            }
        });
    }

    @Override
    public String toString()
    {
        return mutex.criticalSection(new Function0<String>()
        {
            @Override
            public String run()
            {
                return innerList.toString();
            }
        });
    }

    @Override
    public java.util.Iterator<T> iterator()
    {
        return mutex.criticalSection(new Function0<java.util.Iterator<T>>()
        {
            @Override
            public java.util.Iterator<T> run()
            {
                return innerList.iterator();
            }
        });
    }
}
