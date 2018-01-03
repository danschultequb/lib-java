package qub;

import org.junit.Test;

import static org.junit.Assert.*;

public class FolderTests
{
    @Test
    public void exists()
    {
        final Folder folder = getFolder();
        assertFalse(folder.exists());

        folder.create();
        assertTrue(folder.exists());
    }

    @Test
    public void deleteWhenFolderDoesntExist()
    {
        final Folder folder = getFolder();
        assertFalse(folder.delete());
        assertFalse(folder.exists());
    }

    @Test
    public void deleteWhenFolderExists()
    {
        final Folder folder = getFolder();
        folder.create();

        assertTrue(folder.delete());
        assertFalse(folder.exists());
    }

    @Test
    public void create()
    {
        final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
        fileSystem.createRoot("/");

        final Folder folder = fileSystem.getFolder("/test/folder");
        assertFalse(folder.exists());

        assertTrue(folder.create());
        assertTrue(folder.exists());

        assertFalse(folder.create());
        assertTrue(folder.exists());
    }

    @Test
    public void createFolderWithNullString()
    {
        final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
        fileSystem.createRoot("/");

        final Folder folder = fileSystem.getFolder("/test/folder");
        assertFalse(folder.createFolder((String)null));
        assertFalse(folder.exists());
    }

    @Test
    public void createFolderWithEmptyString()
    {
        final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
        fileSystem.createRoot("/");

        final Folder folder = fileSystem.getFolder("/test/folder");
        assertFalse(folder.createFolder(""));
        assertFalse(folder.exists());
    }

    @Test
    public void createFolderWithNonEmptyRelativeString()
    {
        final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
        fileSystem.createRoot("/");

        final Folder folder = fileSystem.getFolder("/test/folder");
        assertTrue(folder.createFolder("thing"));
        assertTrue(folder.exists());
    }

    @Test
    public void createFolderWithNullPath()
    {
        final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
        fileSystem.createRoot("/");

        final Folder folder = fileSystem.getFolder("/test/folder");
        assertFalse(folder.createFolder((Path)null));
        assertFalse(folder.exists());
    }

    @Test
    public void createFolderWithEmptyPath()
    {
        final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
        fileSystem.createRoot("/");

        final Folder folder = fileSystem.getFolder("/test/folder");
        assertFalse(folder.createFolder(Path.parse("")));
        assertFalse(folder.exists());
    }

    @Test
    public void createFolderWithNonEmptyPath()
    {
        final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
        fileSystem.createRoot("/");

        final Folder folder = fileSystem.getFolder("/test/folder");
        assertTrue(folder.createFolder(Path.parse("place")));
        assertTrue(folder.exists());
    }

    @Test
    public void createFolderWithNonEmptyPathAndOutputFolder()
    {
        final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
        fileSystem.createRoot("/");

        final Folder folder = fileSystem.getFolder("/test/folder");
        final Value<Folder> childFolder = new Value<>();
        assertTrue(folder.createFolder(Path.parse("place"), childFolder));
        assertTrue(folder.exists());
        assertNotNull(childFolder.get());
        assertTrue(childFolder.get().exists());
    }

    @Test
    public void createFileWithNullString()
    {
        final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
        fileSystem.createRoot("/");

        final Folder folder = fileSystem.getFolder("/test/folder");
        assertFalse(folder.createFile((String)null));
        assertFalse(folder.exists());
    }

    @Test
    public void createFileWithEmptyString()
    {
        final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
        fileSystem.createRoot("/");

        final Folder folder = fileSystem.getFolder("/test/folder");
        assertFalse(folder.createFile(""));
        assertFalse(folder.exists());
    }

    @Test
    public void createFileWithFileNameString()
    {
        final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
        fileSystem.createRoot("/");

        final Folder folder = fileSystem.getFolder("/test/folder");
        assertTrue(folder.createFile("file.xml"));
        assertTrue(folder.exists());
    }

