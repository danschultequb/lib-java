package qub;

public interface SwingUIVerticalLayoutTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(SwingUIVerticalLayout.class, () ->
        {
            final Function1<Test,SwingUIVerticalLayout> creator = (Test test) ->
            {
                final Display display = test.getDisplays().first();
                final AsyncRunner asyncRunner = test.getMainAsyncRunner();
                return SwingUIVerticalLayout.create(display, asyncRunner);
            };

            UIVerticalLayoutTests.test(runner, creator);

            runner.testGroup("create(JavaUIBase)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> SwingUIVerticalLayout.create(null),
                        new PreConditionFailure("uiBase cannot be null."));
                });

                runner.test("with non-null", (Test test) ->
                {
                    final Display display = test.getDisplays().first();
                    final AsyncRunner asyncRunner = test.getMainAsyncRunner();
                    final AWTUIBase base = AWTUIBase.create(display, asyncRunner);
                    final SwingUIVerticalLayout verticalLayout = SwingUIVerticalLayout.create(base);
                    test.assertNotNull(verticalLayout);
                    test.assertEqual(Distance.zero, verticalLayout.getWidth());
                    test.assertEqual(Distance.zero, verticalLayout.getHeight());
                    test.assertEqual(VerticalDirection.TopToBottom, verticalLayout.getDirection());

                    final javax.swing.JPanel jComponent = verticalLayout.getComponent();
                    test.assertNotNull(jComponent);
                });
            });

            runner.testGroup("create(Display,AsyncRunner)", () ->
            {
                runner.test("with null display", (Test test) ->
                {
                    final Display display = null;
                    final AsyncRunner asyncRunner = test.getMainAsyncRunner();
                    test.assertThrows(() -> SwingUIVerticalLayout.create(display, asyncRunner),
                        new PreConditionFailure("display cannot be null."));
                });

                runner.test("with null asyncRunner", (Test test) ->
                {
                    final Display display = test.getDisplays().first();
                    final AsyncRunner asyncRunner = null;
                    test.assertThrows(() -> SwingUIVerticalLayout.create(display, asyncRunner),
                        new PreConditionFailure("asyncRunner cannot be null."));
                });

                runner.test("with non-null arguments", (Test test) ->
                {
                    final Display display = test.getDisplays().first();
                    final AsyncRunner asyncRunner = test.getMainAsyncRunner();
                    final SwingUIVerticalLayout verticalLayout = SwingUIVerticalLayout.create(display, asyncRunner);
                    test.assertNotNull(verticalLayout);
                    test.assertEqual(Distance.zero, verticalLayout.getWidth());
                    test.assertEqual(Distance.zero, verticalLayout.getHeight());
                    test.assertEqual(VerticalDirection.TopToBottom, verticalLayout.getDirection());

                    final javax.swing.JPanel jComponent = verticalLayout.getComponent();
                    test.assertNotNull(jComponent);
                });
            });

            runner.testGroup("setWidth(Distance)", () ->
            {
                runner.test("should return " + Types.getTypeName(UIVerticalLayout.class), (Test test) ->
                {
                    final UIVerticalLayout verticalLayout = creator.run(test);
                    final UIVerticalLayout setWidthResult = verticalLayout.setWidth(Distance.inches(1));
                    test.assertSame(verticalLayout, setWidthResult);
                });
            });

            runner.testGroup("setHeight(Distance)", () ->
            {
                runner.test("should return " + Types.getTypeName(SwingUIVerticalLayout.class), (Test test) ->
                {
                    final SwingUIVerticalLayout verticalLayout = creator.run(test);
                    final SwingUIVerticalLayout setHeightResult = verticalLayout.setHeight(Distance.inches(1));
                    test.assertSame(verticalLayout, setHeightResult);
                });
            });

            runner.testGroup("setSize(Size2D)", () ->
            {
                runner.test("should return " + Types.getTypeName(SwingUIVerticalLayout.class), (Test test) ->
                {
                    final SwingUIVerticalLayout verticalLayout = creator.run(test);
                    final SwingUIVerticalLayout setHeightResult = verticalLayout.setSize(Size2D.create(Distance.inches(2), Distance.inches(3)));
                    test.assertSame(verticalLayout, setHeightResult);
                });
            });

            runner.testGroup("setSize(Distance,Distance)", () ->
            {
                runner.test("should return " + Types.getTypeName(SwingUIVerticalLayout.class), (Test test) ->
                {
                    final SwingUIVerticalLayout verticalLayout = creator.run(test);
                    final SwingUIVerticalLayout setHeightResult = verticalLayout.setSize(Distance.inches(2), Distance.inches(3));
                    test.assertSame(verticalLayout, setHeightResult);
                });
            });

            runner.testGroup("setDirection(VerticalDirection)", () ->
            {
                runner.test("should return " + Types.getTypeName(SwingUIVerticalLayout.class), (Test test) ->
                {
                    final SwingUIVerticalLayout verticalLayout = creator.run(test);
                    final SwingUIVerticalLayout setHeightResult = verticalLayout.setDirection(VerticalDirection.TopToBottom);
                    test.assertSame(verticalLayout, setHeightResult);
                });
            });

            runner.testGroup("add(UIElement)", () ->
            {
                runner.test("should return " + Types.getTypeName(SwingUIVerticalLayout.class), (Test test) ->
                {
                    final SwingUIVerticalLayout verticalLayout = creator.run(test);
                    final UIElement element = creator.run(test);

                    final SwingUIVerticalLayout addResult = verticalLayout.add(element);
                    test.assertSame(verticalLayout, addResult);
                });
            });

            runner.testGroup("addAll(UIElement...)", () ->
            {
                runner.test("should return " + Types.getTypeName(SwingUIVerticalLayout.class), (Test test) ->
                {
                    final SwingUIVerticalLayout verticalLayout = creator.run(test);

                    final SwingUIVerticalLayout addAllResult = verticalLayout.addAll();
                    test.assertSame(verticalLayout, addAllResult);
                });
            });

            runner.testGroup("addAll(Iterable<? extends UIElement>)", () ->
            {
                runner.test("should return " + Types.getTypeName(SwingUIVerticalLayout.class), (Test test) ->
                {
                    final SwingUIVerticalLayout verticalLayout = creator.run(test);

                    final SwingUIVerticalLayout addResult = verticalLayout.addAll(Iterable.create());
                    test.assertSame(verticalLayout, addResult);
                });
            });
        });
    }
}
