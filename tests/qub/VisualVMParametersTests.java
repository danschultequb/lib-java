package qub;

public interface VisualVMParametersTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(VisualVMParameters.class, () ->
        {
            runner.test("create()", (Test test) ->
            {
                final VisualVMParameters parameters = VisualVMParameters.create();
                test.assertNotNull(parameters);
                test.assertEqual(Path.parse("visualvm"), parameters.getExecutablePath());
                test.assertNull(parameters.getWorkingFolderPath());
                test.assertEqual(Iterable.create(), parameters.getArguments());
                test.assertNull(parameters.getInputStream());
                test.assertNull(parameters.getOutputStreamHandler());
                test.assertNull(parameters.getErrorStreamHandler());
            });

            runner.testGroup("create(Path)", () ->
            {
                final Action2<Path,Throwable> createErrorTest = (Path visualVmPath, Throwable expected) ->
                {
                    runner.test("with " + visualVmPath, (Test test) ->
                    {
                        test.assertThrows(() -> VisualVMParameters.create(visualVmPath),
                            expected);
                    });
                };

                createErrorTest.run(null, new PreConditionFailure("visualVmPath cannot be null."));
                createErrorTest.run(Path.parse("visualvm/"), new PreConditionFailure("executablePath.endsWith('/') || executablePath.endsWith('\\') cannot be true."));
                createErrorTest.run(Path.parse("visualvm\\"), new PreConditionFailure("executablePath.endsWith('/') || executablePath.endsWith('\\') cannot be true."));

                final Action1<Path> createTest = (Path visualVmPath) ->
                {
                    runner.test("with " + visualVmPath, (Test test) ->
                    {
                        final VisualVMParameters parameters = VisualVMParameters.create(visualVmPath);
                        test.assertNotNull(parameters);
                        test.assertEqual(visualVmPath, parameters.getExecutablePath());
                        test.assertNull(parameters.getWorkingFolderPath());
                        test.assertEqual(Iterable.create(), parameters.getArguments());
                        test.assertNull(parameters.getInputStream());
                        test.assertNull(parameters.getOutputStreamHandler());
                        test.assertNull(parameters.getErrorStreamHandler());
                    });
                };

                createTest.run(Path.parse("visualvm"));
                createTest.run(Path.parse("visualvm.exe"));
                createTest.run(Path.parse("hello"));
                createTest.run(Path.parse("visual/vm"));
                createTest.run(Path.parse("visual\\vm"));
                createTest.run(Path.parse("/visual/vm.exe"));
                createTest.run(Path.parse("\\visual\\vm.exe"));
            });
        });
    }
}
