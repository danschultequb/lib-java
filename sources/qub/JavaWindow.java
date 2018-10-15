package qub;

public class JavaWindow extends WindowBase
{
    private final AsyncRunner mainAsyncRunner;
    private final Iterable<Display> displays;
    private BasicAsyncAction windowClosedTask;
    private final javax.swing.JFrame jFrame;
    private UIElement content;
    private boolean disposed;
    private Function1<java.awt.Graphics2D,UIPainter> painterCreator;

    public JavaWindow(final AsyncRunner mainAsyncRunner, Iterable<Display> displays)
    {
        PreCondition.assertNotNull(mainAsyncRunner, "mainAsyncRunner");
        PreCondition.assertNotNull(displays, "displays");

        this.mainAsyncRunner = mainAsyncRunner;
        this.displays = displays;

        this.painterCreator = new Function1<java.awt.Graphics2D,UIPainter>()
        {
            @Override
            public UIPainter run(java.awt.Graphics2D graphics)
            {
                return new Graphics2DUIPainter(graphics, JavaWindow.this);
            }
        };

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
        jFrame.setContentPane(new javax.swing.JPanel()
        {
            @Override
            public void paint(java.awt.Graphics graphics)
            {
                PreCondition.assertInstanceOf(graphics, java.awt.Graphics2D.class, "graphics");

                super.paint(graphics);

                if (content != null && isOpen())
                {
                    final UIPainter painter = painterCreator.run((java.awt.Graphics2D)graphics);
                    content.paint(painter);
                }
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
    @Override
    public void open()
    {
        PreCondition.assertFalse(isOpen(), "isOpen()");
        PreCondition.assertFalse(isDisposed(), "isDisposed()");

        windowClosedTask = new BasicAsyncAction(mainAsyncRunner, "Window Closed Task");
        jFrame.setVisible(true);
    }

    /**
     * Get whether or not this Window is open.
     * @return Whether or not this Window is open.
     */
    @Override
    public boolean isOpen()
    {
        return windowClosedTask != null && !isDisposed();
    }

    /**
     * Set the painter that will be used for this Window.
     * @param painter The painter that will be used for this Window.
     */
    @Override
    public void setPainter(final UIPainter painter)
    {
        PreCondition.assertNotNull(painter, "painter");

        setPainterCreator(new Function1<java.awt.Graphics2D,UIPainter>()
        {
            @Override
            public UIPainter run(java.awt.Graphics2D graphics)
            {
                return painter;
            }
        });
    }

    /**
     * Set the function that will be used to create the painter that this Window will use.
     * @param painterCreator The function that will be used to create the painter that this Window
     *                       will use.
     */
    public void setPainterCreator(Function1<java.awt.Graphics2D,UIPainter> painterCreator)
    {
        PreCondition.assertNotNull(painterCreator, "painterCreator");

        this.painterCreator = painterCreator;
    }

    @Override
    public void setWidth(Distance width)
    {
        PreCondition.assertGreaterThanOrEqualTo(width, Distance.zero, "width");

        final int widthInPixels = (int)convertHorizontalDistanceToPixels(width);
        final int heightInPixels = jFrame.getHeight();
        jFrame.setSize(widthInPixels, heightInPixels);
    }

    @Override
    public Distance getWidth()
    {
        final int widthInPixels = jFrame.getWidth();
        final Distance result = convertHorizontalPixelsToDistance(widthInPixels);

        PostCondition.assertGreaterThanOrEqualTo(result, Distance.zero, "result");

        return result;
    }

    @Override
    public void setHeight(Distance height)
    {
        PreCondition.assertGreaterThanOrEqualTo(height, Distance.zero, "height");

        final int widthInPixels = jFrame.getWidth();
        final int heightInPixels = (int)convertVerticalDistanceToPixels(height);
        jFrame.setSize(widthInPixels, heightInPixels);
    }

    @Override
    public Distance getHeight()
    {
        final int heightInPixels = jFrame.getHeight();
        final Distance result = convertVerticalPixelsToDistance(heightInPixels);

        PostCondition.assertGreaterThanOrEqualTo(result, Distance.zero, "result");

        return result;
    }

    @Override
    public Size2D getSize()
    {
        final Size2D result = new Size2D(getWidth(), getHeight());

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    @Override
    public void setSize(Distance width, Distance height)
    {
        PreCondition.assertGreaterThanOrEqualTo(width, Distance.zero, "width");
        PreCondition.assertGreaterThanOrEqualTo(height, Distance.zero, "height");

        final int widthInPixels = (int)convertHorizontalDistanceToPixels(width);
        final int heightInPixels = (int)convertVerticalDistanceToPixels(height);
        jFrame.setSize(widthInPixels, heightInPixels);
    }

    @Override
    public void setSize(Size2D size)
    {
        PreCondition.assertNotNull(size, "size");

        setSize(size.getWidth(), size.getHeight());
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

    /**
     * Set the content of this Window to be the provided Swing JComponent.
     * @param content The new content of this Window.
     */
    public void setContent(javax.swing.JComponent content)
    {
        throw new NotSupportedException();
    }

    @Override
    public void setContent(UIElement uiElement)
    {
        PreCondition.assertTrue(!isDisposed() || uiElement == null, "!isDisposed() || uiElement == null");

        if (content != uiElement)
        {
            if (content != null)
            {
                content.setParent(null);
                content.parentWindowChanged(this, null);
            }

            content = uiElement;

            if (uiElement != null)
            {
                final Window previousParentWindow = uiElement.getParentWindow();
                uiElement.setParent(this);
                uiElement.parentWindowChanged(previousParentWindow, this);
            }

            if (isOpen())
            {
                repaint();
            }
        }
    }

    @Override
    public UIElement getContent()
    {
        return content;
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

            setContent((UIElement)null);

            result = Result.successTrue();
        }

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertTrue(isDisposed(), "isDisposed()");
        PostCondition.assertNull(getContent(), "getContent()");

        return result;
    }

    @Override
    public void repaint()
    {
        jFrame.repaint();
    }

    @Override
    public double convertHorizontalDistanceToPixels(Distance horizontalDistance)
    {
        PreCondition.assertNotNullAndNotEmpty(displays, "displays");

        return displays.first().convertHorizontalDistanceToPixels(horizontalDistance);
    }

    @Override
    public Distance convertHorizontalPixelsToDistance(double horizontalPixels)
    {
        PreCondition.assertNotNullAndNotEmpty(displays, "displays");

        return displays.first().convertHorizontalPixelsToDistance(horizontalPixels);
    }

    @Override
    public double convertVerticalDistanceToPixels(Distance verticalDistance)
    {
        PreCondition.assertNotNullAndNotEmpty(displays, "displays");

        return displays.first().convertVerticalDistanceToPixels(verticalDistance);
    }

    @Override
    public Distance convertVerticalPixelsToDistance(double verticalPixels)
    {
        PreCondition.assertNotNullAndNotEmpty(displays, "displays");

        return displays.first().convertVerticalPixelsToDistance(verticalPixels);
    }

    @Override
    public Size2D convertPixelsToSize2D(double horizontalPixels, double verticalPixels)
    {
        PreCondition.assertNotNullAndNotEmpty(displays, "displays");

        return displays.first().convertPixelsToSize2D(horizontalPixels, verticalPixels);
    }
}
