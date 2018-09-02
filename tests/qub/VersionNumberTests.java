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
                    test.assertThrows(() -> VersionNumber.parse(null));
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
        });
    }
}
