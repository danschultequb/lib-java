package qub;

public class UIText implements UIElement
{
    private UIElementParent parentElement;
    private String text;

    public UIText(UIElementParent parentElement, String text)
    {
        PreCondition.assertNotNullAndNotEmpty(text, "text");

        setParent(parentElement);
        setText(text);
    }

    public void setText(String text)
    {
        PreCondition.assertNotNullAndNotEmpty(text, "text");

        this.text = text;
    }

    @Override
    public void paint(UIPainter painter)
    {
        painter.drawText(text);
    }

    @Override
    public void repaint()
    {
        parentElement.repaint();
    }

    public void setParent(UIElementParent parentElement)
    {
        this.parentElement = parentElement;
    }
}
