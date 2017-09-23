package qub;

import org.junit.Test;

import static org.junit.Assert.*;

public abstract class FileSystemTests
{
    protected abstract FileSystem getFileSystem();

    @Test
    public void getRoot()
    {
        final FileSystem fileSystem = getFileSystem();
        final Root root1 = fileSystem.getRoot("/daffy/");
        assertFalse(root1.exists());
        assertEquals("/daffy/", root1.toString());
    }

    @Test
    public void getRoots()
    {
        final FileSystem fileSystem = getFileSystem();
        final Iterable<Root> roots = fileSystem.getRoots();
        assertNotNull(roots);
        assertTrue(1 <= roots.getCount());
    }

    @Test
    public void getEntriesForExistingPath()
    {
        final FileSystem fileSystem = getFileSystem();
        fileSystem.createFolder("/folderA");
        fileSystem.createFolder("/folderA/folderB");
        fileSystem.createFile("/file1.txt");
        fileSystem.createFile("/folderA/file2.csv");

        final Iterable<FileSystemEntry> entries = fileSystem.getEntries("/");
        assertTrue(entries.any());
        assertEquals(2, entries.getCount());
        final String[] entryPathStrings = Array.toStringArray(entries.map(new Function1<FileSystemEntry, String>()
        {
            @Override
            public String run(FileSystemEntry arg1)
            {
                return arg1.getPath().toString();
            }
        }));
        assertArrayEquals(new String[] { "/folderA", "/file1.txt" }, entryPathStrings);
    }

    @Test
    public void getEntriesForNonExistingPath()
    {
        final FileSystem fileSystem = getFileSystem();
        final Iterable<FileSystemEntry> entries = fileSystem.getEntries("/i/dont/exist/");
        assertNull(entries);
    }

    @Test
    public void getEntriesForNullPath()
    {
        final FileSystem fileSystem = getFileSystem();
        final Iterable<FileSystemEntry> entries = fileSystem.getEntries((Path)null);
        assertNull(entries);
    }

    @Test
    public void getFoldersWithNullPathString()
    {
        final FileSystem fileSystem = getFileSystem();
        final Iterable<Folder> folders = fileSystem.getFolders((String)null);
        assertNull(folders);
    }

    @Test
    public void getFoldersWithEmptyPathString()
    {
        final FileSystem fileSystem = getFileSystem();
        final Iterable<Folder> folders = fileSystem.getFolders("");
        assertNull(folders);
    }

    @Test
    public void getFilesWithNullPathString()
    {
        final FileSystem fileSystem = getFileSystem();
        final Iterable<File> files = fileSystem.getFiles((String)null);
        assertNull(files);
    }

    @Test
    public void getFilesWithEmptyPathString()
    {
        final FileSystem fileSystem = getFileSystem();
        final Iterable<File> files = fileSystem.getFiles("");
        assertNull(files);
    }

    @Test
public void getFolderWithNullString()
{
    final FileSystem fileSystem = getFileSystem();
    final Folder folder = fileSystem.getFolder((String)null);
    assertNull(folder);
}

    @Test
    public void getFolderWithEmptyString()
    {
        final FileSystem fileSystem = getFileSystem();
        final Folder folder = fileSystem.getFolder("");
        assertNull(folder);
    }

    @Test
    public void getFolderWithNonRootedString()
    {
        final FileSystem fileSystem = getFileSystem();
        final Folder folder = fileSystem.getFolder("a/b/c");
        assertNull(folder);
    }

    @Test
    public void getFolderWithForwardSlashRoot()
    {
        final FileSystem fileSystem = getFileSystem();
        final Folder folder = fileSystem.getFolder("/");
        assertNotNull(folder);
        assertEquals("/", folder.toString());
    }

    @Test
    public void getFolderWithBackslashRoot()
    {
        final FileSystem fileSystem = getFileSystem();
        final Folder folder = fileSystem.getFolder("\\");
        assertNotNull(folder);
        assertEquals("\\", folder.toString());
    }

    @Test
    public void getFolderWithWindowsRoot()
    {
        final FileSystem fileSystem = getFileSystem();
        final Folder folder = fileSystem.getFolder("Z:\\");
        assertNotNull(folder);
        assertEquals("Z:\\", folder.toString());
    }

    @Test
    public void getFolderWithRootAndFolder()
    {
        final FileSystem fileSystem = getFileSystem();
        final Folder folder = fileSystem.getFolder("/a/b");
        assertNotNull(folder);
        assertEquals("/a/b", folder.toString());
    }

