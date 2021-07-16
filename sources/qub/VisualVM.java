package qub;

public class VisualVM extends ChildProcessRunnerWrapper<VisualVM,VisualVMParameters>
{
    private VisualVM(ChildProcessRunner childProcessRunner)
    {
        super(childProcessRunner, VisualVMParameters::create, "visualvm");
    }

    public static VisualVM create(ChildProcessRunner childProcessRunner)
    {
        return new VisualVM(childProcessRunner);
    }
}
