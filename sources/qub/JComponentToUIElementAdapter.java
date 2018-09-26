package qub;

import java.awt.Rectangle;

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
        final javax.swing.JPanel container = new javax.swing.JPanel()
        {
            @Override
            public void repaint(long tm, int x, int y, int width, int height)
            {
                JComponentToUIElementAdapter.this.repaint();
            }

            @Override
            public void repaint(Rectangle r)
            {
                JComponentToUIElementAdapter.this.repaint();
            }

            @Override
            public void repaint()
            {
                JComponentToUIElementAdapter.this.repaint();
            }

            @Override
            public void repaint(long tm)
            {
                JComponentToUIElementAdapter.this.repaint();
            }

            @Override
            public void repaint(int x, int y, int width, int height)
            {
                JComponentToUIElementAdapter.this.repaint();
            }
        };
        container.add(jComponent);
    }

    @Override
    public void paint(UIPainter painter)
    {
        PreCondition.assertInstanceOf(painter, java.awt.Graphics2D.class, "painter");

        jComponent.paint((java.awt.Graphics2D)painter);
    }

    @Override
    public void repaint()
    {
        if (parentElement != null)
        {
            parentElement.repaint();
        }
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
        PreCondition.assertInstanceOf(event, JavaMouseEvent.class, "event");

        final java.awt.event.MouseEvent javaMouseEvent = ((JavaMouseEvent)event).getJavaEvent();
        javaMouseEvent.setSource(jComponent);
        jComponent.dispatchEvent(javaMouseEvent);
    }
}
