package qub;

public interface ArrayTests
{
    static void test(TestRunner runner)
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
                    test.assertThrows(iterator::getCurrent, new PreConditionFailure("this.hasCurrent() cannot be false."));

                    test.assertFalse(iterator.next());
                    test.assertTrue(iterator.hasStarted());
                    test.assertFalse(iterator.hasCurrent());
                    test.assertThrows(iterator::getCurrent, new PreConditionFailure("this.hasCurrent() cannot be false."));
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
                    test.assertThrows(iterator::getCurrent, new PreConditionFailure("this.hasCurrent() cannot be false."));

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
                    test.assertThrows(iterator::getCurrent, new PreConditionFailure("this.hasCurrent() cannot be false."));
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
                    test.assertEqual(new byte[0], Array.toByteArray(Iterator.create()));
                });

                runner.test("with non-empty", (Test test) ->
                {
                    test.assertEqual(new byte[] { 0, 1, 2 }, Array.toByteArray(Iterator.create((byte)0, (byte)1, (byte)2)));
                });

                runner.test("with null value", (Test test) ->
                {
                    test.assertThrows(() -> Array.toByteArray(Iterator.create((byte)0, (byte)1, null)),
                        new PreConditionFailure("The 2nd element cannot be null."));
                });
            });

            runner.testGroup("toByteArray(Iterable<Byte>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> Array.toByteArray((Iterable<Byte>)null),
                        new PreConditionFailure("values cannot be null."));
                });

                runner.test("with empty", (Test test) ->
                {
                    test.assertEqual(new byte[0], Array.toByteArray(Iterable.create()));
                });

                runner.test("with non-empty Array", (Test test) ->
                {
                    test.assertEqual(new byte[] { 0, 1, 2 }, Array.toByteArray(Array.create((byte)0, (byte)1, (byte)2)));
                });

                runner.test("with non-empty ByteArray", (Test test) ->
                {
                    test.assertEqual(new byte[] { 0, 1, 2 }, Array.toByteArray(ByteArray.create(0, 1, 2)));
                });

                runner.test("with non-empty ByteList", (Test test) ->
                {
                    test.assertEqual(new byte[] { 0, 1, 2 }, Array.toByteArray(ByteList.createFromBytes((byte)0, (byte)1, (byte)2)));
                });

                runner.test("with null value", (Test test) ->
                {
                    test.assertThrows(() -> Array.toByteArray(Iterable.create((byte)0, (byte)1, null)),
                        new PreConditionFailure("The 2nd element cannot be null."));
                });
            });

            runner.testGroup("toByteArray(int...)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> Array.toByteArray((int[])null),
                        new PreConditionFailure("values cannot be null."));
                });

                runner.test("with integer lower than Bytes.minimum", (Test test) ->
                {
                    test.assertThrows(() -> Array.toByteArray(-129),
                        new PreConditionFailure("The 0th value (-129) must be between -128 and 127."));
                });

                runner.test("with integer greater than Bytes.maximum", (Test test) ->
                {
                    test.assertThrows(() -> Array.toByteArray(128),
                        new PreConditionFailure("The 0th value (128) must be between -128 and 127."));
                });

                runner.test("with no arguments", (Test test) ->
                {
                    test.assertEqual(new byte[0], Array.toByteArray());
                });

                runner.test("with empty", (Test test) ->
                {
                    test.assertEqual(new byte[0], Array.toByteArray(new int[0]));
                });

                runner.test("with non-empty", (Test test) ->
                {
                    test.assertEqual(new byte[] { 1, 2, 3 }, Array.toByteArray(1, 2, 3));
                });
            });

            runner.testGroup("toCharArray(Iterator<Character>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> Array.toCharArray((Iterator<Character>)null),
                        new PreConditionFailure("values cannot be null."));
                });

                runner.test("with empty", (Test test) ->
                {
                    test.assertEqual(new char[0], Array.toCharArray(Iterator.create()).await());
                });

                runner.test("with non-empty", (Test test) ->
                {
                    test.assertEqual(new char[] { 'a', 'b', 'c' }, Array.toCharArray(Iterator.create('a', 'b', 'c')).await());
                });

                runner.test("with null value", (Test test) ->
                {
                    test.assertThrows(() -> Array.toCharArray(Iterator.create('a', 'b', null)).await(),
                        new NullPointerException("The 2 element cannot be null."));
                });
            });

            runner.testGroup("toCharArray(Iterable<Character>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> Array.toCharArray((Iterable<Character>)null),
                        new PreConditionFailure("values cannot be null."));
                });

                runner.test("with empty", (Test test) ->
                {
                    test.assertEqual(new char[0], Array.toCharArray(Iterable.create()).await());
                });

                runner.test("with non-empty Array", (Test test) ->
                {
                    test.assertEqual(new char[] { 'a', 'b', 'c' }, Array.toCharArray(Iterable.create('a', 'b', 'c')).await());
                });

                runner.test("with non-empty CharArray", (Test test) ->
                {
                    test.assertEqual(new char[] { 'a', 'b', 'c' }, Array.toCharArray(CharacterArray.create('a', 'b', 'c')).await());
                });

                runner.test("with non-empty CharList", (Test test) ->
                {
                    test.assertEqual(new char[] { 'a', 'b', 'c' }, Array.toCharArray(CharacterList.create('a', 'b', 'c')).await());
                });

                runner.test("with null value", (Test test) ->
                {
                    test.assertThrows(() -> Array.toCharArray(Iterable.create('a', 'b', null)).await(),
                        new NullPointerException("The 2 element cannot be null."));
                });
            });

            runner.testGroup("toIntArray(Iterator<Integer>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> Array.toIntArray((Iterator<Integer>)null),
                        new PreConditionFailure("values cannot be null."));
                });

                runner.test("with empty", (Test test) ->
                {
                    test.assertEqual(new int[0], Array.toIntArray(Iterator.create()).await());
                });

                runner.test("with non-empty", (Test test) ->
                {
                    test.assertEqual(new int[] { 0, 1, 2 }, Array.toIntArray(Iterator.create(0, 1, 2)).await());
                });

                runner.test("with null value", (Test test) ->
                {
                    test.assertThrows(() -> Array.toIntArray(Iterator.create(0, 1, null)).await(),
                        new NullPointerException("The 2 element cannot be null."));
                });
            });

            runner.testGroup("toIntArray(Iterable<Integer>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> Array.toIntArray((Iterable<Integer>)null),
                        new PreConditionFailure("values cannot be null."));
                });

                runner.test("with empty", (Test test) ->
                {
                    test.assertEqual(new int[0], Array.toIntArray(Iterable.create()).await());
                });

                runner.test("with non-empty Array", (Test test) ->
                {
                    test.assertEqual(new int[] { 0, 1, 2 }, Array.toIntArray(Array.create(0, 1, 2)).await());
                });

                runner.test("with non-empty IntegerArray", (Test test) ->
                {
                    test.assertEqual(new int[] { 0, 1, 2 }, Array.toIntArray(IntegerArray.create(0, 1, 2)).await());
                });

                runner.test("with null value", (Test test) ->
                {
                    test.assertThrows(() -> Array.toIntArray(Iterable.create(0, 1, null)).await(),
                        new NullPointerException("The 2 element cannot be null."));
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



            runner.testGroup("copy(int[],int,int[],int,int)", () ->
            {
                final Action6<int[],Integer,int[],Integer,Integer,Throwable> copyErrorTest = (int[] copyFrom, Integer copyFromStartIndex, int[] copyTo, Integer copyToStartIndex, Integer length, Throwable expected) ->
                {
                    runner.test("copy " + Array.toString(copyFrom) + " at index " + copyFromStartIndex + " to " + Array.toString(copyTo) + " at index " + copyToStartIndex + " for length " + length, (Test test) ->
                    {
                        final int[] copyFromClone = Array.clone(copyFrom);
                        final int[] copyToClone = Array.clone(copyTo);

                        test.assertThrows(() -> Array.copy(copyFrom, copyFromStartIndex, copyTo, copyToStartIndex, length), expected);

                        test.assertEqual(copyFromClone, copyFrom);
                        test.assertEqual(copyToClone, copyTo);
                    });
                };

                copyErrorTest.run(null, 0, new int[0], 0, 0, new PreConditionFailure("copyFrom cannot be null."));
                copyErrorTest.run(new int[0], -1, new int[0], 0, 0, new PreConditionFailure("copyFromStartIndex (-1) must be equal to 0."));
                copyErrorTest.run(new int[0], 1, new int[0], 0, 0, new PreConditionFailure("copyFromStartIndex (1) must be equal to 0."));
                copyErrorTest.run(new int[0], 0, null, 0, 0, new PreConditionFailure("copyTo cannot be null."));
                copyErrorTest.run(new int[0], 0, new int[0], -1, 0, new PreConditionFailure("copyToStartIndex (-1) must be equal to 0."));
                copyErrorTest.run(new int[0], 0, new int[0], 1, 0, new PreConditionFailure("copyToStartIndex (1) must be equal to 0."));
                copyErrorTest.run(new int[0], 0, new int[0], 0, -1, new PreConditionFailure("length (-1) must be equal to 0."));
                copyErrorTest.run(new int[0], 0, new int[0], 0, 1, new PreConditionFailure("length (1) must be equal to 0."));

                final Action6<int[],Integer,int[],Integer,Integer,int[]> copyTest = (int[] copyFrom, Integer copyFromStartIndex, int[] copyTo, Integer copyToStartIndex, Integer length, int[] expectedBytes) ->
                {
                    runner.test("copy " + Array.toString(copyFrom) + " at index " + copyFromStartIndex + " to " + Array.toString(copyTo) + " at index " + copyToStartIndex + " for length " + length, (Test test) ->
                    {
                        final int[] copyFromClone = Array.clone(copyFrom);

                        Array.copy(copyFrom, copyFromStartIndex, copyTo, copyToStartIndex, length);

                        test.assertEqual(copyFromClone, copyFrom);
                        test.assertEqual(expectedBytes, copyTo);
                    });
                };

                copyTest.run(new int[0], 0, new int[0], 0, 0, new int[0]);
                copyTest.run(new int[0], 0, new int[] { 2 }, 0, 0, new int[] { 2 });
                copyTest.run(new int[] { 1 }, 0, new int[0], 0, 0, new int[0]);
                copyTest.run(new int[] { 1 }, 0, new int[] { 2 }, 0, 0, new int[] { 2 });
                copyTest.run(new int[] { 1 }, 0, new int[] { 2 }, 0, 1, new int[] { 1 });
                copyTest.run(new int[] { 0, 1, 2, 3, 4 }, 1, new int[] { 5, 6, 7, 8, 9, 10, 11 }, 2, 3, new int[] { 5, 6, 1, 2, 3, 10, 11 });
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
                    runner.test("with " + (bytes == null ? "null" : Iterable.create(bytes).map(Iterable::create)), (Test test) ->
                    {
                        final Iterable<Byte> expectedArray = ByteArray.create(expected);
                        final byte[] mergedBytes = Array.mergeBytes(Iterable.create(bytes));
                        final Iterable<Byte> actualArray = ByteArray.create(mergedBytes);

                        test.assertEqual(expectedArray, actualArray);
                    });
                };

                mergeTest.run(new byte[][] { }, new byte[0]);
                mergeTest.run(new byte[][] { new byte[0] }, new byte[0]);
                mergeTest.run(new byte[][] { new byte[] { 1, 2, 3 } }, new byte[] { 1, 2, 3 });
                mergeTest.run(new byte[][] { new byte[] { 1, 2, 3 }, new byte[] { 4 } }, new byte[] { 1, 2, 3, 4 });
                mergeTest.run(new byte[][] { null, new byte[] { 1, 2, 3 }, null, new byte[] { 4 }, null }, new byte[] { 1, 2, 3, 4 });
                mergeTest.run(new byte[][] { new byte[] { 1, 2, 3 }, new byte[] { 4 }, new byte[] { 5, 6, 7, 8} }, new byte[] { 1, 2, 3, 4, 5, 6, 7, 8 });
            });

            runner.testGroup("mergeCharacters(Iterable<char[]>)", () ->
            {
                final Action2<char[][],char[]> mergeTest = (char[][] characters, char[] expected) ->
                {
                    runner.test("with " + (characters == null ? "null" : Iterable.create(characters).map(Iterable::create)), (Test test) ->
                    {
                        final Iterable<Character> expectedArray = CharacterArray.create(expected);
                        final char[] mergedCharacters = Array.mergeCharacters(Iterable.create(characters));
                        final Iterable<Character> actualArray = CharacterArray.create(mergedCharacters);

                        test.assertEqual(expectedArray, actualArray);
                    });
                };

                mergeTest.run(new char[][] { }, new char[0]);
                mergeTest.run(new char[][] { new char[0] }, new char[0]);
                mergeTest.run(new char[][] { new char[] { 'a', 'b', 'c' } }, new char[] { 'a', 'b', 'c' });
                mergeTest.run(new char[][] { new char[] { 'a', 'b', 'c' }, new char[] { 'd' } }, new char[] { 'a', 'b', 'c', 'd' });
                mergeTest.run(new char[][] { null, new char[] { 'a', 'b', 'c' }, null, new char[] { 'd' }, null }, new char[] { 'a', 'b', 'c', 'd' });
                mergeTest.run(new char[][] { new char[] { 'a', 'b', 'c' }, new char[] { 'd' }, new char[] { 'e', 'f', 'g', 'h' } }, new char[] { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h' });
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

            runner.testGroup("toString(char[],Function1<Character,String>)", () ->
            {
                runner.test("with null transform", (Test test) ->
                {
                    test.assertThrows(() -> Array.toString(new char[] { 'a' }, null),
                        new PreConditionFailure("characterTransform cannot be null."));
                });

                final Action2<char[],String> toStringTest = (char[] array, String expected) ->
                {
                    runner.test("with " + Array.toString(array), (Test test) ->
                    {
                        test.assertEqual(expected, Array.toString(array, (Character c) -> Characters.toString(Character.toUpperCase(c))));
                    });
                };

                toStringTest.run(null, "null");
                toStringTest.run(new char[0], "[]");
                toStringTest.run(new char[] { 'a' }, "[A]");
                toStringTest.run(new char[] { 'A' }, "[A]");
                toStringTest.run(new char[] { 'b', 'c' }, "[B,C]");
                toStringTest.run(new char[] { 'd', 'e', 'f' }, "[D,E,F]");
            });

            runner.testGroup("toString(short[])", () ->
            {
                final Action2<short[],String> toStringTest = (short[] array, String expected) ->
                {
                    runner.test("with " + Array.toString(array), (Test test) ->
                    {
                        test.assertEqual(expected, Array.toString(array));
                    });
                };

                toStringTest.run(null, "null");
                toStringTest.run(new short[0], "[]");
                toStringTest.run(new short[] { 1 }, "[1]");
                toStringTest.run(new short[] { 2, 3 }, "[2,3]");
                toStringTest.run(new short[] { 4, 5, 6 }, "[4,5,6]");
            });

            runner.testGroup("toString(short[],Function1<Short,String>)", () ->
            {
                runner.test("with null transform", (Test test) ->
                {
                    test.assertThrows(() -> Array.toString(new short[] { 1 }, null),
                        new PreConditionFailure("transform cannot be null."));
                });

                final Action2<short[],String> toStringTest = (short[] array, String expected) ->
                {
                    runner.test("with " + Array.toString(array), (Test test) ->
                    {
                        test.assertEqual(expected, Array.toString(array, (Short value) -> Integers.toString(value + 1)));
                    });
                };

                toStringTest.run(null, "null");
                toStringTest.run(new short[0], "[]");
                toStringTest.run(new short[] { 1 }, "[2]");
                toStringTest.run(new short[] { 2, 3 }, "[3,4]");
                toStringTest.run(new short[] { 4, 5, 6 }, "[5,6,7]");
            });

            runner.testGroup("toString(int[])", () ->
            {
                final Action2<int[],String> toStringTest = (int[] array, String expected) ->
                {
                    runner.test("with " + Array.toString(array), (Test test) ->
                    {
                        test.assertEqual(expected, Array.toString(array));
                    });
                };

                toStringTest.run(null, "null");
                toStringTest.run(new int[0], "[]");
                toStringTest.run(new int[] { 1 }, "[1]");
                toStringTest.run(new int[] { 2, 3 }, "[2,3]");
                toStringTest.run(new int[] { 4, 5, 6 }, "[4,5,6]");
            });

            runner.testGroup("toString(int[],Function1<Integer,String>)", () ->
            {
                runner.test("with null transform", (Test test) ->
                {
                    test.assertThrows(() -> Array.toString(new int[] { 1 }, null),
                        new PreConditionFailure("transform cannot be null."));
                });

                final Action2<int[],String> toStringTest = (int[] array, String expected) ->
                {
                    runner.test("with " + Array.toString(array), (Test test) ->
                    {
                        test.assertEqual(expected, Array.toString(array, (Integer value) -> Integers.toString(value + 1)));
                    });
                };

                toStringTest.run(null, "null");
                toStringTest.run(new int[0], "[]");
                toStringTest.run(new int[] { 1 }, "[2]");
                toStringTest.run(new int[] { 2, 3 }, "[3,4]");
                toStringTest.run(new int[] { 4, 5, 6 }, "[5,6,7]");
            });

            runner.testGroup("toString(long[])", () ->
            {
                final Action2<long[],String> toStringTest = (long[] array, String expected) ->
                {
                    runner.test("with " + Array.toString(array), (Test test) ->
                    {
                        test.assertEqual(expected, Array.toString(array));
                    });
                };

                toStringTest.run(null, "null");
                toStringTest.run(new long[0], "[]");
                toStringTest.run(new long[] { 1 }, "[1]");
                toStringTest.run(new long[] { 2, 3 }, "[2,3]");
                toStringTest.run(new long[] { 4, 5, 6 }, "[4,5,6]");
            });

            runner.testGroup("toString(long[],Function1<Long,String>)", () ->
            {
                runner.test("with null transform", (Test test) ->
                {
                    test.assertThrows(() -> Array.toString(new long[] { 1 }, null),
                        new PreConditionFailure("transform cannot be null."));
                });

                final Action2<long[],String> toStringTest = (long[] array, String expected) ->
                {
                    runner.test("with " + Array.toString(array), (Test test) ->
                    {
                        test.assertEqual(expected, Array.toString(array, (Long value) -> Longs.toString(value + 1)));
                    });
                };

                toStringTest.run(null, "null");
                toStringTest.run(new long[0], "[]");
                toStringTest.run(new long[] { 1 }, "[2]");
                toStringTest.run(new long[] { 2, 3 }, "[3,4]");
                toStringTest.run(new long[] { 4, 5, 6 }, "[5,6,7]");
            });

            runner.testGroup("toString(float[])", () ->
            {
                final Action2<float[],String> toStringTest = (float[] array, String expected) ->
                {
                    runner.test("with " + Array.toString(array), (Test test) ->
                    {
                        test.assertEqual(expected, Array.toString(array));
                    });
                };

                toStringTest.run(null, "null");
                toStringTest.run(new float[0], "[]");
                toStringTest.run(new float[] { 1 }, "[1.0]");
                toStringTest.run(new float[] { 2, 3 }, "[2.0,3.0]");
                toStringTest.run(new float[] { 4, 5, 6 }, "[4.0,5.0,6.0]");
            });

            runner.testGroup("toString(float[],Function1<Float,String>)", () ->
            {
                runner.test("with null transform", (Test test) ->
                {
                    test.assertThrows(() -> Array.toString(new float[] { 1 }, null),
                        new PreConditionFailure("transform cannot be null."));
                });

                final Action2<float[],String> toStringTest = (float[] array, String expected) ->
                {
                    runner.test("with " + Array.toString(array), (Test test) ->
                    {
                        test.assertEqual(expected, Array.toString(array, (Float value) -> Floats.toString(value + 1)));
                    });
                };

                toStringTest.run(null, "null");
                toStringTest.run(new float[0], "[]");
                toStringTest.run(new float[] { 1 }, "[2.0]");
                toStringTest.run(new float[] { 2, 3 }, "[3.0,4.0]");
                toStringTest.run(new float[] { 4, 5, 6 }, "[5.0,6.0,7.0]");
            });

            runner.testGroup("toString(double[])", () ->
            {
                final Action2<double[],String> toStringTest = (double[] array, String expected) ->
                {
                    runner.test("with " + Array.toString(array), (Test test) ->
                    {
                        test.assertEqual(expected, Array.toString(array));
                    });
                };

                toStringTest.run(null, "null");
                toStringTest.run(new double[0], "[]");
                toStringTest.run(new double[] { 1 }, "[1.0]");
                toStringTest.run(new double[] { 2, 3 }, "[2.0,3.0]");
                toStringTest.run(new double[] { 4, 5, 6 }, "[4.0,5.0,6.0]");
            });

            runner.testGroup("toString(double[],Function1<Double,String>)", () ->
            {
                runner.test("with null transform", (Test test) ->
                {
                    test.assertThrows(() -> Array.toString(new double[] { 1 }, null),
                        new PreConditionFailure("transform cannot be null."));
                });

                final Action2<double[],String> toStringTest = (double[] array, String expected) ->
                {
                    runner.test("with " + Array.toString(array), (Test test) ->
                    {
                        test.assertEqual(expected, Array.toString(array, (Double value) -> Doubles.toString(value + 1)));
                    });
                };

                toStringTest.run(null, "null");
                toStringTest.run(new double[0], "[]");
                toStringTest.run(new double[] { 1 }, "[2.0]");
                toStringTest.run(new double[] { 2, 3 }, "[3.0,4.0]");
                toStringTest.run(new double[] { 4, 5, 6 }, "[5.0,6.0,7.0]");
            });

            runner.testGroup("toString(T[])", () ->
            {
                final Action2<String[],String> toStringTest = (String[] array, String expected) ->
                {
                    runner.test("with " + Array.toString(array), (Test test) ->
                    {
                        test.assertEqual(expected, Array.toString(array));
                    });
                };

                toStringTest.run(null, "null");
                toStringTest.run(new String[0], "[]");
                toStringTest.run(new String[] { "apples" }, "[apples]");
                toStringTest.run(new String[] { "apples", "oranges" }, "[apples,oranges]");
                toStringTest.run(new String[] { "apples", "oranges", "bananas" }, "[apples,oranges,bananas]");
            });

            runner.testGroup("toString(T[],Function1<T,String>)", () ->
            {
                runner.test("with null transform", (Test test) ->
                {
                    test.assertThrows(() -> Array.toString(new String[] { "apples" }, null),
                        new PreConditionFailure("transform cannot be null."));
                });

                final Action2<String[],String> toStringTest = (String[] array, String expected) ->
                {
                    runner.test("with " + Array.toString(array), (Test test) ->
                    {
                        test.assertEqual(expected, Array.toString(array, (String value) -> Characters.toString(value.charAt(0))));
                    });
                };

                toStringTest.run(null, "null");
                toStringTest.run(new String[0], "[]");
                toStringTest.run(new String[] { "apples" }, "[a]");
                toStringTest.run(new String[] { "apples", "oranges" }, "[a,o]");
                toStringTest.run(new String[] { "apples", "oranges", "bananas" }, "[a,o,b]");
            });

            runner.testGroup("indexOf(char[],char)", () ->
            {
                runner.test("with null characters", (Test test) ->
                {
                    test.assertThrows(() -> Array.indexOf(null, 'a'),
                        new PreConditionFailure("characters cannot be null."));
                });

                runner.test("with empty characters", (Test test) ->
                {
                    test.assertEqual(-1, Array.indexOf(new char[0], 'a'));
                });

                runner.test("with non-empty characters and not-found character", (Test test) ->
                {
                    test.assertEqual(-1, Array.indexOf(new char[] { 'x', 'y', 'z' }, 'a'));
                });

                runner.test("with non-empty characters and found character", (Test test) ->
                {
                    test.assertEqual(2, Array.indexOf(new char[] { 'x', 'y', 'z' }, 'z'));
                });
            });

            runner.testGroup("indexOf(char[],int,int,char)", () ->
            {
                runner.test("with null characters", (Test test) ->
                {
                    test.assertThrows(() -> Array.indexOf(null, 0, 0, 'a'),
                        new PreConditionFailure("characters cannot be null."));
                });

                runner.test("with negative startIndex", (Test test) ->
                {
                    test.assertThrows(() -> Array.indexOf(new char[] { 'x', 'y', 'z' }, -1, 0, 'a'),
                        new PreConditionFailure("startIndex (-1) must be between 0 and 2."));
                });

                runner.test("with startIndex equal to values length", (Test test) ->
                {
                    test.assertThrows(() -> Array.indexOf(new char[] { 'x', 'y', 'z' }, 3, 0, 'a'),
                        new PreConditionFailure("startIndex (3) must be between 0 and 2."));
                });

                runner.test("with startIndex greater than values length", (Test test) ->
                {
                    test.assertThrows(() -> Array.indexOf(new char[] { 'x', 'y', 'z' }, 4, 0, 'a'),
                        new PreConditionFailure("startIndex (4) must be between 0 and 2."));
                });

                runner.test("with negative length", (Test test) ->
                {
                    test.assertThrows(() -> Array.indexOf(new char[] { 'x', 'y', 'z' }, 1, -1, 'a'),
                        new PreConditionFailure("length (-1) must be between 0 and 2."));
                });

                runner.test("with zero length", (Test test) ->
                {
                    test.assertEqual(-1, Array.indexOf(new char[] { 'x', 'y', 'z' }, 1, 0, 'a'));
                });

                runner.test("with length greater than values length", (Test test) ->
                {
                    test.assertThrows(() -> Array.indexOf(new char[] { 'x', 'y', 'z' }, 0, 4, 'a'),
                        new PreConditionFailure("length (4) must be between 0 and 3."));
                });

                runner.test("with length greater than values length - startIndex", (Test test) ->
                {
                    test.assertThrows(() -> Array.indexOf(new char[] { 'x', 'y', 'z' }, 1, 3, 'a'),
                        new PreConditionFailure("length (3) must be between 0 and 2."));
                });

                runner.test("with empty characters", (Test test) ->
                {
                    test.assertEqual(-1, Array.indexOf(new char[0], 0, 0, 'a'));
                });

                runner.test("with non-empty characters and not-found character", (Test test) ->
                {
                    test.assertEqual(-1, Array.indexOf(new char[] { 'x', 'y', 'z' }, 1, 2, 'a'));
                });

                runner.test("with non-empty characters and found character", (Test test) ->
                {
                    test.assertEqual(2, Array.indexOf(new char[] { 'x', 'y', 'z' }, 1, 2, 'z'));
                });
            });

            runner.testGroup("shiftLeft(byte[],int,int)", () ->
            {
                final Action4<byte[],Integer,Integer,Throwable> shiftLeftErrorTest = (byte[] values, Integer startIndex, Integer valuesToShift, Throwable expected) ->
                {
                    runner.test("with " + English.andList(values == null ? null : ByteArray.create(values), startIndex, valuesToShift), (Test test) ->
                    {
                        final byte[] valuesBackup = values == null ? null : Array.clone(values);

                        test.assertThrows(() -> Array.shiftLeft(values, startIndex, valuesToShift),
                            expected);
                        test.assertEqual(valuesBackup, values);
                    });
                };

                shiftLeftErrorTest.run(null, 0, 1, new PreConditionFailure("values cannot be null."));
                shiftLeftErrorTest.run(new byte[0], 0, 1, new PreConditionFailure("values cannot be empty."));
                shiftLeftErrorTest.run(new byte[] { 0, 1, 2 }, -1, 2, new PreConditionFailure("indexToRemove (-1) must be between 0 and 1."));
                shiftLeftErrorTest.run(new byte[] { 0, 1, 2 }, 2, 2, new PreConditionFailure("indexToRemove (2) must be between 0 and 1."));
                shiftLeftErrorTest.run(new byte[] { 0, 1, 2 }, 3, 2, new PreConditionFailure("indexToRemove (3) must be between 0 and 1."));
                shiftLeftErrorTest.run(new byte[] { 0, 1, 2 }, 4, 2, new PreConditionFailure("indexToRemove (4) must be between 0 and 1."));
                shiftLeftErrorTest.run(new byte[] { 0, 1, 2 }, 1, -1, new PreConditionFailure("valuesToShift (-1) must be between 0 and 1."));
                shiftLeftErrorTest.run(new byte[] { 0, 1, 2 }, 1, 2, new PreConditionFailure("valuesToShift (2) must be between 0 and 1."));
                shiftLeftErrorTest.run(new byte[] { 0, 1, 2 }, 1, 3, new PreConditionFailure("valuesToShift (3) must be between 0 and 1."));

                final Action4<byte[],Integer,Integer,byte[]> shiftLeftTest = (byte[] values, Integer startIndex, Integer valuesToShift, byte[] expected) ->
                {
                    runner.test("with " + English.andList(ByteArray.create(values), startIndex, valuesToShift), (Test test) ->
                    {
                        Array.shiftLeft(values, startIndex, valuesToShift);
                        test.assertEqual(expected, values);
                    });
                };

                shiftLeftTest.run(new byte[] { 0, 1, 2 }, 0, 0, new byte[] { 0, 1, 2 });
                shiftLeftTest.run(new byte[] { 0, 1, 2 }, 1, 0, new byte[] { 0, 1, 2 });
                shiftLeftTest.run(new byte[] { 0, 1, 2 }, 0, 1, new byte[] { 1, 1, 2 });
                shiftLeftTest.run(new byte[] { 0, 1, 2 }, 1, 1, new byte[] { 0, 2, 2 });
                shiftLeftTest.run(new byte[] { 0, 1, 2 }, 0, 2, new byte[] { 1, 2, 2 });
            });

            runner.testGroup("shiftLeft(int[],int,int)", () ->
            {
                final Action4<int[],Integer,Integer,Throwable> shiftLeftErrorTest = (int[] values, Integer startIndex, Integer valuesToShift, Throwable expected) ->
                {
                    runner.test("with " + English.andList(values == null ? null : IntegerArray.create(values), startIndex, valuesToShift), (Test test) ->
                    {
                        final int[] valuesBackup = values == null ? null : Array.clone(values);

                        test.assertThrows(() -> Array.shiftLeft(values, startIndex, valuesToShift),
                            expected);
                        test.assertEqual(valuesBackup, values);
                    });
                };

                shiftLeftErrorTest.run(null, 0, 1, new PreConditionFailure("values cannot be null."));
                shiftLeftErrorTest.run(new int[0], 0, 1, new PreConditionFailure("values cannot be empty."));
                shiftLeftErrorTest.run(new int[] { 0, 1, 2 }, -1, 2, new PreConditionFailure("indexToRemove (-1) must be between 0 and 1."));
                shiftLeftErrorTest.run(new int[] { 0, 1, 2 }, 2, 2, new PreConditionFailure("indexToRemove (2) must be between 0 and 1."));
                shiftLeftErrorTest.run(new int[] { 0, 1, 2 }, 3, 2, new PreConditionFailure("indexToRemove (3) must be between 0 and 1."));
                shiftLeftErrorTest.run(new int[] { 0, 1, 2 }, 4, 2, new PreConditionFailure("indexToRemove (4) must be between 0 and 1."));
                shiftLeftErrorTest.run(new int[] { 0, 1, 2 }, 1, -1, new PreConditionFailure("valuesToShift (-1) must be between 0 and 1."));
                shiftLeftErrorTest.run(new int[] { 0, 1, 2 }, 1, 2, new PreConditionFailure("valuesToShift (2) must be between 0 and 1."));
                shiftLeftErrorTest.run(new int[] { 0, 1, 2 }, 1, 3, new PreConditionFailure("valuesToShift (3) must be between 0 and 1."));

                final Action4<int[],Integer,Integer,int[]> shiftLeftTest = (int[] values, Integer startIndex, Integer valuesToShift, int[] expected) ->
                {
                    runner.test("with " + English.andList(IntegerArray.create(values), startIndex, valuesToShift), (Test test) ->
                    {
                        Array.shiftLeft(values, startIndex, valuesToShift);
                        test.assertEqual(expected, values);
                    });
                };

                shiftLeftTest.run(new int[] { 0, 1, 2 }, 0, 0, new int[] { 0, 1, 2 });
                shiftLeftTest.run(new int[] { 0, 1, 2 }, 1, 0, new int[] { 0, 1, 2 });
                shiftLeftTest.run(new int[] { 0, 1, 2 }, 0, 1, new int[] { 1, 1, 2 });
                shiftLeftTest.run(new int[] { 0, 1, 2 }, 1, 1, new int[] { 0, 2, 2 });
                shiftLeftTest.run(new int[] { 0, 1, 2 }, 0, 2, new int[] { 1, 2, 2 });
            });

            runner.testGroup("shiftRight(byte[],int,int)", () ->
            {
                final Action4<byte[],Integer,Integer,Throwable> shiftRightErrorTest = (byte[] values, Integer startIndex, Integer valuesToShift, Throwable expected) ->
                {
                    runner.test("with " + English.andList(values == null ? null : ByteArray.create(values), startIndex, valuesToShift), (Test test) ->
                    {
                        final byte[] valuesBackup = values == null ? null : Array.clone(values);

                        test.assertThrows(() -> Array.shiftRight(values, startIndex, valuesToShift),
                            expected);
                        test.assertEqual(valuesBackup, values);
                    });
                };

                shiftRightErrorTest.run(null, 0, 1, new PreConditionFailure("values cannot be null."));
                shiftRightErrorTest.run(new byte[0], 0, 1, new PreConditionFailure("values cannot be empty."));
                shiftRightErrorTest.run(new byte[] { 0, 1, 2 }, -1, 2, new PreConditionFailure("indexToOpen (-1) must be between 0 and 1."));
                shiftRightErrorTest.run(new byte[] { 0, 1, 2 }, 2, 2, new PreConditionFailure("indexToOpen (2) must be between 0 and 1."));
                shiftRightErrorTest.run(new byte[] { 0, 1, 2 }, 3, 2, new PreConditionFailure("indexToOpen (3) must be between 0 and 1."));
                shiftRightErrorTest.run(new byte[] { 0, 1, 2 }, 4, 2, new PreConditionFailure("indexToOpen (4) must be between 0 and 1."));
                shiftRightErrorTest.run(new byte[] { 0, 1, 2 }, 1, -1, new PreConditionFailure("valuesToShift (-1) must be between 0 and 1."));
                shiftRightErrorTest.run(new byte[] { 0, 1, 2 }, 1, 2, new PreConditionFailure("valuesToShift (2) must be between 0 and 1."));
                shiftRightErrorTest.run(new byte[] { 0, 1, 2 }, 1, 3, new PreConditionFailure("valuesToShift (3) must be between 0 and 1."));

                final Action4<byte[],Integer,Integer,byte[]> shiftRightTest = (byte[] values, Integer startIndex, Integer valuesToShift, byte[] expected) ->
                {
                    runner.test("with " + English.andList(ByteArray.create(values), startIndex, valuesToShift), (Test test) ->
                    {
                        Array.shiftRight(values, startIndex, valuesToShift);
                        test.assertEqual(expected, values);
                    });
                };

                shiftRightTest.run(new byte[] { 0, 1, 2 }, 0, 0, new byte[] { 0, 1, 2 });
                shiftRightTest.run(new byte[] { 0, 1, 2 }, 1, 0, new byte[] { 0, 1, 2 });
                shiftRightTest.run(new byte[] { 0, 1, 2 }, 0, 1, new byte[] { 0, 0, 2 });
                shiftRightTest.run(new byte[] { 0, 1, 2 }, 1, 1, new byte[] { 0, 1, 1 });
                shiftRightTest.run(new byte[] { 0, 1, 2 }, 0, 2, new byte[] { 0, 0, 1 });
            });

            runner.testGroup("shiftRight(int[],int,int)", () ->
            {
                final Action4<int[],Integer,Integer,Throwable> shiftRightErrorTest = (int[] values, Integer startIndex, Integer valuesToShift, Throwable expected) ->
                {
                    runner.test("with " + English.andList(values == null ? null : ByteArray.create(values), startIndex, valuesToShift), (Test test) ->
                    {
                        final int[] valuesBackup = values == null ? null : Array.clone(values);

                        test.assertThrows(() -> Array.shiftRight(values, startIndex, valuesToShift),
                            expected);
                        test.assertEqual(valuesBackup, values);
                    });
                };

                shiftRightErrorTest.run(null, 0, 1, new PreConditionFailure("values cannot be null."));
                shiftRightErrorTest.run(new int[0], 0, 1, new PreConditionFailure("values cannot be empty."));
                shiftRightErrorTest.run(new int[] { 0, 1, 2 }, -1, 2, new PreConditionFailure("indexToOpen (-1) must be between 0 and 1."));
                shiftRightErrorTest.run(new int[] { 0, 1, 2 }, 2, 2, new PreConditionFailure("indexToOpen (2) must be between 0 and 1."));
                shiftRightErrorTest.run(new int[] { 0, 1, 2 }, 3, 2, new PreConditionFailure("indexToOpen (3) must be between 0 and 1."));
                shiftRightErrorTest.run(new int[] { 0, 1, 2 }, 4, 2, new PreConditionFailure("indexToOpen (4) must be between 0 and 1."));
                shiftRightErrorTest.run(new int[] { 0, 1, 2 }, 1, -1, new PreConditionFailure("valuesToShift (-1) must be between 0 and 1."));
                shiftRightErrorTest.run(new int[] { 0, 1, 2 }, 1, 2, new PreConditionFailure("valuesToShift (2) must be between 0 and 1."));
                shiftRightErrorTest.run(new int[] { 0, 1, 2 }, 1, 3, new PreConditionFailure("valuesToShift (3) must be between 0 and 1."));

                final Action4<int[],Integer,Integer,int[]> shiftRightTest = (int[] values, Integer startIndex, Integer valuesToShift, int[] expected) ->
                {
                    runner.test("with " + English.andList(ByteArray.create(values), startIndex, valuesToShift), (Test test) ->
                    {
                        Array.shiftRight(values, startIndex, valuesToShift);
                        test.assertEqual(expected, values);
                    });
                };

                shiftRightTest.run(new int[] { 0, 1, 2 }, 0, 0, new int[] { 0, 1, 2 });
                shiftRightTest.run(new int[] { 0, 1, 2 }, 1, 0, new int[] { 0, 1, 2 });
                shiftRightTest.run(new int[] { 0, 1, 2 }, 0, 1, new int[] { 0, 0, 2 });
                shiftRightTest.run(new int[] { 0, 1, 2 }, 1, 1, new int[] { 0, 1, 1 });
                shiftRightTest.run(new int[] { 0, 1, 2 }, 0, 2, new int[] { 0, 0, 1 });
            });
        });
    }
}
