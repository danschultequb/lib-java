package qub;

public interface SwingUIButtonTests
{
    static AWTUIBase createUIBase(FakeDesktopProcess process)
    {
        PreCondition.assertNotNull(process, "process");

        final AWTUIBase result = AWTUIBase.create(process.getDisplays().first(), process.getMainAsyncRunner(), process.getParallelAsyncRunner());

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    static SwingUIBuilder createUIBuilder(FakeDesktopProcess process)
    {
        PreCondition.assertNotNull(process, "process");

        final AWTUIBase uiBase = SwingUIButtonTests.createUIBase(process);
        final SwingUIBuilder result = SwingUIBuilder.create(uiBase);

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    static SwingUIButton createUIButton(FakeDesktopProcess process)
    {
        final SwingUIBuilder uiBuilder = SwingUIButtonTests.createUIBuilder(process);
        final SwingUIButton result = uiBuilder.create(SwingUIButton.class).await();

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    static void test(TestRunner runner)
    {
        runner.testGroup(SwingUIButton.class, () ->
        {
            UIButtonTests.test(runner, SwingUIButtonTests::createUIButton);
            SwingUIElementTests.test(runner, SwingUIButtonTests::createUIButton);

            runner.testGroup("create(SwingUIBase)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> SwingUIButton.create(null),
                        new PreConditionFailure("uiBase cannot be null."));
                });

                runner.test("with non-null", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final AWTUIBase uiBase = SwingUIButtonTests.createUIBase(process);
                        final SwingUIButton uiButton = SwingUIButton.create(uiBase);
                        test.assertNotNull(uiButton);
                        test.assertEqual(Distance.inches(0.34), uiButton.getWidth());
                        test.assertEqual(Distance.inches(0.1), uiButton.getHeight());
                        test.assertEqual("", uiButton.getText());
                        test.assertEqual(Distance.fontPoints(12), uiButton.getFontSize());
                        test.assertEqual(UIPaddingInPixels.create(17, 5, 17, 5), uiButton.getPaddingInPixels());

                        final javax.swing.JButton component = uiButton.getComponent();
                        test.assertNotNull(component);

                        final javax.swing.JButton jComponent = uiButton.getJComponent();
                        test.assertNotNull(jComponent);
                        test.assertSame(component, jComponent);
                    }
                });
            });

            runner.testGroup("setWidth(Distance)", () ->
            {
                runner.test("should return " + Types.getTypeName(SwingUIButton.class), (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final SwingUIButton button = SwingUIButtonTests.createUIButton(process);
                        final SwingUIButton setWidthResult = button.setWidth(Distance.inches(1));
                        test.assertSame(button, setWidthResult);
                    }
                });
            });

            runner.testGroup("setWidthInPixels(Distance)", () ->
            {
                runner.test("should return " + Types.getTypeName(SwingUIButton.class), (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final SwingUIButton button = SwingUIButtonTests.createUIButton(process);
                        final SwingUIButton setWidthResult = button.setWidthInPixels(1);
                        test.assertSame(button, setWidthResult);
                    }
                });
            });

            runner.testGroup("setHeight(Distance)", () ->
            {
                runner.test("should return " + Types.getTypeName(SwingUIButton.class), (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final SwingUIButton button = SwingUIButtonTests.createUIButton(process);
                        final SwingUIButton setHeightResult = button.setHeight(Distance.inches(1));
                        test.assertSame(button, setHeightResult);
                    }
                });
            });

            runner.testGroup("setHeightInPixels(int)", () ->
            {
                runner.test("should return " + Types.getTypeName(SwingUIButton.class), (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final SwingUIButton button = SwingUIButtonTests.createUIButton(process);
                        final SwingUIButton setHeightInPixelsResult = button.setHeightInPixels(1);
                        test.assertSame(button, setHeightInPixelsResult);
                    }
                });
            });

            runner.testGroup("setSize(Size2D)", () ->
            {
                runner.test("should return " + Types.getTypeName(SwingUIButton.class), (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final SwingUIButton button = SwingUIButtonTests.createUIButton(process);
                        final SwingUIButton setSizeResult = button.setSize(Size2D.create(Distance.inches(2), Distance.inches(3)));
                        test.assertSame(button, setSizeResult);
                    }
                });
            });

            runner.testGroup("setSize(Distance,Distance)", () ->
            {
                runner.test("should return " + Types.getTypeName(SwingUIButton.class), (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final SwingUIButton button = SwingUIButtonTests.createUIButton(process);
                        final SwingUIButton setSizeResult = button.setSize(Distance.inches(2), Distance.inches(3));
                        test.assertSame(button, setSizeResult);
                    }
                });
            });

            runner.testGroup("setSizeInPixels(int,int)", () ->
            {
                runner.test("should return " + Types.getTypeName(SwingUIButton.class), (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final SwingUIButton button = SwingUIButtonTests.createUIButton(process);
                        final SwingUIButton setSizeInPixelsResult = button.setSizeInPixels(2, 3);
                        test.assertSame(button, setSizeInPixelsResult);
                    }
                });
            });

            runner.testGroup("setText(String)", () ->
            {
                runner.test("should return " + Types.getTypeName(SwingUIButton.class), (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final SwingUIButton button = SwingUIButtonTests.createUIButton(process);
                        final SwingUIButton setHeightResult = button.setText("hello");
                        test.assertSame(button, setHeightResult);
                    }
                });
            });

            runner.testGroup("setFontSize(Distance)", () ->
            {
                runner.test("should return " + Types.getTypeName(SwingUIButton.class), (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final SwingUIButton button = SwingUIButtonTests.createUIButton(process);
                        final SwingUIButton setFontSizeResult = button.setFontSize(Distance.inches(1));
                        test.assertSame(button, setFontSizeResult);
                    }
                });
            });

            runner.testGroup("onClick(Action0)", () ->
            {
                runner.test("with callback that throws an exception", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final SwingUIButton button = SwingUIButtonTests.createUIButton(process);
                        final IntegerValue value = IntegerValue.create(0);

                        final Disposable subscription = button.onClick(() ->
                        {
                            value.increment();
                            throw new RuntimeException("hello");
                        });
                        test.assertNotNull(subscription);

                        test.assertThrows(() -> button.click(),
                            new RuntimeException("hello"));
                        test.assertEqual(1, value.get());

                        test.assertTrue(subscription.dispose().await());

                        // Nothing should happen.
                        button.click();
                        test.assertEqual(1, value.get());
                    }
                });

                runner.test("with callback that doesn't throw an exception", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final SwingUIButton button = SwingUIButtonTests.createUIButton(process);

                        final AsyncRunner mainAsyncRunner = process.getMainAsyncRunner();
                        final IntegerValue value = IntegerValue.create(0);
                        final SpinGate gate = SpinGate.create();
                        final Disposable subscription = button.onClick(() ->
                        {
                            value.increment();
                            gate.open();
                        });
                        test.assertNotNull(subscription);

                        button.click();
                        gate.passThrough(() -> mainAsyncRunner.schedule(Action0.empty).await()).await();
                        test.assertEqual(1, value.getAsInt());

                        gate.close();

                        button.click();
                        gate.passThrough(() -> mainAsyncRunner.schedule(Action0.empty).await()).await();
                        test.assertEqual(2, value.getAsInt());

                        test.assertTrue(subscription.dispose().await());

                        gate.close();

                        button.click();
                        mainAsyncRunner.schedule(Action0.empty).await();
                        test.assertFalse(gate.isOpen());
                        test.assertEqual(2, value.getAsInt());
                    }
                });
            });
        });
    }
}
