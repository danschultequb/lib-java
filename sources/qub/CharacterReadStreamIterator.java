package qub;

public class CharacterReadStreamIterator implements Iterator<Character>
{
    private final CharacterReadStream characterReadStream;
    private boolean hasStarted;
    private Character current;

    private CharacterReadStreamIterator(CharacterReadStream characterReadStream)
    {
        PreCondition.assertNotNull(characterReadStream, "characterReadStream");

        this.characterReadStream = characterReadStream;
    }

    public static CharacterReadStreamIterator create(CharacterReadStream characterReadStream)
    {
        PreCondition.assertNotNull(characterReadStream, "characterReadStream");

        return new CharacterReadStreamIterator(characterReadStream);
    }

    @Override
    public boolean hasStarted()
    {
        return this.hasStarted;
    }

    @Override
    public boolean hasCurrent()
    {
        return this.current != null;
    }

    @Override
    public Character getCurrent()
    {
        return this.getCurrentChar();
    }

    public char getCurrentChar()
    {
        PreCondition.assertTrue(this.hasCurrent(), "this.hasCurrent()");

        return this.current;
    }

    @Override
    public boolean next()
    {
        this.hasStarted = true;

        this.characterReadStream.readCharacter()
            .then((Character readByte) ->
            {
                this.current = readByte;
            })
            .catchError(EmptyException.class, () ->
            {
                this.current = null;
            })
            .await();

        return this.hasCurrent();
    }
}
