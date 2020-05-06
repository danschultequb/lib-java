package qub;

/**
 * A UIVerticalLayout that displays other SwingUIElements in a vertical stack.
 */
public class SwingUIVerticalLayout implements UIVerticalLayout, SwingUIElement
{
    private final javax.swing.JPanel jPanel;
    private final Display display;

    private SwingUIVerticalLayout(Display display)
    {
        PreCondition.assertNotNull(display, "display");

        this.display = display;

        this.jPanel = new javax.swing.JPanel();
        this.jPanel.setLayout(new javax.swing.BoxLayout(this.jPanel, javax.swing.BoxLayout.PAGE_AXIS));
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
    public SwingUIVerticalLayout add(UIElement element)
    {
        PreCondition.assertNotNull(element, "element");
        PreCondition.assertInstanceOf(element, SwingUIElement.class, "element");

        final SwingUIElement swingElement = (SwingUIElement)element;
        this.jPanel.add(swingElement.getJComponent());
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
