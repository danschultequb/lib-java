package qub;

/**
 * A user interface Window.
 */
public interface Window
{
    /**
     * Set the title of this Window.
     * @param title The title of this Window.
     * @return This object for method chaining.
     */
    Window setTitle(String title);

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
    Window setContent(UIElement content);

    /**
     * Get the content of this Window, or null if no content has been set.
     * @return The content of this Window or null if no content has been set.
     */
    UIElement getContent();
}
