package qub;

public interface RealDefaultApplicationLauncherTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(RealDefaultApplicationLauncher.class, () ->
        {
            DefaultApplicationLauncherTests.test(runner, RealDefaultApplicationLauncher::new);

            runner.testGroup("openFileWithDefaultApplication(String)", () ->
            {
                runner.test("with relative file path that doesn't exist", (Test test) ->
                {
                    final RealDefaultApplicationLauncher launcher = new RealDefaultApplicationLauncher();
                    test.assertThrows(() -> launcher.openFileWithDefaultApplication("helloThere.txt").await(),
                        new java.lang.IllegalArgumentException("The file: helloThere.txt doesn't exist."));
                });

                runner.test("with relative file path that doesn't have a registered default application", runner.skip("opens default application dialog launcher"), (Test test) ->
                {
                    final RealDefaultApplicationLauncher launcher = new RealDefaultApplicationLauncher();
                    test.assertNull(launcher.openFileWithDefaultApplication("qub-java.iml").await());
                });

                runner.test("with relative file path that has a registered default application", runner.skip("opens default application"), (Test test) ->
                {
                    final RealDefaultApplicationLauncher launcher = new RealDefaultApplicationLauncher();
                    test.assertNull(launcher.openFileWithDefaultApplication("project.json").await());
                });
            });
        });
    }
}
