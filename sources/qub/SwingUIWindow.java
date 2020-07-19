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

    @Override
    public SwingUIWindow setWidth(Distance width)
    {
        this.uiElementBase.setWidth(width);
        return this;
    }

    @Override
    public SwingUIWindow setWidthInPixels(int widthInPixels)
    {
        this.uiElementBase.setWidthInPixels(widthInPixels);
        return this;
    }

    @Override
    public Distance getWidth()
    {
        return this.uiElementBase.getWidth();
    }

    @Override
    public int getWidthInPixels()
    {
        return this.uiElementBase.getWidthInPixels();
    }

    @Override
    public SwingUIWindow setHeight(Distance height)
    {
        this.uiElementBase.setHeight(height);
        return this;
    }

    @Override
    public SwingUIWindow setHeightInPixels(int heightInPixels)
    {
        this.uiElementBase.setHeightInPixels(heightInPixels);
        return this;
    }

    @Override
    public Distance getHeight()
    {
        return this.uiElementBase.getHeight();
    }

    @Override
    public int getHeightInPixels()
    {
        return this.uiElementBase.getHeightInPixels();
    }

    @Override
    public SwingUIWindow setSize(Size2D size)
    {
        this.uiElementBase.setSize(size);
        return this;
    }

    @Override
    public SwingUIWindow setSize(Distance width, Distance height)
    {
        this.uiElementBase.setSize(width, height);
        return this;
    }

    @Override
    public SwingUIWindow setSizeInPixels(int widthInPixels, int heightInPixels)
    {
        this.uiElementBase.setSizeInPixels(widthInPixels, heightInPixels);
        return this;
    }

    @Override
    public Size2D getSize()
    {
        return this.uiElementBase.getSize();
    }

    @Override
    public Disposable onSizeChanged(Action0 callback)
    {
        return this.uiElementBase.onSizeChanged(callback);
    }

    @Override
    public UIPadding getPadding()
    {
        return this.uiElementBase.getPadding(this.jFrame);
    }

    @Override
    public UIPaddingInPixels getPaddingInPixels()
    {
        return this.uiElementBase.getPaddingInPixels(this.jFrame);
    }

    @Override
    public Size2D getContentSpaceSize()
    {
        return this.uiElementBase.getContentSpaceSize(this.jFrame);
    }

    public SwingUIWindow setContentSpaceSize(Size2D contentSpaceSize)
    {
        PreCondition.assertNotNull(contentSpaceSize, "contentSpaceSize");

        return this.setContentSpaceSize(contentSpaceSize.getWidth(), contentSpaceSize.getHeight());
    }

    public SwingUIWindow setContentSpaceSize(Distance contentSpaceWidth, Distance contentSpaceHeight)
    {
        PreCondition.assertNotNull(contentSpaceWidth, "contentSpaceWidth");
        PreCondition.assertGreaterThanOrEqualTo(contentSpaceWidth, Distance.zero, "contentSpaceWidth");
        PreCondition.assertNotNull(contentSpaceHeight, "contentSpaceHeight");
        PreCondition.assertGreaterThanOrEqualTo(contentSpaceHeight, Distance.zero, "contentSpaceHeight");

        final UIPadding padding = this.getPadding();
        final Distance width = contentSpaceWidth.plus(padding.getWidth());
        final Distance height = contentSpaceHeight.plus(padding.getHeight());
        return this.setSize(Size2D.create(width, height));
    }

    @Override
    public Distance getContentSpaceWidth()
    {
        return this.uiElementBase.getContentSpaceWidth(this.jFrame);
    }

    @Override
    public int getContentSpaceWidthInPixels()
    {
        return this.uiElementBase.getContentSpaceWidthInPixels(this.jFrame);
    }

    public SwingUIWindow setContentSpaceWidth(Distance contentSpaceWidth)
    {
        PreCondition.assertNotNull(contentSpaceWidth, "contentSpaceWidth");
        PreCondition.assertGreaterThanOrEqualTo(contentSpaceWidth, Distance.zero, "contentSpaceWidth");

        final UIPadding padding = this.getPadding();
        final Distance width = contentSpaceWidth.plus(padding.getWidth());
        return this.setWidth(width);
    }

    public SwingUIWindow setContentSpaceWidthInPixels(int contentSpaceWidthInPixels)
    {
        PreCondition.assertGreaterThanOrEqualTo(contentSpaceWidthInPixels, 0, "contentSpaceWidthInPixels");

        final UIPaddingInPixels padding = this.getPaddingInPixels();
        final int widthInPixels = contentSpaceWidthInPixels + padding.getWidth();
        return this.setWidthInPixels(widthInPixels);
    }

    @Override
    public Distance getContentSpaceHeight()
    {
        return this.uiElementBase.getContentSpaceHeight(this.jFrame);
    }

    @Override
    public int getContentSpaceHeightInPixels()
    {
        return this.uiElementBase.getContentSpaceHeightInPixels(this.jFrame);
    }

    public SwingUIWindow setContentSpaceHeight(Distance contentSpaceHeight)
    {
        PreCondition.assertNotNull(contentSpaceHeight, "contentSpaceHeight");
        PreCondition.assertGreaterThanOrEqualTo(contentSpaceHeight, Distance.zero, "contentSpaceHeight");

        final UIPadding padding = this.getPadding();
        final Distance height = contentSpaceHeight.plus(padding.getHeight());
        return this.setHeight(height);
    }

    public SwingUIWindow setContentSpaceHeightInPixels(int contentSpaceHeightInPixels)
    {
        PreCondition.assertGreaterThanOrEqualTo(contentSpaceHeightInPixels, 0, "contentSpaceHeightInPixels");

        final UIPaddingInPixels padding = this.getPaddingInPixels();
        final int widthInPixels = contentSpaceHeightInPixels + padding.getHeight();
        return this.setHeightInPixels(widthInPixels);
    }

    @Override
    public Color getBackgroundColor()
    {
        return this.uiElementBase.getBackgroundColor();
    }

    @Override
    public SwingUIWindow setBackgroundColor(Color backgroundColor)
    {
        this.uiElementBase.setBackgroundColor(backgroundColor);
        return this;
    }
}
