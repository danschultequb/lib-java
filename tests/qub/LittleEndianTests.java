package qub;

public class LittleEndianTests
{
    private static final ByteOrder byteOrder = new LittleEndian();

    public static void test(TestRunner runner)
    {
        runner.testGroup(LittleEndian.class, () ->
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
                encodeShortTest.run((short)1, new byte[] { 1, 0 });
                encodeShortTest.run((short)16, new byte[] { 16, 0 });
                encodeShortTest.run((short)128, new byte[] { -128, 0 });
                encodeShortTest.run((short)256, new byte[] { 0, 1 });
                encodeShortTest.run((short)1024, new byte[] { 0, 4 });
                encodeShortTest.run((short)2048, new byte[] { 0, 8 });
                encodeShortTest.run((short)4096, new byte[] { 0, 16 });
                encodeShortTest.run((short)8192, new byte[] { 0, 32 });
                encodeShortTest.run((short)32768, new byte[] { 0, -128 });
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
                encodeIntegerTest.run(1, new byte[] { 1, 0, 0, 0 });
                encodeIntegerTest.run(16, new byte[] { 16, 0, 0, 0 });
                encodeIntegerTest.run(64, new byte[] { 64, 0, 0, 0 });
                encodeIntegerTest.run(128, new byte[] { -128, 0, 0, 0 });
                encodeIntegerTest.run(256, new byte[] { 0, 1, 0, 0 });
                encodeIntegerTest.run(1024, new byte[] { 0, 4, 0, 0 });
                encodeIntegerTest.run(2048, new byte[] { 0, 8, 0, 0 });
                encodeIntegerTest.run(4096, new byte[] { 0, 16, 0, 0 });
                encodeIntegerTest.run(8192, new byte[] { 0, 32, 0, 0 });
                encodeIntegerTest.run(16384, new byte[] { 0, 64, 0, 0 });
                encodeIntegerTest.run(32768, new byte[] { 0, -128, 0, 0 });
                encodeIntegerTest.run(65536, new byte[] { 0, 0, 1, 0 });
                encodeIntegerTest.run(1048576, new byte[] { 0, 0, 16, 0 });
                encodeIntegerTest.run(4194304, new byte[] { 0, 0, 64, 0 });
                encodeIntegerTest.run(8388608, new byte[] { 0, 0, -128, 0 });
                encodeIntegerTest.run(16777216, new byte[] { 0, 0, 0, 1 });
                encodeIntegerTest.run(268435456, new byte[] { 0, 0, 0, 16 });
                encodeIntegerTest.run(1073741824, new byte[] { 0, 0, 0, 64 });
                encodeIntegerTest.run(-2147483648, new byte[] { 0, 0, 0, -128 });
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
                encodeLongTest.run(1L, new byte[] { 1, 0, 0, 0, 0, 0, 0, 0 });
                encodeLongTest.run(16L, new byte[] { 16, 0, 0, 0, 0, 0, 0, 0 });
                encodeLongTest.run(64L, new byte[] { 64, 0, 0, 0, 0, 0, 0, 0 });
                encodeLongTest.run(128L, new byte[] { -128, 0, 0, 0, 0, 0, 0, 0 });
                encodeLongTest.run(256L, new byte[] { 0, 1, 0, 0, 0, 0, 0, 0 });
                encodeLongTest.run(1024L, new byte[] { 0, 4, 0, 0, 0, 0, 0, 0 });
                encodeLongTest.run(2048L, new byte[] { 0, 8, 0, 0, 0, 0, 0, 0 });
                encodeLongTest.run(4096L, new byte[] { 0, 16, 0, 0, 0, 0, 0, 0 });
                encodeLongTest.run(8192L, new byte[] { 0, 32, 0, 0, 0, 0, 0, 0 });
                encodeLongTest.run(16384L, new byte[] { 0, 64, 0, 0, 0, 0, 0, 0 });
                encodeLongTest.run(32768L, new byte[] { 0, -128, 0, 0, 0, 0, 0, 0 });
                encodeLongTest.run(65536L, new byte[] { 0, 0, 1, 0, 0, 0, 0, 0 });
                encodeLongTest.run(1048576L, new byte[] { 0, 0, 16, 0, 0, 0, 0, 0 });
                encodeLongTest.run(4194304L, new byte[] { 0, 0, 64, 0, 0, 0, 0, 0 });
                encodeLongTest.run(8388608L, new byte[] { 0, 0, -128, 0, 0, 0, 0, 0 });
                encodeLongTest.run(16777216L, new byte[] { 0, 0, 0, 1, 0, 0, 0, 0 });
                encodeLongTest.run(268435456L, new byte[] { 0, 0, 0, 16, 0, 0, 0, 0 });
                encodeLongTest.run(1073741824L, new byte[] { 0, 0, 0, 64, 0, 0, 0, 0 });
                encodeLongTest.run(2147483648L, new byte[] { 0, 0, 0, -128, 0, 0, 0, 0 });
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
                encodeFloatTest.run(1f, new byte[] { 0, 0, -128, 63 });
                encodeFloatTest.run(16f, new byte[] { 0, 0, -128, 65 });
                encodeFloatTest.run(64f, new byte[] { 0, 0, -128, 66 });
                encodeFloatTest.run(128f, new byte[] { 0, 0, 0, 67 });
                encodeFloatTest.run(256f, new byte[] { 0, 0, -128, 67 });
                encodeFloatTest.run(1024f, new byte[] { 0, 0, -128, 68 });
                encodeFloatTest.run(2048f, new byte[] { 0, 0, 0, 69 });
                encodeFloatTest.run(4096f, new byte[] { 0, 0, -128, 69 });
                encodeFloatTest.run(8192f, new byte[] { 0, 0, 0, 70 });
                encodeFloatTest.run(16384f, new byte[] { 0, 0, -128, 70 });
                encodeFloatTest.run(32768f, new byte[] { 0, 0, 0, 71 });
                encodeFloatTest.run(65536f, new byte[] { 0, 0, -128, 71 });
                encodeFloatTest.run(1048576f, new byte[] { 0, 0, -128, 73 });
                encodeFloatTest.run(4194304f, new byte[] { 0, 0, -128, 74 });
                encodeFloatTest.run(8388608f, new byte[] { 0, 0, 0, 75 });
                encodeFloatTest.run(16777216f, new byte[] { 0, 0, -128, 75 });
                encodeFloatTest.run(268435456f, new byte[] { 0, 0, -128, 77 });
                encodeFloatTest.run(1073741824f, new byte[] { 0, 0, -128, 78 });
                encodeFloatTest.run(2147483648f, new byte[] { 0, 0, 0, 79 });
                encodeFloatTest.run(-1f, new byte[] { 0, 0, -128, -65 });
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
                encodeDoubleTest.run(1.0, new byte[] { 0,0,0,0,0,0,-16,63 });
                encodeDoubleTest.run(16.0, new byte[] { 0, 0, 0, 0, 0, 0, 48, 64 });
                encodeDoubleTest.run(64.0, new byte[] { 0, 0, 0, 0, 0, 0, 80, 64 });
                encodeDoubleTest.run(128.0, new byte[] { 0, 0, 0, 0, 0, 0, 96, 64 });
                encodeDoubleTest.run(256.0, new byte[] { 0, 0, 0, 0, 0, 0, 112, 64 });
                encodeDoubleTest.run(1024.0, new byte[] { 0, 0, 0, 0, 0, 0, -112, 64 });
                encodeDoubleTest.run(2048.0, new byte[] { 0, 0, 0, 0, 0, 0, -96, 64 });
                encodeDoubleTest.run(4096.0, new byte[] { 0, 0, 0, 0, 0, 0, -80, 64 });
                encodeDoubleTest.run(8192.0, new byte[] { 0, 0, 0, 0, 0, 0, -64, 64 });
                encodeDoubleTest.run(16384.0, new byte[] { 0, 0, 0, 0, 0, 0, -48, 64 });
                encodeDoubleTest.run(32768.0, new byte[] { 0, 0, 0, 0, 0, 0, -32, 64 });
                encodeDoubleTest.run(65536.0, new byte[] { 0, 0, 0, 0, 0, 0, -16, 64 });
                encodeDoubleTest.run(1048576.0, new byte[] { 0, 0, 0, 0, 0, 0, 48, 65 });
                encodeDoubleTest.run(4194304.0, new byte[] { 0, 0, 0, 0, 0, 0, 80, 65 });
                encodeDoubleTest.run(8388608.0, new byte[] { 0, 0, 0, 0, 0, 0, 96, 65 });
                encodeDoubleTest.run(16777216.0, new byte[] { 0, 0, 0, 0, 0, 0, 112, 65 });
                encodeDoubleTest.run(268435456.0, new byte[] { 0, 0, 0, 0, 0, 0, -80, 65 });
                encodeDoubleTest.run(1073741824.0, new byte[] { 0, 0, 0, 0, 0, 0, -48, 65 });
                encodeDoubleTest.run(2147483648.0, new byte[] { 0, 0, 0, 0, 0, 0, -32, 65 });
                encodeDoubleTest.run(-1.0, new byte[] { 0, 0, 0, 0, 0, 0, -16, -65 });
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
                encodeShortArrayTest.run(new short[] { 0, 1, 2 }, new byte[] { 0, 0, 1, 0, 2, 0 });
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
                encodeIntegerArrayTest.run(new int[] { 0, 1, 2 }, new byte[] { 0, 0, 0, 0, 1, 0, 0, 0, 2, 0, 0, 0 });
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
                encodeLongArrayTest.run(new long[] { 0, 1, 2 }, new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0 });
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
                encodeFloatArrayTest.run(new float[] { 0, 1, 2 }, new byte[] { 0, 0, 0, 0, 0, 0, -128, 63, 0, 0, 0, 64 });
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
                encodeDoubleArrayTest.run(new double[] { 0, 1, 2 }, new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -16, 63, 0, 0, 0, 0, 0, 0, 0, 64 });
            });

