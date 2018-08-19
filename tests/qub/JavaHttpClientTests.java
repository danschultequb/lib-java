package qub;

public class JavaHttpClientTests
{
    private static HttpClient createHttpClient(Test test)
    {
        return new JavaHttpClient(test.getNetwork());
    }

    public static void test(TestRunner runner)
    {
        runner.testGroup(JavaHttpClient.class, () ->
        {
            HttpClientTests.test(runner, JavaHttpClientTests::createHttpClient);

            runner.testGroup("send(HttpRequest)", () ->
            {
                runner.test("with GET request to https://www.treasurydirect.gov/TA_WS/securities/auctioned?format=json&type=Bill", (Test test) ->
                {
                    final HttpClient httpClient = createHttpClient(test);
                    final HttpRequest httpRequest = HttpRequest.create(HttpMethod.GET, "https://www.treasurydirect.gov/TA_WS/securities/auctioned?format=json&type=Bill").getValue();

                    final Result<HttpResponse> httpResponseResult = httpClient.send(httpRequest);
                    test.assertSuccess(httpResponseResult);

                    final HttpResponse httpResponse = httpResponseResult.getValue();
                    test.assertEqual("HTTP/1.1", httpResponse.getHttpVersion());
                    test.assertEqual(200, httpResponse.getStatusCode());
                    test.assertEqual("OK", httpResponse.getReasonPhrase());
                    test.assertNotNull(httpResponse.getHeaders());
                    test.assertSuccess("application/json;charset=UTF-8", httpResponse.getHeaders().getValue("content-type"));
                    test.assertNotNull(httpResponse.getBody());
                    final Result<String> responseBody = CharacterEncoding.UTF_8.decodeAsString(httpResponse.getBody().readAllBytes().getValue());
                    test.assertSuccess(responseBody);
                    final JSONDocument document = JSON.parse(responseBody.getValue());
                    test.assertNotNull(document);
                    test.assertEqual(JSONArray.class, document.getRoot().getClass());
                });
            });
        });
    }
}
