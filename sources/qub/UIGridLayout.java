package qub;

/**
 * A UIElement that displays other UIElements in a grid.
 */
public interface UIGridLayout extends UIElement
{
    @Override
    UIGridLayout setWidth(Distance width);

    @Override
    UIGridLayout setHeight(Distance height);

    @Override
    UIGridLayout setSize(Size2D size);

    @Override
    UIGridLayout setSize(Distance width, Distance height);

    /**
     * Add the provided element to this UIGridLayout.
     * @param x The x-coordinate in the UIGridLayout that the element will be set to.
     * @param y The y-coordinate in the UIGridLayout that the element will be set to.
     * @param element The element to add to this UIGridLayout.
     * @return This object for method chaining.
     */
    UIGridLayout setUIElement(int x, int y, UIElement element);

    /**
     * Get the element at the provided row and column.
     * @param x The x-coordinate in the UIGridLayout to get the element from.
     * @param y The y-coordinate in the UIGridLayout to get the element from.
     * @return The element at the provided x and y coordinates.
     */
    Result<? extends UIElement> getUIElement(int x, int y);
}
