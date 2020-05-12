package qub;

/**
 * A UIElement that displays other UIElements in a horizontal stack.
 */
public interface UIHorizontalLayout extends UIElement
{
    @Override
    default UIHorizontalLayout setWidth(Distance width)
    {
        return (UIHorizontalLayout)UIElement.super.setWidth(width);
    }

    @Override
    default UIHorizontalLayout setHeight(Distance height)
    {
        return (UIHorizontalLayout)UIElement.super.setHeight(height);
    }

    @Override
    default UIHorizontalLayout setSize(Size2D size)
    {
        return (UIHorizontalLayout)UIElement.super.setSize(size);
    }

    @Override
    UIHorizontalLayout setSize(Distance width, Distance height);

    /**
     * Set the direction that elements will flow in this UIHorizontalLayout.
     * @param direction The direction that elements will flow in this UIHorizontalLayout.
     * @return This object for method chaining.
     */
    UIHorizontalLayout setDirection(HorizontalDirection direction);

    /**
     * Get the direction that elements will flow in this UIHorizontalLayout.
     * @return The direction that elements will flow in this UIHorizontalLayout.
     */
    HorizontalDirection getDirection();

    /**
     * Add the provided element to this UIHorizontalLayout.
     * @param element The element to add to this UIHorizontalLayout.
     * @return This object for method chaining.
     */
    UIHorizontalLayout add(UIElement element);

    /**
     * Add the provided elements to this UIHorizontalLayout.
     * @param uiElements The elements to add to this UIHorizontalLayout.
     * @return This object for method chaining.
     */
    default UIHorizontalLayout addAll(UIElement... uiElements)
    {
        PreCondition.assertNotNull(uiElements, "uiElements");

        return this.addAll(Iterable.create(uiElements));
    }

    /**
     * Add the provided elements to this UIHorizontalLayout.
     * @param uiElements The elements to add to this UIHorizontalLayout.
     * @return This object for method chaining.
     */
    default UIHorizontalLayout addAll(Iterable<? extends UIElement> uiElements)
    {
        PreCondition.assertNotNull(uiElements, "uiElements");

        for (final UIElement element : uiElements)
        {
            this.add(element);
        }

        return this;
    }
}
