package qub;

public interface Tuple4Tests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(Tuple4.class, () ->
        {
            runner.testGroup("create(T1,T2,T3,T4)", () ->
            {
                final Action4<Boolean,String,Boolean,Integer> createTest = (Boolean value1, String value2, Boolean value3, Integer value4) ->
                {
                    runner.test("with " + English.andList(value1, Strings.escapeAndQuote(value2), value3, value4), (Test test) ->
                    {
                        final Tuple4<Boolean,String,Boolean,Integer> tuple = Tuple4.create(value1, value2, value3, value4);
                        test.assertNotNull(tuple);
                        test.assertSame(value1, tuple.getValue1());
                        test.assertSame(value2, tuple.getValue2());
                        test.assertSame(value3, tuple.getValue3());
                        test.assertSame(value4, tuple.getValue4());
                    });
                };

                createTest.run(false, null, null, 5);
                createTest.run(true, "hello", false, null);
            });

            runner.testGroup("equals(Object)", () ->
            {
                final Action3<Tuple4<String,Integer,Double,Boolean>,Object,Boolean> equalsTest = (Tuple4<String,Integer,Double,Boolean> tuple, Object rhs, Boolean expected) ->
                {
                    runner.test("with " + English.andList(tuple, rhs), (Test test) ->
                    {
                        test.assertEqual(expected, tuple.equals(rhs));
                    });
                };

                equalsTest.run(Tuple.create("a", 1, 2.0, false), null, false);
                equalsTest.run(Tuple.create("a", 1, 2.0, false), 1, false);
                equalsTest.run(Tuple.create("a", 1, 2.0, false), Tuple.create(null), false);
                equalsTest.run(Tuple.create("a", 1, 2.0, false), Tuple.create(null, null), false);
                equalsTest.run(Tuple.create("a", 1, 2.0, false), Tuple.create(null, null, null), false);
                equalsTest.run(Tuple.create("a", 1, 2.0, false), Tuple.create("a", 1, 2.0), false);
                equalsTest.run(Tuple.create("a", 1, 2.0, false), Tuple.create("a", 1, 2.0, false), true);
                equalsTest.run(Tuple.create("a", 1, 2.0, false), Tuple.create("a", 1, 2.0, true), false);
                equalsTest.run(Tuple.create("a", 1, 2.0, false), Tuple.create("a", 1, 2.0, "false"), false);
                equalsTest.run(Tuple.create("a", 1, 2.0, false), Tuple.create('a', 1, 2, false), false);
                equalsTest.run(Tuple.create("a", 1, 2.0, false), Tuple.create("a", 1.0, 2.0, false), false);
                equalsTest.run(Tuple.create("a", 1, 2.0, false), Tuple.create("a", 2, 2.0, false), false);
                equalsTest.run(Tuple.create("a", 1, 2.0, false), Tuple.create("b", 1, 2.0, false), false);
                equalsTest.run(Tuple.create("a", 1, 2.0, false), Tuple.create("a", 1, 2.1, false), false);
            });

            runner.testGroup("toString()", () ->
            {
                final Action2<Tuple4<String,String,Boolean,Character>,String> toStringTest = (Tuple4<String,String,Boolean,Character> tuple, String expected) ->
                {
                    runner.test("with " + tuple, (Test test) ->
                    {
                        test.assertEqual(expected, tuple.toString());
                    });
                };

                toStringTest.run(Tuple.create(null, null, null, null), "[null,null,null,null]");
                toStringTest.run(Tuple.create("", "", false, 'a'), "[,,false,a]");
                toStringTest.run(Tuple.create("a", "b", true, 'y'), "[a,b,true,y]");
            });
        });
    }
}
