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
                            public void run(final Test test)
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
                        });

                        runner.test("with existing file path", new Action1<Test>()
                        {
                            @Override
                            public void run(final Test test)
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
                        });
                    }
                });

                runner.testGroup("fileExistsAsync(Path)", new Action0()
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
                        });

                        runner.test("with existing file path", new Action1<Test>()
                        {
                            @Override
                            public void run(final Test test)
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
                                final FileSystem fileSystem = getFileSystem(creator);
                                test.assertFalse(fileSystem.createFile((String)null));
                            }
                        });

                        runner.test("with empty path", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                test.assertFalse(fileSystem.createFile(""));
                            }
                        });

                        runner.test("with relative path", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                test.assertFalse(fileSystem.createFile("things.txt"));
                            }
                        });

                        runner.test("with non-existing rooted path", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);

                                test.assertTrue(fileSystem.createFile("/things.txt"));

                                test.assertTrue(fileSystem.fileExists("/things.txt"));
                                test.assertEqual(new byte[0], fileSystem.getFileContents("/things.txt"));
                            }
                        });

                        runner.test("with existing rooted path", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                fileSystem.createFile("/things.txt");

                                test.assertFalse(fileSystem.createFile("/things.txt"));

                                test.assertTrue(fileSystem.fileExists("/things.txt"));
                                test.assertEqual(new byte[0], fileSystem.getFileContents("/things.txt"));
                            }
                        });

                        runner.test("with non-existing rooted path and content", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);

                                test.assertTrue(fileSystem.createFile("/things.txt", new byte[] { 0, 1, 2, 3 }));

                                test.assertTrue(fileSystem.fileExists("/things.txt"));
                                test.assertEqual(new byte[] { 0, 1, 2, 3 }, fileSystem.getFileContents("/things.txt"));
                            }
                        });

                        runner.test("with existing rooted path and byte[] contents", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                fileSystem.createFile("/things.txt");

                                test.assertFalse(fileSystem.createFile("/things.txt", new byte[] { 0, 1, 2, 3 }));

                                test.assertTrue(fileSystem.fileExists("/things.txt"));
                                test.assertEqual(new byte[0], fileSystem.getFileContents("/things.txt"));
                            }
                        });

                        runner.test("with existing rooted path and String contents", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                fileSystem.createFile("/things.txt");

                                test.assertFalse(fileSystem.createFile("/things.txt", "ABC"));

                                test.assertTrue(fileSystem.fileExists("/things.txt"));
                                test.assertEqual(new byte[0], fileSystem.getFileContents("/things.txt"));
                            }
                        });

                        runner.test("with existing rooted path, String contents, and encoding", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                fileSystem.createFile("/things.txt");

                                test.assertFalse(fileSystem.createFile("/things.txt", "ABC", CharacterEncoding.UTF_8));

                                test.assertTrue(fileSystem.fileExists("/things.txt"));
                                test.assertEqual(new byte[0], fileSystem.getFileContents("/things.txt"));
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
                                final FileSystem fileSystem = getFileSystem(creator);
                                test.assertFalse(fileSystem.createFile((String)null, (Out<File>)null));
                            }
                        });

                        runner.test("with empty path and null value", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                test.assertFalse(fileSystem.createFile("", (Out<File>)null));
                            }
                        });

                        runner.test("with relative path and null value", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                test.assertFalse(fileSystem.createFile("things.txt", (Out<File>)null));
                            }
                        });

                        runner.test("with non-existing rooted path and null value", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                test.assertTrue(fileSystem.createFile("/things.txt", (Out<File>)null));
                                test.assertTrue(fileSystem.fileExists("/things.txt"));
                                test.assertEqual(new byte[0], fileSystem.getFileContents("/things.txt"));
                            }
                        });

                        runner.test("with non-existing rooted path, byte[] contents, and null value", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                test.assertTrue(fileSystem.createFile("/things.txt", new byte[] { 10, 11, 12 }, null));
                                test.assertTrue(fileSystem.fileExists("/things.txt"));
                                test.assertEqual(new byte[] { 10, 11, 12 }, fileSystem.getFileContents("/things.txt"));
                            }
                        });

                        runner.test("with non-existing rooted path, String contents, and null value", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);

                                test.assertTrue(fileSystem.createFile("/things.txt", "ABC", (Out<File>)null));

                                test.assertTrue(fileSystem.fileExists("/things.txt"));
                                test.assertEqual("ABC", fileSystem.getFileContentsAsString("/things.txt"));
                            }
                        });

                        runner.test("with non-existing rooted path, String contents, encoding, and null value", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);

                                test.assertTrue(fileSystem.createFile("/things.txt", "ABC", CharacterEncoding.UTF_8, null));

                                test.assertTrue(fileSystem.fileExists("/things.txt"));
                                test.assertEqual("ABC", fileSystem.getFileContentsAsString("/things.txt"));
                            }
                        });

                        runner.test("with existing rooted path and null value", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                fileSystem.createFile("/things.txt");

                                test.assertFalse(fileSystem.createFile("/things.txt", (Out<File>)null));

                                test.assertTrue(fileSystem.fileExists("/things.txt"));
                                test.assertEqual(new byte[0], fileSystem.getFileContents("/things.txt"));
                            }
                        });

                        runner.test("with existing rooted path, byte[] contents, and null value", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                fileSystem.createFile("/things.txt");

                                test.assertFalse(fileSystem.createFile("/things.txt", new byte[] { 0, 1 }, null));

                                test.assertTrue(fileSystem.fileExists("/things.txt"));
                                test.assertEqual(new byte[0], fileSystem.getFileContents("/things.txt"));
                            }
                        });

                        runner.test("with null path and non-null value", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                final Value<File> file = new Value<>();
                                test.assertFalse(fileSystem.createFile((String)null, file));
                            }
                        });

                        runner.test("with empty path and non-null value", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                final Value<File> file = new Value<>();
                                test.assertFalse(fileSystem.createFile("", file));
                            }
                        });

                        runner.test("with relative path and non-null value", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                final Value<File> file = new Value<>();
                                final boolean fileCreated = fileSystem.createFile("things.txt", file);
                                test.assertFalse(fileCreated);
                                test.assertFalse(file.hasValue());
                            }
                        });

                        runner.test("with non-existing rooted path and non-null value", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                final Value<File> file = new Value<>();
                                test.assertTrue(fileSystem.createFile("/things.txt", file));
                                test.assertTrue(file.hasValue());
                                test.assertNotNull(file.get());
                                test.assertEqual("/things.txt", file.get().getPath().toString());
                            }
                        });

                        runner.test("with existing rooted path and non-null value", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                fileSystem.createFile("/things.txt");

                                final Value<File> file = new Value<>();
                                test.assertFalse(fileSystem.createFile("/things.txt", file));
                                test.assertTrue(file.hasValue());
                                test.assertEqual(Path.parse("/things.txt"), file.get().getPath());
                            }
                        });
                    }
                });

                runner.testGroup("createFile(Path)", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("with null path and byte[] contents", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                test.assertFalse(fileSystem.createFile((Path)null, new byte[] { 0, 1, 2 }));
                            }
                        });

                        runner.test("with null path and String contents", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                test.assertFalse(fileSystem.createFile((Path)null, "ABC"));
                            }
                        });

                        runner.test("with null path, String contents, and encoding", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                test.assertFalse(fileSystem.createFile((Path)null, "ABC", CharacterEncoding.UTF_8));
                            }
                        });

                        runner.test("with invalid path", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                final Value<File> file = new Value<>();
                                final boolean fileCreated = fileSystem.createFile("/\u0000?#!.txt", file);
                                test.assertFalse(fileCreated, "Wrong fileCreated");
                                test.assertFalse(file.hasValue(), "Wrong file.hasValue()");
                                test.assertNull(file.get(), "Wrong file.get()");
                            }
                        });
                    }
                });

                runner.testGroup("createFileAsync(String)", new Action0()
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
                        });

                        runner.test("with non-existing rooted path", new Action1<Test>()
                        {
                            @Override
                            public void run(final Test test)
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
                        });

                        runner.test("with existing rooted path", new Action1<Test>()
                        {
                            @Override
                            public void run(final Test test)
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
                        });
                    }
                });

                runner.testGroup("createFileAsync(String,Value<File>)", new Action0()
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
                        });

                        runner.test("with non-existing rooted path and null value", new Action1<Test>()
                        {
                            @Override
                            public void run(final Test test)
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
                        });

                        runner.test("with existing rooted path and null value", new Action1<Test>()
                        {
                            @Override
                            public void run(final Test test)
                            {
                                asyncTest(new Action1<FileSystem>()
                                {
                                    @Override
                                    public void run(final FileSystem fileSystem)
                                    {
                                        fileSystem.createFile("/things.txt");

                                        fileSystem.createFileAsync("/things.txt", null)
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
                        });

                        runner.test("with empty path and non-null value", new Action1<Test>()
                        {
                            @Override
                            public void run(final Test test)
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
                        });

                        runner.test("with relative path and non-null value", new Action1<Test>()
                        {
                            @Override
                            public void run(final Test test)
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
                        });

                        runner.test("with non-existing rooted path and non-null value", new Action1<Test>()
                        {
                            @Override
                            public void run(final Test test)
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
                        });

                        runner.test("with existing rooted path and non-null value", new Action1<Test>()
                        {
                            @Override
                            public void run(final Test test)
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
                        });

                        runner.test("with invalid path", new Action1<Test>()
                        {
                            @Override
                            public void run(final Test test)
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
                        });
                    }
                });

                runner.testGroup("createFileAsync(Path)", new Action0()
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
                        });

                        runner.test("with non-existing rooted path", new Action1<Test>()
                        {
                            @Override
                            public void run(final Test test)
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
                        });

                        runner.test("with existing rooted path", new Action1<Test>()
                        {
                            @Override
                            public void run(final Test test)
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
                        });
                    }
                });

                runner.testGroup("createFileAsync(Path,Value<File>)", new Action0()
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
                        });

                        runner.test("with non-existing rooted path and null value", new Action1<Test>()
                        {
                            @Override
                            public void run(final Test test)
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
                        });

                        runner.test("with existing rooted path and null value", new Action1<Test>()
                        {
                            @Override
                            public void run(final Test test)
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
                        });

                        runner.test("with empty path and non-null value", new Action1<Test>()
                        {
                            @Override
                            public void run(final Test test)
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
                        });

                        runner.test("with relative path and non-null value", new Action1<Test>()
                        {
                            @Override
                            public void run(final Test test)
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
                        });

                        runner.test("with non-existing rooted path and non-null value", new Action1<Test>()
                        {
                            @Override
                            public void run(final Test test)
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
                        });

                        runner.test("with existing rooted path and non-null value", new Action1<Test>()
                        {
                            @Override
                            public void run(final Test test)
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
                        });

                        runner.test("with invalid path", new Action1<Test>()
                        {
                            @Override
                            public void run(final Test test)
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
                        });
                    }
                });

                runner.testGroup("deleteFile(String)", new Action0()
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
                                test.assertFalse(fileSystem.deleteFile((String)null));
                            }
                        });

                        runner.test("with empty path", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                test.assertFalse(fileSystem.deleteFile(""));
                            }
                        });

                        runner.test("with relative path", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                test.assertFalse(fileSystem.deleteFile("relativeFile.txt"));
                            }
                        });

                        runner.test("with non-existing rooted path", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                test.assertFalse(fileSystem.deleteFile("/idontexist.txt"));
                            }
                        });

                        runner.test("with existing rooted path", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                fileSystem.createFile("/iexist.txt");

                                test.assertTrue(fileSystem.deleteFile("/iexist.txt"));
                                test.assertFalse(fileSystem.fileExists("/iexist.txt"));

                                test.assertFalse(fileSystem.deleteFile("/iexist.txt"));
                                test.assertFalse(fileSystem.fileExists("/iexist.txt"));
                            }
                        });
                    }
                });

                runner.testGroup("deleteFileAsync(String)", new Action0()
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
                        });

                        runner.test("with non-existing rooted path", new Action1<Test>()
                        {
                            @Override
                            public void run(final Test test)
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
                        });

                        runner.test("with existing rooted path", new Action1<Test>()
                        {
                            @Override
                            public void run(final Test test)
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
                        });
                    }
                });

                runner.testGroup("deleteFileAsync(Path)", new Action0()
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
                        });

                        runner.test("with non-existing rooted path", new Action1<Test>()
                        {
                            @Override
                            public void run(final Test test)
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
                        });

                        runner.test("with existing rooted path", new Action1<Test>()
                        {
                            @Override
                            public void run(final Test test)
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
                        });
                    }
                });

                runner.testGroup("getFileContents(String)", new Action0()
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
                                test.assertNull(fileSystem.getFileContents((String)null));
                            }
                        });

                        runner.test("with empty path", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                test.assertNull(fileSystem.getFileContents(""));
                            }
                        });

                        runner.test("with relative path", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                test.assertNull(fileSystem.getFileContents("thing.txt"));
                            }
                        });

                        runner.test("with non-existing rooted path", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                test.assertNull(fileSystem.getFileContents("/thing.txt"));
                            }
                        });

                        runner.test("with existing rooted path with no contents", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                fileSystem.createFile("/thing.txt");
                                test.assertEqual(new byte[0], fileSystem.getFileContents("/thing.txt"));
                            }
                        });
                    }
                });

                runner.testGroup("getFileContents(Path)", new Action0()
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
                                test.assertNull(fileSystem.getFileContents((Path)null));
                            }
                        });

                        runner.test("with empty path", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                test.assertNull(fileSystem.getFileContents(Path.parse("")));
                            }
                        });

                        runner.test("with relative path", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                test.assertNull(fileSystem.getFileContents(Path.parse("thing.txt")));
                            }
                        });

                        runner.test("with non-existing rooted path", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                test.assertNull(fileSystem.getFileContents(Path.parse("/thing.txt")));
                            }
                        });

                        runner.test("with existing rooted path with no contents", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                fileSystem.createFile("/thing.txt");
                                test.assertEqual(new byte[0], fileSystem.getFileContents(Path.parse("/thing.txt")));
                            }
                        });
                    }
                });

                runner.testGroup("getFileContentsAsString(String)", new Action0()
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
                                test.assertNull(fileSystem.getFileContentsAsString((String)null));
                            }
                        });

                        runner.test("with empty path", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                test.assertNull(fileSystem.getFileContentsAsString(""));
                            }
                        });

                        runner.test("with relative path", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                test.assertNull(fileSystem.getFileContentsAsString("file.txt"));
                            }
                        });

                        runner.test("with non-existing rooted path", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                test.assertNull(fileSystem.getFileContentsAsString("/file.txt"));
                            }
                        });

                        runner.test("with existing rooted path with no contents", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                fileSystem.createFile("/file.txt");
                                test.assertEqual("", fileSystem.getFileContentsAsString("/file.txt"));
                            }
                        });

                        runner.test("with existing rooted path with contents", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                fileSystem.createFile("/file.txt", CharacterEncoding.UTF_8.encode("Hello there!"));
                                test.assertEqual("Hello there!", fileSystem.getFileContentsAsString("/file.txt"));
                            }
                        });
                    }
                });

                runner.testGroup("getFileContentsAsString(String,CharacterEncoding)", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("with null path and null encoding", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                test.assertNull(fileSystem.getFileContentsAsString((String)null, null));
                            }
                        });

                        runner.test("with empty path and null encoding", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                test.assertNull(fileSystem.getFileContentsAsString("", null));
                            }
                        });

                        runner.test("with relative path and null encoding", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                test.assertNull(fileSystem.getFileContentsAsString("file.txt", null));
                            }
                        });

                        runner.test("with non-existing rooted path and null encoding", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                test.assertNull(fileSystem.getFileContentsAsString("/file.txt", null));
                            }
                        });

                        runner.test("with existing rooted path with no contents and null encoding", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                fileSystem.createFile("/file.txt");
                                test.assertNull(fileSystem.getFileContentsAsString("/file.txt", null));
                            }
                        });

                        runner.test("with existing rooted path with contents and null encoding", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                fileSystem.createFile("/file.txt", CharacterEncoding.UTF_8.encode("Hello there!"));
                                test.assertNull(fileSystem.getFileContentsAsString("/file.txt", null));
                            }
                        });
                    }
                });

                runner.testGroup("getFileContentByteReadStream(String)", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("with non-existing file", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                test.assertNull(fileSystem.getFileContentByteReadStream("C:/i/dont/exist.txt"));
                            }
                        });
                    }
                });

                runner.testGroup("getFileContentCharacterReadStream(String)", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("with non-existing file", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                test.assertNull(fileSystem.getFileContentCharacterReadStream("C:/i/dont/exist.txt"));
                            }
                        });
                    }
                });

                runner.testGroup("getFileContentBlocks(String)", new Action0()
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
                                test.assertNull(fileSystem.getFileContentBlocks((String)null));
                            }
                        });

                        runner.test("with empty path", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                test.assertNull(fileSystem.getFileContentBlocks(""));
                            }
                        });

                        runner.test("with relative path", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                test.assertNull(fileSystem.getFileContentBlocks("B"));
                            }
                        });

                        runner.test("with non-existing rooted path", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                test.assertNull(fileSystem.getFileContentBlocks("/a.txt"));
                            }
                        });

                        runner.test("with existing rooted path with no contents", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                fileSystem.createFile("/a.txt");

                                final Iterable<byte[]> fileContentBlocks = fileSystem.getFileContentBlocks("/a.txt");
                                test.assertNotNull(fileContentBlocks);
                                test.assertEqual(0, fileContentBlocks.getCount());
                            }
                        });

                        runner.test("with existing rooted path with contents", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                fileSystem.createFile("/a.txt", new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11});

                                final Iterable<byte[]> fileContentBlocks = fileSystem.getFileContentBlocks("/a.txt");
                                test.assertNotNull(fileContentBlocks);

                                final byte[] fileContents = Array.merge(fileContentBlocks);
                                test.assertEqual(new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11 }, fileContents);
                            }
                        });
                    }
                });

                runner.testGroup("getFileContentLines(String)", new Action0()
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
                                test.assertNull(fileSystem.getFileContentLines((String)null));
                            }
                        });

                        runner.test("with empty path", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                test.assertNull(fileSystem.getFileContentLines(""));
                            }
                        });

                        runner.test("with relative path", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                test.assertNull(fileSystem.getFileContentLines("relative/file.txt"));
                            }
                        });

                        runner.test("with non-existing rooted path", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                test.assertNull(fileSystem.getFileContentLines("/folder/file.csv"));
                            }
                        });

                        runner.test("with existing rooted path with no contents", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                fileSystem.createFile("/folder/file.csv");
                                test.assertEqual(new String[0], Array.toStringArray(fileSystem.getFileContentLines("/folder/file.csv")));
                            }
                        });

                        runner.test("with existing rooted path with single line", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                fileSystem.createFile("/folder/file.csv", "abcdef");
                                test.assertEqual(new String[] { "abcdef" }, Array.toStringArray(fileSystem.getFileContentLines("/folder/file.csv")));
                            }
                        });

                        runner.test("with existing rooted path with multiple lines", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                fileSystem.createFile("/folder/file.csv", "ab\ncd\r\ne\nf\n");
                                test.assertEqual(new String[] { "ab\n", "cd\r\n", "e\n", "f\n" }, Array.toStringArray(fileSystem.getFileContentLines("/folder/file.csv")));
                            }
                        });
                    }
                });

                runner.testGroup("getFileContentLines(Path)", new Action0()
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
                                test.assertNull(fileSystem.getFileContentLines((Path)null));
                            }
                        });

                        runner.test("with empty path", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                test.assertNull(fileSystem.getFileContentLines(Path.parse("")));
                            }
                        });

                        runner.test("with relative path", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                test.assertNull(fileSystem.getFileContentLines(Path.parse("relative/file.txt")));
                            }
                        });

                        runner.test("with non-existing rooted path", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                test.assertNull(fileSystem.getFileContentLines(Path.parse("/folder/file.csv")));
                            }
                        });

                        runner.test("with existing rooted path with no content", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                fileSystem.createFile("/folder/file.csv");
                                test.assertEqual(new String[0], Array.toStringArray(fileSystem.getFileContentLines(Path.parse("/folder/file.csv"))));
                            }
                        });

                        runner.test("with existing rooted path with single line", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                fileSystem.createFile("/folder/file.csv", "abcdef");
                                test.assertEqual(new String[] { "abcdef" }, Array.toStringArray(fileSystem.getFileContentLines(Path.parse("/folder/file.csv"))));
                            }
                        });

                        runner.test("with existing rooted path with multiple lines", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                fileSystem.createFile("/folder/file.csv", "ab\ncd\r\ne\nf\n");
                                test.assertEqual(new String[] { "ab\n", "cd\r\n", "e\n", "f\n" }, Array.toStringArray(fileSystem.getFileContentLines(Path.parse("/folder/file.csv"))));
                            }
                        });
                    }
                });

                runner.testGroup("getFileContentLines(String,boolean)", new Action0()
                {
                   @Override
                   public void run()
                   {
                       runner.test("with null path and include new lines", new Action1<Test>()
                       {
                           @Override
                           public void run(Test test)
                           {
                               final FileSystem fileSystem = getFileSystem(creator);
                               test.assertNull(fileSystem.getFileContentLines((String)null, true));
                           }
                       });

                       runner.test("with empty path and include new lines", new Action1<Test>()
                       {
                           @Override
                           public void run(Test test)
                           {
                               final FileSystem fileSystem = getFileSystem(creator);
                               test.assertNull(fileSystem.getFileContentLines("", true));
                           }
                       });

                       runner.test("with relative path and include new lines", new Action1<Test>()
                       {
                           @Override
                           public void run(Test test)
                           {
                               final FileSystem fileSystem = getFileSystem(creator);
                               test.assertNull(fileSystem.getFileContentLines("relative/file.txt", true));
                           }
                       });

                       runner.test("with non-existing rooted path and include new lines", new Action1<Test>()
                       {
                           @Override
                           public void run(Test test)
                           {
                               final FileSystem fileSystem = getFileSystem(creator);
                               test.assertNull(fileSystem.getFileContentLines("/folder/file.csv", true));
                           }
                       });

                       runner.test("with existing rooted path with no contents and include new lines", new Action1<Test>()
                       {
                           @Override
                           public void run(Test test)
                           {
                               final FileSystem fileSystem = getFileSystem(creator);
                               fileSystem.createFile("/folder/file.csv");
                               test.assertEqual(new String[0], Array.toStringArray(fileSystem.getFileContentLines("/folder/file.csv", true)));
                           }
                       });

                       runner.test("with existing rooted path with single line and include new lines", new Action1<Test>()
                       {
                           @Override
                           public void run(Test test)
                           {
                               final FileSystem fileSystem = getFileSystem(creator);
                               fileSystem.createFile("/folder/file.csv", "abcdef");
                               test.assertEqual(new String[] { "abcdef" }, Array.toStringArray(fileSystem.getFileContentLines("/folder/file.csv", true)));
                           }
                       });

                       runner.test("with existing rooted path with multiple lines and include new lines", new Action1<Test>()
                       {
                           @Override
                           public void run(Test test)
                           {
                               final FileSystem fileSystem = getFileSystem(creator);
                               fileSystem.createFile("/folder/file.csv", "ab\ncd\r\ne\nf\n");
                               test.assertEqual(new String[] { "ab\n", "cd\r\n", "e\n", "f\n" }, Array.toStringArray(fileSystem.getFileContentLines("/folder/file.csv", true)));
                           }
                       });

                       runner.test("with null path and don't include new lines", new Action1<Test>()
                       {
                           @Override
                           public void run(Test test)
                           {
                               final FileSystem fileSystem = getFileSystem(creator);
                               test.assertNull(fileSystem.getFileContentLines((String)null, false));
                           }
                       });

                       runner.test("with empty path and don't include new lines", new Action1<Test>()
                       {
                           @Override
                           public void run(Test test)
                           {
                               final FileSystem fileSystem = getFileSystem(creator);
                               test.assertNull(fileSystem.getFileContentLines("", false));
                           }
                       });

                       runner.test("with relative path and don't include new lines", new Action1<Test>()
                       {
                           @Override
                           public void run(Test test)
                           {
                               final FileSystem fileSystem = getFileSystem(creator);
                               test.assertNull(fileSystem.getFileContentLines("relative/file.txt", false));
                           }
                       });

                       runner.test("with non-existing rooted path and don't include new lines", new Action1<Test>()
                       {
                           @Override
                           public void run(Test test)
                           {
                               final FileSystem fileSystem = getFileSystem(creator);
                               test.assertNull(fileSystem.getFileContentLines("/folder/file.csv", false));
                           }
                       });

                       runner.test("with existing rooted path with no contents and don't include new lines", new Action1<Test>()
                       {
                           @Override
                           public void run(Test test)
                           {
                               final FileSystem fileSystem = getFileSystem(creator);
                               fileSystem.createFile("/folder/file.csv");

                               test.assertEqual(new String[0], Array.toStringArray(fileSystem.getFileContentLines("/folder/file.csv", false)));
                           }
                       });

                       runner.test("with existing rooted path with single line and don't include new lines", new Action1<Test>()
                       {
                           @Override
                           public void run(Test test)
                           {
                               final FileSystem fileSystem = getFileSystem(creator);
                               fileSystem.createFile("/folder/file.csv", "abcdef");
                               test.assertEqual(new String[] { "abcdef" }, Array.toStringArray(fileSystem.getFileContentLines("/folder/file.csv", false)));
                           }
                       });

                       runner.test("with existing rooted path with multiple lines and don't include new lines", new Action1<Test>()
                       {
                           @Override
                           public void run(Test test)
                           {
                               final FileSystem fileSystem = getFileSystem(creator);
                               fileSystem.createFile("/folder/file.csv", "ab\ncd\r\ne\nf\n");
                               test.assertEqual(new String[] { "ab", "cd", "e", "f" }, Array.toStringArray(fileSystem.getFileContentLines("/folder/file.csv", false)));
                           }
                       });
                   }
                });

                runner.testGroup("getFileContentLines(String,boolean,CharacterEncoding)", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("with existing rooted path with multiple lines, character encoding, and don't include new lines", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                fileSystem.createFile("/folder/file.csv", "ab\ncd\r\ne\nf\n");
                                test.assertEqual(new String[] { "ab", "cd", "e", "f" }, Array.toStringArray(fileSystem.getFileContentLines("/folder/file.csv", false, CharacterEncoding.UTF_8)));
                            }
                        });
                    }
                });

                runner.testGroup("getFileContentLines(Path,boolean)", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("with null path and include new lines", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                test.assertNull(fileSystem.getFileContentLines((Path)null, true));
                            }
                        });

                        runner.test("with empty path and include new lines", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                test.assertNull(fileSystem.getFileContentLines(Path.parse(""), true));
                            }
                        });

                        runner.test("with relative path and include new lines", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                test.assertNull(fileSystem.getFileContentLines(Path.parse("relative/file.txt"), true));
                            }
                        });

                        runner.test("with non-existing rooted path and include new lines", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                test.assertNull(fileSystem.getFileContentLines(Path.parse("/folder/file.csv"), true));
                            }
                        });

                        runner.test("with existing rooted path with no contents and include new lines", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                fileSystem.createFile("/folder/file.csv");
                                test.assertEqual(new String[0], Array.toStringArray(fileSystem.getFileContentLines(Path.parse("/folder/file.csv"), true)));
                            }
                        });

                        runner.test("with existing rooted path with single line and include new lines", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                fileSystem.createFile("/folder/file.csv", "abcdef");
                                test.assertEqual(new String[] { "abcdef" }, Array.toStringArray(fileSystem.getFileContentLines(Path.parse("/folder/file.csv"), true)));
                            }
                        });

                        runner.test("with existing rooted path with multiple lines and include new lines", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                fileSystem.createFile("/folder/file.csv", "ab\ncd\r\ne\nf\n");
                                test.assertEqual(new String[] { "ab\n", "cd\r\n", "e\n", "f\n" }, Array.toStringArray(fileSystem.getFileContentLines(Path.parse("/folder/file.csv"), true)));
                            }
                        });

                        runner.test("with null path and don't include new lines", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                test.assertNull(fileSystem.getFileContentLines((Path)null, false));
                            }
                        });

                        runner.test("with empty path and don't include new lines", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                test.assertNull(fileSystem.getFileContentLines(Path.parse(""), false));
                            }
                        });

                        runner.test("with relative path and don't include new lines", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                test.assertNull(fileSystem.getFileContentLines(Path.parse("relative/file.txt"), false));
                            }
                        });

                        runner.test("with non-existing rooted path and don't include new lines", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                test.assertNull(fileSystem.getFileContentLines(Path.parse("/folder/file.csv"), false));
                            }
                        });

                        runner.test("with existing rooted path with no contents and don't include new lines", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                fileSystem.createFile("/folder/file.csv");
                                test.assertEqual(new String[0], Array.toStringArray(fileSystem.getFileContentLines(Path.parse("/folder/file.csv"), false)));
                            }
                        });

                        runner.test("with existing rooted path with single line and don't include new lines", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                fileSystem.createFile("/folder/file.csv", "abcdef");
                                test.assertEqual(new String[] { "abcdef" }, Array.toStringArray(fileSystem.getFileContentLines(Path.parse("/folder/file.csv"), false)));
                            }
                        });

                        runner.test("with existing rooted path with multiple lines and don't include new lines", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                fileSystem.createFile("/folder/file.csv", "ab\ncd\r\ne\nf\n");
                                test.assertEqual(new String[] { "ab", "cd", "e", "f" }, Array.toStringArray(fileSystem.getFileContentLines(Path.parse("/folder/file.csv"), false)));
                            }
                        });
                    }
                });

                runner.testGroup("getFileContentLines(String,CharacterEncoding)", new Action0()
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
                                test.assertNull(fileSystem.getFileContentLines((String)null, CharacterEncoding.UTF_8));
                            }
                        });

                        runner.test("with empty path", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                test.assertNull(fileSystem.getFileContentLines("", CharacterEncoding.UTF_8));
                            }
                        });

                        runner.test("with relative path", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                test.assertNull(fileSystem.getFileContentLines("relative/file.txt", CharacterEncoding.UTF_8));
                            }
                        });

                        runner.test("with non-existing rooted path", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                test.assertNull(fileSystem.getFileContentLines("/folder/file.csv", CharacterEncoding.UTF_8));
                            }
                        });

                        runner.test("with existing rooted path with no contents", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                fileSystem.createFile("/folder/file.csv");
                                test.assertEqual(new String[0], Array.toStringArray(fileSystem.getFileContentLines("/folder/file.csv", CharacterEncoding.UTF_8)));
                            }
                        });

                        runner.test("with existing rooted path with single line", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                fileSystem.createFile("/folder/file.csv", "abcdef");
                                test.assertEqual(new String[] { "abcdef" }, Array.toStringArray(fileSystem.getFileContentLines("/folder/file.csv", CharacterEncoding.UTF_8)));
                            }
                        });

                        runner.test("with existing rooted path with multiple lines", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                fileSystem.createFile("/folder/file.csv", "ab\ncd\r\ne\nf\n");
                                test.assertEqual(new String[] { "ab\n", "cd\r\n", "e\n", "f\n" }, Array.toStringArray(fileSystem.getFileContentLines("/folder/file.csv", CharacterEncoding.UTF_8)));
                            }
                        });
                    }
                });

                runner.testGroup("getFileContentLines(Path,CharacterEncoding)", new Action0()
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
                                test.assertNull(fileSystem.getFileContentLines((Path)null, CharacterEncoding.UTF_8));
                            }
                        });

                        runner.test("with empty path", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                test.assertNull(fileSystem.getFileContentLines(Path.parse(""), CharacterEncoding.UTF_8));
                            }
                        });

                        runner.test("with relative path", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                test.assertNull(fileSystem.getFileContentLines(Path.parse("relative/file.txt"), CharacterEncoding.UTF_8));
                            }
                        });

                        runner.test("with non-existing rooted path", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                test.assertNull(fileSystem.getFileContentLines(Path.parse("/folder/file.csv"), CharacterEncoding.UTF_8));
                            }
                        });

                        runner.test("with existing rooted path with no contents", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                fileSystem.createFile("/folder/file.csv");
                                test.assertEqual(new String[0], Array.toStringArray(fileSystem.getFileContentLines(Path.parse("/folder/file.csv"), CharacterEncoding.UTF_8)));
                            }
                        });

                        runner.test("with existing rooted path with single line", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                fileSystem.createFile("/folder/file.csv", "abcdef");
                                test.assertEqual(new String[] { "abcdef" }, Array.toStringArray(fileSystem.getFileContentLines(Path.parse("/folder/file.csv"), CharacterEncoding.UTF_8)));
                            }
                        });

                        runner.test("with existing rooted path with multiple lines", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                fileSystem.createFile("/folder/file.csv", "ab\ncd\r\ne\nf\n");

                                final String[] expected = new String[] { "ab\n", "cd\r\n", "e\n", "f\n" };
                                final Path filePath = Path.parse("/folder/file.csv");
                                final Iterable<String> fileLines = fileSystem.getFileContentLines(filePath, CharacterEncoding.UTF_8);
                                final String[] actual = Array.toStringArray(fileLines);
                                test.assertEqual(expected, actual);
                            }
                        });
                    }
                });

                runner.testGroup("setFileContents(String,byte[])", new Action0()
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
                                test.assertFalse(fileSystem.setFileContents((String)null, new byte[] { 0, 1, 2 }));
                            }
                        });

                        runner.test("with empty path", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                test.assertFalse(fileSystem.setFileContents("", new byte[] { 0, 1, 2 }));
                            }
                        });

                        runner.test("with relative path", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                test.assertFalse(fileSystem.setFileContents("relative.file", new byte[] { 0, 1, 2 }));
                            }
                        });

                        runner.test("with non-existing rooted path with null contents", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                test.assertTrue(fileSystem.setFileContents("/A.txt", (byte[])null));
                                test.assertTrue(fileSystem.fileExists("/A.txt"));
                                test.assertEqual(new byte[0], fileSystem.getFileContents("/A.txt"));
                            }
                        });

                        runner.test("with existing rooted path and null contents", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                fileSystem.createFile("/A.txt", new byte[] { 0, 1 });

                                test.assertTrue(fileSystem.setFileContents("/A.txt", (byte[])null));
                                test.assertTrue(fileSystem.fileExists("/A.txt"));
                                test.assertEqual(new byte[0], fileSystem.getFileContents("/A.txt"));
                            }
                        });

                        runner.test("with non-existing rooted path and empty contents", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                test.assertTrue(fileSystem.setFileContents("/A.txt", new byte[0]));
                                test.assertTrue(fileSystem.fileExists("/A.txt"));
                                test.assertEqual(new byte[0], fileSystem.getFileContents("/A.txt"));
                            }
                        });

                        runner.test("with existing rooted path and empty contents", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                fileSystem.createFile("/A.txt", new byte[] { 0, 1 });

                                test.assertTrue(fileSystem.setFileContents("/A.txt", new byte[0]));
                                test.assertTrue(fileSystem.fileExists("/A.txt"));
                                test.assertEqual(new byte[0], fileSystem.getFileContents("/A.txt"));
                            }
                        });

                        runner.test("with non-existing rooted path and non-empty contents", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                test.assertTrue(fileSystem.setFileContents("/A.txt", new byte[] { 0, 1, 2 }));
                                test.assertTrue(fileSystem.fileExists("/A.txt"));
                                test.assertEqual(new byte[] { 0, 1, 2 }, fileSystem.getFileContents("/A.txt"));
                            }
                        });

                        runner.test("with non-existing rooted path with non-existing parent folder and non-empty contents", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                test.assertTrue(fileSystem.setFileContents("/folder/A.txt", new byte[] { 0, 1, 2 }));
                                test.assertTrue(fileSystem.folderExists("/folder"));
                                test.assertTrue(fileSystem.fileExists("/folder/A.txt"));
                                test.assertEqual(new byte[] { 0, 1, 2 }, fileSystem.getFileContents("/folder/A.txt"));
                            }
                        });

                        runner.test("with existing rooted path and non-empty contents", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                fileSystem.createFile("/A.txt");

                                test.assertTrue(fileSystem.setFileContents("/A.txt", new byte[] { 0, 1, 2 }));
                                test.assertTrue(fileSystem.fileExists("/A.txt"));
                                test.assertEqual(new byte[] { 0, 1, 2 }, fileSystem.getFileContents("/A.txt"));
                            }
                        });
                    }
                });

                runner.testGroup("setFileContents(Path,byte[])", new Action0()
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
                                test.assertFalse(fileSystem.setFileContents((Path)null, new byte[] { 0, 1, 2 }));
                            }
                        });

                        runner.test("with empty path", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                test.assertFalse(fileSystem.setFileContents(Path.parse(""), new byte[] { 0, 1, 2 }));
                            }
                        });

                        runner.test("with relative path", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                test.assertFalse(fileSystem.setFileContents(Path.parse("relative.file"), new byte[] { 0, 1, 2 }));
                            }
                        });

                        runner.test("with non-existing rooted path and null contents", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                test.assertTrue(fileSystem.setFileContents(Path.parse("/A.txt"), (byte[])null));
                                test.assertTrue(fileSystem.fileExists("/A.txt"));
                                test.assertEqual(new byte[0], fileSystem.getFileContents("/A.txt"));
                            }
                        });

                        runner.test("with existing rooted path and null contents", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                fileSystem.createFile("/A.txt", new byte[] { 0, 1 });
                                test.assertTrue(fileSystem.setFileContents(Path.parse("/A.txt"), (byte[])null));
                                test.assertTrue(fileSystem.fileExists("/A.txt"));
                                test.assertEqual(new byte[0], fileSystem.getFileContents("/A.txt"));
                            }
                        });

                        runner.test("with non-existing rooted path and empty contents", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                test.assertTrue(fileSystem.setFileContents(Path.parse("/A.txt"), new byte[0]));
                                test.assertTrue(fileSystem.fileExists("/A.txt"));
                                test.assertEqual(new byte[0], fileSystem.getFileContents("/A.txt"));
                            }
                        });

                        runner.test("with existing rooted path and empty contents", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                fileSystem.createFile("/A.txt", new byte[] { 0, 1 });
                                test.assertTrue(fileSystem.setFileContents(Path.parse("/A.txt"), new byte[0]));
                                test.assertTrue(fileSystem.fileExists("/A.txt"));
                                test.assertEqual(new byte[0], fileSystem.getFileContents("/A.txt"));
                            }
                        });

                        runner.test("with non-existing rooted path and non-empty contents", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                test.assertTrue(fileSystem.setFileContents(Path.parse("/A.txt"), new byte[] { 0, 1, 2 }));
                                test.assertTrue(fileSystem.fileExists("/A.txt"));
                                test.assertEqual(new byte[] { 0, 1, 2 }, fileSystem.getFileContents("/A.txt"));
                            }
                        });

                        runner.test("with existing rooted path and non-empty contents", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                fileSystem.createFile("/A.txt");

                                test.assertTrue(fileSystem.setFileContents(Path.parse("/A.txt"), new byte[] { 0, 1, 2 }));

                                test.assertTrue(fileSystem.fileExists("/A.txt"));
                                test.assertEqual(new byte[] { 0, 1, 2 }, fileSystem.getFileContents("/A.txt"));
                            }
                        });
                    }
                });

                runner.testGroup("setFileContents(String,String)", new Action0()
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
                                test.assertFalse(fileSystem.setFileContents((String)null, "ABC"));
                            }
                        });

                        runner.test("with empty path", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                test.assertFalse(fileSystem.setFileContents("", "ABC"));
                            }
                        });

                        runner.test("with relative path", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                test.assertFalse(fileSystem.setFileContents("relative.file", "ABC"));
                            }
                        });

                        runner.test("with non-existing rooted path and null contents", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                test.assertTrue(fileSystem.setFileContents("/A.txt", (String)null));
                                test.assertTrue(fileSystem.fileExists("/A.txt"));
                                test.assertEqual("", fileSystem.getFileContentsAsString("/A.txt"));
                            }
                        });

                        runner.test("with existing rooted path and null contents", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                fileSystem.createFile("/A.txt", new byte[] { 0, 1 });
                                test.assertTrue(fileSystem.setFileContents("/A.txt", (String)null));
                                test.assertTrue(fileSystem.fileExists("/A.txt"));
                                test.assertEqual("", fileSystem.getFileContentsAsString("/A.txt"));
                            }
                        });

                        runner.test("with non-existing rooted path and empty contents", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                test.assertTrue(fileSystem.setFileContents("/A.txt", ""));
                                test.assertTrue(fileSystem.fileExists("/A.txt"));
                                test.assertEqual("", fileSystem.getFileContentsAsString("/A.txt"));
                            }
                        });

                        runner.test("with existing rooted path and empty contents", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                fileSystem.createFile("/A.txt", new byte[] { 0, 1 });
                                test.assertTrue(fileSystem.setFileContents("/A.txt", ""));
                                test.assertTrue(fileSystem.fileExists("/A.txt"));
                                test.assertEqual("", fileSystem.getFileContentsAsString("/A.txt"));
                            }
                        });

                        runner.test("with non-existing rooted path with non-empty contents", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                test.assertTrue(fileSystem.setFileContents("/A.txt", "ABC"));
                                test.assertTrue(fileSystem.fileExists("/A.txt"));
                                test.assertEqual("ABC", fileSystem.getFileContentsAsString("/A.txt"));
                            }
                        });

                        runner.test("with non-existing rooted path with non-existing parent folder and non-empty contents", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                test.assertTrue(fileSystem.setFileContents("/folder/A.txt", "ABC"));
                                test.assertTrue(fileSystem.folderExists("/folder"));
                                test.assertTrue(fileSystem.fileExists("/folder/A.txt"));
                                test.assertEqual("ABC", fileSystem.getFileContentsAsString("/folder/A.txt"));
                            }
                        });

                        runner.test("with existing rooted path and non-empty contents", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                fileSystem.createFile("/A.txt");
                                test.assertTrue(fileSystem.setFileContents("/A.txt", "ABC"));
                                test.assertTrue(fileSystem.fileExists("/A.txt"));
                                test.assertEqual("ABC", fileSystem.getFileContentsAsString("/A.txt"));
                            }
                        });
                    }
                });

                runner.testGroup("setFileContents(Path,String)", new Action0()
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
                                test.assertFalse(fileSystem.setFileContents((Path)null, "ABC"));
                            }
                        });

                        runner.test("with empty path", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                test.assertFalse(fileSystem.setFileContents(Path.parse(""), "ABC"));
                            }
                        });

                        runner.test("with relative path", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                test.assertFalse(fileSystem.setFileContents(Path.parse("relative.file"), "ABC"));
                            }
                        });

                        runner.test("with non-existing rooted path and null contents", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                test.assertTrue(fileSystem.setFileContents(Path.parse("/A.txt"), (String)null));
                                test.assertTrue(fileSystem.fileExists("/A.txt"));
                                test.assertEqual("", fileSystem.getFileContentsAsString("/A.txt"));
                            }
                        });

                        runner.test("with existing rooted path and null contents", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                fileSystem.createFile("/A.txt", "Test");
                                test.assertTrue(fileSystem.setFileContents(Path.parse("/A.txt"), (String)null));
                                test.assertTrue(fileSystem.fileExists("/A.txt"));
                                test.assertEqual("", fileSystem.getFileContentsAsString("/A.txt"));
                            }
                        });

                        runner.test("with non-existing rooted path and empty contents", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                test.assertTrue(fileSystem.setFileContents(Path.parse("/A.txt"), ""));
                                test.assertTrue(fileSystem.fileExists("/A.txt"));
                                test.assertEqual("", fileSystem.getFileContentsAsString("/A.txt"));
                            }
                        });

                        runner.test("with existing rooted path and empty contents", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                fileSystem.createFile("/A.txt", new byte[] { 0, 1 });
                                test.assertTrue(fileSystem.setFileContents(Path.parse("/A.txt"), ""));
                                test.assertTrue(fileSystem.fileExists("/A.txt"));
                                test.assertEqual("", fileSystem.getFileContentsAsString("/A.txt"));
                            }
                        });

                        runner.test("with non-existing rooted path and non-empty contents", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final FileSystem fileSystem = getFileSystem(creator);
                                test.assertTrue(fileSystem.setFileContents(Path.parse("/A.txt"), "ABC"));
                                test.assertTrue(fileSystem.fileExists("/A.txt"));
                                test.assertEqual("ABC", fileSystem.getFileContentsAsString("/A.txt"));
                            }
                        });
                    }
                });
            }
        });
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
