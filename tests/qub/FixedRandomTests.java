package qub;

public class FixedRandomTests
{
    public static void test(final TestRunner runner)
    {
        runner.testGroup("FixedRandom", () ->
        {
            runner.test("constructor()", (Test test) ->
            {
                final FixedRandom random = new FixedRandom(7);
                for (int i = 0; i < 3; ++i)
                {
                    test.assertEqual(7, random.getRandomInteger());
                }
            });

            runner.test("getRandomIntegerBetween(int,int)", (Test test) ->
            {
                final FixedRandom r0 = new FixedRandom(0);
                test.assertEqual(0, r0.getRandomIntegerBetween(0, 9));
                test.assertEqual(1, r0.getRandomIntegerBetween(1, 3));
                test.assertEqual(11, r0.getRandomIntegerBetween(11, 11));
                test.assertEqual(7, r0.getRandomIntegerBetween(10, 7));

                final FixedRandom r10 = new FixedRandom(10);
                test.assertEqual(0, r0.getRandomIntegerBetween(0, 9));
                test.assertEqual(1, r0.getRandomIntegerBetween(1, 10));
                test.assertEqual(1, r0.getRandomIntegerBetween(1, 3));
                test.assertEqual(11, r0.getRandomIntegerBetween(11, 11));
                test.assertEqual(7, r0.getRandomIntegerBetween(10, 7));
            });

            runner.test("getRandomBoolean()", (Test test) ->
            {
                final FixedRandom random = new FixedRandom(0);
                for (int i = 0; i < 10; ++i)
                {
                    test.assertTrue(random.getRandomBoolean());
                }

                random.setValue(1);
                for (int i = 0; i < 10; ++i)
                {
                    test.assertFalse(random.getRandomBoolean());
                }
            });
        });
    }
}
