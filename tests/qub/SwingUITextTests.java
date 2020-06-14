package qub;

public interface SwingUITextTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(SwingUIText.class, () ->
        {
            final Function1<Test,SwingUIText> creator = (Test test) ->
            {
                final Display display = test.getDisplays().first();
                final AsyncRunner asyncRunner = test.getMainAsyncRunner();
                final AWTUIBase base = AWTUIBase.create(display, asyncRunner);
                return SwingUIText.create(base);
            };

            UITextTests.test(runner, creator);

            runner.testGroup("create(JavaUIBase)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> SwingUIText.create((AWTUIBase)null),
                        new PreConditionFailure("uiBase cannot be null."));
                });

                runner.test("with non-null", (Test test) ->
                {
                    final SwingUIText text = SwingUIText.create(AWTUIBase.create(test.getDisplays().first(), test.getMainAsyncRunner()));
                    test.assertNotNull(text);
                    test.assertEqual("", text.getText());
                    final javax.swing.JLabel jLabel = text.getComponent();
                    test.assertNotNull(jLabel);
                });
            });

            runner.testGroup("create(Display,AsyncRunner)", () ->
            {
                runner.test("with null display", (Test test) ->
                {
                    test.assertThrows(() -> SwingUIText.create(null, test.getMainAsyncRunner()),
                        new PreConditionFailure("display cannot be null."));
                });

                runner.test("with null asyncRunner", (Test test) ->
                {
                    test.assertThrows(() -> SwingUIText.create(test.getDisplays().first(), null),
                        new PreConditionFailure("asyncRunner cannot be null."));
                });

                runner.test("with non-null arguments", (Test test) ->
                {
                    final SwingUIText text = SwingUIText.create(test.getDisplays().first(), test.getMainAsyncRunner());
                    test.assertNotNull(text);
                    test.assertEqual("", text.getText());
                    final javax.swing.JLabel jLabel = text.getComponent();
                    test.assertNotNull(jLabel);
                });
            });
            
            runner.testGroup("setWidth(Distance)", () ->
            {
                runner.test("should return " + Types.getTypeName(SwingUIText.class), (Test test) ->
                {
                    final SwingUIText text = creator.run(test);
                    final SwingUIText setWidthResult = text.setWidth(Distance.inches(1));
                    test.assertSame(text, setWidthResult);
                });
            });

            runner.testGroup("setHeight(Distance)", () ->
            {
                runner.test("should return " + Types.getTypeName(SwingUIText.class), (Test test) ->
                {
                    final SwingUIText text = creator.run(test);
                    final SwingUIText setHeightResult = text.setHeight(Distance.inches(1));
                    test.assertSame(text, setHeightResult);
                });
            });

            runner.testGroup("setSize(Size2D)", () ->
            {
                runner.test("should return " + Types.getTypeName(SwingUIText.class), (Test test) ->
                {
                    final SwingUIText text = creator.run(test);
                    final SwingUIText setHeightResult = text.setSize(Size2D.create(Distance.inches(2), Distance.inches(3)));
                    test.assertSame(text, setHeightResult);
                });
            });

            runner.testGroup("setSize(Distance,Distance)", () ->
            {
                runner.test("should return " + Types.getTypeName(SwingUIText.class), (Test test) ->
                {
                    final SwingUIText text = creator.run(test);
                    final SwingUIText setHeightResult = text.setSize(Distance.inches(2), Distance.inches(3));
                    test.assertSame(text, setHeightResult);
                });
            });

            runner.testGroup("setText(String)", () ->
            {
                runner.test("should return " + Types.getTypeName(SwingUIText.class), (Test test) ->
                {
                    final SwingUIText text = creator.run(test);
                    final SwingUIText setHeightResult = text.setText("hello");
                    test.assertSame(text, setHeightResult);
                });
            });

            runner.testGroup("setFontSize(Distance)", () ->
            {
                runner.test("should return " + Types.getTypeName(SwingUIButton.class), (Test test) ->
                {
                    final SwingUIText text = creator.run(test);
                    final SwingUIText setFontSizeResult = text.setFontSize(Distance.inches(1));
                    test.assertSame(text, setFontSizeResult);
                });
            });
        });
    }
}
