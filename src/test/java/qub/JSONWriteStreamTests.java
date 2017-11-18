package qub;

import org.junit.Test;

import static org.junit.Assert.*;

public class JSONWriteStreamTests
{
    @Test
    public void constructor()
    {
        final InMemoryLineWriteStream inMemoryWriteStream = new InMemoryLineWriteStream();
        final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);
        assertTrue(writeStream.isOpen());
    }

    @Test
    public void close()
    {
        final InMemoryLineWriteStream inMemoryWriteStream = new InMemoryLineWriteStream();
        final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);

        writeStream.close();
        assertFalse(writeStream.isOpen());
        assertFalse(inMemoryWriteStream.isOpen());

        writeStream.close();
        assertFalse(writeStream.isOpen());
        assertFalse(inMemoryWriteStream.isOpen());
    }

    @Test
    public void writeBooleanWithFalse()
    {
        final InMemoryLineWriteStream inMemoryWriteStream = new InMemoryLineWriteStream();
        final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);
        writeStream.writeBoolean(false);
        assertEquals("false", inMemoryWriteStream.getText());
    }

    @Test
    public void writeBooleanWithTrue()
    {
        final InMemoryLineWriteStream inMemoryWriteStream = new InMemoryLineWriteStream();
        final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);
        writeStream.writeBoolean(true);
        assertEquals("true", inMemoryWriteStream.getText());
    }

    @Test
    public void writeNull()
    {
        final InMemoryLineWriteStream inMemoryWriteStream = new InMemoryLineWriteStream();
        final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);
        writeStream.writeNull();
        assertEquals("null", inMemoryWriteStream.getText());
    }

    @Test
    public void writeNumberWithNegativeDouble()
    {
        final InMemoryLineWriteStream inMemoryWriteStream = new InMemoryLineWriteStream();
        final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);
        writeStream.writeNumber(-12.3);
        assertEquals("-12.3", inMemoryWriteStream.getText());
    }

    @Test
    public void writeNumberWithNegativeInteger()
    {
        final InMemoryLineWriteStream inMemoryWriteStream = new InMemoryLineWriteStream();
        final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);
        writeStream.writeNumber(-123);
        assertEquals("-123", inMemoryWriteStream.getText());
    }

    @Test
    public void writeNumberWithNegativeZero()
    {
        final InMemoryLineWriteStream inMemoryWriteStream = new InMemoryLineWriteStream();
        final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);
        writeStream.writeNumber(-0);
        assertEquals("0", inMemoryWriteStream.getText());
    }

    @Test
    public void writeNumberWithPositiveZero()
    {
        final InMemoryLineWriteStream inMemoryWriteStream = new InMemoryLineWriteStream();
        final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);
        writeStream.writeNumber(0);
        assertEquals("0", inMemoryWriteStream.getText());
    }

    @Test
    public void writeNumberWithPositiveInteger()
    {
        final InMemoryLineWriteStream inMemoryWriteStream = new InMemoryLineWriteStream();
        final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);
        writeStream.writeNumber(1234567);
        assertEquals("1234567", inMemoryWriteStream.getText());
    }

    @Test
    public void writeNumberWithPositiveDouble()
    {
        final InMemoryLineWriteStream inMemoryWriteStream = new InMemoryLineWriteStream();
        final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);
        writeStream.writeNumber(1234.567);
        assertEquals("1234.567", inMemoryWriteStream.getText());
    }

    @Test
    public void writeQuotedStringWithNull()
    {
        final InMemoryLineWriteStream inMemoryWriteStream = new InMemoryLineWriteStream();
        final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);
        writeStream.writeQuotedString(null);
        assertEquals("\"\"", inMemoryWriteStream.getText());
    }

    @Test
    public void writeQuotedStringWithEmpty()
    {
        final InMemoryLineWriteStream inMemoryWriteStream = new InMemoryLineWriteStream();
        final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);
        writeStream.writeQuotedString("");
        assertEquals("\"\"", inMemoryWriteStream.getText());
    }

    @Test
    public void writeQuotedStringWithNonEmpty()
    {
        final InMemoryLineWriteStream inMemoryWriteStream = new InMemoryLineWriteStream();
        final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);
        writeStream.writeQuotedString("test");
        assertEquals("\"test\"", inMemoryWriteStream.getText());
    }

    @Test
    public void writeObjectWithNoAction()
    {
        final InMemoryLineWriteStream inMemoryWriteStream = new InMemoryLineWriteStream();
        final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);
        writeStream.writeObject();
        assertEquals("{}", inMemoryWriteStream.getText());
    }

    @Test
    public void writeObjectWithNullAction()
    {
        final InMemoryLineWriteStream inMemoryWriteStream = new InMemoryLineWriteStream();
        final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);
        writeStream.writeObject(null);
        assertEquals("{}", inMemoryWriteStream.getText());
    }

    @Test
    public void writeObjectWithEmptyAction()
    {
        final InMemoryLineWriteStream inMemoryWriteStream = new InMemoryLineWriteStream();
        final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);
        writeStream.writeObject(new Action1<JSONObjectWriteStream>()
        {
            @Override
            public void run(JSONObjectWriteStream objectWriteStream)
            {
            }
        });
        assertEquals("{}", inMemoryWriteStream.getText());
    }

    @Test
    public void writeObjectWithNullProperty()
    {
        final InMemoryLineWriteStream inMemoryWriteStream = new InMemoryLineWriteStream();
        final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);
        writeStream.writeObject(new Action1<JSONObjectWriteStream>()
        {
            @Override
            public void run(JSONObjectWriteStream objectWriteStream)
            {
                objectWriteStream.writeNullProperty("apples");
            }
        });
        assertEquals("{\"apples\":null}", inMemoryWriteStream.getText());
    }

    @Test
    public void writeObjectWithBooleanProperties()
    {
        final InMemoryLineWriteStream inMemoryWriteStream = new InMemoryLineWriteStream();
        final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);
        writeStream.writeObject(new Action1<JSONObjectWriteStream>()
        {
            @Override
            public void run(JSONObjectWriteStream objectWriteStream)
            {
                objectWriteStream.writeBooleanProperty("apples", false);
                objectWriteStream.writeBooleanProperty("oranges", true);
            }
        });
        assertEquals("{\"apples\":false,\"oranges\":true}", inMemoryWriteStream.getText());
    }

    @Test
    public void writeObjectWithQuotedStringProperty()
    {
        final InMemoryLineWriteStream inMemoryWriteStream = new InMemoryLineWriteStream();
        final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);
        writeStream.writeObject(new Action1<JSONObjectWriteStream>()
        {
            @Override
            public void run(JSONObjectWriteStream objectWriteStream)
            {
                objectWriteStream.writeQuotedStringProperty("apples", "oranges");
            }
        });
        assertEquals("{\"apples\":\"oranges\"}", inMemoryWriteStream.getText());
    }

    @Test
    public void writeObjectWithIntegerProperty()
    {
        final InMemoryLineWriteStream inMemoryWriteStream = new InMemoryLineWriteStream();
        final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);
        writeStream.writeObject(new Action1<JSONObjectWriteStream>()
        {
            @Override
            public void run(JSONObjectWriteStream objectWriteStream)
            {
                objectWriteStream.writeNumberProperty("apples", 15);
            }
        });
        assertEquals("{\"apples\":15}", inMemoryWriteStream.getText());
    }

    @Test
    public void writeObjectWithDoubleProperty()
    {
        final InMemoryLineWriteStream inMemoryWriteStream = new InMemoryLineWriteStream();
        final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);
        writeStream.writeObject(new Action1<JSONObjectWriteStream>()
        {
            @Override
            public void run(JSONObjectWriteStream objectWriteStream)
            {
                objectWriteStream.writeNumberProperty("apples", 15.5);
            }
        });
        assertEquals("{\"apples\":15.5}", inMemoryWriteStream.getText());
    }

    @Test
    public void writeObjectWithObjectProperty()
    {
        final InMemoryLineWriteStream inMemoryWriteStream = new InMemoryLineWriteStream();
        final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);
        writeStream.writeObject(new Action1<JSONObjectWriteStream>()
        {
            @Override
            public void run(JSONObjectWriteStream objectWriteStream)
            {
                objectWriteStream.writeObjectProperty("apples", new Action1<JSONObjectWriteStream>()
                {
                    @Override
                    public void run(JSONObjectWriteStream arg1)
                    {
                    }
                });
            }
        });
        assertEquals("{\"apples\":{}}", inMemoryWriteStream.getText());
    }

    @Test
    public void writeObjectWithArrayProperty()
    {
        final InMemoryLineWriteStream inMemoryWriteStream = new InMemoryLineWriteStream();
        final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);
        writeStream.writeObject(new Action1<JSONObjectWriteStream>()
        {
            @Override
            public void run(JSONObjectWriteStream objectWriteStream)
            {
                objectWriteStream.writeArrayProperty("apples", new Action1<JSONArrayWriteStream>()
                {
                    @Override
                    public void run(JSONArrayWriteStream arg1)
                    {
                    }
                });
            }
        });
        assertEquals("{\"apples\":[]}", inMemoryWriteStream.getText());
    }

    @Test
    public void writeArrayWithNoAction()
    {
        final InMemoryLineWriteStream inMemoryWriteStream = new InMemoryLineWriteStream();
        final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);
        writeStream.writeArray();
        assertEquals("[]", inMemoryWriteStream.getText());
    }

    @Test
    public void writeArrayWithNullAction()
    {
        final InMemoryLineWriteStream inMemoryWriteStream = new InMemoryLineWriteStream();
        final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);
        writeStream.writeArray(null);
        assertEquals("[]", inMemoryWriteStream.getText());
    }

    @Test
    public void writeArrayWithEmptyAction()
    {
        final InMemoryLineWriteStream inMemoryWriteStream = new InMemoryLineWriteStream();
        final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);
        writeStream.writeArray(new Action1<JSONArrayWriteStream>()
        {
            @Override
            public void run(JSONArrayWriteStream arrayWriteStream)
            {
            }
        });
        assertEquals("[]", inMemoryWriteStream.getText());
    }

    @Test
    public void writeArrayWithNullElement()
    {
        final InMemoryLineWriteStream inMemoryWriteStream = new InMemoryLineWriteStream();
        final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);
        writeStream.writeArray(new Action1<JSONArrayWriteStream>()
        {
            @Override
            public void run(JSONArrayWriteStream arrayWriteStream)
            {
                arrayWriteStream.writeNull();
            }
        });
        assertEquals("[null]", inMemoryWriteStream.getText());
    }

    @Test
    public void writeArrayWithBooleanElements()
    {
        final InMemoryLineWriteStream inMemoryWriteStream = new InMemoryLineWriteStream();
        final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);
        writeStream.writeArray(new Action1<JSONArrayWriteStream>()
        {
            @Override
            public void run(JSONArrayWriteStream arrayWriteStream)
            {
                arrayWriteStream.writeBoolean(false);
                arrayWriteStream.writeBoolean(true);
            }
        });
        assertEquals("[false,true]", inMemoryWriteStream.getText());
    }

    @Test
    public void writeArrayWithQuotedStringElement()
    {
        final InMemoryLineWriteStream inMemoryWriteStream = new InMemoryLineWriteStream();
        final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);
        writeStream.writeArray(new Action1<JSONArrayWriteStream>()
        {
            @Override
            public void run(JSONArrayWriteStream arrayWriteStream)
            {
                arrayWriteStream.writeQuotedString("oranges");
            }
        });
        assertEquals("[\"oranges\"]", inMemoryWriteStream.getText());
    }

    @Test
    public void writeArrayWithIntegerElement()
    {
        final InMemoryLineWriteStream inMemoryWriteStream = new InMemoryLineWriteStream();
        final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);
        writeStream.writeArray(new Action1<JSONArrayWriteStream>()
        {
            @Override
            public void run(JSONArrayWriteStream arrayWriteStream)
            {
                arrayWriteStream.writeNumber(15);
            }
        });
        assertEquals("[15]", inMemoryWriteStream.getText());
    }

    @Test
    public void writeArrayWithDoubleElement()
    {
        final InMemoryLineWriteStream inMemoryWriteStream = new InMemoryLineWriteStream();
        final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);
        writeStream.writeArray(new Action1<JSONArrayWriteStream>()
        {
            @Override
            public void run(JSONArrayWriteStream arrayWriteStream)
            {
                arrayWriteStream.writeNumber(15.5);
            }
        });
        assertEquals("[15.5]", inMemoryWriteStream.getText());
    }

    @Test
    public void writeArrayWithObjectElement()
    {
        final InMemoryLineWriteStream inMemoryWriteStream = new InMemoryLineWriteStream();
        final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);
        writeStream.writeArray(new Action1<JSONArrayWriteStream>()
        {
            @Override
            public void run(JSONArrayWriteStream arrayWriteStream)
            {
                arrayWriteStream.writeObject(new Action1<JSONObjectWriteStream>()
                {
                    @Override
                    public void run(JSONObjectWriteStream arg1)
                    {
                    }
                });
            }
        });
        assertEquals("[{}]", inMemoryWriteStream.getText());
    }

    @Test
    public void writeArrayWithArrayElement()
    {
        final InMemoryLineWriteStream inMemoryWriteStream = new InMemoryLineWriteStream();
        final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);
        writeStream.writeArray(new Action1<JSONArrayWriteStream>()
        {
            @Override
            public void run(JSONArrayWriteStream arrayWriteStream)
            {
                arrayWriteStream.writeArray(new Action1<JSONArrayWriteStream>()
                {
                    @Override
                    public void run(JSONArrayWriteStream arg1)
                    {
                    }
                });
            }
        });
        assertEquals("[[]]", inMemoryWriteStream.getText());
    }
}
