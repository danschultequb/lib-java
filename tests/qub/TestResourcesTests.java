package qub;

public interface TestResourcesTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(TestResources.class, () ->
        {
            runner.testGroup("create(DesktopProcess)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> TestResources.create(null),
                        new PreConditionFailure("process cannot be null."));
                });

                runner.test("with non-null", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        try (final TestResources resources = TestResources.create(process))
                        {
                            test.assertNotNull(resources);
                            test.assertSame(process, resources.getProcess());
                            test.assertFalse(resources.isDisposed());
                            test.assertFalse(process.isDisposed());
                        }
                        test.assertFalse(process.isDisposed());
                    }
                });
            });

            runner.test("createFakeDesktopProcess()", (Test test) ->
            {
                try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                {
                    try (final TestResources resources = TestResources.create(process))
                    {
                        final FakeDesktopProcess fakeDesktopProcess1 = resources.createFakeDesktopProcess();
                        test.assertNotNull(fakeDesktopProcess1);
                        test.assertNotSame(process, fakeDesktopProcess1);
                        test.assertFalse(fakeDesktopProcess1.isDisposed());

                        final FakeDesktopProcess fakeDesktopProcess2 = resources.createFakeDesktopProcess();
                        test.assertNotNull(fakeDesktopProcess2);
                        test.assertNotSame(process, fakeDesktopProcess2);
                        test.assertNotSame(fakeDesktopProcess1, fakeDesktopProcess2);
                        test.assertFalse(fakeDesktopProcess1.isDisposed());
                        test.assertFalse(fakeDesktopProcess2.isDisposed());

                        test.assertTrue(resources.dispose().await());
                        test.assertTrue(resources.isDisposed());
                        test.assertTrue(fakeDesktopProcess1.isDisposed());
                        test.assertTrue(fakeDesktopProcess2.isDisposed());
                        test.assertFalse(process.isDisposed());

                        test.assertThrows(() -> resources.createFakeDesktopProcess(),
                            new PreConditionFailure("this.isDisposed() cannot be true."));
                    }
                }
            });

            runner.testGroup("getProcess()", () ->
            {
                runner.test("when not disposed", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        try (final TestResources resources = TestResources.create(process))
                        {
                            final Process p = resources.getProcess();
                            test.assertSame(process, p);
                        }
                    }
                });

                runner.test("when disposed", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        try (final TestResources resources = TestResources.create(process))
                        {
                            test.assertTrue(resources.dispose().await());
                            test.assertThrows(() -> resources.getProcess(),
                                new PreConditionFailure("this.isDisposed() cannot be true."));
                        }
                    }
                });
            });

            runner.testGroup("getMainAsyncRunner()", () ->
            {
                runner.test("when not disposed", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        try (final TestResources resources = TestResources.create(process))
                        {
                            final AsyncScheduler mainAsyncRunner = resources.getMainAsyncRunner();
                            test.assertSame(process.getMainAsyncRunner(), mainAsyncRunner);
                        }
                    }
                });

                runner.test("when disposed", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        try (final TestResources resources = TestResources.create(process))
                        {
                            test.assertTrue(resources.dispose().await());
                            test.assertThrows(() -> resources.getMainAsyncRunner(),
                                new PreConditionFailure("this.isDisposed() cannot be true."));
                        }
                    }
                });
            });

            runner.testGroup("getParallelAsyncRunner()", () ->
            {
                runner.test("when not disposed", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        try (final TestResources resources = TestResources.create(process))
                        {
                            final AsyncScheduler parallelAsyncRunner = resources.getParallelAsyncRunner();
                            test.assertSame(process.getParallelAsyncRunner(), parallelAsyncRunner);
                        }
                    }
                });

                runner.test("when disposed", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        try (final TestResources resources = TestResources.create(process))
                        {
                            test.assertTrue(resources.dispose().await());
                            test.assertThrows(() -> resources.getParallelAsyncRunner(),
                                new PreConditionFailure("this.isDisposed() cannot be true."));
                        }
                    }
                });
            });

            runner.testGroup("getRandom()", () ->
            {
                runner.test("when not disposed", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        try (final TestResources resources = TestResources.create(process))
                        {
                            final Random random = resources.getRandom();
                            test.assertSame(process.getRandom(), random);
                        }
                    }
                });

                runner.test("when disposed", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        try (final TestResources resources = TestResources.create(process))
                        {
                            test.assertTrue(resources.dispose().await());
                            test.assertThrows(() -> resources.getRandom(),
                                new PreConditionFailure("this.isDisposed() cannot be true."));
                        }
                    }
                });
            });

            runner.testGroup("getFileSystem()", () ->
            {
                runner.test("when not disposed", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        try (final TestResources resources = TestResources.create(process))
                        {
                            final FileSystem fileSystem = resources.getFileSystem();
                            test.assertSame(process.getFileSystem(), fileSystem);
                        }
                    }
                });

                runner.test("when disposed", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        try (final TestResources resources = TestResources.create(process))
                        {
                            test.assertTrue(resources.dispose().await());
                            test.assertThrows(() -> resources.getFileSystem(),
                                new PreConditionFailure("this.isDisposed() cannot be true."));
                        }
                    }
                });
            });

            runner.testGroup("getCurrentFolder()", () ->
            {
                runner.test("when not disposed", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        try (final TestResources resources = TestResources.create(process))
                        {
                            final Folder currentFolder = resources.getCurrentFolder();
                            test.assertSame(process.getCurrentFolder(), currentFolder);
                        }
                    }
                });

                runner.test("when disposed", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        try (final TestResources resources = TestResources.create(process))
                        {
                            test.assertTrue(resources.dispose().await());
                            test.assertThrows(() -> resources.getCurrentFolder(),
                                new PreConditionFailure("this.isDisposed() cannot be true."));
                        }
                    }
                });
            });

            runner.testGroup("getTemporaryFolder()", () ->
            {
                runner.test("when disposed", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        try (final TestResources resources = TestResources.create(process))
                        {
                            test.assertTrue(resources.dispose().await());

                            test.assertThrows(() -> resources.getTemporaryFolder(),
                                new PreConditionFailure("this.isDisposed() cannot be true."));
                        }
                    }
                });

                runner.test("when not disposed", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        try (final TestResources resources = TestResources.create(process))
                        {
                            final TemporaryFolder tempFolder = resources.getTemporaryFolder();
                            test.assertNotNull(tempFolder);
                        }
                    }
                });
            });
        });
    }
}
