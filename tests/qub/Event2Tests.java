package qub;

public interface Event2Tests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(Event2.class, () ->
        {
            runner.test("create()", (Test test) ->
            {
                final RunnableEvent2<Integer,Boolean> event = Event2.create();
                test.assertNotNull(event);
            });
        });
    }

    static void test(TestRunner runner, Function0<? extends Event2<Integer,Integer>> creator)
    {
        runner.testGroup(Event2.class, () ->
        {
            Event0Tests.test(runner, creator);

            runner.testGroup("subscribe(Action2<T1,T2>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final Event2<Integer,Integer> event = creator.run();
                    test.assertThrows(() -> event.subscribe((Action2<Integer,Integer>)null),
                        new PreConditionFailure("callback cannot be null."));
                });

                runner.test("with non-null", (Test test) ->
                {
                    final Event2<Integer,Integer> event = creator.run();
                    final Disposable disposable = event.subscribe((Integer arg1, Integer arg2) -> {});
                    test.assertNotNull(disposable);
                    test.assertFalse(disposable.isDisposed());
                });
            });
        });
    }
}
