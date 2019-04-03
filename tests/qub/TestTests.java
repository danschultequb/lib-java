package qub;

public class TestTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(Test.class, () ->
        {
            runner.testGroup("constructor", () ->
            {
                runner.test("with null name", (Test test) ->
                {
                    test.assertThrows(() -> new Test(null, null, null, test.getProcess()), new PreConditionFailure("name cannot be null."));
                });

                runner.test("with empty name", (Test test) ->
                {
                    test.assertThrows(() -> new Test("", null, null, test.getProcess()), new PreConditionFailure("name cannot be empty."));
                });

                runner.test("with null process", (Test test) ->
                {
                    test.assertThrows(() -> new Test("my fake test", null, null, null), new PreConditionFailure("process cannot be null."));
                });

                runner.test("with non-empty name and non-null process", (Test test) ->
                {
                    final Test t = new Test("my fake test", null, null, test.getProcess());
                    test.assertEqual("my fake test", t.getName());
                    test.assertEqual("my fake test", t.getFullName());
                    test.assertNull(t.getParentTestGroup());
                    test.assertFalse(t.shouldSkip());
                    test.assertNull(t.getSkipMessage());
                    test.assertNotNull(t.getMainAsyncRunner());
                    test.assertNotNull(t.getParallelAsyncRunner());
                    test.assertNotNull(t.getNetwork());
                    test.assertNotNull(t.getFileSystem());
                    test.assertNotNull(t.getClock());
                    test.assertNotNull(t.getDisplays());
                });

                runner.test("with non-null test group parent", (Test test) ->
                {
                    final TestGroup tg = new TestGroup("my fake test group", null, null);
                    final Test t = new Test("my fake test", tg, null, test.getProcess());
                    test.assertEqual("my fake test", t.getName());
                    test.assertEqual("my fake test group my fake test", t.getFullName());
                    test.assertSame(tg, t.getParentTestGroup());
                    test.assertFalse(t.shouldSkip());
                    test.assertNull(t.getSkipMessage());
                    test.assertNotNull(t.getMainAsyncRunner());
                    test.assertNotNull(t.getParallelAsyncRunner());
                });

                runner.test("with non-null test group grandparent", (Test test) ->
                {
                    final TestGroup tg1 = new TestGroup("apples", null, null);
                    final TestGroup tg2 = new TestGroup("my fake test group", tg1, null);
                    final Test t = new Test("my fake test", tg2, null, test.getProcess());
                    test.assertEqual("my fake test", t.getName());
                    test.assertEqual("apples my fake test group my fake test", t.getFullName());
                    test.assertSame(tg2, t.getParentTestGroup());
                    test.assertFalse(t.shouldSkip());
                    test.assertNull(t.getSkipMessage());
                    test.assertNotNull(t.getMainAsyncRunner());
                    test.assertNotNull(t.getParallelAsyncRunner());
                });
            });

            runner.testGroup("isMatch()", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertTrue(test.matches(null));
                });

                runner.test("with pattern that doesn't match test name or full name", (Test test) ->
                {
                    final Test t = new Test("apples", null, null, test.getProcess());
                    test.assertFalse(t.matches(PathPattern.parse("bananas")));
                });

                runner.test("with pattern that isMatch test name", (Test test) ->
                {
                    final Test t = new Test("apples", null, null, test.getProcess());
                    test.assertTrue(t.matches(PathPattern.parse("apples")));
                });

                runner.test("with pattern that isMatch test full name", (Test test) ->
                {
                    final TestGroup tg = new TestGroup("apples and", null, null);
                    final Test t = new Test("bananas", tg, null, test.getProcess());
                    test.assertTrue(t.matches(PathPattern.parse("apples*bananas")));
                });
            });

            runner.testGroup("shouldSkip()", () ->
            {
                runner.test("with null skip and null parentTestGroup", (Test test) ->
                {
                    final Test t = new Test("abc", null, null, test.getProcess());
                    test.assertFalse(t.shouldSkip());
                });

                runner.test("with non-null skip with no arguments and null parentTestGroup", (Test test) ->
                {
                    final Test t = new Test("abc", null, runner.skip(), test.getProcess());
                    test.assertTrue(t.shouldSkip());
                });

                runner.test("with non-null skip with false and null parentTestGroup", (Test test) ->
                {
                    final Test t = new Test("abc", null, runner.skip(false), test.getProcess());
                    test.assertFalse(t.shouldSkip());
                });

                runner.test("with non-null skip with false and message and null parentTestGroup", (Test test) ->
                {
                    final Test t = new Test("abc", null, runner.skip(false, "xyz"), test.getProcess());
                    test.assertFalse(t.shouldSkip());
                });

                runner.test("with non-null skip with true and null parentTestGroup", (Test test) ->
                {
                    final Test t = new Test("abc", null, runner.skip(true), test.getProcess());
                    test.assertTrue(t.shouldSkip());
                });

                runner.test("with non-null skip with false and message and null parentTestGroup", (Test test) ->
                {
                    final Test t = new Test("abc", null, runner.skip(true, "xyz"), test.getProcess());
                    test.assertTrue(t.shouldSkip());
                });

                runner.test("with null skip and non-null parentTestGroup with null skip", (Test test) ->
                {
                    final TestGroup tg = new TestGroup("my", null, null);
                    final Test t = new Test("abc", tg, null, test.getProcess());
                    test.assertFalse(t.shouldSkip());
                });

                runner.test("with non-null skip with no arguments and non-null parentTestGroup with null skip", (Test test) ->
                {
                    final TestGroup tg = new TestGroup("my", null, null);
                    final Test t = new Test("abc", tg, runner.skip(), test.getProcess());
                    test.assertTrue(t.shouldSkip());
                });

                runner.test("with non-null skip with false and non-null parentTestGroup with null skip", (Test test) ->
                {
                    final TestGroup tg = new TestGroup("my", null, null);
                    final Test t = new Test("abc", tg, runner.skip(false), test.getProcess());
                    test.assertFalse(t.shouldSkip());
                });

                runner.test("with non-null skip with false and message and non-null parentTestGroup with null skip", (Test test) ->
                {
                    final TestGroup tg = new TestGroup("my", null, null);
                    final Test t = new Test("abc", tg, runner.skip(false, "xyz"), test.getProcess());
                    test.assertFalse(t.shouldSkip());
                });

                runner.test("with non-null skip with true and non-null parentTestGroup with null skip", (Test test) ->
                {
                    final TestGroup tg = new TestGroup("my", null, null);
                    final Test t = new Test("abc", tg, runner.skip(true), test.getProcess());
                    test.assertTrue(t.shouldSkip());
                });

                runner.test("with non-null skip with false and message and non-null parentTestGroup with null skip", (Test test) ->
                {
                    final TestGroup tg = new TestGroup("my", null, null);
                    final Test t = new Test("abc", tg, runner.skip(true, "xyz"), test.getProcess());
                    test.assertTrue(t.shouldSkip());
                });

                runner.test("with non-null skip with false and non-null parentTestGroup with non-null skip with true", (Test test) ->
                {
                    final TestGroup tg = new TestGroup("my", null, runner.skip(true));
                    final Test t = new Test("abc", tg, runner.skip(false), test.getProcess());
                    test.assertTrue(t.shouldSkip());
                });
            });

            runner.testGroup("getSkipMessage()", () ->
            {
                runner.test("with null skip and null parentTestGroup", (Test test) ->
                {
                    final Test t = new Test("abc", null, null, test.getProcess());
                    test.assertEqual(null, t.getSkipMessage());
                });

                runner.test("with non-null skip with no arguments and null parentTestGroup", (Test test) ->
                {
                    final Test t = new Test("abc", null, runner.skip(), test.getProcess());
                    test.assertEqual(null, t.getSkipMessage());
                });

                runner.test("with non-null skip with false and null parentTestGroup", (Test test) ->
                {
                    final Test t = new Test("abc", null, runner.skip(false), test.getProcess());
                    test.assertEqual(null, t.getSkipMessage());
                });

                runner.test("with non-null skip with false and message and null parentTestGroup", (Test test) ->
                {
                    final Test t = new Test("abc", null, runner.skip(false, "xyz"), test.getProcess());
                    test.assertEqual(null, t.getSkipMessage());
                });

                runner.test("with non-null skip with true and null parentTestGroup", (Test test) ->
                {
                    final Test t = new Test("abc", null, runner.skip(true), test.getProcess());
                    test.assertEqual(null, t.getSkipMessage());
                });

                runner.test("with non-null skip with true and message and null parentTestGroup", (Test test) ->
                {
                    final Test t = new Test("abc", null, runner.skip(true, "xyz"), test.getProcess());
                    test.assertEqual("xyz", t.getSkipMessage());
                });
            });

            runner.testGroup("writeLine()", () ->
            {
                runner.test("with null process.getOutputByteWriteStream()", (Test test) ->
                {
                    try (final Process p = new Process())
                    {
                        p.setOutputByteWriteStream((ByteWriteStream)null);
                        final Test t = new Test("abc", null, null, p);
                        test.assertThrows(() -> t.writeLine("Hello"),
                            new PreConditionFailure("process.getOutputByteWriteStream() cannot be null."));
                    }
                });

                runner.test("with null formattedText", (Test test) ->
                {
                    try (final Process p = new Process())
                    {
                        final InMemoryCharacterStream stdout = new InMemoryCharacterStream();
                        p.setOutputCharacterWriteStream(stdout);
                        final Test t = new Test("abc", null, null, p);
                        test.assertThrows(() -> t.writeLine(null), new PreConditionFailure("formattedText cannot be null."));
                    }
                });

                runner.test("with empty formattedText", (Test test) ->
                {
                    try (final Process p = new Process())
                    {
                        final InMemoryCharacterStream stdout = new InMemoryCharacterStream();
                        p.setOutputCharacterWriteStream(stdout);
                        final Test t = new Test("abc", null, null, p);
                        test.assertThrows(() -> t.writeLine(""), new PreConditionFailure("formattedText cannot be empty."));
                    }
                });

                runner.test("with non-empty formattedText", (Test test) ->
                {
                    try (final Process p = new Process())
                    {
                        final InMemoryCharacterStream stdout = new InMemoryCharacterStream();
                        p.setOutputCharacterWriteStream(stdout);
                        final Test t = new Test("abc", null, null, p);
                        t.writeLine("hello");
                        test.assertEqual("hello\n", stdout.getText().await());
                    }
                });
            });

            runner.testGroup("assertTrue(boolean)", () ->
            {
                runner.test("with false", (Test test) ->
                {
                    final Test t = createTest("abc", test);
                    test.assertThrows(() -> t.assertTrue(false),
                        new TestAssertionFailure("abc", new String[]
                            {
                                "Expected: true",
                                "Actual:   false"
                            }));
                });

                runner.test("with true", (Test test) ->
                {
                    test.assertTrue(true);
                });
            });

            runner.testGroup("assertTrue(boolean, String)", () ->
            {
                runner.test("with false and null", (Test test) ->
                {
                    final Test t = createTest("abc", test);
                    test.assertThrows(() -> t.assertTrue(false, null),
                        new TestAssertionFailure("abc", new String[]
                                                            {
                                                                "Expected: true",
                                                                "Actual:   false"
                                                            }));
                });

                runner.test("with false and empty", (Test test) ->
                {
                    final Test t = createTest("abc", test);
                    test.assertThrows(() -> t.assertTrue(false, ""),
                        new TestAssertionFailure("abc", new String[]
                                                            {
                                                                "Expected: true",
                                                                "Actual:   false"
                                                            }));
                });

                runner.test("with false and non-empty", (Test test) ->
                {
                    final Test t = createTest("abc", test);
                    test.assertThrows(() -> t.assertTrue(false, "blah"),
                        new TestAssertionFailure("abc", new String[]
                                                            {
                                                                "Message:  blah",
                                                                "Expected: true",
                                                                "Actual:   false"
                                                            }));
                });

                runner.test("with true and null", (Test test) ->
                {
                    test.assertTrue(true, null);
                });

                runner.test("with true and empty", (Test test) ->
                {
                    test.assertTrue(true, "");
                });

                runner.test("with true and non-empty", (Test test) ->
                {
                    test.assertTrue(true, "blah");
                });
            });

            runner.testGroup("assertFalse(boolean)", () ->
            {
                runner.test("with true", (Test test) ->
                {
                    final Test t = createTest("abc", test);
                    test.assertThrows(() -> t.assertFalse(true),
                        new TestAssertionFailure("abc", new String[]
                                                            {
                                                                "Expected: false",
                                                                "Actual:   true"
                                                            }));
                });

                runner.test("with false", (Test test) ->
                {
                    test.assertFalse(false);
                });
            });

            runner.testGroup("assertFalse(boolean, String)", () ->
            {
                runner.test("with true and null", (Test test) ->
                {
                    final Test t = createTest("abc", test);
                    test.assertThrows(() -> t.assertFalse(true, (String)null),
                        new TestAssertionFailure("abc", new String[]
                                                            {
                                                                "Expected: false",
                                                                "Actual:   true"
                                                            }));
                });

                runner.test("with true and empty", (Test test) ->
                {
                    final Test t = createTest("abc", test);
                    test.assertThrows(() -> t.assertFalse(true, ""),
                        new TestAssertionFailure("abc", new String[]
                                                            {
                                                                "Expected: false",
                                                                "Actual:   true"
                                                            }));
                });

                runner.test("with true and non-empty", (Test test) ->
                {
                    final Test t = createTest("abc", test);
                    test.assertThrows(() -> t.assertFalse(true, "blah"),
                        new TestAssertionFailure("abc", new String[]
                                                            {
                                                                "Message:  blah",
                                                                "Expected: false",
                                                                "Actual:   true"
                                                            }));
                });

                runner.test("with false and null", (Test test) ->
                {
                    test.assertFalse(false, (String)null);
                });

                runner.test("with false and empty", (Test test) ->
                {
                    test.assertFalse(false, "");
                });

                runner.test("with false and non-empty", (Test test) ->
                {
                    test.assertFalse(false, "blah");
                });
            });

            runner.testGroup("assertFalse(boolean, Function0<String>)", () ->
            {
                runner.test("with true and null", (Test test) ->
                {
                    final Test t = createTest("abc", test);
                    test.assertThrows(() -> t.assertFalse(true, (Function0<String>)null),
                        new TestAssertionFailure("abc", new String[]
                                                            {
                                                                "Expected: false",
                                                                "Actual:   true"
                                                            }));
                });

                runner.test("with true and function that returns null", (Test test) ->
                {
                    final Test t = createTest("abc", test);
                    test.assertThrows(() -> t.assertFalse(true, () -> null),
                        new TestAssertionFailure("abc", new String[]
                                                            {
                                                                "Expected: false",
                                                                "Actual:   true"
                                                            }));
                });

                runner.test("with true and function that returns empty", (Test test) ->
                {
                    final Test t = createTest("abc", test);
                    test.assertThrows(() -> t.assertFalse(true, () -> ""),
                        new TestAssertionFailure("abc", new String[]
                                                            {
                                                                "Expected: false",
                                                                "Actual:   true"
                                                            }));
                });

                runner.test("with true and function that returns non-empty", (Test test) ->
                {
                    final Test t = createTest("abc", test);
                    test.assertThrows(() -> t.assertFalse(true, () -> "blah"),
                        new TestAssertionFailure("abc", new String[]
                                                            {
                                                                "Message:  blah",
                                                                "Expected: false",
                                                                "Actual:   true"
                                                            }));
                });

                runner.test("with false and null", (Test test) ->
                {
                    test.assertFalse(false, (Function0<String>)null);
                });

                runner.test("with false and function that returns null", (Test test) ->
                {
                    test.assertFalse(false, () -> null);
                });

                runner.test("with false and empty", (Test test) ->
                {
                    test.assertFalse(false, "");
                });

                runner.test("with false and non-empty", (Test test) ->
                {
                    test.assertFalse(false, "blah");
                });
            });

            runner.testGroup("assertNull(Object)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertNull(null);
                });

                runner.test("with non-null", (Test test) ->
                {
                    final Test t = createTest("abc", test);
                    test.assertThrows(() -> t.assertNull("Hello"),
                        new TestAssertionFailure("abc", new String[]
                                                            {
                                                                "Expected: null",
                                                                "Actual:   \"Hello\""
                                                            }));
                });
            });

            runner.testGroup("assertNull(Object,String)", () ->
            {
                runner.test("with null and null message", (Test test) ->
                {
                    test.assertNull(null, null);
                });

                runner.test("with null and empty message", (Test test) ->
                {
                    test.assertNull(null, "");
                });

                runner.test("with null and non-empty message", (Test test) ->
                {
                    test.assertNull(null, "blah");
                });

                runner.test("with non-null and null message", (Test test) ->
                {
                    final Test t = createTest("abc", test);
                    test.assertThrows(() -> t.assertNull("Hello", null),
                        new TestAssertionFailure("abc", new String[]
                                                            {
                                                                "Expected: null",
                                                                "Actual:   \"Hello\""
                                                            }));
                });

                runner.test("with non-null and empty message", (Test test) ->
                {
                    final Test t = createTest("abc", test);
                    test.assertThrows(() -> t.assertNull("Hello", ""),
                        new TestAssertionFailure("abc", new String[]
                                                            {
                                                                "Expected: null",
                                                                "Actual:   \"Hello\""
                                                            }));
                });

                runner.test("with non-null and non-empty message", (Test test) ->
                {
                    final Test t = createTest("abc", test);
                    test.assertThrows(() -> t.assertNull("Hello", "blah"),
                        new TestAssertionFailure("abc", new String[]
                                                            {
                                                                "Message:  blah",
                                                                "Expected: null",
                                                                "Actual:   \"Hello\""
                                                            }));
                });
            });

            runner.testGroup("assertNotNull(Object)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final Test t = createTest("abc", test);
                    test.assertThrows(() -> t.assertNotNull(null),
                        new TestAssertionFailure("abc", new String[]
                                                            {
                                                                "Expected: \"not null\"",
                                                                "Actual:   null"
                                                            }));
                });

                runner.test("with non-null", (Test test) ->
                {
                    final Test t = createTest("abc", test);
                    t.assertNotNull("Hello");
                });
            });

            runner.testGroup("assertNotNull(Object,String)", () ->
            {
                runner.test("with null and null message", (Test test) ->
                {
                    final Test t = createTest("abc", test);
                    test.assertThrows(() -> t.assertNotNull(null),
                        new TestAssertionFailure("abc", new String[]
                                                            {
                                                                "Expected: \"not null\"",
                                                                "Actual:   null"
                                                            }));
                });

                runner.test("with null and empty message", (Test test) ->
                {
                    final Test t = createTest("abc", test);
                    test.assertThrows(() -> t.assertNotNull(null, ""),
                        new TestAssertionFailure("abc", new String[]
                                                            {
                                                                "Expected: \"not null\"",
                                                                "Actual:   null"
                                                            }));
                });

                runner.test("with null and non-empty message", (Test test) ->
                {
                    final Test t = createTest("abc", test);
                    test.assertThrows(() -> t.assertNotNull(null, "blah"),
                        new TestAssertionFailure("abc", new String[]
                                                            {
                                                                "Message:  blah",
                                                                "Expected: \"not null\"",
                                                                "Actual:   null"
                                                            }));
                });

                runner.test("with non-null and null message", (Test test) ->
                {
                    final Test t = createTest("abc", test);
                    t.assertNotNull("Hello", null);
                });

                runner.test("with non-null and empty message", (Test test) ->
                {
                    final Test t = createTest("abc", test);
                    t.assertNotNull("Hello", "");
                });

                runner.test("with non-null and non-empty message", (Test test) ->
                {
                    final Test t = createTest("abc", test);
                    t.assertNotNull("Hello", "blah");
                });
            });

            runner.testGroup("assertNotNullAndNotEmpty(String)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final Test t = createTest("abc", test);
                    test.assertThrows(() -> t.assertNotNullAndNotEmpty((String)null),
                        new TestAssertionFailure("abc", new String[]
                                                            {
                                                                "Expected: \"not null and not empty\"",
                                                                "Actual:   null"
                                                            }));
                });

                runner.test("with empty", (Test test) ->
                {
                    final Test t = createTest("abc", test);
                    test.assertThrows(() -> t.assertNotNullAndNotEmpty(""),
                        new TestAssertionFailure("abc", Iterable.create("Expected: \"not null and not empty\"", "Actual:   \"\"")));
                });

                runner.test("with non-empty", (Test test) ->
                {
                    final Test t = createTest("abc", test);
                    t.assertNotNullAndNotEmpty("Hello");
                });
            });

            runner.testGroup("assertEqual(Throwable,Throwable)", () ->
            {
                runner.test("with null and null", (Test test) ->
                {
                    final Test t = createTest("abc", test);
                    t.assertEqual((Throwable)null, (Throwable)null);
                });

                runner.test("with null and NullPointerException", (Test test) ->
                {
                    final Test t = createTest("abc", test);
                    test.assertThrows(() -> t.assertEqual(null, new NullPointerException("abc")),
                        new TestAssertionFailure("abc", new String[]
                        {
                            "Expected: null",
                            "Actual:   java.lang.NullPointerException: abc"
                        }));
                });

                runner.test("with NullPointerException and null", (Test test) ->
                {
                    final Test t = createTest("abc", test);
                    test.assertThrows(() -> t.assertEqual(new NullPointerException("abc"), null),
                        new TestAssertionFailure("abc", new String[]
                        {
                            "Expected: java.lang.NullPointerException: abc",
                            "Actual:   null"
                        }));
                });

                runner.test("with NullPointerException(a) and NullPointerException(b)", (Test test) ->
                {
                    final Test t = createTest("abc", test);
                    test.assertThrows(() -> t.assertEqual(new NullPointerException("a"), new NullPointerException("b")),
                        new TestAssertionFailure("abc", new String[]
                        {
                            "Expected: java.lang.NullPointerException: a",
                            "Actual:   java.lang.NullPointerException: b"
                        }));
                });

                runner.test("with NullPointerException(a) and NullPointerException(a)", (Test test) ->
                {
                    final Test t = createTest("abc", test);
                    t.assertEqual(new NullPointerException("a"), new NullPointerException("a"));
                });
            });

            runner.testGroup("assertThrows(Action0,Throwable)", () ->
            {
                runner.test("with null action", (Test test) ->
                {
                    final Test t = createTest("abc", test);
                    test.assertThrows(() -> t.assertThrows(null, new EndOfStreamException()),
                        new PreConditionFailure("action cannot be null."));
                });

                runner.test("with null error", (Test test) ->
                {
                    final Test t = createTest("abc", test);
                    test.assertThrows(() -> t.assertThrows(() -> {}, null),
                        new PreConditionFailure("expectedException cannot be null."));
                });

                runner.test("with action that doesn't throw and expected error with no message", (Test test) ->
                {
                    final Test t = createTest("abc", test);
                    test.assertThrows(() -> t.assertThrows(() -> {}, new EndOfStreamException()),
                        new TestAssertionFailure("abc", new String[] { "Expected a qub.EndOfStreamException to be thrown with no message." }));
                });

                runner.test("with action that doesn't throw and expected error with no message", (Test test) ->
                {
                    final Test t = createTest("abcd", test);
                    test.assertThrows(() -> t.assertThrows(() -> {}, new NotFoundException("blah")),
                        new TestAssertionFailure("abcd", new String[] { "Expected a qub.NotFoundException to be thrown with the message \"blah\"." }));
                });

                runner.test("with action that throws a different error", (Test test) ->
                {
                    final Test t = createTest("abcd", test);
                    test.assertThrows(() -> t.assertThrows(() -> { throw new NullPointerException(); }, new NotFoundException("blah")),
                        new TestAssertionFailure("abcd", new String[]
                            {
                                "Message:  Incorrect exception thrown",
                                "Expected: qub.NotFoundException: blah",
                                "Actual:   java.lang.NullPointerException"
                            }));
                });

                runner.test("with action that throws the same error", (Test test) ->
                {
                    test.assertThrows(() -> { throw new NotFoundException("blah"); }, new NotFoundException("blah"));
                });

                runner.test("with action that throws the same error with a different message", (Test test) ->
                {
                    final Test t = createTest("abcd", test);
                    test.assertThrows(() -> t.assertThrows(() -> { throw new NotFoundException("grapes"); }, new NotFoundException("blah")),
                        new TestAssertionFailure("abcd", new String[]
                            {
                                "Message:  Incorrect exception thrown",
                                "Expected: qub.NotFoundException: blah",
                                "Actual:   qub.NotFoundException: grapes"
                            }));
                });

                runner.test("with action that throws an error derived from the expected error", (Test test) ->
                {
                    final Test t = createTest("abcd", test);
                    test.assertThrows(() -> t.assertThrows(() -> { throw new NotFoundException("blah"); }, new RuntimeException("blah")),
                        new TestAssertionFailure("abcd", new String[]
                            {
                                "Message:  Incorrect exception thrown",
                                "Expected: java.lang.RuntimeException: blah",
                                "Actual:   qub.NotFoundException: blah"
                            }));
                });

                runner.test("with action that throws the same error but is wrapped in a RuntimeException", (Test test) ->
                {
                    test.assertThrows(() -> { throw new RuntimeException(new NotFoundException("blah")); }, new NotFoundException("blah"));
                });

                runner.test("with action that throws the same error but is wrapped in two RuntimeExceptions", (Test test) ->
                {
                    test.assertThrows(() -> { throw new RuntimeException(new RuntimeException(new NotFoundException("blah"))); }, new NotFoundException("blah"));
                });

                runner.test("with action that throws the same error with different message and is wrapped in a RuntimeException", (Test test) ->
                {
                    final Test t = createTest("abcd", test);
                    test.assertThrows(() -> t.assertThrows(() -> { throw new RuntimeException(new NotFoundException("grapes")); }, new NotFoundException("blah")),
                        new TestAssertionFailure("abcd", new String[]
                            {
                                "Message:  Incorrect exception thrown",
                                "Expected: qub.NotFoundException: blah",
                                "Actual:   java.lang.RuntimeException: qub.NotFoundException: grapes"
                            }));
                });

                runner.test("with action that throws the same error but is wrapped in an AwaitException", (Test test) ->
                {
                    test.assertThrows(() -> { throw new AwaitException(new NotFoundException("blah")); }, new NotFoundException("blah"));
                });

                runner.test("with action that throws the same error but is wrapped in two AwaitExceptions", (Test test) ->
                {
                    test.assertThrows(() -> { throw new AwaitException(new AwaitException(new NotFoundException("blah"))); }, new NotFoundException("blah"));
                });

                runner.test("with action that throws the same error but is wrapped in a RuntimeException and an AwaitException", (Test test) ->
                {
                    test.assertThrows(() -> { throw new RuntimeException(new AwaitException(new NotFoundException("blah"))); }, new NotFoundException("blah"));
                });

                runner.test("with action that throws the same error with different message and is wrapped in an AwaitException", (Test test) ->
                {
                    final Test t = createTest("abcd", test);
                    test.assertThrows(() -> t.assertThrows(() -> { throw new AwaitException(new NotFoundException("grapes")); }, new NotFoundException("blah")),
                        new TestAssertionFailure("abcd", new String[]
                            {
                                "Message:  Incorrect exception thrown",
                                "Expected: qub.NotFoundException: blah",
                                "Actual:   qub.AwaitException: qub.NotFoundException: grapes"
                            }));
                });
            });
        });
    }

    private static Test createTest(String testName, Test test)
    {
        return new Test(testName, null, null, test.getProcess());
    }
}
