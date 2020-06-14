package qub;

/**
 * A UIElement that is implemented using a javax.swing.JComponent.
 */
public interface SwingUIElement extends AWTUIElement
{
    @Override
    default java.awt.Component getComponent()
    {
        return this.getJComponent();
    }

    /**
     * Get the underlying JComponent that is used to implement this UIElement.
     * @return The underlying JComponent that is used to implement this UIElement.
     */
    javax.swing.JComponent getJComponent();
}
