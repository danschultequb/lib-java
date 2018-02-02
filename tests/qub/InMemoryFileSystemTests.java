package qub;

public class InMemoryFileSystemTests
{
    public static void test(final TestRunner runner)
    {
        runner.testGroup("InMemoryFileSystem", new Action0()
        {
            @Override
            public void run()
            {
                FileSystemTests.test(runner, new Function0<FileSystem>()
                {
                    @Override
                    public FileSystem run()
                    {
                        final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
                        fileSystem.createRoot("/");
                        return fileSystem;
                    }
                });

                runner.testGroup("setFileCanDelete(String,boolean)", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("when root doesn't exist", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
                                test.assertFalse(fileSystem.setFileCanDelete("C:\\folder\\file.bmp", true));
                            }
                        });
                        
                        runner.test("when parent folder doesn't exist", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
                                fileSystem.createRoot("C:\\");
                                test.assertFalse(fileSystem.setFileCanDelete("C:\\folder\\file.bmp", true));
                            }
                        });
                        
                        runner.test("when file doesn't exist", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
                                fileSystem.createRoot("C:\\");
                                fileSystem.createFolder("C:\\folder");
                                test.assertFalse(fileSystem.setFileCanDelete("C:\\folder\\file.bmp", true));
                            }
                        });
                        
                        runner.test("when file exists", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
                                fileSystem.createRoot("C:\\");
                                fileSystem.createFolder("C:\\folder");
                                fileSystem.createFile("C:\\folder\\file.bmp");
                                test.assertTrue(fileSystem.setFileCanDelete("C:\\folder\\file.bmp", true));
                            }
                        });
                    }
                });

                runner.testGroup("deleteFile(String)", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("when file cannot be deleted", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
                                fileSystem.createRoot("Z:/");
                                fileSystem.createFile("Z:/file.png");
                                test.assertTrue(fileSystem.setFileCanDelete("Z:/file.png", false));
                                test.assertFalse(fileSystem.deleteFile("Z:/file.png"));
                                test.assertTrue(fileSystem.fileExists("Z:/file.png"));
                            }
                        });
                    }
                });

                runner.testGroup("setFolderCanDelete(String,boolean)", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("when root doesn't exist", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
                                test.assertFalse(fileSystem.setFolderCanDelete("C:\\folder\\file.bmp", true));
                            }
                        });

                        runner.test("when parent folder doesn't exist", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
                                fileSystem.createRoot("C:\\");
                                test.assertFalse(fileSystem.setFolderCanDelete("C:\\folder\\file.bmp", true));
                            }
                        });

                        runner.test("when folder doesn't exist", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
                                fileSystem.createRoot("C:\\");
                                fileSystem.createFolder("C:\\folder");
                                test.assertFalse(fileSystem.setFolderCanDelete("C:\\folder\\file.bmp", true));
                            }
                        });

                        runner.test("when folder exists", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
                                fileSystem.createRoot("C:\\");
                                fileSystem.createFolder("C:\\folder\\file.bmp");
                                test.assertTrue(fileSystem.setFolderCanDelete("C:\\folder\\file.bmp", true));
                            }
                        });
                    }
                });

                runner.testGroup("deleteFolder(String)", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("when folder cannot be deleted", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
                                fileSystem.createRoot("Z:/");
                                fileSystem.createFolder("Z:/file.png");
                                test.assertTrue(fileSystem.setFolderCanDelete("Z:/file.png", false));
                                test.assertFalse(fileSystem.deleteFolder("Z:/file.png"));
                                test.assertTrue(fileSystem.folderExists("Z:/file.png"));
                            }
                        });

                        runner.test("when child file cannot be deleted", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
                                fileSystem.createRoot("Z:/");
                                fileSystem.createFolder("Z:/file.png");
                                fileSystem.createFile("Z:/file.png/notme");
                                fileSystem.setFileCanDelete("Z:/file.png/notme", false);
                                test.assertFalse(fileSystem.deleteFolder("Z:/file.png"));
                                test.assertTrue(fileSystem.folderExists("Z:/file.png"));
                            }
                        });

                        runner.test("when child folder cannot be deleted", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
                                fileSystem.createRoot("Z:/");
                                fileSystem.createFolder("Z:/file.png");
                                fileSystem.createFolder("Z:/file.png/notme");
                                fileSystem.setFolderCanDelete("Z:/file.png/notme", false);
                                test.assertFalse(fileSystem.deleteFolder("Z:/file.png"));
                                test.assertTrue(fileSystem.folderExists("Z:/file.png"));
                            }
                        });
                    }
                });
            }
        });
    }
}
