package qub;

public interface EnvironmentVariablesTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(EnvironmentVariables.class, () ->
        {
            runner.test("create()", (Test test) ->
            {
                final MutableEnvironmentVariables env = EnvironmentVariables.create();
                test.assertNotNull(env);
            });

            runner.testGroup("set(String,String)", () ->
            {
                final Action3<String,String,Throwable> setErrorTest = (String variableName, String variableValue, Throwable expected) ->
                {
                    runner.test("with " + English.andList(variableName, variableValue), (Test test) ->
                    {
                        final MutableEnvironmentVariables env = EnvironmentVariables.create();
                        test.assertThrows(() -> env.set(variableName, variableValue), expected);
                    });
                };

                setErrorTest.run(null, "b", new PreConditionFailure("variableName cannot be null."));
                setErrorTest.run("", "b", new PreConditionFailure("variableName cannot be empty."));
                setErrorTest.run("a", null, new PreConditionFailure("variableValue cannot be null."));

                final Action2<String,String> setTest = (String variableName, String variableValue) ->
                {
                    runner.test("with " + English.andList(variableName, variableValue), (Test test) ->
                    {
                        final MutableEnvironmentVariables env = EnvironmentVariables.create();
                        final MutableEnvironmentVariables setResult = env.set(variableName, variableValue);
                        test.assertSame(env, setResult);
                        test.assertEqual(variableValue, env.get(variableName).await());
                    });
                };

                setTest.run("a", "");
                setTest.run("a", "b");

                runner.test("with z=hello when z already equals rain", (Test test) ->
                {
                    final MutableEnvironmentVariables env = EnvironmentVariables.create()
                        .set("z", "rain");
                    final MutableEnvironmentVariables setResult = env.set("z", "hello");
                    test.assertSame(env, setResult);
                    test.assertEqual("hello", env.get("z").await());
                });

                runner.test("with z=hello when Z already equals rain", (Test test) ->
                {
                    final MutableEnvironmentVariables env = EnvironmentVariables.create()
                        .set("Z", "rain");
                    final MutableEnvironmentVariables setResult = env.set("z", "hello");
                    test.assertSame(env, setResult);
                    test.assertEqual("hello", env.get("z").await());
                    test.assertEqual("hello", env.get("Z").await());
                });
            });

            runner.testGroup("get(String)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final MutableEnvironmentVariables env = EnvironmentVariables.create();
                    test.assertThrows(() -> env.get(null),
                        new PreConditionFailure("variableName cannot be null."));
                });

                runner.test("with empty", (Test test) ->
                {
                    final MutableEnvironmentVariables env = EnvironmentVariables.create();
                    test.assertThrows(() -> env.get(""),
                        new PreConditionFailure("variableName cannot be empty."));
                });

                runner.test("with non-existing variable name", (Test test) ->
                {
                    final MutableEnvironmentVariables env = EnvironmentVariables.create();
                    test.assertThrows(() -> env.get("idontexist").await(),
                        new NotFoundException("No environment variable named \"idontexist\" found."));
                });

                runner.test("with existing variable name", (Test test) ->
                {
                    final MutableEnvironmentVariables env = EnvironmentVariables.create()
                        .set("a", "b");
                    test.assertEqual("b", env.get("a").await());
                });

                runner.test("with existing variable name with different case", (Test test) ->
                {
                    final MutableEnvironmentVariables env = EnvironmentVariables.create()
                        .set("a", "b");
                    test.assertEqual("b", env.get("A").await());
                });
            });

            runner.testGroup("resolve(String)", () ->
            {
                final Action2<String,Throwable> resolveErrorTest = (String value, Throwable expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(value), (Test test) ->
                    {
                        final MutableEnvironmentVariables env = EnvironmentVariables.create();
                        test.assertThrows(() -> env.resolve(value), expected);
                    });
                };

                resolveErrorTest.run(null, new PreConditionFailure("value cannot be null."));

                final Action3<EnvironmentVariables,String,String> resolveTest = (EnvironmentVariables env, String value, String expected) ->
                {
                    runner.test("with " + English.andList(env, Strings.escapeAndQuote(value)), (Test test) ->
                    {
                        test.assertEqual(expected, env.resolve(value));
                    });
                };

                resolveTest.run(
                    EnvironmentVariables.create(),
                    "",
                    "");
                resolveTest.run(
                    EnvironmentVariables.create(),
                    "hello",
                    "hello");
                resolveTest.run(
                    EnvironmentVariables.create(),
                    "ab%c",
                    "ab%c");
                resolveTest.run(
                    EnvironmentVariables.create(),
                    "ab%%",
                    "ab%%");
                resolveTest.run(
                    EnvironmentVariables.create(),
                    "ab%c%",
                    "ab");
                resolveTest.run(
                    EnvironmentVariables.create()
                        .set("c", "CATS"),
                    "ab%c%",
                    "abCATS");
                resolveTest.run(
                    EnvironmentVariables.create(),
                    "ab%c%d%",
                    "abd%");
                resolveTest.run(
                    EnvironmentVariables.create()
                        .set("c", "CATS"),
                    "ab%c%d%",
                    "abCATSd%");
                resolveTest.run(
                    EnvironmentVariables.create()
                        .set("c", "CATS"),
                    "ab%c%d%e",
                    "abCATSd%e");
                resolveTest.run(
                    EnvironmentVariables.create()
                        .set("c", "CATS"),
                    "ab%c%d%e%",
                    "abCATSd");
                resolveTest.run(
                    EnvironmentVariables.create()
                        .set("c", "CATS")
                        .set("e", "EGGS"),
                    "ab%c%d%e%",
                    "abCATSdEGGS");
            });
        });
    }
}
