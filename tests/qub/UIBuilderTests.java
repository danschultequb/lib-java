package qub;

public interface UIBuilderTests
{
    static void test(TestRunner runner, Function1<FakeDesktopProcess,? extends UIBuilder> creator)
    {
        runner.testGroup(UIBuilder.class, () ->
        {
            runner.testGroup("setCreator(Class<U>,Function0<T>)", () ->
            {
                runner.test("with null uiElementType", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final UIBuilder uiBuilder = creator.run(process);
                        test.assertThrows(() -> uiBuilder.setCreator((Class<UIElement>)null, () -> null),
                            new PreConditionFailure("uiElementType cannot be null."));
                    }
                });

                runner.test("with null uiElementCreator", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final UIBuilder uiBuilder = creator.run(process);
                        test.assertThrows(() -> uiBuilder.setCreator(UIElement.class, null),
                            new PreConditionFailure("uiElementCreator cannot be null."));
                    }
                });
            });

            runner.testGroup("setCreator(Iterable<Class<U>>,Function0<T>)", () ->
            {
                runner.test("with null uiElementTypes", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final UIBuilder uiBuilder = creator.run(process);
                        test.assertThrows(() -> uiBuilder.setCreator((Iterable<Class<? extends UIText>>)null, () -> null),
                            new PreConditionFailure("uiElementTypes cannot be null."));
                    }
                });

                runner.test("with null uiElementCreator", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final UIBuilder uiBuilder = creator.run(process);
                        test.assertThrows(() -> uiBuilder.setCreator(Iterable.create(UIElement.class), null),
                            new PreConditionFailure("uiElementCreator cannot be null."));
                    }
                });
            });

            runner.testGroup("create(Class<? extends UIElement>)", () ->
            {
                final Action2<Class<? extends UIElement>,Throwable> createErrorTest = (Class<? extends UIElement> uiType, Throwable expected) ->
                {
                    runner.test("with " + Types.getTypeName(uiType), (Test test) ->
                    {
                        try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                        {
                            final UIBuilder uiBuilder = creator.run(process);
                            test.assertThrows(() -> uiBuilder.create(uiType).await(), expected);
                        }
                    });
                };

                createErrorTest.run(null, new PreConditionFailure("uiElementType cannot be null."));
                createErrorTest.run(UIElement.class, new NotFoundException("No UI creator function registered for UIElement type qub.UIElement."));

                runner.test("with UIButton", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final UIBuilder uiBuilder = creator.run(process);
                        final UIButton uiButton = uiBuilder.create(UIButton.class).await();
                        test.assertNotNull(uiButton);
                        test.assertEqual("", uiButton.getText());
                    }
                });

                runner.test("with UIText", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final UIBuilder uiBuilder = creator.run(process);
                        final UIText text = uiBuilder.create(UIText.class).await();
                        test.assertNotNull(text);
                        test.assertEqual("", text.getText());
                    }
                });

                runner.test("with UITextBox", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final UIBuilder uiBuilder = creator.run(process);
                        final UITextBox textBox = uiBuilder.create(UITextBox.class).await();
                        test.assertNotNull(textBox);
                        test.assertEqual("", textBox.getText());
                    }
                });

                runner.test("with UIVerticalLayout", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final UIBuilder uiBuilder = creator.run(process);
                        final UIVerticalLayout verticalLayout = uiBuilder.create(UIVerticalLayout.class).await();
                        test.assertNotNull(verticalLayout);
                    }
                });

                runner.test("with UIHorizontalLayout", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final UIBuilder uiBuilder = creator.run(process);
                        final UIHorizontalLayout horizontalLayout = uiBuilder.create(UIHorizontalLayout.class).await();
                        test.assertNotNull(horizontalLayout);
                    }
                });
            });

            runner.test("createUIButton()", (Test test) ->
            {
                try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                {
                    final UIBuilder uiBuilder = creator.run(process);
                    final UIButton button = uiBuilder.createUIButton().await();
                    test.assertNotNull(button);
                    test.assertEqual("", button.getText());
                }
            });

            runner.test("createUIText()", (Test test) ->
            {
                try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                {
                    final UIBuilder uiBuilder = creator.run(process);
                    final UIText text = uiBuilder.createUIText().await();
                    test.assertNotNull(text);
                    test.assertEqual("", text.getText());
                }
            });

            runner.test("createUITextBox()", (Test test) ->
            {
                try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                {
                    final UIBuilder uiBuilder = creator.run(process);
                    final UITextBox textBox = uiBuilder.createUITextBox().await();
                    test.assertNotNull(textBox);
                    test.assertEqual("", textBox.getText());
                }
            });

            runner.test("createUIVerticalLayout()", (Test test) ->
            {
                try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                {
                    final UIBuilder uiBuilder = creator.run(process);
                    final UIVerticalLayout verticalLayout = uiBuilder.createUIVerticalLayout().await();
                    test.assertNotNull(verticalLayout);
                }
            });

            runner.test("createUIHorizontalLayout()", (Test test) ->
            {
                try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                {
                    final UIBuilder uiBuilder = creator.run(process);
                    final UIHorizontalLayout horizontalLayout = uiBuilder.createUIHorizontalLayout().await();
                    test.assertNotNull(horizontalLayout);
                }
            });
        });
    }
}
