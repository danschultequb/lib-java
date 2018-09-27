package qub;

/**
 * A UIPainter that records the actions that are performed while drawing UIElements.
 */
public class TestPainter implements UIPainter
{
    private final List<PainterAction> actions;
    private final List<Vector2D> translation;

    /**
     * Create a new TestPainter.
     */
    public TestPainter()
    {
        this.actions = new ArrayList<>();
        this.translation = ArrayList.fromValues(new Vector2D[] { Vector2D.zero });
    }

    private Vector2D getTranslation()
    {
        return translation.last();
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
        actions.add(new DrawTextAction(text, transform(topLeft)));
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
        actions.add(new DrawTextAction(text, transformX(topLeftX), transformY(topLeftY)));
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
        final int savedTranslationCount = translation.getCount();
        translation.set(savedTranslationCount - 1, currentTranslation.plusVector(x, y));
    }

    @Override
    public void saveTransform()
    {
        translation.add(translation.last());
    }

    @Override
    public void restoreTransform()
    {
        PreCondition.assertLessThanOrEqualTo(translation.getCount(), 2, "translation.getCount()");

        translation.removeLast();
    }
}
