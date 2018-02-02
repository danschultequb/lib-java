package qub;

public class ComparerTests
{
    public static void test(final TestRunner runner)
    {
        runner.testGroup("Comparer", new Action0()
        {
            @Override
            public void run()
            {
                runner.test("constructor()", new Action1<Test>()
                {
                    @Override
                    public void run(Test test)
                    {
                        new Comparer();
                    }
                });

                runner.testGroup("equal()", new Action0()
                {
                    @Override
                    public void run()
                    {
                        final Action3<Object,Object,Boolean> equalTest = new Action3<Object, Object, Boolean>()
                        {
                            @Override
                            public void run(final Object lhs, final Object rhs, final Boolean expected)
                            {
                                runner.test("with " + lhs + " and " + rhs, new Action1<Test>()
                                {
                                    @Override
                                    public void run(Test test)
                                    {
                                        test.assertEqual(expected, Comparer.equal(lhs, rhs));
                                    }
                                });
                            }
                        };

                        equalTest.run(null, null, true);
                        equalTest.run(null, false, false);
                        equalTest.run(false, false, true);
                        equalTest.run(false, true, false);
                        equalTest.run(20, 20, true);
                        equalTest.run(new char[0], new char[0], true);
                    }
                });
            }
        });
    }
}
