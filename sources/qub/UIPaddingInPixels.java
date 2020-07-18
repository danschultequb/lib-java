package qub;

/**
 * Padding that can be added to a UIElement in pixels.
 */
public interface UIPaddingInPixels
{
    /**
     * A UIPaddingInPixels object with zero padding.
     */
    UIPaddingInPixels zero = UIPaddingInPixels.create();

    /**
     * Create a new MutableUIPaddingInPixels object with zero padding.
     * @return A new MutableUIPaddingInPixels object with zero padding.
     */
    static MutableUIPaddingInPixels create()
    {
        return MutableUIPaddingInPixels.create();
    }

    /**
     * Create a new MutableUIPaddingInPixels object with the provided padding on all sides.
     * @param padding The padding to use for all sides.
     * @return A new MutableUIPaddingInPixels object with the provided padding on all sides.
     */
    static MutableUIPaddingInPixels create(int paddingInPixels)
    {
        PreCondition.assertNotNull(paddingInPixels, "paddingInPixels");

        return MutableUIPaddingInPixels.create(paddingInPixels);
    }

    /**
     * Create a new MutableUIPaddingInPixels object with the provided padding.
     * @param leftInPixels The padding on the left side in pixels.
     * @param topInPixels The padding on the top side.
     * @param rightInPixels The pdding on the right side.
     * @param bottomInPixels The padding on the bottom side.
     * @return A new MutableUIPaddingInPixels object with the provided padding.
     */
    static MutableUIPaddingInPixels create(int leftInPixels, int topInPixels, int rightInPixels, int bottomInPixels)
    {
        return MutableUIPaddingInPixels.create(leftInPixels, topInPixels, rightInPixels, bottomInPixels);
    }

    /**
     * Get the padding on the left side.
     * @return The padding on the left side.
     */
    int getLeft();

    /**
     * Get the padding on the right side.
     * @return The padding on the right side.
     */
    int getRight();

    /**
     * Get the padding on the top side.
     * @return The padding on the top side.
     */
    int getTop();

    /**
     * Get the padding on the bottom side.
     * @return The padding on the bottom side.
     */
    int getBottom();

    /**
     * Get the sum of the left and right padding.
     * @return The sum of the left and right padding.
     */
    default int getWidth()
    {
        return this.getLeft() + this.getRight();
    }

    /**
     * Get the sum of the top and bottom padding.
     * @return The sum of the top and bottom padding.
     */
    default int getHeight()
    {
        return this.getTop() + this.getBottom();
    }

    /**
     * Get the String representation of the provided UIPaddingInPixels.
     * @param padding The UIPaddingInPixels to get the String representation of.
     * @return The String representation of the provided UIPaddingInPixels.
     */
    static String toString(UIPaddingInPixels padding)
    {
        PreCondition.assertNotNull(padding, "padding");

        final CharacterList list = CharacterList.create();

        list.add('{');
        list.addAll("\"left\":" + padding.getLeft());
        list.add(',');
        list.addAll("\"top\":" + padding.getTop());
        list.add(',');
        list.addAll("\"right\":" + padding.getRight());
        list.add(',');
        list.addAll("\"bottom\":" + padding.getBottom());
        list.add('}');

        final String result = list.toString(true);

        PostCondition.assertNotNullAndNotEmpty(result, "result");

        return result;
    }

    /**
     * Get whether or not the provided UIPaddingInPixels is equal to the provided Object.
     * @param lhs The UIPaddingInPixels to compare to the Object.
     * @param rhs The Object to compare to the UIPaddingInPixels.
     * @return Whether or not the provided UIPaddingInPixels is equal to the provided Object.
     */
    static boolean equals(UIPaddingInPixels lhs, Object rhs)
    {
        return lhs != null && rhs instanceof UIPaddingInPixels && lhs.equals((UIPaddingInPixels)rhs);
    }

    /**
     * Get whether or not this UIPaddingInPixels is equal to the provided UIPaddingInPixels.
     * @param rhs The UIPaddingInPixels to compare to this UIPaddingInPixels.
     * @return Whether or not this UIPaddingInPixels is equal to the provided UIPaddingInPixels.
     */
    default boolean equals(UIPaddingInPixels rhs)
    {
        return rhs != null &&
            this.getLeft() == rhs.getLeft() &&
            this.getTop() == rhs.getTop() &&
            this.getRight() == rhs.getRight() &&
            this.getBottom() == rhs.getBottom();
    }
}
