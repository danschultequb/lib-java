package qub;

public class MathTests
{
    public static void test(final TestRunner runner)
    {
        runner.testGroup("Math", new Action0()
        {
            @Override
            public void run()
            {
                runner.test("constructor", new Action1<Test>()
                {
                    @Override
                    public void run(Test test)
                    {
                        new Math();
                    }
                });
                
                runner.test("minimum()", new Action1<Test>()
                {
                    @Override
                    public void run(Test test)
                    {
                        test.assertEqual(-10, Math.minimum(-10, -5));
                        test.assertEqual(-10, Math.minimum(-5, -10));

                        test.assertEqual(-3, Math.minimum(-3, -3));

                        test.assertEqual(-1, Math.minimum(-1, 0));
                        test.assertEqual(-1, Math.minimum(0, -1));
                    }
                });
                
                runner.test("maximum()", new Action1<Test>()
                {
                    @Override
                    public void run(Test test)
                    {
                        test.assertEqual(-5, Math.maximum(-10, -5));
                        test.assertEqual(-5, Math.maximum(-5, -10));

                        test.assertEqual(-3, Math.maximum(-3, -3));

                        test.assertEqual(0, Math.maximum(-1, 0));
                        test.assertEqual(0, Math.maximum(0, -1));
                    }
                });
                
                runner.test("isOdd()", new Action1<Test>()
                {
                    @Override
                    public void run(Test test)
                    {
                        test.assertTrue(Math.isOdd(1));
                        test.assertTrue(Math.isOdd(3));
                        test.assertTrue(Math.isOdd(-1));

                        test.assertFalse(Math.isOdd(0));
                        test.assertFalse(Math.isOdd(2));
                        test.assertFalse(Math.isOdd(-2));

                        test.assertFalse(Math.isOdd.run(null));

                        test.assertTrue(Math.isOdd.run(1));
                        test.assertTrue(Math.isOdd.run(3));
                        test.assertTrue(Math.isOdd.run(-1));

                        test.assertFalse(Math.isOdd.run(0));
                        test.assertFalse(Math.isOdd.run(2));
                        test.assertFalse(Math.isOdd.run(-2));
                    }
                });

                runner.test("isEven()", new Action1<Test>()
                {
                    @Override
                    public void run(Test test)
                    {
                        test.assertFalse(Math.isEven(1));
                        test.assertFalse(Math.isEven(3));
                        test.assertFalse(Math.isEven(-1));

                        test.assertTrue(Math.isEven(0));
                        test.assertTrue(Math.isEven(2));
                        test.assertTrue(Math.isEven(-2));

                        test.assertFalse(Math.isEven.run(null));

                        test.assertFalse(Math.isEven.run(1));
                        test.assertFalse(Math.isEven.run(3));
                        test.assertFalse(Math.isEven.run(-1));

                        test.assertTrue(Math.isEven.run(0));
                        test.assertTrue(Math.isEven.run(2));
                        test.assertTrue(Math.isEven.run(-2));
                    }
                });

                runner.test("ceiling()", new Action1<Test>()
                {
                    @Override
                    public void run(Test test)
                    {
                        test.assertEqual(0.0, Math.ceiling(0));
                        test.assertEqual(0.0, Math.ceiling(-0));

                        test.assertEqual(1.0, Math.ceiling(0.1));
                        test.assertEqual(-0.0, Math.ceiling(-0.1));

                        test.assertEqual(10.0, Math.ceiling(9.9));
                        test.assertEqual(-9.0, Math.ceiling(-9.9));
                    }
                });

                runner.test("floor()", new Action1<Test>()
                {
                    @Override
                    public void run(Test test)
                    {
                        test.assertEqual(0.0, Math.floor(0));
                        test.assertEqual(0.0, Math.floor(-0));

                        test.assertEqual(0.0, Math.floor(0.1));
                        test.assertEqual(-1.0, Math.floor(-0.1));

                        test.assertEqual(9.0, Math.floor(9.9));
                        test.assertEqual(-10.0, Math.floor(-9.9));
                    }
                });

                runner.testGroup("round(double)", new Action0()
                {
                    @Override
                    public void run()
                    {
                        final Action2<Double,Double> roundTest = new Action2<Double, Double>()
                        {
                            @Override
                            public void run(final Double value, final Double expectedRoundedValue)
                            {
                                runner.test("with " + value, new Action1<Test>()
                                {
                                    @Override
                                    public void run(Test test)
                                    {
                                        test.assertEqual(expectedRoundedValue, Math.round(value));
                                    }
                                });
                            }
                        };

                        roundTest.run(1.0, 1.0);
                        roundTest.run(1.1, 1.0);
                        roundTest.run(1.49, 1.0);
                        roundTest.run(1.5, 2.0);
                        roundTest.run(1.51, 2.0);
                        roundTest.run(1.99, 2.0);
                        roundTest.run(2.0, 2.0);

                        roundTest.run(-1.0, -1.0);
                        roundTest.run(-1.1, -1.0);
                        roundTest.run(-1.49, -1.0);
                        roundTest.run(-1.5, -1.0);
                        roundTest.run(-1.51, -2.0);
                        roundTest.run(-1.99, -2.0);
                        roundTest.run(-2.0, -2.0);
                    }
                });

                runner.testGroup("round(double,double)", new Action0()
                {
                    @Override
                    public void run()
                    {
                        final Action3<Double,Double,Double> roundTest = new Action3<Double, Double, Double>()
                        {
                            @Override
                            public void run(final Double value, final Double scale, final Double expected)
                            {
                                runner.test("with " + value + " to nearest " + scale, new Action1<Test>()
                                {
                                    @Override
                                    public void run(Test test)
                                    {
                                        test.assertEqual(expected, Math.round(value, scale), 0.000000000001);
                                    }
                                });
                            }
                        };

                        roundTest.run(1.0, 1.0, 1.0);
                        roundTest.run(1.1, 1.0, 1.0);
                        roundTest.run(1.49, 1.0, 1.0);
                        roundTest.run(1.5, 1.0, 2.0);
                        roundTest.run(1.51, 1.0, 2.0);
                        roundTest.run(1.99, 1.0, 2.0);
                        roundTest.run(2.0, 1.0, 2.0);

                        roundTest.run(-1.0, 1.0, -1.0);
                        roundTest.run(-1.1, 1.0, -1.0);
                        roundTest.run(-1.49, 1.0, -1.0);
                        roundTest.run(-1.5, 1.0, -1.0);
                        roundTest.run(-1.51, 1.0, -2.0);
                        roundTest.run(-1.99, 1.0, -2.0);
                        roundTest.run(-2.0, 1.0, -2.0);

                        roundTest.run(10.0, 10.0, 10.0);
                        roundTest.run(11.0, 10.0, 10.0);
                        roundTest.run(14.0, 10.0, 10.0);
                        roundTest.run(15.0, 10.0, 20.0);
                        roundTest.run(16.0, 10.0, 20.0);
                        roundTest.run(19.0, 10.0, 20.0);
                        roundTest.run(20.0, 10.0, 20.0);

                        roundTest.run(0.0, 7.0, 0.0);
                        roundTest.run(1.0, 7.0, 0.0);
                        roundTest.run(3.0, 7.0, 0.0);
                        roundTest.run(4.0, 7.0, 7.0);
                        roundTest.run(6.0, 7.0, 7.0);
                        roundTest.run(7.0, 7.0, 7.0);

                        roundTest.run(123.456, 0.01, 123.46);
                    }
                });
            }
        });
    }
}