package qub;

public interface TestRunnerTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(TestRunner.class, () ->
        {
            runner.testGroup("create(DesktopProcess)", () ->
            {
                runner.test("with null process", (Test test) ->
                {
                    test.assertThrows(() -> TestRunner.create(null),
                        new PreConditionFailure("process cannot be null."));
                });

                runner.test("with non-null process",
                    (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                    (Test test, FakeDesktopProcess process) ->
                {
                    final TestRunner testRunner = TestRunner.create(process);
                    test.assertNotNull(testRunner);
                });
            });

            runner.testGroup("create(DesktopProcess,TestRunnerParameters)", () ->
            {
                runner.test("with null process", (Test test) ->
                {
                    test.assertThrows(() -> TestRunner.create(null, TestRunnerParameters.create()),
                        new PreConditionFailure("process cannot be null."));
                });

                runner.test("with null parameters",
                    (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                    (Test test, FakeDesktopProcess process) ->
                {
                    test.assertThrows(() -> TestRunner.create(process, (TestRunnerParameters)null),
                        new PreConditionFailure("parameters cannot be null."));
                });

                runner.test("with non-null process and parameters",
                    (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                    (Test test, FakeDesktopProcess process) ->
                {
                    final TestRunner testRunner = TestRunner.create(process, TestRunnerParameters.create());
                    test.assertNotNull(testRunner);
                });
            });
        });
    }
}
