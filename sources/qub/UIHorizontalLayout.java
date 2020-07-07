package qub;

/**
 * A UIElement that displays other UIElements in a horizontal stack.
 */
public interface UIHorizontalLayout extends UIElement
{
    @Override
    UIHorizontalLayout setWidth(Distance width);

    @Override
    UIHorizontalLayout setHeight(Distance height);

    @Override
    UIHorizontalLayout setSize(Size2D size);

    @Override
    UIHorizontalLayout setSize(Distance width, Distance height);

    /**
     * Add the provided element to this UIHorizontalLayout.
     * @param uiElement The element to add to this UIHorizontalLayout.
     * @return This object for method chaining.
     */
    default UIHorizontalLayout add(UIElement uiElement)
    {
        return this.addAll(uiElement);
    }

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
    UIHorizontalLayout addAll(Iterable<? extends UIElement> uiElements);
}
