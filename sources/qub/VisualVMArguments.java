package qub;

/**
 * The arguments that can be passed on the command line to the visualvm application.
 * @param <T> The type that is implementing this interface.
 */
public interface VisualVMArguments<T>
{
    /**
     * Add the provided arguments to the list of arguments that will be provided to the executable
     * when this ProcessBuilder is run.
     * @param arguments The arguments to add.
     * @return This object for method chaining.
     */
    T addArguments(String... arguments);

    /**
     * Set the id of the process to open in VisualVM.
     * @param processId The id of the process to open in VisualVM.
     * @return This object for method chaining.
     */
    default T setOpenPid(long processId)
    {
        return this.addArguments("--openpid", Longs.toString(processId));
    }

    /**
     * Set whether or not to show the splash screen when VisualVM is opening.
     * @param showSplashScreen Whether or not to show the splash screen when VisualVM is opening.
     * @return This object for method chaining.
     */
    @SuppressWarnings("unchecked")
    default T setShowSplashScreen(boolean showSplashScreen)
    {
        if (!showSplashScreen)
        {
            this.addArguments("--nosplash");
        }
        return (T)this;
    }

    /**
     * Set the base font size of the VisualVM user interface.
     * @param fontSize The base font size of the VisualVM user interface.
     * @return This object for method chaining.
     */
    default T setFontSize(Distance fontSize)
    {
        PreCondition.assertNotNull(fontSize, "fontSize");
        PreCondition.assertGreaterThanOrEqualTo(fontSize, Distance.zero, "fontSize");

        final Distance fontPoints = fontSize.toFontPoints();
        return this.addArguments("--fontsize", Integers.toString((int)fontPoints.getValue()));
    }
}
