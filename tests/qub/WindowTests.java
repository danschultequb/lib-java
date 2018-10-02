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

            runner.testGroup("setContent(UIElement)", () ->
            {
                runner.test("from null to null", (Test test) ->
                {
                    try (final Window window = windowCreator.run(test))
                    {
                        test.assertThrows(() -> window.setContent(null));
                    }
                });

                runner.test("from null to non-null", (Test test) ->
                {
                    try (final Window window = windowCreator.run(test))
                    {
                        final UIText text = new UIText("abc");
                        window.setContent(text);
                        test.assertSame(window, text.getParentWindow());
                    }
                });

                runner.test("from non-null to null", (Test test) ->
                {
                    try (final Window window = windowCreator.run(test))
                    {
                        final UIText text = new UIText("abc");
                        window.setContent(text);

                        test.assertThrows(() -> window.setContent(null));
                        test.assertSame(window, text.getParentWindow());
                    }
                });

                runner.test("from non-null to non-null", (Test test) ->
                {
                    try (final Window window = windowCreator.run(test))
                    {
                        final UIText text1 = new UIText("abc");
                        window.setContent(text1);

                        final UIText text2 = new UIText("xyz");
                        window.setContent(text2);

                        test.assertThrows(() -> text1.getParentWindow());
                        test.assertSame(window, text2.getParentWindow());
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

            runner.testGroup("setWidth(Distance)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    try (final Window window = windowCreator.run(test))
                    {
                        test.assertThrows(() -> window.setWidth(null));
                    }
                });

                runner.test("with negative", (Test test) ->
                {
                    try (final Window window = windowCreator.run(test))
                    {
                        test.assertThrows(() -> window.setWidth(Distance.inches(-1)));
                    }
                });

                runner.test("with zero", (Test test) ->
                {
                    try (final Window window = windowCreator.run(test))
                    {
                        window.open();
                        window.setWidth(Distance.zero);
                        test.assertOneOf(new Distance[] { Distance.zero, Distance.inches(1.34375) }, window.getWidth());
                    }
                });

                runner.test("with positive", (Test test) ->
                {
                    try (final Window window = windowCreator.run(test))
                    {
                        window.open();
                        window.setWidth(Distance.inches(5));
                        test.assertEqual(Distance.inches(5), window.getWidth());
                    }
                });
            });
        });
    }
}
