package qub;

public class FileSystemTests
{
    public static void test(final TestRunner runner, final Function0<FileSystem> creator)
    {
        runner.testGroup("FileSystem", new Action0()
        {
            @Override
            public void run()
            {
                runner.testGroup("rootExists()", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("with null String path", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                test.assertFalse(fileSystem.rootExists((String)null));
                            }
                        });
                        
                        runner.test("with empty String path", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                test.assertFalse(fileSystem.rootExists(""));
                            }
                        });
                        
                        runner.test("with non-existing String path", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                test.assertFalse(fileSystem.rootExists("notme:/"));
                            }
                        });
                    }
                });
                
                runner.testGroup("rootExistsAsync(String)", new Action0()
                {
                    @Override
                    public void run()
                    {
                        final Action2<String,Boolean> rootExistsAsyncTest = new Action2<String, Boolean>()
                        {
                            @Override
                            public void run(final String rootPath, final Boolean expectedToExist)
                            {
                                runner.test("with " + (rootPath == null ? null : "\"" + rootPath + "\""), new Action1<Test>()
                                {
                                    @Override
                                    public void run(final Test test)
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
                                                            test.assertEqual(expectedToExist, rootExists);
                                                        }
                                                    });
                                            }
                                        });
                                    }
                                });
                            }
                        };
                        
                        rootExistsAsyncTest.run(null, false);
                        rootExistsAsyncTest.run("", false);
                        rootExistsAsyncTest.run("notme:\\", false);
                    }
                });
                
                runner.testGroup("rootExistsAsync(Path)", new Action0()
                {
                    @Override
                    public void run()
                    {
                        final Action2<String,Boolean> rootExistsAsyncTest = new Action2<String, Boolean>()
                        {
                            @Override
                            public void run(final String rootPath, final Boolean expectedToExist)
                            {
                                runner.test("with " + (rootPath == null ? null : "\"" + rootPath + "\""), new Action1<Test>()
                                {
                                    @Override
                                    public void run(final Test test)
                                    {
                                        asyncTest(new Action1<FileSystem>()
                                        {
                                            @Override
                                            public void run(FileSystem fileSystem)
                                            {
                                                fileSystem.rootExistsAsync(Path.parse(rootPath))
                                                    .then(new Action1<Boolean>()
                                                    {
                                                        @Override
                                                        public void run(Boolean rootExists)
                                                        {
                                                            test.assertEqual(expectedToExist, rootExists);
                                                        }
                                                    });
                                            }
                                        });
                                    }
                                });
                            }
                        };
                        
                        rootExistsAsyncTest.run(null, false);
                        rootExistsAsyncTest.run("", false);
                        rootExistsAsyncTest.run("notme:\\", false);
                    }
                });
                
                runner.test("getRoot()", new Action1<Test>()
                {
                    @Override
                    public void run(Test test)
                    {
                        final FileSystem fileSystem = getFileSystem(creator);
                        final Root root1 = fileSystem.getRoot("/daffy/");
                        test.assertFalse(root1.exists());
                        test.assertEqual("/daffy/", root1.toString());
                    }
                });
                
                runner.test("getRoots()", new Action1<Test>()
                {
                    @Override
                    public void run(Test test)
                    {
                        final FileSystem fileSystem = getFileSystem(creator);
                        final Iterable<Root> roots = fileSystem.getRoots();
                        test.assertNotNull(roots);
                        test.assertTrue(1 <= roots.getCount());
                    }
                });
                
                runner.test("getRootsAsync()", new Action1<Test>()
                {
                    @Override
                    public void run(final Test test)
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
                                            test.assertNotNull(roots);
                                            test.assertTrue(1 <= roots.getCount());
                                        }
                                    });
                            }
                        });
                    }
                });
                
                runner.testGroup("getFilesAndFolders(String)", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("with null String path", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                final Iterable<FileSystemEntry> entries = fileSystem.getFilesAndFolders((String)null);
                                test.assertNull(entries);
                            }
                        });
                        
                        runner.test("with empty String path", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                final Iterable<FileSystemEntry> entries = fileSystem.getFilesAndFolders("");
                                test.assertNull(entries);
                            }
                        });
                        
                        runner.test("with null Path", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                final Iterable<FileSystemEntry> entries = fileSystem.getFilesAndFolders((Path)null);
                                test.assertNull(entries);
                            }
                        });
                        
                        runner.test("with non-existing path", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                final Iterable<FileSystemEntry> entries = fileSystem.getFilesAndFolders("/i/dont/exist/");
                                test.assertNull(entries);
                            }
                        });
                        
                        runner.test("with existing path with no contents", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                fileSystem.createFolder("/folderA");
                                test.assertFalse(fileSystem.getFilesAndFolders("/folderA").any());
                            }
                        });
                        
                        runner.test("with existing path with contents", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                fileSystem.createFolder("/folderA");
                                fileSystem.createFolder("/folderA/folderB");
                                fileSystem.createFile("/file1.txt");
                                fileSystem.createFile("/folderA/file2.csv");

                                final Iterable<FileSystemEntry> entries = fileSystem.getFilesAndFolders("/");
                                test.assertTrue(entries.any());
                                test.assertEqual(2, entries.getCount());
                                final String[] entryPathStrings = Array.toStringArray(entries.map(new Function1<FileSystemEntry, String>()
                                {
                                    @Override
                                    public String run(FileSystemEntry arg1)
                                    {
                                        return arg1.getPath().toString();
                                    }
                                }));
                                test.assertEqual(new String[] { "/folderA", "/file1.txt" }, entryPathStrings);
                            }
                        });
                    }
                });

                runner.testGroup("getFilesAndFoldersAsync(String)", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("with null path", new Action1<Test>()
                        {
                            @Override
                            public void run(final Test test)
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
                                                    test.assertNull(entries);
                                                }
                                            });
                                    }
                                });
                            }
                        });

                        runner.test("with empty path", new Action1<Test>()
                        {
                            @Override
                            public void run(final Test test)
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
                                                    test.assertNull(entries);
                                                }
                                            });
                                    }
                                });
                            }
                        });

                        runner.test("with non-existing path", new Action1<Test>()
                        {
                            @Override
                            public void run(final Test test)
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
                                                    test.assertNull(entries);
                                                }
                                            });
                                    }
                                });
                            }
                        });

                        runner.test("with existing path with no content", new Action1<Test>()
                        {
                            @Override
                            public void run(final Test test)
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
                                                    test.assertFalse(entries.any());
                                                }
                                            });
                                    }
                                });
                            }
                        });

                        runner.test("with existing path with content", new Action1<Test>()
                        {
                            @Override
                            public void run(final Test test)
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
                                                    test.assertTrue(entries.any());
                                                    test.assertEqual(2, entries.getCount());
                                                    final String[] entryPathStrings = Array.toStringArray(entries.map(new Function1<FileSystemEntry, String>()
                                                    {
                                                        @Override
                                                        public String run(FileSystemEntry entry)
                                                        {
                                                            return entry.getPath().toString();
                                                        }
                                                    }));
                                                    test.assertEqual(new String[] { "/folderA", "/file1.txt" }, entryPathStrings);
                                                }
                                            });
                                    }
                                });
                            }
                        });
                    }
                });

                runner.testGroup("getFilesAndFoldersAsync(Path)", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("with null path", new Action1<Test>()
                        {
                            @Override
                            public void run(final Test test)
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
                                                    test.assertNull(entries);
                                                }
                                            });
                                    }
                                });
                            }
                        });

                        runner.test("with empty path", new Action1<Test>()
                        {
                            @Override
                            public void run(final Test test)
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
                                                    test.assertNull(entries);
                                                }
                                            });
                                    }
                                });
                            }
                        });

                        runner.test("with non-existing path", new Action1<Test>()
                        {
                            @Override
                            public void run(final Test test)
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
                                                    test.assertNull(entries);
                                                }
                                            });
                                    }
                                });
                            }
                        });

                        runner.test("with existing path with no contents", new Action1<Test>()
                        {
                            @Override
                            public void run(final Test test)
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
                                                    test.assertFalse(entries.any());
                                                }
                                            });
                                    }
                                });
                            }
                        });

                        runner.test("with existing path with contents", new Action1<Test>()
                        {
                            @Override
                            public void run(final Test test)
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
                                                    test.assertTrue(entries.any());
                                                    test.assertEqual(2, entries.getCount());
                                                    final String[] entryPathStrings = Array.toStringArray(entries.map(new Function1<FileSystemEntry, String>()
                                                    {
                                                        @Override
                                                        public String run(FileSystemEntry entry)
                                                        {
                                                            return entry.getPath().toString();
                                                        }
                                                    }));
                                                    test.assertEqual(new String[] { "/folderA", "/file1.txt" }, entryPathStrings);
                                                }
                                            });
                                    }
                                });
                            }
                        });
                    }
                });

                runner.testGroup("getFolders(String)", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("with null path", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                final Iterable<Folder> folders = fileSystem.getFolders((String)null);
                                test.assertNull(folders);
                            }
                        });

                        runner.test("with empty path", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                final Iterable<Folder> folders = fileSystem.getFolders("");
                                test.assertNull(folders);
                            }
                        });
                    }
                });

                runner.testGroup("getFoldersRecursively(String)", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("with null path", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                test.assertNull(fileSystem.getFoldersRecursively((String)null));
                            }
                        });

                        runner.test("with empty path", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                test.assertNull(fileSystem.getFoldersRecursively(""));
                            }
                        });

                        runner.test("with relative path", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                test.assertNull(fileSystem.getFoldersRecursively("test/folder"));
                            }
                        });

                        runner.test("with rooted path when root doesn't exist", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                test.assertNull(fileSystem.getFoldersRecursively("F:/test/folder"));
                            }
                        });

                        runner.test("with rooted path when parent folder doesn't exist", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                test.assertNull(fileSystem.getFoldersRecursively("/test/folder"));
                            }
                        });

                        runner.test("with rooted path when folder doesn't exist", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                fileSystem.createFolder("/test/");
                                test.assertNull(fileSystem.getFoldersRecursively("/test/folder"));
                            }
                        });

                        runner.test("with rooted path when folder is empty", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                fileSystem.createFolder("/test/folder");
                                test.assertEqual(new Array<Folder>(0), fileSystem.getFoldersRecursively("/test/folder"));
                            }
                        });

                        runner.test("with rooted path when folder has files", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                fileSystem.createFolder("/test/folder");
                                fileSystem.createFile("/test/folder/1.txt");
                                fileSystem.createFile("/test/folder/2.txt");
                                test.assertEqual(
                                    new Array<Folder>(0),
                                    fileSystem.getFoldersRecursively("/test/folder"));
                            }
                        });

                        runner.test("with rooted path when folder has folders", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                fileSystem.createFolder("/test/folder");
                                fileSystem.createFolder("/test/folder/1.txt");
                                fileSystem.createFolder("/test/folder/2.txt");
                                test.assertEqual(
                                    Array.fromValues(
                                        fileSystem.getFolder("/test/folder/1.txt"),
                                        fileSystem.getFolder("/test/folder/2.txt")),
                                    fileSystem.getFoldersRecursively("/test/folder"));
                            }
                        });

                        runner.test("with rooted path when folder has grandchild folders and files", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                fileSystem.createFile("/test/folder/1.txt");
                                fileSystem.createFile("/test/folder/2.txt");
                                fileSystem.createFile("/test/folder/A/3.csv");
                                fileSystem.createFile("/test/folder/B/C/4.xml");
                                fileSystem.createFile("/test/folder/A/5.png");

                                final Iterable<Folder> expectedEntries =
                                    Array.fromValues(
                                        fileSystem.getFolder("/test/folder/A"),
                                        fileSystem.getFolder("/test/folder/B"),
                                        fileSystem.getFolder("/test/folder/B/C"));
                                final Iterable<Folder> actualEntries = fileSystem.getFoldersRecursively("/test/folder");
                                test.assertEqual(expectedEntries, actualEntries);
                            }
                        });
                    }
                });

                runner.testGroup("getFoldersAsync(String)", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("with null path", new Action1<Test>()
                        {
                            @Override
                            public void run(final Test test)
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
                                                    test.assertNull(folders);
                                                }
                                            });
                                    }
                                });
                            }
                        });

                        runner.test("with empty path", new Action1<Test>()
                        {
                            @Override
                            public void run(final Test test)
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
                                                    test.assertNull(folders);
                                                }
                                            });
                                    }
                                });
                            }
                        });
                    }
                });

                runner.testGroup("getFoldersAsync(Path)", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("with null path", new Action1<Test>()
                        {
                            @Override
                            public void run(final Test test)
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
                                                    test.assertNull(folders);
                                                }
                                            });
                                    }
                                });
                            }
                        });

                        runner.test("with empty path", new Action1<Test>()
                        {
                            @Override
                            public void run(final Test test)
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
                                                    test.assertNull(folders);
                                                }
                                            });
                                    }
                                });
                            }
                        });
                    }
                });

                runner.testGroup("getFilesAsync(String)", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("with null path", new Action1<Test>()
                        {
                            @Override
                            public void run(final Test test)
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
                                                    test.assertNull(files);
                                                }
                                            });
                                    }
                                });
                            }
                        });

                        runner.test("with empty path", new Action1<Test>()
                        {
                            @Override
                            public void run(final Test test)
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
                                                    test.assertNull(files);
                                                }
                                            });
                                    }
                                });
                            }
                        });
                    }
                });

                runner.testGroup("getFilesAsync(Path)", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("with null path", new Action1<Test>()
                        {
                            @Override
                            public void run(final Test test)
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
                                                    test.assertNull(files);
                                                }
                                            });
                                    }
                                });
                            }
                        });

                        runner.test("with empty path", new Action1<Test>()
                        {
                            @Override
                            public void run(final Test test)
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
                                                    test.assertNull(files);
                                                }
                                            });
                                    }
                                });
                            }
                        });
                    }
                });

                runner.testGroup("getFiles(String)", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("with null path", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                final Iterable<File> files = fileSystem.getFiles((String)null);
                                test.assertNull(files);
                            }
                        });

                        runner.test("with empty path", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                final Iterable<File> files = fileSystem.getFiles("");
                                test.assertNull(files);
                            }
                        });
                    }
                });

                runner.testGroup("getFilesRecursively(String)", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("with null path", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                test.assertNull(fileSystem.getFilesRecursively((String)null));
                            }
                        });

                        runner.test("with empty path", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                test.assertNull(fileSystem.getFilesRecursively(""));
                            }
                        });

                        runner.test("with relative path", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                test.assertNull(fileSystem.getFilesRecursively("test/folder"));
                            }
                        });

                        runner.test("with rooted path when root doesn't exist", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                test.assertNull(fileSystem.getFilesRecursively("F:/test/folder"));
                            }
                        });

                        runner.test("with rooted path when parent folder doesn't exist", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                test.assertNull(fileSystem.getFilesRecursively("/test/folder"));
                            }
                        });

                        runner.test("with rooted path when folder doesn't exist", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                fileSystem.createFolder("/test/");
                                test.assertNull(fileSystem.getFilesRecursively("/test/folder"));
                            }
                        });

                        runner.test("with rooted path when folder is empty", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                fileSystem.createFolder("/test/folder");
                                test.assertEqual(new Array<FileSystemEntry>(0), fileSystem.getFilesRecursively("/test/folder"));
                            }
                        });

                        runner.test("with rooted path when folder has files", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                fileSystem.createFolder("/test/folder");
                                fileSystem.createFile("/test/folder/1.txt");
                                fileSystem.createFile("/test/folder/2.txt");
                                test.assertEqual(
                                    Array.fromValues(
                                        fileSystem.getFile("/test/folder/1.txt"),
                                        fileSystem.getFile("/test/folder/2.txt")),
                                    fileSystem.getFilesRecursively("/test/folder"));
                            }
                        });

                        runner.test("with rooted path when folder has folders", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                fileSystem.createFolder("/test/folder");
                                fileSystem.createFolder("/test/folder/1.txt");
                                fileSystem.createFolder("/test/folder/2.txt");
                                test.assertEqual(
                                    new Array<File>(0),
                                    fileSystem.getFilesRecursively("/test/folder"));
                            }
                        });

                        runner.test("with rooted path when folder has grandchild folders and files", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                fileSystem.createFile("/test/folder/1.txt");
                                fileSystem.createFile("/test/folder/2.txt");
                                fileSystem.createFile("/test/folder/A/3.csv");
                                fileSystem.createFile("/test/folder/B/C/4.xml");
                                fileSystem.createFile("/test/folder/A/5.png");

                                final Iterable<File> expectedEntries =
                                    Array.fromValues(
                                        fileSystem.getFile("/test/folder/1.txt"),
                                        fileSystem.getFile("/test/folder/2.txt"),
                                        fileSystem.getFile("/test/folder/A/3.csv"),
                                        fileSystem.getFile("/test/folder/A/5.png"),
                                        fileSystem.getFile("/test/folder/B/C/4.xml"));
                                final Iterable<File> actualEntries = fileSystem.getFilesRecursively("/test/folder");
                                test.assertEqual(expectedEntries, actualEntries);
                            }
                        });
                    }
                });

                runner.testGroup("getFolder(String)", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("with null path", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                final Folder folder = fileSystem.getFolder((String)null);
                                test.assertNull(folder);
                            }
                        });

                        runner.test("with empty path", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                final Folder folder = fileSystem.getFolder("");
                                test.assertNull(folder);
                            }
                        });

                        runner.test("with relative path", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                final Folder folder = fileSystem.getFolder("a/b/c");
                                test.assertNull(folder);
                            }
                        });

                        runner.test("with forward-slash", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                final Folder folder = fileSystem.getFolder("/");
                                test.assertNotNull(folder);
                                test.assertEqual("/", folder.toString());
                            }
                        });

                        runner.test("with backslash", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                final Folder folder = fileSystem.getFolder("\\");
                                test.assertNotNull(folder);
                                test.assertEqual("\\", folder.toString());
                            }
                        });

                        runner.test("with Windows root", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                final Folder folder = fileSystem.getFolder("Z:\\");
                                test.assertNotNull(folder);
                                test.assertEqual("Z:\\", folder.toString());
                            }
                        });

                        runner.test("with root and folder", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                final Folder folder = fileSystem.getFolder("/a/b");
                                test.assertNotNull(folder);
                                test.assertEqual("/a/b", folder.toString());
                            }
                        });
                    }
                });

                runner.testGroup("folderExists(String)", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("with root path", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                final Root folder = fileSystem.getRoots().first();
                                test.assertTrue(fileSystem.folderExists(folder.getPath().toString()));
                            }
                        });

                        runner.test("with existing folder path", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                fileSystem.createFolder("/folderName");
                                test.assertTrue(fileSystem.folderExists("/folderName"));
                            }
                        });
                    }
                });

                runner.testGroup("folderExistsAsync(String)", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("with root path", new Action1<Test>()
                        {
                            @Override
                            public void run(final Test test)
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
                                                    test.assertTrue(folderExists);
                                                }
                                            });
                                    }
                                });
                            }
                        });

                        runner.test("with existing folder path", new Action1<Test>()
                        {
                            @Override
                            public void run(final Test test)
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
                                                    test.assertTrue(folderExists);
                                                }
                                            });
                                    }
                                });
                            }
                        });
                    }
                });

                runner.testGroup("folderExistsAsync(Path)", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("with root path", new Action1<Test>()
                        {
                            @Override
                            public void run(final Test test)
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
                                                    test.assertTrue(folderExists);
                                                }
                                            });
                                    }
                                });
                            }
                        });

                        runner.test("with existing folder path", new Action1<Test>()
                        {
                            @Override
                            public void run(final Test test)
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
                                                    test.assertTrue(folderExists);
                                                }
                                            });
                                    }
                                });
                            }
                        });
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
                                final FileSystem fileSystem = getFileSystem(creator);
                                test.assertFalse(fileSystem.createFolder((String)null));
                            }
                        });

                        runner.test("with empty path", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                test.assertFalse(fileSystem.createFolder(""));
                            }
                        });

                        runner.test("with relative path", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                test.assertFalse(fileSystem.createFolder("folder"));
                            }
                        });

                        runner.test("with rooted path", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                test.assertTrue(fileSystem.createFolder("/folder"));
                                test.assertTrue(fileSystem.folderExists("/folder"));
                            }
                        });
                    }
                });

                runner.testGroup("createFolder(String,Value<Folder>)", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("with null path and null value", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                test.assertFalse(fileSystem.createFolder((String)null, null));
                            }
                        });

                        runner.test("with empty path and null value", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                test.assertFalse(fileSystem.createFolder("", null));
                            }
                        });

                        runner.test("with relative path and null value", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                test.assertFalse(fileSystem.createFolder("folder", null));
                            }
                        });

                        runner.test("with rooted path and null value", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                test.assertTrue(fileSystem.createFolder("/folder", null));
                                test.assertTrue(fileSystem.folderExists("/folder"));
                            }
                        });

                        runner.test("with null path and non-null value", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                final Value<Folder> folder = new Value<>();
                                test.assertFalse(fileSystem.createFolder((String)null, folder));
                                test.assertFalse(folder.hasValue());
                            }
                        });

                        runner.test("with empty path and non-null value", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                final Value<Folder> folder = new Value<>();
                                test.assertFalse(fileSystem.createFolder("", folder));
                                test.assertFalse(folder.hasValue());
                            }
                        });

                        runner.test("with relative path and non-null value", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                final Value<Folder> folder = new Value<>();
                                test.assertFalse(fileSystem.createFolder("folder", folder));
                                test.assertFalse(folder.hasValue());
                            }
                        });

                        runner.test("with rooted path and non-null value", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                final Value<Folder> folder = new Value<>();
                                test.assertTrue(fileSystem.createFolder("/folder", folder));
                                test.assertTrue(fileSystem.folderExists("/folder"));
                                test.assertTrue(folder.hasValue());
                                test.assertNotNull(folder.get());
                                test.assertEqual("/folder", folder.get().getPath().toString());
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
                                final FileSystem fileSystem = getFileSystem(creator);
                                test.assertFalse(fileSystem.createFolder((Path)null));
                            }
                        });
                    }
                });

                runner.testGroup("createFolder(Path,Value<Folder>)", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("with null path and non-null value", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                final Value<Folder> folder = new Value<>();
                                test.assertFalse(fileSystem.createFolder((Path)null, folder));
                                test.assertFalse(folder.hasValue());
                                test.assertNull(folder.get());
                            }
                        });
                    }
                });

                runner.testGroup("createFolderAsync(String)", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("with null path", new Action1<Test>()
                        {
                            @Override
                            public void run(final Test test)
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
                                                    test.assertFalse(folderCreated);
                                                }
                                            });
                                    }
                                });
                            }
                        });

                        runner.test("with empty path", new Action1<Test>()
                        {
                            @Override
                            public void run(final Test test)
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
                                                    test.assertFalse(folderCreated);
                                                }
                                            });
                                    }
                                });
                            }
                        });

                        runner.test("with relative path", new Action1<Test>()
                        {
                            @Override
                            public void run(final Test test)
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
                                                    test.assertFalse(arg1);
                                                }
                                            });
                                    }
                                });
                            }
                        });

                        runner.test("with rooted path", new Action1<Test>()
                        {
                            @Override
                            public void run(final Test test)
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
                                                    test.assertTrue(folderCreated);
                                                    test.assertTrue(fileSystem.folderExists("/folder"));
                                                }
                                            });
                                    }
                                });
                            }
                        });
                    }
                });

                runner.testGroup("createFolderAsync(String,Value<Folder>)", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("with null path and null value", new Action1<Test>()
                        {
                            @Override
                            public void run(final Test test)
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
                                                    test.assertFalse(folderCreated);
                                                }
                                            });
                                    }
                                });
                            }
                        });

                        runner.test("with empty path and null value", new Action1<Test>()
                        {
                            @Override
                            public void run(final Test test)
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
                                                    test.assertFalse(folderCreated);
                                                }
                                            });
                                    }
                                });
                            }
                        });

                        runner.test("with relative path and null value", new Action1<Test>()
                        {
                            @Override
                            public void run(final Test test)
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
                                                    test.assertFalse(folderCreated);
                                                }
                                            });
                                    }
                                });
                            }
                        });

                        runner.test("with rooted path and null value", new Action1<Test>()
                        {
                            @Override
                            public void run(final Test test)
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
                                                    test.assertTrue(folderCreated);
                                                    test.assertTrue(fileSystem.folderExists("/folder"));
                                                }
                                            });
                                    }
                                });
                            }
                        });

                        runner.test("with null path and non-null value", new Action1<Test>()
                        {
                            @Override
                            public void run(final Test test)
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
                                                    test.assertFalse(folderCreated);
                                                    test.assertFalse(folder.hasValue());
                                                }
                                            });
                                    }
                                });
                            }
                        });

                        runner.test("with empty path and non-null value", new Action1<Test>()
                        {
                            @Override
                            public void run(final Test test)
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
                                                    test.assertFalse(folderCreated);
                                                    test.assertFalse(folder.hasValue());
                                                }
                                            });
                                    }
                                });
                            }
                        });

                        runner.test("with relative path and non-null value", new Action1<Test>()
                        {
                            @Override
                            public void run(final Test test)
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
                                                    test.assertFalse(folderCreated);
                                                    test.assertFalse(folder.hasValue());
                                                }
                                            });
                                    }
                                });
                            }
                        });

                        runner.test("with rooted path and non-null value", new Action1<Test>()
                        {
                            @Override
                            public void run(final Test test)
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
                                                    test.assertTrue(folderCreated);
                                                    test.assertTrue(fileSystem.folderExists("/folder"));

                                                    test.assertTrue(folder.hasValue());
                                                    test.assertNotNull(folder.get());
                                                    test.assertEqual("/folder", folder.get().getPath().toString());
                                                }
                                            });
                                    }
                                });
                            }
                        });
                    }
                });

                runner.testGroup("createFolderAsync(Path)", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("with null path", new Action1<Test>()
                        {
                            @Override
                            public void run(final Test test)
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
                                                    test.assertFalse(folderCreated);
                                                }
                                            });
                                    }
                                });
                            }
                        });

                        runner.test("with empty path", new Action1<Test>()
                        {
                            @Override
                            public void run(final Test test)
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
                                                    test.assertFalse(folderCreated);
                                                }
                                            });
                                    }
                                });
                            }
                        });

                        runner.test("with relative path", new Action1<Test>()
                        {
                            @Override
                            public void run(final Test test)
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
                                                    test.assertFalse(arg1);
                                                }
                                            });
                                    }
                                });
                            }
                        });

                        runner.test("with rooted path", new Action1<Test>()
                        {
                            @Override
                            public void run(final Test test)
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
                                                    test.assertTrue(folderCreated);
                                                    test.assertTrue(fileSystem.folderExists("/folder"));
                                                }
                                            });
                                    }
                                });
                            }
                        });
                    }
                });

                runner.testGroup("createFolderAsync(Path,Value<Folder>)", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("with null path and null value", new Action1<Test>()
                        {
                            @Override
                            public void run(final Test test)
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
                                                    test.assertFalse(folderCreated);
                                                }
                                            });
                                    }
                                });
                            }
                        });

                        runner.test("with empty path and null value", new Action1<Test>()
                        {
                            @Override
                            public void run(final Test test)
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
                                                    test.assertFalse(folderCreated);
                                                }
                                            });
                                    }
                                });
                            }
                        });

                        runner.test("with relative path and null value", new Action1<Test>()
                        {
                            @Override
                            public void run(final Test test)
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
                                                    test.assertFalse(folderCreated);
                                                }
                                            });
                                    }
                                });
                            }
                        });

                        runner.test("with rooted path and null value", new Action1<Test>()
                        {
                            @Override
                            public void run(final Test test)
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
                                                    test.assertTrue(folderCreated);
                                                    test.assertTrue(fileSystem.folderExists("/folder"));
                                                }
                                            });
                                    }
                                });
                            }
                        });

                        runner.test("with null path and non-null value", new Action1<Test>()
                        {
                            @Override
                            public void run(final Test test)
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
                                                    test.assertFalse(folderCreated);
                                                    test.assertFalse(folder.hasValue());
                                                }
                                            });
                                    }
                                });
                            }
                        });

                        runner.test("with empty path and non-null value", new Action1<Test>()
                        {
                            @Override
                            public void run(final Test test)
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
                                                    test.assertFalse(folderCreated);
                                                    test.assertFalse(folder.hasValue());
                                                }
                                            });
                                    }
                                });
                            }
                        });

                        runner.test("with relative path and non-null value", new Action1<Test>()
                        {
                            @Override
                            public void run(final Test test)
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
                                                    test.assertFalse(folderCreated);
                                                    test.assertFalse(folder.hasValue());
                                                }
                                            });
                                    }
                                });
                            }
                        });

                        runner.test("with rooted path and non-null value", new Action1<Test>()
                        {
                            @Override
                            public void run(final Test test)
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
                                                    test.assertTrue(folderCreated);
                                                    test.assertTrue(fileSystem.folderExists("/folder"));

                                                    test.assertTrue(folder.hasValue());
                                                    test.assertNotNull(folder.get());
                                                    test.assertEqual("/folder", folder.get().getPath().toString());
                                                }
                                            });
                                    }
                                });
                            }
                        });
                    }
                });

                runner.testGroup("deleteFolder(String)", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("with null path", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                test.assertFalse(fileSystem.deleteFolder((String)null));
                            }
                        });

                        runner.test("with empty path", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                test.assertFalse(fileSystem.deleteFolder(""));
                            }
                        });

                        runner.test("with relative path", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                fileSystem.createFolder("/folder");
                                test.assertFalse(fileSystem.deleteFolder("folder"));
                            }
                        });

                        runner.test("with non-existing path", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                test.assertFalse(fileSystem.deleteFolder("/folder"));
                            }
                        });

                        runner.test("with existing folder path", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                fileSystem.createFolder("/folder/");
                                test.assertTrue(fileSystem.deleteFolder("/folder/"));
                                test.assertFalse(fileSystem.deleteFolder("/folder/"));
                            }
                        });

                        runner.test("with existing folder path with sibling folders", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                fileSystem.createFolder("/folder/a");
                                fileSystem.createFolder("/folder/b");
                                fileSystem.createFolder("/folder/c");
                                test.assertTrue(fileSystem.deleteFolder("/folder/c"));
                                test.assertFalse(fileSystem.deleteFolder("/folder/c"));
                            }
                        });
                    }
                });

                runner.testGroup("deleteFolderAsync(String)", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("with null path", new Action1<Test>()
                        {
                            @Override
                            public void run(final Test test)
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
                                                    test.assertFalse(folderDeleted);
                                                }
                                            });
                                    }
                                });
                            }
                        });

                        runner.test("with empty path", new Action1<Test>()
                        {
                            @Override
                            public void run(final Test test)
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
                                                    test.assertFalse(folderDeleted);
                                                }
                                            });
                                    }
                                });
                            }
                        });

                        runner.test("with relative path", new Action1<Test>()
                        {
                            @Override
                            public void run(final Test test)
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
                                                    test.assertFalse(folderDeleted);
                                                    test.assertTrue(fileSystem.folderExists("/folder"));
                                                }
                                            });
                                    }
                                });
                            }
                        });

                        runner.test("with non-existing folder path", new Action1<Test>()
                        {
                            @Override
                            public void run(final Test test)
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
                                                    test.assertFalse(folderDeleted);
                                                }
                                            });
                                    }
                                });
                            }
                        });

                        runner.test("with existing folder path", new Action1<Test>()
                        {
                            @Override
                            public void run(final Test test)
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
                                                    test.assertTrue(folderDeleted);
                                                    test.assertFalse(fileSystem.folderExists("/folder/"));
                                                }
                                            });
                                    }
                                });
                            }
                        });

                        runner.test("with existing folder path and sibling folders", new Action1<Test>()
                        {
                            @Override
                            public void run(final Test test)
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
                                                    test.assertTrue(folderDeleted);
                                                    test.assertFalse(fileSystem.folderExists("/folder/c"));
                                                    test.assertTrue(fileSystem.folderExists("/folder/a"));
                                                    test.assertTrue(fileSystem.folderExists("/folder/b"));
                                                }
                                            });
                                    }
                                });
                            }
                        });
                    }
                });

                runner.testGroup("deleteFolderAsync(Path)", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("with null path", new Action1<Test>()
                        {
                            @Override
                            public void run(final Test test)
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
                                                    test.assertFalse(folderDeleted);
                                                }
                                            });
                                    }
                                });
                            }
                        });

                        runner.test("with empty path", new Action1<Test>()
                        {
                            @Override
                            public void run(final Test test)
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
                                                    test.assertFalse(folderDeleted);
                                                }
                                            });
                                    }
                                });
                            }
                        });

                        runner.test("with relative path", new Action1<Test>()
                        {
                            @Override
                            public void run(final Test test)
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
                                                    test.assertFalse(folderDeleted);
                                                    test.assertTrue(fileSystem.folderExists("/folder"));
                                                }
                                            });
                                    }
                                });
                            }
                        });

                        runner.test("with non-existing folder path", new Action1<Test>()
                        {
                            @Override
                            public void run(final Test test)
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
                                                    test.assertFalse(folderDeleted);
                                                }
                                            });
                                    }
                                });
                            }
                        });

                        runner.test("with existing folder path", new Action1<Test>()
                        {
                            @Override
                            public void run(final Test test)
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
                                                    test.assertTrue(folderDeleted);
                                                    test.assertFalse(fileSystem.folderExists("/folder/"));
                                                }
                                            });
                                    }
                                });
                            }
                        });

                        runner.test("with existing folder path with sibling folders", new Action1<Test>()
                        {
                            @Override
                            public void run(final Test test)
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
                                                    test.assertTrue(folderDeleted);
                                                    test.assertFalse(fileSystem.folderExists("/folder/c"));
                                                    test.assertTrue(fileSystem.folderExists("/folder/a"));
                                                    test.assertTrue(fileSystem.folderExists("/folder/b"));
                                                }
                                            });
                                    }
                                });
                            }
                        });
                    }
                });

                runner.testGroup("getFile(String)", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("with null path", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                final File file = fileSystem.getFile((String)null);
                                test.assertNull(file);
                            }
                        });

                        runner.test("with empty path", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                final File file = fileSystem.getFile("");
                                test.assertNull(file);
                            }
                        });

                        runner.test("with relative path", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                final File file = fileSystem.getFile("a/b/c");
                                test.assertNull(file);
                            }
                        });

                        runner.test("with forward slash", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                final File file = fileSystem.getFile("/");
                                test.assertNull(file);
                            }
                        });

                        runner.test("with backslash", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                final File file = fileSystem.getFile("\\");
                                test.assertNull(file);
                            }
                        });

                        runner.test("with Windows root", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                final File file = fileSystem.getFile("Z:\\");
                                test.assertNull(file);
                            }
                        });

                        runner.test("with rooted file path", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                final File file = fileSystem.getFile("/a/b");
                                test.assertNotNull(file);
                                test.assertEqual("/a/b", file.toString());
                            }
                        });
                    }
                });

                runner.testGroup("fileExists(String)", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("with rooted path", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                final Root root = fileSystem.getRoots().first();
                                test.assertFalse(fileSystem.fileExists(root.getPath().toString()));
                            }
                        });

                        runner.test("with existing folder path", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                fileSystem.createFolder("/folderName");
                                test.assertFalse(fileSystem.fileExists("/folerName"));
                            }
                        });

                        runner.test("with existing file path", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                fileSystem.createFile("/file1.xml");
                                test.assertTrue(fileSystem.fileExists("/file1.xml"));
                            }
                        });
                    }
                });

                runner.testGroup("fileExistsAsync(String)", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("with root path", new Action1<Test>()
                        {
                            @Override
                            public void run(final Test arg1)
                            {

                            }
                        });
                    }
                });
            }
        });
    }
    
    private static FileSystem getFileSystem(Function0<FileSystem> creator)
    {
        return creator.run();
    }

    private static FileSystem getFileSystem(Function0<FileSystem> creator, AsyncRunner parallelRunner)
    {
        final FileSystem fileSystem = getFileSystem(creator);
        fileSystem.setAsyncRunner(parallelRunner);
        return fileSystem;
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
                                test.assertFalse(fileExists);
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
                                test.assertFalse(fileExists);
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
                                test.assertTrue(fileExists);
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
                                test.assertFalse(fileExists);
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
                                test.assertFalse(fileExists);
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
                                test.assertTrue(fileExists);
                            }
                        });
            }
        });
    }

    @Test
    public void createFileWithNullString()
    {
        final FileSystem fileSystem = getFileSystem(creator);
        test.assertFalse(fileSystem.createFile((String)null));
    }

    @Test
    public void createFileWithEmptyString()
    {
        final FileSystem fileSystem = getFileSystem(creator);
        test.assertFalse(fileSystem.createFile(""));
    }

    @Test
    public void createFileWithRelativePathString()
    {
        final FileSystem fileSystem = getFileSystem(creator);
        test.assertFalse(fileSystem.createFile("things.txt"));
    }

    @Test
    public void createFileWithNonExistingRootedPathString()
    {
        final FileSystem fileSystem = getFileSystem(creator);

        test.assertTrue(fileSystem.createFile("/things.txt"));

        test.assertTrue(fileSystem.fileExists("/things.txt"));
        test.assertEqual(new byte[0], fileSystem.getFileContents("/things.txt"));
    }

    @Test
    public void createFileWithNonExistingRootedPathStringAndContents()
    {
        final FileSystem fileSystem = getFileSystem(creator);

        test.assertTrue(fileSystem.createFile("/things.txt", new byte[] { 0, 1, 2, 3 }));

        test.assertTrue(fileSystem.fileExists("/things.txt"));
        test.assertEqual(new byte[] { 0, 1, 2, 3 }, fileSystem.getFileContents("/things.txt"));
    }

    @Test
    public void createFileWithExistingRootedPathString()
    {
        final FileSystem fileSystem = getFileSystem(creator);
        fileSystem.createFile("/things.txt");

        test.assertFalse(fileSystem.createFile("/things.txt"));

        test.assertTrue(fileSystem.fileExists("/things.txt"));
        test.assertEqual(new byte[0], fileSystem.getFileContents("/things.txt"));
    }

    @Test
    public void createFileWithExistingRootedPathStringAndByteArrayContents()
    {
        final FileSystem fileSystem = getFileSystem(creator);
        fileSystem.createFile("/things.txt");

        test.assertFalse(fileSystem.createFile("/things.txt", new byte[] { 0, 1, 2, 3 }));

        test.assertTrue(fileSystem.fileExists("/things.txt"));
        test.assertEqual(new byte[0], fileSystem.getFileContents("/things.txt"));
    }

    @Test
    public void createFileWithExistingRootedPathStringAndStringContents()
    {
        final FileSystem fileSystem = getFileSystem(creator);
        fileSystem.createFile("/things.txt");

        test.assertFalse(fileSystem.createFile("/things.txt", "ABC"));

        test.assertTrue(fileSystem.fileExists("/things.txt"));
        test.assertEqual(new byte[0], fileSystem.getFileContents("/things.txt"));
    }

    @Test
    public void createFileWithExistingRootedPathStringAndStringContentsAndEncoding()
    {
        final FileSystem fileSystem = getFileSystem(creator);
        fileSystem.createFile("/things.txt");

        test.assertFalse(fileSystem.createFile("/things.txt", "ABC", CharacterEncoding.UTF_8));

        test.assertTrue(fileSystem.fileExists("/things.txt"));
        test.assertEqual(new byte[0], fileSystem.getFileContents("/things.txt"));
    }

    @Test
    public void createFileWithNullStringAndNullValue()
    {
        final FileSystem fileSystem = getFileSystem(creator);
        test.assertFalse(fileSystem.createFile((String)null, (Out<File>)null));
    }

    @Test
    public void createFileWithEmptyStringAndNullValue()
    {
        final FileSystem fileSystem = getFileSystem(creator);
        test.assertFalse(fileSystem.createFile("", (Out<File>)null));
    }

    @Test
    public void createFileWithRelativePathStringAndNullValue()
    {
        final FileSystem fileSystem = getFileSystem(creator);
        test.assertFalse(fileSystem.createFile("things.txt", (Out<File>)null));
    }

    @Test
    public void createFileWithNonExistingRootedPathStringAndNullValue()
    {
        final FileSystem fileSystem = getFileSystem(creator);
        test.assertTrue(fileSystem.createFile("/things.txt", (Out<File>)null));
        test.assertTrue(fileSystem.fileExists("/things.txt"));
        test.assertEqual(new byte[0], fileSystem.getFileContents("/things.txt"));
    }

    @Test
    public void createFileWithNonExistingRootedPathStringAndContentsAndNullValue()
    {
        final FileSystem fileSystem = getFileSystem(creator);
        test.assertTrue(fileSystem.createFile("/things.txt", new byte[] { 10, 11, 12 }, null));
        test.assertTrue(fileSystem.fileExists("/things.txt"));
        test.assertEqual(new byte[] { 10, 11, 12 }, fileSystem.getFileContents("/things.txt"));
    }

    @Test
    public void createFileWithNonExistingRootedPathStringAndStringContentsAndNullValue()
    {
        final FileSystem fileSystem = getFileSystem(creator);

        test.assertTrue(fileSystem.createFile("/things.txt", "ABC", (Out<File>)null));

        test.assertTrue(fileSystem.fileExists("/things.txt"));
        test.assertEqual("ABC", fileSystem.getFileContentsAsString("/things.txt"));
    }

    @Test
    public void createFileWithNonExistingRootedPathStringAndStringContentsAndEncodingAndNullValue()
    {
        final FileSystem fileSystem = getFileSystem(creator);

        test.assertTrue(fileSystem.createFile("/things.txt", "ABC", CharacterEncoding.UTF_8, null));

        test.assertTrue(fileSystem.fileExists("/things.txt"));
        test.assertEqual("ABC", fileSystem.getFileContentsAsString("/things.txt"));
    }

    @Test
    public void createFileWithExistingRootedPathStringAndNullValue()
    {
        final FileSystem fileSystem = getFileSystem(creator);
        fileSystem.createFile("/things.txt");

        test.assertFalse(fileSystem.createFile("/things.txt", (Out<File>)null));

        test.assertTrue(fileSystem.fileExists("/things.txt"));
        test.assertEqual(new byte[0], fileSystem.getFileContents("/things.txt"));
    }

    @Test
    public void createFileWithExistingRootedPathStringAndContentsNullValue()
    {
        final FileSystem fileSystem = getFileSystem(creator);
        fileSystem.createFile("/things.txt");

        test.assertFalse(fileSystem.createFile("/things.txt", new byte[] { 0, 1 }, null));

        test.assertTrue(fileSystem.fileExists("/things.txt"));
        test.assertEqual(new byte[0], fileSystem.getFileContents("/things.txt"));
    }

    @Test
    public void createFileWithNullStringAndNonNullValue()
    {
        final FileSystem fileSystem = getFileSystem(creator);
        final Value<File> file = new Value<>();
        test.assertFalse(fileSystem.createFile((String)null, file));
    }

    @Test
    public void createFileWithEmptyStringAndNonNullValue()
    {
        final FileSystem fileSystem = getFileSystem(creator);
        final Value<File> file = new Value<>();
        test.assertFalse(fileSystem.createFile("", file));
    }

    @Test
    public void createFileWithRelativePathStringAndNonNullValue()
    {
        final FileSystem fileSystem = getFileSystem(creator);
        final Value<File> file = new Value<>();
        final boolean fileCreated = fileSystem.createFile("things.txt", file);
        test.assertFalse(fileCreated);
        test.assertFalse(file.hasValue());
    }

    @Test
    public void createFileWithNonExistingRootedPathStringAndNonNullValue()
    {
        final FileSystem fileSystem = getFileSystem(creator);
        final Value<File> file = new Value<>();
        final boolean fileCreated = fileSystem.createFile("/things.txt", file);
        test.assertTrue(fileCreated);
        test.assertTrue(file.hasValue());
        test.assertNotNull(file.get());
        test.assertEqual("/things.txt", file.get().getPath().toString());
    }

    @Test
    public void createFileWithExistingRootedPathStringAndNonNullValue()
    {
        final FileSystem fileSystem = getFileSystem(creator);
        fileSystem.createFile("/things.txt");

        final Value<File> file = new Value<>();
        test.assertFalse(fileSystem.createFile("/things.txt", file));
        test.assertTrue(file.hasValue());
        test.assertEqual(Path.parse("/things.txt"), file.get().getPath());
    }

    @Test
    public void createFileWithNullPathAndContents()
    {
        final FileSystem fileSystem = getFileSystem(creator);
        test.assertFalse(fileSystem.createFile((Path)null, new byte[] { 0, 1, 2 }));
    }

    @Test
    public void createFileWithNullPathAndStringContents()
    {
        final FileSystem fileSystem = getFileSystem(creator);
        test.assertFalse(fileSystem.createFile((Path)null, "ABC"));
    }

    @Test
    public void createFileWithNullPathAndStringContentsAndEncoding()
    {
        final FileSystem fileSystem = getFileSystem(creator);
        test.assertFalse(fileSystem.createFile((Path)null, "ABC", CharacterEncoding.UTF_8));
    }

    @Test
    public void createFileWithInvalidPath()
    {
        final FileSystem fileSystem = getFileSystem(creator);
        final Value<File> file = new Value<>();
        final boolean fileCreated = fileSystem.createFile("/\u0000?#!.txt", file);
        test.assertFalse("Wrong fileCreated", fileCreated);
        test.assertFalse("Wrong file.hasValue()", file.hasValue());
        test.assertNull("Wrong file.get()", file.get());
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
                                test.assertFalse(fileCreated);
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
                                test.assertFalse(fileCreated);
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
                                test.assertFalse(fileCreated);
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
                                test.assertTrue(fileCreated);
                                test.assertTrue(fileSystem.fileExists("/things.txt"));
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
                                test.assertFalse(fileCreated);
                                test.assertTrue(fileSystem.fileExists("/things.txt"));
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
                                test.assertFalse(fileCreated);
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
                                test.assertFalse(fileCreated);
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
                                test.assertFalse(fileCreated);
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
                                test.assertTrue(fileCreated);
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
                                test.assertFalse(fileCreated);
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
                                test.assertFalse(fileCreated);
                                test.assertFalse(file.hasValue());
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
                                test.assertFalse(fileCreated);
                                test.assertFalse(file.hasValue());
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
                                test.assertFalse(fileCreated);
                                test.assertFalse(file.hasValue());
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
                                test.assertTrue(fileCreated);
                                test.assertEqual("/things.txt", file.get().getPath().toString());
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
                                test.assertFalse(fileCreated);
                                test.assertTrue(file.hasValue());
                                test.assertNotNull(file.get());
                                test.assertEqual(Path.parse("/things.txt"), file.get().getPath());
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
                                test.assertFalse(fileCreated);
                                test.assertFalse(file.hasValue());
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
                                test.assertFalse(fileCreated);
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
                                test.assertFalse(fileCreated);
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
                                test.assertFalse(fileCreated);
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
                                test.assertTrue(fileCreated);
                                test.assertTrue(fileSystem.fileExists("/things.txt"));
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
                                test.assertFalse(fileCreated);
                                test.assertTrue(fileSystem.fileExists("/things.txt"));
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
                                test.assertFalse(fileCreated);
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
                                test.assertFalse(fileCreated);
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
                                test.assertFalse(fileCreated);
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
                                test.assertTrue(fileCreated);
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
                                test.assertFalse(fileCreated);
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
                                test.assertFalse(fileCreated);
                                test.assertFalse(file.hasValue());
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
                                test.assertFalse(fileCreated);
                                test.assertFalse(file.hasValue());
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
                                test.assertFalse(fileCreated);
                                test.assertFalse(file.hasValue());
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
                                test.assertTrue(fileCreated);
                                test.assertEqual("/things.txt", file.get().getPath().toString());
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
                                test.assertFalse(fileCreated);
                                test.assertTrue(file.hasValue());
                                test.assertNotNull(file.get());
                                test.assertEqual(Path.parse("/things.txt"), file.get().getPath());
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
                                test.assertFalse(fileCreated);
                                test.assertFalse(file.hasValue());
                            }
                        });
            }
        });
    }

    @Test
    public void deleteFileWithNullPathString()
    {
        final FileSystem fileSystem = getFileSystem(creator);
        test.assertFalse(fileSystem.deleteFile((String)null));
    }

    @Test
    public void deleteFileWithEmptyPathString()
    {
        final FileSystem fileSystem = getFileSystem(creator);
        test.assertFalse(fileSystem.deleteFile(""));
    }

    @Test
    public void deleteFileWithRelativePathString()
    {
        final FileSystem fileSystem = getFileSystem(creator);
        test.assertFalse(fileSystem.deleteFile("relativeFile.txt"));
    }

    @Test
    public void deleteFileWithRootedPathStringThatDoesntExist()
    {
        final FileSystem fileSystem = getFileSystem(creator);
        test.assertFalse(fileSystem.deleteFile("/idontexist.txt"));
    }

    @Test
    public void deleteFileWithRootedPathStringThatExists()
    {
        final FileSystem fileSystem = getFileSystem(creator);
        fileSystem.createFile("/iexist.txt");

        test.assertTrue(fileSystem.deleteFile("/iexist.txt"));
        test.assertFalse(fileSystem.fileExists("/iexist.txt"));

        test.assertFalse(fileSystem.deleteFile("/iexist.txt"));
        test.assertFalse(fileSystem.fileExists("/iexist.txt"));
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
                                test.assertFalse(fileDeleted);
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
                                test.assertFalse(fileDeleted);
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
                                test.assertFalse(fileDeleted);
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
                                test.assertFalse(fileDeleted);
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
                                test.assertTrue(fileDeleted);
                                test.assertFalse(fileSystem.fileExists("/iexist.txt"));
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
                                test.assertFalse(fileDeleted);
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
                                test.assertFalse(fileDeleted);
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
                                test.assertFalse(fileDeleted);
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
                                test.assertFalse(fileDeleted);
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
                                test.assertTrue(fileDeleted);
                                test.assertFalse(fileSystem.fileExists("/iexist.txt"));
                            }
                        });
            }
        });
    }

    @Test
    public void getFileContentsStringWithNull()
    {
        final FileSystem fileSystem = getFileSystem(creator);
        test.assertNull(fileSystem.getFileContents((String)null));
    }

    @Test
    public void getFileContentsStringWithEmpty()
    {
        final FileSystem fileSystem = getFileSystem(creator);
        test.assertNull(fileSystem.getFileContents(""));
    }

    @Test
    public void getFileContentsStringWithRelativePath()
    {
        final FileSystem fileSystem = getFileSystem(creator);
        test.assertNull(fileSystem.getFileContents("thing.txt"));
    }

    @Test
    public void getFileContentsStringWithNonExistingRootedPath()
    {
        final FileSystem fileSystem = getFileSystem(creator);
        test.assertNull(fileSystem.getFileContents("/thing.txt"));
    }

    @Test
    public void getFileContentsStringWithEmptyFile()
    {
        final FileSystem fileSystem = getFileSystem(creator);
        fileSystem.createFile("/thing.txt");
        test.assertEqual(new byte[0], fileSystem.getFileContents("/thing.txt"));
    }

    @Test
    public void getFileContentsPathWithNull()
    {
        final FileSystem fileSystem = getFileSystem(creator);
        test.assertNull(fileSystem.getFileContents((Path)null));
    }

    @Test
    public void getFileContentsPathWithEmpty()
    {
        final FileSystem fileSystem = getFileSystem(creator);
        test.assertNull(fileSystem.getFileContents(Path.parse("")));
    }

    @Test
    public void getFileContentsPathWithRelativePath()
    {
        final FileSystem fileSystem = getFileSystem(creator);
        test.assertNull(fileSystem.getFileContents(Path.parse("thing.txt")));
    }

    @Test
    public void getFileContentsPathWithNonExistingRootedPath()
    {
        final FileSystem fileSystem = getFileSystem(creator);
        test.assertNull(fileSystem.getFileContents(Path.parse("/thing.txt")));
    }

    @Test
    public void getFileContentsPathWithEmptyFile()
    {
        final FileSystem fileSystem = getFileSystem(creator);
        fileSystem.createFile("/thing.txt");
        test.assertEqual(new byte[0], fileSystem.getFileContents(Path.parse("/thing.txt")));
    }

    @Test
    public void getFileContentsAsStringWithNullPathString()
    {
        final FileSystem fileSystem = getFileSystem(creator);
        test.assertNull(fileSystem.getFileContentsAsString((String)null));
    }

    @Test
    public void getFileContentsAsStringWithEmptyPathString()
    {
        final FileSystem fileSystem = getFileSystem(creator);
        test.assertNull(fileSystem.getFileContentsAsString(""));
    }

    @Test
    public void getFileContentsAsStringWithRelativePathString()
    {
        final FileSystem fileSystem = getFileSystem(creator);
        test.assertNull(fileSystem.getFileContentsAsString("file.txt"));
    }

    @Test
    public void getFileContentsAsStringWithNonExistingRootedPathString()
    {
        final FileSystem fileSystem = getFileSystem(creator);
        test.assertNull(fileSystem.getFileContentsAsString("/file.txt"));
    }

    @Test
    public void getFileContentsAsStringWithExistingRootedPathStringWithNoContents()
    {
        final FileSystem fileSystem = getFileSystem(creator);
        fileSystem.createFile("/file.txt");
        test.assertEqual("", fileSystem.getFileContentsAsString("/file.txt"));
    }

    @Test
    public void getFileContentsAsStringWithExistingRootPathStringWithContents()
    {
        final FileSystem fileSystem = getFileSystem(creator);
        fileSystem.createFile("/file.txt", CharacterEncoding.UTF_8.encode("Hello there!"));
        test.assertEqual("Hello there!", fileSystem.getFileContentsAsString("/file.txt"));
    }

    @Test
    public void getFileContentsAsStringWithNullEncodingAndNullPathString()
    {
        final FileSystem fileSystem = getFileSystem(creator);
        test.assertNull(fileSystem.getFileContentsAsString((String)null));
    }

    @Test
    public void getFileContentsAsStringWithNullEncodingAndEmptyPathString()
    {
        final FileSystem fileSystem = getFileSystem(creator);
        test.assertNull(fileSystem.getFileContentsAsString(""));
    }

    @Test
    public void getFileContentsAsStringWithNullEncodingAndRelativePathString()
    {
        final FileSystem fileSystem = getFileSystem(creator);
        test.assertNull(fileSystem.getFileContentsAsString("file.txt"));
    }

    @Test
    public void getFileContentsAsStringWithNullEncodingAndNonExistingRootedPathString()
    {
        final FileSystem fileSystem = getFileSystem(creator);
        test.assertNull(fileSystem.getFileContentsAsString("/file.txt"));
    }

    @Test
    public void getFileContentsAsStringWithNullEncodingAndExistingRootedPathStringWithNoContents()
    {
        final FileSystem fileSystem = getFileSystem(creator);
        fileSystem.createFile("/file.txt");
        test.assertNull(fileSystem.getFileContentsAsString("/file.txt", null));
    }

    @Test
    public void getFileContentsAsStringWithNullEncodingAndExistingRootPathStringWithContents()
    {
        final FileSystem fileSystem = getFileSystem(creator);
        fileSystem.createFile("/file.txt", CharacterEncoding.UTF_8.encode("Hello there!"));
        test.assertNull(fileSystem.getFileContentsAsString("/file.txt", null));
    }

    @Test
    public void getFileContentByteReadStreamWhenFileDoesntExist()
    {
        final FileSystem fileSystem = getFileSystem(creator);
        test.assertNull(fileSystem.getFileContentByteReadStream("C:/i/dont/exist.txt"));
    }

    @Test
    public void getFileContentCharacterReadStreamWhenFileDoesntExist()
    {
        final FileSystem fileSystem = getFileSystem(creator);
        test.assertNull(fileSystem.getFileContentCharacterReadStream("C:/i/dont/exist.txt"));
    }

    @Test
    public void getFileContentBlocksStringWithNull()
    {
        final FileSystem fileSystem = getFileSystem(creator);
        test.assertNull(fileSystem.getFileContentBlocks((String)null));
    }

    @Test
    public void getFileContentBlocksStringWithEmpty()
    {
        final FileSystem fileSystem = getFileSystem(creator);
        test.assertNull(fileSystem.getFileContentBlocks(""));
    }

    @Test
    public void getFileContentBlocksStringWithRelativePath()
    {
        final FileSystem fileSystem = getFileSystem(creator);
        test.assertNull(fileSystem.getFileContentBlocks("B"));
    }

    @Test
    public void getFileContentBlocksStringWithNonExistingRootedFilePath()
    {
        final FileSystem fileSystem = getFileSystem(creator);
        test.assertNull(fileSystem.getFileContentBlocks("/a.txt"));
    }

    @Test
    public void getFileContentBlocksStringWithExistingRootedFilePathWithNoContents()
    {
        final FileSystem fileSystem = getFileSystem(creator);
        fileSystem.createFile("/a.txt");

        final Iterable<byte[]> fileContentBlocks = fileSystem.getFileContentBlocks("/a.txt");
        test.assertNotNull(fileContentBlocks);
        test.assertEqual(0, fileContentBlocks.getCount());
    }

    @Test
    public void getFileContentBlocksStringWithExistingRootedFilePathWithContents()
    {
        final FileSystem fileSystem = getFileSystem(creator);
        fileSystem.createFile("/a.txt", new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11});

        final Iterable<byte[]> fileContentBlocks = fileSystem.getFileContentBlocks("/a.txt");
        test.assertNotNull(fileContentBlocks);

        final byte[] fileContents = Array.merge(fileContentBlocks);
        test.assertEqual(new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11 }, fileContents);
    }

    @Test
    public void getFileContentLinesWithNullPathString()
    {
        final FileSystem fileSystem = getFileSystem(creator);

        test.assertNull(fileSystem.getFileContentLines((String)null));
    }

    @Test
    public void getFileContentLinesWithEmptyPathString()
    {
        final FileSystem fileSystem = getFileSystem(creator);

        test.assertNull(fileSystem.getFileContentLines(""));
    }

    @Test
    public void getFileContentLinesWithRelativePathString()
    {
        final FileSystem fileSystem = getFileSystem(creator);

        test.assertNull(fileSystem.getFileContentLines("relative/file.txt"));
    }

    @Test
    public void getFileContentLinesWithNonExistingRootedPathString()
    {
        final FileSystem fileSystem = getFileSystem(creator);

        test.assertNull(fileSystem.getFileContentLines("/folder/file.csv"));
    }

    @Test
    public void getFileContentLinesWithExistingEmptyFileAtRootedPathString()
    {
        final FileSystem fileSystem = getFileSystem(creator);
        fileSystem.createFile("/folder/file.csv");

        test.assertEqual(new String[0], Array.toStringArray(fileSystem.getFileContentLines("/folder/file.csv")));
    }

    @Test
    public void getFileContentLinesWithExistingSingleLineFileAtRootedPathString()
    {
        final FileSystem fileSystem = getFileSystem(creator);
        fileSystem.createFile("/folder/file.csv", "abcdef");

        test.assertEqual(new String[] { "abcdef" }, Array.toStringArray(fileSystem.getFileContentLines("/folder/file.csv")));
    }

    @Test
    public void getFileContentLinesWithExistingMultipleLineFileAtRootedPathString()
    {
        final FileSystem fileSystem = getFileSystem(creator);
        fileSystem.createFile("/folder/file.csv", "ab\ncd\r\ne\nf\n");

        test.assertEqual(new String[] { "ab\n", "cd\r\n", "e\n", "f\n" }, Array.toStringArray(fileSystem.getFileContentLines("/folder/file.csv")));
    }

    @Test
    public void getFileContentLinesWithNullPath()
    {
        final FileSystem fileSystem = getFileSystem(creator);

        test.assertNull(fileSystem.getFileContentLines((Path)null));
    }

    @Test
    public void getFileContentLinesWithEmptyPath()
    {
        final FileSystem fileSystem = getFileSystem(creator);

        test.assertNull(fileSystem.getFileContentLines(Path.parse("")));
    }

    @Test
    public void getFileContentLinesWithRelativePath()
    {
        final FileSystem fileSystem = getFileSystem(creator);

        test.assertNull(fileSystem.getFileContentLines(Path.parse("relative/file.txt")));
    }

    @Test
    public void getFileContentLinesWithNonExistingRootedPath()
    {
        final FileSystem fileSystem = getFileSystem(creator);

        test.assertNull(fileSystem.getFileContentLines(Path.parse("/folder/file.csv")));
    }

    @Test
    public void getFileContentLinesWithExistingEmptyFileAtRootedPath()
    {
        final FileSystem fileSystem = getFileSystem(creator);
        fileSystem.createFile("/folder/file.csv");

        test.assertEqual(new String[0], Array.toStringArray(fileSystem.getFileContentLines(Path.parse("/folder/file.csv"))));
    }

    @Test
    public void getFileContentLinesWithExistingSingleLineFileAtRootedPath()
    {
        final FileSystem fileSystem = getFileSystem(creator);
        fileSystem.createFile("/folder/file.csv", "abcdef");

        test.assertEqual(new String[] { "abcdef" }, Array.toStringArray(fileSystem.getFileContentLines(Path.parse("/folder/file.csv"))));
    }

    @Test
    public void getFileContentLinesWithExistingMultipleLineFileAtRootedPath()
    {
        final FileSystem fileSystem = getFileSystem(creator);
        fileSystem.createFile("/folder/file.csv", "ab\ncd\r\ne\nf\n");

        test.assertEqual(new String[] { "ab\n", "cd\r\n", "e\n", "f\n" }, Array.toStringArray(fileSystem.getFileContentLines(Path.parse("/folder/file.csv"))));
    }

    @Test
    public void getFileContentLinesIncludeNewLinesWithNullPathString()
    {
        final FileSystem fileSystem = getFileSystem(creator);

        test.assertNull(fileSystem.getFileContentLines((String)null, true));
    }

    @Test
    public void getFileContentLinesIncludeNewLinesWithEmptyPathString()
    {
        final FileSystem fileSystem = getFileSystem(creator);

        test.assertNull(fileSystem.getFileContentLines("", true));
    }

    @Test
    public void getFileContentLinesIncludeNewLinesWithRelativePathString()
    {
        final FileSystem fileSystem = getFileSystem(creator);

        test.assertNull(fileSystem.getFileContentLines("relative/file.txt", true));
    }

    @Test
    public void getFileContentLinesIncludeNewLinesWithNonExistingRootedPathString()
    {
        final FileSystem fileSystem = getFileSystem(creator);

        test.assertNull(fileSystem.getFileContentLines("/folder/file.csv", true));
    }

    @Test
    public void getFileContentLinesIncludeNewLinesWithExistingEmptyFileAtRootedPathString()
    {
        final FileSystem fileSystem = getFileSystem(creator);
        fileSystem.createFile("/folder/file.csv");

        test.assertEqual(new String[0], Array.toStringArray(fileSystem.getFileContentLines("/folder/file.csv", true)));
    }

    @Test
    public void getFileContentLinesIncludeNewLinesWithExistingSingleLineFileAtRootedPathString()
    {
        final FileSystem fileSystem = getFileSystem(creator);
        fileSystem.createFile("/folder/file.csv", "abcdef");

        test.assertEqual(new String[] { "abcdef" }, Array.toStringArray(fileSystem.getFileContentLines("/folder/file.csv", true)));
    }

    @Test
    public void getFileContentLinesIncludeNewLinesWithExistingMultipleLineFileAtRootedPathString()
    {
        final FileSystem fileSystem = getFileSystem(creator);
        fileSystem.createFile("/folder/file.csv", "ab\ncd\r\ne\nf\n");

        test.assertEqual(new String[] { "ab\n", "cd\r\n", "e\n", "f\n" }, Array.toStringArray(fileSystem.getFileContentLines("/folder/file.csv", true)));
    }

    @Test
    public void getFileContentLinesIncludeNewLinesWithNullPath()
    {
        final FileSystem fileSystem = getFileSystem(creator);

        test.assertNull(fileSystem.getFileContentLines((Path)null, true));
    }

    @Test
    public void getFileContentLinesIncludeNewLinesWithEmptyPath()
    {
        final FileSystem fileSystem = getFileSystem(creator);

        test.assertNull(fileSystem.getFileContentLines(Path.parse(""), true));
    }

    @Test
    public void getFileContentLinesIncludeNewLinesWithRelativePath()
    {
        final FileSystem fileSystem = getFileSystem(creator);

        test.assertNull(fileSystem.getFileContentLines(Path.parse("relative/file.txt"), true));
    }

    @Test
    public void getFileContentLinesIncludeNewLinesWithNonExistingRootedPath()
    {
        final FileSystem fileSystem = getFileSystem(creator);

        test.assertNull(fileSystem.getFileContentLines(Path.parse("/folder/file.csv"), true));
    }

    @Test
    public void getFileContentLinesIncludeNewLinesWithExistingEmptyFileAtRootedPath()
    {
        final FileSystem fileSystem = getFileSystem(creator);
        fileSystem.createFile("/folder/file.csv");

        test.assertEqual(new String[0], Array.toStringArray(fileSystem.getFileContentLines(Path.parse("/folder/file.csv"), true)));
    }

    @Test
    public void getFileContentLinesIncludeNewLinesWithExistingSingleLineFileAtRootedPath()
    {
        final FileSystem fileSystem = getFileSystem(creator);
        fileSystem.createFile("/folder/file.csv", "abcdef");

        test.assertEqual(new String[] { "abcdef" }, Array.toStringArray(fileSystem.getFileContentLines(Path.parse("/folder/file.csv"), true)));
    }

    @Test
    public void getFileContentLinesIncludeNewLinesWithExistingMultipleLineFileAtRootedPath()
    {
        final FileSystem fileSystem = getFileSystem(creator);
        fileSystem.createFile("/folder/file.csv", "ab\ncd\r\ne\nf\n");

        test.assertEqual(new String[] { "ab\n", "cd\r\n", "e\n", "f\n" }, Array.toStringArray(fileSystem.getFileContentLines(Path.parse("/folder/file.csv"), true)));
    }

    @Test
    public void getFileContentLinesDontIncludeNewLinesWithNullPathString()
    {
        final FileSystem fileSystem = getFileSystem(creator);

        test.assertNull(fileSystem.getFileContentLines((String)null, false));
    }

    @Test
    public void getFileContentLinesDontIncludeNewLinesWithEmptyPathString()
    {
        final FileSystem fileSystem = getFileSystem(creator);

        test.assertNull(fileSystem.getFileContentLines("", false));
    }

    @Test
    public void getFileContentLinesDontIncludeNewLinesWithRelativePathString()
    {
        final FileSystem fileSystem = getFileSystem(creator);

        test.assertNull(fileSystem.getFileContentLines("relative/file.txt", false));
    }

    @Test
    public void getFileContentLinesDontIncludeNewLinesWithNonExistingRootedPathString()
    {
        final FileSystem fileSystem = getFileSystem(creator);

        test.assertNull(fileSystem.getFileContentLines("/folder/file.csv", false));
    }

    @Test
    public void getFileContentLinesDontIncludeNewLinesWithExistingEmptyFileAtRootedPathString()
    {
        final FileSystem fileSystem = getFileSystem(creator);
        fileSystem.createFile("/folder/file.csv");

        test.assertEqual(new String[0], Array.toStringArray(fileSystem.getFileContentLines("/folder/file.csv", false)));
    }

    @Test
    public void getFileContentLinesDontIncludeNewLinesWithExistingSingleLineFileAtRootedPathString()
    {
        final FileSystem fileSystem = getFileSystem(creator);
        fileSystem.createFile("/folder/file.csv", "abcdef");

        test.assertEqual(new String[] { "abcdef" }, Array.toStringArray(fileSystem.getFileContentLines("/folder/file.csv", false)));
    }

    @Test
    public void getFileContentLinesDontIncludeNewLinesWithExistingMultipleLineFileAtRootedPathString()
    {
        final FileSystem fileSystem = getFileSystem(creator);
        fileSystem.createFile("/folder/file.csv", "ab\ncd\r\ne\nf\n");

        test.assertEqual(new String[] { "ab", "cd", "e", "f" }, Array.toStringArray(fileSystem.getFileContentLines("/folder/file.csv", false)));
    }

    @Test
    public void getFileContentLinesDontIncludeNewLinesEncodingWithExistingMultipleLineFileAtRootedPathString()
    {
        final FileSystem fileSystem = getFileSystem(creator);
        fileSystem.createFile("/folder/file.csv", "ab\ncd\r\ne\nf\n");

        test.assertEqual(new String[] { "ab", "cd", "e", "f" }, Array.toStringArray(fileSystem.getFileContentLines("/folder/file.csv", false, CharacterEncoding.UTF_8)));
    }

    @Test
    public void getFileContentLinesDontIncludeNewLinesWithNullPath()
    {
        final FileSystem fileSystem = getFileSystem(creator);

        test.assertNull(fileSystem.getFileContentLines((Path)null, false));
    }

    @Test
    public void getFileContentLinesDontIncludeNewLinesWithEmptyPath()
    {
        final FileSystem fileSystem = getFileSystem(creator);

        test.assertNull(fileSystem.getFileContentLines(Path.parse(""), false));
    }

    @Test
    public void getFileContentLinesDontIncludeNewLinesWithRelativePath()
    {
        final FileSystem fileSystem = getFileSystem(creator);

        test.assertNull(fileSystem.getFileContentLines(Path.parse("relative/file.txt"), false));
    }

    @Test
    public void getFileContentLinesDontIncludeNewLinesWithNonExistingRootedPath()
    {
        final FileSystem fileSystem = getFileSystem(creator);

        test.assertNull(fileSystem.getFileContentLines(Path.parse("/folder/file.csv"), false));
    }

    @Test
    public void getFileContentLinesDontIncludeNewLinesWithExistingEmptyFileAtRootedPath()
    {
        final FileSystem fileSystem = getFileSystem(creator);
        fileSystem.createFile("/folder/file.csv");

        test.assertEqual(new String[0], Array.toStringArray(fileSystem.getFileContentLines(Path.parse("/folder/file.csv"), false)));
    }

    @Test
    public void getFileContentLinesDontIncludeNewLinesWithExistingSingleLineFileAtRootedPath()
    {
        final FileSystem fileSystem = getFileSystem(creator);
        fileSystem.createFile("/folder/file.csv", "abcdef");

        test.assertEqual(new String[] { "abcdef" }, Array.toStringArray(fileSystem.getFileContentLines(Path.parse("/folder/file.csv"), false)));
    }

    @Test
    public void getFileContentLinesDontIncludeNewLinesWithExistingMultipleLineFileAtRootedPath()
    {
        final FileSystem fileSystem = getFileSystem(creator);
        fileSystem.createFile("/folder/file.csv", "ab\ncd\r\ne\nf\n");

        test.assertEqual(new String[] { "ab", "cd", "e", "f" }, Array.toStringArray(fileSystem.getFileContentLines(Path.parse("/folder/file.csv"), false)));
    }

    @Test
    public void getFileContentLinesEncodingWithNullPathString()
    {
        final FileSystem fileSystem = getFileSystem(creator);

        test.assertNull(fileSystem.getFileContentLines((String)null, CharacterEncoding.UTF_8));
    }

    @Test
    public void getFileContentLinesEncodingWithEmptyPathString()
    {
        final FileSystem fileSystem = getFileSystem(creator);

        test.assertNull(fileSystem.getFileContentLines("", CharacterEncoding.UTF_8));
    }

    @Test
    public void getFileContentLinesEncodingWithRelativePathString()
    {
        final FileSystem fileSystem = getFileSystem(creator);

        test.assertNull(fileSystem.getFileContentLines("relative/file.txt", CharacterEncoding.UTF_8));
    }

    @Test
    public void getFileContentLinesEncodingWithNonExistingRootedPathString()
    {
        final FileSystem fileSystem = getFileSystem(creator);

        test.assertNull(fileSystem.getFileContentLines("/folder/file.csv", CharacterEncoding.UTF_8));
    }

    @Test
    public void getFileContentLinesEncodingWithExistingEmptyFileAtRootedPathString()
    {
        final FileSystem fileSystem = getFileSystem(creator);
        fileSystem.createFile("/folder/file.csv");

        test.assertEqual(new String[0], Array.toStringArray(fileSystem.getFileContentLines("/folder/file.csv", CharacterEncoding.UTF_8)));
    }

    @Test
    public void getFileContentLinesEncodingWithExistingSingleLineFileAtRootedPathString()
    {
        final FileSystem fileSystem = getFileSystem(creator);
        fileSystem.createFile("/folder/file.csv", "abcdef");

        test.assertEqual(new String[] { "abcdef" }, Array.toStringArray(fileSystem.getFileContentLines("/folder/file.csv", CharacterEncoding.UTF_8)));
    }

    @Test
    public void getFileContentLinesEncodingWithExistingMultipleLineFileAtRootedPathString()
    {
        final FileSystem fileSystem = getFileSystem(creator);
        fileSystem.createFile("/folder/file.csv", "ab\ncd\r\ne\nf\n");

        test.assertEqual(new String[] { "ab\n", "cd\r\n", "e\n", "f\n" }, Array.toStringArray(fileSystem.getFileContentLines("/folder/file.csv", CharacterEncoding.UTF_8)));
    }

    @Test
    public void getFileContentLinesEncodingWithNullPath()
    {
        final FileSystem fileSystem = getFileSystem(creator);

        test.assertNull(fileSystem.getFileContentLines((Path)null, CharacterEncoding.UTF_8));
    }

    @Test
    public void getFileContentLinesEncodingWithEmptyPath()
    {
        final FileSystem fileSystem = getFileSystem(creator);

        test.assertNull(fileSystem.getFileContentLines(Path.parse(""), CharacterEncoding.UTF_8));
    }

    @Test
    public void getFileContentLinesEncodingWithRelativePath()
    {
        final FileSystem fileSystem = getFileSystem(creator);

        test.assertNull(fileSystem.getFileContentLines(Path.parse("relative/file.txt"), CharacterEncoding.UTF_8));
    }

    @Test
    public void getFileContentLinesEncodingWithNonExistingRootedPath()
    {
        final FileSystem fileSystem = getFileSystem(creator);

        test.assertNull(fileSystem.getFileContentLines(Path.parse("/folder/file.csv"), CharacterEncoding.UTF_8));
    }

    @Test
    public void getFileContentLinesEncodingWithExistingEmptyFileAtRootedPath()
    {
        final FileSystem fileSystem = getFileSystem(creator);
        fileSystem.createFile("/folder/file.csv");

        test.assertEqual(new String[0], Array.toStringArray(fileSystem.getFileContentLines(Path.parse("/folder/file.csv"), CharacterEncoding.UTF_8)));
    }

    @Test
    public void getFileContentLinesEncodingWithExistingSingleLineFileAtRootedPath()
    {
        final FileSystem fileSystem = getFileSystem(creator);
        fileSystem.createFile("/folder/file.csv", "abcdef");

        test.assertEqual(new String[] { "abcdef" }, Array.toStringArray(fileSystem.getFileContentLines(Path.parse("/folder/file.csv"), CharacterEncoding.UTF_8)));
    }

    @Test
    public void getFileContentLinesEncodingWithExistingMultipleLineFileAtRootedPath()
    {
        final FileSystem fileSystem = getFileSystem(creator);
        fileSystem.createFile("/folder/file.csv", "ab\ncd\r\ne\nf\n");

        final String[] expected = new String[] { "ab\n", "cd\r\n", "e\n", "f\n" };
        final Path filePath = Path.parse("/folder/file.csv");
        final Iterable<String> fileLines = fileSystem.getFileContentLines(filePath, CharacterEncoding.UTF_8);
        final String[] actual = Array.toStringArray(fileLines);
        test.assertEqual(expected, actual);
    }

    @Test
    public void setFileContentsWithNullPathString()
    {
        final FileSystem fileSystem = getFileSystem(creator);
        test.assertFalse(fileSystem.setFileContents((String)null, new byte[] { 0, 1, 2 }));
    }

    @Test
    public void setFileContentsWithEmptyPathString()
    {
        final FileSystem fileSystem = getFileSystem(creator);
        test.assertFalse(fileSystem.setFileContents("", new byte[] { 0, 1, 2 }));
    }

    @Test
    public void setFileContentsWithRelativePathString()
    {
        final FileSystem fileSystem = getFileSystem(creator);
        test.assertFalse(fileSystem.setFileContents("relative.file", new byte[] { 0, 1, 2 }));
    }

    @Test
    public void setFileContentsWithNonExistingRootedPathStringAndNullContents()
    {
        final FileSystem fileSystem = getFileSystem(creator);
        test.assertTrue(fileSystem.setFileContents("/A.txt", (byte[])null));
        test.assertTrue(fileSystem.fileExists("/A.txt"));
        test.assertEqual(new byte[0], fileSystem.getFileContents("/A.txt"));
    }

    @Test
    public void setFileContentsWithExistingRootedPathStringAndNullContents()
    {
        final FileSystem fileSystem = getFileSystem(creator);
        fileSystem.createFile("/A.txt", new byte[] { 0, 1 });

        test.assertTrue(fileSystem.setFileContents("/A.txt", (byte[])null));
        test.assertTrue(fileSystem.fileExists("/A.txt"));
        test.assertEqual(new byte[0], fileSystem.getFileContents("/A.txt"));
    }

    @Test
    public void setFileContentsWithNonExistingRootedPathStringAndEmptyContents()
    {
        final FileSystem fileSystem = getFileSystem(creator);
        test.assertTrue(fileSystem.setFileContents("/A.txt", new byte[0]));
        test.assertTrue(fileSystem.fileExists("/A.txt"));
        test.assertEqual(new byte[0], fileSystem.getFileContents("/A.txt"));
    }

    @Test
    public void setFileContentsWithExistingRootedPathStringAndEmptyContents()
    {
        final FileSystem fileSystem = getFileSystem(creator);
        fileSystem.createFile("/A.txt", new byte[] { 0, 1 });

        test.assertTrue(fileSystem.setFileContents("/A.txt", new byte[0]));
        test.assertTrue(fileSystem.fileExists("/A.txt"));
        test.assertEqual(new byte[0], fileSystem.getFileContents("/A.txt"));
    }

    @Test
    public void setFileContentsWithNonExistingRootedPathStringAndNonEmptyContents()
    {
        final FileSystem fileSystem = getFileSystem(creator);
        test.assertTrue(fileSystem.setFileContents("/A.txt", new byte[] { 0, 1, 2 }));
        test.assertTrue(fileSystem.fileExists("/A.txt"));
        test.assertEqual(new byte[] { 0, 1, 2 }, fileSystem.getFileContents("/A.txt"));
    }

    @Test
    public void setFileContentsWithNonExistingRootedPathStringWithNonExistingParentFolderAndNonEmptyContents()
    {
        final FileSystem fileSystem = getFileSystem(creator);
        test.assertTrue(fileSystem.setFileContents("/folder/A.txt", new byte[] { 0, 1, 2 }));
        test.assertTrue(fileSystem.folderExists("/folder"));
        test.assertTrue(fileSystem.fileExists("/folder/A.txt"));
        test.assertEqual(new byte[] { 0, 1, 2 }, fileSystem.getFileContents("/folder/A.txt"));
    }

    @Test
    public void setFileContentsWithExistingRootedPathStringAndNonEmptyContents()
    {
        final FileSystem fileSystem = getFileSystem(creator);
        fileSystem.createFile("/A.txt");

        test.assertTrue(fileSystem.setFileContents("/A.txt", new byte[] { 0, 1, 2 }));
        test.assertTrue(fileSystem.fileExists("/A.txt"));
        test.assertEqual(new byte[] { 0, 1, 2 }, fileSystem.getFileContents("/A.txt"));
    }

    @Test
    public void setFileContentsWithNullPath()
    {
        final FileSystem fileSystem = getFileSystem(creator);
        test.assertFalse(fileSystem.setFileContents((Path)null, new byte[] { 0, 1, 2 }));
    }

    @Test
    public void setFileContentsWithEmptyPath()
    {
        final FileSystem fileSystem = getFileSystem(creator);
        test.assertFalse(fileSystem.setFileContents(Path.parse(""), new byte[] { 0, 1, 2 }));
    }

    @Test
    public void setFileContentsWithRelativePath()
    {
        final FileSystem fileSystem = getFileSystem(creator);
        test.assertFalse(fileSystem.setFileContents(Path.parse("relative.file"), new byte[] { 0, 1, 2 }));
    }

    @Test
    public void setFileContentsWithNonExistingRootedPathAndNullContents()
    {
        final FileSystem fileSystem = getFileSystem(creator);

        test.assertTrue(fileSystem.setFileContents(Path.parse("/A.txt"), (byte[])null));

        test.assertTrue(fileSystem.fileExists("/A.txt"));
        test.assertEqual(new byte[0], fileSystem.getFileContents("/A.txt"));
    }

    @Test
    public void setFileContentsWithExistingRootedPathAndNullContents()
    {
        final FileSystem fileSystem = getFileSystem(creator);
        fileSystem.createFile("/A.txt", new byte[] { 0, 1 });

        test.assertTrue(fileSystem.setFileContents(Path.parse("/A.txt"), (byte[])null));

        test.assertTrue(fileSystem.fileExists("/A.txt"));
        test.assertEqual(new byte[0], fileSystem.getFileContents("/A.txt"));
    }

    @Test
    public void setFileContentsWithNonExistingRootedPathAndEmptyContents()
    {
        final FileSystem fileSystem = getFileSystem(creator);

        test.assertTrue(fileSystem.setFileContents(Path.parse("/A.txt"), new byte[0]));

        test.assertTrue(fileSystem.fileExists("/A.txt"));
        test.assertEqual(new byte[0], fileSystem.getFileContents("/A.txt"));
    }

    @Test
    public void setFileContentsWithExistingRootedPathAndEmptyContents()
    {
        final FileSystem fileSystem = getFileSystem(creator);
        fileSystem.createFile("/A.txt", new byte[] { 0, 1 });

        test.assertTrue(fileSystem.setFileContents(Path.parse("/A.txt"), new byte[0]));

        test.assertTrue(fileSystem.fileExists("/A.txt"));
        test.assertEqual(new byte[0], fileSystem.getFileContents("/A.txt"));
    }

    @Test
    public void setFileContentsWithNonExistingRootedPathAndNonEmptyContents()
    {
        final FileSystem fileSystem = getFileSystem(creator);

        test.assertTrue(fileSystem.setFileContents(Path.parse("/A.txt"), new byte[] { 0, 1, 2 }));

        test.assertTrue(fileSystem.fileExists("/A.txt"));
        test.assertEqual(new byte[] { 0, 1, 2 }, fileSystem.getFileContents("/A.txt"));
    }

    @Test
    public void setFileContentsWithExistingRootedPathAndNonEmptyContents()
    {
        final FileSystem fileSystem = getFileSystem(creator);
        fileSystem.createFile("/A.txt");

        test.assertTrue(fileSystem.setFileContents(Path.parse("/A.txt"), new byte[] { 0, 1, 2 }));

        test.assertTrue(fileSystem.fileExists("/A.txt"));
        test.assertEqual(new byte[] { 0, 1, 2 }, fileSystem.getFileContents("/A.txt"));
    }

    @Test
    public void setFileContentsStringWithNullPathString()
    {
        final FileSystem fileSystem = getFileSystem(creator);
        test.assertFalse(fileSystem.setFileContents((String)null, "ABC"));
    }

    @Test
    public void setFileContentsStringWithEmptyPathString()
    {
        final FileSystem fileSystem = getFileSystem(creator);
        test.assertFalse(fileSystem.setFileContents("", "ABC"));
    }

    @Test
    public void setFileContentsStringWithRelativePathString()
    {
        final FileSystem fileSystem = getFileSystem(creator);
        test.assertFalse(fileSystem.setFileContents("relative.file", "ABC"));
    }

    @Test
    public void setFileContentsStringWithNonExistingRootedPathStringAndNullContents()
    {
        final FileSystem fileSystem = getFileSystem(creator);

        test.assertTrue(fileSystem.setFileContents("/A.txt", (String)null));

        test.assertTrue(fileSystem.fileExists("/A.txt"));
        test.assertEqual("", fileSystem.getFileContentsAsString("/A.txt"));
    }

    @Test
    public void setFileContentsStringWithExistingRootedPathStringAndNullContents()
    {
        final FileSystem fileSystem = getFileSystem(creator);
        fileSystem.createFile("/A.txt", new byte[] { 0, 1 });

        test.assertTrue(fileSystem.setFileContents("/A.txt", (String)null));

        test.assertTrue(fileSystem.fileExists("/A.txt"));
        test.assertEqual("", fileSystem.getFileContentsAsString("/A.txt"));
    }

    @Test
    public void setFileContentsStringWithNonExistingRootedPathStringAndEmptyContents()
    {
        final FileSystem fileSystem = getFileSystem(creator);

        test.assertTrue(fileSystem.setFileContents("/A.txt", ""));

        test.assertTrue(fileSystem.fileExists("/A.txt"));
        test.assertEqual("", fileSystem.getFileContentsAsString("/A.txt"));
    }

    @Test
    public void setFileContentsStringWithExistingRootedPathStringAndEmptyContents()
    {
        final FileSystem fileSystem = getFileSystem(creator);
        fileSystem.createFile("/A.txt", new byte[] { 0, 1 });

        test.assertTrue(fileSystem.setFileContents("/A.txt", ""));

        test.assertTrue(fileSystem.fileExists("/A.txt"));
        test.assertEqual("", fileSystem.getFileContentsAsString("/A.txt"));
    }

    @Test
    public void setFileContentsStringWithNonExistingRootedPathStringAndNonEmptyContents()
    {
        final FileSystem fileSystem = getFileSystem(creator);

        test.assertTrue(fileSystem.setFileContents("/A.txt", "ABC"));

        test.assertTrue(fileSystem.fileExists("/A.txt"));
        test.assertEqual("ABC", fileSystem.getFileContentsAsString("/A.txt"));
    }

    @Test
    public void setFileContentsStringWithNonExistingRootedPathStringWithNonExistingParentFolderAndNonEmptyContents()
    {
        final FileSystem fileSystem = getFileSystem(creator);

        test.assertTrue(fileSystem.setFileContents("/folder/A.txt", "ABC"));

        test.assertTrue(fileSystem.folderExists("/folder"));
        test.assertTrue(fileSystem.fileExists("/folder/A.txt"));
        test.assertEqual("ABC", fileSystem.getFileContentsAsString("/folder/A.txt"));
    }

    @Test
    public void setFileContentsStringWithExistingRootedPathStringAndNonEmptyContents()
    {
        final FileSystem fileSystem = getFileSystem(creator);
        fileSystem.createFile("/A.txt");

        test.assertTrue(fileSystem.setFileContents("/A.txt", "ABC"));

        test.assertTrue(fileSystem.fileExists("/A.txt"));
        test.assertEqual("ABC", fileSystem.getFileContentsAsString("/A.txt"));
    }

    @Test
    public void setFileContentsStringWithNullPath()
    {
        final FileSystem fileSystem = getFileSystem(creator);
        test.assertFalse(fileSystem.setFileContents((Path)null, "ABC"));
    }

    @Test
    public void setFileContentsStringWithEmptyPath()
    {
        final FileSystem fileSystem = getFileSystem(creator);
        test.assertFalse(fileSystem.setFileContents(Path.parse(""), "ABC"));
    }

    @Test
    public void setFileContentsStringWithRelativePath()
    {
        final FileSystem fileSystem = getFileSystem(creator);
        test.assertFalse(fileSystem.setFileContents(Path.parse("relative.file"), "ABC"));
    }

    @Test
    public void setFileContentsStringWithNonExistingRootedPathAndNullContents()
    {
        final FileSystem fileSystem = getFileSystem(creator);

        test.assertTrue(fileSystem.setFileContents(Path.parse("/A.txt"), (String)null));

        test.assertTrue(fileSystem.fileExists("/A.txt"));
        test.assertEqual("", fileSystem.getFileContentsAsString("/A.txt"));
    }

    @Test
    public void setFileContentsStringWithExistingRootedPathAndNullContents()
    {
        final FileSystem fileSystem = getFileSystem(creator);
        fileSystem.createFile("/A.txt", "Test");

        test.assertTrue(fileSystem.setFileContents(Path.parse("/A.txt"), (String)null));

        test.assertTrue(fileSystem.fileExists("/A.txt"));
        test.assertEqual("", fileSystem.getFileContentsAsString("/A.txt"));
    }

    @Test
    public void setFileContentsStringWithNonExistingRootedPathAndEmptyContents()
    {
        final FileSystem fileSystem = getFileSystem(creator);

        test.assertTrue(fileSystem.setFileContents(Path.parse("/A.txt"), ""));

        test.assertTrue(fileSystem.fileExists("/A.txt"));
        test.assertEqual("", fileSystem.getFileContentsAsString("/A.txt"));
    }

    @Test
    public void setFileContentsStringWithExistingRootedPathAndEmptyContents()
    {
        final FileSystem fileSystem = getFileSystem(creator);
        fileSystem.createFile("/A.txt", new byte[] { 0, 1 });

        test.assertTrue(fileSystem.setFileContents(Path.parse("/A.txt"), ""));

        test.assertTrue(fileSystem.fileExists("/A.txt"));
        test.assertEqual("", fileSystem.getFileContentsAsString("/A.txt"));
    }

    @Test
    public void setFileContentsStringWithNonExistingRootedPathAndNonEmptyContents()
    {
        final FileSystem fileSystem = getFileSystem(creator);

        test.assertTrue(fileSystem.setFileContents(Path.parse("/A.txt"), "ABC"));

        test.assertTrue(fileSystem.fileExists("/A.txt"));
        test.assertEqual("ABC", fileSystem.getFileContentsAsString("/A.txt"));
    }

    @Test
    public void setFileContentsStringWithExistingRootedPathAndNonEmptyContents()
    {
        final FileSystem fileSystem = getFileSystem(creator);
        fileSystem.createFile("/A.txt");

        test.assertTrue(fileSystem.setFileContents(Path.parse("/A.txt"), "ABC"));

        test.assertTrue(fileSystem.fileExists("/A.txt"));
        test.assertEqual("ABC", fileSystem.getFileContentsAsString("/A.txt"));
    }

    @Test
    public void getFilesAndFoldersRecursivelyWithNullPathString()
    {
        final FileSystem fileSystem = getFileSystem(creator);
        test.assertNull(fileSystem.getFilesAndFoldersRecursively((String)null));
    }

    @Test
    public void getFilesAndFoldersRecursivelyWithEmptyPathString()
    {
        final FileSystem fileSystem = getFileSystem(creator);
        test.assertNull(fileSystem.getFilesAndFoldersRecursively(""));
    }

    @Test
    public void getFilesAndFoldersRecursivelyWithRelativePathString()
    {
        final FileSystem fileSystem = getFileSystem(creator);
        test.assertNull(fileSystem.getFilesAndFoldersRecursively("test/folder"));
    }

    @Test
    public void getFilesAndFoldersRecursivelyWithRootedPathStringWhenRootDoesntExist()
    {
        final FileSystem fileSystem = getFileSystem(creator);
        test.assertNull(fileSystem.getFilesAndFoldersRecursively("F:/test/folder"));
    }

    @Test
    public void getFilesAndFoldersRecursivelyWithRootedPathStringWhenParentFolderDoesntExist()
    {
        final FileSystem fileSystem = getFileSystem(creator);
        test.assertNull(fileSystem.getFilesAndFoldersRecursively("/test/folder"));
    }

    @Test
    public void getFilesAndFoldersRecursivelyWithRootedPathStringWhenFolderDoesntExist()
    {
        final FileSystem fileSystem = getFileSystem(creator);
        fileSystem.createFolder("/test/");
        test.assertNull(fileSystem.getFilesAndFoldersRecursively("/test/folder"));
    }

    @Test
    public void getFilesAndFoldersRecursivelyWithRootedPathStringWhenFolderIsEmpty()
    {
        final FileSystem fileSystem = getFileSystem(creator);
        fileSystem.createFolder("/test/folder");
        test.assertEqual(new Array<FileSystemEntry>(0), fileSystem.getFilesAndFoldersRecursively("/test/folder"));
    }

    @Test
    public void getFilesAndFoldersRecursivelyWithRootedPathStringWhenFolderHasFiles()
    {
        final FileSystem fileSystem = getFileSystem(creator);
        fileSystem.createFolder("/test/folder");
        fileSystem.createFile("/test/folder/1.txt");
        fileSystem.createFile("/test/folder/2.txt");
        test.assertEqual(
            Array.fromValues(
                fileSystem.getFile("/test/folder/1.txt"),
                fileSystem.getFile("/test/folder/2.txt")),
            fileSystem.getFilesAndFoldersRecursively("/test/folder"));
    }

    @Test
    public void getFilesAndFoldersRecursivelyWithRootedPathStringWhenFolderHasFolders()
    {
        final FileSystem fileSystem = getFileSystem(creator);
        fileSystem.createFolder("/test/folder");
        fileSystem.createFolder("/test/folder/1.txt");
        fileSystem.createFolder("/test/folder/2.txt");
        test.assertEqual(
            Array.fromValues(
                fileSystem.getFolder("/test/folder/1.txt"),
                fileSystem.getFolder("/test/folder/2.txt")),
            fileSystem.getFilesAndFoldersRecursively("/test/folder"));
    }

    @Test
    public void getFilesAndFoldersRecursivelyWithRootedPathStringWhenFolderHasGrandchildFilesAndFolders()
    {
        final FileSystem fileSystem = getFileSystem(creator);
        fileSystem.createFile("/test/folder/1.txt");
        fileSystem.createFile("/test/folder/2.txt");
        fileSystem.createFile("/test/folder/A/3.csv");
        fileSystem.createFile("/test/folder/B/C/4.xml");
        fileSystem.createFile("/test/folder/A/5.png");

        final Iterable<FileSystemEntry> expectedEntries =
            Array.fromValues(
                fileSystem.getFolder("/test/folder/A"),
                fileSystem.getFolder("/test/folder/B"),
                fileSystem.getFile("/test/folder/1.txt"),
                fileSystem.getFile("/test/folder/2.txt"),
                fileSystem.getFile("/test/folder/A/3.csv"),
                fileSystem.getFile("/test/folder/A/5.png"),
                fileSystem.getFolder("/test/folder/B/C"),
                fileSystem.getFile("/test/folder/B/C/4.xml"));
        final Iterable<FileSystemEntry> actualEntries = fileSystem.getFilesAndFoldersRecursively("/test/folder");
        test.assertEqual(expectedEntries, actualEntries);
    }

    private static void asyncTest(final Action1<FileSystem> action)
    {
        final Synchronization synchronization = new Synchronization();
        CurrentThreadAsyncRunner.withRegistered(synchronization, new Action1<CurrentThreadAsyncRunner>()
        {
            @Override
            public void run(CurrentThreadAsyncRunner mainRunner)
            {
                final CurrentThreadAsyncRunner backgroundRunner = new CurrentThreadAsyncRunner(synchronization);
                final FileSystem fileSystem = getFileSystem(backgroundRunner);
                
                action.run(fileSystem);
                test.assertEqual(0, mainRunner.getScheduledTaskCount());
                test.assertEqual(1, backgroundRunner.getScheduledTaskCount());

                backgroundRunner.await();
                test.assertEqual(1, mainRunner.getScheduledTaskCount());
                test.assertEqual(0, backgroundRunner.getScheduledTaskCount());

                mainRunner.await();
                test.assertEqual(0, mainRunner.getScheduledTaskCount());
                test.assertEqual(0, backgroundRunner.getScheduledTaskCount());
            }
        });
    }
}
