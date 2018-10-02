package qub;

/**
 * A Window that can be displayed on the screen of a device.
 */
public interface Window extends UIElementParent, Disposable
{
    /**
     * Get whether or not this Window is open.
     * @return Whether or not this Window is open.
     */
    boolean isOpen();

    /**
     * Open this Window so that it is visible.
     */
    void open();

    /**
     * Set the content of this Window.
     * @param uiElement The content.
     */
    void setContent(UIElement uiElement);

    /**
     * Set the painter that will be used for this Window.
     * @param painter The painter that will be used for this Window.
     */
    void setPainter(UIPainter painter);

    /**
     * Set the width of this Window (including the Window's frame).
     * @param width The width of this Window (including the Window's frame).
     */
    void setWidth(Distance width);

    /**
     * Get the width of this Window (including the Window's frame).
     * @return The width of this Window (including the Window's frame).
     */
    Distance getWidth();
}
