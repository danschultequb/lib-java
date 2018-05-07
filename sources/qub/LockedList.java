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
    public void add(final T value)
    {
        mutex.criticalSection(new Action0()
        {
            @Override
            public void run()
            {
                innerList.add(value);
            }
        });
    }

    @Override
    public void addAll(final T[] values)
    {
        mutex.criticalSection(new Action0()
        {
            @Override
            public void run()
            {
                innerList.addAll(values);
            }
        });
    }

    @Override
    public void addAll(final Iterator<T> values)
    {
        mutex.criticalSection(new Action0()
        {
            @Override
            public void run()
            {
                innerList.addAll(values);
            }
        });
    }

    @Override
    public void addAll(final Iterable<T> values)
    {
        mutex.criticalSection(new Action0()
        {
            @Override
            public void run()
            {
                innerList.addAll(values);
            }
        });
    }

    @Override
    public boolean remove(final T value)
    {
        return mutex.criticalSection(new Function0<Boolean>()
        {
            @Override
            public Boolean run()
            {
                return innerList.remove(value);
            }
        });
    }

    @Override
    public T removeAt(final int index)
    {
        return mutex.criticalSection(new Function0<T>()
        {
            @Override
            public T run()
            {
                return innerList.removeAt(index);
            }
        });
    }

    @Override
    public T removeFirst()
    {
        return mutex.criticalSection(new Function0<T>()
        {
            @Override
            public T run()
            {
                return innerList.removeFirst();
            }
        });
    }

    @Override
    public T removeFirst(final Function1<T, Boolean> condition)
    {
        return mutex.criticalSection(new Function0<T>()
        {
            @Override
            public T run()
            {
                return innerList.removeFirst(condition);
            }
        });
    }

    @Override
    public void clear()
    {
        mutex.criticalSection(new Action0()
        {
            @Override
            public void run()
            {
                innerList.clear();
            }
        });
    }

    @Override
    public void set(final int index, final T value)
    {
        mutex.criticalSection(new Action0()
        {
            @Override
            public void run()
            {
                innerList.set(index, value);
            }
        });
    }

    @Override
    public T get(final int index)
    {
        return mutex.criticalSection(new Function0<T>()
        {
            @Override
            public T run()
            {
                return innerList.get(index);
            }
        });
    }

    @Override
    public int indexOf(final Function1<T, Boolean> condition)
    {
        return mutex.criticalSection(new Function0<Integer>()
        {
            @Override
            public Integer run()
            {
                return innerList.indexOf(condition);
            }
        });
    }

    @Override
    public int indexOf(final T value)
    {
        return mutex.criticalSection(new Function0<Integer>()
        {
            @Override
            public Integer run()
            {
                return innerList.indexOf(value);
            }
        });
    }

    @Override
    public Iterator<T> iterate()
    {
        return mutex.criticalSection(new Function0<Iterator<T>>()
        {
            @Override
            public Iterator<T> run()
            {
                return innerList.iterate();
            }
        });
    }

    @Override
    public boolean any()
    {
        return mutex.criticalSection(new Function0<Boolean>()
        {
            @Override
            public Boolean run()
            {
                return innerList.any();
            }
        });
    }

    @Override
    public int getCount()
    {
        return mutex.criticalSection(new Function0<Integer>()
        {
            @Override
            public Integer run()
            {
                return innerList.getCount();
            }
        });
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
    public Iterable<T> take(final int toTake)
    {
        return ListBase.take(this, toTake);
    }

    @Override
    public Iterable<T> skip(final int toSkip)
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
    public Iterable<T> where(final Function1<T, Boolean> condition)
    {
        return ListBase.where(this, condition);
    }

    @Override
    public <U> Indexable<U> map(final Function1<T, U> conversion)
    {
        return ListBase.map(this, conversion);
    }

    @Override
    public <U> Iterable<U> instanceOf(final Class<U> type)
    {
        return ListBase.instanceOf(this, type);
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
