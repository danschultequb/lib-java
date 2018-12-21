package qub;

public class TrackedValue
{
    private final State state;
    private boolean isOpen;
    private final StringBuilder value;

    public TrackedValue(State state)
    {
        this(state, true, null);
    }

    public TrackedValue(State state, boolean isOpen, StringBuilder values)
    {
        PreCondition.assertNotNull(state, "state");

        this.state = state;
        this.isOpen = isOpen;
        this.value = new StringBuilder();
        if (!Strings.isNullOrEmpty(values))
        {
            this.value.append(values);
        }
    }

    public State getState()
    {
        return state;
    }

    public TrackedValue add(char value)
    {
        this.value.append(value);
        return this;
    }

    public void close()
    {
        isOpen = false;
    }

    public boolean isOpen()
    {
        return isOpen;
    }

    public TrackedValue clone()
    {
        return new TrackedValue(state, isOpen, value);
    }

    public Iterable<Character> getValues()
    {
        return Strings.iterable(value);
    }
}
