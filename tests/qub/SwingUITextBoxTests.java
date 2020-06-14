package qub;

public interface SwingUITextBoxTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(SwingUITextBox.class, () ->
        {
            final Function1<Test,SwingUITextBox> creator = (Test test) ->
            {
                final Display display = test.getDisplays().first();
                final AsyncRunner asyncRunner = test.getMainAsyncRunner();
                return SwingUITextBox.create(display, asyncRunner);
            };

            UITextBoxTests.test(runner, creator);

            runner.testGroup("create(Display,AsyncRunner)", () ->
            {
                runner.test("with null display", (Test test) ->
                {
                    final Display display = null;
                    final AsyncRunner asyncRunner = test.getMainAsyncRunner();
                    test.assertThrows(() -> SwingUITextBox.create(display, asyncRunner),
                        new PreConditionFailure("display cannot be null."));
                });

                runner.test("with null asyncRunner", (Test test) ->
                {
                    final Display display = test.getDisplays().first();
                    final AsyncRunner asyncRunner = null;
                    test.assertThrows(() -> SwingUITextBox.create(display, asyncRunner),
                        new PreConditionFailure("asyncRunner cannot be null."));
                });

                runner.test("with valid arguments", (Test test) ->
                {
                    final Display display = new Display(1000, 1000, 100, 100);
                    final AsyncRunner asyncRunner = test.getMainAsyncRunner();
                    final SwingUITextBox textBox = SwingUITextBox.create(display, asyncRunner);
                    test.assertNotNull(textBox);
                    test.assertEqual("", textBox.getText());
                    test.assertEqual(Distance.zero, textBox.getWidth());
                    test.assertEqual(Distance.zero, textBox.getHeight());

                    final javax.swing.JTextField component = textBox.getComponent();
                    test.assertNotNull(component);

                    final javax.swing.JTextField jComponent = textBox.getJComponent();
                    test.assertNotNull(jComponent);
                });
            });

            runner.testGroup("setWidth(Distance)", () ->
            {
                runner.test("should return " + Types.getTypeName(SwingUITextBox.class), (Test test) ->
                {
                    final SwingUITextBox textBox = creator.run(test);
                    final SwingUITextBox setWidthResult = textBox.setWidth(Distance.inches(1));
                    test.assertSame(textBox, setWidthResult);
                });
            });

            runner.testGroup("setHeight(Distance)", () ->
            {
                runner.test("should return " + Types.getTypeName(SwingUITextBox.class), (Test test) ->
                {
                    final SwingUITextBox textBox = creator.run(test);
                    final SwingUITextBox setHeightResult = textBox.setHeight(Distance.inches(1));
                    test.assertSame(textBox, setHeightResult);
                });
            });

            runner.testGroup("setSize(Size2D)", () ->
            {
                runner.test("should return " + Types.getTypeName(SwingUITextBox.class), (Test test) ->
                {
                    final SwingUITextBox textBox = creator.run(test);
                    final SwingUITextBox setHeightResult = textBox.setSize(Size2D.create(Distance.inches(2), Distance.inches(3)));
                    test.assertSame(textBox, setHeightResult);
                });
            });

            runner.testGroup("setSize(Distance,Distance)", () ->
            {
                runner.test("should return " + Types.getTypeName(SwingUITextBox.class), (Test test) ->
                {
                    final SwingUITextBox textBox = creator.run(test);
                    final SwingUITextBox setHeightResult = textBox.setSize(Distance.inches(2), Distance.inches(3));
                    test.assertSame(textBox, setHeightResult);
                });
            });

            runner.testGroup("setText(String)", () ->
            {
                runner.test("should return " + Types.getTypeName(SwingUITextBox.class), (Test test) ->
                {
                    final SwingUITextBox textBox = creator.run(test);
                    final SwingUITextBox setHeightResult = textBox.setText("hello");
                    test.assertSame(textBox, setHeightResult);
                });
            });

            runner.testGroup("setFontSize(Distance)", () ->
            {
                runner.test("should return " + Types.getTypeName(SwingUITextBox.class), (Test test) ->
                {
                    final SwingUITextBox textBox = creator.run(test);
                    final SwingUITextBox setFontSizeResult = textBox.setFontSize(Distance.inches(1));
                    test.assertSame(textBox, setFontSizeResult);
                });
            });
        });
    }
}
