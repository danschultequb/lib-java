package qub;

public class WindowTests
{
    private static Window createWindow(Test test)
    {
        return new Window(test.getMainAsyncRunner(), test.getDisplays());
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
                    test.assertFalse(window.isOpen());
                    test.assertEqual("", window.getTitle());

                    test.assertSuccess(true, window.dispose());
                    test.assertTrue(window.isDisposed());
                    test.assertFalse(window.isOpen());
                    test.assertEqual("", window.getTitle());
                }
            });

            runner.test("open()", (Test test) ->
            {
                 try (final Window window = createWindow(test))
                 {
                     test.assertFalse(window.isOpen());
                     test.assertFalse(window.isDisposed());

                     window.open();
                     test.assertTrue(window.isOpen());
                     test.assertFalse(window.isDisposed());

                     window.dispose();
                     test.assertFalse(window.isOpen());
                     test.assertTrue(window.isDisposed());
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

                runner.test("with JButton", (Test test) ->
                {
                    try (final Window window = createWindow(test))
                    {
                        window.setContent(new javax.swing.JButton("Hello"));
                        window.open();
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

                runner.test("with UIText", (Test test) ->
                {
                    try (final Window window = createWindow(test))
                    {
                        window.setContent(new UIText("Hello World!"));
                        window.open();
                    }
                });
            });

            runner.testGroup("awaitClose()", () ->
            {
                runner.test("when not open", (Test test) ->
                {
                    try (final Window window = createWindow(test))
                    {
                        test.assertThrows(() -> window.awaitClose());
                    }
                });
            });
        });
    }
}
