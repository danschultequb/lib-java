package qub;

/**
 * A button element within a UI.
 */
public interface UIButton extends UIElement
{
    @Override
    default UIButton setWidth(Distance width)
    {
        return (UIButton)UIElement.super.setWidth(width);
    }

    @Override
    default UIButton setHeight(Distance height)
    {
        return (UIButton)UIElement.super.setHeight(height);
    }

    @Override
    default UIButton setSize(Size2D size)
    {
        return (UIButton)UIElement.super.setSize(size);
    }

    @Override
    UIButton setSize(Distance width, Distance height);

    /**
     * Get the text of this button.
     * @return The text of this button.
     */
    String getText();

    /**
     * Set the text of this button.
     * @param text The text of this button.
     * @return This object for method chaining.
     */
    UIButton setText(String text);

    /**
     * Get the size of the font used to display this text.
     * @return The size of the font used to display this text.
     */
    Distance getFontSize();

    /**
     * Set the size of the font used to display this text.
     * @param fontSize The size of the font used to display this text.
     * @return This object for method chaining.
     */
    UIButton setFontSize(Distance fontSize);

    /**
     * Register the provided callback to be invoked when this button is clicked.
     * @param callback The callback to invoke when this button is clicked.
     * @return A Disposable that can be disposed to unregister the provided callback.
     */
    Disposable onClick(Action0 callback);
}
