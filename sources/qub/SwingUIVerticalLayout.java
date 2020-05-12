package qub;

/**
 * A UIVerticalLayout that displays other SwingUIElements in a vertical stack.
 */
public class SwingUIVerticalLayout implements UIVerticalLayout, JavaUIElement
{
    private final JavaUIBase uiBase;
    private final javax.swing.JPanel jPanel;
    private final List<JavaUIElement> elements;
    private VerticalDirection direction;

    private SwingUIVerticalLayout(JavaUIBase uiBase)
    {
        PreCondition.assertNotNull(uiBase, "uiBase");

        this.uiBase = uiBase;
        this.jPanel = new javax.swing.JPanel();
        this.jPanel.setLayout(new javax.swing.BoxLayout(this.jPanel, javax.swing.BoxLayout.PAGE_AXIS));
        this.elements = List.create();
        this.direction = VerticalDirection.TopToBottom;
    }

    public static SwingUIVerticalLayout create(JavaUIBase base)
    {
        return new SwingUIVerticalLayout(base);
    }

    public static SwingUIVerticalLayout create(Display display, AsyncRunner asyncRunner)
    {
        return SwingUIVerticalLayout.create(JavaUIBase.create(display, asyncRunner));
    }

    @Override
    public javax.swing.JPanel getComponent()
    {
        return this.jPanel;
    }

    @Override
    public SwingUIVerticalLayout setWidth(Distance width)
    {
        return (SwingUIVerticalLayout)UIVerticalLayout.super.setWidth(width);
    }

    @Override
    public Distance getWidth()
    {
        return this.uiBase.getWidth(this.jPanel);
    }

    @Override
    public SwingUIVerticalLayout setHeight(Distance height)
    {
        return (SwingUIVerticalLayout)UIVerticalLayout.super.setHeight(height);
    }

    @Override
    public Distance getHeight()
    {
        return this.uiBase.getHeight(this.jPanel);
    }

    @Override
    public SwingUIVerticalLayout setSize(Size2D size)
    {
        return (SwingUIVerticalLayout)UIVerticalLayout.super.setSize(size);
    }

    @Override
    public SwingUIVerticalLayout setSize(Distance width, Distance height)
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
    public SwingUIVerticalLayout setDirection(VerticalDirection direction)
    {
        PreCondition.assertNotNull(direction, "direction");

        if (this.direction != direction)
        {
            this.jPanel.removeAll();

            this.direction = direction;
            if (this.direction == VerticalDirection.BottomToTop)
            {
                this.jPanel.add(javax.swing.Box.createVerticalGlue(), 0);
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
    public VerticalDirection getDirection()
    {
        return this.direction;
    }

    @Override
    public SwingUIVerticalLayout add(UIElement uiElement)
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
    public SwingUIVerticalLayout addAll(UIElement... uiElements)
    {
        return (SwingUIVerticalLayout)UIVerticalLayout.super.addAll(uiElements);
    }

    @Override
    public SwingUIVerticalLayout addAll(Iterable<? extends UIElement> uiElements)
    {
        return (SwingUIVerticalLayout)UIVerticalLayout.super.addAll(uiElements);
    }
}
