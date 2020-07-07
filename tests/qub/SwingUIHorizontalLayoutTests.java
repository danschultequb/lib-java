package qub;

public interface SwingUIHorizontalLayoutTests
{
    static SwingUIBase createUIBase(Test test)
    {
        final Display display = new Display(1000, 1000, 100, 100);
        return SwingUIBase.create(display, test.getMainAsyncRunner(), test.getParallelAsyncRunner());
    }
    
    static SwingUIBuilder createUIBuilder(Test test)
    {
        final SwingUIBase uiBase = SwingUIHorizontalLayoutTests.createUIBase(test);
        return SwingUIBuilder.create(uiBase);
    }
    
    static SwingUIHorizontalLayout createUIHorizontalLayout(Test test)
    {
        final SwingUIBuilder uiBuilder = SwingUIHorizontalLayoutTests.createUIBuilder(test);
        return uiBuilder.create(SwingUIHorizontalLayout.class).await();
    }
    
    static void test(TestRunner runner)
    {
        runner.testGroup(SwingUIHorizontalLayout.class, () ->
        {
            UIHorizontalLayoutTests.test(runner, SwingUIHorizontalLayoutTests::createUIHorizontalLayout);
            SwingUIElementTests.test(runner, SwingUIHorizontalLayoutTests::createUIHorizontalLayout);

            runner.testGroup("create(SwingUIBase)", () ->
            {
                runner.test("with null uiBase", (Test test) ->
                {
                    test.assertThrows(() -> SwingUIHorizontalLayout.create(null),
                        new PreConditionFailure("uiBase cannot be null."));
                });

                runner.test("with valid arguments", (Test test) ->
                {
                    final SwingUIBase base = SwingUIHorizontalLayoutTests.createUIBase(test);
                    final SwingUIHorizontalLayout verticalLayout = SwingUIHorizontalLayout.create(base);
                    test.assertNotNull(verticalLayout);
                    test.assertEqual(Distance.zero, verticalLayout.getWidth());
                    test.assertEqual(Distance.zero, verticalLayout.getHeight());

                    final javax.swing.JPanel jComponent = verticalLayout.getComponent();
                    test.assertNotNull(jComponent);
                });
            });

            runner.testGroup("setWidth(Distance)", () ->
            {
                runner.test("should return " + Types.getTypeName(UIVerticalLayout.class), (Test test) ->
                {
                    final SwingUIHorizontalLayout verticalLayout = SwingUIHorizontalLayoutTests.createUIHorizontalLayout(test);
                    final SwingUIHorizontalLayout setWidthResult = verticalLayout.setWidth(Distance.inches(1));
                    test.assertSame(verticalLayout, setWidthResult);
                });
            });

            runner.testGroup("setHeight(Distance)", () ->
            {
                runner.test("should return " + Types.getTypeName(SwingUIHorizontalLayout.class), (Test test) ->
                {
                    final SwingUIHorizontalLayout verticalLayout = SwingUIHorizontalLayoutTests.createUIHorizontalLayout(test);
                    final SwingUIHorizontalLayout setHeightResult = verticalLayout.setHeight(Distance.inches(1));
                    test.assertSame(verticalLayout, setHeightResult);
                });
            });

            runner.testGroup("setSize(Size2D)", () ->
            {
                runner.test("should return " + Types.getTypeName(SwingUIHorizontalLayout.class), (Test test) ->
                {
                    final SwingUIHorizontalLayout verticalLayout = SwingUIHorizontalLayoutTests.createUIHorizontalLayout(test);
                    final SwingUIHorizontalLayout setHeightResult = verticalLayout.setSize(Size2D.create(Distance.inches(2), Distance.inches(3)));
                    test.assertSame(verticalLayout, setHeightResult);
                });
            });

            runner.testGroup("setSize(Distance,Distance)", () ->
            {
                runner.test("should return " + Types.getTypeName(SwingUIHorizontalLayout.class), (Test test) ->
                {
                    final SwingUIHorizontalLayout verticalLayout = SwingUIHorizontalLayoutTests.createUIHorizontalLayout(test);
                    final SwingUIHorizontalLayout setHeightResult = verticalLayout.setSize(Distance.inches(2), Distance.inches(3));
                    test.assertSame(verticalLayout, setHeightResult);
                });
            });

            runner.testGroup("add(UIElement)", () ->
            {
                runner.test("should return " + Types.getTypeName(SwingUIHorizontalLayout.class), (Test test) ->
                {
                    final SwingUIHorizontalLayout verticalLayout = SwingUIHorizontalLayoutTests.createUIHorizontalLayout(test);
                    final UIElement element = SwingUIHorizontalLayoutTests.createUIHorizontalLayout(test);

                    final SwingUIHorizontalLayout addResult = verticalLayout.add(element);
                    test.assertSame(verticalLayout, addResult);
                });
            });

            runner.testGroup("addAll(UIElement...)", () ->
            {
                runner.test("should return " + Types.getTypeName(SwingUIHorizontalLayout.class), (Test test) ->
                {
                    final SwingUIHorizontalLayout verticalLayout = SwingUIHorizontalLayoutTests.createUIHorizontalLayout(test);

                    final SwingUIHorizontalLayout addAllResult = verticalLayout.addAll();
                    test.assertSame(verticalLayout, addAllResult);
                });
            });

            runner.testGroup("addAll(Iterable<? extends UIElement>)", () ->
            {
                runner.test("should return " + Types.getTypeName(SwingUIHorizontalLayout.class), (Test test) ->
                {
                    final SwingUIHorizontalLayout verticalLayout = SwingUIHorizontalLayoutTests.createUIHorizontalLayout(test);

                    final SwingUIHorizontalLayout addResult = verticalLayout.addAll(Iterable.create());
                    test.assertSame(verticalLayout, addResult);
                });
            });

            runner.test("getComponent()", (Test test) ->
            {
                final SwingUIHorizontalLayout uiElement = SwingUIHorizontalLayoutTests.createUIHorizontalLayout(test);
                final javax.swing.JPanel component = uiElement.getComponent();
                test.assertNotNull(component);
                test.assertSame(component, uiElement.getComponent());
            });

            runner.test("getJComponent()", (Test test) ->
            {
                final SwingUIHorizontalLayout uiElement = SwingUIHorizontalLayoutTests.createUIHorizontalLayout(test);
                final javax.swing.JPanel jComponent = uiElement.getJComponent();
                test.assertNotNull(jComponent);
                test.assertSame(jComponent, uiElement.getJComponent());
                test.assertSame(jComponent, uiElement.getComponent());
            });
        });
    }
}
