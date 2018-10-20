package qub;

/**
 * An interface that defines a set of operations that can be used to draw UI elements.
 */
public interface UIPainter
{
    /**
     * Draw the provided text using the UIPainter's current transform to position it.
     * @param text The text to draw.
     */
    void drawText(String text);

    /**
     * Draw the provided text at the provided top-left point.
     * @param text The text to draw.
     * @param topLeft The top-left point to draw the text at.
     */
    void drawText(String text, Point2D topLeft);

    /**
     * Draw the provided text at the provided top-left x-and-y-coordinates.
     * @param text The text to draw.
     * @param topLeftX The top-left x-coordinate to draw the text at.
     * @param topLeftY The top-left y-coordinate to draw the text at.
     */
    void drawText(String text, Distance topLeftX, Distance topLeftY);

    /**
     * Draw a line from the provided start point to the provided end point.
     * @param start The point to start the line at.
     * @param end The point to end the line at.
     */
    void drawLine(Point2D start, Point2D end);

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
     * Draw a rectangle from the painter's origin (top left corner) with the provided width and
     * height.
     * @param width The width of the rectangle.
     * @param height The height of the rectangle.
     */
    void drawRectangle(Distance width, Distance height);

    /**
     * Draw a rectangle from the provided top-left-x-and-y-coordinates with the provided width and
     * height.
     * @param topLeftX The top-left-x-coordinate of the rectangle.
     * @param topLeftY The top-left-y-coordinate of the rectangle.
     * @param width The width of the rectangle.
     * @param height The height of the rectangle.
     */
    void drawRectangle(Distance topLeftX, Distance topLeftY, Distance width, Distance height);

    /**
     * Fill a rectangle from the painter's origin (top left corner) with the provided width and
     * height.
     * @param width The width of the rectangle.
     * @param height The height of the rectangle.
     */
    void fillRectangle(Distance width, Distance height);

    /**
     * Fill a rectangle from the provided top-left-x-and-y-coordinates with the provided width and
     * height.
     * @param topLeftX The top-left-x-coordinate of the rectangle.
     * @param topLeftY The top-left-y-coordinate of the rectangle.
     * @param width The width of the rectangle.
     * @param height The height of the rectangle.
     */
    void fillRectangle(Distance topLeftX, Distance topLeftY, Distance width, Distance height);

    /**
     * Translate all proceeding operations x Distance to the right and y Distance down.
     * @param x The horizontal Distance to translate proceeding operations.
     * @param y The vertical Distance to translate proceeding operations.
     * @return A Disposable that will undo this translation when disposed.
     */
    Disposable translate(Distance x, Distance y);

    /**
     * Translate all proceeded operations x Distance to the right.
     * @param x The horizontal Distance to translate proceeding operations.
     * @return A Disposable that will undo this translation when disposed.
     */
    Disposable translateX(Distance x);

    /**
     * Translate all proceeding operations y Distance down.
     * @param y The vertical Distance to translate proceeding operations.
     * @return A Disposable that will undo this translation when disposed.
     */
    Disposable translateY(Distance y);

    /**
     * Save the current transformation so that it can be restored later.
     * @return A Disposable that when disposed will restore the saved transform.
     */
    Disposable saveTransform();

    /**
     * Restore the most recently saved transform.
     */
    void restoreTransform();

    /**
     * Set the font that this UIPainter will draw text with.
     * @param font The font that this UIPainter will draw text with.
     */
    Disposable setFont(Font font);

    /**
     * Set the font size that this UIPainter will draw text with.
     * @param fontSize The font size that this UIPainter will draw text with.
     */
    Disposable setFontSize(Distance fontSize);

    /**
     * Save the current font so that it can be restored later.
     * @return A Disposable that when disposed will restore the saved font.
     */
    Disposable saveFont();

    /**
     * Restore the most recently saved font.
     */
    void restoreFont();

    /**
     * Set the color that the UIPainter will draw and fill with.
     * @param color The color that the UIPainter will draw and fill with.
     */
    void setColor(Color color);

    /**
     * Save the current color so that it can be restored later.
     * @return A Disposable that when disposed will restore the saved color.
     */
    Disposable saveColor();

    /**
     * Restore the most recently saved color.
     */
    void restoreColor();
}
