package qub;

public class UIPainterToGraphics2DAdapter extends java.awt.Graphics2D
{
    private final UIPainter painter;

    public UIPainterToGraphics2DAdapter(UIPainter painter)
    {
        PreCondition.assertNotNull(painter, "painter");

        this.painter = painter;
    }

    @Override
    public java.awt.Graphics create()
    {
        return this;
    }

    @Override
    public void translate(int x, int y)
    {
        throw new NotSupportedException();
    }

    @Override
    public void translate(double tx, double ty)
    {
        throw new NotSupportedException();
    }

    @Override
    public void rotate(double theta)
    {
        throw new NotSupportedException();
    }

    @Override
    public void rotate(double theta, double x, double y)
    {
        throw new NotSupportedException();
    }

    @Override
    public void scale(double sx, double sy)
    {
        throw new NotSupportedException();
    }

    @Override
    public void shear(double shx, double shy)
    {
        throw new NotSupportedException();
    }

    @Override
    public void transform(java.awt.geom.AffineTransform Tx)
    {
        throw new NotSupportedException();
    }

    @Override
    public void setTransform(java.awt.geom.AffineTransform Tx)
    {
        throw new NotSupportedException();
    }

    @Override
    public java.awt.geom.AffineTransform getTransform()
    {
        throw new NotSupportedException();
    }

    @Override
    public java.awt.Paint getPaint()
    {
        throw new NotSupportedException();
    }

    @Override
    public java.awt.Composite getComposite()
    {
        throw new NotSupportedException();
    }

    @Override
    public void setBackground(java.awt.Color color)
    {
        throw new NotSupportedException();
    }

    @Override
    public java.awt.Color getBackground()
    {
        throw new NotSupportedException();
    }

    @Override
    public java.awt.Stroke getStroke()
    {
        throw new NotSupportedException();
    }

    @Override
    public void clip(java.awt.Shape s)
    {
        throw new NotSupportedException();
    }

    @Override
    public java.awt.font.FontRenderContext getFontRenderContext()
    {
        throw new NotSupportedException();
    }

    @Override
    public java.awt.Color getColor()
    {
        throw new NotSupportedException();
    }

    @Override
    public void setColor(java.awt.Color c)
    {
        throw new NotSupportedException();
    }

    @Override
    public void setPaintMode()
    {
        throw new NotSupportedException();
    }

    @Override
    public void setXORMode(java.awt.Color c1)
    {
        throw new NotSupportedException();
    }

    @Override
    public java.awt.Font getFont()
    {
        throw new NotSupportedException();
    }

    @Override
    public void setFont(java.awt.Font font)
    {
        throw new NotSupportedException();
    }

    @Override
    public java.awt.FontMetrics getFontMetrics(java.awt.Font f)
    {
        throw new NotSupportedException();
    }

    @Override
    public java.awt.Rectangle getClipBounds()
    {
        throw new NotSupportedException();
    }

    @Override
    public void clipRect(int x, int y, int width, int height)
    {
        throw new NotSupportedException();
    }

    @Override
    public void setClip(int x, int y, int width, int height)
    {
        throw new NotSupportedException();
    }

    @Override
    public java.awt.Shape getClip()
    {
        throw new NotSupportedException();
    }

    @Override
    public void setClip(java.awt.Shape clip)
    {
        throw new NotSupportedException();
    }

    @Override
    public void copyArea(int x, int y, int width, int height, int dx, int dy)
    {
        throw new NotSupportedException();
    }

    @Override
    public void drawLine(int x1, int y1, int x2, int y2)
    {
        painter.drawLine(x1, y1, x2, y2);
    }

    @Override
    public void fillRect(int x, int y, int width, int height)
    {
        throw new NotSupportedException();
    }

    @Override
    public void clearRect(int x, int y, int width, int height)
    {
        throw new NotSupportedException();
    }

