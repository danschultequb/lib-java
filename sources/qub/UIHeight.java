package qub;

public abstract class UIHeight
{
    public static final UIHeight fitContent = new FitContentUIHeight();

    public static UIHeight fixed(Distance height)
    {
        return new FixedUIHeight(height);
    }

    /**
     * Get the height of the provided UIElement.
     * @param uiElement The UIElement to get the height of.
     * @return The height of the provided UIElement.
     */
    public abstract Distance getHeight(UIElement uiElement);
}
