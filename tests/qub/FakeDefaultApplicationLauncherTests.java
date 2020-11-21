package qub;

public interface FakeDefaultApplicationLauncherTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(FakeDefaultApplicationLauncher.class, () ->
        {
            DefaultApplicationLauncherTests.test(runner, FakeDefaultApplicationLauncher::create);

            runner.testGroup("openFileWithDefaultApplication(String)", () ->
            {
                runner.test("with file that doesn't exist", (Test test) ->
                {
                    final FakeDefaultApplicationLauncher launcher = FakeDefaultApplicationLauncher.create();
                    test.assertNull(launcher.openFileWithDefaultApplication("hello there").await());
                });
            });
        });
    }
}
