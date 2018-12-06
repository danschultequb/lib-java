package qub;

/**
 * A single state within a DFA.
 */
public class State
{
    private final String name;

    /**
     * Create a new State with the provided name.
     * @param name The name of this new State.
     */
    public State(String name)
    {
        PreCondition.assertNotNullAndNotEmpty(name, "name");

        this.name = name;
    }

    /**
     * Get the name of this State.
     * @return The name of this State.
     */
    public String getName()
    {
        return name;
    }
}
