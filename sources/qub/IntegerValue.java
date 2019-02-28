package qub;

public class IntegerValue implements Value<java.lang.Integer>, Comparable<java.lang.Integer>
{
    private final java.util.concurrent.atomic.AtomicInteger value;
    private volatile boolean hasValue;

    public IntegerValue()
    {
        this.value = new java.util.concurrent.atomic.AtomicInteger();
        hasValue = false;
    }

    public IntegerValue(int value)
    {
        this.value = new java.util.concurrent.atomic.AtomicInteger(value);
        hasValue = true;
    }

    public IntegerValue(java.lang.Integer value)
    {
        PreCondition.assertNotNull(value, "value");

        this.value = new java.util.concurrent.atomic.AtomicInteger(value.intValue());
        hasValue = true;
    }

    public static IntegerValue create()
    {
        return new IntegerValue();
    }

    public static IntegerValue create(int value)
    {
        return new IntegerValue(value);
    }

    public static IntegerValue create(java.lang.Integer value)
    {
        PreCondition.assertNotNull(value, "value");

        return new IntegerValue(value);
    }

    public int getAsInt()
    {
        PreCondition.assertTrue(hasValue(), "hasValue()");

        return value.get();
    }

    @Override
    public boolean hasValue()
    {
        return hasValue;
    }

    @Override
    public java.lang.Integer get()
    {
        PreCondition.assertTrue(hasValue(), "hasValue()");

        return value.get();
    }

    @Override
    public void clear()
    {
        value.set(0);
        hasValue = false;
    }

    public void set(int value)
    {
        this.value.set(value);
        hasValue = true;
    }

    @Override
    public void set(java.lang.Integer value)
    {
        PreCondition.assertNotNull(value, "value");

        set(value.intValue());
    }

    public void plusAssign(int value)
    {
        PreCondition.assertTrue(hasValue(), "hasValue()");

        this.value.addAndGet(value);
    }

    public void plusAssign(java.lang.Integer value)
    {
        PreCondition.assertNotNull(value, "value");
        PreCondition.assertTrue(hasValue(), "hasValue()");

        plusAssign(value.intValue());
    }

    public void minusAssign(int value)
    {
        PreCondition.assertTrue(hasValue(), "hasValue()");

        this.value.addAndGet(-value);
    }

    public void minusAssign(java.lang.Integer value)
    {
        PreCondition.assertNotNull(value, "value");
        PreCondition.assertTrue(hasValue(), "hasValue()");

        minusAssign(value.intValue());
    }

    public IntegerValue increment()
    {
        PreCondition.assertTrue(hasValue(), "hasValue()");

        value.incrementAndGet();
        return this;
    }

    public IntegerValue decrement()
    {
        PreCondition.assertTrue(hasValue(), "hasValue()");

        value.decrementAndGet();
        return this;
    }

    @Override
    public Comparison compareTo(Integer value)
    {
        return value == null ? Comparison.GreaterThan : Comparison.from(get() - value);
    }

    @Override
    public boolean equals(Object rhs)
    {
        boolean result = false;
        if (rhs != null)
        {
            if (rhs instanceof java.lang.Integer)
            {
                result = equals((java.lang.Integer)rhs);
            }
            else if (rhs instanceof IntegerValue)
            {
                result = equals((IntegerValue)rhs);
            }
        }
        return result;
    }

    public boolean equals(int rhs)
    {
        return get() == rhs;
    }

    public boolean equals(java.lang.Integer rhs)
    {
        return rhs != null && get() == rhs.intValue();
    }

    public boolean equals(IntegerValue rhs)
    {
        return rhs != null &&
            hasValue() == rhs.hasValue() &&
            (!hasValue() || (getAsInt() == rhs.getAsInt()));
    }

    @Override
    public String toString()
    {
        return !hasValue() ? "no value" : Integers.toString(getAsInt());
    }
}
