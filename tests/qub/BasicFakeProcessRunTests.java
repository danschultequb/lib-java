package qub;

public interface BasicFakeProcessRunTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(BasicFakeProcessRun.class, () ->
        {
            FakeProcessRunTests.test(runner, (String executablePath) -> new BasicFakeProcessRun(Path.parse(executablePath)));

            runner.testGroup("constructor(Path)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> new BasicFakeProcessRun((Path)null),
                        new PreConditionFailure("executableFilePath cannot be null."));
                });

                runner.test("with relative path", (Test test) ->
                {
                    final BasicFakeProcessRun fakeProcessRun = new BasicFakeProcessRun(Path.parse("testFile.exe"));
                    test.assertEqual(Path.parse("testFile.exe"), fakeProcessRun.getExecutablePath());
                    test.assertEqual(Iterable.create(), fakeProcessRun.getArguments());
                    test.assertNull(fakeProcessRun.getWorkingFolderPath());
                    test.assertNull(fakeProcessRun.getFunction());
                });

                runner.test("with rooted path", (Test test) ->
                {
                    final BasicFakeProcessRun fakeProcessRun = new BasicFakeProcessRun(Path.parse("/testFile.exe"));
                    test.assertEqual(Path.parse("/testFile.exe"), fakeProcessRun.getExecutablePath());
                    test.assertEqual(Iterable.create(), fakeProcessRun.getArguments());
                    test.assertNull(fakeProcessRun.getWorkingFolderPath());
                    test.assertNull(fakeProcessRun.getFunction());
                });
            });
        });
    }
}
