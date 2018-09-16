package qub;

public interface UIPainter extends Disposable
{
    Color getColor();
    void setColor(Color color);

    Font getFont();
    void setFont(Font font);

    Rectangle getClipBounds();

    void translate(double xInPixels, double yInPixels);

    void drawText(String text, double baselineXInPixels, double baselineYInPixels);
    void drawText(String text, Distance baselineX, Distance baselineY);

    void drawLine(double startXInPixels, double startYInPixels, double endXInPixels, double endYInPixels);
    void drawLine(Distance startX, Distance startY, Distance endX, Distance endY);

    void drawRectangle(double topLeftXInPixels, double topLeftYInPixels, double widthInPixels, double heightInPixels);
    void drawRectangle(Distance topLeftX, Distance topLeftY, Distance width, Distance height);
}
