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

    /**
     * Get the Window that contains this UIElement. If this UIElement has not been assigned to a
     * Window, then null will be returned.
     * @return The Window that contains this UIElement, or null if this UIElement has not been
     * assigned to a Window.
     */
    Window getParentWindow();

    /**
     * Respond to a mouse-related event.
     * @param event The mouse-related event.
     */
//    void handleMouseEvent(MouseEvent event);

    /**
     * Get the width of this UIElement.
     * @return The width of this UIElement.
     */
    Distance getWidth();

    /**
     * Set the width of this UIelement.
     * @param width The new fixed width of this UIElement.
     */
    void setWidth(Distance width);

    /**
     * Set the width of this UIElement.
     * @param width The new width of this UIElement.
     */
    void setWidth(UIWidth width);

    /**
     * Get the height of this UIElement.
     * @return The height of this UIElement.
     */
    Distance getHeight();

    /**
     * Set the height of this UIElement.
     * @param height The new fixed height of this UIElement.
     */
    void setHeight(Distance height);

    /**
     * Set the height of this UIElement.
     * @param height The new height of this UIElement.
     */
    void setHeight(UIHeight height);
}
