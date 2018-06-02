package qub;

public class HttpClientTests
{
    public static void test(TestRunner runner, Function1<Test,HttpClient> creator)
    {
        runner.testGroup(HttpClient.class, () ->
        {
            runner.testGroup("send(HttpRequest)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final HttpClient httpClient = creator.run(test);
                    final Result<HttpResponse> response = httpClient.send(null);
                    test.assertError(new IllegalArgumentException("request cannot be null."), response);
                });

                runner.test("with unknown host", (Test test) ->
                {
                    final HttpClient httpClient = creator.run(test);
                    final HttpRequest httpRequest = HttpRequest.create(HttpMethod.GET, "http://www.idontexistbecauseimnotagoodurl.com").getValue();
                    final Result<HttpResponse> httpResponse = httpClient.send(httpRequest);
                    test.assertError(new java.net.UnknownHostException("www.idontexistbecauseimnotagoodurl.com"), httpResponse);
                });

                runner.test("with GET request to www.example.com", (Test test) ->
                {
                    final HttpClient httpClient = creator.run(test);
                    final HttpRequest httpRequest = HttpRequest.create(HttpMethod.GET, "http://www.example.com").getValue();

                    final Result<HttpResponse> httpResponseResult = httpClient.send(httpRequest);
                    test.assertSuccess(httpResponseResult);

                    final HttpResponse httpResponse = httpResponseResult.getValue();
                    test.assertEqual("HTTP/1.1", httpResponse.getHttpVersion());
                    test.assertEqual(200, httpResponse.getStatusCode());
                    test.assertEqual("OK", httpResponse.getReasonPhrase());
                    test.assertNotNull(httpResponse.getHeaders());
                    test.assertSuccess("1270", httpResponse.getHeaders().getValue("content-length"));
                    test.assertNotNull(httpResponse.getBody());
                    final String bodyString = httpResponse.getBody().asCharacterReadStream().getValue().readString(3000).getValue();
                    test.assertNotNull(bodyString);
                    test.assertTrue(bodyString.startsWith("<!doctype html>"));
                    test.assertTrue(bodyString.contains("<html>"));
                    test.assertTrue(bodyString.contains("<h1>Example Domain</h1>"));
                    test.assertTrue(bodyString.contains("</html>"));
                });

                runner.test("with GET request to http://www.treasurydirect.gov/TA_WS/securities/auctioned?format=json&type=Bill", (Test test) ->
                {
                    final HttpClient httpClient = creator.run(test);
                    final HttpRequest httpRequest = HttpRequest.create(HttpMethod.GET, "http://www.treasurydirect.gov/TA_WS/securities/auctioned?format=json&type=Bill").getValue();

                    final Result<HttpResponse> httpResponseResult = httpClient.send(httpRequest);
                    test.assertSuccess(httpResponseResult);

                    final HttpResponse httpResponse = httpResponseResult.getValue();
                    test.assertTrue(httpResponse.getHttpVersion().equals("HTTP/1.0") || httpResponse.getHttpVersion().equals("HTTP/1.1"));
                    test.assertEqual(302, httpResponse.getStatusCode());
                    test.assertEqual("Found", httpResponse.getReasonPhrase());
                    test.assertNotNull(httpResponse.getHeaders());
                    final Result<String> locationHeader = httpResponse.getHeaders().getValue("location");
                    test.assertSuccess(locationHeader);
                    test.assertEndsWith(locationHeader.getValue(), "www.treasurydirect.gov/TA_WS/securities/auctioned?format=json&type=Bill", "Incorrect Location header");
                    test.assertSuccess("0", httpResponse.getHeaders().getValue("content-length"));
                    test.assertNull(httpResponse.getBody());
                });
            });
        });
    }
}
