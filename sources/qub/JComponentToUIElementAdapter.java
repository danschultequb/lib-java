package qub;

public class JComponentToUIElementAdapter implements UIElement
{
    private final javax.swing.JComponent jComponent;
    private UIElementParent parentElement;

    public JComponentToUIElementAdapter(javax.swing.JComponent jComponent)
    {
        this(null, jComponent);
    }

    public JComponentToUIElementAdapter(UIElementParent parentElement, javax.swing.JComponent jComponent)
    {
        PreCondition.assertNotNull(jComponent, "jComponent");

        this.jComponent = jComponent;
    }


    @Override
    public void paint(UIPainter painter)
    {
        PreCondition.assertNotNull(painter, "painter");

        jComponent.paint(new UIPainterToGraphics2DAdapter(painter));
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
}
