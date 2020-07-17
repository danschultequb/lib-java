package qub;

/**
 * An individual component/element within a UI.
 */
public interface UIElement
{
    /**
     * Set the width of this UIElement.
     * @param width The width of this UIElement.
     * @return This object for method chaining.
     */
    UIElement setWidth(Distance width);

    /**
     * Get the width of this UIElement.
     * @return The width of this UIElement.
     */
    Distance getWidth();

    /**
     * Set the height of this UIElement.
     * @param height The height of this UIElement.
     * @return This object for method chaining.
     */
    UIElement setHeight(Distance height);

    /**
     * Get the height of this UIElement.
     * @return The height of this UIElement.
     */
    Distance getHeight();

    /**
     * Set the size of this UIElement.
     * @param size The size of this UIElement.
     * @return This object for method chaining.
     */
    default UIElement setSize(Size2D size)
    {
        PreCondition.assertNotNull(size, "size");

        return this.setSize(size.getWidth(), size.getHeight());
    }

    /**
     * Set the size of this UIElement.
     * @param width The width of this UIElement.
     * @param height The height of this UIElement.
     * @return This object for method chaining.
     */
    UIElement setSize(Distance width, Distance height);

    /**
     * Get the size of this UIElement.
     * @return The size of this UIElement.
     */
    Size2D getSize();

    /**
     * Register the provided callback to be invoked when this UIElement's size changes.
     * @param callback The callback to be invoked when this UIElement's size changes.
     * @return A Disposable that can be disposed to unregister the provided callback.
     */
    Disposable onSizeChanged(Action0 callback);

    /**
     * Get the padding that surrounds this UIElement's content.
     * @return The padding that surrounds this UIElement's content.
     */
    UIPadding getPadding();

    /**
     * Set the padding for this UIElement.
     * @param padding The padding to set on this UIElement.
     * @return This object for method chaining.
     */
    UIElement setPadding(UIPadding padding);

    /**
     * Subscribe to be notified when this UIElement's padding changes.
     * @param callback The callback that will be invoked when this UIElement's padding changes.
     * @return A Disposable that can be disposed to unsubscribe the provided callback.
     */
    default Disposable onPaddingChanged(Action0 callback)
    {
        PreCondition.assertNotNull(callback, "callback");

        return this.onPaddingChanged((UIPadding newPadding) -> callback.run());
    }

    /**
     * Subscribe to be notified when this UIElement's padding changes.
     * @param callback The callback that will be invoked when this UIElement's padding changes.
     * @return A Disposable that can be disposed to unsubscribe the provided callback.
     */
    default Disposable onPaddingChanged(Action1<UIPadding> callback)
    {
        PreCondition.assertNotNull(callback, "callback");

        return this.onPaddingChanged((UIPadding oldPadding, UIPadding newPadding) -> callback.run(newPadding));
    }

    /**
     * Subscribe to be notified when this UIElement's padding changes.
     * @param callback The callback that will be invoked when this UIElement's padding changes.
     * @return A Disposable that can be disposed to unsubscribe the provided callback.
     */
    Disposable onPaddingChanged(Action2<UIPadding,UIPadding> callback);

    /**
     * Get the size of the space available for the content of this UIElement.
     * @return The size of the space available for the content of this UIElement.
     */
    Size2D getContentSpaceSize();

    /**
     * Get the width of the space available for the content of this UIElement.
     * @return The width of the space available for the content of this UIElement.
     */
    Size2D getContentSpaceWidth();

    /**
     * Get the height of the space available for the content of this UIElement.
     * @return The height of the space available for the content of this UIElement.
     */
    Size2D getContentSpaceHeight();

    /**
     * Get the actual size of the content of this UIElement.
     * @return The actual size of the content of this UIElement.
     */
    Size2D getContentSize();

    /**
     * Get the actual width of the content of this UIElement.
     * @return The actual width of the content of this UIElement.
     */
    Distance getContentWidth();

    /**
     * Get the actual height of the content of this UIElement.
     * @return The actual height of the content of this UIElement.
     */
    Distance getContentHeight();

    /**
     * Get the background color of this UIElement.
     * @return The background color of this UIElement.
     */
    Color getBackgroundColor();

    /**
     * Set the background color of this UIElement.
     * @param backgroundColor The background color of this UIElement.
     * @return This object for method chaining.
     */
    UIElement setBackgroundColor(Color backgroundColor);
}
