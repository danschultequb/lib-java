package qub;

public interface DateTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(Date.class, () ->
        {
            runner.test("epoch", (Test test) ->
            {
                final Date epoch = Date.epoch;
                test.assertEqual(1970, epoch.getYear());
                test.assertEqual(1, epoch.getMonth());
                test.assertEqual(1, epoch.getDayOfMonth());
                test.assertEqual(Duration.zero, epoch.getDurationSinceEpoch());
            });

            runner.testGroup("create(DateTime)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> Date.create(null),
                        new PreConditionFailure("dateTime cannot be null."));
                });
            });
        });
    }
}
