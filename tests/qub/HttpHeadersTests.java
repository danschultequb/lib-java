package qub;

public class HttpHeadersTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(HttpHeaders.class, () ->
        {
            runner.test("constructor()", (Test test) ->
            {
                final HttpHeaders headers = new HttpHeaders();
                test.assertFalse(headers.any());
                test.assertEqual(0, headers.getCount());
            });

            runner.testGroup("set(String,String)", () ->
            {
                runner.test("with null header name", (Test test) ->
                {
                    final HttpHeaders headers = new HttpHeaders();
                    final Result<Boolean> setResult = headers.set(null, "header-value");
                    test.assertDone(false, new IllegalArgumentException("name cannot be null or empty."), setResult);
                });

                runner.test("with empty header name", (Test test) ->
                {
                    final HttpHeaders headers = new HttpHeaders();
                    final Result<Boolean> setResult = headers.set("", "header-value");
                    test.assertDone(false, new IllegalArgumentException("name cannot be null or empty."), setResult);
                });

                runner.test("with null header value", (Test test) ->
                {
                    final HttpHeaders headers = new HttpHeaders();
                    final Result<Boolean> setResult = headers.set("header-name", null);
                    test.assertDone(false, new IllegalArgumentException("value cannot be null or empty."), setResult);
                });

                runner.test("with empty header name", (Test test) ->
                {
                    final HttpHeaders headers = new HttpHeaders();
                    final Result<Boolean> setResult = headers.set("header-name", "");
                    test.assertDone(false, new IllegalArgumentException("value cannot be null or empty."), setResult);
                });

                runner.test("with non-existing header", (Test test) ->
                {
                    final HttpHeaders headers = new HttpHeaders();
                    final Result<Boolean> setResult = headers.set("header-name", "header-value");
                    test.assertSuccess(true, setResult);
                    test.assertEqual("header-value", headers.get("header-name").getValue().getValue());
                });

                runner.test("with existing header", (Test test) ->
                {
                    final HttpHeaders headers = new HttpHeaders();
                    headers.set("header-name", "header-value-1");

                    final Result<Boolean> setResult = headers.set("header-name", "header-value-2");
                    test.assertSuccess(true, setResult);

                    test.assertEqual("header-value-2", headers.get("header-name").getValue().getValue());
                });
            });

            runner.testGroup("get(String)", () ->
            {
                runner.test("with null header name", (Test test) ->
                {
                    final HttpHeaders headers = new HttpHeaders();
                    test.assertError(new IllegalArgumentException("headerName cannot be null or empty."), headers.get(null));
                });

                runner.test("with empty header name", (Test test) ->
                {
                    final HttpHeaders headers = new HttpHeaders();
                    test.assertError(new IllegalArgumentException("headerName cannot be null or empty."), headers.get(""));
                });

                runner.test("with non-existing header name", (Test test) ->
                {
                    final HttpHeaders headers = new HttpHeaders();
                    test.assertError(new KeyNotFoundException("header-name"), headers.get("header-name"));
                });

                runner.test("with existing header name", (Test test) ->
                {
                    final HttpHeaders headers = new HttpHeaders();
                    headers.set("header-name", "header-value");
                    test.assertSuccess(HttpHeader.create("header-name", "header-value").getValue(), headers.get("header-name"));
                });

                runner.test("with different case of existing header name", (Test test) ->
                {
                    final HttpHeaders headers = new HttpHeaders();
                    headers.set("header-name", "header-value");
                    test.assertSuccess(HttpHeader.create("header-name", "header-value").getValue(), headers.get("HEADER-NAME"));
                });
            });
        });
    }
}
