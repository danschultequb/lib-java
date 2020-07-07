package qub;

/**
 * An individual component/element within a UI.
 */
public interface UIElement
{
    /**
     * Set the width of this Window.
     * @param width The width of this Window.
     * @return This object for method chaining.
     */
    UIElement setWidth(Distance width);

    /**
     * Get the width of this Window.
     * @return The width of this Window.
     */
    Distance getWidth();

    /**
     * Set the height of this Window.
     * @param height The height of this Window.
     * @return This object for method chaining.
     */
    UIElement setHeight(Distance height);

    /**
     * Get the height of this Window.
     * @return The height of this Window.
     */
    Distance getHeight();

    /**
     * Set the size of this Window.
     * @param size The size of this Window.
     * @return This object for method chaining.
     */
    default UIElement setSize(Size2D size)
    {
        PreCondition.assertNotNull(size, "size");

        return this.setSize(size.getWidth(), size.getHeight());
    }

    /**
     * Set the size of this Window.
     * @param width The width of this Window.
     * @param height The height of this Window.
     * @return This object for method chaining.
     */
    UIElement setSize(Distance width, Distance height);

    /**
     * Get the size of this Window.
     * @return The size of this Window.
     */
    Size2D getSize();

    /**
     * Register the provided callback to be invoked when this UIElement's size changes.
     * @param callback The callback to be invoked when this UIElement's size changes.
     * @return A Disposable that can be disposed to unregister the provided callback.
     */
    Disposable onSizeChanged(Action0 callback);

    /**
     * Get the background color of this UIElement.
     * @return The background color of this UIElement.
     */
    Color getBackgroundColor();

    /**
     * Set the background color of this UIElement.
     * @param backgroundColor The background color of this UIElement.
     * @return This object for method chaining.
     */
    UIElement setBackgroundColor(Color backgroundColor);
}
