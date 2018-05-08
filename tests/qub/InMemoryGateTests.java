package qub;

public class InMemoryGateTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(SpinGate.class, () ->
        {
            runner.testGroup("constructor(boolean)", () ->
            {
                runner.test("with true", (Test test) ->
                {
                    final SpinGate gate = new SpinGate(true);
                    test.assertTrue(gate.isOpen());
                });

                runner.test("with false", (Test test) ->
                {
                    final SpinGate gate = new SpinGate(false);
                    test.assertFalse(gate.isOpen());
                });
            });

            runner.testGroup("open()", () ->
            {
                runner.test("when open", (Test test) ->
                {
                    final SpinGate gate = new SpinGate(true);
                    test.assertFalse(gate.open());
                    test.assertTrue(gate.isOpen());
                });

                runner.test("when closed", (Test test) ->
                {
                    final SpinGate gate = new SpinGate(false);
                    test.assertTrue(gate.open());
                    test.assertTrue(gate.isOpen());
                });
            });

            runner.testGroup("close()", () ->
            {
                runner.test("when closed", (Test test) ->
                {
                    final SpinGate gate = new SpinGate(false);
                    test.assertFalse(gate.close());
                    test.assertFalse(gate.isOpen());
                });

                runner.test("when open", (Test test) ->
                {
                    final SpinGate gate = new SpinGate(true);
                    test.assertTrue(gate.close());
                    test.assertFalse(gate.isOpen());
                });
            });
        });
    }
}
