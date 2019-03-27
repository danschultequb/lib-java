package qub;

public class VersionRangeTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(VersionRange.class, () ->
        {
            runner.testGroup("parse(String,Action1<Issue>)", () ->
            {
                runner.test("with null text", (Test test) ->
                {
                    test.assertThrows(() -> VersionRange.parse(null), new PreConditionFailure("text cannot be null."));
                });

                runner.test("with empty text", (Test test) ->
                {
                    test.assertThrows(() -> VersionRange.parse(""), new PreConditionFailure("text cannot be empty."));
                });

                runner.test("with \"*\"", (Test test) ->
                {
                    final VersionRange range = VersionRange.parse("*");
                    test.assertEqual("*", range.toString());
                    test.assertTrue(range.matches("1"));
                    test.assertTrue(range.matches("2"));
                });

                runner.test("with \"1\"", (Test test) ->
                {
                    final VersionRange range = VersionRange.parse("1");
                    test.assertEqual("1", range.toString());
                    test.assertTrue(range.matches("1"));
                    test.assertFalse(range.matches("2"));
                });

                runner.test("with \"1.2\"", (Test test) ->
                {
                    final VersionRange range = VersionRange.parse("1.2");
                    test.assertEqual("1.2", range.toString());
                    test.assertTrue(range.matches("1.2"));
                    test.assertFalse(range.matches("1.2.1"));
                });

                runner.test("with \"1.2.3\"", (Test test) ->
                {
                    final VersionRange range = VersionRange.parse("1.2.3");
                    test.assertEqual("1.2.3", range.toString());
                    test.assertTrue(range.matches("1.2.3"));
                    test.assertFalse(range.matches("1.2.4"));
                });

                runner.test("with \"^0.2.3\"", (Test test) ->
                {
                    final VersionRange range = VersionRange.parse("^0.2.3");
                    test.assertEqual("^0.2.3", range.toString());
                    test.assertTrue(range.matches("0.2.3"));
                    test.assertTrue(range.matches("0.2.4"));
                    test.assertTrue(range.matches("0.2.29"));
                    test.assertTrue(range.matches("0.3.0"));
                    test.assertFalse(range.matches("1.0.0"));
                    test.assertFalse(range.matches("0.2.2"));
                });

                runner.test("with \"~3.4.5\"", (Test test) ->
                {
                    final VersionRange range = VersionRange.parse("~3.4.5");
                    test.assertEqual("~3.4.5", range.toString());
                    test.assertTrue(range.matches("3.4.5"));
                    test.assertTrue(range.matches("3.4.6"));
                    test.assertTrue(range.matches("3.4.10"));
                    test.assertFalse(range.matches("3.5.0"));
                    test.assertFalse(range.matches("3.4.4"));
                    test.assertFalse(range.matches("2.0.0"));
                    test.assertFalse(range.matches("4.0.0"));
                });
            });

            runner.testGroup("equals(Object)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final VersionRange range = VersionRange.parse("^5.7.0");
                    test.assertFalse(range.equals((Object)null));
                });

                runner.test("with String", (Test test) ->
                {
                    final VersionRange range = VersionRange.parse("^5.7.0");
                    test.assertFalse(range.equals((Object)"^5.7.0"));
                });

                runner.test("with same", (Test test) ->
                {
                    final VersionRange range = VersionRange.parse("^5.7.0");
                    test.assertTrue(range.equals((Object)range));
                });

                runner.test("with equal", (Test test) ->
                {
                    final VersionRange range = VersionRange.parse("^5.7.0");
                    test.assertTrue(range.equals((Object)VersionRange.parse(range.toString())));
                });

                runner.test("with different", (Test test) ->
                {
                    final VersionRange range = VersionRange.parse("^5.7.0");
                    test.assertFalse(range.equals((Object)VersionRange.parse("5.7.0")));
                });
            });

            runner.testGroup("equals(VersionRange)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final VersionRange range = VersionRange.parse("^5.7.0");
                    test.assertFalse(range.equals(null));
                });

                runner.test("with same", (Test test) ->
                {
                    final VersionRange range = VersionRange.parse("^5.7.0");
                    test.assertTrue(range.equals(range));
                });

                runner.test("with equal", (Test test) ->
                {
                    final VersionRange range = VersionRange.parse("^5.7.0");
                    test.assertTrue(range.equals(VersionRange.parse(range.toString())));
                });

                runner.test("with different", (Test test) ->
                {
                    final VersionRange range = VersionRange.parse("^5.7.0");
                    test.assertFalse(range.equals(VersionRange.parse("5.7.0")));
                });
            });

            runner.test("hashCode()", (Test test) ->
            {
                final VersionRange range = VersionRange.parse("1.2.3");
                test.assertEqual(range.hashCode(), range.hashCode());
            });
        });
    }
}
