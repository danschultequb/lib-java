package qub;

public class HttpClientTests
{
    public static void test(TestRunner runner, Function1<Test,HttpClient> creator)
    {
        runner.testGroup(HttpClient.class, () ->
        {
            runner.testGroup("send(MutableHttpRequest)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final HttpClient httpClient = creator.run(test);
                    test.assertThrows(() -> httpClient.send(null));
                });

                runner.test("with unknown host", runner.skip(runner.hasNetworkConnection()), (Test test) ->
                {
                    final HttpClient httpClient = creator.run(test);
                    final MutableHttpRequest httpRequest = new MutableHttpRequest(HttpMethod.GET, URL.parse("http://www.idontexistbecauseimnotagoodurl.com").throwErrorOrGetValue());
                    final Result<HttpResponse> httpResponse = httpClient.send(httpRequest);
                    test.assertError(new java.net.UnknownHostException("www.idontexistbecauseimnotagoodurl.com"), httpResponse);
                });

                runner.test("with GET request to www.example.com", runner.skip(runner.hasNetworkConnection()), (Test test) ->
                {
                    final HttpClient httpClient = creator.run(test);
                    final MutableHttpRequest httpRequest = new MutableHttpRequest(HttpMethod.GET, URL.parse("http://www.example.com").throwErrorOrGetValue());

                    final Result<HttpResponse> httpResponseResult = httpClient.send(httpRequest);
                    test.assertSuccess(httpResponseResult);

                    final HttpResponse httpResponse = httpResponseResult.getValue();
                    test.assertEqual("HTTP/1.1", httpResponse.getHTTPVersion());
                    test.assertEqual(200, httpResponse.getStatusCode());
                    test.assertEqual("OK", httpResponse.getReasonPhrase());
                    test.assertNotNull(httpResponse.getHeaders());
                    final Result<String> contentLengthResult = httpResponse.getHeaders().getValue("content-length");
                    test.assertSuccess(contentLengthResult);
                    test.assertOneOf(new String[] { "1164", "1270" }, contentLengthResult.getValue());
                    test.assertNotNull(httpResponse.getBody());
                    final String bodyString = httpResponse.getBody().asCharacterReadStream().readString(3000).getValue();
                    test.assertNotNull(bodyString);
                    test.assertStartsWith(bodyString, "<!doctype html>", CharacterComparer.CaseInsensitive);
                    test.assertContains(bodyString, "<div>");
                    test.assertContains(bodyString, "<h1>Example Domain</h1>");
                    test.assertContains(bodyString, "</div>");
                });

                runner.test("with GET request to http://www.treasurydirect.gov/TA_WS/securities/auctioned?format=json&type=Bill", runner.skip(runner.hasNetworkConnection()), (Test test) ->
                {
                    final HttpClient httpClient = creator.run(test);
                    final MutableHttpRequest httpRequest = new MutableHttpRequest(HttpMethod.GET, URL.parse("http://www.treasurydirect.gov/TA_WS/securities/auctioned?format=json&type=Bill").throwErrorOrGetValue());

                    final Result<HttpResponse> httpResponseResult = httpClient.send(httpRequest);
                    test.assertSuccess(httpResponseResult);

                    final HttpResponse httpResponse = httpResponseResult.getValue();
                    test.assertTrue(httpResponse.getHTTPVersion().equals("HTTP/1.0") || httpResponse.getHTTPVersion().equals("HTTP/1.1"));
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
