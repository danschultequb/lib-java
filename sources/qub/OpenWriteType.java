package qub;

/**
 * The different ways that a file can be opened for writing.
 */
public enum OpenWriteType
{
    /**
     * If the file doesn't exist, create it. If it already exists, overwrite it.
     */
    CreateOrOverwrite,

    /**
     * If the file doesn't exist, create it. If it already exists, append to it.
     */
    CreateOrAppend,
}
