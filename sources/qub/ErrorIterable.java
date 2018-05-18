package qub;

public class ErrorIterable extends RuntimeException implements Iterable<Throwable>
{
    private final Iterable<Throwable> errors;

    private ErrorIterable(Iterable<Throwable> errors)
    {
        this.errors = errors;
    }

    public static RuntimeException from(Iterable<Throwable> errors)
    {
        RuntimeException result;
        if (errors == null || !errors.any())
        {
            result = null;
        }
        else if (errors.getCount() == 1)
        {
            result = Exceptions.asRuntime(errors.first());
        }
        else
        {
            result = new ErrorIterable(errors);
        }
        return result;
    }

    @Override
    public String toString()
    {
        return IterableBase.toString(this);
    }

    @Override
    public boolean equals(Object rhs)
    {
        return rhs instanceof ErrorIterable && equals((ErrorIterable)rhs);
    }

    public boolean equals(ErrorIterable rhs)
    {
        return IterableBase.equals(this, rhs);
    }

    @Override
    public Iterator<Throwable> iterate()
    {
        return errors.iterate();
    }

    @Override
    public boolean any()
    {
        return errors.any();
    }

    @Override
    public int getCount()
    {
        return errors.getCount();
    }

    @Override
    public Throwable first()
    {
        return errors.first();
    }

    @Override
    public Throwable first(Function1<Throwable, Boolean> condition)
    {
        return errors.first(condition);
    }

    @Override
    public Throwable last()
    {
        return errors.last();
    }

    @Override
    public Throwable last(Function1<Throwable, Boolean> condition)
    {
        return errors.last(condition);
    }

    @Override
    public boolean contains(Throwable value)
    {
        return errors.contains(value);
    }

    @Override
    public boolean contains(Function1<Throwable, Boolean> condition)
    {
        return errors.contains(condition);
    }

    @Override
    public Iterable<Throwable> take(int toTake)
    {
        return errors.take(toTake);
    }

    @Override
    public Iterable<Throwable> skip(int toSkip)
    {
        return errors.skip(toSkip);
    }

    @Override
    public Iterable<Throwable> skipFirst()
    {
        return errors.skipFirst();
    }

    @Override
    public Iterable<Throwable> skipLast()
    {
        return errors.skipLast();
    }

    @Override
    public Iterable<Throwable> skipLast(int toSkip)
    {
        return errors.skipLast(toSkip);
    }

    @Override
    public Iterable<Throwable> skipUntil(Function1<Throwable, Boolean> condition)
    {
        return errors.skipUntil(condition);
    }

    @Override
    public Iterable<Throwable> where(Function1<Throwable, Boolean> condition)
    {
        return errors.where(condition);
    }

    @Override
    public <U> Iterable<U> map(Function1<Throwable, U> conversion)
    {
        return errors.map(conversion);
    }

    @Override
    public <U> Iterable<U> instanceOf(Class<U> type)
    {
        return errors.instanceOf(type);
    }

    @Override
    public boolean equals(Iterable<Throwable> rhs)
    {
        return errors.equals(rhs);
    }

    @Override
    public java.util.Iterator<Throwable> iterator()
    {
        return iterate().iterator();
    }
}
