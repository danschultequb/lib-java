package qub;

public class HttpHeaderTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(HttpHeader.class, () ->
        {
            runner.testGroup("create(String,String)", () ->
            {
                runner.test("with null name", (Test test) ->
                {
                    test.assertError(new IllegalArgumentException("name cannot be null or empty."), HttpHeader.create(null, "V"));
                });

                runner.test("with empty name", (Test test) ->
                {
                    test.assertError(new IllegalArgumentException("name cannot be null or empty."), HttpHeader.create("", "V"));
                });

                runner.test("with null value", (Test test) ->
                {
                    test.assertError(new IllegalArgumentException("value cannot be null or empty."), HttpHeader.create("N", null));
                });

                runner.test("with empty value", (Test test) ->
                {
                    test.assertError(new IllegalArgumentException("value cannot be null or empty."), HttpHeader.create("N", ""));
                });

                runner.test("with name and value", (Test test) ->
                {
                    final Result<HttpHeader> header = HttpHeader.create("user-agent", "qub-browser");
                    test.assertSuccess(header);
                    test.assertEqual("user-agent", header.getValue().getName());
                    test.assertEqual("qub-browser", header.getValue().getValue());
                });
            });
        });
    }
}
