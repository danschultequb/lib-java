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
        setWidth(UIWidth.fitContent);
        setHeight(UIHeight.fitContent);
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
    public UIText setPadding(Distance padding)
    {
        PreCondition.assertNullOrGreaterThanOrEqualTo(padding, Distance.zero, "padding");

        this.padding = padding;

        return this;
    }

    @Override
    public Distance getPadding()
    {
        return padding;
    }

    /**
     * Set the text that this UIText element will display.
     * @param text The text that this UIText element will display.
     */
    public UIText setText(String text)
    {
        this.text = text;
        return this;
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
    public UIText setFontSize(Distance fontSize)
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

        return this;
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
    public UIText setBackground(Color background)
    {
        this.background = background;
        return this;
    }

    @Override
    public void paint(UIPainter painter)
    {
        PreCondition.assertNotNull(painter, "painter");

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

        try (final Disposable paddingTranslation = painter.translate(padding, padding))
        {
            try (final Disposable fontSetting = painter.setFont(font))
            {
                painter.drawText(text);
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
    public UIText setWidth(Distance width)
    {
        PreCondition.assertGreaterThanOrEqualTo(width, Distance.zero, "width");

        return setWidth(UIWidth.fixed(width));
    }

    @Override
    public UIText setWidth(UIWidth width)
    {
        PreCondition.assertNotNull(width, "width");

        this.width = width;

        return this;
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
    public UIText setHeight(Distance height)
    {
        PreCondition.assertGreaterThanOrEqualTo(height, Distance.zero, "height");

        return setHeight(UIHeight.fixed(height));
    }

    @Override
    public UIText setHeight(UIHeight height)
    {
        PreCondition.assertNotNull(height, "height");

        this.height = height;

        return this;
    }

    @Override
    public UIText setSize(Distance width, Distance height)
    {
        setWidth(width);
        setHeight(height);

        return this;
    }

    @Override
    public UIElement setSize(Size2D size)
    {
        PreCondition.assertNotNull(size, "size");

        return setSize(size.getWidth(), size.getHeight());
    }

    @Override
    public Distance getContentWidth()
    {
        return contentSize == null ? null : contentSize.getWidth();
    }

    @Override
    public Distance getContentHeight()
    {
        return contentSize == null ? null : contentSize.getHeight();
    }

    @Override
    public Size2D getContentSize()
    {
        return contentSize;
    }
}
