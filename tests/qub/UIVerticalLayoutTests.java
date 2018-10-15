package qub;

public class UIVerticalLayoutTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(UIVerticalLayout.class, () ->
        {
            runner.testGroup("paint(UIPainter)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final UIVerticalLayout layout = new UIVerticalLayout();
                    test.assertThrows(() -> layout.paint(null));
                });

                runner.test("with two child elements with fixed heights", (Test test) ->
                {
                    final UIVerticalLayout layout = new UIVerticalLayout();

                    final UIText text1 = new UIText("One");
                    text1.setHeight(Distance.inches(1));
                    layout.add(text1);

                    final UIText text2 = new UIText("Two");
                    layout.add(text2);

                    final FakePainter painter = new FakePainter();

                    layout.paint(painter);

                    test.assertEqual(
                        Array.fromValues(new PainterAction[]
                        {
                            new DrawTextAction("One", Point2D.zero, Distance.fontPoints(14), Color.black),
                            new DrawTextAction("Two", new Point2D(Distance.zero, Distance.inches(1)), Distance.fontPoints(14), Color.black)
                        }),
                        painter.getActions());
                });
            });

            runner.testGroup("getContentSize()", () ->
            {
                runner.test("with no child elements", (Test test) ->
                {
                    final UIVerticalLayout layout = new UIVerticalLayout();
                    test.assertEqual(Size2D.zero, layout.getContentSize());
                });
            });

            runner.testGroup("getContentWidth()", () ->
            {
                runner.test("with no child elements", (Test test) ->
                {
                    final UIVerticalLayout layout = new UIVerticalLayout();
                    test.assertEqual(Distance.zero, layout.getContentWidth());
                });

                runner.test("with one child element with fixed width", (Test test) ->
                {
                    final UIVerticalLayout layout = new UIVerticalLayout()
                        .add(new UIText("Test")
                            .setWidth(Distance.inches(2)));
                    test.assertEqual(Distance.inches(2), layout.getContentWidth());
                });

                runner.test("with one child element with fit-to-content width without window", (Test test) ->
                {
                    final UIVerticalLayout layout = new UIVerticalLayout()
                                                        .add(new UIText("Test")
                                                                 .setWidth(UIWidth.fitContent));
                    test.assertEqual(Distance.zero, layout.getContentWidth());
                });

                runner.test("with one child element with fit-to-content width with window", (Test test) ->
                {
                    try (final FakeWindow window = new FakeWindow())
                    {
                        final UIVerticalLayout layout = new UIVerticalLayout()
                                                            .add(new UIText("Test")
                                                                     .setWidth(UIWidth.fitContent));
                        window.setContent(layout);
                        test.assertEqual(Distance.inches(0.24), layout.getContentWidth());
                    }
                });

                runner.test("with two child elements with fit-to-content width with window", (Test test) ->
                {
                    try (final FakeWindow window = new FakeWindow())
                    {
                        final UIVerticalLayout layout = new UIVerticalLayout()
                                                            .add(new UIText("Test")
                                                                     .setWidth(UIWidth.fitContent))
                                                            .add(new UIText("and test and test")
                                                                    .setWidth(UIWidth.fitContent));
                        window.setContent(layout);
                        test.assertEqual(Distance.inches(0.91), layout.getContentWidth());
                    }
                });

                runner.test("with three child elements with fit-to-content width with window", (Test test) ->
                {
                    try (final FakeWindow window = new FakeWindow())
                    {
                        final UIVerticalLayout layout = new UIVerticalLayout()
                                                            .add(new UIText("Test")
                                                                     .setWidth(UIWidth.fitContent))
                                                            .add(new UIText("and test and test")
                                                                     .setWidth(UIWidth.fitContent))
                                                            .add(new UIText("success")
                                                                    .setWidth(UIWidth.fitContent));
                        window.setContent(layout);
                        test.assertEqual(Distance.inches(0.91), layout.getContentWidth());
                    }
                });
            });

            runner.testGroup("getContentHeight()", () ->
            {
                runner.test("with no child elements", (Test test) ->
                {
                    final UIVerticalLayout layout = new UIVerticalLayout();
                    test.assertEqual(Distance.zero, layout.getContentHeight());
                });

                runner.test("with one child element with fixed height", (Test test) ->
                {
                    final UIVerticalLayout layout = new UIVerticalLayout()
                                                        .add(new UIText("Test")
                                                                 .setHeight(Distance.inches(2)));
                    test.assertEqual(Distance.inches(2), layout.getContentHeight());
                });
            });
        });
    }
}
