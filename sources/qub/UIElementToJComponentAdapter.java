package qub;

import java.awt.*;

public class UIElementToJComponentAdapter extends javax.swing.JComponent
{
    private final UIElement uiElement;

    public UIElementToJComponentAdapter(UIElement uiElement)
    {
        PreCondition.assertNotNull(uiElement, "uiElement");

        this.uiElement = uiElement;
        this.enableEvents(AWTEvent.MOUSE_EVENT_MASK |
                          AWTEvent.COMPONENT_EVENT_MASK);
    }

    @Override
    public void paint(java.awt.Graphics graphics)
    {
        PreCondition.assertInstanceOf(graphics, java.awt.Graphics2D.class, "graphics");

        final Window parentWindow = uiElement.getParentWindow();
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
        super.processMouseEvent(e);

        uiElement.handleMouseEvent(new JavaMouseEvent(e));
    }
}
