package qub;

public interface JavaClassFileTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(JavaClassFile.class, () ->
        {
            runner.testGroup("constructor()", () ->
            {
                runner.test("with null file", (Test test) ->
                {
                    test.assertThrows(() -> new JavaClassFile(null),
                        new PreConditionFailure("fileSystem cannot be null."));
                });

                runner.test("with class file that doesn't exist", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = InMemoryFileSystem.create();
                    fileSystem.createRoot("/").await();

                    final File file = fileSystem.getFile("/outputs/a/b.class").await();
                    final JavaClassFile classFile = new JavaClassFile(file);
                    test.assertNotNull(classFile);
                    test.assertFalse(classFile.exists().await());
                    test.assertEqual("/outputs/a/b.class", classFile.getPath().toString());
                });

                runner.test("with non-class file that doesn't exist", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = InMemoryFileSystem.create();
                    fileSystem.createRoot("/").await();

                    final File file = fileSystem.getFile("/outputs/a/b.txt").await();
                    final JavaClassFile classFile = new JavaClassFile(file);
                    test.assertNotNull(classFile);
                    test.assertFalse(classFile.exists().await());
                    test.assertEqual("/outputs/a/b.txt", classFile.getPath().toString());
                });
            });

            runner.testGroup("isAnonymousClass()", () ->
            {
                runner.test("with non-anonymous class file", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = InMemoryFileSystem.create();
                    final JavaClassFile classFile = new JavaClassFile(fileSystem.getFile("/outputs/a/b.class").await());
                    test.assertFalse(classFile.isAnonymousClass());
                });

                runner.test("with anonymous class file", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = InMemoryFileSystem.create();
                    final JavaClassFile classFile = new JavaClassFile(fileSystem.getFile("/outputs/a/b$1.class").await());
                    test.assertTrue(classFile.isAnonymousClass());
                });
            });

            runner.testGroup("isAnonymousClass(String)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> JavaClassFile.isAnonymousClass((String)null),
                        new PreConditionFailure("classFilePath cannot be null."));
                });

                runner.test("with empty", (Test test) ->
                {
                    test.assertThrows(() -> JavaClassFile.isAnonymousClass(""),
                        new PreConditionFailure("classFilePath cannot be empty."));
                });

                runner.test("with rooted folder path", (Test test) ->
                {
                    test.assertThrows(() -> JavaClassFile.isAnonymousClass("/outputs/"),
                        new PreConditionFailure("classFilePath.endsWith(\"/\") cannot be true."));
                });

                runner.test("with non-rooted folder path", (Test test) ->
                {
                    test.assertThrows(() -> JavaClassFile.isAnonymousClass("outputs/"),
                        new PreConditionFailure("classFilePath.endsWith(\"/\") cannot be true."));
                });

                runner.test("with rooted non-class file", (Test test) ->
                {
                    test.assertFalse(JavaClassFile.isAnonymousClass("/outputs/a.txt"));
                });

                runner.test("with rooted non-anonymous class file", (Test test) ->
                {
                    test.assertFalse(JavaClassFile.isAnonymousClass("/outputs/a.class"));
                });

                runner.test("with rooted anonymous class file", (Test test) ->
                {
                    test.assertTrue(JavaClassFile.isAnonymousClass("/outputs/a$20.class"));
                });

                runner.test("with non-rooted non-class file", (Test test) ->
                {
                    test.assertFalse(JavaClassFile.isAnonymousClass("outputs/a.txt"));
                });

                runner.test("with non-rooted non-anonymous class file", (Test test) ->
                {
                    test.assertFalse(JavaClassFile.isAnonymousClass("outputs/a.class"));
                });

                runner.test("with non-rooted anonymous class file", (Test test) ->
                {
                    test.assertTrue(JavaClassFile.isAnonymousClass("outputs/a$20.class"));
                });
            });

            runner.testGroup("isAnonymousClass(Path)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> JavaClassFile.isAnonymousClass((Path)null),
                        new PreConditionFailure("classFilePath cannot be null."));
                });

                runner.test("with rooted folder path", (Test test) ->
                {
                    test.assertThrows(() -> JavaClassFile.isAnonymousClass(Path.parse("/outputs/")),
                        new PreConditionFailure("classFilePath.endsWith(\"/\") cannot be true."));
                });

                runner.test("with non-rooted folder path", (Test test) ->
                {
                    test.assertThrows(() -> JavaClassFile.isAnonymousClass(Path.parse("outputs/")),
                        new PreConditionFailure("classFilePath.endsWith(\"/\") cannot be true."));
                });

                runner.test("with rooted non-class file", (Test test) ->
                {
                    test.assertFalse(JavaClassFile.isAnonymousClass(Path.parse("/outputs/a.txt")));
                });

                runner.test("with rooted non-anonymous class file", (Test test) ->
                {
                    test.assertFalse(JavaClassFile.isAnonymousClass(Path.parse("/outputs/a.class")));
                });

                runner.test("with rooted anonymous class file", (Test test) ->
                {
                    test.assertTrue(JavaClassFile.isAnonymousClass(Path.parse("/outputs/a$20.class")));
                });

                runner.test("with non-rooted non-class file", (Test test) ->
                {
                    test.assertFalse(JavaClassFile.isAnonymousClass(Path.parse("outputs/a.txt")));
                });

                runner.test("with non-rooted non-anonymous class file", (Test test) ->
                {
                    test.assertFalse(JavaClassFile.isAnonymousClass(Path.parse("outputs/a.class")));
                });

                runner.test("with non-rooted anonymous class file", (Test test) ->
                {
                    test.assertTrue(JavaClassFile.isAnonymousClass(Path.parse("outputs/a$20.class")));
                });
            });

            runner.testGroup("getTypeName()", () ->
            {
                runner.test("with non-anonymous class file", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = InMemoryFileSystem.create();
                    final JavaClassFile classFile = new JavaClassFile(fileSystem.getFile("/outputs/a/banana.class").await());
                    test.assertEqual("banana", classFile.getTypeName());
                });

                runner.test("with anonymous class file", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = InMemoryFileSystem.create();
                    final JavaClassFile classFile = new JavaClassFile(fileSystem.getFile("/outputs/a/boat$1.class").await());
                    test.assertEqual("boat", classFile.getTypeName());
                });
            });

            runner.testGroup("getTypeName(String)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> JavaClassFile.getTypeName((String)null),
                        new PreConditionFailure("classFilePath cannot be null."));
                });

                runner.test("with empty", (Test test) ->
                {
                    test.assertThrows(() -> JavaClassFile.getTypeName(""),
                        new PreConditionFailure("classFilePath cannot be empty."));
                });

                runner.test("with rooted folder path", (Test test) ->
                {
                    test.assertThrows(() -> JavaClassFile.getTypeName("/outputs/"),
                        new PreConditionFailure("classFilePath.endsWith(\"/\") cannot be true."));
                });

                runner.test("with non-rooted folder path", (Test test) ->
                {
                    test.assertThrows(() -> JavaClassFile.getTypeName("outputs/"),
                        new PreConditionFailure("classFilePath.endsWith(\"/\") cannot be true."));
                });

                runner.test("with rooted non-class file", (Test test) ->
                {
                    test.assertEqual("a", JavaClassFile.getTypeName("/outputs/a.txt"));
                });

                runner.test("with rooted non-anonymous class file", (Test test) ->
                {
                    test.assertEqual("a", JavaClassFile.getTypeName("/outputs/a.class"));
                });

                runner.test("with rooted anonymous class file", (Test test) ->
                {
                    test.assertEqual("a", JavaClassFile.getTypeName("/outputs/a$20.class"));
                });

                runner.test("with non-rooted non-class file", (Test test) ->
                {
                    test.assertEqual("a", JavaClassFile.getTypeName("outputs/a.txt"));
                });

                runner.test("with non-rooted non-anonymous class file", (Test test) ->
                {
                    test.assertEqual("a", JavaClassFile.getTypeName("outputs/a.class"));
                });

                runner.test("with non-rooted anonymous class file", (Test test) ->
                {
                    test.assertEqual("apples", JavaClassFile.getTypeName("outputs/apples$20.class"));
                });
            });

            runner.testGroup("getTypeName(Path)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> JavaClassFile.getTypeName((Path)null),
                        new PreConditionFailure("classFilePath cannot be null."));
                });

                runner.test("with rooted folder path", (Test test) ->
                {
                    test.assertThrows(() -> JavaClassFile.getTypeName(Path.parse("/outputs/")),
                        new PreConditionFailure("classFilePath.endsWith(\"/\") cannot be true."));
                });

                runner.test("with non-rooted folder path", (Test test) ->
                {
                    test.assertThrows(() -> JavaClassFile.getTypeName(Path.parse("outputs/")),
                        new PreConditionFailure("classFilePath.endsWith(\"/\") cannot be true."));
                });

                runner.test("with rooted non-class file", (Test test) ->
                {
                    test.assertEqual("a", JavaClassFile.getTypeName(Path.parse("/outputs/a.txt")));
                });

                runner.test("with rooted non-anonymous class file", (Test test) ->
                {
                    test.assertEqual("a", JavaClassFile.getTypeName(Path.parse("/outputs/a.class")));
                });

                runner.test("with rooted anonymous class file", (Test test) ->
                {
                    test.assertEqual("a", JavaClassFile.getTypeName(Path.parse("/outputs/a$20.class")));
                });

                runner.test("with non-rooted non-class file", (Test test) ->
                {
                    test.assertEqual("a", JavaClassFile.getTypeName(Path.parse("outputs/a.txt")));
                });

                runner.test("with non-rooted non-anonymous class file", (Test test) ->
                {
                    test.assertEqual("a", JavaClassFile.getTypeName(Path.parse("outputs/a.class")));
                });

                runner.test("with non-rooted anonymous class file", (Test test) ->
                {
                    test.assertEqual("apples", JavaClassFile.getTypeName(Path.parse("outputs/apples$20.class")));
                });
            });

            runner.testGroup("getFullTypeName(Folder)", () ->
            {
                runner.test("with null folder", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = InMemoryFileSystem.create();
                    final JavaClassFile classFile = new JavaClassFile(fileSystem.getFile("/outputs/a/banana.class").await());
                    test.assertThrows(() -> classFile.getFullTypeName((Folder)null),
                        new PreConditionFailure("outputFolder cannot be null."));
                });

                runner.test("with non-anonymous class file", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = InMemoryFileSystem.create();
                    final JavaClassFile classFile = new JavaClassFile(fileSystem.getFile("/outputs/a/banana.class").await());
                    test.assertEqual("a.banana", classFile.getFullTypeName(fileSystem.getFolder("/outputs/").await()));
                });

                runner.test("with anonymous class file", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = InMemoryFileSystem.create();
                    final JavaClassFile classFile = new JavaClassFile(fileSystem.getFile("/outputs/a/boat$1.class").await());
                    test.assertEqual("boat", classFile.getFullTypeName(fileSystem.getFolder("/outputs/a").await()));
                });
            });

            runner.testGroup("getFullTypeName(String)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> JavaClassFile.getFullTypeName((String)null),
                        new PreConditionFailure("classFilePath cannot be null."));
                });

                runner.test("with empty", (Test test) ->
                {
                    test.assertThrows(() -> JavaClassFile.getFullTypeName(""),
                        new PreConditionFailure("classFilePath cannot be empty."));
                });

                runner.test("with rooted folder path", (Test test) ->
                {
                    test.assertThrows(() -> JavaClassFile.getFullTypeName("/outputs/"),
                        new PreConditionFailure("classFilePath.isRooted() cannot be true."));
                });

                runner.test("with non-rooted folder path", (Test test) ->
                {
                    test.assertThrows(() -> JavaClassFile.getFullTypeName("outputs/"),
                        new PreConditionFailure("classFilePath.endsWith(\"/\") cannot be true."));
                });

                runner.test("with rooted non-class file", (Test test) ->
                {
                    test.assertThrows(() -> JavaClassFile.getFullTypeName("/outputs/a.txt"),
                        new PreConditionFailure("classFilePath.isRooted() cannot be true."));
                });

                runner.test("with rooted non-anonymous class file", (Test test) ->
                {
                    test.assertThrows(() -> JavaClassFile.getFullTypeName("/outputs/a.class"),
                        new PreConditionFailure("classFilePath.isRooted() cannot be true."));
                });

                runner.test("with rooted anonymous class file", (Test test) ->
                {
                    test.assertThrows(() -> JavaClassFile.getFullTypeName("/outputs/a$20.class"),
                        new PreConditionFailure("classFilePath.isRooted() cannot be true."));
                });

                runner.test("with non-rooted non-class file", (Test test) ->
                {
                    test.assertEqual("outputs.a", JavaClassFile.getFullTypeName("outputs/a.txt"));
                });

                runner.test("with non-rooted non-anonymous class file", (Test test) ->
                {
                    test.assertEqual("outputs.a", JavaClassFile.getFullTypeName("outputs/a.class"));
                });

                runner.test("with non-rooted anonymous class file", (Test test) ->
                {
                    test.assertEqual("outputs.apples", JavaClassFile.getFullTypeName("outputs/apples$20.class"));
                });
            });

            runner.testGroup("getFullTypeName(String,String)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> JavaClassFile.getFullTypeName((String)null, "/outputs/"),
                        new PreConditionFailure("classFilePath cannot be null."));
                });

                runner.test("with empty", (Test test) ->
                {
                    test.assertThrows(() -> JavaClassFile.getFullTypeName("", "/outputs/"),
                        new PreConditionFailure("classFilePath cannot be empty."));
                });

                runner.test("with rooted folder path", (Test test) ->
                {
                    test.assertThrows(() -> JavaClassFile.getFullTypeName("/outputs/", "/"),
                        new PreConditionFailure("classFilePath.endsWith(\"/\") cannot be true."));
                });

                runner.test("with classFilePath equal to outputsFolderPath", (Test test) ->
                {
                    test.assertThrows(() -> JavaClassFile.getFullTypeName("/outputs/", "/outputs/"),
                        new PreConditionFailure("classFilePath.endsWith(\"/\") cannot be true."));
                });

                runner.test("with non-rooted folder path", (Test test) ->
                {
                    test.assertThrows(() -> JavaClassFile.getFullTypeName("outputs/", "/outputs/"),
                        new PreConditionFailure("classFilePath.isRooted() cannot be false."));
                });

                runner.test("with rooted non-class file", (Test test) ->
                {
                    test.assertEqual("c.a", JavaClassFile.getFullTypeName("/outputs/c/a.txt", "/outputs/"));
                });

                runner.test("with rooted non-anonymous class file", (Test test) ->
                {
                    test.assertEqual("a", JavaClassFile.getFullTypeName("/outputs/a.class", "/outputs/"));
                });

                runner.test("with rooted anonymous class file", (Test test) ->
                {
                    test.assertEqual("b.a", JavaClassFile.getFullTypeName("/outputs/c/b/a$20.class", "/outputs/c"));
                });

                runner.test("with non-rooted non-class file", (Test test) ->
                {
                    test.assertThrows(() -> JavaClassFile.getFullTypeName("outputs/a.txt", "/outputs/"),
                        new PreConditionFailure("classFilePath.isRooted() cannot be false."));
                });

                runner.test("with non-rooted non-anonymous class file", (Test test) ->
                {
                    test.assertThrows(() -> JavaClassFile.getFullTypeName("outputs/b/a.class", "/outputs/"),
                        new PreConditionFailure("classFilePath.isRooted() cannot be false."));
                });

                runner.test("with non-rooted anonymous class file", (Test test) ->
                {
                    test.assertThrows(() -> JavaClassFile.getFullTypeName("outputs/c/apples$20.class", "/outputs/"),
                        new PreConditionFailure("classFilePath.isRooted() cannot be false."));
                });
            });
        });
    }
}
