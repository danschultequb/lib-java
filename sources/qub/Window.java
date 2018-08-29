package qub;

import javax.swing.JFrame;

public class Window extends DisposableBase
{
    private final JFrame jFrame;
    private boolean disposed;

    public Window()
    {
        jFrame = new JFrame();
    }

    /**
     * Get whether or not this Window is visible.
     * @return Whether or not this Window is visible.
     */
    public boolean isVisible()
    {
        return jFrame.isVisible();
    }

    /**
     * Set whether or not this Window is visible.
     * @param visible Whether or not this Window is visible.
     */
    public void setVisible(boolean visible)
    {
        jFrame.setVisible(visible);
    }

    /**
     * Get the title of this Window.
     * @return The title of this Window.
     */
    public String getTitle()
    {
        return jFrame.getTitle();
    }

    /**
     * Set the title of this Window.
     * @param title The title of this Window.
     */
    public void setTitle(String title)
    {
        PreCondition.assertNotNull(title, "title");

        jFrame.setTitle(title);

        PostCondition.assertEqual(title, getTitle(), "getTitle()");
    }

    @Override
    public boolean isDisposed()
    {
        return disposed;
    }

    @Override
    public Result<Boolean> dispose()
    {
        Result<Boolean> result;

        if (disposed)
        {
            result = Result.successFalse();
        }
        else
        {
            disposed = true;

            jFrame.dispose();

            result = Result.successTrue();
        }

        PostCondition.assertNotNull(result, "result");

        return result;
    }
}
