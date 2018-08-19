package qub;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.Map;

/**
 * An HttpClient implementation that uses the default Java libraries to send HTTP requests.
 */
public class JavaHttpClient implements HttpClient
{
    @Override
    public Result<HttpResponse> send(HttpRequest request)
    {
        Result<HttpResponse> result = Result.notNull(request, "request");
        if (result == null)
        {
            try
            {
                final HttpURLConnection urlConnection = (HttpURLConnection)new java.net.URL(request.getUrl().toString()).openConnection();
                urlConnection.setInstanceFollowRedirects(false);

                urlConnection.setRequestMethod(request.getMethod().toString());

                final HttpHeaders requestHeaders = request.getHeaders();
                if (requestHeaders != null)
                {
                    for (final HttpHeader header : requestHeaders)
                    {
                        urlConnection.setRequestProperty(header.getName(), header.getValue());
                    }
                }

                final ByteReadStream body = request.getBody();
                if (body != null)
                {
                    urlConnection.setDoOutput(true);

                    final ByteWriteStream writeStream = new OutputStreamToByteWriteStream(urlConnection.getOutputStream());
                    writeStream.writeAll(body);
                    body.close();
                    writeStream.close();
                }

                final String httpVersion = "HTTP/1.1";
                final String reasonPhrase = urlConnection.getResponseMessage();
                final int statusCode = urlConnection.getResponseCode();

                final MutableHttpHeaders resultHeaders = new MutableHttpHeaders();
                for (final Map.Entry<String,List<String>> responseHeader : urlConnection.getHeaderFields().entrySet())
                {
                    resultHeaders.set(responseHeader.getKey(), Strings.join(',', responseHeader.getValue()));
                }

                InMemoryByteStream resultBody = new InMemoryByteStream();
                final ByteReadStream responseBody = new InputStreamToByteReadStream((400 <= statusCode && statusCode <= 599) ? urlConnection.getErrorStream() : urlConnection.getInputStream(), null);
                resultBody.writeAll(responseBody);
                responseBody.close();
                resultBody.endOfStream();
                if (resultBody.getCount() == 0)
                {
                    resultBody = null;
                }

                result = Result.success(new HttpResponse(httpVersion, statusCode, reasonPhrase, resultHeaders, resultBody));
            }
            catch (IOException e)
            {
                result = Result.error(e);
            }
        }

        PostCondition.assertNotNull(result, "result");

        return result;
    }
}
