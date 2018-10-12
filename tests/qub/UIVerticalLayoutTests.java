package qub;

public class UIVerticalLayoutTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(UIVerticalLayout.class, () ->
        {
            runner.testGroup("painter(UIPainter)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final UIVerticalLayout layout = new UIVerticalLayout();
                    test.assertThrows(() -> layout.paint(null));
                });

                runner.test("with two child elements", (Test test) ->
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
        });
    }
}
