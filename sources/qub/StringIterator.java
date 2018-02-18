package qub;

/**
 * A SeekableIterator that iterates over the characters in a String.
 */
public class StringIterator implements SeekableIterator<Character>
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

    @Override
    public int getCurrentIndex()
    {
        return index;
    }

    @Override
    public void setCurrentIndex(int index)
    {
        this.index = (index < 0 ? 0 : (index > textLength ? textLength : index));
        this.hasStarted = true;
    }
}
