package qub;

public class StringIterator extends IteratorBase<Character>
{
    private final String text;
    private final int textLength;
    private boolean hasStarted;
    private int index;

    public StringIterator(String text)
    {
        this.text = text;
        textLength = text == null ? 0 : text.length();
    }

    @Override
    public boolean hasStarted()
    {
        return hasStarted;
    }

    @Override
    public boolean hasCurrent()
    {
        return hasStarted && index < textLength;
    }

    @Override
    public Character getCurrent()
    {
        return hasCurrent() ? text.charAt(index) : null;
    }

    @Override
    public boolean next()
    {
        if (!hasStarted)
        {
            hasStarted = true;
        }
        else if (index < textLength)
        {
            ++index;
        }

        return index < textLength;
    }
}
