package qub;

public interface TestTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(Test.class, () ->
        {
            runner.testGroup("create(String,TestParent,Skip)", () ->
            {
                runner.test("with null name", (Test test) ->
                {
                    test.assertThrows(() -> Test.create(null, null, null),
                        new PreConditionFailure("name cannot be null."));
                });

                runner.test("with empty name", (Test test) ->
                {
                    test.assertThrows(() -> Test.create("", null, null),
                        new PreConditionFailure("name cannot be empty."));
                });

                runner.test("with non-empty name", (Test test) ->
                {
                    final Test t = Test.create("my fake test", null, null);
                    test.assertEqual("my fake test", t.getName());
                    test.assertEqual("my fake test", t.getFullName());
                    test.assertNull(t.getParent());
                    test.assertFalse(t.shouldSkip());
                    test.assertNull(t.getSkipMessage());
                });

                runner.test("with non-null test group parent", (Test test) ->
                {
                    final TestGroup tg = TestGroup.create("my fake test group", null, null);
                    final Test t = Test.create("my fake test", tg, null);
                    test.assertEqual("my fake test", t.getName());
                    test.assertEqual("my fake test group my fake test", t.getFullName());
                    test.assertSame(tg, t.getParent());
                    test.assertFalse(t.shouldSkip());
                    test.assertNull(t.getSkipMessage());
                });

                runner.test("with non-null test group grandparent", (Test test) ->
                {
                    final TestGroup tg1 = TestGroup.create("apples", null, null);
                    final TestGroup tg2 = TestGroup.create("my fake test group", tg1, null);
                    final Test t = Test.create("my fake test", tg2, null);
                    test.assertEqual("my fake test", t.getName());
                    test.assertEqual("apples my fake test group my fake test", t.getFullName());
                    test.assertSame(tg2, t.getParent());
                    test.assertFalse(t.shouldSkip());
                    test.assertNull(t.getSkipMessage());
                });
            });

            runner.testGroup("getFullName()", () ->
            {
                runner.test("with null parent", (Test test) ->
                {
                    final Test t = Test.create("apples", null, null);
                    test.assertEqual("apples", t.getFullName());
                });

                runner.test("with TestClassParent parent", (Test test) ->
                {
                    final TestClass parent = TestClass.create(TestTests.class);
                    final Test t = Test.create("apples", parent, null);
                    test.assertEqual("qub.TestTests apples", t.getFullName());
                });

                runner.test("with TestGroup parent with no grandparent", (Test test) ->
                {
                    final TestGroup parent = TestGroup.create("bananas", null, null);
                    final Test t = Test.create("apples", parent, null);
                    test.assertEqual("bananas apples", t.getFullName());
                });

                runner.test("with TestGroup parent with TestClass grandparent", (Test test) ->
                {
                    final TestClass grandparent = TestClass.create(TestTests.class);
                    final TestGroup parent = TestGroup.create("bananas", grandparent, null);
                    final Test t = Test.create("apples", parent, null);
                    test.assertEqual("qub.TestTests bananas apples", t.getFullName());
                });
            });

            runner.testGroup("getTestClass()", () ->
            {
                runner.test("with no parent", (Test test) ->
                {
                    final Test t = Test.create("apples", null, null);
                    test.assertNull(t.getTestClass());
                });

                runner.test("with no TestClass ancestor", (Test test) ->
                {
                    final TestGroup grandparent = TestGroup.create("grandparent", null, null);
                    final TestGroup parent = TestGroup.create("parent", grandparent, null);
                    final Test t = Test.create("apples", parent, null);
                    test.assertNull(t.getTestClass());
                });

                runner.test("with TestClass parent", (Test test) ->
                {
                    final TestClass parent = TestClass.create(TestTests.class);
                    final Test t = Test.create("apples", parent, null);
                    test.assertSame(parent, t.getTestClass());
                });

                runner.test("with TestClass grandparent", (Test test) ->
                {
                    final TestClass grandparent = TestClass.create(TestTests.class);
                    final TestGroup parent = TestGroup.create("parent", grandparent, null);
                    final Test t = Test.create("apples", parent, null);
                    test.assertSame(grandparent, t.getTestClass());
                });
            });

            runner.testGroup("matches()", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertTrue(test.matches(null));
                });

                runner.test("with pattern that doesn't match test name or full name", (Test test) ->
                {
                    final Test t = Test.create("apples", null, null);
                    test.assertFalse(t.matches(PathPattern.parse("bananas")));
                });

                runner.test("with pattern that matches test name", (Test test) ->
                {
                    final Test t = Test.create("apples", null, null);
                    test.assertTrue(t.matches(PathPattern.parse("apples")));
                });

                runner.test("with pattern that matches test full name", (Test test) ->
                {
                    final TestGroup tg = TestGroup.create("apples and", null, null);
                    final Test t = Test.create("bananas", tg, null);
                    test.assertTrue(t.matches(PathPattern.parse("apples*bananas")));
                });

                runner.test("with pattern that matches parent name", (Test test) ->
                {
                    final TestGroup tg = TestGroup.create("apples and", null, null);
                    final Test t = Test.create("bananas", tg, null);
                    test.assertTrue(t.matches(PathPattern.parse("apples*d")));
                });

                runner.test("with pattern that matches parent full name", (Test test) ->
                {
                    final TestGroup tg2 = TestGroup.create("oranges and ", null, null);
                    final TestGroup tg = TestGroup.create("apples and", tg2, null);
                    final Test t = Test.create("bananas", tg, null);
                    test.assertTrue(t.matches(PathPattern.parse("oranges*apples*d")));
                });

                runner.test("with pattern that doesn't match parent", (Test test) ->
                {
                    final TestGroup tg2 = TestGroup.create("oranges and ", null, null);
                    final TestGroup tg = TestGroup.create("apples and", tg2, null);
                    final Test t = Test.create("bananas", tg, null);
                    test.assertFalse(t.matches(PathPattern.parse("spando")));
                });
            });

            runner.testGroup("shouldSkip()", () ->
            {
                runner.test("with null skip and null parentTestGroup", (Test test) ->
                {
                    final Test t = Test.create("abc", null, null);
                    test.assertFalse(t.shouldSkip());
                });

                runner.test("with non-null skip with no arguments and null parentTestGroup", (Test test) ->
                {
                    final Test t = Test.create("abc", null, runner.skip());
                    test.assertTrue(t.shouldSkip());
                });

                runner.test("with non-null skip with false and null parentTestGroup", (Test test) ->
                {
                    final Test t = Test.create("abc", null, runner.skip(false));
                    test.assertFalse(t.shouldSkip());
                });

                runner.test("with non-null skip with false and message and null parentTestGroup", (Test test) ->
                {
                    final Test t = Test.create("abc", null, runner.skip(false, "xyz"));
                    test.assertFalse(t.shouldSkip());
                });

                runner.test("with non-null skip with true and null parentTestGroup", (Test test) ->
                {
                    final Test t = Test.create("abc", null, runner.skip(true));
                    test.assertTrue(t.shouldSkip());
                });

                runner.test("with non-null skip with false and message and null parentTestGroup", (Test test) ->
                {
                    final Test t = Test.create("abc", null, runner.skip(true, "xyz"));
                    test.assertTrue(t.shouldSkip());
                });

                runner.test("with null skip and non-null parentTestGroup with null skip", (Test test) ->
                {
                    final TestGroup tg = TestGroup.create("my", null, null);
                    final Test t = Test.create("abc", tg, null);
                    test.assertFalse(t.shouldSkip());
                });

                runner.test("with non-null skip with no arguments and non-null parentTestGroup with null skip", (Test test) ->
                {
                    final TestGroup tg = TestGroup.create("my", null, null);
                    final Test t = Test.create("abc", tg, runner.skip());
                    test.assertTrue(t.shouldSkip());
                });

                runner.test("with non-null skip with false and non-null parentTestGroup with null skip", (Test test) ->
                {
                    final TestGroup tg = TestGroup.create("my", null, null);
                    final Test t = Test.create("abc", tg, runner.skip(false));
                    test.assertFalse(t.shouldSkip());
                });

                runner.test("with non-null skip with false and message and non-null parentTestGroup with null skip", (Test test) ->
                {
                    final TestGroup tg = TestGroup.create("my", null, null);
                    final Test t = Test.create("abc", tg, runner.skip(false, "xyz"));
                    test.assertFalse(t.shouldSkip());
                });

                runner.test("with non-null skip with true and non-null parentTestGroup with null skip", (Test test) ->
                {
                    final TestGroup tg = TestGroup.create("my", null, null);
                    final Test t = Test.create("abc", tg, runner.skip(true));
                    test.assertTrue(t.shouldSkip());
                });

                runner.test("with non-null skip with false and message and non-null parentTestGroup with null skip", (Test test) ->
                {
                    final TestGroup tg = TestGroup.create("my", null, null);
                    final Test t = Test.create("abc", tg, runner.skip(true, "xyz"));
                    test.assertTrue(t.shouldSkip());
                });

                runner.test("with non-null skip with false and non-null parentTestGroup with non-null skip with true", (Test test) ->
                {
                    final TestGroup tg = TestGroup.create("my", null, runner.skip(true));
                    final Test t = Test.create("abc", tg, runner.skip(false));
                    test.assertTrue(t.shouldSkip());
                });
            });

            runner.testGroup("getSkipMessage()", () ->
            {
                runner.test("with null skip and null parentTestGroup", (Test test) ->
                {
                    final Test t = Test.create("abc", null, null);
                    test.assertEqual(null, t.getSkipMessage());
                });

                runner.test("with non-null skip with no arguments and null parentTestGroup", (Test test) ->
                {
                    final Test t = Test.create("abc", null, runner.skip());
                    test.assertEqual("", t.getSkipMessage());
                });

                runner.test("with non-null skip with false and null parentTestGroup", (Test test) ->
                {
                    final Test t = Test.create("abc", null, runner.skip(false));
                    test.assertEqual(null, t.getSkipMessage());
                });

                runner.test("with non-null skip with false and message and null parentTestGroup", (Test test) ->
                {
                    final Test t = Test.create("abc", null, runner.skip(false, "xyz"));
                    test.assertEqual(null, t.getSkipMessage());
                });

                runner.test("with non-null skip with true and null parentTestGroup", (Test test) ->
                {
                    final Test t = Test.create("abc", null, runner.skip(true));
                    test.assertEqual("", t.getSkipMessage());
                });

                runner.test("with non-null skip with true and message and null parentTestGroup", (Test test) ->
                {
                    final Test t = Test.create("abc", null, runner.skip(true, "xyz"));
                    test.assertEqual("xyz", t.getSkipMessage());
                });
            });

            runner.testGroup("assertTrue(boolean)", () ->
            {
                runner.test("with false", (Test test) ->
                {
                    final Test t = TestTests.createTest("abc");
                    test.assertThrows(() -> t.assertTrue(false),
                        new TestError(
                            "abc",
                            TestTests.createErrorMessage(
                                "Expected: true",
                                "Actual:   false")));
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
                    final Test t = TestTests.createTest("abc");
                    test.assertThrows(() -> t.assertTrue(false, null),
                        new TestError(
                            "abc",
                            TestTests.createErrorMessage(
                                "Expected: true",
                                "Actual:   false")));
                });

                runner.test("with false and empty", (Test test) ->
                {
                    final Test t = TestTests.createTest("abc");
                    test.assertThrows(() -> t.assertTrue(false, ""),
                        new TestError(
                            "abc",
                            TestTests.createErrorMessage(
                                "Expected: true",
                                "Actual:   false")));
                });

                runner.test("with false and non-empty", (Test test) ->
                {
                    final Test t = TestTests.createTest("abc");
                    test.assertThrows(() -> t.assertTrue(false, "blah"),
                        new TestError(
                            "abc",
                            TestTests.createErrorMessage(
                                "Message:  blah",
                                "Expected: true",
                                "Actual:   false")));
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
                    final Test t = TestTests.createTest("abc");
                    test.assertThrows(() -> t.assertFalse(true),
                        new TestError("abc", TestTests.createErrorMessage(
                            "Expected: false",
                            "Actual:   true")));
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
                    final Test t = TestTests.createTest("abc");
                    test.assertThrows(() -> t.assertFalse(true, (String)null),
                        new TestError("abc", TestTests.createErrorMessage(
                            "Expected: false",
                            "Actual:   true")));
                });

                runner.test("with true and empty", (Test test) ->
                {
                    final Test t = TestTests.createTest("abc");
                    test.assertThrows(() -> t.assertFalse(true, ""),
                        new TestError("abc", TestTests.createErrorMessage(
                            "Expected: false",
                            "Actual:   true")));
                });

                runner.test("with true and non-empty", (Test test) ->
                {
                    final Test t = TestTests.createTest("abc");
                    test.assertThrows(() -> t.assertFalse(true, "blah"),
                        new TestError("abc", TestTests.createErrorMessage(
                            "Message:  blah",
                            "Expected: false",
                            "Actual:   true")));
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
                    final Test t = TestTests.createTest("abc");
                    test.assertThrows(() -> t.assertFalse(true, (Function0<String>)null),
                        new TestError("abc", TestTests.createErrorMessage(
                            "Expected: false",
                            "Actual:   true")));
                });

                runner.test("with true and function that returns null", (Test test) ->
                {
                    final Test t = TestTests.createTest("abc");
                    test.assertThrows(() -> t.assertFalse(true, () -> null),
                        new TestError("abc", TestTests.createErrorMessage(
                            "Expected: false",
                            "Actual:   true")));
                });

                runner.test("with true and function that returns empty", (Test test) ->
                {
                    final Test t = TestTests.createTest("abc");
                    test.assertThrows(() -> t.assertFalse(true, () -> ""),
                        new TestError("abc", TestTests.createErrorMessage(
                            "Expected: false",
                            "Actual:   true")));
                });

                runner.test("with true and function that returns non-empty", (Test test) ->
                {
                    final Test t = TestTests.createTest("abc");
                    test.assertThrows(() -> t.assertFalse(true, () -> "blah"),
                        new TestError("abc", TestTests.createErrorMessage(
                            "Message:  blah",
                            "Expected: false",
                            "Actual:   true")));
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
                    final Test t = TestTests.createTest("abc");
                    test.assertThrows(() -> t.assertNull("Hello"),
                        new TestError("abc", TestTests.createErrorMessage(
                            "Expected: null",
                            "Actual:   \"Hello\"")));
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
                    final Test t = TestTests.createTest("abc");
                    test.assertThrows(() -> t.assertNull("Hello", null),
                        new TestError("abc", TestTests.createErrorMessage(
                            "Expected: null",
                            "Actual:   \"Hello\"")));
                });

                runner.test("with non-null and empty message", (Test test) ->
                {
                    final Test t = TestTests.createTest("abc");
                    test.assertThrows(() -> t.assertNull("Hello", ""),
                        new TestError("abc", TestTests.createErrorMessage(
                            "Expected: null",
                            "Actual:   \"Hello\"")));
                });

                runner.test("with non-null and non-empty message", (Test test) ->
                {
                    final Test t = TestTests.createTest("abc");
                    test.assertThrows(() -> t.assertNull("Hello", "blah"),
                        new TestError("abc", TestTests.createErrorMessage(
                            "Message:  blah",
                            "Expected: null",
                            "Actual:   \"Hello\"")));
                });
            });

            runner.testGroup("assertNotNull(Object)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final Test t = TestTests.createTest("abc");
                    test.assertThrows(() -> t.assertNotNull(null),
                        new TestError("abc", TestTests.createErrorMessage(
                            "Expected: \"not null\"",
                            "Actual:   null")));
                });

                runner.test("with non-null", (Test test) ->
                {
                    final Test t = TestTests.createTest("abc");
                    t.assertNotNull("Hello");
                });
            });

            runner.testGroup("assertNotNull(Object,String)", () ->
            {
                runner.test("with null and null message", (Test test) ->
                {
                    final Test t = TestTests.createTest("abc");
                    test.assertThrows(() -> t.assertNotNull(null),
                        new TestError("abc", TestTests.createErrorMessage(
                            "Expected: \"not null\"",
                            "Actual:   null")));
                });

                runner.test("with null and empty message", (Test test) ->
                {
                    final Test t = TestTests.createTest("abc");
                    test.assertThrows(() -> t.assertNotNull(null, ""),
                        new TestError("abc", TestTests.createErrorMessage(
                            "Expected: \"not null\"",
                            "Actual:   null")));
                });

                runner.test("with null and non-empty message", (Test test) ->
                {
                    final Test t = TestTests.createTest("abc");
                    test.assertThrows(() -> t.assertNotNull(null, "blah"),
                        new TestError("abc", TestTests.createErrorMessage(
                            "Message:  blah",
                            "Expected: \"not null\"",
                            "Actual:   null")));
                });

                runner.test("with non-null and null message", (Test test) ->
                {
                    final Test t = TestTests.createTest("abc");
                    t.assertNotNull("Hello", null);
                });

                runner.test("with non-null and empty message", (Test test) ->
                {
                    final Test t = TestTests.createTest("abc");
                    t.assertNotNull("Hello", "");
                });

                runner.test("with non-null and non-empty message", (Test test) ->
                {
                    final Test t = TestTests.createTest("abc");
                    t.assertNotNull("Hello", "blah");
                });
            });

            runner.testGroup("assertNotNullAndNotEmpty(String)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final Test t = TestTests.createTest("abc");
                    test.assertThrows(() -> t.assertNotNullAndNotEmpty((String)null),
                        new TestError("abc", TestTests.createErrorMessage(
                            "Expected: \"not null and not empty\"",
                            "Actual:   null")));
                });

                runner.test("with empty", (Test test) ->
                {
                    final Test t = TestTests.createTest("abc");
                    test.assertThrows(() -> t.assertNotNullAndNotEmpty(""),
                        new TestError("abc", TestTests.createErrorMessage(
                            "Expected: \"not null and not empty\"",
                            "Actual:   \"\"")));
                });

                runner.test("with non-empty", (Test test) ->
                {
                    final Test t = TestTests.createTest("abc");
                    t.assertNotNullAndNotEmpty("Hello");
                });
            });

            runner.testGroup("assertNotNullAndNotEmpty(String,String)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final Test t = TestTests.createTest("abc");
                    test.assertThrows(() -> t.assertNotNullAndNotEmpty((String)null, "hello"),
                        new TestError("abc", TestTests.createErrorMessage(
                            "Message:  hello",
                            "Expected: \"not null and not empty\"",
                            "Actual:   null")));
                });

                runner.test("with empty", (Test test) ->
                {
                    final Test t = TestTests.createTest("abc");
                    test.assertThrows(() -> t.assertNotNullAndNotEmpty("", "hello"),
                        new TestError("abc", TestTests.createErrorMessage(
                            "Message:  hello",
                            "Expected: \"not null and not empty\"",
                            "Actual:   \"\"")));
                });

                runner.test("with non-empty", (Test test) ->
                {
                    final Test t = TestTests.createTest("abc");
                    t.assertNotNullAndNotEmpty("Hello", "there");
                });
            });

            runner.testGroup("assertNotNullAndNotEmpty(Iterable<T>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final Test t = TestTests.createTest("abc");
                    test.assertThrows(() -> t.assertNotNullAndNotEmpty((Iterable<Integer>)null),
                        new TestError("abc", TestTests.createErrorMessage(
                            "Expected: \"not null\"",
                            "Actual:   null")));
                });

                runner.test("with empty", (Test test) ->
                {
                    final Test t = TestTests.createTest("abc");
                    test.assertThrows(() -> t.assertNotNullAndNotEmpty(Iterable.create()),
                        new TestError("abc", TestTests.createErrorMessage(
                            "Expected: \"not null and not empty\"",
                            "Actual:   []")));
                });

                runner.test("with non-empty", (Test test) ->
                {
                    final Test t = TestTests.createTest("abc");
                    t.assertNotNullAndNotEmpty(Iterable.create(1, 2, 3));
                });
            });

            runner.testGroup("assertNotNullAndNotEmpty(Iterable<T>,String)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final Test t = TestTests.createTest("abc");
                    test.assertThrows(() -> t.assertNotNullAndNotEmpty((Iterable<Integer>)null, "hello"),
                        new TestError("abc", TestTests.createErrorMessage(
                            "Message:  hello",
                            "Expected: \"not null\"",
                            "Actual:   null")));
                });

                runner.test("with empty", (Test test) ->
                {
                    final Test t = TestTests.createTest("abc");
                    test.assertThrows(() -> t.assertNotNullAndNotEmpty(Iterable.create(), "hello"),
                        new TestError("abc", TestTests.createErrorMessage(
                            "Message:  hello",
                            "Expected: \"not null and not empty\"",
                            "Actual:   []")));
                });

                runner.test("with non-empty", (Test test) ->
                {
                    final Test t = TestTests.createTest("abc");
                    t.assertNotNullAndNotEmpty(Iterable.create("Hello"), "there");
                });
            });

            runner.testGroup("assertEqual(T,T)", () ->
            {
                runner.test("with null and null", (Test test) ->
                {
                    final Test t = TestTests.createTest("abc");
                    t.assertEqual((Integer)null, (Integer)null);
                });

                runner.test("with null and not-null", (Test test) ->
                {
                    final Test t = TestTests.createTest("abc");
                    test.assertThrows(() -> t.assertEqual(null, "hello"),
                        new TestError("abc", TestTests.createErrorMessage(
                            "Expected: null",
                            "Actual:   \"hello\"")));
                });

                runner.test("with not-null and null", (Test test) ->
                {
                    final Test t = TestTests.createTest("abc");
                    test.assertThrows(() -> t.assertEqual("there", null),
                        new TestError("abc", TestTests.createErrorMessage(
                            "Expected: \"there\"",
                            "Actual:   null")));
                });

                runner.test("with same object", (Test test) ->
                {
                    final Test t = TestTests.createTest("abc");
                    final String value = "fifty";
                    t.assertEqual(value, value);
                });

                runner.test("with equal objects", (Test test) ->
                {
                    final Test t = TestTests.createTest("abc");
                    t.assertEqual(Distance.meters(1), Distance.centimeters(100));
                });

                runner.test("with not equal objects", (Test test) ->
                {
                    final Test t = TestTests.createTest("abc");
                    test.assertThrows(() -> t.assertEqual(Distance.meters(10), Distance.centimeters(100)),
                        new TestError("abc", TestTests.createErrorMessage(
                            "Expected: 10.0 Meters",
                            "Actual:   100.0 Centimeters")));
                });
            });

            runner.testGroup("assertEqual(byte,Byte)", () ->
            {
                final Action3<Byte,Byte,TestError> assertEqualTest = (Byte lhs, Byte rhs, TestError expectedError) ->
                {
                    runner.test("with " + lhs + " and " + Objects.toString(rhs), (Test test) ->
                    {
                        final Test t = TestTests.createTest("abc");
                        if (expectedError == null)
                        {
                            t.assertEqual(lhs.byteValue(), rhs);
                        }
                        else
                        {
                            test.assertThrows(() -> t.assertEqual(lhs.byteValue(), rhs), expectedError);
                        }
                    });
                };

                assertEqualTest.run((byte)1, null, new TestError("abc", TestTests.createErrorMessage(
                    "Expected: 1",
                    "Actual:   null")));
                assertEqualTest.run((byte)1, (byte)2, new TestError("abc", TestTests.createErrorMessage(
                    "Expected: 1",
                    "Actual:   2")));
                assertEqualTest.run((byte)1, (byte)1, null);
            });

            runner.testGroup("assertEqual(int,Byte)", () ->
            {
                final Action3<Integer,Byte,TestError> assertEqualTest = (Integer lhs, Byte rhs, TestError expectedError) ->
                {
                    runner.test("with " + lhs + " and " + Objects.toString(rhs), (Test test) ->
                    {
                        final Test t = TestTests.createTest("abc");
                        if (expectedError == null)
                        {
                            t.assertEqual(lhs.intValue(), rhs);
                        }
                        else
                        {
                            test.assertThrows(() -> t.assertEqual(lhs.intValue(), rhs), expectedError);
                        }
                    });
                };

                assertEqualTest.run(1, null, new TestError("abc", TestTests.createErrorMessage(
                    "Expected: 1",
                    "Actual:   null")));
                assertEqualTest.run(1, (byte)2, new TestError("abc", TestTests.createErrorMessage(
                    "Expected: 1",
                    "Actual:   2")));
                assertEqualTest.run(1, (byte)1, null);
            });

            runner.testGroup("assertEqual(char,Character)", () ->
            {
                final Action3<Character,Character,TestError> assertEqualTest = (Character lhs, Character rhs, TestError expectedError) ->
                {
                    runner.test("with " + lhs + " and " + Objects.toString(rhs), (Test test) ->
                    {
                        final Test t = TestTests.createTest("abc");
                        if (expectedError == null)
                        {
                            t.assertEqual(lhs.charValue(), rhs);
                        }
                        else
                        {
                            test.assertThrows(() -> t.assertEqual(lhs.charValue(), rhs), expectedError);
                        }
                    });
                };

                assertEqualTest.run('a', null, new TestError("abc", TestTests.createErrorMessage(
                    "Expected: a",
                    "Actual:   null")));
                assertEqualTest.run('a', 'b', new TestError("abc", TestTests.createErrorMessage(
                    "Expected: a",
                    "Actual:   b")));
                assertEqualTest.run('a', 'a', null);
            });

            runner.testGroup("assertEqual(Character,char)", () ->
            {
                final Action3<Character,Character,TestError> assertEqualTest = (Character lhs, Character rhs, TestError expectedError) ->
                {
                    runner.test("with " + lhs + " and " + Objects.toString(rhs), (Test test) ->
                    {
                        final Test t = TestTests.createTest("abc");
                        if (expectedError == null)
                        {
                            t.assertEqual(lhs, rhs.charValue());
                        }
                        else
                        {
                            test.assertThrows(() -> t.assertEqual(lhs, rhs.charValue()), expectedError);
                        }
                    });
                };

                assertEqualTest.run(null, 'a', new TestError("abc", TestTests.createErrorMessage(
                    "Expected: null",
                    "Actual:   a")));
                assertEqualTest.run('a', 'b', new TestError("abc", TestTests.createErrorMessage(
                    "Expected: a",
                    "Actual:   b")));
                assertEqualTest.run('a', 'a', null);
            });

            runner.testGroup("assertEqual(short,short)", () ->
            {
                final Action3<Short,Short,TestError> assertEqualTest = (Short lhs, Short rhs, TestError expectedError) ->
                {
                    runner.test("with " + lhs + " and " + Objects.toString(rhs), (Test test) ->
                    {
                        final Test t = TestTests.createTest("abc");
                        if (expectedError == null)
                        {
                            t.assertEqual(lhs.shortValue(), rhs.shortValue());
                        }
                        else
                        {
                            test.assertThrows(() -> t.assertEqual(lhs.shortValue(), rhs.shortValue()), expectedError);
                        }
                    });
                };

                assertEqualTest.run((short)1, (short)2, new TestError("abc", TestTests.createErrorMessage(
                    "Expected: 1",
                    "Actual:   2")));
                assertEqualTest.run((short)1, (short)1, null);
            });

            runner.testGroup("assertEqual(short,Short)", () ->
            {
                final Action3<Short,Short,TestError> assertEqualTest = (Short lhs, Short rhs, TestError expectedError) ->
                {
                    runner.test("with " + lhs + " and " + Objects.toString(rhs), (Test test) ->
                    {
                        final Test t = TestTests.createTest("abc");
                        if (expectedError == null)
                        {
                            t.assertEqual(lhs.shortValue(), rhs);
                        }
                        else
                        {
                            test.assertThrows(() -> t.assertEqual(lhs.shortValue(), rhs), expectedError);
                        }
                    });
                };

                assertEqualTest.run((short)1, null, new TestError("abc", TestTests.createErrorMessage(
                    "Expected: 1",
                    "Actual:   null")));
                assertEqualTest.run((short)1, (short)2, new TestError("abc", TestTests.createErrorMessage(
                    "Expected: 1",
                    "Actual:   2")));
                assertEqualTest.run((short)1, (short)1, null);
            });

            runner.testGroup("assertEqual(Short,short)", () ->
            {
                final Action3<Short,Short,TestError> assertEqualTest = (Short lhs, Short rhs, TestError expectedError) ->
                {
                    runner.test("with " + lhs + " and " + Objects.toString(rhs), (Test test) ->
                    {
                        final Test t = TestTests.createTest("abc");
                        if (expectedError == null)
                        {
                            t.assertEqual(lhs, rhs.shortValue());
                        }
                        else
                        {
                            test.assertThrows(() -> t.assertEqual(lhs, rhs.shortValue()), expectedError);
                        }
                    });
                };

                assertEqualTest.run(null, (short)2, new TestError("abc", TestTests.createErrorMessage(
                    "Expected: null",
                    "Actual:   2")));
                assertEqualTest.run((short)1, (short)2, new TestError("abc", TestTests.createErrorMessage(
                    "Expected: 1",
                    "Actual:   2")));
                assertEqualTest.run((short)1, (short)1, null);
            });

            runner.testGroup("assertEqual(int,int)", () ->
            {
                final Action3<Integer,Integer,TestError> assertEqualTest = (Integer lhs, Integer rhs, TestError expectedError) ->
                {
                    runner.test("with " + lhs + " and " + rhs, (Test test) ->
                    {
                        final Test t = TestTests.createTest("abc");
                        if (expectedError == null)
                        {
                            t.assertEqual(lhs.intValue(), rhs.intValue());
                        }
                        else
                        {
                            test.assertThrows(() -> t.assertEqual(lhs.intValue(), rhs.intValue()), expectedError);
                        }
                    });
                };

                assertEqualTest.run(1, 2, new TestError("abc", TestTests.createErrorMessage(
                    "Expected: 1",
                    "Actual:   2")));
                assertEqualTest.run(1, 1, null);
            });

            runner.testGroup("assertEqual(int,Integer)", () ->
            {
                final Action3<Integer,Integer,TestError> assertEqualTest = (Integer lhs, Integer rhs, TestError expectedError) ->
                {
                    runner.test("with " + lhs + " and " + Objects.toString(rhs), (Test test) ->
                    {
                        final Test t = TestTests.createTest("abc");
                        if (expectedError == null)
                        {
                            t.assertEqual(lhs.intValue(), rhs);
                        }
                        else
                        {
                            test.assertThrows(() -> t.assertEqual(lhs.intValue(), rhs), expectedError);
                        }
                    });
                };

                assertEqualTest.run(1, null, new TestError("abc", TestTests.createErrorMessage(
                    "Expected: 1",
                    "Actual:   null")));
                assertEqualTest.run(1, 2, new TestError("abc", TestTests.createErrorMessage(
                    "Expected: 1",
                    "Actual:   2")));
                assertEqualTest.run(1, 1, null);
            });

            runner.testGroup("assertEqual(Integer,int)", () ->
            {
                final Action3<Integer,Integer,TestError> assertEqualTest = (Integer lhs, Integer rhs, TestError expectedError) ->
                {
                    runner.test("with " + Objects.toString(lhs) + " and " + rhs, (Test test) ->
                    {
                        final Test t = TestTests.createTest("abc");
                        if (expectedError == null)
                        {
                            t.assertEqual(lhs, rhs.intValue());
                        }
                        else
                        {
                            test.assertThrows(() -> t.assertEqual(lhs, rhs.intValue()), expectedError);
                        }
                    });
                };

                assertEqualTest.run(null, 2, new TestError("abc", TestTests.createErrorMessage(
                    "Expected: null",
                    "Actual:   2")));
                assertEqualTest.run(1, 2, new TestError("abc", TestTests.createErrorMessage(
                    "Expected: 1",
                    "Actual:   2")));
                assertEqualTest.run(1, 1, null);
            });

            runner.testGroup("assertEqual(long,long)", () ->
            {
                final Action3<Long,Long,TestError> assertEqualTest = (Long lhs, Long rhs, TestError expectedError) ->
                {
                    runner.test("with " + lhs + " and " + rhs, (Test test) ->
                    {
                        final Test t = TestTests.createTest("abc");
                        if (expectedError == null)
                        {
                            t.assertEqual(lhs.longValue(), rhs.longValue());
                        }
                        else
                        {
                            test.assertThrows(() -> t.assertEqual(lhs.longValue(), rhs.longValue()), expectedError);
                        }
                    });
                };

                assertEqualTest.run((long)1, (long)2, new TestError("abc", TestTests.createErrorMessage(
                    "Expected: 1",
                    "Actual:   2")));
                assertEqualTest.run((long)1, (long)1, null);
            });

            runner.testGroup("assertEqual(long,Long)", () ->
            {
                final Action3<Long,Long,TestError> assertEqualTest = (Long lhs, Long rhs, TestError expectedError) ->
                {
                    runner.test("with " + lhs + " and " + Objects.toString(rhs), (Test test) ->
                    {
                        final Test t = TestTests.createTest("abc");
                        if (expectedError == null)
                        {
                            t.assertEqual(lhs.longValue(), rhs);
                        }
                        else
                        {
                            test.assertThrows(() -> t.assertEqual(lhs.longValue(), rhs), expectedError);
                        }
                    });
                };

                assertEqualTest.run((long)1, null, new TestError("abc", TestTests.createErrorMessage(
                    "Expected: 1",
                    "Actual:   null")));
                assertEqualTest.run((long)1, (long)2, new TestError("abc", TestTests.createErrorMessage(
                    "Expected: 1",
                    "Actual:   2")));
                assertEqualTest.run((long)1, (long)1, null);
            });

            runner.testGroup("assertEqual(Long,long)", () ->
            {
                final Action3<Long,Long,TestError> assertEqualTest = (Long lhs, Long rhs, TestError expectedError) ->
                {
                    runner.test("with " + Objects.toString(lhs) + " and " + rhs, (Test test) ->
                    {
                        final Test t = TestTests.createTest("abc");
                        if (expectedError == null)
                        {
                            t.assertEqual(lhs, rhs.longValue());
                        }
                        else
                        {
                            test.assertThrows(() -> t.assertEqual(lhs, rhs.longValue()), expectedError);
                        }
                    });
                };

                assertEqualTest.run(null, (long)2, new TestError("abc", TestTests.createErrorMessage(
                    "Expected: null",
                    "Actual:   2")));
                assertEqualTest.run((long)1, (long)2, new TestError("abc", TestTests.createErrorMessage(
                    "Expected: 1",
                    "Actual:   2")));
                assertEqualTest.run((long)1, (long)1, null);
            });

            runner.testGroup("assertEqual(float,float)", () ->
            {
                final Action3<Float,Float,TestError> assertEqualTest = (Float lhs, Float rhs, TestError expectedError) ->
                {
                    runner.test("with " + lhs + " and " + rhs, (Test test) ->
                    {
                        final Test t = TestTests.createTest("abc");
                        if (expectedError == null)
                        {
                            t.assertEqual(lhs.floatValue(), rhs.floatValue());
                        }
                        else
                        {
                            test.assertThrows(() -> t.assertEqual(lhs.floatValue(), rhs.floatValue()), expectedError);
                        }
                    });
                };

                assertEqualTest.run((float)1, (float)2, new TestError("abc", TestTests.createErrorMessage(
                    "Expected: 1.0",
                    "Actual:   2.0")));
                assertEqualTest.run((float)1, (float)1, null);
            });

            runner.testGroup("assertEqual(float,Float)", () ->
            {
                final Action3<Float,Float,TestError> assertEqualTest = (Float lhs, Float rhs, TestError expectedError) ->
                {
                    runner.test("with " + lhs + " and " + Objects.toString(rhs), (Test test) ->
                    {
                        final Test t = TestTests.createTest("abc");
                        if (expectedError == null)
                        {
                            t.assertEqual(lhs.floatValue(), rhs);
                        }
                        else
                        {
                            test.assertThrows(() -> t.assertEqual(lhs.floatValue(), rhs), expectedError);
                        }
                    });
                };

                assertEqualTest.run((float)1, null, new TestError("abc", TestTests.createErrorMessage(
                    "Expected: 1.0",
                    "Actual:   null")));
                assertEqualTest.run((float)1, (float)2, new TestError("abc", TestTests.createErrorMessage(
                    "Expected: 1.0",
                    "Actual:   2.0")));
                assertEqualTest.run((float)1, (float)1, null);
            });

            runner.testGroup("assertEqual(Float,float)", () ->
            {
                final Action3<Float,Float,TestError> assertEqualTest = (Float lhs, Float rhs, TestError expectedError) ->
                {
                    runner.test("with " + Objects.toString(lhs) + " and " + rhs, (Test test) ->
                    {
                        final Test t = TestTests.createTest("abc");
                        if (expectedError == null)
                        {
                            t.assertEqual(lhs, rhs.floatValue());
                        }
                        else
                        {
                            test.assertThrows(() -> t.assertEqual(lhs, rhs.floatValue()), expectedError);
                        }
                    });
                };

                assertEqualTest.run(null, (float)2, new TestError("abc", TestTests.createErrorMessage(
                    "Expected: null",
                    "Actual:   2.0")));
                assertEqualTest.run((float)1, (float)2, new TestError("abc", TestTests.createErrorMessage(
                    "Expected: 1.0",
                    "Actual:   2.0")));
                assertEqualTest.run((float)1, (float)1, null);
            });

            runner.testGroup("assertEqual(double,double)", () ->
            {
                final Action3<Double,Double,TestError> assertEqualTest = (Double lhs, Double rhs, TestError expectedError) ->
                {
                    runner.test("with " + lhs + " and " + rhs, (Test test) ->
                    {
                        final Test t = TestTests.createTest("abc");
                        if (expectedError == null)
                        {
                            t.assertEqual(lhs.doubleValue(), rhs.doubleValue());
                        }
                        else
                        {
                            test.assertThrows(() -> t.assertEqual(lhs.doubleValue(), rhs.doubleValue()), expectedError);
                        }
                    });
                };

                assertEqualTest.run((double)1, (double)2, new TestError("abc", TestTests.createErrorMessage(
                    "Expected: 1.0",
                    "Actual:   2.0")));
                assertEqualTest.run((double)1, (double)1, null);
            });

            runner.testGroup("assertEqual(double,double,double)", () ->
            {
                final Action4<Double,Double,Double,Throwable> assertEqualTest = (Double lhs, Double rhs, Double marginOfError, Throwable expectedError) ->
                {
                    runner.test("with " + lhs + " and " + rhs + " (+-" + marginOfError + ")", (Test test) ->
                    {
                        final Test t = TestTests.createTest("abc");
                        if (expectedError == null)
                        {
                            t.assertEqual(lhs.doubleValue(), rhs.doubleValue(), marginOfError.doubleValue());
                        }
                        else
                        {
                            test.assertThrows(() -> t.assertEqual(lhs.doubleValue(), rhs.doubleValue(), marginOfError.doubleValue()), expectedError);
                        }
                    });
                };

                assertEqualTest.run((double)1, (double)2, -1.0001,
                    new PreConditionFailure("marginOfError (-1.0001) must be greater than or equal to 0.0."));
                assertEqualTest.run((double)1, (double)2, -1.0,
                    new PreConditionFailure("marginOfError (-1.0) must be greater than or equal to 0.0."));
                assertEqualTest.run((double)1, (double)2, -0.99999,
                    new PreConditionFailure("marginOfError (-0.99999) must be greater than or equal to 0.0."));
                assertEqualTest.run((double)1, (double)2, -0.5,
                    new PreConditionFailure("marginOfError (-0.5) must be greater than or equal to 0.0."));
                assertEqualTest.run((double)1, (double)2, (double)0, new TestError("abc", TestTests.createErrorMessage(
                    "Expected: 1.0",
                    "Actual:   2.0")));
                assertEqualTest.run((double)1, (double)2, 0.5, new TestError("abc", TestTests.createErrorMessage(
                    "Expected: 1.0",
                    "Actual:   2.0")));
                assertEqualTest.run((double)1, (double)2, 0.99999, new TestError("abc", TestTests.createErrorMessage(
                    "Expected: 1.0",
                    "Actual:   2.0")));
                assertEqualTest.run((double)1, (double)2, 1.0, null);
                assertEqualTest.run((double)1, (double)2, 1.0001, null);
            });

            runner.testGroup("assertEqual(double,Double)", () ->
            {
                final Action3<Double,Double,TestError> assertEqualTest = (Double lhs, Double rhs, TestError expectedError) ->
                {
                    runner.test("with " + lhs + " and " + Objects.toString(rhs), (Test test) ->
                    {
                        final Test t = TestTests.createTest("abc");
                        if (expectedError == null)
                        {
                            t.assertEqual(lhs.doubleValue(), rhs);
                        }
                        else
                        {
                            test.assertThrows(() -> t.assertEqual(lhs.doubleValue(), rhs), expectedError);
                        }
                    });
                };

                assertEqualTest.run((double)1, null, new TestError("abc", TestTests.createErrorMessage(
                    "Expected: 1.0",
                    "Actual:   null")));
                assertEqualTest.run((double)1, (double)2, new TestError("abc", TestTests.createErrorMessage(
                    "Expected: 1.0",
                    "Actual:   2.0")));
                assertEqualTest.run((double)1, (double)1, null);
            });

            runner.testGroup("assertEqual(Double,double)", () ->
            {
                final Action3<Double,Double,TestError> assertEqualTest = (Double lhs, Double rhs, TestError expectedError) ->
                {
                    runner.test("with " + Objects.toString(lhs) + " and " + rhs, (Test test) ->
                    {
                        final Test t = TestTests.createTest("abc");
                        if (expectedError == null)
                        {
                            t.assertEqual(lhs, rhs.doubleValue());
                        }
                        else
                        {
                            test.assertThrows(() -> t.assertEqual(lhs, rhs.doubleValue()), expectedError);
                        }
                    });
                };

                assertEqualTest.run(null, (double)2, new TestError("abc", TestTests.createErrorMessage(
                    "Expected: null",
                    "Actual:   2.0")));
                assertEqualTest.run((double)1, (double)2, new TestError("abc", TestTests.createErrorMessage(
                    "Expected: 1.0",
                    "Actual:   2.0")));
                assertEqualTest.run((double)1, (double)1, null);
            });

            runner.testGroup("assertEqual(Throwable,Throwable)", () ->
            {
                runner.test("with null and null", (Test test) ->
                {
                    final Test t = TestTests.createTest("abc");
                    t.assertEqual((Throwable)null, (Throwable)null);
                });

                runner.test("with null and NullPointerException", (Test test) ->
                {
                    final Test t = TestTests.createTest("abc");
                    test.assertThrows(() -> t.assertEqual(null, new NullPointerException("abc")),
                        new TestError("abc", TestTests.createErrorMessage(
                            "Expected: null",
                            "Actual:   java.lang.NullPointerException: abc")));
                });

                runner.test("with NullPointerException and null", (Test test) ->
                {
                    final Test t = TestTests.createTest("abc");
                    test.assertThrows(() -> t.assertEqual(new NullPointerException("abc"), null),
                        new TestError("abc", TestTests.createErrorMessage(
                            "Expected: java.lang.NullPointerException: abc",
                            "Actual:   null")));
                });

                runner.test("with NullPointerException(a) and NullPointerException(b)", (Test test) ->
                {
                    final Test t = TestTests.createTest("abc");
                    test.assertThrows(() -> t.assertEqual(new NullPointerException("a"), new NullPointerException("b")),
                        new TestError("abc", TestTests.createErrorMessage(
                            "Expected: java.lang.NullPointerException: a",
                            "Actual:   java.lang.NullPointerException: b")));
                });

                runner.test("with NullPointerException(a) and NullPointerException(a)", (Test test) ->
                {
                    final Test t = TestTests.createTest("abc");
                    t.assertEqual(new NullPointerException("a"), new NullPointerException("a"));
                });
            });

            runner.testGroup("assertLinesEqual(Iterable<String>,InMemoryCharacterStream)", () ->
            {
                final Action3<Iterable<String>,InMemoryCharacterStream,Throwable> assertLinesEqualErrorTest = (Iterable<String> expectedLines, InMemoryCharacterStream stream, Throwable expected) ->
                {
                    runner.test("with " + English.andList(expectedLines, Strings.escapeAndQuote(stream == null ? null : stream.getText().await())), (Test test) ->
                    {
                        final Test t = TestTests.createTest("abc");
                        test.assertThrows(() -> t.assertLinesEqual(expectedLines, stream), expected);
                    });
                };

                assertLinesEqualErrorTest.run(null, InMemoryCharacterStream.create(), new PreConditionFailure("expected cannot be null."));
                assertLinesEqualErrorTest.run(Iterable.create(), null, new PreConditionFailure("stream cannot be null."));

                final InMemoryCharacterStream disposedStream = InMemoryCharacterStream.create();
                disposedStream.dispose().await();
                assertLinesEqualErrorTest.run(Iterable.create(), disposedStream, new PreConditionFailure("stream.isDisposed() cannot be true."));

                assertLinesEqualErrorTest.run(
                    Iterable.create(),
                    InMemoryCharacterStream.create("a").endOfStream(),
                    new TestError("abc", TestTests.createErrorMessage(
                        "Expected: []",
                        "Actual:   [",
                        "  \"a\"",
                        "]")));
                assertLinesEqualErrorTest.run(
                    Iterable.create("a"),
                    InMemoryCharacterStream.create("b").endOfStream(),
                    new TestError("abc", TestTests.createErrorMessage(
                        "Expected: [",
                        "  \"a\"",
                        "]",
                        "Actual:   [",
                        "  \"b\"",
                        "]")));
                assertLinesEqualErrorTest.run(
                    Iterable.create("a", "b", "c"),
                    InMemoryCharacterStream.create("a\nb\nc\nd").endOfStream(),
                    new TestError("abc", TestTests.createErrorMessage(
                        "Expected: [",
                        "  \"a\",",
                        "  \"b\",",
                        "  \"c\"",
                        "]",
                        "Actual:   [",
                        "  \"a\",",
                        "  \"b\",",
                        "  \"c\",",
                        "  \"d\"",
                        "]")));

                final Action2<Iterable<String>,InMemoryCharacterStream> assertLinesEqualTest = (Iterable<String> expected, InMemoryCharacterStream stream) ->
                {
                    runner.test("with " + English.andList(expected, Strings.escapeAndQuote(stream == null ? null : stream.getText().await())), (Test test) ->
                    {
                        final Test t = TestTests.createTest("abc");
                        t.assertLinesEqual(expected, stream);
                    });
                };

                assertLinesEqualTest.run(
                    Iterable.create(),
                    InMemoryCharacterStream.create().endOfStream());
                assertLinesEqualTest.run(
                    Iterable.create("abc"),
                    InMemoryCharacterStream.create("abc").endOfStream());
                assertLinesEqualTest.run(
                    Iterable.create("a", "b", "c"),
                    InMemoryCharacterStream.create("a\nb\nc").endOfStream());
            });

            runner.testGroup("assertLinesEqual(Iterable<String>,InMemoryCharacterStream,String)", () ->
            {
                final Action4<Iterable<String>,InMemoryCharacterStream,String,Throwable> assertLinesEqualErrorTest = (Iterable<String> expectedLines, InMemoryCharacterStream stream, String message, Throwable expected) ->
                {
                    runner.test("with " + English.andList(expectedLines, Strings.escapeAndQuote(stream == null ? null : stream.getText().await()), Strings.escapeAndQuote(message)), (Test test) ->
                    {
                        final Test t = TestTests.createTest("abc");
                        test.assertThrows(() -> t.assertLinesEqual(expectedLines, stream, message), expected);
                    });
                };

                assertLinesEqualErrorTest.run(null, InMemoryCharacterStream.create(), null, new PreConditionFailure("expected cannot be null."));
                assertLinesEqualErrorTest.run(Iterable.create(), null, null, new PreConditionFailure("stream cannot be null."));

                final InMemoryCharacterStream disposedStream = InMemoryCharacterStream.create();
                disposedStream.dispose().await();
                assertLinesEqualErrorTest.run(Iterable.create(), disposedStream, null, new PreConditionFailure("stream.isDisposed() cannot be true."));

                assertLinesEqualErrorTest.run(
                    Iterable.create(),
                    InMemoryCharacterStream.create("a").endOfStream(),
                    null,
                    new TestError("abc", TestTests.createErrorMessage(
                        "Expected: []",
                        "Actual:   [",
                        "  \"a\"",
                        "]")));
                assertLinesEqualErrorTest.run(
                    Iterable.create(),
                    InMemoryCharacterStream.create("a").endOfStream(),
                    "hello",
                    new TestError("abc", TestTests.createErrorMessage(
                        "Message:  hello",
                        "Expected: []",
                        "Actual:   [",
                        "  \"a\"",
                        "]")));
                assertLinesEqualErrorTest.run(
                    Iterable.create("a"),
                    InMemoryCharacterStream.create("b").endOfStream(),
                    null,
                    new TestError("abc", TestTests.createErrorMessage(
                        "Expected: [",
                        "  \"a\"",
                        "]",
                        "Actual:   [",
                        "  \"b\"",
                        "]")));
                assertLinesEqualErrorTest.run(
                    Iterable.create("a"),
                    InMemoryCharacterStream.create("b").endOfStream(),
                    "hello",
                    new TestError("abc", TestTests.createErrorMessage(
                        "Message:  hello",
                        "Expected: [",
                        "  \"a\"",
                        "]",
                        "Actual:   [",
                        "  \"b\"",
                        "]")));
                assertLinesEqualErrorTest.run(
                    Iterable.create("a", "b", "c"),
                    InMemoryCharacterStream.create("a\nb\nc\nd").endOfStream(),
                    null,
                    new TestError("abc", TestTests.createErrorMessage(
                        "Expected: [",
                        "  \"a\",",
                        "  \"b\",",
                        "  \"c\"",
                        "]",
                        "Actual:   [",
                        "  \"a\",",
                        "  \"b\",",
                        "  \"c\",",
                        "  \"d\"",
                        "]")));
                assertLinesEqualErrorTest.run(
                    Iterable.create("a", "b", "c"),
                    InMemoryCharacterStream.create("a\nb\nc\nd").endOfStream(),
                    "hello",
                    new TestError("abc", TestTests.createErrorMessage(
                        "Message:  hello",
                        "Expected: [",
                        "  \"a\",",
                        "  \"b\",",
                        "  \"c\"",
                        "]",
                        "Actual:   [",
                        "  \"a\",",
                        "  \"b\",",
                        "  \"c\",",
                        "  \"d\"",
                        "]")));

                final Action3<Iterable<String>,InMemoryCharacterStream,String> assertLinesEqualNoErrorTest = (Iterable<String> expected, InMemoryCharacterStream stream, String message) ->
                {
                    runner.test("with " + English.andList(expected, Strings.escapeAndQuote(stream == null ? null : stream.getText().await()), Strings.escapeAndQuote(message)), (Test test) ->
                    {
                        final Test t = TestTests.createTest("abc");
                        t.assertLinesEqual(expected, stream, message);
                    });
                };

                assertLinesEqualNoErrorTest.run(
                    Iterable.create(),
                    InMemoryCharacterStream.create().endOfStream(),
                    null);
                assertLinesEqualNoErrorTest.run(
                    Iterable.create("abc"),
                    InMemoryCharacterStream.create("abc").endOfStream(),
                    null);
                assertLinesEqualNoErrorTest.run(
                    Iterable.create("a", "b", "c"),
                    InMemoryCharacterStream.create("a\nb\nc").endOfStream(),
                    null);
                assertLinesEqualNoErrorTest.run(
                    Iterable.create(),
                    InMemoryCharacterStream.create().endOfStream(),
                    "hello");
                assertLinesEqualNoErrorTest.run(
                    Iterable.create("abc"),
                    InMemoryCharacterStream.create("abc").endOfStream(),
                    "hello");
                assertLinesEqualNoErrorTest.run(
                    Iterable.create("a", "b", "c"),
                    InMemoryCharacterStream.create("a\nb\nc").endOfStream(),
                    "hello");
            });

            runner.testGroup("assertLinesEqual(Iterable<String>,String)", () ->
            {
                final Action3<Iterable<String>,String,Throwable> assertLinesEqualErrorTest = (Iterable<String> expectedLines, String text, Throwable expected) ->
                {
                    runner.test("with " + English.andList(expectedLines, Strings.escapeAndQuote(text)), (Test test) ->
                    {
                        final Test t = TestTests.createTest("abc");
                        test.assertThrows(() -> t.assertLinesEqual(expectedLines, text), expected);
                    });
                };

                assertLinesEqualErrorTest.run(null, "hello", new PreConditionFailure("expected cannot be null."));
                assertLinesEqualErrorTest.run(Iterable.create(), null, new PreConditionFailure("text cannot be null."));

                assertLinesEqualErrorTest.run(
                    Iterable.create(),
                    "a",
                    new TestError("abc", TestTests.createErrorMessage(
                        "Expected: []",
                        "Actual:   [",
                        "  \"a\"",
                        "]")));
                assertLinesEqualErrorTest.run(
                    Iterable.create("a"),
                    "b",
                    new TestError("abc", TestTests.createErrorMessage(
                        "Expected: [",
                        "  \"a\"",
                        "]",
                        "Actual:   [",
                        "  \"b\"",
                        "]")));
                assertLinesEqualErrorTest.run(
                    Iterable.create("a", "b", "c"),
                    "a\nb\nc\nd",
                    new TestError("abc", TestTests.createErrorMessage(
                        "Expected: [",
                        "  \"a\",",
                        "  \"b\",",
                        "  \"c\"",
                        "]",
                        "Actual:   [",
                        "  \"a\",",
                        "  \"b\",",
                        "  \"c\",",
                        "  \"d\"",
                        "]")));

                final Action2<Iterable<String>,String> assertLinesEqualNoErrorTest = (Iterable<String> expected, String text) ->
                {
                    runner.test("with " + English.andList(expected, Strings.escapeAndQuote(text)), (Test test) ->
                    {
                        final Test t = TestTests.createTest("abc");
                        t.assertLinesEqual(expected, text);
                    });
                };

                assertLinesEqualNoErrorTest.run(
                    Iterable.create(),
                    "");
                assertLinesEqualNoErrorTest.run(
                    Iterable.create("abc"),
                    "abc");
                assertLinesEqualNoErrorTest.run(
                    Iterable.create("a", "b", "c"),
                    "a\nb\nc");
            });

            runner.testGroup("assertLinesEqual(Iterable<String>,String,String)", () ->
            {
                final Action4<Iterable<String>,String,String,Throwable> assertLinesEqualErrorTest = (Iterable<String> expectedLines, String text, String message, Throwable expected) ->
                {
                    runner.test("with " + English.andList(expectedLines, Strings.escapeAndQuote(text), Strings.escapeAndQuote(message)), (Test test) ->
                    {
                        final Test t = TestTests.createTest("abc");
                        test.assertThrows(() -> t.assertLinesEqual(expectedLines, text, message), expected);
                    });
                };

                assertLinesEqualErrorTest.run(null, "abc", null, new PreConditionFailure("expected cannot be null."));
                assertLinesEqualErrorTest.run(Iterable.create(), null, null, new PreConditionFailure("text cannot be null."));

                assertLinesEqualErrorTest.run(
                    Iterable.create(),
                    "a",
                    null,
                    new TestError("abc", TestTests.createErrorMessage(
                        "Expected: []",
                        "Actual:   [",
                        "  \"a\"",
                        "]")));
                assertLinesEqualErrorTest.run(
                    Iterable.create(),
                    "a",
                    "hello",
                    new TestError("abc", TestTests.createErrorMessage(
                        "Message:  hello",
                        "Expected: []",
                        "Actual:   [",
                        "  \"a\"",
                        "]")));
                assertLinesEqualErrorTest.run(
                    Iterable.create("a"),
                    "b",
                    null,
                    new TestError("abc", TestTests.createErrorMessage(
                        "Expected: [",
                        "  \"a\"",
                        "]",
                        "Actual:   [",
                        "  \"b\"",
                        "]")));
                assertLinesEqualErrorTest.run(
                    Iterable.create("a"),
                    "b",
                    "hello",
                    new TestError("abc", TestTests.createErrorMessage(
                        "Message:  hello",
                        "Expected: [",
                        "  \"a\"",
                        "]",
                        "Actual:   [",
                        "  \"b\"",
                        "]")));
                assertLinesEqualErrorTest.run(
                    Iterable.create("a", "b", "c"),
                    "a\nb\nc\nd",
                    null,
                    new TestError("abc", TestTests.createErrorMessage(
                        "Expected: [",
                        "  \"a\",",
                        "  \"b\",",
                        "  \"c\"",
                        "]",
                        "Actual:   [",
                        "  \"a\",",
                        "  \"b\",",
                        "  \"c\",",
                        "  \"d\"",
                        "]")));
                assertLinesEqualErrorTest.run(
                    Iterable.create("a", "b", "c"),
                    "a\nb\nc\nd",
                    "hello",
                    new TestError("abc", TestTests.createErrorMessage(
                        "Message:  hello",
                        "Expected: [",
                        "  \"a\",",
                        "  \"b\",",
                        "  \"c\"",
                        "]",
                        "Actual:   [",
                        "  \"a\",",
                        "  \"b\",",
                        "  \"c\",",
                        "  \"d\"",
                        "]")));

                final Action3<Iterable<String>,String,String> assertLinesEqualNoErrorTest = (Iterable<String> expected, String text, String message) ->
                {
                    runner.test("with " + English.andList(expected, Strings.escapeAndQuote(text), Strings.escapeAndQuote(message)), (Test test) ->
                    {
                        final Test t = TestTests.createTest("abc");
                        t.assertLinesEqual(expected, text, message);
                    });
                };

                assertLinesEqualNoErrorTest.run(
                    Iterable.create(),
                    "",
                    null);
                assertLinesEqualNoErrorTest.run(
                    Iterable.create("abc"),
                    "abc",
                    null);
                assertLinesEqualNoErrorTest.run(
                    Iterable.create("a", "b", "c"),
                    "a\nb\nc",
                    null);
                assertLinesEqualNoErrorTest.run(
                    Iterable.create(),
                    "",
                    "hello");
                assertLinesEqualNoErrorTest.run(
                    Iterable.create("abc"),
                    "abc",
                    "hello");
                assertLinesEqualNoErrorTest.run(
                    Iterable.create("a", "b", "c"),
                    "a\nb\nc",
                    "hello");
            });

            runner.testGroup("assertLinesEqual(Iterable<String>,Iterator<String>)", () ->
            {
                final Action3<Iterable<String>,Iterator<String>,Throwable> assertLinesEqualErrorTest = (Iterable<String> expectedLines, Iterator<String> lines, Throwable expected) ->
                {
                    runner.test("with " + English.andList(expectedLines, Strings.escapeAndQuote(lines)), (Test test) ->
                    {
                        final Test t = TestTests.createTest("abc");
                        test.assertThrows(() -> t.assertLinesEqual(expectedLines, lines), expected);
                    });
                };

                assertLinesEqualErrorTest.run(null, Iterator.create("hello"), new PreConditionFailure("expected cannot be null."));
                assertLinesEqualErrorTest.run(Iterable.create(), null, new PreConditionFailure("lines cannot be null."));

                assertLinesEqualErrorTest.run(
                    Iterable.create(),
                    Iterator.create("a"),
                    new TestError("abc", TestTests.createErrorMessage(
                        "Expected: []",
                        "Actual:   [",
                        "  \"a\"",
                        "]")));
                assertLinesEqualErrorTest.run(
                    Iterable.create("a"),
                    Iterator.create("b"),
                    new TestError("abc", TestTests.createErrorMessage(
                        "Expected: [",
                        "  \"a\"",
                        "]",
                        "Actual:   [",
                        "  \"b\"",
                        "]")));
                assertLinesEqualErrorTest.run(
                    Iterable.create("a", "b", "c"),
                    Iterator.create("a", "b", "c", "d"),
                    new TestError("abc", TestTests.createErrorMessage(
                        "Expected: [",
                        "  \"a\",",
                        "  \"b\",",
                        "  \"c\"",
                        "]",
                        "Actual:   [",
                        "  \"a\",",
                        "  \"b\",",
                        "  \"c\",",
                        "  \"d\"",
                        "]")));

                final Action2<Iterable<String>,Iterator<String>> assertLinesEqualNoErrorTest = (Iterable<String> expected, Iterator<String> lines) ->
                {
                    runner.test("with " + English.andList(expected, Strings.escapeAndQuote(lines)), (Test test) ->
                    {
                        final Test t = TestTests.createTest("abc");
                        t.assertLinesEqual(expected, lines);
                    });
                };

                assertLinesEqualNoErrorTest.run(
                    Iterable.create(),
                    Iterator.create());
                assertLinesEqualNoErrorTest.run(
                    Iterable.create("abc"),
                    Iterator.create("abc"));
                assertLinesEqualNoErrorTest.run(
                    Iterable.create("a", "b", "c"),
                    Iterator.create("a", "b", "c"));
            });

            runner.testGroup("assertLinesEqual(Iterable<String>,Iterator<String>,String)", () ->
            {
                final Action4<Iterable<String>,Iterator<String>,String,Throwable> assertLinesEqualErrorTest = (Iterable<String> expectedLines, Iterator<String> lines, String message, Throwable expected) ->
                {
                    runner.test("with " + English.andList(expectedLines, Strings.escapeAndQuote(lines), Strings.escapeAndQuote(message)), (Test test) ->
                    {
                        final Test t = TestTests.createTest("abc");
                        test.assertThrows(() -> t.assertLinesEqual(expectedLines, lines, message), expected);
                    });
                };

                assertLinesEqualErrorTest.run(null, Iterator.create("abc"), null, new PreConditionFailure("expected cannot be null."));
                assertLinesEqualErrorTest.run(Iterable.create(), null, null, new PreConditionFailure("lines cannot be null."));

                assertLinesEqualErrorTest.run(
                    Iterable.create(),
                    Iterator.create("a"),
                    null,
                    new TestError("abc", TestTests.createErrorMessage(
                        "Expected: []",
                        "Actual:   [",
                        "  \"a\"",
                        "]")));
                assertLinesEqualErrorTest.run(
                    Iterable.create(),
                    Iterator.create("a"),
                    "hello",
                    new TestError("abc", TestTests.createErrorMessage(
                        "Message:  hello",
                        "Expected: []",
                        "Actual:   [",
                        "  \"a\"",
                        "]")));
                assertLinesEqualErrorTest.run(
                    Iterable.create("a"),
                    Iterator.create("b"),
                    null,
                    new TestError("abc", TestTests.createErrorMessage(
                        "Expected: [",
                        "  \"a\"",
                        "]",
                        "Actual:   [",
                        "  \"b\"",
                        "]")));
                assertLinesEqualErrorTest.run(
                    Iterable.create("a"),
                    Iterator.create("b"),
                    "hello",
                    new TestError("abc", TestTests.createErrorMessage(
                        "Message:  hello",
                        "Expected: [",
                        "  \"a\"",
                        "]",
                        "Actual:   [",
                        "  \"b\"",
                        "]")));
                assertLinesEqualErrorTest.run(
                    Iterable.create("a", "b", "c"),
                    Iterator.create("a", "b", "c", "d"),
                    null,
                    new TestError("abc", TestTests.createErrorMessage(
                        "Expected: [",
                        "  \"a\",",
                        "  \"b\",",
                        "  \"c\"",
                        "]",
                        "Actual:   [",
                        "  \"a\",",
                        "  \"b\",",
                        "  \"c\",",
                        "  \"d\"",
                        "]")));
                assertLinesEqualErrorTest.run(
                    Iterable.create("a", "b", "c"),
                    Iterator.create("a", "b", "c", "d"),
                    "hello",
                    new TestError("abc", TestTests.createErrorMessage(
                        "Message:  hello",
                        "Expected: [",
                        "  \"a\",",
                        "  \"b\",",
                        "  \"c\"",
                        "]",
                        "Actual:   [",
                        "  \"a\",",
                        "  \"b\",",
                        "  \"c\",",
                        "  \"d\"",
                        "]")));

                final Action3<Iterable<String>,Iterator<String>,String> assertLinesEqualTest = (Iterable<String> expected, Iterator<String> lines, String message) ->
                {
                    runner.test("with " + English.andList(expected, Strings.escapeAndQuote(lines), Strings.escapeAndQuote(message)), (Test test) ->
                    {
                        final Test t = TestTests.createTest("abc");
                        t.assertLinesEqual(expected, lines, message);
                    });
                };

                assertLinesEqualTest.run(
                    Iterable.create(),
                    Iterator.create(),
                    null);
                assertLinesEqualTest.run(
                    Iterable.create("abc"),
                    Iterator.create("abc"),
                    null);
                assertLinesEqualTest.run(
                    Iterable.create("a", "b", "c"),
                    Iterator.create("a", "b", "c"),
                    null);
                assertLinesEqualTest.run(
                    Iterable.create(),
                    Iterator.create(),
                    "hello");
                assertLinesEqualTest.run(
                    Iterable.create("abc"),
                    Iterator.create("abc"),
                    "hello");
                assertLinesEqualTest.run(
                    Iterable.create("a", "b", "c"),
                    Iterator.create("a", "b", "c"),
                    "hello");
            });

            runner.testGroup("assertNotEqual(T,T)", () ->
            {
                Action3<String,String,Throwable> assertNotEqualTest = (String expected, String actual, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(expected) + " and " + Strings.escapeAndQuote(actual), (Test test) ->
                    {
                        final Test t = TestTests.createTest("abc");
                        if (expectedError != null)
                        {
                            test.assertThrows(() -> t.assertNotEqual(expected, actual), expectedError);
                        }
                        else
                        {
                            t.assertNotEqual(expected, actual);
                        }
                    });
                };

                assertNotEqualTest.run(null, null, new TestError("abc", TestTests.createErrorMessage(
                    "Expected: \"not null\"",
                    "Actual:   null")));
                assertNotEqualTest.run("hello", null, null);
                assertNotEqualTest.run(null, "there", null);
                assertNotEqualTest.run("hello", "there", null);
                assertNotEqualTest.run("hello", "hello", new TestError("abc", TestTests.createErrorMessage(
                    "Expected: \"not hello\"",
                    "Actual:   \"hello\"")));
            });

            runner.testGroup("assertSame(T,T)", () ->
            {
                Action3<String,String,Throwable> assertSameTest = (String expected, String actual, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(expected) + " and " + Strings.escapeAndQuote(actual), (Test test) ->
                    {
                        final Test t = TestTests.createTest("abc");
                        if (expectedError != null)
                        {
                            test.assertThrows(() -> t.assertSame(expected, actual), expectedError);
                        }
                        else
                        {
                            t.assertSame(expected, actual);
                        }
                    });
                };

                assertSameTest.run(null, null, null);
                assertSameTest.run("hello", null, new TestError("abc", TestTests.createErrorMessage(
                    "Expected: \"hello\"",
                    "Actual:   null")));
                assertSameTest.run(null, "there", new TestError("abc", TestTests.createErrorMessage(
                    "Expected: null",
                    "Actual:   \"there\"")));
                assertSameTest.run("hello", "there", new TestError("abc", TestTests.createErrorMessage(
                    "Expected: \"hello\"",
                    "Actual:   \"there\"")));
                assertSameTest.run("hello", "hello", null);
                assertSameTest.run(new String("hello"), new String("hello"), new TestError("abc", TestTests.createErrorMessage(
                    "Expected: \"hello\" (java.lang.String)",
                    "Actual:   \"hello\" (java.lang.String)")));
            });

            runner.testGroup("assertNotSame(T,T)", () ->
            {
                Action3<String,String,Throwable> assertNotSameTest = (String expected, String actual, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(expected) + " and " + Strings.escapeAndQuote(actual), (Test test) ->
                    {
                        final Test t = TestTests.createTest("abc");
                        if (expectedError != null)
                        {
                            test.assertThrows(() -> t.assertNotSame(expected, actual), expectedError);
                        }
                        else
                        {
                            t.assertNotSame(expected, actual);
                        }
                    });
                };

                assertNotSameTest.run(null, null, new TestError("abc", TestTests.createErrorMessage(
                    "Expected: \"not null\"",
                    "Actual:   null")));
                assertNotSameTest.run("hello", null, null);
                assertNotSameTest.run(null, "there", null);
                assertNotSameTest.run("hello", "there", null);
                assertNotSameTest.run("hello", "hello", new TestError("abc", TestTests.createErrorMessage(
                    "Expected: \"not hello\"",
                    "Actual:   \"hello\"")));
                assertNotSameTest.run(new String("hello"), new String("hello"), null);
            });

            runner.testGroup("assertNullOrNotSame(T,T)", () ->
            {
                final Action3<String,String,Throwable> assertNullOrNotSameErrorTest = (String expected, String actual, Throwable expectedError) ->
                {
                    runner.test("with " + English.andList(Iterable.create(expected, actual).map(Strings::escapeAndQuote)), (Test test) ->
                    {
                        final Test t = TestTests.createTest("abc");
                        test.assertThrows(() -> t.assertNullOrNotSame(expected, actual), expectedError);
                    });
                };

                assertNullOrNotSameErrorTest.run("hello", "hello", new TestError("abc", TestTests.createErrorMessage(
                    "Expected: \"not hello\"",
                    "Actual:   \"hello\"")));

                Action2<String,String> assertNullOrNotSameTest = (String expected, String actual) ->
                {
                    runner.test("with " + English.andList(Iterable.create(expected, actual).map(Strings::escapeAndQuote)), (Test test) ->
                    {
                        final Test t = TestTests.createTest("abc");
                        t.assertNullOrNotSame(expected, actual);
                    });
                };

                assertNullOrNotSameTest.run(null, null);
                assertNullOrNotSameTest.run("hello", null);
                assertNullOrNotSameTest.run(null, "there");
                assertNullOrNotSameTest.run("hello", "there");
                assertNullOrNotSameTest.run(new String("hello"), new String("hello"));
            });

            runner.testGroup("assertNullOrNotSame(T,T,String)", () ->
            {
                final Action4<String,String,String,Throwable> assertNullOrNotSameErrorTest = (String expected, String actual, String message, Throwable expectedError) ->
                {
                    runner.test("with " + English.andList(Iterable.create(expected, actual, message).map(Strings::escapeAndQuote)), (Test test) ->
                    {
                        final Test t = TestTests.createTest("abc");
                        test.assertThrows(() -> t.assertNullOrNotSame(expected, actual, message), expectedError);
                    });
                };

                assertNullOrNotSameErrorTest.run("hello", "hello", "my message", new TestError("abc", TestTests.createErrorMessage(
                    "Message:  my message",
                    "Expected: \"not hello\"",
                    "Actual:   \"hello\"")));

                Action3<String,String,String> assertNullOrNotSameTest = (String expected, String actual, String message) ->
                {
                    runner.test("with " + English.andList(Iterable.create(expected, actual, message).map(Strings::escapeAndQuote)), (Test test) ->
                    {
                        final Test t = TestTests.createTest("abc");
                        t.assertNullOrNotSame(expected, actual, message);
                    });
                };

                assertNullOrNotSameTest.run(null, null, "my message");
                assertNullOrNotSameTest.run("hello", null, "my message");
                assertNullOrNotSameTest.run(null, "there", "my message");
                assertNullOrNotSameTest.run("hello", "there", "my message");
                assertNullOrNotSameTest.run(new String("hello"), new String("hello"), "my message");
            });

            runner.testGroup("assertLessThan(T,T)", () ->
            {
                Action3<Distance,Distance,Throwable> assertLessThanTest = (Distance value, Distance upperBound, Throwable expectedError) ->
                {
                    runner.test("with " + Objects.toString(value) + " and " + Objects.toString(upperBound), (Test test) ->
                    {
                        final Test t = TestTests.createTest("abc");
                        if (expectedError != null)
                        {
                            test.assertThrows(() -> t.assertLessThan(value, upperBound), expectedError);
                        }
                        else
                        {
                            t.assertLessThan(value, upperBound);
                        }
                    });
                };

                assertLessThanTest.run(null, null, new TestError("abc", TestTests.createErrorMessage(
                    "Expected: \"less than null\"",
                    "Actual:   null")));
                assertLessThanTest.run(Distance.inches(1), null, new TestError("abc", TestTests.createErrorMessage(
                    "Expected: \"less than null\"",
                    "Actual:   1.0 Inches")));
                assertLessThanTest.run(null, Distance.miles(5), null);
                assertLessThanTest.run(Distance.inches(5), Distance.miles(3), null);
                assertLessThanTest.run(Distance.miles(1), Distance.miles(1), new TestError("abc", TestTests.createErrorMessage(
                    "Expected: \"less than 1.0 Miles\"",
                    "Actual:   1.0 Miles")));
                assertLessThanTest.run(Distance.miles(4), Distance.inches(1), new TestError("abc", TestTests.createErrorMessage(
                    "Expected: \"less than 1.0 Inches\"",
                    "Actual:   4.0 Miles")));
            });

            runner.testGroup("assertLessThanOrEqualTo(T,T)", () ->
            {
                Action3<Distance,Distance,Throwable> assertLessThanOrEqualToTest = (Distance value, Distance upperBound, Throwable expectedError) ->
                {
                    runner.test("with " + Objects.toString(value) + " and " + Objects.toString(upperBound), (Test test) ->
                    {
                        final Test t = TestTests.createTest("abc");
                        if (expectedError != null)
                        {
                            test.assertThrows(() -> t.assertLessThanOrEqualTo(value, upperBound), expectedError);
                        }
                        else
                        {
                            t.assertLessThanOrEqualTo(value, upperBound);
                        }
                    });
                };

                assertLessThanOrEqualToTest.run(null, null, null);
                assertLessThanOrEqualToTest.run(Distance.inches(1), null, new TestError("abc", TestTests.createErrorMessage(
                    "Expected: \"less than or equal to null\"",
                    "Actual:   1.0 Inches")));
                assertLessThanOrEqualToTest.run(null, Distance.miles(5), null);
                assertLessThanOrEqualToTest.run(Distance.inches(5), Distance.miles(3), null);
                assertLessThanOrEqualToTest.run(Distance.miles(1), Distance.miles(1), null);
                assertLessThanOrEqualToTest.run(Distance.miles(4), Distance.inches(1), new TestError("abc", TestTests.createErrorMessage(
                    "Expected: \"less than or equal to 1.0 Inches\"",
                    "Actual:   4.0 Miles")));
            });

            runner.testGroup("assertGreaterThanOrEqualTo(T,T)", () ->
            {
                Action3<Distance,Distance,Throwable> assertGreaterThanOrEqualToTest = (Distance value, Distance upperBound, Throwable expectedError) ->
                {
                    runner.test("with " + Objects.toString(value) + " and " + Objects.toString(upperBound), (Test test) ->
                    {
                        final Test t = TestTests.createTest("abc");
                        if (expectedError != null)
                        {
                            test.assertThrows(() -> t.assertGreaterThanOrEqualTo(value, upperBound), expectedError);
                        }
                        else
                        {
                            t.assertGreaterThanOrEqualTo(value, upperBound);
                        }
                    });
                };

                assertGreaterThanOrEqualToTest.run(null, null, null);
                assertGreaterThanOrEqualToTest.run(Distance.inches(1), null, null);
                assertGreaterThanOrEqualToTest.run(null, Distance.miles(5), new TestError("abc", TestTests.createErrorMessage(
                    "Expected: \"greater than or equal to 5.0 Miles\"",
                    "Actual:   null")));
                assertGreaterThanOrEqualToTest.run(Distance.inches(5), Distance.miles(3), new TestError("abc", TestTests.createErrorMessage(
                    "Expected: \"greater than or equal to 3.0 Miles\"",
                    "Actual:   5.0 Inches")));
                assertGreaterThanOrEqualToTest.run(Distance.miles(1), Distance.miles(1), null);
                assertGreaterThanOrEqualToTest.run(Distance.miles(4), Distance.inches(1), null);
            });

            runner.testGroup("assertGreaterThanOrEqualTo(double,double)", () ->
            {
                Action3<Double,Double,Throwable> assertGreaterThanOrEqualToTest = (Double value, Double upperBound, Throwable expectedError) ->
                {
                    runner.test("with " + Objects.toString(value) + " and " + Objects.toString(upperBound), (Test test) ->
                    {
                        final Test t = TestTests.createTest("abc");
                        if (expectedError != null)
                        {
                            test.assertThrows(() -> t.assertGreaterThanOrEqualTo(value.doubleValue(), upperBound.doubleValue()), expectedError);
                        }
                        else
                        {
                            t.assertGreaterThanOrEqualTo(value.doubleValue(), upperBound.doubleValue());
                        }
                    });
                };

                assertGreaterThanOrEqualToTest.run(5.0, 6.0, new TestError("abc", TestTests.createErrorMessage(
                    "Expected: \"greater than or equal to 6.0\"",
                    "Actual:   5.0")));
                assertGreaterThanOrEqualToTest.run(5.0, 5.0, null);
                assertGreaterThanOrEqualToTest.run(5.0, 4.0, null);
            });

            runner.testGroup("assertGreaterThan(T,T)", () ->
            {
                Action3<Distance,Distance,Throwable> assertGreaterThanTest = (Distance value, Distance upperBound, Throwable expectedError) ->
                {
                    runner.test("with " + Objects.toString(value) + " and " + Objects.toString(upperBound), (Test test) ->
                    {
                        final Test t = TestTests.createTest("abc");
                        if (expectedError != null)
                        {
                            test.assertThrows(() -> t.assertGreaterThan(value, upperBound), expectedError);
                        }
                        else
                        {
                            t.assertGreaterThan(value, upperBound);
                        }
                    });
                };

                assertGreaterThanTest.run(null, null, new TestError("abc", TestTests.createErrorMessage(
                    "Expected: \"greater than null\"",
                    "Actual:   null")));
                assertGreaterThanTest.run(Distance.inches(1), null, null);
                assertGreaterThanTest.run(null, Distance.miles(5), new TestError("abc", TestTests.createErrorMessage(
                    "Expected: \"greater than 5.0 Miles\"",
                    "Actual:   null")));
                assertGreaterThanTest.run(Distance.inches(5), Distance.miles(3), new TestError("abc", TestTests.createErrorMessage(
                    "Expected: \"greater than 3.0 Miles\"",
                    "Actual:   5.0 Inches")));
                assertGreaterThanTest.run(Distance.miles(1), Distance.miles(1), new TestError("abc", TestTests.createErrorMessage(
                    "Expected: \"greater than 1.0 Miles\"",
                    "Actual:   1.0 Miles")));
                assertGreaterThanTest.run(Distance.miles(4), Distance.inches(1), null);
            });

            runner.testGroup("assertBetween(T,T,T)", () ->
            {
                final Action4<Distance,Distance,Distance,Throwable> assertBetweenTest = (Distance lowerBound, Distance value, Distance upperBound, Throwable expectedError) ->
                {
                    runner.test("with " + Objects.toString(lowerBound) + ", " + Objects.toString(value) + ", and " + Objects.toString(upperBound), (Test test) ->
                    {
                        final Test t = TestTests.createTest("abc");
                        if (expectedError != null)
                        {
                            test.assertThrows(() -> t.assertBetween(lowerBound, value, upperBound), expectedError);
                        }
                        else
                        {
                            t.assertBetween(lowerBound, value, upperBound);
                        }
                    });
                };

                assertBetweenTest.run(null, null, null, null);
                assertBetweenTest.run(null, null, Distance.miles(0.6), null);
                assertBetweenTest.run(null, Distance.miles(0.5), null, new TestError("abc", TestTests.createErrorMessage(
                    "Expected: \"between null and null\"",
                    "Actual:   0.5 Miles")));
                assertBetweenTest.run(null, Distance.miles(0.5), Distance.miles(0.4), new TestError("abc", TestTests.createErrorMessage(
                    "Expected: \"between null and 0.4 Miles\"",
                    "Actual:   0.5 Miles")));
                assertBetweenTest.run(null, Distance.miles(0.5), Distance.miles(0.5), null);
                assertBetweenTest.run(null, Distance.miles(0.5), Distance.miles(0.6), null);
                assertBetweenTest.run(Distance.miles(1), null, null, new TestError("abc", TestTests.createErrorMessage(
                    "Expected: \"between 1.0 Miles and null\"",
                    "Actual:   null")));
                assertBetweenTest.run(Distance.miles(1), Distance.miles(0.5), null, new TestError("abc", TestTests.createErrorMessage(
                    "Expected: \"between 1.0 Miles and null\"",
                    "Actual:   0.5 Miles")));
                assertBetweenTest.run(Distance.miles(1), Distance.miles(1.0), null, new TestError("abc", TestTests.createErrorMessage(
                    "Expected: \"between 1.0 Miles and null\"",
                    "Actual:   1.0 Miles")));
                assertBetweenTest.run(Distance.miles(1), Distance.miles(1.2), null, new TestError("abc", TestTests.createErrorMessage(
                    "Expected: \"between 1.0 Miles and null\"",
                    "Actual:   1.2 Miles")));
                assertBetweenTest.run(Distance.miles(1), null, Distance.miles(0.6), new TestError("abc", TestTests.createErrorMessage(
                    "Expected: \"between 1.0 Miles and 0.6 Miles\"",
                    "Actual:   null")));
                assertBetweenTest.run(Distance.miles(1), null, Distance.miles(1), new TestError("abc", TestTests.createErrorMessage(
                    "Expected: \"between 1.0 Miles and 1.0 Miles\"",
                    "Actual:   null")));
                assertBetweenTest.run(Distance.miles(1), null, Distance.miles(2), new TestError("abc", TestTests.createErrorMessage(
                    "Expected: \"between 1.0 Miles and 2.0 Miles\"",
                    "Actual:   null")));
            });

            runner.testGroup("assertThrows(Action0,Throwable)", () ->
            {
                runner.test("with null action", (Test test) ->
                {
                    final Test t = TestTests.createTest("abc");
                    test.assertThrows(() -> t.assertThrows(null, new EmptyException()),
                        new PreConditionFailure("action cannot be null."));
                });

                runner.test("with null error", (Test test) ->
                {
                    final Test t = TestTests.createTest("abc");
                    test.assertThrows(() -> t.assertThrows(() -> {}, (Throwable)null),
                        new PreConditionFailure("expectedException cannot be null."));
                });

                runner.test("with action that doesn't throw and expected error with no message", (Test test) ->
                {
                    final Test t = TestTests.createTest("abc");
                    test.assertThrows(() -> t.assertThrows(() -> {}, new EmptyException()),
                        new TestError("abc", "Expected a qub.EmptyException to be thrown with no message."));
                });

                runner.test("with action that doesn't throw and expected error with message", (Test test) ->
                {
                    final Test t = TestTests.createTest("abcd");
                    test.assertThrows(() -> t.assertThrows(() -> {}, new NotFoundException("blah")),
                        new TestError("abcd", "Expected a qub.NotFoundException to be thrown with the message \"blah\"."));
                });

                runner.test("with action that throws a different error", (Test test) ->
                {
                    final Test t = TestTests.createTest("abcd");
                    test.assertThrows(() -> t.assertThrows(() -> { throw new NullPointerException(); }, new NotFoundException("blah")),
                        new TestError(
                            "abcd",
                            TestTests.createErrorMessage(
                                "Message:  Incorrect exception thrown",
                                "Expected: qub.NotFoundException: blah",
                                "Actual:   java.lang.NullPointerException"),
                            new NullPointerException()));
                });

                runner.test("with action that throws the same error", (Test test) ->
                {
                    test.assertThrows(() -> { throw new NotFoundException("blah"); }, new NotFoundException("blah"));
                });

                runner.test("with action that throws the same error with a different message", (Test test) ->
                {
                    final Test t = TestTests.createTest("abcd");
                    test.assertThrows(() -> t.assertThrows(() -> { throw new NotFoundException("grapes"); }, new NotFoundException("blah")),
                        new TestError(
                            "abcd",
                            TestTests.createErrorMessage(
                                "Message:  Incorrect exception thrown",
                                "Expected: qub.NotFoundException: blah",
                                "Actual:   qub.NotFoundException: grapes"),
                            new NotFoundException("grapes")));
                });

                runner.test("with action that throws an error derived from the expected error", (Test test) ->
                {
                    final Test t = TestTests.createTest("abcd");
                    test.assertThrows(() -> t.assertThrows(() -> { throw new NotFoundException("blah"); }, new RuntimeException("blah")),
                        new TestError(
                            "abcd",
                            TestTests.createErrorMessage(
                                "Message:  Incorrect exception thrown",
                                "Expected: java.lang.RuntimeException: blah",
                                "Actual:   qub.NotFoundException: blah"),
                            new NotFoundException("blah")));
                });

                runner.test("with action that throws an error derived from the expected error with a different message", (Test test) ->
                {
                    final Test t = TestTests.createTest("abcd");
                    test.assertThrows(() -> t.assertThrows(() -> { throw new NotFoundException("blah"); }, new RuntimeException("blah2")),
                        new TestError(
                            "abcd",
                            TestTests.createErrorMessage(
                                "Message:  Incorrect exception thrown",
                                "Expected: java.lang.RuntimeException: blah2",
                                "Actual:   qub.NotFoundException: blah"),
                            new NotFoundException("blah")));
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
                    final Test t = TestTests.createTest("abcd");
                    test.assertThrows(() -> t.assertThrows(() -> { throw new RuntimeException(new NotFoundException("grapes")); }, new NotFoundException("blah")),
                        new TestError(
                            "abcd",
                            TestTests.createErrorMessage(
                                "Message:  Incorrect exception thrown",
                                "Expected: qub.NotFoundException: blah",
                                "Actual:   java.lang.RuntimeException: qub.NotFoundException: grapes",
                                "  Caused by: qub.NotFoundException: grapes"),
                            new RuntimeException(new NotFoundException("grapes"))));
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
                    final Test t = TestTests.createTest("abcd");
                    test.assertThrows(() -> t.assertThrows(() -> { throw new AwaitException(new NotFoundException("grapes")); }, new NotFoundException("blah")),
                        new TestError(
                            "abcd",
                            TestTests.createErrorMessage(
                                "Message:  Incorrect exception thrown",
                                "Expected: qub.NotFoundException: blah",
                                "Actual:   qub.AwaitException: qub.NotFoundException: grapes",
                                "  Caused by: qub.NotFoundException: grapes"),
                            new AwaitException(new NotFoundException("grapes"))));
                });
            });

            runner.testGroup("assertThrows(Action0,Class<? extends Throwable>)", () ->
            {
                runner.test("with null action", (Test test) ->
                {
                    final Test t = TestTests.createTest("abc");
                    test.assertThrows(() -> t.assertThrows(null, EmptyException.class),
                        new PreConditionFailure("action cannot be null."));
                });

                runner.test("with null error", (Test test) ->
                {
                    final Test t = TestTests.createTest("abc");
                    test.assertThrows(() -> t.assertThrows(() -> {}, (Class<? extends Throwable>)null),
                        new PreConditionFailure("expectedExceptionType cannot be null."));
                });

                runner.test("with action that doesn't throw and expected error with no message", (Test test) ->
                {
                    final Test t = TestTests.createTest("abc");
                    test.assertThrows(() -> t.assertThrows(() -> {}, EmptyException.class),
                        new TestError("abc", "Expected a qub.EmptyException to be thrown."));
                });

                runner.test("with action that doesn't throw and expected error with message", (Test test) ->
                {
                    final Test t = TestTests.createTest("abcd");
                    test.assertThrows(() -> t.assertThrows(() -> {}, NotFoundException.class),
                        new TestError("abcd", "Expected a qub.NotFoundException to be thrown."));
                });

                runner.test("with action that throws a different error", (Test test) ->
                {
                    final Test t = TestTests.createTest("abcd");
                    test.assertThrows(() -> t.assertThrows(() -> { throw new NullPointerException(); }, NotFoundException.class),
                        new TestError(
                            "abcd",
                            TestTests.createErrorMessage(
                                "Message:  Incorrect exception thrown",
                                "Expected: qub.NotFoundException",
                                "Actual:   java.lang.NullPointerException"),
                            new NullPointerException()));
                });

                runner.test("with action that throws the same error type", (Test test) ->
                {
                    final NotFoundException error = new NotFoundException("blah");
                    final NotFoundException thrown = test.assertThrows(() -> { throw error; }, NotFoundException.class);
                    test.assertSame(error, thrown);
                });

                runner.test("with action that throws an error derived from the expected error", (Test test) ->
                {
                    final Test t = TestTests.createTest("abcd");
                    test.assertThrows(() -> t.assertThrows(() -> { throw new NotFoundException("blah"); }, RuntimeException.class),
                        new TestError(
                            "abcd",
                            TestTests.createErrorMessage(
                                "Message:  Incorrect exception thrown",
                                "Expected: java.lang.RuntimeException",
                                "Actual:   qub.NotFoundException: blah"),
                            new NotFoundException("blah")));
                });

                runner.test("with action that throws the same error but is wrapped in a RuntimeException", (Test test) ->
                {
                    final NotFoundException error = new NotFoundException("blah");
                    final NotFoundException thrown = test.assertThrows(() -> { throw new RuntimeException(error); }, NotFoundException.class);
                    test.assertSame(error, thrown);
                });

                runner.test("with action that throws the same error but is wrapped in two RuntimeExceptions", (Test test) ->
                {
                    final NotFoundException error = new NotFoundException("blah");
                    final NotFoundException thrown = test.assertThrows(() -> { throw new RuntimeException(new RuntimeException(error)); }, NotFoundException.class);
                    test.assertSame(error, thrown);
                });

                runner.test("with action that throws the same error but is wrapped in an AwaitException", (Test test) ->
                {
                    final NotFoundException error = new NotFoundException("blah");
                    final NotFoundException thrown = test.assertThrows(() -> { throw new AwaitException(error); }, NotFoundException.class);
                    test.assertSame(error, thrown);
                });

                runner.test("with action that throws the same error but is wrapped in two AwaitExceptions", (Test test) ->
                {
                    final NotFoundException error = new NotFoundException("blah");
                    final NotFoundException thrown = test.assertThrows(() -> { throw new AwaitException(new AwaitException(error)); }, NotFoundException.class);
                    test.assertSame(error, thrown);
                });

                runner.test("with action that throws the same error but is wrapped in a RuntimeException and an AwaitException", (Test test) ->
                {
                    final NotFoundException error = new NotFoundException("blah");
                    final NotFoundException thrown = test.assertThrows(() -> { throw new RuntimeException(new AwaitException(error)); }, NotFoundException.class);
                    test.assertSame(error, thrown);
                });
            });

            runner.testGroup("assertStartsWith(String,String)", () ->
            {
                final Action3<String,String,Throwable> assertStartsWithTest = (String value, String prefix, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(value) + " and " + Strings.escapeAndQuote(prefix), (Test test) ->
                    {
                        final Test t = TestTests.createTest("abc");
                        if (expectedError != null)
                        {
                            test.assertThrows(() -> t.assertStartsWith(value, prefix), expectedError);
                        }
                        else
                        {
                            t.assertStartsWith(value, prefix);
                        }
                    });
                };

                assertStartsWithTest.run(null, null, new TestError("abc", TestTests.createErrorMessage(
                    "Expected null to start with null.")));
                assertStartsWithTest.run(null, "", new TestError("abc", TestTests.createErrorMessage(
                    "Expected null to start with \"\".")));
                assertStartsWithTest.run(null, "ba", new TestError("abc", TestTests.createErrorMessage(
                    "Expected null to start with \"ba\".")));
                assertStartsWithTest.run("", null, new TestError("abc", TestTests.createErrorMessage(
                    "Expected \"\" to start with null.")));
                assertStartsWithTest.run("", "", new TestError("abc", TestTests.createErrorMessage(
                    "Expected \"\" to start with \"\".")));
                assertStartsWithTest.run("", "ba", new TestError("abc", TestTests.createErrorMessage(
                    "Expected \"\" to start with \"ba\".")));
                assertStartsWithTest.run("apples", null, new TestError("abc", TestTests.createErrorMessage(
                    "Expected \"apples\" to start with null.")));
                assertStartsWithTest.run("apples", "", new TestError("abc", TestTests.createErrorMessage(
                    "Expected \"apples\" to start with \"\".")));
                assertStartsWithTest.run("apples", "ba", new TestError("abc", TestTests.createErrorMessage(
                    "Expected \"apples\" to start with \"ba\".")));
                assertStartsWithTest.run("apples", "Ap", new TestError("abc", TestTests.createErrorMessage(
                    "Expected \"apples\" to start with \"Ap\".")));
                assertStartsWithTest.run("apples", "ap", null);
            });

            runner.testGroup("assertStartsWith(String,String,CharacterComparer)", () ->
            {
                final Action4<String,String,CharacterComparer,Throwable> assertStartsWithTest = (String value, String prefix, CharacterComparer comparer, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(value) + " and " + Strings.escapeAndQuote(prefix), (Test test) ->
                    {
                        final Test t = TestTests.createTest("abc");
                        if (expectedError != null)
                        {
                            test.assertThrows(() -> t.assertStartsWith(value, prefix, comparer), expectedError);
                        }
                        else
                        {
                            t.assertStartsWith(value, prefix, comparer);
                        }
                    });
                };

                assertStartsWithTest.run(null, null, null, new PreConditionFailure("characterComparer cannot be null."));
                assertStartsWithTest.run(null, null, CharacterComparer.Exact, new TestError("abc", TestTests.createErrorMessage(
                    "Expected null to start with null.")));
                assertStartsWithTest.run(null, null, CharacterComparer.CaseInsensitive, new TestError("abc", TestTests.createErrorMessage(
                    "Expected null to start with null.")));
                assertStartsWithTest.run(null, "", CharacterComparer.Exact, new TestError("abc", TestTests.createErrorMessage(
                    "Expected null to start with \"\".")));
                assertStartsWithTest.run(null, "ba", CharacterComparer.Exact, new TestError("abc", TestTests.createErrorMessage(
                    "Expected null to start with \"ba\".")));
                assertStartsWithTest.run("", null, CharacterComparer.Exact, new TestError("abc", TestTests.createErrorMessage(
                    "Expected \"\" to start with null.")));
                assertStartsWithTest.run("", "", CharacterComparer.Exact, new TestError("abc", TestTests.createErrorMessage(
                    "Expected \"\" to start with \"\".")));
                assertStartsWithTest.run("", "ba", CharacterComparer.Exact, new TestError("abc", TestTests.createErrorMessage(
                    "Expected \"\" to start with \"ba\".")));
                assertStartsWithTest.run("apples", null, CharacterComparer.Exact, new TestError("abc", TestTests.createErrorMessage(
                    "Expected \"apples\" to start with null.")));
                assertStartsWithTest.run("apples", "", CharacterComparer.Exact, new TestError("abc", TestTests.createErrorMessage(
                    "Expected \"apples\" to start with \"\".")));
                assertStartsWithTest.run("apples", "ba", CharacterComparer.Exact, new TestError("abc", TestTests.createErrorMessage(
                    "Expected \"apples\" to start with \"ba\".")));
                assertStartsWithTest.run("apples", "Ap", CharacterComparer.Exact, new TestError("abc", TestTests.createErrorMessage(
                    "Expected \"apples\" to start with \"Ap\".")));
                assertStartsWithTest.run("apples", "Ap", CharacterComparer.CaseInsensitive, null);
                assertStartsWithTest.run("apples", "ap", CharacterComparer.Exact, null);
                assertStartsWithTest.run("apples", "ap", CharacterComparer.CaseInsensitive, null);
            });

            runner.testGroup("assertEndsWith(String,String)", () ->
            {
                final Action3<String,String,Throwable> assertEndsWithTest = (String value, String prefix, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(value) + " and " + Strings.escapeAndQuote(prefix), (Test test) ->
                    {
                        final Test t = TestTests.createTest("abc");
                        if (expectedError != null)
                        {
                            test.assertThrows(() -> t.assertEndsWith(value, prefix), expectedError);
                        }
                        else
                        {
                            t.assertEndsWith(value, prefix);
                        }
                    });
                };

                assertEndsWithTest.run(null, null, new PreConditionFailure("text cannot be null."));
                assertEndsWithTest.run(null, "", new PreConditionFailure("text cannot be null."));
                assertEndsWithTest.run(null, "ba", new PreConditionFailure("text cannot be null."));
                assertEndsWithTest.run("", null, new PreConditionFailure("suffix cannot be null."));
                assertEndsWithTest.run("", "", new PreConditionFailure("suffix cannot be empty."));
                assertEndsWithTest.run("", "ba", new TestError("abc", TestTests.createErrorMessage(
                    "Expected \"\" to end with \"ba\".")));
                assertEndsWithTest.run("apples", null, new PreConditionFailure("suffix cannot be null."));
                assertEndsWithTest.run("apples", "", new PreConditionFailure("suffix cannot be empty."));
                assertEndsWithTest.run("apples", "ba", new TestError("abc", TestTests.createErrorMessage(
                    "Expected \"apples\" to end with \"ba\".")));
                assertEndsWithTest.run("apples", "LES", new TestError("abc", TestTests.createErrorMessage(
                    "Expected \"apples\" to end with \"LES\".")));
                assertEndsWithTest.run("apples", "les", null);
            });

            runner.testGroup("assertEndsWith(String,String,String)", () ->
            {
                final Action3<String,String,Throwable> assertEndsWithTest = (String value, String prefix, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(value) + " and " + Strings.escapeAndQuote(prefix), (Test test) ->
                    {
                        final Test t = TestTests.createTest("abc");
                        if (expectedError != null)
                        {
                            test.assertThrows(() -> t.assertEndsWith(value, prefix, "hello"), expectedError);
                        }
                        else
                        {
                            t.assertEndsWith(value, prefix, "hello");
                        }
                    });
                };

                assertEndsWithTest.run(null, null, new PreConditionFailure("text cannot be null."));
                assertEndsWithTest.run(null, "", new PreConditionFailure("text cannot be null."));
                assertEndsWithTest.run(null, "ba", new PreConditionFailure("text cannot be null."));
                assertEndsWithTest.run("", null, new PreConditionFailure("suffix cannot be null."));
                assertEndsWithTest.run("", "", new PreConditionFailure("suffix cannot be empty."));
                assertEndsWithTest.run("", "ba", new TestError("abc", TestTests.createErrorMessage(
                    "hello",
                    "Expected \"\" to end with \"ba\".")));
                assertEndsWithTest.run("apples", null, new PreConditionFailure("suffix cannot be null."));
                assertEndsWithTest.run("apples", "", new PreConditionFailure("suffix cannot be empty."));
                assertEndsWithTest.run("apples", "ba", new TestError("abc", TestTests.createErrorMessage(
                    "hello",
                    "Expected \"apples\" to end with \"ba\".")));
                assertEndsWithTest.run("apples", "LES", new TestError("abc", TestTests.createErrorMessage(
                    "hello",
                    "Expected \"apples\" to end with \"LES\".")));
                assertEndsWithTest.run("apples", "les", null);
            });

            runner.testGroup("assertContains(String,String)", () ->
            {
                final Action3<String,String,Throwable> assertContainsTest = (String value, String prefix, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(value) + " and " + Strings.escapeAndQuote(prefix), (Test test) ->
                    {
                        final Test t = TestTests.createTest("abc");
                        if (expectedError != null)
                        {
                            test.assertThrows(() -> t.assertContains(value, prefix), expectedError);
                        }
                        else
                        {
                            t.assertContains(value, prefix);
                        }
                    });
                };

                assertContainsTest.run(null, null, new TestError("abc", TestTests.createErrorMessage(
                    "Expected null to contain null.")));
                assertContainsTest.run(null, "", new TestError("abc", TestTests.createErrorMessage(
                    "Expected null to contain \"\".")));
                assertContainsTest.run(null, "ba", new TestError("abc", TestTests.createErrorMessage(
                    "Expected null to contain \"ba\".")));
                assertContainsTest.run("", null, new TestError("abc", TestTests.createErrorMessage(
                    "Expected \"\" to contain null.")));
                assertContainsTest.run("", "", new TestError("abc", TestTests.createErrorMessage(
                    "Expected \"\" to contain \"\".")));
                assertContainsTest.run("", "ba", new TestError("abc", TestTests.createErrorMessage(
                    "Expected \"\" to contain \"ba\".")));
                assertContainsTest.run("apples", null, new TestError("abc", TestTests.createErrorMessage(
                    "Expected \"apples\" to contain null.")));
                assertContainsTest.run("apples", "", new TestError("abc", TestTests.createErrorMessage(
                    "Expected \"apples\" to contain \"\".")));
                assertContainsTest.run("apples", "ba", new TestError("abc", TestTests.createErrorMessage(
                    "Expected \"apples\" to contain \"ba\".")));
                assertContainsTest.run("apples", "LES", new TestError("abc", TestTests.createErrorMessage(
                    "Expected \"apples\" to contain \"LES\".")));
                assertContainsTest.run("apples", "les", null);
            });

            runner.testGroup("assertContains(String,String,String)", () ->
            {
                final Action3<String,String,Throwable> assertContainsTest = (String value, String prefix, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(value) + " and " + Strings.escapeAndQuote(prefix), (Test test) ->
                    {
                        final Test t = TestTests.createTest("abc");
                        if (expectedError != null)
                        {
                            test.assertThrows(() -> t.assertContains(value, prefix, "hello"), expectedError);
                        }
                        else
                        {
                            t.assertContains(value, prefix, "hello");
                        }
                    });
                };

                assertContainsTest.run(null, null, new TestError("abc", TestTests.createErrorMessage(
                    "hello",
                    "Expected null to contain null.")));
                assertContainsTest.run(null, "", new TestError("abc", TestTests.createErrorMessage(
                    "hello",
                    "Expected null to contain \"\".")));
                assertContainsTest.run(null, "ba", new TestError("abc", TestTests.createErrorMessage(
                    "hello",
                    "Expected null to contain \"ba\".")));
                assertContainsTest.run("", null, new TestError("abc", TestTests.createErrorMessage(
                    "hello",
                    "Expected \"\" to contain null.")));
                assertContainsTest.run("", "", new TestError("abc", TestTests.createErrorMessage(
                    "hello",
                    "Expected \"\" to contain \"\".")));
                assertContainsTest.run("", "ba", new TestError("abc", TestTests.createErrorMessage(
                    "hello",
                    "Expected \"\" to contain \"ba\".")));
                assertContainsTest.run("apples", null, new TestError("abc", TestTests.createErrorMessage(
                    "hello",
                    "Expected \"apples\" to contain null.")));
                assertContainsTest.run("apples", "", new TestError("abc", TestTests.createErrorMessage(
                    "hello",
                    "Expected \"apples\" to contain \"\".")));
                assertContainsTest.run("apples", "ba", new TestError("abc", TestTests.createErrorMessage(
                    "hello",
                    "Expected \"apples\" to contain \"ba\".")));
                assertContainsTest.run("apples", "LES", new TestError("abc", TestTests.createErrorMessage(
                    "hello",
                    "Expected \"apples\" to contain \"LES\".")));
                assertContainsTest.run("apples", "les", null);
            });

            runner.testGroup("assertOneOf(T[],T)", () ->
            {
                final Action3<String[],String,Throwable> assertOneOfTest = (String[] possibleValues, String value, Throwable expectedError) ->
                {
                    runner.test("with " + Objects.toString(possibleValues) + " and " + value, (Test test) ->
                    {
                        final Test t = TestTests.createTest("abc");
                        if (expectedError != null)
                        {
                            test.assertThrows(() -> t.assertOneOf(possibleValues, value), expectedError);
                        }
                        else
                        {
                            t.assertOneOf(possibleValues, value);
                        }
                    });
                };

                assertOneOfTest.run(null, null, new PreConditionFailure("possibleValues cannot be null."));
                assertOneOfTest.run(new String[0], null, new PreConditionFailure("possibleValues cannot be empty."));
                assertOneOfTest.run(new String[] { null }, null, null);
                assertOneOfTest.run(new String[] { "a" }, null, new TestError("abc", TestTests.createErrorMessage(
                    "Actual value (null) must be \"a\".")));
                assertOneOfTest.run(new String[] { "a", "b" }, null, new TestError("abc", TestTests.createErrorMessage(
                    "Actual value (null) must be \"a\" or \"b\".")));
                assertOneOfTest.run(new String[] { "a", "b", "c" }, null, new TestError("abc", TestTests.createErrorMessage(
                    "Actual value (null) must be \"a\", \"b\", or \"c\".")));

                assertOneOfTest.run(new String[] { null }, "a", new TestError("abc", TestTests.createErrorMessage(
                    "Actual value (\"a\") must be null.")));
                assertOneOfTest.run(new String[] { "a" }, "a", null);
                assertOneOfTest.run(new String[] { "a" }, "z", new TestError("abc", TestTests.createErrorMessage(
                    "Actual value (\"z\") must be \"a\".")));
                assertOneOfTest.run(new String[] { "a", "b" }, "a", null);
                assertOneOfTest.run(new String[] { "a", "b" }, "b", null);
                assertOneOfTest.run(new String[] { "a", "b" }, "z", new TestError("abc", TestTests.createErrorMessage(
                    "Actual value (\"z\") must be \"a\" or \"b\".")));
                assertOneOfTest.run(new String[] { "a", "b", "c" }, "a", null);
                assertOneOfTest.run(new String[] { "a", "b", "c" }, "b", null);
                assertOneOfTest.run(new String[] { "a", "b", "c" }, "c", null);
                assertOneOfTest.run(new String[] { "a", "b", "c" }, "z", new TestError("abc", TestTests.createErrorMessage(
                    "Actual value (\"z\") must be \"a\", \"b\", or \"c\".")));
            });

            runner.testGroup("assertOneOf(Iterable<T>,T)", () ->
            {
                final Action3<Iterable<String>,String,Throwable> assertOneOfTest = (Iterable<String> possibleValues, String value, Throwable expectedError) ->
                {
                    runner.test("with " + Objects.toString(possibleValues) + " and " + value, (Test test) ->
                    {
                        final Test t = TestTests.createTest("abc");
                        if (expectedError != null)
                        {
                            test.assertThrows(() -> t.assertOneOf(possibleValues, value), expectedError);
                        }
                        else
                        {
                            t.assertOneOf(possibleValues, value);
                        }
                    });
                };

                assertOneOfTest.run(null, null, new PreConditionFailure("possibleValues cannot be null."));
                assertOneOfTest.run(Iterable.create(), null, new PreConditionFailure("possibleValues cannot be empty."));
                assertOneOfTest.run(Iterable.create((String)null), null, null);
                assertOneOfTest.run(Iterable.create("a"), null, new TestError("abc", TestTests.createErrorMessage(
                    "Actual value (null) must be \"a\".")));
                assertOneOfTest.run(Iterable.create("a", "b"), null, new TestError("abc", TestTests.createErrorMessage(
                    "Actual value (null) must be \"a\" or \"b\".")));
                assertOneOfTest.run(Iterable.create("a", "b", "c"), null, new TestError("abc", TestTests.createErrorMessage(
                    "Actual value (null) must be \"a\", \"b\", or \"c\".")));

                assertOneOfTest.run(Iterable.create((String)null), "a", new TestError("abc", TestTests.createErrorMessage(
                    "Actual value (\"a\") must be null.")));
                assertOneOfTest.run(Iterable.create("a"), "a", null);
                assertOneOfTest.run(Iterable.create("a"), "z", new TestError("abc", TestTests.createErrorMessage(
                    "Actual value (\"z\") must be \"a\".")));
                assertOneOfTest.run(Iterable.create("a", "b"), "a", null);
                assertOneOfTest.run(Iterable.create("a", "b"), "b", null);
                assertOneOfTest.run(Iterable.create("a", "b"), "z", new TestError("abc", TestTests.createErrorMessage(
                    "Actual value (\"z\") must be \"a\" or \"b\".")));
                assertOneOfTest.run(Iterable.create("a", "b", "c"), "a", null);
                assertOneOfTest.run(Iterable.create("a", "b", "c"), "b", null);
                assertOneOfTest.run(Iterable.create("a", "b", "c"), "c", null);
                assertOneOfTest.run(Iterable.create("a", "b", "c"), "z", new TestError("abc", TestTests.createErrorMessage(
                    "Actual value (\"z\") must be \"a\", \"b\", or \"c\".")));
            });

            runner.testGroup("fail(String)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final Test t = TestTests.createTest("abc");
                    test.assertThrows(() -> t.fail((String)null), new PreConditionFailure("message cannot be null."));
                });

                runner.test("with empty", (Test test) ->
                {
                    final Test t = TestTests.createTest("abc");
                    test.assertThrows(() -> t.fail(""), new PreConditionFailure("message cannot be empty."));
                });

                runner.test("with non-empty", (Test test) ->
                {
                    final Test t = TestTests.createTest("abc");
                    test.assertThrows(() -> t.fail("hello"), new TestError("abc", "hello"));
                });
            });

            runner.testGroup("fail(Throwable)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final Test t = TestTests.createTest("abc");
                    test.assertThrows(() -> t.fail((Throwable)null),
                        new PreConditionFailure("e cannot be null."));
                });

                runner.test("with null message", (Test test) ->
                {
                    final Test t = TestTests.createTest("abc");
                    test.assertThrows(() -> t.fail(new RuntimeException((String)null)),
                        new TestError("abc", "RuntimeException"));
                });

                runner.test("with empty message", (Test test) ->
                {
                    final Test t = TestTests.createTest("abc");
                    test.assertThrows(() -> t.fail(new RuntimeException("")),
                        new TestError("abc", "RuntimeException"));
                });

                runner.test("with non-empty message", (Test test) ->
                {
                    final Test t = TestTests.createTest("abc");
                    test.assertThrows(() -> t.fail(new RuntimeException("hello")),
                        new TestError("abc", "RuntimeException: hello"));
                });
            });
        });
    }

    static Test createTest(String testName)
    {
        return Test.create(testName, null, null);
    }

    public static String createErrorMessage(String... errorMessageLines)
    {
        PreCondition.assertNotNullAndNotEmpty(errorMessageLines, "errorMessageLines");

        return TestTests.createErrorMessage(Iterable.create(errorMessageLines));
    }

    public static String createErrorMessage(Iterable<String> errorMessageLines)
    {
        PreCondition.assertNotNullAndNotEmpty(errorMessageLines, "errorMessageLines");

        return Strings.join("\n", errorMessageLines);
    }
}
