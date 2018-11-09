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

                    headerList.add(new HttpHeader("a", "b"));
                    test.assertEqual(1, headerList.getCount());
                    test.assertEqual(0, headers.getCount(), "The MutableHttpHeaders should not be bound to the Iterable that was passed to its constructor.");
                });

                runner.test("with non-empty", (Test test) ->
                {
                    final List<HttpHeader> headerList = ArrayList.fromValues(new HttpHeader[]
                    {
                        new HttpHeader("a", "A"),
                        new HttpHeader("b", "B")
                    });
                    final MutableHttpHeaders headers = new MutableHttpHeaders(headerList);
                    test.assertEqual(2, headers.getCount());
                    test.assertSuccess(new HttpHeader("a", "A"), headers.get("a"));
                    test.assertSuccess(new HttpHeader("b", "B"), headers.get("b"));

                    headerList.add(new HttpHeader("c", "C"));
                    test.assertEqual(3, headerList.getCount());
                    test.assertEqual(2, headers.getCount(), "The MutableHttpHeaders should not be bound to the Iterable that was passed to its constructor.");
                });
            });

            runner.testGroup("set(HttpHeader)", () ->
            {
                runner.test("with null header", (Test test) ->
                {
                    final MutableHttpHeaders headers = new MutableHttpHeaders();
                    test.assertThrows(() -> headers.set(null));
                });

                runner.test("with non-null header", (Test test) ->
                {
                    final MutableHttpHeaders headers = new MutableHttpHeaders();
                    headers.set(new HttpHeader("header-name", "header-value"));
                    test.assertEqual("header-value", headers.get("header-name").getValue().getValue());
                });
            });

            runner.testGroup("set(String,String)", () ->
            {
                runner.test("with null header name", (Test test) ->
                {
                    final MutableHttpHeaders headers = new MutableHttpHeaders();
                    test.assertThrows(() -> headers.set(null, "header-value"));
                });

                runner.test("with empty header name", (Test test) ->
                {
                    final MutableHttpHeaders headers = new MutableHttpHeaders();
                    test.assertThrows(() -> headers.set("", "header-value"));
                });

                runner.test("with null header value", (Test test) ->
                {
                    final MutableHttpHeaders headers = new MutableHttpHeaders();
                    test.assertThrows(() -> headers.set("header-name", null));
                    test.assertFalse(headers.contains("header-name"));
                });

                runner.test("with empty header name", (Test test) ->
                {
                    final MutableHttpHeaders headers = new MutableHttpHeaders();
                    test.assertThrows(() -> headers.set("header-name", ""));
                    test.assertFalse(headers.contains("header-name"));
                });

                runner.test("with non-existing header", (Test test) ->
                {
                    final MutableHttpHeaders headers = new MutableHttpHeaders();
                    headers.set("header-name", "header-value");
                    test.assertEqual("header-value", headers.get("header-name").getValue().getValue());
                });

                runner.test("with existing header", (Test test) ->
                {
                    final MutableHttpHeaders headers = new MutableHttpHeaders();
                    headers.set("header-name", "header-value-1");

                    headers.set("header-name", "header-value-2");

                    test.assertEqual("header-value-2", headers.get("header-name").getValue().getValue());
                });
            });

            runner.testGroup("get(String)", () ->
            {
                runner.test("with null header name", (Test test) ->
                {
                    final MutableHttpHeaders headers = new MutableHttpHeaders();
                    test.assertThrows(() -> headers.get(null));
                });

                runner.test("with empty header name", (Test test) ->
                {
                    final MutableHttpHeaders headers = new MutableHttpHeaders();
                    test.assertThrows(() -> headers.get(""));
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
                    test.assertSuccess(new HttpHeader("header-name", "header-value"), headers.get("header-name"));
                });

                runner.test("with different case of existing header name", (Test test) ->
                {
                    final MutableHttpHeaders headers = new MutableHttpHeaders();
                    headers.set("header-name", "header-value");
                    test.assertSuccess(new HttpHeader("header-name", "header-value"), headers.get("HEADER-NAME"));
                });
            });

            runner.testGroup("getValue(String)", () ->
            {
                runner.test("with null header name", (Test test) ->
                {
                    final MutableHttpHeaders headers = new MutableHttpHeaders();
                    test.assertThrows(() -> headers.getValue(null));
                });

                runner.test("with empty header name", (Test test) ->
                {
                    final MutableHttpHeaders headers = new MutableHttpHeaders();
                    test.assertThrows(() -> headers.getValue(""));
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
