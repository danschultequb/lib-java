package qub;

/**
 * An interface that defines a set of operations that can be used to draw UI elements.
 */
public interface UIPainter
{
    /**
     * Draw the provided text at the provided baseline point.
     * @param text The text to draw.
     * @param baseline The baseline point to draw the text at.
     */
    void drawText(String text, Point2D baseline);

    /**
     * Draw the provided text at the provided baseline x-and-y-coordinates.
     * @param text The text to draw.
     * @param baselineX The baseline x-coordinate to draw the text at.
     * @param baselineY The baseline y-coordinate to draw the text at.
     */
    void drawText(String text, Distance baselineX, Distance baselineY);

    /**
     * Draw a line from the provided start x-and-y-coordinates to the provided end
     * x-and-y-coordinates.
     * @param startX The x-coordinate to start the line at.
     * @param startY The y-coordinate to start the line at.
     * @param endX The x-coordinate to end the line at.
     * @param endY The y-coordinate to end the line at.
     */
    void drawLine(Distance startX, Distance startY, Distance endX, Distance endY);

    /**
     * Draw a rectangle from the provided top-left-x-and-y-coordinates with the provided with and
     * height.
     * @param topLeftX The top-left-x-coordinate of the rectangle.
     * @param topLeftY The top-left-y-coordinate of the rectangle.
     * @param width The width of the rectangle.
     * @param height The height of the rectangle.
     */
    void drawRectangle(Distance topLeftX, Distance topLeftY, Distance width, Distance height);

    /**
     * Translate all proceeding operations x Distance to the right and y Distance down.
     * @param x The horizontal Distance to translate proceeding operations.
     * @param y The vertical Distance to translate proceeding operations.
     */
    void translate(Distance x, Distance y);

    /**
     * Save the current transformation so that it can be restored later.
     */
    void saveTransform();

    /**
     * Restore the most recently saved transform.
     */
    void restoreTransform();
}
