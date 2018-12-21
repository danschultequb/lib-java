package qub;

public class TestGroupTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(TestGroup.class, () ->
        {
            runner.testGroup("constructor", () ->
            {
                runner.test("with null name", (Test test) ->
                {
                    test.assertThrows(() -> new TestGroup(null, null, null));
                });

                runner.test("with empty name", (Test test) ->
                {
                    test.assertThrows(() -> new TestGroup("", null, null));
                });

                runner.test("with non-empty name", (Test test) ->
                {
                    final TestGroup tg = new TestGroup("ab", null, null);
                    test.assertEqual("ab", tg.getName());
                    test.assertNull(tg.getParentTestGroup());
                });
            });

            runner.testGroup("getFullName()", () ->
            {
                runner.test("with null parentTestGroup", (Test test) ->
                {
                    final TestGroup tg = new TestGroup("ab", null, null);
                    test.assertEqual("ab", tg.getFullName());
                });

                runner.test("with non-null parentTestGroup", (Test test) ->
                {
                    final TestGroup parentTG = new TestGroup("apples", null, null);
                    final TestGroup tg = new TestGroup("ab", parentTG, null);
                    test.assertEqual("apples ab", tg.getFullName());
                });

                runner.test("with non-null grandParentTestGroup", (Test test) ->
                {
                    final TestGroup grandparentTG = new TestGroup("cinnamon", null, null);
                    final TestGroup parentTG = new TestGroup("apples", grandparentTG, null);
                    final TestGroup tg = new TestGroup("ab", parentTG, null);
                    test.assertEqual("cinnamon apples ab", tg.getFullName());
                });
            });

            runner.testGroup("isMatch()", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final TestGroup tg = new TestGroup("ab", null, null);
                    test.assertTrue(tg.matches(null));
                });

                runner.test("with pattern that doesn't match test name or full name", (Test test) ->
                {
                    final TestGroup tg = new TestGroup("ab", null, null);
                    test.assertFalse(tg.matches(PathPattern.parse("bananas")));
                });

                runner.test("with pattern that isMatch test name", (Test test) ->
                {
                    final TestGroup tg = new TestGroup("ab", null, null);
                    test.assertTrue(tg.matches(PathPattern.parse("ab")));
                });

                runner.test("with pattern that isMatch test full name", (Test test) ->
                {
                    final TestGroup grandparentTG = new TestGroup("cinnamon", null, null);
                    final TestGroup parentTG = new TestGroup("apples", grandparentTG, null);
                    final TestGroup tg = new TestGroup("ab", parentTG, null);
                    test.assertTrue(tg.matches(PathPattern.parse("cinnamon*")));
                });
            });

            runner.testGroup("shouldSkip()", () ->
            {
                runner.test("with null skip and null parentTestGroup", (Test test) ->
                {
                    final TestGroup tg = new TestGroup("ab", null, null);
                    test.assertFalse(tg.shouldSkip());
                });

                runner.test("with non-null skip with no arguments and null parentTestGroup", (Test test) ->
                {
                    final TestGroup tg = new TestGroup("ab", null, runner.skip());
                    test.assertTrue(tg.shouldSkip());
                });

                runner.test("with non-null skip with false and null parentTestGroup", (Test test) ->
                {
                    final TestGroup tg = new TestGroup("ab", null, runner.skip(false));
                    test.assertFalse(tg.shouldSkip());
                });

                runner.test("with non-null skip with false and message and null parentTestGroup", (Test test) ->
                {
                    final TestGroup tg = new TestGroup("ab", null, runner.skip(false, "xyz"));
                    test.assertFalse(tg.shouldSkip());
                });

                runner.test("with non-null skip with true and null parentTestGroup", (Test test) ->
                {
                    final TestGroup tg = new TestGroup("ab", null, runner.skip(true));
                    test.assertTrue(tg.shouldSkip());
                });

                runner.test("with non-null skip with true and message and null parentTestGroup", (Test test) ->
                {
                    final TestGroup tg = new TestGroup("ab", null, runner.skip(true, "xyz"));
                    test.assertTrue(tg.shouldSkip());
                });

                runner.test("with null skip and non-null parentTestGroup with null skip", (Test test) ->
                {
                    final TestGroup parentTG = new TestGroup("my", null, null);
                    final TestGroup tg = new TestGroup("ab", parentTG, null);
                    test.assertFalse(tg.shouldSkip());
                });

                runner.test("with non-null skip with no arguments and non-null parentTestGroup with null skip", (Test test) ->
                {
                    final TestGroup parentTG = new TestGroup("my", null, null);
                    final TestGroup tg = new TestGroup("ab", parentTG, runner.skip());
                    test.assertTrue(tg.shouldSkip());
                });

                runner.test("with non-null skip with false and non-null parentTestGroup with null skip", (Test test) ->
                {
                    final TestGroup parentTG = new TestGroup("my", null, null);
                    final TestGroup tg = new TestGroup("ab", parentTG, runner.skip(false));
                    test.assertFalse(tg.shouldSkip());
                });

                runner.test("with non-null skip with false and message and non-null parentTestGroup with null skip", (Test test) ->
                {
                    final TestGroup parentTG = new TestGroup("my", null, null);
                    final TestGroup tg = new TestGroup("ab", parentTG, runner.skip(false, "xyz"));
                    test.assertFalse(tg.shouldSkip());
                });

                runner.test("with non-null skip with true and non-null parentTestGroup with null skip", (Test test) ->
                {
                    final TestGroup parentTG = new TestGroup("my", null, null);
                    final TestGroup tg = new TestGroup("ab", parentTG, runner.skip(true));
                    test.assertTrue(tg.shouldSkip());
                });

                runner.test("with non-null skip with false and message and non-null parentTestGroup with null skip", (Test test) ->
                {
                    final TestGroup parentTG = new TestGroup("my", null, null);
                    final TestGroup tg = new TestGroup("ab", parentTG, runner.skip(true, "xyz"));
                    test.assertTrue(tg.shouldSkip());
                });

                runner.test("with non-null skip with false and non-null parentTestGroup with non-null skip with true", (Test test) ->
                {
                    final TestGroup parentTG = new TestGroup("my", null, runner.skip(true));
                    final TestGroup tg = new TestGroup("ab", parentTG, runner.skip(false));
                    test.assertTrue(tg.shouldSkip());
                });
            });

            runner.testGroup("getSkipMessage()", () ->
            {
                runner.test("with null skip and null parentTestGroup", (Test test) ->
                {
                    final TestGroup tg = new TestGroup("ab", null, null);
                    test.assertEqual(null, tg.getSkipMessage());
                });

                runner.test("with non-null skip with no arguments and null parentTestGroup", (Test test) ->
                {
                    final TestGroup tg = new TestGroup("ab", null, runner.skip());
                    test.assertEqual(null, tg.getSkipMessage());
                });

                runner.test("with non-null skip with false and null parentTestGroup", (Test test) ->
                {
                    final TestGroup tg = new TestGroup("ab", null, runner.skip(false));
                    test.assertEqual(null, tg.getSkipMessage());
                });

                runner.test("with non-null skip with false and message and null parentTestGroup", (Test test) ->
                {
                    final TestGroup tg = new TestGroup("ab", null, runner.skip(false, "xyz"));
                    test.assertEqual(null, tg.getSkipMessage());
                });

                runner.test("with non-null skip with true and null parentTestGroup", (Test test) ->
                {
                    final TestGroup tg = new TestGroup("ab", null, runner.skip(true));
                    test.assertEqual(null, tg.getSkipMessage());
                });

                runner.test("with non-null skip with false and message and null parentTestGroup", (Test test) ->
                {
                    final TestGroup tg = new TestGroup("ab", null, runner.skip(true, "xyz"));
                    test.assertEqual("xyz", tg.getSkipMessage());
                });
            });

            runner.testGroup("toString()", () ->
            {
                runner.test("with no parent and no skip", (Test test) ->
                {
                    final TestGroup tg = new TestGroup("B", null, null);
                    test.assertEqual("B", tg.toString());
                });

                runner.test("with parent but no skip", (Test test) ->
                {
                    final TestGroup tgParent = new TestGroup("A", null, null);
                    final TestGroup tg = new TestGroup("B", tgParent, null);
                    test.assertEqual("A B", tg.toString());
                });

                runner.test("with no parent but skip with null message", (Test test) ->
                {
                    final TestGroup tg = new TestGroup("B", null, new Skip(null));
                    test.assertEqual("B (Skipped)", tg.toString());
                });

                runner.test("with parent and skip with null message", (Test test) ->
                {
                    final TestGroup tgParent = new TestGroup("A", null, null);
                    final TestGroup tg = new TestGroup("B", tgParent, new Skip(null));
                    test.assertEqual("A B (Skipped)", tg.toString());
                });

                runner.test("with no parent but skip with empty message", (Test test) ->
                {
                    final TestGroup tg = new TestGroup("B", null, new Skip(""));
                    test.assertEqual("B (Skipped)", tg.toString());
                });

                runner.test("with parent and skip with empty message", (Test test) ->
                {
                    final TestGroup tgParent = new TestGroup("A", null, null);
                    final TestGroup tg = new TestGroup("B", tgParent, new Skip(""));
                    test.assertEqual("A B (Skipped)", tg.toString());
                });

                runner.test("with no parent but skip with non-empty message", (Test test) ->
                {
                    final TestGroup tg = new TestGroup("B", null, new Skip("oops"));
                    test.assertEqual("B (Skipped: oops)", tg.toString());
                });

                runner.test("with parent and skip with non-empty message", (Test test) ->
                {
                    final TestGroup tgParent = new TestGroup("A", null, null);
                    final TestGroup tg = new TestGroup("B", tgParent, new Skip("oops!"));
                    test.assertEqual("A B (Skipped: oops!)", tg.toString());
                });
            });

            runner.testGroup("equals(Object)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final TestGroup tg = new TestGroup("B", null, null);
                    test.assertFalse(tg.equals((Object)null));
                });

                runner.test("with non-TestGroup", (Test test) ->
                {
                    final TestGroup tg = new TestGroup("B", null, null);
                    test.assertFalse(tg.equals((Object)"abc"));
                });

                runner.test("with different name", (Test test) ->
                {
                    final TestGroup tg = new TestGroup("B", null, null);
                    test.assertFalse(tg.equals((Object)new TestGroup("A", null, null)));
                });

                runner.test("with different parent", (Test test) ->
                {
                    final TestGroup tg = new TestGroup("B", null, null);
                    test.assertFalse(tg.equals((Object)new TestGroup("B", new TestGroup("A", null, null), null)));
                });

                runner.test("with different skip", (Test test) ->
                {
                    final TestGroup tg = new TestGroup("B", null, null);
                    test.assertFalse(tg.equals((Object)new TestGroup("B", null, new Skip(null))));
                });

                runner.test("with same", (Test test) ->
                {
                    final TestGroup tg = new TestGroup("B", null, null);
                    test.assertTrue(tg.equals((Object)tg));
                });

                runner.test("with equal", (Test test) ->
                {
                    final TestGroup tg = new TestGroup("B", null, null);
                    test.assertTrue(tg.equals((Object)new TestGroup("B", null, null)));
                });
            });
        });
    }
}
