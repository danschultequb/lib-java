package qub;

/**
 * A UIVerticalLayout that displays other SwingUIElements in a vertical stack.
 */
public class SwingUIVerticalLayout implements UIVerticalLayout, SwingUIElement
{
    private final javax.swing.JPanel jPanel;
    private final Display display;
    private final List<SwingUIElement> elements;
    private VerticalDirection direction;

    private SwingUIVerticalLayout(Display display)
    {
        PreCondition.assertNotNull(display, "display");

        this.display = display;
        this.elements = List.create();
        this.direction = VerticalDirection.TopToBottom;

        this.jPanel = new javax.swing.JPanel();
        final javax.swing.BoxLayout boxLayout = new javax.swing.BoxLayout(this.jPanel, javax.swing.BoxLayout.PAGE_AXIS);
        this.jPanel.setLayout(boxLayout);
        this.jPanel.setBackground(java.awt.Color.CYAN);
    }

    public static SwingUIVerticalLayout create(Display display)
    {
        return new SwingUIVerticalLayout(display);
    }

    @Override
    public javax.swing.JPanel getJComponent()
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
        final int widthInPixels = this.getJComponent().getWidth();
        final Distance result = this.display.convertHorizontalPixelsToDistance(widthInPixels);

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertGreaterThanOrEqualTo(result, Distance.zero, "result");

        return result;
    }

    @Override
    public SwingUIVerticalLayout setHeight(Distance height)
    {
        return (SwingUIVerticalLayout)UIVerticalLayout.super.setHeight(height);
    }

    @Override
    public Distance getHeight()
    {
        final int heightInPixels = this.getJComponent().getHeight();
        final Distance result = this.display.convertVerticalPixelsToDistance(heightInPixels);

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertGreaterThanOrEqualTo(result, Distance.zero, "result");

        return result;
    }

    @Override
    public SwingUIVerticalLayout setSize(Size2D size)
    {
        return (SwingUIVerticalLayout)UIVerticalLayout.super.setSize(size);
    }

    @Override
    public SwingUIVerticalLayout setSize(Distance width, Distance height)
    {
        PreCondition.assertNotNull(width, "width");
        PreCondition.assertGreaterThanOrEqualTo(width, Distance.zero, "width");
        PreCondition.assertNotNull(height, "height");
        PreCondition.assertGreaterThanOrEqualTo(height, Distance.zero, "height");

        final double widthInPixels = this.display.convertHorizontalDistanceToPixels(width);
        final double heightInPixels = this.display.convertVerticalDistanceToPixels(height);
        this.getJComponent().setSize((int)widthInPixels, (int)heightInPixels);

        return this;
    }

    @Override
    public SwingUIVerticalLayout setDirection(VerticalDirection direction)
    {
        PreCondition.assertNotNull(direction, "direction");

        if (this.direction != direction)
        {
            this.jPanel.removeAll();

            this.direction = direction;
            if (this.direction == VerticalDirection.TopToBottom)
            {
                for (final SwingUIElement element : this.elements)
                {
                    this.jPanel.add(element.getJComponent());
                }
            }
            else
            {
                for (final SwingUIElement element : this.elements)
                {
                    this.jPanel.add(element.getJComponent(), 0);
                }
                this.jPanel.add(javax.swing.Box.createVerticalGlue(), 0);
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
    public SwingUIVerticalLayout add(UIElement element)
    {
        PreCondition.assertNotNull(element, "element");
        PreCondition.assertInstanceOf(element, SwingUIElement.class, "element");

        final SwingUIElement swingElement = (SwingUIElement)element;
        this.elements.add(swingElement);

        final javax.swing.JComponent jComponent = swingElement.getJComponent();
        this.jPanel.add(jComponent);
        this.jPanel.revalidate();

        return this;
    }

    @Override
    public SwingUIVerticalLayout addAll(UIElement... elements)
    {
        return (SwingUIVerticalLayout)UIVerticalLayout.super.addAll(elements);
    }

    @Override
    public SwingUIVerticalLayout addAll(Iterable<? extends UIElement> elements)
    {
        return (SwingUIVerticalLayout)UIVerticalLayout.super.addAll(elements);
    }
}
