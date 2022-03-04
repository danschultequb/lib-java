package qub;

public abstract class Point2Base<T> implements Point2<T>
{
    @Override
    public String toString()
    {
        return Point2.toString(this);
    }

    @Override
    public boolean equals(Object rhs)
    {
        return Point2.equals(this, rhs);
    }

    @Override
    public int hashCode()
    {
        return Point2.hashCode(this);
    }
}
