package qub;

public class JSONWriteStream implements Stream
{
    private final LineWriteStream writeStream;

    public JSONWriteStream(LineWriteStream writeStream)
    {
        this.writeStream = writeStream;
    }

    void writeColon()
    {
        writeStream.write(':');
    }

    void writeComma()
    {
        writeStream.write(',');
    }

    void writeLeftCurlyBracket()
    {
        writeStream.write('{');
    }

    void writeRightCurlyBracket()
    {
        writeStream.write('}');
    }

    void writeLeftSquareBracket()
    {
        writeStream.write('[');
    }

    void writeRightSquareBracket()
    {
        writeStream.write(']');
    }

    public void writeBoolean(boolean value)
    {
        writeStream.write(value ? "true" : "false");
    }

    public void writeNull()
    {
        writeStream.write("null");
    }

    public void writeNumber(long value)
    {
        writeStream.write(Long.toString(value));
    }

    public void writeNumber(double value)
    {
        writeStream.write(Double.toString(value));
    }

    public void writeQuotedString(String unquotedText)
    {
        writeQuotedString('\"', unquotedText);
    }

    public void writeQuotedString(char quoteCharacter, String unquotedText)
    {
        writeStream.write(quoteCharacter);
        if (unquotedText != null && !unquotedText.isEmpty())
        {
            writeStream.write(unquotedText);
        }
        writeStream.write(quoteCharacter);
    }

    public void writeObject()
    {
        writeObject(null);
    }

    public void writeObject(Action1<JSONObjectWriteStream> writeArrayAction)
    {
        writeLeftCurlyBracket();
        if (writeArrayAction != null)
        {
            writeArrayAction.run(new JSONObjectWriteStream(this));
        }
        writeRightCurlyBracket();
    }

    public void writeArray()
    {
        writeArray(null);
    }

    public void writeArray(Action1<JSONArrayWriteStream> writeArrayAction)
    {
        writeLeftSquareBracket();
        if (writeArrayAction != null)
        {
            writeArrayAction.run(new JSONArrayWriteStream(this));
        }
        writeRightSquareBracket();
    }

    @Override
    public boolean isOpen()
    {
        return writeStream.isOpen();
    }

    @Override
    public void close()
    {
        writeStream.close();
    }
}
