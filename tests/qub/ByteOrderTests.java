package qub;

public class ByteOrderTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(ByteOrder.class, () ->
        {
            runner.test("LittleEndian", (Test test) ->
            {
                test.assertNotNull(ByteOrder.LittleEndian);
            });

            runner.test("BigEndian", (Test test) ->
            {
                test.assertNotNull(ByteOrder.BigEndian);
            });

            runner.test("Network", (Test test) ->
            {
                test.assertNotNull(ByteOrder.Network);
                test.assertSame(ByteOrder.Network, ByteOrder.BigEndian);
            });

            runner.test("Java", (Test test) ->
            {
                test.assertNotNull(ByteOrder.Java);
                test.assertSame(ByteOrder.Java, ByteOrder.BigEndian);
            });

            runner.testGroup("toLittleEndianShort(short)", () ->
            {
                runner.test("with 0x0000", (Test test) ->
                {
                    test.assertEqual((short)0x0000, ByteOrder.toLittleEndianShort((short)0x0000));
                });

                runner.test("with 0x0001", (Test test) ->
                {
                    test.assertEqual((short)0x0100, ByteOrder.toLittleEndianShort((short)0x0001));
                });

                runner.test("with 0x0010", (Test test) ->
                {
                    test.assertEqual((short)0x1000, ByteOrder.toLittleEndianShort((short)0x0010));
                });

                runner.test("with 0x0100", (Test test) ->
                {
                    test.assertEqual((short)0x0001, ByteOrder.toLittleEndianShort((short)0x0100));
                });

                runner.test("with 0x1000", (Test test) ->
                {
                    test.assertEqual((short)0x0010, ByteOrder.toLittleEndianShort((short)0x1000));
                });
            });

            runner.testGroup("toLittleEndianInteger(int)", () ->
            {
                runner.test("with 0x00000000", (Test test) ->
                {
                    test.assertEqual(0x00000000, ByteOrder.toLittleEndianInteger(0x00000000));
                });

                runner.test("with 0x00000001", (Test test) ->
                {
                    test.assertEqual(0x01000000, ByteOrder.toLittleEndianInteger(0x00000001));
                });

                runner.test("with 0x00000010", (Test test) ->
                {
                    test.assertEqual(0x10000000, ByteOrder.toLittleEndianInteger(0x00000010));
                });

                runner.test("with 0x00000100", (Test test) ->
                {
                    test.assertEqual(0x00010000, ByteOrder.toLittleEndianInteger(0x00000100));
                });

                runner.test("with 0x00001000", (Test test) ->
                {
                    test.assertEqual(0x00100000, ByteOrder.toLittleEndianInteger(0x00001000));
                });

                runner.test("with 0x00010000", (Test test) ->
                {
                    test.assertEqual(0x00000100, ByteOrder.toLittleEndianInteger(0x00010000));
                });

                runner.test("with 0x00100000", (Test test) ->
                {
                    test.assertEqual(0x00001000, ByteOrder.toLittleEndianInteger(0x00100000));
                });

                runner.test("with 0x01000000", (Test test) ->
                {
                    test.assertEqual(0x00000001, ByteOrder.toLittleEndianInteger(0x01000000));
                });

                runner.test("with 0x10000000", (Test test) ->
                {
                    test.assertEqual(0x00000010, ByteOrder.toLittleEndianInteger(0x10000000));
                });
            });
        });
    }
}
