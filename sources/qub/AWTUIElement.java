package qub;

/**
 * A UIElement that is implemented using a java.awt.Component.
 */
public interface AWTUIElement extends UIElement
{
    @Override
    AWTUIElement setWidth(Distance width);

    @Override
    AWTUIElement setDynamicWidth(DynamicDistance dynamicWidth);

    @Override
    AWTUIElement setWidthInPixels(int widthInPixels);

    @Override
    AWTUIElement setHeight(Distance height);

    @Override
    AWTUIElement setHeightInPixels(int heightInPixels);

    @Override
    AWTUIElement setSize(Size2D size);

    @Override
    AWTUIElement setSize(Distance width, Distance height);

    @Override
    AWTUIElement setSizeInPixels(int widthInPixels, int heightInPixels);

    @Override
    AWTUIElement setPadding(UIPadding padding);

    @Override
    AWTUIElement setPaddingInPixels(UIPaddingInPixels padding);

    @Override
    AWTUIElement setBackgroundColor(Color backgroundColor);

    /**
     * Get the underlying Component that is used to implement this UIElement.
     * @return The underlying Component that is used to implement this UIElement.
     */
    java.awt.Component getComponent();
}
