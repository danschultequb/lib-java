package qub;

public interface UIText extends UIElement
{
    @Override
    default UIText setWidth(Distance width)
    {
        return (UIText)UIElement.super.setWidth(width);
    }

    @Override
    default UIText setHeight(Distance height)
    {
        return (UIText)UIElement.super.setHeight(height);
    }

    @Override
    default UIText setSize(Size2D size)
    {
        return (UIText)UIElement.super.setSize(size);
    }

    @Override
    UIText setSize(Distance width, Distance height);

    /**
     * Get the text of this element.
     * @return The text of this element.
     */
    String getText();

    /**
     * Set the text of this element.
     * @param text The text of this element.
     * @return This object for method chaining.
     */
    UIText setText(String text);

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
    UIText setFontSize(Distance fontSize);
}
