package qub;

public class FolderFileSystemTests
{
    public static void test(final TestRunner runner)
    {
        runner.testGroup("FolderFileSystem", new Action0()
        {
            @Override
            public void run()
            {
                runner.testGroup("create(FileSystem,String)", new Action0()
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

                                test.assertNull(FolderFileSystem.create(fileSystem, (String)null));
                            }
                        });
                                
                        runner.test("with empty path", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
                                fileSystem.createRoot("/");

                                test.assertNull(FolderFileSystem.create(fileSystem, ""));
                            }
                        });
                                
                        runner.test("with relative path", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
                                fileSystem.createRoot("/");

                                final FolderFileSystem folderFileSystem = FolderFileSystem.create(fileSystem, "basefolder");
                                test.assertEqual(Path.parse("basefolder"), folderFileSystem.getBaseFolderPath());
                            }
                        });

                        runner.test("with relative path that ends with backslash", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
                                fileSystem.createRoot("/");

                                final FolderFileSystem folderFileSystem = FolderFileSystem.create(fileSystem, "basefolder\\");
                                test.assertEqual(Path.parse("basefolder"), folderFileSystem.getBaseFolderPath());
                            }
                        });

                        runner.test("with relative path that ends with forward slash", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
                                fileSystem.createRoot("/");

                                final FolderFileSystem folderFileSystem = FolderFileSystem.create(fileSystem, "basefolder/");
                                test.assertEqual(Path.parse("basefolder"), folderFileSystem.getBaseFolderPath());
                            }
                        });

                        runner.test("with rooted path", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
                                fileSystem.createRoot("/");

                                final FolderFileSystem folderFileSystem = FolderFileSystem.create(fileSystem, "\\basefolder");
                                test.assertEqual(Path.parse("\\basefolder"), folderFileSystem.getBaseFolderPath());
                            }
                        });

                        runner.test("with rooted path that ends with backslash", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
                                fileSystem.createRoot("/");

                                final FolderFileSystem folderFileSystem = FolderFileSystem.create(fileSystem, "/basefolder\\");
                                test.assertEqual(Path.parse("/basefolder"), folderFileSystem.getBaseFolderPath());
                            }
                        });

                        runner.test("with rooted path that ends with forward slash", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
                                fileSystem.createRoot("/");

                                final FolderFileSystem folderFileSystem = FolderFileSystem.create(fileSystem, "/basefolder/");
                                test.assertEqual(Path.parse("/basefolder"), folderFileSystem.getBaseFolderPath());
                            }
                        });
                    }
                });
            }
        });
    }
}
