package qub;

/**
 * A user interface Window.
 */
public interface UIWindow extends Disposable
{
    /**
     * Set the title of this Window.
     * @param title The title of this Window.
     * @return This object for method chaining.
     */
    UIWindow setTitle(String title);

    /**
     * Get the title of this Window.
     * @return The title of this Window.
     */
    String getTitle();

    /**
     * Set the content of this Window.
     * @param content The content of this Window.
     * @return This object for method chaining.
     */
    UIWindow setContent(UIElement content);

    /**
     * Get the content of this Window, or null if no content has been set.
     * @return The content of this Window or null if no content has been set.
     */
    UIElement getContent();
    
    /**
     * Set the width of this UIWindow.
     * @param width The width of this UIWindow.
     * @return This object for method chaining.
     */
    UIWindow setWidth(Distance width);

    /**
     * Set the width of this UIWindow in pixels.
     * @param widthInPixels The width of this UIWindow in pixels.
     * @return This object for method chaining.
     */
    UIWindow setWidthInPixels(int widthInPixels);

    /**
     * Get the width of this UIWindow.
     * @return The width of this UIWindow.
     */
    Distance getWidth();

    /**
     * Get the width of this UIWindow in pixels.
     * @return The width of this UIWindow in pixels;
     */
    int getWidthInPixels();

    /**
     * Set the height of this UIWindow.
     * @param height The height of this UIWindow.
     * @return This object for method chaining.
     */
    UIWindow setHeight(Distance height);

    /**
     * Set the height of this UIWindow in pixels.
     * @param heightInPixels The height of this UIWindow in pixels.
     * @return This object for method chaining.
     */
    UIWindow setHeightInPixels(int heightInPixels);

    /**
     * Get the height of this UIWindow.
     * @return The height of this UIWindow.
     */
    Distance getHeight();

    /**
     * Get the height of this UIWindow in pixels.
     * @return The height of this UIWindow in pixels;
     */
    int getHeightInPixels();

    /**
     * Set the size of this UIWindow.
     * @param size The size of this UIWindow.
     * @return This object for method chaining.
     */
    UIWindow setSize(Size2D size);

    /**
     * Set the size of this UIWindow.
     * @param width The width of this UIWindow.
     * @param height The height of this UIWindow.
     * @return This object for method chaining.
     */
    UIWindow setSize(Distance width, Distance height);

    /**
     * Set the size of this UIWindow in pixels.
     * @param widthInPixels The width of this UIWindow in pixels.
     * @param heightInPixels The height of this UIWindow in pixels.
     * @return This object for method chaining.
     */
    UIWindow setSizeInPixels(int widthInPixels, int heightInPixels);

    /**
     * Get the size of this UIWindow.
     * @return The size of this UIWindow.
     */
    Size2D getSize();

    /**
     * Register the provided callback to be invoked when this UIWindow's size changes.
     * @param callback The callback to be invoked when this UIWindow's size changes.
     * @return A Disposable that can be disposed to unregister the provided callback.
     */
    Disposable onSizeChanged(Action0 callback);

    /**
     * Get the padding that surrounds this UIWindow's content.
     * @return The padding that surrounds this UIWindow's content.
     */
    UIPadding getPadding();

    /**
     * Get the padding that surrounds this UIWindow's content in pixels.
     * @return The padding that surrounds this UIWindow's content in pixels.
     */
    UIPaddingInPixels getPaddingInPixels();

    /**
     * Get the size of the space available for the content of this UIWindow.
     * @return The size of the space available for the content of this UIWindow.
     */
    Size2D getContentSpaceSize();

    /**
     * Get the width of the space available for the content of this UIWindow.
     * @return The width of the space available for the content of this UIWindow.
     */
    Distance getContentSpaceWidth();

    /**
     * Get the width of the space available for the content of this UIWindow in pixels.
     * @return The width of the space available for the content of this UIWindow in pixels.
     */
    int getContentSpaceWidthInPixels();

    /**
     * Get the height of the space available for the content of this UIWindow.
     * @return The height of the space available for the content of this UIWindow.
     */
    Distance getContentSpaceHeight();

    /**
     * Get the height of the space available for the content of this UIWindow in pixels.
     * @return The height of the space available for the content of this UIWindow in pixels.
     */
    int getContentSpaceHeightInPixels();

    /**
     * Get the background color of this UIWindow.
     * @return The background color of this UIWindow.
     */
    Color getBackgroundColor();

    /**
     * Set the background color of this UIWindow.
     * @param backgroundColor The background color of this UIWindow.
     * @return This object for method chaining.
     */
    UIWindow setBackgroundColor(Color backgroundColor);
}