    @Test
    public void createFileWithBackslashRelativeFilePathString()
    {
        final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
        fileSystem.createRoot("/");

        final Folder folder = fileSystem.getFolder("/test/folder");
        assertTrue(folder.createFile("subfolder\\file.xml"));
        assertTrue(folder.exists());
        assertFalse(folder.getFiles().any());
    }

    @Test
    public void createFileWithForwardSlashRelativeFilePathString()
    {
        final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
        fileSystem.createRoot("/");

        final Folder folder = fileSystem.getFolder("/test/folder");
        assertTrue(folder.createFile("subfolder/file.xml"));
        assertTrue(folder.exists());
        assertFalse(folder.getFiles().any());
    }

    @Test
    public void createFileWithNullStringAndNullValue()
    {
        final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
        fileSystem.createRoot("/");

        final Folder folder = fileSystem.getFolder("/test/folder");
        assertFalse(folder.createFile((String)null, null));
        assertFalse(folder.exists());
    }

    @Test
    public void createFileWithEmptyStringAndNullValue()
    {
        final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
        fileSystem.createRoot("/");

        final Folder folder = fileSystem.getFolder("/test/folder");
        assertFalse(folder.createFile("", null));
        assertFalse(folder.exists());
    }

    @Test
    public void createFileWithFileNameStringAndNullValue()
    {
        final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
        fileSystem.createRoot("/");

        final Folder folder = fileSystem.getFolder("/test/folder");
        assertTrue(folder.createFile("file.xml", null));
        assertTrue(folder.exists());
    }

    @Test
    public void createFileWithBackslashRelativeFilePathStringAndNullValue()
    {
        final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
        fileSystem.createRoot("/");

        final Folder folder = fileSystem.getFolder("/test/folder");
        assertTrue(folder.createFile("subfolder\\file.xml", null));
        assertTrue(folder.exists());
        assertFalse(folder.getFiles().any());
    }

    @Test
    public void createFileWithForwardSlashRelativeFilePathStringAndNullValue()
    {
        final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
        fileSystem.createRoot("/");

        final Folder folder = fileSystem.getFolder("/test/folder");
        assertTrue(folder.createFile("subfolder/file.xml", null));
        assertTrue(folder.exists());
        assertFalse(folder.getFiles().any());
    }

    @Test
    public void createFileWithNullStringAndNonNullValue()
    {
        final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
        fileSystem.createRoot("/");

        final Folder folder = fileSystem.getFolder("/test/folder");
        final Value<File> file = new Value<>();
        assertFalse(folder.createFile((String)null, file));
        assertFalse(folder.exists());
        assertFalse(file.hasValue());
    }

    @Test
    public void createFileWithEmptyStringAndNonNullValue()
    {
        final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
        fileSystem.createRoot("/");

        final Folder folder = fileSystem.getFolder("/test/folder");
        final Value<File> file = new Value<>();
        assertFalse(folder.createFile("", file));
        assertFalse(folder.exists());
        assertFalse(file.hasValue());
    }

    @Test
    public void createFileWithFileNameStringAndNonNullValue()
    {
        final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
        fileSystem.createRoot("/");

        final Folder folder = fileSystem.getFolder("/test/folder");
        final Value<File> file = new Value<>();
        assertTrue(folder.createFile("file.xml", file));
        assertTrue(folder.exists());
        assertNotNull(file.get());
        assertEquals(Path.parse("/test/folder/file.xml"), file.get().getPath());
        assertTrue(file.get().exists());
    }

    @Test
    public void createFileWithBackslashRelativeFilePathStringAndNonNullValue()
    {
        final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
        fileSystem.createRoot("/");

        final Folder folder = fileSystem.getFolder("/test/folder");
        final Value<File> file = new Value<>();
        assertTrue(folder.createFile("subfolder\\file.xml", file));
        assertTrue(folder.exists());
        assertFalse(folder.getFiles().any());
        assertNotNull(file.get());
        assertEquals(Path.parse("/test/folder/subfolder/file.xml"), file.get().getPath());
        assertTrue(file.get().exists());
    }

