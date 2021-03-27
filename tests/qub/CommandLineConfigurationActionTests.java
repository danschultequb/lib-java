package qub;

public interface CommandLineConfigurationActionTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(CommandLineConfigurationAction.class, () ->
        {
            runner.testGroup("addAction(CommandLineActions)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> CommandLineConfigurationAction.addAction(null),
                        new PreConditionFailure("actions cannot be null."));
                });

                runner.test("with non-null", (Test test) ->
                {
                    final CommandLineActions actions = CommandLineActions.create();
                    final CommandLineAction action = CommandLineConfigurationAction.addAction(actions);
                    test.assertNotNull(action);
                    test.assertEqual("configuration", action.getName());
                    test.assertEqual("Open the configuration file for this application.", action.getDescription());
                    test.assertEqual(Iterable.create("config"), action.getAliases());
                    test.assertTrue(actions.containsActionName("configuration"));
                });
            });

            runner.testGroup("addAction(CommandLineActions,CommandLineConfigurationActionParameters)", () ->
            {
                runner.test("with null actions", (Test test) ->
                {
                    final CommandLineActions actions = null;
                    final CommandLineConfigurationActionParameters parameters = CommandLineConfigurationActionParameters.create();

                    test.assertThrows(() -> CommandLineConfigurationAction.addAction(actions, parameters),
                        new PreConditionFailure("actions cannot be null."));
                });

                runner.test("with null parameters", (Test test) ->
                {
                    final CommandLineActions actions = CommandLineActions.create();
                    final CommandLineConfigurationActionParameters parameters = null;

                    test.assertThrows(() -> CommandLineConfigurationAction.addAction(actions, parameters),
                        new PreConditionFailure("parameters cannot be null."));
                });

                runner.test("with non-null arguments", (Test test) ->
                {
                    final CommandLineActions actions = CommandLineActions.create();
                    final CommandLineConfigurationActionParameters parameters = CommandLineConfigurationActionParameters.create();
                    final CommandLineAction action = CommandLineConfigurationAction.addAction(actions, parameters);
                    test.assertNotNull(action);
                    test.assertEqual("configuration", action.getName());
                    test.assertEqual("Open the configuration file for this application.", action.getDescription());
                    test.assertEqual(Iterable.create("config"), action.getAliases());
                    test.assertTrue(actions.containsActionName("configuration"));
                    test.assertNull(parameters.getDataFolder());
                    test.assertNull(parameters.getDefaultApplicationLauncher());
                });
            });

            runner.testGroup("getParameters(DesktopProcess,CommandLineConfigurationActionParameters)", () ->
            {
                runner.test("with null process", (Test test) ->
                {
                    final FakeDesktopProcess process = null;
                    final CommandLineAction action = CommandLineAction.create("full-action-name", (DesktopProcess actionProcess) -> {});
                    final CommandLineConfigurationActionParameters parameters = CommandLineConfigurationActionParameters.create();
                    test.assertThrows(() -> CommandLineConfigurationAction.getParameters(process, action, parameters),
                        new PreConditionFailure("process cannot be null."));
                });

                runner.test("with null action", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final CommandLineConfigurationActionParameters parameters = CommandLineConfigurationActionParameters.create();
                        final CommandLineAction action = null;
                        test.assertThrows(() -> CommandLineConfigurationAction.getParameters(process, action, parameters),
                            new PreConditionFailure("action cannot be null."));
                    }
                });

                runner.test("with null parameters", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final CommandLineAction action = CommandLineAction.create("full-action-name", (DesktopProcess actionProcess) -> {});
                        final CommandLineConfigurationActionParameters parameters = null;
                        test.assertThrows(() -> CommandLineConfigurationAction.getParameters(process, action, parameters),
                            new PreConditionFailure("parameters cannot be null."));
                    }
                });

                runner.test("with --help", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create("--help"))
                    {
                        final CommandLineAction action = CommandLineAction.create("my-application config", (DesktopProcess actionProcess) -> {})
                            .setDescription("Open the configuration file for this application.");
                        final CommandLineConfigurationActionParameters parameters = CommandLineConfigurationActionParameters.create();

                        final CommandLineConfigurationActionParameters getParametersResult = CommandLineConfigurationAction.getParameters(process, action, parameters);
                        test.assertNull(getParametersResult);

                        test.assertLinesEqual(
                            Iterable.create(
                                "Usage: my-application config [--help]",
                                "  Open the configuration file for this application.",
                                "  --help(?): Show the help message for this application."
                            ),
                            process.getOutputWriteStream());
                    }
                });

                runner.test("with empty parameters", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final CommandLineAction action = CommandLineAction.create("my-application config", (DesktopProcess actionProcess) -> {});
                        final CommandLineConfigurationActionParameters parameters = CommandLineConfigurationActionParameters.create();

                        final CommandLineConfigurationActionParameters getParametersResult = CommandLineConfigurationAction.getParameters(process, action, parameters);
                        test.assertSame(parameters, getParametersResult);
                        test.assertEqual(process.getFileSystem().getFolder("/qub/fake-publisher/fake-project/data/").await(), parameters.getDataFolder());
                        test.assertSame(process.getDefaultApplicationLauncher(), parameters.getDefaultApplicationLauncher());
                        test.assertEqual(null, parameters.getConfigurationSchemaFileRelativePath());
                        test.assertEqual(Path.parse("configuration.json"), parameters.getConfigurationFileRelativePath());
                        test.assertNull(parameters.getConfigurationSchema());
                        test.assertNull(parameters.getDefaultConfiguration());
                        test.assertEqual("", process.getOutputWriteStream().getText().await());
                    }
                });

                runner.test("with data folder", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final CommandLineAction action = CommandLineAction.create("my-application config", (DesktopProcess actionProcess) -> {});
                        final InMemoryFileSystem fileSystem = process.getFileSystem();
                        final Folder dataFolder = fileSystem.getFolder("/data/folder/").await();
                        final CommandLineConfigurationActionParameters parameters = CommandLineConfigurationActionParameters.create()
                            .setDataFolder(dataFolder);

                        final CommandLineConfigurationActionParameters getParametersResult = CommandLineConfigurationAction.getParameters(process, action, parameters);
                        test.assertSame(parameters, getParametersResult);
                        test.assertEqual(dataFolder, parameters.getDataFolder());
                        test.assertSame(process.getDefaultApplicationLauncher(), parameters.getDefaultApplicationLauncher());
                        test.assertEqual(null, parameters.getConfigurationSchemaFileRelativePath());
                        test.assertEqual(Path.parse("configuration.json"), parameters.getConfigurationFileRelativePath());
                        test.assertNull(parameters.getConfigurationSchema());
                        test.assertNull(parameters.getDefaultConfiguration());
                        test.assertEqual("", process.getOutputWriteStream().getText().await());
                    }
                });

                runner.test("with default application launcher", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final CommandLineAction action = CommandLineAction.create("my-application config", (DesktopProcess actionProcess) -> {});
                        final FakeDefaultApplicationLauncher defaultApplicationLauncher = process.getDefaultApplicationLauncher();
                        final CommandLineConfigurationActionParameters parameters = CommandLineConfigurationActionParameters.create()
                            .setDefaultApplicationLauncher(defaultApplicationLauncher);

                        final CommandLineConfigurationActionParameters getParametersResult = CommandLineConfigurationAction.getParameters(process, action, parameters);
                        test.assertSame(parameters, getParametersResult);
                        test.assertEqual(process.getFileSystem().getFolder("/qub/fake-publisher/fake-project/data/").await(), parameters.getDataFolder());
                        test.assertSame(defaultApplicationLauncher, parameters.getDefaultApplicationLauncher());
                        test.assertEqual(null, parameters.getConfigurationSchemaFileRelativePath());
                        test.assertEqual(Path.parse("configuration.json"), parameters.getConfigurationFileRelativePath());
                        test.assertNull(parameters.getConfigurationSchema());
                        test.assertNull(parameters.getDefaultConfiguration());
                        test.assertEqual("", process.getOutputWriteStream().getText().await());
                    }
                });

                runner.test("with null configuration schema file relative path and non-null configuration schema", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final CommandLineAction action = CommandLineAction.create("my-application config", (DesktopProcess actionProcess) -> {});
                        final JSONSchema configurationSchema = JSONSchema.create();
                        final CommandLineConfigurationActionParameters parameters = CommandLineConfigurationActionParameters.create()
                            .setConfigurationSchema(configurationSchema);

                        final CommandLineConfigurationActionParameters getParametersResult = CommandLineConfigurationAction.getParameters(process, action, parameters);
                        test.assertSame(parameters, getParametersResult);
                        test.assertEqual(process.getFileSystem().getFolder("/qub/fake-publisher/fake-project/data/").await(), parameters.getDataFolder());
                        test.assertSame(process.getDefaultApplicationLauncher(), parameters.getDefaultApplicationLauncher());
                        test.assertEqual(Path.parse("configuration.schema.json"), parameters.getConfigurationSchemaFileRelativePath());
                        test.assertEqual(Path.parse("configuration.json"), parameters.getConfigurationFileRelativePath());
                        test.assertEqual(configurationSchema, parameters.getConfigurationSchema());
                        test.assertNull(parameters.getDefaultConfiguration());
                        test.assertEqual("", process.getOutputWriteStream().getText().await());
                    }
                });

                runner.test("with non-null configuration schema file relative path and null configuration schema", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final CommandLineAction action = CommandLineAction.create("my-application config", (DesktopProcess actionProcess) -> {});
                        final Path configurationSchemaFileRelativePath = Path.parse("fake-configuration.schema.json");
                        final CommandLineConfigurationActionParameters parameters = CommandLineConfigurationActionParameters.create()
                            .setConfigurationSchemaFileRelativePath(configurationSchemaFileRelativePath);

                        final CommandLineConfigurationActionParameters getParametersResult = CommandLineConfigurationAction.getParameters(process, action, parameters);
                        test.assertSame(parameters, getParametersResult);
                        test.assertEqual(process.getFileSystem().getFolder("/qub/fake-publisher/fake-project/data/").await(), parameters.getDataFolder());
                        test.assertSame(process.getDefaultApplicationLauncher(), parameters.getDefaultApplicationLauncher());
                        test.assertEqual(configurationSchemaFileRelativePath, parameters.getConfigurationSchemaFileRelativePath());
                        test.assertEqual(Path.parse("configuration.json"), parameters.getConfigurationFileRelativePath());
                        test.assertNull(parameters.getConfigurationSchema());
                        test.assertNull(parameters.getDefaultConfiguration());
                        test.assertEqual("", process.getOutputWriteStream().getText().await());
                    }
                });

                runner.test("with non-null configuration file relative path", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final CommandLineAction action = CommandLineAction.create("my-application config", (DesktopProcess actionProcess) -> {});
                        final Path configurationFileRelativePath = Path.parse("fake-configuration.json");
                        final CommandLineConfigurationActionParameters parameters = CommandLineConfigurationActionParameters.create()
                            .setConfigurationFileRelativePath(configurationFileRelativePath);

                        final CommandLineConfigurationActionParameters getParametersResult = CommandLineConfigurationAction.getParameters(process, action, parameters);
                        test.assertSame(parameters, getParametersResult);
                        test.assertEqual(process.getFileSystem().getFolder("/qub/fake-publisher/fake-project/data/").await(), parameters.getDataFolder());
                        test.assertSame(process.getDefaultApplicationLauncher(), parameters.getDefaultApplicationLauncher());
                        test.assertEqual(null, parameters.getConfigurationSchemaFileRelativePath());
                        test.assertEqual(configurationFileRelativePath, parameters.getConfigurationFileRelativePath());
                        test.assertNull(parameters.getConfigurationSchema());
                        test.assertNull(parameters.getDefaultConfiguration());
                        test.assertEqual("", process.getOutputWriteStream().getText().await());
                    }
                });
            });

            runner.testGroup("run(CommandLineConfigurationActionParameters)", () ->
            {
                runner.test("with null parameters", (Test test) ->
                {
                    test.assertThrows(() -> CommandLineConfigurationAction.run(null),
                        new PreConditionFailure("parameters cannot be null."));
                });

                runner.test("with null data folder", (Test test) ->
                {
                    final CommandLineConfigurationActionParameters parameters = CommandLineConfigurationActionParameters.create();
                    test.assertThrows(() -> CommandLineConfigurationAction.run(parameters),
                        new PreConditionFailure("parameters.getDataFolder() cannot be null."));
                });

                runner.test("with null default application launcher", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = InMemoryFileSystem.create();
                    fileSystem.createRoot("/").await();
                    final Folder dataFolder = fileSystem.getFolder("/data/folder/").await();
                    final CommandLineConfigurationActionParameters parameters = CommandLineConfigurationActionParameters.create()
                        .setDataFolder(dataFolder);
                    test.assertThrows(() -> CommandLineConfigurationAction.run(parameters),
                        new PreConditionFailure("parameters.getDefaultApplicationLauncher() cannot be null."));
                });

                runner.test("with null configuration file relative path", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = InMemoryFileSystem.create();
                    fileSystem.createRoot("/").await();
                    final Folder dataFolder = fileSystem.getFolder("/data/folder/").await();
                    final FakeDefaultApplicationLauncher defaultApplicationLauncher = FakeDefaultApplicationLauncher.create(fileSystem);
                    final CommandLineConfigurationActionParameters parameters = CommandLineConfigurationActionParameters.create()
                        .setDataFolder(dataFolder)
                        .setDefaultApplicationLauncher(defaultApplicationLauncher);
                    test.assertThrows(() -> CommandLineConfigurationAction.run(parameters),
                        new PreConditionFailure("parameters.getConfigurationFileRelativePath() cannot be null."));
                });

                runner.test("with null configuration file schema relative path, null configuration schema, and null default configuration", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = InMemoryFileSystem.create();
                    fileSystem.createRoot("/").await();
                    final Folder dataFolder = fileSystem.getFolder("/data/folder/").await();
                    final FakeDefaultApplicationLauncher defaultApplicationLauncher = FakeDefaultApplicationLauncher.create(fileSystem);
                    final Path configurationFileRelativePath = Path.parse("config/c.json");
                    final CommandLineConfigurationActionParameters parameters = CommandLineConfigurationActionParameters.create()
                        .setDataFolder(dataFolder)
                        .setDefaultApplicationLauncher(defaultApplicationLauncher)
                        .setConfigurationFileRelativePath(configurationFileRelativePath);

                    CommandLineConfigurationAction.run(parameters);

                    final File configurationFile = dataFolder.getFile(configurationFileRelativePath).await();
                    test.assertEqual(
                        Iterable.create(
                            dataFolder.getFolder("config").await(),
                            configurationFile),
                        dataFolder.getFilesAndFoldersRecursively().await());
                    test.assertEqual("", configurationFile.getContentsAsString().await());
                });

                runner.test("with null configuration file schema relative path, null configuration schema, and non-null default configuration", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = InMemoryFileSystem.create();
                    fileSystem.createRoot("/").await();
                    final Folder dataFolder = fileSystem.getFolder("/data/folder/").await();
                    final FakeDefaultApplicationLauncher defaultApplicationLauncher = FakeDefaultApplicationLauncher.create(fileSystem);
                    final Path configurationFileRelativePath = Path.parse("config/c.json");
                    final JSONObject defaultConfiguration = JSONObject.create()
                        .setString("a", "b");
                    final CommandLineConfigurationActionParameters parameters = CommandLineConfigurationActionParameters.create()
                        .setDataFolder(dataFolder)
                        .setDefaultApplicationLauncher(defaultApplicationLauncher)
                        .setConfigurationFileRelativePath(configurationFileRelativePath)
                        .setDefaultConfiguration(defaultConfiguration);

                    CommandLineConfigurationAction.run(parameters);

                    final File configurationFile = dataFolder.getFile(configurationFileRelativePath).await();
                    test.assertEqual(
                        Iterable.create(
                            dataFolder.getFolder("config").await(),
                            configurationFile),
                        dataFolder.getFilesAndFoldersRecursively().await());
                    test.assertEqual(defaultConfiguration.toString(JSONFormat.pretty), configurationFile.getContentsAsString().await());
                });

                runner.test("with non-null configuration file schema relative path, null configuration schema, and null default configuration", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = InMemoryFileSystem.create();
                    fileSystem.createRoot("/").await();
                    final Folder dataFolder = fileSystem.getFolder("/data/folder/").await();
                    final FakeDefaultApplicationLauncher defaultApplicationLauncher = FakeDefaultApplicationLauncher.create(fileSystem);
                    final Path configurationFileRelativePath = Path.parse("config/c.json");
                    final Path configurationSchemaFileRelativePath = Path.parse("schemas/c.schema.json");
                    final CommandLineConfigurationActionParameters parameters = CommandLineConfigurationActionParameters.create()
                        .setDataFolder(dataFolder)
                        .setDefaultApplicationLauncher(defaultApplicationLauncher)
                        .setConfigurationFileRelativePath(configurationFileRelativePath)
                        .setConfigurationSchemaFileRelativePath(configurationSchemaFileRelativePath);

                    CommandLineConfigurationAction.run(parameters);

                    final File configurationFile = dataFolder.getFile(configurationFileRelativePath).await();
                    final File configurationSchemaFile = dataFolder.getFile(configurationSchemaFileRelativePath).await();
                    test.assertEqual(
                        Iterable.create(
                            dataFolder.getFolder("config").await(),
                            configurationFile),
                        dataFolder.getFilesAndFoldersRecursively().await());
                    test.assertEqual("", configurationFile.getContentsAsString().await());
                });

                runner.test("with null configuration file schema relative path, non-null configuration schema, and null default configuration", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = InMemoryFileSystem.create();
                    fileSystem.createRoot("/").await();
                    final Folder dataFolder = fileSystem.getFolder("/data/folder/").await();
                    final FakeDefaultApplicationLauncher defaultApplicationLauncher = FakeDefaultApplicationLauncher.create(fileSystem);
                    final Path configurationFileRelativePath = Path.parse("config/c.json");
                    final JSONSchema configurationSchema = JSONSchema.create()
                        .setDescription("hello");
                    final CommandLineConfigurationActionParameters parameters = CommandLineConfigurationActionParameters.create()
                        .setDataFolder(dataFolder)
                        .setDefaultApplicationLauncher(defaultApplicationLauncher)
                        .setConfigurationFileRelativePath(configurationFileRelativePath)
                        .setConfigurationSchema(configurationSchema);

                    CommandLineConfigurationAction.run(parameters);

                    final File configurationFile = dataFolder.getFile(configurationFileRelativePath).await();
                    test.assertEqual(
                        Iterable.create(
                            dataFolder.getFolder("config").await(),
                            configurationFile),
                        dataFolder.getFilesAndFoldersRecursively().await());
                    test.assertEqual("", configurationFile.getContentsAsString().await());
                });

                runner.test("with non-null configuration file schema relative path, non-null configuration schema, and non-null default configuration", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = InMemoryFileSystem.create();
                    fileSystem.createRoot("/").await();
                    final Folder dataFolder = fileSystem.getFolder("/data/folder/").await();
                    final FakeDefaultApplicationLauncher defaultApplicationLauncher = FakeDefaultApplicationLauncher.create(fileSystem);
                    final Path configurationFileRelativePath = Path.parse("config/c.json");
                    final Path configurationSchemaFileRelativePath = Path.parse("schemas/c.schema.json");
                    final JSONSchema configurationSchema = JSONSchema.create()
                        .setDescription("hello");
                    final JSONObject defaultConfiguration = JSONObject.create()
                        .setString("a", "b");
                    final CommandLineConfigurationActionParameters parameters = CommandLineConfigurationActionParameters.create()
                        .setDataFolder(dataFolder)
                        .setDefaultApplicationLauncher(defaultApplicationLauncher)
                        .setConfigurationFileRelativePath(configurationFileRelativePath)
                        .setConfigurationSchemaFileRelativePath(configurationSchemaFileRelativePath)
                        .setConfigurationSchema(configurationSchema)
                        .setDefaultConfiguration(defaultConfiguration);

                    CommandLineConfigurationAction.run(parameters);

                    final File configurationFile = dataFolder.getFile(configurationFileRelativePath).await();
                    final File configurationSchemaFile = dataFolder.getFile(configurationSchemaFileRelativePath).await();
                    test.assertEqual(
                        Iterable.create(
                            dataFolder.getFolder("config").await(),
                            dataFolder.getFolder("schemas").await(),
                            configurationFile,
                            configurationSchemaFile),
                        dataFolder.getFilesAndFoldersRecursively().await());
                    test.assertEqual(defaultConfiguration.toString(JSONFormat.pretty), configurationFile.getContentsAsString().await());
                    test.assertEqual(configurationSchema.toString(JSONFormat.pretty), configurationSchemaFile.getContentsAsString().await());
                });

                runner.test("with existing configuration file", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = InMemoryFileSystem.create();
                    fileSystem.createRoot("/").await();
                    final Folder dataFolder = fileSystem.getFolder("/data/folder/").await();
                    final FakeDefaultApplicationLauncher defaultApplicationLauncher = FakeDefaultApplicationLauncher.create(fileSystem);
                    final Path configurationFileRelativePath = Path.parse("config/c.json");
                    final Path configurationSchemaFileRelativePath = Path.parse("schemas/c.schema.json");
                    final JSONSchema configurationSchema = JSONSchema.create()
                        .setDescription("hello");
                    final JSONObject defaultConfiguration = JSONObject.create()
                        .setString("a", "b");
                    final CommandLineConfigurationActionParameters parameters = CommandLineConfigurationActionParameters.create()
                        .setDataFolder(dataFolder)
                        .setDefaultApplicationLauncher(defaultApplicationLauncher)
                        .setConfigurationFileRelativePath(configurationFileRelativePath)
                        .setConfigurationSchemaFileRelativePath(configurationSchemaFileRelativePath)
                        .setConfigurationSchema(configurationSchema)
                        .setDefaultConfiguration(defaultConfiguration);

                    final File configurationFile = dataFolder.getFile(configurationFileRelativePath).await();
                    configurationFile.setContentsAsString("hello").await();

                    CommandLineConfigurationAction.run(parameters);

                    final File configurationSchemaFile = dataFolder.getFile(configurationSchemaFileRelativePath).await();
                    test.assertEqual(
                        Iterable.create(
                            dataFolder.getFolder("config").await(),
                            dataFolder.getFolder("schemas").await(),
                            configurationFile,
                            configurationSchemaFile),
                        dataFolder.getFilesAndFoldersRecursively().await());
                    test.assertEqual("hello", configurationFile.getContentsAsString().await());
                    test.assertEqual(configurationSchema.toString(JSONFormat.pretty), configurationSchemaFile.getContentsAsString().await());
                });

                runner.test("with existing configuration schema file", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = InMemoryFileSystem.create();
                    fileSystem.createRoot("/").await();
                    final Folder dataFolder = fileSystem.getFolder("/data/folder/").await();
                    final FakeDefaultApplicationLauncher defaultApplicationLauncher = FakeDefaultApplicationLauncher.create(fileSystem);
                    final Path configurationFileRelativePath = Path.parse("config/c.json");
                    final Path configurationSchemaFileRelativePath = Path.parse("schemas/c.schema.json");
                    final JSONSchema configurationSchema = JSONSchema.create()
                        .setDescription("hello");
                    final JSONObject defaultConfiguration = JSONObject.create()
                        .setString("a", "b");
                    final CommandLineConfigurationActionParameters parameters = CommandLineConfigurationActionParameters.create()
                        .setDataFolder(dataFolder)
                        .setDefaultApplicationLauncher(defaultApplicationLauncher)
                        .setConfigurationFileRelativePath(configurationFileRelativePath)
                        .setConfigurationSchemaFileRelativePath(configurationSchemaFileRelativePath)
                        .setConfigurationSchema(configurationSchema)
                        .setDefaultConfiguration(defaultConfiguration);

                    final File configurationSchemaFile = dataFolder.getFile(configurationSchemaFileRelativePath).await();
                    configurationSchemaFile.setContentsAsString("hello").await();

                    CommandLineConfigurationAction.run(parameters);

                    final File configurationFile = dataFolder.getFile(configurationFileRelativePath).await();
                    test.assertEqual(
                        Iterable.create(
                            dataFolder.getFolder("config").await(),
                            dataFolder.getFolder("schemas").await(),
                            configurationFile,
                            configurationSchemaFile),
                        dataFolder.getFilesAndFoldersRecursively().await());
                    test.assertEqual(defaultConfiguration.toString(JSONFormat.pretty), configurationFile.getContentsAsString().await());
                    test.assertEqual(configurationSchema.toString(JSONFormat.pretty), configurationSchemaFile.getContentsAsString().await());
                });
            });
        });
    }
}
