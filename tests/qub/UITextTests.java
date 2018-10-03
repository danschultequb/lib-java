package qub;

public class UITextTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(UIText.class, () ->
        {
            runner.testGroup("constructor(String)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> new UIText(null));
                });

                runner.test("with empty", (Test test) ->
                {
                    final UIText text = new UIText("");
                    test.assertEqual("", text.getText());
                    test.assertEqual(Distance.zero, text.getPadding());
                    test.assertNull(text.getParentWindow());
                    test.assertEqual(Distance.zero, text.getWidth());
                });

                runner.test("with non-empty", (Test test) ->
                {
                    final UIText text = new UIText("test");
                    test.assertEqual("test", text.getText());
                    test.assertEqual(Distance.zero, text.getPadding());
                    test.assertNull(text.getParentWindow());
                    test.assertEqual(Distance.zero, text.getWidth());
                });
            });

            runner.testGroup("setParent(UIElementParent)", () ->
            {
                runner.test("from null to null", (Test test) ->
                {
                    final UIText text = new UIText("a");
                    test.assertNull(text.getParent());

                    text.setParent(null);

                    test.assertNull(text.getParent());
                });

                runner.test("from null to non-null", (Test test) ->
                {
                    try (final FakeWindow window = new FakeWindow())
                    {
                        final UIText text = new UIText("a");

                        text.setParent(window);

                        test.assertSame(window, text.getParent());
                        test.assertSame(window, text.getParentWindow());
                    }
                });
            });

            runner.testGroup("paint(UIPainter)", () ->
            {
                runner.test("with null painter", (Test test) ->
                {
                    final UIText text = new UIText("ABC");
                    test.assertThrows(() -> text.paint(null));
                });

                runner.test("with no padding or font size", (Test test) ->
                {
                    final UIText text = new UIText("ABC");

                    final FakePainter painter = new FakePainter();
                    text.paint(painter);

                    test.assertEqual(
                        Array.fromValues(new PainterAction[]
                        {
                            new DrawTextAction("ABC", Point2D.zero, Distance.fontPoints(14))
                        }),
                        painter.getActions());
                });

                runner.test("with padding and font size", (Test test) ->
                {
                    final UIText text = new UIText("Tulips");
                    text.setPadding(Distance.inches(3));
                    text.setFontSize(Distance.millimeters(5));

                    final FakePainter painter = new FakePainter();
                    text.paint(painter);

                    test.assertEqual(
                        Array.fromValues(new PainterAction[]
                        {
                            new DrawTextAction("Tulips", new Point2D(Distance.inches(3), Distance.inches(3)), Distance.millimeters(5))
                        }),
                        painter.getActions());
                });
            });

            runner.testGroup("repaint()", () ->
            {
                runner.test("with no parentElement", (Test test) ->
                {
                    final UIText text = new UIText("apples");
                    test.assertThrows(() -> text.repaint());
                });

                runner.test("with parentElement", (Test test) ->
                {
                    final FakePainter painter = new FakePainter();
                    final FakeWindow window = new FakeWindow();
                    window.setPainter(painter);

                    final UIText text = new UIText("apples");
                    window.setContent(text);
                    test.assertSame(window, text.getParentWindow());

                    window.open();
                    painter.clearActions();

                    text.repaint();

                    test.assertEqual(
                        Array.fromValues(new PainterAction[]
                        {
                            new DrawTextAction("apples", Point2D.zero, Distance.fontPoints(14))
                        }),
                        painter.getActions());
                });
            });

            runner.testGroup("getParentWindow()", () ->
            {
                runner.test("with no parentElement", (Test test) ->
                {
                    final UIText text = new UIText("apples");
                    test.assertNull(text.getParentWindow());
                });

                runner.test("with parentElement", (Test test) ->
                {
                    try (final FakeWindow window = new FakeWindow())
                    {
                        final UIText text = new UIText("apples");
                        text.setParent(window);
                        test.assertSame(window, text.getParentWindow());
                    }
                });

                runner.test("after the parent window has been disposed", (Test test) ->
                {
                    final UIText text = new UIText("apples");
                    try (final FakeWindow window = new FakeWindow())
                    {
                        text.setParent(window);
                        test.assertSame(window, text.getParent());
                        test.assertSame(window, text.getParentWindow());
                    }
                    test.assertNull(text.getParent());
                    test.assertNull(text.getParentWindow());
                });
            });

            runner.testGroup("setWidth(Distance)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final UIText text = new UIText("apples");
                    test.assertThrows(() -> text.setWidth(null));
                });

                runner.test("with negative", (Test test) ->
                {
                    final UIText text = new UIText("apples");
                    test.assertThrows(() -> text.setWidth(Distance.millimeters(-0.1)));
                });

                runner.test("with zero", (Test test) ->
                {
                    final UIText text = new UIText("apples");
                    text.setWidth(Distance.zero);
                    test.assertEqual(Distance.zero, text.getWidth());
                });
            });
        });
    }
}
