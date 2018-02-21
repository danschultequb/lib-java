package qub;

public class VersionNumberTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(VersionNumber.class, () ->
        {
            runner.testGroup("constructor()", () ->
            {
                runner.test("with major", (Test test) ->
                {
                    final VersionNumber versionNumber = new VersionNumber(17, null, null);
                    test.assertEqual(17, versionNumber.getMajor());
                    test.assertEqual(null, versionNumber.getMinor());
                    test.assertEqual(null, versionNumber.getPatch());
                    test.assertEqual("17", versionNumber.toString());
                });

                runner.test("with major and minor", (Test test) ->
                {
                    final VersionNumber versionNumber = new VersionNumber(3, 9, null);
                    test.assertEqual(3, versionNumber.getMajor());
                    test.assertEqual(9, versionNumber.getMinor());
                    test.assertEqual(null, versionNumber.getPatch());
                    test.assertEqual("3.9", versionNumber.toString());
                });

                runner.test("with major, minor, and patch", (Test test) ->
                {
                    final VersionNumber versionNumber = new VersionNumber(1, 2, 3);
                    test.assertEqual(1, versionNumber.getMajor());
                    test.assertEqual(2, versionNumber.getMinor());
                    test.assertEqual(3, versionNumber.getPatch());
                    test.assertEqual("1.2.3", versionNumber.toString());
                });

                runner.test("with major and patch", (Test test) ->
                {
                    final VersionNumber versionNumber = new VersionNumber(2, null, 4);
                    test.assertEqual(2, versionNumber.getMajor());
                    test.assertEqual(null, versionNumber.getMinor());
                    test.assertEqual(4, versionNumber.getPatch());
                    test.assertEqual("2", versionNumber.toString());
                });
            });

            runner.testGroup("parse(String)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertNull(VersionNumber.parse(null));
                });

                runner.test("with \"\"", (Test test) ->
                {
                    test.assertNull(VersionNumber.parse(""));
                });

                runner.test("with \"abc\"", (Test test) ->
                {
                    test.assertNull(VersionNumber.parse("abc"));
                });

                runner.test("with \"1\"", (Test test) ->
                {
                    final VersionNumber versionNumber = VersionNumber.parse("1");
                    test.assertNotNull(versionNumber);
                    test.assertEqual(1, versionNumber.getMajor());
                    test.assertEqual(null, versionNumber.getMinor());
                    test.assertEqual(null, versionNumber.getPatch());
                    test.assertEqual("1", versionNumber.toString());
                });

                runner.test("with \"2.\"", (Test test) ->
                {
                    final VersionNumber versionNumber = VersionNumber.parse("2.");
                    test.assertNotNull(versionNumber);
                    test.assertEqual(2, versionNumber.getMajor());
                    test.assertEqual(null, versionNumber.getMinor());
                    test.assertEqual(null, versionNumber.getPatch());
                    test.assertEqual("2", versionNumber.toString());
                });

                runner.test("with \"2a\"", (Test test) ->
                {
                    final VersionNumber versionNumber = VersionNumber.parse("2a");
                    test.assertNotNull(versionNumber);
                    test.assertEqual(2, versionNumber.getMajor());
                    test.assertEqual(null, versionNumber.getMinor());
                    test.assertEqual(null, versionNumber.getPatch());
                    test.assertEqual("2", versionNumber.toString());
                });

                runner.test("with \"2.a\"", (Test test) ->
                {
                    final VersionNumber versionNumber = VersionNumber.parse("2.a");
                    test.assertNotNull(versionNumber);
                    test.assertEqual(2, versionNumber.getMajor());
                    test.assertEqual(null, versionNumber.getMinor());
                    test.assertEqual(null, versionNumber.getPatch());
                    test.assertEqual("2", versionNumber.toString());
                });

                runner.test("with \"3.4\"", (Test test) ->
                {
                    final VersionNumber versionNumber = VersionNumber.parse("3.4");
                    test.assertNotNull(versionNumber);
                    test.assertEqual(3, versionNumber.getMajor());
                    test.assertEqual(4, versionNumber.getMinor());
                    test.assertEqual(null, versionNumber.getPatch());
                    test.assertEqual("3.4", versionNumber.toString());
                });

                runner.test("with \"5.6.\"", (Test test) ->
                {
                    final VersionNumber versionNumber = VersionNumber.parse("5.6.");
                    test.assertNotNull(versionNumber);
                    test.assertEqual(5, versionNumber.getMajor());
                    test.assertEqual(6, versionNumber.getMinor());
                    test.assertEqual(null, versionNumber.getPatch());
                    test.assertEqual("5.6", versionNumber.toString());
                });

                runner.test("with \"5.6_\"", (Test test) ->
                {
                    final VersionNumber versionNumber = VersionNumber.parse("5.6_");
                    test.assertNotNull(versionNumber);
                    test.assertEqual(5, versionNumber.getMajor());
                    test.assertEqual(6, versionNumber.getMinor());
                    test.assertEqual(null, versionNumber.getPatch());
                    test.assertEqual("5.6", versionNumber.toString());
                });

                runner.test("with \"5.6.*\"", (Test test) ->
                {
                    final VersionNumber versionNumber = VersionNumber.parse("5.6.*");
                    test.assertNotNull(versionNumber);
                    test.assertEqual(5, versionNumber.getMajor());
                    test.assertEqual(6, versionNumber.getMinor());
                    test.assertEqual(null, versionNumber.getPatch());
                    test.assertEqual("5.6", versionNumber.toString());
                });

                runner.test("with \"5.6.7\"", (Test test) ->
                {
                    final VersionNumber versionNumber = VersionNumber.parse("5.6.7");
                    test.assertNotNull(versionNumber);
                    test.assertEqual(5, versionNumber.getMajor());
                    test.assertEqual(6, versionNumber.getMinor());
                    test.assertEqual(7, versionNumber.getPatch());
                    test.assertEqual("5.6.7", versionNumber.toString());
                });
            });
        });
    }
}
