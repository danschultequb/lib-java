package qub;

public interface UIElement
{
    /**
     * Draw this UIElement to the provided painter's drawing surface.
     * @param painter The UIPainter to use to draw this UIElement.
     */
    void paint(UIPainter painter);

    /**
     * Indicate to the running process that this UIElement needs to be redrawn.
     */
    void repaint();

    /**
     * Set the parent element of this UIElement.
     * @param parentElement The new parent element of this UIElement.
     */
    void setParent(UIElementParent parentElement);
}
