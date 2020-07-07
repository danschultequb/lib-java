package qub;

/**
 * A UIElement that displays other UIElements at specified positions relative to this UIElement's
 * top-left corner.
 */
public interface UIPositionLayout extends UIElement
{
    @Override
    UIPositionLayout setWidth(Distance width);

    @Override
    UIPositionLayout setHeight(Distance height);

    @Override
    UIPositionLayout setSize(Size2D size);

    @Override
    UIPositionLayout setSize(Distance width, Distance height);

    /**
     * Add the provided element to this UIPositionLayout.
     * @param topLeft The position relative to this UIPositionLayout's top-left corner that the
     *                provided uiElement will be positioned at.
     * @param uiElement The element to add to this UIPositionLayout.
     * @return This object for method chaining.
     */
    default UIPositionLayout add(Point2D topLeft, UIElement uiElement)
    {
        PreCondition.assertNotNull(topLeft, "topLeft");

        return this.add(topLeft.getX(), topLeft.getY(), uiElement);
    }

    /**
     * Add the provided element to this UIPositionLayout.
     * @param left The distance between this UIPositionLayout's left edge and the provided
     *             uiElement's left edge.
     * @param top The distance between this UIPositionLayout's top edge and the provided
     *            uiElement's top edge.
     * @param uiElement The element to add to this UIPositionLayout.
     * @return This object for method chaining.
     */
    UIPositionLayout add(Distance left, Distance top, UIElement uiElement);
}