//            public static TestGroup createTests()
//            {
//                retval.add(new Test("DecodeAsShort")
//                {
//                    public void run()
//                    {
//                        final LittleEndian e = new LittleEndian();
//
//                        Test.Assert.Equal((short)0, e.decodeAsShort(new byte[] { 0, 0 }));
//
//                        Test.Assert.Equal((short)1, e.decodeAsShort(new byte[] { 1, 0 }));
//                        Test.Assert.Equal((short)16, e.decodeAsShort(new byte[] { 16, 0 }));
//                        Test.Assert.Equal((short)64, e.decodeAsShort(new byte[] { 64, 0 }));
//                        Test.Assert.Equal((short)128, e.decodeAsShort(new byte[] { -128, 0 }));
//
//                        Test.Assert.Equal((short)256, e.decodeAsShort(new byte[] { 0, 1 }));
//                        Test.Assert.Equal((short)4096, e.decodeAsShort(new byte[] { 0, 16 }));
//                        Test.Assert.Equal((short)16384, e.decodeAsShort(new byte[] { 0, 64 }));
//                        Test.Assert.Equal((short)-32768, e.decodeAsShort(new byte[] { 0, -128 }));
//
//                        Test.Assert.Equal((short)-1, e.decodeAsShort(new byte[] { -1, -1 }));
//                    }
//                });
//                retval.add(new Test("DecodeAsInt")
//                {
//                    public void run()
//                    {
//                        final LittleEndian e = new LittleEndian();
//
//                        Test.Assert.Equal((int)0, e.decodeAsInt(new byte[] { 0, 0, 0, 0 }));
//
//                        Test.Assert.Equal((int)1, e.decodeAsInt(new byte[] { 1, 0, 0, 0 }));
//                        Test.Assert.Equal((int)16, e.decodeAsInt(new byte[] { 16, 0, 0, 0 }));
//                        Test.Assert.Equal((int)64, e.decodeAsInt(new byte[] { 64, 0, 0, 0 }));
//                        Test.Assert.Equal((int)128, e.decodeAsInt(new byte[] { -128, 0, 0, 0 }));
//
//                        Test.Assert.Equal((int)256, e.decodeAsInt(new byte[] { 0, 1, 0, 0 }));
//                        Test.Assert.Equal((int)4096, e.decodeAsInt(new byte[] { 0, 16, 0, 0 }));
//                        Test.Assert.Equal((int)16384, e.decodeAsInt(new byte[] { 0, 64, 0, 0 }));
//                        Test.Assert.Equal((int)32768, e.decodeAsInt(new byte[] { 0, -128, 0, 0 }));
//
//                        Test.Assert.Equal((int)65536, e.decodeAsInt(new byte[] { 0, 0, 1, 0 }));
//                        Test.Assert.Equal((int)1048576, e.decodeAsInt(new byte[] { 0, 0, 16, 0 }));
//                        Test.Assert.Equal((int)4194304, e.decodeAsInt(new byte[] { 0, 0, 64, 0 }));
//                        Test.Assert.Equal((int)8388608, e.decodeAsInt(new byte[] { 0, 0, -128, 0 }));
//
//                        Test.Assert.Equal((int)16777216, e.decodeAsInt(new byte[] { 0, 0, 0, 1 }));
//                        Test.Assert.Equal((int)268435456, e.decodeAsInt(new byte[] { 0, 0, 0, 16 }));
//                        Test.Assert.Equal((int)1073741824, e.decodeAsInt(new byte[] { 0, 0, 0, 64 }));
//                        Test.Assert.Equal((int)-2147483648, e.decodeAsInt(new byte[] { 0, 0, 0, -128 }));
//
//                        Test.Assert.Equal((int)-1, e.decodeAsInt(new byte[] { -1, -1, -1, -1 }));
//                    }
//                });
//
//                Debug.PostCondition.NotNullOrEmpty(retval);
//
//                return retval;
//            }
        });
    }
}
