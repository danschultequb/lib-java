package qub;

public class UIElementToJComponentAdapter extends javax.swing.JComponent
{
    private final UIElement uiElement;

    public UIElementToJComponentAdapter(UIElement uiElement)
    {
        PreCondition.assertNotNull(uiElement, "uiElement");

        this.uiElement = uiElement;
        this.enableEvents(java.awt.AWTEvent.MOUSE_EVENT_MASK |
                          java.awt.AWTEvent.COMPONENT_EVENT_MASK);
    }

    @Override
    public void paint(java.awt.Graphics graphics)
    {
        PreCondition.assertInstanceOf(graphics, java.awt.Graphics2D.class, "graphics");

        final JavaWindow parentWindow = (JavaWindow)uiElement.getParentWindow();
        final Graphics2DUIPainter painter = new Graphics2DUIPainter((java.awt.Graphics2D)graphics, parentWindow);

        super.paint(graphics);
        uiElement.paint(painter);
    }

    @Override
    protected void processComponentEvent(java.awt.event.ComponentEvent e)
    {
        super.processComponentEvent(e);
    }

    @Override
    protected void processMouseEvent(java.awt.event.MouseEvent e)
    {
//        uiElement.handleMouseEvent(new JavaMouseEvent(e));
    }
}
