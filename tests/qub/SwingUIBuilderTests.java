package qub;

public interface SwingUIBuilderTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(SwingUIBuilder.class, () ->
        {
            runner.testGroup("create(Display,AsyncRunner)", () ->
            {
                runner.test("with null Display", (Test test) ->
                {
                    test.assertThrows(() -> SwingUIBuilder.create(null, test.getMainAsyncRunner()),
                        new PreConditionFailure("display cannot be null."));
                });

                runner.test("with null AsyncRunner", (Test test) ->
                {
                    test.assertThrows(() -> SwingUIBuilder.create(test.getDisplays().first(), null),
                        new PreConditionFailure("asyncRunner cannot be null."));
                });

                runner.test("with non-null values", (Test test) ->
                {
                    final SwingUIBuilder uiBuilder = SwingUIBuilder.create(test.getDisplays().first(), test.getMainAsyncRunner());
                    test.assertNotNull(uiBuilder);
                });
            });

            runner.testGroup("create(SwingUIBase)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> SwingUIBuilder.create((SwingUIBase)null),
                        new PreConditionFailure("uiBase cannot be null."));
                });

                runner.test("with non-null", (Test test) ->
                {
                    final SwingUIBase uiBase = SwingUIBase.create(test.getDisplays().first(), test.getMainAsyncRunner());
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

            runner.test("createUIWindow()", (Test test) ->
            {
                final SwingUIBuilder uiBuilder = SwingUIBuilder.create(test.getProcess());
                try (final SwingUIWindow window = uiBuilder.createUIWindow())
                {
                    test.assertNotNull(window);
                    test.assertFalse(window.isVisible());
                    test.assertEqual("", window.getTitle());
                }
            });

            runner.test("createUIButton()", (Test test) ->
            {
                final SwingUIBuilder uiBuilder = SwingUIBuilder.create(test.getProcess());
                final SwingUIButton button = uiBuilder.createUIButton();
                test.assertNotNull(button);
                test.assertEqual("", button.getText());
            });

            runner.test("createUIText()", (Test test) ->
            {
                final SwingUIBuilder uiBuilder = SwingUIBuilder.create(test.getProcess());
                final SwingUIText text = uiBuilder.createUIText();
                test.assertNotNull(text);
                test.assertEqual("", text.getText());
            });

            runner.test("createUITextBox()", (Test test) ->
            {
                final SwingUIBuilder uiBuilder = SwingUIBuilder.create(test.getProcess());
                final SwingUITextBox textBox = uiBuilder.createUITextBox();
                test.assertNotNull(textBox);
                test.assertEqual("", textBox.getText());
            });

            runner.test("createUIVerticalLayout()", (Test test) ->
            {
                final SwingUIBuilder uiBuilder = SwingUIBuilder.create(test.getProcess());
                final SwingUIVerticalLayout verticalLayout = uiBuilder.createUIVerticalLayout();
                test.assertNotNull(verticalLayout);
            });

            runner.test("createUIHorizontalLayout()", (Test test) ->
            {
                final SwingUIBuilder uiBuilder = SwingUIBuilder.create(test.getProcess());
                final SwingUIHorizontalLayout horizontalLayout = uiBuilder.createUIHorizontalLayout();
                test.assertNotNull(horizontalLayout);
            });
        });
    }
}
