package qub;

public interface DefaultApplicationLauncherTests
{
    static void test(TestRunner runner, Function0<DefaultApplicationLauncher> creator)
    {
        PreCondition.assertNotNull(runner, "runner");
        PreCondition.assertNotNull(creator, "creator");

        runner.testGroup(DefaultApplicationLauncher.class, () ->
        {
            runner.testGroup("openFileWithDefaultApplication(String)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final DefaultApplicationLauncher launcher = creator.run();
                    test.assertThrows(() -> launcher.openFileWithDefaultApplication((String)null),
                        new PreConditionFailure("filePathToOpen cannot be null."));
                });

                runner.test("with empty", (Test test) ->
                {
                    final DefaultApplicationLauncher launcher = creator.run();
                    test.assertThrows(() -> launcher.openFileWithDefaultApplication(""),
                        new PreConditionFailure("filePathToOpen cannot be empty."));
                });
            });

            runner.testGroup("openFileWithDefaultApplication(Path)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final DefaultApplicationLauncher launcher = creator.run();
                    test.assertThrows(() -> launcher.openFileWithDefaultApplication((Path)null),
                        new PreConditionFailure("filePathToOpen cannot be null."));
                });
            });

            runner.testGroup("openFileWithDefaultApplication(File)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final DefaultApplicationLauncher launcher = creator.run();
                    test.assertThrows(() -> launcher.openFileWithDefaultApplication((File)null),
                        new PreConditionFailure("fileToOpen cannot be null."));
                });
            });
        });
    }
}
