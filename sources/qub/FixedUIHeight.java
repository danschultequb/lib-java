package qub;

public class FixedUIHeight extends UIHeight
{
    private final Distance height;

    public FixedUIHeight(Distance height)
    {
        PreCondition.assertGreaterThanOrEqualTo(height, Distance.zero, "height");

        this.height = height;
    }

    @Override
    public Distance getHeight(UIElement uiElement)
    {
        return height;
    }
}
