package qub;

public class MutableInteger extends Value<java.lang.Integer> implements Comparable<java.lang.Integer>
{
    public MutableInteger()
    {
        this(0);
    }

    public MutableInteger(int value)
    {
        this(java.lang.Integer.valueOf(value));
    }

    public MutableInteger(java.lang.Integer value)
    {
        super(value);

        PreCondition.assertNotNull(value, "value");
    }

    @Override
    public void set(java.lang.Integer value)
    {
        PreCondition.assertNotNull(value, "value");

        super.set(value);
    }

    public void plusAssign(java.lang.Integer rhs)
    {
        PreCondition.assertNotNull(rhs, "rhs");

        set(get() + rhs);
    }

    public void minusAssign(java.lang.Integer rhs)
    {
        PreCondition.assertNotNull(rhs, "rhs");

        set(get() - rhs);
    }

    public int increment()
    {
        final int result = get() + 1;
        set(result);

        return result;
    }

    public int decrement()
    {
        final int result = get() - 1;
        set(result);

        return result;
    }

    @Override
    public Comparison compareTo(Integer value)
    {
        return value == null ? Comparison.GreaterThan : Comparison.from(get() - value);
    }
}
