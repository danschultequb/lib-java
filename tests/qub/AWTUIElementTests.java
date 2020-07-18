package qub;

public interface AWTUIElementTests
{
    static void test(TestRunner runner, Function1<Test,? extends AWTUIElement> creator)
    {
        PreCondition.assertNotNull(runner, "runner");
        PreCondition.assertNotNull(creator, "creator");

        runner.testGroup(AWTUIElement.class, () ->
        {
            UIElementTests.test(runner, creator);

            runner.testGroup("setWidth(Distance)", () ->
            {
                runner.test("returns AWTUIElement", (Test test) ->
                {
                    final AWTUIElement uiElement = creator.run(test);
                    final AWTUIElement setWidthResult = uiElement.setWidth(Distance.inches(2));
                    test.assertSame(uiElement, setWidthResult);
                });
            });

            runner.testGroup("setWidthInPixels(int)", () ->
            {
                runner.test("returns AWTUIElement", (Test test) ->
                {
                    final AWTUIElement uiElement = creator.run(test);
                    final AWTUIElement setWidthInPixelsResult = uiElement.setWidthInPixels(2);
                    test.assertSame(uiElement, setWidthInPixelsResult);
                });
            });

            runner.testGroup("setHeight(Distance)", () ->
            {
                runner.test("returns AWTUIElement", (Test test) ->
                {
                    final AWTUIElement uiElement = creator.run(test);
                    final AWTUIElement setHeightResult = uiElement.setHeight(Distance.inches(3));
                    test.assertSame(uiElement, setHeightResult);
                });
            });

            runner.testGroup("setHeightInPixels(int)", () ->
            {
                runner.test("returns AWTUIElement", (Test test) ->
                {
                    final AWTUIElement uiElement = creator.run(test);
                    final AWTUIElement setHeightInPixelsResult = uiElement.setHeightInPixels(2);
                    test.assertSame(uiElement, setHeightInPixelsResult);
                });
            });

            runner.testGroup("setSize(Size2D)", () ->
            {
                runner.test("returns AWTUIElement", (Test test) ->
                {
                    final AWTUIElement uiElement = creator.run(test);
                    final AWTUIElement setWidthResult = uiElement.setSize(Size2D.create(Distance.inches(2), Distance.inches(3)));
                    test.assertSame(uiElement, setWidthResult);
                });
            });

            runner.testGroup("setSize(Distance,Distance)", () ->
            {
                runner.test("returns AWTUIElement", (Test test) ->
                {
                    final AWTUIElement uiElement = creator.run(test);
                    final AWTUIElement setWidthResult = uiElement.setSize(Distance.inches(2), Distance.inches(3));
                    test.assertSame(uiElement, setWidthResult);
                });
            });

            runner.testGroup("setPadding(UIPadding)", () ->
            {
                runner.test("returns AWTUIElement", (Test test) ->
                {
                    final AWTUIElement uiElement = creator.run(test);
                    final AWTUIElement setPaddingResult = uiElement.setPadding(UIPadding.zero);
                    test.assertSame(uiElement, setPaddingResult);
                });
            });

            runner.testGroup("setBackgroundColor(Color)", () ->
            {
                runner.test("returns AWTUIElement", (Test test) ->
                {
                    final AWTUIElement uiElement = creator.run(test);
                    final AWTUIElement setBackgroundColorResult = uiElement.setBackgroundColor(Color.blue);
                    test.assertSame(uiElement, setBackgroundColorResult);
                });
            });

            runner.test("getComponent()", (Test test) ->
            {
                final AWTUIElement uiElement = creator.run(test);
                final java.awt.Component component = uiElement.getComponent();
                test.assertNotNull(component);
                test.assertSame(component, uiElement.getComponent());
            });
        });
    }
}
