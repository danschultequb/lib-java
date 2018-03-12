package qub;

public class IssueTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(Issue.class, () ->
        {
            runner.test("constructor()", (Test test) ->
            {
                final Issue issue = new Issue("test message", new Span(1, 2), Issue.Type.Warning);
                test.assertEqual("test message", issue.getMessage());
                test.assertEqual(new Span(1, 2), issue.getSpan());
                test.assertEqual(Issue.Type.Warning, issue.getType());
                test.assertEqual("Warning [1, 3): \"test message\"", issue.toString());
            });

            runner.test("warning()", (Test test) ->
            {
                final Issue issue = Issue.warning("abc", 7, 10);
                test.assertEqual("abc", issue.getMessage());
                test.assertEqual(new Span(7, 10), issue.getSpan());
                test.assertEqual(Issue.Type.Warning, issue.getType());
            });

            runner.test("error()", (Test test) ->
            {
                final Issue issue = Issue.error("abc", 7, 10);
                test.assertEqual("abc", issue.getMessage());
                test.assertEqual(new Span(7, 10), issue.getSpan());
                test.assertEqual(Issue.Type.Error, issue.getType());
            });
        });
    }
}
