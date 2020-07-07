package qub;

public interface SwingUIBuilderTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(SwingUIBuilder.class, () ->
        {
            UIBuilderTests.test(runner, (Test test) -> SwingUIBuilder.create(test.getProcess()));

            runner.testGroup("create(SwingUIBase)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> SwingUIBuilder.create((SwingUIBase)null),
                        new PreConditionFailure("uiBase cannot be null."));
                });

                runner.test("with non-null", (Test test) ->
                {
                    final SwingUIBase uiBase = SwingUIBase.create(test.getDisplays().first(), test.getMainAsyncRunner(), test.getParallelAsyncRunner());
                    final SwingUIBuilder uiBuilder = SwingUIBuilder.create(uiBase);
                    test.assertNotNull(uiBuilder);
                });
            });

            runner.testGroup("create(SwingUIBase)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> SwingUIBuilder.create((Process)null),
                        new PreConditionFailure("process cannot be null."));
                });

                runner.test("with non-null", (Test test) ->
                {
                    final SwingUIBuilder uiBuilder = SwingUIBuilder.create(test.getProcess());
                    test.assertNotNull(uiBuilder);
                });
            });

            runner.testGroup("setCreator(Class<U>,Function1<SwingUIBase,T>)", () ->
            {
                runner.test("with null type", (Test test) ->
                {
                    final SwingUIBuilder uiBuilder = SwingUIBuilder.create(test.getProcess());
                    test.assertThrows(() -> uiBuilder.setCreator((Class<UIElement>)null, (SwingUIBase uiBase) -> SwingUIButton.create(uiBase)),
                        new PreConditionFailure("uiType cannot be null."));
                });

                runner.test("with null type", (Test test) ->
                {
                    final SwingUIBuilder uiBuilder = SwingUIBuilder.create(test.getProcess());
                    test.assertThrows(() -> uiBuilder.setCreator((Class<UIElement>)null, (SwingUIBase uiBase) -> SwingUIButton.create(uiBase)),
                        new PreConditionFailure("uiType cannot be null."));
                });
            });
        });
    }
}
