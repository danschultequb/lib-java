package qub;

public interface TypeLoaderTests
{
    static void test(TestRunner runner, Function0<TypeLoader> creator)
    {
        PreCondition.assertNotNull(runner, "runner");
        PreCondition.assertNotNull(creator, "creator");

        runner.testGroup(TypeLoader.class, () ->
        {
            runner.testGroup("getType(String)", () ->
            {
                final Action2<String,Throwable> getTypeErrorTest = (String fullTypeName, Throwable expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(fullTypeName), (Test test) ->
                    {
                        final TypeLoader typeLoader = creator.run();
                        test.assertThrows(() -> typeLoader.getType(fullTypeName).await(),
                            expected);
                    });
                };

                getTypeErrorTest.run(null, new PreConditionFailure("fullTypeName cannot be null."));
                getTypeErrorTest.run("", new PreConditionFailure("fullTypeName cannot be empty."));
                getTypeErrorTest.run("blah", new NotFoundException("Could not load a class named \"blah\"."));

                final Action2<String,Class<?>> getTypeTest = (String fullTypeName, Class<?> expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(fullTypeName), (Test test) ->
                    {
                        final TypeLoader typeLoader = creator.run();
                        test.assertEqual(expected, typeLoader.getType(fullTypeName).await());
                    });
                };

                getTypeTest.run("java.lang.Integer", java.lang.Integer.class);
                getTypeTest.run("java.lang.String", java.lang.String.class);
                getTypeTest.run("qub.TypeLoader", qub.TypeLoader.class);
            });

            runner.testGroup("getTypeContainerPathString(String)", () ->
            {
                final Action2<String,Throwable> getTypeContainerPathStringErrorTest = (String fullTypeName, Throwable expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(fullTypeName), (Test test) ->
                    {
                        final TypeLoader typeLoader = creator.run();
                        test.assertThrows(() -> typeLoader.getTypeContainerPathString(fullTypeName).await(),
                            expected);
                    });
                };

                getTypeContainerPathStringErrorTest.run(null, new PreConditionFailure("fullTypeName cannot be null."));
                getTypeContainerPathStringErrorTest.run("", new PreConditionFailure("fullTypeName cannot be empty."));
                getTypeContainerPathStringErrorTest.run("blah", new NotFoundException("Could not find a type container for a type named \"blah\"."));
                getTypeContainerPathStringErrorTest.run("java.lang.String", new NotFoundException("Could not find a type container for a type named \"java.lang.String\"."));
                getTypeContainerPathStringErrorTest.run("java.lang.Integer", new NotFoundException("Could not find a type container for a type named \"java.lang.Integer\"."));
            });

            runner.testGroup("getTypeContainerPathString(Class<?>)", () ->
            {
                final Action2<Class<?>,Throwable> getTypeContainerPathStringErrorTest = (Class<?> type, Throwable expected) ->
                {
                    runner.test("with " + (type == null ? "null" : Strings.escapeAndQuote(Types.getFullTypeName(type))), (Test test) ->
                    {
                        final TypeLoader typeLoader = creator.run();
                        test.assertThrows(() -> typeLoader.getTypeContainerPathString(type).await(),
                            expected);
                    });
                };

                getTypeContainerPathStringErrorTest.run(null, new PreConditionFailure("type cannot be null."));
                getTypeContainerPathStringErrorTest.run(String.class, new NotFoundException("Could not find a type container for a type named \"java.lang.String\"."));
                getTypeContainerPathStringErrorTest.run(java.lang.Integer.class, new NotFoundException("Could not find a type container for a type named \"java.lang.Integer\"."));
            });

            runner.testGroup("getTypeContainerPath(String)", () ->
            {
                final Action2<String,Throwable> getTypeContainerPathErrorTest = (String fullTypeName, Throwable expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(fullTypeName), (Test test) ->
                    {
                        final TypeLoader typeLoader = creator.run();
                        test.assertThrows(() -> typeLoader.getTypeContainerPath(fullTypeName).await(),
                            expected);
                    });
                };

                getTypeContainerPathErrorTest.run(null, new PreConditionFailure("fullTypeName cannot be null."));
                getTypeContainerPathErrorTest.run("", new PreConditionFailure("fullTypeName cannot be empty."));
                getTypeContainerPathErrorTest.run("blah", new NotFoundException("Could not find a type container for a type named \"blah\"."));
                getTypeContainerPathErrorTest.run("java.lang.String", new NotFoundException("Could not find a type container for a type named \"java.lang.String\"."));
                getTypeContainerPathErrorTest.run("java.lang.Integer", new NotFoundException("Could not find a type container for a type named \"java.lang.Integer\"."));
            });

            runner.testGroup("getTypeContainerPath(Class<?>)", () ->
            {
                final Action2<Class<?>,Throwable> getTypeContainerPathErrorTest = (Class<?> type, Throwable expected) ->
                {
                    runner.test("with " + (type == null ? "null" : Strings.escapeAndQuote(Types.getFullTypeName(type))), (Test test) ->
                    {
                        final TypeLoader typeLoader = creator.run();
                        test.assertThrows(() -> typeLoader.getTypeContainerPath(type).await(),
                            expected);
                    });
                };

                getTypeContainerPathErrorTest.run(null, new PreConditionFailure("type cannot be null."));
                getTypeContainerPathErrorTest.run(String.class, new NotFoundException("Could not find a type container for a type named \"java.lang.String\"."));
                getTypeContainerPathErrorTest.run(java.lang.Integer.class, new NotFoundException("Could not find a type container for a type named \"java.lang.Integer\"."));
            });
        });
    }
}
