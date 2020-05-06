package qub;

public interface SwingUIElement extends UIElement
{
    /**
     * Get the underlying JComponent that is used to implement this UIElement.
     * @return The underlying JComponent that is used to implement this UIElement.
     */
    javax.swing.JComponent getJComponent();
}
