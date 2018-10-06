package qub;

/**
 * A UIPainter that records the actions that are performed while drawing UIElements.
 */
public class FakePainter implements UIPainter
{
    private final List<PainterAction> actions;
    private final List<Vector2D> translations;
    private final List<Distance> fontSizes;

    /**
     * Create a new FakePainter.
     */
    public FakePainter()
    {
        this.actions = new ArrayList<>();
        this.translations = ArrayList.fromValues(new Vector2D[] { Vector2D.zero });
        this.fontSizes = ArrayList.fromValues(new Distance[] { Distance.fontPoints(14) });
    }

    public void clearActions()
    {
        actions.clear();
    }

    public Indexable<PainterAction> getActions()
    {
        return actions;
    }

    public Vector2D getTranslation()
    {
        return translations.last();
    }

    public Distance getFontSize()
    {
        return fontSizes.last();
    }

    private Point2D transform(Point2D point)
    {
        PreCondition.assertNotNull(point, "point");

        return getTranslation().plus(point);
    }

    @Override
    public void drawText(String text, Point2D topLeft)
    {
        actions.add(new DrawTextAction(text, transform(topLeft), getFontSize()));
    }

    @Override
    public void drawText(String text)
    {
        PreCondition.assertNotNullAndNotEmpty(text, "text");

        drawText(text, Point2D.zero);
    }

    @Override
    public void drawText(String text, Distance topLeftX, Distance topLeftY)
    {
        drawText(text, new Point2D(topLeftX, topLeftY));
    }

    @Override
    public void drawLine(Point2D start, Point2D end)
    {
        actions.add(new DrawLineAction(transform(start), transform(end)));
    }

    @Override
    public void drawLine(Distance startX, Distance startY, Distance endX, Distance endY)
    {
        drawLine(new Point2D(startX, startY), new Point2D(endX, endY));
    }

    @Override
    public void drawRectangle(Distance topLeftX, Distance topLeftY, Distance width, Distance height)
    {
        PreCondition.assertNotNull(topLeftX, "topLeftX");
        PreCondition.assertNotNull(topLeftY, "topLeftY");
        PreCondition.assertGreaterThan(width, Distance.zero, "width");
        PreCondition.assertGreaterThan(height, Distance.zero, "height");

        actions.add(new DrawRectangleAction(new Point2D(topLeftX, topLeftY), width, height));
    }

    @Override
    public void translate(Distance x, Distance y)
    {
        final Vector2D currentTranslation = getTranslation();
        translations.setLast(currentTranslation.plusVector(x, y));
    }

    @Override
    public void translateY(Distance y)
    {
        translate(Distance.zero, y);
    }

    @Override
    public Disposable saveTransform()
    {
        translations.add(getTranslation());
        return new BasicDisposable()
        {
            @Override
            protected void onDispose()
            {
                restoreTransform();
            }
        };
    }

    @Override
    public void restoreTransform()
    {
        PreCondition.assertLessThanOrEqualTo(translations.getCount(), 2, "translations.getCount()");

        translations.removeLast();
    }

    @Override
    public void setFontSize(Distance fontSize)
    {
        PreCondition.assertNotNull(fontSize, "fonstSize");
        PreCondition.assertGreaterThan(fontSize, Distance.zero, "fontSize");

        fontSizes.setLast(fontSize);
    }

    @Override
    public void saveFont()
    {
        fontSizes.add(getFontSize());
    }

    @Override
    public void restoreFont()
    {
        PreCondition.assertGreaterThanOrEqualTo(fontSizes.getCount(), 2, "fontSizes.getCount()");

        fontSizes.removeLast();
    }
}
