package qub;

public class SpinGateTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(SpinGate.class, () ->
        {
            GateTests.test(runner, SpinGate::new);
        });
    }
}
