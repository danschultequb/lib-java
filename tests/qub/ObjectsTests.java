package qub;

public interface ObjectsTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(Objects.class, () ->
        {
            runner.testGroup("coalesce(T...)", () ->
            {
                runner.test("with no arguments", (Test test) ->
                {
                    test.assertNull(Objects.coalesce());
                });

                runner.test("with one null argument", (Test test) ->
                {
                    final String result = Objects.coalesce((String)null);
                    test.assertNull(result);
                });

                runner.test("with two null arguments", (Test test) ->
                {
                    final Object result = Objects.coalesce((String)null, (Integer)null);
                    test.assertNull(result);
                });

                runner.test("with null followed by non-null", (Test test) ->
                {
                    final Boolean result = Objects.coalesce(null, false);
                    test.assertFalse(result);
                });

                runner.test("with two non-null values", (Test test) ->
                {
                    final Integer result = Objects.coalesce(1, 2);
                    test.assertEqual(1, result);
                });
            });

            runner.testGroup("coalesce(Function0<? extends T>)", () ->
            {
                runner.test("with a null value followed by a lambda function that returns null", (Test test) ->
                {
                    final IntegerValue counter = IntegerValue.create(0);
                    final Integer result = Objects.coalesce(
                        () -> null,
                        () ->
                        {
                            counter.increment();
                            return 5;
                        });
                    test.assertEqual(5, result);
                    test.assertEqual(1, counter.get());
                });

                runner.test("with a non-null value followed by null", (Test test) ->
                {
                    final IntegerValue counter = IntegerValue.create(0);
                    final Integer result = Objects.coalesce(
                        () -> 5,
                        () ->
                        {
                            counter.increment();
                            return null;
                        });
                    test.assertEqual(5, result);
                    test.assertEqual(0, counter.get());
                });

                runner.test("with method references", (Test test) ->
                {
                    final IntegerValue value1 = IntegerValue.create(1);
                    final IntegerValue value2 = IntegerValue.create(2);
                    final Integer result = Objects.coalesce(value1::get, value2::get);
                    test.assertEqual(1, result);
                });
            });
        });
    }
}
