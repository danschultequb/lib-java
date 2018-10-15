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
     * A function that is called when the UIElement's parent window changes.
     * @param previousParentWindow The previous parent window.
     * @param newParentWindow The new parent window.
     */
    void parentWindowChanged(Window previousParentWindow, Window newParentWindow);

    /**
     * Respond to a mouse-related event.
     * @param event The mouse-related event.
     */
//    void handleMouseEvent(MouseEvent event);

    /**
     * Get the padding that will be added to the UIElement's width.
     * @return The padding that will be added to the UIElement's width.
     */
    Distance getPadding();

    /**
     * Set the padding that will be added to each side of this UIElement.
     * @param padding The padding that will be added to each side of this UIElement.
     * @return This UIElement.
     */
    UIElement setPadding(Distance padding);

    /**
     * Get the width of this UIElement.
     * @return The width of this UIElement.
     */
    Distance getWidth();

    /**
     * Set the width of this UIelement.
     * @param width The new fixed width of this UIElement.
     * @return This UIElement
     */
    UIElement setWidth(Distance width);

    /**
     * Set the width of this UIElement.
     * @param width The new width of this UIElement.
     * @return This UIElement
     */
    UIElement setWidth(UIWidth width);

    /**
     * Get the height of this UIElement.
     * @return The height of this UIElement.
     */
    Distance getHeight();

    /**
     * Set the height of this UIElement.
     * @param height The new fixed height of this UIElement.
     * @return This UIElement
     */
    UIElement setHeight(Distance height);

    /**
     * Set the height of this UIElement.
     * @param height The new height of this UIElement.
     * @return This UIElement
     */
    UIElement setHeight(UIHeight height);

    /**
     * Set the width and height of this UIElement.
     * @param width The new width of this UIElement.
     * @param height The new height of this UIElement.
     * @return This UIElement
     */
    UIElement setSize(Distance width, Distance height);

    /**
     * Set the size of this UIElement.
     * @param size The new size of this UIElement
     * @return This UIElement.
     */
    UIElement setSize(Size2D size);

    /**
     * Get the width of this UIElement's content.
     * @return The width of this UIElement's content.
     */
    Distance getContentWidth();

    /**
     * Get the height of this UIElement's content.
     * @return The height of this UIElement's content.
     */
    Distance getContentHeight();

    /**
     * Get the size of this UIElement's content.
     * @return The size of this UIElement's content.
     */
    Size2D getContentSize();
}
