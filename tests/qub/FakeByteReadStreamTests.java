package qub;

public interface FakeByteReadStreamTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(FakeByteReadStream.class, () ->
        {
            runner.testGroup("create(Function0<Result<Byte>>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> FakeByteReadStream.create((Function0<Result<Byte>>)null),
                        new PreConditionFailure("readByteFunction cannot be null."));
                });

                runner.test("with non-null", (Test test) ->
                {
                    final IntegerValue value = IntegerValue.create(0);
                    final FakeByteReadStream stream = FakeByteReadStream.create(() ->
                    {
                        return Result.success((byte)value.incrementAndGetAsInt());
                    });
                    test.assertEqual(1, stream.readByte().await());
                    test.assertEqual(2, stream.readByte().await());
                    test.assertEqual(new byte[] { 3, 4, 5 }, stream.readBytes(3).await());
                    final byte[] outputBytes = new byte[4];
                    test.assertEqual(4, stream.readBytes(outputBytes).await());
                    test.assertEqual(new byte[] { 6, 7, 8, 9 }, outputBytes);
                });
            });

            runner.testGroup("create(Function3<byte[],Integer,Integer,Result<Byte>>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> FakeByteReadStream.create((Function3<byte[],Integer,Integer,Result<Integer>>)null),
                        new PreConditionFailure("readBytesFunction cannot be null."));
                });

                runner.test("with non-null", (Test test) ->
                {
                    final IntegerValue value = IntegerValue.create(0);
                    final FakeByteReadStream stream = FakeByteReadStream.create((byte[] outputBytes, Integer startIndex, Integer length) ->
                    {
                        outputBytes[startIndex] = (byte)value.incrementAndGetAsInt();
                        return Result.successOne();
                    });
                    test.assertEqual(1, stream.readByte().await());
                    test.assertEqual(2, stream.readByte().await());
                    test.assertEqual(new byte[] { 3 }, stream.readBytes(3).await());
                    final byte[] outputBytes = new byte[4];
                    test.assertEqual(1, stream.readBytes(outputBytes).await());
                    test.assertEqual(new byte[] { 4, 0, 0, 0 }, outputBytes);
                });
            });
        });
    }
}
