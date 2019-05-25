package qub;

public interface MathTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(Math.class, () ->
        {
            runner.test("minimum()", (Test test) ->
            {
                test.assertEqual(-10, Math.minimum(-10, -5));
                test.assertEqual(-10, Math.minimum(-5, -10));

                test.assertEqual(-3, Math.minimum(-3, -3));

                test.assertEqual(-1, Math.minimum(-1, 0));
                test.assertEqual(-1, Math.minimum(0, -1));
            });
            
            runner.test("maximum()", (Test test) ->
            {
                test.assertEqual(-5, Math.maximum(-10, -5));
                test.assertEqual(-5, Math.maximum(-5, -10));

                test.assertEqual(-3, Math.maximum(-3, -3));

                test.assertEqual(0, Math.maximum(-1, 0));
                test.assertEqual(0, Math.maximum(0, -1));
            });

            runner.testGroup("clip(int,int,int)", () ->
            {
                final Action4<Integer,Integer,Integer,Integer> clipTest = (Integer lowerBound, Integer value, Integer upperBound, Integer expected) ->
                {
                    runner.test("with " + lowerBound + ", " + value + ", and " + upperBound, (Test test) ->
                    {
                        test.assertEqual(expected, Math.clip(lowerBound, value, upperBound));
                    });
                };

                clipTest.run(0, -1, 2, 0);
                clipTest.run(0, 0, 2, 0);
                clipTest.run(0, 1, 2, 1);
                clipTest.run(0, 2, 2, 2);
                clipTest.run(0, 3, 2, 2);
            });
            
            runner.test("isOdd()", (Test test) ->
            {
                test.assertTrue(Math.isOdd(1));
                test.assertTrue(Math.isOdd(3));
                test.assertTrue(Math.isOdd(-1));

                test.assertFalse(Math.isOdd(0));
                test.assertFalse(Math.isOdd(2));
                test.assertFalse(Math.isOdd(-2));
            });

            runner.test("isEven()", (Test test) ->
            {
                test.assertFalse(Math.isEven(1));
                test.assertFalse(Math.isEven(3));
                test.assertFalse(Math.isEven(-1));

                test.assertTrue(Math.isEven(0));
                test.assertTrue(Math.isEven(2));
                test.assertTrue(Math.isEven(-2));
            });

            runner.test("ceiling()", (Test test) ->
            {
                test.assertEqual(0.0, Math.ceiling(0));
                test.assertEqual(0.0, Math.ceiling(-0));

                test.assertEqual(1.0, Math.ceiling(0.1));
                test.assertEqual(-0.0, Math.ceiling(-0.1));

                test.assertEqual(10.0, Math.ceiling(9.9));
                test.assertEqual(-9.0, Math.ceiling(-9.9));
            });

            runner.test("floor()", (Test test) ->
            {
                test.assertEqual(0.0, Math.floor(0));
                test.assertEqual(0.0, Math.floor(-0));

                test.assertEqual(0.0, Math.floor(0.1));
                test.assertEqual(-1.0, Math.floor(-0.1));

                test.assertEqual(9.0, Math.floor(9.9));
                test.assertEqual(-10.0, Math.floor(-9.9));
            });

            runner.testGroup("round(double)", () ->
            {
                final Action2<Double,Double> roundTest = (Double value, Double expectedRoundedValue) ->
                {
                    runner.test("with " + value, (Test test) ->
                    {
                        test.assertEqual(expectedRoundedValue, Math.round(value));
                    });
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
            });

            runner.testGroup("round(double,double)", () ->
            {
                final Action3<Double,Double,Double> roundTest = (Double value, Double scale, Double expected) ->
                {
                    runner.test("with " + value + " to nearest " + scale, (Test test) ->
                    {
                        test.assertEqual(expected, Math.round(value, scale), 0.000000000001);
                    });
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
            });

            runner.testGroup("modulo(int,int)", () ->
            {
                final Action3<Integer,Integer,Integer> moduloTest = (Integer value, Integer scale, Integer expected) ->
                {
                    runner.test("with " + value + " and " + scale, (Test test) ->
                    {
                        test.assertEqual(expected, Math.modulo(value, scale));
                    });
                };

                moduloTest.run(-4, -3, -1);
                moduloTest.run(-3, -3, 0);
                moduloTest.run(-2, -3, -2);
                moduloTest.run(-1, -3, -1);
                moduloTest.run(0, -3, 0);
                moduloTest.run(1, -3, -2);
                moduloTest.run(2, -3, -1);
                moduloTest.run(3, -3, 0);
                moduloTest.run(4, -3, -2);

                moduloTest.run(-4, -2, 0);
                moduloTest.run(-3, -2, -1);
                moduloTest.run(-2, -2, 0);
                moduloTest.run(-1, -2, -1);
                moduloTest.run(0, -2, 0);
                moduloTest.run(1, -2, -1);
                moduloTest.run(2, -2, 0);
                moduloTest.run(3, -2, -1);
                moduloTest.run(4, -2, 0);

                moduloTest.run(-4, -1, 0);
                moduloTest.run(-3, -1, 0);
                moduloTest.run(-2, -1, 0);
                moduloTest.run(-1, -1, 0);
                moduloTest.run(0, -1, 0);
                moduloTest.run(1, -1, 0);
                moduloTest.run(2, -1, 0);
                moduloTest.run(3, -1, 0);
                moduloTest.run(4, -1, 0);

                moduloTest.run(-2, 1, 0);
                moduloTest.run(-1, 1, 0);
                moduloTest.run(0, 1, 0);
                moduloTest.run(1, 1, 0);
                moduloTest.run(2, 1, 0);

                moduloTest.run(-4, 2, 0);
                moduloTest.run(-3, 2, 1);
                moduloTest.run(-2, 2, 0);
                moduloTest.run(-1, 2, 1);
                moduloTest.run(0, 2, 0);
                moduloTest.run(1, 2, 1);
                moduloTest.run(2, 2, 0);
                moduloTest.run(3, 2, 1);
                moduloTest.run(4, 2, 0);

                moduloTest.run(-4, 3, 2);
                moduloTest.run(-3, 3, 0);
                moduloTest.run(-2, 3, 1);
                moduloTest.run(-1, 3, 2);
                moduloTest.run(0, 3, 0);
                moduloTest.run(1, 3, 1);
                moduloTest.run(2, 3, 2);
                moduloTest.run(3, 3, 0);
                moduloTest.run(4, 3, 1);
            });

            runner.testGroup("modulo(long,long)", () ->
            {
                runner.test("with 1 and 0", (Test test) ->
                {
                    test.assertThrows(() -> Math.modulo(1L, 0L), new PreConditionFailure("scale (0) must not be 0."));
                });

                final Action3<Long,Long,Long> moduloTest = (Long value, Long scale, Long expected) ->
                {
                    runner.test("with " + value + " and " + scale, (Test test) ->
                    {
                        test.assertEqual(expected, Math.modulo(value, scale));
                    });
                };

                moduloTest.run(-4L, -3L, -1L);
                moduloTest.run(-3L, -3L, 0L);
                moduloTest.run(-2L, -3L, -2L);
                moduloTest.run(-1L, -3L, -1L);
                moduloTest.run(0L, -3L, 0L);
                moduloTest.run(1L, -3L, -2L);
                moduloTest.run(2L, -3L, -1L);
                moduloTest.run(3L, -3L, 0L);
                moduloTest.run(4L, -3L, -2L);

                moduloTest.run(-4L, -2L, 0L);
                moduloTest.run(-3L, -2L, -1L);
                moduloTest.run(-2L, -2L, 0L);
                moduloTest.run(-1L, -2L, -1L);
                moduloTest.run(0L, -2L, 0L);
                moduloTest.run(1L, -2L, -1L);
                moduloTest.run(2L, -2L, 0L);
                moduloTest.run(3L, -2L, -1L);
                moduloTest.run(4L, -2L, 0L);

                moduloTest.run(-4L, -1L, 0L);
                moduloTest.run(-3L, -1L, 0L);
                moduloTest.run(-2L, -1L, 0L);
                moduloTest.run(-1L, -1L, 0L);
                moduloTest.run(0L, -1L, 0L);
                moduloTest.run(1L, -1L, 0L);
                moduloTest.run(2L, -1L, 0L);
                moduloTest.run(3L, -1L, 0L);
                moduloTest.run(4L, -1L, 0L);

                moduloTest.run(-4L, 1L, 0L);
                moduloTest.run(-3L, 1L, 0L);
                moduloTest.run(-2L, 1L, 0L);
                moduloTest.run(-1L, 1L, 0L);
                moduloTest.run(0L, 1L, 0L);
                moduloTest.run(1L, 1L, 0L);
                moduloTest.run(2L, 1L, 0L);
                moduloTest.run(3L, 1L, 0L);
                moduloTest.run(4L, 1L, 0L);

                moduloTest.run(-4L, 2L, 0L);
                moduloTest.run(-3L, 2L, 1L);
                moduloTest.run(-2L, 2L, 0L);
                moduloTest.run(-1L, 2L, 1L);
                moduloTest.run(0L, 2L, 0L);
                moduloTest.run(1L, 2L, 1L);
                moduloTest.run(2L, 2L, 0L);
                moduloTest.run(3L, 2L, 1L);
                moduloTest.run(4L, 2L, 0L);

                moduloTest.run(-4L, 3L, 2L);
                moduloTest.run(-3L, 3L, 0L);
                moduloTest.run(-2L, 3L, 1L);
                moduloTest.run(-1L, 3L, 2L);
                moduloTest.run(0L, 3L, 0L);
                moduloTest.run(1L, 3L, 1L);
                moduloTest.run(2L, 3L, 2L);
                moduloTest.run(3L, 3L, 0L);
                moduloTest.run(4L, 3L, 1L);
            });

            runner.testGroup("summation(int)", () ->
            {
                final Action2<Integer,Integer> summationTest = (Integer upperBound, Integer expectedValue) ->
                {
                    runner.test("with " + upperBound, (Test test) ->
                    {
                        test.assertEqual(expectedValue, Math.summation(upperBound));
                    });
                };

                summationTest.run(0, 0);
                summationTest.run(1, 1);
                summationTest.run(2, 3);
                summationTest.run(3, 6);
                summationTest.run(4, 10);
                summationTest.run(5, 15);
                summationTest.run(10, 55);
                summationTest.run(100, 5050);
                summationTest.run(1000, 500500);
                summationTest.run(10000, 50005000);
                summationTest.run(25000, 312512500);
                summationTest.run(37500, 703143750);
                summationTest.run(43750, 957053125);
                summationTest.run(46339, 1073674630);
                summationTest.run(46340, 1073720970);

                final Action1<Integer> summationTestFailures = (Integer upperBound) ->
                {
                    runner.test("with " + upperBound, (Test test) ->
                    {
                        test.assertThrows(new PreConditionFailure("upperBound (" + upperBound + ") must be between 0 and 46340."),
                            () -> Math.summation(upperBound));
                    });
                };

                summationTestFailures.run(Integers.minimum);
                summationTestFailures.run(Integers.minimum + 1);
                summationTestFailures.run(-2);
                summationTestFailures.run(-1);
                summationTestFailures.run(46341);
                summationTestFailures.run(46342);
                summationTestFailures.run(Integers.maximum - 1);
                summationTestFailures.run(Integers.maximum);
            });

            runner.testGroup("summation(long)", () ->
            {
                final Action2<Long,Long> summationTest = (Long upperBound, Long expectedValue) ->
                {
                    runner.test("with " + upperBound, (Test test) ->
                    {
                        test.assertEqual(expectedValue, Math.summation(upperBound));
                    });
                };

                summationTest.run(0L, 0L);
                summationTest.run(1L, 1L);
                summationTest.run(2L, 3L);
                summationTest.run(3L, 6L);
                summationTest.run(4L, 10L);
                summationTest.run(5L, 15L);
                summationTest.run(10L, 55L);
                summationTest.run(100L, 5050L);
                summationTest.run(1000L, 500500L);
                summationTest.run(10000L, 50005000L);
                summationTest.run(25000L, 312512500L);
                summationTest.run(37500L, 703143750L);
                summationTest.run(43750L, 957053125L);
                summationTest.run(46339L, 1073674630L);
                summationTest.run(46340L, 1073720970L);
                summationTest.run(100000L, 5000050000L);
                summationTest.run(1000000L, 500000500000L);
                summationTest.run(10000000L, 50000005000000L);
                summationTest.run(100000000L, 5000000050000000L);
                summationTest.run(1000000000L, 500000000500000000L);
                summationTest.run(2500000000L, 3125000001250000000L);
                summationTest.run(2812500000L, 3955078126406250000L);
                summationTest.run(3037000498L, 4611686013944624251L);
                summationTest.run(3037000499L, 4611686016981624750L);

                final Action1<Long> summationTestFailures = (Long upperBound) ->
                {
                    runner.test("with " + upperBound, (Test test) ->
                    {
                        test.assertThrows(new PreConditionFailure("upperBound (" + upperBound + ") must be between 0 and 3037000499."),
                            () -> Math.summation(upperBound));
                    });
                };

                summationTestFailures.run(Longs.minimum);
                summationTestFailures.run(Longs.minimum + 1);
                summationTestFailures.run(-2L);
                summationTestFailures.run(-1L);
                summationTestFailures.run(3037000500L);
                summationTestFailures.run(3037000501L);
                summationTestFailures.run(Longs.maximum - 1);
                summationTestFailures.run(Longs.maximum);
            });
        });
    }
}
