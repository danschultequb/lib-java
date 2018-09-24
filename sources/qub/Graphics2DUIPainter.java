package qub;

public class Graphics2DUIPainter extends java.awt.Graphics2D implements UIPainter
{
    private final java.awt.Graphics2D graphics;
    private final Window parentWindow;
    private boolean disposed;

    public Graphics2DUIPainter(java.awt.Graphics2D graphics, Window parentWindow)
    {
        PreCondition.assertNotNull(graphics, "graphics");
        PreCondition.assertNotNull(parentWindow, "parentWindow");

        this.graphics = graphics;
        this.parentWindow = parentWindow;
    }

    @Override
    public void draw(java.awt.Shape s)
    {
        PreCondition.assertNotNull(s, "s");

        graphics.draw(s);
    }

    @Override
    public boolean drawImage(java.awt.Image img, java.awt.geom.AffineTransform xform, java.awt.image.ImageObserver obs)
    {
        PreCondition.assertNotNull(img, "img");

        return graphics.drawImage(img, xform, obs);
    }

    @Override
    public void drawImage(java.awt.image.BufferedImage img, java.awt.image.BufferedImageOp op, int x, int y)
    {
        PreCondition.assertNotNull(img, "img");

        graphics.drawImage(img, op, x, y);
    }

    @Override
    public void drawRenderedImage(java.awt.image.RenderedImage img, java.awt.geom.AffineTransform xform)
    {
        PreCondition.assertNotNull(img, "img");

        graphics.drawRenderedImage(img, xform);
    }

    @Override
    public void drawRenderableImage(java.awt.image.renderable.RenderableImage img, java.awt.geom.AffineTransform xform)
    {
        PreCondition.assertNotNull(img, "img");

        graphics.drawRenderableImage(img, xform);
    }

    @Override
    public void drawString(String str, int x, int y)
    {
        graphics.drawString(str, x, y);
    }

    @Override
    public void drawString(String str, float x, float y)
    {
        graphics.drawString(str, x, y);
    }

    @Override
    public void drawString(java.text.AttributedCharacterIterator iterator, int x, int y)
    {
        graphics.drawString(iterator, x, y);
    }

    @Override
    public boolean drawImage(java.awt.Image img, int x, int y, java.awt.image.ImageObserver observer)
    {
        return graphics.drawImage(img, x, y, observer);
    }

    @Override
    public boolean drawImage(java.awt.Image img, int x, int y, int width, int height, java.awt.image.ImageObserver observer)
    {
        return graphics.drawImage(img, x, y, width, height, observer);
    }

    @Override
    public boolean drawImage(java.awt.Image img, int x, int y, java.awt.Color bgcolor, java.awt.image.ImageObserver observer)
    {
        return graphics.drawImage(img, x, y, bgcolor, observer);
    }

    @Override
    public boolean drawImage(java.awt.Image img, int x, int y, int width, int height, java.awt.Color bgcolor, java.awt.image.ImageObserver observer)
    {
        return graphics.drawImage(img, x, y, width, height, bgcolor, observer);
    }

    @Override
    public boolean drawImage(java.awt.Image img, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2, java.awt.image.ImageObserver observer)
    {
        return graphics.drawImage(img, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, observer);
    }

    @Override
    public boolean drawImage(java.awt.Image img, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2, java.awt.Color bgcolor, java.awt.image.ImageObserver observer)
    {
        return graphics.drawImage(img, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, bgcolor, observer);
    }

    @Override
    public void dispose()
    {
        graphics.dispose();
    }

    @Override
    public void drawString(java.text.AttributedCharacterIterator iterator, float x, float y)
    {
        graphics.drawString(iterator, x, y);
    }

    @Override
    public void drawGlyphVector(java.awt.font.GlyphVector g, float x, float y)
    {
        graphics.drawGlyphVector(g, x, y);
    }

    @Override
    public void fill(java.awt.Shape s)
    {
        graphics.fill(s);
    }

