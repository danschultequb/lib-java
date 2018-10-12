package qub;

public class UIText implements UIElement
{
    private UIElementParent parentElement;
    private Distance padding;
    private String text;
    private Font font;
    private UIWidth width;
    private UIHeight height;
    private Color background;
    private Size2D contentSize;

    public UIText()
    {
        this(null);
    }

    public UIText(String text)
    {
        setText(text);
        setWidth(Distance.inches(1));
        setHeight(Distance.inches(1));
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
        PreCondition.assertNullOrGreaterThanOrEqualTo(padding, Distance.zero, "padding");

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
        return font == null ? null : font.getSize();
    }

    /**
     * Set the size of the font that the text will be displayed with.
     * @param fontSize The size of the font that the text will be displayed with.
     */
    public void setFontSize(Distance fontSize)
    {
        PreCondition.assertNullOrGreaterThan(fontSize, Distance.zero, "fontSize");

        if (font == null)
        {
            font = new Font(fontSize);
        }
        else
        {
            font = font.changeSize(fontSize);
        }
    }

    /**
     * Get the background that will be painted behind the text.
     * @return The background that will be painted behind the text.
     */
    public Color getBackground()
    {
        return background;
    }

    /**
     * Set the background that will be painted behind the text.
     * @param background The background that will be painted behind the text.
     */
    public void setBackground(Color background)
    {
        this.background = background;
    }

    @Override
    public void paint(UIPainter painter)
    {
        PreCondition.assertNotNull(painter, "painter");

        final boolean applyPadding = (padding != null && padding.getValue() != 0);
        if (applyPadding)
        {
            painter.saveTransform();
            painter.translate(padding, padding);
        }

        final boolean applyFont = (font != null);
        if (applyFont)
        {
            painter.saveFont();
            painter.setFont(font);
        }

        final Distance width = getWidth();
        final Distance height = getHeight();
        final Color background = getBackground();
        if (width != null && height != null && background != null)
        {
            try (final Disposable savedColor = painter.saveColor())
            {
                painter.setColor(background);
                painter.fillRectangle(width, height);
            }
        }

        try
        {
            painter.drawText(text);
        }
        finally
        {
            if (applyFont)
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
    public void parentWindowChanged(Window previousParentWindow, Window newParentWindow)
    {
        if (newParentWindow != null)
        {
            contentSize = newParentWindow.getTextSize(text, font);
        }
    }

    /**
     * Set the width of this UIElement (including the padding).
     * @param width The width of this UIElement (including the padding).
     */
    public void setWidth(Distance width)
    {
        PreCondition.assertGreaterThanOrEqualTo(width, Distance.zero, "width");

        setWidth(UIWidth.fixed(width));
    }

    @Override
    public void setWidth(UIWidth width)
    {
        PreCondition.assertNotNull(width, "width");

        this.width = width;
    }

    /**
     * Get the width of this UIElement (including the padding).
     * @return The width of this UIElement (including the padding).
     */
    public Distance getWidth()
    {
        return width.getWidth(this);
    }

    @Override
    public Distance getHeight()
    {
        return height.getHeight(this);
    }

    @Override
    public void setHeight(Distance height)
    {
        PreCondition.assertGreaterThanOrEqualTo(height, Distance.zero, "height");

        setHeight(UIHeight.fixed(height));
    }

    @Override
    public void setHeight(UIHeight height)
    {
        PreCondition.assertNotNull(height, "height");

        this.height = height;
    }

    @Override
    public void setSize(Distance width, Distance height)
    {
        setWidth(width);
        setHeight(height);
    }

    public Distance getContentWidth()
    {
        return contentSize == null ? null : contentSize.getWidth();
    }

    public Distance getContentHeight()
    {
        return contentSize == null ? null : contentSize.getHeight();
    }

    public Size2D getContentSize()
    {
        return contentSize;
    }
}
