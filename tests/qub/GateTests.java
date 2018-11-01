package qub;

public class GateTests
{
    public static void test(TestRunner runner, Function2<Clock,Boolean,Gate> creator)
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
                    test.assertThrows(() -> gate.passThrough((Duration)null));
                });

                runner.test("with negative Duration", (Test test) ->
                {
                    final Gate gate = create(creator, true, test);
                    test.assertThrows(() -> gate.passThrough(Duration.seconds(-1)));
                });

                runner.test("with zero Duration", (Test test) ->
                {
                    final Gate gate = create(creator, true, test);
                    test.assertThrows(() -> gate.passThrough(Duration.zero));
                });

                runner.test("with positive Duration with no clock", (Test test) ->
                {
                    final Gate gate = create(creator, true);
                    test.assertThrows(() -> gate.passThrough(Duration.zero));
                });

                runner.test("with positive Duration when open", (Test test) ->
                {
                    final Gate gate = create(creator, true, test);
                    test.assertSuccess(true, gate.passThrough(Duration.seconds(1)));
                });

                runner.test("with positive Duration when closed", (Test test) ->
                {
                    final Gate gate = create(creator, false, test);
                    test.assertError(new TimeoutException(), gate.passThrough(Duration.seconds(1)));
                });
            });

            runner.testGroup("passThrough(DateTime)", () ->
            {
                runner.test("with null DateTime", (Test test) ->
                {
                    final Gate gate = create(creator, true, test);
                    test.assertThrows(() -> gate.passThrough((Duration)null));
                });

                runner.test("with non-null DateTime and no clock", (Test test) ->
                {
                    final Gate gate = create(creator, true);
                    test.assertThrows(() -> gate.passThrough(test.getClock().getCurrentDateTime()));
                });

                runner.test("with DateTime before now", (Test test) ->
                {
                    final Gate gate = create(creator, true, test);
                    test.assertError(new TimeoutException(), gate.passThrough(test.getClock().getCurrentDateTime().minus(Duration.seconds(1))));
                });

                runner.test("with DateTime at now", (Test test) ->
                {
                    final Gate gate = create(creator, true, test);
                    test.assertError(new TimeoutException(), gate.passThrough(test.getClock().getCurrentDateTime()));
                });

                runner.test("with future Duration when open", (Test test) ->
                {
                    final Gate gate = create(creator, true, test);
                    test.assertSuccess(true, gate.passThrough(test.getClock().getCurrentDateTime().plus(Duration.seconds(1))));
                });

                runner.test("with positive Duration when not open", (Test test) ->
                {
                    final Gate gate = create(creator, false, test);
                    test.assertError(new TimeoutException(), gate.passThrough(test.getClock().getCurrentDateTime().plus(Duration.seconds(1))));
                });
            });
        });
    }

    private static Gate create(Function2<Clock,Boolean,Gate> creator, boolean open)
    {
        return creator.run(null, open);
    }

    private static Gate create(Function2<Clock,Boolean,Gate> creator, boolean open, Test test)
    {
        return creator.run(test.getClock(), open);
    }
}
