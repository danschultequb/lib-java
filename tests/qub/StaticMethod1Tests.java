package qub;

public interface StaticMethod1Tests
{
    static int staticMethod1(int value)
    {
        return value + 5;
    }

    static String staticMethod2(Character value)
    {
        throw new NotFoundException("blah - " + value);
    }

    static void test(TestRunner runner)
    {
        runner.testGroup(StaticMethod1.class, () ->
        {
            runner.testGroup("get(Class<TType>,java.lang.reflect.Method)", () ->
            {
                runner.test("with null type", (Test test) ->
                {
                    test.assertThrows(() -> StaticMethod1.get(null, Types.getRawMethod(StaticMethod1Tests.class, "staticMethod1", int.class).await()),
                        new PreConditionFailure("type cannot be null."));
                });

                runner.test("with null staticMethod", (Test test) ->
                {
                    test.assertThrows(() -> StaticMethod1.get(StaticMethod1Tests.class, null),
                        new PreConditionFailure("staticMethod cannot be null."));
                });

                runner.test("with valid arguments", (Test test) ->
                {
                    final StaticMethod1<StaticMethod1Tests,java.lang.Integer,java.lang.Integer> staticMethod =
                        StaticMethod1.get(StaticMethod1Tests.class, Types.getRawMethod(StaticMethod1Tests.class, "staticMethod1", int.class).await());
                    test.assertNotNull(staticMethod);
                    test.assertSame(StaticMethod1Tests.class, staticMethod.getType());
                });

                runner.test("with wrong return type", (Test test) ->
                {
                    final StaticMethod1<StaticMethod1Tests,java.lang.Integer,java.lang.Boolean> staticMethod =
                        StaticMethod1.get(StaticMethod1Tests.class, Types.getRawMethod(StaticMethod1Tests.class, "staticMethod2", Character.class).await());
                    test.assertNotNull(staticMethod);
                    test.assertSame(StaticMethod1Tests.class, staticMethod.getType());
                });
            });

            runner.testGroup("run()", () ->
            {
                runner.test("when static method doesn't throw an exception", (Test test) ->
                {
                    final StaticMethod1<StaticMethod1Tests,java.lang.Integer,java.lang.Integer> staticMethod =
                        StaticMethod1.get(StaticMethod1Tests.class, Types.getRawMethod(StaticMethod1Tests.class, "staticMethod1", int.class).await());
                    test.assertEqual(12, staticMethod.run(7));
                    test.assertEqual(17, staticMethod.run(12));
                });

                runner.test("when static method throws an exception", (Test test) ->
                {
                    final StaticMethod1<StaticMethod1Tests,Character,String> staticMethod =
                        StaticMethod1.get(StaticMethod1Tests.class, Types.getRawMethod(StaticMethod1Tests.class, "staticMethod2", Character.class).await());
                    test.assertThrows(() -> staticMethod.run('a'), new NotFoundException("blah - a"));
                    test.assertThrows(() -> staticMethod.run(';'), new NotFoundException("blah - ;"));
                });
            });
        });
    }
}
