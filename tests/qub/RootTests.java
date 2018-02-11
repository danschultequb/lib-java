package qub;

public class RootTests
{
    public static void test(final TestRunner runner)
    {
        runner.testGroup("Root", new Action0()
        {
            @Override
            public void run()
            {
                runner.test("constructor", new Action1<Test>()
                {
                    @Override
                    public void run(Test test)
                    {
                        final Root root = new Root(null, Path.parse("/path/to/root/"));
                        test.assertEqual("/path/to/root/", root.toString());
                    }
                });
                
                runner.testGroup("equals()", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("with null", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Root root = getRoot();
                                test.assertFalse(root.equals(null));
                            }
                        });
                        
                        runner.test("with String", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Root root = getRoot();
                                test.assertFalse(root.equals(root.toString()));
                            }
                        });
                        
                        runner.test("with same Root", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Root root = getRoot();
                                test.assertTrue(root.equals((Object)root));
                            }
                        });
                        
                        runner.test("with equal Root", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final InMemoryFileSystem fileSystem = getFileSystem();
                                final Root root = getRoot(fileSystem);
                                final Root root2 = getRoot(fileSystem);
                                test.assertTrue(root.equals((Object)root2));
                            }
                        });
                        
                        runner.test("with different Root", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final InMemoryFileSystem fileSystem = getFileSystem();
                                final Root root = getRoot(fileSystem, "C:\\");
                                final Root root2 = getRoot(fileSystem, "D:\\");
                                test.assertFalse(root.equals((Object)root2));
                            }
                        });
                    }
                });
                
                runner.testGroup("getFolder(String)", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("with null", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Root root = getRoot();
                                test.assertNull(root.getFolder((String)null));
                            }
                        });
                        
                        runner.test("with empty String", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Root root = getRoot();
                                test.assertNull(root.getFolder(""));
                            }
                        });
                        
                        runner.test("with relative path that doesn't exist", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Root root = getRoot();
                                final Folder folder = root.getFolder("folderName");
                                test.assertNotNull(folder);
                                test.assertEqual("/folderName", folder.getPath().toString());
                            }
                        });

                        runner.test("with relative path that exists", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Root root = getRoot();
                                root.createFolder("folderName");

                                final Folder folder = root.getFolder("folderName");
                                test.assertNotNull(folder);
                                test.assertEqual("/folderName", folder.getPath().toString());
                            }
                        });
                    }
                });

                runner.testGroup("getFolder(Path)", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("with null", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Root root = getRoot();
                                test.assertNull(root.getFolder((Path)null));
                            }
                        });

                        runner.test("with empty Path", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Root root = getRoot();
                                test.assertNull(root.getFolder(Path.parse("")));
                            }
                        });

                        runner.test("with relative path that doesn't exist", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Root root = getRoot();
                                final Folder folder = root.getFolder(Path.parse("folderName"));
                                test.assertNotNull(folder);
                                test.assertEqual("/folderName", folder.getPath().toString());
                            }
                        });

                        runner.test("with relative path that exists", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Root root = getRoot();
                                root.createFolder("folderName");

                                final Folder folder = root.getFolder(Path.parse("folderName"));
                                test.assertNotNull(folder);
                                test.assertEqual("/folderName", folder.getPath().toString());
                            }
                        });
                    }
                });

                runner.testGroup("getFile(String)", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("with null String", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Root root = getRoot();
                                test.assertNull(root.getFile((String)null));
                            }
                        });

                        runner.test("with empty String", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Root root = getRoot();
                                test.assertNull(root.getFile(""));
                            }
                        });

                        runner.test("with relative path that doesn't exist", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Root root = getRoot();
                                final File file = root.getFile("fileName");
                                test.assertNotNull(file);
                                test.assertEqual("/fileName", file.getPath().toString());
                            }
                        });

                        runner.test("with relative path that exists", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Root root = getRoot();
                                root.createFile("fileName");

                                final File file = root.getFile("fileName");
                                test.assertNotNull(file);
                                test.assertEqual("/fileName", file.getPath().toString());
                            }
                        });
                    }
                });

                runner.testGroup("getFile(Path)", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("with null Path", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Root root = getRoot();
                                test.assertNull(root.getFile((Path)null));
                            }
                        });

                        runner.test("with empty Path", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Root root = getRoot();
                                test.assertNull(root.getFile(Path.parse("")));
                            }
                        });

                        runner.test("with relative Path that doesn't exist", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Root root = getRoot();
                                final File file = root.getFile(Path.parse("fileName"));
                                test.assertNotNull(file);
                                test.assertEqual("/fileName", file.getPath().toString());
                            }
                        });

                        runner.test("with relative Path that exists", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Root root = getRoot();
                                root.createFile("fileName");

                                final File file = root.getFile(Path.parse("fileName"));
                                test.assertNotNull(file);
                                test.assertEqual("/fileName", file.getPath().toString());
                            }
                        });
                    }
                });

                runner.testGroup("createFolder(String)", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("with null String", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Root root = getRoot();
                                test.assertFalse(root.createFolder((String)null));
                            }
                        });

                        runner.test("with empty String", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Root root = getRoot();
                                test.assertFalse(root.createFolder(""));
                            }
                        });

                        runner.test("with relative Path with non-existing folder", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Root root = getRoot();
                                test.assertTrue(root.createFolder("folderName"));
                            }
                        });

                        runner.test("with relative Path when root doesn't exist", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Root root = getRoot("C:/");
                                test.assertFalse(root.createFolder("folderName"));
                            }
                        });

                        runner.test("with relative Path with folder that exists", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Root root = getRoot();
                                root.createFolder("A");

                                test.assertFalse(root.createFolder("A"));
                            }
                        });
                    }
                });

                runner.testGroup("createFolder(String,Value<Folder>)", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("with null String", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Root root = getRoot();
                                final Value<Folder> createdFolder = new Value<>();
                                test.assertFalse(root.createFolder((String)null, createdFolder));
                                test.assertFalse(createdFolder.hasValue());
                            }
                        });

                        runner.test("with empty String", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Root root = getRoot();
                                final Value<Folder> createdFolder = new Value<>();
                                test.assertFalse(root.createFolder(""));
                                test.assertFalse(createdFolder.hasValue());
                            }
                        });

                        runner.test("with non-existing folder", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Root root = getRoot();
                                final Value<Folder> createdFolder = new Value<>();
                                test.assertTrue(root.createFolder("folderName", createdFolder));
                                test.assertEqual(Path.parse("/folderName"), createdFolder.get().getPath());
                            }
                        });

                        runner.test("with non-existing folder when root doesn't exist", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Root root = getRoot("C:/");
                                final Value<Folder> createdFolder = new Value<>();
                                test.assertFalse(root.createFolder("folderName", createdFolder));
                                test.assertFalse(createdFolder.hasValue());
                            }
                        });

                        runner.test("with existing folder", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Root root = getRoot();
                                root.createFolder("A");

                                final Value<Folder> createdFolder = new Value<>();
                                test.assertFalse(root.createFolder("A", createdFolder));
                                test.assertEqual(Path.parse("/A"), createdFolder.get().getPath());
                            }
                        });
                    }
                });

                runner.testGroup("createFolder(Path)", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("with null Path", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Root root = getRoot();
                                test.assertFalse(root.createFolder((Path)null));
                            }
                        });

                        runner.test("with empty Path", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Root root = getRoot();
                                test.assertFalse(root.createFolder(Path.parse("")));
                            }
                        });

                        runner.test("with non-existing folder", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Root root = getRoot();
                                test.assertTrue(root.createFolder(Path.parse("folderName")));
                            }
                        });

                        runner.test("with non-existing folder when root doesn't exist", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Root root = getRoot("C:/");
                                test.assertFalse(root.createFolder(Path.parse("folderName")));
                            }
                        });

                        runner.test("with existing folder", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Root root = getRoot();
                                root.createFolder("A");

                                test.assertFalse(root.createFolder(Path.parse("A")));
                            }
                        });
                    }
                });

                runner.testGroup("createFolder(Path,Value<Folder>)", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("with null Path", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Root root = getRoot();
                                final Value<Folder> createdFolder = new Value<>();
                                test.assertFalse(root.createFolder((Path)null, createdFolder));
                                test.assertFalse(createdFolder.hasValue());
                            }
                        });

                        runner.test("with empty Path", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Root root = getRoot();
                                final Value<Folder> createdFolder = new Value<>();
                                test.assertFalse(root.createFolder(Path.parse("")));
                                test.assertFalse(createdFolder.hasValue());
                            }
                        });

                        runner.test("with non-existing folder", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Root root = getRoot();
                                final Value<Folder> createdFolder = new Value<>();
                                test.assertTrue(root.createFolder(Path.parse("folderName"), createdFolder));
                                test.assertEqual(Path.parse("/folderName"), createdFolder.get().getPath());
                            }
                        });

                        runner.test("with non-existing folder when root doesn't exist", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Root root = getRoot("C:/");
                                final Value<Folder> createdFolder = new Value<>();
                                test.assertFalse(root.createFolder(Path.parse("folderName"), createdFolder));
                                test.assertFalse(createdFolder.hasValue());
                            }
                        });

                        runner.test("with existing folder", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Root root = getRoot();
                                root.createFolder("A");

                                final Value<Folder> createdFolder = new Value<>();
                                test.assertFalse(root.createFolder(Path.parse("A"), createdFolder));
                                test.assertEqual(Path.parse("/A"), createdFolder.get().getPath());
                            }
                        });
                    }
                });

                runner.testGroup("createFile(String)", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("with null String", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Root root = getRoot();
                                test.assertFalse(root.createFile((String)null));
                            }
                        });

                        runner.test("with empty String", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Root root = getRoot();
                                test.assertFalse(root.createFile(""));
                            }
                        });

                        runner.test("with non-existing file", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Root root = getRoot();
                                test.assertTrue(root.createFile("fileName"));
                            }
                        });

                        runner.test("with non-exisitng file when root doesn't exist", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Root root = getRoot("C:/");
                                test.assertFalse(root.createFile("fileName"));
                            }
                        });

                        runner.test("with existing file", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Root root = getRoot();
                                root.createFile("A");

                                test.assertFalse(root.createFile("A"));
                            }
                        });
                    }
                });

                runner.testGroup("createFile(String,Value<File>)", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("with null String", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Root root = getRoot();
                                final Value<File> createdFile = new Value<>();
                                test.assertFalse(root.createFile((String)null, createdFile));
                                test.assertFalse(createdFile.hasValue());
                            }
                        });

                        runner.test("with empty String", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Root root = getRoot();
                                final Value<File> createdFile = new Value<>();
                                test.assertFalse(root.createFile(""));
                                test.assertFalse(createdFile.hasValue());
                            }
                        });

                        runner.test("with non-existing file", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Root root = getRoot();
                                final Value<File> createdFile = new Value<>();
                                test.assertTrue(root.createFile("fileName", createdFile));
                                test.assertEqual(Path.parse("/fileName"), createdFile.get().getPath());
                            }
                        });

                        runner.test("with non-existing file when root doesn't exist", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Root root = getRoot("C:/");
                                final Value<File> createdFile = new Value<>();
                                test.assertFalse(root.createFile("fileName", createdFile));
                                test.assertFalse(createdFile.hasValue());
                            }
                        });

                        runner.test("with existing file", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Root root = getRoot();
                                root.createFile("A");

                                final Value<File> createdFile = new Value<>();
                                test.assertFalse(root.createFile("A", createdFile));
                                test.assertEqual(Path.parse("/A"), createdFile.get().getPath());
                            }
                        });
                    }
                });

                runner.testGroup("createFile(Path)", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("with null Path", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Root root = getRoot();
                                test.assertFalse(root.createFile((Path)null));
                            }
                        });

                        runner.test("with empty Path", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Root root = getRoot();
                                test.assertFalse(root.createFile(Path.parse("")));
                            }
                        });

                        runner.test("with non-existing file", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Root root = getRoot();
                                test.assertTrue(root.createFile(Path.parse("fileName")));
                            }
                        });

                        runner.test("with non-existing file when root doesn't exist", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Root root = getRoot("C:/");
                                test.assertFalse(root.createFile(Path.parse("fileName")));
                            }
                        });

                        runner.test("with existing file", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Root root = getRoot();
                                root.createFile("A");

                                test.assertFalse(root.createFile(Path.parse("A")));
                            }
                        });
                    }
                });

                runner.testGroup("createFile(Path,Value<File>)", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("with null Path", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Root root = getRoot();
                                final Value<File> createdFile = new Value<>();
                                test.assertFalse(root.createFile((Path)null, createdFile));
                                test.assertFalse(createdFile.hasValue());
                            }
                        });

                        runner.test("with empty Path", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Root root = getRoot();
                                final Value<File> createdFile = new Value<>();
                                test.assertFalse(root.createFile(Path.parse("")));
                                test.assertFalse(createdFile.hasValue());
                            }
                        });

                        runner.test("with non-existing file", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Root root = getRoot();
                                final Value<File> createdFile = new Value<>();
                                test.assertTrue(root.createFile(Path.parse("fileName"), createdFile));
                                test.assertEqual(Path.parse("/fileName"), createdFile.get().getPath());
                            }
                        });

                        runner.test("with non-existing file when root doesn't exist", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Root root = getRoot("C:/");
                                final Value<File> createdFile = new Value<>();
                                test.assertFalse(root.createFile(Path.parse("fileName"), createdFile));
                                test.assertFalse(createdFile.hasValue());
                            }
                        });

                        runner.test("with existing file", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Root root = getRoot();
                                root.createFile("A");

                                final Value<File> createdFile = new Value<>();
                                test.assertFalse(root.createFile(Path.parse("A"), createdFile));
                                test.assertEqual(Path.parse("/A"), createdFile.get().getPath());
                            }
                        });
                    }
                });

                runner.testGroup("getFolders()", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("when root doesn't exist", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Root root = getRoot("C:/");
                                test.assertNull(root.getFolders());
                            }
                        });

                        runner.test("when root is empty", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Root root = getRoot();
                                final Iterable<Folder> folders = root.getFolders();
                                test.assertNotNull(folders);
                                test.assertFalse(folders.any());
                            }
                        });

                        runner.test("when root has a file", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Root root = getRoot();
                                root.createFile("thing.txt");

                                final Iterable<Folder> folders = root.getFolders();
                                test.assertNotNull(folders);
                                test.assertFalse(folders.any());
                            }
                        });

                        runner.test("when root has a folder", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Root root = getRoot();
                                root.createFolder("things");

                                final Iterable<Folder> folders = root.getFolders();
                                test.assertNotNull(folders);
                                test.assertEqual(1, folders.getCount());
                                test.assertEqual(root.getFolder("things"), folders.first());
                            }
                        });
                    }
                });

                runner.testGroup("getFiles()", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("when root doesn't exist", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Root root = getRoot("C:/");
                                test.assertNull(root.getFiles());
                            }
                        });

                        runner.test("when root is empty", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Root root = getRoot();
                                final Iterable<File> files = root.getFiles();
                                test.assertNotNull(files);
                                test.assertFalse(files.any());
                            }
                        });

                        runner.test("when root has a file", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Root root = getRoot();
                                root.createFile("thing.txt");

                                final Iterable<File> files = root.getFiles();
                                test.assertNotNull(files);
                                test.assertEqual(1, files.getCount());
                                test.assertEqual(root.getFile("thing.txt"), files.first());
                            }
                        });

                        runner.test("when root has a folder", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Root root = getRoot();
                                root.createFolder("things");

                                final Iterable<File> files = root.getFiles();
                                test.assertNotNull(files);
                                test.assertFalse(files.any());
                            }
                        });
                    }
                });

                runner.testGroup("getFilesAndFolders()", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("when root doesn't exist", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Root root = getRoot("C:/");
                                test.assertNull(root.getFilesAndFolders());
                            }
                        });

                        runner.test("when root is empty", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Root root = getRoot();
                                final Iterable<FileSystemEntry> entries = root.getFilesAndFolders();
                                test.assertNotNull(entries);
                                test.assertFalse(entries.any());
                            }
                        });

                        runner.test("when root has a file", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Root root = getRoot();
                                root.createFile("thing.txt");

                                final Iterable<FileSystemEntry> entries = root.getFilesAndFolders();
                                test.assertNotNull(entries);
                                test.assertEqual(1, entries.getCount());
                                test.assertEqual(root.getFile("thing.txt"), entries.first());
                            }
                        });

                        runner.test("when root has a folder", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Root root = getRoot();
                                root.createFolder("things");

                                final Iterable<FileSystemEntry> entries = root.getFilesAndFolders();
                                test.assertNotNull(entries);
                                test.assertEqual(1, entries.getCount());
                                test.assertEqual(root.getFolder("things"), entries.first());
                            }
                        });
                    }
                });

                runner.testGroup("getFilesAndFoldersRecursively()", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("when root doesn't exist", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Root root = getRoot("C:/");
                                test.assertNull(root.getFilesAndFoldersRecursively());
                            }
                        });

                        runner.test("when root is empty", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Root root = getRoot();
                                test.assertEqual(new Array<FileSystemEntry>(0), root.getFilesAndFoldersRecursively());
                            }
                        });

                        runner.test("when root has files", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Root root = getRoot();
                                root.createFile("1.txt");
                                root.createFile("2.txt");
                                test.assertEqual(
                                    Array.fromValues(new FileSystemEntry[]
                                    {
                                        root.getFile("1.txt"),
                                        root.getFile("2.txt")
                                    }),
                                    root.getFilesAndFoldersRecursively());
                            }
                        });

                        runner.test("when root has folders", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Root root = getRoot();
                                root.createFolder("1.txt");
                                root.createFolder("2.txt");
                                test.assertEqual(
                                    Array.fromValues(new FileSystemEntry[]
                                    {
                                        root.getFolder("1.txt"),
                                        root.getFolder("2.txt")
                                    }),
                                    root.getFilesAndFoldersRecursively());
                            }
                        });

                        runner.test("when root has grandchild files and folders", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Root root = getRoot();
                                root.createFile("1.txt");
                                root.createFile("2.txt");
                                root.createFile("A/3.csv");
                                root.createFile("B/C/4.xml");
                                root.createFile("A/5.png");

                                final Iterable<FileSystemEntry> expectedEntries =
                                    Array.fromValues(new FileSystemEntry[]
                                    {
                                        root.getFolder("A"),
                                        root.getFolder("B"),
                                        root.getFile("1.txt"),
                                        root.getFile("2.txt"),
                                        root.getFile("A/3.csv"),
                                        root.getFile("A/5.png"),
                                        root.getFolder("B/C"),
                                        root.getFile("B/C/4.xml")
                                    });
                                final Iterable<FileSystemEntry> actualEntries = root.getFilesAndFoldersRecursively();
                                test.assertEqual(expectedEntries, actualEntries);
                            }
                        });
                    }
                });

                runner.testGroup("getFilesRecursively()", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("when root doesn't exist", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Root root = getRoot("C:/");
                                test.assertNull(root.getFilesRecursively());
                            }
                        });

                        runner.test("when root is empty", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Root root = getRoot();
                                test.assertEqual(new Array<FileSystemEntry>(0), root.getFilesRecursively());
                            }
                        });

                        runner.test("when root has files", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Root root = getRoot();
                                root.createFile("1.txt");
                                root.createFile("2.txt");
                                test.assertEqual(
                                    Array.fromValues(new File[]
                                    {
                                        root.getFile("1.txt"),
                                        root.getFile("2.txt")
                                    }),
                                    root.getFilesRecursively());
                            }
                        });

                        runner.test("when root has folders", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Root root = getRoot();
                                root.createFolder("1.txt");
                                root.createFolder("2.txt");
                                test.assertEqual(
                                    new Array<File>(0),
                                    root.getFilesRecursively());
                            }
                        });

                        runner.test("when root has grandchild files and folders", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Root root = getRoot();
                                root.createFile("1.txt");
                                root.createFile("2.txt");
                                root.createFile("A/3.csv");
                                root.createFile("B/C/4.xml");
                                root.createFile("A/5.png");

                                final Iterable<File> expectedEntries =
                                    Array.fromValues(new File[]
                                    {
                                        root.getFile("1.txt"),
                                        root.getFile("2.txt"),
                                        root.getFile("A/3.csv"),
                                        root.getFile("A/5.png"),
                                        root.getFile("B/C/4.xml")
                                    });
                                final Iterable<File> actualEntries = root.getFilesRecursively();
                                test.assertEqual(expectedEntries, actualEntries);
                            }
                        });
                    }
                });

                runner.testGroup("getFoldersRecursively()", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("when root doesn't exist", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Root root = getRoot("C:/");
                                test.assertNull(root.getFoldersRecursively());
                            }
                        });

                        runner.test("when root is empty", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Root root = getRoot();
                                test.assertEqual(new Array<FileSystemEntry>(0), root.getFoldersRecursively());
                            }
                        });

                        runner.test("when root has files", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Root root = getRoot();
                                root.createFile("1.txt");
                                root.createFile("2.txt");
                                test.assertEqual(
                                    new Array<Folder>(0),
                                    root.getFoldersRecursively());
                            }
                        });

                        runner.test("when root has folders", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Root root = getRoot();
                                root.createFolder("1.txt");
                                root.createFolder("2.txt");
                                test.assertEqual(
                                    Array.fromValues(new Folder[]
                                    {
                                        root.getFolder("1.txt"),
                                        root.getFolder("2.txt")
                                    }),
                                    root.getFoldersRecursively());
                            }
                        });

                        runner.test("when root has grandchild files and folders", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Root root = getRoot();
                                root.createFile("1.txt");
                                root.createFile("2.txt");
                                root.createFile("A/3.csv");
                                root.createFile("B/C/4.xml");
                                root.createFile("A/5.png");

                                final Iterable<Folder> expectedEntries =
                                    Array.fromValues(new Folder[]
                                    {
                                        root.getFolder("A"),
                                        root.getFolder("B"),
                                        root.getFolder("B/C")
                                    });
                                final Iterable<Folder> actualEntries = root.getFoldersRecursively();
                                test.assertEqual(expectedEntries, actualEntries);
                            }
                        });
                    }
                });
            }
        });
    }

    private static InMemoryFileSystem getFileSystem()
    {
        final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
        fileSystem.createRoot("/");
        return fileSystem;
    }

    private static Root getRoot()
    {
        return getRoot("/");
    }

    private static Root getRoot(String rootPath)
    {
        final InMemoryFileSystem fileSystem = getFileSystem();
        return getRoot(fileSystem, rootPath);
    }

    private static Root getRoot(FileSystem fileSystem)
    {
        return getRoot(fileSystem, "/");
    }

    private static Root getRoot(FileSystem fileSystem, String rootPath)
    {
        return fileSystem.getRoot(rootPath);
    }
}
