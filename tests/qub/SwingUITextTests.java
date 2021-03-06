package qub;

public interface SwingUITextTests
{
    static AWTUIBase createUIBase(FakeDesktopProcess process)
    {
        return AWTUIBase.create(process.getDisplays().first(), process.getMainAsyncRunner(), process.getParallelAsyncRunner());
    }

    static SwingUIBuilder createUIBuilder(FakeDesktopProcess process)
    {
        final AWTUIBase uiBase = SwingUITextTests.createUIBase(process);
        return SwingUIBuilder.create(uiBase);
    }
    
    static SwingUIText createUIText(FakeDesktopProcess process)
    {
        final SwingUIBuilder uiBuilder = SwingUITextTests.createUIBuilder(process);
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
                    test.assertThrows(() -> SwingUIText.create((AWTUIBase)null),
                        new PreConditionFailure("uiBase cannot be null."));
                });

                runner.test("with non-null", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final AWTUIBase uiBase = SwingUITextTests.createUIBase(process);
                        final SwingUIText text = SwingUIText.create(uiBase);
                        test.assertNotNull(text);
                        test.assertEqual(Distance.zero, text.getWidth());
                        test.assertEqual(Distance.zero, text.getHeight());
                        test.assertEqual(Size2D.create(Distance.zero, Distance.zero), text.getSize());
                        test.assertEqual(Distance.fontPoints(12), text.getFontSize());
                        test.assertEqual("", text.getText());
                        test.assertEqual(UIPaddingInPixels.create(), text.getPaddingInPixels());

                        final javax.swing.JLabel component = text.getComponent();
                        test.assertNotNull(component);

                        final javax.swing.JLabel jComponent = text.getJComponent();
                        test.assertNotNull(jComponent);
                        test.assertSame(component, jComponent);
                    }
                });
            });
            
            runner.testGroup("setWidth(Distance)", () ->
            {
                runner.test("should return " + Types.getTypeName(SwingUIText.class), (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final SwingUIText text = SwingUITextTests.createUIText(process);
                        final SwingUIText setWidthResult = text.setWidth(Distance.inches(1));
                        test.assertSame(text, setWidthResult);
                    }
                });
            });

            runner.testGroup("setWidthInPixels(Distance)", () ->
            {
                runner.test("should return " + Types.getTypeName(SwingUIButton.class), (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final SwingUIText text = SwingUITextTests.createUIText(process);
                        final SwingUIText setWidthInPixelsResult = text.setWidthInPixels(1);
                        test.assertSame(text, setWidthInPixelsResult);
                    }
                });
            });

            runner.testGroup("setHeight(Distance)", () ->
            {
                runner.test("should return " + Types.getTypeName(SwingUIText.class), (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final SwingUIText text = SwingUITextTests.createUIText(process);
                        final SwingUIText setHeightResult = text.setHeight(Distance.inches(1));
                        test.assertSame(text, setHeightResult);
                    }
                });
            });

            runner.testGroup("setHeightInPixels(int)", () ->
            {
                runner.test("should return " + Types.getTypeName(SwingUIText.class), (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final SwingUIText text = SwingUITextTests.createUIText(process);
                        final SwingUIText setHeightInPixelsResult = text.setHeightInPixels(1);
                        test.assertSame(text, setHeightInPixelsResult);
                    }
                });
            });

            runner.testGroup("setSize(Size2D)", () ->
            {
                runner.test("should return " + Types.getTypeName(SwingUIText.class), (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final SwingUIText text = SwingUITextTests.createUIText(process);
                        final SwingUIText setSizeResult = text.setSize(Size2D.create(Distance.inches(2), Distance.inches(3)));
                        test.assertSame(text, setSizeResult);
                    }
                });
            });

            runner.testGroup("setSize(Distance,Distance)", () ->
            {
                runner.test("should return " + Types.getTypeName(SwingUIText.class), (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final SwingUIText text = SwingUITextTests.createUIText(process);
                        final SwingUIText setSizeResult = text.setSize(Distance.inches(2), Distance.inches(3));
                        test.assertSame(text, setSizeResult);
                    }
                });
            });

            runner.testGroup("setSizeInPixels(int,int)", () ->
            {
                runner.test("should return " + Types.getTypeName(SwingUIText.class), (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final SwingUIText text = SwingUITextTests.createUIText(process);
                        final SwingUIText setSizeInPixelsResult = text.setSizeInPixels(2, 3);
                        test.assertSame(text, setSizeInPixelsResult);
                    }
                });
            });

            runner.testGroup("setText(String)", () ->
            {
                runner.test("should return " + Types.getTypeName(SwingUIText.class), (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final SwingUIText text = SwingUITextTests.createUIText(process);
                        final SwingUIText setHeightResult = text.setText("hello");
                        test.assertSame(text, setHeightResult);

                        test.assertEqual(Distance.inches(0.27), text.getWidth());
                        test.assertEqual(Distance.inches(0.16), text.getHeight());
                        test.assertEqual(Size2D.create(Distance.inches(0.27), Distance.inches(0.16)), text.getSize());
                        test.assertEqual("hello", text.getText());
                    }
                });
            });

            runner.testGroup("setFontSize(Distance)", () ->
            {
                runner.test("should return " + Types.getTypeName(SwingUIButton.class), (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final SwingUIText text = SwingUITextTests.createUIText(process);
                        final SwingUIText setFontSizeResult = text.setFontSize(Distance.inches(1));
                        test.assertSame(text, setFontSizeResult);

                        test.assertEqual(Distance.zero, text.getWidth());
                        test.assertEqual(Distance.zero, text.getHeight());
                        test.assertEqual(Size2D.create(Distance.zero, Distance.zero), text.getSize());
                        test.assertEqual(Distance.inches(1), text.getFontSize());
                    }
                });
            });
        });
    }
}
