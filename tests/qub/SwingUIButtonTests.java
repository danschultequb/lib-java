package qub;

public interface SwingUIButtonTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(SwingUIButton.class, () ->
        {
            final Function1<Test,SwingUIButton> creator = (Test test) ->
            {
                final Display display = test.getDisplays().first();
                final AsyncRunner asyncRunner = test.getMainAsyncRunner();
                return SwingUIButton.create(display, asyncRunner);
            };

            UIButtonTests.test(runner, creator);

            runner.testGroup("create(Display,AsyncRunner)", () ->
            {
                runner.test("with null display", (Test test) ->
                {
                    final Display display = null;
                    final AsyncRunner asyncRunner = test.getMainAsyncRunner();
                    test.assertThrows(() -> SwingUIButton.create(display, asyncRunner),
                        new PreConditionFailure("display cannot be null."));
                });

                runner.test("with null asyncRunner", (Test test) ->
                {
                    final Display display = test.getDisplays().first();
                    final AsyncRunner asyncRunner = null;
                    test.assertThrows(() -> SwingUIButton.create(display, asyncRunner),
                        new PreConditionFailure("asyncRunner cannot be null."));
                });

                runner.test("with valid arguments", (Test test) ->
                {
                    final Display display = new Display(1000, 1000, 100, 100);
                    final AsyncRunner asyncRunner = test.getMainAsyncRunner();
                    final SwingUIButton button = SwingUIButton.create(display, asyncRunner);
                    test.assertNotNull(button);
                    test.assertEqual("", button.getText());
                    test.assertEqual(Distance.zero, button.getWidth());
                    test.assertEqual(Distance.zero, button.getHeight());

                    final javax.swing.JButton jButton = button.getJComponent();
                    test.assertNotNull(jButton);
                });
            });

            runner.testGroup("setWidth(Distance)", () ->
            {
                runner.test("should return " + Types.getTypeName(SwingUIButton.class), (Test test) ->
                {
                    final SwingUIButton button = creator.run(test);
                    final SwingUIButton setWidthResult = button.setWidth(Distance.inches(1));
                    test.assertSame(button, setWidthResult);
                });
            });

            runner.testGroup("setHeight(Distance)", () ->
            {
                runner.test("should return " + Types.getTypeName(SwingUIButton.class), (Test test) ->
                {
                    final SwingUIButton button = creator.run(test);
                    final SwingUIButton setHeightResult = button.setHeight(Distance.inches(1));
                    test.assertSame(button, setHeightResult);
                });
            });

            runner.testGroup("setSize(Size2D)", () ->
            {
                runner.test("should return " + Types.getTypeName(SwingUIButton.class), (Test test) ->
                {
                    final SwingUIButton button = creator.run(test);
                    final SwingUIButton setHeightResult = button.setSize(new Size2D(Distance.inches(2), Distance.inches(3)));
                    test.assertSame(button, setHeightResult);
                });
            });

            runner.testGroup("setSize(Distance,Distance)", () ->
            {
                runner.test("should return " + Types.getTypeName(SwingUIButton.class), (Test test) ->
                {
                    final SwingUIButton button = creator.run(test);
                    final SwingUIButton setHeightResult = button.setSize(Distance.inches(2), Distance.inches(3));
                    test.assertSame(button, setHeightResult);
                });
            });

            runner.testGroup("setText(String)", () ->
            {
                runner.test("should return " + Types.getTypeName(SwingUIButton.class), (Test test) ->
                {
                    final SwingUIButton button = creator.run(test);
                    final SwingUIButton setHeightResult = button.setText("hello");
                    test.assertSame(button, setHeightResult);
                });
            });

            runner.testGroup("setFontSize(Distance)", () ->
            {
                runner.test("should return " + Types.getTypeName(SwingUIButton.class), (Test test) ->
                {
                    final SwingUIButton button = creator.run(test);
                    final SwingUIButton setFontSizeResult = button.setFontSize(Distance.inches(1));
                    test.assertSame(button, setFontSizeResult);
                });
            });

            runner.testGroup("onClick(Action0)", () ->
            {
                runner.test("with callback that throws an exception", (Test test) ->
                {
                    final SwingUIButton button = creator.run(test);

                    final Disposable subscription = button.onClick(() -> { throw new RuntimeException("hello"); });
                    test.assertNotNull(subscription);

                    test.assertThrows(() -> button.click(),
                        new RuntimeException("hello"));

                    test.assertTrue(subscription.dispose().await());
                    // Nothing should happen.
                    button.click();
                });

                runner.test("with callback that doesn't throw an exception", (Test test) ->
                {
                    final SwingUIButton button = creator.run(test);

                    final IntegerValue value = IntegerValue.create(0);

                    final Disposable subscription = button.onClick(value::increment);
                    test.assertNotNull(subscription);

                    button.click();
                    test.assertEqual(1, value.getAsInt());

                    button.click();
                    test.assertEqual(2, value.getAsInt());

                    test.assertTrue(subscription.dispose().await());

                    button.click();
                    test.assertEqual(2, value.getAsInt());
                });
            });
        });
    }
}
