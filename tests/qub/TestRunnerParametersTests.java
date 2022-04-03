package qub;

public interface TestRunnerParametersTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(TestRunnerParameters.class, () ->
        {
            runner.test("create()", (Test test) ->
            {
                final TestRunnerParameters parameters = TestRunnerParameters.create();
                test.assertNotNull(parameters);
                test.assertNull(parameters.getTestPattern());
            });

            TestRunnerParametersTests.test(runner, TestRunnerParameters::create);
        });
    }

    public static void test(TestRunner runner, Function0<? extends TestRunnerParameters> creator)
    {
        runner.testGroup(TestRunnerParameters.class, () ->
        {
            runner.testGroup("setTestPattern(PathPattern)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final TestRunnerParameters parameters = creator.run();
                    test.assertThrows(() -> parameters.setTestPattern(null),
                        new PreConditionFailure("testPattern cannot be null."));
                    test.assertNull(parameters.getTestPattern());
                });

                runner.test("with non-null", (Test test) ->
                {
                    final TestRunnerParameters parameters = creator.run();
                    final PathPattern testPattern = PathPattern.parse("hello*there");
                    final TestRunnerParameters setTestPatternResult = parameters.setTestPattern(testPattern);
                    test.assertSame(parameters, setTestPatternResult);
                    test.assertEqual(testPattern, parameters.getTestPattern());
                });
            });
        });
    }
}
