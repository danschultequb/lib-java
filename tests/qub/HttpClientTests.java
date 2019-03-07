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

                runner.test("with unknown host", runner.skip(!runner.hasNetworkConnection().await()), (Test test) ->
                {
                    final HttpClient httpClient = creator.run(test);
                    final MutableHttpRequest httpRequest = new MutableHttpRequest(HttpMethod.GET, URL.parse("http://www.idontexistbecauseimnotagoodurl.com").await());
                    httpClient.send(httpRequest)
                        .then((HttpResponse response) ->
                        {
                            test.fail("Expected a java.net.UnknownHostException to be thrown.");
                        })
                        .catchError(java.net.UnknownHostException.class, (java.net.UnknownHostException error) ->
                        {
                            test.assertEqual("www.idontexistbecauseimnotagoodurl.com", error.getMessage());
                        })
                        .await();
                });

                runner.test("with GET request to www.example.com", runner.skip(!runner.hasNetworkConnection().await()), (Test test) ->
                {
                    final HttpClient httpClient = creator.run(test);
                    final MutableHttpRequest httpRequest = new MutableHttpRequest(HttpMethod.GET, URL.parse("http://www.example.com").await());

                    final HttpResponse httpResponse = httpClient.send(httpRequest).awaitError();
                    test.assertEqual("HTTP/1.1", httpResponse.getHTTPVersion());
                    test.assertEqual(200, httpResponse.getStatusCode());
                    test.assertEqual("OK", httpResponse.getReasonPhrase());
                    test.assertNotNull(httpResponse.getHeaders());
                    final String contentLength = httpResponse.getHeaders().getValue("content-length").awaitError();
                    test.assertOneOf(new String[] { "1164", "1270" }, contentLength);
                    test.assertNotNull(httpResponse.getBody());
                    final String bodyString = httpResponse.getBody().asCharacterReadStream().readEntireString().awaitError();
                    test.assertNotNull(bodyString);
                    test.assertStartsWith(bodyString, "<!doctype html>", CharacterComparer.CaseInsensitive);
                    test.assertContains(bodyString, "<div>");
                    test.assertContains(bodyString, "<h1>Example Domain</h1>");
                    test.assertContains(bodyString, "</div>");
                });

                runner.test("with GET request to http://www.treasurydirect.gov/TA_WS/securities/auctioned?format=json&type=Bill", runner.skip(!runner.hasNetworkConnection().await()), (Test test) ->
                {
                    final HttpClient httpClient = creator.run(test);
                    final MutableHttpRequest httpRequest = new MutableHttpRequest(HttpMethod.GET, URL.parse("http://www.treasurydirect.gov/TA_WS/securities/auctioned?format=json&type=Bill").throwErrorOrGetValue());

                    final HttpResponse httpResponse = httpClient.send(httpRequest).awaitError();
                    test.assertTrue(httpResponse.getHTTPVersion().equals("HTTP/1.0") || httpResponse.getHTTPVersion().equals("HTTP/1.1"));
                    test.assertEqual(302, httpResponse.getStatusCode());
                    test.assertEqual("Found", httpResponse.getReasonPhrase());
                    test.assertNotNull(httpResponse.getHeaders());
                    final String locationHeader = httpResponse.getHeaders().getValue("location").awaitError();
                    test.assertEndsWith(locationHeader, "www.treasurydirect.gov/TA_WS/securities/auctioned?format=json&type=Bill", "Incorrect Location header");
                    test.assertEqual("0", httpResponse.getHeaders().getValue("content-length").awaitError());
                    test.assertNull(httpResponse.getBody());
                });
            });
        });
    }
}
