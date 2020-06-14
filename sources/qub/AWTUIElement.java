package qub;

/**
 * A UIElement that is implemented using a java.awt.Component.
 */
public interface AWTUIElement extends UIElement
{
    /**
     * Get the underlying Component that is used to implement this UIElement.
     * @return The underlying Component that is used to implement this UIElement.
     */
    java.awt.Component getComponent();
}
