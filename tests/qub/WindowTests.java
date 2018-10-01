package qub;

public class WindowTests
{
    public static void test(TestRunner runner, Function1<Test,Window> windowCreator)
    {
        runner.testGroup(Window.class, () ->
        {
            runner.testGroup("open()", () ->
            {
                runner.test("when not open and not disposed", (Test test) ->
                {
                    try (final Window window = windowCreator.run(test))
                    {
                        window.open();
                        test.assertTrue(window.isOpen());
                    }
                });

                runner.test("when open but not disposed", (Test test) ->
                {
                    try (final Window window = windowCreator.run(test))
                    {
                        window.open();
                        test.assertTrue(window.isOpen());

                        test.assertThrows(window::open);
                        test.assertTrue(window.isOpen());
                    }
                });

                runner.test("when not open but disposed", (Test test) ->
                {
                    try (final Window window = windowCreator.run(test))
                    {
                        window.dispose();
                        test.assertFalse(window.isOpen());
                        test.assertTrue(window.isDisposed());

                        test.assertThrows(window::open);
                        test.assertFalse(window.isOpen());
                        test.assertTrue(window.isDisposed());
                    }
                });

                runner.test("when open and disposed", (Test test) ->
                {
                    try (final Window window = windowCreator.run(test))
                    {
                        window.open();
                        window.dispose();
                        test.assertFalse(window.isOpen());
                        test.assertTrue(window.isDisposed());

                        test.assertThrows(window::open);
                        test.assertFalse(window.isOpen());
                        test.assertTrue(window.isDisposed());
                    }
                });
            });

            runner.testGroup("setPainter(UIPainter)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    try (final Window window = windowCreator.run(test))
                    {
                        test.assertThrows(() -> window.setPainter(null));
                    }
                });

                runner.test("with non-null", (Test test) ->
                {
                    try (final Window window = windowCreator.run(test))
                    {
                        window.setPainter(new FakePainter());
                    }
                });
            });

            runner.testGroup("repaint()", () ->
            {
                runner.test("before open with no content", (Test test) ->
                {
                    try (final Window window = windowCreator.run(test))
                    {
                        final FakePainter painter = new FakePainter();
                        window.setPainter(painter);

                        window.repaint();

                        test.assertFalse(painter.getActions().any());
                    }
                });

                runner.test("before open with content", (Test test) ->
                {
                    try (final Window window = windowCreator.run(test))
                    {
                        final FakePainter painter = new FakePainter();
                        window.setPainter(painter);
                        window.setContent(new UIText("ABC"));

                        window.repaint();

                        test.assertFalse(painter.getActions().any());
                    }
                });

                runner.test("after open with no content", (Test test) ->
                {
                    try (final Window window = windowCreator.run(test))
                    {
                        final FakePainter painter = new FakePainter();
                        window.setPainter(painter);

                        window.open();
                        test.assertFalse(painter.getActions().any());

                        window.repaint();
                        test.assertFalse(painter.getActions().any());
                    }
                });
            });
        });
    }
}
