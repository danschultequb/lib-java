package qub;

public class MutableInteger implements Comparable<java.lang.Integer>
{
    private int value;

    public MutableInteger()
    {
        this.value = 0;
    }

    public MutableInteger(int value)
    {
        this.value = value;
    }

    public MutableInteger(java.lang.Integer value)
    {
        PreCondition.assertNotNull(value, "value");

        this.value = value;
    }

    public int get()
    {
        return value;
    }

    public void plusAssign(java.lang.Integer rhs)
    {
        PreCondition.assertNotNull(rhs, "rhs");

        value += rhs;
    }

    @Override
    public Comparison compareTo(Integer value)
    {
        return value == null ? Comparison.GreaterThan : Comparison.from(this.value - value);
    }
}
