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
                test.assertEqual(Iterable.create(), painter.getActions());
            });

            runner.testGroup("clearActions()", () ->
            {
                runner.test("with no actions", (Test test) ->
                {
                    final FakePainter painter = new FakePainter();
                    painter.clearActions();
                    test.assertEqual(Iterable.create(), painter.getActions());
                });

                runner.test("with one action", (Test test) ->
                {
                    final FakePainter painter = new FakePainter();
                    painter.drawText("Hello");

                    painter.clearActions();

                    test.assertEqual(Iterable.create(), painter.getActions());
                });
            });

            runner.testGroup("drawText(String)", () ->
            {
                runner.test("with null text", (Test test) ->
                {
                   final FakePainter painter = new FakePainter();
                   test.assertThrows(() -> painter.drawText(null),
                       new PreConditionFailure("text cannot be null."));
                   test.assertEqual(Iterable.create(), painter.getActions());
                });

                runner.test("with empty text", (Test test) ->
                {
                    final FakePainter painter = new FakePainter();
                    test.assertThrows(() -> painter.drawText(""),
                        new PreConditionFailure("text cannot be empty."));
                    test.assertEqual(Iterable.create(), painter.getActions());
                });

                runner.test("with non-empty text", (Test test) ->
                {
                    final FakePainter painter = new FakePainter();
                    painter.drawText("kittens");
                    test.assertEqual(
                        Iterable.create(
                            new DrawTextAction("kittens", Point2D.zero, Distance.fontPoints(14), Color.black)),
                        painter.getActions());
                });
            });

            runner.testGroup("drawText(String,Distance,Distance)", () ->
            {
                runner.test("with null text", (Test test) ->
                {
                    final FakePainter painter = new FakePainter();
                    test.assertThrows(() -> painter.drawText(null, Distance.zero, Distance.zero),
                        new PreConditionFailure("text cannot be null."));
                    test.assertEqual(Iterable.create(), painter.getActions());
                });

                runner.test("with empty text", (Test test) ->
                {
                    final FakePainter painter = new FakePainter();
                    test.assertThrows(() -> painter.drawText("", Distance.zero, Distance.zero),
                        new PreConditionFailure("text cannot be empty."));
                    test.assertEqual(Iterable.create(), painter.getActions());
                });

                runner.test("with null topLeftX", (Test test) ->
                {
                    final FakePainter painter = new FakePainter();
                    test.assertThrows(() -> painter.drawText("test", null, Distance.zero),
                        new PreConditionFailure("topLeftX cannot be null."));
                    test.assertEqual(Iterable.create(), painter.getActions());
                });

                runner.test("with null topLeftY", (Test test) ->
                {
                    final FakePainter painter = new FakePainter();
                    test.assertThrows(() -> painter.drawText("test", Distance.zero, null),
                        new PreConditionFailure("topLeftY cannot be null."));
                    test.assertEqual(Iterable.create(), painter.getActions());
                });

                runner.test("with non-empty text", (Test test) ->
                {
                    final FakePainter painter = new FakePainter();
                    painter.setColor(Color.green);
                    painter.drawText("kittens", Distance.inches(3), Distance.inches(4));
                    test.assertEqual(
                        Iterable.create(
                            new DrawTextAction("kittens", new Point2D(Distance.inches(3), Distance.inches(4)), Distance.fontPoints(14), Color.green)),
                        painter.getActions());
                });
            });

            runner.testGroup("drawText(String,Point2D)", () ->
            {
                runner.test("with null text", (Test test) ->
                {
                    final FakePainter painter = new FakePainter();
                    test.assertThrows(() -> painter.drawText(null, Point2D.zero),
                        new PreConditionFailure("text cannot be null."));
                    test.assertEqual(Iterable.create(), painter.getActions());
                });

                runner.test("with empty text", (Test test) ->
                {
                    final FakePainter painter = new FakePainter();
                    test.assertThrows(() -> painter.drawText("", Point2D.zero),
                        new PreConditionFailure("text cannot be empty."));
                    test.assertEqual(Iterable.create(), painter.getActions());
                });

                runner.test("with null topLeft", (Test test) ->
                {
                    final FakePainter painter = new FakePainter();
                    test.assertThrows(() -> painter.drawText("test", null),
                        new PreConditionFailure("topLeft cannot be null."));
                    test.assertEqual(Iterable.create(), painter.getActions());
                });

                runner.test("with non-empty text", (Test test) ->
                {
                    final FakePainter painter = new FakePainter();
                    painter.drawText("kittens", new Point2D(Distance.inches(0.1), Distance.inches(0.2)));
                    test.assertEqual(
                        Iterable.create(
                            new DrawTextAction("kittens", new Point2D(Distance.inches(0.1), Distance.inches(0.2)), Distance.fontPoints(14), Color.black)),
                        painter.getActions());
                });
            });

            runner.testGroup("drawLine(Point2D,Point2D)", () ->
            {
                runner.test("with null start", (Test test) ->
                {
                    final FakePainter painter = new FakePainter();
                    test.assertThrows(() -> painter.drawLine(null, Point2D.zero),
                        new PreConditionFailure("start cannot be null."));
                    test.assertEqual(Iterable.create(), painter.getActions());
                });

                runner.test("with null end", (Test test) ->
                {
                    final FakePainter painter = new FakePainter();
                    test.assertThrows(() -> painter.drawLine(Point2D.zero, null),
                        new PreConditionFailure("end cannot be null."));
                    test.assertEqual(Iterable.create(), painter.getActions());
                });

                runner.test("with valid arguments", (Test test) ->
                {
                    final FakePainter painter = new FakePainter();
                    painter.drawLine(
                        new Point2D(Distance.inches(10), Distance.inches(10.1)),
                        new Point2D(Distance.inches(7), Distance.inches(2)));
                    test.assertEqual(
                        Iterable.create(
                            new DrawLineAction(Distance.inches(10), Distance.inches(10.1), Distance.inches(7), Distance.inches(2))),
                        painter.getActions());
                });
            });

            runner.testGroup("drawLine(Distance,Distance,Distance,Distance)", () ->
            {
                runner.test("with null startX", (Test test) ->
                {
                    final FakePainter painter = new FakePainter();
                    test.assertThrows(() -> painter.drawLine(null, Distance.zero, Distance.zero, Distance.zero),
                        new PreConditionFailure("startX cannot be null."));
                    test.assertEqual(Iterable.create(), painter.getActions());
                });

                runner.test("with null startY", (Test test) ->
                {
                    final FakePainter painter = new FakePainter();
                    test.assertThrows(() -> painter.drawLine(Distance.zero, null, Distance.zero, Distance.zero),
                        new PreConditionFailure("startY cannot be null."));
                    test.assertEqual(Iterable.create(), painter.getActions());
                });

                runner.test("with null endX", (Test test) ->
                {
                    final FakePainter painter = new FakePainter();
                    test.assertThrows(() -> painter.drawLine(Distance.zero, Distance.zero, null, Distance.zero),
                        new PreConditionFailure("endX cannot be null."));
                    test.assertEqual(Iterable.create(), painter.getActions());
                });

                runner.test("with null endY", (Test test) ->
                {
                    final FakePainter painter = new FakePainter();
                    test.assertThrows(() -> painter.drawLine(Distance.zero, Distance.zero, Distance.zero, null),
                        new PreConditionFailure("endY cannot be null."));
                    test.assertEqual(Iterable.create(), painter.getActions());
                });

                runner.test("with valid arguments", (Test test) ->
                {
                    final FakePainter painter = new FakePainter();
                    painter.drawLine(Distance.inches(1), Distance.inches(2), Distance.inches(3), Distance.inches(4));
                    test.assertEqual(
                        Iterable.create(
                            new DrawLineAction(Distance.inches(1), Distance.inches(2), Distance.inches(3), Distance.inches(4))),
                        painter.getActions());
                });
            });

            runner.testGroup("drawRectangle(Distance,Distance,Distance,Distance)", () ->
            {
                runner.test("with null topLeftX", (Test test) ->
                {
                    final FakePainter painter = new FakePainter();
                    test.assertThrows(() -> painter.drawRectangle(null, Distance.zero, Distance.zero, Distance.zero),
                        new PreConditionFailure("topLeftX cannot be null."));
                    test.assertEqual(Iterable.create(), painter.getActions());
                });

                runner.test("with null topLeftY", (Test test) ->
                {
                    final FakePainter painter = new FakePainter();
                    test.assertThrows(() -> painter.drawRectangle(Distance.zero, null, Distance.zero, Distance.zero),
                        new PreConditionFailure("topLeftY cannot be null."));
                    test.assertEqual(Iterable.create(), painter.getActions());
                });

                runner.test("with null width", (Test test) ->
                {
                    final FakePainter painter = new FakePainter();
                    test.assertThrows(() -> painter.drawRectangle(Distance.zero, Distance.zero, null, Distance.zero),
                        new PreConditionFailure("width cannot be null."));
                    test.assertEqual(Iterable.create(), painter.getActions());
                });

                runner.test("with negative width", (Test test) ->
                {
                    final FakePainter painter = new FakePainter();
                    test.assertThrows(() -> painter.drawRectangle(Distance.zero, Distance.zero, Distance.inches(-1), Distance.zero),
                        new PreConditionFailure("width (-1.0 Inches) must be greater than 0.0 Inches."));
                    test.assertEqual(Iterable.create(), painter.getActions());
                });

                runner.test("with null height", (Test test) ->
                {
                    final FakePainter painter = new FakePainter();
                    test.assertThrows(() -> painter.drawRectangle(Distance.zero, Distance.zero, Distance.inches(2), null),
                        new PreConditionFailure("height cannot be null."));
                    test.assertEqual(Iterable.create(), painter.getActions());
                });

                runner.test("with negative height", (Test test) ->
                {
                    final FakePainter painter = new FakePainter();
                    test.assertThrows(() -> painter.drawRectangle(Distance.zero, Distance.zero, Distance.inches(1), Distance.inches(-1)),
                        new PreConditionFailure("height (-1.0 Inches) must be greater than 0.0 Inches."));
                    test.assertEqual(Iterable.create(), painter.getActions());
                });

                runner.test("with valid arguments", (Test test) ->
                {
                    final FakePainter painter = new FakePainter();
                    painter.drawRectangle(Distance.zero, Distance.zero, Distance.inches(1), Distance.inches(2));
                    test.assertEqual(
                        Iterable.create(
                            new DrawRectangleAction(Point2D.zero, Distance.inches(1), Distance.inches(2), Color.black)),
                        painter.getActions());
                });
            });

            runner.testGroup("fillRectangle(Distance,Distance)", () ->
            {
                runner.test("with null width", (Test test) ->
                {
                    final FakePainter painter = new FakePainter();
                    test.assertThrows(() -> painter.fillRectangle(null, Distance.miles(1)),
                        new PreConditionFailure("width cannot be null."));
                });

                runner.test("with negative width", (Test test) ->
                {
                    final FakePainter painter = new FakePainter();
                    test.assertThrows(() -> painter.fillRectangle(Distance.millimeters(-1), Distance.miles(1)),
                        new PreConditionFailure("width (-1.0 Millimeters) must be greater than 0.0 Inches."));
                });

                runner.test("with null height", (Test test) ->
                {
                    final FakePainter painter = new FakePainter();
                    test.assertThrows(() -> painter.fillRectangle(Distance.inches(10), null),
                        new PreConditionFailure("height cannot be null."));
                });

                runner.test("with negative width", (Test test) ->
                {
                    final FakePainter painter = new FakePainter();
                    test.assertThrows(() -> painter.fillRectangle(Distance.inches(10), Distance.centimeters(-10)),
                        new PreConditionFailure("height (-10.0 Centimeters) must be greater than 0.0 Inches."));
                });

                runner.test("with positive width and height", (Test test) ->
                {
                    final FakePainter painter = new FakePainter();

                    painter.fillRectangle(Distance.inches(1), Distance.inches(3));

                    test.assertEqual(
                        Iterable.create(
                            new FillRectangleAction(Point2D.zero, Distance.inches(1), Distance.inches(3), Color.black)),
                        painter.getActions());
                });

                runner.test("with positive width and height and color", (Test test) ->
                {
                    final FakePainter painter = new FakePainter();
                    painter.setColor(Color.green);

                    painter.fillRectangle(Distance.inches(1), Distance.inches(3));

                    test.assertEqual(
                        Iterable.create(
                            new FillRectangleAction(Point2D.zero, Distance.inches(1), Distance.inches(3), Color.green)),
                        painter.getActions());
                });
            });

            runner.testGroup("translate(Distance,Distance)", () ->
            {
                runner.test("with null x", (Test test) ->
                {
                    final FakePainter painter = new FakePainter();
                    painter.translate(null, Distance.zero);
                    test.assertEqual(Vector2D.zero, painter.getTranslation());
                });

                runner.test("with null y", (Test test) ->
                {
                    final FakePainter painter = new FakePainter();
                    painter.translate(Distance.zero, null);
                    test.assertEqual(Vector2D.zero, painter.getTranslation());
                });

                runner.test("with zero x and zero y", (Test test) ->
                {
                    final FakePainter painter = new FakePainter();
                    painter.translate(Distance.zero, Distance.zero);
                    test.assertEqual(Vector2D.zero, painter.getTranslation());
                });
            });

            runner.testGroup("saveTransform()", () ->
            {
                runner.test("when no changes happen between save and restore", (Test test) ->
                {
                    final FakePainter painter = new FakePainter();
                    final Disposable disposable = painter.saveTransform();
                    test.assertNotNull(disposable);
                    test.assertEqual(Vector2D.zero, painter.getTranslation());
                    test.assertSuccess(true, disposable.dispose());
                    test.assertEqual(Vector2D.zero, painter.getTranslation());
                    test.assertSuccess(false, disposable.dispose());
                    test.assertEqual(Vector2D.zero, painter.getTranslation());
                });

                runner.test("when changes happen between save and restore", (Test test) ->
                {
                    final FakePainter painter = new FakePainter();
                    final Disposable disposable = painter.saveTransform();
                    test.assertNotNull(disposable);
                    test.assertEqual(Vector2D.zero, painter.getTranslation());

                    painter.translate(Distance.inches(1), Distance.inches(2));
                    test.assertEqual(new Vector2D(Distance.inches(1), Distance.inches(2)), painter.getTranslation());

                    test.assertSuccess(true, disposable.dispose());
                    test.assertEqual(Vector2D.zero, painter.getTranslation());
                    test.assertSuccess(false, disposable.dispose());
                    test.assertEqual(Vector2D.zero, painter.getTranslation());
                });
            });

            runner.testGroup("restoreTransform()", () ->
            {
                runner.test("when no saves have happened", (Test test) ->
                {
                    final FakePainter painter = new FakePainter();
                    test.assertThrows(painter::restoreTransform,
                        new PreConditionFailure("translations.getCount() (1) must be greater than or equal to 2."));
                    test.assertEqual(Vector2D.zero, painter.getTranslation());
                });
            });
        });
    }
}
