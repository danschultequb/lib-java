package qub;

public interface BasicCharacterToByteWriteStreamTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(BasicCharacterToByteWriteStream.class, () ->
        {
            runner.testGroup("write(char)", () ->
            {
                runner.test("when not disposed", (Test test) ->
                {
                    final InMemoryByteStream byteStream = new InMemoryByteStream();
                    final BasicCharacterToByteWriteStream characterStream = new BasicCharacterToByteWriteStream(byteStream);
                    test.assertEqual(1, characterStream.write('f').await());
                    test.assertEqual(new byte[] { 102 }, byteStream.getBytes());
                });

                runner.test("when disposed", (Test test) ->
                {
                    final InMemoryByteStream byteStream = new InMemoryByteStream();
                    final BasicCharacterToByteWriteStream characterStream = new BasicCharacterToByteWriteStream(byteStream);
                    test.assertTrue(characterStream.dispose().await());
                    test.assertThrows(() -> characterStream.write('f'),
                        new PreConditionFailure("this.isDisposed() cannot be true."));
                    test.assertEqual(new byte[0], byteStream.getBytes());
                });
            });

            runner.testGroup("write(char[])", () ->
            {
                runner.test("with null characters", (Test test) ->
                {
                    final InMemoryByteStream byteStream = new InMemoryByteStream();
                    final BasicCharacterToByteWriteStream characterStream = new BasicCharacterToByteWriteStream(byteStream);
                    test.assertThrows(() -> characterStream.write((char[])null),
                        new PreConditionFailure("toWrite cannot be null."));
                    test.assertEqual(new byte[0], byteStream.getBytes());
                });

                runner.test("with empty characters", (Test test) ->
                {
                    final InMemoryByteStream byteStream = new InMemoryByteStream();
                    final BasicCharacterToByteWriteStream characterStream = new BasicCharacterToByteWriteStream(byteStream);
                    test.assertEqual(0, characterStream.write(new char[0]).await());
                    test.assertEqual(new byte[0], byteStream.getBytes());
                });

                runner.test("when disposed", (Test test) ->
                {
                    final InMemoryByteStream byteStream = new InMemoryByteStream();
                    final BasicCharacterToByteWriteStream characterStream = new BasicCharacterToByteWriteStream(byteStream);
                    test.assertTrue(characterStream.dispose().await());
                    test.assertThrows(() -> characterStream.write(new char[] { 'f' }),
                        new PreConditionFailure("this.isDisposed() cannot be true."));
                    test.assertEqual(new byte[0], byteStream.getBytes());
                });

                runner.test("with non-empty characters", (Test test) ->
                {
                    final InMemoryByteStream byteStream = new InMemoryByteStream();
                    final BasicCharacterToByteWriteStream characterStream = new BasicCharacterToByteWriteStream(byteStream);
                    test.assertEqual(3, characterStream.write(new char[] { 'l', 'o', 'l' }).await());
                    test.assertEqual(new byte[] { 108, 111, 108 }, byteStream.getBytes());
                });
            });

            runner.testGroup("write(char[],int,int)", () ->
            {
                runner.test("with null characters", (Test test) ->
                {
                    final InMemoryByteStream byteStream = new InMemoryByteStream();
                    final BasicCharacterToByteWriteStream characterStream = new BasicCharacterToByteWriteStream(byteStream);
                    test.assertThrows(() -> characterStream.write((char[])null, 0, 0),
                        new PreConditionFailure("toWrite cannot be null."));
                    test.assertEqual(new byte[0], byteStream.getBytes());
                });

                runner.test("with empty characters", (Test test) ->
                {
                    final InMemoryByteStream byteStream = new InMemoryByteStream();
                    final BasicCharacterToByteWriteStream characterStream = new BasicCharacterToByteWriteStream(byteStream);
                    test.assertEqual(0, characterStream.write(new char[0], 0, 0).await());
                    test.assertEqual(new byte[0], byteStream.getBytes());
                });

                runner.test("with negative start index", (Test test) ->
                {
                    final InMemoryByteStream byteStream = new InMemoryByteStream();
                    final BasicCharacterToByteWriteStream characterStream = new BasicCharacterToByteWriteStream(byteStream);
                    test.assertThrows(() -> characterStream.write(new char[] { 'a', 'b', 'c' }, -1, 2),
                        new PreConditionFailure("startIndex (-1) must be between 0 and 2."));
                    test.assertEqual(new byte[0], byteStream.getBytes());
                });

                runner.test("with start index equal to toWrite.length", (Test test) ->
                {
                    final InMemoryByteStream byteStream = new InMemoryByteStream();
                    final BasicCharacterToByteWriteStream characterStream = new BasicCharacterToByteWriteStream(byteStream);
                    test.assertThrows(() -> characterStream.write(new char[] { 'a', 'b', 'c' }, 3, 2),
                        new PreConditionFailure("startIndex (3) must be between 0 and 2."));
                    test.assertEqual(new byte[0], byteStream.getBytes());
                });

                runner.test("with start index greater than toWrite.length", (Test test) ->
                {
                    final InMemoryByteStream byteStream = new InMemoryByteStream();
                    final BasicCharacterToByteWriteStream characterStream = new BasicCharacterToByteWriteStream(byteStream);
                    test.assertThrows(() -> characterStream.write(new char[] { 'a', 'b', 'c' }, 4, 2),
                        new PreConditionFailure("startIndex (4) must be between 0 and 2."));
                    test.assertEqual(new byte[0], byteStream.getBytes());
                });

                runner.test("with negative length", (Test test) ->
                {
                    final InMemoryByteStream byteStream = new InMemoryByteStream();
                    final BasicCharacterToByteWriteStream characterStream = new BasicCharacterToByteWriteStream(byteStream);
                    test.assertThrows(() -> characterStream.write(new char[] { 'a', 'b', 'c' }, 1, -1),
                        new PreConditionFailure("length (-1) must be between 0 and 2."));
                    test.assertEqual(new byte[0], byteStream.getBytes());
                });

                runner.test("with length greater than toWrite.length - startIndex", (Test test) ->
                {
                    final InMemoryByteStream byteStream = new InMemoryByteStream();
                    final BasicCharacterToByteWriteStream characterStream = new BasicCharacterToByteWriteStream(byteStream);
                    test.assertThrows(() -> characterStream.write(new char[] { 'a', 'b', 'c' }, 1, 3),
                        new PreConditionFailure("length (3) must be between 0 and 2."));
                    test.assertEqual(new byte[0], byteStream.getBytes());
                });

                runner.test("when disposed", (Test test) ->
                {
                    final InMemoryByteStream byteStream = new InMemoryByteStream();
                    final BasicCharacterToByteWriteStream characterStream = new BasicCharacterToByteWriteStream(byteStream);
                    test.assertTrue(characterStream.dispose().await());
                    test.assertThrows(() -> characterStream.write(new char[] { 'f' }, 0, 1),
                        new PreConditionFailure("this.isDisposed() cannot be true."));
                    test.assertEqual(new byte[0], byteStream.getBytes());
                });

                runner.test("with non-empty characters and not disposed", (Test test) ->
                {
                    final InMemoryByteStream byteStream = new InMemoryByteStream();
                    final BasicCharacterToByteWriteStream characterStream = new BasicCharacterToByteWriteStream(byteStream);
                    test.assertEqual(2, characterStream.write(new char[] { 'l', 'o', 'l' }, 0, 2).await());
                    test.assertEqual(new byte[] { 108, 111 }, byteStream.getBytes());
                });
            });

            runner.testGroup("write(String,Object...)", () ->
            {
                runner.test("with null String", (Test test) ->
                {
                    final InMemoryByteStream byteStream = new InMemoryByteStream();
                    final BasicCharacterToByteWriteStream characterStream = new BasicCharacterToByteWriteStream(byteStream);
                    test.assertThrows(() -> characterStream.write((String)null),
                        new PreConditionFailure("toWrite cannot be null."));
                    test.assertEqual(new byte[0], byteStream.getBytes());
                });

                runner.test("with empty String", (Test test) ->
                {
                    final InMemoryByteStream byteStream = new InMemoryByteStream();
                    final BasicCharacterToByteWriteStream characterStream = new BasicCharacterToByteWriteStream(byteStream);
                    test.assertEqual(0, characterStream.write("").await());
                    test.assertEqual(new byte[0], byteStream.getBytes());
                });

                runner.test("with non-empty String", (Test test) ->
                {
                    final InMemoryByteStream byteStream = new InMemoryByteStream();
                    final BasicCharacterToByteWriteStream characterStream = new BasicCharacterToByteWriteStream(byteStream);
                    test.assertEqual(3, characterStream.write("abc").await());
                    test.assertEqual(new byte[] { 97, 98, 99 }, byteStream.getBytes());
                });

                runner.test("when disposed", (Test test) ->
                {
                    final InMemoryByteStream byteStream = new InMemoryByteStream();
                    final BasicCharacterToByteWriteStream characterStream = new BasicCharacterToByteWriteStream(byteStream);
                    test.assertTrue(characterStream.dispose().await());
                    test.assertThrows(() -> characterStream.write("abc"),
                        new PreConditionFailure("this.isDisposed() cannot be true."));
                    test.assertEqual(new byte[0], byteStream.getBytes());
                });
            });
        });
    }
}
