package qub;

public interface UIHorizontalLayoutTests
{
    static void test(TestRunner runner, Function1<FakeDesktopProcess,? extends UIHorizontalLayout> creator)
    {
        PreCondition.assertNotNull(runner, "runner");
        PreCondition.assertNotNull(creator, "creator");

        runner.testGroup(UIHorizontalLayout.class, () ->
        {
            runner.testGroup("setWidth(Distance)", () ->
            {
                runner.test("should return " + Types.getTypeName(UIHorizontalLayout.class), (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final UIHorizontalLayout horizontalLayout = creator.run(process);
                        final UIHorizontalLayout setWidthResult = horizontalLayout.setWidth(Distance.inches(1));
                        test.assertSame(horizontalLayout, setWidthResult);
                    }
                });
            });

            runner.testGroup("setHeight(Distance)", () ->
            {
                runner.test("should return " + Types.getTypeName(UIHorizontalLayout.class), (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final UIHorizontalLayout horizontalLayout = creator.run(process);
                        final UIHorizontalLayout setHeightResult = horizontalLayout.setHeight(Distance.inches(1));
                        test.assertSame(horizontalLayout, setHeightResult);
                    }
                });
            });

            runner.testGroup("setSize(Size2D)", () ->
            {
                runner.test("should return " + Types.getTypeName(UIHorizontalLayout.class), (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final UIHorizontalLayout horizontalLayout = creator.run(process);
                        final UIHorizontalLayout setHeightResult = horizontalLayout.setSize(Size2D.create(Distance.inches(2), Distance.inches(3)));
                        test.assertSame(horizontalLayout, setHeightResult);
                    }
                });
            });

            runner.testGroup("setSize(Distance,Distance)", () ->
            {
                runner.test("should return " + Types.getTypeName(UIHorizontalLayout.class), (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final UIHorizontalLayout horizontalLayout = creator.run(process);
                        final UIHorizontalLayout setHeightResult = horizontalLayout.setSize(Distance.inches(2), Distance.inches(3));
                        test.assertSame(horizontalLayout, setHeightResult);
                    }
                });
            });

            runner.testGroup("add(UIElement)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final UIHorizontalLayout horizontalLayout = creator.run(process);
                        test.assertThrows(() -> horizontalLayout.add((UIElement)null),
                            new PreConditionFailure("uiElement cannot be null."));
                    }
                });

                runner.test("with non-null", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final UIHorizontalLayout horizontalLayout = creator.run(process);
                        final UIElement element = creator.run(process);

                        final UIHorizontalLayout addResult = horizontalLayout.add(element);
                        test.assertSame(horizontalLayout, addResult);
                    }
                });
            });

            runner.testGroup("addAll(UIElement...)", () ->
            {
                runner.test("with null element", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final UIHorizontalLayout horizontalLayout = creator.run(process);

                        test.assertThrows(() -> horizontalLayout.addAll((UIElement)null),
                            new PreConditionFailure("uiElement cannot be null."));
                    }
                });

                runner.test("with no arguments", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final UIHorizontalLayout horizontalLayout = creator.run(process);

                        final UIHorizontalLayout addResult = horizontalLayout.addAll();
                        test.assertSame(horizontalLayout, addResult);
                    }
                });

                runner.test("with one argument", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final UIHorizontalLayout horizontalLayout = creator.run(process);
                        final UIElement element = creator.run(process);

                        final UIHorizontalLayout addResult = horizontalLayout.addAll(element);
                        test.assertSame(horizontalLayout, addResult);
                    }
                });

                runner.test("with multiple arguments", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final UIHorizontalLayout horizontalLayout = creator.run(process);
                        final UIElement element1 = creator.run(process);
                        final UIElement element2 = creator.run(process);

                        final UIHorizontalLayout addResult = horizontalLayout.addAll(element1, element2);
                        test.assertSame(horizontalLayout, addResult);
                    }
                });

                runner.test("with null array", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final UIHorizontalLayout horizontalLayout = creator.run(process);

                        test.assertThrows(() -> horizontalLayout.addAll((UIElement[])null),
                            new PreConditionFailure("uiElements cannot be null."));
                    }
                });

                runner.test("with empty array", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final UIHorizontalLayout horizontalLayout = creator.run(process);

                        final UIHorizontalLayout addResult = horizontalLayout.addAll(new UIElement[0]);
                        test.assertSame(horizontalLayout, addResult);
                    }
                });

                runner.test("with one element array", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final UIHorizontalLayout horizontalLayout = creator.run(process);
                        final UIElement element = creator.run(process);

                        final UIHorizontalLayout addResult = horizontalLayout.addAll(new UIElement[]{ element });
                        test.assertSame(horizontalLayout, addResult);
                    }
                });

                runner.test("with multiple element array", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final UIHorizontalLayout horizontalLayout = creator.run(process);
                        final UIElement element1 = creator.run(process);
                        final UIElement element2 = creator.run(process);

                        final UIHorizontalLayout addResult = horizontalLayout.addAll(new UIElement[]{ element1, element2 });
                        test.assertSame(horizontalLayout, addResult);
                    }
                });
            });

            runner.testGroup("addAll(Iterable<UIElement>)", () ->
            {
                runner.test("with null Iterable", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final UIHorizontalLayout horizontalLayout = creator.run(process);

                        test.assertThrows(() -> horizontalLayout.addAll((Iterable<UIElement>)null),
                            new PreConditionFailure("uiElements cannot be null."));
                    }
                });

                runner.test("with empty Iterable", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final UIHorizontalLayout horizontalLayout = creator.run(process);

                        final UIHorizontalLayout addResult = horizontalLayout.addAll(Iterable.create());
                        test.assertSame(horizontalLayout, addResult);
                    }
                });

                runner.test("with one element Iterable", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final UIHorizontalLayout horizontalLayout = creator.run(process);
                        final UIElement element = creator.run(process);

                        final UIHorizontalLayout addResult = horizontalLayout.addAll(Iterable.create(element));
                        test.assertSame(horizontalLayout, addResult);
                    }
                });

                runner.test("with multiple element Iterable", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final UIHorizontalLayout horizontalLayout = creator.run(process);
                        final UIElement element1 = creator.run(process);
                        final UIElement element2 = creator.run(process);

                        final UIHorizontalLayout addResult = horizontalLayout.addAll(Iterable.create(element1, element2));
                        test.assertSame(horizontalLayout, addResult);
                    }
                });
            });
        });
    }
}
