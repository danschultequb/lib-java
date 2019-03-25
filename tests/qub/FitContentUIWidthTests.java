package qub;

public class FitContentUIWidthTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(FitContentUIWidth.class, () ->
        {
            runner.testGroup("getWidth(UIElement)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final FitContentUIWidth uiWidth = new FitContentUIWidth();
                    test.assertThrows(() -> uiWidth.getWidth(null), new PreConditionFailure("uiElement cannot be null."));
                });

                runner.test("with UIElement that has null content width", (Test test) ->
                {
                    final FitContentUIWidth uiWidth = new FitContentUIWidth();
                    final UIText uiText = new UIText("test");
                    test.assertNull(uiText.getContentWidth());
                    test.assertEqual(Distance.zero, uiWidth.getWidth(uiText));
                });

                runner.test("with UIElement that has non-null content width and no padding", (Test test) ->
                {
                    try (final FakeWindow window = new FakeWindow())
                    {
                        final FitContentUIWidth uiWidth = new FitContentUIWidth();

                        final UIText uiText = new UIText("test");
                        window.setContent(uiText);
                        test.assertGreaterThan(uiText.getContentWidth(), Distance.zero);
                        test.assertEqual(Distance.inches(0.2), uiWidth.getWidth(uiText));
                    }
                });

                runner.test("with UIElement that has non-null content width and padding", (Test test) ->
                {
                    try (final FakeWindow window = new FakeWindow())
                    {
                        final FitContentUIWidth uiWidth = new FitContentUIWidth();

                        final UIText uiText = new UIText("test");
                        window.setContent(uiText);
                        uiText.setPadding(Distance.inches(0.5));
                        test.assertGreaterThan(uiText.getContentWidth(), Distance.zero);
                        test.assertEqual(Distance.inches(1.2), uiWidth.getWidth(uiText));
                    }
                });
            });
        });
    }
}
