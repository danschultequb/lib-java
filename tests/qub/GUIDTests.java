package qub;

public class GUIDTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(GUID.class, () ->
        {
            final GUID guid = GUID.createRandom();

            runner.testGroup("equals(Object)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertFalse(guid.equals((Object)null));
                });

                runner.test("with \"hello\"", (Test test) ->
                {
                    test.assertFalse(guid.equals((Object)"hello"));
                });

                runner.test("with same", (Test test) ->
                {
                    test.assertTrue(guid.equals((Object)guid));
                });
            });

            runner.testGroup("equals(GUID)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertFalse(guid.equals((GUID)null));
                });

                runner.test("with same", (Test test) ->
                {
                    test.assertTrue(guid.equals(guid));
                });
            });

            runner.test("toString()", (Test test) ->
            {
                final String guidString = "123e4567-e89b-12d3-a456-426655440000";
                test.assertEqual(guidString, GUID.parseString(guidString).toString());
            });

            runner.test("hashCode()", (Test test) ->
            {
                final GUID hashCodeGuid = GUID.createRandom();
                test.assertEqual(hashCodeGuid.hashCode(), hashCodeGuid.hashCode());
            });

            runner.test("createRandom()", (Test test) ->
            {
                test.assertNotEqual(GUID.createRandom(), GUID.createRandom());
            });
        });
    }
}
