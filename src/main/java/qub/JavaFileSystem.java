package qub;

/**
 * A FileSystem implementation that interacts with a typical Windows, Linux, or MacOS device.
 */
public class JavaFileSystem extends FileSystemBase
{
    @Override
    public Iterable<Root> getRoots()
    {
        return Array
                .fromValues(java.io.File.listRoots())
                .map(new Function1<java.io.File, Root>()
                {
                    @Override
                    public Root run(java.io.File root)
                    {
                        return new Root(JavaFileSystem.this, Path.parse(root.getAbsolutePath()));
                    }
                });
    }

    @Override
    public Iterable<FileSystemEntry> getEntries(Path containerPath)
    {
        Iterable<FileSystemEntry> result;

        if (containerPath == null)
        {
            result = new Array<>(0);
        }
        else
        {
            final java.io.File containerFile = new java.io.File(containerPath.toString());
            final java.io.File[] containerEntryFiles = containerFile.listFiles();
            if (containerEntryFiles == null || containerEntryFiles.length == 0)
            {
                result = new Array<>(0);
            }
            else
            {
                result = new Array<>(containerEntryFiles.length);
                for (int i = 0; i < containerEntryFiles.length; ++i)
                {
                    final java.io.File containerEntryFile = containerEntryFiles[i];
                    if (containerEntryFile.isFile())
                    {
                        result.set(i, new File(this, ))
                    }
                }
            }
            if (containerFile.exists() && containerFile.)
            final FileSystemContainer container = getContainer(containerPath);
        }

        return result;
    }
}
