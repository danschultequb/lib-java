package qub;

public class ArrayTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(Array.class, () ->
        {
            MutableIndexableTests.test(runner, (Integer count) ->
            {
                final Array<Integer> result = Array.createWithLength(count);
                for (int i = 0; i < count; ++i)
                {
                    result.set(i, i);
                }
                PreCondition.assertEqual(count.intValue(), result.getCount(), "result.getCount()");
                return result; 
            });

            runner.testGroup("create(Iterator<T>)", () ->
            {
                runner.test("with null Iterator", (Test test) ->
                {
                    test.assertThrows(() -> Array.create((Iterator<Integer>)null),
                        new PreConditionFailure("values cannot be null."));
                });

                runner.test("with empty Iterator", (Test test) ->
                {
                    final Array<Integer> array = Array.create(Iterator.create());
                    test.assertEqual(0, array.getCount());
                });

                runner.test("with non-empty Iterator", (Test test) ->
                {
                    final Array<Integer> array = Array.create(IntegerArrayIterator.create(1, 2, 3));
                    test.assertEqual(3, array.getCount());
                    test.assertEqual(1, array.get(0));
                    test.assertEqual(2, array.get(1));
                    test.assertEqual(3, array.get(2));
                });
            });

            runner.testGroup("create(Iterable<T>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> Array.create((Iterable<Integer>)null),
                        new PreConditionFailure("values cannot be null."));
                });

                runner.test("with empty", (Test test) ->
                {
                    final Array<Integer> array = Array.create(Iterable.create());
                    test.assertEqual(0, array.getCount());
                });

                runner.test("with non-empty", (Test test) ->
                {
                    final Array<Integer> array = Array.create(Iterable.create(1, 2, 3));
                    test.assertEqual(3, array.getCount());
                    test.assertEqual(1, array.get(0));
                    test.assertEqual(2, array.get(1));
                    test.assertEqual(3, array.get(2));
                });
            });

            runner.testGroup("createBitArray(long)", () ->
            {
                runner.test("with negative", (Test test) ->
                {
                    test.assertThrows(() -> Array.createBitArray(-1), new PreConditionFailure("length (-1) must be between 0 and 68719476704."));
                });

                runner.test("with zero", (Test test) ->
                {
                    final BitArray array = Array.createBitArray(0);
                    test.assertNotNull(array);
                    test.assertEqual(0, array.getCount());
                });

                runner.test("with positive", (Test test) ->
                {
                    final BitArray array = Array.createBitArray(1000);
                    test.assertNotNull(array);
                    test.assertEqual(1000, array.getCount());
                });
            });

            runner.testGroup("createBitArrayFromBits(int...)", () ->
            {
                runner.test("with null array", (Test test) ->
                {
                    test.assertThrows(() -> Array.createBitArrayFromBits((int[])null),
                        new PreConditionFailure("values cannot be null."));
                });

                runner.test("with empty array", (Test test) ->
                {
                    final BitArray array = Array.createBitArrayFromBits(new int[0]);
                    test.assertEqual(0, array.getCount());
                });

                runner.test("with one value array", (Test test) ->
                {
                    final BitArray array = Array.createBitArrayFromBits(new int[] { 1 });
                    test.assertEqual(1, array.getCount());
                    test.assertEqual(1, array.get(0));
                });

                runner.test("with two value array", (Test test) ->
                {
                    final BitArray array = Array.createBitArrayFromBits(new int[] { 1, 0 });
                    test.assertEqual(2, array.getCount());
                    test.assertEqual(1, array.get(0));
                    test.assertEqual(0, array.get(1));
                });

                runner.test("with no values", (Test test) ->
                {
                    final BitArray array = Array.createBitArrayFromBits();
                    test.assertEqual(0, array.getCount());
                });

                runner.test("with one value", (Test test) ->
                {
                    final BitArray array = Array.createBitArrayFromBits(1);
                    test.assertEqual(1, array.getCount());
                    test.assertEqual(1, array.get(0));
                });

                runner.test("with two values", (Test test) ->
                {
                    final BitArray array = Array.createBitArrayFromBits(1, 0);
                    test.assertEqual(2, array.getCount());
                    test.assertEqual(1, array.get(0));
                    test.assertEqual(0, array.get(1));
                });

                runner.test("with invalid values", (Test test) ->
                {
                    test.assertThrows(() -> Array.createBitArrayFromBits(3, 0), new PreConditionFailure("The 0 bit (3) must be either 0 or 1."));
                });
            });

            runner.testGroup("createBitArrayFromBytes(byte...)", () ->
            {
                runner.test("with null array", (Test test) ->
                {
                    test.assertThrows(() -> Array.createBitArrayFromBytes((byte[])null), new PreConditionFailure("values cannot be null."));
                });

                runner.test("with empty array", (Test test) ->
                {
                    final BitArray array = Array.createBitArrayFromBytes(new byte[0]);
                    test.assertNotNull(array);
                    test.assertEqual(0, array.getCount());
                });

                runner.test("with non-empty array", (Test test) ->
                {
                    final BitArray array = Array.createBitArrayFromBytes(new byte[] { 0, 1, 2 });
                    test.assertNotNull(array);
                    test.assertEqual(24, array.getCount());
                    test.assertEqual(Array.createBitArrayFromHexString("000102"), array);
                });

                runner.test("with no values", (Test test) ->
                {
                    final BitArray array = Array.createBitArrayFromBytes();
                    test.assertNotNull(array);
                    test.assertEqual(0, array.getCount());
                });

                runner.test("with values", (Test test) ->
                {
                    final BitArray array = Array.createBitArrayFromBytes((byte)3, (byte)4, (byte)5, (byte)6);
                    test.assertNotNull(array);
                    test.assertEqual(32, array.getCount());
                    test.assertEqual(Array.createBitArrayFromHexString("03040506"), array);
                });
            });

            runner.testGroup("createBitArrayFromBytes(byte[],long)", () ->
            {
                runner.test("with null array", (Test test) ->
                {
                    test.assertThrows(() -> Array.createBitArrayFromBytes((byte[])null, 4), new PreConditionFailure("values cannot be null."));
                });

                runner.test("with negative bitCount", (Test test) ->
                {
                    test.assertThrows(() -> Array.createBitArrayFromBytes(new byte[] { 0, 1, 2 }, -1), new PreConditionFailure("bitCount (-1) must be between 17 and 24."));
                });

                runner.test("with too small bitCount", (Test test) ->
                {
                    test.assertThrows(() -> Array.createBitArrayFromBytes(new byte[] { 0, 1 }, 8), new PreConditionFailure("bitCount (8) must be between 9 and 16."));
                });

                runner.test("with too large bitCount", (Test test) ->
                {
                    test.assertThrows(() -> Array.createBitArrayFromBytes(new byte[] { 0, 1, 2 }, 25), new PreConditionFailure("bitCount (25) must be between 17 and 24."));
                });

                runner.test("with empty array", (Test test) ->
                {
                    final BitArray array = Array.createBitArrayFromBytes(new byte[0], 0);
                    test.assertNotNull(array);
                    test.assertEqual(0, array.getCount());
                });

                runner.test("with non-empty array", (Test test) ->
                {
                    final BitArray array = Array.createBitArrayFromBytes(new byte[] { 0, 1, 2 }, 23);
                    test.assertNotNull(array);
                    test.assertEqual(23, array.getCount());
                    test.assertEqual("00000000000000010000001", array.toBitString());
                });
            });

            runner.testGroup("createBitArrayFromBytes(int...)", () ->
            {
                runner.test("with null array", (Test test) ->
                {
                    test.assertThrows(() -> Array.createBitArrayFromBytes((int[])null), new PreConditionFailure("values cannot be null."));
                });

                runner.test("with empty array", (Test test) ->
                {
                    final BitArray array = Array.createBitArrayFromBytes(new int[0]);
                    test.assertNotNull(array);
                    test.assertEqual(0, array.getCount());
                });

                runner.test("with non-empty array", (Test test) ->
                {
                    final BitArray array = Array.createBitArrayFromBytes(new int[] { 0, 1, 2 });
                    test.assertNotNull(array);
                    test.assertEqual(24, array.getCount());
                    test.assertEqual(Array.createBitArrayFromHexString("000102"), array);
                });

                runner.test("with no values", (Test test) ->
                {
                    final BitArray array = Array.createBitArrayFromBytes();
                    test.assertNotNull(array);
                    test.assertEqual(0, array.getCount());
                });

                runner.test("with values", (Test test) ->
                {
                    final BitArray array = Array.createBitArrayFromBytes(3, 4, 5, 6);
                    test.assertNotNull(array);
                    test.assertEqual(32, array.getCount());
                    test.assertEqual(Array.createBitArrayFromHexString("03040506"), array);
                });
            });

            runner.testGroup("createBitArrayFromIntegers(int...)", () ->
            {
                runner.test("with null array", (Test test) ->
                {
                    test.assertThrows(() -> Array.createBitArrayFromIntegers((int[])null),
                        new PreConditionFailure("values cannot be null."));
                });

                runner.test("with empty array", (Test test) ->
                {
                    final BitArray array = Array.createBitArrayFromIntegers(new int[0]);
                    test.assertEqual("", array.toHexString());
                });

                runner.test("with one value array", (Test test) ->
                {
                    final BitArray array = Array.createBitArrayFromIntegers(new int[] { 1 });
                    test.assertEqual("00000001", array.toHexString());
                });

                runner.test("with two value array", (Test test) ->
                {
                    final BitArray array = Array.createBitArrayFromIntegers(new int[] { 1, 0 });
                    test.assertEqual("0000000100000000", array.toHexString());
                });

                runner.test("with no values", (Test test) ->
                {
                    final BitArray array = Array.createBitArrayFromIntegers();
                    test.assertEqual("", array.toHexString());
                });

                runner.test("with one value", (Test test) ->
                {
                    final BitArray array = Array.createBitArrayFromIntegers(1);
                    test.assertEqual("00000001", array.toHexString());
                });

                runner.test("with two values", (Test test) ->
                {
                    final BitArray array = Array.createBitArrayFromIntegers(1, 0);
                    test.assertEqual("0000000100000000", array.toHexString());
                });
            });

            runner.testGroup("createBitArrayFromBitString(String)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> Array.createBitArrayFromBitString(null), new PreConditionFailure("bits cannot be null."));
                });

                runner.test("with " + Strings.quote(""), (Test test) ->
                {
                    test.assertEqual(Array.createBitArray(0), Array.createBitArrayFromBitString(""));
                });

                runner.test("with " + Strings.quote("1"), (Test test) ->
                {
                    test.assertEqual(Array.createBitArrayFromBits(1), Array.createBitArrayFromBitString("1"));
                });

                runner.test("with " + Strings.quote("10101"), (Test test) ->
                {
                    test.assertEqual(Array.createBitArrayFromBits(1, 0, 1, 0, 1), Array.createBitArrayFromBitString("10101"));
                });
            });

            runner.testGroup("createBooleanArray(int)", () ->
            {
                runner.test("with negative", (Test test) ->
                {
                    test.assertThrows(() -> Array.createBooleanArray(-1), new PreConditionFailure("length (-1) must be greater than or equal to 0."));
                });

                runner.test("with zero", (Test test) ->
                {
                    final BooleanArray array = Array.createBooleanArray(0);
                    test.assertNotNull(array);
                    test.assertEqual(0, array.getCount());
                });

                runner.test("with positive", (Test test) ->
                {
                    final BooleanArray array = Array.createBooleanArray(1000);
                    test.assertNotNull(array);
                    test.assertEqual(1000, array.getCount());
                });
            });

            runner.testGroup("createBooleanArray(boolean...)", () ->
            {
                runner.test("with null array", (Test test) ->
                {
                    test.assertThrows(() -> Array.createBooleanArray((boolean[])null),
                        new PreConditionFailure("values cannot be null."));
                });

                runner.test("with empty array", (Test test) ->
                {
                    final BooleanArray array = Array.createBooleanArray(new boolean[0]);
                    test.assertEqual(0, array.getCount());
                });

                runner.test("with one value array", (Test test) ->
                {
                    final BooleanArray array = Array.createBooleanArray(new boolean[] { true });
                    test.assertEqual(1, array.getCount());
                    test.assertEqual(true, array.get(0));
                });

                runner.test("with two value array", (Test test) ->
                {
                    final BooleanArray array = Array.createBooleanArray(new boolean[] { true, false });
                    test.assertEqual(2, array.getCount());
                    test.assertEqual(true, array.get(0));
                    test.assertEqual(false, array.get(1));
                });

                runner.test("with no values", (Test test) ->
                {
                    final BooleanArray array = Array.createBooleanArray();
                    test.assertEqual(0, array.getCount());
                });

                runner.test("with one value", (Test test) ->
                {
                    final BooleanArray array = Array.createBooleanArray(true);
                    test.assertEqual(1, array.getCount());
                    test.assertEqual(true, array.get(0));
                });

                runner.test("with two values", (Test test) ->
                {
                    final BooleanArray array = Array.createBooleanArray(true, false);
                    test.assertEqual(2, array.getCount());
                    test.assertEqual(true, array.get(0));
                    test.assertEqual(false, array.get(1));
                });
            });

            runner.testGroup("toBooleanArray(Iterator<Boolean>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> Array.toBooleanArray((Iterator<Boolean>)null),
                        new PreConditionFailure("values cannot be null."));
                });

                runner.test("with empty", (Test test) ->
                {
                    test.assertEqual(
                        new boolean[0],
                        Array.toBooleanArray(Iterator.create()).await());
                });

                runner.test("with non-empty", (Test test) ->
                {
                    test.assertEqual(
                        new boolean[] { false, true, false },
                        Array.toBooleanArray(Iterator.create(false, true, false)).await());
                });

                runner.test("with null value", (Test test) ->
                {
                    test.assertThrows(() -> Array.toBooleanArray(Iterator.create(null, true, false)).await(),
                        new NullPointerException("The 0 element cannot be null."));
                });
            });

            runner.testGroup("toBooleanArray(Iterable<Boolean>)", () ->
            {
                runner.test("with null Iterable", (Test test) ->
                {
                    test.assertThrows(() -> Array.toBooleanArray((Iterable<Boolean>)null), new PreConditionFailure("values cannot be null."));
                });

                runner.test("with empty Iterable", (Test test) ->
                {
                    test.assertEqual(new boolean[0], Array.toBooleanArray(Iterable.create()).await());
                });

                runner.test("with non-empty", (Test test) ->
                {
                    test.assertEqual(
                        new boolean[] { false, true, false },
                        Array.toBooleanArray(Iterable.create(false, true, false)).await());
                });

                runner.test("with null value", (Test test) ->
                {
                    test.assertThrows(() -> Array.toBooleanArray(Iterable.create(null, true, false)).await(),
                        new NullPointerException("The 0 element cannot be null."));
                });
            });

            runner.testGroup("createCharacter(char[])", () ->
            {
                runner.test("with null array", (Test test) ->
                {
                    test.assertThrows(() -> Array.createCharacter((char[])null),
                        new PreConditionFailure("values cannot be null."));
                });

                runner.test("with empty array", (Test test) ->
                {
                    final Array<Character> array = Array.createCharacter(new char[0]);
                    test.assertEqual(0, array.getCount());
                });

                runner.test("with one value array", (Test test) ->
                {
                    final Array<Character> array = Array.createCharacter(new char[] { 'a' });
                    test.assertEqual(1, array.getCount());
                    test.assertEqual('a', array.get(0));
                });

                runner.test("with two value array", (Test test) ->
                {
                    final Array<Character> array = Array.createCharacter(new char[] { 'b', 'c' });
                    test.assertEqual(2, array.getCount());
                    test.assertEqual('b', array.get(0));
                    test.assertEqual('c', array.get(1));
                });

                runner.test("with one value", (Test test) ->
                {
                    final Array<Character> array = Array.createCharacter('a');
                    test.assertEqual(1, array.getCount());
                    test.assertEqual('a', array.get(0));
                });

                runner.test("with two values", (Test test) ->
                {
                    final Array<Character> array = Array.createCharacter('b', 'c');
                    test.assertEqual(2, array.getCount());
                    test.assertEqual('b', array.get(0));
                    test.assertEqual('c', array.get(1));
                });
            });

            runner.testGroup("createInteger(int[])", () ->
            {
                runner.test("with null array", (Test test) ->
                {
                    test.assertThrows(() -> Array.createInteger((int[])null),
                        new PreConditionFailure("values cannot be null."));
                });

                runner.test("with empty array", (Test test) ->
                {
                    final Array<Integer> array = Array.createInteger(new int[0]);
                    test.assertEqual(0, array.getCount());
                });

                runner.test("with one value array", (Test test) ->
                {
                    final Array<Integer> array = Array.createInteger(new int[] { 101 });
                    test.assertEqual(1, array.getCount());
                    test.assertEqual(101, array.get(0));
                });

                runner.test("with two value array", (Test test) ->
                {
                    final Array<Integer> array = Array.createInteger(new int[] { 101, 102 });
                    test.assertEqual(2, array.getCount());
                    test.assertEqual(101, array.get(0));
                    test.assertEqual(102, array.get(1));
                });

                runner.test("with no values", (Test test) ->
                {
                    final Array<Integer> array = Array.createInteger();
                    test.assertEqual(0, array.getCount());
                });

                runner.test("with one value", (Test test) ->
                {
                    final Array<Integer> array = Array.createInteger(101);
                    test.assertEqual(1, array.getCount());
                    test.assertEqual(101, array.get(0));
                });

                runner.test("with two values", (Test test) ->
                {
                    final Array<Integer> array = Array.createInteger(101, 102);
                    test.assertEqual(2, array.getCount());
                    test.assertEqual(101, array.get(0));
                    test.assertEqual(102, array.get(1));
                });
            });

            runner.testGroup("create(T...)", () ->
            {
                runner.test("with null array", (Test test) ->
                {
                    test.assertThrows(() -> Array.create((String[])null), new PreConditionFailure("values cannot be null."));
                });

                runner.test("with empty array", (Test test) ->
                {
                    final Array<String> array = Array.create(new String[0]);
                    test.assertEqual(0, array.getCount());
                });

                runner.test("with one value array", (Test test) ->
                {
                    final Array<String> array = Array.create(new String[] { "101" });
                    test.assertEqual(1, array.getCount());
                    test.assertEqual("101", array.get(0));
                });

                runner.test("with two value array", (Test test) ->
                {
                    final Array<String> array = Array.create(new String[] { "101", "102" });
                    test.assertEqual(2, array.getCount());
                    test.assertEqual("101", array.get(0));
                    test.assertEqual("102", array.get(1));
                });

                runner.test("with one value", (Test test) ->
                {
                    final Array<String> array = Array.create("101");
                    test.assertEqual(1, array.getCount());
                    test.assertEqual("101", array.get(0));
                });

                runner.test("with two values", (Test test) ->
                {
                    final Array<String> array = Array.create("101", "102");
                    test.assertEqual(2, array.getCount());
                    test.assertEqual("101", array.get(0));
                    test.assertEqual("102", array.get(1));
                });
            });
            
            runner.testGroup("get()", () ->
            {
                runner.test("with negative index", (Test test) ->
                {
                    final Array<Integer> a = Array.createWithLength(10);
                    test.assertThrows(() -> a.get(-1),
                        new PreConditionFailure("index (-1) must be between 0 and 9."));
                });
                
                runner.test("with too large index", (Test test) ->
                {
                    final Array<Integer> a = Array.createWithLength(10);
                    test.assertThrows(() -> a.get(10),
                        new PreConditionFailure("index (10) must be between 0 and 9."));
                });
            });
            
            runner.testGroup("set()", () ->
            {
                runner.test("with negative index", (Test test) ->
                {
                    final Array<Integer> a = Array.createWithLength(10);
                    test.assertThrows(() -> a.set(-1, 49),
                        new PreConditionFailure("index (-1) must be between 0 and 9."));
                });
                
                runner.test("with too large index", (Test test) ->
                {
                    final Array<Integer> a = Array.createWithLength(10);
                    test.assertThrows(() -> a.set(10, 48),
                        new PreConditionFailure("index (10) must be between 0 and 9."));
                });
                
                runner.test("with indexes in bounds", (Test test) ->
                {
                    final Array<Integer> a = Array.createWithLength(11);
                    for (int i = 0; i < a.getCount(); ++i) {
                        a.set(i, i);
                        test.assertEqual(i, a.get(i));
                    }
                });
            });
            
            runner.testGroup("iterateReverse()", () ->
            {
                runner.test("with empty Array", (Test test) ->
                {
                    final Array<Integer> array = Array.createWithLength(0);
                    final Iterator<Integer> iterator = array.iterateReverse();
                    test.assertFalse(iterator.hasStarted());
                    test.assertFalse(iterator.hasCurrent());
                    test.assertThrows(iterator::getCurrent, new PreConditionFailure("hasCurrent() cannot be false."));

                    test.assertFalse(iterator.next());
                    test.assertTrue(iterator.hasStarted());
                    test.assertFalse(iterator.hasCurrent());
                    test.assertThrows(iterator::getCurrent, new PreConditionFailure("hasCurrent() cannot be false."));
                });

                runner.test("with non-empty Array", (Test test) ->
                {
                    final Array<Integer> array = Array.createWithLength(10);
                    for (int i = 0; i < array.getCount(); ++i)
                    {
                        array.set(i, i);
                    }
                    final Iterator<Integer> iterator = array.iterateReverse();
                    test.assertFalse(iterator.hasStarted());
                    test.assertFalse(iterator.hasCurrent());
                    test.assertThrows(iterator::getCurrent, new PreConditionFailure("hasCurrent() cannot be false."));

                    for (int i = 9; i >= 0; --i)
                    {
                        test.assertTrue(iterator.next());
                        test.assertTrue(iterator.hasStarted());
                        test.assertTrue(iterator.hasCurrent());
                        test.assertEqual(i, iterator.getCurrent());
                    }

                    test.assertFalse(iterator.next());
                    test.assertTrue(iterator.hasStarted());
                    test.assertFalse(iterator.hasCurrent());
                    test.assertThrows(iterator::getCurrent, new PreConditionFailure("hasCurrent() cannot be false."));
                });
            });

            runner.testGroup("createByte(byte...)", () ->
            {
                runner.test("with null array", (Test test) ->
                {
                    test.assertThrows(() -> Array.createByte((byte[])null),
                        new PreConditionFailure("values cannot be null."));
                });

                runner.test("with empty array", (Test test) ->
                {
                    final ByteArray array = Array.createByte(new byte[0]);
                    test.assertEqual(0, array.getCount());
                });

                runner.test("with one value array", (Test test) ->
                {
                    final ByteArray array = Array.createByte(new byte[] { 12 });
                    test.assertEqual(1, array.getCount());
                    test.assertEqual(12, array.get(0).intValue());
                });

                runner.test("with two value array", (Test test) ->
                {
                    final ByteArray array = Array.createByte(new byte[] { 13, 14 });
                    test.assertEqual(2, array.getCount());
                    test.assertEqual(13, array.get(0).intValue());
                    test.assertEqual(14, array.get(1).intValue());
                });

                runner.test("with one value", (Test test) ->
                {
                    final ByteArray array = Array.createByte((byte)12);
                    test.assertEqual(1, array.getCount());
                    test.assertEqual(12, array.get(0).intValue());
                });

                runner.test("with two value array", (Test test) ->
                {
                    final ByteArray array = Array.createByte((byte)13, (byte)14);
                    test.assertEqual(2, array.getCount());
                    test.assertEqual(13, array.get(0).intValue());
                    test.assertEqual(14, array.get(1).intValue());
                });
            });

            runner.testGroup("createByte(byte[],int,int)", () ->
            {
                runner.test("with null array", (Test test) ->
                {
                    test.assertThrows(() -> Array.createByte((byte[])null, 1, 2),
                        new PreConditionFailure("values cannot be null."));
                });

                runner.test("with empty array and negative start index", (Test test) ->
                {
                    test.assertThrows(() -> Array.createByte(new byte[0], -1, 1),
                        new PreConditionFailure("startIndex (-1) must be equal to 0."));
                });

                runner.test("with empty array and too large start index", (Test test) ->
                {
                    test.assertThrows(() -> Array.createByte(new byte[0], 1, 2),
                        new PreConditionFailure("startIndex (1) must be equal to 0."));
                });

                runner.test("with empty array and negative length", (Test test) ->
                {
                    test.assertThrows(() -> Array.createByte(new byte[0], 0, -1),
                        new PreConditionFailure("length (-1) must be between 1 and 0."));
                });

                runner.test("with empty array and zero length", (Test test) ->
                {
                    test.assertThrows(() -> Array.createByte(new byte[0], 0, 0),
                        new PreConditionFailure("length (0) must be between 1 and 0."));
                });

                runner.test("with empty array and too large length", (Test test) ->
                {
                    test.assertThrows(() -> Array.createByte(new byte[0], 0, 1),
                        new PreConditionFailure("length (1) must be between 1 and 0."));
                });

                runner.test("with one value array and negative start index", (Test test) ->
                {
                    test.assertThrows(() -> Array.createByte(new byte[] { 12 }, -1, 1),
                        new PreConditionFailure("startIndex (-1) must be equal to 0."));
                });

                runner.test("with one value array and too large start index", (Test test) ->
                {
                    test.assertThrows(() -> Array.createByte(new byte[] { 12 }, 1, 1),
                        new PreConditionFailure("startIndex (1) must be equal to 0."));
                });

                runner.test("with one value array and negative length", (Test test) ->
                {
                    test.assertThrows(() -> Array.createByte(new byte[] { 12 }, 0, -1),
                        new PreConditionFailure("length (-1) must be equal to 1."));
                });

                runner.test("with one value array and zero length", (Test test) ->
                {
                    test.assertThrows(() -> Array.createByte(new byte[] { 12 }, 0, 0),
                        new PreConditionFailure("length (0) must be equal to 1."));
                });

                runner.test("with one value array and too large length", (Test test) ->
                {
                    test.assertThrows(() -> Array.createByte(new byte[] { 12 }, 0, 2),
                        new PreConditionFailure("length (2) must be equal to 1."));
                });

                runner.test("with one value array and valid startIndex and length", (Test test) ->
                {
                    final ByteArray array = Array.createByte(new byte[] { 12 }, 0, 1);
                    test.assertEqual(1, array.getCount());
                    test.assertEqual(12, array.get(0).intValue());
                });

                runner.test("with two value array and negative startIndex", (Test test) ->
                {
                    test.assertThrows(() -> Array.createByte(new byte[] { 13, 14 }, -1, 1),
                        new PreConditionFailure("startIndex (-1) must be between 0 and 1."));
                });

                runner.test("with two value array and too large startIndex", (Test test) ->
                {
                    test.assertThrows(() -> Array.createByte(new byte[] { 13, 14 }, 2, 1),
                        new PreConditionFailure("startIndex (2) must be between 0 and 1."));
                });

                runner.test("with two value array and negative length", (Test test) ->
                {
                    test.assertThrows(() -> Array.createByte(new byte[] { 13, 14 }, 0, -1),
                        new PreConditionFailure("length (-1) must be between 1 and 2."));
                });

                runner.test("with two value array and too large length", (Test test) ->
                {
                    test.assertThrows(() -> Array.createByte(new byte[] { 13, 14 }, 0, 3),
                        new PreConditionFailure("length (3) must be between 1 and 2."));
                });

                runner.test("with two value array and valid startIndex and length", (Test test) ->
                {
                    final ByteArray array = Array.createByte(new byte[] { 13, 14 }, 1, 1);
                    test.assertEqual(1, array.getCount());
                    test.assertEqual(14, array.get(0).intValue());
                });
            });

            runner.testGroup("createByte(int...)", () ->
            {
                runner.test("with null array", (Test test) ->
                {
                    test.assertThrows(() -> Array.createByte((int[])null),
                        new PreConditionFailure("bytes cannot be null."));
                });

                runner.test("with empty array", (Test test) ->
                {
                    final ByteArray array = Array.createByte(new int[0]);
                    test.assertEqual(0, array.getCount());
                });

                runner.test("with one value array", (Test test) ->
                {
                    final ByteArray array = Array.createByte(new int[] { 12 });
                    test.assertEqual(1, array.getCount());
                    test.assertEqual(12, array.get(0).intValue());
                });

                runner.test("with two value array", (Test test) ->
                {
                    final ByteArray array = Array.createByte(new int[] { 13, 14 });
                    test.assertEqual(2, array.getCount());
                    test.assertEqual(13, array.get(0).intValue());
                    test.assertEqual(14, array.get(1).intValue());
                });

                runner.test("with one value", (Test test) ->
                {
                    final ByteArray array = Array.createByte(12);
                    test.assertEqual(1, array.getCount());
                    test.assertEqual(12, array.get(0).intValue());
                });

                runner.test("with two value array", (Test test) ->
                {
                    final ByteArray array = Array.createByte(13, 14);
                    test.assertEqual(2, array.getCount());
                    test.assertEqual(13, array.get(0).intValue());
                    test.assertEqual(14, array.get(1).intValue());
                });

                runner.test("with non-byte value", (Test test) ->
                {
                    test.assertThrows(() -> Array.createByte(13, 1334),
                        new PreConditionFailure("The 1 element (1334) must be between -128 and 127."));
                });
            });

            runner.testGroup("createByte(int[],int,int)", () ->
            {
                runner.test("with null array", (Test test) ->
                {
                    test.assertThrows(() -> Array.createByte((int[])null, 1, 2),
                        new PreConditionFailure("values cannot be null."));
                });

                runner.test("with empty array and negative start index", (Test test) ->
                {
                    test.assertThrows(() -> Array.createByte(new int[0], -1, 1),
                        new PreConditionFailure("startIndex (-1) must be equal to 0."));
                });

                runner.test("with empty array and too large start index", (Test test) ->
                {
                    test.assertThrows(() -> Array.createByte(new int[0], 1, 2),
                        new PreConditionFailure("startIndex (1) must be equal to 0."));
                });

                runner.test("with empty array and negative length", (Test test) ->
                {
                    test.assertThrows(() -> Array.createByte(new int[0], 0, -1),
                        new PreConditionFailure("length (-1) must be between 1 and 0."));
                });

                runner.test("with empty array and zero length", (Test test) ->
                {
                    test.assertThrows(() -> Array.createByte(new int[0], 0, 0),
                        new PreConditionFailure("length (0) must be between 1 and 0."));
                });

                runner.test("with empty array and too large length", (Test test) ->
                {
                    test.assertThrows(() -> Array.createByte(new int[0], 0, 1),
                        new PreConditionFailure("length (1) must be between 1 and 0."));
                });

                runner.test("with one value array and negative start index", (Test test) ->
                {
                    test.assertThrows(() -> Array.createByte(new int[] { 12 }, -1, 1),
                        new PreConditionFailure("startIndex (-1) must be equal to 0."));
                });

                runner.test("with one value array and too large start index", (Test test) ->
                {
                    test.assertThrows(() -> Array.createByte(new int[] { 12 }, 1, 1),
                        new PreConditionFailure("startIndex (1) must be equal to 0."));
                });

                runner.test("with one value array and negative length", (Test test) ->
                {
                    test.assertThrows(() -> Array.createByte(new int[] { 12 }, 0, -1),
                        new PreConditionFailure("length (-1) must be equal to 1."));
                });

                runner.test("with one value array and zero length", (Test test) ->
                {
                    test.assertThrows(() -> Array.createByte(new int[] { 12 }, 0, 0),
                        new PreConditionFailure("length (0) must be equal to 1."));
                });

                runner.test("with one value array and too large length", (Test test) ->
                {
                    test.assertThrows(() -> Array.createByte(new int[] { 12 }, 0, 2),
                        new PreConditionFailure("length (2) must be equal to 1."));
                });

                runner.test("with one value array and valid startIndex and length", (Test test) ->
                {
                    final ByteArray array = Array.createByte(new int[] { 12 }, 0, 1);
                    test.assertEqual(1, array.getCount());
                    test.assertEqual(12, array.get(0).intValue());
                });

                runner.test("with two value array and negative startIndex", (Test test) ->
                {
                    test.assertThrows(() -> Array.createByte(new int[] { 13, 14 }, -1, 1),
                        new PreConditionFailure("startIndex (-1) must be between 0 and 1."));
                });

                runner.test("with two value array and too large startIndex", (Test test) ->
                {
                    test.assertThrows(() -> Array.createByte(new int[] { 13, 14 }, 2, 1),
                        new PreConditionFailure("startIndex (2) must be between 0 and 1."));
                });

                runner.test("with two value array and negative length", (Test test) ->
                {
                    test.assertThrows(() -> Array.createByte(new int[] { 13, 14 }, 0, -1),
                        new PreConditionFailure("length (-1) must be between 1 and 2."));
                });

                runner.test("with two value array and too large length", (Test test) ->
                {
                    test.assertThrows(() -> Array.createByte(new int[] { 13, 14 }, 0, 3),
                        new PreConditionFailure("length (3) must be between 1 and 2."));
                });

                runner.test("with two value array and valid startIndex and length", (Test test) ->
                {
                    final ByteArray array = Array.createByte(new int[] { 13, 14 }, 1, 1);
                    test.assertEqual(1, array.getCount());
                    test.assertEqual(14, array.get(0).intValue());
                });
            });

            runner.testGroup("toByteArray(Iterator<Byte>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> Array.toByteArray((Iterator<Byte>)null), new PreConditionFailure("values cannot be null."));
                });

                runner.test("with empty", (Test test) ->
                {
                    test.assertEqual(new byte[0], Array.toByteArray(Iterator.create()).await());
                });

                runner.test("with non-empty", (Test test) ->
                {
                    test.assertEqual(new byte[] { 0, 1, 2 }, Array.toByteArray(Iterator.create((byte)0, (byte)1, (byte)2)).await());
                });

                runner.test("with null value", (Test test) ->
                {
                    test.assertThrows(() -> Array.toByteArray(Iterator.create((byte)0, (byte)1, null)).await(),
                        new NullPointerException("The 2 element cannot be null."));
                });
            });

            runner.testGroup("toByteArray(Iterable<Byte>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> Array.toByteArray((Iterable<Byte>)null), new PreConditionFailure("values cannot be null."));
                });

                runner.test("with empty", (Test test) ->
                {
                    test.assertEqual(new byte[0], Array.toByteArray(Iterable.create()).await());
                });

                runner.test("with non-empty", (Test test) ->
                {
                    test.assertEqual(new byte[] { 0, 1, 2 }, Array.toByteArray(Iterable.create((byte)0, (byte)1, (byte)2)).await());
                });

                runner.test("with null value", (Test test) ->
                {
                    test.assertThrows(() -> Array.toByteArray(Iterable.create((byte)0, (byte)1, null)).await(),
                        new NullPointerException("The 2 element cannot be null."));
                });
            });

            runner.testGroup("toIntArray()", () ->
            {
                runner.test("with null Iterator", (Test test) ->
                {
                    test.assertThrows(() -> Array.toIntArray((Iterator<Integer>)null), new PreConditionFailure("values cannot be null."));
                });

                runner.test("with empty Iterator", (Test test) ->
                {
                    test.assertEqual(new int[0], Array.toIntArray(Iterator.create()));
                });

                runner.test("with non-empty Iterator", (Test test) ->
                {
                    test.assertEqual(new int[] { 0, 1, 2 }, Array.toIntArray(Iterator.create(0, 1, 2)));
                });

                runner.test("with null Iterable", (Test test) ->
                {
                    test.assertThrows(() -> Array.toIntArray((Iterable<Integer>)null), new PreConditionFailure("values cannot be null."));
                });

                runner.test("with empty Iterable", (Test test) ->
                {
                    test.assertEqual(new int[0], Array.toIntArray(Iterable.create()));
                });
            });

            runner.testGroup("toStringArray()", () ->
            {
                runner.test("with null Iterator", (Test test) ->
                {
                    test.assertThrows(() -> Array.toStringArray((Iterator<String>)null), new PreConditionFailure("values cannot be null."));
                });

                runner.test("with empty Iterator", (Test test) ->
                {
                    test.assertEqual(new String[0], Array.toStringArray(Iterator.create()));
                });

                runner.test("with non-empty Iterator", (Test test) ->
                {
                    test.assertEqual(new String[] { "0", "1", "2" }, Array.toStringArray(Iterator.create("0", "1", "2")));
                });

                runner.test("with null Iterable", (Test test) ->
                {
                    test.assertThrows(() -> Array.toStringArray((Iterable<String>)null), new PreConditionFailure("values cannot be null."));
                });

                runner.test("with empty Iterable", (Test test) ->
                {
                    test.assertEqual(new String[0], Array.toStringArray(Iterable.create()));
                });
            });

            runner.testGroup("clone(byte[])", () ->
            {
                final Action1<byte[]> cloneTest = (byte[] bytes) ->
                {
                    runner.test("with " + (bytes == null ? "null byte[]" : ("byte[" + bytes.length + "]")), (Test test) ->
                    {
                        final byte[] clonedBytes = Array.clone(bytes);
                        if (bytes == null || bytes.length == 0)
                        {
                            test.assertSame(bytes, clonedBytes);
                        }
                        else
                        {
                            test.assertEqual(bytes, clonedBytes);
                            test.assertNotSame(bytes, clonedBytes);
                        }
                    });
                };

                cloneTest.run(null);
                cloneTest.run(new byte[0]);
                cloneTest.run(new byte[] { 0 });
                cloneTest.run(new byte[] { 0, 1, 2, 3, 4 });
            });

            runner.testGroup("clone(byte[],int,int)", () ->
            {
                final Action4<byte[],Integer,Integer,byte[]> cloneTest = (byte[] bytes, Integer startIndex, Integer length, byte[] expectedBytes) ->
                {
                    runner.test("with " + (bytes == null ? "null byte[]" : ("byte[" + bytes.length + "]")) + " at " + startIndex + " for " + length + " length", (Test test) ->
                    {
                        final byte[] clonedBytes = Array.clone(bytes, startIndex, length);
                        test.assertEqual(expectedBytes, clonedBytes);
                    });
                };

                cloneTest.run(null, -1, -2, null);
                cloneTest.run(null, -1, 0, null);
                cloneTest.run(null, -1, 2, null);
                cloneTest.run(null, 0, -2, null);
                cloneTest.run(null, 0, 0, null);
                cloneTest.run(null, 0, 2, null);
                cloneTest.run(null, 1, -2, null);
                cloneTest.run(null, 1, 0, null);
                cloneTest.run(null, 1, 2, null);
                cloneTest.run(new byte[0], -1, -2, null);
                cloneTest.run(new byte[0], -1, 0, null);
                cloneTest.run(new byte[0], -1, 2, null);
                cloneTest.run(new byte[0], 0, -2, null);
                cloneTest.run(new byte[0], 0, 0, new byte[0]);
                cloneTest.run(new byte[0], 0, 2, new byte[0]);
                cloneTest.run(new byte[0], 1, -2, null);
                cloneTest.run(new byte[0], 1, 0, null);
                cloneTest.run(new byte[0], 1, 2, null);
                cloneTest.run(new byte[] { 0, 1, 2 }, 0, 3, new byte[] { 0, 1, 2 });
                cloneTest.run(new byte[] { 0, 1, 2 }, 1, 1, new byte[] { 1 });
            });

            runner.testGroup("clone(char[])", () ->
            {
                final Action1<char[]> test = (char[] characters) ->
                {
                    runner.test("with " + (characters == null ? "null char[]" : ("char[" + characters.length + "]")), test1 ->
                    {
                        final char[] clonedCharacters = Array.clone(characters);
                        if (characters == null || characters.length == 0)
                        {
                            test1.assertSame(characters, clonedCharacters);
                        }
                        else
                        {
                            test1.assertEqual(characters, clonedCharacters);
                            test1.assertNotSame(characters, clonedCharacters);
                        }
                    });
                };

                test.run(null);
                test.run(new char[0]);
                test.run(new char[] { 'a' });
                test.run(new char[] { 'a', 'b', 'c', 'd', 'e' });
            });
            
            runner.testGroup("clone(char[],int,int)", () ->
            {
                final Action4<char[],Integer,Integer,char[]> cloneTest = (char[] characters, Integer startIndex, Integer length, char[] expectedCharacters) ->
                {
                    runner.test("with " + (characters == null ? "null char[]" : ("char[" + characters.length + "]")) + " at " + startIndex + " for " + length + " length", (Test test) ->
                    {
                        final char[] clonedCharacters = Array.clone(characters, startIndex, length);
                        test.assertEqual(expectedCharacters, clonedCharacters);
                    });
                };

                cloneTest.run(null, -1, -2, null);
                cloneTest.run(null, -1, 0, null);
                cloneTest.run(null, -1, 2, null);
                cloneTest.run(null, 0, -2, null);
                cloneTest.run(null, 0, 0, null);
                cloneTest.run(null, 0, 2, null);
                cloneTest.run(null, 1, -2, null);
                cloneTest.run(null, 1, 0, null);
                cloneTest.run(null, 1, 2, null);
                cloneTest.run(new char[0], -1, -2, null);
                cloneTest.run(new char[0], -1, 0, null);
                cloneTest.run(new char[0], -1, 2, null);
                cloneTest.run(new char[0], 0, -2, null);
                cloneTest.run(new char[0], 0, 0, new char[0]);
                cloneTest.run(new char[0], 0, 2, new char[0]);
                cloneTest.run(new char[0], 1, -2, null);
                cloneTest.run(new char[0], 1, 0, null);
                cloneTest.run(new char[0], 1, 2, null);
                cloneTest.run(new char[] { 'a', 'b', 'c' }, 0, 3, new char[] { 'a', 'b', 'c' });
                cloneTest.run(new char[] { 'x', 'y', 'z' }, 1, 1, new char[] { 'y' });
            });

            runner.testGroup("clone(int[])", () ->
            {
                final Action1<int[]> test = (int[] integers) ->
                {
                    runner.test("with " + (integers == null ? "null int[]" : ("int[" + integers.length + "]")), test1 ->
                    {
                        final int[] clonedIntegers = Array.clone(integers);
                        if (integers == null || integers.length == 0)
                        {
                            test1.assertSame(integers, clonedIntegers);
                        }
                        else
                        {
                            test1.assertEqual(integers, clonedIntegers);
                            test1.assertNotSame(integers, clonedIntegers);
                        }
                    });
                };

                test.run(null);
                test.run(new int[0]);
                test.run(new int[] { 1 });
                test.run(new int[] { 1, 2, 3, 4, 5 });
            });

            runner.testGroup("clone(int[],int,int)", () ->
            {
                final Action4<int[],Integer,Integer,int[]> cloneTest = (int[] integers, Integer startIndex, Integer length, int[] expectedIntegers) ->
                {
                    runner.test("with " + (integers == null ? "null int[]" : ("int[" + integers.length + "]")) + " at " + startIndex + " for " + length + " length", (Test test) ->
                    {
                        final int[] clonedCharacters = Array.clone(integers, startIndex, length);
                        test.assertEqual(expectedIntegers, clonedCharacters);
                    });
                };

                cloneTest.run(null, -1, -2, null);
                cloneTest.run(null, -1, 0, null);
                cloneTest.run(null, -1, 2, null);
                cloneTest.run(null, 0, -2, null);
                cloneTest.run(null, 0, 0, null);
                cloneTest.run(null, 0, 2, null);
                cloneTest.run(null, 1, -2, null);
                cloneTest.run(null, 1, 0, null);
                cloneTest.run(null, 1, 2, null);
                cloneTest.run(new int[0], -1, -2, null);
                cloneTest.run(new int[0], -1, 0, null);
                cloneTest.run(new int[0], -1, 2, null);
                cloneTest.run(new int[0], 0, -2, null);
                cloneTest.run(new int[0], 0, 0, new int[0]);
                cloneTest.run(new int[0], 0, 2, new int[0]);
                cloneTest.run(new int[0], 1, -2, null);
                cloneTest.run(new int[0], 1, 0, null);
                cloneTest.run(new int[0], 1, 2, null);
                cloneTest.run(new int[] { 1, 2, 3 }, 0, 3, new int[] { 1, 2, 3 });
                cloneTest.run(new int[] { 10, 11, 12 }, 1, 1, new int[] { 11 });
            });

            runner.testGroup("copy(byte[],int,byte[],int,int)", () ->
            {
                final Action6<byte[],Integer,byte[],Integer,Integer,RuntimeException> copyErrorTest = (byte[] copyFrom, Integer copyFromStartIndex, byte[] copyTo, Integer copyToStartIndex, Integer length, RuntimeException expectedError) ->
                {
                    runner.test("copy " + Array.toString(copyFrom) + " at index " + copyFromStartIndex + " to " + Array.toString(copyTo) + " at index " + copyToStartIndex + " for length " + length, (Test test) ->
                    {
                        final byte[] copyFromClone = Array.clone(copyFrom);
                        final byte[] copyToClone = Array.clone(copyTo);

                        test.assertThrows(() -> Array.copy(copyFrom, copyFromStartIndex, copyTo, copyToStartIndex, length), expectedError);

                        test.assertEqual(copyFromClone, copyFrom);
                        test.assertEqual(copyToClone, copyTo);
                    });
                };

                copyErrorTest.run(null, 0, new byte[0], 0, 0, new PreConditionFailure("copyFrom cannot be null."));
                copyErrorTest.run(new byte[0], -1, new byte[0], 0, 0, new PreConditionFailure("copyFromStartIndex (-1) must be equal to 0."));
                copyErrorTest.run(new byte[0], 1, new byte[0], 0, 0, new PreConditionFailure("copyFromStartIndex (1) must be equal to 0."));
                copyErrorTest.run(new byte[0], 0, null, 0, 0, new PreConditionFailure("copyTo cannot be null."));
                copyErrorTest.run(new byte[0], 0, new byte[0], -1, 0, new PreConditionFailure("copyToStartIndex (-1) must be equal to 0."));
                copyErrorTest.run(new byte[0], 0, new byte[0], 1, 0, new PreConditionFailure("copyToStartIndex (1) must be equal to 0."));
                copyErrorTest.run(new byte[0], 0, new byte[0], 0, -1, new PreConditionFailure("length (-1) must be equal to 0."));
                copyErrorTest.run(new byte[0], 0, new byte[0], 0, 1, new PreConditionFailure("length (1) must be equal to 0."));

                final Action6<byte[],Integer,byte[],Integer,Integer,byte[]> copyTest = (byte[] copyFrom, Integer copyFromStartIndex, byte[] copyTo, Integer copyToStartIndex, Integer length, byte[] expectedBytes) ->
                {
                    runner.test("copy " + Array.toString(copyFrom) + " at index " + copyFromStartIndex + " to " + Array.toString(copyTo) + " at index " + copyToStartIndex + " for length " + length, (Test test) ->
                    {
                        final byte[] copyFromClone = Array.clone(copyFrom);

                        Array.copy(copyFrom, copyFromStartIndex, copyTo, copyToStartIndex, length);

                        test.assertEqual(copyFromClone, copyFrom);
                        test.assertEqual(expectedBytes, copyTo);
                    });
                };

                copyTest.run(new byte[0], 0, new byte[0], 0, 0, new byte[0]);
                copyTest.run(new byte[0], 0, new byte[] { 2 }, 0, 0, new byte[] { 2 });
                copyTest.run(new byte[] { 1 }, 0, new byte[0], 0, 0, new byte[0]);
                copyTest.run(new byte[] { 1 }, 0, new byte[] { 2 }, 0, 0, new byte[] { 2 });
                copyTest.run(new byte[] { 1 }, 0, new byte[] { 2 }, 0, 1, new byte[] { 1 });
                copyTest.run(new byte[] { 0, 1, 2, 3, 4 }, 1, new byte[] { 5, 6, 7, 8, 9, 10, 11 }, 2, 3, new byte[] { 5, 6, 1, 2, 3, 10, 11 });
            });

            runner.testGroup("copy(char[],int,char[],int,int)", () ->
            {
                final Action6<char[],Integer,char[],Integer,Integer,char[]> copyTest = (char[] copyFrom, Integer copyFromStartIndex, char[] copyTo, Integer copyToStartIndex, Integer length, char[] expected) ->
                {
                    runner.test("create " + Array.toString(copyFrom) + " at index " + copyFromStartIndex + " to " + Array.toString(copyTo) + " at index " + copyToStartIndex + " for length " + length, (Test test) ->
                    {
                        final char[] copyFromClone = Array.clone(copyFrom);

                        Array.copy(copyFrom, copyFromStartIndex, copyTo, copyToStartIndex, length);

                        test.assertEqual(copyFromClone, copyFrom);
                        test.assertEqual(expected, copyTo);
                    });
                };

                copyTest.run(null, -1, null, -1, -1, null);
                copyTest.run(null, -1, null, -1, 0, null);
                copyTest.run(null, -1, null, -1, 1, null);
                copyTest.run(null, -1, null, 0, -1, null);
                copyTest.run(null, -1, null, 0, 0, null);
                copyTest.run(null, -1, null, 0, 1, null);
                copyTest.run(null, -1, null, 1, -1, null);
                copyTest.run(null, -1, null, 1, 0, null);
                copyTest.run(null, -1, null, 1, 1, null);
                copyTest.run(null, -1, new char[0], -1, -1, new char[0]);
                copyTest.run(null, -1, new char[0], -1, 0, new char[0]);
                copyTest.run(null, -1, new char[0], -1, 1, new char[0]);
                copyTest.run(null, -1, new char[0], 0, -1, new char[0]);
                copyTest.run(null, -1, new char[0], 0, 0, new char[0]);
                copyTest.run(null, -1, new char[0], 0, 1, new char[0]);
                copyTest.run(null, -1, new char[0], 1, -1, new char[0]);
                copyTest.run(null, -1, new char[0], 1, 0, new char[0]);
                copyTest.run(null, -1, new char[0], 1, 1, new char[0]);
                copyTest.run(null, -1, new char[] { 'b' }, -1, -1, new char[] { 'b' });
                copyTest.run(null, -1, new char[] { 'b' }, -1, 0, new char[] { 'b' });
                copyTest.run(null, -1, new char[] { 'b' }, -1, 1, new char[] { 'b' });
                copyTest.run(null, -1, new char[] { 'b' }, 0, -1, new char[] { 'b' });
                copyTest.run(null, -1, new char[] { 'b' }, 0, 0, new char[] { 'b' });
                copyTest.run(null, -1, new char[] { 'b' }, 0, 1, new char[] { 'b' });
                copyTest.run(null, -1, new char[] { 'b' }, 1, -1, new char[] { 'b' });
                copyTest.run(null, -1, new char[] { 'b' }, 1, 0, new char[] { 'b' });
                copyTest.run(null, -1, new char[] { 'b' }, 1, 1, new char[] { 'b' });
                copyTest.run(null, 0, null, -1, -1, null);
                copyTest.run(null, 0, null, -1, 0, null);
                copyTest.run(null, 0, null, -1, 1, null);
                copyTest.run(null, 0, null, 0, -1, null);
                copyTest.run(null, 0, null, 0, 0, null);
                copyTest.run(null, 0, null, 0, 1, null);
                copyTest.run(null, 0, null, 1, -1, null);
                copyTest.run(null, 0, null, 1, 0, null);
                copyTest.run(null, 0, null, 1, 1, null);
                copyTest.run(null, 0, new char[0], -1, -1, new char[0]);
                copyTest.run(null, 0, new char[0], -1, 0, new char[0]);
                copyTest.run(null, 0, new char[0], -1, 1, new char[0]);
                copyTest.run(null, 0, new char[0], 0, -1, new char[0]);
                copyTest.run(null, 0, new char[0], 0, 0, new char[0]);
                copyTest.run(null, 0, new char[0], 0, 1, new char[0]);
                copyTest.run(null, 0, new char[0], 1, -1, new char[0]);
                copyTest.run(null, 0, new char[0], 1, 0, new char[0]);
                copyTest.run(null, 0, new char[0], 1, 1, new char[0]);
                copyTest.run(null, 0, new char[] { 'b' }, -1, -1, new char[] { 'b' });
                copyTest.run(null, 0, new char[] { 'b' }, -1, 0, new char[] { 'b' });
                copyTest.run(null, 0, new char[] { 'b' }, -1, 1, new char[] { 'b' });
                copyTest.run(null, 0, new char[] { 'b' }, 0, -1, new char[] { 'b' });
                copyTest.run(null, 0, new char[] { 'b' }, 0, 0, new char[] { 'b' });
                copyTest.run(null, 0, new char[] { 'b' }, 0, 1, new char[] { 'b' });
                copyTest.run(null, 0, new char[] { 'b' }, 1, -1, new char[] { 'b' });
                copyTest.run(null, 0, new char[] { 'b' }, 1, 0, new char[] { 'b' });
                copyTest.run(null, 0, new char[] { 'b' }, 1, 1, new char[] { 'b' });
                copyTest.run(null, 1, null, -1, -1, null);
                copyTest.run(null, 1, null, -1, 0, null);
                copyTest.run(null, 1, null, -1, 1, null);
                copyTest.run(null, 1, null, 0, -1, null);
                copyTest.run(null, 1, null, 0, 0, null);
                copyTest.run(null, 1, null, 0, 1, null);
                copyTest.run(null, 1, null, 1, -1, null);
                copyTest.run(null, 1, null, 1, 0, null);
                copyTest.run(null, 1, null, 1, 1, null);
                copyTest.run(null, 1, new char[0], -1, -1, new char[0]);
                copyTest.run(null, 1, new char[0], -1, 0, new char[0]);
                copyTest.run(null, 1, new char[0], -1, 1, new char[0]);
                copyTest.run(null, 1, new char[0], 0, -1, new char[0]);
                copyTest.run(null, 1, new char[0], 0, 0, new char[0]);
                copyTest.run(null, 1, new char[0], 0, 1, new char[0]);
                copyTest.run(null, 1, new char[0], 1, -1, new char[0]);
                copyTest.run(null, 1, new char[0], 1, 0, new char[0]);
                copyTest.run(null, 1, new char[0], 1, 1, new char[0]);
                copyTest.run(null, 1, new char[] { 'b' }, -1, -1, new char[] { 'b' });
                copyTest.run(null, 1, new char[] { 'b' }, -1, 0, new char[] { 'b' });
                copyTest.run(null, 1, new char[] { 'b' }, -1, 1, new char[] { 'b' });
                copyTest.run(null, 1, new char[] { 'b' }, 0, -1, new char[] { 'b' });
                copyTest.run(null, 1, new char[] { 'b' }, 0, 0, new char[] { 'b' });
                copyTest.run(null, 1, new char[] { 'b' }, 0, 1, new char[] { 'b' });
                copyTest.run(null, 1, new char[] { 'b' }, 1, -1, new char[] { 'b' });
                copyTest.run(null, 1, new char[] { 'b' }, 1, 0, new char[] { 'b' });
                copyTest.run(null, 1, new char[] { 'b' }, 1, 1, new char[] { 'b' });
                copyTest.run(new char[0], -1, null, -1, -1, null);
                copyTest.run(new char[0], -1, null, -1, 0, null);
                copyTest.run(new char[0], -1, null, -1, 1, null);
                copyTest.run(new char[0], -1, null, 0, -1, null);
                copyTest.run(new char[0], -1, null, 0, 0, null);
                copyTest.run(new char[0], -1, null, 0, 1, null);
                copyTest.run(new char[0], -1, null, 1, -1, null);
                copyTest.run(new char[0], -1, null, 1, 0, null);
                copyTest.run(new char[0], -1, null, 1, 1, null);
                copyTest.run(new char[0], -1, new char[0], -1, -1, new char[0]);
                copyTest.run(new char[0], -1, new char[0], -1, 0, new char[0]);
                copyTest.run(new char[0], -1, new char[0], -1, 1, new char[0]);
                copyTest.run(new char[0], -1, new char[0], 0, -1, new char[0]);
                copyTest.run(new char[0], -1, new char[0], 0, 0, new char[0]);
                copyTest.run(new char[0], -1, new char[0], 0, 1, new char[0]);
                copyTest.run(new char[0], -1, new char[0], 1, -1, new char[0]);
                copyTest.run(new char[0], -1, new char[0], 1, 0, new char[0]);
                copyTest.run(new char[0], -1, new char[0], 1, 1, new char[0]);
                copyTest.run(new char[0], -1, new char[] { 'b' }, -1, -1, new char[] { 'b' });
                copyTest.run(new char[0], -1, new char[] { 'b' }, -1, 0, new char[] { 'b' });
                copyTest.run(new char[0], -1, new char[] { 'b' }, -1, 1, new char[] { 'b' });
                copyTest.run(new char[0], -1, new char[] { 'b' }, 0, -1, new char[] { 'b' });
                copyTest.run(new char[0], -1, new char[] { 'b' }, 0, 0, new char[] { 'b' });
                copyTest.run(new char[0], -1, new char[] { 'b' }, 0, 1, new char[] { 'b' });
                copyTest.run(new char[0], -1, new char[] { 'b' }, 1, -1, new char[] { 'b' });
                copyTest.run(new char[0], -1, new char[] { 'b' }, 1, 0, new char[] { 'b' });
                copyTest.run(new char[0], -1, new char[] { 'b' }, 1, 1, new char[] { 'b' });
                copyTest.run(new char[0], 0, null, -1, -1, null);
                copyTest.run(new char[0], 0, null, -1, 0, null);
                copyTest.run(new char[0], 0, null, -1, 1, null);
                copyTest.run(new char[0], 0, null, 0, -1, null);
                copyTest.run(new char[0], 0, null, 0, 0, null);
                copyTest.run(new char[0], 0, null, 0, 1, null);
                copyTest.run(new char[0], 0, null, 1, -1, null);
                copyTest.run(new char[0], 0, null, 1, 0, null);
                copyTest.run(new char[0], 0, null, 1, 1, null);
                copyTest.run(new char[0], 0, new char[0], -1, -1, new char[0]);
                copyTest.run(new char[0], 0, new char[0], -1, 0, new char[0]);
                copyTest.run(new char[0], 0, new char[0], -1, 1, new char[0]);
                copyTest.run(new char[0], 0, new char[0], 0, -1, new char[0]);
                copyTest.run(new char[0], 0, new char[0], 0, 0, new char[0]);
                copyTest.run(new char[0], 0, new char[0], 0, 1, new char[0]);
                copyTest.run(new char[0], 0, new char[0], 1, -1, new char[0]);
                copyTest.run(new char[0], 0, new char[0], 1, 0, new char[0]);
                copyTest.run(new char[0], 0, new char[0], 1, 1, new char[0]);
                copyTest.run(new char[0], 0, new char[] { 'b' }, -1, -1, new char[] { 'b' });
                copyTest.run(new char[0], 0, new char[] { 'b' }, -1, 0, new char[] { 'b' });
                copyTest.run(new char[0], 0, new char[] { 'b' }, -1, 1, new char[] { 'b' });
                copyTest.run(new char[0], 0, new char[] { 'b' }, 0, -1, new char[] { 'b' });
                copyTest.run(new char[0], 0, new char[] { 'b' }, 0, 0, new char[] { 'b' });
                copyTest.run(new char[0], 0, new char[] { 'b' }, 0, 1, new char[] { 'b' });
                copyTest.run(new char[0], 0, new char[] { 'b' }, 1, -1, new char[] { 'b' });
                copyTest.run(new char[0], 0, new char[] { 'b' }, 1, 0, new char[] { 'b' });
                copyTest.run(new char[0], 0, new char[] { 'b' }, 1, 1, new char[] { 'b' });
                copyTest.run(new char[0], 1, null, -1, -1, null);
                copyTest.run(new char[0], 1, null, -1, 0, null);
                copyTest.run(new char[0], 1, null, -1, 1, null);
                copyTest.run(new char[0], 1, null, 0, -1, null);
                copyTest.run(new char[0], 1, null, 0, 0, null);
                copyTest.run(new char[0], 1, null, 0, 1, null);
                copyTest.run(new char[0], 1, null, 1, -1, null);
                copyTest.run(new char[0], 1, null, 1, 0, null);
                copyTest.run(new char[0], 1, null, 1, 1, null);
                copyTest.run(new char[0], 1, new char[0], -1, -1, new char[0]);
                copyTest.run(new char[0], 1, new char[0], -1, 0, new char[0]);
                copyTest.run(new char[0], 1, new char[0], -1, 1, new char[0]);
                copyTest.run(new char[0], 1, new char[0], 0, -1, new char[0]);
                copyTest.run(new char[0], 1, new char[0], 0, 0, new char[0]);
                copyTest.run(new char[0], 1, new char[0], 0, 1, new char[0]);
                copyTest.run(new char[0], 1, new char[0], 1, -1, new char[0]);
                copyTest.run(new char[0], 1, new char[0], 1, 0, new char[0]);
                copyTest.run(new char[0], 1, new char[0], 1, 1, new char[0]);
                copyTest.run(new char[0], 1, new char[] { 'b' }, -1, -1, new char[] { 'b' });
                copyTest.run(new char[0], 1, new char[] { 'b' }, -1, 0, new char[] { 'b' });
                copyTest.run(new char[0], 1, new char[] { 'b' }, -1, 1, new char[] { 'b' });
                copyTest.run(new char[0], 1, new char[] { 'b' }, 0, -1, new char[] { 'b' });
                copyTest.run(new char[0], 1, new char[] { 'b' }, 0, 0, new char[] { 'b' });
                copyTest.run(new char[0], 1, new char[] { 'b' }, 0, 1, new char[] { 'b' });
                copyTest.run(new char[0], 1, new char[] { 'b' }, 1, -1, new char[] { 'b' });
                copyTest.run(new char[0], 1, new char[] { 'b' }, 1, 0, new char[] { 'b' });
                copyTest.run(new char[0], 1, new char[] { 'b' }, 1, 1, new char[] { 'b' });
                copyTest.run(new char[] { 'a' }, -1, null, -1, -1, null);
                copyTest.run(new char[] { 'a' }, -1, null, -1, 0, null);
                copyTest.run(new char[] { 'a' }, -1, null, -1, 1, null);
                copyTest.run(new char[] { 'a' }, -1, null, 0, -1, null);
                copyTest.run(new char[] { 'a' }, -1, null, 0, 0, null);
                copyTest.run(new char[] { 'a' }, -1, null, 0, 1, null);
                copyTest.run(new char[] { 'a' }, -1, null, 1, -1, null);
                copyTest.run(new char[] { 'a' }, -1, null, 1, 0, null);
                copyTest.run(new char[] { 'a' }, -1, null, 1, 1, null);
                copyTest.run(new char[] { 'a' }, -1, new char[0], -1, -1, new char[0]);
                copyTest.run(new char[] { 'a' }, -1, new char[0], -1, 0, new char[0]);
                copyTest.run(new char[] { 'a' }, -1, new char[0], -1, 1, new char[0]);
                copyTest.run(new char[] { 'a' }, -1, new char[0], 0, -1, new char[0]);
                copyTest.run(new char[] { 'a' }, -1, new char[0], 0, 0, new char[0]);
                copyTest.run(new char[] { 'a' }, -1, new char[0], 0, 1, new char[0]);
                copyTest.run(new char[] { 'a' }, -1, new char[0], 1, -1, new char[0]);
                copyTest.run(new char[] { 'a' }, -1, new char[0], 1, 0, new char[0]);
                copyTest.run(new char[] { 'a' }, -1, new char[0], 1, 1, new char[0]);
                copyTest.run(new char[] { 'a' }, -1, new char[] { 'b' }, -1, -1, new char[] { 'b' });
                copyTest.run(new char[] { 'a' }, -1, new char[] { 'b' }, -1, 0, new char[] { 'b' });
                copyTest.run(new char[] { 'a' }, -1, new char[] { 'b' }, -1, 1, new char[] { 'b' });
                copyTest.run(new char[] { 'a' }, -1, new char[] { 'b' }, 0, -1, new char[] { 'b' });
                copyTest.run(new char[] { 'a' }, -1, new char[] { 'b' }, 0, 0, new char[] { 'b' });
                copyTest.run(new char[] { 'a' }, -1, new char[] { 'b' }, 0, 1, new char[] { 'b' });
                copyTest.run(new char[] { 'a' }, -1, new char[] { 'b' }, 1, -1, new char[] { 'b' });
                copyTest.run(new char[] { 'a' }, -1, new char[] { 'b' }, 1, 0, new char[] { 'b' });
                copyTest.run(new char[] { 'a' }, -1, new char[] { 'b' }, 1, 1, new char[] { 'b' });
                copyTest.run(new char[] { 'a' }, 0, null, -1, -1, null);
                copyTest.run(new char[] { 'a' }, 0, null, -1, 0, null);
                copyTest.run(new char[] { 'a' }, 0, null, -1, 1, null);
                copyTest.run(new char[] { 'a' }, 0, null, 0, -1, null);
                copyTest.run(new char[] { 'a' }, 0, null, 0, 0, null);
                copyTest.run(new char[] { 'a' }, 0, null, 0, 1, null);
                copyTest.run(new char[] { 'a' }, 0, null, 1, -1, null);
                copyTest.run(new char[] { 'a' }, 0, null, 1, 0, null);
                copyTest.run(new char[] { 'a' }, 0, null, 1, 1, null);
                copyTest.run(new char[] { 'a' }, 0, new char[0], -1, -1, new char[0]);
                copyTest.run(new char[] { 'a' }, 0, new char[0], -1, 0, new char[0]);
                copyTest.run(new char[] { 'a' }, 0, new char[0], -1, 1, new char[0]);
                copyTest.run(new char[] { 'a' }, 0, new char[0], 0, -1, new char[0]);
                copyTest.run(new char[] { 'a' }, 0, new char[0], 0, 0, new char[0]);
                copyTest.run(new char[] { 'a' }, 0, new char[0], 0, 1, new char[0]);
                copyTest.run(new char[] { 'a' }, 0, new char[0], 1, -1, new char[0]);
                copyTest.run(new char[] { 'a' }, 0, new char[0], 1, 0, new char[0]);
                copyTest.run(new char[] { 'a' }, 0, new char[0], 1, 1, new char[0]);
                copyTest.run(new char[] { 'a' }, 0, new char[] { 'b' }, -1, -1, new char[] { 'b' });
                copyTest.run(new char[] { 'a' }, 0, new char[] { 'b' }, -1, 0, new char[] { 'b' });
                copyTest.run(new char[] { 'a' }, 0, new char[] { 'b' }, -1, 1, new char[] { 'b' });
                copyTest.run(new char[] { 'a' }, 0, new char[] { 'b' }, 0, -1, new char[] { 'b' });
                copyTest.run(new char[] { 'a' }, 0, new char[] { 'b' }, 0, 0, new char[] { 'b' });
                copyTest.run(new char[] { 'a' }, 0, new char[] { 'b' }, 0, 1, new char[] { 'a' });
                copyTest.run(new char[] { 'a' }, 0, new char[] { 'b' }, 1, -1, new char[] { 'b' });
                copyTest.run(new char[] { 'a' }, 0, new char[] { 'b' }, 1, 0, new char[] { 'b' });
                copyTest.run(new char[] { 'a' }, 0, new char[] { 'b' }, 1, 1, new char[] { 'b' });
                copyTest.run(new char[] { 'a' }, 1, null, -1, -1, null);
                copyTest.run(new char[] { 'a' }, 1, null, -1, 0, null);
                copyTest.run(new char[] { 'a' }, 1, null, -1, 1, null);
                copyTest.run(new char[] { 'a' }, 1, null, 0, -1, null);
                copyTest.run(new char[] { 'a' }, 1, null, 0, 0, null);
                copyTest.run(new char[] { 'a' }, 1, null, 0, 1, null);
                copyTest.run(new char[] { 'a' }, 1, null, 1, -1, null);
                copyTest.run(new char[] { 'a' }, 1, null, 1, 0, null);
                copyTest.run(new char[] { 'a' }, 1, null, 1, 1, null);
                copyTest.run(new char[] { 'a' }, 1, new char[0], -1, -1, new char[0]);
                copyTest.run(new char[] { 'a' }, 1, new char[0], -1, 0, new char[0]);
                copyTest.run(new char[] { 'a' }, 1, new char[0], -1, 1, new char[0]);
                copyTest.run(new char[] { 'a' }, 1, new char[0], 0, -1, new char[0]);
                copyTest.run(new char[] { 'a' }, 1, new char[0], 0, 0, new char[0]);
                copyTest.run(new char[] { 'a' }, 1, new char[0], 0, 1, new char[0]);
                copyTest.run(new char[] { 'a' }, 1, new char[0], 1, -1, new char[0]);
                copyTest.run(new char[] { 'a' }, 1, new char[0], 1, 0, new char[0]);
                copyTest.run(new char[] { 'a' }, 1, new char[0], 1, 1, new char[0]);
                copyTest.run(new char[] { 'a' }, 1, new char[] { 'b' }, -1, -1, new char[] { 'b' });
                copyTest.run(new char[] { 'a' }, 1, new char[] { 'b' }, -1, 0, new char[] { 'b' });
                copyTest.run(new char[] { 'a' }, 1, new char[] { 'b' }, -1, 1, new char[] { 'b' });
                copyTest.run(new char[] { 'a' }, 1, new char[] { 'b' }, 0, -1, new char[] { 'b' });
                copyTest.run(new char[] { 'a' }, 1, new char[] { 'b' }, 0, 0, new char[] { 'b' });
                copyTest.run(new char[] { 'a' }, 1, new char[] { 'b' }, 0, 1, new char[] { 'b' });
                copyTest.run(new char[] { 'a' }, 1, new char[] { 'b' }, 1, -1, new char[] { 'b' });
                copyTest.run(new char[] { 'a' }, 1, new char[] { 'b' }, 1, 0, new char[] { 'b' });
                copyTest.run(new char[] { 'a' }, 1, new char[] { 'b' }, 1, 1, new char[] { 'b' });
            });

            runner.testGroup("contains(char[],char)", () ->
            {
                runner.test("with null array", (Test test) ->
                {
                    test.assertFalse(Array.contains((char[])null, 'a'));
                });

                runner.test("with empty array", (Test test) ->
                {
                    test.assertFalse(Array.contains(new char[0], 'a'));
                });

                runner.test("with not found value", (Test test) ->
                {
                    test.assertFalse(Array.contains(new char[] { 'a', 'b', 'c' }, 'd'));
                });

                runner.test("with found value", (Test test) ->
                {
                    test.assertTrue(Array.contains(new char[] { 'a', 'b' , 'c' }, 'c'));
                });
            });

            runner.testGroup("contains(int[],int)", () ->
            {
                runner.test("with null array", (Test test) ->
                {
                    test.assertFalse(Array.contains((int[])null, 7));
                });

                runner.test("with empty array", (Test test) ->
                {
                    test.assertFalse(Array.contains(new int[0], 7));
                });

                runner.test("with not found value", (Test test) ->
                {
                    test.assertFalse(Array.contains(new int[] { 4, 5, 6 }, 7));
                });

                runner.test("with found value", (Test test) ->
                {
                    test.assertTrue(Array.contains(new int[] { 5, 6, 7 }, 7));
                });
            });

            runner.testGroup("contains(long[],long)", () ->
            {
                runner.test("with null array", (Test test) ->
                {
                    test.assertFalse(Array.contains((long[])null, 7));
                });

                runner.test("with empty array", (Test test) ->
                {
                    test.assertFalse(Array.contains(new long[0], 7));
                });

                runner.test("with not found value", (Test test) ->
                {
                    test.assertFalse(Array.contains(new long[] { 4, 5, 6 }, 7));
                });

                runner.test("with found value", (Test test) ->
                {
                    test.assertTrue(Array.contains(new long[] { 5, 6, 7 }, 7));
                });
            });

            runner.testGroup("contains(Object[],Object)", () ->
            {
                runner.test("with null array", (Test test) ->
                {
                    test.assertFalse(Array.contains((String[])null, "hello"));
                });

                runner.test("with empty array", (Test test) ->
                {
                    test.assertFalse(Array.contains(new String[0], "hello"));
                });

                runner.test("with not found value", (Test test) ->
                {
                    test.assertFalse(Array.contains(new String[] { "hello", "there", "buddy" }, "oops"));
                });

                runner.test("with found value", (Test test) ->
                {
                    test.assertTrue(Array.contains(new String[] { "hello", "there", "buddy" }, "hello"));
                });
            });

            runner.testGroup("mergeBytes(Iterable<byte[]>)", () ->
            {
                final Action2<byte[][],byte[]> mergeTest = (byte[][] bytes, byte[] expected) ->
                {
                    runner.test("with " + (bytes == null ? "null" : Iterable.create(bytes).map(Array::createByte)), (Test test) ->
                    {
                        final Iterable<Byte> expectedArray = Array.createByte(expected);
                        final byte[] mergedBytes = Array.mergeBytes(Iterable.create(bytes));
                        final Iterable<Byte> actualArray = Array.createByte(mergedBytes);

                        test.assertEqual(expectedArray, actualArray);
                    });
                };

                mergeTest.run(new byte[][] { }, new byte[0]);
                mergeTest.run(new byte[][] { new byte[0] }, new byte[0]);
                mergeTest.run(new byte[][] { new byte[] { 1, 2, 3 } }, new byte[] { 1, 2, 3 });
                mergeTest.run(new byte[][] { new byte[] { 1, 2, 3 }, new byte[] { 4 } }, new byte[] { 1, 2, 3, 4 });
                mergeTest.run(new byte[][] { new byte[] { 1, 2, 3 }, new byte[] { 4 }, new byte[] { 5, 6, 7, 8} }, new byte[] { 1, 2, 3, 4, 5, 6, 7, 8 });
            });

            runner.testGroup("toString(byte[])", () ->
            {
                final Action2<byte[],String> toStringTest = (byte[] array, String expected) ->
                {
                    runner.test("with " + Array.toString(array), (Test test) ->
                    {
                        test.assertEqual(expected, Array.toString(array));
                    });
                };

                toStringTest.run(null, "null");
                toStringTest.run(new byte[0], "[]");
                toStringTest.run(new byte[] { 1 }, "[1]");
                toStringTest.run(new byte[] { 2, 3 }, "[2,3]");
                toStringTest.run(new byte[] { 4, 5, 6 }, "[4,5,6]");
            });

            runner.testGroup("toString(char[])", () ->
            {
                final Action2<char[],String> toStringTest = (char[] array, String expected) ->
                {
                    runner.test("with " + Array.toString(array), (Test test) ->
                    {
                        test.assertEqual(expected, Array.toString(array));
                    });
                };

                toStringTest.run(null, "null");
                toStringTest.run(new char[0], "[]");
                toStringTest.run(new char[] { 'a' }, "['a']");
                toStringTest.run(new char[] { 'b', 'c' }, "['b','c']");
                toStringTest.run(new char[] { 'd', 'e', 'f' }, "['d','e','f']");
            });

            runner.testGroup("shiftLeft(byte[],int,int)", () ->
            {
                runner.test("with null values", (Test test) ->
                {
                    test.assertThrows(() -> Array.shiftLeft((byte[])null, 0, 1),
                        new PreConditionFailure("values cannot be null."));
                });

                runner.test("with empty values", (Test test) ->
                {
                    test.assertThrows(() -> Array.shiftLeft(new byte[0], 0, 1),
                        new PreConditionFailure("values cannot be empty."));
                });

                runner.test("with negative indexToRemove", (Test test) ->
                {
                    test.assertThrows(() -> Array.shiftLeft(new byte[] { 0, 1, 2 }, -1, 2),
                        new PreConditionFailure("indexToRemove (-1) must be between 0 and 1."));
                });

                runner.test("with indexToRemove equal to values.length - 1", (Test test) ->
                {
                    test.assertThrows(() -> Array.shiftLeft(new byte[] { 0, 1, 2 }, 2, 2),
                        new PreConditionFailure("indexToRemove (2) must be between 0 and 1."));
                });

                runner.test("with indexToRemove equal to values.length", (Test test) ->
                {
                    test.assertThrows(() -> Array.shiftLeft(new byte[] { 0, 1, 2 }, 3, 2),
                        new PreConditionFailure("indexToRemove (3) must be between 0 and 1."));
                });

                runner.test("with indexToRemove greater than values.length", (Test test) ->
                {
                    test.assertThrows(() -> Array.shiftLeft(new byte[] { 0, 1, 2 }, 4, 2),
                        new PreConditionFailure("indexToRemove (4) must be between 0 and 1."));
                });

                runner.test("with negative valuesToShift", (Test test) ->
                {
                    test.assertThrows(() -> Array.shiftLeft(new byte[] { 0, 1, 2 }, 1, -1),
                        new PreConditionFailure("valuesToShift (-1) must be between 0 and 1."));
                });

                runner.test("with negative valuesToShift", (Test test) ->
                {
                    test.assertThrows(() -> Array.shiftLeft(new byte[] { 0, 1, 2 }, 1, -1),
                        new PreConditionFailure("valuesToShift (-1) must be between 0 and 1."));
                });

                runner.test("with valuesToShift equal to count - indexToRemove", (Test test) ->
                {
                    test.assertThrows(() -> Array.shiftLeft(new byte[] { 0, 1, 2 }, 1, 2),
                        new PreConditionFailure("valuesToShift (2) must be between 0 and 1."));
                });

                runner.test("with valuesToShift greater than count - indexToRemove", (Test test) ->
                {
                    test.assertThrows(() -> Array.shiftLeft(new byte[] { 0, 1, 2 }, 1, 3),
                        new PreConditionFailure("valuesToShift (3) must be between 0 and 1."));
                });

                runner.test("with 0 indexToRemove and 0 valuesToShift", (Test test) ->
                {
                    final byte[] values = new byte[] { 0, 1, 2 };
                    Array.shiftLeft(values, 0, 0);
                    test.assertEqual(new byte[] { 0, 1, 2 }, values);
                });

                runner.test("with 1 indexToRemove and 0 valuesToShift", (Test test) ->
                {
                    final byte[] values = new byte[] { 0, 1, 2 };
                    Array.shiftLeft(values, 1, 0);
                    test.assertEqual(new byte[] { 0, 1, 2 }, values);
                });

                runner.test("with 0 indexToRemove and 1 valuesToShift", (Test test) ->
                {
                    final byte[] values = new byte[] { 0, 1, 2 };
                    Array.shiftLeft(values, 0, 1);
                    test.assertEqual(new byte[] { 1, 1, 2 }, values);
                });

                runner.test("with 1 indexToRemove and 1 valuesToShift", (Test test) ->
                {
                    final byte[] values = new byte[] { 0, 1, 2 };
                    Array.shiftLeft(values, 1, 1);
                    test.assertEqual(new byte[] { 0, 2, 2 }, values);
                });

                runner.test("with 0 indexToRemove and 2 valuesToShift", (Test test) ->
                {
                    final byte[] values = new byte[] { 0, 1, 2 };
                    Array.shiftLeft(values, 0, 2);
                    test.assertEqual(new byte[] { 1, 2, 2 }, values);
                });
            });

            runner.testGroup("shiftRight(byte[],int,int)", () ->
            {
                runner.test("with null values", (Test test) ->
                {
                    test.assertThrows(() -> Array.shiftRight((byte[])null, 0, 1),
                        new PreConditionFailure("values cannot be null."));
                });

                runner.test("with empty values", (Test test) ->
                {
                    test.assertThrows(() -> Array.shiftRight(new byte[0], 0, 1),
                        new PreConditionFailure("values cannot be empty."));
                });

                runner.test("with negative indexToOpen", (Test test) ->
                {
                    test.assertThrows(() -> Array.shiftRight(new byte[] { 0, 1, 2 }, -1, 2),
                        new PreConditionFailure("indexToOpen (-1) must be between 0 and 1."));
                });

                runner.test("with indexToOpen equal to values.length - 1", (Test test) ->
                {
                    test.assertThrows(() -> Array.shiftRight(new byte[] { 0, 1, 2 }, 2, 2),
                        new PreConditionFailure("indexToOpen (2) must be between 0 and 1."));
                });

                runner.test("with indexToOpen equal to values.length", (Test test) ->
                {
                    test.assertThrows(() -> Array.shiftRight(new byte[] { 0, 1, 2 }, 3, 2),
                        new PreConditionFailure("indexToOpen (3) must be between 0 and 1."));
                });

                runner.test("with indexToOpen greater than values.length", (Test test) ->
                {
                    test.assertThrows(() -> Array.shiftRight(new byte[] { 0, 1, 2 }, 4, 2),
                        new PreConditionFailure("indexToOpen (4) must be between 0 and 1."));
                });

                runner.test("with negative valuesToShift", (Test test) ->
                {
                    test.assertThrows(() -> Array.shiftRight(new byte[] { 0, 1, 2 }, 1, -1),
                        new PreConditionFailure("valuesToShift (-1) must be between 0 and 1."));
                });

                runner.test("with negative valuesToShift", (Test test) ->
                {
                    test.assertThrows(() -> Array.shiftRight(new byte[] { 0, 1, 2 }, 1, -1),
                        new PreConditionFailure("valuesToShift (-1) must be between 0 and 1."));
                });

                runner.test("with valuesToShift equal to count - indexToOpen", (Test test) ->
                {
                    test.assertThrows(() -> Array.shiftRight(new byte[] { 0, 1, 2 }, 1, 2),
                        new PreConditionFailure("valuesToShift (2) must be between 0 and 1."));
                });

                runner.test("with valuesToShift greater than count - indexToOpen", (Test test) ->
                {
                    test.assertThrows(() -> Array.shiftRight(new byte[] { 0, 1, 2 }, 1, 3),
                        new PreConditionFailure("valuesToShift (3) must be between 0 and 1."));
                });

                runner.test("with 0 indexToOpen and 0 valuesToShift", (Test test) ->
                {
                    final byte[] values = new byte[] { 0, 1, 2 };
                    Array.shiftRight(values, 0, 0);
                    test.assertEqual(new byte[] { 0, 1, 2 }, values);
                });

                runner.test("with 1 indexToOpen and 0 valuesToShift", (Test test) ->
                {
                    final byte[] values = new byte[] { 0, 1, 2 };
                    Array.shiftRight(values, 1, 0);
                    test.assertEqual(new byte[] { 0, 1, 2 }, values);
                });

                runner.test("with 0 indexToOpen and 1 valuesToShift", (Test test) ->
                {
                    final byte[] values = new byte[] { 0, 1, 2 };
                    Array.shiftRight(values, 0, 1);
                    test.assertEqual(new byte[] { 0, 0, 2 }, values);
                });

                runner.test("with 1 indexToOpen and 1 valuesToShift", (Test test) ->
                {
                    final byte[] values = new byte[] { 0, 1, 2 };
                    Array.shiftRight(values, 1, 1);
                    test.assertEqual(new byte[] { 0, 1, 1 }, values);
                });

                runner.test("with 0 indexToOpen and 2 valuesToShift", (Test test) ->
                {
                    final byte[] values = new byte[] { 0, 1, 2 };
                    Array.shiftRight(values, 0, 2);
                    test.assertEqual(new byte[] { 0, 0, 1 }, values);
                });
            });
        });
    }
}
