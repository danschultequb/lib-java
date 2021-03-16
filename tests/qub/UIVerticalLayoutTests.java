package qub;

public interface UIVerticalLayoutTests
{
    static void test(TestRunner runner, Function1<FakeDesktopProcess,? extends UIVerticalLayout> creator)
    {
        PreCondition.assertNotNull(runner, "runner");
        PreCondition.assertNotNull(creator, "creator");

        runner.testGroup(UIVerticalLayout.class, () ->
        {
            runner.testGroup("setWidth(Distance)", () ->
            {
                runner.test("should return " + Types.getTypeName(UIVerticalLayout.class), (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final UIVerticalLayout verticalLayout = creator.run(process);
                        final UIVerticalLayout setWidthResult = verticalLayout.setWidth(Distance.inches(1));
                        test.assertSame(verticalLayout, setWidthResult);
                    }
                });
            });

            runner.testGroup("setHeight(Distance)", () ->
            {
                runner.test("should return " + Types.getTypeName(UIVerticalLayout.class), (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final UIVerticalLayout verticalLayout = creator.run(process);
                        final UIVerticalLayout setHeightResult = verticalLayout.setHeight(Distance.inches(1));
                        test.assertSame(verticalLayout, setHeightResult);
                    }
                });
            });

            runner.testGroup("setSize(Size2D)", () ->
            {
                runner.test("should return " + Types.getTypeName(UIVerticalLayout.class), (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final UIVerticalLayout verticalLayout = creator.run(process);
                        final UIVerticalLayout setHeightResult = verticalLayout.setSize(Size2D.create(Distance.inches(2), Distance.inches(3)));
                        test.assertSame(verticalLayout, setHeightResult);
                    }
                });
            });

