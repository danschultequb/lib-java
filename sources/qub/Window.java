package qub;

/**
 * A Window that can be displayed on the screen of a device.
 */
public interface Window extends UIElementParent, Disposable
{
    @Override
    default Window getParentElement()
    {
        return this;
    }

    @Override
    default Window getParentWindow()
    {
        return this;
    }

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
    default Distance getTextWidth(String text)
    {
        Distance result;
        if (Strings.isNullOrEmpty(text))
        {
            result = Distance.zero;
        }
        else
        {
            final java.awt.Graphics imageGraphics = createImageGraphics();
            try
            {
                final java.awt.Font defaultFont = imageGraphics.getFont();
                result = getTextWidth(this, text, imageGraphics, defaultFont);
            }
            finally
            {
                imageGraphics.dispose();
            }
        }

        PostCondition.assertGreaterThanOrEqualTo(result, Distance.zero, "result");

        return result;
    }

    /**
     * Get the width of the text using the provided font.
     * @param text The text to measure.
     * @param font The font to use to measure the text.
     * @return The width of the text using the provided font.
     */
    default Distance getTextWidth(String text, Font font)
    {
        Distance result;
        if (Strings.isNullOrEmpty(text) || font == null)
        {
            result = getTextWidth(text);
        }
        else
        {
            final java.awt.Graphics imageGraphics = createImageGraphics();
            try
            {
                final float fontSize = (float)font.getSize().toFontPoints().getValue();
                final java.awt.Font javaFont = imageGraphics.getFont().deriveFont(fontSize);
                result = getTextWidth(this, text, imageGraphics, javaFont);
            }
            finally
            {
                imageGraphics.dispose();
            }
        }

        PostCondition.assertGreaterThanOrEqualTo(result, Distance.zero, "result");

        return result;
    }

    /**
     * Get the size of the text using the default font.
     * @param text The text to measure.
     * @return The size of the text using the default font.
     */
    default Size2D getTextSize(String text)
    {
        Size2D result;
        if (Strings.isNullOrEmpty(text))
        {
            result = Size2D.zero;
        }
        else
        {
            final java.awt.Graphics imageGraphics = createImageGraphics();
            try
            {
                final java.awt.Font defaultFont = imageGraphics.getFont();
                result = getTextSize(this, text, imageGraphics, defaultFont);
            }
            finally
            {
                imageGraphics.dispose();
            }
        }

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    /**
     * Get the size of the text using the provided font.
     * @param text The text to measure.
     * @param font The font to use to measure the text.
     * @return The size of the text using the provided font.
     */
    default Size2D getTextSize(String text, Font font)
    {
        Size2D result;
        if (Strings.isNullOrEmpty(text) || font == null)
        {
            result = getTextSize(text);
        }
        else
        {
            final java.awt.Graphics imageGraphics = createImageGraphics();
            try
            {
                final float fontSize = (float)font.getSize().toFontPoints().getValue();
                final java.awt.Font javaFont = imageGraphics.getFont().deriveFont(fontSize);
                result = getTextSize(this, text, imageGraphics, javaFont);
            }
            finally
            {
                imageGraphics.dispose();
            }
        }

        PostCondition.assertNotNull(result, "result");

        return result;
    }

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

    static Distance getTextWidth(Window window, String text, java.awt.Graphics graphics, java.awt.Font font)
    {
        final Size2D textSize = getTextSize(window, text, graphics, font);
        final Distance result = textSize.getWidth();

        PostCondition.assertGreaterThan(result, Distance.zero, "result");

        return result;
    }

    static Size2D getTextSize(Window window, String text, java.awt.Graphics graphics, java.awt.Font font)
    {
        final java.awt.FontMetrics fontMetrics = graphics.getFontMetrics(font);
        final java.awt.geom.Rectangle2D stringBounds = fontMetrics.getStringBounds(text, graphics);
        final Size2D result = window.convertPixelsToSize2D(stringBounds.getWidth(), stringBounds.getHeight());

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    static java.awt.Graphics createImageGraphics()
    {
        final java.awt.image.BufferedImage image = new java.awt.image.BufferedImage(1, 1, java.awt.image.BufferedImage.TYPE_INT_ARGB);
        final java.awt.Graphics result = image.getGraphics();

        PostCondition.assertNotNull(result, "result");

        return result;
    }
}
