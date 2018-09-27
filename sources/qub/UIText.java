package qub;

public class UIText implements UIElement
{
    private UIElementParent parentElement;
    private Distance padding;
    private String text;
    private Distance fontSize;

    public UIText(String text)
    {
        this(null, text);
    }

    public UIText(UIElementParent parentElement, String text)
    {
        PreCondition.assertNotNullAndNotEmpty(text, "text");

        setParent(parentElement);
        setPadding(Distance.inches(0));
        setText(text);
    }

    @Override
    public void setParent(UIElementParent parentElement)
    {
        this.parentElement = parentElement;
    }

    public void setPadding(Distance padding)
    {
        PreCondition.assertNotNull(padding, "padding");
        PreCondition.assertLessThanOrEqualTo(Distance.zero, padding, "padding");

        this.padding = padding;
    }

    public void setText(String text)
    {
        PreCondition.assertNotNullAndNotEmpty(text, "text");

        this.text = text;
    }

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
        PreCondition.assertNotNull(parentElement, "parentElement");

        return parentElement.getParentWindow();
    }

    @Override
    public void handleMouseEvent(MouseEvent event)
    {
    }
}
