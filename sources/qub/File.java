package qub;

import java.nio.Buffer;

public class File extends FileSystemEntry
{
    File(FileSystem fileSystem, String path)
    {
        this(fileSystem, Path.parse(path));
    }

    File(FileSystem fileSystem, Path path)
    {
        super(fileSystem, path);
    }

    /**
     * Get the file extension (including the '.' character) for this File.
     * @return
     */
    public String getFileExtension()
    {
        return this.getPath().getFileExtension();
    }

    /**
     * Get the name of this File without the file extension.
     * @return The name of this File without the file extension.
     */
    public String getNameWithoutFileExtension()
    {
        return this.getPath().withoutFileExtension().getSegments().last();
    }

    public Path relativeTo(Path path)
    {
        return this.getPath().relativeTo(path);
    }

    public Path relativeTo(Folder folder)
    {
        return this.getPath().relativeTo(folder);
    }

    public Path relativeTo(Root root)
    {
        return this.getPath().relativeTo(root);
    }

    /**
     * Create this File and return whether or not it was created as a result of this function.
     * @return Whether or not this function created the file.
     */
    public Result<File> create()
    {
        return this.getFileSystem().createFile(getPath());
    }

    /**
     * Get whether or not this File exists.
     */
    @Override
    public Result<Boolean> exists()
    {
        return this.getFileSystem().fileExists(getPath());
    }

    @Override
    public Result<Void> delete()
    {
        return this.getFileSystem().deleteFile(this.getPath());
    }

    /**
     * Get the date and time of the most recent modification of this file, or null if this file
     * doesn't exist.
     * @return The date and time of the most recent modification of this file, or null if this file
     * doesn't exist.
     */
    public Result<DateTime> getLastModified()
    {
        return this.getFileSystem().getFileLastModified(this.getPath());
    }

    /**
     * Get the content data size of this file.
     * @return The content data size of this file.
     */
    public Result<DataSize> getContentsDataSize()
    {
        return this.getFileSystem().getFileContentDataSize(this.getPath());
    }

    /**
     * Get a ByteReadStream to this file's contents.
     * @return A ByteReadStream to this file's contents.
     */
    public Result<CharacterToByteReadStream> getContentsReadStream()
    {
        final FileSystem fileSystem = this.getFileSystem();
        final Path path = this.getPath();
        return fileSystem.getFileContentReadStream(path);
    }

    /**
     * Get a ByteWriteStream to this file's contents.
     * @return A ByteWriteStream to this file's contents.
     */
    public Result<BufferedByteWriteStream> getContentsByteWriteStream()
    {
        return this.getFileSystem().getFileContentsByteWriteStream(this.getPath());
    }

    /**
     * Get a ByteWriteStream to this file's contents.
     * @return A ByteWriteStream to this file's contents.
     */
    public Result<BufferedByteWriteStream> getContentsByteWriteStream(OpenWriteType openWriteType)
    {
        return this.getFileSystem().getFileContentsByteWriteStream(this.getPath(), openWriteType);
    }

    /**
     * Get a CharacterWriteStream to this file's contents.
     * @return A CharacterWriteStream to this file's contents.
     */
    public Result<CharacterToByteWriteStream> getContentsCharacterWriteStream()
    {
        return this.getFileSystem().getFileContentsCharacterWriteStream(this.getPath());
    }

    /**
     * Get a CharacterWriteStream to this file's contents.
     * @return A CharacterWriteStream to this file's contents.
     */
    public Result<CharacterToByteWriteStream> getContentsCharacterWriteStream(OpenWriteType openWriteType)
    {
        return this.getFileSystem().getFileContentsCharacterWriteStream(this.getPath(), openWriteType);
    }

    public Result<byte[]> getContents()
    {
        return this.getFileSystem().getFileContent(getPath());
    }

    public Result<String> getContentsAsString()
    {
        return this.getFileSystem().getFileContentsAsString(getPath());
    }

    public Result<Void> setContents(byte[] content)
    {
        return getFileSystem().setFileContents(getPath(), content);
    }

    public Result<Void> setContentsAsString(String content)
    {
        return getFileSystem().setFileContentAsString(getPath(), content);
    }

    public Result<Void> copyTo(Path destinationPath)
    {
        return getFileSystem().copyFileTo(this, destinationPath);
    }

    public Result<Void> copyTo(File destinationFile)
    {
        return getFileSystem().copyFileTo(this, destinationFile);
    }

    public Result<Void> copyToFolder(Path destinationFolderPath)
    {
        return getFileSystem().copyFileToFolder(this, destinationFolderPath);
    }

    public Result<Void> copyToFolder(Folder destinationFolder)
    {
        return getFileSystem().copyFileToFolder(this, destinationFolder);
    }

    @Override
    public boolean equals(Object rhs)
    {
        return rhs instanceof File && equals((File)rhs);
    }

    public boolean equals(File rhs)
    {
        return rhs != null &&
            Comparer.equal(getFileSystem(), rhs.getFileSystem()) &&
            Comparer.equal(getPath(), rhs.getPath());
    }
}
