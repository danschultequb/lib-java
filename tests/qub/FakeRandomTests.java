package qub;

public interface FakeRandomTests
{
    public static void test(final TestRunner runner)
    {
        runner.testGroup(FakeRandom.class, () ->
        {
            runner.test("create()", (Test test) ->
            {
                final FakeRandom random = FakeRandom.create();
                for (int i = 0; i < 100; i++)
                {
                    test.assertEqual(0, random.getRandomInteger());
                }
            });

            runner.testGroup("create(int)", () ->
            {
                final Action1<Integer> createTest = (Integer value) ->
                {
                    runner.test("with " + value, (Test test) ->
                    {
                        final FakeRandom random = FakeRandom.create(value);
                        for (int i = 0; i < 100; i++)
                        {
                            test.assertEqual(value, random.getRandomInteger());
                        }
                    });
                };

                createTest.run(0);
                createTest.run(1);
                createTest.run(2);
            });

            runner.testGroup("create(Function0<Integer>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> FakeRandom.create(null),
                        new PreConditionFailure("creator cannot be null."));
                });

                runner.test("with non-null", (Test test) ->
                {
                    final FakeRandom random = FakeRandom.create(() -> 20);
                    for (int i = 0; i < 100; i++)
                    {
                        test.assertEqual(20, random.getRandomInteger());
                    }
                });
            });

            runner.test("getRandomInteger()", (Test test) ->
            {
                final FakeRandom random = FakeRandom.create(7);
                for (int i = 0; i < 3; ++i)
                {
                    test.assertEqual(7, random.getRandomInteger());
                }
            });

            runner.test("getRandomIntegerBetween(int,int)", (Test test) ->
            {
                final FakeRandom r0 = FakeRandom.create(0);
                test.assertEqual(0, r0.getRandomIntegerBetween(0, 9));
                test.assertEqual(1, r0.getRandomIntegerBetween(1, 3));
                test.assertEqual(11, r0.getRandomIntegerBetween(11, 11));
                test.assertEqual(7, r0.getRandomIntegerBetween(10, 7));

                final FakeRandom r10 = FakeRandom.create(10);
                test.assertEqual(0, r0.getRandomIntegerBetween(0, 9));
                test.assertEqual(1, r0.getRandomIntegerBetween(1, 10));
                test.assertEqual(1, r0.getRandomIntegerBetween(1, 3));
                test.assertEqual(11, r0.getRandomIntegerBetween(11, 11));
                test.assertEqual(7, r0.getRandomIntegerBetween(10, 7));
            });

            runner.test("getRandomBoolean()", (Test test) ->
            {
                final FakeRandom random = FakeRandom.create(0);
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
