package qub;

public class Font
{
    private final Distance size;

    public Font(Distance size)
    {
        PreCondition.assertGreaterThanOrEqualTo(size, Distance.zero, "size");

        this.size = size;
    }

    public Distance getSize()
    {
        return size;
    }

    /**
     * Get a new Font that is identical to this Font except that the size is the provided size.
     * @param size The size of the new Font.
     * @return The new Font with the provided size.
     */
    public Font changeSize(Distance size)
    {
        PreCondition.assertGreaterThanOrEqualTo(size, Distance.zero, "size");

        Font result = this;
        if (!this.size.equals(size))
        {
            result = new Font(size);
        }

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(result.getSize(), size, "result.getSize()");

        return result;
    }

    @Override
    public String toString()
    {
        return "{\"type\":\"Font\",\"size\":\"" + size + "\"}";
    }

    @Override
    public boolean equals(Object rhs)
    {
        return rhs instanceof Font && equals((Font)rhs);
    }

    public boolean equals(Font rhs)
    {
        return rhs != null &&
           size.equals(rhs.size);
    }

    @Override
    public int hashCode()
    {
        return Hash.getHashCode(size);
    }
}
