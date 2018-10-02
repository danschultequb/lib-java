package qub;

public class UITextTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(UIText.class, () ->
        {
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
                    test.assertThrows(() -> text.getParentWindow());
                });

                runner.test("with parentElement", (Test test) ->
                {
                    final FakeWindow window = new FakeWindow();
                    final UIText text = new UIText("apples");
                    text.setParent(window);
                    test.assertSame(window, text.getParentWindow());
                });
            });
        });
    }
}
