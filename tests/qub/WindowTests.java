package qub;

public class WindowTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(Window.class, () ->
        {
            runner.test("constructor", (Test test) ->
            {
                try (final Window window = new Window())
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
                 try (final Window window = new Window())
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
                    try (final Window window = new Window())
                    {
                        test.assertThrows(() -> window.setTitle(null));
                    }
                });

                runner.test("with empty", (Test test) ->
                {
                    try (final Window window = new Window())
                    {
                        window.setTitle("");
                        test.assertEqual("", window.getTitle());
                    }
                });

                runner.test("with non-empty", (Test test) ->
                {
                    try (final Window window = new Window())
                    {
                        window.setTitle("Apples and Bananas");
                        test.assertEqual("Apples and Bananas", window.getTitle());

                        window.dispose();
                        test.assertEqual("Apples and Bananas", window.getTitle());
                    }
                });
            });
        });
    }
}
