package qub;

/**
 * A UIElement that is implemented using a javax.swing.JComponent.
 */
public interface SwingUIElement extends AWTUIElement
{
    @Override
    SwingUIElement setWidth(Distance width);

    @Override
    SwingUIElement setHeight(Distance height);

    @Override
    default SwingUIElement setSize(Size2D size)
    {
        return (SwingUIElement)AWTUIElement.super.setSize(size);
    }

    @Override
    SwingUIElement setSize(Distance width, Distance height);

    @Override
    SwingUIElement setPadding(UIPadding padding);

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
