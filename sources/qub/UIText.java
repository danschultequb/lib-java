package qub;

public class UIText implements UIElement
{
    private UIElementParent parentElement;
    private Distance padding;
    private String text;

    public UIText(String text)
    {
        this(null, text);
    }

    public UIText(UIElementParent parentElement, String text)
    {
        PreCondition.assertNotNullAndNotEmpty(text, "text");

        setParent(parentElement);
        setText(text);
    }

    @Override
    public void setParent(UIElementParent parentElement)
    {
        this.parentElement = parentElement;
    }

    public Distance getPadding()
    {
        return padding;
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

    @Override
    public void paint(UIPainter painter)
    {
        final boolean applyPadding = padding.getValue() > 0;
        if (applyPadding)
        {
            painter.saveTransform();
            painter.translate(padding, padding);
        }
        try
        {
            painter.drawText(text, Distance.zero, Distance.zero);
        }
        finally
        {
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
