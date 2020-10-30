package qub;

public class IntegerValue implements Value<java.lang.Integer>, Comparable<java.lang.Integer>
{
    private final java.util.concurrent.atomic.AtomicInteger value;
    private volatile boolean hasValue;

    public IntegerValue()
    {
        this.value = new java.util.concurrent.atomic.AtomicInteger();
        this.hasValue = false;
    }

    public IntegerValue(int value)
    {
        this.value = new java.util.concurrent.atomic.AtomicInteger(value);
        this.hasValue = true;
    }

    public IntegerValue(java.lang.Integer value)
    {
        PreCondition.assertNotNull(value, "value");

        this.value = new java.util.concurrent.atomic.AtomicInteger(value.intValue());
        this.hasValue = true;
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

        return this.value.get();
    }

    @Override
    public boolean hasValue()
    {
        return this.hasValue;
    }

    @Override
    public java.lang.Integer get()
    {
        PreCondition.assertTrue(hasValue(), "hasValue()");

        return this.value.get();
    }

    @Override
    public IntegerValue clear()
    {
        this.value.set(0);
        this.hasValue = false;
        return this;
    }

    public IntegerValue set(int value)
    {
        this.value.set(value);
        this.hasValue = true;
        return this;
    }

    @Override
    public IntegerValue set(java.lang.Integer value)
    {
        PreCondition.assertNotNull(value, "value");

        return this.set(value.intValue());
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

        this.plusAssign(value.intValue());
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

        this.minusAssign(value.intValue());
    }

    public IntegerValue increment()
    {
        PreCondition.assertTrue(hasValue(), "hasValue()");

        this.value.incrementAndGet();
        return this;
    }

    public java.lang.Integer incrementAndGet()
    {
        return this.incrementAndGetAsInt();
    }

    public int incrementAndGetAsInt()
    {
        PreCondition.assertTrue(hasValue(), "hasValue()");

        return this.value.incrementAndGet();
    }

    public IntegerValue decrement()
    {
        PreCondition.assertTrue(hasValue(), "hasValue()");

        this.value.decrementAndGet();
        return this;
    }

    @Override
    public Comparison compareWith(Integer value)
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
            else if (rhs instanceof IntegerValue)
            {
                result = this.equals((IntegerValue)rhs);
            }
        }
        return result;
    }

    public boolean equals(int rhs)
    {
        return this.get() == rhs;
    }

    public boolean equals(java.lang.Integer rhs)
    {
        return rhs != null && this.get() == rhs.intValue();
    }

    public boolean equals(IntegerValue rhs)
    {
        return rhs != null &&
            this.hasValue() == rhs.hasValue() &&
            (!hasValue() || (this.getAsInt() == rhs.getAsInt()));
    }

    @Override
    public String toString()
    {
        return !this.hasValue() ? "no value" : Integers.toString(this.getAsInt());
    }
}
