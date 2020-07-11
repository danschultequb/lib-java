package qub;

public interface SwingUIElementTests
{
    static void test(TestRunner runner, Function1<Test,? extends SwingUIElement> creator)
    {
        PreCondition.assertNotNull(runner, "runner");
        PreCondition.assertNotNull(creator, "creator");

        runner.testGroup(SwingUIElement.class, () ->
        {
            UIElementTests.test(runner, creator);

            runner.testGroup("setWidth(Distance)", () ->
            {
                runner.test("returns SwingUIElement", (Test test) ->
                {
                    final SwingUIElement uiElement = creator.run(test);
                    final SwingUIElement setWidthResult = uiElement.setWidth(Distance.inches(2));
                    test.assertSame(uiElement, setWidthResult);
                });
            });

            runner.testGroup("setHeight(Distance)", () ->
            {
                runner.test("returns SwingUIElement", (Test test) ->
                {
                    final SwingUIElement uiElement = creator.run(test);
                    final SwingUIElement setHeightResult = uiElement.setHeight(Distance.inches(3));
                    test.assertSame(uiElement, setHeightResult);
                });
            });

            runner.testGroup("setSize(Size2D)", () ->
            {
                runner.test("returns SwingUIElement", (Test test) ->
                {
                    final SwingUIElement uiElement = creator.run(test);
                    final SwingUIElement setWidthResult = uiElement.setSize(Size2D.create(Distance.inches(2), Distance.inches(3)));
                    test.assertSame(uiElement, setWidthResult);
                });
            });

            runner.testGroup("setSize(Distance,Distance)", () ->
            {
                runner.test("returns SwingUIElement", (Test test) ->
                {
                    final SwingUIElement uiElement = creator.run(test);
                    final SwingUIElement setWidthResult = uiElement.setSize(Distance.inches(2), Distance.inches(3));
                    test.assertSame(uiElement, setWidthResult);
                });
            });

            runner.testGroup("setPadding(UIPadding)", () ->
            {
                runner.test("returns SwingUIElement", (Test test) ->
                {
                    final SwingUIElement uiElement = creator.run(test);
                    final SwingUIElement setPaddingResult = uiElement.setPadding(UIPadding.zero);
                    test.assertSame(uiElement, setPaddingResult);
                });
            });

            runner.testGroup("setBackgroundColor(Color)", () ->
            {
                runner.test("returns SwingUIElement", (Test test) ->
                {
                    final SwingUIElement uiElement = creator.run(test);
                    final SwingUIElement setBackgroundColorResult = uiElement.setBackgroundColor(Color.blue);
                    test.assertSame(uiElement, setBackgroundColorResult);
                });
            });

            runner.test("getComponent()", (Test test) ->
            {
                final SwingUIElement uiElement = creator.run(test);
                final javax.swing.JComponent component = uiElement.getComponent();
                test.assertNotNull(component);
                test.assertSame(component, uiElement.getComponent());
            });

            runner.test("getJComponent()", (Test test) ->
            {
                final SwingUIElement uiElement = creator.run(test);
                final javax.swing.JComponent jComponent = uiElement.getJComponent();
                test.assertNotNull(jComponent);
                test.assertSame(jComponent, uiElement.getJComponent());
                test.assertSame(jComponent, uiElement.getComponent());
            });
        });
    }
}
