package qub;

/**
 * A UIElement that is implemented using a java.awt.Component.
 */
public interface AWTUIElement extends UIElement
{
    @Override
    AWTUIElement setWidth(Distance width);

    @Override
    AWTUIElement setWidthInPixels(int widthInPixels);

    @Override
    default int getWidthInPixels()
    {
        return this.getComponent().getWidth();
    }

    @Override
    AWTUIElement setHeight(Distance height);

    @Override
    AWTUIElement setHeightInPixels(int heightInPixels);

    /**
     * Get the height of this AWTUIElement in pixels.
     * @return The height of this AWTUIElement in pixels.
     */
    default int getHeightInPixels()
    {
        return this.getComponent().getHeight();
    }

    @Override
    default AWTUIElement setSize(Size2D size)
    {
        return (AWTUIElement)UIElement.super.setSize(size);
    }

    @Override
    AWTUIElement setSize(Distance width, Distance height);

    @Override
    AWTUIElement setSizeInPixels(int widthInPixels, int heightInPixels);

    @Override
    AWTUIElement setPadding(UIPadding padding);

    @Override
    AWTUIElement setBackgroundColor(Color backgroundColor);

    /**
     * Get the underlying Component that is used to implement this UIElement.
     * @return The underlying Component that is used to implement this UIElement.
     */
    java.awt.Component getComponent();
}
