package qub;

public class VersionNumberTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(VersionNumber.class, () ->
        {
            runner.test("constructor(int)", (Test test) ->
            {
                final VersionNumber versionNumber = new VersionNumber(17);
                test.assertEqual(17, versionNumber.getMajor());
                test.assertEqual(null, versionNumber.getMinor());
                test.assertEqual(null, versionNumber.getPatch());
                test.assertEqual(null, versionNumber.getSuffix());
                test.assertEqual("17", versionNumber.toString());
            });

            runner.test("constructor(int, String)", (Test test) ->
            {
                final VersionNumber versionNumber = new VersionNumber(17, "alpha");
                test.assertEqual(17, versionNumber.getMajor());
                test.assertEqual(null, versionNumber.getMinor());
                test.assertEqual(null, versionNumber.getPatch());
                test.assertEqual("alpha", versionNumber.getSuffix());
                test.assertEqual("17alpha", versionNumber.toString());
            });

            runner.test("constructor(int,int)", (Test test) ->
            {
                final VersionNumber versionNumber = new VersionNumber(2, 3);
                test.assertEqual(2, versionNumber.getMajor());
                test.assertEqual(3, versionNumber.getMinor());
                test.assertEqual(null, versionNumber.getPatch());
                test.assertEqual(null, versionNumber.getSuffix());
                test.assertEqual("2.3", versionNumber.toString());
            });

            runner.test("constructor(int,int,String)", (Test test) ->
            {
                final VersionNumber versionNumber = new VersionNumber(3, 4, "-beta");
                test.assertEqual(3, versionNumber.getMajor());
                test.assertEqual(4, versionNumber.getMinor());
                test.assertEqual(null, versionNumber.getPatch());
                test.assertEqual("-beta", versionNumber.getSuffix());
                test.assertEqual("3.4-beta", versionNumber.toString());
            });

            runner.test("constructor(int,int,int)", (Test test) ->
            {
                final VersionNumber versionNumber = new VersionNumber(5, 6, 7);
                test.assertEqual(5, versionNumber.getMajor());
                test.assertEqual(6, versionNumber.getMinor());
                test.assertEqual(7, versionNumber.getPatch());
                test.assertEqual(null, versionNumber.getSuffix());
                test.assertEqual("5.6.7", versionNumber.toString());
            });

            runner.test("constructor(int,int,int,String)", (Test test) ->
            {
                final VersionNumber versionNumber = new VersionNumber(8, 9, 10, "-preview");
                test.assertEqual(8, versionNumber.getMajor());
                test.assertEqual(9, versionNumber.getMinor());
                test.assertEqual(10, versionNumber.getPatch());
                test.assertEqual("-preview", versionNumber.getSuffix());
                test.assertEqual("8.9.10-preview", versionNumber.toString());
            });

            runner.testGroup("parse(String)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> VersionNumber.parse(null),
                        new PreConditionFailure("versionNumberString cannot be null."));
                });

                runner.test("with \"\"", (Test test) ->
                {
                    final VersionNumber versionNumber = VersionNumber.parse("");
                    test.assertNull(versionNumber.getMajor());
                    test.assertNull(versionNumber.getMinor());
                    test.assertNull(versionNumber.getPatch());
                    test.assertNull(versionNumber.getSuffix());
                    test.assertEqual("", versionNumber.toString());
                });

                runner.test("with \"abc\"", (Test test) ->
                {
                    final VersionNumber versionNumber = VersionNumber.parse("abc");
                    test.assertNull(versionNumber.getMajor());
                    test.assertNull(versionNumber.getMinor());
                    test.assertNull(versionNumber.getPatch());
                    test.assertEqual("abc", versionNumber.getSuffix());
                    test.assertEqual("abc", versionNumber.toString());
                });

                runner.test("with \"1\"", (Test test) ->
                {
                    final VersionNumber versionNumber = VersionNumber.parse("1");
                    test.assertNotNull(versionNumber);
                    test.assertEqual(1, versionNumber.getMajor());
                    test.assertEqual(null, versionNumber.getMinor());
                    test.assertEqual(null, versionNumber.getPatch());
                    test.assertNull(versionNumber.getSuffix());
                    test.assertEqual("1", versionNumber.toString());
                });

                runner.test("with \"2.\"", (Test test) ->
                {
                    final VersionNumber versionNumber = VersionNumber.parse("2.");
                    test.assertNotNull(versionNumber);
                    test.assertEqual(2, versionNumber.getMajor());
                    test.assertEqual(null, versionNumber.getMinor());
                    test.assertEqual(null, versionNumber.getPatch());
                    test.assertNull(versionNumber.getSuffix());
                    test.assertEqual("2.", versionNumber.toString());
                });

                runner.test("with \"2a\"", (Test test) ->
                {
                    final VersionNumber versionNumber = VersionNumber.parse("2a");
                    test.assertNotNull(versionNumber);
                    test.assertEqual(2, versionNumber.getMajor());
                    test.assertEqual(null, versionNumber.getMinor());
                    test.assertEqual(null, versionNumber.getPatch());
                    test.assertEqual("a", versionNumber.getSuffix());
                    test.assertEqual("2a", versionNumber.toString());
                });

                runner.test("with \"2.a\"", (Test test) ->
                {
                    final VersionNumber versionNumber = VersionNumber.parse("2.a");
                    test.assertNotNull(versionNumber);
                    test.assertEqual(2, versionNumber.getMajor());
                    test.assertEqual(null, versionNumber.getMinor());
                    test.assertEqual(null, versionNumber.getPatch());
                    test.assertEqual("a", versionNumber.getSuffix());
                    test.assertEqual("2.a", versionNumber.toString());
                });

                runner.test("with \"3.4\"", (Test test) ->
                {
                    final VersionNumber versionNumber = VersionNumber.parse("3.4");
                    test.assertNotNull(versionNumber);
                    test.assertEqual(3, versionNumber.getMajor());
                    test.assertEqual(4, versionNumber.getMinor());
                    test.assertEqual(null, versionNumber.getPatch());
                    test.assertNull(versionNumber.getSuffix());
                    test.assertEqual("3.4", versionNumber.toString());
                });

                runner.test("with \"5.6.\"", (Test test) ->
                {
                    final VersionNumber versionNumber = VersionNumber.parse("5.6.");
                    test.assertNotNull(versionNumber);
                    test.assertEqual(5, versionNumber.getMajor());
                    test.assertEqual(6, versionNumber.getMinor());
                    test.assertEqual(null, versionNumber.getPatch());
                    test.assertNull(versionNumber.getSuffix());
                    test.assertEqual("5.6.", versionNumber.toString());
                });

                runner.test("with \"5.6_\"", (Test test) ->
                {
                    final VersionNumber versionNumber = VersionNumber.parse("5.6_");
                    test.assertNotNull(versionNumber);
                    test.assertEqual(5, versionNumber.getMajor());
                    test.assertEqual(6, versionNumber.getMinor());
                    test.assertEqual(null, versionNumber.getPatch());
                    test.assertEqual("_", versionNumber.getSuffix());
                    test.assertEqual("5.6_", versionNumber.toString());
                });

                runner.test("with \"5.6.*\"", (Test test) ->
                {
                    final VersionNumber versionNumber = VersionNumber.parse("5.6.*");
                    test.assertNotNull(versionNumber);
                    test.assertEqual(5, versionNumber.getMajor());
                    test.assertEqual(6, versionNumber.getMinor());
                    test.assertEqual(null, versionNumber.getPatch());
                    test.assertEqual("*", versionNumber.getSuffix());
                    test.assertEqual("5.6.*", versionNumber.toString());
                });

                runner.test("with \"5.6.7\"", (Test test) ->
                {
                    final VersionNumber versionNumber = VersionNumber.parse("5.6.7");
                    test.assertNotNull(versionNumber);
                    test.assertEqual(5, versionNumber.getMajor());
                    test.assertEqual(6, versionNumber.getMinor());
                    test.assertEqual(7, versionNumber.getPatch());
                    test.assertNull(versionNumber.getSuffix());
                    test.assertEqual("5.6.7", versionNumber.toString());
                });
            });

            runner.testGroup("compareTo(VersionNumber)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final VersionNumber versionNumber = VersionNumber.parse("1.2.3");
                    test.assertEqual(Comparison.GreaterThan, versionNumber.compareTo(null));
                });

                runner.test("with same", (Test test) ->
                {
                    final VersionNumber versionNumber = VersionNumber.parse("1.2.3");
                    test.assertEqual(Comparison.Equal, versionNumber.compareTo(versionNumber));
                });

                runner.test("with equal", (Test test) ->
                {
                    final VersionNumber versionNumber = VersionNumber.parse("1.2.3");
                    test.assertEqual(Comparison.Equal, versionNumber.compareTo(VersionNumber.parse("1.2.3")));
                });

                runner.test("with lower major version", (Test test) ->
                {
                    final VersionNumber versionNumber = VersionNumber.parse("1.2.3");
                    test.assertEqual(Comparison.GreaterThan, versionNumber.compareTo(VersionNumber.parse("0.2.3")));
                });

                runner.test("with higher major version", (Test test) ->
                {
                    final VersionNumber versionNumber = VersionNumber.parse("1.2.3");
                    test.assertEqual(Comparison.LessThan, versionNumber.compareTo(VersionNumber.parse("4.2.3")));
                });

                runner.test("with lower minor version", (Test test) ->
                {
                    final VersionNumber versionNumber = VersionNumber.parse("1.2.3");
                    test.assertEqual(Comparison.GreaterThan, versionNumber.compareTo(VersionNumber.parse("1.0.3")));
                });

                runner.test("with higher minor version", (Test test) ->
                {
                    final VersionNumber versionNumber = VersionNumber.parse("1.2.3");
                    test.assertEqual(Comparison.LessThan, versionNumber.compareTo(VersionNumber.parse("1.5.3")));
                });

                runner.test("with lower patch version", (Test test) ->
                {
                    final VersionNumber versionNumber = VersionNumber.parse("1.2.3");
                    test.assertEqual(Comparison.GreaterThan, versionNumber.compareTo(VersionNumber.parse("1.2.0")));
                });

                runner.test("with higher patch version", (Test test) ->
                {
                    final VersionNumber versionNumber = VersionNumber.parse("1.2.3");
                    test.assertEqual(Comparison.LessThan, versionNumber.compareTo(VersionNumber.parse("1.2.10")));
                });

                runner.test("with lower suffix", (Test test) ->
                {
                    final VersionNumber versionNumber = VersionNumber.parse("1.2.3-beta");
                    test.assertEqual(Comparison.GreaterThan, versionNumber.compareTo(VersionNumber.parse("1.2.0-alpha")));
                });

                runner.test("with higher suffix", (Test test) ->
                {
                    final VersionNumber versionNumber = VersionNumber.parse("1.2.3-beta");
                    test.assertEqual(Comparison.LessThan, versionNumber.compareTo(VersionNumber.parse("1.2.10-preview")));
                });
            });

            runner.testGroup("equals(Object)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final VersionNumber versionNumber = VersionNumber.parse("10.4.8");
                    test.assertFalse(versionNumber.equals((Object)null));
                });

                runner.test("with String", (Test test) ->
                {
                    final VersionNumber versionNumber = VersionNumber.parse("10.4.8");
                    test.assertFalse(versionNumber.equals((Object)"10.4.8"));
                });

                runner.test("with same", (Test test) ->
                {
                    final VersionNumber versionNumber = VersionNumber.parse("10.4.8");
                    test.assertTrue(versionNumber.equals((Object)versionNumber));
                });

                runner.test("with equal", (Test test) ->
                {
                    final VersionNumber versionNumber = VersionNumber.parse("10.4.8");
                    test.assertTrue(versionNumber.equals((Object)VersionNumber.parse(versionNumber.toString())));
                });

                runner.test("with different", (Test test) ->
                {
                    final VersionNumber versionNumber = VersionNumber.parse("10.4.8");
                    test.assertFalse(versionNumber.equals((Object)VersionNumber.parse("1.2.3")));
                });
            });

            runner.testGroup("equals(VersionNumber)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final VersionNumber versionNumber = VersionNumber.parse("10.4.8");
                    test.assertFalse(versionNumber.equals((VersionNumber)null));
                });

                runner.test("with same", (Test test) ->
                {
                    final VersionNumber versionNumber = VersionNumber.parse("10.4.8");
                    test.assertTrue(versionNumber.equals(versionNumber));
                });

                runner.test("with equal", (Test test) ->
                {
                    final VersionNumber versionNumber = VersionNumber.parse("10.4.8");
                    test.assertTrue(versionNumber.equals(VersionNumber.parse(versionNumber.toString())));
                });

                runner.test("with different", (Test test) ->
                {
                    final VersionNumber versionNumber = VersionNumber.parse("10.4.8");
                    test.assertFalse(versionNumber.equals(VersionNumber.parse("1.2.3")));
                });
            });

            runner.testGroup("hashCode()", () ->
            {
                runner.test("with same", (Test test) ->
                {
                    final VersionNumber versionNumber = VersionNumber.parse("1.2.3");
                    test.assertEqual(versionNumber.hashCode(), versionNumber.hashCode());
                });

                runner.test("with equal", (Test test) ->
                {
                    final VersionNumber versionNumber = VersionNumber.parse("1.2.3");
                    test.assertEqual(versionNumber.hashCode(), VersionNumber.parse(versionNumber.toString()).hashCode());
                });
            });
            runner.test("hashCode()", (Test test) ->
            {
                final VersionNumber versionNumber = VersionNumber.parse("1.2.3");
                test.assertEqual(versionNumber.hashCode(), versionNumber.hashCode());
            });
        });
    }
}