    @Override
    public boolean hit(java.awt.Rectangle rect, java.awt.Shape s, boolean onStroke)
    {
        return graphics.hit(rect, s, onStroke);
    }

    @Override
    public java.awt.GraphicsConfiguration getDeviceConfiguration()
    {
        return graphics.getDeviceConfiguration();
    }

    @Override
    public void setComposite(java.awt.Composite comp)
    {
        graphics.setComposite(comp);
    }

    @Override
    public void setPaint(java.awt.Paint paint)
    {
        graphics.setPaint(paint);
    }

    @Override
    public void setStroke(java.awt.Stroke s)
    {
        graphics.setStroke(s);
    }

    @Override
    public void setRenderingHint(java.awt.RenderingHints.Key hintKey, Object hintValue)
    {
        graphics.setRenderingHint(hintKey, hintValue);
    }

    @Override
    public Object getRenderingHint(java.awt.RenderingHints.Key hintKey)
    {
        return graphics.getRenderingHint(hintKey);
    }

    @Override
    public void setRenderingHints(java.util.Map<?,?> hints)
    {
        graphics.setRenderingHints(hints);
    }

    @Override
    public void addRenderingHints(java.util.Map<?,?> hints)
    {
        graphics.addRenderingHints(hints);
    }

    @Override
    public java.awt.RenderingHints getRenderingHints()
    {
        return graphics.getRenderingHints();
    }

    @Override
    public java.awt.Graphics create()
    {
        return graphics.create();
    }

    @Override
    public void translate(int x, int y)
    {
        graphics.translate(x, y);
    }

    @Override
    public java.awt.Color getColor()
    {
        return graphics.getColor();
    }

    @Override
    public void setColor(java.awt.Color c)
    {
        graphics.setColor(c);
    }

    @Override
    public void setPaintMode()
    {
        graphics.setPaintMode();
    }

    @Override
    public void setXORMode(java.awt.Color c1)
    {
        graphics.setXORMode(c1);
    }

    @Override
    public java.awt.Font getFont()
    {
        return graphics.getFont();
    }

    @Override
    public void setFont(java.awt.Font font)
    {
        graphics.setFont(font);
    }

    @Override
    public java.awt.FontMetrics getFontMetrics(java.awt.Font f)
    {
        return graphics.getFontMetrics(f);
    }

    @Override
    public java.awt.Rectangle getClipBounds()
    {
        return graphics.getClipBounds();
    }

    @Override
    public void clipRect(int x, int y, int width, int height)
    {
        graphics.clipRect(x, y, width, height);
    }

    @Override
    public void setClip(int x, int y, int width, int height)
    {
        graphics.setClip(x, y, width, height);
    }

    @Override
    public java.awt.Shape getClip()
    {
        return graphics.getClip();
    }

    @Override
    public void setClip(java.awt.Shape clip)
    {
        graphics.setClip(clip);
    }

    @Override
    public void copyArea(int x, int y, int width, int height, int dx, int dy)
    {
        graphics.copyArea(x, y, width, height, dx, dy);
    }

    @Override
    public void drawLine(int x1, int y1, int x2, int y2)
    {
        graphics.drawLine(x1, y1, x2, y2);
    }

    @Override
    public void fillRect(int x, int y, int width, int height)
    {
        graphics.fillRect(x, y, width, height);
    }

    @Override
    public void clearRect(int x, int y, int width, int height)
    {
        graphics.clearRect(x, y, width, height);
    }