    @Test
    public void createFileWithForwardSlashRelativeFilePathStringAndNonNullValue()
    {
        final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
        fileSystem.createRoot("/");

        final Folder folder = fileSystem.getFolder("/test/folder");
        final Value<File> file = new Value<>();
        assertTrue(folder.createFile("subfolder/file.xml", file));
        assertTrue(folder.exists());
        assertFalse(folder.getFiles().any());
        assertNotNull(file.get());
        assertEquals(Path.parse("/test/folder/subfolder/file.xml"), file.get().getPath());
        assertTrue(file.get().exists());
    }

    @Test
    public void createFileWithNullPath()
    {
        final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
        fileSystem.createRoot("/");

        final Folder folder = fileSystem.getFolder("/test/folder");
        assertFalse(folder.createFile((Path)null));
        assertFalse(folder.exists());
    }

    @Test
    public void createFileWithEmptyPath()
    {
        final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
        fileSystem.createRoot("/");

        final Folder folder = fileSystem.getFolder("/test/folder");
        assertFalse(folder.createFile(Path.parse("")));
        assertFalse(folder.exists());
    }

    @Test
    public void createFileWithFileNamePath()
    {
        final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
        fileSystem.createRoot("/");

        final Folder folder = fileSystem.getFolder("/test/folder");
        assertTrue(folder.createFile(Path.parse("file.xml")));
        assertTrue(folder.exists());
    }

    @Test
    public void createFileWithBackslashRelativeFilePath()
    {
        final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
        fileSystem.createRoot("/");

        final Folder folder = fileSystem.getFolder("/test/folder");
        assertTrue(folder.createFile(Path.parse("subfolder\\file.xml")));
        assertTrue(folder.exists());
        assertFalse(folder.getFiles().any());
    }

    @Test
    public void createFileWithForwardSlashRelativeFilePath()
    {
        final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
        fileSystem.createRoot("/");

        final Folder folder = fileSystem.getFolder("/test/folder");
        assertTrue(folder.createFile(Path.parse("subfolder/file.xml")));
        assertTrue(folder.exists());
        assertFalse(folder.getFiles().any());
    }

    @Test
    public void createFileWithNullPathAndNullValue()
    {
        final Folder folder = getFolder();
        assertFalse(folder.createFile((Path)null, null));
        assertFalse(folder.exists());
    }

    @Test
    public void createFileWithEmptyPathAndNullValue()
    {
        final Folder folder = getFolder();
        assertFalse(folder.createFile(Path.parse(""), null));
        assertFalse(folder.exists());
    }

    @Test
    public void createFileWithFileNamePathAndNullValue()
    {
        final Folder folder = getFolder();
        assertTrue(folder.createFile(Path.parse("file.xml"), null));
        assertTrue(folder.exists());
    }

    @Test
    public void createFileWithBackslashRelativeFilePathAndNullValue()
    {
        final Folder folder = getFolder();
        assertTrue(folder.createFile(Path.parse("subfolder\\file.xml"), null));
        assertTrue(folder.exists());
        assertFalse(folder.getFiles().any());
    }

    @Test
    public void createFileWithForwardSlashRelativeFilePathAndNullValue()
    {
        final Folder folder = getFolder();
        assertTrue(folder.createFile(Path.parse("subfolder/file.xml"), null));
        assertTrue(folder.exists());
        assertFalse(folder.getFiles().any());
    }

    @Test
    public void createFileWithNullPathAndNonNullValue()
    {
        final Folder folder = getFolder();
        final Value<File> file = new Value<>();
        assertFalse(folder.createFile((Path)null, file));
        assertFalse(folder.exists());
        assertFalse(file.hasValue());
    }

    @Test
    public void createFileWithEmptyAndNonNullValue()
    {
        final Folder folder = getFolder();
        final Value<File> file = new Value<>();
        assertFalse(folder.createFile(Path.parse(""), file));
        assertFalse(folder.exists());
        assertFalse(file.hasValue());
    }

