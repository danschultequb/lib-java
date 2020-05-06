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
    default UIElement setWidth(Distance width)
    {
        PreCondition.assertNotNull(width, "width");
        PreCondition.assertGreaterThanOrEqualTo(width, Distance.zero, "width");

        return this.setSize(width, this.getHeight());
    }

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
    default UIElement setHeight(Distance height)
    {
        PreCondition.assertNotNull(height, "height");
        PreCondition.assertGreaterThanOrEqualTo(height, Distance.zero, "height");

        return this.setSize(this.getWidth(), height);
    }

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
    default Size2D getSize()
    {
        return new Size2D(this.getWidth(), this.getHeight());
    }
}