            runner.testGroup("setSize(Distance,Distance)", () ->
            {
                runner.test("should return " + Types.getTypeName(UIVerticalLayout.class), (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final UIVerticalLayout verticalLayout = creator.run(process);
                        final UIVerticalLayout setHeightResult = verticalLayout.setSize(Distance.inches(2), Distance.inches(3));
                        test.assertSame(verticalLayout, setHeightResult);
                    }
                });
            });

            runner.testGroup("add(UIElement)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final UIVerticalLayout verticalLayout = creator.run(process);
                        test.assertThrows(() -> verticalLayout.add((UIElement)null),
                            new PreConditionFailure("uiElement cannot be null."));
                    }
                });

                runner.test("with non-null", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final UIVerticalLayout verticalLayout = creator.run(process);
                        final UIElement element = creator.run(process);

                        final UIVerticalLayout addResult = verticalLayout.add(element);
                        test.assertSame(verticalLayout, addResult);
                    }
                });
            });

            runner.testGroup("addAll(UIElement...)", () ->
            {
                runner.test("with null element", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final UIVerticalLayout verticalLayout = creator.run(process);

                        test.assertThrows(() -> verticalLayout.addAll((UIElement)null),
                            new PreConditionFailure("uiElement cannot be null."));
                    }
                });

                runner.test("with no arguments", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final UIVerticalLayout verticalLayout = creator.run(process);

                        final UIVerticalLayout addResult = verticalLayout.addAll();
                        test.assertSame(verticalLayout, addResult);
                    }
                });

                runner.test("with one argument", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final UIVerticalLayout verticalLayout = creator.run(process);
                        final UIElement element = creator.run(process);

                        final UIVerticalLayout addResult = verticalLayout.addAll(element);
                        test.assertSame(verticalLayout, addResult);
                    }
                });

                runner.test("with multiple arguments", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final UIVerticalLayout verticalLayout = creator.run(process);
                        final UIElement element1 = creator.run(process);
                        final UIElement element2 = creator.run(process);

                        final UIVerticalLayout addResult = verticalLayout.addAll(element1, element2);
                        test.assertSame(verticalLayout, addResult);
                    }
                });

                runner.test("with null array", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final UIVerticalLayout verticalLayout = creator.run(process);

                        test.assertThrows(() -> verticalLayout.addAll((UIElement[])null),
                            new PreConditionFailure("uiElements cannot be null."));
                    }
                });

                runner.test("with empty array", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final UIVerticalLayout verticalLayout = creator.run(process);

                        final UIVerticalLayout addResult = verticalLayout.addAll(new UIElement[0]);
                        test.assertSame(verticalLayout, addResult);
                    }
                });

                runner.test("with one element array", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final UIVerticalLayout verticalLayout = creator.run(process);
                        final UIElement element = creator.run(process);

                        final UIVerticalLayout addResult = verticalLayout.addAll(new UIElement[]{ element });
                        test.assertSame(verticalLayout, addResult);
                    }
                });

                runner.test("with multiple element array", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final UIVerticalLayout verticalLayout = creator.run(process);
                        final UIElement element1 = creator.run(process);
                        final UIElement element2 = creator.run(process);

                        final UIVerticalLayout addResult = verticalLayout.addAll(new UIElement[]{ element1, element2 });
                        test.assertSame(verticalLayout, addResult);
                    }
                });
            });

            runner.testGroup("addAll(Iterable<UIElement>)", () ->
            {
                runner.test("with null Iterable", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final UIVerticalLayout verticalLayout = creator.run(process);

                        test.assertThrows(() -> verticalLayout.addAll((Iterable<UIElement>)null),
                            new PreConditionFailure("uiElements cannot be null."));
                    }
                });

                runner.test("with empty Iterable", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final UIVerticalLayout verticalLayout = creator.run(process);

                        final UIVerticalLayout addResult = verticalLayout.addAll(Iterable.create());
                        test.assertSame(verticalLayout, addResult);
                    }
                });

                runner.test("with one element Iterable", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final UIVerticalLayout verticalLayout = creator.run(process);
                        final UIElement element = creator.run(process);

                        final UIVerticalLayout addResult = verticalLayout.addAll(Iterable.create(element));
                        test.assertSame(verticalLayout, addResult);
                    }
                });

                runner.test("with multiple element Iterable", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final UIVerticalLayout verticalLayout = creator.run(process);
                        final UIElement element1 = creator.run(process);
                        final UIElement element2 = creator.run(process);

                        final UIVerticalLayout addResult = verticalLayout.addAll(Iterable.create(element1, element2));
                        test.assertSame(verticalLayout, addResult);
                    }
                });
            });

            runner.testGroup("setElementHorizontalAlignment(HorizontalAlignment)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final UIVerticalLayout verticalLayout = creator.run(process);
                        test.assertThrows(() -> verticalLayout.setElementHorizontalAlignment(null),
                            new PreConditionFailure("elementHorizontalAlignment cannot be null."));
                        test.assertEqual(HorizontalAlignment.Left, verticalLayout.getElementHorizontalAlignment());
                    }
                });

                final Action1<HorizontalAlignment> setElementHorizontalAlignmentTest = (HorizontalAlignment elementHorizontalAlignment) ->
                {
                    runner.test("with " + elementHorizontalAlignment, (Test test) ->
                    {
                        try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                        {
                            final UIVerticalLayout verticalLayout = creator.run(process);
                            final UIVerticalLayout setElementHorizontalAlignmentResult = verticalLayout.setElementHorizontalAlignment(elementHorizontalAlignment);
                            test.assertSame(verticalLayout, setElementHorizontalAlignmentResult);
                            test.assertEqual(elementHorizontalAlignment, verticalLayout.getElementHorizontalAlignment());
                        }
                    });
                };

                setElementHorizontalAlignmentTest.run(HorizontalAlignment.Left);
                setElementHorizontalAlignmentTest.run(HorizontalAlignment.Center);
                setElementHorizontalAlignmentTest.run(HorizontalAlignment.Right);
            });

            runner.testGroup("setElementVerticalAlignment(VerticalAlignment)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final UIVerticalLayout verticalLayout = creator.run(process);
                        test.assertThrows(() -> verticalLayout.setElementVerticalAlignment(null),
                            new PreConditionFailure("elementVerticalAlignment cannot be null."));
                        test.assertEqual(VerticalAlignment.Top, verticalLayout.getElementVerticalAlignment());
                    }
                });

                final Action1<VerticalAlignment> setElementVerticalAlignmentTest = (VerticalAlignment elementVerticalAlignment) ->
                {
                    runner.test("with " + elementVerticalAlignment, (Test test) ->
                    {
                        try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                        {
                            final UIVerticalLayout verticalLayout = creator.run(process);
                            final UIVerticalLayout setElementVerticalAlignmentResult = verticalLayout.setElementVerticalAlignment(elementVerticalAlignment);
                            test.assertSame(verticalLayout, setElementVerticalAlignmentResult);
                            test.assertEqual(elementVerticalAlignment, verticalLayout.getElementVerticalAlignment());
                        }
                    });
                };

                setElementVerticalAlignmentTest.run(VerticalAlignment.Top);
                setElementVerticalAlignmentTest.run(VerticalAlignment.Center);
                setElementVerticalAlignmentTest.run(VerticalAlignment.Bottom);
            });
        });
    }
}
