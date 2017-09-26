package qub;

import org.junit.Test;

import static org.junit.Assert.*;

public class FolderTests
{
    @Test
    public void exists()
    {
        final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
        fileSystem.createRoot("/");

        final Folder folder = fileSystem.getFolder("/test/folder");
        assertFalse(folder.exists());

        fileSystem.createFolder(folder.getPath());
        assertTrue(folder.exists());
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
    public void getFolders()
    {
        final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
        fileSystem.createRoot("/");

        final Folder folder = fileSystem.getFolder("/test/folder");
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
        final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
        fileSystem.createRoot("/");

        final Folder folder = fileSystem.getFolder("/test/folder");
        assertFalse(folder.exists());
        assertNull(folder.getFiles());
    }

    @Test
    public void getFilesWhenFolderExistsButDoesntHaveChildFiles()
    {
        final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
        fileSystem.createRoot("/");

        final Folder folder = fileSystem.getFolder("/test/folder");
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
        final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
        fileSystem.createRoot("/");

        final Folder folder = fileSystem.getFolder("/test/folder");
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
        final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
        fileSystem.createRoot("/");

        final Folder folder = fileSystem.getFolder("/test/folder");
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
}
