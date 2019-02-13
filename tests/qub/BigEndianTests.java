package qub;

public class BigEndianTests
{
    private static final ByteOrder byteOrder = new BigEndian();

    public static void test(TestRunner runner)
    {
        runner.testGroup(BigEndian.class, () ->
        {
            runner.testGroup("encodeShort(short)", () ->
            {
                final Action2<Short,byte[]> encodeShortTest = (Short value, byte[] expected) ->
                {
                    runner.test("with " + value, (Test test) ->
                    {
                        test.assertEqual(expected, byteOrder.encodeShort(value));
                    });
                };

                encodeShortTest.run((short)0, new byte[] { 0, 0 });
                encodeShortTest.run((short)1, new byte[] { 0, 1 });
                encodeShortTest.run((short)16, new byte[] { 0, 16 });
                encodeShortTest.run((short)128, new byte[] { 0, -128 });
                encodeShortTest.run((short)256, new byte[] { 1, 0 });
                encodeShortTest.run((short)1024, new byte[] { 4, 0 });
                encodeShortTest.run((short)2048, new byte[] { 8, 0 });
                encodeShortTest.run((short)4096, new byte[] { 16, 0 });
                encodeShortTest.run((short)8192, new byte[] { 32, 0 });
                encodeShortTest.run((short)32768, new byte[] { -128, 0 });
                encodeShortTest.run((short)-1, new byte[] { -1, -1 });
            });

            runner.testGroup("encodeInteger(int)", () ->
            {
                final Action2<Integer,byte[]> encodeIntegerTest = (Integer value, byte[] expected) ->
                {
                    runner.test("with " + value, (Test test) ->
                    {
                        test.assertEqual(expected, byteOrder.encodeInteger(value));
                    });
                };

                encodeIntegerTest.run(0, new byte[] { 0, 0, 0, 0 });
                encodeIntegerTest.run(1, new byte[] { 0, 0, 0, 1 });
                encodeIntegerTest.run(16, new byte[] { 0, 0, 0, 16 });
                encodeIntegerTest.run(64, new byte[] { 0, 0, 0, 64 });
                encodeIntegerTest.run(128, new byte[] { 0, 0, 0, -128 });
                encodeIntegerTest.run(256, new byte[] { 0, 0, 1, 0 });
                encodeIntegerTest.run(1024, new byte[] { 0, 0, 4, 0 });
                encodeIntegerTest.run(2048, new byte[] { 0, 0, 8, 0 });
                encodeIntegerTest.run(4096, new byte[] { 0, 0, 16, 0 });
                encodeIntegerTest.run(8192, new byte[] { 0, 0, 32, 0 });
                encodeIntegerTest.run(16384, new byte[] { 0, 0, 64, 0 });
                encodeIntegerTest.run(32768, new byte[] { 0, 0, -128, 0 });
                encodeIntegerTest.run(65536, new byte[] { 0, 1, 0, 0 });
                encodeIntegerTest.run(1048576, new byte[] { 0, 16, 0, 0 });
                encodeIntegerTest.run(4194304, new byte[] { 0, 64, 0, 0 });
                encodeIntegerTest.run(8388608, new byte[] { 0, -128, 0, 0 });
                encodeIntegerTest.run(16777216, new byte[] { 1, 0, 0, 0 });
                encodeIntegerTest.run(268435456, new byte[] { 16, 0, 0, 0 });
                encodeIntegerTest.run(1073741824, new byte[] { 64, 0, 0, 0 });
                encodeIntegerTest.run(-2147483648, new byte[] { -128, 0, 0, 0 });
                encodeIntegerTest.run(-1, new byte[] { -1, -1, -1, -1 });
            });

            runner.testGroup("encodeLong(long)", () ->
            {
                final Action2<Long,byte[]> encodeLongTest = (Long value, byte[] expected) ->
                {
                    runner.test("with " + value, (Test test) ->
                    {
                        test.assertEqual(expected, byteOrder.encodeLong(value));
                    });
                };

                encodeLongTest.run(0L, new byte[] { 0, 0, 0, 0, 0, 0, 0, 0 });
                encodeLongTest.run(1L, new byte[] { 0, 0, 0, 0, 0, 0, 0, 1 });
                encodeLongTest.run(16L, new byte[] { 0, 0, 0, 0, 0, 0, 0, 16 });
                encodeLongTest.run(64L, new byte[] { 0, 0, 0, 0, 0, 0, 0, 64 });
                encodeLongTest.run(128L, new byte[] { 0, 0, 0, 0, 0, 0, 0, -128 });
                encodeLongTest.run(256L, new byte[] { 0, 0, 0, 0, 0, 0, 1, 0 });
                encodeLongTest.run(1024L, new byte[] { 0, 0, 0, 0, 0, 0, 4, 0 });
                encodeLongTest.run(2048L, new byte[] { 0, 0, 0, 0, 0, 0, 8, 0 });
                encodeLongTest.run(4096L, new byte[] { 0, 0, 0, 0, 0, 0, 16, 0 });
                encodeLongTest.run(8192L, new byte[] { 0, 0, 0, 0, 0, 0, 32, 0 });
                encodeLongTest.run(16384L, new byte[] { 0, 0, 0, 0, 0, 0, 64, 0 });
                encodeLongTest.run(32768L, new byte[] { 0, 0, 0, 0, 0, 0, -128, 0 });
                encodeLongTest.run(65536L, new byte[] { 0, 0, 0, 0, 0, 1, 0, 0 });
                encodeLongTest.run(1048576L, new byte[] { 0, 0, 0, 0, 0, 16, 0, 0 });
                encodeLongTest.run(4194304L, new byte[] { 0, 0, 0, 0, 0, 64, 0, 0 });
                encodeLongTest.run(8388608L, new byte[] { 0, 0, 0, 0, 0, -128, 0, 0 });
                encodeLongTest.run(16777216L, new byte[] { 0, 0, 0, 0, 1, 0, 0, 0 });
                encodeLongTest.run(268435456L, new byte[] { 0, 0, 0, 0, 16, 0, 0, 0 });
                encodeLongTest.run(1073741824L, new byte[] { 0, 0, 0, 0, 64, 0, 0, 0 });
                encodeLongTest.run(2147483648L, new byte[] { 0, 0, 0, 0, -128, 0, 0, 0 });
                encodeLongTest.run(-1L, new byte[] { -1, -1, -1, -1, -1, -1, -1, -1 });
            });

            runner.testGroup("encodeFloat(float)", () ->
            {
                final Action2<Float,byte[]> encodeFloatTest = (Float value, byte[] expected) ->
                {
                    runner.test("with " + value, (Test test) ->
                    {
                        test.assertEqual(expected, byteOrder.encodeFloat(value));
                    });
                };

                encodeFloatTest.run(0f, new byte[] { 0, 0, 0, 0 });
                encodeFloatTest.run(1f, new byte[] { 63, -128, 0, 0 });
                encodeFloatTest.run(16f, new byte[] { 65, -128, 0, 0 });
                encodeFloatTest.run(64f, new byte[] { 66, -128, 0, 0 });
                encodeFloatTest.run(128f, new byte[] { 67, 0, 0, 0 });
                encodeFloatTest.run(256f, new byte[] { 67, -128, 0, 0 });
                encodeFloatTest.run(1024f, new byte[] { 68, -128, 0, 0 });
                encodeFloatTest.run(2048f, new byte[] { 69, 0, 0, 0 });
                encodeFloatTest.run(4096f, new byte[] { 69, -128, 0, 0 });
                encodeFloatTest.run(8192f, new byte[] { 70, 0, 0, 0 });
                encodeFloatTest.run(16384f, new byte[] { 70, -128, 0, 0 });
                encodeFloatTest.run(32768f, new byte[] { 71, 0, 0, 0 });
                encodeFloatTest.run(65536f, new byte[] { 71, -128, 0, 0 });
                encodeFloatTest.run(1048576f, new byte[] { 73, -128, 0, 0 });
                encodeFloatTest.run(4194304f, new byte[] { 74, -128, 0, 0 });
                encodeFloatTest.run(8388608f, new byte[] { 75, 0, 0, 0 });
                encodeFloatTest.run(16777216f, new byte[] { 75, -128, 0, 0 });
                encodeFloatTest.run(268435456f, new byte[] { 77, -128, 0, 0 });
                encodeFloatTest.run(1073741824f, new byte[] { 78, -128, 0, 0 });
                encodeFloatTest.run(2147483648f, new byte[] { 79, 0, 0, 0 });
                encodeFloatTest.run(-1f, new byte[] { -65, -128, 0, 0 });
            });

            runner.testGroup("encodeDouble(double)", () ->
            {
                final Action2<Double,byte[]> encodeDoubleTest = (Double value, byte[] expected) ->
                {
                    runner.test("with " + value, (Test test) ->
                    {
                        test.assertEqual(expected, byteOrder.encodeDouble(value));
                    });
                };

                encodeDoubleTest.run(0.0, new byte[] { 0, 0, 0, 0, 0, 0, 0, 0 });
                encodeDoubleTest.run(1.0, new byte[] { 63, -16, 0, 0, 0, 0, 0, 0 });
                encodeDoubleTest.run(16.0, new byte[] { 64, 48, 0, 0, 0, 0, 0, 0 });
                encodeDoubleTest.run(64.0, new byte[] { 64, 80, 0, 0, 0, 0, 0, 0 });
                encodeDoubleTest.run(128.0, new byte[] { 64, 96, 0, 0, 0, 0, 0, 0 });
                encodeDoubleTest.run(256.0, new byte[] { 64, 112, 0, 0, 0, 0, 0, 0 });
                encodeDoubleTest.run(1024.0, new byte[] { 64, -112, 0, 0, 0, 0, 0, 0 });
                encodeDoubleTest.run(2048.0, new byte[] { 64, -96, 0, 0, 0, 0, 0, 0 });
                encodeDoubleTest.run(4096.0, new byte[] { 64, -80, 0, 0, 0, 0, 0, 0 });
                encodeDoubleTest.run(8192.0, new byte[] { 64, -64, 0, 0, 0, 0, 0, 0 });
                encodeDoubleTest.run(16384.0, new byte[] { 64, -48, 0, 0, 0, 0, 0, 0 });
                encodeDoubleTest.run(32768.0, new byte[] { 64, -32, 0, 0, 0, 0, 0, 0 });
                encodeDoubleTest.run(65536.0, new byte[] { 64, -16, 0, 0, 0, 0, 0, 0 });
                encodeDoubleTest.run(1048576.0, new byte[] { 65, 48, 0, 0, 0, 0, 0, 0 });
                encodeDoubleTest.run(4194304.0, new byte[] { 65, 80, 0, 0, 0, 0, 0, 0 });
                encodeDoubleTest.run(8388608.0, new byte[] { 65, 96, 0, 0, 0, 0, 0, 0 });
                encodeDoubleTest.run(16777216.0, new byte[] { 65, 112, 0, 0, 0, 0, 0, 0 });
                encodeDoubleTest.run(268435456.0, new byte[] { 65, -80, 0, 0, 0, 0, 0, 0 });
                encodeDoubleTest.run(1073741824.0, new byte[] { 65, -48, 0, 0, 0, 0, 0, 0 });
                encodeDoubleTest.run(2147483648.0, new byte[] { 65, -32, 0, 0, 0, 0, 0, 0 });
                encodeDoubleTest.run(-1.0, new byte[] { -65, -16, 0, 0, 0, 0, 0, 0 });
            });

            runner.testGroup("encodeShortArray(short[])", () ->
            {
                final Action2<short[],byte[]> encodeShortArrayTest = (short[] values, byte[] expected) ->
                {
                    runner.test("with " + Objects.toString(values), (Test test) ->
                    {
                        test.assertEqual(expected, byteOrder.encodeShortArray(values));
                    });
                };

                encodeShortArrayTest.run(new short[0], new byte[0]);
                encodeShortArrayTest.run(new short[] { 0 }, new byte[] { 0, 0 });
                encodeShortArrayTest.run(new short[] { 0, 1, 2 }, new byte[] { 0, 0, 0, 1, 0, 2 });
            });

            runner.testGroup("encodeIntegerArray(int[])", () ->
            {
                final Action2<int[],byte[]> encodeIntegerArrayTest = (int[] values, byte[] expected) ->
                {
                    runner.test("with " + Objects.toString(values), (Test test) ->
                    {
                        test.assertEqual(expected, byteOrder.encodeIntegerArray(values));
                    });
                };

                encodeIntegerArrayTest.run(new int[0], new byte[0]);
                encodeIntegerArrayTest.run(new int[] { 0 }, new byte[] { 0, 0, 0, 0 });
                encodeIntegerArrayTest.run(new int[] { 0, 1, 2 }, new byte[] { 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 2 });
            });

            runner.testGroup("encodeLongArray(long[])", () ->
            {
                final Action2<long[],byte[]> encodeLongArrayTest = (long[] values, byte[] expected) ->
                {
                    runner.test("with " + Objects.toString(values), (Test test) ->
                    {
                        test.assertEqual(expected, byteOrder.encodeLongArray(values));
                    });
                };

                encodeLongArrayTest.run(new long[0], new byte[0]);
                encodeLongArrayTest.run(new long[] { 0 }, new byte[] { 0, 0, 0, 0, 0, 0, 0, 0 });
                encodeLongArrayTest.run(new long[] { 0, 1, 2 }, new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 2 });
            });

            runner.testGroup("encodeFloatArray(float[])", () ->
            {
                final Action2<float[],byte[]> encodeFloatArrayTest = (float[] values, byte[] expected) ->
                {
                    runner.test("with " + Objects.toString(values), (Test test) ->
                    {
                        test.assertEqual(expected, byteOrder.encodeFloatArray(values));
                    });
                };

                encodeFloatArrayTest.run(new float[0], new byte[0]);
                encodeFloatArrayTest.run(new float[] { 0 }, new byte[] { 0, 0, 0, 0 });
                encodeFloatArrayTest.run(new float[] { 0, 1, 2 }, new byte[] { 0, 0, 0, 0, 63, -128, 0, 0, 64, 0, 0, 0 });
            });

            runner.testGroup("encodeDoubleArray(double[])", () ->
            {
                final Action2<double[],byte[]> encodeDoubleArrayTest = (double[] values, byte[] expected) ->
                {
                    runner.test("with " + Objects.toString(values), (Test test) ->
                    {
                        test.assertEqual(expected, byteOrder.encodeDoubleArray(values));
                    });
                };

                encodeDoubleArrayTest.run(new double[0], new byte[0]);
                encodeDoubleArrayTest.run(new double[] { 0 }, new byte[] { 0, 0, 0, 0, 0, 0, 0, 0 });
                encodeDoubleArrayTest.run(new double[] { 0, 1, 2 }, new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 63, -16, 0, 0, 0, 0, 0, 0, 64, 0, 0, 0, 0, 0, 0, 0 });
            });

            runner.testGroup("decodeAsShort(byte[])", () ->
            {
                final Action2<byte[],Short> decodeAsShortTest = (byte[] values, Short expected) ->
                {
                    runner.test("with " + Objects.toString(values), (Test test) ->
                    {
                        test.assertEqual(expected, byteOrder.decodeAsShort(values));
                    });
                };

                decodeAsShortTest.run(new byte[] { 0, 0 }, (short)0);
                decodeAsShortTest.run(new byte[] { 0, 1 }, (short)1);
                decodeAsShortTest.run(new byte[] { 0, 16 }, (short)16);
                decodeAsShortTest.run(new byte[] { 0, 64 }, (short)64);
                decodeAsShortTest.run(new byte[] { 0, -128 }, (short)128);
                decodeAsShortTest.run(new byte[] { 1, 0 }, (short)256);
                decodeAsShortTest.run(new byte[] { 16, 0 }, (short)4096);
                decodeAsShortTest.run(new byte[] { 64, 0 }, (short)16384);
                decodeAsShortTest.run(new byte[] { -128, 0 }, (short)-32768);
                decodeAsShortTest.run(new byte[] { -1, -1 }, (short)-1);
            });

            runner.testGroup("decodeAsShort(Indexable<Byte>)", () ->
            {
                final Action2<byte[],Short> decodeAsShortTest = (byte[] values, Short expected) ->
                {
                    runner.test("with " + Objects.toString(values), (Test test) ->
                    {
                        test.assertEqual(expected, byteOrder.decodeAsShort(Arrays.createFrom(values)));
                    });
                };

                decodeAsShortTest.run(new byte[] { 0, 0 }, (short)0);
                decodeAsShortTest.run(new byte[] { 0, 1 }, (short)1);
                decodeAsShortTest.run(new byte[] { 0, 16 }, (short)16);
                decodeAsShortTest.run(new byte[] { 0, 64 }, (short)64);
                decodeAsShortTest.run(new byte[] { 0, -128 }, (short)128);
                decodeAsShortTest.run(new byte[] { 1, 0 }, (short)256);
                decodeAsShortTest.run(new byte[] { 16, 0 }, (short)4096);
                decodeAsShortTest.run(new byte[] { 64, 0 }, (short)16384);
                decodeAsShortTest.run(new byte[] { -128, 0 }, (short)-32768);
                decodeAsShortTest.run(new byte[] { -1, -1 }, (short)-1);
            });

            runner.testGroup("decodeAsInteger(byte[])", () ->
            {
                final Action2<byte[],Integer> decodeAsIntegerTest = (byte[] values, Integer expected) ->
                {
                    runner.test("with " + Objects.toString(values), (Test test) ->
                    {
                        test.assertEqual(expected, byteOrder.decodeAsInteger(values));
                    });
                };

                decodeAsIntegerTest.run(new byte[] { 0, 0, 0, 0 }, 0);
                decodeAsIntegerTest.run(new byte[] { 0, 0, 0, 1 }, 1);
                decodeAsIntegerTest.run(new byte[] { 0, 0, 0, 16 }, 16);
                decodeAsIntegerTest.run(new byte[] { 0, 0, 0, 64 }, 64);
                decodeAsIntegerTest.run(new byte[] { 0, 0, 0, -128 }, 128);
                decodeAsIntegerTest.run(new byte[] { 0, 0, 1, 0 }, 256);
                decodeAsIntegerTest.run(new byte[] { 0, 0, 4, 0 }, 1024);
                decodeAsIntegerTest.run(new byte[] { 0, 0, 8, 0 }, 2048);
                decodeAsIntegerTest.run(new byte[] { 0, 0, 16, 0 }, 4096);
                decodeAsIntegerTest.run(new byte[] { 0, 0, 32, 0 }, 8192);
                decodeAsIntegerTest.run(new byte[] { 0, 0, 64, 0 }, 16384);
                decodeAsIntegerTest.run(new byte[] { 0, 0, -128, 0 }, 32768);
                decodeAsIntegerTest.run(new byte[] { 0, 1, 0, 0 }, 65536);
                decodeAsIntegerTest.run(new byte[] { 0, 16, 0, 0 }, 1048576);
                decodeAsIntegerTest.run(new byte[] { 0, 64, 0, 0 }, 4194304);
                decodeAsIntegerTest.run(new byte[] { 0, -128, 0, 0 }, 8388608);
                decodeAsIntegerTest.run(new byte[] { 1, 0, 0, 0 }, 16777216);
                decodeAsIntegerTest.run(new byte[] { 16, 0, 0, 0 }, 268435456);
                decodeAsIntegerTest.run(new byte[] { 64, 0, 0, 0 }, 1073741824);
                decodeAsIntegerTest.run(new byte[] { -128, 0, 0, 0 }, -2147483648);
                decodeAsIntegerTest.run(new byte[] { -1, -1, -1, -1 }, -1);
            });

            runner.testGroup("decodeAsInteger(Indexable<byte>)", () ->
            {
                final Action2<byte[],Integer> decodeAsIntegerTest = (byte[] values, Integer expected) ->
                {
                    runner.test("with " + Objects.toString(values), (Test test) ->
                    {
                        test.assertEqual(expected, byteOrder.decodeAsInteger(Arrays.createFrom(values)));
                    });
                };

                decodeAsIntegerTest.run(new byte[] { 0, 0, 0, 0 }, 0);
                decodeAsIntegerTest.run(new byte[] { 0, 0, 0, 1 }, 1);
                decodeAsIntegerTest.run(new byte[] { 0, 0, 0, 16 }, 16);
                decodeAsIntegerTest.run(new byte[] { 0, 0, 0, 64 }, 64);
                decodeAsIntegerTest.run(new byte[] { 0, 0, 0, -128 }, 128);
                decodeAsIntegerTest.run(new byte[] { 0, 0, 1, 0 }, 256);
                decodeAsIntegerTest.run(new byte[] { 0, 0, 4, 0 }, 1024);
                decodeAsIntegerTest.run(new byte[] { 0, 0, 8, 0 }, 2048);
                decodeAsIntegerTest.run(new byte[] { 0, 0, 16, 0 }, 4096);
                decodeAsIntegerTest.run(new byte[] { 0, 0, 32, 0 }, 8192);
                decodeAsIntegerTest.run(new byte[] { 0, 0, 64, 0 }, 16384);
                decodeAsIntegerTest.run(new byte[] { 0, 0, -128, 0 }, 32768);
                decodeAsIntegerTest.run(new byte[] { 0, 1, 0, 0 }, 65536);
                decodeAsIntegerTest.run(new byte[] { 0, 16, 0, 0 }, 1048576);
                decodeAsIntegerTest.run(new byte[] { 0, 64, 0, 0 }, 4194304);
                decodeAsIntegerTest.run(new byte[] { 0, -128, 0, 0 }, 8388608);
                decodeAsIntegerTest.run(new byte[] { 1, 0, 0, 0 }, 16777216);
                decodeAsIntegerTest.run(new byte[] { 16, 0, 0, 0 }, 268435456);
                decodeAsIntegerTest.run(new byte[] { 64, 0, 0, 0 }, 1073741824);
                decodeAsIntegerTest.run(new byte[] { -128, 0, 0, 0 }, -2147483648);
                decodeAsIntegerTest.run(new byte[] { -1, -1, -1, -1 }, -1);
            });

            runner.testGroup("decodeAsLong(byte[])", () ->
            {
                final Action2<byte[],Long> decodeAsLongTest = (byte[] values, Long expected) ->
                {
                    runner.test("with " + Objects.toString(values), (Test test) ->
                    {
                        test.assertEqual(expected, byteOrder.decodeAsLong(values));
                    });
                };

                decodeAsLongTest.run(new byte[] { 0, 0, 0, 0, 0, 0, 0, 0 }, 0L);
                decodeAsLongTest.run(new byte[] { 0, 0, 0, 0, 0, 0, 0, 1 }, 1L);
                decodeAsLongTest.run(new byte[] { 0, 0, 0, 0, 0, 0, 0, 16 }, 16L);
                decodeAsLongTest.run(new byte[] { 0, 0, 0, 0, 0, 0, 0, 64 }, 64L);
                decodeAsLongTest.run(new byte[] { 0, 0, 0, 0, 0, 0, 0, -128 }, 128L);
                decodeAsLongTest.run(new byte[] { 0, 0, 0, 0, 0, 0, 1, 0 }, 256L);
                decodeAsLongTest.run(new byte[] { 0, 0, 0, 0, 0, 0, 4, 0 }, 1024L);
                decodeAsLongTest.run(new byte[] { 0, 0, 0, 0, 0, 0, 8, 0 }, 2048L);
                decodeAsLongTest.run(new byte[] { 0, 0, 0, 0, 0, 0, 16, 0 }, 4096L);
                decodeAsLongTest.run(new byte[] { 0, 0, 0, 0, 0, 0, 32, 0 }, 8192L);
                decodeAsLongTest.run(new byte[] { 0, 0, 0, 0, 0, 0, 64, 0 }, 16384L);
                decodeAsLongTest.run(new byte[] { 0, 0, 0, 0, 0, 0, -128, 0 }, 32768L);
                decodeAsLongTest.run(new byte[] { 0, 0, 0, 0, 0, 1, 0, 0 }, 65536L);
                decodeAsLongTest.run(new byte[] { 0, 0, 0, 0, 0, 16, 0, 0 }, 1048576L);
                decodeAsLongTest.run(new byte[] { 0, 0, 0, 0, 0, 64, 0, 0 }, 4194304L);
                decodeAsLongTest.run(new byte[] { 0, 0, 0, 0, 0, -128, 0, 0 }, 8388608L);
                decodeAsLongTest.run(new byte[] { 0, 0, 0, 0, 1, 0, 0, 0 }, 16777216L);
                decodeAsLongTest.run(new byte[] { 0, 0, 0, 0, 16, 0, 0, 0 }, 268435456L);
                decodeAsLongTest.run(new byte[] { 0, 0, 0, 0, 64, 0, 0, 0 }, 1073741824L);
                decodeAsLongTest.run(new byte[] { 0, 0, 0, 0, -128, 0, 0, 0 }, 2147483648L);
                decodeAsLongTest.run(new byte[] { -1, -1, -1, -1, -1, -1, -1, -1 }, -1L);
            });

            runner.testGroup("decodeAsLong(Indexable<Byte>)", () ->
            {
                final Action2<byte[],Long> decodeAsLongTest = (byte[] values, Long expected) ->
                {
                    runner.test("with " + Objects.toString(values), (Test test) ->
                    {
                        test.assertEqual(expected, byteOrder.decodeAsLong(Arrays.createFrom(values)));
                    });
                };

                decodeAsLongTest.run(new byte[] { 0, 0, 0, 0, 0, 0, 0, 0 }, 0L);
                decodeAsLongTest.run(new byte[] { 0, 0, 0, 0, 0, 0, 0, 1 }, 1L);
                decodeAsLongTest.run(new byte[] { 0, 0, 0, 0, 0, 0, 0, 16 }, 16L);
                decodeAsLongTest.run(new byte[] { 0, 0, 0, 0, 0, 0, 0, 64 }, 64L);
                decodeAsLongTest.run(new byte[] { 0, 0, 0, 0, 0, 0, 0, -128 }, 128L);
                decodeAsLongTest.run(new byte[] { 0, 0, 0, 0, 0, 0, 1, 0 }, 256L);
                decodeAsLongTest.run(new byte[] { 0, 0, 0, 0, 0, 0, 4, 0 }, 1024L);
                decodeAsLongTest.run(new byte[] { 0, 0, 0, 0, 0, 0, 8, 0 }, 2048L);
                decodeAsLongTest.run(new byte[] { 0, 0, 0, 0, 0, 0, 16, 0 }, 4096L);
                decodeAsLongTest.run(new byte[] { 0, 0, 0, 0, 0, 0, 32, 0 }, 8192L);
                decodeAsLongTest.run(new byte[] { 0, 0, 0, 0, 0, 0, 64, 0 }, 16384L);
                decodeAsLongTest.run(new byte[] { 0, 0, 0, 0, 0, 0, -128, 0 }, 32768L);
                decodeAsLongTest.run(new byte[] { 0, 0, 0, 0, 0, 1, 0, 0 }, 65536L);
                decodeAsLongTest.run(new byte[] { 0, 0, 0, 0, 0, 16, 0, 0 }, 1048576L);
                decodeAsLongTest.run(new byte[] { 0, 0, 0, 0, 0, 64, 0, 0 }, 4194304L);
                decodeAsLongTest.run(new byte[] { 0, 0, 0, 0, 0, -128, 0, 0 }, 8388608L);
                decodeAsLongTest.run(new byte[] { 0, 0, 0, 0, 1, 0, 0, 0 }, 16777216L);
                decodeAsLongTest.run(new byte[] { 0, 0, 0, 0, 16, 0, 0, 0 }, 268435456L);
                decodeAsLongTest.run(new byte[] { 0, 0, 0, 0, 64, 0, 0, 0 }, 1073741824L);
                decodeAsLongTest.run(new byte[] { 0, 0, 0, 0, -128, 0, 0, 0 }, 2147483648L);
                decodeAsLongTest.run(new byte[] { -1, -1, -1, -1, -1, -1, -1, -1 }, -1L);
            });

            runner.testGroup("decodeAsFloat(byte[])", () ->
            {
                final Action2<byte[],Float> decodeAsFloatTest = (byte[] values, Float expected) ->
                {
                    runner.test("with " + Objects.toString(values), (Test test) ->
                    {
                        test.assertEqual(expected, byteOrder.decodeAsFloat(values));
                    });
                };

                decodeAsFloatTest.run(new byte[] { 0, 0, 0, 0 }, 0f);
                decodeAsFloatTest.run(new byte[] { 63, -128, 0, 0 }, 1f);
                decodeAsFloatTest.run(new byte[] { 65, -128, 0, 0 }, 16f);
                decodeAsFloatTest.run(new byte[] { 66, -128, 0, 0 }, 64f);
                decodeAsFloatTest.run(new byte[] { 67, 0, 0, 0 }, 128f);
                decodeAsFloatTest.run(new byte[] { 67, -128, 0, 0 }, 256f);
                decodeAsFloatTest.run(new byte[] { 68, -128, 0, 0 }, 1024f);
                decodeAsFloatTest.run(new byte[] { 69, 0, 0, 0 }, 2048f);
                decodeAsFloatTest.run(new byte[] { 69, -128, 0, 0 }, 4096f);
                decodeAsFloatTest.run(new byte[] { 70, 0, 0, 0 }, 8192f);
                decodeAsFloatTest.run(new byte[] { 70, -128, 0, 0 }, 16384f);
                decodeAsFloatTest.run(new byte[] { 71, 0, 0, 0 }, 32768f);
                decodeAsFloatTest.run(new byte[] { 71, -128, 0, 0 }, 65536f);
                decodeAsFloatTest.run(new byte[] { 73, -128, 0, 0 }, 1048576f);
                decodeAsFloatTest.run(new byte[] { 74, -128, 0, 0 }, 4194304f);
                decodeAsFloatTest.run(new byte[] { 75, 0, 0, 0 }, 8388608f);
                decodeAsFloatTest.run(new byte[] { 75, -128, 0, 0 }, 16777216f);
                decodeAsFloatTest.run(new byte[] { 77, -128, 0, 0 }, 268435456f);
                decodeAsFloatTest.run(new byte[] { 78, -128, 0, 0 }, 1073741824f);
                decodeAsFloatTest.run(new byte[] { 79, 0, 0, 0 }, 2147483648f);
                decodeAsFloatTest.run(new byte[] { -65, -128, 0, 0 }, -1f);
            });

            runner.testGroup("decodeAsFloat(Indexable<Byte>)", () ->
            {
                final Action2<byte[],Float> decodeAsFloatTest = (byte[] values, Float expected) ->
                {
                    runner.test("with " + Objects.toString(values), (Test test) ->
                    {
                        test.assertEqual(expected, byteOrder.decodeAsFloat(Arrays.createFrom(values)));
                    });
                };

                decodeAsFloatTest.run(new byte[] { 0, 0, 0, 0 }, 0f);
                decodeAsFloatTest.run(new byte[] { 63, -128, 0, 0 }, 1f);
                decodeAsFloatTest.run(new byte[] { 65, -128, 0, 0 }, 16f);
                decodeAsFloatTest.run(new byte[] { 66, -128, 0, 0 }, 64f);
                decodeAsFloatTest.run(new byte[] { 67, 0, 0, 0 }, 128f);
                decodeAsFloatTest.run(new byte[] { 67, -128, 0, 0 }, 256f);
                decodeAsFloatTest.run(new byte[] { 68, -128, 0, 0 }, 1024f);
                decodeAsFloatTest.run(new byte[] { 69, 0, 0, 0 }, 2048f);
                decodeAsFloatTest.run(new byte[] { 69, -128, 0, 0 }, 4096f);
                decodeAsFloatTest.run(new byte[] { 70, 0, 0, 0 }, 8192f);
                decodeAsFloatTest.run(new byte[] { 70, -128, 0, 0 }, 16384f);
                decodeAsFloatTest.run(new byte[] { 71, 0, 0, 0 }, 32768f);
                decodeAsFloatTest.run(new byte[] { 71, -128, 0, 0 }, 65536f);
                decodeAsFloatTest.run(new byte[] { 73, -128, 0, 0 }, 1048576f);
                decodeAsFloatTest.run(new byte[] { 74, -128, 0, 0 }, 4194304f);
                decodeAsFloatTest.run(new byte[] { 75, 0, 0, 0 }, 8388608f);
                decodeAsFloatTest.run(new byte[] { 75, -128, 0, 0 }, 16777216f);
                decodeAsFloatTest.run(new byte[] { 77, -128, 0, 0 }, 268435456f);
                decodeAsFloatTest.run(new byte[] { 78, -128, 0, 0 }, 1073741824f);
                decodeAsFloatTest.run(new byte[] { 79, 0, 0, 0 }, 2147483648f);
                decodeAsFloatTest.run(new byte[] { -65, -128, 0, 0 }, -1f);
            });

            runner.testGroup("decodeAsDouble(byte[])", () ->
            {
                final Action2<byte[],Double> decodeAsDoubleTest = (byte[] values, Double expected) ->
                {
                    runner.test("with " + Objects.toString(values), (Test test) ->
                    {
                        test.assertEqual(expected, byteOrder.decodeAsDouble(values));
                    });
                };

                decodeAsDoubleTest.run(new byte[] { 0, 0, 0, 0, 0, 0, 0, 0 }, 0.0);
                decodeAsDoubleTest.run(new byte[] { 63, -16, 0, 0, 0, 0, 0, 0 }, 1.0);
                decodeAsDoubleTest.run(new byte[] { 64, 48, 0, 0, 0, 0, 0, 0 }, 16.0);
                decodeAsDoubleTest.run(new byte[] { 64, 80, 0, 0, 0, 0, 0, 0 }, 64.0);
                decodeAsDoubleTest.run(new byte[] { 64, 96, 0, 0, 0, 0, 0, 0 }, 128.0);
                decodeAsDoubleTest.run(new byte[] { 64, 112, 0, 0, 0, 0, 0, 0 }, 256.0);
                decodeAsDoubleTest.run(new byte[] { 64, -112, 0, 0, 0, 0, 0, 0 }, 1024.0);
                decodeAsDoubleTest.run(new byte[] { 64, -96, 0, 0, 0, 0, 0, 0 }, 2048.0);
                decodeAsDoubleTest.run(new byte[] { 64, -80, 0, 0, 0, 0, 0, 0 }, 4096.0);
                decodeAsDoubleTest.run(new byte[] { 64, -64, 0, 0, 0, 0, 0, 0 }, 8192.0);
                decodeAsDoubleTest.run(new byte[] { 64, -48, 0, 0, 0, 0, 0, 0 }, 16384.0);
                decodeAsDoubleTest.run(new byte[] { 64, -32, 0, 0, 0, 0, 0, 0 }, 32768.0);
                decodeAsDoubleTest.run(new byte[] { 64, -16, 0, 0, 0, 0, 0, 0 }, 65536.0);
                decodeAsDoubleTest.run(new byte[] { 65, 48, 0, 0, 0, 0, 0, 0 }, 1048576.0);
                decodeAsDoubleTest.run(new byte[] { 65, 80, 0, 0, 0, 0, 0, 0 }, 4194304.0);
                decodeAsDoubleTest.run(new byte[] { 65, 96, 0, 0, 0, 0, 0, 0 }, 8388608.0);
                decodeAsDoubleTest.run(new byte[] { 65, 112, 0, 0, 0, 0, 0, 0 }, 16777216.0);
                decodeAsDoubleTest.run(new byte[] { 65, -80, 0, 0, 0, 0, 0, 0 }, 268435456.0);
                decodeAsDoubleTest.run(new byte[] { 65, -48, 0, 0, 0, 0, 0, 0 }, 1073741824.0);
                decodeAsDoubleTest.run(new byte[] { 65, -32, 0, 0, 0, 0, 0, 0 }, 2147483648.0);
                decodeAsDoubleTest.run(new byte[] { -65, -16, 0, 0, 0, 0, 0, 0 }, -1.0);
            });

            runner.testGroup("decodeAsDouble(Indexable<Byte>)", () ->
            {
                final Action2<byte[],Double> decodeAsDoubleTest = (byte[] values, Double expected) ->
                {
                    runner.test("with " + Objects.toString(values), (Test test) ->
                    {
                        test.assertEqual(expected, byteOrder.decodeAsDouble(Arrays.createFrom(values)));
                    });
                };

                decodeAsDoubleTest.run(new byte[] { 0, 0, 0, 0, 0, 0, 0, 0 }, 0.0);
                decodeAsDoubleTest.run(new byte[] { 63, -16, 0, 0, 0, 0, 0, 0 }, 1.0);
                decodeAsDoubleTest.run(new byte[] { 64, 48, 0, 0, 0, 0, 0, 0 }, 16.0);
                decodeAsDoubleTest.run(new byte[] { 64, 80, 0, 0, 0, 0, 0, 0 }, 64.0);
                decodeAsDoubleTest.run(new byte[] { 64, 96, 0, 0, 0, 0, 0, 0 }, 128.0);
                decodeAsDoubleTest.run(new byte[] { 64, 112, 0, 0, 0, 0, 0, 0 }, 256.0);
                decodeAsDoubleTest.run(new byte[] { 64, -112, 0, 0, 0, 0, 0, 0 }, 1024.0);
                decodeAsDoubleTest.run(new byte[] { 64, -96, 0, 0, 0, 0, 0, 0 }, 2048.0);
                decodeAsDoubleTest.run(new byte[] { 64, -80, 0, 0, 0, 0, 0, 0 }, 4096.0);
                decodeAsDoubleTest.run(new byte[] { 64, -64, 0, 0, 0, 0, 0, 0 }, 8192.0);
                decodeAsDoubleTest.run(new byte[] { 64, -48, 0, 0, 0, 0, 0, 0 }, 16384.0);
                decodeAsDoubleTest.run(new byte[] { 64, -32, 0, 0, 0, 0, 0, 0 }, 32768.0);
                decodeAsDoubleTest.run(new byte[] { 64, -16, 0, 0, 0, 0, 0, 0 }, 65536.0);
                decodeAsDoubleTest.run(new byte[] { 65, 48, 0, 0, 0, 0, 0, 0 }, 1048576.0);
                decodeAsDoubleTest.run(new byte[] { 65, 80, 0, 0, 0, 0, 0, 0 }, 4194304.0);
                decodeAsDoubleTest.run(new byte[] { 65, 96, 0, 0, 0, 0, 0, 0 }, 8388608.0);
                decodeAsDoubleTest.run(new byte[] { 65, 112, 0, 0, 0, 0, 0, 0 }, 16777216.0);
                decodeAsDoubleTest.run(new byte[] { 65, -80, 0, 0, 0, 0, 0, 0 }, 268435456.0);
                decodeAsDoubleTest.run(new byte[] { 65, -48, 0, 0, 0, 0, 0, 0 }, 1073741824.0);
                decodeAsDoubleTest.run(new byte[] { 65, -32, 0, 0, 0, 0, 0, 0 }, 2147483648.0);
                decodeAsDoubleTest.run(new byte[] { -65, -16, 0, 0, 0, 0, 0, 0 }, -1.0);
            });

            runner.testGroup("decodeAsShortArray(byte[])", () ->
            {
                final Action2<byte[],short[]> decodeAsShortArrayTest = (byte[] values, short[] expected) ->
                {
                    runner.test("with " + Objects.toString(values), (Test test) ->
                    {
                        test.assertEqual(expected, byteOrder.decodeAsShortArray(values));
                    });
                };

                decodeAsShortArrayTest.run(new byte[0], new short[0]);
                decodeAsShortArrayTest.run(new byte[] { 0, 0 }, new short[] { 0 });
                decodeAsShortArrayTest.run(new byte[] { 0, 0, 0, 1, 0, 2 }, new short[] { 0, 1, 2 });
            });

            runner.testGroup("decodeAsShortArray(Indexable<Byte>)", () ->
            {
                final Action2<byte[],short[]> decodeAsShortArrayTest = (byte[] values, short[] expected) ->
                {
                    runner.test("with " + Objects.toString(values), (Test test) ->
                    {
                        test.assertEqual(expected, byteOrder.decodeAsShortArray(Arrays.createFrom(values)));
                    });
                };

                decodeAsShortArrayTest.run(new byte[0], new short[0]);
                decodeAsShortArrayTest.run(new byte[] { 0, 0 }, new short[] { 0 });
                decodeAsShortArrayTest.run(new byte[] { 0, 0, 0, 1, 0, 2 }, new short[] { 0, 1, 2 });
            });

            runner.testGroup("decodeAsIntegerArray(byte[])", () ->
            {
                final Action2<byte[],int[]> decodeAsIntegerArrayTest = (byte[] values, int[] expected) ->
                {
                    runner.test("with " + Objects.toString(values), (Test test) ->
                    {
                        test.assertEqual(expected, byteOrder.decodeAsIntegerArray(values));
                    });
                };

                decodeAsIntegerArrayTest.run(new byte[0], new int[0]);
                decodeAsIntegerArrayTest.run(new byte[] { 0, 0, 0, 0 }, new int[] { 0 });
                decodeAsIntegerArrayTest.run(new byte[] { 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 2 }, new int[] { 0, 1, 2 });
            });

            runner.testGroup("decodeAsIntegerArray(Indexable<Byte>)", () ->
            {
                final Action2<byte[],int[]> decodeAsIntegerArrayTest = (byte[] values, int[] expected) ->
                {
                    runner.test("with " + Objects.toString(values), (Test test) ->
                    {
                        test.assertEqual(expected, byteOrder.decodeAsIntegerArray(Arrays.createFrom(values)));
                    });
                };

                decodeAsIntegerArrayTest.run(new byte[0], new int[0]);
                decodeAsIntegerArrayTest.run(new byte[] { 0, 0, 0, 0 }, new int[] { 0 });
                decodeAsIntegerArrayTest.run(new byte[] { 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 2 }, new int[] { 0, 1, 2 });
            });

            runner.testGroup("decodeAsLongArray(byte[])", () ->
            {
                final Action2<byte[],long[]> decodeAsLongArrayTest = (byte[] values, long[] expected) ->
                {
                    runner.test("with " + Objects.toString(values), (Test test) ->
                    {
                        test.assertEqual(expected, byteOrder.decodeAsLongArray(values));
                    });
                };

                decodeAsLongArrayTest.run(new byte[0], new long[0]);
                decodeAsLongArrayTest.run(new byte[] { 0, 0, 0, 0, 0, 0, 0, 0 }, new long[] { 0 });
                decodeAsLongArrayTest.run(new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 2 }, new long[] { 0, 1, 2 });
            });

