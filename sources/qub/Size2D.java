package qub;

public class Size2D
{
    public static final Size2D zero = new Size2D(Distance.zero, Distance.zero);

    private final Distance width;
    private final Distance height;

    public Size2D(Distance width, Distance height)
    {
        PreCondition.assertGreaterThanOrEqualTo(width, Distance.zero, "width");
        PreCondition.assertGreaterThanOrEqualTo(height, Distance.zero, "height");

        this.width = width;
        this.height = height;
    }

    public Distance getWidth()
    {
        return width;
    }

    public Size2D changeWidth(Distance width)
    {
        PreCondition.assertGreaterThanOrEqualTo(width, Distance.zero, "width");

        final Size2D result = (this.width.equals(width) ? this : new Size2D(width, height));

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(width, result.getWidth(), "result.getWidth()");

        return result;
    }

    public Distance getHeight()
    {
        return height;
    }

    public Size2D changeHeight(Distance height)
    {
        PreCondition.assertGreaterThanOrEqualTo(height, Distance.zero, "height");

        final Size2D result = (this.height.equals(height) ? this : new Size2D(width, height));

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(height, result.getHeight(), "result.getHeight()");

        return result;
    }

    @Override
    public String toString()
    {
        return "{\"type\":\"Size2D\",\"width\":\"" + width + "\",\"height\":\"" + height + "\"}";
    }

    @Override
    public boolean equals(Object rhs)
    {
        return rhs instanceof Size2D && equals((Size2D)rhs);
    }

    public boolean equals(Size2D rhs)
    {
        return rhs != null &&
            width.equals(rhs.width) &&
            height.equals(rhs.height);
    }

    @Override
    public int hashCode()
    {
        return Hash.getHashCode(width, height);
    }
}
