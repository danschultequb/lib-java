package qub;

import org.junit.Test;

import static org.junit.Assert.*;

public class RootTests
{
    private InMemoryFileSystem getFileSystem()
    {
        final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
        fileSystem.createRoot("/");
        return fileSystem;
    }

    private Root getRoot()
    {
        return getRoot("/");
    }

    private Root getRoot(String rootPath)
    {
        final InMemoryFileSystem fileSystem = getFileSystem();
        return getRoot(fileSystem, rootPath);
    }

    private Root getRoot(FileSystem fileSystem)
    {
        return getRoot(fileSystem, "/");
    }

    private Root getRoot(FileSystem fileSystem, String rootPath)
    {
        return fileSystem.getRoot(rootPath);
    }

    @Test
    public void constructor()
    {
        final Root root = new Root(null, Path.parse("/path/to/root/"));
        assertEquals("/path/to/root/", root.toString());
    }

    @Test
    public void equalsObjectWithNull()
    {
        final Root root = getRoot();
        assertFalse(root.equals(null));
    }

    @Test
    public void equalsObjectWithString()
    {
        final Root root = getRoot();
        assertFalse(root.equals(root.toString()));
    }

    @Test
    public void equalsObjectWithSameRoot()
    {
        final Root root = getRoot();
        assertTrue(root.equals((Object)root));
    }

    @Test
    public void equalsObjectWithEqualRoot()
    {
        final InMemoryFileSystem fileSystem = getFileSystem();
        final Root root = getRoot(fileSystem);
        final Root root2 = getRoot(fileSystem);
        assertTrue(root.equals((Object)root2));
    }

    @Test
    public void equalsObjectWithDifferentRoot()
    {
        final InMemoryFileSystem fileSystem = getFileSystem();
        final Root root = getRoot(fileSystem, "C:\\");
        final Root root2 = getRoot(fileSystem, "D:\\");
        assertFalse(root.equals((Object)root2));
    }

    @Test
    public void createFolderStringWithNullString()
    {
        final Root root = getRoot();
        assertFalse(root.createFolder((String)null));
    }

    @Test
    public void createFolderStringWithEmpty()
    {
        final Root root = getRoot();
        assertFalse(root.createFolder(""));
    }

    @Test
    public void createFolderStringWithNonExistingFolder()
    {
        final Root root = getRoot();
        assertTrue(root.createFolder("folderName"));
    }

    @Test
    public void createFolderStringWithNonExistingFolderWhenRootDoesntExist()
    {
        final Root root = getRoot("C:/");
        assertFalse(root.createFolder("folderName"));
    }

    @Test
    public void createFolderStringWithExistingFolder()
    {
        final Root root = getRoot();
        root.createFolder("A");

        assertFalse(root.createFolder("A"));
    }

    @Test
    public void createFolderStringValueWithNullString()
    {
        final Root root = getRoot();
        final Value<Folder> createdFolder = new Value<>();
        assertFalse(root.createFolder((String)null, createdFolder));
        assertFalse(createdFolder.hasValue());
    }

    @Test
    public void createFolderStringValueWithEmpty()
    {
        final Root root = getRoot();
        final Value<Folder> createdFolder = new Value<>();
        assertFalse(root.createFolder(""));
        assertFalse(createdFolder.hasValue());
    }

    @Test
    public void createFolderStringValueWithNonExistingFolder()
    {
        final Root root = getRoot();
        final Value<Folder> createdFolder = new Value<>();
        assertTrue(root.createFolder("folderName", createdFolder));
        assertEquals(Path.parse("/folderName"), createdFolder.get().getPath());
    }

    @Test
    public void createFolderStringValueWithNonExistingFolderWhenRootDoesntExist()
    {
        final Root root = getRoot("C:/");
        final Value<Folder> createdFolder = new Value<>();
        assertFalse(root.createFolder("folderName", createdFolder));
        assertFalse(createdFolder.hasValue());
    }

    @Test
    public void createFolderStringValueWithExistingFolder()
    {
        final Root root = getRoot();
        root.createFolder("A");

        final Value<Folder> createdFolder = new Value<>();
        assertFalse(root.createFolder("A", createdFolder));
        assertEquals(Path.parse("/A"), createdFolder.get().getPath());
    }

    @Test
    public void createFolderPathWithNullPath()
    {
        final Root root = getRoot();
        assertFalse(root.createFolder((Path)null));
    }

    @Test
    public void createFolderPathWithEmptyPath()
    {
        final Root root = getRoot();
        assertFalse(root.createFolder(Path.parse("")));
    }

    @Test
    public void createFolderPathWithNonExistingFolder()
    {
        final Root root = getRoot();
        assertTrue(root.createFolder(Path.parse("folderName")));
    }

