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
     * Get the size of this Window (including the Window's frame).
     * @return The size of this Window (including the Window's frame).
     */
    Size2D getSize();

    /**
     * Set the size of this Window (including the Window's frame).
     * @param width The width of this Window (including the Window's frame).
     * @param height The height of this Window (including the Window's frame).
     */
    void setSize(Distance width, Distance height);

    /**
     * Set the size of this Window (including the Window's frame).
     * @param size The size of this Window (including the Window's frame).
     */
    void setSize(Size2D size);

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
    Distance getTextWidth(String text, Font font);

    /**
     * Get the size of the text using the default font.
     * @param text The text to measure.
     * @return The size of the text using the default font.
     */
    Size2D getTextSize(String text);

    /**
     * Get the size of the text using the provided font.
     * @param text The text to measure.
     * @param font The font to use to measure the text.
     * @return The size of the text using the provided font.
     */
    Size2D getTextSize(String text, Font font);

    /**
     * Get the number of pixels that fit into the provided horizontal distance.
     * @param horizontalDistance The distance to convert to pixels.
     * @return The number of pixels that fit into the provided horizontal distance.
     */
    double convertHorizontalDistanceToPixels(Distance horizontalDistance);

    /**
     * Get the distance that the provided number of horizontal pixels covers.
     * @param horizontalPixels The number of horizontal pixels.
     * @return The distance that the provided number of horizontal pixels covers.
     */
    Distance convertHorizontalPixelsToDistance(double horizontalPixels);

    /**
     * Get the number of pixels that fit into the provided vertical distance.
     * @param verticalDistance The distance to convert to pixels
     * @return The number of pixels that fit into the provided vertical distance.
     */
    double convertVerticalDistanceToPixels(Distance verticalDistance);

    /**
     * Get the distance that the provided number of vertical pixels covers.
     * @param verticalPixels The number of vertical pixels.
     * @return The distance that the provided number of vertical pixels covers.
     */
    Distance convertVerticalPixelsToDistance(double verticalPixels);

    /**
     * Get the size that the provided number of horizontal and vertical pixels covers.
     * @param horizontalPixels The number of horizontal pixels.
     * @param verticalPixels The number of vertical pixels.
     * @return The size that the provided number of horizontal and vertical pixels covers.
     */
    Size2D convertPixelsToSize2D(double horizontalPixels, double verticalPixels);
}
