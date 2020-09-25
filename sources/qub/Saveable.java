package qub;

/**
 * A type that can have its state saved and restored.
 */
public interface Saveable
{
    /**
     * Create a Save of this object's state.
     * @return The new Save of this object's state.
     */
    Save save();
}
