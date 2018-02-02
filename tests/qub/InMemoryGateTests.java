package qub;

public class InMemoryGateTests
{
    public static void test(final TestRunner runner)
    {
        runner.testGroup("InMemoryGate", new Action0()
        {
            @Override
            public void run()
            {
                runner.testGroup("constructor(boolean)", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("with true", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final InMemoryGate gate = new InMemoryGate(true);
                                test.assertTrue(gate.isOpen());
                            }
                        });

                        runner.test("with false", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final InMemoryGate gate = new InMemoryGate(false);
                                test.assertFalse(gate.isOpen());
                            }
                        });
                    }
                });

                runner.testGroup("open()", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("when open", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final InMemoryGate gate = new InMemoryGate(true);
                                test.assertFalse(gate.open());
                                test.assertTrue(gate.isOpen());
                            }
                        });

                        runner.test("when closed", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final InMemoryGate gate = new InMemoryGate(false);
                                test.assertTrue(gate.open());
                                test.assertTrue(gate.isOpen());
                            }
                        });
                    }
                });

                runner.testGroup("close()", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("when closed", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final InMemoryGate gate = new InMemoryGate(false);
                                test.assertFalse(gate.close());
                                test.assertFalse(gate.isOpen());
                            }
                        });

                        runner.test("when open", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final InMemoryGate gate = new InMemoryGate(true);
                                test.assertTrue(gate.close());
                                test.assertFalse(gate.isOpen());
                            }
                        });
                    }
                });
            }
        });
    }
}
