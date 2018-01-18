package qub;

public class FolderTests
{
    public static void test(final TestRunner runner)
    {
        runner.testGroup("Folder", new Action0()
        {
            @Override
            public void run()
            {
                runner.test("exists()", new Action1<Test>()
                {
                    @Override
                    public void run(Test test)
                    {
                        final Folder folder = getFolder();
                        test.assertFalse(folder.exists());

                        folder.create();
                        test.assertTrue(folder.exists());
                    }
                });
                
                runner.testGroup("delete()", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("when folder doesn't exist", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Folder folder = getFolder();
                                test.assertFalse(folder.delete());
                                test.assertFalse(folder.exists());
                            }
                        });
                        
                        runner.test("when folder exists", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Folder folder = getFolder();
                                folder.create();

                                test.assertTrue(folder.delete());
                                test.assertFalse(folder.exists());
                            }
                        });
                    }
                });
                
                runner.test("create()", new Action1<Test>()
                {
                    @Override
                    public void run(Test test)
                    {
                        final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
                        fileSystem.createRoot("/");

                        final Folder folder = fileSystem.getFolder("/test/folder");
                        test.assertFalse(folder.exists());

                        test.assertTrue(folder.create());
                        test.assertTrue(folder.exists());

                        test.assertFalse(folder.create());
                        test.assertTrue(folder.exists());
                    }
                });
                
                runner.testGroup("createFolder(String)", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("with null path", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
                                fileSystem.createRoot("/");

                                final Folder folder = fileSystem.getFolder("/test/folder");
                                test.assertFalse(folder.createFolder((String)null));
                                test.assertFalse(folder.exists());
                            }
                        });
                        
                        runner.test("with empty path", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
                                fileSystem.createRoot("/");

                                final Folder folder = fileSystem.getFolder("/test/folder");
                                test.assertFalse(folder.createFolder(""));
                                test.assertFalse(folder.exists());
                            }
                        });
                        
                        runner.test("with non-existing relative path", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
                                fileSystem.createRoot("/");

                                final Folder folder = fileSystem.getFolder("/test/folder");
                                test.assertTrue(folder.createFolder("thing"));
                                test.assertTrue(folder.exists());
                                test.assertTrue(folder.getFolder("thing").exists());
                            }
                        });
                    }
                });
                
                runner.testGroup("createFolder(Path)", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("with null path", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
                                fileSystem.createRoot("/");

                                final Folder folder = fileSystem.getFolder("/test/folder");
                                test.assertFalse(folder.createFolder((Path)null));
                                test.assertFalse(folder.exists());
                            }
                        });
                        
                        runner.test("with empty path", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
                                fileSystem.createRoot("/");

                                final Folder folder = fileSystem.getFolder("/test/folder");
                                test.assertFalse(folder.createFolder(Path.parse("")));
                                test.assertFalse(folder.exists());
                            }
                        });
                        
                        runner.test("with non-existing relative path", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
                                fileSystem.createRoot("/");

                                final Folder folder = fileSystem.getFolder("/test/folder");
                                test.assertTrue(folder.createFolder(Path.parse("place")));
                                test.assertTrue(folder.exists());
                                test.assertTrue(folder.getFolder("place").exists());
                            }
                        });
                    }
                });
                
                runner.testGroup("createFolder(Path,Value<Folder>)", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("with relative path and output folder", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
                                fileSystem.createRoot("/");

                                final Folder folder = fileSystem.getFolder("/test/folder");
                                final Value<Folder> childFolder = new Value<>();
                                test.assertTrue(folder.createFolder(Path.parse("place"), childFolder));
                                test.assertTrue(folder.exists());
                                test.assertNotNull(childFolder.get());
                                test.assertTrue(childFolder.get().exists());
                            }
                        });
                    }
                });
                
                runner.testGroup("createFile(String)", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("with null path", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
                                fileSystem.createRoot("/");

                                final Folder folder = fileSystem.getFolder("/test/folder");
                                test.assertFalse(folder.createFile((String)null));
                                test.assertFalse(folder.exists());
                            }
                        });
                        
                        runner.test("with empty path", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
                                fileSystem.createRoot("/");

                                final Folder folder = fileSystem.getFolder("/test/folder");
                                test.assertFalse(folder.createFile(""));
                                test.assertFalse(folder.exists());
                            }
                        });
                        
                        runner.test("with non-existing relative path", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
                                fileSystem.createRoot("/");

                                final Folder folder = fileSystem.getFolder("/test/folder");
                                test.assertTrue(folder.createFile("file.xml"));
                                test.assertTrue(folder.exists());
                                test.assertTrue(folder.getFile("file.xml").exists());
                            }
                        });
                        
                        runner.test("with non-existing relative path in subfolder with backslash separator", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
                                fileSystem.createRoot("/");

                                final Folder folder = fileSystem.getFolder("/test/folder");
                                test.assertTrue(folder.createFile("subfolder\\file.xml"));
                                test.assertTrue(folder.exists());
                                test.assertFalse(folder.getFiles().any());
                                test.assertTrue(folder.getFolder("subfolder").exists());
                                test.assertTrue(folder.getFolder("subfolder").getFile("file.xml").exists());
                            }
                        });
                        
                        runner.test("with non-existing relative path in subfolder with forward slash separator", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
                                fileSystem.createRoot("/");

                                final Folder folder = fileSystem.getFolder("/test/folder");
                                test.assertTrue(folder.createFile("subfolder/file.xml"));
                                test.assertTrue(folder.exists());
                                test.assertFalse(folder.getFiles().any());
                                test.assertTrue(folder.getFolder("subfolder").exists());
                                test.assertTrue(folder.getFolder("subfolder").getFile("file.xml").exists());
                            }
                        });
                    }
                });
                
                runner.testGroup("createFile(String,Value<File>)", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("with null path and null value", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
                                fileSystem.createRoot("/");

                                final Folder folder = fileSystem.getFolder("/test/folder");
                                test.assertFalse(folder.createFile((String)null, null));
                                test.assertFalse(folder.exists());
                            }
                        });
                        
                        runner.test("with empty path and null value", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
                                fileSystem.createRoot("/");

                                final Folder folder = fileSystem.getFolder("/test/folder");
                                test.assertFalse(folder.createFile("", null));
                                test.assertFalse(folder.exists());
                            }
                        });
                        
                        runner.test("with non-existing relative path and null value", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
                                fileSystem.createRoot("/");

                                final Folder folder = fileSystem.getFolder("/test/folder");
                                test.assertTrue(folder.createFile("file.xml", null));
                                test.assertTrue(folder.exists());
                            }
                        });
                        
                        runner.test("with subfolder relative path with backslash separator and null value", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
                                fileSystem.createRoot("/");

                                final Folder folder = fileSystem.getFolder("/test/folder");
                                test.assertTrue(folder.createFile("subfolder\\file.xml", null));
                                test.assertTrue(folder.exists());
                                test.assertFalse(folder.getFiles().any());
                            }
                        });
                        
                        runner.test("with subfolder relative path with forward slash separator and null value", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
                                fileSystem.createRoot("/");

                                final Folder folder = fileSystem.getFolder("/test/folder");
                                test.assertTrue(folder.createFile("subfolder/file.xml", null));
                                test.assertTrue(folder.exists());
                                test.assertFalse(folder.getFiles().any());
                            }
                        });
                        
                        runner.test("with null path and non-null value", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
                                fileSystem.createRoot("/");

                                final Folder folder = fileSystem.getFolder("/test/folder");
                                final Value<File> file = new Value<>();
                                test.assertFalse(folder.createFile((String)null, file));
                                test.assertFalse(folder.exists());
                                test.assertFalse(file.hasValue());
                            }
                        });
                        
                        runner.test("with empty path and non-null value", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
                                fileSystem.createRoot("/");

                                final Folder folder = fileSystem.getFolder("/test/folder");
                                final Value<File> file = new Value<>();
                                test.assertFalse(folder.createFile("", file));
                                test.assertFalse(folder.exists());
                                test.assertFalse(file.hasValue());
                            }
                        });
                        
                        runner.test("with non-existing relative path and non-null value", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
                                fileSystem.createRoot("/");

                                final Folder folder = fileSystem.getFolder("/test/folder");
                                final Value<File> file = new Value<>();
                                test.assertTrue(folder.createFile("file.xml", file));
                                test.assertTrue(folder.exists());
                                test.assertNotNull(file.get());
                                test.assertEqual(Path.parse("/test/folder/file.xml"), file.get().getPath());
                                test.assertTrue(file.get().exists());
                            }
                        });

                        runner.test("with non-existing subfolder relative path with backslash separator and non-null value", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
                                fileSystem.createRoot("/");

                                final Folder folder = fileSystem.getFolder("/test/folder");
                                final Value<File> file = new Value<>();
                                test.assertTrue(folder.createFile("subfolder\\file.xml", file));
                                test.assertTrue(folder.exists());
                                test.assertFalse(folder.getFiles().any());
                                test.assertNotNull(file.get());
                                test.assertEqual(Path.parse("/test/folder/subfolder/file.xml"), file.get().getPath());
                                test.assertTrue(file.get().exists());
                            }
                        });

                        runner.test("with non-existing subfolder relative path with forward slash separator and non-null value", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
                                fileSystem.createRoot("/");

                                final Folder folder = fileSystem.getFolder("/test/folder");
                                final Value<File> file = new Value<>();
                                test.assertTrue(folder.createFile("subfolder/file.xml", file));
                                test.assertTrue(folder.exists());
                                test.assertFalse(folder.getFiles().any());
                                test.assertNotNull(file.get());
                                test.assertEqual(Path.parse("/test/folder/subfolder/file.xml"), file.get().getPath());
                                test.assertTrue(file.get().exists());
                            }
                        });
                    }
                });

                runner.testGroup("createFile(Path)", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("with null path", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
                                fileSystem.createRoot("/");

                                final Folder folder = fileSystem.getFolder("/test/folder");
                                test.assertFalse(folder.createFile((Path)null));
                                test.assertFalse(folder.exists());
                            }
                        });

                        runner.test("with empty path", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
                                fileSystem.createRoot("/");

                                final Folder folder = fileSystem.getFolder("/test/folder");
                                test.assertFalse(folder.createFile(Path.parse("")));
                                test.assertFalse(folder.exists());
                            }
                        });
                        
                        runner.test("with non-existing relative path", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
                                fileSystem.createRoot("/");

                                final Folder folder = fileSystem.getFolder("/test/folder");
                                test.assertTrue(folder.createFile(Path.parse("file.xml")));
                                test.assertTrue(folder.exists());
                            }
                        });
                        
                        runner.test("with non-existing subfolder relative path with backslash separator", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
                                fileSystem.createRoot("/");

                                final Folder folder = fileSystem.getFolder("/test/folder");
                                test.assertTrue(folder.createFile(Path.parse("subfolder\\file.xml")));
                                test.assertTrue(folder.exists());
                                test.assertFalse(folder.getFiles().any());
                            }
                        });
                        
                        runner.test("with non-existing subfolder relative path with forward slash separator", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
                                fileSystem.createRoot("/");

                                final Folder folder = fileSystem.getFolder("/test/folder");
                                test.assertTrue(folder.createFile(Path.parse("subfolder/file.xml")));
                                test.assertTrue(folder.exists());
                                test.assertFalse(folder.getFiles().any());
                            }
                        });
                    }
                });
                
                runner.testGroup("createFile(Path,Value<File>)", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("with null path and null value", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Folder folder = getFolder();
                                test.assertFalse(folder.createFile((Path)null, null));
                                test.assertFalse(folder.exists());
                            }
                        });
                        
                        runner.test("with empty path and null value", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Folder folder = getFolder();
                                test.assertFalse(folder.createFile(Path.parse(""), null));
                                test.assertFalse(folder.exists());
                            }
                        });
                        
                        runner.test("with non-existing relative path and null value", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Folder folder = getFolder();
                                test.assertTrue(folder.createFile(Path.parse("file.xml"), null));
                                test.assertTrue(folder.exists());
                            }
                        });
                        
                        runner.test("with non-existing subfolder relative path with backslash separator and null value", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Folder folder = getFolder();
                                test.assertTrue(folder.createFile(Path.parse("subfolder\\file.xml"), null));
                                test.assertTrue(folder.exists());
                                test.assertFalse(folder.getFiles().any());
                            }
                        });
                        
                        runner.test("with non-existing subfolder relative path with forward slash separator and null value", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Folder folder = getFolder();
                                test.assertTrue(folder.createFile(Path.parse("subfolder/file.xml"), null));
                                test.assertTrue(folder.exists());
                                test.assertFalse(folder.getFiles().any());
                            }
                        });
                        
                        runner.test("with null path and non-null value", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Folder folder = getFolder();
                                final Value<File> file = new Value<>();
                                test.assertFalse(folder.createFile((Path)null, file));
                                test.assertFalse(folder.exists());
                                test.assertFalse(file.hasValue());
                            }
                        });
                        
                        runner.test("with empty path and non-null value", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Folder folder = getFolder();
                                final Value<File> file = new Value<>();
                                test.assertFalse(folder.createFile(Path.parse(""), file));
                                test.assertFalse(folder.exists());
                                test.assertFalse(file.hasValue());
                            }
                        });
                        
                        runner.test("with non-existing relative path and non-null value", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Folder folder = getFolder();
                                final Value<File> file = new Value<>();
                                test.assertTrue(folder.createFile(Path.parse("file.xml"), file));
                                test.assertTrue(folder.exists());
                                test.assertNotNull(file.get());
                                test.assertEqual(Path.parse("/test/folder/file.xml"), file.get().getPath());
                                test.assertTrue(file.get().exists());
                            }
                        });
                        
                        runner.test("with non-existing subfolder relative path with backslash separator and non-null value", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Folder folder = getFolder();
                                final Value<File> file = new Value<>();
                                test.assertTrue(folder.createFile(Path.parse("subfolder\\file.xml"), file));
                                test.assertTrue(folder.exists());
                                test.assertFalse(folder.getFiles().any());
                                test.assertNotNull(file.get());
                                test.assertEqual(Path.parse("/test/folder/subfolder/file.xml"), file.get().getPath());
                                test.assertTrue(file.get().exists());
                            }
                        });
                        
                        runner.test("with non-existing subfolder relative path with forward slash separator and non-null value", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Folder folder = getFolder();
                                final Value<File> file = new Value<>();
                                test.assertTrue(folder.createFile(Path.parse("subfolder/file.xml"), file));
                                test.assertTrue(folder.exists());
                                test.assertFalse(folder.getFiles().any());
                                test.assertNotNull(file.get());
                                test.assertEqual(Path.parse("/test/folder/subfolder/file.xml"), file.get().getPath());
                                test.assertTrue(file.get().exists());
                            }
                        });
                    }
                });
                
                runner.test("getFolders()", new Action1<Test>()
                {
                    @Override
                    public void run(Test test)
                    {
                        final Folder folder = getFolder();
                        test.assertFalse(folder.exists());
                        test.assertNull(folder.getFolders());

                        folder.create();
                        test.assertEqual(new String[0], Array.toStringArray(folder.getFolders().map(new Function1<Folder, String>()
                        {
                            @Override
                            public String run(Folder childFolder)
                            {
                                return childFolder.getPath().toString();
                            }
                        })));

                        final Value<Folder> childFolder = new Value<>();
                        test.assertTrue(folder.createFolder("childFolder1", childFolder));
                        test.assertEqual(new String[] { "/test/folder/childFolder1" }, Array.toStringArray(folder.getFolders().map(new Function1<Folder, String>()
                        {
                            @Override
                            public String run(Folder childFolder)
                            {
                                return childFolder.getPath().toString();
                            }
                        })));

                        test.assertTrue(childFolder.get().createFolder("grandchildFolder1"));
                        test.assertEqual(new String[] { "/test/folder/childFolder1" }, Array.toStringArray(folder.getFolders().map(new Function1<Folder, String>()
                        {
                            @Override
                            public String run(Folder childFolder)
                            {
                                return childFolder.getPath().toString();
                            }
                        })));
                    }
                });

                runner.testGroup("getFiles()", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("when folder doesn't exist", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Folder folder = getFolder();
                                test.assertFalse(folder.exists());
                                test.assertNull(folder.getFiles());
                            }
                        });

                        runner.test("when folder exists but is empty", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Folder folder = getFolder();
                                test.assertTrue(folder.create());
                                test.assertTrue(folder.exists());
                                final Iterable<File> files = folder.getFiles();
                                test.assertEqual(new String[0], Array.toStringArray(files.map(new Function1<File, String>()
                                {
                                    @Override
                                    public String run(File childFile)
                                    {
                                        return childFile.getPath().toString();
                                    }
                                })));
                            }
                        });

                        runner.test("when folder exists and has one file", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Folder folder = getFolder();
                                test.assertTrue(folder.create());
                                test.assertTrue(folder.exists());
                                test.assertTrue(folder.createFile("data.txt"));
                                final Iterable<File> files = folder.getFiles();
                                test.assertEqual(new String[] { "/test/folder/data.txt" }, Array.toStringArray(files.map(new Function1<File, String>()
                                {
                                    @Override
                                    public String run(File childFile)
                                    {
                                        return childFile.getPath().toString();
                                    }
                                })));
                            }
                        });

                        runner.test("when folder exists and has one grandchild file", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Folder folder = getFolder();
                                test.assertTrue(folder.create());
                                test.assertTrue(folder.exists());
                                test.assertTrue(folder.createFile("subfolder/data.txt"));
                                final Iterable<File> files = folder.getFiles();
                                test.assertEqual(new String[0], Array.toStringArray(files.map(new Function1<File, String>()
                                {
                                    @Override
                                    public String run(File childFile)
                                    {
                                        return childFile.getPath().toString();
                                    }
                                })));
                            }
                        });
                    }
                });

                runner.testGroup("getFilesAndFolders()", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("when folder doesn't exist", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Folder folder = getFolder();
                                test.assertFalse(folder.exists());

                                final Iterable<FileSystemEntry> filesAndFolders = folder.getFilesAndFolders();
                                test.assertNull(filesAndFolders);
                            }
                        });

                        runner.test("when folder exists but is empty", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Folder folder = getFolder();
                                test.assertTrue(folder.create());

                                final Iterable<FileSystemEntry> filesAndFolders = folder.getFilesAndFolders();
                                test.assertNotNull(filesAndFolders);
                                test.assertFalse(filesAndFolders.any());
                            }
                        });

                        runner.test("when folder exists and has one child folder", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Folder folder = getFolder();
                                test.assertTrue(folder.create());
                                test.assertTrue(folder.createFolder("subfolder"));

                                final Iterable<FileSystemEntry> filesAndFolders = folder.getFilesAndFolders();
                                test.assertNotNull(filesAndFolders);
                                test.assertTrue(filesAndFolders.any());
                                test.assertEqual(new String[] { "/test/folder/subfolder" }, Array.toStringArray(filesAndFolders.map(new Function1<FileSystemEntry, String>()
                                {
                                    @Override
                                    public String run(FileSystemEntry childEntry)
                                    {
                                        return childEntry.getPath().toString();
                                    }
                                })));
                            }
                        });

                        runner.test("when folder exists and has one child file", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Folder folder = getFolder();
                                test.assertTrue(folder.create());
                                test.assertTrue(folder.createFile("file.java"));

                                final Iterable<FileSystemEntry> filesAndFolders = folder.getFilesAndFolders();
                                test.assertNotNull(filesAndFolders);
                                test.assertTrue(filesAndFolders.any());
                                test.assertEqual(new String[] { "/test/folder/file.java" }, Array.toStringArray(filesAndFolders.map(new Function1<FileSystemEntry, String>()
                                {
                                    @Override
                                    public String run(FileSystemEntry childEntry)
                                    {
                                        return childEntry.getPath().toString();
                                    }
                                })));
                            }
                        });

                        runner.test("when folder exists and has one child file and one child folder", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Folder folder = getFolder();
                                test.assertTrue(folder.create());
                                test.assertTrue(folder.createFile("file.java"));
                                test.assertTrue(folder.createFolder("childfolder"));

                                final Iterable<FileSystemEntry> filesAndFolders = folder.getFilesAndFolders();
                                test.assertNotNull(filesAndFolders);
                                test.assertTrue(filesAndFolders.any());
                                test.assertEqual(new String[] { "/test/folder/childfolder", "/test/folder/file.java" }, Array.toStringArray(filesAndFolders.map(new Function1<FileSystemEntry, String>()
                                {
                                    @Override
                                    public String run(FileSystemEntry childEntry)
                                    {
                                        return childEntry.getPath().toString();
                                    }
                                })));
                            }
                        });
                    }
                });

                runner.testGroup("fileExists(String)", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("when file doesn't exist", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Folder folder = getFolder();
                                test.assertFalse(folder.fileExists("test.txt"));
                            }
                        });

                        runner.test("when file exists", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Folder folder = getFolder();
                                folder.createFile("test.txt");
                                test.assertTrue(folder.fileExists("test.txt"));
                            }
                        });
                    }
                });

                runner.testGroup("folderExists(String)", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("when folder doesn't exist", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Folder folder = getFolder();
                                test.assertFalse(folder.folderExists("test"));
                            }
                        });

                        runner.test("when folder does exist", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Folder folder = getFolder();
                                folder.createFolder("test");
                                test.assertTrue(folder.folderExists("test"));
                            }
                        });
                    }
                });
            }
        });
    }

    private static Folder getFolder()
    {
        final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
        fileSystem.createRoot("/");

        return fileSystem.getFolder("/test/folder");
    }
}
