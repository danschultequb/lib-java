package qub;

public class LongValue implements Value<java.lang.Long>, Comparable<java.lang.Long>
{
    private final java.util.concurrent.atomic.AtomicLong value;
    private volatile boolean hasValue;

    public LongValue()
    {
        this.value = new java.util.concurrent.atomic.AtomicLong();
        hasValue = false;
    }

    public LongValue(long value)
    {
        this.value = new java.util.concurrent.atomic.AtomicLong(value);
        hasValue = true;
    }

    public LongValue(java.lang.Integer value)
    {
        PreCondition.assertNotNull(value, "value");

        this.value = new java.util.concurrent.atomic.AtomicLong(value.longValue());
        hasValue = true;
    }

    public LongValue(java.lang.Long value)
    {
        PreCondition.assertNotNull(value, "value");

        this.value = new java.util.concurrent.atomic.AtomicLong(value.longValue());
        hasValue = true;
    }

    public static LongValue create()
    {
        return new LongValue();
    }

    public static LongValue create(long value)
    {
        return new LongValue(value);
    }

    public static LongValue create(java.lang.Integer value)
    {
        PreCondition.assertNotNull(value, "value");

        return new LongValue(value);
    }

    public static LongValue create(java.lang.Long value)
    {
        PreCondition.assertNotNull(value, "value");

        return new LongValue(value);
    }

    public long getAsLong()
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
    public java.lang.Long get()
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

    public void set(long value)
    {
        this.value.set(value);
        hasValue = true;
    }

    public void set(java.lang.Integer value)
    {
        PreCondition.assertNotNull(value, "value");

        set(value.longValue());
    }

    @Override
    public void set(java.lang.Long value)
    {
        PreCondition.assertNotNull(value, "value");

        set(value.longValue());
    }

    public void plusAssign(long value)
    {
        PreCondition.assertTrue(hasValue(), "hasValue()");

        this.value.addAndGet(value);
    }

    public void plusAssign(java.lang.Integer value)
    {
        PreCondition.assertNotNull(value, "value");
        PreCondition.assertTrue(hasValue(), "hasValue()");

        plusAssign(value.longValue());
    }

    public void plusAssign(java.lang.Long value)
    {
        PreCondition.assertNotNull(value, "value");
        PreCondition.assertTrue(hasValue(), "hasValue()");

        plusAssign(value.longValue());
    }

    public void minusAssign(long value)
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

    public void minusAssign(java.lang.Long value)
    {
        PreCondition.assertNotNull(value, "value");
        PreCondition.assertTrue(hasValue(), "hasValue()");

        minusAssign(value.intValue());
    }

    public LongValue increment()
    {
        PreCondition.assertTrue(hasValue(), "hasValue()");

        value.incrementAndGet();
        return this;
    }

    public LongValue decrement()
    {
        PreCondition.assertTrue(hasValue(), "hasValue()");

        value.decrementAndGet();
        return this;
    }

    public boolean compareAndSet(long expectedValue, long newValue)
    {
        return value.compareAndSet(expectedValue, newValue);
    }

    @Override
    public Comparison compareTo(java.lang.Long value)
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
            else if (rhs instanceof LongValue)
            {
                result = equals((LongValue)rhs);
            }
        }
        return result;
    }

    public boolean equals(long rhs)
    {
        return get() == rhs;
    }

    public boolean equals(java.lang.Integer rhs)
    {
        return rhs != null && get() == rhs.longValue();
    }

    public boolean equals(java.lang.Long rhs)
    {
        return rhs != null && get() == rhs.longValue();
    }

    public boolean equals(LongValue rhs)
    {
        return rhs != null &&
            hasValue() == rhs.hasValue() &&
            (!hasValue() || (getAsLong() == rhs.getAsLong()));
    }

    @Override
    public String toString()
    {
        return !hasValue() ? "no value" : Longs.toString(getAsLong());
    }
}
