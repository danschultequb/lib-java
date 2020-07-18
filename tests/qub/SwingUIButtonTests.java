package qub;

public interface SwingUIButtonTests
{
    static SwingUIBase createUIBase(Test test)
    {
        PreCondition.assertNotNull(test, "test");

        final Display display = new Display(1000, 1000, 100, 100);
        final SwingUIBase result = SwingUIBase.create(display, test.getMainAsyncRunner(), test.getParallelAsyncRunner());

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    static SwingUIBuilder createUIBuilder(Test test)
    {
        PreCondition.assertNotNull(test, "test");

        final SwingUIBase uiBase = SwingUIButtonTests.createUIBase(test);
        final SwingUIBuilder result = SwingUIBuilder.create(uiBase);

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    static SwingUIButton createUIButton(Test test)
    {
        final SwingUIBuilder uiBuilder = SwingUIButtonTests.createUIBuilder(test);
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
                    final SwingUIBase uiBase = SwingUIButtonTests.createUIBase(test);
                    final SwingUIButton uiButton = SwingUIButton.create(uiBase);
                    test.assertNotNull(uiButton);
                    test.assertEqual(Distance.inches(0.34), uiButton.getWidth());
                    test.assertEqual(Distance.inches(0.1), uiButton.getHeight());
                    test.assertEqual("", uiButton.getText());
                    test.assertEqual(Distance.fontPoints(12), uiButton.getFontSize());

                    final javax.swing.JButton component = uiButton.getComponent();
                    test.assertNotNull(component);

                    final javax.swing.JButton jComponent = uiButton.getJComponent();
                    test.assertNotNull(jComponent);
                    test.assertSame(component, jComponent);
                });
            });

            runner.testGroup("setWidth(Distance)", () ->
            {
                runner.test("should return " + Types.getTypeName(SwingUIButton.class), (Test test) ->
                {
                    final SwingUIButton button = SwingUIButtonTests.createUIButton(test);
                    final SwingUIButton setWidthResult = button.setWidth(Distance.inches(1));
                    test.assertSame(button, setWidthResult);
                });
            });

            runner.testGroup("setWidthInPixels(Distance)", () ->
            {
                runner.test("should return " + Types.getTypeName(SwingUIButton.class), (Test test) ->
                {
                    final SwingUIButton button = SwingUIButtonTests.createUIButton(test);
                    final SwingUIButton setWidthResult = button.setWidthInPixels(1);
                    test.assertSame(button, setWidthResult);
                });
            });

            runner.testGroup("setHeight(Distance)", () ->
            {
                runner.test("should return " + Types.getTypeName(SwingUIButton.class), (Test test) ->
                {
                    final SwingUIButton button = SwingUIButtonTests.createUIButton(test);
                    final SwingUIButton setHeightResult = button.setHeight(Distance.inches(1));
                    test.assertSame(button, setHeightResult);
                });
            });

            runner.testGroup("setHeightInPixels(int)", () ->
            {
                runner.test("should return " + Types.getTypeName(SwingUIButton.class), (Test test) ->
                {
                    final SwingUIButton button = SwingUIButtonTests.createUIButton(test);
                    final SwingUIButton setHeightInPixelsResult = button.setHeightInPixels(1);
                    test.assertSame(button, setHeightInPixelsResult);
                });
            });

            runner.testGroup("setSize(Size2D)", () ->
            {
                runner.test("should return " + Types.getTypeName(SwingUIButton.class), (Test test) ->
                {
                    final SwingUIButton button = SwingUIButtonTests.createUIButton(test);
                    final SwingUIButton setHeightResult = button.setSize(Size2D.create(Distance.inches(2), Distance.inches(3)));
                    test.assertSame(button, setHeightResult);
                });
            });

            runner.testGroup("setSize(Distance,Distance)", () ->
            {
                runner.test("should return " + Types.getTypeName(SwingUIButton.class), (Test test) ->
                {
                    final SwingUIButton button = SwingUIButtonTests.createUIButton(test);
                    final SwingUIButton setHeightResult = button.setSize(Distance.inches(2), Distance.inches(3));
                    test.assertSame(button, setHeightResult);
                });
            });

            runner.testGroup("setText(String)", () ->
            {
                runner.test("should return " + Types.getTypeName(SwingUIButton.class), (Test test) ->
                {
                    final SwingUIButton button = SwingUIButtonTests.createUIButton(test);
                    final SwingUIButton setHeightResult = button.setText("hello");
                    test.assertSame(button, setHeightResult);
                });
            });

            runner.testGroup("setFontSize(Distance)", () ->
            {
                runner.test("should return " + Types.getTypeName(SwingUIButton.class), (Test test) ->
                {
                    final SwingUIButton button = SwingUIButtonTests.createUIButton(test);
                    final SwingUIButton setFontSizeResult = button.setFontSize(Distance.inches(1));
                    test.assertSame(button, setFontSizeResult);
                });
            });

            runner.testGroup("onClick(Action0)", () ->
            {
                runner.test("with callback that throws an exception", (Test test) ->
                {
                    final SwingUIButton button = SwingUIButtonTests.createUIButton(test);
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
                });

                runner.test("with callback that doesn't throw an exception", (Test test) ->
                {
                    final SwingUIButton button = SwingUIButtonTests.createUIButton(test);

                    final AsyncRunner mainAsyncRunner = test.getMainAsyncRunner();
                    final IntegerValue value = IntegerValue.create(0);
                    final SpinGate gate = SpinGate.create(test.getClock());
                    final Disposable subscription = button.onClick(() ->
                    {
                        value.increment();
                        gate.open();
                    });
                    test.assertNotNull(subscription);

                    button.click();
                    gate.passThrough(Duration.seconds(1), () -> mainAsyncRunner.schedule(Action0.empty).await());
                    test.assertEqual(1, value.getAsInt());

                    gate.close();

                    button.click();
                    gate.passThrough(Duration.seconds(1), () -> mainAsyncRunner.schedule(Action0.empty).await());
                    test.assertEqual(2, value.getAsInt());

                    test.assertTrue(subscription.dispose().await());

                    gate.close();

                    button.click();
                    gate.passThrough(Duration.seconds(1), () -> mainAsyncRunner.schedule(Action0.empty).await());
                    test.assertEqual(2, value.getAsInt());
                });
            });
        });
    }
}