    @Override
    public void drawRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight)
    {
        throw new NotSupportedException();
    }

    @Override
    public void fillRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight)
    {
        throw new NotSupportedException();
    }

    @Override
    public void drawOval(int x, int y, int width, int height)
    {
        throw new NotSupportedException();
    }

    @Override
    public void fillOval(int x, int y, int width, int height)
    {
        throw new NotSupportedException();
    }

    @Override
    public void drawArc(int x, int y, int width, int height, int startAngle, int arcAngle)
    {
        throw new NotSupportedException();
    }

    @Override
    public void fillArc(int x, int y, int width, int height, int startAngle, int arcAngle)
    {
        throw new NotSupportedException();
    }

    @Override
    public void drawPolyline(int[] xPoints, int[] yPoints, int nPoints)
    {
        throw new NotSupportedException();
    }

    @Override
    public void drawPolygon(int[] xPoints, int[] yPoints, int nPoints)
    {
        throw new NotSupportedException();
    }

    @Override
    public void fillPolygon(int[] xPoints, int[] yPoints, int nPoints)
    {
        throw new NotSupportedException();
    }

    @Override
    public void draw(java.awt.Shape s)
    {
        throw new NotSupportedException();
    }

    @Override
    public boolean drawImage(java.awt.Image img, java.awt.geom.AffineTransform xform, java.awt.image.ImageObserver obs)
    {
        throw new NotSupportedException();
    }

    @Override
    public void drawImage(java.awt.image.BufferedImage img, java.awt.image.BufferedImageOp op, int x, int y)
    {
        throw new NotSupportedException();
    }

    @Override
    public void drawRenderedImage(java.awt.image.RenderedImage img, java.awt.geom.AffineTransform xform)
    {
        throw new NotSupportedException();
    }

    @Override
    public void drawRenderableImage(java.awt.image.renderable.RenderableImage img, java.awt.geom.AffineTransform xform)
    {
        throw new NotSupportedException();
    }

    @Override
    public void drawString(String str, int x, int y)
    {
        painter.drawText(str, x, y);
    }

    @Override
    public void drawString(String str, float x, float y)
    {
        painter.drawText(str, x, y);
    }

    @Override
    public void drawString(java.text.AttributedCharacterIterator iterator, int x, int y)
    {
        throw new NotSupportedException();
    }

    @Override
    public void drawString(java.text.AttributedCharacterIterator iterator, float x, float y)
    {
        throw new NotSupportedException();
    }

    @Override
    public void drawGlyphVector(java.awt.font.GlyphVector g, float x, float y)
    {
        throw new NotSupportedException();
    }

    @Override
    public void fill(java.awt.Shape s)
    {
        throw new NotSupportedException();
    }

    @Override
    public boolean hit(java.awt.Rectangle rect, java.awt.Shape s, boolean onStroke)
    {
        throw new NotSupportedException();
    }

    @Override
    public java.awt.GraphicsConfiguration getDeviceConfiguration()
    {
        throw new NotSupportedException();
    }

    @Override
    public void setComposite(java.awt.Composite comp)
    {
        throw new NotSupportedException();
    }

    @Override
    public void setPaint(java.awt.Paint paint)
    {
        throw new NotSupportedException();
    }

    @Override
    public void setStroke(java.awt.Stroke s)
    {
        throw new NotSupportedException();
    }

    @Override
    public void setRenderingHint(java.awt.RenderingHints.Key hintKey, Object hintValue)
    {
        throw new NotSupportedException();
    }

    @Override
    public Object getRenderingHint(java.awt.RenderingHints.Key hintKey)
    {
        throw new NotSupportedException();
    }

    @Override
    public void setRenderingHints(java.util.Map<?,?> hints)
    {
        throw new NotSupportedException();
    }

    @Override
    public void addRenderingHints(java.util.Map<?,?> hints)
    {
        throw new NotSupportedException();
    }

    @Override
    public java.awt.RenderingHints getRenderingHints()
    {
        throw new NotSupportedException();
    }

    @Override
    public boolean drawImage(java.awt.Image img, int x, int y, java.awt.image.ImageObserver observer)
    {
        throw new NotSupportedException();
    }

    @Override
    public boolean drawImage(java.awt.Image img, int x, int y, int width, int height, java.awt.image.ImageObserver observer)
    {
        throw new NotSupportedException();
    }

    @Override
    public boolean drawImage(java.awt.Image img, int x, int y, java.awt.Color bgcolor, java.awt.image.ImageObserver observer)
    {
        throw new NotSupportedException();
    }

    @Override
    public boolean drawImage(java.awt.Image img, int x, int y, int width, int height, java.awt.Color bgcolor, java.awt.image.ImageObserver observer)
    {
        throw new NotSupportedException();
    }

    @Override
    public boolean drawImage(java.awt.Image img, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2, java.awt.image.ImageObserver observer)
    {
        throw new NotSupportedException();
    }

    @Override
    public boolean drawImage(java.awt.Image img, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2, java.awt.Color bgcolor, java.awt.image.ImageObserver observer)
    {
        throw new NotSupportedException();
    }

    @Override
    public void dispose()
    {
        throw new NotSupportedException();
    }

    @Override
    public String toString()
    {
        return painter.toString();
    }
}
