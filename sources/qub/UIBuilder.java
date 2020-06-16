package qub;

/**
 * An object that can be used to build user interfaces.
 */
public interface UIBuilder
{
    /**
     * Set the creator function that will be used when an object of the type U is requested.
     * @param uiElementType The type that will invoke the provided uiElementCreator when it is requested.
     * @param uiElementCreator The creator that will be invoked when a uiElementType is requested.
     * @param <U> The type of UIElement that will invoke the provided uiElementCreator when it is requested.
     * @param <T>The UIElement type that will be created.
     * @return This object for method chaining.
     */
    <U extends UIElement, T extends U> UIBuilder setCreator(Class<? extends U> uiElementType, Function0<T> uiElementCreator);

    /**
     * Set the creator function that will be used when an object of the type U is requested.
     * @param uiElementTypes The types of UIElement that will invoke the provided uiElementCreator when it is requested.
     * @param uiElementCreator The creator that will be invoked when any of the uiElementTypes are requested.
     * @param <U> The type of UIElement that will invoke the provided uiElementCreator when it is requested.
     * @param <T>The UIElement type that will be created.
     * @return This object for method chaining.
     */
    default <U extends UIElement, T extends U> UIBuilder setCreator(Iterable<Class<? extends U>> uiElementTypes, Function0<T> uiElementCreator)
    {
        PreCondition.assertNotNullAndNotEmpty(uiElementTypes, "uiElementTypes");
        PreCondition.assertNotNull(uiElementCreator, "uiElementCreator");

        for (final Class<? extends U> uiType : uiElementTypes)
        {
            this.setCreator(uiType, uiElementCreator);
        }

        return this;
    }

    /**
     * Create a new UIElement with the provided type.
     * @param uiElementType The type of the UIElement to create.
     * @param <T> The type of the UIElement to create.
     * @return The created UIElement.
     */
    <T extends UIElement> Result<T> create(Class<T> uiElementType);

    /**
     * Create a new UIButton.
     * @return The new UIButton.
     */
    default Result<? extends UIButton> createUIButton()
    {
        return this.create(UIButton.class);
    }

    /**
     * Create a new UIText.
     * @return The new UIText.
     */
    default Result<? extends UIText> createUIText()
    {
        return this.create(UIText.class);
    }

    /**
     * Create a new UITextBox.
     * @return The new UITextBox.
     */
    default Result<? extends UITextBox> createUITextBox()
    {
        return this.create(UITextBox.class);
    }

    /**
     * Create a new UIVerticalLayout.
     * @return The new UIVerticalLayout.
     */
    default Result<? extends UIVerticalLayout> createUIVerticalLayout()
    {
        return this.create(UIVerticalLayout.class);
    }

    /**
     * Create a new UIHorizontalLayout.
     * @return The new UIHorizontalLayout.
     */
    default Result<? extends UIHorizontalLayout> createUIHorizontalLayout()
    {
        return this.create(UIHorizontalLayout.class);
    }
}
