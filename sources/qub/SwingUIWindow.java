package qub;

public class SwingUIWindow implements UIWindow
{
    private final AWTUIElementBase uiElementBase;
    private final javax.swing.JFrame jFrame;
    private final PausedAsyncTask<Void> disposedTask;
    private boolean isDisposed;
    private AWTUIElement content;

    private SwingUIWindow(AWTUIBase uiBase)
    {
        PreCondition.assertNotNull(uiBase, "uiBase");

        this.jFrame = new javax.swing.JFrame();
        this.jFrame.setDefaultCloseOperation(javax.swing.JFrame.DISPOSE_ON_CLOSE);
        this.jFrame.pack();
        this.jFrame.addWindowListener(new java.awt.event.WindowListener()
        {
            @Override
            public void windowOpened(java.awt.event.WindowEvent e)
            {
                uiBase.registerUIEventThread();
            }

            @Override
            public void windowClosing(java.awt.event.WindowEvent e)
            {
            }

            @Override
            public void windowClosed(java.awt.event.WindowEvent e)
            {
                SwingUIWindow.this.dispose().await();
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
        this.disposedTask = uiBase.createPausedAsyncTask();
        this.uiElementBase = new AWTUIElementBase(uiBase, this.jFrame);
    }

    public static SwingUIWindow create(AWTUIBase base)
    {
        PreCondition.assertNotNull(base, "base");

        return new SwingUIWindow(base);
    }

    public javax.swing.JFrame getJFrame()
    {
        return this.jFrame;
    }

    @Override
    public SwingUIWindow setTitle(String title)
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
    public SwingUIWindow setContent(UIElement content)
    {
        PreCondition.assertNotNull(content, "content");
        PreCondition.assertInstanceOf(content, AWTUIElement.class, "content");

        return this.setContent((AWTUIElement)content);
    }

    public SwingUIWindow setContent(AWTUIElement content)
    {
        PreCondition.assertNotNull(content, "content");

        if (this.content != null)
        {
            this.jFrame.remove(this.content.getComponent());
        }
        this.content = content;
        this.jFrame.add(this.content.getComponent());
        this.uiElementBase.updateSize();

        return this;
    }

    @Override
    public AWTUIElement getContent()
    {
        return this.content;
    }

    /**
     * Set whether or not this SwingWindow is visible.
     * @param visible Whether or not this SwingWindow is visible.
     * @return This object for method chaining.
     */
    public SwingUIWindow setVisible(boolean visible)
    {
        PreCondition.assertNotDisposed(this, "this");

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
    public SwingUIWindow setWidth(Distance width)
    {
        this.uiElementBase.setWidth(width);
        return this;
    }

    /**
     * Get the width of this Window.
     * @return The width of this Window.
     */
    public Distance getWidth()
    {
        return this.uiElementBase.getWidth();
    }

    /**
     * Set the height of this Window.
     * @param height The height of this Window.
     * @return This object for method chaining.
     */
    public SwingUIWindow setHeight(Distance height)
    {
        this.uiElementBase.setHeight(height);
        return this;
    }

    /**
     * Get the height of this Window.
     * @return The height of this Window.
     */
    public Distance getHeight()
    {
        return this.uiElementBase.getHeight();
    }

    /**
     * Set the size of this Window.
     * @param size The size of this Window.
     * @return This object for method chaining.
     */
    public SwingUIWindow setSize(Size2D size)
    {
        this.uiElementBase.setSize(size);
        return this;
    }

    /**
     * Set the size of this Window.
     * @param width The width of this Window.
     * @param height The height of this Window.
     * @return This object for method chaining.
     */
    public SwingUIWindow setSize(Distance width, Distance height)
    {
        this.uiElementBase.setSize(width, height);
        return this;
    }

    /**
     * Get the size of this Window.
     * @return The size of this Window.
     */
    public Size2D getSize()
    {
        return this.uiElementBase.getSize();
    }

    /**
     * Register the provided callback to be invoked when this SwingWindow's size changes.
     * @param callback The callback to be invoked when this SwingWindow's size changes.
     * @return A Disposable that can be disposed to unregister the provided callback.
     */
    public Disposable onSizeChanged(Action0 callback)
    {
        return this.uiElementBase.onSizeChanged(callback);
    }

    /**
     * Set the width of this Window.
     * @param contentWidth The width of this Window.
     * @return This object for method chaining.
     */
    public SwingUIWindow setContentWidth(Distance contentWidth)
    {
        PreCondition.assertNotNull(contentWidth, "contentWidth");
        PreCondition.assertGreaterThanOrEqualTo(contentWidth, Distance.zero, "contentWidth");

        return this.setContentSize(contentWidth, this.getContentHeight());
    }

    /**
     * Get the content width of this Window.
     * @return The content width of this Window.
     */
    public Distance getContentWidth()
    {
        return this.uiElementBase.getContentWidth(this.jFrame);
    }

    /**
     * Set the content height of this Window.
     * @param contentHeight The content height of this Window.
     * @return This object for method chaining.
     */
    public SwingUIWindow setContentHeight(Distance contentHeight)
    {
        PreCondition.assertNotNull(contentHeight, "contentHeight");
        PreCondition.assertGreaterThanOrEqualTo(contentHeight, Distance.zero, "contentHeight");

        return this.setContentSize(this.getContentWidth(), contentHeight);
    }

    /**
     * Get the height of this Window.
     * @return The height of this Window.
     */
    public Distance getContentHeight()
    {
        return this.uiElementBase.getContentHeight(this.jFrame);
    }

    /**
     * Set the content size of this Window.
     * @param contentSize The content size of this Window.
     * @return This object for method chaining.
     */
    public SwingUIWindow setContentSize(Size2D contentSize)
    {
        PreCondition.assertNotNull(contentSize, "contentSize");

        this.uiElementBase.setContentSize(this.jFrame, contentSize);
        return this;
    }

    /**
     * Set the content size of this Window.
     * @param contentWidth The width of this Window.
     * @param contentHeight The height of this Window.
     * @return This object for method chaining.
     */
    public SwingUIWindow setContentSize(Distance contentWidth, Distance contentHeight)
    {
        this.uiElementBase.setContentSize(this.jFrame, contentWidth, contentHeight);
        return this;
    }

    /**
     * Get the size of this Window.
     * @return The size of this Window.
     */
    public Size2D getContentSize()
    {
        return this.uiElementBase.getContentSize(this.jFrame);
    }
}
