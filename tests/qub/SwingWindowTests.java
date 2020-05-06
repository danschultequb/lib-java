package qub;

public interface SwingWindowTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(SwingWindow.class, () ->
        {
            runner.testGroup("create(Display)", () ->
            {
                runner.test("with null Display", (Test test) ->
                {
                    test.assertThrows(() -> SwingWindow.create(null, test.getMainAsyncRunner()),
                        new PreConditionFailure("display cannot be null."));
                });

                runner.test("with null AsyncRunner", (Test test) ->
                {
                    final Display display = new Display(1000, 1000, 100, 100);
                    test.assertThrows(() -> SwingWindow.create(display, null),
                            new PreConditionFailure("asyncRunner cannot be null."));
                });

                runner.test("with valid arguments", (Test test) ->
                {
                    final Display display = new Display(1000, 1000, 100, 100);
                    try (final SwingWindow window = SwingWindow.create(display, test.getMainAsyncRunner()))
                    {
                        test.assertNotNull(window);
                        test.assertEqual("", window.getTitle());
                        test.assertFalse(window.isVisible());
                        test.assertFalse(window.isDisposed());
                        test.assertNull(window.getContent());
                    }
                });
            });

            runner.testGroup("setTitle(String)", () ->
            {
                final Action2<String,Throwable> setTitleErrorTest = (String title, Throwable expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(title), (Test test) ->
                    {
                        final Display display = new Display(1000, 1000, 100, 100);
                        final AsyncRunner asyncRunner = test.getMainAsyncRunner();
                        try (final SwingWindow window = SwingWindow.create(display, asyncRunner))
                        {
                            test.assertThrows(() -> window.setTitle(title), expected);
                            test.assertEqual("", window.getTitle());
                        }
                    });
                };

                setTitleErrorTest.run(null, new PreConditionFailure("title cannot be null."));

                final Action1<String> setTitleTest = (String title) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(title), (Test test) ->
                    {
                        final Display display = new Display(1000, 1000, 100, 100);
                        final AsyncRunner asyncRunner = test.getMainAsyncRunner();
                        try (final SwingWindow window = SwingWindow.create(display, asyncRunner))
                        {
                            final SwingWindow setTitleResult = window.setTitle(title);
                            test.assertSame(window, setTitleResult);
                            test.assertEqual(title, window.getTitle());
                        }
                    });
                };

                setTitleTest.run("");
                setTitleTest.run("a");
                setTitleTest.run("hello world");
            });

            runner.testGroup("setVisible(boolean)", () ->
            {
                runner.test("with false when not visible", (Test test) ->
                {
                    final Display display = new Display(1000, 1000, 100, 100);
                    final AsyncRunner asyncRunner = test.getMainAsyncRunner();
                    try (final SwingWindow window = SwingWindow.create(display, asyncRunner))
                    {
                        final SwingWindow setVisibleResult = window.setVisible(false);
                        test.assertSame(window, setVisibleResult);
                        test.assertFalse(window.isVisible());
                    }
                });

                runner.test("with true when not visible", (Test test) ->
                {
                    final Display display = new Display(1000, 1000, 100, 100);
                    final AsyncRunner asyncRunner = test.getMainAsyncRunner();
                    try (final SwingWindow window = SwingWindow.create(display, asyncRunner))
                    {
                        final SwingWindow setVisibleResult = window.setVisible(true);
                        test.assertSame(window, setVisibleResult);
                        test.assertTrue(window.isVisible());
                    }
                });

                runner.test("with false when visible", (Test test) ->
                {
                    final Display display = new Display(1000, 1000, 100, 100);
                    final AsyncRunner asyncRunner = test.getMainAsyncRunner();
                    try (final SwingWindow window = SwingWindow.create(display, asyncRunner).setVisible(true))
                    {
                        final SwingWindow setVisibleResult = window.setVisible(false);
                        test.assertSame(window, setVisibleResult);
                        test.assertFalse(window.isVisible());
                    }
                });

                runner.test("with true when visible", (Test test) ->
                {
                    final Display display = new Display(1000, 1000, 100, 100);
                    final AsyncRunner asyncRunner = test.getMainAsyncRunner();
                    try (final SwingWindow window = SwingWindow.create(display, asyncRunner).setVisible(true))
                    {
                        final SwingWindow setVisibleResult = window.setVisible(true);
                        test.assertSame(window, setVisibleResult);
                        test.assertTrue(window.isVisible());
                    }
                });

                runner.test("with false when disposed", (Test test) ->
                {
                    final Display display = new Display(1000, 1000, 100, 100);
                    final AsyncRunner asyncRunner = test.getMainAsyncRunner();
                    try (final SwingWindow window = SwingWindow.create(display, asyncRunner))
                    {
                        window.dispose().await();

                        test.assertThrows(() -> window.setVisible(false),
                                new PreConditionFailure("this.isDisposed() cannot be true."));
                    }
                });

                runner.test("with true when disposed", (Test test) ->
                {
                    final Display display = new Display(1000, 1000, 100, 100);
                    final AsyncRunner asyncRunner = test.getMainAsyncRunner();
                    try (final SwingWindow window = SwingWindow.create(display, asyncRunner))
                    {
                        window.dispose().await();

                        test.assertThrows(() -> window.setVisible(true),
                                new PreConditionFailure("this.isDisposed() cannot be true."));
                    }
                });
            });

            runner.test("dispose()", (Test test) ->
            {
                final Display display = new Display(1000, 1000, 100, 100);
                final AsyncRunner asyncRunner = test.getMainAsyncRunner();
                try (final SwingWindow window = SwingWindow.create(display, asyncRunner))
                {
                    test.assertFalse(window.isDisposed());

                    test.assertTrue(window.dispose().await());
                    test.assertTrue(window.isDisposed());

                    test.assertFalse(window.dispose().await());
                    test.assertTrue(window.isDisposed());
                }
            });

            runner.testGroup("await()", () ->
            {
                runner.test("when not visible", (Test test) ->
                {
                    final Display display = test.getDisplays().first();
                    final AsyncRunner asyncRunner = test.getMainAsyncRunner();
                    try (final SwingWindow window = SwingWindow.create(display, asyncRunner))
                    {
                        test.assertThrows(() -> window.await(),
                                new PreConditionFailure("this.isVisible() cannot be false."));
                        test.assertFalse(window.isVisible());
                        test.assertFalse(window.isDisposed());
                    }
                });

                runner.test("when visible", runner.skip(), (Test test) ->
                {
                    final Display display = test.getDisplays().first();
                    final AsyncRunner asyncRunner = test.getMainAsyncRunner();
                    try (final SwingWindow window = SwingWindow.create(display, asyncRunner).setTitle(test.getFullName()))
                    {
                        window.setSize(Distance.inches(4), Distance.inches(4));

                        final UIVerticalLayout verticalLayout = SwingUIVerticalLayout.create(display);

                        final IntegerValue fontPoints = IntegerValue.create(10);

                        final UIText text = SwingUIText.create(display)
                            .setText("Welcome to my program.")
                            .setFontSize(Distance.fontPoints(fontPoints.get()));
                        verticalLayout.add(text);

                        final UIButton button = SwingUIButton.create(display, asyncRunner)
                            .setText("Font Points: " + fontPoints)
                            .setFontSize(Distance.fontPoints(fontPoints.get()));
                        verticalLayout.add(button);

                        final UIButton switchDirectionButton = SwingUIButton.create(display, asyncRunner)
                            .setText("Switch Direction")
                            .setFontSize(Distance.fontPoints(fontPoints.get()));
                        switchDirectionButton.onClick(() ->
                        {
                            verticalLayout.setDirection(verticalLayout.getDirection() == VerticalDirection.TopToBottom
                                ? VerticalDirection.BottomToTop
                                : VerticalDirection.TopToBottom);
                        });
                        verticalLayout.add(switchDirectionButton);

                        button.onClick(() ->
                        {
                            fontPoints.increment();

                            text.setFontSize(Distance.fontPoints(fontPoints.get()));

                            button.setText("Font Points: " + fontPoints);
                            button.setFontSize(Distance.fontPoints(fontPoints.get()));

                            switchDirectionButton.setFontSize(Distance.fontPoints(fontPoints.get()));
                        });

                        window.setContent(verticalLayout);

                        window.setVisible(true);

                        window.await();

                        test.assertFalse(window.isVisible());
                        test.assertTrue(window.isDisposed());
                    }
                });
            });

            runner.testGroup("setContent(UIElement)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final Display display = test.getDisplays().first();
                    final AsyncRunner asyncRunner = test.getMainAsyncRunner();
                    try (final SwingWindow window = SwingWindow.create(display, asyncRunner))
                    {
                        test.assertThrows(() -> window.setContent((UIElement)null),
                                new PreConditionFailure("content cannot be null."));
                        test.assertNull(window.getContent());
                    }
                });

                runner.test("with non-null when no content exists", (Test test) ->
                {
                    final Display display = test.getDisplays().first();
                    final AsyncRunner asyncRunner = test.getMainAsyncRunner();
                    try (final SwingWindow window = SwingWindow.create(display, asyncRunner))
                    {
                        final SwingUIButton content = SwingUIButton.create(display, asyncRunner).setText("Hello World!");
                        final SwingWindow setContentResult = window.setContent((UIElement)content);
                        test.assertSame(window, setContentResult);
                        test.assertSame(content, window.getContent());
                    }
                });

                runner.test("with non-null when content exists", (Test test) ->
                {
                    final Display display = test.getDisplays().first();
                    final AsyncRunner asyncRunner = test.getMainAsyncRunner();
                    try (final SwingWindow window = SwingWindow.create(display, asyncRunner))
                    {
                        final SwingUIButton content1 = SwingUIButton.create(display, asyncRunner).setText("First Content");
                        window.setContent(content1);
                        test.assertSame(content1, window.getContent());

                        final SwingUIButton content2 = SwingUIButton.create(display, asyncRunner).setText("Hello World!");
                        final SwingWindow setContentResult = window.setContent((UIElement)content2);
                        test.assertSame(window, setContentResult);
                        test.assertSame(content2, window.getContent());
                    }
                });
            });

            runner.testGroup("setContent(SwingUIElement)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final Display display = test.getDisplays().first();
                    final AsyncRunner asyncRunner = test.getMainAsyncRunner();
                    try (final SwingWindow window = SwingWindow.create(display, asyncRunner))
                    {
                        test.assertThrows(() -> window.setContent((SwingUIElement)null),
                                new PreConditionFailure("content cannot be null."));
                        test.assertNull(window.getContent());
                    }
                });

                runner.test("with non-null when no content exists", (Test test) ->
                {
                    final Display display = test.getDisplays().first();
                    final AsyncRunner asyncRunner = test.getMainAsyncRunner();
                    try (final SwingWindow window = SwingWindow.create(display, asyncRunner))
                    {
                        final SwingUIButton content = SwingUIButton.create(display, asyncRunner).setText("Hello World!");
                        final SwingWindow setContentResult = window.setContent((SwingUIElement)content);
                        test.assertSame(window, setContentResult);
                        test.assertSame(content, window.getContent());
                    }
                });

                runner.test("with non-null when content exists", (Test test) ->
                {
                    final Display display = test.getDisplays().first();
                    final AsyncRunner asyncRunner = test.getMainAsyncRunner();
                    try (final SwingWindow window = SwingWindow.create(display, asyncRunner))
                    {
                        final SwingUIButton content1 = SwingUIButton.create(display, asyncRunner).setText("First Content");
                        window.setContent(content1);
                        test.assertSame(content1, window.getContent());

                        final SwingUIButton content2 = SwingUIButton.create(display, asyncRunner).setText("Hello World!");
                        final SwingWindow setContentResult = window.setContent((SwingUIElement)content2);
                        test.assertSame(window, setContentResult);
                        test.assertSame(content2, window.getContent());
                    }
                });
            });

            runner.testGroup("setWidth(Distance)", () ->
            {
                final Action2<Distance,Throwable> setWidthErrorTest = (Distance width, Throwable expected) ->
                {
                    runner.test("with " + width, (Test test) ->
                    {
                        final Display display = new Display(1000, 1000, 100, 100);
                        final AsyncRunner asyncRunner = test.getMainAsyncRunner();
                        try (final SwingWindow window = SwingWindow.create(display, asyncRunner))
                        {
                            test.assertThrows(() -> window.setWidth(width), expected);
                        }
                    });
                };

                setWidthErrorTest.run(null, new PreConditionFailure("width cannot be null."));
                setWidthErrorTest.run(Distance.inches(-1), new PreConditionFailure("width (-1.0 Inches) must be greater than or equal to 0.0 Inches."));

                final Action2<Distance,Distance> setWidthTest = (Distance width, Distance expectedWidth) ->
                {
                    runner.test("with " + width, (Test test) ->
                    {
                        final Display display = new Display(1000, 1000, 100, 100);
                        final AsyncRunner asyncRunner = test.getMainAsyncRunner();
                        try (final SwingWindow window = SwingWindow.create(display, asyncRunner))
                        {
                            final SwingWindow setWidthResult = window.setWidth(width);
                            test.assertSame(window, setWidthResult);
                            test.assertEqual(expectedWidth, window.getWidth());
                        }
                    });
                };

                setWidthTest.run(Distance.zero, Distance.zero);
                setWidthTest.run(Distance.inches(1), Distance.inches(1));
                setWidthTest.run(Distance.feet(1), Distance.feet(1));
            });

            runner.testGroup("setHeight(Distance)", () ->
            {
                final Action2<Distance,Throwable> setHeightErrorTest = (Distance height, Throwable expected) ->
                {
                    runner.test("with " + height, (Test test) ->
                    {
                        final Display display = new Display(1000, 1000, 100, 100);
                        final AsyncRunner asyncRunner = test.getMainAsyncRunner();
                        try (final SwingWindow window = SwingWindow.create(display, asyncRunner))
                        {
                            test.assertThrows(() -> window.setHeight(height), expected);
                        }
                    });
                };

                setHeightErrorTest.run(null, new PreConditionFailure("height cannot be null."));
                setHeightErrorTest.run(Distance.inches(-1), new PreConditionFailure("height (-1.0 Inches) must be greater than or equal to 0.0 Inches."));

                final Action1<Distance> setHeightTest = (Distance height) ->
                {
                    runner.test("with " + height, (Test test) ->
                    {
                        final Display display = new Display(1000, 1000, 100, 100);
                        final AsyncRunner asyncRunner = test.getMainAsyncRunner();
                        try (final SwingWindow window = SwingWindow.create(display, asyncRunner))
                        {
                            final SwingWindow setHeightResult = window.setHeight(height);
                            test.assertSame(window, setHeightResult);
                            test.assertEqual(height, window.getHeight());
                        }
                    });
                };

                setHeightTest.run(Distance.zero);
                setHeightTest.run(Distance.inches(1));
                setHeightTest.run(Distance.feet(1));
            });

            runner.testGroup("setSize(Size2D)", () ->
            {
                final Action2<Size2D,Throwable> setSizeErrorTest = (Size2D size, Throwable expected) ->
                {
                    runner.test("with " + size, (Test test) ->
                    {
                        final Display display = new Display(1000, 1000, 100, 100);
                        final AsyncRunner asyncRunner = test.getMainAsyncRunner();
                        try (final SwingWindow window = SwingWindow.create(display, asyncRunner))
                        {
                            test.assertThrows(() -> window.setSize(size), expected);
                        }
                    });
                };

                setSizeErrorTest.run(null, new PreConditionFailure("size cannot be null."));

                final Action1<Size2D> setSizeTest = (Size2D size) ->
                {
                    runner.test("with " + size, (Test test) ->
                    {
                        final Display display = new Display(1000, 1000, 100, 100);
                        final AsyncRunner asyncRunner = test.getMainAsyncRunner();
                        try (final SwingWindow window = SwingWindow.create(display, asyncRunner))
                        {
                            final SwingWindow setSizeResult = window.setSize(size);
                            test.assertSame(window, setSizeResult);
                            test.assertEqual(size, window.getSize());
                        }
                    });
                };

                setSizeTest.run(Size2D.zero);
                setSizeTest.run(new Size2D(Distance.zero, Distance.inches(1)));
                setSizeTest.run(new Size2D(Distance.inches(1), Distance.zero));
                setSizeTest.run(new Size2D(Distance.inches(2), Distance.inches(3)));
            });

            runner.testGroup("setSize(Distance,Distance)", () ->
            {
                final Action3<Distance,Distance,Throwable> setSizeErrorTest = (Distance width, Distance height, Throwable expected) ->
                {
                    runner.test("with " + English.andList(width, height), (Test test) ->
                    {
                        final Display display = new Display(1000, 1000, 100, 100);
                        final AsyncRunner asyncRunner = test.getMainAsyncRunner();
                        try (final SwingWindow window = SwingWindow.create(display, asyncRunner))
                        {
                            test.assertThrows(() -> window.setSize(width, height), expected);
                        }
                    });
                };

                setSizeErrorTest.run(null, Distance.inches(1), new PreConditionFailure("width cannot be null."));
                setSizeErrorTest.run(Distance.inches(-1), Distance.inches(1), new PreConditionFailure("width (-1.0 Inches) must be greater than or equal to 0.0 Inches."));
                setSizeErrorTest.run(Distance.zero, null, new PreConditionFailure("height cannot be null."));
                setSizeErrorTest.run(Distance.zero, Distance.inches(-1), new PreConditionFailure("height (-1.0 Inches) must be greater than or equal to 0.0 Inches."));

                final Action2<Distance,Distance> setSizeTest = (Distance width, Distance height) ->
                {
                    runner.test("with " + English.andList(width, height), (Test test) ->
                    {
                        final Display display = new Display(1000, 1000, 100, 100);
                        final AsyncRunner asyncRunner = test.getMainAsyncRunner();
                        try (final SwingWindow window = SwingWindow.create(display, asyncRunner))
                        {
                            final SwingWindow setHeightResult = window.setSize(width, height);
                            test.assertSame(window, setHeightResult);
                            test.assertEqual(width, window.getWidth());
                            test.assertEqual(height, window.getHeight());
                        }
                    });
                };

                setSizeTest.run(Distance.zero, Distance.zero);
                setSizeTest.run(Distance.zero, Distance.inches(2));
                setSizeTest.run(Distance.inches(1.5), Distance.zero);
                setSizeTest.run(Distance.inches(5), Distance.inches(4));
            });
        });
    }
}
