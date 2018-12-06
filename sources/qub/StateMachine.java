package qub;

/**
 * A Deterministic Finite-state Automaton, also known as a PatternState Machine.
 */
public class StateMachine
{
    private final MutableMap<String,State> states;

    /**
     * Create a new StateMachine.
     */
    public StateMachine()
    {
        this.states = Map.create();
    }

    /**
     * Get the number of states in this StateMachine.
     * @return The number of states in this StateMachine.
     */
    public int getCount()
    {
        return states.getCount();
    }

    /**
     * Create a new state in this StateMachine.
     */
    public Result<State> createState()
    {
        return createState(Integers.toString(getCount() + 1));
    }

    /**
     * Create a new state in this StateMachine with the provided name.
     * @param stateName The name of the new state.
     */
    public Result<State> createState(String stateName)
    {
        PreCondition.assertNotNullAndNotEmpty(stateName, "stateName");

        final String stateKey = getStateKey(stateName);
        return states.get(stateKey)
            .thenResult((State state) -> Result.<State>error(new AlreadyExistsException(stateName, "A state with the name " + Strings.escapeAndQuote(stateName) + " already exists.")))
            .catchErrorResult(NotFoundException.class, () ->
            {
                final State state = new State(stateName);
                states.set(stateKey, state);
                return Result.success(state);
            });
    }

    /**
     * Get the state in this StateMachine with the provided name.
     * @param stateName The name of the state to look for.
     * @return The State with the provided name.
     */
    public Result<State> getState(String stateName)
    {
        PreCondition.assertNotNullAndNotEmpty(stateName, "stateName");

        final String stateKey = getStateKey(stateName);
        return states.get(stateKey)
            .catchError(NotFoundException.class, () -> new NotFoundException(stateName, "No state was found in this DFA with the name " + Strings.escapeAndQuote(stateName)));
    }

    private static String getStateKey(String stateName)
    {
        return stateName.toLowerCase();
    }
}
