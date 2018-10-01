package qub;

public class FakePainterTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(FakePainter.class, () ->
        {
            runner.test("constructor()", (Test test) ->
            {
                final FakePainter painter = new FakePainter();
                test.assertEqual(new Array<PainterAction>(0), painter.getActions());
            });

            runner.testGroup("clearActions()", () ->
            {
                runner.test("with no actions", (Test test) ->
                {
                    final FakePainter painter = new FakePainter();
                    painter.clearActions();
                    test.assertEqual(new Array<PainterAction>(0), painter.getActions());
                });

                runner.test("with one action", (Test test) ->
                {
                    final FakePainter painter = new FakePainter();
                    painter.drawText("Hello");

                    painter.clearActions();

                    test.assertEqual(new Array<PainterAction>(0), painter.getActions());
                });
            });

            runner.testGroup("drawText(String)", () ->
            {
                runner.test("with null text", (Test test) ->
                {
                   final FakePainter painter = new FakePainter();
                   test.assertThrows(() -> painter.drawText(null));
                   test.assertEqual(new Array<PainterAction>(0), painter.getActions());
                });

                runner.test("with empty text", (Test test) ->
                {
                    final FakePainter painter = new FakePainter();
                    test.assertThrows(() -> painter.drawText(""));
                    test.assertEqual(new Array<PainterAction>(0), painter.getActions());
                });

                runner.test("with non-empty text", (Test test) ->
                {
                    final FakePainter painter = new FakePainter();
                    painter.drawText("kittens");
                    test.assertEqual(
                        Array.fromValues(new PainterAction[]
                        {
                            new DrawTextAction("kittens", Point2D.zero, Distance.fontPoints(14))
                        }),
                        painter.getActions());
                });
            });

            runner.testGroup("drawText(String,Distance,Distance)", () ->
            {
                runner.test("with null text", (Test test) ->
                {
                    final FakePainter painter = new FakePainter();
                    test.assertThrows(() -> painter.drawText(null, Distance.zero, Distance.zero));
                    test.assertEqual(new Array<PainterAction>(0), painter.getActions());
                });

                runner.test("with empty text", (Test test) ->
                {
                    final FakePainter painter = new FakePainter();
                    test.assertThrows(() -> painter.drawText("", Distance.zero, Distance.zero));
                    test.assertEqual(new Array<PainterAction>(0), painter.getActions());
                });

                runner.test("with null topLeftX", (Test test) ->
                {
                    final FakePainter painter = new FakePainter();
                    test.assertThrows(() -> painter.drawText("test", null, Distance.zero));
                    test.assertEqual(new Array<PainterAction>(0), painter.getActions());
                });

                runner.test("with null topLeftY", (Test test) ->
                {
                    final FakePainter painter = new FakePainter();
                    test.assertThrows(() -> painter.drawText("test", Distance.zero, null));
                    test.assertEqual(new Array<PainterAction>(0), painter.getActions());
                });

                runner.test("with non-empty text", (Test test) ->
                {
                    final FakePainter painter = new FakePainter();
                    painter.drawText("kittens", Distance.inches(3), Distance.inches(4));
                    test.assertEqual(
                        Array.fromValues(new PainterAction[]
                        {
                            new DrawTextAction("kittens", new Point2D(Distance.inches(3), Distance.inches(4)), Distance.fontPoints(14))
                        }),
                        painter.getActions());
                });
            });

            runner.testGroup("drawText(String,Point2D)", () ->
            {
                runner.test("with null text", (Test test) ->
                {
                    final FakePainter painter = new FakePainter();
                    test.assertThrows(() -> painter.drawText(null, Point2D.zero));
                    test.assertEqual(new Array<PainterAction>(0), painter.getActions());
                });

                runner.test("with empty text", (Test test) ->
                {
                    final FakePainter painter = new FakePainter();
                    test.assertThrows(() -> painter.drawText("", Point2D.zero));
                    test.assertEqual(new Array<PainterAction>(0), painter.getActions());
                });

                runner.test("with null topLeft", (Test test) ->
                {
                    final FakePainter painter = new FakePainter();
                    test.assertThrows(() -> painter.drawText("test", null));
                    test.assertEqual(new Array<PainterAction>(0), painter.getActions());
                });

                runner.test("with non-empty text", (Test test) ->
                {
                    final FakePainter painter = new FakePainter();
                    painter.drawText("kittens", new Point2D(Distance.inches(0.1), Distance.inches(0.2)));
                    test.assertEqual(
                        Array.fromValues(new PainterAction[]
                        {
                            new DrawTextAction("kittens", new Point2D(Distance.inches(0.1), Distance.inches(0.2)), Distance.fontPoints(14))
                        }),
                        painter.getActions());
                });
            });

            runner.testGroup("drawLine(Point2D,Point2D)", () ->
            {
                runner.test("with null start", (Test test) ->
                {
                    final FakePainter painter = new FakePainter();
                    test.assertThrows(() -> painter.drawLine(null, Point2D.zero));
                    test.assertEqual(new Array<PainterAction>(0), painter.getActions());
                });

                runner.test("with null end", (Test test) ->
                {
                    final FakePainter painter = new FakePainter();
                    test.assertThrows(() -> painter.drawLine(Point2D.zero, null));
                    test.assertEqual(new Array<PainterAction>(0), painter.getActions());
                });

                runner.test("with valid arguments", (Test test) ->
                {
                    final FakePainter painter = new FakePainter();
                    painter.drawLine(
                        new Point2D(Distance.inches(10), Distance.inches(10.1)),
                        new Point2D(Distance.inches(7), Distance.inches(2)));
                    test.assertEqual(
                        Array.fromValues(new PainterAction[]
                        {
                            new DrawLineAction(Distance.inches(10), Distance.inches(10.1), Distance.inches(7), Distance.inches(2))
                        }),
                        painter.getActions());
                });
            });

            runner.testGroup("drawLine(Distance,Distance,Distance,Distance)", () ->
            {
                runner.test("with null startX", (Test test) ->
                {
                    final FakePainter painter = new FakePainter();
                    test.assertThrows(() -> painter.drawLine(null, Distance.zero, Distance.zero, Distance.zero));
                    test.assertEqual(new Array<PainterAction>(0), painter.getActions());
                });

                runner.test("with null startY", (Test test) ->
                {
                    final FakePainter painter = new FakePainter();
                    test.assertThrows(() -> painter.drawLine(Distance.zero, null, Distance.zero, Distance.zero));
                    test.assertEqual(new Array<PainterAction>(0), painter.getActions());
                });

                runner.test("with null endX", (Test test) ->
                {
                    final FakePainter painter = new FakePainter();
                    test.assertThrows(() -> painter.drawLine(Distance.zero, Distance.zero, null, Distance.zero));
                    test.assertEqual(new Array<PainterAction>(0), painter.getActions());
                });

                runner.test("with null endY", (Test test) ->
                {
                    final FakePainter painter = new FakePainter();
                    test.assertThrows(() -> painter.drawLine(Distance.zero, Distance.zero, Distance.zero, null));
                    test.assertEqual(new Array<PainterAction>(0), painter.getActions());
                });

                runner.test("with valid arguments", (Test test) ->
                {
                    final FakePainter painter = new FakePainter();
                    painter.drawLine(Distance.inches(1), Distance.inches(2), Distance.inches(3), Distance.inches(4));
                    test.assertEqual(
                        Array.fromValues(new PainterAction[]
                        {
                            new DrawLineAction(Distance.inches(1), Distance.inches(2), Distance.inches(3), Distance.inches(4))
                        }),
                        painter.getActions());
                });
            });

            runner.testGroup("drawRectangle(Distance,Distance,Distance,Distance)", () ->
            {
                runner.test("with null topLeftX", (Test test) ->
                {
                    final FakePainter painter = new FakePainter();
                    test.assertThrows(() -> painter.drawRectangle(null, Distance.zero, Distance.zero, Distance.zero));
                    test.assertEqual(new Array<PainterAction>(0), painter.getActions());
                });

                runner.test("with null topLeftY", (Test test) ->
                {
                    final FakePainter painter = new FakePainter();
                    test.assertThrows(() -> painter.drawRectangle(Distance.zero, null, Distance.zero, Distance.zero));
                    test.assertEqual(new Array<PainterAction>(0), painter.getActions());
                });

                runner.test("with null width", (Test test) ->
                {
                    final FakePainter painter = new FakePainter();
                    test.assertThrows(() -> painter.drawRectangle(Distance.zero, Distance.zero, null, Distance.zero));
                    test.assertEqual(new Array<PainterAction>(0), painter.getActions());
                });

                runner.test("with negative width", (Test test) ->
                {
                    final FakePainter painter = new FakePainter();
                    test.assertThrows(() -> painter.drawRectangle(Distance.zero, Distance.zero, Distance.inches(-1), Distance.zero));
                    test.assertEqual(new Array<PainterAction>(0), painter.getActions());
                });

                runner.test("with null height", (Test test) ->
                {
                    final FakePainter painter = new FakePainter();
                    test.assertThrows(() -> painter.drawRectangle(Distance.zero, Distance.zero, Distance.zero, null));
                    test.assertEqual(new Array<PainterAction>(0), painter.getActions());
                });

                runner.test("with negative height", (Test test) ->
                {
                    final FakePainter painter = new FakePainter();
                    test.assertThrows(() -> painter.drawRectangle(Distance.zero, Distance.zero, Distance.zero, Distance.inches(-1)));
                    test.assertEqual(new Array<PainterAction>(0), painter.getActions());
                });

                runner.test("with valid arguments", (Test test) ->
                {
                    final FakePainter painter = new FakePainter();
                    painter.drawRectangle(Distance.zero, Distance.zero, Distance.inches(1), Distance.inches(2));
                    test.assertEqual(
                        Array.fromValues(new PainterAction[]
                        {
                            new DrawRectangleAction(Point2D.zero, Distance.inches(1), Distance.inches(2))
                        }),
                        painter.getActions());
                });
            });
        });
    }
}
