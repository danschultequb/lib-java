package qub;

public class ArrayTests
{
    public static void test(final TestRunner runner)
    {
        runner.testGroup("Array<T>", new Action0()
        {
            @Override
            public void run()
            {
                IndexableTests.test(runner, new Function1<Integer, Indexable<Integer>>()
                {
                    @Override
                    public Indexable<Integer> run(Integer count)
                    {
                        final Array<Integer> result = new Array<>(count);
                        for (int i = 0; i < count; ++i)
                        {
                            result.set(i, i);
                        }
                        return result; 
                    }
                });
                
                runner.testGroup("fromValues()", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("with no arguments", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Array<Integer> array = Array.fromValues(new Integer[0]);
                                test.assertEqual(0, array.getCount());
                            }
                        });
                        
                        runner.test("with one argument", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Array<Integer> array = Array.fromValues(new Integer[] { 101 });
                                test.assertEqual(1, array.getCount());
                                test.assertEqual(101, array.get(0));
                            }
                        });
                        
                        runner.test("with two arguments", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Array<Integer> array = Array.fromValues(new Integer[] { 101, 102 });
                                test.assertEqual(2, array.getCount());
                                test.assertEqual(101, array.get(0));
                                test.assertEqual(102, array.get(1));
                            }
                        });

                        runner.test("with null Iterator", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Array<Integer> array = Array.fromValues((Iterator<Integer>)null);
                                test.assertEqual(0, array.getCount());
                            }
                        });

                        runner.test("with empty Iterator", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Array<Integer> array = Array.fromValues(new Array<Integer>(0).iterate());
                                test.assertEqual(0, array.getCount());
                            }
                        });

                        runner.test("with non-empty Iterator", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Array<Integer> array = Array.fromValues(Array.fromValues(new Integer[] { 1, 2, 3 }).iterate());
                                test.assertEqual(3, array.getCount());
                                test.assertEqual(1, array.get(0));
                                test.assertEqual(2, array.get(1));
                                test.assertEqual(3, array.get(2));
                            }
                        });
                    }
                });
                
                runner.testGroup("constructor()", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("with 0 length", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Array<Integer> a = new Array<>(0);
                                test.assertEqual(0, a.getCount());
                            }
                        });
                        
                        runner.test("with 1 length", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Array<Integer> a = new Array<>(1);
                                test.assertEqual(1, a.getCount());
                                test.assertEqual(null, a.get(0));
                            }
                        });
                    }
                });
                
                runner.testGroup("get()", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("with negative index", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Array<Integer> a = new Array<>(10);
                                test.assertEqual(null, a.get(-1));
                            }
                        });
                        
                        runner.test("with too large index", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Array<Integer> a = new Array<>(10);
                                test.assertEqual(null, a.get(10));
                            }
                        });
                    }
                });
                
                runner.testGroup("set()", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("with negative index", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Array<Integer> a = new Array<>(10);
                                a.set(-1, 49);
                                test.assertEqual(null, a.get(-1));
                            }
                        });
                        
                        runner.test("with too large index", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Array<Integer> a = new Array<>(10);
                                a.set(10, 48);
                                test.assertEqual(null, a.get(10));
                            }
                        });
                        
                        runner.test("with indexes in bounds", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Array<Integer> a = new Array<>(11);
                                for (int i = 0; i < a.getCount(); ++i) {
                                    a.set(i, i);
                                    test.assertEqual(i, a.get(i));
                                }
                            }
                        });
                    }
                });
                
                runner.test("setAll()", new Action1<Test>()
                {
                    @Override
                    public void run(Test test)
                    {
                        final Array<Integer> a = new Array<>(0);
                        a.setAll(50);

                        final Array<Integer> a2 = new Array<>(200);
                        a2.setAll(3);
                        for (int i = 0; i < a2.getCount(); ++i)
                        {
                            test.assertEqual(3, a2.get(i));
                        }
                    }
                });
                
                runner.testGroup("iterateReverse()", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("with empty Array", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
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
                            }
                        });

                        runner.test("with non-empty Array", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
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
                            }
                        });
                    }
                });
                
                runner.testGroup("toBooleanArray()", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("with null Iterator", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                test.assertNull(Array.toBooleanArray((Iterator<Boolean>)null));
                            }
                        });
                        
                        runner.test("with empty Iterator", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                test.assertEqual(
                                    new boolean[0],
                                    Array.toBooleanArray(new Array<Boolean>(0).iterate()));
                            }
                        });

                        runner.test("with non-empty Iterator", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                test.assertEqual(
                                    new boolean[] { false, true, false },
                                    Array.toBooleanArray(Array.fromValues(new Boolean[] { false, true, false }).iterate()));
                            }
                        });

                        runner.test("with null Iterable", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                test.assertNull(Array.toBooleanArray((Iterable<Boolean>)null));
                            }
                        });

                        runner.test("with empty Iterable", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                test.assertEqual(new boolean[0], Array.toBooleanArray(new Array<Boolean>(0)));
                            }
                        });
                    }
                });

                runner.testGroup("toByteArray()", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("with null Iterator", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                test.assertNull(Array.toByteArray((Iterator<Byte>)null));
                            }
                        });

                        runner.test("with empty Iterator", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                test.assertEqual(new byte[0], Array.toByteArray(new Array<Byte>(0).iterate()));
                            }
                        });

                        runner.test("with non-empty Iterator", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                test.assertEqual(new byte[] { 0, 1, 2 }, Array.toByteArray(Array.fromValues(new Byte[] { 0, 1, 2 }).iterate()));
                            }
                        });

                        runner.test("with null Iterable", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                test.assertNull(Array.toByteArray((Iterable<Byte>)null));
                            }
                        });

                        runner.test("with empty Iterable", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                test.assertEqual(new byte[0], Array.toByteArray(new Array<Byte>(0)));
                            }
                        });
                    }
                });

                runner.testGroup("toIntArray()", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("with null Iterator", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                test.assertNull(Array.toIntArray((Iterator<Integer>)null));
                            }
                        });

                        runner.test("with empty Iterator", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                test.assertEqual(new int[0], Array.toIntArray(new Array<Integer>(0).iterate()));
                            }
                        });

                        runner.test("with non-empty Iterator", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                test.assertEqual(new int[] { 0, 1, 2 }, Array.toIntArray(Array.fromValues(new Integer[] { 0, 1, 2 }).iterate()));
                            }
                        });

                        runner.test("with null Iterable", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                test.assertNull(Array.toIntArray((Iterable<Integer>)null));
                            }
                        });

                        runner.test("with empty Iterable", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                test.assertEqual(new int[0], Array.toIntArray(new Array<Integer>(0)));
                            }
                        });
                    }
                });

                runner.testGroup("toStringArray()", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("with null Iterator", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                test.assertNull(Array.toStringArray((Iterator<String>)null));
                            }
                        });

                        runner.test("with empty Iterator", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                test.assertEqual(new String[0], Array.toStringArray(new Array<String>(0).iterate()));
                            }
                        });

                        runner.test("with non-empty Iterator", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                test.assertEqual(new String[] { "0", "1", "2" }, Array.toStringArray(Array.fromValues(new String[] { "0", "1", "2" }).iterate()));
                            }
                        });

                        runner.test("with null Iterable", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                test.assertNull(Array.toStringArray((Iterable<String>)null));
                            }
                        });

                        runner.test("with empty Iterable", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                test.assertEqual(new String[0], Array.toStringArray(new Array<String>(0)));
                            }
                        });
                    }
                });

                runner.testGroup("clone(byte[])", new Action0()
                {
                    @Override
                    public void run()
                    {
                        final Action1<byte[]> test = new Action1<byte[]>()
                        {
                            @Override
                            public void run(final byte[] bytes)
                            {
                                runner.test("with " + (bytes == null ? "null byte[]" : ("byte[" + bytes.length + "]")), new Action1<Test>()
                                {
                                    @Override
                                    public void run(Test test)
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
                                    }
                                });
                            }
                        };

                        test.run(null);
                        test.run(new byte[0]);
                        test.run(new byte[] { 0 });
                        test.run(new byte[] { 0, 1, 2, 3, 4 });
                    }
                });

                runner.testGroup("clone(byte[],int,int)", new Action0()
                {
                    @Override
                    public void run()
                    {
                        final Action4<byte[],Integer,Integer,byte[]> test = new Action4<byte[], Integer, Integer, byte[]>()
                        {
                            @Override
                            public void run(final byte[] bytes, final Integer startIndex, final Integer length, final byte[] expectedBytes)
                            {
                                runner.test("with " + (bytes == null ? "null byte[]" : ("byte[" + bytes.length + "]")) + " at " + startIndex + " for " + length + " length", new Action1<Test>()
                                {
                                    @Override
                                    public void run(Test test)
                                    {
                                        final byte[] clonedBytes = Array.clone(bytes, startIndex, length);
                                        test.assertEqual(expectedBytes, clonedBytes);
                                    }
                                });
                            }
                        };

                        test.run(null, -1, -2, null);
                        test.run(null, -1, 0, null);
                        test.run(null, -1, 2, null);
                        test.run(null, 0, -2, null);
                        test.run(null, 0, 0, null);
                        test.run(null, 0, 2, null);
                        test.run(null, 1, -2, null);
                        test.run(null, 1, 0, null);
                        test.run(null, 1, 2, null);
                        test.run(new byte[] { 0, 1, 2 }, 0, 3, new byte[] { 0, 1, 2 });
                        test.run(new byte[] { 0, 1, 2 }, 1, 1, new byte[] { 1 });
                    }
                });

                runner.testGroup("clone(char[])", new Action0()
                {
                    @Override
                    public void run()
                    {
                        final Action1<char[]> test = new Action1<char[]>()
                        {
                            @Override
                            public void run(final char[] characters)
                            {
                                runner.test("with " + (characters == null ? "null char[]" : ("char[" + characters.length + "]")), new Action1<Test>()
                                {
                                    @Override
                                    public void run(Test test)
                                    {
                                        final char[] clonedCharacters = Array.clone(characters);
                                        if (characters == null || characters.length == 0)
                                        {
                                            test.assertSame(characters, clonedCharacters);
                                        }
                                        else
                                        {
                                            test.assertEqual(characters, clonedCharacters);
                                            test.assertNotSame(characters, clonedCharacters);
                                        }
                                    }
                                });
                            }
                        };

                        test.run(null);
                        test.run(new char[0]);
                        test.run(new char[] { 'a' });
                        test.run(new char[] { 'a', 'b', 'c', 'd', 'e' });
                    }
                });
                
                runner.testGroup("clone(char[],int,int)", new Action0()
                {
                    @Override
                    public void run()
                    {
                        final Action4<char[],Integer,Integer,char[]> test = new Action4<char[], Integer, Integer, char[]>()
                        {
                            @Override
                            public void run(final char[] characters, final Integer startIndex, final Integer length, final char[] expectedCharacters)
                            {
                                runner.test("with " + (characters == null ? "null char[]" : ("char[" + characters.length + "]")) + " at " + startIndex + " for " + length + " length", new Action1<Test>()
                                {
                                    @Override
                                    public void run(Test test)
                                    {
                                        final char[] clonedCharacters = Array.clone(characters, startIndex, length);
                                        test.assertEqual(expectedCharacters, clonedCharacters);
                                    }
                                });
                            }
                        };

                        test.run(null, -1, -2, null);
                        test.run(null, -1, 0, null);
                        test.run(null, -1, 2, null);
                        test.run(null, 0, -2, null);
                        test.run(null, 0, 0, null);
                        test.run(null, 0, 2, null);
                        test.run(null, 1, -2, null);
                        test.run(null, 1, 0, null);
                        test.run(null, 1, 2, null);
                        test.run(new char[] { 'a', 'b', 'c' }, 0, 3, new char[] { 'a', 'b', 'c' });
                        test.run(new char[] { 'x', 'y', 'z' }, 1, 1, new char[] { 'y' });
                    }
                });

                runner.testGroup("copy(byte[],int,byte[],int,int)", new Action0()
                {
                    @Override
                    public void run()
                    {
                        final Action6<String,byte[],byte[],Integer,Integer,byte[]> test = new Action6<String,byte[], byte[], Integer, Integer, byte[]>()
                        {
                            @Override
                            public void run(String testName, final byte[] copyFrom, final byte[] copyTo, final Integer copyToStartIndex, final Integer length, final byte[] expectedBytes)
                            {
                                runner.test(testName, new Action1<Test>()
                                {
                                    @Override
                                    public void run(Test test)
                                    {
                                        final byte[] copyFromClone = Array.clone(copyFrom);

                                        Array.copy(copyFrom, 0, copyTo, copyToStartIndex, length);

                                        test.assertEqual(copyFromClone, copyFrom);
                                        test.assertEqual(expectedBytes, copyTo);
                                    }
                                });
                            }
                        };

                        test.run("with null copyFrom",
                            null,
                            new byte[] { 0, 11, 22, 33, 44 },
                            1,
                            2,
                            new byte[] { 0, 11, 22, 33, 44 });
                        test.run("with null copyTo",
                            new byte[] { 0, 1, 2, 3, 4 },
                            null,
                            1,
                            2,
                            null);
                        test.run("with negative copyToStartIndex and negative length",
                            new byte[] { 0, 1, 2, 3, 4 },
                            new byte[] { 5, 6, 7, 8, 9 },
                            -1,
                            -2,
                            new byte[] { 5, 6, 7, 8, 9 });
                        test.run("with negative copyToStartIndex and zero length",
                            new byte[] { 0, 1, 2, 3, 4 },
                            new byte[] { 5, 6, 7, 8, 9 },
                            -1,
                            0,
                            new byte[] { 5, 6, 7, 8, 9 });
                        test.run("with negative copyToStartIndex and positive length",
                            new byte[] { 0, 1, 2, 3, 4 },
                            new byte[] { 5, 6, 7, 8, 9 },
                            -1,
                            2,
                            new byte[] { 5, 6, 7, 8, 9 });
                        test.run("with zero copyToStartIndex and negative length",
                            new byte[] { 0, 1, 2, 3, 4 },
                            new byte[] { 5, 6, 7, 8, 9 },
                            0,
                            -2,
                            new byte[] { 5, 6, 7, 8, 9 });
                        test.run("with zero copyToStartIndex and zero length",
                            new byte[] { 0, 1, 2, 3, 4 },
                            new byte[] { 5, 6, 7, 8, 9 },
                            0,
                            0,
                            new byte[] { 5, 6, 7, 8, 9 });
                        test.run("with zero copyToStartIndex and positive length",
                            new byte[] { 0, 1, 2, 3, 4 },
                            new byte[] { 5, 6, 7, 8, 9 },
                            0,
                            3,
                            new byte[] { 0, 1, 2, 8, 9 });
                        test.run("with zero copyToStartIndex and positive greater than copyFrom and less than copyTo length",
                            new byte[] { 0, 1, 2 },
                            new byte[] { 5, 6, 7, 8, 9 },
                            0,
                            4,
                            new byte[] { 0, 1, 2, 8, 9 });
                        test.run("with zero copyToStartIndex and positive less than copyFrom and greater than copyTo length",
                            new byte[] { 0, 1, 2, 3, 4 },
                            new byte[] { 5, 6, 7 },
                            0,
                            4,
                            new byte[] { 0, 1, 2 });
                        test.run("with zero copyToStartIndex and positive greater than copyFrom and copyTo length",
                            new byte[] { 0, 1, 2, 3, 4 },
                            new byte[] { 5, 6, 7, 8, 9 },
                            0,
                            30,
                            new byte[] { 0, 1, 2, 3, 4 });
                        test.run("with positive copyToStartIndex and negative length",
                            new byte[] { 0, 1, 2, 3, 4 },
                            new byte[] { 5, 6, 7, 8, 9 },
                            1,
                            -2,
                            new byte[] {5, 6, 7, 8, 9 });
                        test.run("with positive copyToStartIndex and zero length",
                            new byte[] { 0, 1, 2, 3, 4 },
                            new byte[] { 5, 6, 7, 8, 9 },
                            1,
                            0,
                            new byte[] { 5, 6, 7, 8, 9 });
                        test.run("with positive copyToStartIndex and positive less than copyFrom and copyTo length",
                            new byte[] { 0, 1, 2, 3, 4 },
                            new byte[] { 5, 6, 7, 8, 9 },
                            1,
                            3,
                            new byte[] { 5, 0, 1, 2, 9 });
                        test.run("with positive copyToStartIndex and positive greater than copyFrom and less than copyTo length",
                            new byte[] { 0, 1, 2 },
                            new byte[] { 5, 6, 7, 8, 9 },
                            1,
                            4,
                            new byte[] { 5, 0, 1, 2, 9 });
                        test.run("with positive copyToStartIndex and positive less than copyFrom and greater than copyTo length",
                            new byte[] { 0, 1, 2, 3 ,4 },
                            new byte[] { 5, 6, 7 },
                            1,
                            4,
                            new byte[] { 5, 0, 1 });
                        test.run("with positive copyToStartIndex and positive greater than copyFrom and copyTo length",
                            new byte[] { 0, 1, 2, 3, 4 },
                            new byte[] { 5, 6, 7, 8, 9 },
                            1,
                            30,
                            new byte[] { 5, 0, 1, 2, 3 });
                    }
                });

                runner.testGroup("copy(char[],int,char[],int,int)", new Action0()
                {
                    @Override
                    public void run()
                    {
                        final Action5<String,char[],char[],Integer,char[]> test = new Action5<String, char[], char[], Integer, char[]>()
                        {
                            @Override
                            public void run(String testName, final char[] copyFrom, final char[] copyTo, final Integer length, final char[] expectedCharacters)
                            {
                                runner.test(testName, new Action1<Test>()
                                {
                                    @Override
                                    public void run(Test test)
                                    {
                                        Array.copy(copyFrom, 0, copyTo, 0, length);
                                        test.assertEqual(expectedCharacters, copyTo);
                                    }
                                });
                            }
                        };

                        test.run("with null copyFrom and null copyTo",
                            null,
                            null,
                            0,
                            null);
                        test.run("with empty copyFrom and empty copyTo",
                            new char[0],
                            new char[0],
                            0,
                            new char[0]);
                        test.run("with non-empty copyFrom, non-empty copyTo, and zero length",
                            new char[] { 'a', 'b', 'c' },
                            new char[] { '0', '1', '2' },
                            0,
                            new char[] { '0', '1', '2' });
                        test.run("with non-empty copyFrom, non-empty copyTo, and non-zero length",
                            new char[] { 'a', 'b', 'c' },
                            new char[] { '0', '1', '2' },
                            2,
                            new char[] { 'a', 'b', '2' });
                    }
                });
            }
        });
    }
}
