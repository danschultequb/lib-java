package qub;

public interface JavaConcurrentHashMapTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(JavaConcurrentHashMap.class, () ->
        {
            MutableMapTests.test(runner, JavaConcurrentHashMap::create, false, false);
        });

        runner.testGroup("set(TKey,TValue)", () ->
        {
            runner.testGroup("speed tests", () ->
            {
                final Action2<Integer,Duration> noConflictsSpeedTest = (Integer targetCount, Duration expectedDuration) ->
                {
                    runner.speedTest("with " + targetCount + " assignments and no conflicts", expectedDuration, (Test test) ->
                    {
                        final MutableMap<Integer,Boolean> map = JavaConcurrentHashMap.create();

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

                noConflictsSpeedTest.run(10, Duration.seconds(0.025));
                noConflictsSpeedTest.run(100, Duration.seconds(0.025));
                noConflictsSpeedTest.run(1000, Duration.seconds(0.025));
                noConflictsSpeedTest.run(10000, Duration.seconds(0.025));
                noConflictsSpeedTest.run(100000, Duration.seconds(0.1));
                noConflictsSpeedTest.run(1000000, Duration.seconds(0.3));

                final Action2<Integer,Duration> conflictsSpeedTest = (Integer targetCount, Duration expectedDuration) ->
                {
                    runner.speedTest("with " + targetCount + " assignments with key hash code conflicts", expectedDuration, (Test test) ->
                    {
                        final MutableMap<Integer,Boolean> map = JavaConcurrentHashMap.create();

                        for (int i = 0; i < targetCount; i++)
                        {
                            final int key = (int)(i * 3.14159);
                            final MutableMap<Integer,Boolean> setResult = map.set(key, Math.isOdd(key));
                            test.assertSame(map, setResult);
                            test.assertEqual(Math.isOdd(key), map.get(key).await());
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

                conflictsSpeedTest.run(10, Duration.seconds(0.025));
                conflictsSpeedTest.run(100, Duration.seconds(0.025));
                conflictsSpeedTest.run(1000, Duration.seconds(0.025));
                conflictsSpeedTest.run(10000, Duration.seconds(0.025));
                conflictsSpeedTest.run(100000, Duration.seconds(0.1));
                conflictsSpeedTest.run(1000000, Duration.seconds(0.5));
            });
        });
    }
}
