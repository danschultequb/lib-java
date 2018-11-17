package qub;

public class ArrayTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(Array.class, () ->
        {
            IndexableTests.test(runner, (Integer count) ->
            {
                final Array<Integer> result = new Array<>(count);
                for (int i = 0; i < count; ++i)
                {
                    result.set(i, i);
                }
                return result; 
            });

            runner.testGroup("fromValues(boolean[])", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final Array<Boolean> array = Array.fromValues((boolean[])null);
                    test.assertEqual(0, array.getCount());
                });

                runner.test("with no values", (Test test) ->
                {
                    final Array<Boolean> array = Array.fromValues(new boolean[0]);
                    test.assertEqual(0, array.getCount());
                });

                runner.test("with one value", (Test test) ->
                {
                    final Array<Boolean> array = Array.fromValues(new boolean[] { true });
                    test.assertEqual(1, array.getCount());
                    test.assertEqual(true, array.get(0));
                });

                runner.test("with two values", (Test test) ->
                {
                    final Array<Boolean> array = Array.fromValues(new boolean[] { true, false });
                    test.assertEqual(2, array.getCount());
                    test.assertEqual(true, array.get(0));
                    test.assertEqual(false, array.get(1));
                });
            });

            runner.testGroup("fromValues(byte[])", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> Array.fromValues((byte[])null));
                });

                runner.test("with no values", (Test test) ->
                {
                    final Array<Byte> array = Array.fromValues(new byte[0]);
                    test.assertEqual(0, array.getCount());
                });

                runner.test("with one value", (Test test) ->
                {
                    final Array<Byte> array = Array.fromValues(new byte[] { 12 });
                    test.assertEqual(1, array.getCount());
                    test.assertEqual(12, array.get(0).intValue());
                });

                runner.test("with two values", (Test test) ->
                {
                    final Array<Byte> array = Array.fromValues(new byte[] { 13, 14 });
                    test.assertEqual(2, array.getCount());
                    test.assertEqual(13, array.get(0).intValue());
                    test.assertEqual(14, array.get(1).intValue());
                });
            });

            runner.testGroup("fromValues(char[])", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final Array<Character> array = Array.fromValues((char[])null);
                    test.assertEqual(0, array.getCount());
                });

                runner.test("with no values", (Test test) ->
                {
                    final Array<Character> array = Array.fromValues(new char[0]);
                    test.assertEqual(0, array.getCount());
                });

                runner.test("with one value", (Test test) ->
                {
                    final Array<Character> array = Array.fromValues(new char[] { 'a' });
                    test.assertEqual(1, array.getCount());
                    test.assertEqual('a', array.get(0));
                });

                runner.test("with two values", (Test test) ->
                {
                    final Array<Character> array = Array.fromValues(new char[] { 'b', 'c' });
                    test.assertEqual(2, array.getCount());
                    test.assertEqual('b', array.get(0));
                    test.assertEqual('c', array.get(1));
                });
            });

            runner.testGroup("fromValues(int[])", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final Array<Integer> array = Array.fromValues((int[])null);
                    test.assertEqual(0, array.getCount());
                });

                runner.test("with no values", (Test test) ->
                {
                    final Array<Integer> array = Array.fromValues(new int[0]);
                    test.assertEqual(0, array.getCount());
                });

                runner.test("with one value", (Test test) ->
                {
                    final Array<Integer> array = Array.fromValues(new int[] { 101 });
                    test.assertEqual(1, array.getCount());
                    test.assertEqual(101, array.get(0));
                });

                runner.test("with two values", (Test test) ->
                {
                    final Array<Integer> array = Array.fromValues(new int[] { 101, 102 });
                    test.assertEqual(2, array.getCount());
                    test.assertEqual(101, array.get(0));
                    test.assertEqual(102, array.get(1));
                });
            });
            
            runner.testGroup("fromValues(T[])", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final Array<Integer> array = Array.fromValues((Integer[])null);
                    test.assertEqual(0, array.getCount());
                });

                runner.test("with no values", (Test test) ->
                {
                    final Array<Integer> array = Array.fromValues(new Integer[0]);
                    test.assertEqual(0, array.getCount());
                });

                runner.test("with one value", (Test test) ->
                {
                    final Array<Integer> array = Array.fromValues(new Integer[] { 101 });
                    test.assertEqual(1, array.getCount());
                    test.assertEqual(101, array.get(0));
                });

                runner.test("with two values", (Test test) ->
                {
                    final Array<Integer> array = Array.fromValues(new Integer[] { 101, 102 });
                    test.assertEqual(2, array.getCount());
                    test.assertEqual(101, array.get(0));
                    test.assertEqual(102, array.get(1));
                });
            });

            runner.testGroup("fromValues(Iterator<T>)", () ->
            {
                runner.test("with null Iterator", (Test test) ->
                {
                    final Array<Integer> array = Array.fromValues((Iterator<Integer>)null);
                    test.assertEqual(0, array.getCount());
                });

                runner.test("with empty Iterator", (Test test) ->
                {
                    final Array<Integer> array = Array.fromValues(new Array<Integer>(0).iterate());
                    test.assertEqual(0, array.getCount());
                });

                runner.test("with non-empty Iterator", (Test test) ->
                {
                    final Array<Integer> array = Array.fromValues(Array.fromValues(new Integer[] { 1, 2, 3 }).iterate());
                    test.assertEqual(3, array.getCount());
                    test.assertEqual(1, array.get(0));
                    test.assertEqual(2, array.get(1));
                    test.assertEqual(3, array.get(2));
                });
            });

            runner.testGroup("fromValues(Iterable<T>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final Array<Integer> array = Array.fromValues((Iterable<Integer>)null);
                    test.assertEqual(0, array.getCount());
                });

                runner.test("with empty", (Test test) ->
                {
                    final Array<Integer> array = Array.fromValues(new Array<Integer>(0));
                    test.assertEqual(0, array.getCount());
                });

                runner.test("with non-empty", (Test test) ->
                {
                    final Array<Integer> array = Array.fromValues(Array.fromValues(new Integer[] { 1, 2, 3 }));
                    test.assertEqual(3, array.getCount());
                    test.assertEqual(1, array.get(0));
                    test.assertEqual(2, array.get(1));
                    test.assertEqual(3, array.get(2));
                });
            });
            
            runner.testGroup("constructor(int)", () ->
            {
                runner.test("with 0 length", (Test test) ->
                {
                    final Array<Integer> a = new Array<>(0);
                    test.assertEqual(0, a.getCount());
                });
                
                runner.test("with 1 length", (Test test) ->
                {
                    final Array<Integer> a = new Array<>(1);
                    test.assertEqual(1, a.getCount());
                    test.assertEqual(null, a.get(0));
                });
            });
            
            runner.testGroup("get()", () ->
            {
                runner.test("with negative index", (Test test) ->
                {
                    final Array<Integer> a = new Array<>(10);
                    test.assertThrows(() -> a.get(-1));
                });
                
                runner.test("with too large index", (Test test) ->
                {
                    final Array<Integer> a = new Array<>(10);
                    test.assertThrows(() -> a.get(10));
                });
            });
            
            runner.testGroup("set()", () ->
            {
                runner.test("with negative index", (Test test) ->
                {
                    final Array<Integer> a = new Array<>(10);
                    test.assertThrows(() -> a.set(-1, 49));
                });
                
                runner.test("with too large index", (Test test) ->
                {
                    final Array<Integer> a = new Array<>(10);
                    test.assertThrows(() -> a.set(10, 48));
                });
                
                runner.test("with indexes in bounds", (Test test) ->
                {
                    final Array<Integer> a = new Array<>(11);
                    for (int i = 0; i < a.getCount(); ++i) {
                        a.set(i, i);
                        test.assertEqual(i, a.get(i));
                    }
                });
            });
            
            runner.test("setAll()", (Test test) ->
            {
                final Array<Integer> a = new Array<>(0);
                a.setAll(50);

                final Array<Integer> a2 = new Array<>(200);
                a2.setAll(3);
                for (int i = 0; i < a2.getCount(); ++i)
                {
                    test.assertEqual(3, a2.get(i));
                }
            });
            
            runner.testGroup("iterateReverse()", () ->
            {
                runner.test("with empty Array", (Test test) ->
                {
                    final Array<Integer> array = new Array<>(0);
                    final Iterator<Integer> iterator = array.iterateReverse();
                    test.assertFalse(iterator.hasStarted());
                    test.assertFalse(iterator.hasCurrent());
                    test.assertNull(iterator.getCurrent());

                    test.assertFalse(iterator.next());
                    test.assertTrue(iterator.hasStarted());
                    test.assertFalse(iterator.hasCurrent());
                    test.assertNull(iterator.getCurrent());
                });

                runner.test("with non-empty Array", (Test test) ->
                {
                    final Array<Integer> array = new Array<>(10);
                    for (int i = 0; i < array.getCount(); ++i)
                    {
                        array.set(i, i);
                    }
                    final Iterator<Integer> iterator = array.iterateReverse();
                    test.assertFalse(iterator.hasStarted());
                    test.assertFalse(iterator.hasCurrent());
                    test.assertNull(iterator.getCurrent());

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
                    test.assertNull(iterator.getCurrent());
                });
            });
            
            runner.testGroup("toBooleanArray()", () ->
            {
                runner.test("with null Iterator", (Test test) ->
                {
                    test.assertNull(Array.toBooleanArray((Iterator<Boolean>)null));
                });
                
                runner.test("with empty Iterator", (Test test) ->
                {
                    test.assertEqual(
                        new boolean[0],
                        Array.toBooleanArray(new Array<Boolean>(0).iterate()));
                });

                runner.test("with non-empty Iterator", (Test test) ->
                {
                    test.assertEqual(
                        new boolean[] { false, true, false },
                        Array.toBooleanArray(Array.fromValues(new Boolean[] { false, true, false }).iterate()));
                });

                runner.test("with null Iterable", (Test test) ->
                {
                    test.assertNull(Array.toBooleanArray((Iterable<Boolean>)null));
                });

                runner.test("with empty Iterable", (Test test) ->
                {
                    test.assertEqual(new boolean[0], Array.toBooleanArray(new Array<Boolean>(0)));
                });
            });

            runner.testGroup("toByteArray()", () ->
            {
                runner.test("with null Iterator", (Test test) ->
                {
                    test.assertNull(Array.toByteArray((Iterator<Byte>)null));
                });

                runner.test("with empty Iterator", (Test test) ->
                {
                    test.assertEqual(new byte[0], Array.toByteArray(new Array<Byte>(0).iterate()));
                });

                runner.test("with non-empty Iterator", (Test test) ->
                {
                    test.assertEqual(new byte[] { 0, 1, 2 }, Array.toByteArray(Array.fromValues(new Byte[] { 0, 1, 2 }).iterate()));
                });

                runner.test("with null Iterable", (Test test) ->
                {
                    test.assertNull(Array.toByteArray((Iterable<Byte>)null));
                });

                runner.test("with empty Iterable", (Test test) ->
                {
                    test.assertEqual(new byte[0], Array.toByteArray(new Array<Byte>(0)));
                });
            });

            runner.testGroup("toIntArray()", () ->
            {
                runner.test("with null Iterator", (Test test) ->
                {
                    test.assertNull(Array.toIntArray((Iterator<Integer>)null));
                });

                runner.test("with empty Iterator", (Test test) ->
                {
                    test.assertEqual(new int[0], Array.toIntArray(new Array<Integer>(0).iterate()));
                });

                runner.test("with non-empty Iterator", (Test test) ->
                {
                    test.assertEqual(new int[] { 0, 1, 2 }, Array.toIntArray(Array.fromValues(new Integer[] { 0, 1, 2 }).iterate()));
                });

                runner.test("with null Iterable", (Test test) ->
                {
                    test.assertNull(Array.toIntArray((Iterable<Integer>)null));
                });

                runner.test("with empty Iterable", (Test test) ->
                {
                    test.assertEqual(new int[0], Array.toIntArray(new Array<>(0)));
                });
            });

            runner.testGroup("toStringArray()", () ->
            {
                runner.test("with null Iterator", (Test test) ->
                {
                    test.assertNull(Array.toStringArray((Iterator<String>)null));
                });

                runner.test("with empty Iterator", (Test test) ->
                {
                    test.assertEqual(new String[0], Array.toStringArray(new Array<String>(0).iterate()));
                });

                runner.test("with non-empty Iterator", (Test test) ->
                {
                    test.assertEqual(new String[] { "0", "1", "2" }, Array.toStringArray(Array.fromValues(new String[] { "0", "1", "2" }).iterate()));
                });

                runner.test("with null Iterable", (Test test) ->
                {
                    test.assertNull(Array.toStringArray((Iterable<String>)null));
                });

                runner.test("with empty Iterable", (Test test) ->
                {
                    test.assertEqual(new String[0], Array.toStringArray(new Array<>(0)));
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

            runner.testGroup("copy(byte[],int,byte[],int,int)", () ->
            {
                final Action6<byte[],Integer,byte[],Integer,Integer,byte[]> copyTest = (byte[] copyFrom, Integer copyFromStartIndex, byte[] copyTo, Integer copyToStartIndex, Integer length, byte[] expectedBytes) ->
                {
                    runner.test("from " + Array.toString(copyFrom) + " at index " + copyFromStartIndex + " to " + Array.toString(copyTo) + " at index " + copyToStartIndex + " for length " + length, (Test test) ->
                    {
                        final byte[] copyFromClone = Array.clone(copyFrom);

                        Array.copy(copyFrom, copyFromStartIndex, copyTo, copyToStartIndex, length);

                        test.assertEqual(copyFromClone, copyFrom);
                        test.assertEqual(expectedBytes, copyTo);
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
                copyTest.run(null, -1, new byte[0], -1, -1, new byte[0]);
                copyTest.run(null, -1, new byte[0], -1, 0, new byte[0]);
                copyTest.run(null, -1, new byte[0], -1, 1, new byte[0]);
                copyTest.run(null, -1, new byte[0], 0, -1, new byte[0]);
                copyTest.run(null, -1, new byte[0], 0, 0, new byte[0]);
                copyTest.run(null, -1, new byte[0], 0, 1, new byte[0]);
                copyTest.run(null, -1, new byte[0], 1, -1, new byte[0]);
                copyTest.run(null, -1, new byte[0], 1, 0, new byte[0]);
                copyTest.run(null, -1, new byte[0], 1, 1, new byte[0]);
                copyTest.run(null, -1, new byte[] { 2 }, -1, -1, new byte[] { 2 });
                copyTest.run(null, -1, new byte[] { 2 }, -1, 0, new byte[] { 2 });
                copyTest.run(null, -1, new byte[] { 2 }, -1, 1, new byte[] { 2 });
                copyTest.run(null, -1, new byte[] { 2 }, 0, -1, new byte[] { 2 });
                copyTest.run(null, -1, new byte[] { 2 }, 0, 0, new byte[] { 2 });
                copyTest.run(null, -1, new byte[] { 2 }, 0, 1, new byte[] { 2 });
                copyTest.run(null, -1, new byte[] { 2 }, 1, -1, new byte[] { 2 });
                copyTest.run(null, -1, new byte[] { 2 }, 1, 0, new byte[] { 2 });
                copyTest.run(null, -1, new byte[] { 2 }, 1, 1, new byte[] { 2 });
                copyTest.run(null, 0, null, -1, -1, null);
                copyTest.run(null, 0, null, -1, 0, null);
                copyTest.run(null, 0, null, -1, 1, null);
                copyTest.run(null, 0, null, 0, -1, null);
                copyTest.run(null, 0, null, 0, 0, null);
                copyTest.run(null, 0, null, 0, 1, null);
                copyTest.run(null, 0, null, 1, -1, null);
                copyTest.run(null, 0, null, 1, 0, null);
                copyTest.run(null, 0, null, 1, 1, null);
                copyTest.run(null, 0, new byte[0], -1, -1, new byte[0]);
                copyTest.run(null, 0, new byte[0], -1, 0, new byte[0]);
                copyTest.run(null, 0, new byte[0], -1, 1, new byte[0]);
                copyTest.run(null, 0, new byte[0], 0, -1, new byte[0]);
                copyTest.run(null, 0, new byte[0], 0, 0, new byte[0]);
                copyTest.run(null, 0, new byte[0], 0, 1, new byte[0]);
                copyTest.run(null, 0, new byte[0], 1, -1, new byte[0]);
                copyTest.run(null, 0, new byte[0], 1, 0, new byte[0]);
                copyTest.run(null, 0, new byte[0], 1, 1, new byte[0]);
                copyTest.run(null, 0, new byte[] { 2 }, -1, -1, new byte[] { 2 });
                copyTest.run(null, 0, new byte[] { 2 }, -1, 0, new byte[] { 2 });
                copyTest.run(null, 0, new byte[] { 2 }, -1, 1, new byte[] { 2 });
                copyTest.run(null, 0, new byte[] { 2 }, 0, -1, new byte[] { 2 });
                copyTest.run(null, 0, new byte[] { 2 }, 0, 0, new byte[] { 2 });
                copyTest.run(null, 0, new byte[] { 2 }, 0, 1, new byte[] { 2 });
                copyTest.run(null, 0, new byte[] { 2 }, 1, -1, new byte[] { 2 });
                copyTest.run(null, 0, new byte[] { 2 }, 1, 0, new byte[] { 2 });
                copyTest.run(null, 0, new byte[] { 2 }, 1, 1, new byte[] { 2 });
                copyTest.run(null, 1, null, -1, -1, null);
                copyTest.run(null, 1, null, -1, 0, null);
                copyTest.run(null, 1, null, -1, 1, null);
                copyTest.run(null, 1, null, 0, -1, null);
                copyTest.run(null, 1, null, 0, 0, null);
                copyTest.run(null, 1, null, 0, 1, null);
                copyTest.run(null, 1, null, 1, -1, null);
                copyTest.run(null, 1, null, 1, 0, null);
                copyTest.run(null, 1, null, 1, 1, null);
                copyTest.run(null, 1, new byte[0], -1, -1, new byte[0]);
                copyTest.run(null, 1, new byte[0], -1, 0, new byte[0]);
                copyTest.run(null, 1, new byte[0], -1, 1, new byte[0]);
                copyTest.run(null, 1, new byte[0], 0, -1, new byte[0]);
                copyTest.run(null, 1, new byte[0], 0, 0, new byte[0]);
                copyTest.run(null, 1, new byte[0], 0, 1, new byte[0]);
                copyTest.run(null, 1, new byte[0], 1, -1, new byte[0]);
                copyTest.run(null, 1, new byte[0], 1, 0, new byte[0]);
                copyTest.run(null, 1, new byte[0], 1, 1, new byte[0]);
                copyTest.run(null, 1, new byte[] { 2 }, -1, -1, new byte[] { 2 });
                copyTest.run(null, 1, new byte[] { 2 }, -1, 0, new byte[] { 2 });
                copyTest.run(null, 1, new byte[] { 2 }, -1, 1, new byte[] { 2 });
                copyTest.run(null, 1, new byte[] { 2 }, 0, -1, new byte[] { 2 });
                copyTest.run(null, 1, new byte[] { 2 }, 0, 0, new byte[] { 2 });
                copyTest.run(null, 1, new byte[] { 2 }, 0, 1, new byte[] { 2 });
                copyTest.run(null, 1, new byte[] { 2 }, 1, -1, new byte[] { 2 });
                copyTest.run(null, 1, new byte[] { 2 }, 1, 0, new byte[] { 2 });
                copyTest.run(null, 1, new byte[] { 2 }, 1, 1, new byte[] { 2 });
                copyTest.run(new byte[0], -1, null, -1, -1, null);
                copyTest.run(new byte[0], -1, null, -1, 0, null);
                copyTest.run(new byte[0], -1, null, -1, 1, null);
                copyTest.run(new byte[0], -1, null, 0, -1, null);
                copyTest.run(new byte[0], -1, null, 0, 0, null);
                copyTest.run(new byte[0], -1, null, 0, 1, null);
                copyTest.run(new byte[0], -1, null, 1, -1, null);
                copyTest.run(new byte[0], -1, null, 1, 0, null);
                copyTest.run(new byte[0], -1, null, 1, 1, null);
                copyTest.run(new byte[0], -1, new byte[0], -1, -1, new byte[0]);
                copyTest.run(new byte[0], -1, new byte[0], -1, 0, new byte[0]);
                copyTest.run(new byte[0], -1, new byte[0], -1, 1, new byte[0]);
                copyTest.run(new byte[0], -1, new byte[0], 0, -1, new byte[0]);
                copyTest.run(new byte[0], -1, new byte[0], 0, 0, new byte[0]);
                copyTest.run(new byte[0], -1, new byte[0], 0, 1, new byte[0]);
                copyTest.run(new byte[0], -1, new byte[0], 1, -1, new byte[0]);
                copyTest.run(new byte[0], -1, new byte[0], 1, 0, new byte[0]);
                copyTest.run(new byte[0], -1, new byte[0], 1, 1, new byte[0]);
                copyTest.run(new byte[0], -1, new byte[] { 2 }, -1, -1, new byte[] { 2 });
                copyTest.run(new byte[0], -1, new byte[] { 2 }, -1, 0, new byte[] { 2 });
                copyTest.run(new byte[0], -1, new byte[] { 2 }, -1, 1, new byte[] { 2 });
                copyTest.run(new byte[0], -1, new byte[] { 2 }, 0, -1, new byte[] { 2 });
                copyTest.run(new byte[0], -1, new byte[] { 2 }, 0, 0, new byte[] { 2 });
                copyTest.run(new byte[0], -1, new byte[] { 2 }, 0, 1, new byte[] { 2 });
                copyTest.run(new byte[0], -1, new byte[] { 2 }, 1, -1, new byte[] { 2 });
                copyTest.run(new byte[0], -1, new byte[] { 2 }, 1, 0, new byte[] { 2 });
                copyTest.run(new byte[0], -1, new byte[] { 2 }, 1, 1, new byte[] { 2 });
                copyTest.run(new byte[0], 0, null, -1, -1, null);
                copyTest.run(new byte[0], 0, null, -1, 0, null);
                copyTest.run(new byte[0], 0, null, -1, 1, null);
                copyTest.run(new byte[0], 0, null, 0, -1, null);
                copyTest.run(new byte[0], 0, null, 0, 0, null);
                copyTest.run(new byte[0], 0, null, 0, 1, null);
                copyTest.run(new byte[0], 0, null, 1, -1, null);
                copyTest.run(new byte[0], 0, null, 1, 0, null);
                copyTest.run(new byte[0], 0, null, 1, 1, null);
                copyTest.run(new byte[0], 0, new byte[0], -1, -1, new byte[0]);
                copyTest.run(new byte[0], 0, new byte[0], -1, 0, new byte[0]);
                copyTest.run(new byte[0], 0, new byte[0], -1, 1, new byte[0]);
                copyTest.run(new byte[0], 0, new byte[0], 0, -1, new byte[0]);
                copyTest.run(new byte[0], 0, new byte[0], 0, 0, new byte[0]);
                copyTest.run(new byte[0], 0, new byte[0], 0, 1, new byte[0]);
                copyTest.run(new byte[0], 0, new byte[0], 1, -1, new byte[0]);
                copyTest.run(new byte[0], 0, new byte[0], 1, 0, new byte[0]);
                copyTest.run(new byte[0], 0, new byte[0], 1, 1, new byte[0]);
                copyTest.run(new byte[0], 0, new byte[] { 2 }, -1, -1, new byte[] { 2 });
                copyTest.run(new byte[0], 0, new byte[] { 2 }, -1, 0, new byte[] { 2 });
                copyTest.run(new byte[0], 0, new byte[] { 2 }, -1, 1, new byte[] { 2 });
                copyTest.run(new byte[0], 0, new byte[] { 2 }, 0, -1, new byte[] { 2 });
                copyTest.run(new byte[0], 0, new byte[] { 2 }, 0, 0, new byte[] { 2 });
                copyTest.run(new byte[0], 0, new byte[] { 2 }, 0, 1, new byte[] { 2 });
                copyTest.run(new byte[0], 0, new byte[] { 2 }, 1, -1, new byte[] { 2 });
                copyTest.run(new byte[0], 0, new byte[] { 2 }, 1, 0, new byte[] { 2 });
                copyTest.run(new byte[0], 0, new byte[] { 2 }, 1, 1, new byte[] { 2 });
                copyTest.run(new byte[0], 1, null, -1, -1, null);
                copyTest.run(new byte[0], 1, null, -1, 0, null);
                copyTest.run(new byte[0], 1, null, -1, 1, null);
                copyTest.run(new byte[0], 1, null, 0, -1, null);
                copyTest.run(new byte[0], 1, null, 0, 0, null);
                copyTest.run(new byte[0], 1, null, 0, 1, null);
                copyTest.run(new byte[0], 1, null, 1, -1, null);
                copyTest.run(new byte[0], 1, null, 1, 0, null);
                copyTest.run(new byte[0], 1, null, 1, 1, null);
                copyTest.run(new byte[0], 1, new byte[0], -1, -1, new byte[0]);
                copyTest.run(new byte[0], 1, new byte[0], -1, 0, new byte[0]);
                copyTest.run(new byte[0], 1, new byte[0], -1, 1, new byte[0]);
                copyTest.run(new byte[0], 1, new byte[0], 0, -1, new byte[0]);
                copyTest.run(new byte[0], 1, new byte[0], 0, 0, new byte[0]);
                copyTest.run(new byte[0], 1, new byte[0], 0, 1, new byte[0]);
                copyTest.run(new byte[0], 1, new byte[0], 1, -1, new byte[0]);
                copyTest.run(new byte[0], 1, new byte[0], 1, 0, new byte[0]);
                copyTest.run(new byte[0], 1, new byte[0], 1, 1, new byte[0]);
                copyTest.run(new byte[0], 1, new byte[] { 2 }, -1, -1, new byte[] { 2 });
                copyTest.run(new byte[0], 1, new byte[] { 2 }, -1, 0, new byte[] { 2 });
                copyTest.run(new byte[0], 1, new byte[] { 2 }, -1, 1, new byte[] { 2 });
                copyTest.run(new byte[0], 1, new byte[] { 2 }, 0, -1, new byte[] { 2 });
                copyTest.run(new byte[0], 1, new byte[] { 2 }, 0, 0, new byte[] { 2 });
                copyTest.run(new byte[0], 1, new byte[] { 2 }, 0, 1, new byte[] { 2 });
                copyTest.run(new byte[0], 1, new byte[] { 2 }, 1, -1, new byte[] { 2 });
                copyTest.run(new byte[0], 1, new byte[] { 2 }, 1, 0, new byte[] { 2 });
                copyTest.run(new byte[0], 1, new byte[] { 2 }, 1, 1, new byte[] { 2 });
                copyTest.run(new byte[] { 1 }, -1, null, -1, -1, null);
                copyTest.run(new byte[] { 1 }, -1, null, -1, 0, null);
                copyTest.run(new byte[] { 1 }, -1, null, -1, 1, null);
                copyTest.run(new byte[] { 1 }, -1, null, 0, -1, null);
                copyTest.run(new byte[] { 1 }, -1, null, 0, 0, null);
                copyTest.run(new byte[] { 1 }, -1, null, 0, 1, null);
                copyTest.run(new byte[] { 1 }, -1, null, 1, -1, null);
                copyTest.run(new byte[] { 1 }, -1, null, 1, 0, null);
                copyTest.run(new byte[] { 1 }, -1, null, 1, 1, null);
                copyTest.run(new byte[] { 1 }, -1, new byte[0], -1, -1, new byte[0]);
                copyTest.run(new byte[] { 1 }, -1, new byte[0], -1, 0, new byte[0]);
                copyTest.run(new byte[] { 1 }, -1, new byte[0], -1, 1, new byte[0]);
                copyTest.run(new byte[] { 1 }, -1, new byte[0], 0, -1, new byte[0]);
                copyTest.run(new byte[] { 1 }, -1, new byte[0], 0, 0, new byte[0]);
                copyTest.run(new byte[] { 1 }, -1, new byte[0], 0, 1, new byte[0]);
                copyTest.run(new byte[] { 1 }, -1, new byte[0], 1, -1, new byte[0]);
                copyTest.run(new byte[] { 1 }, -1, new byte[0], 1, 0, new byte[0]);
                copyTest.run(new byte[] { 1 }, -1, new byte[0], 1, 1, new byte[0]);
                copyTest.run(new byte[] { 1 }, -1, new byte[] { 2 }, -1, -1, new byte[] { 2 });
                copyTest.run(new byte[] { 1 }, -1, new byte[] { 2 }, -1, 0, new byte[] { 2 });
                copyTest.run(new byte[] { 1 }, -1, new byte[] { 2 }, -1, 1, new byte[] { 2 });
                copyTest.run(new byte[] { 1 }, -1, new byte[] { 2 }, 0, -1, new byte[] { 2 });
                copyTest.run(new byte[] { 1 }, -1, new byte[] { 2 }, 0, 0, new byte[] { 2 });
                copyTest.run(new byte[] { 1 }, -1, new byte[] { 2 }, 0, 1, new byte[] { 2 });
                copyTest.run(new byte[] { 1 }, -1, new byte[] { 2 }, 1, -1, new byte[] { 2 });
                copyTest.run(new byte[] { 1 }, -1, new byte[] { 2 }, 1, 0, new byte[] { 2 });
                copyTest.run(new byte[] { 1 }, -1, new byte[] { 2 }, 1, 1, new byte[] { 2 });
                copyTest.run(new byte[] { 1 }, 0, null, -1, -1, null);
                copyTest.run(new byte[] { 1 }, 0, null, -1, 0, null);
                copyTest.run(new byte[] { 1 }, 0, null, -1, 1, null);
                copyTest.run(new byte[] { 1 }, 0, null, 0, -1, null);
                copyTest.run(new byte[] { 1 }, 0, null, 0, 0, null);
                copyTest.run(new byte[] { 1 }, 0, null, 0, 1, null);
                copyTest.run(new byte[] { 1 }, 0, null, 1, -1, null);
                copyTest.run(new byte[] { 1 }, 0, null, 1, 0, null);
                copyTest.run(new byte[] { 1 }, 0, null, 1, 1, null);
                copyTest.run(new byte[] { 1 }, 0, new byte[0], -1, -1, new byte[0]);
                copyTest.run(new byte[] { 1 }, 0, new byte[0], -1, 0, new byte[0]);
                copyTest.run(new byte[] { 1 }, 0, new byte[0], -1, 1, new byte[0]);
                copyTest.run(new byte[] { 1 }, 0, new byte[0], 0, -1, new byte[0]);
                copyTest.run(new byte[] { 1 }, 0, new byte[0], 0, 0, new byte[0]);
                copyTest.run(new byte[] { 1 }, 0, new byte[0], 0, 1, new byte[0]);
                copyTest.run(new byte[] { 1 }, 0, new byte[0], 1, -1, new byte[0]);
                copyTest.run(new byte[] { 1 }, 0, new byte[0], 1, 0, new byte[0]);
                copyTest.run(new byte[] { 1 }, 0, new byte[0], 1, 1, new byte[0]);
                copyTest.run(new byte[] { 1 }, 0, new byte[] { 2 }, -1, -1, new byte[] { 2 });
                copyTest.run(new byte[] { 1 }, 0, new byte[] { 2 }, -1, 0, new byte[] { 2 });
                copyTest.run(new byte[] { 1 }, 0, new byte[] { 2 }, -1, 1, new byte[] { 2 });
                copyTest.run(new byte[] { 1 }, 0, new byte[] { 2 }, 0, -1, new byte[] { 2 });
                copyTest.run(new byte[] { 1 }, 0, new byte[] { 2 }, 0, 0, new byte[] { 2 });
                copyTest.run(new byte[] { 1 }, 0, new byte[] { 2 }, 0, 1, new byte[] { 1 });
                copyTest.run(new byte[] { 1 }, 0, new byte[] { 2 }, 1, -1, new byte[] { 2 });
                copyTest.run(new byte[] { 1 }, 0, new byte[] { 2 }, 1, 0, new byte[] { 2 });
                copyTest.run(new byte[] { 1 }, 0, new byte[] { 2 }, 1, 1, new byte[] { 2 });
                copyTest.run(new byte[] { 1 }, 1, null, -1, -1, null);
                copyTest.run(new byte[] { 1 }, 1, null, -1, 0, null);
                copyTest.run(new byte[] { 1 }, 1, null, -1, 1, null);
                copyTest.run(new byte[] { 1 }, 1, null, 0, -1, null);
                copyTest.run(new byte[] { 1 }, 1, null, 0, 0, null);
                copyTest.run(new byte[] { 1 }, 1, null, 0, 1, null);
                copyTest.run(new byte[] { 1 }, 1, null, 1, -1, null);
                copyTest.run(new byte[] { 1 }, 1, null, 1, 0, null);
                copyTest.run(new byte[] { 1 }, 1, null, 1, 1, null);
                copyTest.run(new byte[] { 1 }, 1, new byte[0], -1, -1, new byte[0]);
                copyTest.run(new byte[] { 1 }, 1, new byte[0], -1, 0, new byte[0]);
                copyTest.run(new byte[] { 1 }, 1, new byte[0], -1, 1, new byte[0]);
                copyTest.run(new byte[] { 1 }, 1, new byte[0], 0, -1, new byte[0]);
                copyTest.run(new byte[] { 1 }, 1, new byte[0], 0, 0, new byte[0]);
                copyTest.run(new byte[] { 1 }, 1, new byte[0], 0, 1, new byte[0]);
                copyTest.run(new byte[] { 1 }, 1, new byte[0], 1, -1, new byte[0]);
                copyTest.run(new byte[] { 1 }, 1, new byte[0], 1, 0, new byte[0]);
                copyTest.run(new byte[] { 1 }, 1, new byte[0], 1, 1, new byte[0]);
                copyTest.run(new byte[] { 1 }, 1, new byte[] { 2 }, -1, -1, new byte[] { 2 });
                copyTest.run(new byte[] { 1 }, 1, new byte[] { 2 }, -1, 0, new byte[] { 2 });
                copyTest.run(new byte[] { 1 }, 1, new byte[] { 2 }, -1, 1, new byte[] { 2 });
                copyTest.run(new byte[] { 1 }, 1, new byte[] { 2 }, 0, -1, new byte[] { 2 });
                copyTest.run(new byte[] { 1 }, 1, new byte[] { 2 }, 0, 0, new byte[] { 2 });
                copyTest.run(new byte[] { 1 }, 1, new byte[] { 2 }, 0, 1, new byte[] { 2 });
                copyTest.run(new byte[] { 1 }, 1, new byte[] { 2 }, 1, -1, new byte[] { 2 });
                copyTest.run(new byte[] { 1 }, 1, new byte[] { 2 }, 1, 0, new byte[] { 2 });
                copyTest.run(new byte[] { 1 }, 1, new byte[] { 2 }, 1, 1, new byte[] { 2 });
            });

            runner.testGroup("copy(char[],int,char[],int,int)", () ->
            {
                final Action6<char[],Integer,char[],Integer,Integer,char[]> copyTest = (char[] copyFrom, Integer copyFromStartIndex, char[] copyTo, Integer copyToStartIndex, Integer length, char[] expected) ->
                {
                    runner.test("from " + Array.toString(copyFrom) + " at index " + copyFromStartIndex + " to " + Array.toString(copyTo) + " at index " + copyToStartIndex + " for length " + length, (Test test) ->
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

            runner.testGroup("merge(Iterable<byte[]>)", () ->
            {
                final Action2<byte[][],byte[]> mergeTest = (byte[][] bytes, byte[] expected) ->
                {
                    runner.test("with " + (bytes == null ? "null" : Array.fromValues(bytes).map(Array::fromValues)), (Test test) ->
                    {
                        final Array<Byte> expectedArray = expected == null ? null : Array.fromValues(expected);
                        final byte[] mergedBytes = Array.merge(bytes == null ? null : Array.fromValues(bytes));
                        final Array<Byte> actualArray = mergedBytes == null ? null : Array.fromValues(mergedBytes);

                        test.assertEqual(expectedArray, actualArray);
                    });
                };

                mergeTest.run(null, null);
                mergeTest.run(new byte[][] { }, new byte[0]);
                mergeTest.run(new byte[][] { null }, new byte[0]);
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
        });
    }
}
