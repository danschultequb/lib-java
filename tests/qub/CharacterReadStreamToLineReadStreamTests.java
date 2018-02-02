package qub;

public class CharacterReadStreamToLineReadStreamTests
{
    public static void test(final TestRunner runner)
    {
        runner.testGroup("CharacterReadStreamToLineReadStream", new Action0()
        {
            @Override
            public void run()
            {
                runner.test("constructor()", new Action1<Test>()
                {
                    @Override
                    public void run(Test test)
                    {
                        final InMemoryCharacterReadStream characterReadStream = new InMemoryCharacterReadStream();
                        final CharacterReadStreamToLineReadStream lineReadStream = new CharacterReadStreamToLineReadStream(characterReadStream);
                        assertLineReadStream(test, lineReadStream, true, false, null);
                        test.assertEqual(CharacterEncoding.UTF_8, lineReadStream.getCharacterEncoding());
                        test.assertFalse(lineReadStream.getIncludeNewLines());
                    }
                });
                
                runner.test("close()", new Action1<Test>()
                {
                    @Override
                    public void run(Test test)
                    {
                        final CharacterReadStreamToLineReadStream lineReadStream = getLineReadStream();
                        lineReadStream.close();
                        assertLineReadStream(test, lineReadStream, false, false, null);

                        lineReadStream.close();
                        assertLineReadStream(test, lineReadStream, false, false, null);
                    }
                });
                
                runner.testGroup("readLine()", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("with false includeNewLines", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final CharacterReadStreamToLineReadStream lineReadStream = getLineReadStream("a\nb\r\nc");

                                test.assertEqual("a", lineReadStream.readLine(false));
                                assertLineReadStream(test, lineReadStream, true, true, "a");

                                test.assertEqual("b", lineReadStream.readLine(false));
                                assertLineReadStream(test, lineReadStream, true, true, "b");

                                test.assertEqual("c", lineReadStream.readLine(false));
                                assertLineReadStream(test, lineReadStream, true, true, "c");

                                test.assertEqual(null, lineReadStream.readLine(false));
                                assertLineReadStream(test, lineReadStream, true, true, null);
                            }
                        });
                        
                        runner.test("with true includeNewLines", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final CharacterReadStreamToLineReadStream lineReadStream = getLineReadStream("a\nb\r\nc");

                                test.assertEqual("a\n", lineReadStream.readLine(true));
                                assertLineReadStream(test, lineReadStream, true, true, "a\n");

                                test.assertEqual("b\r\n", lineReadStream.readLine(true));
                                assertLineReadStream(test, lineReadStream, true, true, "b\r\n");

                                test.assertEqual("c", lineReadStream.readLine(true));
                                assertLineReadStream(test, lineReadStream, true, true, "c");

                                test.assertEqual(null, lineReadStream.readLine(true));
                                assertLineReadStream(test, lineReadStream, true, true, null);
                            }
                        });
                    }
                });
                
                runner.test("next() with true includeNewLines", new Action1<Test>()
                {
                    @Override
                    public void run(Test test)
                    {
                        final CharacterReadStreamToLineReadStream lineReadStream = getLineReadStream("a\nb\r\nc", true);

                        test.assertTrue(lineReadStream.next());
                        assertLineReadStream(test, lineReadStream, true, true, "a\n");

                        test.assertTrue(lineReadStream.next());
                        assertLineReadStream(test, lineReadStream, true, true, "b\r\n");

                        test.assertTrue(lineReadStream.next());
                        assertLineReadStream(test, lineReadStream, true, true, "c");

                        test.assertFalse(lineReadStream.next());
                        assertLineReadStream(test, lineReadStream, true, true, null);
                    }
                });
            }
        });
    }

    private static CharacterReadStreamToLineReadStream getLineReadStream()
    {
        return getLineReadStream("");
    }

    private static CharacterReadStreamToLineReadStream getLineReadStream(String text)
    {
        return getLineReadStream(text, false);
    }

    private static CharacterReadStreamToLineReadStream getLineReadStream(String text, boolean includeNewLines)
    {
        final InMemoryCharacterReadStream characterReadStream = new InMemoryCharacterReadStream(text);
        return new CharacterReadStreamToLineReadStream(characterReadStream, includeNewLines);
    }

    private static void assertLineReadStream(Test test, LineReadStream lineReadStream, boolean isOpen, boolean hasStarted, String current)
    {
        test.assertNotNull(lineReadStream);
        test.assertEqual(isOpen, lineReadStream.isOpen());
        test.assertEqual(hasStarted, lineReadStream.hasStarted());
        test.assertEqual(current != null, lineReadStream.hasCurrent());
        test.assertEqual(current, lineReadStream.getCurrent());
    }
}
