package qub;

public class LockedCharacterToByteWriteStream implements CharacterToByteWriteStream
{
    private final Locked<CharacterToByteWriteStream> innerStream;

    private LockedCharacterToByteWriteStream(CharacterToByteWriteStream innerStream)
    {
        PreCondition.assertNotNull(innerStream, "innerStream");

        this.innerStream = Locked.create(innerStream);
    }

    public static LockedCharacterToByteWriteStream create(CharacterToByteWriteStream innerStream)
    {
        return new LockedCharacterToByteWriteStream(innerStream);
    }

    @Override
    public Result<Integer> write(byte toWrite)
    {
        return Result.create(() ->
        {
            return this.innerStream.unlock(stream ->
            {
                return stream.write(toWrite).await();
            });
        });
    }

    @Override
    public Result<Integer> write(byte[] toWrite)
    {
        return Result.create(() ->
        {
            return this.innerStream.unlock(stream ->
            {
                return stream.write(toWrite).await();
            });
        });
    }

    @Override
    public Result<Integer> write(byte[] toWrite, int startIndex, int length)
    {
        return Result.create(() ->
        {
            return this.innerStream.unlock(stream ->
            {
                return stream.write(toWrite, startIndex, length).await();
            });
        });
    }

    @Override
    public Result<Integer> writeAll(byte[] toWrite)
    {
        return Result.create(() ->
        {
            return this.innerStream.unlock(stream ->
            {
                return stream.writeAll(toWrite).await();
            });
        });
    }

    @Override
    public Result<Integer> writeAll(byte[] toWrite, int startIndex, int length)
    {
        return Result.create(() ->
        {
            return this.innerStream.unlock(stream ->
            {
                return stream.writeAll(toWrite, startIndex, length).await();
            });
        });
    }

    @Override
    public Result<Long> writeAll(ByteReadStream byteReadStream)
    {
        return Result.create(() ->
        {
            return this.innerStream.unlock(stream ->
            {
                return stream.writeAll(byteReadStream).await();
            });
        });
    }

    @Override
    public Result<Long> writeAll(ByteReadStream byteReadStream, int initialBufferCapacity)
    {
        return Result.create(() ->
        {
            return this.innerStream.unlock(stream ->
            {
                return stream.writeAll(byteReadStream, initialBufferCapacity).await();
            });
        });
    }

    @Override
    public CharacterEncoding getCharacterEncoding()
    {
        return this.innerStream.unlock(CharacterToByteWriteStream::getCharacterEncoding);
    }

    @Override
    public LockedCharacterToByteWriteStream setCharacterEncoding(CharacterEncoding characterEncoding)
    {
        PreCondition.assertNotNull(characterEncoding, "characterEncoding");

        this.innerStream.unlock(stream ->
        {
            stream.setCharacterEncoding(characterEncoding);
        });

        return this;
    }

    @Override
    public String getNewLine()
    {
        return this.innerStream.unlock(CharacterWriteStream::getNewLine);
    }

    @Override
    public LockedCharacterToByteWriteStream setNewLine(char newLine)
    {
        this.innerStream.unlock(stream ->
        {
            stream.setNewLine(newLine);
        });

        return this;
    }

    @Override
    public LockedCharacterToByteWriteStream setNewLine(char[] newLine)
    {
        PreCondition.assertNotNull(newLine, "newLine");

        this.innerStream.unlock(stream ->
        {
            stream.setNewLine(newLine);
        });

        return this;
    }

    @Override
    public LockedCharacterToByteWriteStream setNewLine(String newLine)
    {
        PreCondition.assertNotNull(newLine, "newLine");

        this.innerStream.unlock(stream ->
        {
            stream.setNewLine(newLine);
        });

        return this;
    }

    @Override
    public Result<Integer> write(char toWrite)
    {
        return Result.create(() ->
        {
            return this.innerStream.unlock(stream ->
            {
                return stream.write(toWrite).await();
            });
        });
    }

    @Override
    public Result<Integer> write(char[] toWrite)
    {
        return Result.create(() ->
        {
            return this.innerStream.unlock(stream ->
            {
                return stream.write(toWrite).await();
            });
        });
    }

    @Override
    public Result<Integer> write(char[] toWrite, int startIndex, int length)
    {
        return Result.create(() ->
        {
            return this.innerStream.unlock(stream ->
            {
                return stream.write(toWrite, startIndex, length).await();
            });
        });
    }

    @Override
    public Result<Integer> write(String toWrite, Object... formattedStringArguments)
    {
        return Result.create(() ->
        {
            return this.innerStream.unlock(stream ->
            {
                return stream.write(toWrite, formattedStringArguments).await();
            });
        });
    }

    @Override
    public Result<Integer> writeLine()
    {
        return Result.create(() ->
        {
            return this.innerStream.unlock(stream ->
            {
                return stream.writeLine().await();
            });
        });
    }

    @Override
    public Result<Integer> writeLine(String toWrite, Object... formattedStringArguments)
    {
        return Result.create(() ->
        {
            return this.innerStream.unlock(stream ->
            {
                return stream.writeLine(toWrite, formattedStringArguments).await();
            });
        });
    }

    @Override
    public Result<Integer> writeLines(String... lines)
    {
        return Result.create(() ->
        {
            return this.innerStream.unlock(stream ->
            {
                return stream.writeLines(lines).await();
            });
        });
    }

    @Override
    public Result<Integer> writeLines(Iterable<String> lines)
    {
        return Result.create(() ->
        {
            return this.innerStream.unlock(stream ->
            {
                return stream.writeLines(lines).await();
            });
        });
    }

    @Override
    public void close()
    {
        this.innerStream.unlock(Disposable::close);
    }

    @Override
    public boolean isDisposed()
    {
        return this.innerStream.unlock(Disposable::isDisposed);
    }

    @Override
    public Result<Boolean> dispose()
    {
        return Result.create(() ->
        {
            return this.innerStream.unlock(stream ->
            {
                return stream.dispose().await();
            });
        });
    }
}
