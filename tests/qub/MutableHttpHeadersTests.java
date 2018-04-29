package qub;

public class MutableHttpHeadersTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(MutableHttpHeaders.class, () ->
        {
            runner.test("constructor()", (Test test) ->
            {
                final MutableHttpHeaders headers = new MutableHttpHeaders();
                test.assertFalse(headers.any());
                test.assertEqual(0, headers.getCount());
            });

            runner.testGroup("constuctor(Iterable<HttpHeader>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final MutableHttpHeaders headers = new MutableHttpHeaders(null);
                    test.assertEqual(0, headers.getCount());
                });

                runner.test("with empty", (Test test) ->
                {
                    final List<HttpHeader> headerList = new ArrayList<HttpHeader>();
                    final MutableHttpHeaders headers = new MutableHttpHeaders(headerList);
                    test.assertEqual(0, headers.getCount());

                    headerList.add(HttpHeader.create("a", "b").getValue());
                    test.assertEqual(1, headerList.getCount());
                    test.assertEqual(0, headers.getCount(), "The MutableHttpHeaders should not be bound to the Iterable that was passed to its constructor.");
                });

                runner.test("with non-empty", (Test test) ->
                {
                    final List<HttpHeader> headerList = ArrayList.fromValues(new HttpHeader[]
                    {
                        HttpHeader.create("a", "A").getValue(),
                        HttpHeader.create("b", "B").getValue()
                    });
                    final MutableHttpHeaders headers = new MutableHttpHeaders(headerList);
                    test.assertEqual(2, headers.getCount());
                    test.assertSuccess(HttpHeader.create("a", "A").getValue(), headers.get("a"));
                    test.assertSuccess(HttpHeader.create("b", "B").getValue(), headers.get("b"));

                    headerList.add(HttpHeader.create("c", "C").getValue());
                    test.assertEqual(3, headerList.getCount());
                    test.assertEqual(2, headers.getCount(), "The MutableHttpHeaders should not be bound to the Iterable that was passed to its constructor.");
                });
            });

            runner.testGroup("set(HttpHeader)", () ->
            {
                runner.test("with null header", (Test test) ->
                {
                    final MutableHttpHeaders headers = new MutableHttpHeaders();
                    test.assertError(new IllegalArgumentException("header cannot be null."), headers.set(null));
                });

                runner.test("with non-null header", (Test test) ->
                {
                    final MutableHttpHeaders headers = new MutableHttpHeaders();
                    final Result<Boolean> setResult = headers.set(HttpHeader.create("header-name", "header-value").getValue());
                    test.assertSuccess(true, setResult);
                    test.assertEqual("header-value", headers.get("header-name").getValue().getValue());
                });
            });

            runner.testGroup("set(String,String)", () ->
            {
                runner.test("with null header name", (Test test) ->
                {
                    final MutableHttpHeaders headers = new MutableHttpHeaders();
                    final Result<Boolean> setResult = headers.set(null, "header-value");
                    test.assertDone(false, new IllegalArgumentException("name cannot be null or empty."), setResult);
                });

                runner.test("with empty header name", (Test test) ->
                {
                    final MutableHttpHeaders headers = new MutableHttpHeaders();
                    final Result<Boolean> setResult = headers.set("", "header-value");
                    test.assertDone(false, new IllegalArgumentException("name cannot be null or empty."), setResult);
                });

                runner.test("with null header value", (Test test) ->
                {
                    final MutableHttpHeaders headers = new MutableHttpHeaders();
                    final Result<Boolean> setResult = headers.set("header-name", null);
                    test.assertDone(false, new IllegalArgumentException("value cannot be null or empty."), setResult);
                });

                runner.test("with empty header name", (Test test) ->
                {
                    final MutableHttpHeaders headers = new MutableHttpHeaders();
                    final Result<Boolean> setResult = headers.set("header-name", "");
                    test.assertDone(false, new IllegalArgumentException("value cannot be null or empty."), setResult);
                });

                runner.test("with non-existing header", (Test test) ->
                {
                    final MutableHttpHeaders headers = new MutableHttpHeaders();
                    final Result<Boolean> setResult = headers.set("header-name", "header-value");
                    test.assertSuccess(true, setResult);
                    test.assertEqual("header-value", headers.get("header-name").getValue().getValue());
                });

                runner.test("with existing header", (Test test) ->
                {
                    final MutableHttpHeaders headers = new MutableHttpHeaders();
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
                    final MutableHttpHeaders headers = new MutableHttpHeaders();
                    test.assertError(new IllegalArgumentException("headerName cannot be null or empty."), headers.get(null));
                });

                runner.test("with empty header name", (Test test) ->
                {
                    final MutableHttpHeaders headers = new MutableHttpHeaders();
                    test.assertError(new IllegalArgumentException("headerName cannot be null or empty."), headers.get(""));
                });

                runner.test("with non-existing header name", (Test test) ->
                {
                    final MutableHttpHeaders headers = new MutableHttpHeaders();
                    test.assertError(new KeyNotFoundException("header-name"), headers.get("header-name"));
                });

                runner.test("with existing header name", (Test test) ->
                {
                    final MutableHttpHeaders headers = new MutableHttpHeaders();
                    headers.set("header-name", "header-value");
                    test.assertSuccess(HttpHeader.create("header-name", "header-value").getValue(), headers.get("header-name"));
                });

                runner.test("with different case of existing header name", (Test test) ->
                {
                    final MutableHttpHeaders headers = new MutableHttpHeaders();
                    headers.set("header-name", "header-value");
                    test.assertSuccess(HttpHeader.create("header-name", "header-value").getValue(), headers.get("HEADER-NAME"));
                });
            });

            runner.testGroup("getValue(String)", () ->
            {
                runner.test("with null header name", (Test test) ->
                {
                    final MutableHttpHeaders headers = new MutableHttpHeaders();
                    test.assertError(new IllegalArgumentException("headerName cannot be null or empty."), headers.getValue(null));
                });

                runner.test("with empty header name", (Test test) ->
                {
                    final MutableHttpHeaders headers = new MutableHttpHeaders();
                    test.assertError(new IllegalArgumentException("headerName cannot be null or empty."), headers.getValue(""));
                });

                runner.test("with non-existing header name", (Test test) ->
                {
                    final MutableHttpHeaders headers = new MutableHttpHeaders();
                    test.assertError(new KeyNotFoundException("header-name"), headers.getValue("header-name"));
                });

                runner.test("with existing header name", (Test test) ->
                {
                    final MutableHttpHeaders headers = new MutableHttpHeaders();
                    headers.set("header-name", "header-value");
                    test.assertSuccess("header-value", headers.getValue("header-name"));
                });

                runner.test("with different case of existing header name", (Test test) ->
                {
                    final MutableHttpHeaders headers = new MutableHttpHeaders();
                    headers.set("header-name", "header-value");
                    test.assertSuccess("header-value", headers.getValue("HEADER-NAME"));
                });
            });

            runner.testGroup("remove(String)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final MutableHttpHeaders headers = new MutableHttpHeaders();
                    test.assertError(new IllegalArgumentException("headerName cannot be null."), headers.remove(null));
                });

                runner.test("with empty", (Test test) ->
                {
                    final MutableHttpHeaders headers = new MutableHttpHeaders();
                    test.assertError(new IllegalArgumentException("headerName cannot be empty."), headers.remove(""));
                });

                runner.test("with not found header name", (Test test) ->
                {
                    final MutableHttpHeaders headers = new MutableHttpHeaders();
                    test.assertDone(false, new KeyNotFoundException("A"), headers.remove("A"));
                });

                runner.test("with found header name", (Test test) ->
                {
                    final MutableHttpHeaders headers = new MutableHttpHeaders();
                    headers.set("A", "B");
                    test.assertSuccess(true, headers.remove("A"));
                    test.assertDone(false, new KeyNotFoundException("A"), headers.remove("A"));
                });
            });
        });
    }
}