package qub;

public class UIText implements UIElement
{
    private UIElementParent parentElement;
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

    public void setText(String text)
    {
        PreCondition.assertNotNullAndNotEmpty(text, "text");

        this.text = text;
    }

    @Override
    public void paint(UIPainter painter)
    {
        painter.drawText("Apples", Distance.inches(1), Distance.inches(2));
        painter.drawText("Bananas", Distance.inches(0.5), Distance.inches(1));
    }

    @Override
    public void repaint()
    {
        PreCondition.assertNotNull(parentElement, "parentElement");

        parentElement.repaint();
    }

    @Override
    public void setParent(UIElementParent parentElement)
    {
        this.parentElement = parentElement;
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
