package qub;

public interface GateTests
{
    static void test(TestRunner runner, Function2<Clock,Boolean,Gate> creator)
    {
        runner.testGroup(Gate.class, () ->
        {
            runner.testGroup("constructor(Clock,boolean)", () ->
            {
                runner.test("with null Clock and false", (Test test) ->
                {
                    final Gate gate = create(creator, false);
                    test.assertNull(gate.getClock());
                    test.assertFalse(gate.isOpen());
                });

                runner.test("with Clock and true", (Test test) ->
                {
                    final Gate gate = create(creator, true, test);
                    test.assertSame(test.getClock(), gate.getClock());
                    test.assertTrue(gate.isOpen());
                });

                runner.test("with Clock and false", (Test test) ->
                {
                    final Gate gate = create(creator, false, test);
                    test.assertSame(test.getClock(), gate.getClock());
                    test.assertFalse(gate.isOpen());
                });
            });

            runner.testGroup("open()", () ->
            {
                runner.test("when open", (Test test) ->
                {
                    final Gate gate = create(creator, true, test);
                    test.assertFalse(gate.open());
                    test.assertTrue(gate.isOpen());
                });

                runner.test("when closed", (Test test) ->
                {
                    final Gate gate = create(creator, false, test);
                    test.assertTrue(gate.open());
                    test.assertTrue(gate.isOpen());
                });
            });

            runner.testGroup("close()", () ->
            {
                runner.test("when closed", (Test test) ->
                {
                    final Gate gate = create(creator, false, test);
                    test.assertFalse(gate.close());
                    test.assertFalse(gate.isOpen());
                });

                runner.test("when open", (Test test) ->
                {
                    final Gate gate = create(creator, true, test);
                    test.assertTrue(gate.close());
                    test.assertFalse(gate.isOpen());
                });
            });

            runner.testGroup("passThrough()", () ->
            {
                runner.test("when open with no clock", (Test test) ->
                {
                    final Gate gate = create(creator, true);
                    test.assertTrue(gate.isOpen());
                    test.assertNull(gate.getClock());
                    gate.passThrough();
                });

                runner.test("when open with clock", (Test test) ->
                {
                    final Gate gate = create(creator, true, test);
                    test.assertTrue(gate.isOpen());
                    test.assertNotNull(gate.getClock());
                    gate.passThrough();
                });
            });

            runner.testGroup("passThrough(Duration)", () ->
            {
                runner.test("with null Duration", (Test test) ->
                {
                    final Gate gate = create(creator, true, test);
                    test.assertThrows(() -> gate.passThrough((Duration2)null), new PreConditionFailure("timeout cannot be null."));
                });

                runner.test("with negative Duration", (Test test) ->
                {
                    final Gate gate = create(creator, true, test);
                    test.assertThrows(() -> gate.passThrough(Duration2.seconds(-1)), new PreConditionFailure("timeout (-1.0 Seconds) must be greater than 0.0 Seconds."));
                });

                runner.test("with zero Duration", (Test test) ->
                {
                    final Gate gate = create(creator, true, test);
                    test.assertThrows(() -> gate.passThrough(Duration2.zero), new PreConditionFailure("timeout (0.0 Seconds) must be greater than 0.0 Seconds."));
                });

                runner.test("with positive Duration with no clock", (Test test) ->
                {
                    final Gate gate = create(creator, true);
                    test.assertThrows(() -> gate.passThrough(Duration2.zero), new PreConditionFailure("timeout (0.0 Seconds) must be greater than 0.0 Seconds."));
                });

                runner.test("with positive Duration when open", (Test test) ->
                {
                    final Gate gate = create(creator, true, test);
                    test.assertNull(gate.passThrough(Duration2.seconds(1)).await());
                });

                runner.test("with positive Duration when closed", (Test test) ->
                {
                    final Gate gate = create(creator, false, test);
                    test.assertThrows(() -> gate.passThrough(Duration2.seconds(0.1)).await(),
                        new TimeoutException());
                });
            });

            runner.testGroup("passThrough(DateTime)", () ->
            {
                runner.test("with null DateTime", (Test test) ->
                {
                    final Gate gate = create(creator, true, test);
                    test.assertThrows(() -> gate.passThrough((Duration2)null), new PreConditionFailure("timeout cannot be null."));
                });

                runner.test("with non-null DateTime and no clock", (Test test) ->
                {
                    final Gate gate = create(creator, true);
                    final DateTime timeout = test.getClock().getCurrentDateTime();
                    test.assertThrows(() -> gate.passThrough(timeout), new PreConditionFailure("getClock() cannot be null."));
                });

                runner.test("with DateTime before now", (Test test) ->
                {
                    final Gate gate = create(creator, true, test);
                    final DateTime timeout = test.getClock().getCurrentDateTime().minus(Duration2.seconds(1));
                    test.assertThrows(() -> gate.passThrough(timeout).await(),
                        new TimeoutException());
                });

                runner.test("with DateTime at now", (Test test) ->
                {
                    final Gate gate = create(creator, true, test);
                    final DateTime timeout = test.getClock().getCurrentDateTime();
                    test.assertThrows(() -> gate.passThrough(timeout).await(),
                        new TimeoutException());
                });

                runner.test("with future Duration when open", (Test test) ->
                {
                    final Gate gate = create(creator, true, test);
                    final DateTime timeout = test.getClock().getCurrentDateTime().plus(Duration2.seconds(1));
                    test.assertNull(gate.passThrough(timeout).await());
                });

                runner.test("with positive Duration when not open", (Test test) ->
                {
                    final Gate gate = create(creator, false, test);
                    final DateTime timeout = test.getClock().getCurrentDateTime().plus(Duration2.seconds(0.1));
                    test.assertThrows(() -> gate.passThrough(timeout).await(), new TimeoutException());
                });
            });
        });
    }

    static Gate create(Function2<Clock,Boolean,Gate> creator, boolean open)
    {
        return creator.run(null, open);
    }

    static Gate create(Function2<Clock,Boolean,Gate> creator, boolean open, Test test)
    {
        return creator.run(test.getClock(), open);
    }
}