            runner.testGroup("decodeAsLongArray(Indexable<Byte>)", () ->
            {
                final Action2<byte[],long[]> decodeAsLongArrayTest = (byte[] values, long[] expected) ->
                {
                    runner.test("with " + Objects.toString(values), (Test test) ->
                    {
                        test.assertEqual(expected, byteOrder.decodeAsLongArray(Arrays.createFrom(values)));
                    });
                };

                decodeAsLongArrayTest.run(new byte[0], new long[0]);
                decodeAsLongArrayTest.run(new byte[] { 0, 0, 0, 0, 0, 0, 0, 0 }, new long[] { 0 });
                decodeAsLongArrayTest.run(new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 2 }, new long[] { 0, 1, 2 });
            });

            runner.testGroup("decodeAsFloatArray(byte[])", () ->
            {
                final Action2<byte[],float[]> decodeAsFloatArrayTest = (byte[] values, float[] expected) ->
                {
                    runner.test("with " + Objects.toString(values), (Test test) ->
                    {
                        test.assertEqual(expected, byteOrder.decodeAsFloatArray(values));
                    });
                };

                decodeAsFloatArrayTest.run(new byte[0], new float[0]);
                decodeAsFloatArrayTest.run(new byte[] { 0, 0, 0, 0 }, new float[] { 0 });
                decodeAsFloatArrayTest.run(new byte[] { 0, 0, 0, 0, 63, -128, 0, 0, 64, 0, 0, 0 }, new float[] { 0, 1, 2 });
            });

            runner.testGroup("decodeAsFloatArray(Indexable<Byte>)", () ->
            {
                final Action2<byte[],float[]> decodeAsFloatArrayTest = (byte[] values, float[] expected) ->
                {
                    runner.test("with " + Objects.toString(values), (Test test) ->
                    {
                        test.assertEqual(expected, byteOrder.decodeAsFloatArray(Arrays.createFrom(values)));
                    });
                };

                decodeAsFloatArrayTest.run(new byte[0], new float[0]);
                decodeAsFloatArrayTest.run(new byte[] { 0, 0, 0, 0 }, new float[] { 0 });
                decodeAsFloatArrayTest.run(new byte[] { 0, 0, 0, 0, 63, -128, 0, 0, 64, 0, 0, 0 }, new float[] { 0, 1, 2 });
            });

            runner.testGroup("decodeAsDoubleArray(byte[])", () ->
            {
                final Action2<byte[],double[]> decodeAsDoubleArrayTest = (byte[] values, double[] expected) ->
                {
                    runner.test("with " + Objects.toString(values), (Test test) ->
                    {
                        test.assertEqual(expected, byteOrder.decodeAsDoubleArray(values));
                    });
                };

                decodeAsDoubleArrayTest.run(new byte[0], new double[0]);
                decodeAsDoubleArrayTest.run(new byte[] { 0, 0, 0, 0, 0, 0, 0, 0 }, new double[] { 0 });
                decodeAsDoubleArrayTest.run(new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 63, -16, 0, 0, 0, 0, 0, 0, 64, 0, 0, 0, 0, 0, 0, 0 }, new double[] { 0, 1, 2 });
            });

            runner.testGroup("decodeAsDoubleArray(Indexable<Byte>)", () ->
            {
                final Action2<byte[],double[]> decodeAsDoubleArrayTest = (byte[] values, double[] expected) ->
                {
                    runner.test("with " + Objects.toString(values), (Test test) ->
                    {
                        test.assertEqual(expected, byteOrder.decodeAsDoubleArray(Arrays.createFrom(values)));
                    });
                };

                decodeAsDoubleArrayTest.run(new byte[0], new double[0]);
                decodeAsDoubleArrayTest.run(new byte[] { 0, 0, 0, 0, 0, 0, 0, 0 }, new double[] { 0 });
                decodeAsDoubleArrayTest.run(new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 63, -16, 0, 0, 0, 0, 0, 0, 64, 0, 0, 0, 0, 0, 0, 0 }, new double[] { 0, 1, 2 });
            });
        });
    }
}
