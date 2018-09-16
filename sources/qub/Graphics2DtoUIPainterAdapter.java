package qub;

public class Graphics2DtoUIPainterAdapter extends DisposableBase implements UIPainter
{
    private final java.awt.Graphics2D graphics;
    private final Window parentWindow;
    private boolean disposed;

    public Graphics2DtoUIPainterAdapter(java.awt.Graphics2D graphics, Window parentWindow)
    {
        PreCondition.assertNotNull(graphics, "graphics");
        PreCondition.assertNotNull(parentWindow, "parentWindow");

        this.graphics = graphics;
        this.parentWindow = parentWindow;
    }

    @Override
    public Color getColor()
    {
        final java.awt.Color awtColor = graphics.getColor();
        final Color result = Color.rgba(awtColor.getRed(), awtColor.getGreen(), awtColor.getBlue(), awtColor.getAlpha());

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    @Override
    public void setColor(Color color)
    {
        PreCondition.assertNotNull(color, "color");

        graphics.setColor(new java.awt.Color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()));
    }

    @Override
    public Font getFont()
    {
        final java.awt.Font awtFont = graphics.getFont();
        return new Font(awtFont.getFontName(), awtFont.getStyle(), awtFont.getSize());
    }

    @Override
    public void setFont(Font font)
    {
        PreCondition.assertNotNull(font, "font");

        graphics.setFont(new java.awt.Font(font.getName(), font.getStyle(), font.getSize()));
    }

    @Override
    public Rectangle getClipBounds()
    {
        final java.awt.Rectangle awtClipBounds = graphics.getClipBounds();
        return new Rectangle(awtClipBounds.x, awtClipBounds.y, awtClipBounds.width, awtClipBounds.height);
    }

    @Override
    public void translate(double xInPixels, double yInPixels)
    {
        graphics.translate(xInPixels, yInPixels);
    }

    @Override
    public void drawText(String text, double baselineXInPixels, double baselineYInPixels)
    {
        PreCondition.assertNotNullAndNotEmpty(text, "text");

        graphics.drawString(text, (float)baselineXInPixels, (float)baselineYInPixels);
    }

    @Override
    public void drawText(String text, Distance baselineX, Distance baselineY)
    {
        final double baselineXInPixels = parentWindow.convertHorizontalDistanceToPixels(baselineX);
        final double baselineYInPixels = parentWindow.convertVerticalDistanceToPixels(baselineY);
        drawText(text, baselineXInPixels, baselineYInPixels);
    }

    @Override
    public void drawLine(double startXInPixels, double startYInPixels, double endXInPixels, double endYInPixels)
    {
        graphics.drawLine((int)startXInPixels, (int)startYInPixels, (int)endXInPixels, (int)endYInPixels);
    }

    @Override
    public void drawLine(Distance startX, Distance startY, Distance endX, Distance endY)
    {
        final double startXInPixels = parentWindow.convertHorizontalDistanceToPixels(startX);
        final double startYInPixels = parentWindow.convertVerticalDistanceToPixels(startY);
        final double endXInPixels = parentWindow.convertHorizontalDistanceToPixels(endX);
        final double endYInPixels = parentWindow.convertVerticalDistanceToPixels(endY);
        drawLine(startXInPixels, startYInPixels, endXInPixels, endYInPixels);
    }

    @Override
    public void drawRectangle(double topLeftXInPixels, double topLeftYInPixels, double widthInPixels, double heightInPixels)
    {
        graphics.drawRect((int)topLeftXInPixels, (int)topLeftYInPixels, (int)widthInPixels, (int)heightInPixels);
    }

    @Override
    public void drawRectangle(Distance topLeftX, Distance topLeftY, Distance width, Distance height)
    {
        final double topLeftXInPixels = parentWindow.convertHorizontalDistanceToPixels(topLeftX);
        final double topLeftYInPixels = parentWindow.convertVerticalDistanceToPixels(topLeftY);
        final double widthInPixels = parentWindow.convertHorizontalDistanceToPixels(width);
        final double heightInPixels = parentWindow.convertVerticalDistanceToPixels(height);
        drawRectangle(topLeftXInPixels, topLeftYInPixels, widthInPixels, heightInPixels);
    }

    @Override
    public String toString()
    {
        return graphics.toString();
    }

    @Override
    public boolean isDisposed()
    {
        return isDisposed();
    }

    @Override
    public Result<Boolean> dispose()
    {
        Result<Boolean> result;
        if (disposed)
        {
            result = Result.successFalse();
        }
        else
        {
            disposed = true;
            graphics.dispose();
            result = Result.successTrue();
        }

        PostCondition.assertNotNull(result, "result");

        return result;
    }
}
