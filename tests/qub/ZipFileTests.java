package qub;

public interface ZipFileTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(ZipFile.class, () ->
        {
            runner.testGroup("get(File)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> ZipFile.get(null),
                        new PreConditionFailure("file cannot be null."));
                });

                runner.test("with non-null",
                    (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                    (Test test, FakeDesktopProcess process) ->
                {
                    final Folder currentFolder = process.getCurrentFolder();
                    final File file = currentFolder.getFile("test.zip").await();

                    final ZipFile zipFile = ZipFile.get(file);

                    test.assertNotNull(zipFile);
                    test.assertEqual(file, zipFile);
                });
            });

            runner.testGroup("iterateEntries()", () ->
            {
                runner.test("with file that doesn't exist",
                    (TestResources resources) -> Tuple.create(resources.getTemporaryFolder()),
                    (Test test, Folder temporaryFolder) ->
                {
                    final ZipFile file = ZipFile.get(temporaryFolder.getFile("test.zip").await());
                    
                    final ZipEntryIterator iterator = file.iterateEntries();
                    IteratorTests.assertIterator(test, iterator, false, null);
                    test.assertFalse(iterator.isDisposed());

                    test.assertThrows(() -> iterator.next(),
                        new FileNotFoundException(file));

                    IteratorTests.assertIterator(test, iterator, false, null);
                    test.assertFalse(iterator.isDisposed());
                });

                runner.test("with empty file",
                    (TestResources resources) -> Tuple.create(resources.getTemporaryFolder()),
                    (Test test, Folder temporaryFolder) ->
                {
                    final ZipFile file = ZipFile.get(temporaryFolder.getFile("test.zip").await());
                    file.create().await();
                    
                    final ZipEntryIterator iterator = file.iterateEntries();
                    IteratorTests.assertIterator(test, iterator, false, null);
                    test.assertFalse(iterator.isDisposed());

                    test.assertFalse(iterator.next());
                    
                    IteratorTests.assertIterator(test, iterator, true, null);
                    test.assertTrue(iterator.isDisposed());
                });

                runner.test("with non-empty non-zip file",
                    (TestResources resources) -> Tuple.create(resources.getTemporaryFolder()),
                    (Test test, Folder temporaryFolder) ->
                {
                    final ZipFile file = ZipFile.get(temporaryFolder.getFile("test.zip").await());
                    file.setContentsAsString("I'm not a zip file!").await();
                    
                    final ZipEntryIterator iterator = file.iterateEntries();
                    IteratorTests.assertIterator(test, iterator, false, null);
                    test.assertFalse(iterator.isDisposed());

                    test.assertFalse(iterator.next());

                    IteratorTests.assertIterator(test, iterator, true, null);
                    test.assertTrue(iterator.isDisposed());
                });

                runner.test("with actual zip file",
                    (TestResources resources) -> Tuple.create(resources.getFileSystem()),
                    (Test test, FileSystem fileSystem) ->
                {
                    final ZipFile zipFile = ZipFile.get(fileSystem.getFile("C:/qub/qub/lib-java/versions/178/lib-java-sources.zip").await());
                    try (final ZipEntryIterator iterator = zipFile.iterateEntries())
                    {
                        IteratorTests.assertIterator(test, iterator, false, null);
                        test.assertFalse(iterator.isDisposed());

                        for (final ZipEntryCharacterToByteReadStream entry : iterator)
                        {
                            test.assertNotNull(entry);
                            IteratorTests.assertIterator(test, iterator, true, entry);
                        }

                        IteratorTests.assertIterator(test, iterator, true, null);
                        test.assertTrue(iterator.isDisposed());
                    }
                });
            });
        });
    }
}
