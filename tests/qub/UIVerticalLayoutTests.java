package qub;

public interface UIVerticalLayoutTests
{
    static void test(TestRunner runner, Function1<Test,? extends UIVerticalLayout> creator)
    {
        PreCondition.assertNotNull(runner, "runner");
        PreCondition.assertNotNull(creator, "creator");

        runner.testGroup(UIVerticalLayout.class, () ->
        {
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
                runner.test("should return " + Types.getTypeName(UIVerticalLayout.class), (Test test) ->
                {
                    final UIVerticalLayout verticalLayout = creator.run(test);
                    final UIVerticalLayout setHeightResult = verticalLayout.setHeight(Distance.inches(1));
                    test.assertSame(verticalLayout, setHeightResult);
                });
            });

            runner.testGroup("setSize(Size2D)", () ->
            {
                runner.test("should return " + Types.getTypeName(UIVerticalLayout.class), (Test test) ->
                {
                    final UIVerticalLayout verticalLayout = creator.run(test);
                    final UIVerticalLayout setHeightResult = verticalLayout.setSize(Size2D.create(Distance.inches(2), Distance.inches(3)));
                    test.assertSame(verticalLayout, setHeightResult);
                });
            });

            runner.testGroup("setSize(Distance,Distance)", () ->
            {
                runner.test("should return " + Types.getTypeName(UIVerticalLayout.class), (Test test) ->
                {
                    final UIVerticalLayout verticalLayout = creator.run(test);
                    final UIVerticalLayout setHeightResult = verticalLayout.setSize(Distance.inches(2), Distance.inches(3));
                    test.assertSame(verticalLayout, setHeightResult);
                });
            });

            runner.testGroup("add(UIElement)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final UIVerticalLayout verticalLayout = creator.run(test);
                    test.assertThrows(() -> verticalLayout.add((UIElement)null),
                        new PreConditionFailure("uiElement cannot be null."));
                });

