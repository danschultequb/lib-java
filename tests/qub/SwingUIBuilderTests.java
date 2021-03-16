package qub;

public interface SwingUIBuilderTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(SwingUIBuilder.class, () ->
        {
            UIBuilderTests.test(runner, (FakeDesktopProcess process) -> SwingUIBuilder.create(process.getDisplays().first(), process.getMainAsyncRunner(), process.getParallelAsyncRunner()));

            runner.testGroup("create(SwingUIBase)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> SwingUIBuilder.create((AWTUIBase)null),
                        new PreConditionFailure("uiBase cannot be null."));
                });

                runner.test("with non-null", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final AWTUIBase uiBase = AWTUIBase.create(process.getDisplays().first(), process.getMainAsyncRunner(), process.getParallelAsyncRunner());
                        final SwingUIBuilder uiBuilder = SwingUIBuilder.create(uiBase);
                        test.assertNotNull(uiBuilder);
                    }
                });
            });

            runner.testGroup("setCreator(Class<U>,Function1<AWTUIBase,T>)", () ->
            {
                runner.test("with null type", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final SwingUIBuilder uiBuilder = SwingUIBuilder.create(process.getDisplays().first(), process.getMainAsyncRunner(), process.getParallelAsyncRunner());
                        test.assertThrows(() -> uiBuilder.setCreator((Class<UIElement>)null, (AWTUIBase uiBase) -> SwingUIButton.create(uiBase)),
                            new PreConditionFailure("uiType cannot be null."));
                    }
                });

                runner.test("with null creator", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final SwingUIBuilder uiBuilder = SwingUIBuilder.create(process.getDisplays().first(), process.getMainAsyncRunner(), process.getParallelAsyncRunner());
                        test.assertThrows(() -> uiBuilder.setCreator(UIButton.class, (Function1<AWTUIBase,UIButton>)null),
                            new PreConditionFailure("uiType cannot be null."));
                    }
                });
            });
        });
    }
}
