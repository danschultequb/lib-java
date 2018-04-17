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

            runner.testGroup("equals(Object)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final HttpHeader header = HttpHeader.create("apples", "fruit").getValue();
                    test.assertFalse(header.equals((Object)null));
                });

                runner.test("with different type", (Test test) ->
                {
                    final HttpHeader header = HttpHeader.create("apples", "fruit").getValue();
                    test.assertFalse(header.equals((Object)"test"));
                });

                runner.test("with different header name", (Test test) ->
                {
                    final HttpHeader header = HttpHeader.create("apples", "fruit").getValue();
                    test.assertFalse(header.equals((Object)HttpHeader.create("oranges", "fruit").getValue()));
                });

                runner.test("with different header value", (Test test) ->
                {
                    final HttpHeader header = HttpHeader.create("apples", "fruit").getValue();
                    test.assertFalse(header.equals((Object)HttpHeader.create("apples", "yummy").getValue()));
                });

                runner.test("with same header", (Test test) ->
                {
                    final HttpHeader header = HttpHeader.create("apples", "fruit").getValue();
                    test.assertTrue(header.equals((Object)header));
                });

                runner.test("with equal header", (Test test) ->
                {
                    final HttpHeader header = HttpHeader.create("apples", "fruit").getValue();
                    test.assertTrue(header.equals((Object)HttpHeader.create("apples", "fruit").getValue()));
                });
            });

            runner.testGroup("equals(HttpHeader)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final HttpHeader header = HttpHeader.create("apples", "fruit").getValue();
                    test.assertFalse(header.equals((HttpHeader)null));
                });

                runner.test("with different header name", (Test test) ->
                {
                    final HttpHeader header = HttpHeader.create("apples", "fruit").getValue();
                    test.assertFalse(header.equals(HttpHeader.create("oranges", "fruit").getValue()));
                });

                runner.test("with different header value", (Test test) ->
                {
                    final HttpHeader header = HttpHeader.create("apples", "fruit").getValue();
                    test.assertFalse(header.equals(HttpHeader.create("apples", "yummy").getValue()));
                });

                runner.test("with same header", (Test test) ->
                {
                    final HttpHeader header = HttpHeader.create("apples", "fruit").getValue();
                    test.assertTrue(header.equals(header));
                });

                runner.test("with equal header", (Test test) ->
                {
                    final HttpHeader header = HttpHeader.create("apples", "fruit").getValue();
                    test.assertTrue(header.equals(HttpHeader.create("apples", "fruit").getValue()));
                });
            });

            runner.test("toString()", (Test test) ->
            {
                final HttpHeader header = HttpHeader.create("A", "B").getValue();
                test.assertEqual("A: B", header.toString());
            });
        });
    }
}
