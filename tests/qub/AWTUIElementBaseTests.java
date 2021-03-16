package qub;

public interface AWTUIElementBaseTests
{
    static AWTUIBase createUIBase(FakeDesktopProcess process)
    {
        PreCondition.assertNotNull(process, "process");

        return AWTUIBase.create(process.getDisplays().first(), process.getMainAsyncRunner(), process.getParallelAsyncRunner());
    }

    static AWTUIElementBase createUIElementBase(FakeDesktopProcess process)
    {
        return AWTUIElementBaseTests.createUIElementBase(process, new javax.swing.JButton());
    }

    static AWTUIElementBase createUIElementBase(FakeDesktopProcess process, java.awt.Component component)
    {
        PreCondition.assertNotNull(process, "process");
        PreCondition.assertNotNull(component, "component");

        final AWTUIBase uiBase = AWTUIElementBaseTests.createUIBase(process);
        return AWTUIElementBase.create(uiBase, component);
    }

    static void test(TestRunner runner)
    {
        runner.testGroup(AWTUIElementBase.class, () ->
        {
            runner.testGroup("create(AWTUIBase,java.awt.Component)", () ->
            {
                runner.test("with null uiBase", (Test test) ->
                {
                    final AWTUIBase uiBase = null;
                    final java.awt.Component component = new javax.swing.JButton();
                    test.assertThrows(() -> AWTUIElementBase.create(uiBase, component),
                        new PreConditionFailure("uiBase cannot be null."));
                });

                runner.test("with null component", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final AWTUIBase uiBase = AWTUIElementBaseTests.createUIBase(process);
                        final java.awt.Component component = null;
                        test.assertThrows(() -> AWTUIElementBase.create(uiBase, component),
                            new PreConditionFailure("component cannot be null."));
                    }
                });

                runner.test("with valid arguments", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final AWTUIBase uiBase = AWTUIElementBaseTests.createUIBase(process);
                        final java.awt.Component component = new javax.swing.JButton();
                        final AWTUIElementBase uiElementBase = AWTUIElementBase.create(uiBase, component);
                        test.assertNotNull(uiElementBase);
                        test.assertSame(uiBase, uiElementBase.getUIBase());
                        test.assertSame(component, uiElementBase.getComponent());
                    }
                });
            });

            runner.testGroup("scheduleAsyncTask(Action0)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final AWTUIElementBase uiElementBase = AWTUIElementBaseTests.createUIElementBase(process);
                        test.assertThrows(() -> uiElementBase.scheduleAsyncTask(null),
                            new PreConditionFailure("action cannot be null."));
                    }
                });

                runner.test("with non-null", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final AWTUIElementBase uiElementBase = AWTUIElementBaseTests.createUIElementBase(process);
                        final long currentThreadId = CurrentThread.getId();
                        final LongValue asyncThreadId = LongValue.create();

                        final Result<Void> asyncTask = uiElementBase.scheduleAsyncTask(() ->
                        {
                            asyncThreadId.set(CurrentThread.getId());
                        });

                        test.assertNotNull(asyncTask);
                        test.assertFalse(asyncThreadId.hasValue());

                        test.assertNull(asyncTask.await());

                        test.assertTrue(asyncThreadId.hasValue());
                        test.assertEqual(currentThreadId, asyncThreadId.get());
                    }
                });
            });

            runner.testGroup("setWidth(Distance)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final AWTUIElementBase uiElementBase = AWTUIElementBaseTests.createUIElementBase(process);
                        final Distance width = uiElementBase.getWidth();

                        test.assertThrows(() -> uiElementBase.setWidth((Distance)null),
                            new PreConditionFailure("width cannot be null."));

                        test.assertEqual(width, uiElementBase.getWidth());
                    }
                });

                runner.test("with negative", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final AWTUIElementBase uiElementBase = AWTUIElementBaseTests.createUIElementBase(process);
                        final Distance width = uiElementBase.getWidth();

                        test.assertThrows(() -> uiElementBase.setWidth(Distance.inches(-1)),
                            new PreConditionFailure("width (-1.0 Inches) must be greater than or equal to 0.0 Inches."));

                        test.assertEqual(width, uiElementBase.getWidth());
                    }
                });

                runner.test("with zero", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final AWTUIElementBase uiElementBase = AWTUIElementBaseTests.createUIElementBase(process);
                        final AWTUIElementBase setWidthResult = uiElementBase.setWidth(Distance.zero);
                        test.assertSame(uiElementBase, setWidthResult);
                        test.assertEqual(Distance.zero, uiElementBase.getWidth());
                    }
                });

                runner.test("with positive", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final AWTUIElementBase uiElementBase = AWTUIElementBaseTests.createUIElementBase(process);
                        final AWTUIElementBase setWidthResult = uiElementBase.setWidth(Distance.inches(2));
                        test.assertSame(uiElementBase, setWidthResult);
                        test.assertEqual(Distance.inches(2), uiElementBase.getWidth());
                    }
                });

                runner.test("with size changed listener and width set to same value", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final AWTUIElementBase uiElementBase = AWTUIElementBaseTests.createUIElementBase(process);

                        final IntegerValue eventCounter = IntegerValue.create(0);
                        final List<Integer> sizeChangedEvents = List.create();
                        final List<Integer> widthChangedEvents = List.create();
                        final List<Integer> heightChangedEvents = List.create();

                        uiElementBase.onSizeChanged(() -> sizeChangedEvents.add(eventCounter.incrementAndGet()));
                        uiElementBase.onWidthChanged(() -> widthChangedEvents.add(eventCounter.incrementAndGet()));
                        uiElementBase.onHeightChanged(() -> heightChangedEvents.add(eventCounter.incrementAndGet()));

                        uiElementBase.setWidth(uiElementBase.getWidth());

                        test.assertEqual(0, eventCounter.get());
                        test.assertEqual(Iterable.create(), sizeChangedEvents);
                        test.assertEqual(Iterable.create(), widthChangedEvents);
                        test.assertEqual(Iterable.create(), heightChangedEvents);
                    }
                });

                runner.test("with size changed listener and width set to different value", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final AWTUIElementBase uiElementBase = AWTUIElementBaseTests.createUIElementBase(process);

                        final IntegerValue eventCounter = IntegerValue.create(0);
                        final List<Integer> sizeChangedEvents = List.create();
                        final List<Integer> widthChangedEvents = List.create();
                        final List<Integer> heightChangedEvents = List.create();

                        uiElementBase.onSizeChanged(() -> sizeChangedEvents.add(eventCounter.incrementAndGet()));
                        uiElementBase.onWidthChanged(() -> widthChangedEvents.add(eventCounter.incrementAndGet()));
                        uiElementBase.onHeightChanged(() -> heightChangedEvents.add(eventCounter.incrementAndGet()));

                        uiElementBase.setWidth(uiElementBase.getWidth().plus(Distance.inches(1)));

                        test.assertEqual(2, eventCounter.get());
                        test.assertEqual(Iterable.create(1), sizeChangedEvents);
                        test.assertEqual(Iterable.create(2), widthChangedEvents);
                        test.assertEqual(Iterable.create(), heightChangedEvents);
                    }
                });
            });

            runner.testGroup("setWidthInPixels(int)", () ->
            {
                runner.test("with negative", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final AWTUIElementBase uiElementBase = AWTUIElementBaseTests.createUIElementBase(process);
                        final int widthInPixels = uiElementBase.getWidthInPixels();

                        test.assertThrows(() -> uiElementBase.setWidthInPixels(-1),
                            new PreConditionFailure("widthInPixels (-1) must be greater than or equal to 0."));

                        test.assertEqual(widthInPixels, uiElementBase.getWidthInPixels());
                    }
                });

                runner.test("with zero", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final AWTUIElementBase uiElementBase = AWTUIElementBaseTests.createUIElementBase(process);
                        final AWTUIElementBase setWidthResult = uiElementBase.setWidthInPixels(0);
                        test.assertSame(uiElementBase, setWidthResult);
                        test.assertEqual(0, uiElementBase.getWidthInPixels());
                    }
                });

                runner.test("with positive", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final AWTUIElementBase uiElementBase = AWTUIElementBaseTests.createUIElementBase(process);
                        final AWTUIElementBase setWidthResult = uiElementBase.setWidthInPixels(200);
                        test.assertSame(uiElementBase, setWidthResult);
                        test.assertEqual(200, uiElementBase.getWidthInPixels());
                    }
                });

                runner.test("with size changed listener and width set to same value", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final AWTUIElementBase uiElementBase = AWTUIElementBaseTests.createUIElementBase(process);

                        final IntegerValue eventCounter = IntegerValue.create(0);
                        final List<Integer> sizeChangedEvents = List.create();
                        final List<Integer> widthChangedEvents = List.create();
                        final List<Integer> heightChangedEvents = List.create();

                        uiElementBase.onSizeChanged(() -> sizeChangedEvents.add(eventCounter.incrementAndGet()));
                        uiElementBase.onWidthChanged(() -> widthChangedEvents.add(eventCounter.incrementAndGet()));
                        uiElementBase.onHeightChanged(() -> heightChangedEvents.add(eventCounter.incrementAndGet()));

                        uiElementBase.setWidthInPixels(uiElementBase.getWidthInPixels());

                        test.assertEqual(0, eventCounter.get());
                        test.assertEqual(Iterable.create(), sizeChangedEvents);
                        test.assertEqual(Iterable.create(), widthChangedEvents);
                        test.assertEqual(Iterable.create(), heightChangedEvents);
                    }
                });

                runner.test("with size changed listener and width set to different value", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final AWTUIElementBase uiElementBase = AWTUIElementBaseTests.createUIElementBase(process);

                        final IntegerValue eventCounter = IntegerValue.create(0);
                        final List<Integer> sizeChangedEvents = List.create();
                        final List<Integer> widthChangedEvents = List.create();
                        final List<Integer> heightChangedEvents = List.create();

                        uiElementBase.onSizeChanged(() -> sizeChangedEvents.add(eventCounter.incrementAndGet()));
                        uiElementBase.onWidthChanged(() -> widthChangedEvents.add(eventCounter.incrementAndGet()));
                        uiElementBase.onHeightChanged(() -> heightChangedEvents.add(eventCounter.incrementAndGet()));

                        uiElementBase.setWidthInPixels(uiElementBase.getWidthInPixels() + 100);

                        test.assertEqual(2, eventCounter.get());
                        test.assertEqual(Iterable.create(1), sizeChangedEvents);
                        test.assertEqual(Iterable.create(2), widthChangedEvents);
                        test.assertEqual(Iterable.create(), heightChangedEvents);
                    }
                });
            });

            runner.testGroup("setDynamicWidth(DynamicDistance)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final AWTUIElementBase uiElementBase = AWTUIElementBaseTests.createUIElementBase(process);
                        final Size2D size = uiElementBase.getSize();

                        test.assertThrows(() -> uiElementBase.setDynamicWidth(null),
                            new PreConditionFailure("dynamicWidth cannot be null."));

                        test.assertEqual(size, uiElementBase.getSize());
                    }
                });

                runner.test("with non-null", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final AWTUIElementBase uiElementBase = AWTUIElementBaseTests.createUIElementBase(process);
                        final Value<Distance> distance = Value.create(Distance.zero);
                        final RunnableEvent0 distanceChanged = Event0.create();
                        final DynamicDistance dynamicDistance = DynamicDistance.create(distance::get, distanceChanged::subscribe);

                        final IntegerValue eventCounter = IntegerValue.create(0);
                        final List<Integer> sizeChangedEvents = List.create();
                        final List<Integer> widthChangedEvents = List.create();
                        final List<Integer> heightChangedEvents = List.create();

                        uiElementBase.onSizeChanged(() -> sizeChangedEvents.add(eventCounter.incrementAndGet()));
                        uiElementBase.onWidthChanged(() -> widthChangedEvents.add(eventCounter.incrementAndGet()));
                        uiElementBase.onHeightChanged(() -> heightChangedEvents.add(eventCounter.incrementAndGet()));

                        final AWTUIElementBase setDynamicWidthResult = uiElementBase.setDynamicWidth(dynamicDistance);
                        test.assertSame(uiElementBase, setDynamicWidthResult);

                        test.assertEqual(Distance.zero, uiElementBase.getWidth());
                        test.assertEqual(Iterable.create(1), sizeChangedEvents);
                        test.assertEqual(Iterable.create(2), widthChangedEvents);
                        test.assertEqual(Iterable.create(), heightChangedEvents);
                        test.assertEqual(2, eventCounter.get());

                        distance.set(Distance.inches(1));
                        distanceChanged.run();

                        test.assertEqual(Distance.inches(1), uiElementBase.getWidth());
                        test.assertEqual(Iterable.create(1, 3), sizeChangedEvents);
                        test.assertEqual(Iterable.create(2, 4), widthChangedEvents);
                        test.assertEqual(Iterable.create(), heightChangedEvents);
                        test.assertEqual(4, eventCounter.get());

                        uiElementBase.setWidth(Distance.inches(2));
                        test.assertEqual(Distance.inches(2), uiElementBase.getWidth());
                        test.assertEqual(Iterable.create(1, 3, 5), sizeChangedEvents);
                        test.assertEqual(Iterable.create(2, 4, 6), widthChangedEvents);
                        test.assertEqual(Iterable.create(), heightChangedEvents);
                        test.assertEqual(6, eventCounter.get());

                        distance.set(Distance.inches(3));
                        distanceChanged.run();

                        test.assertEqual(Distance.inches(2), uiElementBase.getWidth());
                        test.assertEqual(Iterable.create(1, 3, 5), sizeChangedEvents);
                        test.assertEqual(Iterable.create(2, 4, 6), widthChangedEvents);
                        test.assertEqual(Iterable.create(), heightChangedEvents);
                        test.assertEqual(6, eventCounter.get());
                    }
                });

                runner.test("with equal dynamic width", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final AWTUIElementBase uiElementBase = AWTUIElementBaseTests.createUIElementBase(process);
                        final Value<Distance> distance = Value.create(uiElementBase.getWidth());
                        final RunnableEvent0 distanceChanged = Event0.create();
                        final DynamicDistance dynamicDistance = DynamicDistance.create(distance::get, distanceChanged::subscribe);

                        final IntegerValue eventCounter = IntegerValue.create(0);
                        final List<Integer> sizeChangedEvents = List.create();
                        final List<Integer> widthChangedEvents = List.create();
                        final List<Integer> heightChangedEvents = List.create();
                        uiElementBase.onSizeChanged(() -> sizeChangedEvents.add(eventCounter.incrementAndGet()));
                        uiElementBase.onWidthChanged(() -> widthChangedEvents.add(eventCounter.incrementAndGet()));
                        uiElementBase.onHeightChanged(() -> heightChangedEvents.add(eventCounter.incrementAndGet()));

                        final AWTUIElementBase setDynamicWidthResult = uiElementBase.setDynamicWidth(dynamicDistance);
                        test.assertSame(uiElementBase, setDynamicWidthResult);

                        test.assertEqual(distance.get(), uiElementBase.getWidth());
                        test.assertEqual(Iterable.create(), sizeChangedEvents);
                        test.assertEqual(Iterable.create(), widthChangedEvents);
                        test.assertEqual(Iterable.create(), heightChangedEvents);
                        test.assertEqual(0, eventCounter.get());

                        distance.set(Distance.inches(4));
                        distanceChanged.run();

                        test.assertEqual(Distance.inches(4), uiElementBase.getWidth());
                        test.assertEqual(Iterable.create(1), sizeChangedEvents);
                        test.assertEqual(Iterable.create(2), widthChangedEvents);
                        test.assertEqual(Iterable.create(), heightChangedEvents);
                        test.assertEqual(2, eventCounter.get());
                    }
                });
            });

            runner.testGroup("setHeight(Distance)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final AWTUIElementBase uiElementBase = AWTUIElementBaseTests.createUIElementBase(process);
                        final Distance height = uiElementBase.getHeight();

                        test.assertThrows(() -> uiElementBase.setHeight((Distance)null),
                            new PreConditionFailure("height cannot be null."));

                        test.assertEqual(height, uiElementBase.getHeight());
                    }
                });

                runner.test("with negative", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final AWTUIElementBase uiElementBase = AWTUIElementBaseTests.createUIElementBase(process);
                        final Distance height = uiElementBase.getHeight();

                        test.assertThrows(() -> uiElementBase.setHeight(Distance.inches(-1)),
                            new PreConditionFailure("height (-1.0 Inches) must be greater than or equal to 0.0 Inches."));

                        test.assertEqual(height, uiElementBase.getHeight());
                    }
                });

                runner.test("with zero", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final AWTUIElementBase uiElementBase = AWTUIElementBaseTests.createUIElementBase(process);
                        final AWTUIElementBase setHeightResult = uiElementBase.setHeight(Distance.zero);
                        test.assertSame(uiElementBase, setHeightResult);
                        test.assertEqual(Distance.zero, uiElementBase.getHeight());
                    }
                });

                runner.test("with positive", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final AWTUIElementBase uiElementBase = AWTUIElementBaseTests.createUIElementBase(process);
                        final AWTUIElementBase setHeightResult = uiElementBase.setHeight(Distance.inches(2));
                        test.assertSame(uiElementBase, setHeightResult);
                        test.assertEqual(Distance.inches(2), uiElementBase.getHeight());
                    }
                });

                runner.test("with size changed listener and width set to same value", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final AWTUIElementBase uiElementBase = AWTUIElementBaseTests.createUIElementBase(process);

                        final IntegerValue eventCounter = IntegerValue.create(0);
                        final List<Integer> sizeChangedEvents = List.create();
                        final List<Integer> widthChangedEvents = List.create();
                        final List<Integer> heightChangedEvents = List.create();
                        uiElementBase.onSizeChanged(() -> sizeChangedEvents.add(eventCounter.incrementAndGet()));
                        uiElementBase.onWidthChanged(() -> widthChangedEvents.add(eventCounter.incrementAndGet()));
                        uiElementBase.onHeightChanged(() -> heightChangedEvents.add(eventCounter.incrementAndGet()));

                        uiElementBase.setHeight(uiElementBase.getHeight());

                        test.assertEqual(Iterable.create(), sizeChangedEvents);
                        test.assertEqual(Iterable.create(), widthChangedEvents);
                        test.assertEqual(Iterable.create(), heightChangedEvents);
                        test.assertEqual(0, eventCounter.get());
                    }
                });

                runner.test("with size changed listener and width set to different value", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final AWTUIElementBase uiElementBase = AWTUIElementBaseTests.createUIElementBase(process);

                        final IntegerValue eventCounter = IntegerValue.create(0);
                        final List<Integer> sizeChangedEvents = List.create();
                        final List<Integer> widthChangedEvents = List.create();
                        final List<Integer> heightChangedEvents = List.create();
                        uiElementBase.onSizeChanged(() -> sizeChangedEvents.add(eventCounter.incrementAndGet()));
                        uiElementBase.onWidthChanged(() -> widthChangedEvents.add(eventCounter.incrementAndGet()));
                        uiElementBase.onHeightChanged(() -> heightChangedEvents.add(eventCounter.incrementAndGet()));

                        uiElementBase.setHeight(uiElementBase.getHeight().plus(Distance.inches(1)));

                        test.assertEqual(Iterable.create(1), sizeChangedEvents);
                        test.assertEqual(Iterable.create(), widthChangedEvents);
                        test.assertEqual(Iterable.create(2), heightChangedEvents);
                        test.assertEqual(2, eventCounter.get());
                    }
                });
            });

            runner.testGroup("setHeightInPixels(int)", () ->
            {
                runner.test("with negative", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final AWTUIElementBase uiElementBase = AWTUIElementBaseTests.createUIElementBase(process);
                        final int heightInPixels = uiElementBase.getHeightInPixels();

                        test.assertThrows(() -> uiElementBase.setHeightInPixels(-1),
                            new PreConditionFailure("heightInPixels (-1) must be greater than or equal to 0."));

                        test.assertEqual(heightInPixels, uiElementBase.getHeightInPixels());
                    }
                });

                runner.test("with zero", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final AWTUIElementBase uiElementBase = AWTUIElementBaseTests.createUIElementBase(process);
                        final AWTUIElementBase setHeightInPixelsResult = uiElementBase.setHeightInPixels(0);
                        test.assertSame(uiElementBase, setHeightInPixelsResult);
                        test.assertEqual(0, uiElementBase.getHeightInPixels());
                    }
                });

                runner.test("with positive", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final AWTUIElementBase uiElementBase = AWTUIElementBaseTests.createUIElementBase(process);
                        final AWTUIElementBase setHeightInPixelsResult = uiElementBase.setHeightInPixels(200);
                        test.assertSame(uiElementBase, setHeightInPixelsResult);
                        test.assertEqual(200, uiElementBase.getHeightInPixels());
                    }
                });

                runner.test("with size changed listener and height set to same value", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final AWTUIElementBase uiElementBase = AWTUIElementBaseTests.createUIElementBase(process);

                        final IntegerValue eventCounter = IntegerValue.create(0);
                        final List<Integer> sizeChangedEvents = List.create();
                        final List<Integer> widthChangedEvents = List.create();
                        final List<Integer> heightChangedEvents = List.create();
                        uiElementBase.onSizeChanged(() -> sizeChangedEvents.add(eventCounter.incrementAndGet()));
                        uiElementBase.onWidthChanged(() -> widthChangedEvents.add(eventCounter.incrementAndGet()));
                        uiElementBase.onHeightChanged(() -> heightChangedEvents.add(eventCounter.incrementAndGet()));

                        uiElementBase.setHeightInPixels(uiElementBase.getHeightInPixels());

                        test.assertEqual(Iterable.create(), sizeChangedEvents);
                        test.assertEqual(Iterable.create(), widthChangedEvents);
                        test.assertEqual(Iterable.create(), heightChangedEvents);
                        test.assertEqual(0, eventCounter.get());
                    }
                });

                runner.test("with size changed listener and height set to different value", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final AWTUIElementBase uiElementBase = AWTUIElementBaseTests.createUIElementBase(process);

                        final IntegerValue eventCounter = IntegerValue.create(0);
                        final List<Integer> sizeChangedEvents = List.create();
                        final List<Integer> widthChangedEvents = List.create();
                        final List<Integer> heightChangedEvents = List.create();
                        uiElementBase.onSizeChanged(() -> sizeChangedEvents.add(eventCounter.incrementAndGet()));
                        uiElementBase.onWidthChanged(() -> widthChangedEvents.add(eventCounter.incrementAndGet()));
                        uiElementBase.onHeightChanged(() -> heightChangedEvents.add(eventCounter.incrementAndGet()));

                        uiElementBase.setHeightInPixels(uiElementBase.getHeightInPixels() + 100);

                        test.assertEqual(Iterable.create(1), sizeChangedEvents);
                        test.assertEqual(Iterable.create(), widthChangedEvents);
                        test.assertEqual(Iterable.create(2), heightChangedEvents);
                        test.assertEqual(2, eventCounter.get());
                    }
                });
            });

            runner.testGroup("setDynamicHeight(DynamicDistance)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final AWTUIElementBase uiElementBase = AWTUIElementBaseTests.createUIElementBase(process);
                        final Size2D size = uiElementBase.getSize();

                        test.assertThrows(() -> uiElementBase.setDynamicHeight(null),
                            new PreConditionFailure("dynamicHeight cannot be null."));

                        test.assertEqual(size, uiElementBase.getSize());
                    }
                });

                runner.test("with non-null", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final AWTUIElementBase uiElementBase = AWTUIElementBaseTests.createUIElementBase(process);
                        final Value<Distance> distance = Value.create(Distance.zero);
                        final RunnableEvent0 distanceChanged = Event0.create();
                        final DynamicDistance dynamicDistance = DynamicDistance.create(distance::get, distanceChanged::subscribe);

                        final IntegerValue eventCounter = IntegerValue.create(0);
                        final List<Integer> sizeChangedEvents = List.create();
                        final List<Integer> widthChangedEvents = List.create();
                        final List<Integer> heightChangedEvents = List.create();
                        uiElementBase.onSizeChanged(() -> sizeChangedEvents.add(eventCounter.incrementAndGet()));
                        uiElementBase.onWidthChanged(() -> widthChangedEvents.add(eventCounter.incrementAndGet()));
                        uiElementBase.onHeightChanged(() -> heightChangedEvents.add(eventCounter.incrementAndGet()));

                        final AWTUIElementBase setDynamicHeightResult = uiElementBase.setDynamicHeight(dynamicDistance);
                        test.assertSame(uiElementBase, setDynamicHeightResult);

                        test.assertEqual(Distance.zero, uiElementBase.getHeight());
                        test.assertEqual(Iterable.create(1), sizeChangedEvents);
                        test.assertEqual(Iterable.create(), widthChangedEvents);
                        test.assertEqual(Iterable.create(2), heightChangedEvents);
                        test.assertEqual(2, eventCounter.get());

                        distance.set(Distance.inches(1));
                        distanceChanged.run();

                        test.assertEqual(Distance.inches(1), uiElementBase.getHeight());
                        test.assertEqual(Iterable.create(1, 3), sizeChangedEvents);
                        test.assertEqual(Iterable.create(), widthChangedEvents);
                        test.assertEqual(Iterable.create(2, 4), heightChangedEvents);
                        test.assertEqual(4, eventCounter.get());

                        uiElementBase.setHeight(Distance.inches(2));

                        test.assertEqual(Distance.inches(2), uiElementBase.getHeight());
                        test.assertEqual(Iterable.create(1, 3, 5), sizeChangedEvents);
                        test.assertEqual(Iterable.create(), widthChangedEvents);
                        test.assertEqual(Iterable.create(2, 4, 6), heightChangedEvents);
                        test.assertEqual(6, eventCounter.get());

                        distance.set(Distance.inches(3));
                        distanceChanged.run();

                        test.assertEqual(Distance.inches(2), uiElementBase.getHeight());
                        test.assertEqual(Iterable.create(1, 3, 5), sizeChangedEvents);
                        test.assertEqual(Iterable.create(), widthChangedEvents);
                        test.assertEqual(Iterable.create(2, 4, 6), heightChangedEvents);
                        test.assertEqual(6, eventCounter.get());
                    }
                });

                runner.test("with equal dynamic height", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final AWTUIElementBase uiElementBase = AWTUIElementBaseTests.createUIElementBase(process);
                        final Value<Distance> distance = Value.create(uiElementBase.getHeight());
                        final RunnableEvent0 distanceChanged = Event0.create();
                        final DynamicDistance dynamicDistance = DynamicDistance.create(distance::get, distanceChanged::subscribe);

                        final IntegerValue eventCounter = IntegerValue.create(0);
                        final List<Integer> sizeChangedEvents = List.create();
                        final List<Integer> widthChangedEvents = List.create();
                        final List<Integer> heightChangedEvents = List.create();
                        uiElementBase.onSizeChanged(() -> sizeChangedEvents.add(eventCounter.incrementAndGet()));
                        uiElementBase.onWidthChanged(() -> widthChangedEvents.add(eventCounter.incrementAndGet()));
                        uiElementBase.onHeightChanged(() -> heightChangedEvents.add(eventCounter.incrementAndGet()));

                        final AWTUIElementBase setDynamicHeightResult = uiElementBase.setDynamicHeight(dynamicDistance);
                        test.assertSame(uiElementBase, setDynamicHeightResult);

                        test.assertEqual(distance.get(), uiElementBase.getHeight());
                        test.assertEqual(Iterable.create(), sizeChangedEvents);
                        test.assertEqual(Iterable.create(), widthChangedEvents);
                        test.assertEqual(Iterable.create(), heightChangedEvents);
                        test.assertEqual(0, eventCounter.get());

                        distance.set(Distance.inches(4));
                        distanceChanged.run();

                        test.assertEqual(Distance.inches(4), uiElementBase.getHeight());
                        test.assertEqual(Iterable.create(1), sizeChangedEvents);
                        test.assertEqual(Iterable.create(), widthChangedEvents);
                        test.assertEqual(Iterable.create(2), heightChangedEvents);
                        test.assertEqual(2, eventCounter.get());
                    }
                });
            });

            runner.testGroup("component listener", () ->
            {
                runner.test("when width changes", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final javax.swing.JButton component = new javax.swing.JButton();
                        final AWTUIElementBase uiElementBase = AWTUIElementBaseTests.createUIElementBase(process, component);
                        final IntegerValue counter = IntegerValue.create(0);
                        final SpinGate gate = SpinGate.create();
                        uiElementBase.onSizeChanged(() ->
                        {
                            counter.increment();
                            gate.open();
                        });

                        final int componentHeight = component.getHeight();
                        component.setSize(300, componentHeight);
                        gate.passThrough(() -> process.getMainAsyncRunner().schedule(Action0.empty).await()).await();

                        test.assertEqual(1, counter.get());
                        test.assertEqual(300, uiElementBase.getWidthInPixels());
                        test.assertEqual(300, component.getWidth());
                        test.assertEqual(componentHeight, uiElementBase.getHeightInPixels());
                        test.assertEqual(componentHeight, component.getHeight());
                    }
                });

                runner.test("when height changes", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final javax.swing.JButton component = new javax.swing.JButton();
                        final AWTUIElementBase uiElementBase = AWTUIElementBaseTests.createUIElementBase(process, component);
                        final IntegerValue counter = IntegerValue.create(0);
                        final SpinGate gate = SpinGate.create();
                        uiElementBase.onSizeChanged(() ->
                        {
                            counter.increment();
                            gate.open();
                        });

                        final int componentWidth = component.getWidth();
                        component.setSize(componentWidth, 400);
                        gate.passThrough(() -> process.getMainAsyncRunner().schedule(Action0.empty).await()).await();

                        test.assertEqual(1, counter.get());
                        test.assertEqual(componentWidth, uiElementBase.getWidthInPixels());
                        test.assertEqual(componentWidth, component.getWidth());
                        test.assertEqual(400, uiElementBase.getHeightInPixels());
                        test.assertEqual(400, component.getHeight());
                    }
                });

                runner.test("when width and height changes", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final javax.swing.JButton component = new javax.swing.JButton();
                        final AWTUIElementBase uiElementBase = AWTUIElementBaseTests.createUIElementBase(process, component);
                        final IntegerValue counter = IntegerValue.create(0);
                        final SpinGate gate = SpinGate.create();
                        uiElementBase.onSizeChanged(() ->
                        {
                            counter.increment();
                            gate.open();
                        });

                        component.setSize(300, 400);
                        gate.passThrough(() -> process.getMainAsyncRunner().schedule(Action0.empty).await()).await();

                        test.assertEqual(1, counter.get());
                        test.assertEqual(300, uiElementBase.getWidthInPixels());
                        test.assertEqual(300, component.getWidth());
                        test.assertEqual(400, uiElementBase.getHeightInPixels());
                        test.assertEqual(400, component.getHeight());
                    }
                });
            });
        });
    }
}
