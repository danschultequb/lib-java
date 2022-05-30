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

            runner.testGroup("getStaticMethod0(String,String)", () ->
            {
                final Action3<String,String,Throwable> getStaticMethod0ErrorTest = (String fullTypeName, String methodName, Throwable expected) ->
                {
                    runner.test("with " + English.andList(Iterable.create(fullTypeName, methodName).map(Strings::escapeAndQuote)), (Test test) ->
                    {
                        final TypeLoader typeLoader = creator.run();
                        test.assertThrows(() -> typeLoader.getStaticMethod0(fullTypeName, methodName).await(),
                            expected);
                    });
                };

                getStaticMethod0ErrorTest.run(null, "there", new PreConditionFailure("fullTypeName cannot be null."));
                getStaticMethod0ErrorTest.run("", "there", new PreConditionFailure("fullTypeName cannot be empty."));
                getStaticMethod0ErrorTest.run("hello", null, new PreConditionFailure("methodName cannot be null."));
                getStaticMethod0ErrorTest.run("hello", "", new PreConditionFailure("methodName cannot be empty."));
                getStaticMethod0ErrorTest.run("hello", "there", new NotFoundException("Could not load a class named \"hello\"."));
                getStaticMethod0ErrorTest.run(Types.getFullTypeName(TypeLoaderTests.class), "test", new NotFoundException("Could not find a static method with the signature qub.TypeLoaderTests.test() -> ?."));

                runner.test("with matching static function", (Test test) ->
                {
                    final TypeLoader typeLoader = creator.run();
                    final StaticMethod0<?,?> staticMethod = typeLoader.getStaticMethod0(
                        Types.getFullTypeName(TypeLoaderTests.class),
                        "fakeStaticMethod0")
                        .await();
                    test.assertNotNull(staticMethod);
                    test.assertEqual(5, staticMethod.run());
                });
            });

            runner.testGroup("getStaticMethod0(String,String,Class<TReturn>)", () ->
            {
                final Action4<String,String,Class<?>,Throwable> getStaticMethod0ErrorTest = (String fullTypeName, String methodName, Class<?> returnType, Throwable expected) ->
                {
                    runner.test("with " + English.andList(Strings.escapeAndQuote(fullTypeName), Strings.escapeAndQuote(methodName), returnType), (Test test) ->
                    {
                        final TypeLoader typeLoader = creator.run();
                        test.assertThrows(() -> typeLoader.getStaticMethod0(fullTypeName, methodName, returnType).await(),
                            expected);
                    });
                };

                getStaticMethod0ErrorTest.run(
                    null,
                    "fakeMethodName",
                    Integer.class,
                    new PreConditionFailure("fullTypeName cannot be null."));
                getStaticMethod0ErrorTest.run(
                    "",
                    "fakeMethodName",
                    Integer.class,
                    new PreConditionFailure("fullTypeName cannot be empty."));
                getStaticMethod0ErrorTest.run(
                    "fakeTypeName",
                    null,
                    Integer.class,
                    new PreConditionFailure("methodName cannot be null."));
                getStaticMethod0ErrorTest.run(
                    "fakeTypeName",
                    "",
                    Integer.class,
                    new PreConditionFailure("methodName cannot be empty."));
                getStaticMethod0ErrorTest.run(
                    "fakeTypeName",
                    "fakeMethodName",
                    null,
                    new PreConditionFailure("returnType cannot be null."));
                getStaticMethod0ErrorTest.run(
                    "idontexist",
                    "fakeMethodName",
                    Void.class,
                    new NotFoundException("Could not load a class named \"idontexist\"."));
                getStaticMethod0ErrorTest.run(
                    Types.getFullTypeName(TypeLoaderTests.class),
                    "fakeStaticMethod0",
                    Float.class,
                    new NotFoundException("Could not find a static method with the signature qub.TypeLoaderTests.fakeStaticMethod0() -> java.lang.Float."));
                getStaticMethod0ErrorTest.run(
                    Types.getFullTypeName(TypeLoaderTests.class),
                    "fakeStaticMethod0",
                    Object.class,
                    new NotFoundException("Could not find a static method with the signature qub.TypeLoaderTests.fakeStaticMethod0() -> java.lang.Object."));
                getStaticMethod0ErrorTest.run(
                    Types.getFullTypeName(TypeLoaderTests.class),
                    "test",
                    Void.class,
                    new NotFoundException("Could not find a static method with the signature qub.TypeLoaderTests.test() -> java.lang.Void."));
                getStaticMethod0ErrorTest.run(
                    Types.getFullTypeName(TypeLoaderTests.class),
                    "fakeStaticMethod0",
                    Integer.class,
                    new NotFoundException("Could not find a static method with the signature qub.TypeLoaderTests.fakeStaticMethod0() -> java.lang.Integer."));

                runner.test("with matching static method", (Test test) ->
                {
                    final TypeLoader typeLoader = creator.run();
                    final StaticMethod0<?,Integer> staticMethod = typeLoader.getStaticMethod0(
                        Types.getFullTypeName(TypeLoaderTests.class),
                        "fakeStaticMethod0",
                        int.class)
                        .await();
                    test.assertNotNull(staticMethod);
                    test.assertEqual(5, staticMethod.run());
                });
            });

            runner.testGroup("getStaticMethod1(String,String,Class<T1>)", () ->
            {
                final Action4<String,String,Class<?>,Throwable> getStaticMethod1ErrorTest = (String fullTypeName, String methodName, Class<?> arg1Type, Throwable expected) ->
                {
                    runner.test("with " + English.andList(Strings.escapeAndQuote(fullTypeName), Strings.escapeAndQuote(methodName), arg1Type), (Test test) ->
                    {
                        final TypeLoader typeLoader = creator.run();
                        test.assertThrows(() -> typeLoader.getStaticMethod1(fullTypeName, methodName, arg1Type).await(),
                            expected);
                    });
                };

                getStaticMethod1ErrorTest.run(
                    null,
                    "fakeMethodName",
                    Integer.class,
                    new PreConditionFailure("fullTypeName cannot be null."));
                getStaticMethod1ErrorTest.run(
                    "",
                    "fakeMethodName",
                    Integer.class,
                    new PreConditionFailure("fullTypeName cannot be empty."));
                getStaticMethod1ErrorTest.run(
                    "fakeTypeName",
                    null,
                    Integer.class,
                    new PreConditionFailure("methodName cannot be null."));
                getStaticMethod1ErrorTest.run(
                    "fakeTypeName",
                    "",
                    Integer.class,
                    new PreConditionFailure("methodName cannot be empty."));
                getStaticMethod1ErrorTest.run(
                    "fakeTypeName",
                    "fakeMethodName",
                    null,
                    new PreConditionFailure("arg1Type cannot be null."));
                getStaticMethod1ErrorTest.run(
                    "idontexist",
                    "fakeMethodName",
                    Void.class,
                    new NotFoundException("Could not load a class named \"idontexist\"."));
                getStaticMethod1ErrorTest.run(
                    Types.getFullTypeName(TypeLoaderTests.class),
                    "fakeStaticMethod0",
                    Float.class,
                    new NotFoundException("Could not find a static method with the signature qub.TypeLoaderTests.fakeStaticMethod0(java.lang.Float) -> ?."));
                getStaticMethod1ErrorTest.run(
                    Types.getFullTypeName(TypeLoaderTests.class),
                    "fakeStaticMethod0",
                    Object.class,
                    new NotFoundException("Could not find a static method with the signature qub.TypeLoaderTests.fakeStaticMethod0(java.lang.Object) -> ?."));
                getStaticMethod1ErrorTest.run(
                    Types.getFullTypeName(TypeLoaderTests.class),
                    "test",
                    Void.class,
                    new NotFoundException("Could not find a static method with the signature qub.TypeLoaderTests.test(java.lang.Void) -> ?."));
                getStaticMethod1ErrorTest.run(
                    Types.getFullTypeName(TypeLoaderTests.class),
                    "fakeStaticMethod0",
                    Integer.class,
                    new NotFoundException("Could not find a static method with the signature qub.TypeLoaderTests.fakeStaticMethod0(java.lang.Integer) -> ?."));

                runner.test("with matching static method", (Test test) ->
                {
                    final TypeLoader typeLoader = creator.run();
                    final StaticMethod1<?,Float,?> staticMethod = typeLoader.getStaticMethod1(
                        Types.getFullTypeName(TypeLoaderTests.class),
                        "fakeStaticMethod1",
                        Float.class)
                        .await();
                    test.assertNotNull(staticMethod);
                    test.assertEqual(1.2, staticMethod.run(null));
                    test.assertEqual(6.4, staticMethod.run(3f));
                });
            });

            runner.testGroup("getStaticMethod1(String,String,Class<T1>,Class<TReturn>)", () ->
            {
                final Action5<String,String,Class<?>,Class<?>,Throwable> getStaticMethod1ErrorTest = (String fullTypeName, String methodName, Class<?> arg1Type, Class<?> returnType, Throwable expected) ->
                {
                    runner.test("with " + English.andList(Strings.escapeAndQuote(fullTypeName), Strings.escapeAndQuote(methodName), arg1Type, returnType), (Test test) ->
                    {
                        final TypeLoader typeLoader = creator.run();
                        test.assertThrows(() -> typeLoader.getStaticMethod1(fullTypeName, methodName, arg1Type, returnType).await(),
                            expected);
                    });
                };

                getStaticMethod1ErrorTest.run(
                    null,
                    "fakeMethodName",
                    Integer.class,
                    Boolean.class,
                    new PreConditionFailure("fullTypeName cannot be null."));
                getStaticMethod1ErrorTest.run(
                    "",
                    "fakeMethodName",
                    Integer.class,
                    Boolean.class,
                    new PreConditionFailure("fullTypeName cannot be empty."));
                getStaticMethod1ErrorTest.run(
                    "fakeTypeName",
                    null,
                    Integer.class,
                    Boolean.class,
                    new PreConditionFailure("methodName cannot be null."));
                getStaticMethod1ErrorTest.run(
                    "fakeTypeName",
                    "",
                    Integer.class,
                    Boolean.class,
                    new PreConditionFailure("methodName cannot be empty."));
                getStaticMethod1ErrorTest.run(
                    "fakeTypeName",
                    "fakeMethodName",
                    null,
                    Boolean.class,
                    new PreConditionFailure("arg1Type cannot be null."));
                getStaticMethod1ErrorTest.run(
                    "fakeTypeName",
                    "fakeMethodName",
                    Integer.class,
                    null,
                    new PreConditionFailure("returnType cannot be null."));
                getStaticMethod1ErrorTest.run(
                    "idontexist",
                    "fakeMethodName",
                    Void.class,
                    Boolean.class,
                    new NotFoundException("Could not load a class named \"idontexist\"."));
                getStaticMethod1ErrorTest.run(
                    Types.getFullTypeName(TypeLoaderTests.class),
                    "fakeStaticMethod0",
                    Float.class,
                    Boolean.class,
                    new NotFoundException("Could not find a static method with the signature qub.TypeLoaderTests.fakeStaticMethod0(java.lang.Float) -> java.lang.Boolean."));
                getStaticMethod1ErrorTest.run(
                    Types.getFullTypeName(TypeLoaderTests.class),
                    "fakeStaticMethod0",
                    Object.class,
                    Boolean.class,
                    new NotFoundException("Could not find a static method with the signature qub.TypeLoaderTests.fakeStaticMethod0(java.lang.Object) -> java.lang.Boolean."));
                getStaticMethod1ErrorTest.run(
                    Types.getFullTypeName(TypeLoaderTests.class),
                    "test",
                    Void.class,
                    Short.class,
                    new NotFoundException("Could not find a static method with the signature qub.TypeLoaderTests.test(java.lang.Void) -> java.lang.Short."));
                getStaticMethod1ErrorTest.run(
                    Types.getFullTypeName(TypeLoaderTests.class),
                    "fakeStaticMethod0",
                    Integer.class,
                    Boolean.class,
                    new NotFoundException("Could not find a static method with the signature qub.TypeLoaderTests.fakeStaticMethod0(java.lang.Integer) -> java.lang.Boolean."));

                runner.test("with matching static method", (Test test) ->
                {
                    final TypeLoader typeLoader = creator.run();
                    final StaticMethod1<?,Float,Double> staticMethod = typeLoader.getStaticMethod1(
                        Types.getFullTypeName(TypeLoaderTests.class),
                        "fakeStaticMethod1",
                        Float.class,
                        double.class)
                        .await();
                    test.assertNotNull(staticMethod);
                    test.assertEqual(1.2, staticMethod.run(null));
                    test.assertEqual(6.4, staticMethod.run(3f));
                });
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

    static int fakeStaticMethod0()
    {
        return 5;
    }

    static double fakeStaticMethod1(Float value)
    {
        return value == null ? 1.2 : value + 3.4;
    }
}
