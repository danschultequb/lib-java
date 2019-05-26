package qub;

public class BooleanValue implements Value<java.lang.Boolean>
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

    public void set(boolean value)
    {
        this.value.set(value);
        hasValue = true;
    }

    @Override
    public void set(java.lang.Boolean value)
    {
        PreCondition.assertNotNull(value, "value");

        set(value.booleanValue());
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

        return compareAndSet(expectedValue, newValue.booleanValue());
    }

    public boolean compareAndSet(java.lang.Boolean expectedValue, boolean newValue)
    {
        PreCondition.assertNotNull(expectedValue, "expectedValue");
        PreCondition.assertNotEqual(expectedValue, newValue, "newValue");

        return compareAndSet(expectedValue.booleanValue(), newValue);
    }

    public boolean compareAndSet(java.lang.Boolean expectedValue, java.lang.Boolean newValue)
    {
        PreCondition.assertNotNull(expectedValue, "expectedValue");
        PreCondition.assertNotNull(newValue, "newValue");
        PreCondition.assertNotEqual(expectedValue, newValue, "newValue");

        return compareAndSet(expectedValue.booleanValue(), newValue.booleanValue());
    }

    @Override
    public void clear()
    {
        value.set(false);
        hasValue = false;
    }

    @Override
    public boolean equals(Object rhs)
    {
        boolean result = false;
        if (rhs != null)
        {
            if (rhs instanceof java.lang.Boolean)
            {
                result = equals((java.lang.Boolean)rhs);
            }
            else if (rhs instanceof BooleanValue)
            {
                result = equals((BooleanValue)rhs);
            }
        }
        return result;
    }

    public boolean equals(boolean rhs)
    {
        return hasValue() && getAsBoolean() == rhs;
    }

    public boolean equals(java.lang.Boolean rhs)
    {
        return rhs != null && hasValue() && getAsBoolean() == rhs.booleanValue();
    }

    public boolean equals(BooleanValue rhs)
    {
        return rhs != null &&
            hasValue() == rhs.hasValue() &&
            (!hasValue() || (value.get() == rhs.value.get()));
    }

    @Override
    public String toString()
    {
        return !hasValue() ? "no value" : value.toString();
    }
}
