package qub;

import java.io.IOException;

public interface TCPServer extends Disposable
{
    void setExceptionHandler(Action1<IOException> exceptionHandler);

    Result<TCPClient> accept();

    AsyncFunction<TCPClient> acceptAsync();
}
