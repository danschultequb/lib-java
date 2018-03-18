package qub;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.Socket;

class JavaTCPClient extends DisposableBase implements TCPClient
{
    private final Socket socket;
    private final ByteReadStream socketReadStream;
    private final ByteWriteStream socketWriteStream;

    private JavaTCPClient(Socket socket, ByteReadStream socketReadStream, ByteWriteStream socketWriteStream)
    {
        this.socket = socket;
        this.socketReadStream = socketReadStream;
        this.socketWriteStream = socketWriteStream;
    }

    static Result<TCPClient> create(Socket socket)
    {
        Result<TCPClient> result;

        if (socket == null)
        {
            result = Result.<TCPClient>error(new IllegalArgumentException("socket cannot be null."));
        }
        else
        {
            try
            {
                final ByteReadStream socketReadStream = new InputStreamToByteReadStream(socket.getInputStream());
                final ByteWriteStream socketWriteStream = new OutputStreamToByteWriteStream(socket.getOutputStream());
                final TCPClient tcpClient = new JavaTCPClient(socket, socketReadStream, socketWriteStream);
                result = Result.success(tcpClient);
            }
            catch (IOException e)
            {
                result = Result.error(e);
            }
        }

        return result;
    }

    static Result<TCPClient> create(IPv4Address remoteIPAddress, int remotePort)
    {
        Result<TCPClient> result;

        if (remoteIPAddress == null)
        {
            result = Result.<TCPClient>error(new IllegalArgumentException("remoteIPAddress cannot be null."));
        }
        else if (remotePort <= 0)
        {
            result = Result.<TCPClient>error(new IllegalArgumentException("remotePort must be greater than 0."));
        }
        else
        {
            try
            {
                final byte[] remoteIPAddressBytes = remoteIPAddress.toBytes();
                final InetAddress remoteInetAddress = InetAddress.getByAddress(remoteIPAddressBytes);
                final Socket socket = new Socket(remoteInetAddress, remotePort);
                result = JavaTCPClient.create(socket);
            }
            catch (IOException e)
            {
                result = Result.error(e);
            }
        }

        return result;
    }

    @Override
    public Byte readByte()
    {
        return socketReadStream.readByte();
    }

    @Override
    public byte[] readBytes(int bytesToRead)
    {
        return socketReadStream.readBytes(bytesToRead);
    }

    @Override
    public int readBytes(byte[] outputBytes)
    {
        return socketReadStream.readBytes(outputBytes);
    }

    @Override
    public int readBytes(byte[] outputBytes, int startIndex, int length)
    {
        return socketReadStream.readBytes(outputBytes, startIndex, length);
    }

    @Override
    public InputStream asInputStream()
    {
        return socketReadStream.asInputStream();
    }

    @Override
    public CharacterReadStream asCharacterReadStream()
    {
        return socketReadStream.asCharacterReadStream();
    }

    @Override
    public CharacterReadStream asCharacterReadStream(CharacterEncoding characterEncoding)
    {
        return socketReadStream.asCharacterReadStream(characterEncoding);
    }

    @Override
    public LineReadStream asLineReadStream()
    {
        return socketReadStream.asLineReadStream();
    }

    @Override
    public LineReadStream asLineReadStream(CharacterEncoding characterEncoding)
    {
        return socketReadStream.asLineReadStream(characterEncoding);
    }

    @Override
    public LineReadStream asLineReadStream(boolean includeNewLines)
    {
        return socketReadStream.asLineReadStream(includeNewLines);
    }

    @Override
    public LineReadStream asLineReadStream(CharacterEncoding characterEncoding, boolean includeNewLines)
    {
        return socketReadStream.asLineReadStream(characterEncoding, includeNewLines);
    }

    @Override
    public void setExceptionHandler(Action1<IOException> exceptionHandler)
    {
        socketReadStream.setExceptionHandler(exceptionHandler);
        socketWriteStream.setExceptionHandler(exceptionHandler);
    }

    @Override
    public boolean write(byte toWrite)
    {
        return socketWriteStream.write(toWrite);
    }

    @Override
    public boolean write(byte[] toWrite)
    {
        return socketWriteStream.write(toWrite);
    }

