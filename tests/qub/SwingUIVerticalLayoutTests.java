package qub;

public interface SwingUIVerticalLayoutTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(SwingUIVerticalLayout.class, () ->
        {
            final Function1<Test,SwingUIVerticalLayout> creator = (Test test) ->
            {
                final Display display = new Display(1000, 1000, 100, 100);
                return SwingUIVerticalLayout.create(display);
            };

            UIVerticalLayoutTests.test(runner, creator);

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
                    final SwingUIVerticalLayout setHeightResult = verticalLayout.setSize(new Size2D(Distance.inches(2), Distance.inches(3)));
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
