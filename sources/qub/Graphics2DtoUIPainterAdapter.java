package qub;

public class Graphics2DtoUIPainterAdapter implements UIPainter
{
    private final java.awt.Graphics2D graphics;

    public Graphics2DtoUIPainterAdapter(java.awt.Graphics2D graphics)
    {
        PreCondition.assertNotNull(graphics, "graphics");

        this.graphics = graphics;
    }

    @Override
    public void drawText(String text)
    {
        graphics.drawString(text, 0, 20);
    }
}
