package qub;

public interface JavaUIElement extends UIElement
{
    /**
     * Get the underlying JComponent that is used to implement this UIElement.
     * @return The underlying JComponent that is used to implement this UIElement.
     */
    java.awt.Component getComponent();
}
