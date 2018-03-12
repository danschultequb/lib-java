package qub;

public class ComparerTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(Comparer.class, () ->
        {
            runner.test("constructor()", (Test test) ->
            {
                test.assertNotNull(new Comparer());
            });

            runner.testGroup("equal()", () ->
            {
                final Action3<Object,Object,Boolean> equalTest = (Object lhs, Object rhs, Boolean expected) ->
                {
                    runner.test("with " + lhs + " and " + rhs, (Test test) ->
                    {
                        test.assertEqual(expected, Comparer.equal(lhs, rhs));
                    });
                };

                equalTest.run(null, null, true);
                equalTest.run(null, false, false);
                equalTest.run(false, false, true);
                equalTest.run(false, true, false);
                equalTest.run(20, 20, true);
                equalTest.run(new char[0], new char[0], true);
            });
        });
    }
}
