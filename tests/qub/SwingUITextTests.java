package qub;

public interface SwingUITextTests
{
    static SwingUIBase createUIBase(Test test)
    {
        final Display display = new Display(1000, 1000, 100, 100);
        return SwingUIBase.create(display, test.getMainAsyncRunner(), test.getParallelAsyncRunner());
    }

    static SwingUIBuilder createUIBuilder(Test test)
    {
        final SwingUIBase uiBase = SwingUIVerticalLayoutTests.createUIBase(test);
        return SwingUIBuilder.create(uiBase);
    }
    
    static SwingUIText createUIText(Test test)
    {
        final SwingUIBuilder uiBuilder = SwingUITextTests.createUIBuilder(test);
        return uiBuilder.create(SwingUIText.class).await();
    }
    
    static void test(TestRunner runner)
    {
        runner.testGroup(SwingUIText.class, () ->
        {
            UITextTests.test(runner, SwingUITextTests::createUIText);
            SwingUIElementTests.test(runner, SwingUITextTests::createUIText);

            runner.testGroup("create(SwingUIBase)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> SwingUIText.create((SwingUIBase)null),
                        new PreConditionFailure("uiBase cannot be null."));
                });

                runner.test("with non-null", (Test test) ->
                {
                    final SwingUIBase uiBase = SwingUITextTests.createUIBase(test);
                    final SwingUIText text = SwingUIText.create(uiBase);
                    test.assertNotNull(text);
                    test.assertEqual(Distance.zero, text.getWidth());
                    test.assertEqual(Distance.zero, text.getHeight());
                    test.assertEqual(Size2D.create(Distance.zero, Distance.zero), text.getSize());
                    test.assertEqual(Distance.fontPoints(12), text.getFontSize());
                    test.assertEqual("", text.getText());

                    final javax.swing.JLabel component = text.getComponent();
                    test.assertNotNull(component);

                    final javax.swing.JLabel jComponent = text.getJComponent();
                    test.assertNotNull(jComponent);
                    test.assertSame(component, jComponent);
                });
            });
            
            runner.testGroup("setWidth(Distance)", () ->
            {
                runner.test("should return " + Types.getTypeName(SwingUIText.class), (Test test) ->
                {
                    final SwingUIText text = SwingUITextTests.createUIText(test);
                    final SwingUIText setWidthResult = text.setWidth(Distance.inches(1));
                    test.assertSame(text, setWidthResult);
                });
            });

            runner.testGroup("setHeight(Distance)", () ->
            {
                runner.test("should return " + Types.getTypeName(SwingUIText.class), (Test test) ->
                {
                    final SwingUIText text = SwingUITextTests.createUIText(test);
                    final SwingUIText setHeightResult = text.setHeight(Distance.inches(1));
                    test.assertSame(text, setHeightResult);
                });
            });

            runner.testGroup("setSize(Size2D)", () ->
            {
                runner.test("should return " + Types.getTypeName(SwingUIText.class), (Test test) ->
                {
                    final SwingUIText text = SwingUITextTests.createUIText(test);
                    final SwingUIText setHeightResult = text.setSize(Size2D.create(Distance.inches(2), Distance.inches(3)));
                    test.assertSame(text, setHeightResult);
                });
            });

            runner.testGroup("setSize(Distance,Distance)", () ->
            {
                runner.test("should return " + Types.getTypeName(SwingUIText.class), (Test test) ->
                {
                    final SwingUIText text = SwingUITextTests.createUIText(test);
                    final SwingUIText setHeightResult = text.setSize(Distance.inches(2), Distance.inches(3));
                    test.assertSame(text, setHeightResult);
                });
            });

            runner.testGroup("setText(String)", () ->
            {
                runner.test("should return " + Types.getTypeName(SwingUIText.class), (Test test) ->
                {
                    final SwingUIText text = SwingUITextTests.createUIText(test);
                    final SwingUIText setHeightResult = text.setText("hello");
                    test.assertSame(text, setHeightResult);

                    test.assertEqual(Distance.inches(0.27), text.getWidth());
                    test.assertEqual(Distance.inches(0.16), text.getHeight());
                    test.assertEqual(Size2D.create(Distance.inches(0.27), Distance.inches(0.16)), text.getSize());
                    test.assertEqual("hello", text.getText());
                });
            });

            runner.testGroup("setFontSize(Distance)", () ->
            {
                runner.test("should return " + Types.getTypeName(SwingUIButton.class), (Test test) ->
                {
                    final SwingUIText text = SwingUITextTests.createUIText(test);
                    final SwingUIText setFontSizeResult = text.setFontSize(Distance.inches(1));
                    test.assertSame(text, setFontSizeResult);

                    test.assertEqual(Distance.zero, text.getWidth());
                    test.assertEqual(Distance.zero, text.getHeight());
                    test.assertEqual(Size2D.create(Distance.zero, Distance.zero), text.getSize());
                    test.assertEqual(Distance.inches(1), text.getFontSize());
                });
            });
        });
    }
}
