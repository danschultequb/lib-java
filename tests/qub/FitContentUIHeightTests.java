package qub;

public class FitContentUIHeightTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(FitContentUIHeight.class, () ->
        {
            runner.testGroup("getHeight(UIElement)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final FitContentUIHeight uiHeight = new FitContentUIHeight();
                    test.assertThrows(() -> uiHeight.getHeight(null), new PreConditionFailure("uiElement cannot be null."));
                });

                runner.test("with UIElement that has null content width", (Test test) ->
                {
                    final FitContentUIHeight uiHeight = new FitContentUIHeight();
                    final UIText uiText = new UIText("test");
                    test.assertNull(uiText.getContentWidth());
                    test.assertEqual(Distance.zero, uiHeight.getHeight(uiText));
                });

                runner.test("with UIElement that has non-null content width and no padding", (Test test) ->
                {
                    try (final FakeWindow window = new FakeWindow())
                    {
                        final FitContentUIHeight uiHeight = new FitContentUIHeight();

                        final UIText uiText = new UIText("test");
                        window.setContent(uiText);
                        test.assertGreaterThan(uiText.getContentWidth(), Distance.zero);
                        test.assertEqual(Distance.inches(0.1509375), uiHeight.getHeight(uiText));
                    }
                });

                runner.test("with UIElement that has non-null content width and padding", (Test test) ->
                {
                    try (final FakeWindow window = new FakeWindow())
                    {
                        final FitContentUIHeight uiHeight = new FitContentUIHeight();

                        final UIText uiText = new UIText("test");
                        window.setContent(uiText);
                        uiText.setPadding(Distance.inches(0.5));
                        test.assertGreaterThan(uiText.getContentWidth(), Distance.zero);
                        test.assertEqual(Distance.inches(1.1509375), uiHeight.getHeight(uiText));
                    }
                });
            });
        });
    }
}
