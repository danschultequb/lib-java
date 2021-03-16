package qub;

public interface FakeTypeLoaderTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(FakeTypeLoader.class, () ->
        {
            TypeLoaderTests.test(runner, () ->
            {
                return FakeTypeLoader.create()
                    .addType(Integer.class)
                    .addType(String.class)
                    .addType(TypeLoader.class);
            });

            runner.testGroup("addType(Class<?>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final FakeTypeLoader typeLoader = FakeTypeLoader.create();
                    test.assertThrows(() -> typeLoader.addType((Class<?>)null),
                        new PreConditionFailure("type cannot be null."));
                });

                runner.test("with non-null", (Test test) ->
                {
                    final FakeTypeLoader typeLoader = FakeTypeLoader.create();

                    final FakeTypeLoader addTypeResult = typeLoader.addType(String.class);
                    test.assertSame(typeLoader, addTypeResult);
                    test.assertSame(String.class, typeLoader.getType("java.lang.String").await());
                });
            });

            runner.testGroup("addTypeContainer(String,String)", () ->
            {
                final Action3<String,String,Throwable> addTypeContainerErrorTest = (String fullTypeName, String typeContainerPath, Throwable expected) ->
                {
                    runner.test("with " + English.andList(Iterable.create(fullTypeName, typeContainerPath).map(Strings::escapeAndQuote)), (Test test) ->
                    {
                        final FakeTypeLoader typeLoader = FakeTypeLoader.create();

                        test.assertThrows(() -> typeLoader.addTypeContainer(fullTypeName, typeContainerPath),
                            expected);
                    });
                };

                addTypeContainerErrorTest.run(null, "/fake/type/container/", new PreConditionFailure("fullTypeName cannot be null."));
                addTypeContainerErrorTest.run("", "/fake/type/container/", new PreConditionFailure("fullTypeName cannot be empty."));
                addTypeContainerErrorTest.run("java.lang.String", null, new PreConditionFailure("typeContainerPath cannot be null."));
                addTypeContainerErrorTest.run("java.lang.String", "", new PreConditionFailure("typeContainerPath cannot be empty."));

                final Action2<String,String> addTypeContainerTest = (String fullTypeName, String typeContainerPath) ->
                {
                    runner.test("with " + English.andList(Iterable.create(fullTypeName, typeContainerPath).map(Strings::escapeAndQuote)), (Test test) ->
                    {
                        final FakeTypeLoader typeLoader = FakeTypeLoader.create();

                        final FakeTypeLoader addTypeContainerResult = typeLoader.addTypeContainer(fullTypeName, typeContainerPath);
                        test.assertSame(typeLoader, addTypeContainerResult);
                        test.assertEqual(typeContainerPath, typeLoader.getTypeContainerPathString(fullTypeName).await());
                    });
                };

                addTypeContainerTest.run("a", "b");
                addTypeContainerTest.run("java.lang.String", "/fake/type/container/");
            });

            runner.testGroup("addTypeContainer(String,Path)", () ->
            {
                final Action3<String,Path,Throwable> addTypeContainerErrorTest = (String fullTypeName, Path typeContainerPath, Throwable expected) ->
                {
                    runner.test("with " + English.andList(Iterable.create(fullTypeName, typeContainerPath).map(Strings::escapeAndQuote)), (Test test) ->
                    {
                        final FakeTypeLoader typeLoader = FakeTypeLoader.create();

                        test.assertThrows(() -> typeLoader.addTypeContainer(fullTypeName, typeContainerPath),
                            expected);
                    });
                };

                addTypeContainerErrorTest.run(null, Path.parse("/fake/type/container/"), new PreConditionFailure("fullTypeName cannot be null."));
                addTypeContainerErrorTest.run("", Path.parse("/fake/type/container/"), new PreConditionFailure("fullTypeName cannot be empty."));
                addTypeContainerErrorTest.run("java.lang.String", null, new PreConditionFailure("typeContainerPath cannot be null."));
                addTypeContainerErrorTest.run("java.lang.String", Path.parse("relative/type/container/"), new PreConditionFailure("typeContainerPath.isRooted() cannot be false."));

                final Action2<String,Path> addTypeContainerTest = (String fullTypeName, Path typeContainerPath) ->
                {
                    runner.test("with " + English.andList(Iterable.create(fullTypeName, typeContainerPath).map(Strings::escapeAndQuote)), (Test test) ->
                    {
                        final FakeTypeLoader typeLoader = FakeTypeLoader.create();

                        final FakeTypeLoader addTypeContainerResult = typeLoader.addTypeContainer(fullTypeName, typeContainerPath);
                        test.assertSame(typeLoader, addTypeContainerResult);
                        test.assertEqual(typeContainerPath, typeLoader.getTypeContainerPathString(fullTypeName).await());
                    });
                };

                addTypeContainerTest.run("java.lang.String", Path.parse("/fake/type/container/"));
            });

            runner.testGroup("addTypeContainer(String,File)", () ->
            {
                final Action3<String,Path,Throwable> addTypeContainerErrorTest = (String fullTypeName, Path typeContainerPath, Throwable expected) ->
                {
                    runner.test("with " + English.andList(Iterable.create(fullTypeName, typeContainerPath).map(Strings::escapeAndQuote)), (Test test) ->
                    {
                        final FakeTypeLoader typeLoader = FakeTypeLoader.create();

                        final File typeContainer;
                        if (typeContainerPath == null)
                        {
                            typeContainer = null;
                        }
                        else
                        {
                            final InMemoryFileSystem fileSystem = InMemoryFileSystem.create();
                            fileSystem.createRoot(typeContainerPath.getRoot().await()).await();
                            typeContainer = fileSystem.getFile(typeContainerPath).await();
                        }

                        test.assertThrows(() -> typeLoader.addTypeContainer(fullTypeName, typeContainer),
                            expected);
                    });
                };

                addTypeContainerErrorTest.run(null, Path.parse("/fake/type/container"), new PreConditionFailure("fullTypeName cannot be null."));
                addTypeContainerErrorTest.run("", Path.parse("/fake/type/container"), new PreConditionFailure("fullTypeName cannot be empty."));
                addTypeContainerErrorTest.run("java.lang.String", null, new PreConditionFailure("typeContainer cannot be null."));

                final Action2<String,Path> addTypeContainerTest = (String fullTypeName, Path typeContainerPath) ->
                {
                    runner.test("with " + English.andList(Iterable.create(fullTypeName, typeContainerPath).map(Strings::escapeAndQuote)), (Test test) ->
                    {
                        final FakeTypeLoader typeLoader = FakeTypeLoader.create();

                        final InMemoryFileSystem fileSystem = InMemoryFileSystem.create();
                        fileSystem.createRoot(typeContainerPath.getRoot().await()).await();
                        final File typeContainer = fileSystem.getFile(typeContainerPath).await();

                        final FakeTypeLoader addTypeContainerResult = typeLoader.addTypeContainer(fullTypeName, typeContainer);
                        test.assertSame(typeLoader, addTypeContainerResult);
                        test.assertEqual(typeContainerPath, typeLoader.getTypeContainerPathString(fullTypeName).await());
                    });
                };

                addTypeContainerTest.run("java.lang.String", Path.parse("/fake/type/container"));
                addTypeContainerTest.run("java.lang.Integer", Path.parse("C:/fake/type/container"));
            });

            runner.testGroup("addTypeContainer(String,Folder)", () ->
            {
                final Action3<String,Path,Throwable> addTypeContainerErrorTest = (String fullTypeName, Path typeContainerPath, Throwable expected) ->
                {
                    runner.test("with " + English.andList(Iterable.create(fullTypeName, typeContainerPath).map(Strings::escapeAndQuote)), (Test test) ->
                    {
                        final FakeTypeLoader typeLoader = FakeTypeLoader.create();

                        final Folder typeContainer;
                        if (typeContainerPath == null)
                        {
                            typeContainer = null;
                        }
                        else
                        {
                            final InMemoryFileSystem fileSystem = InMemoryFileSystem.create();
                            fileSystem.createRoot(typeContainerPath.getRoot().await()).await();
                            typeContainer = fileSystem.getFolder(typeContainerPath).await();
                        }

                        test.assertThrows(() -> typeLoader.addTypeContainer(fullTypeName, typeContainer),
                            expected);
                    });
                };

                addTypeContainerErrorTest.run(null, Path.parse("/fake/type/container/"), new PreConditionFailure("fullTypeName cannot be null."));
                addTypeContainerErrorTest.run("", Path.parse("/fake/type/container/"), new PreConditionFailure("fullTypeName cannot be empty."));
                addTypeContainerErrorTest.run("java.lang.String", null, new PreConditionFailure("typeContainer cannot be null."));

                final Action2<String,Path> addTypeContainerTest = (String fullTypeName, Path typeContainerPath) ->
                {
                    runner.test("with " + English.andList(Iterable.create(fullTypeName, typeContainerPath).map(Strings::escapeAndQuote)), (Test test) ->
                    {
                        final FakeTypeLoader typeLoader = FakeTypeLoader.create();

                        final InMemoryFileSystem fileSystem = InMemoryFileSystem.create();
                        fileSystem.createRoot(typeContainerPath.getRoot().await()).await();
                        final Folder typeContainer = fileSystem.getFolder(typeContainerPath).await();

                        final FakeTypeLoader addTypeContainerResult = typeLoader.addTypeContainer(fullTypeName, typeContainer);
                        test.assertSame(typeLoader, addTypeContainerResult);
                        test.assertEqual(typeContainerPath, typeLoader.getTypeContainerPathString(fullTypeName).await());
                    });
                };

                addTypeContainerTest.run("java.lang.String", Path.parse("/fake/type/container/"));
                addTypeContainerTest.run("java.lang.Integer", Path.parse("C:/fake/type/container/"));
            });

            runner.testGroup("addTypeContainer(Class<?>,String)", () ->
            {
                final Action3<Class<?>,String,Throwable> addTypeContainerErrorTest = (Class<?> type, String typeContainerPath, Throwable expected) ->
                {
                    runner.test("with " + English.andList(Iterable.create(type, typeContainerPath).map(Strings::escapeAndQuote)), (Test test) ->
                    {
                        final FakeTypeLoader typeLoader = FakeTypeLoader.create();

                        test.assertThrows(() -> typeLoader.addTypeContainer(type, typeContainerPath),
                            expected);
                    });
                };

                addTypeContainerErrorTest.run(null, "/fake/type/container/", new PreConditionFailure("type cannot be null."));
                addTypeContainerErrorTest.run(String.class, null, new PreConditionFailure("typeContainerPath cannot be null."));
                addTypeContainerErrorTest.run(String.class, "", new PreConditionFailure("typeContainerPath cannot be empty."));

                final Action2<Class<?>,String> addTypeContainerTest = (Class<?> type, String typeContainerPath) ->
                {
                    runner.test("with " + English.andList(Iterable.create(type, typeContainerPath).map(Strings::escapeAndQuote)), (Test test) ->
                    {
                        final FakeTypeLoader typeLoader = FakeTypeLoader.create();

                        final FakeTypeLoader addTypeContainerResult = typeLoader.addTypeContainer(type, typeContainerPath);
                        test.assertSame(typeLoader, addTypeContainerResult);
                        test.assertEqual(typeContainerPath, typeLoader.getTypeContainerPathString(type).await());
                    });
                };

                addTypeContainerTest.run(String.class, "/fake/type/container/");
            });

            runner.testGroup("addTypeContainer(Class<?>,Path)", () ->
            {
                final Action3<Class<?>,Path,Throwable> addTypeContainerErrorTest = (Class<?> type, Path typeContainerPath, Throwable expected) ->
                {
                    runner.test("with " + English.andList(Iterable.create(type, typeContainerPath).map(Strings::escapeAndQuote)), (Test test) ->
                    {
                        final FakeTypeLoader typeLoader = FakeTypeLoader.create();

                        test.assertThrows(() -> typeLoader.addTypeContainer(type, typeContainerPath),
                            expected);
                    });
                };

                addTypeContainerErrorTest.run(null, Path.parse("/fake/type/container/"), new PreConditionFailure("type cannot be null."));
                addTypeContainerErrorTest.run(String.class, null, new PreConditionFailure("typeContainerPath cannot be null."));
                addTypeContainerErrorTest.run(String.class, Path.parse("relative/type/container/"), new PreConditionFailure("typeContainerPath.isRooted() cannot be false."));

                final Action2<Class<?>,Path> addTypeContainerTest = (Class<?> type, Path typeContainerPath) ->
                {
                    runner.test("with " + English.andList(Iterable.create(type, typeContainerPath).map(Strings::escapeAndQuote)), (Test test) ->
                    {
                        final FakeTypeLoader typeLoader = FakeTypeLoader.create();

                        final FakeTypeLoader addTypeContainerResult = typeLoader.addTypeContainer(type, typeContainerPath);
                        test.assertSame(typeLoader, addTypeContainerResult);
                        test.assertEqual(typeContainerPath, typeLoader.getTypeContainerPathString(type).await());
                    });
                };

                addTypeContainerTest.run(String.class, Path.parse("/fake/type/container/"));
            });

            runner.testGroup("addTypeContainer(Class<?>,File)", () ->
            {
                final Action3<Class<?>,Path,Throwable> addTypeContainerErrorTest = (Class<?> type, Path typeContainerPath, Throwable expected) ->
                {
                    runner.test("with " + English.andList(Iterable.create(type, typeContainerPath).map(Strings::escapeAndQuote)), (Test test) ->
                    {
                        final FakeTypeLoader typeLoader = FakeTypeLoader.create();

                        final File typeContainer;
                        if (typeContainerPath == null)
                        {
                            typeContainer = null;
                        }
                        else
                        {
                            final InMemoryFileSystem fileSystem = InMemoryFileSystem.create();
                            fileSystem.createRoot(typeContainerPath.getRoot().await()).await();
                            typeContainer = fileSystem.getFile(typeContainerPath).await();
                        }

                        test.assertThrows(() -> typeLoader.addTypeContainer(type, typeContainer),
                            expected);
                    });
                };

                addTypeContainerErrorTest.run(null, Path.parse("/fake/type/container"), new PreConditionFailure("type cannot be null."));
                addTypeContainerErrorTest.run(String.class, null, new PreConditionFailure("typeContainer cannot be null."));

                final Action2<Class<?>,Path> addTypeContainerTest = (Class<?> type, Path typeContainerPath) ->
                {
                    runner.test("with " + English.andList(Iterable.create(type, typeContainerPath).map(Strings::escapeAndQuote)), (Test test) ->
                    {
                        final FakeTypeLoader typeLoader = FakeTypeLoader.create();

                        final InMemoryFileSystem fileSystem = InMemoryFileSystem.create();
                        fileSystem.createRoot(typeContainerPath.getRoot().await()).await();
                        final File typeContainer = fileSystem.getFile(typeContainerPath).await();

                        final FakeTypeLoader addTypeContainerResult = typeLoader.addTypeContainer(type, typeContainer);
                        test.assertSame(typeLoader, addTypeContainerResult);
                        test.assertEqual(typeContainerPath, typeLoader.getTypeContainerPathString(type).await());
                    });
                };

                addTypeContainerTest.run(String.class, Path.parse("/fake/type/container"));
                addTypeContainerTest.run(Integer.class, Path.parse("C:/fake/type/container"));
            });

            runner.testGroup("addTypeContainer(Class<?>,Folder)", () ->
            {
                final Action3<Class<?>,Path,Throwable> addTypeContainerErrorTest = (Class<?> type, Path typeContainerPath, Throwable expected) ->
                {
                    runner.test("with " + English.andList(Iterable.create(type, typeContainerPath).map(Strings::escapeAndQuote)), (Test test) ->
                    {
                        final FakeTypeLoader typeLoader = FakeTypeLoader.create();

                        final Folder typeContainer;
                        if (typeContainerPath == null)
                        {
                            typeContainer = null;
                        }
                        else
                        {
                            final InMemoryFileSystem fileSystem = InMemoryFileSystem.create();
                            fileSystem.createRoot(typeContainerPath.getRoot().await()).await();
                            typeContainer = fileSystem.getFolder(typeContainerPath).await();
                        }

                        test.assertThrows(() -> typeLoader.addTypeContainer(type, typeContainer),
                            expected);
                    });
                };

                addTypeContainerErrorTest.run(null, Path.parse("/fake/type/container/"), new PreConditionFailure("type cannot be null."));
                addTypeContainerErrorTest.run(String.class, null, new PreConditionFailure("typeContainer cannot be null."));

                final Action2<Class<?>,Path> addTypeContainerTest = (Class<?> type, Path typeContainerPath) ->
                {
                    runner.test("with " + English.andList(Iterable.create(type, typeContainerPath).map(Strings::escapeAndQuote)), (Test test) ->
                    {
                        final FakeTypeLoader typeLoader = FakeTypeLoader.create();

                        final InMemoryFileSystem fileSystem = InMemoryFileSystem.create();
                        fileSystem.createRoot(typeContainerPath.getRoot().await()).await();
                        final Folder typeContainer = fileSystem.getFolder(typeContainerPath).await();

                        final FakeTypeLoader addTypeContainerResult = typeLoader.addTypeContainer(type, typeContainer);
                        test.assertSame(typeLoader, addTypeContainerResult);
                        test.assertEqual(typeContainerPath, typeLoader.getTypeContainerPathString(type).await());
                    });
                };

                addTypeContainerTest.run(String.class, Path.parse("/fake/type/container/"));
                addTypeContainerTest.run(Integer.class, Path.parse("C:/fake/type/container/"));
            });

            runner.testGroup("getTypeContainerPathString(Class<?>)", () ->
            {
                runner.test("with type that exists", (Test test) ->
                {
                    final FakeTypeLoader typeLoader = FakeTypeLoader.create()
                        .addTypeContainer(TypeLoader.class, "/fake/type/container/");
                    test.assertEqual("/fake/type/container/", typeLoader.getTypeContainerPathString("qub.TypeLoader").await());
                });
            });

            runner.testGroup("getTypeContainerPathString(Class<?>)", () ->
            {
                runner.test("with type that exists", (Test test) ->
                {
                    final FakeTypeLoader typeLoader = FakeTypeLoader.create()
                        .addTypeContainer(TypeLoader.class, "/fake/type/container/");
                    test.assertEqual("/fake/type/container/", typeLoader.getTypeContainerPathString(TypeLoader.class).await());
                });
            });

            runner.testGroup("getTypeContainerPath(String)", () ->
            {
                runner.test("with type that exists", (Test test) ->
                {
                    final FakeTypeLoader typeLoader = FakeTypeLoader.create()
                        .addTypeContainer(TypeLoader.class, "/fake/type/container/");
                    test.assertEqual(Path.parse("/fake/type/container/"), typeLoader.getTypeContainerPath("qub.TypeLoader").await());
                });
            });

            runner.testGroup("getTypeContainerPath(Class<?>)", () ->
            {
                runner.test("with type that exists", (Test test) ->
                {
                    final FakeTypeLoader typeLoader = FakeTypeLoader.create()
                        .addTypeContainer(TypeLoader.class, "/fake/type/container/");
                    test.assertEqual(Path.parse("/fake/type/container/"), typeLoader.getTypeContainerPath(TypeLoader.class).await());
                });
            });
        });
    }
}
