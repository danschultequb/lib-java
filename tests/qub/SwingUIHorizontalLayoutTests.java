package qub;

public interface SwingUIHorizontalLayoutTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(SwingUIHorizontalLayout.class, () ->
        {
            final Function1<Test,SwingUIHorizontalLayout> creator = (Test test) ->
            {
                final Display display = test.getDisplays().first();
                final AsyncRunner asyncRunner = test.getMainAsyncRunner();
                final AWTUIBase base = AWTUIBase.create(display, asyncRunner);
                return SwingUIHorizontalLayout.create(base);
            };

            UIHorizontalLayoutTests.test(runner, creator);

            runner.testGroup("create(JavaUIBase)", () ->
            {
                runner.test("with null uiBase", (Test test) ->
                {
                    test.assertThrows(() -> SwingUIHorizontalLayout.create(null),
                        new PreConditionFailure("uiBase cannot be null."));
                });

                runner.test("with valid arguments", (Test test) ->
                {
                    final Display display = test.getDisplays().first();
                    final AsyncRunner asyncRunner = test.getMainAsyncRunner();
                    final AWTUIBase base = AWTUIBase.create(display, asyncRunner);
                    final SwingUIHorizontalLayout verticalLayout = SwingUIHorizontalLayout.create(base);
                    test.assertNotNull(verticalLayout);
                    test.assertEqual(Distance.zero, verticalLayout.getWidth());
                    test.assertEqual(Distance.zero, verticalLayout.getHeight());
                    test.assertEqual(HorizontalDirection.LeftToRight, verticalLayout.getDirection());

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
                    test.assertThrows(() -> SwingUIHorizontalLayout.create(display, asyncRunner),
                        new PreConditionFailure("display cannot be null."));
                });

                runner.test("with null asyncRunner", (Test test) ->
                {
                    final Display display = test.getDisplays().first();
                    final AsyncRunner asyncRunner = null;
                    test.assertThrows(() -> SwingUIHorizontalLayout.create(display, asyncRunner),
                        new PreConditionFailure("asyncRunner cannot be null."));
                });

                runner.test("with valid arguments", (Test test) ->
                {
                    final Display display = test.getDisplays().first();
                    final AsyncRunner asyncRunner = test.getMainAsyncRunner();
                    final SwingUIHorizontalLayout verticalLayout = SwingUIHorizontalLayout.create(display, asyncRunner);
                    test.assertNotNull(verticalLayout);
                    test.assertEqual(Distance.zero, verticalLayout.getWidth());
                    test.assertEqual(Distance.zero, verticalLayout.getHeight());
                    test.assertEqual(HorizontalDirection.LeftToRight, verticalLayout.getDirection());

                    final javax.swing.JPanel jComponent = verticalLayout.getComponent();
                    test.assertNotNull(jComponent);
                });
            });

            runner.testGroup("setWidth(Distance)", () ->
            {
                runner.test("should return " + Types.getTypeName(UIVerticalLayout.class), (Test test) ->
                {
                    final SwingUIHorizontalLayout verticalLayout = creator.run(test);
                    final SwingUIHorizontalLayout setWidthResult = verticalLayout.setWidth(Distance.inches(1));
                    test.assertSame(verticalLayout, setWidthResult);
                });
            });

            runner.testGroup("setHeight(Distance)", () ->
            {
                runner.test("should return " + Types.getTypeName(SwingUIHorizontalLayout.class), (Test test) ->
                {
                    final SwingUIHorizontalLayout verticalLayout = creator.run(test);
                    final SwingUIHorizontalLayout setHeightResult = verticalLayout.setHeight(Distance.inches(1));
                    test.assertSame(verticalLayout, setHeightResult);
                });
            });

            runner.testGroup("setSize(Size2D)", () ->
            {
                runner.test("should return " + Types.getTypeName(SwingUIHorizontalLayout.class), (Test test) ->
                {
                    final SwingUIHorizontalLayout verticalLayout = creator.run(test);
                    final SwingUIHorizontalLayout setHeightResult = verticalLayout.setSize(Size2D.create(Distance.inches(2), Distance.inches(3)));
                    test.assertSame(verticalLayout, setHeightResult);
                });
            });

            runner.testGroup("setSize(Distance,Distance)", () ->
            {
                runner.test("should return " + Types.getTypeName(SwingUIHorizontalLayout.class), (Test test) ->
                {
                    final SwingUIHorizontalLayout verticalLayout = creator.run(test);
                    final SwingUIHorizontalLayout setHeightResult = verticalLayout.setSize(Distance.inches(2), Distance.inches(3));
                    test.assertSame(verticalLayout, setHeightResult);
                });
            });

            runner.testGroup("setDirection(HorizontalDirection)", () ->
            {
                runner.test("should return " + Types.getTypeName(SwingUIHorizontalLayout.class), (Test test) ->
                {
                    final SwingUIHorizontalLayout verticalLayout = creator.run(test);
                    final SwingUIHorizontalLayout setHeightResult = verticalLayout.setDirection(HorizontalDirection.RightToLeft);
                    test.assertSame(verticalLayout, setHeightResult);
                });
            });

            runner.testGroup("add(UIElement)", () ->
            {
                runner.test("should return " + Types.getTypeName(SwingUIHorizontalLayout.class), (Test test) ->
                {
                    final SwingUIHorizontalLayout verticalLayout = creator.run(test);
                    final UIElement element = creator.run(test);

                    final SwingUIHorizontalLayout addResult = verticalLayout.add(element);
                    test.assertSame(verticalLayout, addResult);
                });
            });

            runner.testGroup("addAll(UIElement...)", () ->
            {
                runner.test("should return " + Types.getTypeName(SwingUIHorizontalLayout.class), (Test test) ->
                {
                    final SwingUIHorizontalLayout verticalLayout = creator.run(test);

                    final SwingUIHorizontalLayout addAllResult = verticalLayout.addAll();
                    test.assertSame(verticalLayout, addAllResult);
                });
            });

            runner.testGroup("addAll(Iterable<? extends UIElement>)", () ->
            {
                runner.test("should return " + Types.getTypeName(SwingUIHorizontalLayout.class), (Test test) ->
                {
                    final SwingUIHorizontalLayout verticalLayout = creator.run(test);

                    final SwingUIHorizontalLayout addResult = verticalLayout.addAll(Iterable.create());
                    test.assertSame(verticalLayout, addResult);
                });
            });
        });
    }
}
