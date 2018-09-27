package qub;

/**
 * A UIPainter that records the actions that are performed while drawing UIElements.
 */
public class TestPainter implements UIPainter
{
    private final List<PainterAction> actions;
    private final List<Vector2D> translations;
    private final List<Distance> fontSizes;

    /**
     * Create a new TestPainter.
     */
    public TestPainter()
    {
        this.actions = new ArrayList<>();
        this.translations = ArrayList.fromValues(new Vector2D[] { Vector2D.zero });
        this.fontSizes = ArrayList.fromValues(new Distance[] { Distance.fontPoints(14) });
    }

    private Vector2D getTranslation()
    {
        return translations.last();
    }

    private Distance getFontSize()
    {
        return fontSizes.last();
    }

    private Distance transformX(Distance value)
    {
        PreCondition.assertNotNull(value, "value");

        return getTranslation().getX().plus(value);
    }

    private Distance transformY(Distance value)
    {
        PreCondition.assertNotNull(value, "value");

        return getTranslation().getY().plus(value);
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

        drawText(text, Distance.zero, Distance.zero);
    }

    @Override
    public void drawText(String text, Distance topLeftX, Distance topLeftY)
    {
        drawText(text, new Point2D(topLeftX, topLeftY));
    }

    @Override
    public void drawLine(Distance startX, Distance startY, Distance endX, Distance endY)
    {
        actions.add(new DrawLineAction(transformX(startX), transformY(startY), transformX(endX), transformY(endY)));
    }

    @Override
    public void drawRectangle(Distance topLeftX, Distance topLeftY, Distance width, Distance height)
    {

    }

    @Override
    public void translate(Distance x, Distance y)
    {
        final Vector2D currentTranslation = getTranslation();
        translations.setLast(currentTranslation.plusVector(x, y));
    }

    @Override
    public void saveTransform()
    {
        translations.add(getTranslation());
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
        fontSizes.add(fontSizes.last());
    }

    @Override
    public void restoreFont()
    {
        PreCondition.assertGreaterThanOrEqualTo(fontSizes.getCount(), 2, "fontSizes.getCount()");

        fontSizes.removeLast();
    }
}
