package qub;

/**
 * A Random implementation that always returns the same value.
 */
public class FixedRandom implements Random
{
    private int value;

    /**
     * Create a new FixedRandom object.
     * @param value The fixed value that will be returned.
     */
    public FixedRandom(int value)
    {
        setValue(value);
    }

    /**
     * Set the value that will be returned.
     * @param value The value that will be returned.
     */
    public void setValue(int value)
    {
        this.value = value;
    }

    @Override
    public int getInteger()
    {
        return value;
    }
}
