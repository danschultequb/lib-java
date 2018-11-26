package qub;

public class MutableHttpRequestTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(MutableHttpRequest.class, () ->
        {
            runner.testGroup("constructor(HttpMethod,URL)", () ->
            {
                runner.test("with null HttpMethod", (Test test) ->
                {
                    final HttpMethod method = null;
                    final URL url = URL.parse("https://www.example.com").throwErrorOrGetValue();
                    test.assertThrows(() -> new MutableHttpRequest(method, url));
                });

                runner.test("with null url", (Test test) ->
                {
                    final HttpMethod method = HttpMethod.GET;
                    final URL url = null;
                    test.assertThrows(() -> new MutableHttpRequest(method, url));
                });

                runner.test("with valid HttpMethod and valid url", (Test test) ->
                {
                    final HttpMethod method = HttpMethod.GET;
                    final URL url = URL.parse("https://www.example.com").throwErrorOrGetValue();
                    final MutableHttpRequest request = new MutableHttpRequest(method, url);
                    test.assertEqual(HttpMethod.GET, request.getMethod());
                    test.assertEqual(url, request.getURL());
                    test.assertEqual(new Array<HttpHeader>(0), request.getHeaders());
                    test.assertNull(request.getBody());
                });
            });

            runner.testGroup("setMethod(HttpMethod)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final MutableHttpRequest request = create(HttpMethod.GET, "https://www.example.com");
                    test.assertThrows(() -> request.setMethod(null));
                    test.assertEqual(HttpMethod.GET, request.getMethod());
                });

                runner.test("with POST", (Test test) ->
                {
                    final MutableHttpRequest request = create(HttpMethod.GET, "https://www.example.com");
                    request.setMethod(HttpMethod.POST);
                    test.assertEqual(HttpMethod.POST, request.getMethod());
                });
            });

            runner.testGroup("setUrl(String)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final MutableHttpRequest request = create(HttpMethod.GET, "https://www.example.com");
                    test.assertThrows(() -> request.setUrl((String)null));
                    test.assertEqual(URL.parse("https://www.example.com").throwErrorOrGetValue(), request.getURL());
                });

                runner.test("with empty", (Test test) ->
                {
                    final MutableHttpRequest request = create(HttpMethod.GET, "https://www.example.com");
                    test.assertThrows(() -> request.setUrl(""));
                    test.assertEqual(URL.parse("https://www.example.com").getValue(), request.getURL());
                });

                runner.test("with invalid URL", (Test test) ->
                {
                    final MutableHttpRequest request = create(HttpMethod.GET, "https://www.example.com");
                    test.assertError(new IllegalArgumentException("A URL must begin with either a scheme (such as \"http\") or a host (such as \"www.example.com\"), not \"'\"."), request.setUrl("I'm not a valid url"));
                    test.assertEqual(URL.parse("https://www.example.com").getValue(), request.getURL());
                });

                runner.test("with valid URL", (Test test) ->
                {
                    final MutableHttpRequest request = create(HttpMethod.GET, "https://www.example.com");
                    test.assertSuccess(true, request.setUrl("http://www.google.com"));
                    test.assertEqual(URL.parse("http://www.google.com").getValue(), request.getURL());
                });
            });

            runner.testGroup("setBody(String)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final MutableHttpRequest request = create(HttpMethod.GET, "https://www.example.com");
                    test.assertThrows(() -> request.setBody((String)null));
                    test.assertNull(request.getBody());
                    test.assertEqual(Iterable.empty(), request.getHeaders());
                });

                runner.test("with empty", (Test test) ->
                {
                    final MutableHttpRequest request = create(HttpMethod.GET, "https://www.example.com");
                    request.setBody("");
                    test.assertNull(request.getBody());
                    test.assertEqual(Iterable.empty(), request.getHeaders());
                });

                runner.test("with non-empty", (Test test) ->
                {
                    final MutableHttpRequest request = create(HttpMethod.GET, "https://www.example.com");
                    request.setBody("hello");
                    test.assertSuccess("hello", request.getBody().asLineReadStream().readLine());
                    test.assertEqual(
                        Array.fromValues(new HttpHeader[]
                        {
                            new HttpHeader("Content-Length", 5)
                        }),
                        request.getHeaders());
                });
            });

            runner.testGroup("setBody(byte[])", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final MutableHttpRequest request = create(HttpMethod.GET, "https://www.example.com");
                    request.setBody((byte[])null);
                    test.assertNull(request.getBody());
                    test.assertEqual(new Array<HttpHeader>(0), request.getHeaders());
                });

                runner.test("with empty", (Test test) ->
                {
                    final MutableHttpRequest request = create(HttpMethod.GET, "https://www.example.com");
                    request.setBody(new byte[0]);
                    test.assertNull(request.getBody());
                    test.assertEqual(new Array<HttpHeader>(0), request.getHeaders());
                });

                runner.test("with non-empty", (Test test) ->
                {
                    final MutableHttpRequest request = create(HttpMethod.GET, "https://www.example.com");
                    request.setBody(new byte[] { 0, 1, 2, 3, 4 });
                    test.assertSuccess(new byte[] { 0, 1, 2, 3, 4 }, request.getBody().readAllBytes());
                    test.assertEqual(
                        Array.fromValues(new HttpHeader[]
                        {
                            new HttpHeader("Content-Length", 5)
                        }),
                        request.getHeaders());
                });
            });

            runner.testGroup("setBody(int,ByteReadStream)", () ->
            {
                runner.test("with negative contentLength", (Test test) ->
                {
                    final MutableHttpRequest request = create(HttpMethod.POST, "https://www.example.com");
                    test.assertThrows(() -> request.setBody(-1, null));
                    test.assertNull(request.getBody());
                    test.assertEqual(new Array<HttpHeader>(0), request.getHeaders());
                });

                runner.test("with 0 contentLength and null body", (Test test) ->
                {
                    final MutableHttpRequest request = create(HttpMethod.POST, "https://www.example.com");
                    request.setBody(0, null);
                    test.assertNull(request.getBody());
                    test.assertEqual(new Array<HttpHeader>(0), request.getHeaders());
                });

                runner.test("with 0 contentLength and non-null body", (Test test) ->
                {
                    final MutableHttpRequest request = create(HttpMethod.POST, "https://www.example.com");
                    test.assertThrows(() -> request.setBody(0, new InMemoryByteStream()));
                    test.assertNull(request.getBody());
                    test.assertEqual(new Array<HttpHeader>(0), request.getHeaders());
                });

                runner.test("with 3 contentLength and null body", (Test test) ->
                {
                    final MutableHttpRequest request = create(HttpMethod.POST, "https://www.example.com");
                    test.assertThrows(() -> request.setBody(3, null));
                    test.assertNull(request.getBody());
                    test.assertEqual(new Array<HttpHeader>(0), request.getHeaders());
                });

                runner.test("with 3 contentLength and non-null body", (Test test) ->
                {
                    final MutableHttpRequest request = create(HttpMethod.POST, "https://www.example.com");
                    request.setBody(3, new InMemoryByteStream(new byte[] { 0, 1, 2 }));
                    test.assertNotNull(request.getBody());
                    test.assertEqual(
                        Array.fromValues(new HttpHeader[]
                        {
                            new HttpHeader("Content-Length", 3)
                        }),
                        request.getHeaders());
                });
            });
        });
    }

    private static MutableHttpRequest create(HttpMethod method, String url)
    {
        return new MutableHttpRequest(method, URL.parse(url).throwErrorOrGetValue());
    }
}