package qub;

public class PathPatternTests
{
    public static void test(final TestRunner runner)
    {
        runner.testGroup(PathPattern.class, () ->
        {
            runner.testGroup("parse(Path)", () ->
            {
                final Action1<String> parsePathTest = pathString ->
                {
                    runner.test("with " + Strings.escapeAndQuote(pathString), (Test test) ->
                    {
                        final Path path = Path.parse(pathString);

                        final PathPattern pattern = PathPattern.parse(path);
                        test.assertTrue(pattern instanceof SimplePathPattern);
                        test.assertNotNull(pattern);

                        final String pathString1 = (path == null ? null : path.toString());
                        test.assertEqual(pathString1, pattern.toString());
                        test.assertTrue(pattern.isMatch(path));
                        test.assertTrue(pattern.isMatch(pathString1));
                    });
                };
                
                parsePathTest.run(null);
                parsePathTest.run("");
                parsePathTest.run("/folder/subfolder");
                parsePathTest.run("*700");
            });
            
            runner.testGroup("parse(String)", () ->
            {
                final Action1<String> parseStringTest = pathString ->
                {
                    runner.test("with " + Strings.escapeAndQuote(pathString), (Test test) ->
                    {
                        final PathPattern pattern = PathPattern.parse(pathString);
                        test.assertTrue(pattern instanceof SimplePathPattern);
                        test.assertNotNull(pattern);

                        test.assertEqual(pathString, pattern.toString());
                        test.assertTrue(pattern.isMatch(pathString));
                    });
                };
                
                parseStringTest.run(null);
                parseStringTest.run("");
                parseStringTest.run("abcd");
                parseStringTest.run("\\");
                parseStringTest.run("/");
                parseStringTest.run("*");
                parseStringTest.run("*abc");
                parseStringTest.run("**");
                parseStringTest.run("**test");
                parseStringTest.run("*700");
            });
            
            runner.testGroup("isMatch(String)", () ->
            {
                final Action3<String,String[],String[]> isMatchTest = (String pattern, String[] matches, String[] nonMatches) ->
                {
                    runner.testGroup("with " + Strings.escapeAndQuote(pattern), () ->
                    {
                        final PathPattern pathPattern = PathPattern.parse(pattern);

                        if (matches != null && matches.length > 0)
                        {
                            for (String match : matches)
                            {
                                runner.test("matching " + Strings.escapeAndQuote(match), (Test test) ->
                                {
                                    test.assertTrue(pathPattern.isMatch(match));
                                    test.assertTrue(pathPattern.isMatch(Path.parse(match)));
                                });
                            }
                        }

                        if (nonMatches != null && nonMatches.length > 0)
                        {
                            for (String nonMatch : nonMatches)
                            {
                                runner.test("not matching " + Strings.escapeAndQuote(nonMatch), (Test test) ->
                                {
                                    test.assertFalse(pathPattern.isMatch(nonMatch));
                                    test.assertFalse(pathPattern.isMatch(Path.parse(nonMatch)));
                                });
                            }
                        }
                    });
                };

                isMatchTest.run(null,
                    new String[] { null, "" },
                    new String[] { " ", "a", "1", "7", "*", "**", "*700" });

                isMatchTest.run("",
                    new String[] { null, "" },
                    new String[] { " ", "a", "1", "7", "*", "**", "*700" });

                isMatchTest.run("abc",
                    new String[] { "abc" },
                    new String[] { null, "", "a", "b", "c", "ab", "bc", "ABC", "abcd" });

                isMatchTest.run("a/b",
                    new String[] { "a/b", "a\\b" },
                    new String[] { null, "", "a/b/", "a\\b\\", "a/", "A/B", "a/c" });

                isMatchTest.run("*",
                    new String[] { null, "", "a", "300", ".java", "..java", "Test.jav", "Test.javas" },
                    new String[] { "/", "\\", "/a", "a/b", "a/b/c/d/test.java" });

                isMatchTest.run("*.java",
                    new String[] { ".java", "Test.java", "..java" },
                    new String[] { null, "", "Test.jav", "Test.javas", "a/Test.java", "/Test.java", "\\Test.java" });

                isMatchTest.run("sources/**.java",
                    new String[] { "sources/Test.java", "sources/qub/Test.java", "sources\\qub\\package\\Class.java", "sources/.java" },
                    new String[] { "output/Test.java", "source/Test.java", "sources/blah.javb" });

                isMatchTest.run("700",
                    new String[] { "700" },
                    new String[] { null, "", "1700", "7001", "70", "00", "7", "0" });

                isMatchTest.run("*a",
                    new String[] { "a", "ba", "cba" },
                    new String[] { null, "", "b", "c", "70", "ab" });

                isMatchTest.run("*700",
                    new String[] { "700", "1700", "abc700", " 700", "with cascade strategy and 700" },
                    new String[] { null, "", "BondsAction", "cascade strategy", "70", "100", "with cascade strategy and 100" });
            });

            runner.test("equals()", (Test test) ->
            {
                final PathPattern pattern = PathPattern.parse("a/b/c");

                test.assertFalse(pattern.equals((Object)null));
                test.assertFalse(pattern.equals((PathPattern)null));
                test.assertFalse(pattern.equals("path pattern"));
                test.assertFalse(pattern.equals(PathPattern.parse("a/b")));

                test.assertTrue(pattern.equals(pattern));
                test.assertTrue(pattern.equals(PathPattern.parse(pattern.toString())));
            });
        });
    }
}
