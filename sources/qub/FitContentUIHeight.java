package qub;

public class FitContentUIHeight extends UIHeight
{
    @Override
    public Distance getHeight(UIElement uiElement)
    {
        PreCondition.assertNotNull(uiElement, "uiElement");

        Distance result = uiElement.getContentHeight();
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