    @Test
    public void folderExistsWithRootPathString()
    {
        final FileSystem fileSystem = getFileSystem();
        final Folder folder = fileSystem.getRoots().first();
        assertTrue(fileSystem.folderExists(folder.getPath().toString()));
    }

    @Test
    public void folderExistsWithExistingFolderPathString()
    {
        final FileSystem fileSystem = getFileSystem();
        fileSystem.createFolder("/folderName");
        assertTrue(fileSystem.folderExists("/folderName"));
    }

    @Test
    public void createFolderWithNullString()
    {
        final FileSystem fileSystem = getFileSystem();
        assertFalse(fileSystem.createFolder((String)null));
    }

    @Test
    public void createFolderWithEmptyString()
    {
        final FileSystem fileSystem = getFileSystem();
        assertFalse(fileSystem.createFolder(""));
    }

    @Test
    public void createFolderWithNullStringAndNullValue()
    {
        final FileSystem fileSystem = getFileSystem();
        assertFalse(fileSystem.createFolder((String)null, null));
    }

    @Test
    public void createFolderWithEmptyStringAndNullValue()
    {
        final FileSystem fileSystem = getFileSystem();
        assertFalse(fileSystem.createFolder("", null));
    }

    @Test
    public void createFolderWithNullPath()
    {
        final FileSystem fileSystem = getFileSystem();
        assertFalse(fileSystem.createFolder((Path)null));
    }

    @Test
    public void deleteFolderWithNullString()
    {
        final FileSystem fileSystem = getFileSystem();
        assertFalse(fileSystem.deleteFolder((String)null));
    }

    @Test
    public void deleteFolderWithEmptyString()
    {
        final FileSystem fileSystem = getFileSystem();
        assertFalse(fileSystem.deleteFolder(""));
    }

    @Test
    public void deleteFolderWithNonRootedFolderPathString()
    {
        final FileSystem fileSystem = getFileSystem();
        fileSystem.createFolder("/folder");
        assertFalse(fileSystem.deleteFolder("folder"));
    }

    @Test
    public void deleteFolderWithNonExistingFolderPathString()
    {
        final FileSystem fileSystem = getFileSystem();
        assertFalse(fileSystem.deleteFolder("/folder"));
    }

    @Test
    public void deleteFolderWithExistingFolderPathString()
    {
        final FileSystem fileSystem = getFileSystem();
        fileSystem.createFolder("/folder/");
        assertTrue(fileSystem.deleteFolder("/folder/"));
        assertFalse(fileSystem.deleteFolder("/folder/"));
    }

    @Test
    public void getFileWithNullString()
    {
        final FileSystem fileSystem = getFileSystem();
        final File file = fileSystem.getFile((String)null);
        assertNull(file);
    }

    @Test
    public void getFileWithEmptyString()
    {
        final FileSystem fileSystem = getFileSystem();
        final File file = fileSystem.getFile("");
        assertNull(file);
    }

    @Test
    public void getFileWithNonRootedString()
    {
        final FileSystem fileSystem = getFileSystem();
        final File file = fileSystem.getFile("a/b/c");
        assertNull(file);
    }

    @Test
    public void getFileWithForwardSlashRoot()
    {
        final FileSystem fileSystem = getFileSystem();
        final File file = fileSystem.getFile("/");
        assertNull(file);
    }

    @Test
    public void getFileWithBackslashRoot()
    {
        final FileSystem fileSystem = getFileSystem();
        final File file = fileSystem.getFile("\\");
        assertNull(file);
    }

    @Test
    public void getFileWithWindowsRoot()
    {
        final FileSystem fileSystem = getFileSystem();
        final File file = fileSystem.getFile("Z:\\");
        assertNull(file);
    }

    @Test
    public void getFileWithRootAndFile()
    {
        final FileSystem fileSystem = getFileSystem();
        final File file = fileSystem.getFile("/a/b");
        assertNotNull(file);
        assertEquals("/a/b", file.toString());
    }

    @Test
    public void fileExistsWithRootPathString()
    {
        final FileSystem fileSystem = getFileSystem();
        final Root root = fileSystem.getRoots().first();
        assertFalse(fileSystem.fileExists(root.getPath().toString()));
    }

    @Test
    public void fileExistsWithExistingFolderPathString()
    {
        final FileSystem fileSystem = getFileSystem();
        fileSystem.createFolder("/folderName");
        assertFalse(fileSystem.fileExists("/folerName"));
    }

    @Test
    public void fileExistsWithExistingFilePathString()
    {
        final FileSystem fileSystem = getFileSystem();
        fileSystem.createFile("/file1.xml");
        assertTrue(fileSystem.fileExists("/file1.xml"));
    }
}
