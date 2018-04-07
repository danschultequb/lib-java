package qub;

public interface TCPServer extends AsyncDisposable
{
    Result<TCPClient> accept();

    AsyncFunction<Result<TCPClient>> acceptAsync();
}
