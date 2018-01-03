package qub;

/**
 * A TransitionCondition that is only satisfied by a single specific character.
 */
public class CharacterTransitionCondition implements TransitionCondition
{
    private final char character;

    public CharacterTransitionCondition(char character)
    {
        this.character = character;
    }

    @Override
    public boolean matches(char character)
    {
        return this.character == character;
    }
}
