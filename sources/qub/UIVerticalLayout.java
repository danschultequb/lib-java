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
     * @param uiElement The element to add to this UIVerticalLayout.
     * @return This object for method chaining.
     */
    UIVerticalLayout add(UIElement uiElement);

    /**
     * Add the provided elements to this UIVerticalLayout.
     * @param uiElements The elements to add to this UIVerticalLayout.
     * @return This object for method chaining.
     */
    default UIVerticalLayout addAll(UIElement... uiElements)
    {
        PreCondition.assertNotNull(uiElements, "uiElements");

        return this.addAll(Iterable.create(uiElements));
    }

    /**
     * Add the provided elements to this UIVerticalLayout.
     * @param uiElements The elements to add to this UIVerticalLayout.
     * @return This object for method chaining.
     */
    default UIVerticalLayout addAll(Iterable<? extends UIElement> uiElements)
    {
        PreCondition.assertNotNull(uiElements, "uiElements");

        for (final UIElement element : uiElements)
        {
            this.add(element);
        }

        return this;
    }
}
