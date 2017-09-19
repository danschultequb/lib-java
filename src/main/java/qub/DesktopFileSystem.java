package qub;

import java.io.File;

/**
 * A FileSystem implementation that interacts with a typical Windows, Linux, or MacOS device.
 */
public class DesktopFileSystem implements FileSystem
{
    @Override
    public Iterable<Root> getRoots()
    {
        final java.io.File[] roots = File.listRoots();
        final Array<Root> result = new Array<>(roots.length);
        for (int i = 0; i < roots.length; ++i)
        {
            final File root = roots[i];
            final Path rootPath = Path.parse(root.getAbsolutePath());
            result.set(i, new Root(this, rootPath));
        }
        return result;
    }
}
