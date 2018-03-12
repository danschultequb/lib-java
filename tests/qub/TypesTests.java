package qub;

public class TypesTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(Types.class, () ->
        {
            runner.test("constructor()", (Test test) ->
            {
                test.assertNotNull(new Types());
            });

            runner.testGroup("as(Object)", () ->
            {
                runner.test("null as Integer", (Test test) ->
                {
                    test.assertNull(Types.as(null, Integer.class));
                });

                runner.test("Integer as null", (Test test) ->
                {
                    test.assertNull(Types.as(20, null));
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
        });
    }
}
