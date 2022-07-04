package qub;

public interface CommandLineArgumentsTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(CommandLineArguments.class, () ->
        {
            runner.testGroup("get(int)", () ->
            {
                runner.test("with negative index", (Test test) ->
                {
                    final CommandLineArguments arguments = CommandLineArguments.create();
                    test.assertThrows(() -> arguments.get(-1),
                        new PreConditionFailure("Indexable length (0) must be greater than or equal to 1."));
                });

                runner.test("with 0 index on empty CommandLineArguments", (Test test) ->
                {
                    final CommandLineArguments arguments = CommandLineArguments.create();
                    test.assertThrows(() -> arguments.get(0),
                        new PreConditionFailure("Indexable length (0) must be greater than or equal to 1."));
                });

                runner.test("with 0 index on non-empty CommandLineArguments", (Test test) ->
                {
                    final CommandLineArguments arguments = CommandLineArguments.create("a", "b", "c");
                    test.assertEqual(new CommandLineArgument("", "a"), arguments.get(0));
                });
            });

            runner.testGroup("toString()", () ->
            {
                runner.test("with empty CommandLineArguments", (Test test) ->
                {
                    final CommandLineArguments arguments = CommandLineArguments.create();
                    test.assertEqual("[]", arguments.toString());
                });

                runner.test("with non-empty CommandLineArguments", (Test test) ->
                {
                    final CommandLineArguments arguments = CommandLineArguments.create("a", "b", "c");
                    test.assertEqual("[a,b,c]", arguments.toString());
                });
            });

            runner.testGroup("equals(Object)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final CommandLineArguments arguments = CommandLineArguments.create();
                    test.assertFalse(arguments.equals((Object)null));
                });

                runner.test("with non-CommandLineArguments object", (Test test) ->
                {
                    final CommandLineArguments arguments = CommandLineArguments.create();
                    test.assertFalse(arguments.equals((Object)"hello"));
                });

                runner.test("with different CommandLineArguments object", (Test test) ->
                {
                    final CommandLineArguments arguments = CommandLineArguments.create("a", "b");
                    test.assertFalse(arguments.equals((Object)CommandLineArguments.create("a", "b", "c")));
                });

                runner.test("with equal CommandLineArguments object", (Test test) ->
                {
                    final CommandLineArguments arguments = CommandLineArguments.create();
                    test.assertTrue(arguments.equals((Object)CommandLineArguments.create()));
                });
            });

            runner.testGroup("addNamedArgument(String)", () ->
            {
                runner.test("with null name", (Test test) ->
                {
                    final CommandLineArguments arguments = CommandLineArguments.create();
                    test.assertThrows(() -> arguments.addNamedArgument(null),
                        new PreConditionFailure("argumentName cannot be null."));
                });

                runner.test("with empty name", (Test test) ->
                {
                    final CommandLineArguments arguments = CommandLineArguments.create();
                    test.assertThrows(() -> arguments.addNamedArgument(""),
                        new PreConditionFailure("argumentName cannot be empty."));
                });

                runner.test("with non-empty name", (Test test) ->
                {
                    final CommandLineArguments arguments = CommandLineArguments.create();
                    test.assertSame(arguments, arguments.addNamedArgument("abc"));
                    test.assertEqual("", arguments.getNamedValue("abc").await());
                    test.assertEqual("[--abc]", arguments.toString());
                });
            });

            runner.testGroup("addNamedArgument(String,String)", () ->
            {
                runner.test("with null name", (Test test) ->
                {
                    final CommandLineArguments arguments = CommandLineArguments.create();
                    test.assertThrows(() -> arguments.addNamedArgument(null, "b"),
                        new PreConditionFailure("argumentName cannot be null."));
                });

                runner.test("with empty name", (Test test) ->
                {
                    final CommandLineArguments arguments = CommandLineArguments.create();
                    test.assertThrows(() -> arguments.addNamedArgument("", "b"),
                        new PreConditionFailure("argumentName cannot be empty."));
                });

                runner.test("with null value", (Test test) ->
                {
                    final CommandLineArguments arguments = CommandLineArguments.create();
                    test.assertThrows(() -> arguments.addNamedArgument("a", null),
                        new PreConditionFailure("argumentValue cannot be null."));
                });

                runner.test("with empty value", (Test test) ->
                {
                    final CommandLineArguments arguments = CommandLineArguments.create();
                    test.assertSame(arguments, arguments.addNamedArgument("a", ""));
                    test.assertEqual("", arguments.getNamedValue("a").await());
                    test.assertEqual("[--a]", arguments.toString());
                });

                runner.test("with non-empty value", (Test test) ->
                {
                    final CommandLineArguments arguments = CommandLineArguments.create();
                    test.assertSame(arguments, arguments.addNamedArgument("abc", "xyz"));
                    test.assertEqual("xyz", arguments.getNamedValue("abc").await());
                    test.assertEqual("[--abc=xyz]", arguments.toString());
                });
            });

            runner.testGroup("addAnonymousArgument(String)", () ->
            {
                runner.test("with null value", (Test test) ->
                {
                    final CommandLineArguments arguments = CommandLineArguments.create();
                    test.assertThrows(() -> arguments.addAnonymousArgument(null),
                        new PreConditionFailure("argumentValue cannot be null."));
                });

                runner.test("with empty value", (Test test) ->
                {
                    final CommandLineArguments arguments = CommandLineArguments.create();
                    test.assertThrows(() -> arguments.addAnonymousArgument(""),
                        new PreConditionFailure("argumentValue cannot be empty."));
                });

                runner.test("with non-empty value", (Test test) ->
                {
                    final CommandLineArguments arguments = CommandLineArguments.create();
                    test.assertSame(arguments, arguments.addAnonymousArgument("abc"));
                    test.assertEqual("abc", arguments.getAnonymousValue(0).await());
                    test.assertEqual("[abc]", arguments.toString());
                });
            });

            runner.testGroup("getNamedValue(String)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final CommandLineArguments arguments = CommandLineArguments.create();
                    test.assertThrows(() -> arguments.getNamedValue(null),
                        new PreConditionFailure("argumentName cannot be null."));
                });

                runner.test("with empty", (Test test) ->
                {
                    final CommandLineArguments arguments = CommandLineArguments.create();
                    test.assertThrows(() -> arguments.getNamedValue(""),
                        new PreConditionFailure("argumentName cannot be empty."));
                });

                runner.test("with not found", (Test test) ->
                {
                    final CommandLineArguments arguments = CommandLineArguments.create();
                    test.assertThrows(() -> arguments.getNamedValue("abc").await(),
                        new NotFoundException("Could not find command-line arguments with the name \"abc\"."));
                });

                runner.test("with found", (Test test) ->
                {
                    final CommandLineArguments arguments = CommandLineArguments.create("--abc=20");
                    test.assertEqual("20", arguments.getNamedValue("abc").await());
                });

                runner.test("with multiple found", (Test test) ->
                {
                    final CommandLineArguments arguments = CommandLineArguments.create("--abc=20", "--abc=21");
                    test.assertEqual("20", arguments.getNamedValue("abc").await());
                });

                runner.test("with found but different case", (Test test) ->
                {
                    final CommandLineArguments arguments = CommandLineArguments.create("--abc=20");
                    test.assertEqual("20", arguments.getNamedValue("ABC").await());
                });
            });

            runner.testGroup("getNamedValues(String)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final CommandLineArguments arguments = CommandLineArguments.create();
                    test.assertThrows(() -> arguments.getNamedValues(null),
                        new PreConditionFailure("argumentName cannot be null."));
                });

                runner.test("with empty", (Test test) ->
                {
                    final CommandLineArguments arguments = CommandLineArguments.create();
                    test.assertThrows(() -> arguments.getNamedValues(""),
                        new PreConditionFailure("argumentName cannot be empty."));
                });

                runner.test("with not found", (Test test) ->
                {
                    final CommandLineArguments arguments = CommandLineArguments.create();
                    test.assertThrows(() -> arguments.getNamedValues("abc").await(),
                        new NotFoundException("Could not find command-line arguments with the name \"abc\"."));
                });

                runner.test("with found", (Test test) ->
                {
                    final CommandLineArguments arguments = CommandLineArguments.create("--abc=20");
                    test.assertEqual(Iterable.create("20"), arguments.getNamedValues("abc").await());
                });

                runner.test("with multiple found", (Test test) ->
                {
                    final CommandLineArguments arguments = CommandLineArguments.create("--abc=20", "--abc=21");
                    test.assertEqual(Iterable.create("20", "21"), arguments.getNamedValues("abc").await());
                });

                runner.test("with found but different case", (Test test) ->
                {
                    final CommandLineArguments arguments = CommandLineArguments.create("--abc=20");
                    test.assertEqual(Iterable.create("20"), arguments.getNamedValues("ABC").await());
                });
            });

            runner.testGroup("removeNamedValue(String)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final CommandLineArguments arguments = CommandLineArguments.create();
                    test.assertThrows(() -> arguments.removeNamedValue(null),
                        new PreConditionFailure("argumentName cannot be null."));
                });

                runner.test("with empty", (Test test) ->
                {
                    final CommandLineArguments arguments = CommandLineArguments.create();
                    test.assertThrows(() -> arguments.removeNamedValue(""),
                        new PreConditionFailure("argumentName cannot be empty."));
                });

                runner.test("with not found", (Test test) ->
                {
                    final CommandLineArguments arguments = CommandLineArguments.create();
                    test.assertThrows(() -> arguments.removeNamedValue("abc").await(),
                        new NotFoundException("Could not find a command-line argument with the name \"abc\"."));
                });

                runner.test("with found", (Test test) ->
                {
                    final CommandLineArguments arguments = CommandLineArguments.create("--abc=20", "--def=21");
                    test.assertEqual("20", arguments.removeNamedValue("abc").await());
                    test.assertEqual("[--def=21]", arguments.toString());
                });

                runner.test("with multiple found", (Test test) ->
                {
                    final CommandLineArguments arguments = CommandLineArguments.create("--xyz=false", "--abc=20", "--abc=21");
                    test.assertEqual("20", arguments.removeNamedValue("abc").await());
                    test.assertEqual("[--xyz=false,--abc=21]", arguments.toString());
                });

                runner.test("with found but different case", (Test test) ->
                {
                    final CommandLineArguments arguments = CommandLineArguments.create("--abc=20");
                    test.assertEqual("20", arguments.removeNamedValue("ABC").await());
                    test.assertEqual("[]", arguments.toString());
                });
            });

            runner.testGroup("removeNamedValues(String)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final CommandLineArguments arguments = CommandLineArguments.create();
                    test.assertThrows(() -> arguments.removeNamedValues(null),
                        new PreConditionFailure("argumentName cannot be null."));
                });

                runner.test("with empty", (Test test) ->
                {
                    final CommandLineArguments arguments = CommandLineArguments.create();
                    test.assertThrows(() -> arguments.removeNamedValues(""),
                        new PreConditionFailure("argumentName cannot be empty."));
                });

                runner.test("with not found", (Test test) ->
                {
                    final CommandLineArguments arguments = CommandLineArguments.create();
                    test.assertThrows(() -> arguments.removeNamedValues("abc").await(),
                        new NotFoundException("Could not find a command-line argument with the name \"abc\"."));
                });

                runner.test("with found", (Test test) ->
                {
                    final CommandLineArguments arguments = CommandLineArguments.create("--abc=20", "--def=21");
                    test.assertEqual(Iterable.create("20"), arguments.removeNamedValues("abc").await());
                    test.assertEqual("[--def=21]", arguments.toString());
                });

                runner.test("with multiple found", (Test test) ->
                {
                    final CommandLineArguments arguments = CommandLineArguments.create("--abc=20", "--xyz", "--abc=21");
                    test.assertEqual(Iterable.create("20", "21"), arguments.removeNamedValues("abc").await());
                    test.assertEqual("[--xyz]", arguments.toString());
                });

                runner.test("with found but different case", (Test test) ->
                {
                    final CommandLineArguments arguments = CommandLineArguments.create("--abc=20");
                    test.assertEqual(Iterable.create("20"), arguments.removeNamedValues("ABC").await());
                    test.assertEqual("[]", arguments.toString());
                });
            });

            runner.testGroup("getAnonymousValue(int)", () ->
            {
                runner.test("with negative index", (Test test) ->
                {
                    final CommandLineArguments arguments = CommandLineArguments.create("a", "b", "c");
                    test.assertThrows(() -> arguments.getAnonymousValue(-1),
                        new PreConditionFailure("anonymousValueIndex (-1) must be greater than or equal to 0."));
                });

                runner.test("with 0 when CommandLineArguments is empty", (Test test) ->
                {
                    final CommandLineArguments arguments = CommandLineArguments.create();
                    test.assertThrows(() -> arguments.getAnonymousValue(0).await(),
                        new NotFoundException("Could not find an anonymous command-line argument in the index 0."));
                });

                runner.test("with 0 when CommandLineArguments has no anonymous arguments", (Test test) ->
                {
                    final CommandLineArguments arguments = CommandLineArguments.create("--a=b", "-c=d");
                    test.assertThrows(() -> arguments.getAnonymousValue(0).await(),
                        new NotFoundException("Could not find an anonymous command-line argument in the index 0."));
                });

                runner.test("with 0 when CommandLineArguments has anonymous arguments", (Test test) ->
                {
                    final CommandLineArguments arguments = CommandLineArguments.create("a", "b");
                    test.assertEqual("a", arguments.getAnonymousValue(0).await());
                });

                runner.test("with 0 when CommandLineArguments has anonymous and named arguments", (Test test) ->
                {
                    final CommandLineArguments arguments = CommandLineArguments.create("--xyz", "a", "b");
                    test.assertEqual("a", arguments.getAnonymousValue(0).await());
                });
            });

            runner.testGroup("getAnonymousValues(int)", () ->
            {
                runner.test("with negative index", (Test test) ->
                {
                    final CommandLineArguments arguments = CommandLineArguments.create("a", "b", "c");
                    test.assertThrows(() -> arguments.getAnonymousValues(-1),
                        new PreConditionFailure("anonymousValueIndex (-1) must be greater than or equal to 0."));
                });

                runner.test("with 0 when CommandLineArguments is empty", (Test test) ->
                {
                    final CommandLineArguments arguments = CommandLineArguments.create();
                    test.assertThrows(() -> arguments.getAnonymousValues(0).await(),
                        new NotFoundException("Could not find an anonymous command-line argument at the index 0."));
                });

                runner.test("with 0 when CommandLineArguments has no anonymous arguments", (Test test) ->
                {
                    final CommandLineArguments arguments = CommandLineArguments.create("--a=b", "-c=d");
                    test.assertThrows(() -> arguments.getAnonymousValues(0).await(),
                        new NotFoundException("Could not find an anonymous command-line argument at the index 0."));
                });

                runner.test("with 0 when CommandLineArguments has anonymous arguments", (Test test) ->
                {
                    final CommandLineArguments arguments = CommandLineArguments.create("a", "b");
                    test.assertEqual(Iterable.create("a", "b"), arguments.getAnonymousValues(0).await());
                });

                runner.test("with 0 when CommandLineArguments has anonymous and named arguments", (Test test) ->
                {
                    final CommandLineArguments arguments = CommandLineArguments.create("--xyz", "a", "b");
                    test.assertEqual(Iterable.create("a", "b"), arguments.getAnonymousValues(0).await());
                });

                runner.test("with 1 when CommandLineArguments has anonymous arguments", (Test test) ->
                {
                    final CommandLineArguments arguments = CommandLineArguments.create("a", "b");
                    test.assertEqual(Iterable.create("b"), arguments.getAnonymousValues(1).await());
                });

                runner.test("with 1 when CommandLineArguments has anonymous and named arguments", (Test test) ->
                {
                    final CommandLineArguments arguments = CommandLineArguments.create("--xyz", "a", "b");
                    test.assertEqual(Iterable.create("b"), arguments.getAnonymousValues(1).await());
                });
            });

            runner.testGroup("removeAnonymousValue(int)", () ->
            {
                runner.test("with negative index", (Test test) ->
                {
                    final CommandLineArguments arguments = CommandLineArguments.create("a", "b", "c");
                    test.assertThrows(() -> arguments.removeAnonymousValue(-1),
                        new PreConditionFailure("anonymousValueIndex (-1) must be greater than or equal to 0."));
                });

                runner.test("with 0 when CommandLineArguments is empty", (Test test) ->
                {
                    final CommandLineArguments arguments = CommandLineArguments.create();
                    test.assertThrows(() -> arguments.removeAnonymousValue(0).await(),
                        new NotFoundException("Could not find an anonymous command-line argument in the index 0."));
                });

                runner.test("with 0 when CommandLineArguments has no anonymous arguments", (Test test) ->
                {
                    final CommandLineArguments arguments = CommandLineArguments.create("--a=b", "-c=d");
                    test.assertThrows(() -> arguments.removeAnonymousValue(0).await(),
                        new NotFoundException("Could not find an anonymous command-line argument in the index 0."));
                });

                runner.test("with 0 when CommandLineArguments has anonymous arguments", (Test test) ->
                {
                    final CommandLineArguments arguments = CommandLineArguments.create("a", "b");
                    test.assertEqual("a", arguments.removeAnonymousValue(0).await());
                    test.assertEqual("[b]", arguments.toString());
                });

                runner.test("with 0 when CommandLineArguments has anonymous and named arguments", (Test test) ->
                {
                    final CommandLineArguments arguments = CommandLineArguments.create("--xyz", "a", "b");
                    test.assertEqual("a", arguments.removeAnonymousValue(0).await());
                    test.assertEqual("[--xyz,b]", arguments.toString());
                });

                runner.test("with 1 when CommandLineArguments has anonymous arguments", (Test test) ->
                {
                    final CommandLineArguments arguments = CommandLineArguments.create("a", "b");
                    test.assertEqual("b", arguments.removeAnonymousValue(1).await());
                    test.assertEqual("[a]", arguments.toString());
                });

                runner.test("with 1 when CommandLineArguments has anonymous and named arguments", (Test test) ->
                {
                    final CommandLineArguments arguments = CommandLineArguments.create("--xyz", "a", "b");
                    test.assertEqual("b", arguments.removeAnonymousValue(1).await());
                    test.assertEqual("[--xyz,a]", arguments.toString());
                });
            });

            runner.testGroup("removeAnonymousValues(int)", () ->
            {
                runner.test("with negative index", (Test test) ->
                {
                    final CommandLineArguments arguments = CommandLineArguments.create("a", "b", "c");
                    test.assertThrows(() -> arguments.removeAnonymousValues(-1),
                        new PreConditionFailure("anonymousValueIndex (-1) must be greater than or equal to 0."));
                });

                runner.test("with 0 when CommandLineArguments is empty", (Test test) ->
                {
                    final CommandLineArguments arguments = CommandLineArguments.create();
                    test.assertThrows(() -> arguments.removeAnonymousValues(0).await(),
                        new NotFoundException("Could not find an anonymous command-line argument in the index 0."));
                });

                runner.test("with 0 when CommandLineArguments has no anonymous arguments", (Test test) ->
                {
                    final CommandLineArguments arguments = CommandLineArguments.create("--a=b", "-c=d");
                    test.assertThrows(() -> arguments.removeAnonymousValues(0).await(),
                        new NotFoundException("Could not find an anonymous command-line argument in the index 0."));
                });

                runner.test("with 0 when CommandLineArguments has anonymous arguments", (Test test) ->
                {
                    final CommandLineArguments arguments = CommandLineArguments.create("a", "b");
                    test.assertEqual(Iterable.create("a", "b"), arguments.removeAnonymousValues(0).await());
                    test.assertEqual("[]", arguments.toString());
                });

                runner.test("with 0 when CommandLineArguments has anonymous and named arguments", (Test test) ->
                {
                    final CommandLineArguments arguments = CommandLineArguments.create("--xyz", "a", "b");
                    test.assertEqual(Iterable.create("a", "b"), arguments.removeAnonymousValues(0).await());
                    test.assertEqual("[--xyz]", arguments.toString());
                });

                runner.test("with 1 when CommandLineArguments has anonymous arguments", (Test test) ->
                {
                    final CommandLineArguments arguments = CommandLineArguments.create("a", "b");
                    test.assertEqual(Iterable.create("b"), arguments.removeAnonymousValues(1).await());
                    test.assertEqual("[a]", arguments.toString());
                });

                runner.test("with 1 when CommandLineArguments has anonymous and named arguments", (Test test) ->
                {
                    final CommandLineArguments arguments = CommandLineArguments.create("--xyz", "a", "b");
                    test.assertEqual(Iterable.create("b"), arguments.removeAnonymousValues(1).await());
                    test.assertEqual("[--xyz,a]", arguments.toString());
                });
            });

            runner.testGroup("create(String...)", () ->
            {
                runner.test("with no arguments", (Test test) ->
                {
                    final CommandLineArguments arguments = CommandLineArguments.create();
                    test.assertEqual("[]", arguments.toString());
                });

                runner.test("with null array", (Test test) ->
                {
                    test.assertThrows(() -> CommandLineArguments.create((String[])null),
                        new PreConditionFailure("commandLineArguments cannot be null."));
                });

                runner.test("with empty array", (Test test) ->
                {
                    final CommandLineArguments arguments = CommandLineArguments.create(new String[0]);
                    test.assertNotNull(arguments);
                    test.assertEqual(0, arguments.getCount());
                });

                runner.test("with null argument", (Test test) ->
                {
                    final CommandLineArguments arguments = CommandLineArguments.create(new String[] { null });
                    test.assertNotNull(arguments);
                    test.assertEqual(0, arguments.getCount());
                });

                runner.test("with empty argument", (Test test) ->
                {
                    final CommandLineArguments arguments = CommandLineArguments.create("");
                    test.assertNotNull(arguments);
                    test.assertEqual(0, arguments.getCount());
                });

                runner.test("with one anonymous argument", (Test test) ->
                {
                    final CommandLineArguments arguments = CommandLineArguments.create(new String[] { "hello" });
                    test.assertNotNull(arguments);
                    test.assertEqual(1, arguments.getCount());
                    test.assertEqual("hello", arguments.getAnonymousValue(0).await());
                });

                runner.test("with one forward slash argument", (Test test) ->
                {
                    final CommandLineArguments arguments = CommandLineArguments.create("/");
                    test.assertNotNull(arguments);
                    test.assertEqual(1, arguments.getCount());
                    test.assertEqual("/", arguments.getAnonymousValue(0).await());
                });

                runner.test("with one dash argument", (Test test) ->
                {
                    final CommandLineArguments arguments = CommandLineArguments.create(new String[] { "-" });
                    test.assertNotNull(arguments);
                    test.assertEqual(1, arguments.getCount());
                    test.assertEqual("-", arguments.getAnonymousValue(0).await());
                });

                runner.test("with two dash argument", (Test test) ->
                {
                    final CommandLineArguments arguments = CommandLineArguments.create("--");
                    test.assertNotNull(arguments);
                    test.assertEqual(1, arguments.getCount());
                    test.assertEqual("--", arguments.getAnonymousValue(0).await());
                });

                runner.test("with two anonymous arguments", (Test test) ->
                {
                    final CommandLineArguments arguments = CommandLineArguments.create("hello", "there");
                    test.assertNotNull(arguments);
                    test.assertEqual(2, arguments.getCount());
                    test.assertEqual("hello", arguments.getAnonymousValue(0).await());
                    test.assertEqual("there", arguments.getAnonymousValue(1).await());
                });

                runner.test("with one named argument with forward slash", (Test test) ->
                {
                    final CommandLineArguments arguments = CommandLineArguments.create(new String[] { "/hello=there" });
                    test.assertNotNull(arguments);
                    test.assertEqual(1, arguments.getCount());
                    test.assertEqual("/hello=there", arguments.getAnonymousValue(0).await());
                });

                runner.test("with one named argument with forward slash and no value", (Test test) ->
                {
                    final CommandLineArguments arguments = CommandLineArguments.create(new String[] { "/hello" });
                    test.assertNotNull(arguments);
                    test.assertEqual(1, arguments.getCount());
                    test.assertEqual("/hello", arguments.getAnonymousValue(0).await());
                });

                runner.test("with one named argument with two dashes", (Test test) ->
                {
                    final CommandLineArguments arguments = CommandLineArguments.create(new String[] { "--hello=there" });
                    test.assertNotNull(arguments);
                    test.assertEqual(1, arguments.getCount());
                    test.assertEqual("there", arguments.getNamedValue("hello").await());
                });

                runner.test("with two named arguments with two dashes", (Test test) ->
                {
                    final CommandLineArguments arguments = CommandLineArguments.create(new String[] { "--hello=there", "--name=Dan" });
                    test.assertNotNull(arguments);
                    test.assertEqual(2, arguments.getCount());
                    test.assertEqual("there", arguments.getNamedValue("hello").await());
                    test.assertEqual("Dan", arguments.getNamedValue("name").await());
                });

                runner.test("with two named arguments with one dash", (Test test) ->
                {
                    final CommandLineArguments arguments = CommandLineArguments.create("-hello=there", "-name=Dan");
                    test.assertNotNull(arguments);
                    test.assertEqual(2, arguments.getCount());
                    test.assertEqual("there", arguments.getNamedValue("hello").await());
                    test.assertEqual("Dan", arguments.getNamedValue("name").await());
                });
            });
        });
    }
}
