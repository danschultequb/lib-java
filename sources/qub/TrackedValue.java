package qub;

public class TrackedValue
{
    private final State state;
    private boolean isOpen;
    private final CharacterList value;

    public TrackedValue(State state)
    {
        this(state, true, null);
    }

    public TrackedValue(State state, boolean isOpen, CharacterList values)
    {
        PreCondition.assertNotNull(state, "state");

        this.state = state;
        this.isOpen = isOpen;
        this.value = CharacterList.create();
        if (!Iterable.isNullOrEmpty(values))
        {
            this.value.addAll(values);
        }
    }

    public State getState()
    {
        return state;
    }

    public TrackedValue add(char value)
    {
        this.value.add(value);
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
        return this.value;
    }
}
