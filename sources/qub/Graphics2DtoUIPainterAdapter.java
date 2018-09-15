package qub;

public class Graphics2DtoUIPainterAdapter implements UIPainter
{
    private final java.awt.Graphics2D graphics;
    private final Window parentWindow;

    public Graphics2DtoUIPainterAdapter(java.awt.Graphics2D graphics, Window parentWindow)
    {
        PreCondition.assertNotNull(graphics, "graphics");
        PreCondition.assertNotNull(parentWindow, "parentWindow");

        this.graphics = graphics;
        this.parentWindow = parentWindow;
    }

    @Override
    public void drawText(String text, double baselineXInPixels, double baselineYInPixels)
    {
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
    public String toString()
    {
        return graphics.toString();
    }
}
