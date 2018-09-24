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
        final Class<?> awtContainerClass = java.awt.Container.class;
        try
        {
            final java.lang.reflect.Field parentField = awtContainerClass.getDeclaredField("parent");
            final boolean accessible = parentField.isAccessible();
            try
            {
                parentField.setAccessible(true);
                parentField.set(jComponent, new javax.swing.JComponent()
                {
                    @Override
                    public void repaint(long tm, int x, int y, int width, int height)
                    {
                        JComponentToUIElementAdapter.this.repaint();
                    }
                });
            }
            finally
            {
                if (!accessible)
                {
                    parentField.setAccessible(accessible);
                }
            }
        }
        catch (NoSuchFieldException | IllegalAccessException e)
        {
            jComponent.getClass();
        }
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
        PreCondition.assertInstanceOf(event, JavaMouseEvent.class, "event");

        final java.awt.event.MouseEvent javaMouseEvent = ((JavaMouseEvent)event).getJavaEvent();
        javaMouseEvent.setSource(jComponent);
        jComponent.dispatchEvent(javaMouseEvent);
    }
}
