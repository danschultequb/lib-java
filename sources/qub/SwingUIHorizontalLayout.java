package qub;

/**
 * A UIHorizontalLayout that displays other SwingUIElements in a horizontal stack.
 */
public class SwingUIHorizontalLayout implements UIHorizontalLayout, SwingUIElement
{
    private final javax.swing.JPanel jPanel;
    private final Display display;
    private final List<SwingUIElement> elements;
    private HorizontalDirection direction;

    private SwingUIHorizontalLayout(Display display)
    {
        PreCondition.assertNotNull(display, "display");

        this.display = display;
        this.elements = List.create();
        this.direction = HorizontalDirection.LeftToRight;

        this.jPanel = new javax.swing.JPanel();
        final javax.swing.BoxLayout boxLayout = new javax.swing.BoxLayout(this.jPanel, javax.swing.BoxLayout.LINE_AXIS);
        this.jPanel.setLayout(boxLayout);
    }

    public static SwingUIHorizontalLayout create(Display display)
    {
        return new SwingUIHorizontalLayout(display);
    }

    @Override
    public javax.swing.JPanel getJComponent()
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
        final int widthInPixels = this.getJComponent().getWidth();
        final Distance result = this.display.convertHorizontalPixelsToDistance(widthInPixels);

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertGreaterThanOrEqualTo(result, Distance.zero, "result");

        return result;
    }

    @Override
    public SwingUIHorizontalLayout setHeight(Distance height)
    {
        return (SwingUIHorizontalLayout)UIHorizontalLayout.super.setHeight(height);
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
    public SwingUIHorizontalLayout setSize(Size2D size)
    {
        return (SwingUIHorizontalLayout)UIHorizontalLayout.super.setSize(size);
    }

    @Override
    public SwingUIHorizontalLayout setSize(Distance width, Distance height)
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
    public SwingUIHorizontalLayout setDirection(HorizontalDirection direction)
    {
        PreCondition.assertNotNull(direction, "direction");

        if (this.direction != direction)
        {
            this.jPanel.removeAll();

            this.direction = direction;
            if (this.direction == HorizontalDirection.LeftToRight)
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
                this.jPanel.add(javax.swing.Box.createHorizontalGlue(), 0);
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
    public SwingUIHorizontalLayout add(UIElement element)
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
    public SwingUIHorizontalLayout addAll(UIElement... elements)
    {
        return (SwingUIHorizontalLayout)UIHorizontalLayout.super.addAll(elements);
    }

    @Override
    public SwingUIHorizontalLayout addAll(Iterable<? extends UIElement> elements)
    {
        return (SwingUIHorizontalLayout)UIHorizontalLayout.super.addAll(elements);
    }
}
