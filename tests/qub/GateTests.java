package qub;

public interface GateTests
{
    static Gate createGate(Function2<Clock,Boolean,Gate> creator, boolean open)
    {
        PreCondition.assertNotNull(creator, "creator");

        return creator.run(null, open);
    }

    static Gate createGate(Function2<Clock,Boolean,Gate> creator, boolean open, Clock clock)
    {
        PreCondition.assertNotNull(creator, "creator");
        PreCondition.assertNotNull(clock, "clock");

        return creator.run(clock, open);
    }

    static void test(TestRunner runner, Function2<Clock,Boolean,Gate> creator)
    {
        runner.testGroup(Gate.class, () ->
        {
            runner.testGroup("constructor(Clock,boolean)", () ->
            {
                runner.test("with null Clock and false", (Test test) ->
                {
                    final Gate gate = GateTests.createGate(creator, false);
                    test.assertNull(gate.getClock());
                    test.assertFalse(gate.isOpen());
                });

                runner.test("with Clock and true", (Test test) ->
                {
                    final ManualClock clock = ManualClock.create();
                    final Gate gate = GateTests.createGate(creator, true, clock);
                    test.assertSame(clock, gate.getClock());
                    test.assertTrue(gate.isOpen());
                });

                runner.test("with Clock and false", (Test test) ->
                {
                    final ManualClock clock = ManualClock.create();
                    final Gate gate = GateTests.createGate(creator, false, clock);
                    test.assertSame(clock, gate.getClock());
                    test.assertFalse(gate.isOpen());
                });
            });

            runner.testGroup("open()", () ->
            {
                runner.test("when open", (Test test) ->
                {
                    final Gate gate = GateTests.createGate(creator, true);
                    test.assertFalse(gate.open());
                    test.assertTrue(gate.isOpen());
                });

                runner.test("when closed", (Test test) ->
                {
                    final Gate gate = GateTests.createGate(creator, false);
                    test.assertTrue(gate.open());
                    test.assertTrue(gate.isOpen());
                });
            });

            runner.testGroup("close()", () ->
            {
                runner.test("when closed", (Test test) ->
                {
                    final Gate gate = GateTests.createGate(creator, false);
                    test.assertFalse(gate.close());
                    test.assertFalse(gate.isOpen());
                });

                runner.test("when open", (Test test) ->
                {
                    final Gate gate = GateTests.createGate(creator, true);
                    test.assertTrue(gate.close());
                    test.assertFalse(gate.isOpen());
                });
            });

            runner.testGroup("passThrough()", () ->
            {
                runner.test("when open with no clock", (Test test) ->
                {
                    final Gate gate = GateTests.createGate(creator, true);
                    test.assertTrue(gate.isOpen());
                    test.assertNull(gate.getClock());
                    gate.passThrough().await();
                });

                runner.test("when open with clock", (Test test) ->
                {
                    final ManualClock clock = ManualClock.create();
                    final Gate gate = GateTests.createGate(creator, true, clock);
                    test.assertTrue(gate.isOpen());
                    test.assertSame(clock, gate.getClock());
                    gate.passThrough().await();
                });
            });

            runner.testGroup("passThrough(Duration)", () ->
            {
                runner.test("with null Duration", (Test test) ->
                {
                    final ManualClock clock = ManualClock.create();
                    final Gate gate = GateTests.createGate(creator, true, clock);
                    test.assertThrows(() -> gate.passThrough((Duration)null),
                        new PreConditionFailure("timeout cannot be null."));
                });

                runner.test("with negative Duration", (Test test) ->
                {
                    final ManualClock clock = ManualClock.create();
                    final Gate gate = GateTests.createGate(creator, true, clock);
                    test.assertThrows(() -> gate.passThrough(Duration.seconds(-1)),
                        new PreConditionFailure("timeout (-1.0 Seconds) must be greater than 0.0 Seconds."));
                });

                runner.test("with zero Duration", (Test test) ->
                {
                    final ManualClock clock = ManualClock.create();
                    final Gate gate = GateTests.createGate(creator, true, clock);
                    test.assertThrows(() -> gate.passThrough(Duration.zero),
                        new PreConditionFailure("timeout (0.0 Seconds) must be greater than 0.0 Seconds."));
                });

                runner.test("with positive Duration with no clock", (Test test) ->
                {
                    final Gate gate = GateTests.createGate(creator, true);
                    test.assertThrows(() -> gate.passThrough(Duration.zero),
                        new PreConditionFailure("timeout (0.0 Seconds) must be greater than 0.0 Seconds."));
                });

                runner.test("with positive Duration when open", (Test test) ->
                {
                    final ManualClock clock = ManualClock.create();
                    final Gate gate = GateTests.createGate(creator, true, clock);
                    test.assertNull(gate.passThrough(Duration.seconds(1)).await());
                });

                runner.test("with positive Duration when closed",
                    (TestResources resources) -> Tuple.create(resources.getClock()),
                    (Test test, Clock clock) ->
                {
                    final Gate gate = GateTests.createGate(creator, false, clock);
                    test.assertThrows(() -> gate.passThrough(Duration.seconds(0.1)).await(),
                        new TimeoutException());
                });
            });

            runner.testGroup("passThrough(DateTime)", () ->
            {
                runner.test("with null DateTime", (Test test) ->
                {
                    final ManualClock clock = ManualClock.create();
                    final Gate gate = GateTests.createGate(creator, true, clock);
                    test.assertThrows(() -> gate.passThrough((Duration)null),
                        new PreConditionFailure("timeout cannot be null."));
                });

                runner.test("with non-null DateTime and no clock", (Test test) ->
                {
                    final ManualClock clock = ManualClock.create();
                    final Gate gate = GateTests.createGate(creator, true);
                    final DateTime timeout = clock.getCurrentDateTime();
                    test.assertThrows(() -> gate.passThrough(timeout),
                        new PreConditionFailure("this.getClock() cannot be null."));
                });

                runner.test("with DateTime before now", (Test test) ->
                {
                    final ManualClock clock = ManualClock.create();
                    final Gate gate = GateTests.createGate(creator, true, clock);
                    final DateTime timeout = clock.getCurrentDateTime().minus(Duration.seconds(1));
                    test.assertThrows(() -> gate.passThrough(timeout).await(),
                        new TimeoutException());
                });

                runner.test("with DateTime at now", (Test test) ->
                {
                    final ManualClock clock = ManualClock.create();
                    final Gate gate = GateTests.createGate(creator, true, clock);
                    final DateTime timeout = clock.getCurrentDateTime();
                    test.assertThrows(() -> gate.passThrough(timeout).await(),
                        new TimeoutException());
                });

                runner.test("with future Duration when open", (Test test) ->
                {
                    final ManualClock clock = ManualClock.create();
                    final Gate gate = GateTests.createGate(creator, true, clock);
                    final DateTime timeout = clock.getCurrentDateTime().plus(Duration.seconds(1));
                    test.assertNull(gate.passThrough(timeout).await());
                });

                runner.test("with positive Duration when not open",
                    (TestResources resources) -> Tuple.create(resources.getClock()),
                    (Test test, Clock clock) ->
                {
                    final Gate gate = GateTests.createGate(creator, false, clock);
                    final DateTime timeout = clock.getCurrentDateTime().plus(Duration.seconds(0.1));
                    test.assertThrows(() -> gate.passThrough(timeout).await(),
                        new TimeoutException());
                });
            });
        });
    }
}
