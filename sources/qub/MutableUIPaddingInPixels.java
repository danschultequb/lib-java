package qub;

/**
 * Padding that can be added to a UIElement.
 */
public class MutableUIPaddingInPixels implements UIPaddingInPixels
{
    private int left;
    private int top;
    private int right;
    private int bottom;

    private MutableUIPaddingInPixels(int left, int top, int right, int bottom)
    {
        this.setLeft(left);
        this.setTop(top);
        this.setRight(right);
        this.setBottom(bottom);
    }

    /**
     * Create a new MutableUIPaddingInPixels object with zero padding.
     * @return A new MutableUIPaddingInPixels object with zero padding.
     */
    public static MutableUIPaddingInPixels create()
    {
        return MutableUIPaddingInPixels.create(0);
    }

    /**
     * Create a new MutableUIPaddingInPixels object with the provided padding on all sides.
     * @param padding The padding to use for all sides.
     * @return A new MutableUIPaddingInPixels object with the provided padding on all sides.
     */
    public static MutableUIPaddingInPixels create(int padding)
    {
        PreCondition.assertNotNull(padding, "padding");

        return MutableUIPaddingInPixels.create(padding, padding, padding, padding);
    }

    /**
     * Create a new MutableUIPaddingInPixels object with the provided padding.
     * @param left The padding on the left side.
     * @param top The padding on the top side.
     * @param right The padding on the right side.
     * @param bottom The padding on the bottom side.
     * @return A new MutableUIPaddingInPixels object with the provided padding.
     */
    public static MutableUIPaddingInPixels create(int left, int top, int right, int bottom)
    {
        return new MutableUIPaddingInPixels(left, top, right, bottom);
    }

    @Override
    public int getLeft()
    {
        return this.left;
    }

    /**
     * Set the padding on the left side.
     * @param left The padding on the left side.
     * @return This object for method chaining.
     */
    public MutableUIPaddingInPixels setLeft(int left)
    {
        PreCondition.assertNotNull(left, "left");

        this.left = left;

        return this;
    }

    @Override
    public int getRight()
    {
        return this.right;
    }

    /**
     * Set the padding on the right side.
     * @param right The padding on the right side.
     * @return This object for method chaining.
     */
    public MutableUIPaddingInPixels setRight(int right)
    {
        PreCondition.assertNotNull(right, "right");

        this.right = right;

        return this;
    }

    @Override
    public int getTop()
    {
        return this.top;
    }

    /**
     * Set the padding on the top side.
     * @param top The padding on the top side.
     * @return This object for method chaining.
     */
    public MutableUIPaddingInPixels setTop(int top)
    {
        PreCondition.assertNotNull(top, "top");

        this.top = top;

        return this;
    }

    @Override
    public int getBottom()
    {
        return this.bottom;
    }

    /**
     * Set the padding on the bottom side.
     * @param bottom The padding on the bottom side.
     * @return This object for method chaining.
     */
    public MutableUIPaddingInPixels setBottom(int bottom)
    {
        PreCondition.assertNotNull(bottom, "bottom");

        this.bottom = bottom;

        return this;
    }

    @Override
    public String toString()
    {
        return UIPaddingInPixels.toString(this);
    }

    @Override
    public boolean equals(Object rhs)
    {
        return UIPaddingInPixels.equals(this, rhs);
    }
}
