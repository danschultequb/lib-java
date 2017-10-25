package qub;

import org.junit.Test;

import static org.junit.Assert.*;

public abstract class FileSystemTests
{
    protected abstract FileSystem getFileSystem();

    private FileSystem getFileSystem(AsyncRunner parallelRunner)
    {
        final FileSystem fileSystem = getFileSystem();
        fileSystem.setAsyncRunner(parallelRunner);
        return fileSystem;
    }

    @Test
    public void rootExistsWithNullStringPath()
    {
        final FileSystem fileSystem = getFileSystem();
        assertFalse(fileSystem.rootExists((String)null));
    }

    @Test
    public void rootExistsWithEmptyStringPath()
    {
        final FileSystem fileSystem = getFileSystem();
        assertFalse(fileSystem.rootExists(""));
    }

    @Test
    public void rootExistsWithNonExistingStringPath()
    {
        final FileSystem fileSystem = getFileSystem();
        assertFalse(fileSystem.rootExists("notme:/"));
    }

    private void rootExistsWithStringPathAsync(final String rootPath, final boolean expectedToExist)
    {
        asyncTest(new Action1<FileSystem>()
        {
            @Override
            public void run(FileSystem fileSystem)
            {
                fileSystem.rootExistsAsync(rootPath)
                        .then(new Action1<Boolean>()
                        {
                            @Override
                            public void run(Boolean rootExists)
                            {
                                assertEquals(expectedToExist, rootExists);
                            }
                        });
            }
        });
    }

    @Test
    public void rootExistAsyncWithNullStringPath()
    {
        rootExistsWithStringPathAsync(null, false);
    }

    @Test
    public void rootExistAsyncWithEmptyStringPath()
    {
        rootExistsWithStringPathAsync("", false);
    }

    @Test
    public void rootExistAsyncWithNonExistingStringPath()
    {
        rootExistsWithStringPathAsync("notme:\\", false);
    }

    private void rootExistsWithPathAsync(final Path rootPath, final boolean expectedToExist)
    {
        asyncTest(new Action1<FileSystem>()
        {
            @Override
            public void run(FileSystem fileSystem)
            {
                fileSystem.rootExistsAsync(rootPath)
                        .then(new Action1<Boolean>()
                        {
                            @Override
                            public void run(Boolean rootExists)
                            {
                                assertEquals(expectedToExist, rootExists);
                            }
                        });
            }
        });
    }

    @Test
    public void rootExistAsyncWithNullPath()
    {
        rootExistsWithPathAsync(null, false);
    }

    @Test
    public void rootExistAsyncWithEmptyPath()
    {
        rootExistsWithPathAsync(Path.parse(""), false);
    }

