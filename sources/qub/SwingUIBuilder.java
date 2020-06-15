package qub;

/**
 * An object that can be used to build Swing user interfaces.
 */
public class SwingUIBuilder implements UIBuilder
{
    private final SwingUIBase uiBase;

    private SwingUIBuilder(SwingUIBase uiBase)
    {
        PreCondition.assertNotNull(uiBase, "uiBase");

        this.uiBase = uiBase;
    }

    public static SwingUIBuilder create(Display display, AsyncRunner asyncRunner)
    {
        PreCondition.assertNotNull(display, "display");
        PreCondition.assertNotNull(asyncRunner, "asyncRunner");

        final SwingUIBase uiBase = SwingUIBase.create(display, asyncRunner);
        return SwingUIBuilder.create(uiBase);
    }

    public static SwingUIBuilder create(SwingUIBase uiBase)
    {
        return new SwingUIBuilder(uiBase);
    }

    public static SwingUIBuilder create(Process process)
    {
        PreCondition.assertNotNull(process, "process");

        return SwingUIBuilder.create(SwingUIBase.create(process));
    }

    @Override
    public SwingUIWindow createUIWindow()
    {
        return SwingUIWindow.create(this.uiBase);
    }

    @Override
    public SwingUIButton createUIButton()
    {
        return SwingUIButton.create(this.uiBase);
    }

    @Override
    public SwingUIText createUIText()
    {
        return SwingUIText.create(this.uiBase);
    }

    @Override
    public SwingUITextBox createUITextBox()
    {
        return SwingUITextBox.create(this.uiBase);
    }

    @Override
    public SwingUIVerticalLayout createUIVerticalLayout()
    {
        return SwingUIVerticalLayout.create(this.uiBase);
    }

    @Override
    public SwingUIHorizontalLayout createUIHorizontalLayout()
    {
        return SwingUIHorizontalLayout.create(this.uiBase);
    }
}
