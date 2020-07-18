package qub;

/**
 * Padding that can be added to a UIElement.
 */
public interface UIPadding
{
    /**
     * A UIPadding object with zero padding.
     */
    UIPadding zero = UIPadding.create();

    /**
     * Create a new MutableUIPadding object with zero padding.
     * @return A new MutableUIPadding object with zero padding.
     */
    static MutableUIPadding create()
    {
        return MutableUIPadding.create();
    }

    /**
     * Create a new MutableUIPadding object with the provided padding on all sides.
     * @param padding The padding to use for all sides.
     * @return A new MutableUIPadding object with the provided padding on all sides.
     */
    static MutableUIPadding create(Distance padding)
    {
        PreCondition.assertNotNull(padding, "padding");

        return MutableUIPadding.create(padding);
    }

    /**
     * Create a new MutableUIPadding object with the provided padding.
     * @param left The padding on the left side.
     * @param top The padding on the top side.
     * @param right The pdding on the right side.
     * @param bottom The padding on the bottom side.
     * @return A new MutableUIPadding object with the provided padding.
     */
    static MutableUIPadding create(Distance left, Distance top, Distance right, Distance bottom)
    {
        return MutableUIPadding.create(left, top, right, bottom);
    }

    /**
     * Get the padding on the left side.
     * @return The padding on the left side.
     */
    Distance getLeft();

    /**
     * Get the padding on the right side.
     * @return The padding on the right side.
     */
    Distance getRight();

    /**
     * Get the padding on the top side.
     * @return The padding on the top side.
     */
    Distance getTop();

    /**
     * Get the padding on the bottom side.
     * @return The padding on the bottom side.
     */
    Distance getBottom();

    /**
     * Get the sum of the left and right padding.
     * @return The sum of the left and right padding.
     */
    default Distance getWidth()
    {
        return this.getLeft().plus(this.getRight());
    }

    /**
     * Get the sum of the top and bottom padding.
     * @return The sum of the top and bottom padding.
     */
    default Distance getHeight()
    {
        return this.getTop().plus(this.getBottom());
    }

    /**
     * Get the String representation of the provided UIPadding.
     * @param uiPadding The UIPadding to get the String representation of.
     * @return The String representation of the provided UIPadding.
     */
    static String toString(UIPadding uiPadding)
    {
        PreCondition.assertNotNull(uiPadding, "uiPadding");

        final CharacterList list = CharacterList.create();

        list.add('{');
        list.addAll("\"left\":\"" + uiPadding.getLeft() + "\"");
        list.add(',');
        list.addAll("\"top\":\"" + uiPadding.getTop() + "\"");
        list.add(',');
        list.addAll("\"right\":\"" + uiPadding.getRight() + "\"");
        list.add(',');
        list.addAll("\"bottom\":\"" + uiPadding.getBottom() + "\"");
        list.add('}');

        final String result = list.toString(true);

        PostCondition.assertNotNullAndNotEmpty(result, "result");

        return result;
    }

    /**
     * Get whether or not the provided UIPadding is equal to the provided Object.
     * @param lhs The UIPadding to compare to the Object.
     * @param rhs The Object to compare to the UIPadding.
     * @return Whether or not the provided UIPadding is equal to the provided Object.
     */
    static boolean equals(UIPadding lhs, Object rhs)
    {
        return lhs != null && rhs instanceof UIPadding && lhs.equals((UIPadding)rhs);
    }

    /**
     * Get whether or not this UIPadding is equal to the provided UIPadding.
     * @param rhs The UIPadding to compare to this UIPadding.
     * @return Whether or not this UIPadding is equal to the provided UIPadding.
     */
    default boolean equals(UIPadding rhs)
    {
        return rhs != null &&
            this.getLeft().equals(rhs.getLeft()) &&
            this.getTop().equals(rhs.getTop()) &&
            this.getRight().equals(rhs.getRight()) &&
            this.getBottom().equals(rhs.getBottom());
    }
}
