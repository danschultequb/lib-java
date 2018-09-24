package qub;

public interface UIPainter
{
    void drawText(String text, Distance baselineX, Distance baselineY);

    void drawLine(Distance startX, Distance startY, Distance endX, Distance endY);

    void drawRectangle(Distance topLeftX, Distance topLeftY, Distance width, Distance height);
}
