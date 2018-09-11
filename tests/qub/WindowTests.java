package qub;

public class WindowTests
{
    private static Window createWindow(Test test)
    {
        return new Window(test.getMainAsyncRunner());
    }

    public static void test(TestRunner runner)
    {
        runner.testGroup(Window.class, () ->
        {
            runner.test("constructor", (Test test) ->
            {
                try (final Window window = createWindow(test))
                {
                    test.assertFalse(window.isDisposed());
                    test.assertFalse(window.isVisible());
                    test.assertEqual("", window.getTitle());

                    test.assertSuccess(true, window.dispose());
                    test.assertTrue(window.isDisposed());
                    test.assertFalse(window.isVisible());
                    test.assertEqual("", window.getTitle());
                }
            });

            runner.test("setVisible()", (Test test) ->
            {
                 try (final Window window = createWindow(test))
                 {
                     window.setVisible(true);
                     test.assertTrue(window.isVisible());

                     window.setVisible(false);
                     test.assertFalse(window.isVisible());

                     window.setVisible(true);
                     test.assertTrue(window.isVisible());

                     test.assertSuccess(true, window.dispose());
                     test.assertFalse(window.isVisible());
                 }
            });

            runner.testGroup("setTitle()", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    try (final Window window = createWindow(test))
                    {
                        test.assertThrows(() -> window.setTitle(null));
                    }
                });

                runner.test("with empty", (Test test) ->
                {
                    try (final Window window = createWindow(test))
                    {
                        window.setTitle("");
                        test.assertEqual("", window.getTitle());
                    }
                });

                runner.test("with non-empty", (Test test) ->
                {
                    try (final Window window = createWindow(test))
                    {
                        window.setTitle("Apples and Bananas");
                        test.assertEqual("Apples and Bananas", window.getTitle());

                        window.dispose();
                        test.assertEqual("Apples and Bananas", window.getTitle());
                    }
                });
            });

            runner.testGroup("setContent(javax.swing.JComponent)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    try (final Window window = createWindow(test))
                    {
                        test.assertThrows(() -> window.setContent((javax.swing.JComponent)null));
                    }
                });
            });

            runner.testGroup("setContent(UIElement)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    try (final Window window = createWindow(test))
                    {
                        test.assertThrows(() -> window.setContent((UIElement)null));
                    }
                });
            });
        });
    }
}
