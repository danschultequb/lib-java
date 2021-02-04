package qub;

public interface CommandLineConfigurationActionParametersTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(CommandLineConfigurationActionParameters.class, () ->
        {
            runner.test("create()", (Test test) ->
            {
                final CommandLineConfigurationActionParameters parameters = CommandLineConfigurationActionParameters.create();
                test.assertNotNull(parameters, "parameters");
                test.assertNull(parameters.getDataFolder());
                test.assertNull(parameters.getDefaultApplicationLauncher());
                test.assertNull(parameters.getConfigurationSchemaFileRelativePath());
                test.assertNull(parameters.getConfigurationFileRelativePath());
                test.assertNull(parameters.getConfigurationSchema());
                test.assertNull(parameters.getDefaultConfiguration());
            });

            runner.testGroup("setDataFolder(Folder)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final CommandLineConfigurationActionParameters parameters = CommandLineConfigurationActionParameters.create();
                    test.assertThrows(() -> parameters.setDataFolder(null),
                        new PreConditionFailure("dataFolder cannot be null."));
                    test.assertNull(parameters.getDataFolder());
                });

                runner.test("with non-existing folder", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = InMemoryFileSystem.create();
                    fileSystem.createRoot("/").await();
                    final Folder dataFolder = fileSystem.getFolder("/data/").await();
                    final CommandLineConfigurationActionParameters parameters = CommandLineConfigurationActionParameters.create();

                    final CommandLineConfigurationActionParameters setDataFolderResult = parameters.setDataFolder(dataFolder);

                    test.assertSame(parameters, setDataFolderResult);
                    test.assertSame(dataFolder, parameters.getDataFolder());
                });
            });

            runner.testGroup("setDefaultApplicationLauncher(DefaultApplicationLauncher)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final CommandLineConfigurationActionParameters parameters = CommandLineConfigurationActionParameters.create();
                    test.assertThrows(() -> parameters.setDefaultApplicationLauncher(null),
                        new PreConditionFailure("defaultApplicationLauncher cannot be null."));
                    test.assertNull(parameters.getDefaultApplicationLauncher());
                });

                runner.test("with non-null", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = InMemoryFileSystem.create();
                    final FakeDefaultApplicationLauncher defaultApplicationLauncher = FakeDefaultApplicationLauncher.create(fileSystem);
                    final CommandLineConfigurationActionParameters parameters = CommandLineConfigurationActionParameters.create();

                    final CommandLineConfigurationActionParameters setDefaultApplicationLauncherResult = parameters.setDefaultApplicationLauncher(defaultApplicationLauncher);

                    test.assertSame(parameters, setDefaultApplicationLauncherResult);
                    test.assertSame(defaultApplicationLauncher, parameters.getDefaultApplicationLauncher());
                });
            });

            runner.testGroup("setConfigurationSchemaFileRelativePath(String)", () ->
            {
                final Action2<String,Throwable> setConfigurationSchemaFileRelativePathErrorTest = (String configurationSchemaFileRelativePath, Throwable expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(configurationSchemaFileRelativePath), (Test test) ->
                    {
                        final CommandLineConfigurationActionParameters parameters = CommandLineConfigurationActionParameters.create();
                        test.assertThrows(() -> parameters.setConfigurationSchemaFileRelativePath(configurationSchemaFileRelativePath),
                            expected);
                        test.assertNull(parameters.getConfigurationSchemaFileRelativePath());
                    });
                };

                setConfigurationSchemaFileRelativePathErrorTest.run(null, new PreConditionFailure("configurationSchemaFileRelativePath cannot be null."));
                setConfigurationSchemaFileRelativePathErrorTest.run("", new PreConditionFailure("configurationSchemaFileRelativePath cannot be empty."));
                setConfigurationSchemaFileRelativePathErrorTest.run("/hello", new PreConditionFailure("configurationSchemaFileRelativePath.isRooted() cannot be true."));

                final Action1<String> setConfigurationSchemaFileRelativePathTest = (String configurationSchemaFileRelativePath) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(configurationSchemaFileRelativePath), (Test test) ->
                    {
                        final CommandLineConfigurationActionParameters parameters = CommandLineConfigurationActionParameters.create();

                        final CommandLineConfigurationActionParameters setConfigurationSchemaFileRelativePathResult = parameters.setConfigurationSchemaFileRelativePath(configurationSchemaFileRelativePath);
                        test.assertSame(parameters, setConfigurationSchemaFileRelativePathResult);
                        test.assertEqual(Path.parse(configurationSchemaFileRelativePath), parameters.getConfigurationSchemaFileRelativePath());
                    });
                };

                setConfigurationSchemaFileRelativePathTest.run("hello");
                setConfigurationSchemaFileRelativePathTest.run("configuration.schema.json");
                setConfigurationSchemaFileRelativePathTest.run("schemas/configuration.schema.json");
            });

            runner.testGroup("setConfigurationSchemaFileRelativePath(Path)", () ->
            {
                final Action2<Path,Throwable> setConfigurationSchemaFileRelativePathErrorTest = (Path configurationSchemaFileRelativePath, Throwable expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(configurationSchemaFileRelativePath), (Test test) ->
                    {
                        final CommandLineConfigurationActionParameters parameters = CommandLineConfigurationActionParameters.create();
                        test.assertThrows(() -> parameters.setConfigurationSchemaFileRelativePath(configurationSchemaFileRelativePath),
                            expected);
                        test.assertNull(parameters.getConfigurationSchemaFileRelativePath());
                    });
                };

                setConfigurationSchemaFileRelativePathErrorTest.run(null, new PreConditionFailure("configurationSchemaFileRelativePath cannot be null."));
                setConfigurationSchemaFileRelativePathErrorTest.run(Path.parse("/hello"), new PreConditionFailure("configurationSchemaFileRelativePath.isRooted() cannot be true."));

                final Action1<Path> setConfigurationSchemaFileRelativePathTest = (Path configurationSchemaFileRelativePath) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(configurationSchemaFileRelativePath), (Test test) ->
                    {
                        final CommandLineConfigurationActionParameters parameters = CommandLineConfigurationActionParameters.create();

                        final CommandLineConfigurationActionParameters setConfigurationSchemaFileRelativePathResult = parameters.setConfigurationSchemaFileRelativePath(configurationSchemaFileRelativePath);
                        test.assertSame(parameters, setConfigurationSchemaFileRelativePathResult);
                        test.assertEqual(configurationSchemaFileRelativePath, parameters.getConfigurationSchemaFileRelativePath());
                    });
                };

                setConfigurationSchemaFileRelativePathTest.run(Path.parse("hello"));
                setConfigurationSchemaFileRelativePathTest.run(Path.parse("configuration.schema.json"));
                setConfigurationSchemaFileRelativePathTest.run(Path.parse("schemas/configuration.schema.json"));
            });

            runner.testGroup("setConfigurationFileRelativePath(String)", () ->
            {
                final Action2<String,Throwable> setConfigurationFileRelativePathErrorTest = (String configurationFileRelativePath, Throwable expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(configurationFileRelativePath), (Test test) ->
                    {
                        final CommandLineConfigurationActionParameters parameters = CommandLineConfigurationActionParameters.create();
                        test.assertThrows(() -> parameters.setConfigurationFileRelativePath(configurationFileRelativePath),
                            expected);
                        test.assertNull(parameters.getConfigurationFileRelativePath());
                    });
                };

                setConfigurationFileRelativePathErrorTest.run(null, new PreConditionFailure("configurationFileRelativePath cannot be null."));
                setConfigurationFileRelativePathErrorTest.run("", new PreConditionFailure("configurationFileRelativePath cannot be empty."));
                setConfigurationFileRelativePathErrorTest.run("/hello", new PreConditionFailure("configurationFileRelativePath.isRooted() cannot be true."));

                final Action1<String> setConfigurationFileRelativePathTest = (String configurationFileRelativePath) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(configurationFileRelativePath), (Test test) ->
                    {
                        final CommandLineConfigurationActionParameters parameters = CommandLineConfigurationActionParameters.create();

                        final CommandLineConfigurationActionParameters setConfigurationFileRelativePathResult = parameters.setConfigurationFileRelativePath(configurationFileRelativePath);
                        test.assertSame(parameters, setConfigurationFileRelativePathResult);
                        test.assertEqual(Path.parse(configurationFileRelativePath), parameters.getConfigurationFileRelativePath());
                    });
                };

                setConfigurationFileRelativePathTest.run("hello");
                setConfigurationFileRelativePathTest.run("configuration.json");
                setConfigurationFileRelativePathTest.run("config/configuration.json");
            });

            runner.testGroup("setConfigurationFileRelativePath(Path)", () ->
            {
                final Action2<Path,Throwable> setConfigurationFileRelativePathErrorTest = (Path configurationFileRelativePath, Throwable expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(configurationFileRelativePath), (Test test) ->
                    {
                        final CommandLineConfigurationActionParameters parameters = CommandLineConfigurationActionParameters.create();
                        test.assertThrows(() -> parameters.setConfigurationFileRelativePath(configurationFileRelativePath),
                            expected);
                        test.assertNull(parameters.getConfigurationFileRelativePath());
                    });
                };

                setConfigurationFileRelativePathErrorTest.run(null, new PreConditionFailure("configurationFileRelativePath cannot be null."));
                setConfigurationFileRelativePathErrorTest.run(Path.parse("/hello"), new PreConditionFailure("configurationFileRelativePath.isRooted() cannot be true."));

                final Action1<Path> setConfigurationFileRelativePathTest = (Path configurationFileRelativePath) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(configurationFileRelativePath), (Test test) ->
                    {
                        final CommandLineConfigurationActionParameters parameters = CommandLineConfigurationActionParameters.create();

                        final CommandLineConfigurationActionParameters setConfigurationFileRelativePathResult = parameters.setConfigurationFileRelativePath(configurationFileRelativePath);
                        test.assertSame(parameters, setConfigurationFileRelativePathResult);
                        test.assertEqual(configurationFileRelativePath, parameters.getConfigurationFileRelativePath());
                    });
                };

                setConfigurationFileRelativePathTest.run(Path.parse("hello"));
                setConfigurationFileRelativePathTest.run(Path.parse("configuration.json"));
                setConfigurationFileRelativePathTest.run(Path.parse("config/configuration.json"));
            });

            runner.testGroup("setConfigurationSchema(JSONSchema)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final CommandLineConfigurationActionParameters parameters = CommandLineConfigurationActionParameters.create();
                    test.assertThrows(() -> parameters.setConfigurationSchema(null),
                        new PreConditionFailure("configurationSchema cannot be null."));
                    test.assertNull(parameters.getConfigurationSchema());
                });

                runner.test("with non-null", (Test test) ->
                {
                    final CommandLineConfigurationActionParameters parameters = CommandLineConfigurationActionParameters.create();
                    final JSONSchema configurationSchema = JSONSchema.create();
                    final CommandLineConfigurationActionParameters setConfigurationSchemaResult = parameters.setConfigurationSchema(configurationSchema);
                    test.assertSame(parameters, setConfigurationSchemaResult);
                    test.assertEqual(configurationSchema, parameters.getConfigurationSchema());
                });
            });

            runner.testGroup("setDefaultConfiguration(JSONObject)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final CommandLineConfigurationActionParameters parameters = CommandLineConfigurationActionParameters.create();
                    test.assertThrows(() -> parameters.setDefaultConfiguration(null),
                        new PreConditionFailure("defaultConfiguration cannot be null."));
                    test.assertNull(parameters.getConfigurationSchema());
                });

                runner.test("with non-null", (Test test) ->
                {
                    final CommandLineConfigurationActionParameters parameters = CommandLineConfigurationActionParameters.create();
                    final JSONObject defaultConfiguration = JSONObject.create();
                    final CommandLineConfigurationActionParameters setDefaultConfigurationResult = parameters.setDefaultConfiguration(defaultConfiguration);
                    test.assertSame(parameters, setDefaultConfigurationResult);
                    test.assertEqual(defaultConfiguration, parameters.getDefaultConfiguration());
                });
            });
        });
    }
}
