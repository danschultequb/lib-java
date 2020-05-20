package qub;

public interface CommandLineParametersTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(CommandLineParameters.class, () ->
        {
            runner.test("constructor()", (Test test) ->
            {
                final CommandLineParameters parameters = new CommandLineParameters();
                test.assertNotNull(parameters);
            });

            runner.testGroup("setArguments(CommandLineArguments)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final CommandLineParameters parameters = new CommandLineParameters();
                    test.assertThrows(() -> parameters.setArguments(null),
                        new PreConditionFailure("arguments cannot be null."));
                });

                runner.test("with no parameters", (Test test) ->
                {
                    final CommandLineArguments arguments = CommandLineArguments.create();
                    final CommandLineParameters parameters = new CommandLineParameters();
                    test.assertSame(parameters, parameters.setArguments(arguments));
                });

                runner.test("with existing parameters", (Test test) ->
                {
                    final CommandLineArguments arguments = CommandLineArguments.create();
                    final CommandLineParameters parameters = new CommandLineParameters();
                    final CommandLineParameter<String> parameter = parameters.addString("hello");
                    test.assertNull(parameter.getArguments());
                    test.assertSame(parameters, parameters.setArguments(arguments));
                    test.assertSame(arguments, parameter.getArguments());
                });
            });

            runner.testGroup("addString(String)", () ->
            {
                runner.test("with null parameterName", (Test test) ->
                {
                    final CommandLineParameters parameters = new CommandLineParameters();
                    test.assertThrows(() -> parameters.addString(null),
                        new PreConditionFailure("parameterName cannot be null."));
                });

                runner.test("with empty parameterName", (Test test) ->
                {
                    final CommandLineParameters parameters = new CommandLineParameters();
                    test.assertThrows(() -> parameters.addString(""),
                        new PreConditionFailure("parameterName cannot be empty."));
                });

                runner.test("with non-empty parameterName", (Test test) ->
                {
                    final CommandLineParameters parameters = new CommandLineParameters();
                    final CommandLineParameter<String> parameter = parameters.addString("fakeName");
                    test.assertNotNull(parameter);
                    test.assertEqual("fakeName", parameter.getName());
                    test.assertNull(parameter.getDescription());
                    test.assertNull(parameter.getIndex());
                    test.assertEqual(Iterable.create(), parameter.getAliases());
                    test.assertFalse(parameter.isRequired());
                    test.assertTrue(parameter.isValueRequired());
                    test.assertNull(parameter.getArguments());
                });
            });

            runner.testGroup("addString(String,String)", () ->
            {
                runner.test("with null parameterName", (Test test) ->
                {
                    final CommandLineParameters parameters = new CommandLineParameters();
                    test.assertThrows(() -> parameters.addString(null, "hello"),
                        new PreConditionFailure("parameterName cannot be null."));
                });

                runner.test("with empty parameterName", (Test test) ->
                {
                    final CommandLineParameters parameters = new CommandLineParameters();
                    test.assertThrows(() -> parameters.addString("", "hello"),
                        new PreConditionFailure("parameterName cannot be empty."));
                });

                runner.test("with non-empty parameterName", (Test test) ->
                {
                    final CommandLineParameters parameters = new CommandLineParameters();
                    final CommandLineParameter<String> parameter = parameters.addString("fakeName", "hello");
                    test.assertNotNull(parameter);
                    test.assertEqual("fakeName", parameter.getName());
                    test.assertNull(parameter.getDescription());
                    test.assertNull(parameter.getIndex());
                    test.assertEqual(Iterable.create(), parameter.getAliases());
                    test.assertFalse(parameter.isRequired());
                    test.assertTrue(parameter.isValueRequired());
                    test.assertNull(parameter.getArguments());
                });
            });

            runner.testGroup("addPositionString(String)", () ->
            {
                runner.test("with null parameterName", (Test test) ->
                {
                    final CommandLineParameters parameters = new CommandLineParameters();
                    test.assertThrows(() -> parameters.addPositionString(null),
                        new PreConditionFailure("parameterName cannot be null."));
                });

                runner.test("with empty parameterName", (Test test) ->
                {
                    final CommandLineParameters parameters = new CommandLineParameters();
                    test.assertThrows(() -> parameters.addPositionString(""),
                        new PreConditionFailure("parameterName cannot be empty."));
                });

                runner.test("with non-empty parameterName and non-empty parameterDescription", (Test test) ->
                {
                    final CommandLineParameters parameters = new CommandLineParameters();
                    final CommandLineParameter<String> parameter = parameters.addPositionString("fakeName");
                    test.assertNotNull(parameter);
                    test.assertEqual("fakeName", parameter.getName());
                    test.assertNull(parameter.getDescription());
                    test.assertEqual(0, parameter.getIndex());
                    test.assertEqual(Iterable.create(), parameter.getAliases());
                    test.assertFalse(parameter.isRequired());
                });

                runner.test("with multiple parameters", (Test test) ->
                {
                    final CommandLineParameters parameters = new CommandLineParameters();

                    final CommandLineParameter<String> parameter1 = parameters.addPositionString("fakeName1");
                    test.assertEqual("fakeName1", parameter1.getName());
                    test.assertEqual(0, parameter1.getIndex());

                    final CommandLineParameter<String> parameter2 = parameters.addPositionString("fakeName2");
                    test.assertEqual("fakeName2", parameter2.getName());
                    test.assertEqual(1, parameter2.getIndex());
                });
            });

            runner.testGroup("addBoolean(String)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final CommandLineParameters parameters = new CommandLineParameters();
                    test.assertThrows(() -> parameters.addBoolean(null),
                        new PreConditionFailure("parameterName cannot be null."));
                });

                runner.test("with empty", (Test test) ->
                {
                    final CommandLineParameters parameters = new CommandLineParameters();
                    test.assertThrows(() -> parameters.addBoolean(""),
                        new PreConditionFailure("parameterName cannot be empty."));
                });

                runner.test("with non-empty name that doesn't exist in arguments", (Test test) ->
                {
                    final CommandLineArguments arguments = CommandLineArguments.create();
                    final CommandLineParameters parameters = new CommandLineParameters().setArguments(arguments);
                    final CommandLineParameterBoolean parameter = parameters.addBoolean("a");
                    test.assertFalse(parameter.getValue().await());
                });

                runner.test("with non-empty name that doesn't have a value", (Test test) ->
                {
                    final CommandLineArguments arguments = CommandLineArguments.create("--a");
                    final CommandLineParameters parameters = new CommandLineParameters().setArguments(arguments);
                    final CommandLineParameterBoolean parameter = parameters.addBoolean("a");
                    test.assertTrue(parameter.getValue().await());
                });

                runner.test("with non-empty name that has an empty value", (Test test) ->
                {
                    final CommandLineArguments arguments = CommandLineArguments.create("--a=");
                    final CommandLineParameters parameters = new CommandLineParameters().setArguments(arguments);
                    final CommandLineParameterBoolean parameter = parameters.addBoolean("a");
                    test.assertTrue(parameter.getValue().await());
                });

                runner.test("with non-empty name that has a \"false\" value", (Test test) ->
                {
                    final CommandLineArguments arguments = CommandLineArguments.create("--a=false");
                    final CommandLineParameters parameters = new CommandLineParameters().setArguments(arguments);
                    final CommandLineParameterBoolean parameter = parameters.addBoolean("a");
                    test.assertFalse(parameter.getValue().await());
                });

                runner.test("with non-empty name that has a \"FALSE\" value", (Test test) ->
                {
                    final CommandLineArguments arguments = CommandLineArguments.create("--a=FALSE");
                    final CommandLineParameters parameters = new CommandLineParameters().setArguments(arguments);
                    final CommandLineParameterBoolean parameter = parameters.addBoolean("a");
                    test.assertThrows(() -> parameter.getValue().await(),
                        new ParseException("Expected the value (\"FALSE\") to be either \"true\" or \"false\"."));
                });

                runner.test("with non-empty name that has a \"true\" value", (Test test) ->
                {
                    final CommandLineArguments arguments = CommandLineArguments.create("--a=true");
                    final CommandLineParameters parameters = new CommandLineParameters().setArguments(arguments);
                    final CommandLineParameterBoolean parameter = parameters.addBoolean("a");
                    test.assertTrue(parameter.getValue().await());
                });

                runner.test("with non-empty name that has a \"TRUE\" value", (Test test) ->
                {
                    final CommandLineArguments arguments = CommandLineArguments.create("--a=TRUE");
                    final CommandLineParameters parameters = new CommandLineParameters().setArguments(arguments);
                    final CommandLineParameterBoolean parameter = parameters.addBoolean("a");
                    test.assertThrows(() -> parameter.getValue().await(),
                        new ParseException("Expected the value (\"TRUE\") to be either \"true\" or \"false\"."));
                });

                runner.test("with non-empty name that has a \"10\" value", (Test test) ->
                {
                    final CommandLineArguments arguments = CommandLineArguments.create("--a=10");
                    final CommandLineParameters parameters = new CommandLineParameters().setArguments(arguments);
                    final CommandLineParameterBoolean parameter = parameters.addBoolean("a");
                    test.assertThrows(() -> parameter.getValue().await(),
                        new ParseException("Expected the value (\"10\") to be either \"true\" or \"false\"."));
                });
            });

            runner.testGroup("addBoolean(String,boolean)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final CommandLineParameters parameters = new CommandLineParameters();
                    test.assertThrows(() -> parameters.addBoolean(null, false),
                        new PreConditionFailure("parameterName cannot be null."));
                });

                runner.test("with empty", (Test test) ->
                {
                    final CommandLineParameters parameters = new CommandLineParameters();
                    test.assertThrows(() -> parameters.addBoolean("", true),
                        new PreConditionFailure("parameterName cannot be empty."));
                });

                runner.test("with non-empty name that doesn't exist in arguments and false unspecifiedValue", (Test test) ->
                {
                    final CommandLineArguments arguments = CommandLineArguments.create();
                    final CommandLineParameters parameters = new CommandLineParameters().setArguments(arguments);
                    final CommandLineParameterBoolean parameter = parameters.addBoolean("a", false);
                    test.assertFalse(parameter.getValue().await());
                });

                runner.test("with non-empty name that doesn't exist in arguments and true unspecifiedValue", (Test test) ->
                {
                    final CommandLineArguments arguments = CommandLineArguments.create();
                    final CommandLineParameters parameters = new CommandLineParameters().setArguments(arguments);
                    final CommandLineParameterBoolean parameter = parameters.addBoolean("a", true);
                    test.assertTrue(parameter.getValue().await());
                });

                runner.test("with non-empty name that doesn't have a value and false unspecifiedValue", (Test test) ->
                {
                    final CommandLineArguments arguments = CommandLineArguments.create("--a");
                    final CommandLineParameters parameters = new CommandLineParameters().setArguments(arguments);
                    final CommandLineParameterBoolean parameter = parameters.addBoolean("a", false);
                    test.assertTrue(parameter.getValue().await());
                });

                runner.test("with non-empty name that has an empty value and false unspecifiedValue", (Test test) ->
                {
                    final CommandLineArguments arguments = CommandLineArguments.create("--a=");
                    final CommandLineParameters parameters = new CommandLineParameters().setArguments(arguments);
                    final CommandLineParameterBoolean parameter = parameters.addBoolean("a", false);
                    test.assertTrue(parameter.getValue().await());
                });

                runner.test("with non-empty name that has a \"false\" value", (Test test) ->
                {
                    final CommandLineArguments arguments = CommandLineArguments.create("--a=false");
                    final CommandLineParameters parameters = new CommandLineParameters().setArguments(arguments);
                    final CommandLineParameterBoolean parameter = parameters.addBoolean("a", true);
                    test.assertFalse(parameter.getValue().await());
                });

                runner.test("with non-empty name that has a \"FALSE\" value", (Test test) ->
                {
                    final CommandLineArguments arguments = CommandLineArguments.create("--a=FALSE");
                    final CommandLineParameters parameters = new CommandLineParameters().setArguments(arguments);
                    final CommandLineParameterBoolean parameter = parameters.addBoolean("a", true);
                    test.assertThrows(() -> parameter.getValue().await(),
                        new ParseException("Expected the value (\"FALSE\") to be either \"true\" or \"false\"."));
                });

                runner.test("with non-empty name that has a \"true\" value", (Test test) ->
                {
                    final CommandLineArguments arguments = CommandLineArguments.create("--a=true");
                    final CommandLineParameters parameters = new CommandLineParameters().setArguments(arguments);
                    final CommandLineParameterBoolean parameter = parameters.addBoolean("a", false);
                    test.assertTrue(parameter.getValue().await());
                });

                runner.test("with non-empty name that has a \"TRUE\" value", (Test test) ->
                {
                    final CommandLineArguments arguments = CommandLineArguments.create("--a=TRUE");
                    final CommandLineParameters parameters = new CommandLineParameters().setArguments(arguments);
                    final CommandLineParameterBoolean parameter = parameters.addBoolean("a", false);
                    test.assertThrows(() -> parameter.getValue().await(),
                        new ParseException("Expected the value (\"TRUE\") to be either \"true\" or \"false\"."));
                });

                runner.test("with non-empty name that has a \"10\" value", (Test test) ->
                {
                    final CommandLineArguments arguments = CommandLineArguments.create("--a=10");
                    final CommandLineParameters parameters = new CommandLineParameters().setArguments(arguments);
                    final CommandLineParameterBoolean parameter = parameters.addBoolean("a", true);
                    test.assertThrows(() -> parameter.getValue().await(),
                        new ParseException("Expected the value (\"10\") to be either \"true\" or \"false\"."));
                });
            });

            runner.testGroup("addFolder(String,Process)", () ->
            {
                runner.test("with null Process", (Test test) ->
                {
                    final CommandLineParameters parameters = new CommandLineParameters();
                    test.assertThrows(() -> parameters.addFolder("hello", null),
                        new PreConditionFailure("process cannot be null."));
                });

                runner.test("with null", (Test test) ->
                {
                    try (final Process process = Process.create())
                    {
                        final CommandLineParameters parameters = new CommandLineParameters();
                        test.assertThrows(() -> parameters.addFolder(null, process),
                            new PreConditionFailure("parameterName cannot be null."));
                    }
                });

                runner.test("with empty", (Test test) ->
                {
                    try (final Process process = Process.create())
                    {
                        final CommandLineParameters parameters = new CommandLineParameters();
                        test.assertThrows(() -> parameters.addFolder("", process),
                            new PreConditionFailure("parameterName cannot be empty."));
                    }
                });

                runner.test("with non-empty name that doesn't exist in arguments", (Test test) ->
                {
                    try (final Process process = Process.create())
                    {
                        final CommandLineArguments arguments = CommandLineArguments.create();
                        final CommandLineParameters parameters = new CommandLineParameters().setArguments(arguments);
                        final CommandLineParameter<Folder> parameter = parameters.addFolder("a", process);
                        test.assertEqual(process.getCurrentFolder(), parameter.getValue().await());
                    }
                });

                runner.test("with non-empty name that doesn't have a value", (Test test) ->
                {
                    try (final Process process = Process.create())
                    {
                        final CommandLineArguments arguments = CommandLineArguments.create("--a");
                        final CommandLineParameters parameters = new CommandLineParameters().setArguments(arguments);
                        final CommandLineParameter<Folder> parameter = parameters.addFolder("a", process);
                        test.assertEqual(process.getCurrentFolder(), parameter.getValue().await());
                    }
                });

                runner.test("with non-empty name that has an empty value", (Test test) ->
                {
                    try (final Process process = Process.create())
                    {
                        final CommandLineArguments arguments = CommandLineArguments.create("--a=");
                        final CommandLineParameters parameters = new CommandLineParameters().setArguments(arguments);
                        final CommandLineParameter<Folder> parameter = parameters.addFolder("a", process);
                        test.assertEqual(process.getCurrentFolder(), parameter.getValue().await());
                    }
                });

                runner.test("with non-empty name that has a relative file path value", (Test test) ->
                {
                    try (final Process process = Process.create())
                    {
                        final CommandLineArguments arguments = CommandLineArguments.create("--a=folder/subfolder/");
                        final CommandLineParameters parameters = new CommandLineParameters().setArguments(arguments);
                        final CommandLineParameter<Folder> parameter = parameters.addFolder("a", process);
                        test.assertEqual(process.getCurrentFolder().getFolder("folder/subfolder").await(), parameter.getValue().await());
                    }
                });

                runner.test("with non-empty name that has a rooted folder path value", (Test test) ->
                {
                    try (final Process process = Process.create())
                    {
                        final CommandLineArguments arguments = CommandLineArguments.create("--a=/folder/");
                        final CommandLineParameters parameters = new CommandLineParameters().setArguments(arguments);
                        final CommandLineParameter<Folder> parameter = parameters.addFolder("a", process);
                        test.assertEqual(process.getFileSystem().getFolder("/folder/").await(), parameter.getValue().await());
                    }
                });
            });

            runner.testGroup("addPositionalFolder(String,Process)", () ->
            {
                runner.test("with null parameter name", (Test test) ->
                {
                    try (final Process process = Process.create())
                    {
                        final CommandLineParameters parameters = new CommandLineParameters();
                        test.assertThrows(() -> parameters.addPositionalFolder(null, process),
                            new PreConditionFailure("parameterName cannot be null."));
                    }
                });

                runner.test("with empty parameter name", (Test test) ->
                {
                    try (final Process process = Process.create())
                    {
                        final CommandLineParameters parameters = new CommandLineParameters();
                        test.assertThrows(() -> parameters.addPositionalFolder("", process),
                            new PreConditionFailure("parameterName cannot be empty."));
                    }
                });

                runner.test("with null process", (Test test) ->
                {
                    final CommandLineParameters parameters = new CommandLineParameters();
                    test.assertThrows(() -> parameters.addPositionalFolder("hello", null),
                        new PreConditionFailure("process cannot be null."));
                });

                runner.test("with non-empty name that doesn't exist in arguments", (Test test) ->
                {
                    try (final Process process = Process.create())
                    {
                        final CommandLineArguments arguments = CommandLineArguments.create();
                        final CommandLineParameters parameters = new CommandLineParameters().setArguments(arguments);
                        final CommandLineParameter<Folder> parameter = parameters.addPositionalFolder("a", process);
                        test.assertEqual(process.getCurrentFolder(), parameter.getValue().await());
                    }
                });

                runner.test("with non-empty name that has a positional non-empty value", (Test test) ->
                {
                    try (final Process process = Process.create())
                    {
                        final CommandLineArguments arguments = CommandLineArguments.create("testing");
                        final CommandLineParameters parameters = new CommandLineParameters().setArguments(arguments);
                        final CommandLineParameter<Folder> parameter = parameters.addPositionalFolder("a", process);
                        test.assertEqual(process.getCurrentFolder().getFolder("testing").await(), parameter.getValue().await());
                    }
                });

                runner.test("with non-empty name that doesn't have a value", (Test test) ->
                {
                    try (final Process process = Process.create())
                    {
                        final CommandLineArguments arguments = CommandLineArguments.create("--a");
                        final CommandLineParameters parameters = new CommandLineParameters().setArguments(arguments);
                        final CommandLineParameter<Folder> parameter = parameters.addPositionalFolder("a", process);
                        test.assertEqual(process.getCurrentFolder(), parameter.getValue().await());
                    }
                });

                runner.test("with non-empty name that has an empty value", (Test test) ->
                {
                    try (final Process process = Process.create())
                    {
                        final CommandLineArguments arguments = CommandLineArguments.create("--a=");
                        final CommandLineParameters parameters = new CommandLineParameters().setArguments(arguments);
                        final CommandLineParameter<Folder> parameter = parameters.addPositionalFolder("a", process);
                        test.assertEqual(process.getCurrentFolder(), parameter.getValue().await());
                    }
                });

                runner.test("with non-empty name that has a relative file path value", (Test test) ->
                {
                    try (final Process process = Process.create())
                    {
                        final CommandLineArguments arguments = CommandLineArguments.create("--a=folder/subfolder/");
                        final CommandLineParameters parameters = new CommandLineParameters().setArguments(arguments);
                        final CommandLineParameter<Folder> parameter = parameters.addPositionalFolder("a", process);
                        test.assertEqual(process.getCurrentFolder().getFolder("folder/subfolder").await(), parameter.getValue().await());
                    }
                });

                runner.test("with non-empty name that has a rooted folder path value", (Test test) ->
                {
                    try (final Process process = Process.create())
                    {
                        final CommandLineArguments arguments = CommandLineArguments.create("--a=/folder/");
                        final CommandLineParameters parameters = new CommandLineParameters().setArguments(arguments);
                        final CommandLineParameter<Folder> parameter = parameters.addPositionalFolder("a", process);
                        test.assertEqual(process.getFileSystem().getFolder("/folder/").await(), parameter.getValue().await());
                    }
                });
            });

            runner.testGroup("addFile(String,Process)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    try (final Process process = Process.create())
                    {
                        final CommandLineParameters parameters = new CommandLineParameters();
                        test.assertThrows(() -> parameters.addFile(null, process),
                            new PreConditionFailure("parameterName cannot be null."));
                    }
                });

                runner.test("with empty", (Test test) ->
                {
                    try (final Process process = Process.create())
                    {
                        final CommandLineParameters parameters = new CommandLineParameters();
                        test.assertThrows(() -> parameters.addFile("", process),
                            new PreConditionFailure("parameterName cannot be empty."));
                    }
                });

                runner.test("with null process", (Test test) ->
                {
                    final CommandLineParameters parameters = new CommandLineParameters();
                    test.assertThrows(() -> parameters.addFile("a", null),
                        new PreConditionFailure("process cannot be null."));
                });

                runner.test("with non-empty name that doesn't exist in arguments", (Test test) ->
                {
                    try (final Process process = Process.create())
                    {
                        final CommandLineArguments arguments = CommandLineArguments.create();
                        final CommandLineParameters parameters = new CommandLineParameters().setArguments(arguments);
                        final CommandLineParameter<File> parameter = parameters.addFile("a", process);
                        test.assertNull(parameter.getValue().await());
                    }
                });

                runner.test("with non-empty name that doesn't have a value", (Test test) ->
                {
                    try (final Process process = Process.create())
                    {
                        final CommandLineArguments arguments = CommandLineArguments.create("--a");
                        final CommandLineParameters parameters = new CommandLineParameters().setArguments(arguments);
                        final CommandLineParameter<File> parameter = parameters.addFile("a", process);
                        test.assertNull(parameter.getValue().await());
                    }
                });

                runner.test("with non-empty name that has an empty value", (Test test) ->
                {
                    try (final Process process = Process.create())
                    {
                        final CommandLineArguments arguments = CommandLineArguments.create("--a=");
                        final CommandLineParameters parameters = new CommandLineParameters().setArguments(arguments);
                        final CommandLineParameter<File> parameter = parameters.addFile("a", process);
                        test.assertNull(parameter.getValue().await());
                    }
                });

                runner.test("with non-empty name that has a relative folder path value", (Test test) ->
                {
                    try (final Process process = Process.create())
                    {
                        final CommandLineArguments arguments = CommandLineArguments.create("--a=folder/subfolder/");
                        final CommandLineParameters parameters = new CommandLineParameters().setArguments(arguments);
                        final CommandLineParameter<File> parameter = parameters.addFile("a", process);
                        test.assertThrows(() -> parameter.getValue().await(),
                            new PreConditionFailure("rootedFilePath.endsWith(\"/\") cannot be true."));
                    }
                });

                runner.test("with non-empty name that has a relative file path value", (Test test) ->
                {
                    try (final Process process = Process.create())
                    {
                        final CommandLineArguments arguments = CommandLineArguments.create("--a=folder/subfolder/file");
                        final CommandLineParameters parameters = new CommandLineParameters().setArguments(arguments);
                        final CommandLineParameter<File> parameter = parameters.addFile("a", process);
                        test.assertEqual(process.getCurrentFolder().getFile("folder/subfolder/file").await(), parameter.getValue().await());
                    }
                });

                runner.test("with non-empty name that has a rooted file path value", (Test test) ->
                {
                    try (final Process process = Process.create())
                    {
                        final CommandLineArguments arguments = CommandLineArguments.create("--a=/folder/file");
                        final CommandLineParameters parameters = new CommandLineParameters().setArguments(arguments);
                        final CommandLineParameter<File> parameter = parameters.addFile("a", process);
                        test.assertEqual(process.getFileSystem().getFile("/folder/file").await(), parameter.getValue().await());
                    }
                });

                runner.test("with non-empty name that has a rooted folder path value", (Test test) ->
                {
                    try (final Process process = Process.create())
                    {
                        final CommandLineArguments arguments = CommandLineArguments.create("--a=/folder/");
                        final CommandLineParameters parameters = new CommandLineParameters().setArguments(arguments);
                        final CommandLineParameter<File> parameter = parameters.addFile("a", process);
                        test.assertThrows(() -> parameter.getValue().await(),
                            new PreConditionFailure("rootedFilePath.endsWith(\"/\") cannot be true."));
                    }
                });
            });

            runner.testGroup("addPositionalFile(String,Process)", () ->
            {
                runner.test("with null parameter name", (Test test) ->
                {
                    try (final Process process = Process.create())
                    {
                        final CommandLineParameters parameters = new CommandLineParameters();
                        test.assertThrows(() -> parameters.addPositionalFile(null, process),
                            new PreConditionFailure("parameterName cannot be null."));
                    }
                });

                runner.test("with empty parameter name", (Test test) ->
                {
                    try (final Process process = Process.create())
                    {
                        final CommandLineParameters parameters = new CommandLineParameters();
                        test.assertThrows(() -> parameters.addPositionalFile("", process),
                            new PreConditionFailure("parameterName cannot be empty."));
                    }
                });

                runner.test("with null process", (Test test) ->
                {
                    final CommandLineParameters parameters = new CommandLineParameters();
                    test.assertThrows(() -> parameters.addPositionalFile("hello", null),
                        new PreConditionFailure("process cannot be null."));
                });

                runner.test("with non-empty name that doesn't exist in arguments", (Test test) ->
                {
                    try (final Process process = Process.create())
                    {
                        final CommandLineArguments arguments = CommandLineArguments.create();
                        final CommandLineParameters parameters = new CommandLineParameters().setArguments(arguments);
                        final CommandLineParameter<File> parameter = parameters.addPositionalFile("a", process);
                        test.assertNull(parameter.getValue().await());
                    }
                });

                runner.test("with non-empty name that has a positional non-empty value", (Test test) ->
                {
                    try (final Process process = Process.create())
                    {
                        final CommandLineArguments arguments = CommandLineArguments.create("testing");
                        final CommandLineParameters parameters = new CommandLineParameters().setArguments(arguments);
                        final CommandLineParameter<File> parameter = parameters.addPositionalFile("a", process);
                        test.assertEqual(process.getCurrentFolder().getFile("testing").await(), parameter.getValue().await());
                    }
                });

                runner.test("with non-empty name that doesn't have a value", (Test test) ->
                {
                    try (final Process process = Process.create())
                    {
                        final CommandLineArguments arguments = CommandLineArguments.create("--a");
                        final CommandLineParameters parameters = new CommandLineParameters().setArguments(arguments);
                        final CommandLineParameter<File> parameter = parameters.addPositionalFile("a", process);
                        test.assertNull(parameter.getValue().await());
                    }
                });

                runner.test("with non-empty name that has an empty value", (Test test) ->
                {
                    try (final Process process = Process.create())
                    {
                        final CommandLineArguments arguments = CommandLineArguments.create("--a=");
                        final CommandLineParameters parameters = new CommandLineParameters().setArguments(arguments);
                        final CommandLineParameter<File> parameter = parameters.addPositionalFile("a", process);
                        test.assertNull(parameter.getValue().await());
                    }
                });

                runner.test("with non-empty name that has a relative folder path value", (Test test) ->
                {
                    try (final Process process = Process.create())
                    {
                        final CommandLineArguments arguments = CommandLineArguments.create("--a=folder/subfolder/");
                        final CommandLineParameters parameters = new CommandLineParameters().setArguments(arguments);
                        final CommandLineParameter<File> parameter = parameters.addPositionalFile("a", process);
                        test.assertThrows(() -> parameter.getValue().await(),
                            new PreConditionFailure("rootedFilePath.endsWith(\"/\") cannot be true."));
                    }
                });

                runner.test("with non-empty name that has a relative file path value", (Test test) ->
                {
                    try (final Process process = Process.create())
                    {
                        final CommandLineArguments arguments = CommandLineArguments.create("--a=folder/subfolder/file");
                        final CommandLineParameters parameters = new CommandLineParameters().setArguments(arguments);
                        final CommandLineParameter<File> parameter = parameters.addPositionalFile("a", process);
                        test.assertEqual(process.getCurrentFolder().getFile("folder/subfolder/file").await(), parameter.getValue().await());
                    }
                });

                runner.test("with non-empty name that has a rooted folder path value", (Test test) ->
                {
                    try (final Process process = Process.create())
                    {
                        final CommandLineArguments arguments = CommandLineArguments.create("--a=/folder/");
                        final CommandLineParameters parameters = new CommandLineParameters().setArguments(arguments);
                        final CommandLineParameter<File> parameter = parameters.addPositionalFile("a", process);
                        test.assertThrows(() -> parameter.getValue().await(),
                            new PreConditionFailure("rootedFilePath.endsWith(\"/\") cannot be true."));
                    }
                });

                runner.test("with non-empty name that has a rooted file path value", (Test test) ->
                {
                    try (final Process process = Process.create())
                    {
                        final CommandLineArguments arguments = CommandLineArguments.create("--a=/folder/file");
                        final CommandLineParameters parameters = new CommandLineParameters().setArguments(arguments);
                        final CommandLineParameter<File> parameter = parameters.addPositionalFile("a", process);
                        test.assertEqual(process.getFileSystem().getFile("/folder/file").await(), parameter.getValue().await());
                    }
                });
            });

            runner.testGroup("addEnum(String,T)", () ->
            {
                runner.test("with null defaultValue", (Test test) ->
                {
                    final CommandLineParameters parameters = new CommandLineParameters();
                    test.assertThrows(() -> parameters.addEnum("hello", null),
                        new PreConditionFailure("defaultValue cannot be null."));
                });

                runner.test("with null", (Test test) ->
                {
                    final CommandLineParameters parameters = new CommandLineParameters();
                    test.assertThrows(() -> parameters.addEnum(null, Comparison.Equal),
                        new PreConditionFailure("parameterName cannot be null."));
                });

                runner.test("with empty", (Test test) ->
                {
                    final CommandLineParameters parameters = new CommandLineParameters();
                    test.assertThrows(() -> parameters.addEnum("", Comparison.Equal),
                        new PreConditionFailure("parameterName cannot be empty."));
                });

                runner.test("with non-empty name that doesn't exist in arguments", (Test test) ->
                {
                    final CommandLineArguments arguments = CommandLineArguments.create();
                    final CommandLineParameters parameters = new CommandLineParameters().setArguments(arguments);
                    final CommandLineParameter<Comparison> parameter = parameters.addEnum("a", Comparison.Equal);
                    test.assertEqual(Comparison.Equal, parameter.getValue().await());
                });

                runner.test("with non-empty name that doesn't have a value", (Test test) ->
                {
                    final CommandLineArguments arguments = CommandLineArguments.create("--a");
                    final CommandLineParameters parameters = new CommandLineParameters().setArguments(arguments);
                    final CommandLineParameter<Comparison> parameter = parameters.addEnum("a", Comparison.LessThan);
                    test.assertEqual(Comparison.LessThan, parameter.getValue().await());
                });

                runner.test("with non-empty name that has an empty value", (Test test) ->
                {
                    final CommandLineArguments arguments = CommandLineArguments.create("--a=");
                    final CommandLineParameters parameters = new CommandLineParameters().setArguments(arguments);
                    final CommandLineParameter<Comparison> parameter = parameters.addEnum("a", Comparison.GreaterThan);
                    test.assertEqual(Comparison.GreaterThan, parameter.getValue().await());
                });

                runner.test("with non-empty name that has an unrecognized value", (Test test) ->
                {
                    final CommandLineArguments arguments = CommandLineArguments.create("--a=blah");
                    final CommandLineParameters parameters = new CommandLineParameters().setArguments(arguments);
                    final CommandLineParameter<Comparison> parameter = parameters.addEnum("a", Comparison.Equal);
                    test.assertEqual(Comparison.Equal, parameter.getValue().await());
                });

                runner.test("with non-empty name that has a recognized value", (Test test) ->
                {
                    final CommandLineArguments arguments = CommandLineArguments.create("--a=LessThan");
                    final CommandLineParameters parameters = new CommandLineParameters().setArguments(arguments);
                    final CommandLineParameter<Comparison> parameter = parameters.addEnum("a", Comparison.Equal);
                    test.assertEqual(Comparison.LessThan, parameter.getValue().await());
                });

                runner.test("with non-empty name that has a recognized value with different casing", (Test test) ->
                {
                    final CommandLineArguments arguments = CommandLineArguments.create("--a=greaterTHAN");
                    final CommandLineParameters parameters = new CommandLineParameters().setArguments(arguments);
                    final CommandLineParameter<Comparison> parameter = parameters.addEnum("a", Comparison.Equal);
                    test.assertEqual(Comparison.GreaterThan, parameter.getValue().await());
                });
            });

            runner.test("addVerbose()", (Test test) ->
            {
                try (final Process process = Process.create())
                {
                    final CommandLineParameters parameters = new CommandLineParameters();
                    final CommandLineParameterVerbose parameter = parameters.addVerbose(process);
                    test.assertEqual("verbose", parameter.getName());
                    test.assertEqual(Iterable.create("v"), parameter.getAliases());
                    test.assertEqual("Whether or not to show verbose logs.", parameter.getDescription());
                    test.assertNull(parameter.getValueName());
                }
            });

            runner.test("addDebug()", (Test test) ->
            {
                final CommandLineParameters parameters = new CommandLineParameters();
                final CommandLineParameterBoolean parameter = parameters.addDebug();
                test.assertEqual("debug", parameter.getName());
                test.assertEqual(Iterable.create(), parameter.getAliases());
                test.assertEqual("Whether or not to run this application in debug mode.", parameter.getDescription());
                test.assertNull(parameter.getValueName());
            });

            runner.test("addProfiler()", (Test test) ->
            {
                try (final Process process = Process.create())
                {
                    final CommandLineParameters parameters = new CommandLineParameters();
                    final CommandLineParameterProfiler parameter = parameters.addProfiler(process, CommandLineParameterTests.class);
                    test.assertEqual("profiler", parameter.getName());
                    test.assertEqual(Iterable.create(), parameter.getAliases());
                    test.assertEqual("Whether or not this application should pause before it is run to allow a profiler to be attached.", parameter.getDescription());
                    test.assertNull(parameter.getValueName());
                }
            });

            runner.test("addHelp()", (Test test) ->
            {
                final CommandLineParameters parameters = new CommandLineParameters();
                final CommandLineParameterHelp parameter = parameters.addHelp();
                test.assertEqual("help", parameter.getName());
                test.assertEqual(Iterable.create("?"), parameter.getAliases());
                test.assertEqual("Show the help message for this application.", parameter.getDescription());
                test.assertNull(parameter.getValueName());
            });
        });
    }
}
