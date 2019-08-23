package qub;

/**
 * An object that can contain tests or test groups.
 */
public interface TestParent
{
    /**
     * Get the name of this TestParent object.
     * @return The name of this TestParent object.
     */
    String getName();

    /**
     * Get the full name of this TestParent object.
     * @return The full name of this TestParent object.
     */
    String getFullName();

    /**
     * Get whether or not the provided PathPattern matches this object.
     * @param testPattern The PathPattern to compare against.
     * @return Whether or not the provided PathPattern matches this object.
     */
    boolean matches(PathPattern testPattern);

    /**
     * Whether or not the tests contained by this TestParent object should be skipped.
     * @return Whether or not the tests contained by this TestParent object should be skipped.
     */
    boolean shouldSkip();

    /**
     * Get the message that was provided when this TestParent was skipped.
     * @return The message that was provided when this TestParent was skipped.
     */
    String getSkipMessage();

    /**
     * Get the TestParent of this object.
     * @return The TestParent of this object.
     */
    TestParent getParent();
}
