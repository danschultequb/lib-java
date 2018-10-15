package qub;

public class UITextTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(UIText.class, () ->
        {
            runner.test("constructor()", (Test test) ->
            {
                final UIText text = new UIText();
                test.assertNull(text.getText());
                test.assertNull(text.getPadding());
                test.assertNull(text.getParentWindow());
                test.assertNull(text.getFontSize());
                test.assertEqual(Distance.zero, text.getWidth());
                test.assertEqual(Distance.zero, text.getHeight());
            });

            runner.testGroup("constructor(String)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final UIText text = new UIText(null);
                    test.assertNull(text.getText());
                    test.assertNull(text.getPadding());
                    test.assertNull(text.getParentWindow());
                    test.assertNull(text.getFontSize());
                    test.assertEqual(Distance.zero, text.getWidth());
                    test.assertEqual(Distance.zero, text.getHeight());
                });

                runner.test("with empty", (Test test) ->
                {
                    final UIText text = new UIText("");
                    test.assertEqual("", text.getText());
                    test.assertNull(text.getPadding());
                    test.assertNull(text.getParentWindow());
                    test.assertNull(text.getFontSize());
                    test.assertEqual(Distance.zero, text.getWidth());
                    test.assertEqual(Distance.zero, text.getHeight());
                });

                runner.test("with non-empty", (Test test) ->
                {
                    final UIText text = new UIText("test");
                    test.assertEqual("test", text.getText());
                    test.assertNull(text.getPadding());
                    test.assertNull(text.getParentWindow());
                    test.assertNull(text.getFontSize());
                    test.assertEqual(Distance.zero, text.getWidth());
                    test.assertEqual(Distance.zero, text.getHeight());
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

            runner.testGroup("setPadding(Distance)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final UIText text = new UIText("test");
                    text.setPadding(null);
                    test.assertNull(text.getPadding());
                });

                runner.test("with negative", (Test test) ->
                {
                    final UIText text = new UIText("test");
                    test.assertThrows(() -> text.setPadding(Distance.inches(-5)));
                    test.assertNull(text.getPadding());
                });

                runner.test("with zero", (Test test) ->
                {
                    final UIText text = new UIText("test");
                    text.setPadding(Distance.zero);
                    test.assertEqual(Distance.zero, text.getPadding());
                });

                runner.test("with positive", (Test test) ->
                {
                    final UIText text = new UIText("test");
                    text.setPadding(Distance.inches(1));
                    test.assertEqual(Distance.inches(1), text.getPadding());
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
                            new DrawTextAction("ABC", Point2D.zero, Distance.fontPoints(14), Color.black)
                        }),
                        painter.getActions());
                });

                runner.test("with zero padding and no font size", (Test test) ->
                {
                    final UIText text = new UIText("ABC");
                    text.setPadding(Distance.zero);

                    final FakePainter painter = new FakePainter();
                    text.paint(painter);

                    test.assertEqual(
                        Array.fromValues(new PainterAction[]
                        {
                            new DrawTextAction("ABC", Point2D.zero, Distance.fontPoints(14), Color.black)
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
                            new DrawTextAction("Tulips", new Point2D(Distance.inches(3), Distance.inches(3)), Distance.millimeters(5), Color.black)
                        }),
                        painter.getActions());
                });

                runner.test("with background color", (Test test) ->
                {
                    final UIText text = new UIText("Answers");
                    text.setSize(Distance.inches(2), Distance.inches(0.5));
                    text.setBackground(Color.red);

                    final FakePainter painter = new FakePainter();
                    text.paint(painter);

                    test.assertEqual(
                        Array.fromValues(new PainterAction[]
                        {
                            new FillRectangleAction(Point2D.zero, Distance.inches(2), Distance.inches(0.5), Color.red),
                            new DrawTextAction("Answers", Point2D.zero, Distance.fontPoints(14), Color.black)
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
                            new DrawTextAction("apples", Point2D.zero, Distance.fontPoints(14), Color.black)
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
                        window.setContent(text);
                        test.assertSame(text, window.getContent());
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
                    test.assertThrows(() -> text.setWidth((Distance)null));
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

                runner.test("with positive", (Test test) ->
                {
                    final UIText text = new UIText("apples");
                    text.setWidth(Distance.inches(3));
                    test.assertEqual(Distance.inches(3), text.getWidth());
                });
            });

            runner.testGroup("setHeight(Distance)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final UIText text = new UIText("apples");
                    test.assertThrows(() -> text.setHeight((Distance)null));
                });

                runner.test("with negative", (Test test) ->
                {
                    final UIText text = new UIText("apples");
                    test.assertThrows(() -> text.setHeight(Distance.millimeters(-0.1)));
                });

                runner.test("with zero", (Test test) ->
                {
                    final UIText text = new UIText("apples");
                    text.setHeight(Distance.zero);
                    test.assertEqual(Distance.zero, text.getHeight());
                });

                runner.test("with positive", (Test test) ->
                {
                    final UIText text = new UIText("apples");
                    text.setHeight(Distance.inches(3));
                    test.assertEqual(Distance.inches(3), text.getHeight());
                });
            });

            runner.testGroup("getContentWidth()", () ->
            {
                runner.test("with no parentWindow", (Test test) ->
                {
                    final UIText text = new UIText("test");
                    test.assertNull(text.getContentWidth());
                });

                runner.test("with parentWindow", (Test test) ->
                {
                    final UIText text = new UIText("test");
                    final FakeWindow window = new FakeWindow();
                    window.setContent(text);

                    test.assertEqual(Distance.inches(0.2), text.getContentWidth());
                });

                runner.test("with changed parentWindow and no UIText font", (Test test) ->
                {
                    final UIText text = new UIText("test");

                    final FakeWindow window1 = new FakeWindow();
                    window1.setContent(text);
                    test.assertEqual(Distance.inches(0.2), text.getContentWidth());

                    final FakeWindow window2 = new FakeWindow();
                    window2.setDisplay(new Display(1000, 1000, 200, 200));
                    window2.setContent(text);
                    test.assertEqual(Distance.inches(0.1), text.getContentWidth());
                });

                runner.test("with changed parentWindow and UIText font", (Test test) ->
                {
                    final UIText text = new UIText("test");
                    text.setFontSize(Distance.fontPoints(14));

                    final FakeWindow window1 = new FakeWindow();
                    window1.setContent(text);
                    test.assertEqual(Distance.inches(0.23), text.getContentWidth());

                    final FakeWindow window2 = new FakeWindow();
                    window2.setDisplay(new Display(1000, 1000, 200, 200));
                    window2.setContent(text);
                    test.assertEqual(Distance.inches(0.115), text.getContentWidth());
                });
            });

            runner.testGroup("getContentHeight()", () ->
            {
                runner.test("with no parentWindow", (Test test) ->
                {
                    final UIText text = new UIText("test");
                    test.assertNull(text.getContentHeight());
                });

                runner.test("with parentWindow", (Test test) ->
                {
                    final UIText text = new UIText("test");
                    final FakeWindow window = new FakeWindow();
                    window.setContent(text);

                    test.assertEqual(Distance.inches(0.1509375), text.getContentHeight());
                });

                runner.test("with changed parentWindow and no UIText font", (Test test) ->
                {
                    final UIText text = new UIText("test");

                    final FakeWindow window1 = new FakeWindow();
                    window1.setContent(text);
                    test.assertEqual(Distance.inches(0.1509375), text.getContentHeight());

                    final FakeWindow window2 = new FakeWindow();
                    window2.setDisplay(new Display(1000, 1000, 200, 200));
                    window2.setContent(text);
                    test.assertEqual(Distance.inches(0.07546875), text.getContentHeight());
                });

                runner.test("with changed parentWindow and UIText font", (Test test) ->
                {
                    final UIText text = new UIText("test");
                    text.setFontSize(Distance.fontPoints(14));

                    final FakeWindow window1 = new FakeWindow();
                    window1.setContent(text);
                    test.assertEqual(Distance.inches(0.17609375), text.getContentHeight());

                    final FakeWindow window2 = new FakeWindow();
                    window2.setDisplay(new Display(1000, 1000, 200, 200));
                    window2.setContent(text);
                    test.assertEqual(Distance.inches(0.088046875), text.getContentHeight());
                });
            });
        });
    }
}
