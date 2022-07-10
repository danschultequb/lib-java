package qub;

public interface LockedByteWriteStreamTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(LockedByteWriteStream.class, () ->
        {
            runner.testGroup("create(ByteWriteStream)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> LockedByteWriteStream.create(null),
                        new PreConditionFailure("innerStream cannot be null."));
                });

                runner.test("with non-null", (Test test) ->
                {
                    final InMemoryByteStream stream = InMemoryByteStream.create();
                    final LockedByteWriteStream lockedStream = LockedByteWriteStream.create(stream);
                    test.assertFalse(lockedStream.isDisposed());
                    test.assertEqual(new byte[0], stream.getBytes());
                });

                runner.test("with disposed", (Test test) ->
                {
                    final InMemoryByteStream stream = InMemoryByteStream.create();
                    stream.dispose().await();
                    final LockedByteWriteStream lockedStream = LockedByteWriteStream.create(stream);
                    test.assertTrue(lockedStream.isDisposed());
                    test.assertEqual(new byte[0], stream.getBytes());
                });
            });

            runner.testGroup("create(ByteWriteStream,Mutex)", () ->
            {
                runner.test("with null stream", (Test test) ->
                {
                    test.assertThrows(() -> LockedByteWriteStream.create(null, SpinMutex.create()),
                        new PreConditionFailure("innerStream cannot be null."));
                });

                runner.test("with null mutex", (Test test) ->
                {
                    test.assertThrows(() -> LockedByteWriteStream.create(null, SpinMutex.create()),
                        new PreConditionFailure("innerStream cannot be null."));
                });

                runner.test("with non-null stream and mutex", (Test test) ->
                {
                    final InMemoryByteStream stream = InMemoryByteStream.create();
                    final Mutex mutex = SpinMutex.create();
                    final LockedByteWriteStream lockedStream = LockedByteWriteStream.create(stream, mutex);
                    test.assertFalse(lockedStream.isDisposed());
                    test.assertEqual(new byte[0], stream.getBytes());
                });

                runner.test("with disposed stream", (Test test) ->
                {
                    final InMemoryByteStream stream = InMemoryByteStream.create();
                    stream.dispose().await();
                    final Mutex mutex = SpinMutex.create();
                    final LockedByteWriteStream lockedStream = LockedByteWriteStream.create(stream, mutex);
                    test.assertTrue(lockedStream.isDisposed());
                    test.assertEqual(new byte[0], stream.getBytes());
                });
            });

            runner.testGroup("close()", () ->
            {
                runner.test("when inner stream is closed", (Test test) ->
                {
                    final InMemoryByteStream stream = InMemoryByteStream.create();
                    final LockedByteWriteStream lockedStream = LockedByteWriteStream.create(stream);
                    stream.close();
                    test.assertTrue(stream.isDisposed());
                    test.assertTrue(lockedStream.isDisposed());
                    test.assertEqual(new byte[0], stream.getBytes());
                    stream.close();
                    test.assertTrue(stream.isDisposed());
                    test.assertTrue(lockedStream.isDisposed());
                    test.assertEqual(new byte[0], stream.getBytes());
                });

                runner.test("when locked stream is closed", (Test test) ->
                {
                    final InMemoryByteStream stream = InMemoryByteStream.create();
                    final LockedByteWriteStream lockedStream = LockedByteWriteStream.create(stream);
                    lockedStream.close();
                    test.assertTrue(stream.isDisposed());
                    test.assertTrue(lockedStream.isDisposed());
                    test.assertEqual(new byte[0], stream.getBytes());
                    lockedStream.close();
                    test.assertTrue(stream.isDisposed());
                    test.assertTrue(lockedStream.isDisposed());
                    test.assertEqual(new byte[0], stream.getBytes());
                });
            });

            runner.testGroup("write(byte)", () ->
            {
                runner.test("with 1 byte", (Test test) ->
                {
                    final InMemoryByteStream stream = InMemoryByteStream.create();
                    final LockedByteWriteStream lockedStream = LockedByteWriteStream.create(stream);
                    test.assertEqual(1, lockedStream.write((byte)17).await());
                    test.assertEqual(new byte[] { 17 }, stream.getBytes());
                });

                final int byteCount = 400000;
                runner.speedTest("with " + byteCount + " bytes", Duration.milliseconds(100), (Test test) ->
                {
                    final InMemoryByteStream innerStream = InMemoryByteStream.create();
                    final BufferedByteWriteStream writeStream = ByteWriteStream.buffer(innerStream);
                    final LockedByteWriteStream lockedStream = LockedByteWriteStream.create(writeStream);

                    for (int i = 0; i < byteCount; ++i)
                    {
                        test.assertEqual(1, lockedStream.write((byte)42).await());
                    }
                    test.assertTrue(lockedStream.dispose().await());
                    test.assertEqual(byteCount, innerStream.getCount());
                });
            });

            runner.testGroup("write(byte[])", () ->
            {
                final Action2<byte[],Throwable> writeErrorTest = (byte[] toWrite, Throwable expected) ->
                {
                    runner.test("with " + Array.toString(toWrite), (Test test) ->
                    {
                        final InMemoryByteStream stream = InMemoryByteStream.create();
                        final LockedByteWriteStream lockedStream = LockedByteWriteStream.create(stream);

                        test.assertThrows(() -> lockedStream.write(toWrite).await(),
                            expected);

                        test.assertEqual(new byte[0], stream.getBytes());
                    });
                };

                writeErrorTest.run(null, new PreConditionFailure("toWrite cannot be null."));
                writeErrorTest.run(new byte[0], new PreConditionFailure("toWrite cannot be empty."));

                final Action1<byte[]> writeTest = (byte[] toWrite) ->
                {
                    runner.test("with " + Array.toString(toWrite), (Test test) ->
                    {
                        final InMemoryByteStream stream = InMemoryByteStream.create();
                        final LockedByteWriteStream lockedStream = LockedByteWriteStream.create(stream);

                        test.assertEqual(toWrite.length, lockedStream.write(toWrite).await());

                        test.assertEqual(toWrite, stream.getBytes());
                    });
                };

                writeTest.run(new byte[] { 1, 2, 3, 4 });
            });

            runner.testGroup("writeBytes(byte[],int,int)", () ->
            {
                final Action4<byte[],Integer,Integer,Throwable> writeErrorTest = (byte[] toWrite, Integer startIndex, Integer length, Throwable expected) ->
                {
                    runner.test("with " + English.andList(Array.toString(toWrite), startIndex, length), (Test test) ->
                    {
                        final InMemoryByteStream stream = InMemoryByteStream.create();
                        final LockedByteWriteStream lockedStream = LockedByteWriteStream.create(stream);

                        test.assertThrows(() -> lockedStream.write(toWrite, startIndex, length).await(),
                            expected);

                        test.assertEqual(new byte[0], stream.getBytes());
                    });
                };

                writeErrorTest.run(null, 0, 0, new PreConditionFailure("bytes cannot be null."));
                writeErrorTest.run(new byte[] { 1 }, -1, -1, new PreConditionFailure("startIndex (-1) must be equal to 0."));
                writeErrorTest.run(new byte[] { 1 }, 1, -1, new PreConditionFailure("startIndex (1) must be equal to 0."));
                writeErrorTest.run(new byte[] { 1 }, 0, -1, new PreConditionFailure("length (-1) must be between 0 and 1."));
                writeErrorTest.run(new byte[] { 1 }, 0, 2, new PreConditionFailure("length (2) must be between 0 and 1."));
                writeErrorTest.run(new byte[] { 1, 2 }, -1, -1, new PreConditionFailure("startIndex (-1) must be between 0 and 1."));

                final Action4<byte[],Integer,Integer,byte[]> writeTest = (byte[] toWrite, Integer startIndex, Integer length, byte[] expected) ->
                {
                    runner.test("with " + English.andList(Array.toString(toWrite), startIndex, length), (Test test) ->
                    {
                        final InMemoryByteStream stream = InMemoryByteStream.create();
                        final LockedByteWriteStream lockedStream = LockedByteWriteStream.create(stream);

                        test.assertEqual(length, lockedStream.write(toWrite, startIndex, length).await());

                        test.assertEqual(expected, stream.getBytes());
                    });
                };

                writeTest.run(new byte[] {}, 0, 0, new byte[] {});
                writeTest.run(new byte[] { 1 }, 0, 0, new byte[] {});
                writeTest.run(new byte[] { 0, 1, 2, 3, 4 }, 1, 2, new byte[] { 1, 2 });
            });

            runner.testGroup("writeAll(ByteReadStream)", () ->
            {
                runner.test("with null ByteReadStream", (Test test) ->
                {
                    final InMemoryByteStream stream = InMemoryByteStream.create();
                    final LockedByteWriteStream lockedStream = LockedByteWriteStream.create(stream);
                    test.assertThrows(() -> lockedStream.writeAll((ByteReadStream)null).await(),
                        new PreConditionFailure("byteReadStream cannot be null."));
                    test.assertEqual(new byte[0], stream.getBytes());
                });

                runner.test("with disposed ByteReadStream", (Test test) ->
                {
                    final InMemoryByteStream stream = InMemoryByteStream.create();
                    final LockedByteWriteStream lockedStream = LockedByteWriteStream.create(stream);
                    final InMemoryByteStream readStream = InMemoryByteStream.create();
                    readStream.dispose().await();
                    test.assertThrows(() -> lockedStream.writeAll(readStream).await(),
                        new PreConditionFailure("byteReadStream.isDisposed() cannot be true."));
                    test.assertEqual(new byte[0], stream.getBytes());
                });

                runner.test("with disposed ByteWriteStream", (Test test) ->
                {
                    final InMemoryByteStream stream = InMemoryByteStream.create();
                    final LockedByteWriteStream lockedStream = LockedByteWriteStream.create(stream);
                    lockedStream.dispose().await();
                    final InMemoryByteStream readStream = InMemoryByteStream.create().endOfStream();
                    test.assertThrows(() -> lockedStream.writeAll(readStream).await(),
                        new PreConditionFailure("this.isDisposed() cannot be true."));
                    test.assertEqual(new byte[0], stream.getBytes());
                });

                runner.test("with empty ByteReadStream", (Test test) ->
                {
                    final InMemoryByteStream stream = InMemoryByteStream.create();
                    final LockedByteWriteStream lockedStream = LockedByteWriteStream.create(stream);
                    final InMemoryByteStream readStream = InMemoryByteStream.create().endOfStream();
                    test.assertEqual(0, lockedStream.writeAll(readStream).await());
                    test.assertEqual(new byte[0], stream.getBytes());
                });

                runner.test("with non-empty ByteReadStream", (Test test) ->
                {
                    final InMemoryByteStream stream = InMemoryByteStream.create();
                    final LockedByteWriteStream lockedStream = LockedByteWriteStream.create(stream);
                    final InMemoryByteStream readStream = InMemoryByteStream.create(new byte[] { 0, 1, 2, 3 }).endOfStream();
                    test.assertEqual(4, lockedStream.writeAll(readStream).await());
                    test.assertEqual(new byte[] { 0, 1, 2, 3 }, stream.getBytes());
                });
            });
        });
    }
}
