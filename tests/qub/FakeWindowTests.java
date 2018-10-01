package qub;

public class FakeWindowTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(FakeWindow.class, () ->
        {
            WindowTests.test(runner, (Test test) -> new FakeWindow());

            runner.testGroup("repaint()", () ->
            {
                runner.test("after open with content", (Test test) ->
                {
                    try (final FakeWindow window = new FakeWindow())
                    {
                        final FakePainter painter = new FakePainter();
                        window.setPainter(painter);
                        window.setContent(new UIText("ABC"));

                        window.open();
                        test.assertTrue(painter.getActions().any());
                        test.assertTrue(painter.getActions().contains((PainterAction action) ->
                        {
                            return action instanceof DrawTextAction && ((DrawTextAction)action).getText().equals("ABC");
                        }));

                        window.repaint();
                        test.assertTrue(painter.getActions().any());
                        test.assertTrue(painter.getActions().contains((PainterAction action) ->
                        {
                            return action instanceof DrawTextAction && ((DrawTextAction)action).getText().equals("ABC");
                        }));
                    }
                });
            });
        });
    }
}