    @Test
    public void createFileWithFileNameAndNonNullValue()
    {
        final Folder folder = getFolder();
        final Value<File> file = new Value<>();
        assertTrue(folder.createFile(Path.parse("file.xml"), file));
        assertTrue(folder.exists());
        assertNotNull(file.get());
        assertEquals(Path.parse("/test/folder/file.xml"), file.get().getPath());
        assertTrue(file.get().exists());
    }

    @Test
    public void createFileWithBackslashRelativeFilePathAndNonNullValue()
    {
        final Folder folder = getFolder();
        final Value<File> file = new Value<>();
        assertTrue(folder.createFile(Path.parse("subfolder\\file.xml"), file));
        assertTrue(folder.exists());
        assertFalse(folder.getFiles().any());
        assertNotNull(file.get());
        assertEquals(Path.parse("/test/folder/subfolder/file.xml"), file.get().getPath());
        assertTrue(file.get().exists());
    }

    @Test
    public void createFileWithForwardSlashRelativeFilePathAndNonNullValue()
    {
        final Folder folder = getFolder();
        final Value<File> file = new Value<>();
        assertTrue(folder.createFile(Path.parse("subfolder/file.xml"), file));
        assertTrue(folder.exists());
        assertFalse(folder.getFiles().any());
        assertNotNull(file.get());
        assertEquals(Path.parse("/test/folder/subfolder/file.xml"), file.get().getPath());
        assertTrue(file.get().exists());
    }

    @Test
    public void getFolders()
    {
        final Folder folder = getFolder();
        assertFalse(folder.exists());
        assertNull(folder.getFolders());

        folder.create();
        assertArrayEquals(new String[0], Array.toStringArray(folder.getFolders().map(new Function1<Folder, String>()
        {
            @Override
            public String run(Folder childFolder)
            {
                return childFolder.getPath().toString();
            }
        })));

        final Value<Folder> childFolder = new Value<>();
        assertTrue(folder.createFolder("childFolder1", childFolder));
        assertArrayEquals(new String[] { "/test/folder/childFolder1" }, Array.toStringArray(folder.getFolders().map(new Function1<Folder, String>()
        {
            @Override
            public String run(Folder childFolder)
            {
                return childFolder.getPath().toString();
            }
        })));

        assertTrue(childFolder.get().createFolder("grandchildFolder1"));
        assertArrayEquals(new String[] { "/test/folder/childFolder1" }, Array.toStringArray(folder.getFolders().map(new Function1<Folder, String>()
        {
            @Override
            public String run(Folder childFolder)
            {
                return childFolder.getPath().toString();
            }
        })));
    }

    @Test
    public void getFilesWhenFolderDoesntExist()
    {
        final Folder folder = getFolder();
        assertFalse(folder.exists());
        assertNull(folder.getFiles());
    }

    @Test
    public void getFilesWhenFolderExistsButDoesntHaveChildFiles()
    {
        final Folder folder = getFolder();
        assertTrue(folder.create());
        assertTrue(folder.exists());
        final Iterable<File> files = folder.getFiles();
        assertArrayEquals(new String[0], Array.toStringArray(files.map(new Function1<File, String>()
        {
            @Override
            public String run(File childFile)
            {
                return childFile.getPath().toString();
            }
        })));
    }

    @Test
    public void getFilesWhenFolderExistsAndHasOneChildFile()
    {
        final Folder folder = getFolder();
        assertTrue(folder.create());
        assertTrue(folder.exists());
        assertTrue(folder.createFile("data.txt"));
        final Iterable<File> files = folder.getFiles();
        assertArrayEquals(new String[] { "/test/folder/data.txt" }, Array.toStringArray(files.map(new Function1<File, String>()
        {
            @Override
            public String run(File childFile)
            {
                return childFile.getPath().toString();
            }
        })));
    }

