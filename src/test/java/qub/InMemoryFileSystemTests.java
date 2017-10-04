package qub;

import org.junit.Test;

import static org.junit.Assert.*;

public class InMemoryFileSystemTests extends FileSystemTests
{
    @Override
    protected InMemoryFileSystem getFileSystem()
    {
        final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
        fileSystem.createRoot("/");
        return fileSystem;
    }

    @Test
    public void setFileCanDeleteWhenRootDoesntExist()
    {
        final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
        assertFalse(fileSystem.setFileCanDelete("C:\\folder\\file.bmp", true));
    }

    @Test
    public void setFileCanDeleteWhenParentFolderDoesntExist()
    {
        final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
        fileSystem.createRoot("C:\\");
        assertFalse(fileSystem.setFileCanDelete("C:\\folder\\file.bmp", true));
    }

    @Test
    public void setFileCanDeleteWhenFileDoesntExist()
    {
        final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
        fileSystem.createRoot("C:\\");
        fileSystem.createFolder("C:\\folder");
        assertFalse(fileSystem.setFileCanDelete("C:\\folder\\file.bmp", true));
    }

    @Test
    public void setFileCanDeleteWhenFileExists()
    {
        final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
        fileSystem.createRoot("C:\\");
        fileSystem.createFolder("C:\\folder");
        fileSystem.createFile("C:\\folder\\file.bmp");
        assertTrue(fileSystem.setFileCanDelete("C:\\folder\\file.bmp", true));
    }

    @Test
    public void deleteFileWhenFileCannotBeDeleted()
    {
        final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
        fileSystem.createRoot("Z:/");
        fileSystem.createFile("Z:/file.png");
        assertTrue(fileSystem.setFileCanDelete("Z:/file.png", false));
        assertFalse(fileSystem.deleteFile("Z:/file.png"));
        assertTrue(fileSystem.fileExists("Z:/file.png"));
    }

    @Test
    public void setFolderCanDeleteWhenRootDoesntExist()
    {
        final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
        assertFalse(fileSystem.setFolderCanDelete("C:\\folder\\file.bmp", true));
    }

    @Test
    public void setFolderCanDeleteWhenParentFolderDoesntExist()
    {
        final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
        fileSystem.createRoot("C:\\");
        assertFalse(fileSystem.setFolderCanDelete("C:\\folder\\file.bmp", true));
    }

    @Test
    public void setFolderCanDeleteWhenFolderDoesntExist()
    {
        final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
        fileSystem.createRoot("C:\\");
        fileSystem.createFolder("C:\\folder");
        assertFalse(fileSystem.setFolderCanDelete("C:\\folder\\file.bmp", true));
    }

    @Test
    public void setFolderCanDeleteWhenFolderExists()
    {
        final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
        fileSystem.createRoot("C:\\");
        fileSystem.createFolder("C:\\folder\\file.bmp");
        assertTrue(fileSystem.setFolderCanDelete("C:\\folder\\file.bmp", true));
    }

    @Test
    public void deleteFolderWhenFolderCannotBeDeleted()
    {
        final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
        fileSystem.createRoot("Z:/");
        fileSystem.createFolder("Z:/file.png");
        assertTrue(fileSystem.setFolderCanDelete("Z:/file.png", false));
        assertFalse(fileSystem.deleteFolder("Z:/file.png"));
        assertTrue(fileSystem.folderExists("Z:/file.png"));
    }

    @Test
    public void deleteFolderWhenChildFileCannotBeDeleted()
    {
        final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
        fileSystem.createRoot("Z:/");
        fileSystem.createFolder("Z:/file.png");
        fileSystem.createFile("Z:/file.png/notme");
        fileSystem.setFileCanDelete("Z:/file.png/notme", false);
        assertFalse(fileSystem.deleteFolder("Z:/file.png"));
        assertTrue(fileSystem.folderExists("Z:/file.png"));
    }

    @Test
    public void deleteFolderWhenChildFileCanBeDeleted()
    {
        final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
        fileSystem.createRoot("Z:/");
        fileSystem.createFolder("Z:/file.png");
        fileSystem.createFile("Z:/file.png/notme");
        assertTrue(fileSystem.deleteFolder("Z:/file.png"));
        assertFalse(fileSystem.folderExists("Z:/file.png"));
        assertFalse(fileSystem.fileExists("Z:/file.png/notme"));
    }

    @Test
    public void deleteFolderWhenChildFolderCannotBeDeleted()
    {
        final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
        fileSystem.createRoot("Z:/");
        fileSystem.createFolder("Z:/file.png");
        fileSystem.createFolder("Z:/file.png/notme");
        fileSystem.setFolderCanDelete("Z:/file.png/notme", false);
        assertFalse(fileSystem.deleteFolder("Z:/file.png"));
        assertTrue(fileSystem.folderExists("Z:/file.png"));
    }

    @Test
    public void deleteFolderWhenChildFolderCanBeDeleted()
    {
        final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
        fileSystem.createRoot("Z:/");
        fileSystem.createFolder("Z:/file.png");
        fileSystem.createFolder("Z:/file.png/notme");
        assertTrue(fileSystem.deleteFolder("Z:/file.png"));
        assertFalse(fileSystem.folderExists("Z:/file.png"));
        assertFalse(fileSystem.folderExists("Z:/file.png/notme"));
    }
}
