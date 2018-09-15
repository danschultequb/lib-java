package qub;

public interface UIPainter
{
    void drawText(String text, double baselineXInPixels, double baselineYInPixels);
    void drawText(String text, Distance baselineX, Distance baselineY);

    void drawLine(double startXInPixels, double startYInPixels, double endXInPixels, double endYInPixels);
    void drawLine(Distance startX, Distance startY, Distance endX, Distance endY);
}
