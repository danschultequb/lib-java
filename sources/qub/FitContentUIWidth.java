package qub;

public class FitContentUIWidth extends UIWidth
{
    @Override
    public Distance getWidth(UIElement uiElement)
    {
        PreCondition.assertNotNull(uiElement, "uiElement");

        Distance result = uiElement.getContentWidth();
        if (result == null)
        {
            result = Distance.zero;
        }

        final Distance padding = uiElement.getPadding();
        if (padding != null)
        {
            result = result.plus(padding.times(2));
        }

        PostCondition.assertNullOrGreaterThanOrEqualTo(result, Distance.zero, "result");

        return result;
    }
}
