package qub;

public class SwingWindow implements Window, Disposable
{
    private final javax.swing.JFrame jFrame;
    private final Display display;
    private final AsyncRunner asyncRunner;
    private final PausedAsyncTask<Void> disposedTask;
    private boolean isDisposed;
    private SwingUIElement content;

    private SwingWindow(Display display, AsyncRunner asyncRunner)
    {
        PreCondition.assertNotNull(display, "display");
        PreCondition.assertNotNull(asyncRunner, "asyncRunner");

        this.jFrame = new javax.swing.JFrame();
        this.jFrame.setDefaultCloseOperation(javax.swing.JFrame.DISPOSE_ON_CLOSE);
        this.jFrame.addWindowListener(new java.awt.event.WindowListener()
        {
            @Override
            public void windowOpened(java.awt.event.WindowEvent e)
            {
            }

            @Override
            public void windowClosing(java.awt.event.WindowEvent e)
            {
            }

            @Override
            public void windowClosed(java.awt.event.WindowEvent e)
            {
                SwingWindow.this.dispose().await();
            }

            @Override
            public void windowIconified(java.awt.event.WindowEvent e)
            {
            }

            @Override
            public void windowDeiconified(java.awt.event.WindowEvent e)
            {
            }

            @Override
            public void windowActivated(java.awt.event.WindowEvent e)
            {
            }

            @Override
            public void windowDeactivated(java.awt.event.WindowEvent e)
            {
            }
        });
        this.display = display;

        this.asyncRunner = asyncRunner;
        this.disposedTask = asyncRunner.create(() -> {});
    }

    /**
     * Create a new Java Swing Window.
     * @param display The display that this SwingWindow will use.
     * @param asyncRunner The AsyncRunner that UI events will be scheduled on.
     * @return The new Java Swing Window.
     */
    public static SwingWindow create(Display display, AsyncRunner asyncRunner)
    {
        return new SwingWindow(display, asyncRunner);
    }

    @Override
    public SwingWindow setTitle(String title)
    {
        PreCondition.assertNotNull(title, "title");

        this.jFrame.setTitle(title);

        return this;
    }

    @Override
    public String getTitle()
    {
        return this.jFrame.getTitle();
    }

    @Override
    public SwingWindow setContent(UIElement content)
    {
        PreCondition.assertNotNull(content, "content");
        PreCondition.assertInstanceOf(content, SwingUIElement.class, "content");

        return this.setContent((SwingUIElement)content);
    }

    public SwingWindow setContent(SwingUIElement content)
    {
        PreCondition.assertNotNull(content, "content");

        if (this.content != null)
        {
            this.jFrame.remove(this.content.getJComponent());
        }
        this.content = (SwingUIElement)content;
        this.jFrame.add(this.content.getJComponent());

        return this;
    }

    @Override
    public SwingUIElement getContent()
    {
        return this.content;
    }

    /**
     * Set whether or not this SwingWindow is visible.
     * @param visible Whether or not this SwingWindow is visible.
     * @return This object for method chaining.
     */
    public SwingWindow setVisible(boolean visible)
    {
        PreCondition.assertNotDisposed(this, "this.isDisposed()");

        this.jFrame.setVisible(visible);

        return this;
    }

    /**
     * Get whether or not this SwingWindow is visible.
     * @return Whether or not this SwingWindow is visible.
     */
    public boolean isVisible()
    {
        return this.jFrame.isVisible();
    }

    @Override
    public boolean isDisposed()
    {
        return this.isDisposed;
    }

    @Override
    public Result<Boolean> dispose()
    {
        return Result.create(() ->
        {
            boolean result = !this.isDisposed;
            if (result)
            {
                this.isDisposed = true;
                this.jFrame.dispose();
                this.disposedTask.schedule();
            }
            return result;
        });
    }

    /**
     * Wait for this window to be closed.
     */
    public void await()
    {
        PreCondition.assertTrue(this.isVisible(), "this.isVisible()");

        this.disposedTask.await();
    }

    /**
     * Set the width of this Window.
     * @param width The width of this Window.
     * @return This object for method chaining.
     */
    public SwingWindow setWidth(Distance width)
    {
        PreCondition.assertNotNull(width, "width");
        PreCondition.assertGreaterThanOrEqualTo(width, Distance.zero, "width");

        return this.setSize(width, this.getHeight());
    }

    /**
     * Get the width of this Window.
     * @return The width of this Window.
     */
    public Distance getWidth()
    {
        final int jFrameWidth = this.jFrame.getWidth();
        final Distance result = this.display.convertHorizontalPixelsToDistance(jFrameWidth);

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    /**
     * Set the height of this Window.
     * @param height The height of this Window.
     * @return This object for method chaining.
     */
    public SwingWindow setHeight(Distance height)
    {
        PreCondition.assertNotNull(height, "height");
        PreCondition.assertGreaterThanOrEqualTo(height, Distance.zero, "height");

        return this.setSize(this.getWidth(), height);
    }

    /**
     * Get the height of this Window.
     * @return The height of this Window.
     */
    public Distance getHeight()
    {
        final int jFrameHeight = this.jFrame.getHeight();
        final Distance result = this.display.convertVerticalPixelsToDistance(jFrameHeight);

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    /**
     * Set the size of this Window.
     * @param size The size of this Window.
     * @return This object for method chaining.
     */
    public SwingWindow setSize(Size2D size)
    {
        PreCondition.assertNotNull(size, "size");

        return this.setSize(size.getWidth(), size.getHeight());
    }

    /**
     * Set the size of this Window.
     * @param width The width of this Window.
     * @param height The height of this Window.
     * @return This object for method chaining.
     */
    public SwingWindow setSize(Distance width, Distance height)
    {
        PreCondition.assertNotNull(width, "width");
        PreCondition.assertGreaterThanOrEqualTo(width, Distance.zero, "width");
        PreCondition.assertNotNull(height, "height");
        PreCondition.assertGreaterThanOrEqualTo(height, Distance.zero, "height");

        final double jFrameWidth = this.display.convertHorizontalDistanceToPixels(width);
        final double jFrameHeight = this.display.convertVerticalDistanceToPixels(height);
        this.jFrame.setSize((int)jFrameWidth, (int)jFrameHeight);

        return this;
    }

    /**
     * Get the size of this Window.
     * @return The size of this Window.
     */
    public Size2D getSize()
    {
        return new Size2D(this.getWidth(), this.getHeight());
    }
}
