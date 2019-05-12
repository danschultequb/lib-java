package qub;

public class JavaWindowTests
{
    private static JavaWindow createWindow(Test test)
    {
        return createWindow(test, true);
    }

    private static JavaWindow createWindow(Test test, boolean addTitle)
    {
        final JavaWindow result = new JavaWindow(test.getMainAsyncRunner(), test.getDisplays());
        if (addTitle)
        {
            result.setTitle(test.getFullName());
        }
        return result;
    }

    public static void test(TestRunner runner)
    {
        runner.testGroup(JavaWindow.class, () ->
        {
            WindowTests.test(runner, JavaWindowTests::createWindow);

            runner.test("constructor", (Test test) ->
            {
                try (final JavaWindow window = createWindow(test, false))
                {
                    test.assertFalse(window.isDisposed());
                    test.assertFalse(window.isOpen());
                    test.assertEqual("", window.getTitle());

                    test.assertTrue(window.dispose().await());
                    test.assertTrue(window.isDisposed());
                    test.assertFalse(window.isOpen());
                    test.assertEqual("", window.getTitle());
                }
            });

            runner.testGroup("setTitle()", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    try (final JavaWindow window = createWindow(test))
                    {
                        test.assertThrows(() -> window.setTitle(null), new PreConditionFailure("title cannot be null."));
                    }
                });

                runner.test("with empty", (Test test) ->
                {
                    try (final JavaWindow window = createWindow(test))
                    {
                        window.setTitle("");
                        test.assertEqual("", window.getTitle());
                    }
                });

                runner.test("with non-empty", (Test test) ->
                {
                    try (final JavaWindow window = createWindow(test))
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
                    try (final JavaWindow window = createWindow(test))
                    {
                        test.assertThrows(() -> window.setContent((javax.swing.JComponent)null), new NotSupportedException());
                    }
                });

                runner.test("with JButton", runner.skip(), (Test test) ->
                {
                    try (final JavaWindow window = createWindow(test))
                    {
                        final javax.swing.JButton button = new javax.swing.JButton("Hello");
                        button.addActionListener(new java.awt.event.ActionListener()
                        {
                            @Override
                            public void actionPerformed(java.awt.event.ActionEvent e)
                            {
                                System.out.println("Button click");
                            }
                        });
                        button.setSize(200, 300);
                        window.setContent(button);
                        window.open();
                        window.await();
                    }
                });
            });

            runner.testGroup("await()", () ->
            {
                runner.test("when not open", (Test test) ->
                {
                    try (final JavaWindow window = createWindow(test))
                    {
                        test.assertThrows(window::await,
                            new PreConditionFailure("isOpen() cannot be false."));
                    }
                });

                runner.test("when not opened but disposed", (Test test) ->
                {
                    try (final JavaWindow window = createWindow(test))
                    {
                        window.dispose();
                        test.assertThrows(window::await,
                            new PreConditionFailure("isOpen() cannot be false."));
                    }
                });

                runner.test("when open but not disposed", runner.skip(), (Test test) ->
                {
                    try (final JavaWindow window = createWindow(test))
                    {
                        window.open();
                        window.await();
                        test.assertFalse(window.isOpen());
                        test.assertTrue(window.isDisposed());
                    }
                });

                runner.test("when opened but then disposed", (Test test) ->
                {
                    try (final JavaWindow window = createWindow(test))
                    {
                        window.open();
                        window.dispose().await();
                        test.assertThrows(window::await,
                            new PreConditionFailure("isOpen() cannot be false."));
                    }
                });
            });

            runner.test("demo", runner.skip(true), (Test test) ->
            {
                try (final JavaWindow window = createWindow(test))
                {
                    window.setContent(new UIHorizontalLayout()
                        .add(new UIText("Hello")
                                 .setFontSize(Distance.fontPoints(14))
                                 .setBackground(Color.red)
                                 .setPadding(Distance.inches(0.1)))
                        .add(new UIText("World!")
                                .setBackground(Color.green)
                                .setPadding(Distance.inches(0.1))));

                    window.open();
                    window.await();
                }
            });
        });
    }
}