    @Test
    public void createFolderPathWithNonExistingFolderWhenRootDoesntExist()
    {
        final Root root = getRoot("C:/");
        assertFalse(root.createFolder(Path.parse("folderName")));
    }

    @Test
    public void createFolderPathWithExistingFolder()
    {
        final Root root = getRoot();
        root.createFolder("A");

        assertFalse(root.createFolder(Path.parse("A")));
    }

    @Test
    public void createFolderPathValueWithNullPath()
    {
        final Root root = getRoot();
        final Value<Folder> createdFolder = new Value<>();
        assertFalse(root.createFolder((Path)null, createdFolder));
        assertFalse(createdFolder.hasValue());
    }

    @Test
    public void createFolderPathValueWithEmptyPath()
    {
        final Root root = getRoot();
        final Value<Folder> createdFolder = new Value<>();
        assertFalse(root.createFolder(Path.parse("")));
        assertFalse(createdFolder.hasValue());
    }

    @Test
    public void createFolderPathValueWithNonExistingFolder()
    {
        final Root root = getRoot();
        final Value<Folder> createdFolder = new Value<>();
        assertTrue(root.createFolder(Path.parse("folderName"), createdFolder));
        assertEquals(Path.parse("/folderName"), createdFolder.get().getPath());
    }

    @Test
    public void createFolderPathValueWithNonExistingFolderWhenRootDoesntExist()
    {
        final Root root = getRoot("C:/");
        final Value<Folder> createdFolder = new Value<>();
        assertFalse(root.createFolder(Path.parse("folderName"), createdFolder));
        assertFalse(createdFolder.hasValue());
    }

    @Test
    public void createFolderPathValueWithExistingFolder()
    {
        final Root root = getRoot();
        root.createFolder("A");

        final Value<Folder> createdFolder = new Value<>();
        assertFalse(root.createFolder(Path.parse("A"), createdFolder));
        assertEquals(Path.parse("/A"), createdFolder.get().getPath());
    }

    @Test
    public void createFileStringWithNullString()
    {
        final Root root = getRoot();
        assertFalse(root.createFile((String)null));
    }

    @Test
    public void createFileStringWithEmpty()
    {
        final Root root = getRoot();
        assertFalse(root.createFile(""));
    }

    @Test
    public void createFileStringWithNonExistingFile()
    {
        final Root root = getRoot();
        assertTrue(root.createFile("fileName"));
    }

    @Test
    public void createFileStringWithNonExistingFileWhenRootDoesntExist()
    {
        final Root root = getRoot("C:/");
        assertFalse(root.createFile("fileName"));
    }

    @Test
    public void createFileStringWithExistingFile()
    {
        final Root root = getRoot();
        root.createFile("A");

        assertFalse(root.createFile("A"));
    }

    @Test
    public void createFileStringValueWithNullString()
    {
        final Root root = getRoot();
        final Value<File> createdFile = new Value<>();
        assertFalse(root.createFile((String)null, createdFile));
        assertFalse(createdFile.hasValue());
    }

    @Test
    public void createFileStringValueWithEmpty()
    {
        final Root root = getRoot();
        final Value<File> createdFile = new Value<>();
        assertFalse(root.createFile(""));
        assertFalse(createdFile.hasValue());
    }

    @Test
    public void createFileStringValueWithNonExistingFile()
    {
        final Root root = getRoot();
        final Value<File> createdFile = new Value<>();
        assertTrue(root.createFile("fileName", createdFile));
        assertEquals(Path.parse("/fileName"), createdFile.get().getPath());
    }

    @Test
    public void createFileStringValueWithNonExistingFileWhenRootDoesntExist()
    {
        final Root root = getRoot("C:/");
        final Value<File> createdFile = new Value<>();
        assertFalse(root.createFile("fileName", createdFile));
        assertFalse(createdFile.hasValue());
    }

    @Test
    public void createFileStringValueWithExistingFile()
    {
        final Root root = getRoot();
        root.createFile("A");

        final Value<File> createdFile = new Value<>();
        assertFalse(root.createFile("A", createdFile));
        assertEquals(Path.parse("/A"), createdFile.get().getPath());
    }

    @Test
    public void createFilePathWithNullPath()
    {
        final Root root = getRoot();
        assertFalse(root.createFile((Path)null));
    }

    @Test
    public void createFilePathWithEmptyPath()
    {
        final Root root = getRoot();
        assertFalse(root.createFile(Path.parse("")));
    }

    @Test
    public void createFilePathWithNonExistingFile()
    {
        final Root root = getRoot();
        assertTrue(root.createFile(Path.parse("fileName")));
    }

    @Test
    public void createFilePathWithNonExistingFileWhenRootDoesntExist()
    {
        final Root root = getRoot("C:/");
        assertFalse(root.createFile(Path.parse("fileName")));
    }

