package qub;

public class VisualVMParameters extends ChildProcessParametersDecorator<VisualVMParameters>
{
    private VisualVMParameters(Path visualVMPath)
    {
        super(visualVMPath);
    }

    public static VisualVMParameters create()
    {
        return VisualVMParameters.create(Path.parse("visualvm"));
    }

    public static VisualVMParameters create(Path visualVmPath)
    {
        PreCondition.assertNotNull(visualVmPath, "visualVmPath");

        return new VisualVMParameters(visualVmPath);
    }

    /**
     * Set the id of the process to open in VisualVM.
     * @param processId The id of the process to open in VisualVM.
     * @return This object for method chaining.
     */
    public VisualVMParameters setOpenPid(long processId)
    {
        return this.addArguments("--openpid", Longs.toString(processId));
    }

    /**
     * Set whether or not to show the splash screen when VisualVM is opening.
     * @param showSplashScreen Whether or not to show the splash screen when VisualVM is opening.
     * @return This object for method chaining.
     */
    public VisualVMParameters setShowSplashScreen(boolean showSplashScreen)
    {
        if (!showSplashScreen)
        {
            this.addArguments("--nosplash");
        }
        return this;
    }

    /**
     * Set the base font size of the VisualVM user interface.
     * @param fontSize The base font size of the VisualVM user interface.
     * @return This object for method chaining.
     */
    public VisualVMParameters setFontSize(Distance fontSize)
    {
        PreCondition.assertNotNull(fontSize, "fontSize");
        PreCondition.assertGreaterThanOrEqualTo(fontSize, Distance.zero, "fontSize");

        final Distance fontPoints = fontSize.toFontPoints();
        return this.addArguments("--fontsize", Integers.toString((int)fontPoints.getValue()));
    }

    public VisualVMParameters setJDKHome(String jdkHome)
    {
        PreCondition.assertNotNullAndNotEmpty(jdkHome, "jdkHome");

        return this.setJDKHome(Path.parse(jdkHome));
    }

    public VisualVMParameters setJDKHome(Path jdkHome)
    {
        PreCondition.assertNotNull(jdkHome, "jdkHome");
        PreCondition.assertTrue(jdkHome.isRooted(), "jdkHome.isRooted()");

        return this.addArguments("--jdkhome", Strings.escapeAndQuote(jdkHome.toString()));
    }

    public VisualVMParameters setJDKHome(Folder jdkHome)
    {
        PreCondition.assertNotNull(jdkHome, "jdkHome");

        return this.setJDKHome(jdkHome.getPath());
    }

    public VisualVMParameters setSuppressConsoleOutput(boolean suppressConsoleOutput)
    {
        if (suppressConsoleOutput)
        {
            this.addArguments("--console", "suppress");
        }
        return this;
    }

    public VisualVMParameters setSourceRoots(Iterable<String> sourceRoots)
    {
        PreCondition.assertNotNull(sourceRoots, "sourceRoots");

        return this.addArgument("--source-roots=" + Strings.escapeAndQuote(Strings.join(';', sourceRoots)));
    }
}
