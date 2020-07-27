package qub;

public interface SwingUITextBoxTests
{
    static SwingUITextBox createUITextBox(Test test)
    {
        final Display display = new Display(1000, 1000, 100, 100);
        final AWTUIBase uiBase = AWTUIBase.create(display, test.getMainAsyncRunner(), test.getParallelAsyncRunner());
        final SwingUIBuilder uiBuilder = SwingUIBuilder.create(uiBase);
        return uiBuilder.create(SwingUITextBox.class).await();
    }
    
    static void test(TestRunner runner)
    {
        runner.testGroup(SwingUITextBox.class, () ->
        {
            UITextBoxTests.test(runner, SwingUITextBoxTests::createUITextBox);
            SwingUIElementTests.test(runner, SwingUITextBoxTests::createUITextBox);

            runner.testGroup("create(SwingUIBase)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> SwingUITextBox.create((AWTUIBase)null),
                        new PreConditionFailure("uiBase cannot be null."));
                });

                runner.test("with non-null", (Test test) ->
                {
                    final SwingUITextBox textBox = SwingUITextBoxTests.createUITextBox(test);
                    test.assertNotNull(textBox);
                    test.assertEqual(Distance.inches(0.05), textBox.getWidth());
                    test.assertEqual(Distance.inches(0.2), textBox.getHeight());
                    test.assertEqual(Size2D.create(Distance.inches(0.05), Distance.inches(0.2)), textBox.getSize());
                    test.assertEqual(Distance.fontPoints(12), textBox.getFontSize());
                    test.assertEqual("", textBox.getText());
                    test.assertEqual(UIPaddingInPixels.create(2), textBox.getPaddingInPixels());

                    final javax.swing.JTextField component = textBox.getComponent();
                    test.assertNotNull(component);

                    final javax.swing.JTextField jComponent = textBox.getJComponent();
                    test.assertNotNull(jComponent);
                    test.assertSame(component, jComponent);
                });
            });

            runner.testGroup("setWidth(Distance)", () ->
            {
                runner.test("should return " + Types.getTypeName(SwingUITextBox.class), (Test test) ->
                {
                    final SwingUITextBox textBox = SwingUITextBoxTests.createUITextBox(test);
                    final SwingUITextBox setWidthResult = textBox.setWidth(Distance.inches(1));
                    test.assertSame(textBox, setWidthResult);
                });
            });

            runner.testGroup("setWidthInPixels(int)", () ->
            {
                runner.test("should return " + Types.getTypeName(SwingUITextBox.class), (Test test) ->
                {
                    final SwingUITextBox textBox = SwingUITextBoxTests.createUITextBox(test);
                    final SwingUITextBox setWidthInPixelsResult = textBox.setWidthInPixels(1);
                    test.assertSame(textBox, setWidthInPixelsResult);
                });
            });

            runner.testGroup("setHeight(Distance)", () ->
            {
                runner.test("should return " + Types.getTypeName(SwingUITextBox.class), (Test test) ->
                {
                    final SwingUITextBox textBox = SwingUITextBoxTests.createUITextBox(test);
                    final SwingUITextBox setHeightResult = textBox.setHeight(Distance.inches(1));
                    test.assertSame(textBox, setHeightResult);
                });
            });

            runner.testGroup("setHeightInPixels(Distance)", () ->
            {
                runner.test("should return " + Types.getTypeName(SwingUITextBox.class), (Test test) ->
                {
                    final SwingUITextBox textBox = SwingUITextBoxTests.createUITextBox(test);
                    final SwingUITextBox setHeightInPixelsResult = textBox.setHeightInPixels(1);
                    test.assertSame(textBox, setHeightInPixelsResult);
                });
            });

            runner.testGroup("setSize(Size2D)", () ->
            {
                runner.test("should return " + Types.getTypeName(SwingUITextBox.class), (Test test) ->
                {
                    final SwingUITextBox textBox = SwingUITextBoxTests.createUITextBox(test);
                    final SwingUITextBox setSizeResult = textBox.setSize(Size2D.create(Distance.inches(2), Distance.inches(3)));
                    test.assertSame(textBox, setSizeResult);
                });
            });

            runner.testGroup("setSize(Distance,Distance)", () ->
            {
                runner.test("should return " + Types.getTypeName(SwingUITextBox.class), (Test test) ->
                {
                    final SwingUITextBox textBox = SwingUITextBoxTests.createUITextBox(test);
                    final SwingUITextBox setSizeResult = textBox.setSize(Distance.inches(2), Distance.inches(3));
                    test.assertSame(textBox, setSizeResult);
                });
            });

            runner.testGroup("setSizeInPixels(Distance,Distance)", () ->
            {
                runner.test("should return " + Types.getTypeName(SwingUITextBox.class), (Test test) ->
                {
                    final SwingUITextBox textBox = SwingUITextBoxTests.createUITextBox(test);
                    final SwingUITextBox setSizeInPixelsResult = textBox.setSizeInPixels(2, 3);
                    test.assertSame(textBox, setSizeInPixelsResult);
                });
            });

            runner.testGroup("setText(String)", () ->
            {
                runner.test("should return " + Types.getTypeName(SwingUITextBox.class), (Test test) ->
                {
                    final SwingUITextBox textBox = SwingUITextBoxTests.createUITextBox(test);
                    final SwingUITextBox setHeightResult = textBox.setText("hello");
                    test.assertSame(textBox, setHeightResult);
                });
            });

            runner.testGroup("setFontSize(Distance)", () ->
            {
                runner.test("should return " + Types.getTypeName(SwingUITextBox.class), (Test test) ->
                {
                    final SwingUITextBox textBox = SwingUITextBoxTests.createUITextBox(test);
                    final SwingUITextBox setFontSizeResult = textBox.setFontSize(Distance.inches(1));
                    test.assertSame(textBox, setFontSizeResult);
                });
            });
        });
    }
}
