package qub;

public class Event0Tests
{
    public static void test(TestRunner runner, Function0<Event0> creator)
    {
        runner.testGroup(Event0.class, () ->
        {
            runner.testGroup("subscribe(Action)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final Event0 event = creator.run();

                    test.assertThrows(() -> event.subscribe(null));
                });

                runner.test("with non-null", (Test test) ->
                {
                    final Event0 event = creator.run();
                    final Value<Integer> value = new Value<>(0);

                    event.subscribe(() -> value.set(value.get() + 1));

                    test.assertEqual(0, value.get());
                });
            });
        });
    }
}