                runner.test("with non-null", (Test test) ->
                {
                    final UIVerticalLayout verticalLayout = creator.run(test);
                    final UIElement element = creator.run(test);

                    final UIVerticalLayout addResult = verticalLayout.add(element);
                    test.assertSame(verticalLayout, addResult);
                });
            });

            runner.testGroup("addAll(UIElement...)", () ->
            {
                runner.test("with null element", (Test test) ->
                {
                    final UIVerticalLayout verticalLayout = creator.run(test);

                    test.assertThrows(() -> verticalLayout.addAll((UIElement)null),
                        new PreConditionFailure("uiElement cannot be null."));
                });

                runner.test("with no arguments", (Test test) ->
                {
                    final UIVerticalLayout verticalLayout = creator.run(test);

                    final UIVerticalLayout addResult = verticalLayout.addAll();
                    test.assertSame(verticalLayout, addResult);
                });

                runner.test("with one argument", (Test test) ->
                {
                    final UIVerticalLayout verticalLayout = creator.run(test);
                    final UIElement element = creator.run(test);

                    final UIVerticalLayout addResult = verticalLayout.addAll(element);
                    test.assertSame(verticalLayout, addResult);
                });

                runner.test("with multiple arguments", (Test test) ->
                {
                    final UIVerticalLayout verticalLayout = creator.run(test);
                    final UIElement element1 = creator.run(test);
                    final UIElement element2 = creator.run(test);

                    final UIVerticalLayout addResult = verticalLayout.addAll(element1, element2);
                    test.assertSame(verticalLayout, addResult);
                });

                runner.test("with null array", (Test test) ->
                {
                    final UIVerticalLayout verticalLayout = creator.run(test);

                    test.assertThrows(() -> verticalLayout.addAll((UIElement[])null),
                        new PreConditionFailure("uiElements cannot be null."));
                });

                runner.test("with empty array", (Test test) ->
                {
                    final UIVerticalLayout verticalLayout = creator.run(test);

                    final UIVerticalLayout addResult = verticalLayout.addAll(new UIElement[0]);
                    test.assertSame(verticalLayout, addResult);
                });

                runner.test("with one element array", (Test test) ->
                {
                    final UIVerticalLayout verticalLayout = creator.run(test);
                    final UIElement element = creator.run(test);

                    final UIVerticalLayout addResult = verticalLayout.addAll(new UIElement[] { element });
                    test.assertSame(verticalLayout, addResult);
                });

                runner.test("with multiple element array", (Test test) ->
                {
                    final UIVerticalLayout verticalLayout = creator.run(test);
                    final UIElement element1 = creator.run(test);
                    final UIElement element2 = creator.run(test);

                    final UIVerticalLayout addResult = verticalLayout.addAll(new UIElement[] { element1, element2 });
                    test.assertSame(verticalLayout, addResult);
                });
            });

            runner.testGroup("addAll(Iterable<UIElement>)", () ->
            {
                runner.test("with null Iterable", (Test test) ->
                {
                    final UIVerticalLayout verticalLayout = creator.run(test);

                    test.assertThrows(() -> verticalLayout.addAll((Iterable<UIElement>)null),
                        new PreConditionFailure("uiElements cannot be null."));
                });

                runner.test("with empty Iterable", (Test test) ->
                {
                    final UIVerticalLayout verticalLayout = creator.run(test);

                    final UIVerticalLayout addResult = verticalLayout.addAll(Iterable.create());
                    test.assertSame(verticalLayout, addResult);
                });

                runner.test("with one element Iterable", (Test test) ->
                {
                    final UIVerticalLayout verticalLayout = creator.run(test);
                    final UIElement element = creator.run(test);

                    final UIVerticalLayout addResult = verticalLayout.addAll(Iterable.create(element));
                    test.assertSame(verticalLayout, addResult);
                });

                runner.test("with multiple element Iterable", (Test test) ->
                {
                    final UIVerticalLayout verticalLayout = creator.run(test);
                    final UIElement element1 = creator.run(test);
                    final UIElement element2 = creator.run(test);

                    final UIVerticalLayout addResult = verticalLayout.addAll(Iterable.create(element1, element2));
                    test.assertSame(verticalLayout, addResult);
                });
            });

            runner.testGroup("setElementHorizontalAlignment(HorizontalAlignment)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final UIVerticalLayout verticalLayout = creator.run(test);
                    test.assertThrows(() -> verticalLayout.setElementHorizontalAlignment(null),
                        new PreConditionFailure("elementHorizontalAlignment cannot be null."));
                    test.assertEqual(HorizontalAlignment.Left, verticalLayout.getElementHorizontalAlignment());
                });

                final Action1<HorizontalAlignment> setElementHorizontalAlignmentTest = (HorizontalAlignment elementHorizontalAlignment) ->
                {
                    runner.test("with " + elementHorizontalAlignment, (Test test) ->
                    {
                        final UIVerticalLayout verticalLayout = creator.run(test);
                        final UIVerticalLayout setElementHorizontalAlignmentResult = verticalLayout.setElementHorizontalAlignment(elementHorizontalAlignment);
                        test.assertSame(verticalLayout, setElementHorizontalAlignmentResult);
                        test.assertEqual(elementHorizontalAlignment, verticalLayout.getElementHorizontalAlignment());
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
                    final UIVerticalLayout verticalLayout = creator.run(test);
                    test.assertThrows(() -> verticalLayout.setElementVerticalAlignment(null),
                        new PreConditionFailure("elementVerticalAlignment cannot be null."));
                    test.assertEqual(VerticalAlignment.Top, verticalLayout.getElementVerticalAlignment());
                });

                final Action1<VerticalAlignment> setElementVerticalAlignmentTest = (VerticalAlignment elementVerticalAlignment) ->
                {
                    runner.test("with " + elementVerticalAlignment, (Test test) ->
                    {
                        final UIVerticalLayout verticalLayout = creator.run(test);
                        final UIVerticalLayout setElementVerticalAlignmentResult = verticalLayout.setElementVerticalAlignment(elementVerticalAlignment);
                        test.assertSame(verticalLayout, setElementVerticalAlignmentResult);
                        test.assertEqual(elementVerticalAlignment, verticalLayout.getElementVerticalAlignment());
                    });
                };

                setElementVerticalAlignmentTest.run(VerticalAlignment.Top);
                setElementVerticalAlignmentTest.run(VerticalAlignment.Center);
                setElementVerticalAlignmentTest.run(VerticalAlignment.Bottom);
            });
        });
    }
}
