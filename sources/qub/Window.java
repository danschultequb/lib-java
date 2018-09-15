package qub;

import javax.swing.JFrame;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class Window extends DisposableBase implements UIElementParent
{
    private final AsyncRunner mainAsyncRunner;
    private final Iterable<Display> displays;
    private BasicAsyncAction windowClosedTask;
    private final JFrame jFrame;
    private UIElement content;
    private volatile boolean disposed;

    public Window(final AsyncRunner mainAsyncRunner, Iterable<Display> displays)
    {
        PreCondition.assertNotNull(mainAsyncRunner, "mainAsyncRunner");
        PreCondition.assertNotNull(displays, "displays");

        this.mainAsyncRunner = mainAsyncRunner;
        this.displays = displays;

        this.jFrame = new JFrame();
        this.jFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.jFrame.addWindowListener(new WindowListener()
        {
            @Override
            public void windowOpened(WindowEvent e)
            {
            }

            @Override
            public void windowClosing(WindowEvent e)
            {
            }

            @Override
            public void windowClosed(WindowEvent e)
            {
                if (!isDisposed())
                {
                    mainAsyncRunner.schedule("WindowListener.windowClose()", new Action0()
                    {
                        @Override
                        public void run()
                        {
                            dispose();
                        }
                    }).await();
                }
            }

            @Override
            public void windowIconified(WindowEvent e)
            {
            }

            @Override
            public void windowDeiconified(WindowEvent e)
            {
            }

            @Override
            public void windowActivated(WindowEvent e)
            {
            }

            @Override
            public void windowDeactivated(WindowEvent e)
            {
            }
        });
    }

    /**
     * Wait for this Window to close. This will block the current thread.
     */
    public void awaitClose()
    {
        PreCondition.assertTrue(isOpen(), "isOpen()");

        windowClosedTask.await();
    }

    /**
     * Open this Window so that it is visible.
     */
    public void open()
    {
        PreCondition.assertFalse(isOpen(), "isOpen()");

        windowClosedTask = new BasicAsyncAction(mainAsyncRunner, "Window Closed Task");
        jFrame.setVisible(true);
    }

    /**
     * Get whether or not this Window is open.
     * @return Whether or not this Window is open.
     */
    public boolean isOpen()
    {
        return windowClosedTask != null && !isDisposed();
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

    public void setContent(javax.swing.JComponent content)
    {
        setContent(new JComponentToUIElementAdapter(content));
    }

    public void setContent(UIElement uiElement)
    {
        PreCondition.assertNotNull(uiElement, "uiElement");

        if (content != uiElement)
        {
            if (content != null)
            {
                content.setParent(null);
            }
            content = uiElement;
            uiElement.setParent(this);

            jFrame.setContentPane(new UIElementToJComponentAdapter(uiElement));

            repaint();
        }
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

            if (jFrame.isVisible())
            {
                jFrame.dispose();
            }
            if (windowClosedTask != null)
            {
                windowClosedTask.schedule();
                windowClosedTask.await();
            }

            result = Result.successTrue();
        }

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    @Override
    public void repaint()
    {
        jFrame.repaint();
    }

    @Override
    public UIElementParent getParentElement()
    {
        return this;
    }

    @Override
    public Window getParentWindow()
    {
        return this;
    }

    public double convertHorizontalDistanceToPixels(Distance horizontalDistance)
    {
        return displays.first().convertHorizontalDistanceToPixels(horizontalDistance);
    }

    public double convertVerticalDistanceToPixels(Distance verticalDistance)
    {
        return displays.first().convertVerticalDistanceToPixels(verticalDistance);
    }
}
