package qub;

public interface JSONNumberTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(JSONNumber.class, () ->
        {
            runner.test("create(int)", (Test test) ->
            {
                final JSONNumber number = JSONNumber.create(513);
                test.assertEqual(513L, number.getNumberValue());
                test.assertEqual(513, number.getIntegerValue().await());
                test.assertEqual(513L, number.getLongValue().await());
                test.assertEqual(513.0, number.getDoubleValue());
                test.assertEqual("513", number.toString());
            });

            runner.test("create(long)", (Test test) ->
            {
                final JSONNumber number = JSONNumber.create(513L);
                test.assertEqual(513L, number.getNumberValue());
                test.assertEqual(513, number.getIntegerValue().await());
                test.assertEqual(513L, number.getLongValue().await());
                test.assertEqual(513.0, number.getDoubleValue());
                test.assertEqual("513", number.toString());
            });

            runner.test("create(double)", (Test test) ->
            {
                final JSONNumber number = JSONNumber.create(51.0);
                test.assertEqual(51.0, number.getNumberValue());
                test.assertThrows(() -> number.getIntegerValue().await(),
                    new NumberFormatException("For input string: \"51.0\""));
                test.assertThrows(() -> number.getLongValue().await(),
                    new NumberFormatException("For input string: \"51.0\""));
                test.assertEqual(51.0, number.getDoubleValue());
                test.assertEqual("51.0", number.toString());
            });

            runner.testGroup("create(String)", () ->
            {
                final Action2<String,Number> getTest = (String text, Number expectedValue) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(text), (Test test) ->
                    {
                        final JSONNumber number = JSONNumber.create(text);
                        test.assertEqual(text, number.toString());
                        test.assertEqual(expectedValue, number.getNumberValue());
                    });
                };

                getTest.run("9", 9L);
                getTest.run("9.0", 9.0);
                getTest.run("-79", -79L);
                getTest.run("10e0", 10.0);
                getTest.run("10e1", 100.0);
                getTest.run("10E2", 1000.0);
                getTest.run("10e-2", 0.1);
            });

            runner.testGroup("equals(Object)", () ->
            {
                final Action3<JSONNumber,Object,Boolean> equalsTest = (JSONNumber number, Object rhs, Boolean expected) ->
                {
                    runner.test("with " + number + " and " + rhs, (Test test) ->
                    {
                        test.assertEqual(expected, number.equals(rhs));
                    });
                };

                equalsTest.run(JSONNumber.create(0), null, false);
                equalsTest.run(JSONNumber.create(0), "hello", false);
                equalsTest.run(JSONNumber.create(0), JSONNumber.create(0), true);
                equalsTest.run(JSONNumber.create(0), JSONNumber.create(-0), true);
                equalsTest.run(JSONNumber.create(0), JSONNumber.create(1), false);
                equalsTest.run(JSONNumber.create(1), JSONNumber.create(1.0), false);
            });

            runner.testGroup("equals(JSONNumber)", () ->
            {
                final Action3<JSONNumber,JSONNumber,Boolean> equalsTest = (JSONNumber number, JSONNumber rhs, Boolean expected) ->
                {
                    runner.test("with " + number + " and " + rhs, (Test test) ->
                    {
                        test.assertEqual(expected, number.equals(rhs));
                    });
                };

                equalsTest.run(JSONNumber.create(0), null, false);
                equalsTest.run(JSONNumber.create(0), JSONNumber.create(0), true);
                equalsTest.run(JSONNumber.create(0), JSONNumber.create(-0), true);
                equalsTest.run(JSONNumber.create(0), JSONNumber.create(1), false);
                equalsTest.run(JSONNumber.create(1), JSONNumber.create(1.0), false);
            });
        });
    }
}
