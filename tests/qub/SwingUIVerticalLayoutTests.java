package qub;

public interface SwingUIVerticalLayoutTests
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

    static SwingUIVerticalLayout createUIVerticalLayout(Test test)
    {
        final SwingUIBuilder uiBuilder = SwingUIVerticalLayoutTests.createUIBuilder(test);
        return uiBuilder.create(SwingUIVerticalLayout.class).await();
    }

    static void test(TestRunner runner)
    {
        runner.testGroup(SwingUIVerticalLayout.class, () ->
        {
            UIVerticalLayoutTests.test(runner, SwingUIVerticalLayoutTests::createUIVerticalLayout);
            SwingUIElementTests.test(runner, SwingUIVerticalLayoutTests::createUIVerticalLayout);

            runner.testGroup("create(SwingUIBase)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> SwingUIVerticalLayout.create(null),
                        new PreConditionFailure("uiBase cannot be null."));
                });

                runner.test("with non-null", (Test test) ->
                {
                    final SwingUIBase uiBase = SwingUIVerticalLayoutTests.createUIBase(test);
                    final SwingUIVerticalLayout verticalLayout = SwingUIVerticalLayout.create(uiBase);
                    test.assertNotNull(verticalLayout);
                    test.assertEqual(Distance.zero, verticalLayout.getWidth());
                    test.assertEqual(Distance.zero, verticalLayout.getHeight());

                    final javax.swing.JPanel component = verticalLayout.getComponent();
                    test.assertNotNull(component);

                    final javax.swing.JPanel jComponent = verticalLayout.getJComponent();
                    test.assertNotNull(jComponent);
                    test.assertSame(component, jComponent);
                });
            });

            runner.testGroup("setWidth(Distance)", () ->
            {
                runner.test("should return " + Types.getTypeName(UIVerticalLayout.class), (Test test) ->
                {
                    final SwingUIVerticalLayout verticalLayout = SwingUIVerticalLayoutTests.createUIVerticalLayout(test);
                    final SwingUIVerticalLayout setWidthResult = verticalLayout.setWidth(Distance.inches(1));
                    test.assertSame(verticalLayout, setWidthResult);
                });
            });

            runner.testGroup("setHeight(Distance)", () ->
            {
                runner.test("should return " + Types.getTypeName(SwingUIVerticalLayout.class), (Test test) ->
                {
                    final SwingUIVerticalLayout verticalLayout = SwingUIVerticalLayoutTests.createUIVerticalLayout(test);
                    final SwingUIVerticalLayout setHeightResult = verticalLayout.setHeight(Distance.inches(1));
                    test.assertSame(verticalLayout, setHeightResult);
                });
            });

            runner.testGroup("setSize(Size2D)", () ->
            {
                runner.test("should return " + Types.getTypeName(SwingUIVerticalLayout.class), (Test test) ->
                {
                    final SwingUIVerticalLayout verticalLayout = SwingUIVerticalLayoutTests.createUIVerticalLayout(test);
                    final SwingUIVerticalLayout setHeightResult = verticalLayout.setSize(Size2D.create(Distance.inches(2), Distance.inches(3)));
                    test.assertSame(verticalLayout, setHeightResult);
                });
            });

            runner.testGroup("setSize(Distance,Distance)", () ->
            {
                runner.test("should return " + Types.getTypeName(SwingUIVerticalLayout.class), (Test test) ->
                {
                    final SwingUIVerticalLayout verticalLayout = SwingUIVerticalLayoutTests.createUIVerticalLayout(test);
                    final SwingUIVerticalLayout setHeightResult = verticalLayout.setSize(Distance.inches(2), Distance.inches(3));
                    test.assertSame(verticalLayout, setHeightResult);
                });
            });

            runner.testGroup("add(UIElement)", () ->
            {
                runner.test("should return " + Types.getTypeName(SwingUIVerticalLayout.class), (Test test) ->
                {
                    final SwingUIVerticalLayout verticalLayout = SwingUIVerticalLayoutTests.createUIVerticalLayout(test);
                    final SwingUIVerticalLayout element = SwingUIVerticalLayoutTests.createUIVerticalLayout(test);

                    final SwingUIVerticalLayout addResult = verticalLayout.add(element);
                    test.assertSame(verticalLayout, addResult);
                    test.assertEqual(
                        Map.<AWTUIElement,Point2D>create()
                            .set(element, Point2D.zero),
                        verticalLayout.getElementPositions());
                    test.assertEqual(Point2D.zero, verticalLayout.getElementPosition(element).await());
                    test.assertEqual(Size2D.zero, verticalLayout.getSize());
                    test.assertEqual(Size2D.zero, element.getSize());
                });

                runner.test("with one non-empty sized element with left horizontal alignment", (Test test) ->
                {
                    final SwingUIBuilder uiBuilder = SwingUIVerticalLayoutTests.createUIBuilder(test);
                    final SwingUIVerticalLayout verticalLayout = uiBuilder.create(SwingUIVerticalLayout.class).await()
                        .setElementHorizontalAlignment(HorizontalAlignment.Left);
                    final SwingUIButton uiButton = uiBuilder.create(SwingUIButton.class).await()
                        .setText("Hello!");
                    verticalLayout.add(uiButton);

                    test.assertEqual(Size2D.create(Distance.inches(0.65), Distance.inches(0.26)), verticalLayout.getSize());
                    test.assertEqual(Point2D.zero, verticalLayout.getElementPosition(uiButton).await());
                    test.assertEqual(
                        Map.<AWTUIElement,Point2D>create()
                            .set(uiButton, Point2D.zero),
                        verticalLayout.getElementPositions());
                    test.assertEqual(Size2D.create(Distance.inches(0.65), Distance.inches(0.26)), uiButton.getSize());
                });

                runner.test("with one non-empty sized element with center horizontal alignment", (Test test) ->
                {
                    final SwingUIBuilder uiBuilder = SwingUIVerticalLayoutTests.createUIBuilder(test);
                    final SwingUIVerticalLayout verticalLayout = uiBuilder.create(SwingUIVerticalLayout.class).await()
                        .setElementHorizontalAlignment(HorizontalAlignment.Center);
                    final SwingUIButton uiButton = uiBuilder.create(SwingUIButton.class).await()
                        .setText("Hello!");
                    verticalLayout.add(uiButton);

                    test.assertEqual(Size2D.create(Distance.inches(0.65), Distance.inches(0.26)), verticalLayout.getSize());
                    test.assertEqual(Point2D.zero, verticalLayout.getElementPosition(uiButton).await());
                    test.assertEqual(
                        Map.<AWTUIElement,Point2D>create()
                            .set(uiButton, Point2D.zero),
                        verticalLayout.getElementPositions());
                    test.assertEqual(Size2D.create(Distance.inches(0.65), Distance.inches(0.26)), uiButton.getSize());
                });

                runner.test("with one non-empty sized element with right horizontal alignment", (Test test) ->
                {
                    final SwingUIBuilder uiBuilder = SwingUIVerticalLayoutTests.createUIBuilder(test);
                    final SwingUIVerticalLayout verticalLayout = uiBuilder.create(SwingUIVerticalLayout.class).await()
                        .setElementHorizontalAlignment(HorizontalAlignment.Right);
                    final SwingUIButton uiButton = uiBuilder.create(SwingUIButton.class).await()
                        .setText("Hello!");
                    verticalLayout.add(uiButton);

                    test.assertEqual(Size2D.create(Distance.inches(0.65), Distance.inches(0.26)), verticalLayout.getSize());
                    test.assertEqual(Point2D.zero, verticalLayout.getElementPosition(uiButton).await());
                    test.assertEqual(
                        Map.<AWTUIElement,Point2D>create()
                            .set(uiButton, Point2D.zero),
                        verticalLayout.getElementPositions());
                    test.assertEqual(Size2D.create(Distance.inches(0.65), Distance.inches(0.26)), uiButton.getSize());
                });



                runner.test("with two non-empty sized elements with left horizontal alignment", (Test test) ->
                {
                    final SwingUIBuilder uiBuilder = SwingUIVerticalLayoutTests.createUIBuilder(test);
                    final SwingUIVerticalLayout verticalLayout = uiBuilder.create(SwingUIVerticalLayout.class).await()
                        .setElementHorizontalAlignment(HorizontalAlignment.Left);

                    final SwingUIButton uiButton1 = uiBuilder.create(SwingUIButton.class).await()
                        .setText("Hello!");
                    verticalLayout.add(uiButton1);

                    final SwingUIButton uiButton2 = uiBuilder.create(SwingUIButton.class).await()
                        .setText("I'm a button!");
                    verticalLayout.add(uiButton2);

                    test.assertEqual(Size2D.create(Distance.inches(1.03), Distance.inches(0.52)), verticalLayout.getSize());
                    test.assertEqual(Point2D.zero, verticalLayout.getElementPosition(uiButton1).await());
                    test.assertEqual(Point2D.create(Distance.zero, Distance.inches(0.26)), verticalLayout.getElementPosition(uiButton2).await());
                    test.assertEqual(
                        Map.<AWTUIElement,Point2D>create()
                            .set(uiButton1, Point2D.zero)
                            .set(uiButton2, Point2D.create(Distance.zero, Distance.inches(0.26))),
                        verticalLayout.getElementPositions());
                    test.assertEqual(Size2D.create(Distance.inches(0.65), Distance.inches(0.26)), uiButton1.getSize());
                    test.assertEqual(Size2D.create(Distance.inches(1.03), Distance.inches(0.26)), uiButton2.getSize());
                });

                runner.test("with one non-empty sized element with center horizontal alignment", (Test test) ->
                {
                    final SwingUIBuilder uiBuilder = SwingUIVerticalLayoutTests.createUIBuilder(test);
                    final SwingUIVerticalLayout verticalLayout = uiBuilder.create(SwingUIVerticalLayout.class).await()
                        .setElementHorizontalAlignment(HorizontalAlignment.Center);

                    final SwingUIButton uiButton1 = uiBuilder.create(SwingUIButton.class).await()
                        .setText("Hello!");
                    verticalLayout.add(uiButton1);

                    final SwingUIButton uiButton2 = uiBuilder.create(SwingUIButton.class).await()
                        .setText("I'm a button!");
                    verticalLayout.add(uiButton2);

                    test.assertEqual(Size2D.create(Distance.inches(1.03), Distance.inches(0.52)), verticalLayout.getSize());
                    test.assertEqual(Point2D.create(Distance.inches(0.19), Distance.zero), verticalLayout.getElementPosition(uiButton1).await());
                    test.assertEqual(Point2D.create(Distance.zero, Distance.inches(0.26)), verticalLayout.getElementPosition(uiButton2).await());
                    test.assertEqual(
                        Map.<AWTUIElement,Point2D>create()
                            .set(uiButton1, Point2D.create(Distance.inches(0.19), Distance.zero))
                            .set(uiButton2, Point2D.create(Distance.zero, Distance.inches(0.26))),
                        verticalLayout.getElementPositions());
                    test.assertEqual(Size2D.create(Distance.inches(0.65), Distance.inches(0.26)), uiButton1.getSize());
                    test.assertEqual(Size2D.create(Distance.inches(1.03), Distance.inches(0.26)), uiButton2.getSize());
                });

                runner.test("with one non-empty sized element with right horizontal alignment", (Test test) ->
                {
                    final SwingUIBuilder uiBuilder = SwingUIVerticalLayoutTests.createUIBuilder(test);
                    final SwingUIVerticalLayout verticalLayout = uiBuilder.create(SwingUIVerticalLayout.class).await()
                        .setElementHorizontalAlignment(HorizontalAlignment.Right);

                    final SwingUIButton uiButton1 = uiBuilder.create(SwingUIButton.class).await()
                        .setText("Hello!");
                    verticalLayout.add(uiButton1);

                    final SwingUIButton uiButton2 = uiBuilder.create(SwingUIButton.class).await()
                        .setText("I'm a button!");
                    verticalLayout.add(uiButton2);

                    test.assertEqual(Size2D.create(Distance.inches(1.03), Distance.inches(0.52)), verticalLayout.getSize());
                    test.assertEqual(Point2D.create(Distance.inches(0.38), Distance.zero), verticalLayout.getElementPosition(uiButton1).await());
                    test.assertEqual(Point2D.create(Distance.zero, Distance.inches(0.26)), verticalLayout.getElementPosition(uiButton2).await());
                    test.assertEqual(
                        Map.<AWTUIElement,Point2D>create()
                            .set(uiButton1, Point2D.create(Distance.inches(0.38), Distance.zero))
                            .set(uiButton2, Point2D.create(Distance.zero, Distance.inches(0.26))),
                        verticalLayout.getElementPositions());
                    test.assertEqual(Size2D.create(Distance.inches(0.65), Distance.inches(0.26)), uiButton1.getSize());
                    test.assertEqual(Size2D.create(Distance.inches(1.03), Distance.inches(0.26)), uiButton2.getSize());
                });
            });

            runner.testGroup("addAll(UIElement...)", () ->
            {
                runner.test("should return " + Types.getTypeName(SwingUIVerticalLayout.class), (Test test) ->
                {
                    final SwingUIVerticalLayout verticalLayout = SwingUIVerticalLayoutTests.createUIVerticalLayout(test);

                    final SwingUIVerticalLayout addAllResult = verticalLayout.addAll();
                    test.assertSame(verticalLayout, addAllResult);
                });
            });

            runner.testGroup("addAll(Iterable<? extends UIElement>)", () ->
            {
                runner.test("should return " + Types.getTypeName(SwingUIVerticalLayout.class), (Test test) ->
                {
                    final SwingUIVerticalLayout verticalLayout = SwingUIVerticalLayoutTests.createUIVerticalLayout(test);

                    final SwingUIVerticalLayout addResult = verticalLayout.addAll(Iterable.create());
                    test.assertSame(verticalLayout, addResult);
                    test.assertEqual(Map.create(), verticalLayout.getElementPositions());
                });
            });

            runner.testGroup("getElementPosition(AWTUIElement)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final SwingUIVerticalLayout verticalLayout = SwingUIVerticalLayoutTests.createUIVerticalLayout(test);
                    test.assertThrows(() -> verticalLayout.getElementPosition(null),
                        new PreConditionFailure("awtUiElement cannot be null."));
                });

                runner.test("with not found UIElement", (Test test) ->
                {
                    final SwingUIVerticalLayout verticalLayout = SwingUIVerticalLayoutTests.createUIVerticalLayout(test);
                    test.assertThrows(() -> verticalLayout.getElementPosition(verticalLayout).await(),
                        new NotFoundException(verticalLayout + " doesn't contain " + verticalLayout + ", so it can't get it's relative position."));
                });
            });

            runner.testGroup("setElementHorizontalAlignment(HorizontalAlignment)", () ->
            {
                runner.test("should return " + Types.getTypeName(SwingUIVerticalLayout.class), (Test test) ->
                {
                    final SwingUIVerticalLayout verticalLayout = SwingUIVerticalLayoutTests.createUIVerticalLayout(test);
                    final SwingUIVerticalLayout setElementHorizontalAlignmentResult = verticalLayout.setElementHorizontalAlignment(HorizontalAlignment.Center);
                    test.assertSame(verticalLayout, setElementHorizontalAlignmentResult);
                    test.assertEqual(HorizontalAlignment.Center, verticalLayout.getElementHorizontalAlignment());
                });
            });

            runner.testGroup("setElementVerticalAlignment(VerticalAlignment)", () ->
            {
                runner.test("should return " + Types.getTypeName(SwingUIVerticalLayout.class), (Test test) ->
                {
                    final SwingUIVerticalLayout verticalLayout = SwingUIVerticalLayoutTests.createUIVerticalLayout(test);
                    final SwingUIVerticalLayout setElementVerticalAlignmentResult = verticalLayout.setElementVerticalAlignment(VerticalAlignment.Center);
                    test.assertSame(verticalLayout, setElementVerticalAlignmentResult);
                    test.assertEqual(VerticalAlignment.Center, verticalLayout.getElementVerticalAlignment());
                });
            });

            runner.testGroup("in window", () ->
            {
                runner.test("with labels", runner.skip(), (Test test) ->
                {
                    final SwingUIBuilder uiBuilder = SwingUIBuilder.create(test.getProcess());
                    try (final SwingUIWindow window = uiBuilder.createSwingUIWindow().await())
                    {
                        window.setTitle(test.getFullName());
                        window.setContentSize(Distance.inches(1), Distance.inches(3));

                        final SwingUIVerticalLayout verticalLayout = uiBuilder.create(SwingUIVerticalLayout.class).await();
                        for (int i = 1; i <= 3; ++i)
                        {
                            verticalLayout.add(uiBuilder.createUIText().await()
                                .setFontSize(Distance.inches(0.5))
                                .setText(Integers.toString(i))
                                .setSize(Distance.inches(1), Distance.inches(1)));
                        }
                        window.setContent(verticalLayout);

                        window.setVisible(true);
                        window.await();
                    }
                    test.fail();
                });

                runner.test("with alignment", runner.skip(), (Test test) ->
                {
                    final SwingUIBuilder uiBuilder = SwingUIBuilder.create(test.getProcess());
                    try (final SwingUIWindow window = uiBuilder.createSwingUIWindow().await())
                    {
                        window.setTitle(test.getFullName());
                        window.setContentSize(Distance.inches(3), Distance.inches(3));

                        final SwingUIVerticalLayout verticalLayout = uiBuilder.create(SwingUIVerticalLayout.class).await();

                        final SwingUIHorizontalLayout horizontalAlignmentLayout = uiBuilder.create(SwingUIHorizontalLayout.class).await();
                        for (final HorizontalAlignment horizontalAlignment : HorizontalAlignment.values())
                        {
                            final SwingUIButton button = uiBuilder.create(SwingUIButton.class).await()
                                .setText(horizontalAlignment.toString())
                                .setWidth(Distance.inches(1));
                            button.onClick(() ->
                            {
                                verticalLayout.setElementHorizontalAlignment(horizontalAlignment);
                            });
                            horizontalAlignmentLayout.add(button);
                        }
                        verticalLayout.add(horizontalAlignmentLayout);

                        final SwingUIHorizontalLayout verticalAlignmentLayout = uiBuilder.create(SwingUIHorizontalLayout.class).await();
                        for (final VerticalAlignment verticalAlignment : VerticalAlignment.values())
                        {
                            final SwingUIButton button = uiBuilder.create(SwingUIButton.class).await()
                                .setText(verticalAlignment.toString())
                                .setWidth(Distance.inches(1));
                            button.onClick(() ->
                            {
                                verticalLayout.setElementVerticalAlignment(verticalAlignment);
                            });
                            verticalAlignmentLayout.add(button);
                        }
                        verticalLayout.add(verticalAlignmentLayout);

                        for (int i = 1; i <= 3; ++i)
                        {
                            verticalLayout.add(uiBuilder.createUIText().await()
                                .setFontSize(Distance.inches(0.5))
                                .setText(Integers.toString(i))
                                .setSize(Distance.inches(1), Distance.inches(1)));
                        }

                        window.setContent(verticalLayout);

                        window.setVisible(true);
                        window.await();
                    }
                    test.fail();
                });
            });
        });
    }
}
