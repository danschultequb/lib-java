package qub;

public class DefaultHttpClient implements HttpClient
{
    private final TCPClient tcpClient;

    private DefaultHttpClient(TCPClient tcpClient)
    {
        this.tcpClient = tcpClient;
    }

    public static Result<DefaultHttpClient> create(TCPClient tcpClient)
    {
        Result<DefaultHttpClient> result;
        if (tcpClient == null)
        {
            result = Result.error(new IllegalArgumentException("tcpClient cannot be null."));
        }
        else
        {
            result = Result.success(new DefaultHttpClient(tcpClient));
        }
        return result;
    }

    @Override
    public Result<HttpResponse> send(HttpRequest request)
    {
        return null;
    }
}
