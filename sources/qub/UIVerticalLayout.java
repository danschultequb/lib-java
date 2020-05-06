package qub;

/**
 * A UIElement that displays other UIElements in a vertical stack.
 */
public interface UIVerticalLayout extends UIElement
{
    @Override
    default UIVerticalLayout setWidth(Distance width)
    {
        return (UIVerticalLayout)UIElement.super.setWidth(width);
    }

    @Override
    default UIVerticalLayout setHeight(Distance height)
    {
        return (UIVerticalLayout)UIElement.super.setHeight(height);
    }

    @Override
    default UIVerticalLayout setSize(Size2D size)
    {
        return (UIVerticalLayout)UIElement.super.setSize(size);
    }

    @Override
    UIVerticalLayout setSize(Distance width, Distance height);

    /**
     * Set the direction that elements will flow in this UIVerticalLayout.
     * @param direction The direction that elements will flow in this UIVerticalLayout.
     * @return This object for method chaining.
     */
    UIVerticalLayout setDirection(VerticalDirection direction);

    /**
     * Get the direction that elements will flow in this UIVerticalLayout.
     * @return The direction that elements will flow in this UIVerticalLayout.
     */
    VerticalDirection getDirection();

    /**
     * Add the provided element to this UIVerticalLayout.
     * @param element The element to add to this UIVerticalLayout.
     * @return This object for method chaining.
     */
    UIVerticalLayout add(UIElement element);

    /**
     * Add the provided elements to this UIVerticalLayout.
     * @param elements The elements to add to this UIVerticalLayout.
     * @return This object for method chaining.
     */
    default UIVerticalLayout addAll(UIElement... elements)
    {
        PreCondition.assertNotNull(elements, "elements");

        return this.addAll(Iterable.create(elements));
    }

    /**
     * Add the provided elements to this UIVerticalLayout.
     * @param elements The elements to add to this UIVerticalLayout.
     * @return This object for method chaining.
     */
    default UIVerticalLayout addAll(Iterable<? extends UIElement> elements)
    {
        PreCondition.assertNotNull(elements, "elements");

        for (final UIElement element : elements)
        {
            this.add(element);
        }

        return this;
    }
}
