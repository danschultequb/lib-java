package qub;

public class WindowTests
{
    private static Window createWindow(Test test)
    {
        return createWindow(test, true);
    }

    private static Window createWindow(Test test, boolean addTitle)
    {
        final Window result = new Window(test.getMainAsyncRunner(), test.getDisplays());
        if (addTitle)
        {
            result.setTitle(test.getFullName());
        }
        return result;
    }

    public static void test(TestRunner runner)
    {
        runner.testGroup(Window.class, () ->
        {
            runner.test("constructor", (Test test) ->
            {
                try (final Window window = createWindow(test, false))
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

            runner.testGroup("open()", () ->
            {
                runner.test("when not open or disposed", (Test test) ->
                {
                    try (final Window window = createWindow(test))
                    {
                        window.open();
                        test.assertTrue(window.isOpen());
                        test.assertFalse(window.isDisposed());
                    }
                });

                runner.test("when already open", (Test test) ->
                {
                    try (final Window window = createWindow(test))
                    {
                        window.open();
                        test.assertThrows(() -> window.open());
                    }
                });

                runner.test("when disposed", (Test test) ->
                {
                    try (final Window window = createWindow(test))
                    {
                        window.dispose();
                        test.assertThrows(() -> window.open());
                    }
                });
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
                        final javax.swing.JButton button = new javax.swing.JButton("Hello");
                        button.setSize(200, 300);
                        window.setContent(button);
                        window.open();
                        window.awaitClose();
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

                runner.test("with UIText", runner.skip(), (Test test) ->
                {
                    try (final Window window = createWindow(test))
                    {
                        window.setContent(new UIText("Hello World!"));
                        window.open();
                        window.awaitClose();
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

                runner.test("when not opened but disposed", (Test test) ->
                {
                    try (final Window window = createWindow(test))
                    {
                        window.dispose();
                        test.assertThrows(() -> window.awaitClose());
                    }
                });

                runner.test("when open but not disposed", runner.skip(), (Test test) ->
                {
                    try (final Window window = createWindow(test))
                    {
                        window.open();
                        window.awaitClose();
                        test.assertFalse(window.isOpen());
                        test.assertTrue(window.isDisposed());
                    }
                });

                runner.test("when opened but then disposed", (Test test) ->
                {
                    try (final Window window = createWindow(test))
                    {
                        window.open();
                        window.dispose();
                        test.assertThrows(() -> window.awaitClose());
                    }
                });
            });
        });
    }
}
