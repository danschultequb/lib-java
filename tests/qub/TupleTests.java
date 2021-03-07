package qub;

public interface TupleTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(Tuple.class, () ->
        {
            runner.testGroup("create(T1)", () ->
            {
                final Action1<String> createTest = (String value1) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(value1), (Test test) ->
                    {
                        final Tuple1<String> tuple = Tuple.create(value1);
                        test.assertNotNull(tuple);
                        test.assertSame(value1, tuple.getValue1());
                    });
                };

                createTest.run(null);
                createTest.run("hello");
            });

            runner.testGroup("create(T1,T2)", () ->
            {
                final Action2<Boolean,Long> createTest = (Boolean value1, Long value2) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(value1), (Test test) ->
                    {
                        final Tuple2<Boolean,Long> tuple = Tuple.create(value1, value2);
                        test.assertNotNull(tuple);
                        test.assertEqual(value1, tuple.getValue1());
                        test.assertEqual(value2, tuple.getValue2());
                    });
                };

                createTest.run(null, null);
                createTest.run(false, 5L);
            });

            runner.testGroup("create(T1,T2,T3)", () ->
            {
                final Action3<Boolean,Long,Double> createTest = (Boolean value1, Long value2, Double value3) ->
                {
                    runner.test("with " + English.andList(value1, value2, value3), (Test test) ->
                    {
                        final Tuple3<Boolean,Long,Double> tuple = Tuple.create(value1, value2, value3);
                        test.assertNotNull(tuple);
                        test.assertEqual(value1, tuple.getValue1());
                        test.assertEqual(value2, tuple.getValue2());
                        test.assertEqual(value3, tuple.getValue3());
                    });
                };

                createTest.run(null, null, null);
                createTest.run(false, 5L, 50.0);
            });
        });
    }
}
