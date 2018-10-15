package qub;

/**
 * A Distance that is specifically for width within a UI context.
 */
public abstract class UIWidth
{
    /**
     * A UIWidth that returns a UIElement's content width plus its padding.
     */
    public static final UIWidth fitContent = new FitContentUIWidth();

    /**
     * Get a UIWidth that always returns the provided width.
     * @param width The provided width.
     * @return A UIWidth that always returns the provided width.
     */
    public static UIWidth fixed(Distance width)
    {
        return new FixedUIWidth(width);
    }

    /**
     * Get the width of the provided UIElement.
     * @param uiElement The UIElement to get the width of.
     * @return The width of the provided UIElement.
     */
    public abstract Distance getWidth(UIElement uiElement);
}