    @Test
    public void createFilePathWithExistingFile()
    {
        final Root root = getRoot();
        root.createFile("A");

        assertFalse(root.createFile(Path.parse("A")));
    }

    @Test
    public void createFilePathValueWithNullPath()
    {
        final Root root = getRoot();
        final Value<File> createdFile = new Value<>();
        assertFalse(root.createFile((Path)null, createdFile));
        assertFalse(createdFile.hasValue());
    }

    @Test
    public void createFilePathValueWithEmptyPath()
    {
        final Root root = getRoot();
        final Value<File> createdFile = new Value<>();
        assertFalse(root.createFile(Path.parse("")));
        assertFalse(createdFile.hasValue());
    }

    @Test
    public void createFilePathValueWithNonExistingFile()
    {
        final Root root = getRoot();
        final Value<File> createdFile = new Value<>();
        assertTrue(root.createFile(Path.parse("fileName"), createdFile));
        assertEquals(Path.parse("/fileName"), createdFile.get().getPath());
    }

    @Test
    public void createFilePathValueWithNonExistingFileWhenRootDoesntExist()
    {
        final Root root = getRoot("C:/");
        final Value<File> createdFile = new Value<>();
        assertFalse(root.createFile(Path.parse("fileName"), createdFile));
        assertFalse(createdFile.hasValue());
    }

    @Test
    public void createFilePathValueWithExistingFile()
    {
        final Root root = getRoot();
        root.createFile("A");

        final Value<File> createdFile = new Value<>();
        assertFalse(root.createFile(Path.parse("A"), createdFile));
        assertEquals(Path.parse("/A"), createdFile.get().getPath());
    }

    @Test
    public void getFoldersWhenRootDoesntExist()
    {
        final Root root = getRoot("C:/");
        assertNull(root.getFolders());
    }

    @Test
    public void getFoldersWhenRootIsEmpty()
    {
        final Root root = getRoot();
        final Iterable<Folder> folders = root.getFolders();
        assertNotNull(folders);
        assertFalse(folders.any());
    }

    @Test
    public void getFoldersWhenRootHasAFile()
    {
        final Root root = getRoot();
        root.createFile("thing.txt");

        final Iterable<Folder> folders = root.getFolders();
        assertNotNull(folders);
        assertFalse(folders.any());
    }

    @Test
    public void getFoldersWhenRootHasAFolder()
    {
        final Root root = getRoot();
        root.createFolder("things");

        final Iterable<Folder> folders = root.getFolders();
        assertNotNull(folders);
        assertEquals(1, folders.getCount());
        assertEquals(root.getFolder("things"), folders.first());
    }

    @Test
    public void getFilesWhenRootDoesntExist()
    {
        final Root root = getRoot("C:/");
        assertNull(root.getFiles());
    }

    @Test
    public void getFilesWhenRootIsEmpty()
    {
        final Root root = getRoot();
        final Iterable<File> files = root.getFiles();
        assertNotNull(files);
        assertFalse(files.any());
    }

    @Test
    public void getFilesWhenRootHasAFile()
    {
        final Root root = getRoot();
        root.createFile("thing.txt");

        final Iterable<File> files = root.getFiles();
        assertNotNull(files);
        assertEquals(1, files.getCount());
        assertEquals(root.getFile("thing.txt"), files.first());
    }

    @Test
    public void getFilesWhenRootHasAFolder()
    {
        final Root root = getRoot();
        root.createFolder("things");

        final Iterable<File> files = root.getFiles();
        assertNotNull(files);
        assertFalse(files.any());
    }

    @Test
    public void getFilesAndFoldersWhenRootDoesntExist()
    {
        final Root root = getRoot("C:/");
        assertNull(root.getFilesAndFolders());
    }

    @Test
    public void getFilesAndFoldersWhenRootIsEmpty()
    {
        final Root root = getRoot();
        final Iterable<FileSystemEntry> entries = root.getFilesAndFolders();
        assertNotNull(entries);
        assertFalse(entries.any());
    }

    @Test
    public void getFilesAndFoldersWhenRootHasAFile()
    {
        final Root root = getRoot();
        root.createFile("thing.txt");

        final Iterable<FileSystemEntry> entries = root.getFilesAndFolders();
        assertNotNull(entries);
        assertEquals(1, entries.getCount());
        assertEquals(root.getFile("thing.txt"), entries.first());
    }

    @Test
    public void getFilesAndFoldersWhenRootHasAFolder()
    {
        final Root root = getRoot();
        root.createFolder("things");

        final Iterable<FileSystemEntry> entries = root.getFilesAndFolders();
        assertNotNull(entries);
        assertEquals(1, entries.getCount());
        assertEquals(root.getFolder("things"), entries.first());
    }
}
