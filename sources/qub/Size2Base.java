package qub;

public abstract class Size2Base<T> implements Size2<T>
{
    @Override
    public boolean equals(Object obj)
    {
        return Size2.equals(this, obj);
    }

    @Override
    public boolean equals(Size2<T> rhs)
    {
        return Size2.super.equals(rhs);
    }

    @Override
    public String toString()
    {
        return Size2.toString(this);
    }

    @Override
    public int hashCode()
    {
        return Size2.hashCode(this);
    }
}
