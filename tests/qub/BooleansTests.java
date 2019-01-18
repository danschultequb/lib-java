package qub;

public class BooleansTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(Booleans.class, () ->
        {
            runner.test("constructor()", (Test test) ->
            {
                test.assertNotNull(new Booleans());
            });

            runner.testGroup("isTrue(Boolean)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertFalse(Booleans.isTrue(null));
                });

                runner.test("with false", (Test test) ->
                {
                    test.assertFalse(Booleans.isTrue(false));
                });

                runner.test("with true", (Test test) ->
                {
                    test.assertTrue(Booleans.isTrue(true));
                });
            });

            runner.testGroup("isFalse(Boolean)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertFalse(Booleans.isFalse(null));
                });

                runner.test("with false", (Test test) ->
                {
                    test.assertTrue(Booleans.isFalse(false));
                });

                runner.test("with true", (Test test) ->
                {
                    test.assertFalse(Booleans.isFalse(true));
                });
            });

            runner.testGroup("toString(boolean)", () ->
            {
                runner.test("with true", (Test test) ->
                {
                    test.assertEqual("true", Booleans.toString(true));
                });

                runner.test("with false", (Test test) ->
                {
                    test.assertEqual("false", Booleans.toString(false));
                });
            });

            runner.testGroup("toString(Boolean)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> Booleans.toString(null), new PreConditionFailure("value cannot be null."));
                });

                runner.test("with true", (Test test) ->
                {
                    test.assertEqual("true", Booleans.toString(Boolean.valueOf(true)));
                });

                runner.test("with false", (Test test) ->
                {
                    test.assertEqual("false", Booleans.toString(Boolean.valueOf(false)));
                });
            });
        });
    }
}
