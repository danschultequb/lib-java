package qub;

public class TestGroupTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(TestGroup.class, () ->
        {
            runner.testGroup("create(String,TestGroup,Skip)", () ->
            {
                runner.test("with null name", (Test test) ->
                {
                    test.assertThrows(() -> TestGroup.create(null, null, null),
                        new PreConditionFailure("name cannot be null."));
                });

                runner.test("with empty name", (Test test) ->
                {
                    test.assertThrows(() -> TestGroup.create("", null, null),
                        new PreConditionFailure("name cannot be empty."));
                });

                runner.test("with non-empty name", (Test test) ->
                {
                    final TestGroup tg = TestGroup.create("ab", null, null);
                    test.assertEqual("ab", tg.getName());
                    test.assertNull(tg.getParent());
                });
            });

            runner.testGroup("getFullName()", () ->
            {
                runner.test("with null parentTestGroup", (Test test) ->
                {
                    final TestGroup tg = TestGroup.create("ab", null, null);
                    test.assertEqual("ab", tg.getFullName());
                });

                runner.test("with non-null parentTestGroup", (Test test) ->
                {
                    final TestGroup parentTG = TestGroup.create("apples", null, null);
                    final TestGroup tg = TestGroup.create("ab", parentTG, null);
                    test.assertEqual("apples ab", tg.getFullName());
                });

                runner.test("with non-null grandParentTestGroup", (Test test) ->
                {
                    final TestGroup grandparentTG = TestGroup.create("cinnamon", null, null);
                    final TestGroup parentTG = TestGroup.create("apples", grandparentTG, null);
                    final TestGroup tg = TestGroup.create("ab", parentTG, null);
                    test.assertEqual("cinnamon apples ab", tg.getFullName());
                });
            });

            runner.testGroup("isMatch()", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final TestGroup tg = TestGroup.create("ab", null, null);
                    test.assertTrue(tg.matches(null));
                });

                runner.test("with pattern that doesn't match test name or full name", (Test test) ->
                {
                    final TestGroup tg = TestGroup.create("ab", null, null);
                    test.assertFalse(tg.matches(PathPattern.parse("bananas")));
                });

                runner.test("with pattern that isMatch test name", (Test test) ->
                {
                    final TestGroup tg = TestGroup.create("ab", null, null);
                    test.assertTrue(tg.matches(PathPattern.parse("ab")));
                });

                runner.test("with pattern that isMatch test full name", (Test test) ->
                {
                    final TestGroup grandparentTG = TestGroup.create("cinnamon", null, null);
                    final TestGroup parentTG = TestGroup.create("apples", grandparentTG, null);
                    final TestGroup tg = TestGroup.create("ab", parentTG, null);
                    test.assertTrue(tg.matches(PathPattern.parse("cinnamon*")));
                });
            });

            runner.testGroup("shouldSkip()", () ->
            {
                runner.test("with null skip and null parentTestGroup", (Test test) ->
                {
                    final TestGroup tg = TestGroup.create("ab", null, null);
                    test.assertFalse(tg.shouldSkip());
                });

                runner.test("with non-null skip with no arguments and null parentTestGroup", (Test test) ->
                {
                    final TestGroup tg = TestGroup.create("ab", null, runner.skip());
                    test.assertTrue(tg.shouldSkip());
                });

                runner.test("with non-null skip with false and null parentTestGroup", (Test test) ->
                {
                    final TestGroup tg = TestGroup.create("ab", null, runner.skip(false));
                    test.assertFalse(tg.shouldSkip());
                });

                runner.test("with non-null skip with false and message and null parentTestGroup", (Test test) ->
                {
                    final TestGroup tg = TestGroup.create("ab", null, runner.skip(false, "xyz"));
                    test.assertFalse(tg.shouldSkip());
                });

                runner.test("with non-null skip with true and null parentTestGroup", (Test test) ->
                {
                    final TestGroup tg = TestGroup.create("ab", null, runner.skip(true));
                    test.assertTrue(tg.shouldSkip());
                });

                runner.test("with non-null skip with true and message and null parentTestGroup", (Test test) ->
                {
                    final TestGroup tg = TestGroup.create("ab", null, runner.skip(true, "xyz"));
                    test.assertTrue(tg.shouldSkip());
                });

                runner.test("with null skip and non-null parentTestGroup with null skip", (Test test) ->
                {
                    final TestGroup parentTG = TestGroup.create("my", null, null);
                    final TestGroup tg = TestGroup.create("ab", parentTG, null);
                    test.assertFalse(tg.shouldSkip());
                });

                runner.test("with non-null skip with no arguments and non-null parentTestGroup with null skip", (Test test) ->
                {
                    final TestGroup parentTG = TestGroup.create("my", null, null);
                    final TestGroup tg = TestGroup.create("ab", parentTG, runner.skip());
                    test.assertTrue(tg.shouldSkip());
                });

                runner.test("with non-null skip with false and non-null parentTestGroup with null skip", (Test test) ->
                {
                    final TestGroup parentTG = TestGroup.create("my", null, null);
                    final TestGroup tg = TestGroup.create("ab", parentTG, runner.skip(false));
                    test.assertFalse(tg.shouldSkip());
                });

                runner.test("with non-null skip with false and message and non-null parentTestGroup with null skip", (Test test) ->
                {
                    final TestGroup parentTG = TestGroup.create("my", null, null);
                    final TestGroup tg = TestGroup.create("ab", parentTG, runner.skip(false, "xyz"));
                    test.assertFalse(tg.shouldSkip());
                });

                runner.test("with non-null skip with true and non-null parentTestGroup with null skip", (Test test) ->
                {
                    final TestGroup parentTG = TestGroup.create("my", null, null);
                    final TestGroup tg = TestGroup.create("ab", parentTG, runner.skip(true));
                    test.assertTrue(tg.shouldSkip());
                });

                runner.test("with non-null skip with false and message and non-null parentTestGroup with null skip", (Test test) ->
                {
                    final TestGroup parentTG = TestGroup.create("my", null, null);
                    final TestGroup tg = TestGroup.create("ab", parentTG, runner.skip(true, "xyz"));
                    test.assertTrue(tg.shouldSkip());
                });

                runner.test("with non-null skip with false and non-null parentTestGroup with non-null skip with true", (Test test) ->
                {
                    final TestGroup parentTG = TestGroup.create("my", null, runner.skip(true));
                    final TestGroup tg = TestGroup.create("ab", parentTG, runner.skip(false));
                    test.assertTrue(tg.shouldSkip());
                });
            });

            runner.testGroup("getSkipMessage()", () ->
            {
                runner.test("with null skip and null parentTestGroup", (Test test) ->
                {
                    final TestGroup tg = TestGroup.create("ab", null, null);
                    test.assertEqual(null, tg.getSkipMessage());
                });

                runner.test("with non-null skip with no arguments and null parentTestGroup", (Test test) ->
                {
                    final TestGroup tg = TestGroup.create("ab", null, runner.skip());
                    test.assertEqual("", tg.getSkipMessage());
                });

                runner.test("with non-null skip with false and null parentTestGroup", (Test test) ->
                {
                    final TestGroup tg = TestGroup.create("ab", null, runner.skip(false));
                    test.assertEqual(null, tg.getSkipMessage());
                });

                runner.test("with non-null skip with false and message and null parentTestGroup", (Test test) ->
                {
                    final TestGroup tg = TestGroup.create("ab", null, runner.skip(false, "xyz"));
                    test.assertEqual(null, tg.getSkipMessage());
                });

                runner.test("with non-null skip with true and null parentTestGroup", (Test test) ->
                {
                    final TestGroup tg = TestGroup.create("ab", null, runner.skip(true));
                    test.assertEqual("", tg.getSkipMessage());
                });

                runner.test("with non-null skip with false and message and null parentTestGroup", (Test test) ->
                {
                    final TestGroup tg = TestGroup.create("ab", null, runner.skip(true, "xyz"));
                    test.assertEqual("xyz", tg.getSkipMessage());
                });
            });

            runner.testGroup("toString()", () ->
            {
                runner.test("with no parent and no skip", (Test test) ->
                {
                    final TestGroup tg = TestGroup.create("B", null, null);
                    test.assertEqual("B", tg.toString());
                });

                runner.test("with parent but no skip", (Test test) ->
                {
                    final TestGroup tgParent = TestGroup.create("A", null, null);
                    final TestGroup tg = TestGroup.create("B", tgParent, null);
                    test.assertEqual("A B", tg.toString());
                });

                runner.test("with no parent but skip with null message", (Test test) ->
                {
                    final TestGroup tg = TestGroup.create("B", null, Skip.create());
                    test.assertEqual("B (Skipped)", tg.toString());
                });

                runner.test("with parent and skip with null message", (Test test) ->
                {
                    final TestGroup tgParent = TestGroup.create("A", null, null);
                    final TestGroup tg = TestGroup.create("B", tgParent, Skip.create());
                    test.assertEqual("A B (Skipped)", tg.toString());
                });

                runner.test("with no parent but skip with empty message", (Test test) ->
                {
                    final TestGroup tg = TestGroup.create("B", null, Skip.create(""));
                    test.assertEqual("B (Skipped)", tg.toString());
                });

                runner.test("with parent and skip with empty message", (Test test) ->
                {
                    final TestGroup tgParent = TestGroup.create("A", null, null);
                    final TestGroup tg = TestGroup.create("B", tgParent, Skip.create(""));
                    test.assertEqual("A B (Skipped)", tg.toString());
                });

                runner.test("with no parent but skip with non-empty message", (Test test) ->
                {
                    final TestGroup tg = TestGroup.create("B", null, Skip.create("oops"));
                    test.assertEqual("B (Skipped: oops)", tg.toString());
                });

                runner.test("with parent and skip with non-empty message", (Test test) ->
                {
                    final TestGroup tgParent = TestGroup.create("A", null, null);
                    final TestGroup tg = TestGroup.create("B", tgParent, Skip.create("oops!"));
                    test.assertEqual("A B (Skipped: oops!)", tg.toString());
                });
            });

            runner.testGroup("equals(Object)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final TestGroup tg = TestGroup.create("B", null, null);
                    test.assertFalse(tg.equals((Object)null));
                });

                runner.test("with non-TestGroup", (Test test) ->
                {
                    final TestGroup tg = TestGroup.create("B", null, null);
                    test.assertFalse(tg.equals((Object)"abc"));
                });

                runner.test("with different name", (Test test) ->
                {
                    final TestGroup tg = TestGroup.create("B", null, null);
                    test.assertFalse(tg.equals((Object)TestGroup.create("A", null, null)));
                });

                runner.test("with different parent", (Test test) ->
                {
                    final TestGroup tg = TestGroup.create("B", null, null);
                    test.assertFalse(tg.equals((Object)TestGroup.create("B", TestGroup.create("A", null, null), null)));
                });

                runner.test("with different skip", (Test test) ->
                {
                    final TestGroup tg = TestGroup.create("B", null, null);
                    test.assertFalse(tg.equals((Object)TestGroup.create("B", null, Skip.create())));
                });

                runner.test("with same", (Test test) ->
                {
                    final TestGroup tg = TestGroup.create("B", null, null);
                    test.assertTrue(tg.equals((Object)tg));
                });

                runner.test("with equal", (Test test) ->
                {
                    final TestGroup tg = TestGroup.create("B", null, null);
                    test.assertTrue(tg.equals((Object)TestGroup.create("B", null, null)));
                });
            });
        });
    }
}
