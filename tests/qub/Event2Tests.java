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

    static void test(TestRunner runner, Function0<Event2<Integer,Boolean>> creator)
    {
        runner.testGroup(Event2.class, () ->
        {
            runner.testGroup("add(Action2<T1,T2>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final Event2<Integer,Boolean> event = creator.run();
                    test.assertThrows(() -> event.add(null),
                        new PreConditionFailure("callback cannot be null."));
                });

                runner.test("with non-null", (Test test) ->
                {
                    final Event2<Integer,Boolean> event = creator.run();
                    final Disposable disposable = event.add((Integer arg1, Boolean arg2) -> {});
                    test.assertNotNull(disposable);
                    test.assertFalse(disposable.isDisposed());
                });
            });
        });
    }
}