    @Override
    public void drawRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight)
    {
        graphics.drawRoundRect(x, y, width, height, arcWidth, arcHeight);
    }

    @Override
    public void fillRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight)
    {
        graphics.fillRoundRect(x, y, width, height, arcWidth, arcHeight);
    }

    @Override
    public void drawOval(int x, int y, int width, int height)
    {
        graphics.drawOval(x, y, width, height);
    }

    @Override
    public void fillOval(int x, int y, int width, int height)
    {
        graphics.fillOval(x, y, width, height);
    }

    @Override
    public void drawArc(int x, int y, int width, int height, int startAngle, int arcAngle)
    {
        graphics.drawArc(x, y, width, height, startAngle, arcAngle);
    }

    @Override
    public void fillArc(int x, int y, int width, int height, int startAngle, int arcAngle)
    {
        graphics.fillArc(x, y, width, height, startAngle, arcAngle);
    }

    @Override
    public void drawPolyline(int[] xPoints, int[] yPoints, int nPoints)
    {
        graphics.drawPolyline(xPoints, yPoints, nPoints);
    }

    @Override
    public void drawPolygon(int[] xPoints, int[] yPoints, int nPoints)
    {
        graphics.drawPolygon(xPoints, yPoints, nPoints);
    }

    @Override
    public void fillPolygon(int[] xPoints, int[] yPoints, int nPoints)
    {
        graphics.fillPolygon(xPoints, yPoints, nPoints);
    }

    @Override
    public void translate(double tx, double ty)
    {
        graphics.translate(tx, ty);
    }

    @Override
    public void rotate(double theta)
    {
        graphics.rotate(theta);
    }

    @Override
    public void rotate(double theta, double x, double y)
    {
        graphics.rotate(theta, x, y);
    }

    @Override
    public void scale(double sx, double sy)
    {
        graphics.scale(sx, sy);
    }

    @Override
    public void shear(double shx, double shy)
    {
        graphics.shear(shx, shy);
    }

    @Override
    public void transform(java.awt.geom.AffineTransform Tx)
    {
        graphics.transform(Tx);
    }

    @Override
    public void setTransform(java.awt.geom.AffineTransform Tx)
    {
        graphics.setTransform(Tx);
    }

    @Override
    public java.awt.geom.AffineTransform getTransform()
    {
        return graphics.getTransform();
    }

    @Override
    public java.awt.Paint getPaint()
    {
        return graphics.getPaint();
    }

    @Override
    public java.awt.Composite getComposite()
    {
        return graphics.getComposite();
    }

    @Override
    public void setBackground(java.awt.Color color)
    {
        graphics.setBackground(color);
    }

    @Override
    public java.awt.Color getBackground()
    {
        return graphics.getBackground();
    }

    @Override
    public java.awt.Stroke getStroke()
    {
        return graphics.getStroke();
    }

    @Override
    public void clip(java.awt.Shape s)
    {
        graphics.clip(s);
    }

    @Override
    public java.awt.font.FontRenderContext getFontRenderContext()
    {
        return graphics.getFontRenderContext();
    }

    @Override
    public void drawText(String text, Distance baselineX, Distance baselineY)
    {
        final double baselineXInPixels = parentWindow.convertHorizontalDistanceToPixels(baselineX);
        final double baselineYInPixels = parentWindow.convertVerticalDistanceToPixels(baselineY);
        drawString(text, (float)baselineXInPixels, (float)baselineYInPixels);
    }

    @Override
    public void drawLine(Distance startX, Distance startY, Distance endX, Distance endY)
    {
        final int startXInPixels = (int)parentWindow.convertHorizontalDistanceToPixels(startX);
        final int startYInPixels = (int)parentWindow.convertVerticalDistanceToPixels(startY);
        final int endXInPixels = (int)parentWindow.convertHorizontalDistanceToPixels(endX);
        final int endYInPixels = (int)parentWindow.convertVerticalDistanceToPixels(endY);
        drawLine(startXInPixels, startYInPixels, endXInPixels, endYInPixels);
    }

    @Override
    public void drawRectangle(Distance topLeftX, Distance topLeftY, Distance width, Distance height)
    {
        final int topLeftXInPixels = (int)parentWindow.convertHorizontalDistanceToPixels(topLeftX);
        final int topLeftYInPixels = (int)parentWindow.convertVerticalDistanceToPixels(topLeftY);
        final int widthInPixels = (int)parentWindow.convertHorizontalDistanceToPixels(width);
        final int heightInPixels = (int)parentWindow.convertVerticalDistanceToPixels(height);
        drawRect(topLeftXInPixels, topLeftYInPixels, widthInPixels, heightInPixels);
    }
}
