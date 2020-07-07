package qub;

/**
 * A UIElement that displays other UIElements in a vertical stack.
 */
public interface UIVerticalLayout extends UIElement
{
    @Override
    UIVerticalLayout setWidth(Distance width);

    @Override
    UIVerticalLayout setHeight(Distance height);

    @Override
    UIVerticalLayout setSize(Size2D size);

    @Override
    UIVerticalLayout setSize(Distance width, Distance height);

    /**
     * Add the provided element to this UIVerticalLayout.
     * @param uiElement The element to add to this UIVerticalLayout.
     * @return This object for method chaining.
     */
    default UIVerticalLayout add(UIElement uiElement)
    {
        PreCondition.assertNotNull(uiElement, "uiElement");

        return this.addAll(Iterable.create(uiElement));
    }

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
    UIVerticalLayout addAll(Iterable<? extends UIElement> uiElements);

    /**
     * Set the horizontal alignment of the elements in this UIVerticalLayout.
     * @param elementHorizontalAlignment The horizontal alignment of the elements in this UIVerticalLayout.
     * @return This object for method chaining.
     */
    UIVerticalLayout setElementHorizontalAlignment(HorizontalAlignment elementHorizontalAlignment);

    /**
     * Get the horizontal alignment of the elements in this UIVerticalLayout.
     * @return The horizontal alignment of the elements in this UIVerticalLayout.
     */
    HorizontalAlignment getElementHorizontalAlignment();

    /**
     * Set the vertical alignment of the elements in this UIVerticalLayout.
     * @param elementVerticalAlignment The vertical alignment of the elements in this UIVerticalLayout.
     * @return This object for method chaining.
     */
    UIVerticalLayout setElementVerticalAlignment(VerticalAlignment elementVerticalAlignment);

    /**
     * Get the vertical alignment of the elements in this UIVerticalLayout.
     * @return The vertical alignment of the elements in this UIVerticalLayout.
     */
    VerticalAlignment getElementVerticalAlignment();
}
