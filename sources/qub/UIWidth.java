package qub;

public abstract class UIWidth
{
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
