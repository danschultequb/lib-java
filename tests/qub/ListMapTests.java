package qub;

public interface ListMapTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(ListMap.class, () ->
        {
            MutableMapTests.test(runner, ListMap::create, true, true);

            runner.testGroup("set(TKey,TValue)", () ->
            {
                runner.testGroup("speed tests", () ->
                {
                    final Action2<Integer,Duration> speedTest = (Integer targetCount, Duration expectedDuration) ->
                    {
                        runner.speedTest("with " + targetCount + " assignments", expectedDuration, (Test test) ->
                        {
                            final MutableMap<Integer,Boolean> map = ListMap.create();

                            for (int i = 0; i < targetCount; i++)
                            {
                                final MutableMap<Integer,Boolean> setResult = map.set(i, Math.isOdd(i));
                                test.assertSame(map, setResult);
                                test.assertEqual(Math.isOdd(i), map.get(i).await());
                            }

                            test.assertEqual(targetCount, map.getCount());

                            int entriesReturned = 0;
                            for (final MapEntry<Integer,Boolean> entry : map)
                            {
                                test.assertNotNull(entry);
                                final Integer key = entry.getKey();
                                test.assertEqual(Math.isOdd(key), entry.getValue());
                                entriesReturned++;
                            }
                            test.assertEqual(targetCount, entriesReturned);
                        });
                    };

                    speedTest.run(10, Duration.seconds(0.025));
                    speedTest.run(100, Duration.seconds(0.025));
                    speedTest.run(1000, Duration.seconds(0.04));
                    speedTest.run(10000, Duration.seconds(3));
                    // speedTest.run(1000000, Duration.seconds(a really long, long time));
                });
            });
        });
    }
}
