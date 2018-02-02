package qub;

public class ValueTests
{
    public static void test(final TestRunner runner)
    {
        runner.testGroup("Value<T>", new Action0()
        {
            @Override
            public void run()
            {
                runner.testGroup("constructor()", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("with no arguments", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Value<Character> value = new Value<>();
                                test.assertFalse(value.hasValue());
                                test.assertNull(value.get());
                            }
                        });

                        runner.test("with null argument", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Value<Character> value = new Value<>(null);
                                test.assertTrue(value.hasValue());
                                test.assertNull(value.get());
                            }
                        });

                        runner.test("with argument", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Value<Character> value = new Value<>('n');
                                test.assertTrue(value.hasValue());
                                test.assertEqual('n', value.get());
                            }
                        });
                    }
                });

                runner.testGroup("set()", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("with null", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Value<Character> value = new Value<>();
                                value.set(null);
                                test.assertTrue(value.hasValue());
                                test.assertNull(value.get());
                            }
                        });

                        runner.test("with non-null", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Value<Character> value = new Value<>();
                                value.set('z');
                                test.assertTrue(value.hasValue());
                                test.assertEqual('z', value.get());
                            }
                        });
                    }
                });

                runner.test("clear()", new Action1<Test>()
                {
                    @Override
                    public void run(Test test)
                    {
                        final Value<Character> value = new Value<>('v');
                        value.clear();
                        test.assertFalse(value.hasValue());
                        test.assertNull(value.get());
                    }
                });
            }
        });
    }
}
