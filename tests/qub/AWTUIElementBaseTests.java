package qub;

public interface AWTUIElementBaseTests
{
    static AWTUIElementBase createUIElementBase(Test test)
    {
        return AWTUIElementBaseTests.createUIElementBase(test, new javax.swing.JButton());
    }

    static AWTUIElementBase createUIElementBase(Test test, java.awt.Component component)
    {
        PreCondition.assertNotNull(test, "test");
        PreCondition.assertNotNull(component, "component");

        final AWTUIBase uiBase = AWTUIBase.create(test.getProcess());
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
                    final AWTUIBase uiBase = AWTUIBase.create(test.getProcess());
                    final java.awt.Component component = null;
                    test.assertThrows(() -> AWTUIElementBase.create(uiBase, component),
                        new PreConditionFailure("component cannot be null."));
                });

                runner.test("with valid arguments", (Test test) ->
                {
                    final AWTUIBase uiBase = AWTUIBase.create(test.getProcess());
                    final java.awt.Component component = new javax.swing.JButton();
                    final AWTUIElementBase uiElementBase = AWTUIElementBase.create(uiBase, component);
                    test.assertNotNull(uiElementBase);
                    test.assertSame(uiBase, uiElementBase.getUIBase());
                    test.assertSame(component, uiElementBase.getComponent());
                });
            });

            runner.testGroup("scheduleAsyncTask(Action0)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final AWTUIElementBase uiElementBase = AWTUIElementBaseTests.createUIElementBase(test);
                    test.assertThrows(() -> uiElementBase.scheduleAsyncTask(null),
                        new PreConditionFailure("action cannot be null."));
                });

                runner.test("with non-null", (Test test) ->
                {
                    final AWTUIElementBase uiElementBase = AWTUIElementBaseTests.createUIElementBase(test);
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
                });
            });

            runner.testGroup("setWidth(Distance)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final AWTUIElementBase uiElementBase = AWTUIElementBaseTests.createUIElementBase(test);
                    final Distance width = uiElementBase.getWidth();

                    test.assertThrows(() -> uiElementBase.setWidth((Distance)null),
                        new PreConditionFailure("width cannot be null."));

                    test.assertEqual(width, uiElementBase.getWidth());
                });

                runner.test("with negative", (Test test) ->
                {
                    final AWTUIElementBase uiElementBase = AWTUIElementBaseTests.createUIElementBase(test);
                    final Distance width = uiElementBase.getWidth();

                    test.assertThrows(() -> uiElementBase.setWidth(Distance.inches(-1)),
                        new PreConditionFailure("width (-1.0 Inches) must be greater than or equal to 0.0 Inches."));

                    test.assertEqual(width, uiElementBase.getWidth());
                });

                runner.test("with zero", (Test test) ->
                {
                    final AWTUIElementBase uiElementBase = AWTUIElementBaseTests.createUIElementBase(test);
                    final AWTUIElementBase setWidthResult = uiElementBase.setWidth(Distance.zero);
                    test.assertSame(uiElementBase, setWidthResult);
                    test.assertEqual(Distance.zero, uiElementBase.getWidth());
                });

                runner.test("with positive", (Test test) ->
                {
                    final AWTUIElementBase uiElementBase = AWTUIElementBaseTests.createUIElementBase(test);
                    final AWTUIElementBase setWidthResult = uiElementBase.setWidth(Distance.inches(2));
                    test.assertSame(uiElementBase, setWidthResult);
                    test.assertEqual(Distance.inches(2), uiElementBase.getWidth());
                });

                runner.test("with size changed listener and width set to same value", (Test test) ->
                {
                    final AWTUIElementBase uiElementBase = AWTUIElementBaseTests.createUIElementBase(test);

                    final IntegerValue counter = IntegerValue.create(0);
                    uiElementBase.onSizeChanged(counter::increment);

                    uiElementBase.setWidth(uiElementBase.getWidth());

                    test.assertEqual(0, counter.get());
                });

                runner.test("with size changed listener and width set to different value", (Test test) ->
                {
                    final AWTUIElementBase uiElementBase = AWTUIElementBaseTests.createUIElementBase(test);

                    final IntegerValue counter = IntegerValue.create(0);
                    uiElementBase.onSizeChanged(counter::increment);

                    uiElementBase.setWidth(uiElementBase.getWidth().plus(Distance.inches(1)));

                    test.assertEqual(1, counter.get());
                });
            });

            runner.testGroup("setWidthInPixels(int)", () ->
            {
                runner.test("with negative", (Test test) ->
                {
                    final AWTUIElementBase uiElementBase = AWTUIElementBaseTests.createUIElementBase(test);
                    final int widthInPixels = uiElementBase.getWidthInPixels();

                    test.assertThrows(() -> uiElementBase.setWidthInPixels(-1),
                        new PreConditionFailure("widthInPixels (-1) must be greater than or equal to 0."));

                    test.assertEqual(widthInPixels, uiElementBase.getWidthInPixels());
                });

                runner.test("with zero", (Test test) ->
                {
                    final AWTUIElementBase uiElementBase = AWTUIElementBaseTests.createUIElementBase(test);
                    final AWTUIElementBase setWidthResult = uiElementBase.setWidthInPixels(0);
                    test.assertSame(uiElementBase, setWidthResult);
                    test.assertEqual(0, uiElementBase.getWidthInPixels());
                });

                runner.test("with positive", (Test test) ->
                {
                    final AWTUIElementBase uiElementBase = AWTUIElementBaseTests.createUIElementBase(test);
                    final AWTUIElementBase setWidthResult = uiElementBase.setWidthInPixels(200);
                    test.assertSame(uiElementBase, setWidthResult);
                    test.assertEqual(200, uiElementBase.getWidthInPixels());
                });

                runner.test("with size changed listener and width set to same value", (Test test) ->
                {
                    final AWTUIElementBase uiElementBase = AWTUIElementBaseTests.createUIElementBase(test);

                    final IntegerValue counter = IntegerValue.create(0);
                    uiElementBase.onSizeChanged(counter::increment);

                    uiElementBase.setWidthInPixels(uiElementBase.getWidthInPixels());

                    test.assertEqual(0, counter.get());
                });

                runner.test("with size changed listener and width set to different value", (Test test) ->
                {
                    final AWTUIElementBase uiElementBase = AWTUIElementBaseTests.createUIElementBase(test);

                    final IntegerValue counter = IntegerValue.create(0);
                    uiElementBase.onSizeChanged(counter::increment);

                    uiElementBase.setWidthInPixels(uiElementBase.getWidthInPixels() + 100);

                    test.assertEqual(1, counter.get());
                });
            });

            runner.testGroup("setDynamicWidth(DynamicDistance)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final AWTUIElementBase uiElementBase = AWTUIElementBaseTests.createUIElementBase(test);
                    final Size2D size = uiElementBase.getSize();

                    test.assertThrows(() -> uiElementBase.setDynamicWidth(null),
                        new PreConditionFailure("dynamicWidth cannot be null."));

                    test.assertEqual(size, uiElementBase.getSize());
                });

                runner.test("with non-null", (Test test) ->
                {
                    final AWTUIElementBase uiElementBase = AWTUIElementBaseTests.createUIElementBase(test);
                    final Value<Distance> distance = Value.create(Distance.zero);
                    final RunnableEvent0 distanceChanged = Event0.create();
                    final DynamicDistance dynamicDistance = DynamicDistance.create(distance::get, distanceChanged::subscribe);

                    final IntegerValue eventCounter = IntegerValue.create(0);
                    final List<Integer> sizeChangedEvents = List.create();
                    uiElementBase.onSizeChanged(() -> sizeChangedEvents.add(eventCounter.incrementAndGet()));

                    final AWTUIElementBase setDynamicWidthResult = uiElementBase.setDynamicWidth(dynamicDistance);
                    test.assertSame(uiElementBase, setDynamicWidthResult);

                    test.assertEqual(Distance.zero, uiElementBase.getWidth());
                    test.assertEqual(Iterable.create(1), sizeChangedEvents);
                    test.assertEqual(1, eventCounter.get());

                    distance.set(Distance.inches(1));
                    distanceChanged.run();

                    test.assertEqual(Distance.inches(1), uiElementBase.getWidth());
                    test.assertEqual(Iterable.create(1, 2), sizeChangedEvents);
                    test.assertEqual(2, eventCounter.get());

                    uiElementBase.setWidth(Distance.inches(2));
                    test.assertEqual(Distance.inches(2), uiElementBase.getWidth());
                    test.assertEqual(Iterable.create(1, 2, 3), sizeChangedEvents);
                    test.assertEqual(3, eventCounter.get());

                    distance.set(Distance.inches(3));
                    distanceChanged.run();

                    test.assertEqual(Distance.inches(2), uiElementBase.getWidth());
                    test.assertEqual(Iterable.create(1, 2, 3), sizeChangedEvents);
                    test.assertEqual(3, eventCounter.get());
                });

                runner.test("with equal dynamic width", (Test test) ->
                {
                    final AWTUIElementBase uiElementBase = AWTUIElementBaseTests.createUIElementBase(test);
                    final Value<Distance> distance = Value.create(uiElementBase.getWidth());
                    final RunnableEvent0 distanceChanged = Event0.create();
                    final DynamicDistance dynamicDistance = DynamicDistance.create(distance::get, distanceChanged::subscribe);

                    final IntegerValue eventCounter = IntegerValue.create(0);
                    final List<Integer> sizeChangedEvents = List.create();
                    uiElementBase.onSizeChanged(() -> sizeChangedEvents.add(eventCounter.incrementAndGet()));

                    final AWTUIElementBase setDynamicWidthResult = uiElementBase.setDynamicWidth(dynamicDistance);
                    test.assertSame(uiElementBase, setDynamicWidthResult);

                    test.assertEqual(distance.get(), uiElementBase.getWidth());
                    test.assertEqual(Iterable.create(), sizeChangedEvents);
                    test.assertEqual(0, eventCounter.get());

                    distance.set(Distance.inches(4));
                    distanceChanged.run();

                    test.assertEqual(Distance.inches(4), uiElementBase.getWidth());
                    test.assertEqual(Iterable.create(1), sizeChangedEvents);
                    test.assertEqual(1, eventCounter.get());
                });
            });

            runner.testGroup("component listener", () ->
            {
                runner.test("when width changes", (Test test) ->
                {
                    final javax.swing.JButton component = new javax.swing.JButton();
                    final AWTUIElementBase uiElementBase = AWTUIElementBaseTests.createUIElementBase(test, component);
                    final IntegerValue counter = IntegerValue.create(0);
                    final SpinGate gate = SpinGate.create(test.getClock());
                    uiElementBase.onSizeChanged(() ->
                    {
                        counter.increment();
                        gate.open();
                    });

                    final int componentHeight = component.getHeight();
                    component.setSize(300, componentHeight);
                    gate.passThrough(Duration.seconds(5), () -> test.getMainAsyncRunner().schedule(Action0.empty).await()).await();

                    test.assertEqual(1, counter.get());
                    test.assertEqual(300, uiElementBase.getWidthInPixels());
                    test.assertEqual(300, component.getWidth());
                    test.assertEqual(componentHeight, uiElementBase.getHeightInPixels());
                    test.assertEqual(componentHeight, component.getHeight());
                });

                runner.test("when height changes", (Test test) ->
                {
                    final javax.swing.JButton component = new javax.swing.JButton();
                    final AWTUIElementBase uiElementBase = AWTUIElementBaseTests.createUIElementBase(test, component);
                    final IntegerValue counter = IntegerValue.create(0);
                    final SpinGate gate = SpinGate.create(test.getClock());
                    uiElementBase.onSizeChanged(() ->
                    {
                        counter.increment();
                        gate.open();
                    });

                    final int componentWidth = component.getWidth();
                    component.setSize(componentWidth, 400);
                    gate.passThrough(Duration.seconds(5), () -> test.getMainAsyncRunner().schedule(Action0.empty).await()).await();

                    test.assertEqual(1, counter.get());
                    test.assertEqual(componentWidth, uiElementBase.getWidthInPixels());
                    test.assertEqual(componentWidth, component.getWidth());
                    test.assertEqual(400, uiElementBase.getHeightInPixels());
                    test.assertEqual(400, component.getHeight());
                });

                runner.test("when width and height changes", (Test test) ->
                {
                    final javax.swing.JButton component = new javax.swing.JButton();
                    final AWTUIElementBase uiElementBase = AWTUIElementBaseTests.createUIElementBase(test, component);
                    final IntegerValue counter = IntegerValue.create(0);
                    final SpinGate gate = SpinGate.create(test.getClock());
                    uiElementBase.onSizeChanged(() ->
                    {
                        counter.increment();
                        gate.open();
                    });

                    component.setSize(300, 400);
                    gate.passThrough(Duration.seconds(5), () -> test.getMainAsyncRunner().schedule(Action0.empty).await()).await();

                    test.assertEqual(1, counter.get());
                    test.assertEqual(300, uiElementBase.getWidthInPixels());
                    test.assertEqual(300, component.getWidth());
                    test.assertEqual(400, uiElementBase.getHeightInPixels());
                    test.assertEqual(400, component.getHeight());
                });
            });

            runner.testGroup("onSizeChanged(java.awt.Component,Action0)", () ->
            {
                runner.test("with null component", (Test test) ->
                {
                    final AWTUIBase uiBase = AWTUIBase.create(test.getProcess());
                    final javax.swing.JButton jButton = new javax.swing.JButton();
                    final AWTUIElementBase uiElementBase = new AWTUIElementBase(uiBase, jButton);
                    final Action0 callback = () -> {};
                    test.assertThrows(() -> uiElementBase.onSizeChanged(null, callback),
                        new PreConditionFailure("component cannot be null."));
                });

                runner.test("with null callback", (Test test) ->
                {
                    final AWTUIBase uiBase = AWTUIBase.create(test.getProcess());
                    final javax.swing.JButton jButton = new javax.swing.JButton();
                    final AWTUIElementBase uiElementBase = new AWTUIElementBase(uiBase, jButton);
                    final java.awt.Component component = new javax.swing.JButton();
                    final Action0 callback = null;
                    test.assertThrows(() -> uiElementBase.onSizeChanged(component, callback),
                        new PreConditionFailure("callback cannot be null."));
                });

                runner.test("with valid arguments", (Test test) ->
                {
                    final AWTUIBase uiBase = AWTUIBase.create(test.getProcess());
                    final javax.swing.JButton jButton = new javax.swing.JButton();
                    final AWTUIElementBase uiElementBase = new AWTUIElementBase(uiBase, jButton);
                    final java.awt.Component component = new javax.swing.JButton();
                    final Action0 callback = () -> {};

                    final Disposable disposable = uiElementBase.onSizeChanged(component, callback);
                    test.assertNotNull(disposable);
                    test.assertFalse(disposable.isDisposed());

                    final java.awt.event.ComponentListener[] componentListenersBeforeDispose = component.getComponentListeners();
                    test.assertNotNull(componentListenersBeforeDispose);
                    test.assertEqual(1, componentListenersBeforeDispose.length);

                    test.assertTrue(disposable.dispose().await());

                    final java.awt.event.ComponentListener[] componentListenersAfterDispose = component.getComponentListeners();
                    test.assertNotNull(componentListenersAfterDispose);
                    test.assertEqual(0, componentListenersAfterDispose.length);
                });
            });
        });
    }
}
