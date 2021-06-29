package qub;

public interface BasicFakeProcessRunTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(BasicFakeChildProcessRun.class, () ->
        {
            FakeProcessRunTests.test(runner, (String executablePath) -> BasicFakeChildProcessRun.create(Path.parse(executablePath), Iterable.create()));

            runner.testGroup("constructor(Path)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> BasicFakeChildProcessRun.create((Path)null, Iterable.create()),
                        new PreConditionFailure("executableFilePath cannot be null."));
                });

                runner.test("with relative path", (Test test) ->
                {
                    final BasicFakeChildProcessRun fakeProcessRun = BasicFakeChildProcessRun.create(Path.parse("testFile.exe"), Iterable.create());
                    test.assertEqual(Path.parse("testFile.exe"), fakeProcessRun.getExecutablePath());
                    test.assertEqual(Iterable.create(), fakeProcessRun.getArguments());
                    test.assertNull(fakeProcessRun.getWorkingFolderPath());
                    test.assertNull(fakeProcessRun.getFunction());
                });

                runner.test("with rooted path", (Test test) ->
                {
                    final BasicFakeChildProcessRun fakeProcessRun = BasicFakeChildProcessRun.create(Path.parse("/testFile.exe"), Iterable.create());
                    test.assertEqual(Path.parse("/testFile.exe"), fakeProcessRun.getExecutablePath());
                    test.assertEqual(Iterable.create(), fakeProcessRun.getArguments());
                    test.assertNull(fakeProcessRun.getWorkingFolderPath());
                    test.assertNull(fakeProcessRun.getFunction());
                });
            });
        });
    }
}
