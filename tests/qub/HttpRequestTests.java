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
                    final Result<HttpRequest> request = HttpRequest.create(HttpMethod.GET, null);
                    test.assertError(new IllegalArgumentException("url cannot be null."), request);
                });

                runner.test("with empty url", (Test test) ->
                {
                    final Result<HttpRequest> request = HttpRequest.create(HttpMethod.GET, "");
                    test.assertError(new IllegalArgumentException("url cannot be empty."), request);
                });

                runner.test("with invalid url", (Test test) ->
                {
                    final Result<HttpRequest> request = HttpRequest.create(HttpMethod.GET, "I'm not a good URL");
                    test.assertSuccess(request);
                    test.assertEqual(HttpMethod.GET, request.getValue().getMethod());
                    test.assertEqual("I'm not a good URL", request.getValue().getUrl());
                    test.assertEqual(0, request.getValue().getHeaders().getCount());
                    test.assertNull(request.getValue().getBody());
                });

                runner.test("with valid HttpMethod and valid url", (Test test) ->
                {
                    final Result<HttpRequest> request = HttpRequest.create(HttpMethod.GET, "https://www.example.com");
                    test.assertSuccess(request);
                    test.assertEqual(HttpMethod.GET, request.getValue().getMethod());
                    test.assertEqual("https://www.example.com", request.getValue().getUrl());
                    test.assertEqual(0, request.getValue().getHeaders().getCount());
                    test.assertNull(request.getValue().getBody());
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
                    test.assertError(new IllegalArgumentException("url cannot be null."), request.setUrl(null));
                    test.assertEqual("https://www.example.com", request.getUrl());
                });

                runner.test("with empty", (Test test) ->
                {
                    final HttpRequest request = HttpRequest.create(HttpMethod.GET, "https://www.example.com").getValue();
                    test.assertError(new IllegalArgumentException("url cannot be empty."), request.setUrl(""));
                    test.assertEqual("https://www.example.com", request.getUrl());
                });

                runner.test("with invalid URL", (Test test) ->
                {
                    final HttpRequest request = HttpRequest.create(HttpMethod.GET, "https://www.example.com").getValue();
                    test.assertSuccess(true, request.setUrl("I'm not a valid url"));
                    test.assertEqual("I'm not a valid url", request.getUrl());
                });

                runner.test("with valid URL", (Test test) ->
                {
                    final HttpRequest request = HttpRequest.create(HttpMethod.GET, "https://www.example.com").getValue();
                    test.assertSuccess(true, request.setUrl("http://www.google.com"));
                    test.assertEqual("http://www.google.com", request.getUrl());
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
                    test.assertEqual("hello", request.getBody().asLineReadStream().readLine());
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
                    test.assertEqual(new byte[] { 0, 1, 2, 3, 4 }, request.getBody().readAllBytes());
                });
            });
        });
    }
}
