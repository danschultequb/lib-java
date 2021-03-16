package qub;

public interface SpinGateTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(SpinGate.class, () ->
        {
            GateTests.test(runner, SpinGate::create);

            runner.testGroup("passThrough(Action0)", () ->
            {
                runner.test("with null onWait", (Test test) ->
                {
                    final SpinGate gate = SpinGate.create();
                    test.assertThrows(() -> gate.passThrough((Action0)null),
                        new PreConditionFailure("onWait cannot be null."));
                });
            });

            runner.testGroup("passThrough(Duration,Action0)", () ->
            {
                runner.test("with null onWait", (Test test) ->
                {
                    final ManualClock clock = ManualClock.create();
                    final SpinGate gate = SpinGate.create(clock);
                    test.assertThrows(() -> gate.passThrough(Duration.seconds(5), (Action0)null),
                        new PreConditionFailure("onWait cannot be null."));
                });
            });

            runner.testGroup("passThrough(DateTime,Action0)", () ->
            {
                runner.test("with null onWait", (Test test) ->
                {
                    final ManualClock clock = ManualClock.create();
                    final SpinGate gate = SpinGate.create(clock);
                    test.assertThrows(() -> gate.passThrough(clock.getCurrentDateTime().plus(Duration.seconds(5)), (Action0)null),
                        new PreConditionFailure("onWait cannot be null."));
                });
            });
        });
    }
}
