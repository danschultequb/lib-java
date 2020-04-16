package qub;

/**
 * A CharacterWriteStream that prepends an indentation to beginning of each line.
 */
public interface IndentedCharacterWriteStream extends CharacterWriteStream
{
    static IndentedCharacterWriteStream create(CharacterWriteStream characterWriteStream)
    {
        return BasicIndentedCharacterWriteStream.create(characterWriteStream);
    }

    /**
     * Get the current indentation.
     * @return The current indentation.
     */
    String getCurrentIndent();

    /**
     * Set the current indentation function.
     * @param currentIndentFunction The function that will return the current indentation.
     * @return This object for method chaining.
     */
    IndentedCharacterWriteStream setCurrentIndent(Function0<String> currentIndentFunction);

    /**
     * Set the current indentation.
     * @param currentIndent The current indentation.
     * @return This object for method chaining.
     */
    default IndentedCharacterWriteStream setCurrentIndent(String currentIndent)
    {
        PreCondition.assertNotNull(currentIndent, "currentIndent");

        return this.setCurrentIndent(() -> currentIndent);
    }

    /**
     * Get the String that makes up a single indentation. When this stream is indented, it will add
     * the single indentation to the current indentation. When this stream is unindented, it will
     * remove a single indentation to the current indentation.
     * @return The String that makes up a single indentation.
     */
    String getSingleIndent();

    /**
     * Set the String that makes up a single indentation. When this stream is indented, it will add
     * the single indentation to the current indentation. When this stream is unindented, it will
     * remove a single indentation to the current indentation.
     * @param singleIndent The String that makes up a single indentation.
     * @return This object for method chaining.
     */
    IndentedCharacterWriteStream setSingleIndent(String singleIndent);

    /**
     * Append the single indent to the current indent.
     * @return This object for method chaining.
     */
    default IndentedCharacterWriteStream increaseIndent()
    {
        return this.setCurrentIndent(this.getCurrentIndent() + this.getSingleIndent());
    }

    /**
     * Remove the single indent from the current indent.
     * @return This object for method chaining.
     */
    default IndentedCharacterWriteStream decreaseIndent()
    {
        final String currentIndent = this.getCurrentIndent();
        final int currentIndentLength = currentIndent.length();
        if (currentIndentLength > 0)
        {
            final int singleIndentLength = this.getSingleIndent().length();
            if (currentIndentLength < singleIndentLength)
            {
                this.setCurrentIndent("");
            }
            else
            {
                final int newCurrentIndentLength = currentIndentLength - singleIndentLength;
                this.setCurrentIndent(currentIndent.substring(0, newCurrentIndentLength));
            }
        }
        return this;
    }

    /**
     * Run the provided action with an additional indentation to the current indent.
     * @param action The action to run with an additional indentation to the current indent.
     */
    default void indent(Action0 action)
    {
        PreCondition.assertNotNull(action, "action");

        this.increaseIndent();
        try
        {
            action.run();
        }
        finally
        {
            this.decreaseIndent();
        }
    }

    IndentedCharacterWriteStream setNewLine(String newLine);
}
