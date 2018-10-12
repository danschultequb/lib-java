package qub;

public abstract class WindowBase extends DisposableBase implements Window
{
    @Override
    public Window getParentElement()
    {
        return this;
    }

    @Override
    public Window getParentWindow()
    {
        return this;
    }

    @Override
    final public Distance getTextWidth(String text)
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
                result = getTextWidth(text, imageGraphics, defaultFont);
            }
            finally
            {
                imageGraphics.dispose();
            }
        }

        PostCondition.assertGreaterThanOrEqualTo(result, Distance.zero, "result");

        return result;
    }

    @Override
    final public Distance getTextWidth(String text, Font font)
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
                result = getTextWidth(text, imageGraphics, javaFont);
            }
            finally
            {
                imageGraphics.dispose();
            }
        }

        PostCondition.assertGreaterThanOrEqualTo(result, Distance.zero, "result");

        return result;
    }

    @Override
    final public Size2D getTextSize(String text)
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
                result = getTextSize(text, imageGraphics, defaultFont);
            }
            finally
            {
                imageGraphics.dispose();
            }
        }

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    @Override
    final public Size2D getTextSize(String text, Font font)
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
                result = getTextSize(text, imageGraphics, javaFont);
            }
            finally
            {
                imageGraphics.dispose();
            }
        }

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    private Distance getTextWidth(String text, java.awt.Graphics graphics, java.awt.Font font)
    {
        final Size2D textSize = getTextSize(text, graphics, font);
        final Distance result = textSize.getWidth();

        PostCondition.assertGreaterThan(result, Distance.zero, "result");

        return result;
    }

    private Size2D getTextSize(String text, java.awt.Graphics graphics, java.awt.Font font)
    {
        final java.awt.FontMetrics fontMetrics = graphics.getFontMetrics(font);
        final java.awt.geom.Rectangle2D stringBounds = fontMetrics.getStringBounds(text, graphics);
        final Size2D result = convertPixelsToSize2D(stringBounds.getWidth(), stringBounds.getHeight());

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    private static java.awt.Graphics createImageGraphics()
    {
        final java.awt.image.BufferedImage image = new java.awt.image.BufferedImage(1, 1, java.awt.image.BufferedImage.TYPE_INT_ARGB);
        final java.awt.Graphics result = image.getGraphics();

        PostCondition.assertNotNull(result, "result");

        return result;
    }
}
