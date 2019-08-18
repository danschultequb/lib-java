package qub;

public interface StaticMethod0Tests
{
    static int staticMethod1()
    {
        return 5;
    }

    static String staticMethod2()
    {
        throw new NotFoundException("blah");
    }

    static void test(TestRunner runner)
    {
        runner.testGroup(StaticMethod0.class, () ->
        {
            runner.testGroup("get(Class<TType>,java.lang.reflect.Method)", () ->
            {
                runner.test("with null type", (Test test) ->
                {
                    test.assertThrows(() -> StaticMethod0.get(null, Types.getRawMethod(StaticMethod0Tests.class, "staticMethod1").await()),
                        new PreConditionFailure("type cannot be null."));
                });

                runner.test("with null staticMethod", (Test test) ->
                {
                    test.assertThrows(() -> StaticMethod0.get(StaticMethod0Tests.class, null),
                        new PreConditionFailure("staticMethod cannot be null."));
                });

                runner.test("with valid arguments", (Test test) ->
                {
                    final StaticMethod0<StaticMethod0Tests,java.lang.Integer> staticMethod =
                        StaticMethod0.get(StaticMethod0Tests.class, Types.getRawMethod(StaticMethod0Tests.class, "staticMethod1").await());
                    test.assertNotNull(staticMethod);
                    test.assertSame(StaticMethod0Tests.class, staticMethod.getType());
                });

                runner.test("with wrong return type", (Test test) ->
                {
                    final StaticMethod0<StaticMethod0Tests,java.lang.Integer> staticMethod =
                        StaticMethod0.get(StaticMethod0Tests.class, Types.getRawMethod(StaticMethod0Tests.class, "staticMethod2").await());
                    test.assertNotNull(staticMethod);
                    test.assertSame(StaticMethod0Tests.class, staticMethod.getType());
                });
            });

            runner.testGroup("run()", () ->
            {
                runner.test("when static method doesn't throw an exception", (Test test) ->
                {
                    final StaticMethod0<StaticMethod0Tests,java.lang.Integer> staticMethod =
                        StaticMethod0.get(StaticMethod0Tests.class, Types.getRawMethod(StaticMethod0Tests.class, "staticMethod1").await());
                    test.assertEqual(5, staticMethod.run());
                    test.assertEqual(5, staticMethod.run());
                });

                runner.test("when static method throws an exception", (Test test) ->
                {
                    final StaticMethod0<StaticMethod0Tests,java.lang.String> staticMethod =
                        StaticMethod0.get(StaticMethod0Tests.class, Types.getRawMethod(StaticMethod0Tests.class, "staticMethod2").await());
                    test.assertThrows(() -> staticMethod.run(), new NotFoundException("blah"));
                    test.assertThrows(() -> staticMethod.run(), new NotFoundException("blah"));
                });
            });
        });
    }
}
