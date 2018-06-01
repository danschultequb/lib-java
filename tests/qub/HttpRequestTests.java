package qub;

public class HttpRequestTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(HttpRequest.class, () ->
        {
            runner.testGroup("create(HttpMethod,String)", () ->
            {
                runner.test("with null HttpMethod", (Test test) ->
                {
                    final Result<HttpRequest> request = HttpRequest.create(null, "https://www.example.com");
                    test.assertError(new IllegalArgumentException("method cannot be null."), request);
                });

                runner.test("with null url", (Test test) ->
                {
                    final Result<HttpRequest> request = HttpRequest.create(HttpMethod.GET, (String)null);
                    test.assertError(new IllegalArgumentException("urlString cannot be null."), request);
                });

                runner.test("with empty url", (Test test) ->
                {
                    final Result<HttpRequest> request = HttpRequest.create(HttpMethod.GET, "");
                    test.assertError(new IllegalArgumentException("urlString cannot be empty."), request);
                });

                runner.test("with invalid url", (Test test) ->
                {
                    final Result<HttpRequest> request = HttpRequest.create(HttpMethod.GET, "I'm not a good URL");
                    test.assertError(new IllegalArgumentException("A URL must begin with either a scheme (such as \"http\") or a host (such as \"www.example.com\"), not \"'\"."), request);
                });

                runner.test("with valid HttpMethod and valid url", (Test test) ->
                {
                    final Result<HttpRequest> request = HttpRequest.create(HttpMethod.GET, "https://www.example.com");
                    test.assertSuccess(request);
                    test.assertEqual(HttpMethod.GET, request.getValue().getMethod());
                    test.assertEqual(URL.parse("https://www.example.com").getValue(), request.getValue().getUrl());
                    test.assertEqual(0, request.getValue().getHeaders().getCount());
                    test.assertNull(request.getValue().getBody());
                });
            });

            runner.testGroup("create(HttpMethod,String,Iterable<HttpHeader>,int,ByteReadStream)", () ->
            {
                runner.test("with null HttpMethod", (Test test) ->
                {
                    final Result<HttpRequest> request = HttpRequest.create(null, "https://www.example.com", new Array<>(0), 0, null);
                    test.assertError(new IllegalArgumentException("method cannot be null."), request);
                });

                runner.test("with null url", (Test test) ->
                {
                    final Result<HttpRequest> request = HttpRequest.create(HttpMethod.GET, (String)null, new Array<>(0), 0, null);
                    test.assertError(new IllegalArgumentException("urlString cannot be null."), request);
                });

                runner.test("with empty url", (Test test) ->
                {
                    final Result<HttpRequest> request = HttpRequest.create(HttpMethod.GET, "", new Array<>(0), 0, null);
                    test.assertError(new IllegalArgumentException("urlString cannot be empty."), request);
                });

                runner.test("with invalid url", (Test test) ->
                {
                    final Result<HttpRequest> request = HttpRequest.create(HttpMethod.GET, "I'm not a good URL", new Array<>(0), 0, null);
                    test.assertError(new IllegalArgumentException("A URL must begin with either a scheme (such as \"http\") or a host (such as \"www.example.com\"), not \"'\"."), request);
                });

                runner.test("with valid HttpMethod and valid url", (Test test) ->
                {
                    final Result<HttpRequest> request = HttpRequest.create(HttpMethod.GET, "https://www.example.com", new Array<>(0), 0, null);
                    test.assertSuccess(request);
                    test.assertEqual(HttpMethod.GET, request.getValue().getMethod());
                    test.assertEqual(URL.parse("https://www.example.com").getValue(), request.getValue().getUrl());
                    test.assertEqual(0, request.getValue().getHeaders().getCount());
                    test.assertNull(request.getValue().getBody());
                });

                runner.test("with null headers", (Test test) ->
                {
                    final Result<HttpRequest> request = HttpRequest.create(HttpMethod.GET, "https://www.example.com", null, 0, null);
                    test.assertSuccess(request);
                    test.assertEqual(HttpMethod.GET, request.getValue().getMethod());
                    test.assertEqual(URL.parse("https://www.example.com").getValue(), request.getValue().getUrl());
                    test.assertEqual(0, request.getValue().getHeaders().getCount());
                    test.assertNull(request.getValue().getBody());
                });

                runner.test("with empty headers", (Test test) ->
                {
                    final Result<HttpRequest> request = HttpRequest.create(HttpMethod.GET, "https://www.example.com", new Array<>(0), 0, null);
                    test.assertSuccess(request);
                    test.assertEqual(HttpMethod.GET, request.getValue().getMethod());
                    test.assertEqual(URL.parse("https://www.example.com").getValue(), request.getValue().getUrl());
                    test.assertEqual(0, request.getValue().getHeaders().getCount());
                    test.assertNull(request.getValue().getBody());
                });

                runner.test("with non-empty headers", (Test test) ->
                {
                    final Iterable<HttpHeader> headers = Array.fromValues(new HttpHeader[]
                    {
                        HttpHeader.create("A", "B").getValue()
                    });
                    final Result<HttpRequest> request = HttpRequest.create(HttpMethod.GET, "https://www.example.com", headers, 0, null);
                    test.assertSuccess(request);
                    test.assertEqual(HttpMethod.GET, request.getValue().getMethod());
                    test.assertEqual(URL.parse("https://www.example.com").getValue(), request.getValue().getUrl());
                    test.assertEqual(headers, request.getValue().getHeaders());
                    test.assertNull(request.getValue().getBody());
                });

                runner.test("with negative contentLength", (Test test) ->
                {
                    final Result<HttpRequest> request = HttpRequest.create(HttpMethod.GET, "https://www.example.com", null, -10, null);
                    test.assertError(new IllegalArgumentException("contentLength (-10) must be greater than or equal to 0."), request);
                });

                runner.test("with 0 contentLength and non-null body", (Test test) ->
                {
                    final Result<HttpRequest> request = HttpRequest.create(HttpMethod.GET, "https://www.example.com", null, 0, new InMemoryByteStream());
                    test.assertError(new IllegalArgumentException("If contentLength is 0, then body must be null."), request);
                });

                runner.test("with 5 contentLength and null body", (Test test) ->
                {
                    final Result<HttpRequest> request = HttpRequest.create(HttpMethod.GET, "https://www.example.com", null, 5, null);
                    test.assertError(new IllegalArgumentException("If contentLength is greater than 0, then body must be non-null."), request);
                });

                runner.test("with 5 contentLength and non-null body", (Test test) ->
                {
                    final Result<HttpRequest> request = HttpRequest.create(HttpMethod.GET, "https://www.example.com", null, 5, new InMemoryByteStream());
                    test.assertSuccess(request);
                    test.assertEqual(HttpMethod.GET, request.getValue().getMethod());
                    test.assertEqual(URL.parse("https://www.example.com").getValue(), request.getValue().getUrl());
                    test.assertEqual(
                        Array.fromValues(new HttpHeader[]
                        {
                            HttpHeader.create("Content-Length", "5").getValue()
                        }),
                        request.getValue().getHeaders());
                    test.assertNotNull(request.getValue().getBody());
                });
            });

            runner.testGroup("setMethod(HttpMethod)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final HttpRequest request = HttpRequest.create(HttpMethod.GET, "https://www.example.com").getValue();
                    test.assertError(new IllegalArgumentException("method cannot be null."), request.setMethod(null));
                    test.assertEqual(HttpMethod.GET, request.getMethod());
                });

                runner.test("with POST", (Test test) ->
                {
                    final HttpRequest request = HttpRequest.create(HttpMethod.GET, "https://www.example.com").getValue();
                    test.assertSuccess(true, request.setMethod(HttpMethod.POST));
                    test.assertEqual(HttpMethod.POST, request.getMethod());
                });
            });

            runner.testGroup("setUrl(String)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final HttpRequest request = HttpRequest.create(HttpMethod.GET, "https://www.example.com").getValue();
                    test.assertError(new IllegalArgumentException("urlString cannot be null."), request.setUrl((String)null));
                    test.assertEqual(URL.parse("https://www.example.com").getValue(), request.getUrl());
                });

                runner.test("with empty", (Test test) ->
                {
                    final HttpRequest request = HttpRequest.create(HttpMethod.GET, "https://www.example.com").getValue();
                    test.assertError(new IllegalArgumentException("urlString cannot be empty."), request.setUrl(""));
                    test.assertEqual(URL.parse("https://www.example.com").getValue(), request.getUrl());
                });

                runner.test("with invalid URL", (Test test) ->
                {
                    final HttpRequest request = HttpRequest.create(HttpMethod.GET, "https://www.example.com").getValue();
                    test.assertError(new IllegalArgumentException("A URL must begin with either a scheme (such as \"http\") or a host (such as \"www.example.com\"), not \"'\"."), request.setUrl("I'm not a valid url"));
                    test.assertEqual(URL.parse("https://www.example.com").getValue(), request.getUrl());
                });

                runner.test("with valid URL", (Test test) ->
                {
                    final HttpRequest request = HttpRequest.create(HttpMethod.GET, "https://www.example.com").getValue();
                    test.assertSuccess(true, request.setUrl("http://www.google.com"));
                    test.assertEqual(URL.parse("http://www.google.com").getValue(), request.getUrl());
                });
            });

            runner.testGroup("setBody(String)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final HttpRequest request = HttpRequest.create(HttpMethod.GET, "https://www.example.com").getValue();
                    request.setBody((String)null);
                    test.assertNull(request.getBody());
                });

                runner.test("with empty", (Test test) ->
                {
                    final HttpRequest request = HttpRequest.create(HttpMethod.GET, "https://www.example.com").getValue();
                    request.setBody("");
                    test.assertNull(request.getBody());
                });

                runner.test("with non-empty", (Test test) ->
                {
                    final HttpRequest request = HttpRequest.create(HttpMethod.GET, "https://www.example.com").getValue();
                    request.setBody("hello");
                    test.assertSuccess("hello", request.getBody().asLineReadStream().getValue().readLine());
                });
            });

            runner.testGroup("setBody(byte[])", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final HttpRequest request = HttpRequest.create(HttpMethod.GET, "https://www.example.com").getValue();
                    request.setBody((byte[])null);
                    test.assertNull(request.getBody());
                });

                runner.test("with empty", (Test test) ->
                {
                    final HttpRequest request = HttpRequest.create(HttpMethod.GET, "https://www.example.com").getValue();
                    request.setBody(new byte[0]);
                    test.assertNull(request.getBody());
                });

                runner.test("with non-empty", (Test test) ->
                {
                    final HttpRequest request = HttpRequest.create(HttpMethod.GET, "https://www.example.com").getValue();
                    request.setBody(new byte[] { 0, 1, 2, 3, 4 });
                    test.assertSuccess(new byte[] { 0, 1, 2, 3, 4 }, request.getBody().readAllBytes());
                });
            });

            runner.testGroup("setBody(int,ByteReadStream)", () ->
            {
                runner.test("with negative contentLength", (Test test) ->
                {
                    final HttpRequest request = HttpRequest.create(HttpMethod.POST, "https://www.example.com").getValue();
                    test.assertError(new IllegalArgumentException("contentLength must be greater than or equal to 0."), request.setBody(-1, null));
                    test.assertEqual(0, request.getContentLength());
                    test.assertNull(request.getBody());
                    test.assertError(new KeyNotFoundException("Content-Length"), request.getHeaders().get("Content-Length"));
                });

                runner.test("with 0 contentLength and null body", (Test test) ->
                {
                    final HttpRequest request = HttpRequest.create(HttpMethod.POST, "https://www.example.com").getValue();
                    test.assertSuccess(true, request.setBody(0, null));
                    test.assertEqual(0, request.getContentLength());
                    test.assertNull(request.getBody());
                    test.assertError(new KeyNotFoundException("Content-Length"), request.getHeaders().get("Content-Length"));
                });

                runner.test("with 0 contentLength and non-null body", (Test test) ->
                {
                    final HttpRequest request = HttpRequest.create(HttpMethod.POST, "https://www.example.com").getValue();
                    test.assertError(new IllegalArgumentException("If contentLength is 0, then body must be null."), request.setBody(0, new InMemoryByteStream()));
                    test.assertEqual(0, request.getContentLength());
                    test.assertNull(request.getBody());
                    test.assertError(new KeyNotFoundException("Content-Length"), request.getHeaders().get("Content-Length"));
                });

                runner.test("with 3 contentLength and null body", (Test test) ->
                {
                    final HttpRequest request = HttpRequest.create(HttpMethod.POST, "https://www.example.com").getValue();
                    test.assertError(new IllegalArgumentException("If contentLength is greater than 0, then body must be not null."), request.setBody(3, null));
                    test.assertEqual(0, request.getContentLength());
                    test.assertNull(request.getBody());
                    test.assertError(new KeyNotFoundException("Content-Length"), request.getHeaders().get("Content-Length"));
                });

                runner.test("with 3 contentLength and non-null body", (Test test) ->
                {
                    final HttpRequest request = HttpRequest.create(HttpMethod.POST, "https://www.example.com").getValue();
                    test.assertSuccess(true, request.setBody(3, new InMemoryByteStream(new byte[] { 0, 1, 2 })));
                    test.assertEqual(3, request.getContentLength());
                    test.assertNotNull(request.getBody());
                    test.assertSuccess("3", request.getHeaders().getValue("Content-Length"));
                });
            });
        });
    }
}
