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
     * @param uiElement The content of this Window.
     */
    void setContent(UIElement uiElement);

    /**
     * Get the content of this Window.
     * @return The content of this Window.
     */
    UIElement getContent();

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

    /**
     * Set the height of this Window (including the Window's frame).
     * @param height The height of this Window (including the Window's frame).
     */
    void setHeight(Distance height);

    /**
     * Get the height of this Window (including the Window's frame).
     * @return The height of this Window (including the Window's frame).
     */
    Distance getHeight();

    /**
     * Get the width of the text using the default font.
     * @param text The text to measure.
     * @return The width of the text using the default font.
     */
    Distance getTextWidth(String text);

    /**
     * Get the width of the text using the provided font.
     * @param text The text to measure.
     * @param font The font to use to measure the text.
     * @return The width of the text using the provided font.
     */
    Distance getTextWidth(String text, java.awt.Font font);
}
