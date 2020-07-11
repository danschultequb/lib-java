package qub;

/**
 * Padding that can be added to a UIElement.
 */
public class MutableUIPadding implements UIPadding
{
    private Distance left;
    private Distance top;
    private Distance right;
    private Distance bottom;

    private MutableUIPadding(Distance left, Distance top, Distance right, Distance bottom)
    {
        this.setLeft(left);
        this.setTop(top);
        this.setRight(right);
        this.setBottom(bottom);
    }

    /**
     * Create a new MutableUIPadding object with zero padding.
     * @return A new MutableUIPadding object with zero padding.
     */
    public static MutableUIPadding create()
    {
        return MutableUIPadding.create(Distance.zero);
    }

    /**
     * Create a new MutableUIPadding object with the provided padding on all sides.
     * @param padding The padding to use for all sides.
     * @return A new MutableUIPadding object with the provided padding on all sides.
     */
    public static MutableUIPadding create(Distance padding)
    {
        PreCondition.assertNotNull(padding, "padding");

        return MutableUIPadding.create(padding, padding, padding, padding);
    }

    /**
     * Create a new MutableUIPadding object with the provided padding.
     * @param left The padding on the left side.
     * @param top The padding on the top side.
     * @param right The pdding on the right side.
     * @param bottom The padding on the bottom side.
     * @return A new MutableUIPadding object with the provided padding.
     */
    public static MutableUIPadding create(Distance left, Distance top, Distance right, Distance bottom)
    {
        return new MutableUIPadding(left, top, right, bottom);
    }

    @Override
    public Distance getLeft()
    {
        return this.left;
    }

    /**
     * Set the padding on the left side.
     * @param left The padding on the left side.
     * @return This object for method chaining.
     */
    public MutableUIPadding setLeft(Distance left)
    {
        PreCondition.assertNotNull(left, "left");

        this.left = left;

        return this;
    }

    @Override
    public Distance getRight()
    {
        return this.right;
    }

    /**
     * Set the padding on the right side.
     * @param right The padding on the right side.
     * @return This object for method chaining.
     */
    public MutableUIPadding setRight(Distance right)
    {
        PreCondition.assertNotNull(right, "right");

        this.right = right;

        return this;
    }

    @Override
    public Distance getTop()
    {
        return this.top;
    }

    /**
     * Set the padding on the top side.
     * @param top The padding on the top side.
     * @return This object for method chaining.
     */
    public MutableUIPadding setTop(Distance top)
    {
        PreCondition.assertNotNull(top, "top");

        this.top = top;

        return this;
    }

    @Override
    public Distance getBottom()
    {
        return this.bottom;
    }

    /**
     * Set the padding on the bottom side.
     * @param bottom The padding on the bottom side.
     * @return This object for method chaining.
     */
    public MutableUIPadding setBottom(Distance bottom)
    {
        PreCondition.assertNotNull(bottom, "bottom");

        this.bottom = bottom;

        return this;
    }

    @Override
    public String toString()
    {
        return UIPadding.toString(this);
    }

    @Override
    public boolean equals(Object rhs)
    {
        return UIPadding.equals(this, rhs);
    }
}
