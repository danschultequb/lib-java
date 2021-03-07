package qub;

public interface Tuple2Tests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(Tuple2.class, () ->
        {
            runner.testGroup("create(T1,T2)", () ->
            {
                final Action2<Boolean,String> createTest = (Boolean value1, String value2) ->
                {
                    runner.test("with " + English.andList(value1, Strings.escapeAndQuote(value2)), (Test test) ->
                    {
                        final Tuple2<Boolean,String> tuple = Tuple2.create(value1, value2);
                        test.assertNotNull(tuple);
                        test.assertEqual(value1, tuple.getValue1());
                        test.assertSame(value2, tuple.getValue2());
                    });
                };

                createTest.run(false, null);
                createTest.run(true, "hello");
            });

            runner.testGroup("equals(Object)", () ->
            {
                final Action3<Tuple2<String,Integer>,Object,Boolean> equalsTest = (Tuple2<String,Integer> tuple, Object rhs, Boolean expected) ->
                {
                    runner.test("with " + English.andList(tuple, rhs), (Test test) ->
                    {
                        test.assertEqual(expected, tuple.equals(rhs));
                    });
                };

                equalsTest.run(Tuple.create("a", 1), null, false);
                equalsTest.run(Tuple.create("a", 1), 1, false);
                equalsTest.run(Tuple.create("a", 1), Tuple.create(null, null), false);
                equalsTest.run(Tuple.create("a", 1), Tuple.create("a", 1), true);
                equalsTest.run(Tuple.create("a", 1), Tuple.create('a', 1), false);
                equalsTest.run(Tuple.create("a", 1), Tuple.create("a", 1.0), false);
                equalsTest.run(Tuple.create("a", 1), Tuple.create("a", 2), false);
                equalsTest.run(Tuple.create("a", 1), Tuple.create("b", 1), false);
            });

            runner.testGroup("toString()", () ->
            {
                final Action2<Tuple2<String,String>,String> toStringTest = (Tuple2<String,String> tuple, String expected) ->
                {
                    runner.test("with " + tuple, (Test test) ->
                    {
                        test.assertEqual(expected, tuple.toString());
                    });
                };

                toStringTest.run(Tuple.create(null, null), "[null,null]");
                toStringTest.run(Tuple.create("", ""), "[,]");
                toStringTest.run(Tuple.create("a", "b"), "[a,b]");
            });
        });
    }
}