    @Test
    public void getFilesWhenFolderExistsAndHasOneGrandchildFile()
    {
        final Folder folder = getFolder();
        assertTrue(folder.create());
        assertTrue(folder.exists());
        assertTrue(folder.createFile("subfolder/data.txt"));
        final Iterable<File> files = folder.getFiles();
        assertArrayEquals(new String[0], Array.toStringArray(files.map(new Function1<File, String>()
        {
            @Override
            public String run(File childFile)
            {
                return childFile.getPath().toString();
            }
        })));
    }

    @Test
    public void getFilesAndFoldersWhenFolderDoesntExist()
    {
        final Folder folder = getFolder();
        assertFalse(folder.exists());

        final Iterable<FileSystemEntry> filesAndFolders = folder.getFilesAndFolders();
        assertNull(filesAndFolders);
    }

    @Test
    public void getFilesAndFoldersWhenFolderExistsButIsEmpty()
    {
        final Folder folder = getFolder();
        assertTrue(folder.create());

        final Iterable<FileSystemEntry> filesAndFolders = folder.getFilesAndFolders();
        assertNotNull(filesAndFolders);
        assertFalse(filesAndFolders.any());
    }

    @Test
    public void getFilesAndFoldersWhenFolderHasOneChildFolder()
    {
        final Folder folder = getFolder();
        assertTrue(folder.create());
        assertTrue(folder.createFolder("subfolder"));

        final Iterable<FileSystemEntry> filesAndFolders = folder.getFilesAndFolders();
        assertNotNull(filesAndFolders);
        assertTrue(filesAndFolders.any());
        assertArrayEquals(new String[] { "/test/folder/subfolder" }, Array.toStringArray(filesAndFolders.map(new Function1<FileSystemEntry, String>()
        {
            @Override
            public String run(FileSystemEntry childEntry)
            {
                return childEntry.getPath().toString();
            }
        })));
    }

    @Test
    public void getFilesAndFoldersWhenFolderHasOneChildFile()
    {
        final Folder folder = getFolder();
        assertTrue(folder.create());
        assertTrue(folder.createFile("file.java"));

        final Iterable<FileSystemEntry> filesAndFolders = folder.getFilesAndFolders();
        assertNotNull(filesAndFolders);
        assertTrue(filesAndFolders.any());
        assertArrayEquals(new String[] { "/test/folder/file.java" }, Array.toStringArray(filesAndFolders.map(new Function1<FileSystemEntry, String>()
        {
            @Override
            public String run(FileSystemEntry childEntry)
            {
                return childEntry.getPath().toString();
            }
        })));
    }

    @Test
    public void getFilesAndFoldersWhenFolderHasOneChildFileAndOneChildFolder()
    {
        final Folder folder = getFolder();
        assertTrue(folder.create());
        assertTrue(folder.createFile("file.java"));
        assertTrue(folder.createFolder("childfolder"));

        final Iterable<FileSystemEntry> filesAndFolders = folder.getFilesAndFolders();
        assertNotNull(filesAndFolders);
        assertTrue(filesAndFolders.any());
        assertArrayEquals(new String[] { "/test/folder/childfolder", "/test/folder/file.java" }, Array.toStringArray(filesAndFolders.map(new Function1<FileSystemEntry, String>()
        {
            @Override
            public String run(FileSystemEntry childEntry)
            {
                return childEntry.getPath().toString();
            }
        })));
    }

    @Test
    public void fileExistsWhenFileDoesntExist()
    {
        final Folder folder = getFolder();
        assertFalse(folder.fileExists("test.txt"));
    }

    @Test
    public void fileExistsWhenFileExists()
    {
        final Folder folder = getFolder();
        folder.createFile("test.txt");
        assertTrue(folder.fileExists("test.txt"));
    }

    @Test
    public void folderExistsWhenFolderDoesntExist()
    {
        final Folder folder = getFolder();
        assertFalse(folder.folderExists("test"));
    }

    @Test
    public void folderExistsWhenFolderExists()
    {
        final Folder folder = getFolder();
        folder.createFolder("test");
        assertTrue(folder.folderExists("test"));
    }

    private static Folder getFolder()
    {
        final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
        fileSystem.createRoot("/");

        return fileSystem.getFolder("/test/folder");
    }
}
