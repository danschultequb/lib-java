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
                final Action2<Short,Short> toLittleEndianShortTest = (Short value, Short expected) ->
                {
                    runner.test("with 0x" + Shorts.toHexString(value), (Test test) ->
                    {
                        test.assertEqual(Shorts.toHexString(expected), Shorts.toHexString(ByteOrder.toLittleEndianShort(value)));
                    });
                };

                toLittleEndianShortTest.run((short)0x0000, (short)0x0000);
                toLittleEndianShortTest.run((short)0x0001, (short)0x0100);
                toLittleEndianShortTest.run((short)0x0010, (short)0x1000);
                toLittleEndianShortTest.run((short)0x0100, (short)0x0001);
                toLittleEndianShortTest.run((short)0x1000, (short)0x0010);
            });

            runner.testGroup("toLittleEndianInteger(int)", () ->
            {
                final Action2<Integer,Integer> toLittleEndianIntegerTest = (Integer value, Integer expected) ->
                {
                    runner.test("with 0x" + Integers.toHexString(value), (Test test) ->
                    {
                        test.assertEqual(Integers.toHexString(expected), Integers.toHexString(ByteOrder.toLittleEndianInteger(value)));
                    });
                };

                toLittleEndianIntegerTest.run(0x00000000, 0x00000000);
                toLittleEndianIntegerTest.run(0x00000001, 0x01000000);
                toLittleEndianIntegerTest.run(0x00000010, 0x10000000);
                toLittleEndianIntegerTest.run(0x00000100, 0x00010000);
                toLittleEndianIntegerTest.run(0x00001000, 0x00100000);
                toLittleEndianIntegerTest.run(0x00010000, 0x00000100);
                toLittleEndianIntegerTest.run(0x00100000, 0x00001000);
                toLittleEndianIntegerTest.run(0x01000000, 0x00000001);
                toLittleEndianIntegerTest.run(0x10000000, 0x00000010);
            });

            runner.testGroup("toLittleEndianLong(long)", () ->
            {
                final Action2<Long,Long> toLittleEndianLongTest = (Long value, Long expected) ->
                {
                    runner.test("with 0x" + Longs.toHexString(value), (Test test) ->
                    {
                        test.assertEqual(Longs.toHexString(expected), Longs.toHexString(ByteOrder.toLittleEndianLong(value)));
                    });
                };

                toLittleEndianLongTest.run(0x0000000000000000L, 0x0000000000000000L);
                toLittleEndianLongTest.run(0x0000000000000001L, 0x0100000000000000L);
                toLittleEndianLongTest.run(0x0000000000000010L, 0x1000000000000000L);
                toLittleEndianLongTest.run(0x0000000000000100L, 0x0001000000000000L);
                toLittleEndianLongTest.run(0x0000000000001000L, 0x0010000000000000L);
                toLittleEndianLongTest.run(0x0000000000010000L, 0x0000010000000000L);
                toLittleEndianLongTest.run(0x0000000000100000L, 0x0000100000000000L);
                toLittleEndianLongTest.run(0x0000000001000000L, 0x0000000100000000L);
                toLittleEndianLongTest.run(0x0000000010000000L, 0x0000001000000000L);
                toLittleEndianLongTest.run(0x0000000100000000L, 0x0000000001000000L);
                toLittleEndianLongTest.run(0x0000001000000000L, 0x0000000010000000L);
                toLittleEndianLongTest.run(0x0000010000000000L, 0x0000000000010000L);
                toLittleEndianLongTest.run(0x0000100000000000L, 0x0000000000100000L);
                toLittleEndianLongTest.run(0x0001000000000000L, 0x0000000000000100L);
                toLittleEndianLongTest.run(0x0010000000000000L, 0x0000000000001000L);
                toLittleEndianLongTest.run(0x0100000000000000L, 0x0000000000000001L);
                toLittleEndianLongTest.run(0x1000000000000000L, 0x0000000000000010L);
            });
        });
    }
}
