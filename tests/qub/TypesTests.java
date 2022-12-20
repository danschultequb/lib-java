package qub;

public class TypesTests
{
    public int publicInt;
    protected String protectedString = "testing this";
    private boolean privateBoolean;

    public static int staticGet5()
    {
        return 5;
    }

    public static int staticAdd1(int value)
    {
        return value + 1;
    }

    public static void staticPrimitiveEmpty()
    {
    }

    public static Void staticBoxedEmpty()
    {
        return null;
    }

    public int memberGet6()
    {
        return 6;
    }

    public int memberAdd2(Integer value)
    {
        return value + 2;
    }

    public static void test(TestRunner runner)
    {
        runner.testGroup(Types.class, () ->
        {
            runner.testGroup("getMethodSignature(Class<?>,String)", () ->
            {
                runner.test("with null type", (Test test) ->
                {
                    test.assertThrows(() -> Types.getMethodSignature(null, "methodName"),
                        new PreConditionFailure("type cannot be null."));
                });

                runner.test("with null methodName", (Test test) ->
                {
                    test.assertThrows(() -> Types.getMethodSignature(TypesTests.class, null),
                        new PreConditionFailure("methodName cannot be null."));
                });

                runner.test("with empty methodName", (Test test) ->
                {
                    test.assertThrows(() -> Types.getMethodSignature(TypesTests.class, ""),
                        new PreConditionFailure("methodName cannot be empty."));
                });

                runner.test("with valid parameters", (Test test) ->
                {
                    test.assertEqual("qub.TypesTests.hello() -> ?", Types.getMethodSignature(TypesTests.class, "hello"));
                });
            });

            runner.testGroup("getMethodSignature(Class<?>,String,Iterable<Class<?>>)", () ->
            {
                runner.test("with null type", (Test test) ->
                {
                    test.assertThrows(() -> Types.getMethodSignature(null, "methodName", Iterable.create(Integer.class)),
                        new PreConditionFailure("type cannot be null."));
                });

                runner.test("with null methodName", (Test test) ->
                {
                    test.assertThrows(() -> Types.getMethodSignature(TypesTests.class, null, Iterable.create(Integer.class)),
                        new PreConditionFailure("methodName cannot be null."));
                });

                runner.test("with empty methodName", (Test test) ->
                {
                    test.assertThrows(() -> Types.getMethodSignature(TypesTests.class, "", Iterable.create(Integer.class)),
                        new PreConditionFailure("methodName cannot be empty."));
                });

                runner.test("with null parameterTypes", (Test test) ->
                {
                    test.assertThrows(() -> Types.getMethodSignature(TypesTests.class, "hello", (Iterable<Class<?>>)null),
                        new PreConditionFailure("parameterTypes cannot be null."));
                });

                runner.test("with empty parameterTypes", (Test test) ->
                {
                    test.assertEqual("qub.TypesTests.hello() -> ?", Types.getMethodSignature(TypesTests.class, "hello", Iterable.create()));
                });

                runner.test("with one parameterType", (Test test) ->
                {
                    test.assertEqual("qub.TypesTests.hello(java.lang.Character) -> ?", Types.getMethodSignature(TypesTests.class, "hello", Iterable.create(Character.class)));
                });

                runner.test("with two parameterTypes", (Test test) ->
                {
                    test.assertEqual("qub.TypesTests.hello(short,char) -> ?", Types.getMethodSignature(TypesTests.class, "hello", Iterable.create(short.class, char.class)));
                });
            });

            runner.testGroup("getMethodSignature(Class<?>,String,Iterable<Class<?>>,Class<?>)", () ->
            {
                runner.test("with null type", (Test test) ->
                {
                    test.assertThrows(() -> Types.getMethodSignature(null, "methodName", Iterable.create(Integer.class), int.class),
                        new PreConditionFailure("type cannot be null."));
                });

                runner.test("with null methodName", (Test test) ->
                {
                    test.assertThrows(() -> Types.getMethodSignature(TypesTests.class, null, Iterable.create(Integer.class), char.class),
                        new PreConditionFailure("methodName cannot be null."));
                });

                runner.test("with empty methodName", (Test test) ->
                {
                    test.assertThrows(() -> Types.getMethodSignature(TypesTests.class, "", Iterable.create(Integer.class), boolean.class),
                        new PreConditionFailure("methodName cannot be empty."));
                });

                runner.test("with null parameterTypes", (Test test) ->
                {
                    test.assertThrows(() -> Types.getMethodSignature(TypesTests.class, "hello", (Iterable<Class<?>>)null, short.class),
                        new PreConditionFailure("parameterTypes cannot be null."));
                });

                runner.test("with null returnType", (Test test) ->
                {
                    test.assertThrows(() -> Types.getMethodSignature(TypesTests.class, "hello", Iterable.create(), (Class<?>)null),
                        new PreConditionFailure("returnType cannot be null."));
                });

                runner.test("with empty parameterTypes", (Test test) ->
                {
                    test.assertEqual("qub.TypesTests.hello() -> char",
                        Types.getMethodSignature(TypesTests.class, "hello", Iterable.create(), char.class));
                });

                runner.test("with single-element parameterTypes", (Test test) ->
                {
                    test.assertEqual("qub.TypesTests.hello(java.lang.String) -> java.lang.String",
                        Types.getMethodSignature(TypesTests.class, "hello", Iterable.create(String.class), String.class));
                });

                runner.test("with two-element parameterTypes", (Test test) ->
                {
                    test.assertEqual("qub.TypesTests.hello(java.lang.String,qub.Comparison) -> boolean",
                        Types.getMethodSignature(TypesTests.class, "hello", Iterable.create(String.class, Comparison.class), boolean.class));
                });
            });

            runner.testGroup("getMethodSignature(Class<?>,String,Iterable<Class<?>>,String)", () ->
            {
                runner.test("with null type", (Test test) ->
                {
                    test.assertThrows(() -> Types.getMethodSignature(null, "methodName", Iterable.create(Integer.class), "hello"),
                        new PreConditionFailure("type cannot be null."));
                });

                runner.test("with null methodName", (Test test) ->
                {
                    test.assertThrows(() -> Types.getMethodSignature(TypesTests.class, null, Iterable.create(Integer.class), "hello"),
                        new PreConditionFailure("methodName cannot be null."));
                });

                runner.test("with empty methodName", (Test test) ->
                {
                    test.assertThrows(() -> Types.getMethodSignature(TypesTests.class, "", Iterable.create(Integer.class), "hello"),
                        new PreConditionFailure("methodName cannot be empty."));
                });

                runner.test("with null parameterTypes", (Test test) ->
                {
                    test.assertThrows(() -> Types.getMethodSignature(TypesTests.class, "hello", (Iterable<Class<?>>)null, "hello"),
                        new PreConditionFailure("parameterTypes cannot be null."));
                });

                runner.test("with null returnTypeFullName", (Test test) ->
                {
                    test.assertThrows(() -> Types.getMethodSignature(TypesTests.class, "hello", Iterable.create(), (String)null),
                        new PreConditionFailure("returnTypeFullName cannot be null."));
                });

                runner.test("with empty returnTypeFullName", (Test test) ->
                {
                    test.assertThrows(() -> Types.getMethodSignature(TypesTests.class, "hello", Iterable.create(), ""),
                        new PreConditionFailure("returnTypeFullName cannot be empty."));
                });

                runner.test("with empty parameterTypes", (Test test) ->
                {
                    test.assertEqual("qub.TypesTests.hello() -> test",
                        Types.getMethodSignature(TypesTests.class, "hello", Iterable.create(), "test"));
                });

                runner.test("with single-element parameterTypes", (Test test) ->
                {
                    test.assertEqual("qub.TypesTests.hello(java.lang.String) -> ...",
                        Types.getMethodSignature(TypesTests.class, "hello", Iterable.create(String.class), "..."));
                });

                runner.test("with two-element parameterTypes", (Test test) ->
                {
                    test.assertEqual("qub.TypesTests.hello(java.lang.String,qub.Comparison) -> spam",
                        Types.getMethodSignature(TypesTests.class, "hello", Iterable.create(String.class, Comparison.class), "spam"));
                });
            });

            runner.testGroup("getMethod(Class<?>,String,Iterable<Class<?>>)", () ->
            {
                runner.test("with null type", (Test test) ->
                {
                    test.assertThrows(() -> Types.getMethod(null, "method1", Iterable.create(Integer.class)),
                        new PreConditionFailure("type cannot be null."));
                });

                runner.test("with null methodName", (Test test) ->
                {
                    test.assertThrows(() -> Types.getMethod(TypesTests.class, null, Iterable.create(Integer.class)),
                        new PreConditionFailure("methodName cannot be null."));
                });

                runner.test("with empty methodName", (Test test) ->
                {
                    test.assertThrows(() -> Types.getMethod(TypesTests.class, "", Iterable.create(Integer.class)),
                        new PreConditionFailure("methodName cannot be empty."));
                });

                runner.test("with non-existing method name", (Test test) ->
                {
                    test.assertThrows(() -> Types.getMethod(TypesTests.class, "hello", Iterable.create(Integer.class)).await(),
                        TypesTests.getExpectedMethodNotFoundException("qub.TypesTests.hello(java.lang.Integer) -> ?"));
                });

                runner.test("with existing method with different parameter types", (Test test) ->
                {
                    test.assertThrows(() -> Types.getMethod(TypesTests.class, "staticAdd1", Iterable.create(String.class)).await(),
                        TypesTests.getExpectedMethodNotFoundException("qub.TypesTests.staticAdd1(java.lang.String) -> ?"));
                });

                runner.test("with existing static method with different parameter count", (Test test) ->
                {
                    test.assertThrows(() -> Types.getMethod(TypesTests.class, "staticAdd1", Iterable.create(Integer.class, Integer.class)).await(),
                        TypesTests.getExpectedMethodNotFoundException("qub.TypesTests.staticAdd1(java.lang.Integer,java.lang.Integer) -> ?"));
                });

                runner.test("with existing static method with provided boxed parameter type instead of actual primitive parameter type", (Test test) ->
                {
                    test.assertThrows(() -> Types.getMethod(TypesTests.class, "staticAdd1", Iterable.create(Integer.class)).await(),
                        TypesTests.getExpectedMethodNotFoundException("qub.TypesTests.staticAdd1(java.lang.Integer) -> ?"));
                });

                runner.test("with matching static method", (Test test) ->
                {
                    final java.lang.reflect.Method method = Types.getMethod(TypesTests.class, "staticAdd1", Iterable.create(int.class)).await();
                    test.assertNotNull(method);
                    test.assertSame(TypesTests.class, method.getDeclaringClass());
                    test.assertEqual("staticAdd1", method.getName());
                    try
                    {
                        test.assertEqual(7, method.invoke(null, 6));
                    }
                    catch (Throwable e)
                    {
                        test.fail(e);
                    }
                });

                runner.test("with existing member method with provided primitive parameter type instead of actual boxed parameter type", (Test test) ->
                {
                    test.assertThrows(() -> Types.getMethod(TypesTests.class, "memberAdd2", Iterable.create(int.class)).await(),
                        TypesTests.getExpectedMethodNotFoundException("qub.TypesTests.memberAdd2(int) -> ?"));
                });

                runner.test("with matching member method", (Test test) ->
                {
                    final java.lang.reflect.Method method = Types.getMethod(TypesTests.class, "memberAdd2", Iterable.create(Integer.class)).await();
                    test.assertNotNull(method);
                    test.assertSame(TypesTests.class, method.getDeclaringClass());
                    test.assertEqual("memberAdd2", method.getName());
                    try
                    {
                        test.assertEqual(8, method.invoke(new TypesTests(), 6));
                    }
                    catch (Throwable e)
                    {
                        test.fail(e);
                    }
                });
            });

            runner.testGroup("getMethod0(Class<TType>,String)", () ->
            {
                runner.test("with null type", (Test test) ->
                {
                    test.assertThrows(() -> Types.getMethod0(null, "hello"),
                        new PreConditionFailure("type cannot be null."));
                });

                runner.test("with null methodName", (Test test) ->
                {
                    test.assertThrows(() -> Types.getMethod0(TypesTests.class, null),
                        new PreConditionFailure("methodName cannot be null."));
                });

                runner.test("with empty methodName", (Test test) ->
                {
                    test.assertThrows(() -> Types.getMethod0(TypesTests.class, ""),
                        new PreConditionFailure("methodName cannot be empty."));
                });

                runner.test("with non-existing method", (Test test) ->
                {
                    test.assertThrows(() -> Types.getMethod0(TypesTests.class, "blah").await(),
                        new NotFoundException("Could not find a method with the signature qub.TypesTests.blah() -> ?."));
                });

                runner.test("with existing static method with different parameter types", (Test test) ->
                {
                    test.assertThrows(() -> Types.getMethod0(TypesTests.class, "staticAdd1").await(),
                        new NotFoundException("Could not find a method with the signature qub.TypesTests.staticAdd1() -> ?."));
                });

                runner.test("with existing member method", (Test test) ->
                {
                    final Method0<TypesTests,?> method = Types.getMethod0(TypesTests.class, "memberGet6").await();
                    test.assertNotNull(method);
                    test.assertSame(TypesTests.class, method.getType());
                    test.assertEqual(6, method.run(new TypesTests()));
                });

                runner.test("with existing static method with return type", (Test test) ->
                {
                    test.assertThrows(() -> Types.getMethod0(TypesTests.class, "staticGet5").await(),
                        new NotFoundException("Could not find a method with the signature qub.TypesTests.staticGet5() -> ?."));
                });

                runner.test("with existing static method with void return type", (Test test) ->
                {
                    test.assertThrows(() -> Types.getMethod0(TypesTests.class, "staticPrimitiveEmpty").await(),
                        new NotFoundException("Could not find a method with the signature qub.TypesTests.staticPrimitiveEmpty() -> ?."));
                });
            });

            runner.testGroup("getMethod0(Class<TType>,String,Class<TReturn>)", () ->
            {
                runner.test("with null type", (Test test) ->
                {
                    test.assertThrows(() -> Types.getMethod0(null, "hello", int.class),
                        new PreConditionFailure("type cannot be null."));
                });

                runner.test("with null methodName", (Test test) ->
                {
                    test.assertThrows(() -> Types.getMethod0(TypesTests.class, null, int.class),
                        new PreConditionFailure("methodName cannot be null."));
                });

                runner.test("with empty methodName", (Test test) ->
                {
                    test.assertThrows(() -> Types.getMethod0(TypesTests.class, "", int.class),
                        new PreConditionFailure("methodName cannot be empty."));
                });

                runner.test("with null return type", (Test test) ->
                {
                    test.assertThrows(() -> Types.getMethod0(TypesTests.class, "blah", null).await(),
                        new PreConditionFailure("returnType cannot be null."));
                });

                runner.test("with non-existing method", (Test test) ->
                {
                    test.assertThrows(() -> Types.getMethod0(TypesTests.class, "blah", int.class).await(),
                        new NotFoundException("Could not find a member method with the signature qub.TypesTests.blah() -> int."));
                });

                runner.test("with existing member method with different return type", (Test test) ->
                {
                    test.assertThrows(() -> Types.getMethod0(TypesTests.class, "memberGet6", char.class).await(),
                        new NotFoundException("Could not find a member method with the signature qub.TypesTests.memberGet6() -> char."));
                });

                runner.test("with existing member method with wrong boxed return type", (Test test) ->
                {
                    test.assertThrows(() -> Types.getMethod0(TypesTests.class, "memberGet6", Integer.class).await(),
                        new NotFoundException("Could not find a member method with the signature qub.TypesTests.memberGet6() -> java.lang.Integer."));
                });

                runner.test("with existing member method with same return type", (Test test) ->
                {
                    final Method0<TypesTests,Integer> method = Types.getMethod0(TypesTests.class, "memberGet6", int.class).await();
                    test.assertNotNull(method);
                    test.assertSame(TypesTests.class, method.getType());
                    test.assertEqual(6, method.run(new TypesTests()));
                });

                runner.test("with existing static method with different parameter types", (Test test) ->
                {
                    test.assertThrows(() -> Types.getMethod0(TypesTests.class, "staticAdd1", int.class).await(),
                        new NotFoundException("Could not find a member method with the signature qub.TypesTests.staticAdd1() -> int."));
                });

                runner.test("with existing static method with wrong return type", (Test test) ->
                {
                    test.assertThrows(() -> Types.getMethod0(TypesTests.class, "staticGet5", char.class).await(),
                        new NotFoundException("Could not find a member method with the signature qub.TypesTests.staticGet5() -> char."));
                });

                runner.test("with existing static method with return type", (Test test) ->
                {
                    test.assertThrows(() -> Types.getMethod0(TypesTests.class, "staticGet5", int.class).await(),
                        new NotFoundException("Could not find a member method with the signature qub.TypesTests.staticGet5() -> int."));
                });

                runner.test("with existing static method with wrong boxed void return type", (Test test) ->
                {
                    test.assertThrows(() -> Types.getMethod0(TypesTests.class, "staticPrimitiveEmpty", Void.class).await(),
                        new NotFoundException("Could not find a member method with the signature qub.TypesTests.staticPrimitiveEmpty() -> java.lang.Void."));
                });

                runner.test("with existing static method with correct primitive void return type", (Test test) ->
                {
                    test.assertThrows(() -> Types.getMethod0(TypesTests.class, "staticPrimitiveEmpty", void.class).await(),
                        new NotFoundException("Could not find a member method with the signature qub.TypesTests.staticPrimitiveEmpty() -> void."));
                });

                runner.test("with existing static method with wrong primitive void return type", (Test test) ->
                {
                    test.assertThrows(() -> Types.getMethod0(TypesTests.class, "staticBoxedEmpty", void.class).await(),
                        new NotFoundException("Could not find a member method with the signature qub.TypesTests.staticBoxedEmpty() -> void."));
                });

                runner.test("with existing static method with correct boxed void return type", (Test test) ->
                {
                    test.assertThrows(() -> Types.getMethod0(TypesTests.class, "staticBoxedEmpty", Void.class).await(),
                        new NotFoundException("Could not find a member method with the signature qub.TypesTests.staticBoxedEmpty() -> java.lang.Void."));
                });
            });

            runner.testGroup("getStaticMethod0(Class<TType>,String)", () ->
            {
                runner.test("with null type", (Test test) ->
                {
                    test.assertThrows(() -> Types.getStaticMethod0(null, "hello"),
                        new PreConditionFailure("type cannot be null."));
                });

                runner.test("with null methodName", (Test test) ->
                {
                    test.assertThrows(() -> Types.getStaticMethod0(TypesTests.class, null),
                        new PreConditionFailure("methodName cannot be null."));
                });

                runner.test("with empty methodName", (Test test) ->
                {
                    test.assertThrows(() -> Types.getStaticMethod0(TypesTests.class, ""),
                        new PreConditionFailure("methodName cannot be empty."));
                });

                runner.test("with non-existing method", (Test test) ->
                {
                    test.assertThrows(() -> Types.getStaticMethod0(TypesTests.class, "blah").await(),
                        new NotFoundException("Could not find a static method with the signature qub.TypesTests.blah() -> ?."));
                });

                runner.test("with existing static method with different parameter types", (Test test) ->
                {
                    test.assertThrows(() -> Types.getStaticMethod0(TypesTests.class, "staticAdd1").await(),
                        new NotFoundException("Could not find a static method with the signature qub.TypesTests.staticAdd1() -> ?."));
                });

                runner.test("with existing member method", (Test test) ->
                {
                    test.assertThrows(() -> Types.getStaticMethod0(TypesTests.class, "memberGet6").await(),
                        new NotFoundException("Could not find a static method with the signature qub.TypesTests.memberGet6() -> ?."));
                });

                runner.test("with existing static method with return type", (Test test) ->
                {
                    final StaticMethod0<TypesTests,?> staticMethod = Types.getStaticMethod0(TypesTests.class, "staticGet5").await();
                    test.assertNotNull(staticMethod);
                    test.assertSame(TypesTests.class, staticMethod.getType());
                    test.assertEqual(5, staticMethod.run());
                });

                runner.test("with existing static method with void return type", (Test test) ->
                {
                    final StaticMethod0<TypesTests,?> staticMethod = Types.getStaticMethod0(TypesTests.class, "staticPrimitiveEmpty").await();
                    test.assertNotNull(staticMethod);
                    test.assertSame(TypesTests.class, staticMethod.getType());
                    test.assertNull(staticMethod.run());
                });
            });

            runner.testGroup("getStaticMethod0(Class<TType>,String,Class<TReturn>)", () ->
            {
                runner.test("with null type", (Test test) ->
                {
                    test.assertThrows(() -> Types.getStaticMethod0(null, "hello", int.class),
                        new PreConditionFailure("type cannot be null."));
                });

                runner.test("with null methodName", (Test test) ->
                {
                    test.assertThrows(() -> Types.getStaticMethod0(TypesTests.class, null, int.class),
                        new PreConditionFailure("methodName cannot be null."));
                });

                runner.test("with empty methodName", (Test test) ->
                {
                    test.assertThrows(() -> Types.getStaticMethod0(TypesTests.class, "", int.class),
                        new PreConditionFailure("methodName cannot be empty."));
                });

                runner.test("with non-existing method", (Test test) ->
                {
                    test.assertThrows(() -> Types.getStaticMethod0(TypesTests.class, "blah", int.class).await(),
                        new NotFoundException("Could not find a static method with the signature qub.TypesTests.blah() -> int."));
                });

                runner.test("with existing member method with different return type", (Test test) ->
                {
                    test.assertThrows(() -> Types.getStaticMethod0(TypesTests.class, "memberGet6", char.class).await(),
                        new NotFoundException("Could not find a static method with the signature qub.TypesTests.memberGet6() -> char."));
                });

                runner.test("with existing member method with same return type", (Test test) ->
                {
                    test.assertThrows(() -> Types.getStaticMethod0(TypesTests.class, "memberGet6", int.class).await(),
                        new NotFoundException("Could not find a static method with the signature qub.TypesTests.memberGet6() -> int."));
                });

                runner.test("with existing static method with different parameter types", (Test test) ->
                {
                    test.assertThrows(() -> Types.getStaticMethod0(TypesTests.class, "staticAdd1", int.class).await(),
                        new NotFoundException("Could not find a static method with the signature qub.TypesTests.staticAdd1() -> int."));
                });

                runner.test("with existing static method with wrong return type", (Test test) ->
                {
                    test.assertThrows(() -> Types.getStaticMethod0(TypesTests.class, "staticGet5", char.class).await(),
                        new NotFoundException("Could not find a static method with the signature qub.TypesTests.staticGet5() -> char."));
                });

                runner.test("with existing static method with return type", (Test test) ->
                {
                    final StaticMethod0<TypesTests,Integer> staticMethod = Types.getStaticMethod0(TypesTests.class, "staticGet5", int.class).await();
                    test.assertNotNull(staticMethod);
                    test.assertSame(TypesTests.class, staticMethod.getType());
                    test.assertEqual(5, staticMethod.run());
                });

                runner.test("with existing static method with wrong boxed void return type", (Test test) ->
                {
                    test.assertThrows(() -> Types.getStaticMethod0(TypesTests.class, "staticPrimitiveEmpty", Void.class).await(),
                        new NotFoundException("Could not find a static method with the signature qub.TypesTests.staticPrimitiveEmpty() -> java.lang.Void."));
                });

                runner.test("with existing static method with correct primitive void return type", (Test test) ->
                {
                    final StaticMethod0<TypesTests,Void> staticMethod = Types.getStaticMethod0(TypesTests.class, "staticPrimitiveEmpty", void.class).await();
                    test.assertNotNull(staticMethod);
                    test.assertSame(TypesTests.class, staticMethod.getType());
                    test.assertNull(staticMethod.run());
                });

                runner.test("with existing static method with wrong primitive void return type", (Test test) ->
                {
                    test.assertThrows(() -> Types.getStaticMethod0(TypesTests.class, "staticBoxedEmpty", void.class).await(),
                        new NotFoundException("Could not find a static method with the signature qub.TypesTests.staticBoxedEmpty() -> void."));
                });

                runner.test("with existing static method with correct boxed void return type", (Test test) ->
                {
                    final StaticMethod0<TypesTests,Void> staticMethod = Types.getStaticMethod0(TypesTests.class, "staticBoxedEmpty", Void.class).await();
                    test.assertNotNull(staticMethod);
                    test.assertSame(TypesTests.class, staticMethod.getType());
                    test.assertNull(staticMethod.run());
                });
            });

            runner.testGroup("getStaticMethod1(Class<TType>,String,Class<T1>)", () ->
            {
                runner.test("with null type", (Test test) ->
                {
                    test.assertThrows(() -> Types.getStaticMethod1(null, "hello", int.class),
                        new PreConditionFailure("type cannot be null."));
                });

                runner.test("with null methodName", (Test test) ->
                {
                    test.assertThrows(() -> Types.getStaticMethod1(TypesTests.class, null, int.class),
                        new PreConditionFailure("methodName cannot be null."));
                });

                runner.test("with empty methodName", (Test test) ->
                {
                    test.assertThrows(() -> Types.getStaticMethod1(TypesTests.class, "", int.class),
                        new PreConditionFailure("methodName cannot be empty."));
                });

                runner.test("with non-existing method", (Test test) ->
                {
                    test.assertThrows(() -> Types.getStaticMethod1(TypesTests.class, "blah", int.class).await(),
                        new NotFoundException("Could not find a static method with the signature qub.TypesTests.blah(int) -> ?."));
                });

                runner.test("with existing static method with different parameter types", (Test test) ->
                {
                    test.assertThrows(() -> Types.getStaticMethod1(TypesTests.class, "staticAdd1", char.class).await(),
                        new NotFoundException("Could not find a static method with the signature qub.TypesTests.staticAdd1(char) -> ?."));
                });

                runner.test("with existing static method", (Test test) ->
                {
                    final StaticMethod1<TypesTests,Integer,?> staticMethod = Types.getStaticMethod1(TypesTests.class, "staticAdd1", int.class).await();
                    test.assertNotNull(staticMethod);
                    test.assertSame(TypesTests.class, staticMethod.getType());
                    test.assertEqual(7, staticMethod.run(6));
                });
            });

            runner.testGroup("getStaticMethod1(Class<TType>,String,Class<T1>,Class<TReturn>)", () ->
            {
                runner.test("with null type", (Test test) ->
                {
                    test.assertThrows(() -> Types.getStaticMethod1(null, "hello", int.class, int.class),
                        new PreConditionFailure("type cannot be null."));
                });

                runner.test("with null methodName", (Test test) ->
                {
                    test.assertThrows(() -> Types.getStaticMethod1(TypesTests.class, null, int.class, int.class),
                        new PreConditionFailure("methodName cannot be null."));
                });

                runner.test("with empty methodName", (Test test) ->
                {
                    test.assertThrows(() -> Types.getStaticMethod1(TypesTests.class, "", int.class, int.class),
                        new PreConditionFailure("methodName cannot be empty."));
                });

                runner.test("with non-existing method", (Test test) ->
                {
                    test.assertThrows(() -> Types.getStaticMethod1(TypesTests.class, "blah", int.class, int.class).await(),
                        new NotFoundException("Could not find a static method with the signature qub.TypesTests.blah(int) -> int."));
                });

                runner.test("with existing member method with different return type", (Test test) ->
                {
                    test.assertThrows(() -> Types.getStaticMethod1(TypesTests.class, "memberGet6", int.class, char.class).await(),
                        new NotFoundException("Could not find a static method with the signature qub.TypesTests.memberGet6(int) -> char."));
                });

                runner.test("with existing member method with same return type", (Test test) ->
                {
                    test.assertThrows(() -> Types.getStaticMethod1(TypesTests.class, "memberGet6", int.class, int.class).await(),
                        new NotFoundException("Could not find a static method with the signature qub.TypesTests.memberGet6(int) -> int."));
                });

                runner.test("with existing static method with different parameter types", (Test test) ->
                {
                    test.assertThrows(() -> Types.getStaticMethod1(TypesTests.class, "staticAdd1", char.class, int.class).await(),
                        new NotFoundException("Could not find a static method with the signature qub.TypesTests.staticAdd1(char) -> int."));
                });

                runner.test("with existing static method with wrong return type", (Test test) ->
                {
                    test.assertThrows(() -> Types.getStaticMethod1(TypesTests.class, "staticAdd1", int.class, char.class).await(),
                        new NotFoundException("Could not find a static method with the signature qub.TypesTests.staticAdd1(int) -> char."));
                });

                runner.test("with existing static method with return type", (Test test) ->
                {
                    final StaticMethod1<TypesTests,Integer,Integer> staticMethod = Types.getStaticMethod1(TypesTests.class, "staticAdd1", int.class, int.class).await();
                    test.assertNotNull(staticMethod);
                    test.assertSame(TypesTests.class, staticMethod.getType());
                    test.assertEqual(7, staticMethod.run(6));
                });
            });

            runner.testGroup("instanceOf(Object,Class<?>)", () ->
            {
                final Action3<Object,Class<?>,Boolean> instanceOfTest = (Object value, Class<?> type, Boolean expected) ->
                {
                    runner.test("with " + English.andList(Strings.escapeAndQuote(value), type), (Test test) ->
                    {
                        test.assertEqual(expected, Types.instanceOf(value, type));
                    });
                };

                instanceOfTest.run(null, null, true);
                instanceOfTest.run(null, String.class, false);
                instanceOfTest.run("hello", null, false);
                instanceOfTest.run("hello", Number.class, false);
                instanceOfTest.run("hello", String.class, true);
                instanceOfTest.run("hello", CharSequence.class, true);
                instanceOfTest.run("hello", Object.class, true);
            });

            runner.testGroup("instanceOf(Class<?>,Class<?>)", () ->
            {
                final Action3<Class<?>,Class<?>,Boolean> instanceOfTest = (Class<?> subType, Class<?> type, Boolean expected) ->
                {
                    runner.test("with " + English.andList(subType, type), (Test test) ->
                    {
                        test.assertEqual(expected, Types.instanceOf(subType, type));
                    });
                };

                instanceOfTest.run(null, null, true);
                instanceOfTest.run(null, String.class, false);
                instanceOfTest.run(String.class, null, false);
                instanceOfTest.run(String.class, Number.class, false);
                instanceOfTest.run(String.class, String.class, true);
                instanceOfTest.run(String.class, CharSequence.class, true);
                instanceOfTest.run(String.class, Object.class, true);
            });

            runner.testGroup("instanceOf(Object,Iterable<Class<?>>)", () ->
            {
                final Action3<Object,Iterable<Class<?>>,Throwable> instanceOfErrorTest = (Object value, Iterable<Class<?>> types, Throwable expected) ->
                {
                    runner.test("with " + English.andList(Strings.escapeAndQuote(value), types), (Test test) ->
                    {
                        test.assertThrows(() -> Types.instanceOf(value, types), expected);
                    });
                };

                instanceOfErrorTest.run(null, null, new PreConditionFailure("types cannot be null."));
                instanceOfErrorTest.run(null, Iterable.create(), new PreConditionFailure("types cannot be empty."));

                final Action3<Object,Iterable<Class<?>>,Boolean> instanceOfTest = (Object value, Iterable<Class<?>> types, Boolean expected) ->
                {
                    runner.test("with " + English.andList(Strings.escapeAndQuote(value), types), (Test test) ->
                    {
                        test.assertEqual(expected, Types.instanceOf(value, types));
                    });
                };

                instanceOfTest.run(null, Iterable.create((Class<?>)null), true);
                instanceOfTest.run(null, Iterable.create(String.class), false);
                instanceOfTest.run("hello", Iterable.create((Class<?>)null), false);
                instanceOfTest.run("hello", Iterable.create(Number.class), false);
                instanceOfTest.run("hello", Iterable.create(String.class), true);
                instanceOfTest.run("hello", Iterable.create(CharSequence.class), true);
                instanceOfTest.run("hello", Iterable.create(CharSequence.class, String.class), true);
                instanceOfTest.run("hello", Iterable.create(CharSequence.class, Object.class), true);
                instanceOfTest.run("hello", Iterable.create(CharSequence.class, Number.class), true);
                instanceOfTest.run("hello", Iterable.create(Object.class), true);
            });

            runner.testGroup("instanceOf(Class<?>,Iterable<Class<?>>)", () ->
            {
                final Action3<Class<?>,Iterable<Class<?>>,Throwable> instanceOfErrorTest = (Class<?> subType, Iterable<Class<?>> types, Throwable expected) ->
                {
                    runner.test("with " + English.andList(Strings.escapeAndQuote(subType), types), (Test test) ->
                    {
                        test.assertThrows(() -> Types.instanceOf(subType, types), expected);
                    });
                };

                instanceOfErrorTest.run(null, null, new PreConditionFailure("types cannot be null."));
                instanceOfErrorTest.run(null, Iterable.create(), new PreConditionFailure("types cannot be empty."));

                final Action3<Class<?>,Iterable<Class<?>>,Boolean> instanceOfTest = (Class<?> subType, Iterable<Class<?>> types, Boolean expected) ->
                {
                    runner.test("with " + English.andList(Strings.escapeAndQuote(subType), types), (Test test) ->
                    {
                        test.assertEqual(expected, Types.instanceOf(subType, types));
                    });
                };

                instanceOfTest.run(null, Iterable.create((Class<?>)null), true);
                instanceOfTest.run(null, Iterable.create(String.class), false);
                instanceOfTest.run(String.class, Iterable.create((Class<?>)null), false);
                instanceOfTest.run(String.class, Iterable.create(Number.class), false);
                instanceOfTest.run(String.class, Iterable.create(String.class), true);
                instanceOfTest.run(String.class, Iterable.create(CharSequence.class), true);
                instanceOfTest.run(String.class, Iterable.create(CharSequence.class, String.class), true);
                instanceOfTest.run(String.class, Iterable.create(CharSequence.class, Object.class), true);
                instanceOfTest.run(String.class, Iterable.create(CharSequence.class, Number.class), true);
                instanceOfTest.run(String.class, Iterable.create(Object.class), true);
            });

            runner.testGroup("as(Object)", () ->
            {
                runner.test("null as Integer", (Test test) ->
                {
                    test.assertNull(Types.as(null, Integer.class));
                });

                runner.test("Integer as null", (Test test) ->
                {
                    test.assertThrows(() -> Types.as(20, null),
                        new PreConditionFailure("type cannot be null."));
                });

                runner.test("Integer as Number", (Test test) ->
                {
                    final Number number = Types.as(50, Number.class);
                    test.assertNotNull(number);
                    test.assertEqual(50, number.intValue());
                });

                runner.test("Integer as Integer", (Test test) ->
                {
                    final Integer integer = Types.as(50, Integer.class);
                    test.assertNotNull(integer);
                    test.assertEqual(50, integer);
                });

                runner.test("Integer as Float", (Test test) ->
                {
                    test.assertNull(Types.as(50, Float.class));
                });

                runner.test("Integer as String", (Test test) ->
                {
                    test.assertNull(Types.as(50, String.class));
                });
            });

            runner.testGroup("getTypeName(Object)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertEqual("null", Types.getTypeName((Object)null));
                });

                runner.test("with Object", (Test test) ->
                {
                    test.assertEqual("Object", Types.getTypeName(new Object()));
                });

                runner.test("with String", (Test test) ->
                {
                    test.assertEqual("String", Types.getTypeName("abc"));
                });
            });

