package qub;

public interface Tuple1Tests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(Tuple1.class, () ->
        {
            runner.testGroup("create(T1)", () ->
            {
                final Action1<String> createTest = (String value1) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(value1), (Test test) ->
                    {
                        final Tuple1<String> tuple = Tuple1.create(value1);
                        test.assertNotNull(tuple);
                        test.assertSame(value1, tuple.getValue1());
                    });
                };

                createTest.run(null);
                createTest.run("hello");
            });

            runner.testGroup("equals(Object)", () ->
            {
                final Action3<Tuple1<Integer>,Object,Boolean> equalsTest = (Tuple1<Integer> tuple, Object rhs, Boolean expected) ->
                {
                    runner.test("with " + English.andList(tuple, rhs), (Test test) ->
                    {
                        test.assertEqual(expected, tuple.equals(rhs));
                    });
                };

                equalsTest.run(Tuple1.create(1), null, false);
                equalsTest.run(Tuple1.create(1), 1, false);
                equalsTest.run(Tuple1.create(1), Tuple1.create(null), false);
                equalsTest.run(Tuple1.create(1), Tuple1.create(1), true);
                equalsTest.run(Tuple1.create(1), Tuple1.create(2), false);
            });

            runner.testGroup("toString()", () ->
            {
                final Action2<Tuple1<String>,String> toStringTest = (Tuple1<String> tuple, String expected) ->
                {
                    runner.test("with " + tuple, (Test test) ->
                    {
                        test.assertEqual(expected, tuple.toString());
                    });
                };

                toStringTest.run(Tuple1.create(null), "[null]");
                toStringTest.run(Tuple1.create(""), "[]");
                toStringTest.run(Tuple1.create("a"), "[a]");
            });
        });
    }
}
