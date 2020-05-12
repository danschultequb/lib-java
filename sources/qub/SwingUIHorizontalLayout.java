package qub;

/**
 * A UIHorizontalLayout that displays other SwingUIElements in a horizontal stack.
 */
public class SwingUIHorizontalLayout implements UIHorizontalLayout, JavaUIElement
{
    private final javax.swing.JPanel jPanel;
    private final JavaUIBase uiBase;
    private final List<JavaUIElement> elements;
    private HorizontalDirection direction;

    private SwingUIHorizontalLayout(JavaUIBase uiBase)
    {
        PreCondition.assertNotNull(uiBase, "uiBase");

        this.uiBase = uiBase;
        this.jPanel = new javax.swing.JPanel();
        this.jPanel.setLayout(new javax.swing.BoxLayout(this.jPanel, javax.swing.BoxLayout.LINE_AXIS));
        this.elements = List.create();
        this.direction = HorizontalDirection.LeftToRight;
    }

    public static SwingUIHorizontalLayout create(JavaUIBase base)
    {
        return new SwingUIHorizontalLayout(base);
    }

    public static SwingUIHorizontalLayout create(Display display, AsyncRunner asyncRunner)
    {
        return SwingUIHorizontalLayout.create(JavaUIBase.create(display, asyncRunner));
    }

    @Override
    public javax.swing.JPanel getComponent()
    {
        return this.jPanel;
    }

    @Override
    public SwingUIHorizontalLayout setWidth(Distance width)
    {
        return (SwingUIHorizontalLayout)UIHorizontalLayout.super.setWidth(width);
    }

    @Override
    public Distance getWidth()
    {
        return this.uiBase.getWidth(this.jPanel);
    }

    @Override
    public SwingUIHorizontalLayout setHeight(Distance height)
    {
        return (SwingUIHorizontalLayout)UIHorizontalLayout.super.setHeight(height);
    }

    @Override
    public Distance getHeight()
    {
        return this.uiBase.getHeight(this.jPanel);
    }

    @Override
    public SwingUIHorizontalLayout setSize(Size2D size)
    {
        return (SwingUIHorizontalLayout)UIHorizontalLayout.super.setSize(size);
    }

    @Override
    public SwingUIHorizontalLayout setSize(Distance width, Distance height)
    {
        this.uiBase.setSize(this.jPanel, width, height);
        return this;
    }

    @Override
    public Disposable onSizeChanged(Action0 callback)
    {
        return this.uiBase.onSizeChanged(this.jPanel, callback);
    }

    @Override
    public SwingUIHorizontalLayout setDirection(HorizontalDirection direction)
    {
        PreCondition.assertNotNull(direction, "direction");

        if (this.direction != direction)
        {
            this.jPanel.removeAll();

            this.direction = direction;

            if (this.direction == HorizontalDirection.RightToLeft)
            {
                this.jPanel.add(javax.swing.Box.createHorizontalGlue(), 0);
            }
            for (final JavaUIElement element : this.elements)
            {
                this.jPanel.add(element.getComponent());
            }
            this.jPanel.revalidate();
        }

        return this;
    }

    @Override
    public HorizontalDirection getDirection()
    {
        return this.direction;
    }

    @Override
    public SwingUIHorizontalLayout add(UIElement uiElement)
    {
        PreCondition.assertNotNull(uiElement, "uiElement");
        PreCondition.assertInstanceOf(uiElement, JavaUIElement.class, "uiElement");

        final JavaUIElement javaUIElement = (JavaUIElement)uiElement;
        this.elements.add(javaUIElement);

        final java.awt.Component component = javaUIElement.getComponent();
        this.jPanel.add(component);
        this.jPanel.revalidate();

        return this;
    }

    @Override
    public SwingUIHorizontalLayout addAll(UIElement... uiElements)
    {
        return (SwingUIHorizontalLayout)UIHorizontalLayout.super.addAll(uiElements);
    }

    @Override
    public SwingUIHorizontalLayout addAll(Iterable<? extends UIElement> uiElements)
    {
        return (SwingUIHorizontalLayout)UIHorizontalLayout.super.addAll(uiElements);
    }
}
