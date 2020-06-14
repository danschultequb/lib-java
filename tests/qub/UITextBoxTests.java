package qub;

public interface UITextBoxTests
{
    static void test(TestRunner runner, Function1<Test,? extends UITextBox> creator)
    {
        PreCondition.assertNotNull(runner, "runner");
        PreCondition.assertNotNull(creator, "creator");

        runner.testGroup(UITextBox.class, () ->
        {
            UIElementTests.test(runner, creator);

            runner.testGroup("setWidth(Distance)", () ->
            {
                runner.test("should return " + Types.getTypeName(UITextBox.class), (Test test) ->
                {
                    final UITextBox textBox = creator.run(test);
                    final UITextBox setWidthResult = textBox.setWidth(Distance.inches(1));
                    test.assertSame(textBox, setWidthResult);
                });
            });

            runner.testGroup("setHeight(Distance)", () ->
            {
                runner.test("should return " + Types.getTypeName(UITextBox.class), (Test test) ->
                {
                    final UITextBox textBox = creator.run(test);
                    final UITextBox setHeightResult = textBox.setHeight(Distance.inches(1));
                    test.assertSame(textBox, setHeightResult);
                });
            });

            runner.testGroup("setSize(Size2D)", () ->
            {
                runner.test("should return " + Types.getTypeName(UITextBox.class), (Test test) ->
                {
                    final UITextBox textBox = creator.run(test);
                    final UITextBox setHeightResult = textBox.setSize(Size2D.create(Distance.inches(2), Distance.inches(3)));
                    test.assertSame(textBox, setHeightResult);
                });
            });

            runner.testGroup("setSize(Distance,Distance)", () ->
            {
                runner.test("should return " + Types.getTypeName(UITextBox.class), (Test test) ->
                {
                    final UITextBox textBox = creator.run(test);
                    final UITextBox setHeightResult = textBox.setSize(Distance.inches(2), Distance.inches(3));
                    test.assertSame(textBox, setHeightResult);
                });
            });

            runner.testGroup("setText(String)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final UITextBox textBox = creator.run(test);

                    test.assertThrows(() -> textBox.setText(null),
                        new PreConditionFailure("text cannot be null."));

                    test.assertEqual("", textBox.getText());
                });

                runner.test("with empty", (Test test) ->
                {
                    final UITextBox textBox = creator.run(test);

                    final UITextBox setTextResult = textBox.setText("");
                    test.assertSame(textBox, setTextResult);

                    test.assertEqual("", textBox.getText());
                });

                runner.test("with non-empty", (Test test) ->
                {
                    final UITextBox textBox = creator.run(test);

                    final UITextBox setTextResult = textBox.setText("hello");
                    test.assertSame(textBox, setTextResult);

                    test.assertEqual("hello", textBox.getText());
                });
            });

            runner.testGroup("setFontSize(Distance)", () ->
            {
                final Action2<Distance,Throwable> setFontSizeErrorTest = (Distance fontSize, Throwable expected) ->
                {
                    runner.test("with " + fontSize, (Test test) ->
                    {
                        final UITextBox textBox = creator.run(test);
                        test.assertThrows(() -> textBox.setFontSize(fontSize), expected);
                    });
                };

                setFontSizeErrorTest.run(null, new PreConditionFailure("fontSize cannot be null."));
                setFontSizeErrorTest.run(Distance.inches(-1), new PreConditionFailure("fontSize (-1.0 Inches) must be greater than or equal to 0.0 Inches."));

                final Action1<Distance> setFontSizeTest = (Distance fontSize) ->
                {
                    runner.test("with " + fontSize, (Test test) ->
                    {
                        final UITextBox textBox = creator.run(test);
                        final UITextBox setFontSizeResult = textBox.setFontSize(fontSize);
                        test.assertSame(textBox, setFontSizeResult);
                        test.assertEqual(fontSize, textBox.getFontSize());
                    });
                };

                setFontSizeTest.run(Distance.zero);
                setFontSizeTest.run(Distance.fontPoints(12));
                setFontSizeTest.run(Distance.inches(1));
            });
        });
    }
}
