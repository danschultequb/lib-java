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
     * Set the width of this UIElement to match the provided DynamicDistance. As the
     * DynamicDistance object changes in value, this UIElement will change to match it.
     * @param dynamicWidth The dynamic width of this UIElement.
     * @return This object for method chaining.
     */
    UIElement setDynamicWidth(DynamicDistance dynamicWidth);

    /**
     * Set the width of this UIElement in pixels.
     * @param widthInPixels The width of this UIElement in pixels.
     * @return This object for method chaining.
     */
    UIElement setWidthInPixels(int widthInPixels);

    /**
     * Get the width of this UIElement.
     * @return The width of this UIElement.
     */
    Distance getWidth();

    /**
     * Get the dynamic width of this UIElement.
     * @return The dynamic width of this UIElement.
     */
    default DynamicDistance getDynamicWidth()
    {
        return DynamicDistance.create(this::getWidth, this::onWidthChanged);
    }

    /**
     * Get the width of this UIElement in pixels.
     * @return The width of this UIElement in pixels;
     */
    int getWidthInPixels();

    /**
     * Register the provided callback to be invoked when this UIElement's width changes.
     * @param callback The callback to be invoked when this UIElement's width changes.
     * @return A Disposable that can be disposed to unregister the provided callback.
     */
    Disposable onWidthChanged(Action0 callback);
    
    /**
     * Set the height of this UIElement.
     * @param height The height of this UIElement.
     * @return This object for method chaining.
     */
    UIElement setHeight(Distance height);

    /**
     * Set the height of this UIElement in pixels.
     * @param heightInPixels The height of this UIElement in pixels.
     * @return This object for method chaining.
     */
    UIElement setHeightInPixels(int heightInPixels);

    /**
     * Get the height of this UIElement.
     * @return The height of this UIElement.
     */
    Distance getHeight();

    /**
     * Get the dynamic height of this UIElement.
     * @return The dynamic height of this UIElement.
     */
    default DynamicDistance getDynamicHeight()
    {
        return DynamicDistance.create(this::getHeight, this::onHeightChanged);
    }

    /**
     * Get the height of this UIElement in pixels.
     * @return The height of this UIElement in pixels;
     */
    int getHeightInPixels();

    /**
     * Register the provided callback to be invoked when this UIElement's height changes.
     * @param callback The callback to be invoked when this UIElement's height changes.
     * @return A Disposable that can be disposed to unregister the provided callback.
     */
    Disposable onHeightChanged(Action0 callback);

    /**
     * Set the size of this UIElement.
     * @param size The size of this UIElement.
     * @return This object for method chaining.
     */
    UIElement setSize(Size2D size);

    /**
     * Set the size of this UIElement.
     * @param width The width of this UIElement.
     * @param height The height of this UIElement.
     * @return This object for method chaining.
     */
    UIElement setSize(Distance width, Distance height);

    /**
     * Set the size of this UIElement in pixels.
     * @param widthInPixels The width of this UIElement in pixels.
     * @param heightInPixels The height of this UIElement in pixels.
     * @return This object for method chaining.
     */
    UIElement setSizeInPixels(int widthInPixels, int heightInPixels);

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
     * Get the padding that surrounds this UIElement's content in pixels.
     * @return The padding that surrounds this UIElement's content in pixels.
     */
    UIPaddingInPixels getPaddingInPixels();

    /**
     * Set the padding for this UIElement.
     * @param padding The padding to set on this UIElement.
     * @return This object for method chaining.
     */
    UIElement setPadding(UIPadding padding);

    /**
     * Set the padding for this UIElement in pixels.
     * @param padding The padding to set on this UIElement in pixels.
     * @return This object for method chaining.
     */
    UIElement setPaddingInPixels(UIPaddingInPixels padding);

    /**
     * Subscribe to be notified when this UIElement's padding changes.
     * @param callback The callback that will be invoked when this UIElement's padding changes.
     * @return A Disposable that can be disposed to unsubscribe the provided callback.
     */
    Disposable onPaddingChanged(Action0 callback);

    /**
     * Get the size of the space available for the content of this UIElement.
     * @return The size of the space available for the content of this UIElement.
     */
    Size2D getContentSpaceSize();

    /**
     * Get the width of the space available for the content of this UIElement.
     * @return The width of the space available for the content of this UIElement.
     */
    Distance getContentSpaceWidth();

    /**
     * Get the dynamic content space width of this UIElement.
     * @return The dynamic content space width of this UIElement.
     */
    default DynamicDistance getDynamicContentSpaceWidth()
    {
        return DynamicDistance.create(this::getContentSpaceWidth, (Action0 callback) ->
        {
            return Disposable.create(this.onSizeChanged(callback), this.onPaddingChanged(callback));
        });
    }

    /**
     * Get the width of the space available for the content of this UIElement in pixels.
     * @return The width of the space available for the content of this UIElement in pixels.
     */
    int getContentSpaceWidthInPixels();

    /**
     * Get the height of the space available for the content of this UIElement.
     * @return The height of the space available for the content of this UIElement.
     */
    Distance getContentSpaceHeight();

    /**
     * Get the dynamic content space height of this UIElement.
     * @return The dynamic content space height of this UIElement.
     */
    default DynamicDistance getDynamicContentSpaceHeight()
    {
        return DynamicDistance.create(this::getContentSpaceHeight, (Action0 callback) ->
        {
            final Disposable sizeChangedSubscription = this.onSizeChanged(callback);
            final Disposable paddingChangedSubscription = this.onPaddingChanged(callback);
            return Disposable.create(() ->
            {
                sizeChangedSubscription.dispose().await();
                paddingChangedSubscription.dispose().await();
            });
        });
    }

    /**
     * Get the height of the space available for the content of this UIElement in pixels.
     * @return The height of the space available for the content of this UIElement in pixels.
     */
    int getContentSpaceHeightInPixels();

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
     * Get the actual width of the content of this UIElement in pixels.
     * @return The actual width of the content of this UIElement in pixels.
     */
    int getContentWidthInPixels();

    /**
     * Get the actual height of the content of this UIElement.
     * @return The actual height of the content of this UIElement.
     */
    Distance getContentHeight();

    /**
     * Get the actual height of the content of this UIElement in pixels.
     * @return The actual height of the content of this UIElement in pixels.
     */
    int getContentHeightInPixels();

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
