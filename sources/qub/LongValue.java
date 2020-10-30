package qub;

public class LongValue implements Value<java.lang.Long>, Comparable<java.lang.Long>
{
    private final java.util.concurrent.atomic.AtomicLong value;
    private volatile boolean hasValue;

    public LongValue()
    {
        this.value = new java.util.concurrent.atomic.AtomicLong();
        this.hasValue = false;
    }

    public LongValue(long value)
    {
        this.value = new java.util.concurrent.atomic.AtomicLong(value);
        this.hasValue = true;
    }

    public LongValue(java.lang.Integer value)
    {
        PreCondition.assertNotNull(value, "value");

        this.value = new java.util.concurrent.atomic.AtomicLong(value.longValue());
        this.hasValue = true;
    }

    public LongValue(java.lang.Long value)
    {
        PreCondition.assertNotNull(value, "value");

        this.value = new java.util.concurrent.atomic.AtomicLong(value.longValue());
        this.hasValue = true;
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

        return this.value.get();
    }

    @Override
    public boolean hasValue()
    {
        return this.hasValue;
    }

    @Override
    public java.lang.Long get()
    {
        PreCondition.assertTrue(hasValue(), "hasValue()");

        return this.value.get();
    }

    @Override
    public LongValue clear()
    {
        this.value.set(0);
        this.hasValue = false;
        return this;
    }

    public LongValue set(long value)
    {
        this.value.set(value);
        this.hasValue = true;
        return this;
    }

    public LongValue set(java.lang.Integer value)
    {
        PreCondition.assertNotNull(value, "value");

        return this.set(value.longValue());
    }

    @Override
    public LongValue set(java.lang.Long value)
    {
        PreCondition.assertNotNull(value, "value");

        return this.set(value.longValue());
    }

    public LongValue plusAssign(long value)
    {
        PreCondition.assertTrue(hasValue(), "hasValue()");

        this.value.addAndGet(value);
        return this;
    }

    public LongValue plusAssign(java.lang.Integer value)
    {
        PreCondition.assertNotNull(value, "value");
        PreCondition.assertTrue(hasValue(), "hasValue()");

        return this.plusAssign(value.longValue());
    }

    public LongValue plusAssign(java.lang.Long value)
    {
        PreCondition.assertNotNull(value, "value");
        PreCondition.assertTrue(hasValue(), "hasValue()");

        return this.plusAssign(value.longValue());
    }

    public LongValue minusAssign(long value)
    {
        PreCondition.assertTrue(hasValue(), "hasValue()");

        return this.plusAssign(-value);
    }

    public LongValue minusAssign(java.lang.Integer value)
    {
        PreCondition.assertNotNull(value, "value");
        PreCondition.assertTrue(hasValue(), "hasValue()");

        return this.minusAssign(value.intValue());
    }

    public LongValue minusAssign(java.lang.Long value)
    {
        PreCondition.assertNotNull(value, "value");
        PreCondition.assertTrue(hasValue(), "hasValue()");

        return this.minusAssign(value.intValue());
    }

    public LongValue increment()
    {
        PreCondition.assertTrue(hasValue(), "hasValue()");

        this.value.incrementAndGet();
        return this;
    }

    public LongValue decrement()
    {
        PreCondition.assertTrue(hasValue(), "hasValue()");

        this.value.decrementAndGet();
        return this;
    }

    public boolean compareAndSet(long expectedValue, long newValue)
    {
        return this.value.compareAndSet(expectedValue, newValue);
    }

    @Override
    public Comparison compareWith(java.lang.Long value)
    {
        return value == null ? Comparison.GreaterThan : Comparison.create(this.get() - value);
    }

    @Override
    public boolean equals(Object rhs)
    {
        boolean result = false;
        if (rhs != null)
        {
            if (rhs instanceof java.lang.Integer)
            {
                result = this.equals((java.lang.Integer)rhs);
            }
            else if (rhs instanceof LongValue)
            {
                result = this.equals((LongValue)rhs);
            }
        }
        return result;
    }

    public boolean equals(long rhs)
    {
        return this.get() == rhs;
    }

    public boolean equals(java.lang.Integer rhs)
    {
        return rhs != null && this.get() == rhs.longValue();
    }

    public boolean equals(java.lang.Long rhs)
    {
        return rhs != null && this.get() == rhs.longValue();
    }

    public boolean equals(LongValue rhs)
    {
        return rhs != null &&
            this.hasValue() == rhs.hasValue() &&
            (!hasValue() || (this.getAsLong() == rhs.getAsLong()));
    }

    @Override
    public String toString()
    {
        return !this.hasValue() ? "no value" : Longs.toString(this.getAsLong());
    }
}
