package qub;

/**
 * A UIElement that is implemented using a javax.swing.JComponent.
 */
public interface SwingUIElement extends AWTUIElement
{
    @Override
    SwingUIElement setWidth(Distance width);

    @Override
    SwingUIElement setDynamicWidth(DynamicDistance dynamicWidth);

    @Override
    SwingUIElement setWidthInPixels(int widthInPixels);

    @Override
    SwingUIElement setHeight(Distance height);

    @Override
    SwingUIElement setHeightInPixels(int heightInPixels);

    @Override
    SwingUIElement setSize(Size2D size);

    @Override
    SwingUIElement setSize(Distance width, Distance height);

    @Override
    SwingUIElement setSizeInPixels(int widthInPixels, int heightInPixels);

    @Override
    SwingUIElement setPadding(UIPadding padding);

    @Override
    SwingUIElement setPaddingInPixels(UIPaddingInPixels padding);

    @Override
    SwingUIElement setBackgroundColor(Color backgroundColor);

    @Override
    default javax.swing.JComponent getComponent()
    {
        return this.getJComponent();
    }

    /**
     * Get the underlying JComponent that is used to implement this UIElement.
     * @return The underlying JComponent that is used to implement this UIElement.
     */
    javax.swing.JComponent getJComponent();
}