    @Override
    public boolean write(byte[] toWrite, int startIndex, int length)
    {
        return socketWriteStream.write(toWrite, startIndex, length);
    }

    @Override
    public boolean writeAll(ByteReadStream byteReadStream)
    {
        return socketWriteStream.writeAll(byteReadStream);
    }

    @Override
    public CharacterWriteStream asCharacterWriteStream()
    {
        return socketWriteStream.asCharacterWriteStream();
    }

    @Override
    public CharacterWriteStream asCharacterWriteStream(CharacterEncoding characterEncoding)
    {
        return socketWriteStream.asCharacterWriteStream(characterEncoding);
    }

    @Override
    public LineWriteStream asLineWriteStream()
    {
        return socketWriteStream.asLineWriteStream();
    }

    @Override
    public LineWriteStream asLineWriteStream(CharacterEncoding characterEncoding)
    {
        return socketWriteStream.asLineWriteStream(characterEncoding);
    }

    @Override
    public LineWriteStream asLineWriteStream(String lineSeparator)
    {
        return socketWriteStream.asLineWriteStream(lineSeparator);
    }

    @Override
    public LineWriteStream asLineWriteStream(CharacterEncoding characterEncoding, String lineSeparator)
    {
        return socketWriteStream.asLineWriteStream(characterEncoding, lineSeparator);
    }

    @Override
    public void ensureHasStarted()
    {
        socketReadStream.ensureHasStarted();
    }

    @Override
    public boolean hasStarted()
    {
        return socketReadStream.hasStarted();
    }

    @Override
    public boolean hasCurrent()
    {
        return socketReadStream.hasCurrent();
    }

    @Override
    public Byte getCurrent()
    {
        return socketReadStream.getCurrent();
    }

    @Override
    public boolean next()
    {
        return socketReadStream.next();
    }

    @Override
    public Byte takeCurrent()
    {
        return socketReadStream.takeCurrent();
    }

    @Override
    public boolean any()
    {
        return socketReadStream.any();
    }

    @Override
    public int getCount()
    {
        return socketReadStream.getCount();
    }

    @Override
    public Byte first()
    {
        return socketReadStream.first();
    }

    @Override
    public Byte first(Function1<Byte, Boolean> condition)
    {
        return socketReadStream.first(condition);
    }

    @Override
    public Byte last()
    {
        return socketReadStream.last();
    }

    @Override
    public Byte last(Function1<Byte, Boolean> condition)
    {
        return socketReadStream.last(condition);
    }

    @Override
    public boolean contains(Byte value)
    {
        return socketReadStream.contains(value);
    }

    @Override
    public boolean contains(Function1<Byte, Boolean> condition)
    {
        return socketReadStream.contains(condition);
    }

    @Override
    public Iterator<Byte> take(int toTake)
    {
        return socketReadStream.take(toTake);
    }

    @Override
    public Iterator<Byte> skip(int toSkip)
    {
        return socketReadStream.skip(toSkip);
    }

    @Override
    public Iterator<Byte> skipUntil(Function1<Byte, Boolean> condition)
    {
        return socketReadStream.skipUntil(condition);
    }

    @Override
    public Iterator<Byte> where(Function1<Byte, Boolean> condition)
    {
        return socketReadStream.where(condition);
    }

    @Override
    public <U> Iterator<U> map(Function1<Byte, U> conversion)
    {
        return socketReadStream.map(conversion);
    }

    @Override
    public <U> Iterator<U> instanceOf(Class<U> type)
    {
        return socketReadStream.instanceOf(type);
    }

    @Override
    public java.util.Iterator<Byte> iterator()
    {
        return socketReadStream.iterator();
    }

    @Override
    public boolean isDisposed()
    {
        return socket.isClosed();
    }

    @Override
    public Result<Boolean> dispose()
    {
        Result<Boolean> result;
        if (isDisposed())
        {
            result = Result.success(false);
        }
        else
        {
            try
            {
                socket.close();
                result = Result.success(true);
            }
            catch (IOException e)
            {
                result = Result.<Boolean>error(e);
            }
        }
        return result;
    }
}
