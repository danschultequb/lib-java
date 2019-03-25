package qub;

public class FixedUIWidth extends UIWidth
{
    private final Distance width;

    public FixedUIWidth(Distance width)
    {
        PreCondition.assertNotNull(width, "width");
        PreCondition.assertGreaterThanOrEqualTo(width, Distance.zero, "width");

        this.width = width;
    }

    @Override
    public Distance getWidth(UIElement uiElement)
    {
        return width;
    }
}
