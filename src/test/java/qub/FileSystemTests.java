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
    public void getFilesAndFoldersForNullStringPath()
    {
        final FileSystem fileSystem = getFileSystem();
        final Iterable<FileSystemEntry> entries = fileSystem.getFilesAndFolders((String)null);
        assertNull(entries);
    }

    @Test
    public void getFilesAndFoldersForEmptyStringPath()
    {
        final FileSystem fileSystem = getFileSystem();
        final Iterable<FileSystemEntry> entries = fileSystem.getFilesAndFolders("");
        assertNull(entries);
    }

    @Test
    public void getFilesAndFoldersForNullPath()
    {
        final FileSystem fileSystem = getFileSystem();
        final Iterable<FileSystemEntry> entries = fileSystem.getFilesAndFolders((Path)null);
        assertNull(entries);
    }

    @Test
    public void getFilesAndFoldersForNonExistingPath()
    {
        final FileSystem fileSystem = getFileSystem();
        final Iterable<FileSystemEntry> entries = fileSystem.getFilesAndFolders("/i/dont/exist/");
        assertNull(entries);
    }

    @Test
    public void getFilesAndFoldersForExistingPathWithNoContents()
    {
        final FileSystem fileSystem = getFileSystem();
        fileSystem.createFolder("/folderA");
        assertFalse(fileSystem.getFilesAndFolders("/folderA").any());
    }

    @Test
    public void getFilesAndFoldersForExistingPath()
    {
        final FileSystem fileSystem = getFileSystem();
        fileSystem.createFolder("/folderA");
        fileSystem.createFolder("/folderA/folderB");
        fileSystem.createFile("/file1.txt");
        fileSystem.createFile("/folderA/file2.csv");

        final Iterable<FileSystemEntry> entries = fileSystem.getFilesAndFolders("/");
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
    public void createFolderWithRelativePathString()
    {
        final FileSystem fileSystem = getFileSystem();
        assertFalse(fileSystem.createFolder("folder"));
    }

    @Test
    public void createFolderWithRootedPathString()
    {
        final FileSystem fileSystem = getFileSystem();
        assertTrue(fileSystem.createFolder("/folder"));
        assertTrue(fileSystem.folderExists("/folder"));
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
    public void createFolderWithRelativePathStringAndNullValue()
    {
        final FileSystem fileSystem = getFileSystem();
        assertFalse(fileSystem.createFolder("folder", null));
    }

    @Test
    public void createFolderWithRootedPathStringAndNullValue()
    {
        final FileSystem fileSystem = getFileSystem();
        assertTrue(fileSystem.createFolder("/folder", null));
        assertTrue(fileSystem.folderExists("/folder"));
    }

    @Test
    public void createFolderWithNullStringAndNonNullValue()
    {
        final FileSystem fileSystem = getFileSystem();
        final Value<Folder> folder = new Value<>();
        assertFalse(fileSystem.createFolder((String)null, folder));
        assertFalse(folder.hasValue());
    }

    @Test
    public void createFolderWithEmptyStringAndNonNullValue()
    {
        final FileSystem fileSystem = getFileSystem();
        final Value<Folder> folder = new Value<>();
        assertFalse(fileSystem.createFolder("", folder));
        assertFalse(folder.hasValue());
    }

    @Test
    public void createFolderWithRelativePathStringAndNonNullValue()
    {
        final FileSystem fileSystem = getFileSystem();
        final Value<Folder> folder = new Value<>();
        assertFalse(fileSystem.createFolder("folder", folder));
        assertFalse(folder.hasValue());
    }

    @Test
    public void createFolderWithRootedPathStringAndNonNullValue()
    {
        final FileSystem fileSystem = getFileSystem();
        final Value<Folder> folder = new Value<>();
        assertTrue(fileSystem.createFolder("/folder", folder));
        assertTrue(fileSystem.folderExists("/folder"));
        assertTrue(folder.hasValue());
        assertNotNull(folder.get());
        assertEquals("/folder", folder.get().getPath().toString());
    }

    @Test
    public void createFolderWithNullPathAndNonNullValue()
    {
        final FileSystem fileSystem = getFileSystem();
        final Value<Folder> folder = new Value<>();
        assertFalse(fileSystem.createFolder((Path)null, folder));
        assertFalse(folder.hasValue());
        assertNull(folder.get());
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
    public void deleteFolderWithExistingFolderPathStringWithSiblingFolders()
    {
        final FileSystem fileSystem = getFileSystem();
        fileSystem.createFolder("/folder/a");
        fileSystem.createFolder("/folder/b");
        fileSystem.createFolder("/folder/c");
        assertTrue(fileSystem.deleteFolder("/folder/c"));
        assertFalse(fileSystem.deleteFolder("/folder/c"));
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

    @Test
    public void createFileWithNullString()
    {
        final FileSystem fileSystem = getFileSystem();
        assertFalse(fileSystem.createFile((String)null));
    }

    @Test
    public void createFileWithEmptyString()
    {
        final FileSystem fileSystem = getFileSystem();
        assertFalse(fileSystem.createFile(""));
    }

    @Test
    public void createFileWithRelativePathString()
    {
        final FileSystem fileSystem = getFileSystem();
        final boolean fileCreated = fileSystem.createFile("things.txt");
        assertFalse(fileCreated);
    }

    @Test
    public void createFileWithRootedPathString()
    {
        final FileSystem fileSystem = getFileSystem();
        final boolean fileCreated = fileSystem.createFile("/things.txt");
        assertTrue(fileCreated);

        final boolean fileCreatedAgain = fileSystem.createFile("/things.txt");
        assertFalse(fileCreatedAgain);
    }

    @Test
    public void createFileWithNullStringAndNullValue()
    {
        final FileSystem fileSystem = getFileSystem();
        assertFalse(fileSystem.createFile((String)null, null));
    }

    @Test
    public void createFileWithEmptyStringAndNullValue()
    {
        final FileSystem fileSystem = getFileSystem();
        assertFalse(fileSystem.createFile("", null));
    }

    @Test
    public void createFileWithRelativePathStringAndNullValue()
    {
        final FileSystem fileSystem = getFileSystem();
        final boolean fileCreated = fileSystem.createFile("things.txt", null);
        assertFalse(fileCreated);
    }

    @Test
    public void createFileWithRootedPathStringAndNullValue()
    {
        final FileSystem fileSystem = getFileSystem();
        final boolean fileCreated = fileSystem.createFile("/things.txt", null);
        assertTrue(fileCreated);

        final boolean fileCreatedAgain = fileSystem.createFile("/things.txt", null);
        assertFalse(fileCreatedAgain);
    }

    @Test
    public void createFileWithNullStringAndNonNullValue()
    {
        final FileSystem fileSystem = getFileSystem();
        final Value<File> file = new Value<>();
        assertFalse(fileSystem.createFile((String)null, file));
    }

    @Test
    public void createFileWithEmptyStringAndNonNullValue()
    {
        final FileSystem fileSystem = getFileSystem();
        final Value<File> file = new Value<>();
        assertFalse(fileSystem.createFile("", file));
    }

    @Test
    public void createFileWithRelativePathStringAndNonNullValue()
    {
        final FileSystem fileSystem = getFileSystem();
        final Value<File> file = new Value<>();
        final boolean fileCreated = fileSystem.createFile("things.txt", file);
        assertFalse(fileCreated);
        assertFalse(file.hasValue());
    }

    @Test
    public void createFileWithRootedPathStringAndNonNullValue()
    {
        final FileSystem fileSystem = getFileSystem();
        final Value<File> file = new Value<>();
        final boolean fileCreated = fileSystem.createFile("/things.txt", file);
        assertTrue(fileCreated);
        assertTrue(file.hasValue());
        assertNotNull(file.get());
        assertEquals("/things.txt", file.get().getPath().toString());

        final boolean fileCreatedAgain = fileSystem.createFile("/things.txt", file);
        assertFalse(fileCreatedAgain);
        assertTrue(file.hasValue());
        assertNotNull(file.get());
        assertEquals("/things.txt", file.get().getPath().toString());
    }

    @Test
    public void createFileWithInvalidPath()
    {
        final FileSystem fileSystem = getFileSystem();
        final Value<File> file = new Value<>();
        final boolean fileCreated = fileSystem.createFile("/\u0000?#!.txt", file);
        assertFalse("Wrong fileCreated", fileCreated);
        assertFalse("Wrong file.hasValue()", file.hasValue());
        assertNull("Wrong file.get()", file.get());
    }

    @Test
    public void deleteFileWithNullPathString()
    {
        final FileSystem fileSystem = getFileSystem();
        assertFalse(fileSystem.deleteFile((String)null));
    }

    @Test
    public void deleteFileWithEmptyPathString()
    {
        final FileSystem fileSystem = getFileSystem();
        assertFalse(fileSystem.deleteFile(""));
    }

    @Test
    public void deleteFileWithRelativePathString()
    {
        final FileSystem fileSystem = getFileSystem();
        assertFalse(fileSystem.deleteFile("relativeFile.txt"));
    }

    @Test
    public void deleteFileWithRootedPathStringThatDoesntExist()
    {
        final FileSystem fileSystem = getFileSystem();
        assertFalse(fileSystem.deleteFile("/idontexist.txt"));
    }

    @Test
    public void deleteFileWithRootedPathStringThatExists()
    {
        final FileSystem fileSystem = getFileSystem();
        fileSystem.createFile("/iexist.txt");

        assertTrue(fileSystem.deleteFile("/iexist.txt"));
        assertFalse(fileSystem.fileExists("/iexist.txt"));

        assertFalse(fileSystem.deleteFile("/iexist.txt"));
        assertFalse(fileSystem.fileExists("/iexist.txt"));
    }
}