    @Test
    public void rootExistAsyncWithNonExistingPath()
    {
        rootExistsWithPathAsync(Path.parse("notme:\\"), false);
    }

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
    public void getRootsAsync()
    {
        asyncTest(new Action1<FileSystem>()
        {
            @Override
            public void run(FileSystem fileSystem)
            {
                fileSystem.getRootsAsync()
                        .then(new Action1<Iterable<Root>>()
                        {
                            @Override
                            public void run(Iterable<Root> roots)
                            {
                                assertNotNull(roots);
                                assertTrue(1 <= roots.getCount());
                            }
                        });
            }
        });
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
    public void getFilesAndFoldersAsyncForNullStringPath()
    {
        asyncTest(new Action1<FileSystem>()
        {
            @Override
            public void run(FileSystem fileSystem)
            {
                fileSystem.getFilesAndFoldersAsync((String)null)
                        .then(new Action1<Iterable<FileSystemEntry>>()
                        {
                            @Override
                            public void run(Iterable<FileSystemEntry> entries)
                            {
                                assertNull(entries);
                            }
                        });
            }
        });
    }

    @Test
    public void getFilesAndFoldersAsyncForEmptyStringPath()
    {
        asyncTest(new Action1<FileSystem>()
        {
            @Override
            public void run(FileSystem fileSystem)
            {
                fileSystem.getFilesAndFoldersAsync("")
                        .then(new Action1<Iterable<FileSystemEntry>>()
                        {
                            @Override
                            public void run(Iterable<FileSystemEntry> entries)
                            {
                                assertNull(entries);
                            }
                        });
            }
        });
    }

    @Test
    public void getFilesAndFoldersAsyncForNonExistingStringPath()
    {
        asyncTest(new Action1<FileSystem>()
        {
            @Override
            public void run(FileSystem fileSystem)
            {
                fileSystem.getFilesAndFoldersAsync("/i/dont/exist/")
                        .then(new Action1<Iterable<FileSystemEntry>>()
                        {
                            @Override
                            public void run(Iterable<FileSystemEntry> entries)
                            {
                                assertNull(entries);
                            }
                        });
            }
        });
    }

    @Test
    public void getFilesAndFoldersAsyncForExistingStringPathWithNoContents()
    {
        asyncTest(new Action1<FileSystem>()
        {
            @Override
            public void run(FileSystem fileSystem)
            {
                fileSystem.createFolder("/folderA");
                fileSystem.getFilesAndFoldersAsync("/folderA")
                        .then(new Action1<Iterable<FileSystemEntry>>()
                        {
                            @Override
                            public void run(Iterable<FileSystemEntry> entries)
                            {
                                assertFalse(entries.any());
                            }
                        });
            }
        });
    }

    @Test
    public void getFilesAndFoldersAsyncForExistingStringPathWithContents()
    {
        asyncTest(new Action1<FileSystem>()
        {
            @Override
            public void run(FileSystem fileSystem)
            {
                fileSystem.createFolder("/folderA");
                fileSystem.createFolder("/folderA/folderB");
                fileSystem.createFile("/file1.txt");
                fileSystem.createFile("/folderA/file2.csv");

                fileSystem.getFilesAndFoldersAsync("/")
                        .then(new Action1<Iterable<FileSystemEntry>>()
                        {
                            @Override
                            public void run(Iterable<FileSystemEntry> entries)
                            {
                                assertTrue(entries.any());
                                assertEquals(2, entries.getCount());
                                final String[] entryPathStrings = Array.toStringArray(entries.map(new Function1<FileSystemEntry, String>()
                                {
                                    @Override
                                    public String run(FileSystemEntry entry)
                                    {
                                        return entry.getPath().toString();
                                    }
                                }));
                                assertArrayEquals(new String[] { "/folderA", "/file1.txt" }, entryPathStrings);
                            }
                        });
            }
        });
    }

    @Test
    public void getFilesAndFoldersAsyncForNullPath()
    {
        asyncTest(new Action1<FileSystem>()
        {
            @Override
            public void run(FileSystem fileSystem)
            {
                fileSystem.getFilesAndFoldersAsync((Path)null)
                        .then(new Action1<Iterable<FileSystemEntry>>()
                        {
                            @Override
                            public void run(Iterable<FileSystemEntry> entries)
                            {
                                assertNull(entries);
                            }
                        });
            }
        });
    }

    @Test
    public void getFilesAndFoldersAsyncForEmptyPath()
    {
        asyncTest(new Action1<FileSystem>()
        {
            @Override
            public void run(FileSystem fileSystem)
            {
                fileSystem.getFilesAndFoldersAsync(Path.parse(""))
                        .then(new Action1<Iterable<FileSystemEntry>>()
                        {
                            @Override
                            public void run(Iterable<FileSystemEntry> entries)
                            {
                                assertNull(entries);
                            }
                        });
            }
        });
    }

    @Test
    public void getFilesAndFoldersAsyncForNonExistingPath()
    {
        asyncTest(new Action1<FileSystem>()
        {
            @Override
            public void run(FileSystem fileSystem)
            {
                fileSystem.getFilesAndFoldersAsync(Path.parse("/i/dont/exist/"))
                        .then(new Action1<Iterable<FileSystemEntry>>()
                        {
                            @Override
                            public void run(Iterable<FileSystemEntry> entries)
                            {
                                assertNull(entries);
                            }
                        });
            }
        });
    }

    @Test
    public void getFilesAndFoldersAsyncForExistingPathWithNoContents()
    {
        asyncTest(new Action1<FileSystem>()
        {
            @Override
            public void run(FileSystem fileSystem)
            {
                fileSystem.createFolder("/folderA");
                fileSystem.getFilesAndFoldersAsync(Path.parse("/folderA"))
                        .then(new Action1<Iterable<FileSystemEntry>>()
                        {
                            @Override
                            public void run(Iterable<FileSystemEntry> entries)
                            {
                                assertFalse(entries.any());
                            }
                        });
            }
        });
    }

    @Test
    public void getFilesAndFoldersAsyncForExistingPathWithContents()
    {
        asyncTest(new Action1<FileSystem>()
        {
            @Override
            public void run(FileSystem fileSystem)
            {
                fileSystem.createFolder("/folderA");
                fileSystem.createFolder("/folderA/folderB");
                fileSystem.createFile("/file1.txt");
                fileSystem.createFile("/folderA/file2.csv");

                fileSystem.getFilesAndFoldersAsync(Path.parse("/"))
                        .then(new Action1<Iterable<FileSystemEntry>>()
                        {
                            @Override
                            public void run(Iterable<FileSystemEntry> entries)
                            {
                                assertTrue(entries.any());
                                assertEquals(2, entries.getCount());
                                final String[] entryPathStrings = Array.toStringArray(entries.map(new Function1<FileSystemEntry, String>()
                                {
                                    @Override
                                    public String run(FileSystemEntry entry)
                                    {
                                        return entry.getPath().toString();
                                    }
                                }));
                                assertArrayEquals(new String[] { "/folderA", "/file1.txt" }, entryPathStrings);
                            }
                        });
            }
        });
    }

    @Test
    public void getFoldersWithNullStringPath()
    {
        final FileSystem fileSystem = getFileSystem();
        final Iterable<Folder> folders = fileSystem.getFolders((String)null);
        assertNull(folders);
    }

    @Test
    public void getFoldersWithEmptyStringPath()
    {
        final FileSystem fileSystem = getFileSystem();
        final Iterable<Folder> folders = fileSystem.getFolders("");
        assertNull(folders);
    }

    @Test
    public void getFoldersAsyncWithNullStringPath()
    {
        asyncTest(new Action1<FileSystem>()
        {
            @Override
            public void run(FileSystem fileSystem)
            {
                fileSystem.getFoldersAsync((String)null)
                        .then(new Action1<Iterable<Folder>>()
                        {
                            @Override
                            public void run(Iterable<Folder> folders)
                            {
                                assertNull(folders);
                            }
                        });
            }
        });
    }

    @Test
    public void getFoldersAsyncWithEmptyStringPath()
    {
        asyncTest(new Action1<FileSystem>()
        {
            @Override
            public void run(FileSystem fileSystem)
            {
                fileSystem.getFoldersAsync("")
                        .then(new Action1<Iterable<Folder>>()
                        {
                            @Override
                            public void run(Iterable<Folder> folders)
                            {
                                assertNull(folders);
                            }
                        });
            }
        });
    }

    @Test
    public void getFoldersAsyncWithNullPath()
    {
        asyncTest(new Action1<FileSystem>()
        {
            @Override
            public void run(FileSystem fileSystem)
            {
                fileSystem.getFoldersAsync((Path)null)
                        .then(new Action1<Iterable<Folder>>()
                        {
                            @Override
                            public void run(Iterable<Folder> folders)
                            {
                                assertNull(folders);
                            }
                        });
            }
        });
    }

    @Test
    public void getFoldersAsyncWithEmptyPath()
    {
        asyncTest(new Action1<FileSystem>()
        {
            @Override
            public void run(FileSystem fileSystem)
            {
                fileSystem.getFoldersAsync(Path.parse(""))
                        .then(new Action1<Iterable<Folder>>()
                        {
                            @Override
                            public void run(Iterable<Folder> folders)
                            {
                                assertNull(folders);
                            }
                        });
            }
        });
    }

    @Test
    public void getFilesAsyncWithNullStringPath()
    {
        asyncTest(new Action1<FileSystem>()
        {
            @Override
            public void run(FileSystem fileSystem)
            {
                fileSystem.getFilesAsync((String)null)
                        .then(new Action1<Iterable<File>>()
                        {
                            @Override
                            public void run(Iterable<File> files)
                            {
                                assertNull(files);
                            }
                        });
            }
        });
    }

    @Test
    public void getFilesAsyncWithEmptyStringPath()
    {
        asyncTest(new Action1<FileSystem>()
        {
            @Override
            public void run(FileSystem fileSystem)
            {
                fileSystem.getFilesAsync("")
                        .then(new Action1<Iterable<File>>()
                        {
                            @Override
                            public void run(Iterable<File> files)
                            {
                                assertNull(files);
                            }
                        });
            }
        });
    }

    @Test
    public void getFilesAsyncWithNullPath()
    {
        asyncTest(new Action1<FileSystem>()
        {
            @Override
            public void run(FileSystem fileSystem)
            {
                fileSystem.getFilesAsync((Path)null)
                        .then(new Action1<Iterable<File>>()
                        {
                            @Override
                            public void run(Iterable<File> files)
                            {
                                assertNull(files);
                            }
                        });
            }
        });
    }

    @Test
    public void getFilesAsyncWithEmptyPath()
    {
        asyncTest(new Action1<FileSystem>()
        {
            @Override
            public void run(FileSystem fileSystem)
            {
                fileSystem.getFilesAsync(Path.parse(""))
                        .then(new Action1<Iterable<File>>()
                        {
                            @Override
                            public void run(Iterable<File> files)
                            {
                                assertNull(files);
                            }
                        });
            }
        });
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
        final Root folder = fileSystem.getRoots().first();
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
    public void folderExistsAsyncWithRootStringPath()
    {
        asyncTest(new Action1<FileSystem>()
        {
            @Override
            public void run(FileSystem fileSystem)
            {
                fileSystem.folderExistsAsync(fileSystem.getRoots().first().getPath().toString())
                        .then(new Action1<Boolean>()
                        {
                            @Override
                            public void run(Boolean folderExists)
                            {
                                assertTrue(folderExists);
                            }
                        });
            }
        });
    }

    @Test
    public void folderExistsAsyncWithExistingFolderStringPath()
    {
        asyncTest(new Action1<FileSystem>()
        {
            @Override
            public void run(FileSystem fileSystem)
            {
                fileSystem.createFolder("/folderName");
                fileSystem.folderExistsAsync("/folderName")
                        .then(new Action1<Boolean>()
                        {
                            @Override
                            public void run(Boolean folderExists)
                            {
                                assertTrue(folderExists);
                            }
                        });
            }
        });
    }

    @Test
    public void folderExistsAsyncWithRootPath()
    {
        asyncTest(new Action1<FileSystem>()
        {
            @Override
            public void run(FileSystem fileSystem)
            {
                fileSystem.folderExistsAsync(fileSystem.getRoots().first().getPath())
                        .then(new Action1<Boolean>()
                        {
                            @Override
                            public void run(Boolean folderExists)
                            {
                                assertTrue(folderExists);
                            }
                        });
            }
        });
    }

    @Test
    public void folderExistsAsyncWithExistingFolderPath()
    {
        asyncTest(new Action1<FileSystem>()
        {
            @Override
            public void run(FileSystem fileSystem)
            {
                fileSystem.createFolder("/folderName");
                fileSystem.folderExistsAsync(Path.parse("/folderName"))
                        .then(new Action1<Boolean>()
                        {
                            @Override
                            public void run(Boolean folderExists)
                            {
                                assertTrue(folderExists);
                            }
                        });
            }
        });
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
    public void createFolderAsyncWithNullString()
    {
        asyncTest(new Action1<FileSystem>()
        {
            @Override
            public void run(FileSystem fileSystem)
            {
                fileSystem.createFolderAsync((String)null)
                        .then(new Action1<Boolean>()
                        {
                            @Override
                            public void run(Boolean folderCreated)
                            {
                                assertFalse(folderCreated);
                            }
                        });
            }
        });
    }

    @Test
    public void createFolderAsyncWithEmptyString()
    {
        asyncTest(new Action1<FileSystem>()
        {
            @Override
            public void run(FileSystem fileSystem)
            {
                fileSystem.createFolderAsync("")
                        .then(new Action1<Boolean>()
                        {
                            @Override
                            public void run(Boolean folderCreated)
                            {
                                assertFalse(folderCreated);
                            }
                        });
            }
        });
    }

    @Test
    public void createFolderAsyncWithRelativePathString()
    {
        asyncTest(new Action1<FileSystem>()
        {
            @Override
            public void run(FileSystem fileSystem)
            {
                fileSystem.createFolderAsync("folder")
                        .then(new Action1<Boolean>()
                        {
                            @Override
                            public void run(Boolean arg1)
                            {
                                assertFalse(arg1);
                            }
                        });
            }
        });
    }

    @Test
    public void createFolderAsyncWithRootedPathString()
    {
        asyncTest(new Action1<FileSystem>()
        {
            @Override
            public void run(final FileSystem fileSystem)
            {
                fileSystem.createFolderAsync("/folder")
                        .then(new Action1<Boolean>()
                        {
                            @Override
                            public void run(Boolean folderCreated)
                            {
                                assertTrue(folderCreated);
                                assertTrue(fileSystem.folderExists("/folder"));
                            }
                        });
            }
        });
    }

    @Test
    public void createFolderAsyncWithNullStringAndNullValue()
    {
        asyncTest(new Action1<FileSystem>()
        {
            @Override
            public void run(FileSystem fileSystem)
            {
                fileSystem.createFolderAsync((String)null, null)
                        .then(new Action1<Boolean>()
                        {
                            @Override
                            public void run(Boolean folderCreated)
                            {
                                assertFalse(folderCreated);
                            }
                        });
            }
        });
    }

    @Test
    public void createFolderAsyncWithEmptyStringAndNullValue()
    {
        asyncTest(new Action1<FileSystem>()
        {
            @Override
            public void run(FileSystem fileSystem)
            {
                fileSystem.createFolderAsync("", null)
                        .then(new Action1<Boolean>()
                        {
                            @Override
                            public void run(Boolean folderCreated)
                            {
                                assertFalse(folderCreated);
                            }
                        });
            }
        });
    }

    @Test
    public void createFolderAsyncWithRelativePathStringAndNullValue()
    {
        asyncTest(new Action1<FileSystem>()
        {
            @Override
            public void run(FileSystem fileSystem)
            {
                fileSystem.createFolderAsync("folder", null)
                        .then(new Action1<Boolean>()
                        {
                            @Override
                            public void run(Boolean folderCreated)
                            {
                                assertFalse(folderCreated);
                            }
                        });
            }
        });
    }

    @Test
    public void createFolderAsyncWithRootedPathStringAndNullValue()
    {
        asyncTest(new Action1<FileSystem>()
        {
            @Override
            public void run(final FileSystem fileSystem)
            {
                fileSystem.createFolderAsync("/folder", null)
                        .then(new Action1<Boolean>()
                        {
                            @Override
                            public void run(Boolean folderCreated)
                            {
                                assertTrue(folderCreated);
                                assertTrue(fileSystem.folderExists("/folder"));
                            }
                        });
            }
        });
    }

    @Test
    public void createFolderAsyncWithNullStringAndNonNullValue()
    {
        asyncTest(new Action1<FileSystem>()
        {
            @Override
            public void run(FileSystem fileSystem)
            {
                final Value<Folder> folder = new Value<>();
                fileSystem.createFolderAsync((String)null, folder)
                        .then(new Action1<Boolean>()
                        {
                            @Override
                            public void run(Boolean folderCreated)
                            {
                                assertFalse(folderCreated);
                                assertFalse(folder.hasValue());
                            }
                        });
            }
        });
    }

    @Test
    public void createFolderAsyncWithEmptyStringAndNonNullValue()
    {
        asyncTest(new Action1<FileSystem>()
        {
            @Override
            public void run(final FileSystem fileSystem)
            {
                final Value<Folder> folder = new Value<>();
                fileSystem.createFolderAsync("", folder)
                        .then(new Action1<Boolean>()
                        {
                            @Override
                            public void run(Boolean folderCreated)
                            {
                                assertFalse(folderCreated);
                                assertFalse(folder.hasValue());
                            }
                        });
            }
        });
    }

    @Test
    public void createFolderAsyncWithRelativePathStringAndNonNullValue()
    {
        asyncTest(new Action1<FileSystem>()
        {
            @Override
            public void run(final FileSystem fileSystem)
            {
                final Value<Folder> folder = new Value<>();
                fileSystem.createFolderAsync("folder", folder)
                        .then(new Action1<Boolean>()
                        {
                            @Override
                            public void run(Boolean folderCreated)
                            {
                                assertFalse(folderCreated);
                                assertFalse(folder.hasValue());
                            }
                        });
            }
        });
    }

    @Test
    public void createFolderAsyncWithRootedPathStringAndNonNullValue()
    {
        asyncTest(new Action1<FileSystem>()
        {
            @Override
            public void run(final FileSystem fileSystem)
            {
                final Value<Folder> folder = new Value<>();
                fileSystem.createFolderAsync("/folder", folder)
                        .then(new Action1<Boolean>()
                        {
                            @Override
                            public void run(Boolean folderCreated)
                            {
                                assertTrue(folderCreated);
                                assertTrue(fileSystem.folderExists("/folder"));

                                assertTrue(folder.hasValue());
                                assertNotNull(folder.get());
                                assertEquals("/folder", folder.get().getPath().toString());
                            }
                        });
            }
        });
    }

    @Test
    public void createFolderAsyncWithNullPath()
    {
        asyncTest(new Action1<FileSystem>()
        {
            @Override
            public void run(FileSystem fileSystem)
            {
                fileSystem.createFolderAsync((Path)null)
                        .then(new Action1<Boolean>()
                        {
                            @Override
                            public void run(Boolean folderCreated)
                            {
                                assertFalse(folderCreated);
                            }
                        });
            }
        });
    }

    @Test
    public void createFolderAsyncWithEmptyPath()
    {
        asyncTest(new Action1<FileSystem>()
        {
            @Override
            public void run(FileSystem fileSystem)
            {
                fileSystem.createFolderAsync(Path.parse(""))
                        .then(new Action1<Boolean>()
                        {
                            @Override
                            public void run(Boolean folderCreated)
                            {
                                assertFalse(folderCreated);
                            }
                        });
            }
        });
    }

    @Test
    public void createFolderAsyncWithRelativePath()
    {
        asyncTest(new Action1<FileSystem>()
        {
            @Override
            public void run(FileSystem fileSystem)
            {
                fileSystem.createFolderAsync(Path.parse("folder"))
                        .then(new Action1<Boolean>()
                        {
                            @Override
                            public void run(Boolean arg1)
                            {
                                assertFalse(arg1);
                            }
                        });
            }
        });
    }

    @Test
    public void createFolderAsyncWithRootedPath()
    {
        asyncTest(new Action1<FileSystem>()
        {
            @Override
            public void run(final FileSystem fileSystem)
            {
                fileSystem.createFolderAsync(Path.parse("/folder"))
                        .then(new Action1<Boolean>()
                        {
                            @Override
                            public void run(Boolean folderCreated)
                            {
                                assertTrue(folderCreated);
                                assertTrue(fileSystem.folderExists("/folder"));
                            }
                        });
            }
        });
    }

    @Test
    public void createFolderAsyncWithNullPathAndNullValue()
    {
        asyncTest(new Action1<FileSystem>()
        {
            @Override
            public void run(FileSystem fileSystem)
            {
                fileSystem.createFolderAsync((Path)null, null)
                        .then(new Action1<Boolean>()
                        {
                            @Override
                            public void run(Boolean folderCreated)
                            {
                                assertFalse(folderCreated);
                            }
                        });
            }
        });
    }

    @Test
    public void createFolderAsyncWithEmptyPathAndNullValue()
    {
        asyncTest(new Action1<FileSystem>()
        {
            @Override
            public void run(FileSystem fileSystem)
            {
                fileSystem.createFolderAsync(Path.parse(""), null)
                        .then(new Action1<Boolean>()
                        {
                            @Override
                            public void run(Boolean folderCreated)
                            {
                                assertFalse(folderCreated);
                            }
                        });
            }
        });
    }

    @Test
    public void createFolderAsyncWithRelativePathAndNullValue()
    {
        asyncTest(new Action1<FileSystem>()
        {
            @Override
            public void run(FileSystem fileSystem)
            {
                fileSystem.createFolderAsync(Path.parse("folder"), null)
                        .then(new Action1<Boolean>()
                        {
                            @Override
                            public void run(Boolean folderCreated)
                            {
                                assertFalse(folderCreated);
                            }
                        });
            }
        });
    }

    @Test
    public void createFolderAsyncWithRootedPathAndNullValue()
    {
        asyncTest(new Action1<FileSystem>()
        {
            @Override
            public void run(final FileSystem fileSystem)
            {
                fileSystem.createFolderAsync(Path.parse("/folder"), null)
                        .then(new Action1<Boolean>()
                        {
                            @Override
                            public void run(Boolean folderCreated)
                            {
                                assertTrue(folderCreated);
                                assertTrue(fileSystem.folderExists("/folder"));
                            }
                        });
            }
        });
    }

    @Test
    public void createFolderAsyncWithNullPathAndNonNullValue()
    {
        asyncTest(new Action1<FileSystem>()
        {
            @Override
            public void run(FileSystem fileSystem)
            {
                final Value<Folder> folder = new Value<>();
                fileSystem.createFolderAsync((Path)null, folder)
                        .then(new Action1<Boolean>()
                        {
                            @Override
                            public void run(Boolean folderCreated)
                            {
                                assertFalse(folderCreated);
                                assertFalse(folder.hasValue());
                            }
                        });
            }
        });
    }

    @Test
    public void createFolderAsyncWithEmptyPathAndNonNullValue()
    {
        asyncTest(new Action1<FileSystem>()
        {
            @Override
            public void run(final FileSystem fileSystem)
            {
                final Value<Folder> folder = new Value<>();
                fileSystem.createFolderAsync(Path.parse(""), folder)
                        .then(new Action1<Boolean>()
                        {
                            @Override
                            public void run(Boolean folderCreated)
                            {
                                assertFalse(folderCreated);
                                assertFalse(folder.hasValue());
                            }
                        });
            }
        });
    }

    @Test
    public void createFolderAsyncWithRelativePathAndNonNullValue()
    {
        asyncTest(new Action1<FileSystem>()
        {
            @Override
            public void run(final FileSystem fileSystem)
            {
                final Value<Folder> folder = new Value<>();
                fileSystem.createFolderAsync(Path.parse("folder"), folder)
                        .then(new Action1<Boolean>()
                        {
                            @Override
                            public void run(Boolean folderCreated)
                            {
                                assertFalse(folderCreated);
                                assertFalse(folder.hasValue());
                            }
                        });
            }
        });
    }

    @Test
    public void createFolderAsyncWithRootedPathAndNonNullValue()
    {
        asyncTest(new Action1<FileSystem>()
        {
            @Override
            public void run(final FileSystem fileSystem)
            {
                final Value<Folder> folder = new Value<>();
                fileSystem.createFolderAsync(Path.parse("/folder"), folder)
                        .then(new Action1<Boolean>()
                        {
                            @Override
                            public void run(Boolean folderCreated)
                            {
                                assertTrue(folderCreated);
                                assertTrue(fileSystem.folderExists("/folder"));

                                assertTrue(folder.hasValue());
                                assertNotNull(folder.get());
                                assertEquals("/folder", folder.get().getPath().toString());
                            }
                        });
            }
        });
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
    public void deleteFolderAsyncWithNullString()
    {
        asyncTest(new Action1<FileSystem>()
        {
            @Override
            public void run(FileSystem fileSystem)
            {
                fileSystem.deleteFolderAsync((String)null)
                        .then(new Action1<Boolean>()
                        {
                            @Override
                            public void run(Boolean folderDeleted)
                            {
                                assertFalse(folderDeleted);
                            }
                        });
            }
        });
    }

    @Test
    public void deleteFolderAsyncWithEmptyString()
    {
        asyncTest(new Action1<FileSystem>()
        {
            @Override
            public void run(FileSystem fileSystem)
            {
                fileSystem.deleteFolderAsync("")
                        .then(new Action1<Boolean>()
                        {
                            @Override
                            public void run(Boolean folderDeleted)
                            {
                                assertFalse(folderDeleted);
                            }
                        });
            }
        });
    }

    @Test
    public void deleteFolderAsyncWithNonRootedFolderPathString()
    {
        asyncTest(new Action1<FileSystem>()
        {
            @Override
            public void run(final FileSystem fileSystem)
            {
                fileSystem.createFolder("/folder");
                fileSystem.deleteFolderAsync("folder")
                        .then(new Action1<Boolean>()
                        {
                            @Override
                            public void run(Boolean folderDeleted)
                            {
                                assertFalse(folderDeleted);
                                assertTrue(fileSystem.folderExists("/folder"));
                            }
                        });
            }
        });
    }

    @Test
    public void deleteFolderAsyncWithNonExistingFolderPathString()
    {
        asyncTest(new Action1<FileSystem>()
        {
            @Override
            public void run(FileSystem fileSystem)
            {
                fileSystem.deleteFolderAsync("/folder")
                        .then(new Action1<Boolean>()
                        {
                            @Override
                            public void run(Boolean folderDeleted)
                            {
                                assertFalse(folderDeleted);
                            }
                        });
            }
        });
    }

    @Test
    public void deleteFolderAsyncWithExistingFolderPathString()
    {
        asyncTest(new Action1<FileSystem>()
        {
            @Override
            public void run(final FileSystem fileSystem)
            {
                fileSystem.createFolder("/folder/");

                fileSystem.deleteFolderAsync("/folder/")
                        .then(new Action1<Boolean>()
                        {
                            @Override
                            public void run(Boolean folderDeleted)
                            {
                                assertTrue(folderDeleted);
                                assertFalse(fileSystem.folderExists("/folder/"));
                            }
                        });
            }
        });
    }

    @Test
    public void deleteFolderAsyncWithExistingFolderPathStringWithSiblingFolders()
    {
        asyncTest(new Action1<FileSystem>()
        {
            @Override
            public void run(final FileSystem fileSystem)
            {
                fileSystem.createFolder("/folder/a");
                fileSystem.createFolder("/folder/b");
                fileSystem.createFolder("/folder/c");

                fileSystem.deleteFolderAsync("/folder/c")
                        .then(new Action1<Boolean>()
                        {
                            @Override
                            public void run(Boolean folderDeleted)
                            {
                                assertTrue(folderDeleted);
                                assertFalse(fileSystem.folderExists("/folder/c"));
                                assertTrue(fileSystem.folderExists("/folder/a"));
                                assertTrue(fileSystem.folderExists("/folder/b"));
                            }
                        });
            }
        });
    }

    @Test
    public void deleteFolderAsyncWithNullPath()
    {
        asyncTest(new Action1<FileSystem>()
        {
            @Override
            public void run(FileSystem fileSystem)
            {
                fileSystem.deleteFolderAsync((Path)null)
                        .then(new Action1<Boolean>()
                        {
                            @Override
                            public void run(Boolean folderDeleted)
                            {
                                assertFalse(folderDeleted);
                            }
                        });
            }
        });
    }

    @Test
    public void deleteFolderAsyncWithEmptyPath()
    {
        asyncTest(new Action1<FileSystem>()
        {
            @Override
            public void run(FileSystem fileSystem)
            {
                fileSystem.deleteFolderAsync(Path.parse(""))
                        .then(new Action1<Boolean>()
                        {
                            @Override
                            public void run(Boolean folderDeleted)
                            {
                                assertFalse(folderDeleted);
                            }
                        });
            }
        });
    }

    @Test
    public void deleteFolderAsyncWithNonRootedFolderPath()
    {
        asyncTest(new Action1<FileSystem>()
        {
            @Override
            public void run(final FileSystem fileSystem)
            {
                fileSystem.createFolder("/folder");
                fileSystem.deleteFolderAsync(Path.parse("folder"))
                        .then(new Action1<Boolean>()
                        {
                            @Override
                            public void run(Boolean folderDeleted)
                            {
                                assertFalse(folderDeleted);
                                assertTrue(fileSystem.folderExists("/folder"));
                            }
                        });
            }
        });
    }

    @Test
    public void deleteFolderAsyncWithNonExistingFolderPath()
    {
        asyncTest(new Action1<FileSystem>()
        {
            @Override
            public void run(FileSystem fileSystem)
            {
                fileSystem.deleteFolderAsync(Path.parse("/folder"))
                        .then(new Action1<Boolean>()
                        {
                            @Override
                            public void run(Boolean folderDeleted)
                            {
                                assertFalse(folderDeleted);
                            }
                        });
            }
        });
    }

    @Test
    public void deleteFolderAsyncWithExistingFolderPath()
    {
        asyncTest(new Action1<FileSystem>()
        {
            @Override
            public void run(final FileSystem fileSystem)
            {
                fileSystem.createFolder("/folder/");

                fileSystem.deleteFolderAsync(Path.parse("/folder/"))
                        .then(new Action1<Boolean>()
                        {
                            @Override
                            public void run(Boolean folderDeleted)
                            {
                                assertTrue(folderDeleted);
                                assertFalse(fileSystem.folderExists("/folder/"));
                            }
                        });
            }
        });
    }

    @Test
    public void deleteFolderAsyncWithExistingFolderPathWithSiblingFolders()
    {
        asyncTest(new Action1<FileSystem>()
        {
            @Override
            public void run(final FileSystem fileSystem)
            {
                fileSystem.createFolder("/folder/a");
                fileSystem.createFolder("/folder/b");
                fileSystem.createFolder("/folder/c");

                fileSystem.deleteFolderAsync(Path.parse("/folder/c"))
                        .then(new Action1<Boolean>()
                        {
                            @Override
                            public void run(Boolean folderDeleted)
                            {
                                assertTrue(folderDeleted);
                                assertFalse(fileSystem.folderExists("/folder/c"));
                                assertTrue(fileSystem.folderExists("/folder/a"));
                                assertTrue(fileSystem.folderExists("/folder/b"));
                            }
                        });
            }
        });
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
    public void fileExistsAsyncWithRootPathString()
    {
        asyncTest(new Action1<FileSystem>()
        {
            @Override
            public void run(FileSystem fileSystem)
            {
                final Root root = fileSystem.getRoots().first();
                fileSystem.fileExistsAsync(root.getPath().toString())
                        .then(new Action1<Boolean>()
                        {
                            @Override
                            public void run(Boolean fileExists)
                            {
                                assertFalse(fileExists);
                            }
                        });
            }
        });
    }

    @Test
    public void fileExistsAsyncWithExistingFolderPathString()
    {
        asyncTest(new Action1<FileSystem>()
        {
            @Override
            public void run(FileSystem fileSystem)
            {
                fileSystem.createFolder("/folderName");
                fileSystem.fileExistsAsync("/folderName")
                        .then(new Action1<Boolean>()
                        {
                            @Override
                            public void run(Boolean fileExists)
                            {
                                assertFalse(fileExists);
                            }
                        });
            }
        });
    }

    @Test
    public void fileExistsAsyncWithExistingFilePathString()
    {
        asyncTest(new Action1<FileSystem>()
        {
            @Override
            public void run(FileSystem fileSystem)
            {
                fileSystem.createFile("/file1.xml");
                fileSystem.fileExistsAsync("/file1.xml")
                        .then(new Action1<Boolean>()
                        {
                            @Override
                            public void run(Boolean fileExists)
                            {
                                assertTrue(fileExists);
                            }
                        });
            }
        });
    }

    @Test
    public void fileExistsAsyncWithRootPath()
    {
        asyncTest(new Action1<FileSystem>()
        {
            @Override
            public void run(FileSystem fileSystem)
            {
                final Root root = fileSystem.getRoots().first();
                fileSystem.fileExistsAsync(root.getPath())
                        .then(new Action1<Boolean>()
                        {
                            @Override
                            public void run(Boolean fileExists)
                            {
                                assertFalse(fileExists);
                            }
                        });
            }
        });
    }

    @Test
    public void fileExistsAsyncWithExistingFolderPath()
    {
        asyncTest(new Action1<FileSystem>()
        {
            @Override
            public void run(FileSystem fileSystem)
            {
                fileSystem.createFolder("/folderName");
                fileSystem.fileExistsAsync(Path.parse("/folderName"))
                        .then(new Action1<Boolean>()
                        {
                            @Override
                            public void run(Boolean fileExists)
                            {
                                assertFalse(fileExists);
                            }
                        });
            }
        });
    }

    @Test
    public void fileExistsAsyncWithExistingFilePath()
    {
        asyncTest(new Action1<FileSystem>()
        {
            @Override
            public void run(FileSystem fileSystem)
            {
                fileSystem.createFile("/file1.xml");
                fileSystem.fileExistsAsync(Path.parse("/file1.xml"))
                        .then(new Action1<Boolean>()
                        {
                            @Override
                            public void run(Boolean fileExists)
                            {
                                assertTrue(fileExists);
                            }
                        });
            }
        });
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
        assertFalse(fileSystem.createFile("things.txt"));
    }

    @Test
    public void createFileWithNonExistingRootedPathString()
    {
        final FileSystem fileSystem = getFileSystem();
        assertTrue(fileSystem.createFile("/things.txt"));
        assertTrue(fileSystem.fileExists("/things.txt"));
        assertArrayEquals(new byte[0], fileSystem.getFileContents("/things.txt"));
    }

    @Test
    public void createFileWithNonExistingRootedPathStringAndContents()
    {
        final FileSystem fileSystem = getFileSystem();
        assertTrue(fileSystem.createFile("/things.txt", new byte[] { 0, 1, 2, 3 }));
        assertTrue(fileSystem.fileExists("/things.txt"));
        assertArrayEquals(new byte[] { 0, 1, 2, 3 }, fileSystem.getFileContents("/things.txt"));
    }

    @Test
    public void createFileWithExistingRootedPathString()
    {
        final FileSystem fileSystem = getFileSystem();
        fileSystem.createFile("/things.txt");

        assertFalse(fileSystem.createFile("/things.txt"));
        assertTrue(fileSystem.fileExists("/things.txt"));
        assertArrayEquals(new byte[0], fileSystem.getFileContents("/things.txt"));
    }

    @Test
    public void createFileWithExistingRootedPathStringAndContents()
    {
        final FileSystem fileSystem = getFileSystem();
        fileSystem.createFile("/things.txt");

        assertFalse(fileSystem.createFile("/things.txt", new byte[] { 0, 1, 2, 3 }));
        assertTrue(fileSystem.fileExists("/things.txt"));
        assertArrayEquals(new byte[0], fileSystem.getFileContents("/things.txt"));
    }

    @Test
    public void createFileWithNullStringAndNullValue()
    {
        final FileSystem fileSystem = getFileSystem();
        assertFalse(fileSystem.createFile((String)null, (Out<File>)null));
    }

    @Test
    public void createFileWithEmptyStringAndNullValue()
    {
        final FileSystem fileSystem = getFileSystem();
        assertFalse(fileSystem.createFile("", (Out<File>)null));
    }

    @Test
    public void createFileWithRelativePathStringAndNullValue()
    {
        final FileSystem fileSystem = getFileSystem();
        assertFalse(fileSystem.createFile("things.txt", (Out<File>)null));
    }

    @Test
    public void createFileWithNonExistingRootedPathStringAndNullValue()
    {
        final FileSystem fileSystem = getFileSystem();
        assertTrue(fileSystem.createFile("/things.txt", (Out<File>)null));
        assertTrue(fileSystem.fileExists("/things.txt"));
        assertArrayEquals(new byte[0], fileSystem.getFileContents("/things.txt"));
    }

    @Test
    public void createFileWithNonExistingRootedPathStringAndContentsAndNullValue()
    {
        final FileSystem fileSystem = getFileSystem();
        assertTrue(fileSystem.createFile("/things.txt", new byte[] { 10, 11, 12 }, null));
        assertTrue(fileSystem.fileExists("/things.txt"));
        assertArrayEquals(new byte[] { 10, 11, 12 }, fileSystem.getFileContents("/things.txt"));
    }

    @Test
    public void createFileWithExistingRootedPathStringAndNullValue()
    {
        final FileSystem fileSystem = getFileSystem();
        fileSystem.createFile("/things.txt");

        assertFalse(fileSystem.createFile("/things.txt", (Out<File>)null));

        assertTrue(fileSystem.fileExists("/things.txt"));
        assertArrayEquals(new byte[0], fileSystem.getFileContents("/things.txt"));
    }

    @Test
    public void createFileWithExistingRootedPathStringAndContentsNullValue()
    {
        final FileSystem fileSystem = getFileSystem();
        fileSystem.createFile("/things.txt");

        assertFalse(fileSystem.createFile("/things.txt", new byte[] { 0, 1 }, null));

        assertTrue(fileSystem.fileExists("/things.txt"));
        assertArrayEquals(new byte[0], fileSystem.getFileContents("/things.txt"));
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
    public void createFileWithNonExistingRootedPathStringAndNonNullValue()
    {
        final FileSystem fileSystem = getFileSystem();
        final Value<File> file = new Value<>();
        final boolean fileCreated = fileSystem.createFile("/things.txt", file);
        assertTrue(fileCreated);
        assertTrue(file.hasValue());
        assertNotNull(file.get());
        assertEquals("/things.txt", file.get().getPath().toString());
    }

    @Test
    public void createFileWithExistingRootedPathStringAndNonNullValue()
    {
        final FileSystem fileSystem = getFileSystem();
        fileSystem.createFile("/things.txt");

        final Value<File> file = new Value<>();
        assertFalse(fileSystem.createFile("/things.txt", file));
        assertTrue(file.hasValue());
        assertEquals(Path.parse("/things.txt"), file.get().getPath());
    }

    @Test
    public void createFileWithNullPathAndContents()
    {
        final FileSystem fileSystem = getFileSystem();
        assertFalse(fileSystem.createFile((Path)null, new byte[] { 0, 1, 2 }));
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
    public void createFileAsyncWithNullString()
    {
        asyncTest(new Action1<FileSystem>()
        {
            @Override
            public void run(FileSystem fileSystem)
            {
                fileSystem.createFileAsync((String)null)
                        .then(new Action1<Boolean>()
                        {
                            @Override
                            public void run(Boolean fileCreated)
                            {
                                assertFalse(fileCreated);
                            }
                        });
            }
        });
    }

    @Test
    public void createFileAsyncWithEmptyString()
    {
        asyncTest(new Action1<FileSystem>()
        {
            @Override
            public void run(FileSystem fileSystem)
            {
                fileSystem.createFileAsync("")
                        .then(new Action1<Boolean>()
                        {
                            @Override
                            public void run(Boolean fileCreated)
                            {
                                assertFalse(fileCreated);
                            }
                        });
            }
        });
    }

    @Test
    public void createFileAsyncWithRelativePathString()
    {
        asyncTest(new Action1<FileSystem>()
        {
            @Override
            public void run(FileSystem fileSystem)
            {
                fileSystem.createFileAsync("things.txt")
                        .then(new Action1<Boolean>()
                        {
                            @Override
                            public void run(Boolean fileCreated)
                            {
                                assertFalse(fileCreated);
                            }
                        });
            }
        });
    }

    @Test
    public void createFileAsyncWithNonExistingRootedPathString()
    {
        asyncTest(new Action1<FileSystem>()
        {
            @Override
            public void run(final FileSystem fileSystem)
            {
                fileSystem.createFileAsync("/things.txt")
                        .then(new Action1<Boolean>()
                        {
                            @Override
                            public void run(Boolean fileCreated)
                            {
                                assertTrue(fileCreated);
                                assertTrue(fileSystem.fileExists("/things.txt"));
                            }
                        });
            }
        });
    }

    @Test
    public void createFileAsyncWithExistingRootedPathString()
    {
        asyncTest(new Action1<FileSystem>()
        {
            @Override
            public void run(final FileSystem fileSystem)
            {
                fileSystem.createFile("/things.txt");

                fileSystem.createFileAsync("/things.txt")
                        .then(new Action1<Boolean>()
                        {
                            @Override
                            public void run(Boolean fileCreated)
                            {
                                assertFalse(fileCreated);
                                assertTrue(fileSystem.fileExists("/things.txt"));
                            }
                        });
            }
        });
    }

    @Test
    public void createFileAsyncWithNullStringAndNullValue()
    {
        asyncTest(new Action1<FileSystem>()
        {
            @Override
            public void run(final FileSystem fileSystem)
            {
                fileSystem.createFileAsync((String)null, null)
                        .then(new Action1<Boolean>()
                        {
                            @Override
                            public void run(Boolean fileCreated)
                            {
                                assertFalse(fileCreated);
                            }
                        });
            }
        });
    }

    @Test
    public void createFileAsyncWithEmptyStringAndNullValue()
    {
        asyncTest(new Action1<FileSystem>()
        {
            @Override
            public void run(FileSystem fileSystem)
            {
                fileSystem.createFileAsync("", null)
                        .then(new Action1<Boolean>()
                        {
                            @Override
                            public void run(Boolean fileCreated)
                            {
                                assertFalse(fileCreated);
                            }
                        });
            }
        });
    }

    @Test
    public void createFileAsyncWithRelativePathStringAndNullValue()
    {
        asyncTest(new Action1<FileSystem>()
        {
            @Override
            public void run(FileSystem fileSystem)
            {
                fileSystem.createFileAsync("things.txt", null)
                        .then(new Action1<Boolean>()
                        {
                            @Override
                            public void run(Boolean fileCreated)
                            {
                                assertFalse(fileCreated);
                            }
                        });
            }
        });
    }

    @Test
    public void createFileAsyncWithNonExistingRootedPathStringAndNullValue()
    {
        asyncTest(new Action1<FileSystem>()
        {
            @Override
            public void run(FileSystem fileSystem)
            {
                fileSystem.createFileAsync("/things.txt", null)
                        .then(new Action1<Boolean>()
                        {
                            @Override
                            public void run(Boolean fileCreated)
                            {
                                assertTrue(fileCreated);
                            }
                        });
            }
        });
    }

    @Test
    public void createFileAsyncWithExistingRootedPathStringAndNullValue()
    {
        asyncTest(new Action1<FileSystem>()
        {
            @Override
            public void run(FileSystem fileSystem)
            {
                fileSystem.createFile("/things.txt");

                fileSystem.createFileAsync("/things.txt", null)
                        .then(new Action1<Boolean>()
                        {
                            @Override
                            public void run(Boolean fileCreated)
                            {
                                assertFalse(fileCreated);
                            }
                        });
            }
        });
    }

    @Test
    public void createFileAsyncWithNullStringAndNonNullValue()
    {
        asyncTest(new Action1<FileSystem>()
        {
            @Override
            public void run(FileSystem fileSystem)
            {
                final Value<File> file = new Value<>();
                fileSystem.createFileAsync((String)null, file)
                        .then(new Action1<Boolean>()
                        {
                            @Override
                            public void run(Boolean fileCreated)
                            {
                                assertFalse(fileCreated);
                                assertFalse(file.hasValue());
                            }
                        });
            }
        });
    }

    @Test
    public void createFileAsyncWithEmptyStringAndNonNullValue()
    {
        asyncTest(new Action1<FileSystem>()
        {
            @Override
            public void run(FileSystem fileSystem)
            {
                final Value<File> file = new Value<>();
                fileSystem.createFileAsync("", file)
                        .then(new Action1<Boolean>()
                        {
                            @Override
                            public void run(Boolean fileCreated)
                            {
                                assertFalse(fileCreated);
                                assertFalse(file.hasValue());
                            }
                        });
            }
        });
    }

    @Test
    public void createFileAsyncWithRelativePathStringAndNonNullValue()
    {
        asyncTest(new Action1<FileSystem>()
        {
            @Override
            public void run(FileSystem fileSystem)
            {
                final Value<File> file = new Value<>();
                fileSystem.createFileAsync("things.txt", file)
                        .then(new Action1<Boolean>()
                        {
                            @Override
                            public void run(Boolean fileCreated)
                            {
                                assertFalse(fileCreated);
                                assertFalse(file.hasValue());
                            }
                        });
            }
        });
    }

    @Test
    public void createFileAsyncWithNonExistingRootedPathStringAndNonNullValue()
    {
        asyncTest(new Action1<FileSystem>()
        {
            @Override
            public void run(final FileSystem fileSystem)
            {
                final Value<File> file = new Value<>();
                fileSystem.createFileAsync("/things.txt", file)
                        .then(new Action1<Boolean>()
                        {
                            @Override
                            public void run(Boolean fileCreated)
                            {
                                assertTrue(fileCreated);
                                assertEquals("/things.txt", file.get().getPath().toString());
                            }
                        });
            }
        });
    }

    @Test
    public void createFileAsyncWithExistingRootedPathStringAndNonNullValue()
    {
        asyncTest(new Action1<FileSystem>()
        {
            @Override
            public void run(final FileSystem fileSystem)
            {
                final Value<File> file = new Value<>();
                fileSystem.createFile("/things.txt", file);

                fileSystem.createFileAsync("/things.txt", file)
                        .then(new Action1<Boolean>()
                        {
                            @Override
                            public void run(Boolean fileCreated)
                            {
                                assertFalse(fileCreated);
                                assertTrue(file.hasValue());
                                assertNotNull(file.get());
                                assertEquals(Path.parse("/things.txt"), file.get().getPath());
                            }
                        });
            }
        });
    }

    @Test
    public void createFileAsyncWithInvalidPathString()
    {
        asyncTest(new Action1<FileSystem>()
        {
            @Override
            public void run(final FileSystem fileSystem)
            {
                final Value<File> file = new Value<>();
                fileSystem.createFileAsync("/\u0000?#!.txt", file)
                        .then(new Action1<Boolean>()
                        {
                            @Override
                            public void run(Boolean fileCreated)
                            {
                                assertFalse(fileCreated);
                                assertFalse(file.hasValue());
                            }
                        });
            }
        });
    }

    @Test
    public void createFileAsyncWithNullPath()
    {
        asyncTest(new Action1<FileSystem>()
        {
            @Override
            public void run(FileSystem fileSystem)
            {
                fileSystem.createFileAsync((Path)null)
                        .then(new Action1<Boolean>()
                        {
                            @Override
                            public void run(Boolean fileCreated)
                            {
                                assertFalse(fileCreated);
                            }
                        });
            }
        });
    }

    @Test
    public void createFileAsyncWithEmptyPath()
    {
        asyncTest(new Action1<FileSystem>()
        {
            @Override
            public void run(FileSystem fileSystem)
            {
                fileSystem.createFileAsync(Path.parse(""))
                        .then(new Action1<Boolean>()
                        {
                            @Override
                            public void run(Boolean fileCreated)
                            {
                                assertFalse(fileCreated);
                            }
                        });
            }
        });
    }

    @Test
    public void createFileAsyncWithRelativePath()
    {
        asyncTest(new Action1<FileSystem>()
        {
            @Override
            public void run(FileSystem fileSystem)
            {
                fileSystem.createFileAsync(Path.parse("things.txt"))
                        .then(new Action1<Boolean>()
                        {
                            @Override
                            public void run(Boolean fileCreated)
                            {
                                assertFalse(fileCreated);
                            }
                        });
            }
        });
    }

    @Test
    public void createFileAsyncWithNonExistingRootedPath()
    {
        asyncTest(new Action1<FileSystem>()
        {
            @Override
            public void run(final FileSystem fileSystem)
            {
                fileSystem.createFileAsync(Path.parse("/things.txt"))
                        .then(new Action1<Boolean>()
                        {
                            @Override
                            public void run(Boolean fileCreated)
                            {
                                assertTrue(fileCreated);
                                assertTrue(fileSystem.fileExists("/things.txt"));
                            }
                        });
            }
        });
    }

    @Test
    public void createFileAsyncWithExistingRootedPath()
    {
        asyncTest(new Action1<FileSystem>()
        {
            @Override
            public void run(final FileSystem fileSystem)
            {
                fileSystem.createFile("/things.txt");

                fileSystem.createFileAsync(Path.parse("/things.txt"))
                        .then(new Action1<Boolean>()
                        {
                            @Override
                            public void run(Boolean fileCreated)
                            {
                                assertFalse(fileCreated);
                                assertTrue(fileSystem.fileExists("/things.txt"));
                            }
                        });
            }
        });
    }

    @Test
    public void createFileAsyncWithNullPathAndNullValue()
    {
        asyncTest(new Action1<FileSystem>()
        {
            @Override
            public void run(final FileSystem fileSystem)
            {
                fileSystem.createFileAsync((Path)null, null)
                        .then(new Action1<Boolean>()
                        {
                            @Override
                            public void run(Boolean fileCreated)
                            {
                                assertFalse(fileCreated);
                            }
                        });
            }
        });
    }

    @Test
    public void createFileAsyncWithEmptyPathAndNullValue()
    {
        asyncTest(new Action1<FileSystem>()
        {
            @Override
            public void run(FileSystem fileSystem)
            {
                fileSystem.createFileAsync(Path.parse(""), null)
                        .then(new Action1<Boolean>()
                        {
                            @Override
                            public void run(Boolean fileCreated)
                            {
                                assertFalse(fileCreated);
                            }
                        });
            }
        });
    }

    @Test
    public void createFileAsyncWithRelativePathAndNullValue()
    {
        asyncTest(new Action1<FileSystem>()
        {
            @Override
            public void run(FileSystem fileSystem)
            {
                fileSystem.createFileAsync(Path.parse("things.txt"), null)
                        .then(new Action1<Boolean>()
                        {
                            @Override
                            public void run(Boolean fileCreated)
                            {
                                assertFalse(fileCreated);
                            }
                        });
            }
        });
    }

    @Test
    public void createFileAsyncWithNonExistingRootedPathAndNullValue()
    {
        asyncTest(new Action1<FileSystem>()
        {
            @Override
            public void run(FileSystem fileSystem)
            {
                fileSystem.createFileAsync(Path.parse("/things.txt"), null)
                        .then(new Action1<Boolean>()
                        {
                            @Override
                            public void run(Boolean fileCreated)
                            {
                                assertTrue(fileCreated);
                            }
                        });
            }
        });
    }

    @Test
    public void createFileAsyncWithExistingRootedPathAndNullValue()
    {
        asyncTest(new Action1<FileSystem>()
        {
            @Override
            public void run(FileSystem fileSystem)
            {
                fileSystem.createFile("/things.txt");

                fileSystem.createFileAsync(Path.parse("/things.txt"), null)
                        .then(new Action1<Boolean>()
                        {
                            @Override
                            public void run(Boolean fileCreated)
                            {
                                assertFalse(fileCreated);
                            }
                        });
            }
        });
    }

    @Test
    public void createFileAsyncWithNullPathAndNonNullValue()
    {
        asyncTest(new Action1<FileSystem>()
        {
            @Override
            public void run(FileSystem fileSystem)
            {
                final Value<File> file = new Value<>();
                fileSystem.createFileAsync((Path)null, file)
                        .then(new Action1<Boolean>()
                        {
                            @Override
                            public void run(Boolean fileCreated)
                            {
                                assertFalse(fileCreated);
                                assertFalse(file.hasValue());
                            }
                        });
            }
        });
    }

    @Test
    public void createFileAsyncWithEmptyPathAndNonNullValue()
    {
        asyncTest(new Action1<FileSystem>()
        {
            @Override
            public void run(FileSystem fileSystem)
            {
                final Value<File> file = new Value<>();
                fileSystem.createFileAsync(Path.parse(""), file)
                        .then(new Action1<Boolean>()
                        {
                            @Override
                            public void run(Boolean fileCreated)
                            {
                                assertFalse(fileCreated);
                                assertFalse(file.hasValue());
                            }
                        });
            }
        });
    }

    @Test
    public void createFileAsyncWithRelativePathAndNonNullValue()
    {
        asyncTest(new Action1<FileSystem>()
        {
            @Override
            public void run(FileSystem fileSystem)
            {
                final Value<File> file = new Value<>();
                fileSystem.createFileAsync(Path.parse("things.txt"), file)
                        .then(new Action1<Boolean>()
                        {
                            @Override
                            public void run(Boolean fileCreated)
                            {
                                assertFalse(fileCreated);
                                assertFalse(file.hasValue());
                            }
                        });
            }
        });
    }

    @Test
    public void createFileAsyncWithNonExistingRootedPathAndNonNullValue()
    {
        asyncTest(new Action1<FileSystem>()
        {
            @Override
            public void run(final FileSystem fileSystem)
            {
                final Value<File> file = new Value<>();
                fileSystem.createFileAsync(Path.parse("/things.txt"), file)
                        .then(new Action1<Boolean>()
                        {
                            @Override
                            public void run(Boolean fileCreated)
                            {
                                assertTrue(fileCreated);
                                assertEquals("/things.txt", file.get().getPath().toString());
                            }
                        });
            }
        });
    }

    @Test
    public void createFileAsyncWithExistingRootedPathAndNonNullValue()
    {
        asyncTest(new Action1<FileSystem>()
        {
            @Override
            public void run(final FileSystem fileSystem)
            {
                final Value<File> file = new Value<>();
                fileSystem.createFile("/things.txt", file);

                fileSystem.createFileAsync(Path.parse("/things.txt"), file)
                        .then(new Action1<Boolean>()
                        {
                            @Override
                            public void run(Boolean fileCreated)
                            {
                                assertFalse(fileCreated);
                                assertTrue(file.hasValue());
                                assertNotNull(file.get());
                                assertEquals(Path.parse("/things.txt"), file.get().getPath());
                            }
                        });
            }
        });
    }

    @Test
    public void createFileAsyncWithInvalidPath()
    {
        asyncTest(new Action1<FileSystem>()
        {
            @Override
            public void run(final FileSystem fileSystem)
            {
                final Value<File> file = new Value<>();
                fileSystem.createFileAsync(Path.parse("/\u0000?#!.txt"), file)
                        .then(new Action1<Boolean>()
                        {
                            @Override
                            public void run(Boolean fileCreated)
                            {
                                assertFalse(fileCreated);
                                assertFalse(file.hasValue());
                            }
                        });
            }
        });
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

    @Test
    public void deleteFileAsyncWithNullPathString()
    {
        asyncTest(new Action1<FileSystem>()
        {
            @Override
            public void run(FileSystem fileSystem)
            {
                fileSystem.deleteFileAsync((String)null)
                        .then(new Action1<Boolean>()
                        {
                            @Override
                            public void run(Boolean fileDeleted)
                            {
                                assertFalse(fileDeleted);
                            }
                        });
            }
        });
    }

    @Test
    public void deleteFileAsyncWithEmptyPathString()
    {
        asyncTest(new Action1<FileSystem>()
        {
            @Override
            public void run(FileSystem fileSystem)
            {
                fileSystem.deleteFileAsync("")
                        .then(new Action1<Boolean>()
                        {
                            @Override
                            public void run(Boolean fileDeleted)
                            {
                                assertFalse(fileDeleted);
                            }
                        });
            }
        });
    }

    @Test
    public void deleteFileAsyncWithRelativePathString()
    {
        asyncTest(new Action1<FileSystem>()
        {
            @Override
            public void run(FileSystem fileSystem)
            {
                fileSystem.deleteFileAsync("relativeFile.txt")
                        .then(new Action1<Boolean>()
                        {
                            @Override
                            public void run(Boolean fileDeleted)
                            {
                                assertFalse(fileDeleted);
                            }
                        });
            }
        });
    }

    @Test
    public void deleteFileAsyncWithRootedPathStringThatDoesntExist()
    {
        asyncTest(new Action1<FileSystem>()
        {
            @Override
            public void run(FileSystem fileSystem)
            {
                fileSystem.deleteFileAsync("/idontexist.txt")
                        .then(new Action1<Boolean>()
                        {
                            @Override
                            public void run(Boolean fileDeleted)
                            {
                                assertFalse(fileDeleted);
                            }
                        });
            }
        });
    }

    @Test
    public void deleteFileAsyncWithRootedPathStringThatExists()
    {
        asyncTest(new Action1<FileSystem>()
        {
            @Override
            public void run(final FileSystem fileSystem)
            {
                fileSystem.createFile("/iexist.txt");
                fileSystem.deleteFileAsync("/iexist.txt")
                        .then(new Action1<Boolean>()
                        {
                            @Override
                            public void run(Boolean fileDeleted)
                            {
                                assertTrue(fileDeleted);
                                assertFalse(fileSystem.fileExists("/iexist.txt"));
                            }
                        });
            }
        });
    }

    @Test
    public void deleteFileAsyncWithNullPath()
    {
        asyncTest(new Action1<FileSystem>()
        {
            @Override
            public void run(FileSystem fileSystem)
            {
                fileSystem.deleteFileAsync((Path)null)
                        .then(new Action1<Boolean>()
                        {
                            @Override
                            public void run(Boolean fileDeleted)
                            {
                                assertFalse(fileDeleted);
                            }
                        });
            }
        });
    }

    @Test
    public void deleteFileAsyncWithEmptyPath()
    {
        asyncTest(new Action1<FileSystem>()
        {
            @Override
            public void run(FileSystem fileSystem)
            {
                fileSystem.deleteFileAsync(Path.parse(""))
                        .then(new Action1<Boolean>()
                        {
                            @Override
                            public void run(Boolean fileDeleted)
                            {
                                assertFalse(fileDeleted);
                            }
                        });
            }
        });
    }

    @Test
    public void deleteFileAsyncWithRelativePath()
    {
        asyncTest(new Action1<FileSystem>()
        {
            @Override
            public void run(FileSystem fileSystem)
            {
                fileSystem.deleteFileAsync(Path.parse("relativeFile.txt"))
                        .then(new Action1<Boolean>()
                        {
                            @Override
                            public void run(Boolean fileDeleted)
                            {
                                assertFalse(fileDeleted);
                            }
                        });
            }
        });
    }

    @Test
    public void deleteFileAsyncWithRootedPathThatDoesntExist()
    {
        asyncTest(new Action1<FileSystem>()
        {
            @Override
            public void run(FileSystem fileSystem)
            {
                fileSystem.deleteFileAsync(Path.parse("/idontexist.txt"))
                        .then(new Action1<Boolean>()
                        {
                            @Override
                            public void run(Boolean fileDeleted)
                            {
                                assertFalse(fileDeleted);
                            }
                        });
            }
        });
    }

    @Test
    public void deleteFileAsyncWithRootedPathThatExists()
    {
        asyncTest(new Action1<FileSystem>()
        {
            @Override
            public void run(final FileSystem fileSystem)
            {
                fileSystem.createFile("/iexist.txt");
                fileSystem.deleteFileAsync(Path.parse("/iexist.txt"))
                        .then(new Action1<Boolean>()
                        {
                            @Override
                            public void run(Boolean fileDeleted)
                            {
                                assertTrue(fileDeleted);
                                assertFalse(fileSystem.fileExists("/iexist.txt"));
                            }
                        });
            }
        });
    }

    @Test
    public void getFileContentsStringWithNull()
    {
        final FileSystem fileSystem = getFileSystem();
        assertNull(fileSystem.getFileContents((String)null));
    }

    @Test
    public void getFileContentsStringWithEmpty()
    {
        final FileSystem fileSystem = getFileSystem();
        assertNull(fileSystem.getFileContents(""));
    }

    @Test
    public void getFileContentsStringWithRelativePath()
    {
        final FileSystem fileSystem = getFileSystem();
        assertNull(fileSystem.getFileContents("thing.txt"));
    }

    @Test
    public void getFileContentsStringWithNonExistingRootedPath()
    {
        final FileSystem fileSystem = getFileSystem();
        assertNull(fileSystem.getFileContents("/thing.txt"));
    }

    @Test
    public void getFileContentsStringWithEmptyFile()
    {
        final FileSystem fileSystem = getFileSystem();
        fileSystem.createFile("/thing.txt");
        assertArrayEquals(new byte[0], fileSystem.getFileContents("/thing.txt"));
    }

    @Test
    public void getFileContentsPathWithNull()
    {
        final FileSystem fileSystem = getFileSystem();
        assertNull(fileSystem.getFileContents((Path)null));
    }

    @Test
    public void getFileContentsPathWithEmpty()
    {
        final FileSystem fileSystem = getFileSystem();
        assertNull(fileSystem.getFileContents(Path.parse("")));
    }

    @Test
    public void getFileContentsPathWithRelativePath()
    {
        final FileSystem fileSystem = getFileSystem();
        assertNull(fileSystem.getFileContents(Path.parse("thing.txt")));
    }

    @Test
    public void getFileContentsPathWithNonExistingRootedPath()
    {
        final FileSystem fileSystem = getFileSystem();
        assertNull(fileSystem.getFileContents(Path.parse("/thing.txt")));
    }

    @Test
    public void getFileContentsPathWithEmptyFile()
    {
        final FileSystem fileSystem = getFileSystem();
        fileSystem.createFile("/thing.txt");
        assertArrayEquals(new byte[0], fileSystem.getFileContents(Path.parse("/thing.txt")));
    }

    @Test
    public void getFileContentsAsStringWithNullPathString()
    {
        final FileSystem fileSystem = getFileSystem();
        assertNull(fileSystem.getFileContentsAsString((String)null));
    }

    @Test
    public void getFileContentsAsStringWithEmptyPathString()
    {
        final FileSystem fileSystem = getFileSystem();
        assertNull(fileSystem.getFileContentsAsString(""));
    }

    @Test
    public void getFileContentsAsStringWithRelativePathString()
    {
        final FileSystem fileSystem = getFileSystem();
        assertNull(fileSystem.getFileContentsAsString("file.txt"));
    }

    @Test
    public void getFileContentsAsStringWithNonExistingRootedPathString()
    {
        final FileSystem fileSystem = getFileSystem();
        assertNull(fileSystem.getFileContentsAsString("/file.txt"));
    }

    @Test
    public void getFileContentsAsStringWithExistingRootedPathStringWithNoContents()
    {
        final FileSystem fileSystem = getFileSystem();
        fileSystem.createFile("/file.txt");
        assertEquals("", fileSystem.getFileContentsAsString("/file.txt"));
    }

    @Test
    public void getFileContentsAsStringWithExistingRootPathStringWithContents()
    {
        final FileSystem fileSystem = getFileSystem();
        fileSystem.createFile("/file.txt", CharacterEncoding.ASCII.encode("Hello there!"));
        assertEquals("Hello there!", fileSystem.getFileContentsAsString("/file.txt"));
    }

    @Test
    public void getFileContentsAsStringWithNullEncodingAndNullPathString()
    {
        final FileSystem fileSystem = getFileSystem();
        assertNull(fileSystem.getFileContentsAsString((String)null));
    }

    @Test
    public void getFileContentsAsStringWithNullEncodingAndEmptyPathString()
    {
        final FileSystem fileSystem = getFileSystem();
        assertNull(fileSystem.getFileContentsAsString(""));
    }

    @Test
    public void getFileContentsAsStringWithNullEncodingAndRelativePathString()
    {
        final FileSystem fileSystem = getFileSystem();
        assertNull(fileSystem.getFileContentsAsString("file.txt"));
    }

    @Test
    public void getFileContentsAsStringWithNullEncodingAndNonExistingRootedPathString()
    {
        final FileSystem fileSystem = getFileSystem();
        assertNull(fileSystem.getFileContentsAsString("/file.txt"));
    }

    @Test
    public void getFileContentsAsStringWithNullEncodingAndExistingRootedPathStringWithNoContents()
    {
        final FileSystem fileSystem = getFileSystem();
        fileSystem.createFile("/file.txt");
        assertNull(fileSystem.getFileContentsAsString("/file.txt", null));
    }

    @Test
    public void getFileContentsAsStringWithNullEncodingAndExistingRootPathStringWithContents()
    {
        final FileSystem fileSystem = getFileSystem();
        fileSystem.createFile("/file.txt", CharacterEncoding.ASCII.encode("Hello there!"));
        assertNull(fileSystem.getFileContentsAsString("/file.txt", null));
    }

    @Test
    public void getFileContentBlocksStringWithNull()
    {
        final FileSystem fileSystem = getFileSystem();
        assertNull(fileSystem.getFileContentBlocks((String)null));
    }

    @Test
    public void getFileContentBlocksStringWithEmpty()
    {
        final FileSystem fileSystem = getFileSystem();
        assertNull(fileSystem.getFileContentBlocks(""));
    }

    @Test
    public void getFileContentBlocksStringWithRelativePath()
    {
        final FileSystem fileSystem = getFileSystem();
        assertNull(fileSystem.getFileContentBlocks("B"));
    }

    @Test
    public void getFileContentBlocksStringWithNonExistingRootedFilePath()
    {
        final FileSystem fileSystem = getFileSystem();
        assertNull(fileSystem.getFileContentBlocks("/a.txt"));
    }

    @Test
    public void getFileContentBlocksStringWithExistingRootedFilePathWithNoContents()
    {
        final FileSystem fileSystem = getFileSystem();
        fileSystem.createFile("/a.txt");

        final Iterable<byte[]> fileContentBlocks = fileSystem.getFileContentBlocks("/a.txt");
        assertNotNull(fileContentBlocks);
        assertEquals(0, fileContentBlocks.getCount());
    }

    @Test
    public void getFileContentBlocksStringWithExistingRootedFilePathWithContents()
    {
        final FileSystem fileSystem = getFileSystem();
        fileSystem.createFile("/a.txt", new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11});

        final Iterable<byte[]> fileContentBlocks = fileSystem.getFileContentBlocks("/a.txt");
        assertNotNull(fileContentBlocks);

        final byte[] fileContents = Array.merge(fileContentBlocks);
        assertArrayEquals(new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11 }, fileContents);
    }

    @Test
    public void setFileContentsWithNullPathString()
    {
        final FileSystem fileSystem = getFileSystem();
        assertFalse(fileSystem.setFileContents((String)null, new byte[] { 0, 1, 2 }));
    }

    @Test
    public void setFileContentsWithEmptyPathString()
    {
        final FileSystem fileSystem = getFileSystem();
        assertFalse(fileSystem.setFileContents("", new byte[] { 0, 1, 2 }));
    }

    @Test
    public void setFileContentsWithRelativePathString()
    {
        final FileSystem fileSystem = getFileSystem();
        assertFalse(fileSystem.setFileContents("relative.file", new byte[] { 0, 1, 2 }));
    }

    @Test
    public void setFileContentsWithNonExistingRootedPathStringAndNullContents()
    {
        final FileSystem fileSystem = getFileSystem();
        assertTrue(fileSystem.setFileContents("/A.txt", null));
        assertTrue(fileSystem.fileExists("/A.txt"));
        assertArrayEquals(new byte[0], fileSystem.getFileContents("/A.txt"));
    }

    @Test
    public void setFileContentsWithExistingRootedPathStringAndNullContents()
    {
        final FileSystem fileSystem = getFileSystem();
        fileSystem.createFile("/A.txt", new byte[] { 0, 1 });

        assertTrue(fileSystem.setFileContents("/A.txt", null));
        assertTrue(fileSystem.fileExists("/A.txt"));
        assertArrayEquals(new byte[0], fileSystem.getFileContents("/A.txt"));
    }

    @Test
    public void setFileContentsWithNonExistingRootedPathStringAndEmptyContents()
    {
        final FileSystem fileSystem = getFileSystem();
        assertTrue(fileSystem.setFileContents("/A.txt", new byte[0]));
        assertTrue(fileSystem.fileExists("/A.txt"));
        assertArrayEquals(new byte[0], fileSystem.getFileContents("/A.txt"));
    }

    @Test
    public void setFileContentsWithExistingRootedPathStringAndEmptyContents()
    {
        final FileSystem fileSystem = getFileSystem();
        fileSystem.createFile("/A.txt", new byte[] { 0, 1 });

        assertTrue(fileSystem.setFileContents("/A.txt", new byte[0]));
        assertTrue(fileSystem.fileExists("/A.txt"));
        assertArrayEquals(new byte[0], fileSystem.getFileContents("/A.txt"));
    }

    @Test
    public void setFileContentsWithNonExistingRootedPathStringAndNonEmptyContents()
    {
        final FileSystem fileSystem = getFileSystem();
        assertTrue(fileSystem.setFileContents("/A.txt", new byte[] { 0, 1, 2 }));
        assertTrue(fileSystem.fileExists("/A.txt"));
        assertArrayEquals(new byte[] { 0, 1, 2 }, fileSystem.getFileContents("/A.txt"));
    }

    @Test
    public void setFileContentsWithNonExistingRootedPathStringWithNonExistingParentFolderAndNonEmptyContents()
    {
        final FileSystem fileSystem = getFileSystem();
        assertTrue(fileSystem.setFileContents("/folder/A.txt", new byte[] { 0, 1, 2 }));
        assertTrue(fileSystem.folderExists("/folder"));
        assertTrue(fileSystem.fileExists("/folder/A.txt"));
        assertArrayEquals(new byte[] { 0, 1, 2 }, fileSystem.getFileContents("/folder/A.txt"));
    }

    @Test
    public void setFileContentsWithExistingRootedPathStringAndNonEmptyContents()
    {
        final FileSystem fileSystem = getFileSystem();
        fileSystem.createFile("/A.txt");

        assertTrue(fileSystem.setFileContents("/A.txt", new byte[] { 0, 1, 2 }));
        assertTrue(fileSystem.fileExists("/A.txt"));
        assertArrayEquals(new byte[] { 0, 1, 2 }, fileSystem.getFileContents("/A.txt"));
    }

    @Test
    public void setFileContentsWithNullPath()
    {
        final FileSystem fileSystem = getFileSystem();
        assertFalse(fileSystem.setFileContents((Path)null, new byte[] { 0, 1, 2 }));
    }

    @Test
    public void setFileContentsWithEmptyPath()
    {
        final FileSystem fileSystem = getFileSystem();
        assertFalse(fileSystem.setFileContents(Path.parse(""), new byte[] { 0, 1, 2 }));
    }

    @Test
    public void setFileContentsWithRelativePath()
    {
        final FileSystem fileSystem = getFileSystem();
        assertFalse(fileSystem.setFileContents(Path.parse("relative.file"), new byte[] { 0, 1, 2 }));
    }

    @Test
    public void setFileContentsWithNonExistingRootedPathAndNullContents()
    {
        final FileSystem fileSystem = getFileSystem();

        assertTrue(fileSystem.setFileContents(Path.parse("/A.txt"), null));

        assertTrue(fileSystem.fileExists("/A.txt"));
        assertArrayEquals(new byte[0], fileSystem.getFileContents("/A.txt"));
    }

    @Test
    public void setFileContentsWithExistingRootedPathAndNullContents()
    {
        final FileSystem fileSystem = getFileSystem();
        fileSystem.createFile("/A.txt", new byte[] { 0, 1 });

        assertTrue(fileSystem.setFileContents(Path.parse("/A.txt"), null));

        assertTrue(fileSystem.fileExists("/A.txt"));
        assertArrayEquals(new byte[0], fileSystem.getFileContents("/A.txt"));
    }

    @Test
    public void setFileContentsWithNonExistingRootedPathAndEmptyContents()
    {
        final FileSystem fileSystem = getFileSystem();

        assertTrue(fileSystem.setFileContents(Path.parse("/A.txt"), new byte[0]));

        assertTrue(fileSystem.fileExists("/A.txt"));
        assertArrayEquals(new byte[0], fileSystem.getFileContents("/A.txt"));
    }

    @Test
    public void setFileContentsWithExistingRootedPathAndEmptyContents()
    {
        final FileSystem fileSystem = getFileSystem();
        fileSystem.createFile("/A.txt", new byte[] { 0, 1 });

        assertTrue(fileSystem.setFileContents(Path.parse("/A.txt"), new byte[0]));

        assertTrue(fileSystem.fileExists("/A.txt"));
        assertArrayEquals(new byte[0], fileSystem.getFileContents("/A.txt"));
    }

    @Test
    public void setFileContentsWithNonExistingRootedPathAndNonEmptyContents()
    {
        final FileSystem fileSystem = getFileSystem();

        assertTrue(fileSystem.setFileContents(Path.parse("/A.txt"), new byte[] { 0, 1, 2 }));

        assertTrue(fileSystem.fileExists("/A.txt"));
        assertArrayEquals(new byte[] { 0, 1, 2 }, fileSystem.getFileContents("/A.txt"));
    }

    @Test
    public void setFileContentsWithExistingRootedPathAndNonEmptyContents()
    {
        final FileSystem fileSystem = getFileSystem();
        fileSystem.createFile("/A.txt");

        assertTrue(fileSystem.setFileContents(Path.parse("/A.txt"), new byte[] { 0, 1, 2 }));

        assertTrue(fileSystem.fileExists("/A.txt"));
        assertArrayEquals(new byte[] { 0, 1, 2 }, fileSystem.getFileContents("/A.txt"));
    }

    private void asyncTest(final Action1<FileSystem> action)
    {
        CurrentThreadAsyncRunner.withRegistered(new Action1<CurrentThreadAsyncRunner>()
        {
            @Override
            public void run(CurrentThreadAsyncRunner mainRunner)
            {
                final CurrentThreadAsyncRunner backgroundRunner = new CurrentThreadAsyncRunner();
                final FileSystem fileSystem = getFileSystem(backgroundRunner);
                
                action.run(fileSystem);
                assertEquals(0, mainRunner.getScheduledTaskCount());
                assertEquals(1, backgroundRunner.getScheduledTaskCount());

                backgroundRunner.await();
                assertEquals(1, mainRunner.getScheduledTaskCount());
                assertEquals(0, backgroundRunner.getScheduledTaskCount());

                mainRunner.await();
                assertEquals(0, mainRunner.getScheduledTaskCount());
                assertEquals(0, backgroundRunner.getScheduledTaskCount());
            }
        });
    }
}