            runner.testGroup("getTypeName(Class<?>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertEqual("null", Types.getTypeName((Class<?>)null));
                });

                runner.test("with Object.getClass()", (Test test) ->
                {
                    test.assertEqual("Object", Types.getTypeName(new Object().getClass()));
                });

                runner.test("with Object.class", (Test test) ->
                {
                    test.assertEqual("Object", Types.getTypeName(Object.class));
                });

                runner.test("with String", (Test test) ->
                {
                    test.assertEqual("String", Types.getTypeName("abc".getClass()));
                });

                runner.test("with String.class", (Test test) ->
                {
                    test.assertEqual("String", Types.getTypeName(String.class));
                });
            });

            runner.testGroup("getTypeNameFromFullTypeName(String)", () ->
            {
                final Action2<String,Throwable> getTypeNameFromFullTypeNameErrorTest = (String fullTypeName, Throwable expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(fullTypeName), (Test test) ->
                    {
                        test.assertThrows(() -> Types.getTypeNameFromFullTypeName(fullTypeName),
                            expected);
                    });
                };

                getTypeNameFromFullTypeNameErrorTest.run(null, new PreConditionFailure("fullTypeName cannot be null."));
                getTypeNameFromFullTypeNameErrorTest.run("", new PreConditionFailure("fullTypeName cannot be empty."));

                final Action2<String,String> getTypeNameFromFullTypeNameTest = (String fullTypeName, String expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(fullTypeName), (Test test) ->
                    {
                        test.assertEqual(expected, Types.getTypeNameFromFullTypeName(fullTypeName));
                    });
                };

                getTypeNameFromFullTypeNameTest.run("a", "a");
                getTypeNameFromFullTypeNameTest.run("a.b", "b");
                getTypeNameFromFullTypeNameTest.run("a.b$1", "b$1");
            });

            runner.testGroup("getFullTypeName(Object)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertEqual("null", Types.getFullTypeName((Object)null));
                });

                runner.test("with Object", (Test test) ->
                {
                    test.assertEqual("java.lang.Object", Types.getFullTypeName(new Object()));
                });

                runner.test("with String", (Test test) ->
                {
                    test.assertEqual("java.lang.String", Types.getFullTypeName("abc"));
                });
            });

            runner.testGroup("getFullTypeName(Class<?>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertEqual("null", Types.getFullTypeName((Class<?>)null));
                });

                runner.test("with Object.getClass()", (Test test) ->
                {
                    test.assertEqual("java.lang.Object", Types.getFullTypeName(new Object().getClass()));
                });

                runner.test("with Object.class", (Test test) ->
                {
                    test.assertEqual("java.lang.Object", Types.getFullTypeName(Object.class));
                });

                runner.test("with String", (Test test) ->
                {
                    test.assertEqual("java.lang.String", Types.getFullTypeName("abc".getClass()));
                });
            });
        });
    }

    private static NotFoundException getExpectedMethodNotFoundException(String expectedMethodSignature)
    {
        PreCondition.assertNotNullAndNotEmpty(expectedMethodSignature, "expectedMethodSignature");

        return new NotFoundException("Could not find a method with the signature " + expectedMethodSignature + ".");
    }
}