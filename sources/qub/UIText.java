package qub;

public class UIText implements UIElement
{
    private UIElementParent parentElement;
    private Distance padding;
    private String text;
    private Distance fontSize;
    private Distance width;

    public UIText(String text)
    {
        PreCondition.assertNotNull(text, "text");

        setPadding(Distance.zero);
        setText(text);
        setWidth(Distance.zero);
    }

    @Override
    public void setParent(UIElementParent parentElement)
    {
        this.parentElement = parentElement;
    }

    /**
     * Get the UIElement that contains this UIElement.
     * @return The UIElement that contains this UIElement.
     */
    public UIElementParent getParent()
    {
        return parentElement;
    }

    /**
     * Set the size of the padding that will surround the displayed text.
     * @param padding The size of the padding that will surround the displayed text.
     */
    public void setPadding(Distance padding)
    {
        PreCondition.assertNotNull(padding, "padding");
        PreCondition.assertLessThanOrEqualTo(Distance.zero, padding, "padding");

        this.padding = padding;
    }

    /**
     * Get the size of the padding that will surround the displayed text.
     * @return The size of the padding that will surround the displayed text.
     */
    public Distance getPadding()
    {
        return padding;
    }

    /**
     * Set the text that this UIText element will display.
     * @param text The text that this UIText element will display.
     */
    public void setText(String text)
    {
        PreCondition.assertNotNull(text, "text");

        this.text = text;
    }

    /**
     * Get the text that this UIText element will display.
     * @return The text that this UIText element will display.
     */
    public String getText()
    {
        return text;
    }

    /**
     * Get the size of the font that the text will be displayed with.
     * @return The size of the font that the text will be displayed with.
     */
    public Distance getFontSize()
    {
        return fontSize;
    }

    /**
     * Set the size of the font that the text will be displayed with.
     * @param fontSize The size of the font that the text will be displayed with.
     */
    public void setFontSize(Distance fontSize)
    {
        PreCondition.assertTrue(fontSize == null || fontSize.getValue() > 0, "fontSize == null || fontSize.getValue() > 0");

        this.fontSize = fontSize;
    }

    @Override
    public void paint(UIPainter painter)
    {
        final boolean applyPadding = (padding != null && padding.getValue() != 0);
        if (applyPadding)
        {
            painter.saveTransform();
            painter.translate(padding, padding);
        }

        final boolean applyFontSize = (fontSize != null);
        if (applyFontSize)
        {
            painter.saveFont();
            painter.setFontSize(fontSize);
        }

        try
        {
            painter.drawText(text);
        }
        finally
        {
            if (applyFontSize)
            {
                painter.restoreFont();
            }

            if (applyPadding)
            {
                painter.restoreTransform();
            }
        }
    }

    @Override
    public void repaint()
    {
        PreCondition.assertNotNull(parentElement, "parentElement");

        parentElement.repaint();
    }

    @Override
    public Window getParentWindow()
    {
        return parentElement == null ? null : parentElement.getParentWindow();
    }

    @Override
    public void handleMouseEvent(MouseEvent event)
    {
    }

    /**
     * Set the width of this UIElement (including the padding).
     * @param width The width of this UIElement (including the padding).
     */
    public void setWidth(Distance width)
    {
        PreCondition.assertGreaterThanOrEqualTo(width, Distance.zero, "width");

        this.width = width;
    }

    /**
     * Get the width of this UIElement (including the padding).
     * @return The width of this UIElement (including the padding).
     */
    public Distance getWidth()
    {
        return width;
    }
}
