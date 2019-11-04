package qub;

public interface FakeDefaultApplicationLauncherTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(FakeDefaultApplicationLauncher.class, () ->
        {
            DefaultApplicationLauncherTests.test(runner, FakeDefaultApplicationLauncher::new);

            runner.testGroup("openFileWithDefaultApplication(String)", () ->
            {
                runner.test("with file that doesn't exist", (Test test) ->
                {
                    final FakeDefaultApplicationLauncher launcher = new FakeDefaultApplicationLauncher();
                    test.assertNull(launcher.openFileWithDefaultApplication("hello there").await());
                });
            });
        });
    }
}
