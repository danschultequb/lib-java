package qub;

public class JSONObjectBuilder implements Disposable
{
    private final CharacterWriteStream stream;
    private final BasicDisposable disposable;
    private boolean wroteFirstProperty;

    public JSONObjectBuilder(CharacterWriteStream stream)
    {
        PreCondition.assertNotNull(stream, "stream");
        PreCondition.assertFalse(stream.isDisposed(), "stream.isDisposed()");

        this.stream = stream;
        stream.write('{');
        disposable = new BasicDisposable(() -> stream.write('}'));
    }

    private void property(String propertyName, Action0 writePropertyValue)
    {
        PreCondition.assertNotNullAndNotEmpty(propertyName, "propertyName");
        PreCondition.assertNotNull(writePropertyValue, "writePropertyValue");

        if (!wroteFirstProperty)
        {
            wroteFirstProperty = true;
        }
        else
        {
            stream.write(',');
        }

        stream.write(Strings.escapeAndQuote(propertyName));
        stream.write(':');
        writePropertyValue.run();
    }

    private void property(String propertyName, String propertyValue)
    {
        PreCondition.assertNotNullAndNotEmpty(propertyName, "propertyName");
        PreCondition.assertNotNullAndNotEmpty(propertyValue, "propertyValue");

        property(propertyName, () -> stream.write(propertyValue));
    }

    public void booleanProperty(String propertyName, boolean propertyValue)
    {
        PreCondition.assertNotNullAndNotEmpty(propertyName, "propertyName");

        property(propertyName, Booleans.toString(propertyValue));
    }

    public void numberProperty(String propertyName, long propertyValue)
    {
        PreCondition.assertNotNullAndNotEmpty(propertyName, "propertyName");

        property(propertyName, Longs.toString(propertyValue));
    }

    public void numberProperty(String propertyName, double propertyValue)
    {
        PreCondition.assertNotNullAndNotEmpty(propertyName, "propertyName");

        property(propertyName, Doubles.toString(propertyValue));
    }

    public void stringProperty(String propertyName, String propertyValue)
    {
        PreCondition.assertNotNullAndNotEmpty(propertyName, "propertyName");
        PreCondition.assertNotNull(propertyValue, "propertyValue");

        property(propertyName, Strings.escapeAndQuote(propertyValue));
    }

    public void nullProperty(String propertyName)
    {
        PreCondition.assertNotNullAndNotEmpty(propertyName, "propertyName");

        property(propertyName, "null");
    }

    public void objectProperty(String propertyName)
    {
        PreCondition.assertNotNullAndNotEmpty(propertyName, "propertyName");

        objectProperty(propertyName, (JSONObjectBuilder builder) -> {});
    }

    public void objectProperty(String propertyName, Action1<JSONObjectBuilder> action)
    {
        PreCondition.assertNotNullAndNotEmpty(propertyName, "propertyName");
        PreCondition.assertNotNull(action, "action");

        property(propertyName, () ->
        {
            final JSONObjectBuilder objectBuilder = new JSONObjectBuilder(stream);
            action.run(objectBuilder);
            objectBuilder.dispose();
        });
    }

    public void arrayProperty(String propertyName)
    {
        PreCondition.assertNotNullAndNotEmpty(propertyName, "propertyName");

        arrayProperty(propertyName, (JSONArrayBuilder builder) -> {});
    }

    public void arrayProperty(String propertyName, Action1<JSONArrayBuilder> action)
    {
        PreCondition.assertNotNullAndNotEmpty(propertyName, "propertyName");
        PreCondition.assertNotNull(action, "action");

        property(propertyName, () ->
        {
            final JSONArrayBuilder arrayBuilder = new JSONArrayBuilder(stream);
            action.run(arrayBuilder);
            arrayBuilder.dispose();
        });
    }

    public void stringArrayProperty(String propertyName)
    {
        PreCondition.assertNotNullAndNotEmpty(propertyName, "propertyName");

        stringArrayProperty(propertyName, Iterable.empty());
    }

    public void stringArrayProperty(String propertyName, Iterable<String> stringElements)
    {
        PreCondition.assertNotNullAndNotEmpty(propertyName, "propertyName");
        PreCondition.assertNotNull(stringElements, "stringElements");

        arrayProperty(propertyName, (JSONArrayBuilder builder) ->
        {
            for (final String stringElement : stringElements)
            {
                builder.stringElement(stringElement);
            }
        });
    }

    @Override
    public boolean isDisposed()
    {
        return disposable.isDisposed();
    }

    @Override
    public Result<Boolean> dispose()
    {
        return disposable.dispose();
    }
}
