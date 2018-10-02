package qub;

public class SyncEvent0Tests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(SyncEvent0.class, () ->
        {
            Event0Tests.test(runner, SyncEvent0::new);

            runner.testGroup("run()", () ->
            {
                runner.test("with no actions", (Test test) ->
                {
                    final SyncEvent0 event = new SyncEvent0();
                    event.run();
                });

                runner.test("with one action", (Test test) ->
                {
                    final SyncEvent0 event = new SyncEvent0();
                    final Value<Integer> value = new Value<>(0);
                    event.subscribe(() -> value.set(value.get() + 1));

                    event.run();
                    test.assertEqual(1, value.get());

                    event.run();
                    test.assertEqual(2, value.get());
                });

                runner.test("with two actions", (Test test) ->
                {
                    final SyncEvent0 event = new SyncEvent0();
                    final Value<Integer> value1 = new Value<>(0);
                    event.subscribe(() -> value1.set(value1.get() + 1));
                    final Value<Integer> value2 = new Value<>(10);
                    event.subscribe(() -> value2.set(value2.get() + 1));

                    event.run();
                    test.assertEqual(1, value1.get());
                    test.assertEqual(11, value2.get());

                    event.run();
                    test.assertEqual(2, value1.get());
                    test.assertEqual(12, value2.get());
                });
            });
        });
    }
}
