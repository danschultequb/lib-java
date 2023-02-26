package qub;

public class BooleanValue implements Value.Typed<java.lang.Boolean,BooleanValue>
{
    private final java.util.concurrent.atomic.AtomicBoolean value;
    private volatile boolean hasValue;

    public BooleanValue()
    {
        value = new java.util.concurrent.atomic.AtomicBoolean();
        hasValue = false;
    }

    public BooleanValue(boolean value)
    {
        this.value = new java.util.concurrent.atomic.AtomicBoolean(value);
        hasValue = true;
    }

    public BooleanValue(java.lang.Boolean value)
    {
        PreCondition.assertNotNull(value, "value");

        this.value = new java.util.concurrent.atomic.AtomicBoolean(value);
        hasValue = true;
    }

    public static BooleanValue create()
    {
        return new BooleanValue();
    }

    public static BooleanValue create(boolean value)
    {
        return new BooleanValue(value);
    }

    public static BooleanValue create(java.lang.Boolean value)
    {
        PreCondition.assertNotNull(value, "value");

        return new BooleanValue(value);
    }

    @Override
    public boolean hasValue()
    {
        return hasValue;
    }

    public boolean getAsBoolean()
    {
        PreCondition.assertTrue(hasValue(), "hasValue()");

        return value.get();
    }

    @Override
    public java.lang.Boolean get()
    {
        PreCondition.assertTrue(hasValue(), "hasValue()");

        return value.get();
    }

    public BooleanValue set(boolean value)
    {
        this.value.set(value);
        hasValue = true;
        return this;
    }

    @Override
    public BooleanValue set(java.lang.Boolean value)
    {
        PreCondition.assertNotNull(value, "value");

        return this.set(value.booleanValue());
    }

    public boolean compareAndSet(boolean expectedValue, boolean newValue)
    {
        PreCondition.assertNotEqual(expectedValue, newValue, "newValue");

        return this.value.compareAndSet(expectedValue, newValue);
    }

    public boolean compareAndSet(boolean expectedValue, java.lang.Boolean newValue)
    {
        PreCondition.assertNotNull(newValue, "newValue");
        PreCondition.assertNotEqual(expectedValue, newValue, "newValue");

        return this.compareAndSet(expectedValue, newValue.booleanValue());
    }

    public boolean compareAndSet(java.lang.Boolean expectedValue, boolean newValue)
    {
        PreCondition.assertNotNull(expectedValue, "expectedValue");
        PreCondition.assertNotEqual(expectedValue, newValue, "newValue");

        return this.compareAndSet(expectedValue.booleanValue(), newValue);
    }

    public boolean compareAndSet(java.lang.Boolean expectedValue, java.lang.Boolean newValue)
    {
        PreCondition.assertNotNull(expectedValue, "expectedValue");
        PreCondition.assertNotNull(newValue, "newValue");
        PreCondition.assertNotEqual(expectedValue, newValue, "newValue");

        return this.compareAndSet(expectedValue.booleanValue(), newValue.booleanValue());
    }

    @Override
    public BooleanValue clear()
    {
        this.value.set(false);
        this.hasValue = false;
        return this;
    }

    @Override
    public boolean equals(Object rhs)
    {
        boolean result = false;
        if (rhs != null)
        {
            if (rhs instanceof java.lang.Boolean)
            {
                result = this.equals((java.lang.Boolean)rhs);
            }
            else if (rhs instanceof BooleanValue)
            {
                result = this.equals((BooleanValue)rhs);
            }
        }
        return result;
    }

    public boolean equals(boolean rhs)
    {
        return this.hasValue() && this.getAsBoolean() == rhs;
    }

    public boolean equals(java.lang.Boolean rhs)
    {
        return rhs != null && this.hasValue() && this.getAsBoolean() == rhs.booleanValue();
    }

    public boolean equals(BooleanValue rhs)
    {
        return rhs != null &&
            this.hasValue() == rhs.hasValue() &&
            (!hasValue() || (this.value.get() == rhs.value.get()));
    }

    @Override
    public String toString()
    {
        return !this.hasValue() ? "no value" : this.value.toString();
    }
}
