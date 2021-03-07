package qub;

public interface Tuple3Tests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(Tuple3.class, () ->
        {
            runner.testGroup("create(T1,T2,T3)", () ->
            {
                final Action3<Boolean,String,Boolean> createTest = (Boolean value1, String value2, Boolean value3) ->
                {
                    runner.test("with " + English.andList(value1, Strings.escapeAndQuote(value2), value3), (Test test) ->
                    {
                        final Tuple3<Boolean,String,Boolean> tuple = Tuple3.create(value1, value2, value3);
                        test.assertNotNull(tuple);
                        test.assertEqual(value1, tuple.getValue1());
                        test.assertSame(value2, tuple.getValue2());
                        test.assertEqual(value3, tuple.getValue3());
                    });
                };

                createTest.run(false, null, null);
                createTest.run(true, "hello", false);
            });

            runner.testGroup("equals(Object)", () ->
            {
                final Action3<Tuple3<String,Integer,Double>,Object,Boolean> equalsTest = (Tuple3<String,Integer,Double> tuple, Object rhs, Boolean expected) ->
                {
                    runner.test("with " + English.andList(tuple, rhs), (Test test) ->
                    {
                        test.assertEqual(expected, tuple.equals(rhs));
                    });
                };

                equalsTest.run(Tuple.create("a", 1, 2.0), null, false);
                equalsTest.run(Tuple.create("a", 1, 2.0), 1, false);
                equalsTest.run(Tuple.create("a", 1, 2.0), Tuple.create(null), false);
                equalsTest.run(Tuple.create("a", 1, 2.0), Tuple.create(null, null), false);
                equalsTest.run(Tuple.create("a", 1, 2.0), Tuple.create(null, null, null), false);
                equalsTest.run(Tuple.create("a", 1, 2.0), Tuple.create("a", 1, 2.0), true);
                equalsTest.run(Tuple.create("a", 1, 2.0), Tuple.create('a', 1, 2), false);
                equalsTest.run(Tuple.create("a", 1, 2.0), Tuple.create("a", 1.0, 2.0), false);
                equalsTest.run(Tuple.create("a", 1, 2.0), Tuple.create("a", 2, 2.0), false);
                equalsTest.run(Tuple.create("a", 1, 2.0), Tuple.create("b", 1, 2.0), false);
                equalsTest.run(Tuple.create("a", 1, 2.0), Tuple.create("a", 1, 2.1), false);
            });

            runner.testGroup("toString()", () ->
            {
                final Action2<Tuple3<String,String,Boolean>,String> toStringTest = (Tuple3<String,String,Boolean> tuple, String expected) ->
                {
                    runner.test("with " + tuple, (Test test) ->
                    {
                        test.assertEqual(expected, tuple.toString());
                    });
                };

                toStringTest.run(Tuple.create(null, null, null), "[null,null,null]");
                toStringTest.run(Tuple.create("", "", false), "[,,false]");
                toStringTest.run(Tuple.create("a", "b", true), "[a,b,true]");
            });
        });
    }
}
