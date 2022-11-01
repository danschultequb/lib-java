package qub;

/**
 * An enumerated type that can be expanded/added to at runtime.
 * @param <T> The derived enumerated type.
 */
public abstract class StringEnum<T extends StringEnum<T>>
{
    protected String value;

    protected StringEnum()
    {
    }

    @Override
    public boolean equals(Object rhs)
    {
        return this == rhs;
    }

    @Override
    public String toString()
    {
        return this.value;
    }

    @Override
    public int hashCode()
    {
        return Hash.getHashCode(this.getClass(), this.value);
    }
}
