package qub;

public class SwingUIBase extends AWTUIBase
{
    protected SwingUIBase(Display display, AsyncRunner mainAsyncRunner, AsyncScheduler parallelAsyncRunner)
    {
        super(display, mainAsyncRunner, parallelAsyncRunner);
    }

    public static SwingUIBase create(Display display, AsyncRunner mainAsyncRunner, AsyncScheduler parallelAsyncRunner)
    {
        return new SwingUIBase(display, mainAsyncRunner, parallelAsyncRunner);
    }

    public static SwingUIBase create(Process process)
    {
        return SwingUIBase.create(process.getDisplays().first(), process.getMainAsyncRunner(), process.getParallelAsyncRunner());
    }

    /**
     * Get the padding of the provided Swing JComponent.
     * @param jComponent The JComponent to get the padding of.
     * @return The padding of the provided Swing JComponent.
     */
    public UIPadding getPadding(javax.swing.JComponent jComponent)
    {
        PreCondition.assertNotNull(jComponent, "jComponent");

        return this.convertUIPaddingInPixelsToUIPadding(this.getPaddingInPixels(jComponent));
    }

    /**
     * Get the padding of the provided Swing JComponent in pixels.
     * @param jComponent The JComponent to get the padding of.
     * @return The padding of the provided Swing JComponent in pixels.
     */
    public UIPaddingInPixels getPaddingInPixels(javax.swing.JComponent jComponent)
    {
        PreCondition.assertNotNull(jComponent, "jComponent");

        final java.awt.Insets insets = jComponent.getInsets();
        return UIPaddingInPixels.create(insets.left, insets.top, insets.right, insets.bottom);
    }

    /**
     * Set the padding for the provided JComponent.
     * @param jComponent The JComponent to set the padding of.
     * @param padding The padding to set.
     */
    public void setPadding(javax.swing.JComponent jComponent, UIPadding padding)
    {
        PreCondition.assertNotNull(jComponent, "jComponent");
        PreCondition.assertNotNull(padding, "padding");

        this.setPaddingInPixels(jComponent, this.convertUIPaddingToUIPaddingInPixels(padding));
    }

    /**
     * Set the padding for the provided JComponent in pixels.
     * @param jComponent The JComponent to set the padding of.
     * @param padding The padding to set in pixels.
     */
    public void setPaddingInPixels(javax.swing.JComponent jComponent, UIPaddingInPixels padding)
    {
        PreCondition.assertNotNull(jComponent, "jComponent");
        PreCondition.assertNotNull(padding, "padding");

        final javax.swing.border.Border border = javax.swing.BorderFactory.createEmptyBorder(
            padding.getTop(),
            padding.getLeft(),
            padding.getBottom(),
            padding.getRight());
        jComponent.setBorder(border);
    }

    /**
     * Register the provided callback to be invoked when the provided component's text changes.
     * @param jTextComponent The component to watch.
     * @param callback The callback to register.
     * @return A Disposable that can be disposed to unregister the provided callback from the
     * provided component.
     */
    public Disposable onTextChanged(javax.swing.text.JTextComponent jTextComponent, Action1<String> callback)
    {
        PreCondition.assertNotNull(jTextComponent, "jTextComponent");
        PreCondition.assertNotNull(callback, "callback");

        final javax.swing.event.DocumentListener documentListener = new javax.swing.event.DocumentListener()
        {

            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e)
            {
                callback.run(jTextComponent.getText());
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e)
            {
                callback.run(jTextComponent.getText());
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e)
            {
                callback.run(jTextComponent.getText());
            }
        };
        jTextComponent.getDocument().addDocumentListener(documentListener);
        return Disposable.create(() -> jTextComponent.getDocument().removeDocumentListener(documentListener));
    }

    @Override
    public void setBackgroundColor(java.awt.Component component, Color backgroundColor)
    {
        super.setBackgroundColor(component, backgroundColor);

        if (component instanceof javax.swing.JComponent)
        {
            ((javax.swing.JComponent)component).setOpaque(backgroundColor.getAlphaComponent() != Color.ComponentMin);
        }
    }
}
