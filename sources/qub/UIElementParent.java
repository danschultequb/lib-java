package qub;

public interface UIElementParent
{
    /**
     * Indicate that this UIElementParent should be repainted.
     */
    void repaint();

    /**
     * Get this UIElementParent's parent.
     * @return This UIElementParent's parent.
     */
    UIElementParent getParentElement();

    /**
     * Get the Window that contains this UIElement. If this UIElement has not been assigned to a
     * Window, then null will be returned.
     * @return The Window that contains this UIElement, or null if this UIElement has not been
     * assigned to a Window.
     */
    Window getParentWindow();
}
