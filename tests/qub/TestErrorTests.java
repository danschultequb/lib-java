package qub;

public interface TestErrorTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(TestError.class, () ->
        {
            runner.testGroup("constructor(String,String)", () ->
            {
                final Action3<String,String,Throwable> constructorErrorTest = (String testScope, String errorMessage, Throwable expected) ->
                {
                    runner.test("with " + English.andList(Iterable.create(testScope, errorMessage).map(Strings::escapeAndQuote)), (Test test) ->
                    {
                        test.assertThrows(() -> new TestError(testScope, errorMessage),
                            expected);
                    });
                };

                constructorErrorTest.run(null, null, new PreConditionFailure("testScope cannot be null."));
                constructorErrorTest.run("", null, new PreConditionFailure("testScope cannot be empty."));
                constructorErrorTest.run("hello", null, new PreConditionFailure("errorMessage cannot be null."));
                constructorErrorTest.run("hello", "", new PreConditionFailure("errorMessage cannot be empty."));

                runner.test("with non-empty and non-empty", (Test test) ->
                {
                    final TestError error = new TestError("hello", "there");
                    test.assertNotNull(error);
                    test.assertEqual("hello", error.getTestScope());
                    test.assertEqual("there", error.getErrorMessage());
                    test.assertEqual("hello\nthere\n", error.getMessage());
                });

                runner.test("with non-empty and multi-line non-empty", (Test test) ->
                {
                    final TestError error = new TestError("hello", "there\nagain");
                    test.assertNotNull(error);
                    test.assertEqual("hello", error.getTestScope());
                    test.assertEqual("there\nagain", error.getErrorMessage());
                    test.assertEqual("hello\nthere\nagain\n", error.getMessage());
                });
            });

            runner.testGroup("constructor(String,String,Throwable)", () ->
            {
                final Action4<String,String,Throwable,Throwable> constructorErrorTest = (String testScope, String errorMessage, Throwable cause, Throwable expected) ->
                {
                    runner.test("with " + English.andList(List.create(Iterable.create(testScope, errorMessage).map(Strings::escapeAndQuote)).add(Types.getTypeName(cause))), (Test test) ->
                    {
                        test.assertThrows(() -> new TestError(testScope, errorMessage, cause),
                            expected);
                    });
                };

                constructorErrorTest.run(null, null, null, new PreConditionFailure("testScope cannot be null."));
                constructorErrorTest.run("", null, null, new PreConditionFailure("testScope cannot be empty."));
                constructorErrorTest.run("hello", null, null, new PreConditionFailure("errorMessage cannot be null."));
                constructorErrorTest.run("hello", "", null, new PreConditionFailure("errorMessage cannot be empty."));

                runner.test("with non-empty, non-empty, and null", (Test test) ->
                {
                    final TestError error = new TestError("hello", "there", null);
                    test.assertNotNull(error);
                    test.assertEqual("hello", error.getTestScope());
                    test.assertEqual("there", error.getErrorMessage());
                    test.assertEqual("hello\nthere\n", error.getMessage());
                    test.assertNull(error.getCause());
                });

                runner.test("with non-empty, multi-line non-empty, and null", (Test test) ->
                {
                    final TestError error = new TestError("hello", "there\nagain", null);
                    test.assertNotNull(error);
                    test.assertEqual("hello", error.getTestScope());
                    test.assertEqual("there\nagain", error.getErrorMessage());
                    test.assertEqual("hello\nthere\nagain\n", error.getMessage());
                    test.assertNull(error.getCause());
                });

                runner.test("with non-empty, non-empty, and non-null", (Test test) ->
                {
                    final TestError error = new TestError("hello", "there", new NotFoundException("oops"));
                    test.assertNotNull(error);
                    test.assertEqual("hello", error.getTestScope());
                    test.assertEqual("there", error.getErrorMessage());
                    test.assertEqual("hello\nthere\n", error.getMessage());
                    test.assertEqual(new NotFoundException("oops"), error.getCause());
                });

                runner.test("with non-empty, multi-line non-empty, and non-null", (Test test) ->
                {
                    final TestError error = new TestError("hello", "there\nagain", new NotFoundException("oops"));
                    test.assertNotNull(error);
                    test.assertEqual("hello", error.getTestScope());
                    test.assertEqual("there\nagain", error.getErrorMessage());
                    test.assertEqual("hello\nthere\nagain\n", error.getMessage());
                    test.assertEqual(new NotFoundException("oops"), error.getCause());
                });
            });

            runner.testGroup("equals(Object)", () ->
            {
                final Action3<TestError,Object,Boolean> equalsTest = (TestError error, Object rhs, Boolean expected) ->
                {
                    runner.test("with " + English.andList(Strings.escapeAndQuote(error), Strings.escapeAndQuote(rhs)), (Test test) ->
                    {
                        test.assertEqual(expected, error.equals(rhs));
                    });
                };

                equalsTest.run(new TestError("a", "b"), null, false);
                equalsTest.run(new TestError("a", "b"), "a", false);
                equalsTest.run(new TestError("a", "b"), new TestError("b", "b"), false);
                equalsTest.run(new TestError("a", "b"), new TestError("a", "c"), false);
                equalsTest.run(new TestError("a", "b"), new TestError("a", "b"), true);
                equalsTest.run(new TestError("a", "b\nc"), new TestError("a", "b\nc"), true);
                equalsTest.run(new TestError("a", "b\nc"), new TestError("a", "b\nc", new NotFoundException("oops")), false);
                equalsTest.run(new TestError("a", "b\nc", new NotFoundException("oops")), new TestError("a", "b\nc"), false);
                equalsTest.run(new TestError("a", "b\nc", new NotFoundException("oops")), new TestError("a", "b\nc", new NotFoundException("abc")), false);
                equalsTest.run(new TestError("a", "b\nc", new NotFoundException("oops")), new TestError("a", "b\nc", new NotFoundException("oops")), true);
            });

            runner.testGroup("equals(TestError)", () ->
            {
                final Action3<TestError,TestError,Boolean> equalsTest = (TestError error, TestError rhs, Boolean expected) ->
                {
                    runner.test("with " + English.andList(Strings.escapeAndQuote(error), Strings.escapeAndQuote(rhs)), (Test test) ->
                    {
                        test.assertEqual(expected, error.equals(rhs));
                    });
                };

                equalsTest.run(new TestError("a", "b"), null, false);
                equalsTest.run(new TestError("a", "b"), new TestError("b", "b"), false);
                equalsTest.run(new TestError("a", "b"), new TestError("a", "c"), false);
                equalsTest.run(new TestError("a", "b"), new TestError("a", "b"), true);
                equalsTest.run(new TestError("a", "b\nc"), new TestError("a", "b\nc"), true);
                equalsTest.run(new TestError("a", "b\nc"), new TestError("a", "b\nc", new NotFoundException("oops")), false);
                equalsTest.run(new TestError("a", "b\nc", new NotFoundException("oops")), new TestError("a", "b\nc"), false);
                equalsTest.run(new TestError("a", "b\nc", new NotFoundException("oops")), new TestError("a", "b\nc", new NotFoundException("abc")), false);
                equalsTest.run(new TestError("a", "b\nc", new NotFoundException("oops")), new TestError("a", "b\nc", new NotFoundException("oops")), true);
            });
        });
    }
}
