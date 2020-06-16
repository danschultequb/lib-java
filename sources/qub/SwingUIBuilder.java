package qub;

/**
 * An object that can be used to build Swing user interfaces.
 */
public class SwingUIBuilder extends BasicUIBuilder
{
    private final SwingUIBase uiBase;

    private SwingUIBuilder(SwingUIBase uiBase)
    {
        PreCondition.assertNotNull(uiBase, "uiBase");

        this.uiBase = uiBase;

        this.setCreator(Iterable.create(SwingUIButton.class, UIButton.class), SwingUIButton::create);
        this.setCreator(Iterable.create(SwingUIText.class, UIText.class), SwingUIText::create);
        this.setCreator(Iterable.create(SwingUITextBox.class, UITextBox.class), SwingUITextBox::create);
        this.setCreator(Iterable.create(SwingUIHorizontalLayout.class, UIHorizontalLayout.class), SwingUIHorizontalLayout::create);
        this.setCreator(Iterable.create(SwingUIVerticalLayout.class, UIVerticalLayout.class), SwingUIVerticalLayout::create);
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

    /**
     * Set the creator function that will be used when an object of the type U is requested.
     * @param uiType The type that will invoke the provided uiElementCreator when it is requested.
     * @param uiCreator The creator that will be invoked when a uiType is requested.
     * @param <U> The type that will invoke the provided uiCreator when it is requested.
     * @param <T>The type that will be created.
     * @return This object for method chaining.
     */
    public <U extends UIElement, T extends U> SwingUIBuilder setCreator(Class<? extends U> uiType, Function1<SwingUIBase,T> uiCreator)
    {
        PreCondition.assertNotNull(uiType, "uiType");
        PreCondition.assertNotNull(uiCreator, "uiType");

        return this.setCreator(Iterable.create(uiType), uiCreator);
    }

    /**
     * Set the creator function that will be used when an object of the type U is requested.
     * @param uiTypes The types of UIElement that will invoke the provided uiCreator when it is requested.
     * @param uiCreator The creator that will be invoked when any of the uiTypes are requested.
     * @param <U> The type of that will invoke the provided uiCreator when it is requested.
     * @param <T>The type that will be created.
     * @return This object for method chaining.
     */
    public <U extends UIElement, T extends U> SwingUIBuilder setCreator(Iterable<Class<? extends U>> uiTypes, Function1<SwingUIBase,T> uiCreator)
    {
        PreCondition.assertNotNull(uiTypes, "uiTypes");
        PreCondition.assertNotNull(uiCreator, "uiType");

        return (SwingUIBuilder)this.setCreator(uiTypes, () -> uiCreator.run(this.uiBase));
    }

    /**
     * Create a new SwingUIWindow object.
     * @return The new SwingUIWindow object.
     */
    public Result<SwingUIWindow> createSwingUIWindow()
    {
        return Result.success(SwingUIWindow.create(this.uiBase));
    }
}
