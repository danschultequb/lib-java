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

            runner.testGroup("setSourceRoots(Iterable<String>)", () ->
            {
                final Action2<Iterable<String>,Throwable> setSourceRootsErrorTest = (Iterable<String> sourceRoots, Throwable expected) ->
                {
                    runner.test("with " + (sourceRoots == null ? "null" : sourceRoots.map(Strings::escapeAndQuote)), (Test test) ->
                    {
                        final VisualVMParameters parameters = VisualVMParameters.create();
                        test.assertThrows(() -> parameters.setSourceRoots(sourceRoots),
                            expected);
                        test.assertEqual(Iterable.create(), parameters.getArguments());
                    });
                };

                setSourceRootsErrorTest.run(null, new PreConditionFailure("sourceRoots cannot be null."));

                final Action2<Iterable<String>,Iterable<String>> setSourceRootsTest = (Iterable<String> sourceRoots, Iterable<String> expectedArguments) ->
                {
                    runner.test("with " + sourceRoots.map(Strings::escapeAndQuote), (Test test) ->
                    {
                        final VisualVMParameters parameters = VisualVMParameters.create();
                        final VisualVMParameters setSourceRootsResult = parameters.setSourceRoots(sourceRoots);
                        test.assertSame(parameters, setSourceRootsResult);
                        test.assertEqual(expectedArguments, parameters.getArguments());
                    });
                };

                setSourceRootsTest.run(Iterable.create(), Iterable.create("--source-roots=\"\""));
                setSourceRootsTest.run(Iterable.create("a"), Iterable.create("--source-roots=\"a\""));
                setSourceRootsTest.run(Iterable.create("a", "b"), Iterable.create("--source-roots=\"a;b\""));
                setSourceRootsTest.run(Iterable.create("a", "b", "c"), Iterable.create("--source-roots=\"a;b;c\""));
            });
        });
    }
}
