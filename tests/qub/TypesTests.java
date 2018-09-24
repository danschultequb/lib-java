package qub;

public class TypesTests
{
    public int publicInt;
    protected String protectedString = "testing this";
    private boolean privateBoolean;

    public static void test(TestRunner runner)
    {
        runner.testGroup(Types.class, () ->
        {
            runner.test("constructor()", (Test test) ->
            {
                test.assertNotNull(new Types());
            });

            runner.testGroup("containsMemberVariable(Object,String)", () ->
            {
                runner.test("with null Object", (Test test) ->
                {
                    test.assertThrows(() -> Types.containsMemberVariable(null, "parent"));
                });

                runner.test("with null memberVariableName", (Test test) ->
                {
                    test.assertThrows(() -> Types.containsMemberVariable(test, (String)null));
                });
            });

            runner.testGroup("containsMemberVariable(Object,Function1<java.lang.reflect.Field,Boolean>)", () ->
            {
                runner.test("with null Object", (Test test) ->
                {
                    test.assertThrows(() -> Types.containsMemberVariable((Object)null, (java.lang.reflect.Field memberVariable) -> true));
                });

                runner.test("with null condition", (Test test) ->
                {
                    test.assertThrows(() -> Types.containsMemberVariable(test, (Function1<java.lang.reflect.Field,Boolean>)null));
                });

                runner.test("with non-matching condition", (Test test) ->
                {
                    test.assertFalse(Types.containsMemberVariable(test, (java.lang.reflect.Field memberVariable) -> false));
                });

                runner.test("with matching condition", (Test test) ->
                {
                    test.assertTrue(Types.containsMemberVariable(test, (java.lang.reflect.Field memberVariable) -> true));
                });
            });

            runner.testGroup("getMemberVariable(Object,String)", () ->
            {
                runner.test("with null Object", (Test test) ->
                {
                    test.assertThrows(() -> Types.getMemberVariable((Object)null, "parent"));
                });

                runner.test("with null memberVariableName", (Test test) ->
                {
                    test.assertThrows(() -> Types.getMemberVariable(test, (String)null));
                });

                runner.test("with empty memberVariableName", (Test test) ->
                {
                    test.assertThrows(() -> Types.getMemberVariable(test, ""));
                });

                runner.test("with non-existing memberVariableName", (Test test) ->
                {
                    test.assertThrows(() -> Types.getMemberVariable(test, "abc"));
                });

                runner.test("with existing memberVariableName", (Test test) ->
                {
                    final java.lang.reflect.Field field = Types.getMemberVariable(test, "parentTestGroup");
                    test.assertNotNull(field);
                    test.assertEqual(TestGroup.class, field.getType());
                    test.assertEqual("parentTestGroup", field.getName());
                });
            });

            runner.testGroup("getMemberVariable(Object,Function1<java.lang.reflect.Field,Boolean>)", () ->
            {
                runner.test("with null Object", (Test test) ->
                {
                    test.assertThrows(() -> Types.getMemberVariable((Object)null, memberVariable -> true));
                });

                runner.test("with null condition", (Test test) ->
                {
                    test.assertThrows(() -> Types.getMemberVariable(test, (Function1<java.lang.reflect.Field,Boolean>)null));
                });

                runner.test("with non-matching condition", (Test test) ->
                {
                    test.assertThrows(() -> Types.getMemberVariable(test, memberVariable -> false));
                });

                runner.test("with matching condition", (Test test) ->
                {
                    final java.lang.reflect.Field field = Types.getMemberVariable(test, memberVariable -> memberVariable.getName().equals("parentTestGroup"));
                    test.assertNotNull(field);
                    test.assertEqual(TestGroup.class, field.getType());
                    test.assertEqual("parentTestGroup", field.getName());
                });
            });

            runner.testGroup("getMemberVariableValue(Object,String)", () ->
            {
                runner.test("with null Object", (Test test) ->
                {
                    test.assertThrows(() -> Types.getMemberVariableValue((Object)null, "skip"));
                });

                runner.test("with null memberVariableName", (Test test) ->
                {
                    test.assertThrows(() -> Types.getMemberVariableValue(test, null));
                });

                runner.test("with empty memberVariableName", (Test test) ->
                {
                    test.assertThrows(() -> Types.getMemberVariableValue(test, ""));
                });

                runner.test("with non-existing memberVariableName", (Test test) ->
                {
                    test.assertThrows(() -> Types.getMemberVariableValue(test, "abc"));
                });

                runner.test("with existing public int memberVariable", (Test test) ->
                {
                    final Integer publicInt = (Integer)Types.getMemberVariableValue(new TypesTests(), "publicInt");
                    test.assertNotNull(publicInt);
                    test.assertEqual(0, publicInt);
                });

                runner.test("with existing protected String memberVariable", (Test test) ->
                {
                    final String protectedString = (String)Types.getMemberVariableValue(new TypesTests(), "protectedString");
                    test.assertEqual("testing this", protectedString);
                });

                runner.test("with existing private object memberVariable", (Test test) ->
                {
                    final SingleLinkNode<Integer> node = new SingleLinkNode<>(2);
                    test.assertEqual(2, Types.getMemberVariableValue(node, "value"));
                });
            });

            runner.testGroup("setMemberVariableValue(Object,String,Object)", () ->
            {
                runner.test("with null Object", (Test test) ->
                {
                    test.assertThrows(() -> Types.setMemberVariableValue((Object)null, "publicInt", 7));
                });

                runner.test("with null memberVariableName", (Test test) ->
                {
                    test.assertThrows(() -> Types.setMemberVariableValue(test, null, 6));
                });

                runner.test("with empty memberVariableName", (Test test) ->
                {
                    test.assertThrows(() -> Types.setMemberVariableValue(test, "", 5));
                });

                runner.test("with non-existing memberVariableName", (Test test) ->
                {
                    test.assertThrows(() -> Types.setMemberVariableValue(test, "abc", 4));
                });

                runner.test("with existing public int memberVariable", (Test test) ->
                {
                    final TypesTests typesTests = new TypesTests();
                    Types.setMemberVariableValue(typesTests, "publicInt", 11);
                    test.assertEqual(11, typesTests.publicInt);
                });

                runner.test("with existing protected String memberVariable", (Test test) ->
                {
                    final TypesTests typesTests = new TypesTests();
                    Types.setMemberVariableValue(typesTests, "protectedString", "abc");
                    test.assertEqual("abc", typesTests.protectedString);
                });

                runner.test("with existing private Boolean memberVariable", (Test test) ->
                {
                    final TypesTests typesTests = new TypesTests();
                    Types.setMemberVariableValue(typesTests, "privateBoolean", true);
                    test.assertEqual(true, typesTests.privateBoolean);
                });

                runner.test("with existing private object memberVariable", (Test test) ->
                {
                    final SingleLinkNode<Integer> node = new SingleLinkNode<>(2);
                    Types.setMemberVariableValue(node, "value", 7);
                    test.assertEqual(7, node.getValue());
                });
            });

            runner.testGroup("getMemberVariables(Object)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> Types.getMemberVariables((Object)null));
                });

                runner.test("with Object", (Test test) ->
                {
                    final Iterable<String> actual = Types.getMemberVariables(new Object()).map(java.lang.reflect.Field::getName);
                    final Iterable<String> expected = new ArrayList<>();
                    test.assertEqual(expected, actual);
                });

                runner.test("with String", (Test test) ->
                {
                    final Iterable<String> actual = Types.getMemberVariables("test").map(java.lang.reflect.Field::getName);
                    final Iterable<String> expected = Array.fromValues(new String[] { "value", "coder", "hash", "serialVersionUID", "COMPACT_STRINGS", "serialPersistentFields", "CASE_INSENSITIVE_ORDER", "LATIN1", "UTF16" });
                    test.assertEqual(expected, actual);
                });

                runner.test("with Test", (Test test) ->
                {
                    final Iterable<String> actual = Types.getMemberVariables(test).map(java.lang.reflect.Field::getName).where(value -> !value.equals("$jacocoData"));
                    final Iterable<String> expected = Array.fromValues(new String[] { "name", "parentTestGroup", "skip", "process" });
                    test.assertEqual(expected, actual);
                });
            });

            runner.testGroup("getMemberVariables(Class<?>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> Types.getMemberVariables((Class<?>)null));
                });

                runner.test("with Object.getClass()", (Test test) ->
                {
                    final Iterable<String> actual = Types.getMemberVariables(new Object().getClass()).map(java.lang.reflect.Field::getName);
                    final Iterable<String> expected = new ArrayList<>();
                    test.assertEqual(expected, actual);
                });

                runner.test("with Object.class", (Test test) ->
                {
                    final Iterable<String> actual = Types.getMemberVariables(Object.class).map(java.lang.reflect.Field::getName);
                    final Iterable<String> expected = new ArrayList<>();
                    test.assertEqual(expected, actual);
                });

                runner.test("with String.getClass()", (Test test) ->
                {
                    final Iterable<String> actual = Types.getMemberVariables("test").map(java.lang.reflect.Field::getName);
                    final Iterable<String> expected = Array.fromValues(new String[] { "value", "coder", "hash", "serialVersionUID", "COMPACT_STRINGS", "serialPersistentFields", "CASE_INSENSITIVE_ORDER", "LATIN1", "UTF16" });
                    test.assertEqual(expected, actual);
                });

                runner.test("with String.class", (Test test) ->
                {
                    final Iterable<String> actual = Types.getMemberVariables(String.class).map(java.lang.reflect.Field::getName);
                    final Iterable<String> expected = Array.fromValues(new String[] { "value", "coder", "hash", "serialVersionUID", "COMPACT_STRINGS", "serialPersistentFields", "CASE_INSENSITIVE_ORDER", "LATIN1", "UTF16" });
                    test.assertEqual(expected, actual);
                });

                runner.test("with Test.getClass()", (Test test) ->
                {
                    final Iterable<String> actual = Types.getMemberVariables(test.getClass()).map(java.lang.reflect.Field::getName).where(value -> !value.equals("$jacocoData"));
                    final Iterable<String> expected = Array.fromValues(new String[] { "name", "parentTestGroup", "skip", "process" });
                    test.assertEqual(expected, actual);
                });

                runner.test("with Test.class", (Test test) ->
                {
                    final Iterable<String> actual = Types.getMemberVariables(Test.class).map(java.lang.reflect.Field::getName).where(value -> !value.equals("$jacocoData"));
                    final Iterable<String> expected = Array.fromValues(new String[] { "name", "parentTestGroup", "skip", "process" });
                    test.assertEqual(expected, actual);
                });
            });

            runner.testGroup("instanceOf(Object,Class<?>)", () ->
            {
                runner.test("with String value and String class", (Test test) ->
                {
                    test.assertTrue(Types.instanceOf("hello", String.class));
                });

                runner.test("with String value and Object class", (Test test) ->
                {
                    test.assertTrue(Types.instanceOf("hello", Object.class));
                });
            });

            runner.testGroup("as(Object)", () ->
            {
                runner.test("null as Integer", (Test test) ->
                {
                    test.assertThrows(() -> Types.as(null, Integer.class));
                });

                runner.test("Integer as null", (Test test) ->
                {
                    test.assertThrows(() -> Types.as(20, null));
                });

                runner.test("Integer as Number", (Test test) ->
                {
                    final Number number = Types.as(50, Number.class);
                    test.assertNotNull(number);
                    test.assertEqual(50, number.intValue());
                });

                runner.test("Integer as Integer", (Test test) ->
                {
                    final Integer integer = Types.as(50, Integer.class);
                    test.assertNotNull(integer);
                    test.assertEqual(50, integer);
                });

                runner.test("Integer as Float", (Test test) ->
                {
                    test.assertNull(Types.as(50, Float.class));
                });

                runner.test("Integer as String", (Test test) ->
                {
                    test.assertNull(Types.as(50, String.class));
                });
            });

            runner.testGroup("getTypeName(Object)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> Types.getTypeName((Object)null));
                });

                runner.test("with Object", (Test test) ->
                {
                    test.assertEqual("Object", Types.getTypeName(new Object()));
                });

                runner.test("with String", (Test test) ->
                {
                    test.assertEqual("String", Types.getTypeName("abc"));
                });
            });

            runner.testGroup("getTypeName(Class<?>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> Types.getTypeName((Class<?>)null));
                });

                runner.test("with Object.getClass()", (Test test) ->
                {
                    test.assertEqual("Object", Types.getTypeName(new Object().getClass()));
                });

                runner.test("with Object.class", (Test test) ->
                {
                    test.assertEqual("Object", Types.getTypeName(Object.class));
                });

                runner.test("with String", (Test test) ->
                {
                    test.assertEqual("String", Types.getTypeName("abc".getClass()));
                });

                runner.test("with String.class", (Test test) ->
                {
                    test.assertEqual("String", Types.getTypeName(String.class));
                });
            });

            runner.testGroup("getFullTypeName(Object)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> Types.getFullTypeName((Object)null));
                });

                runner.test("with Object", (Test test) ->
                {
                    test.assertEqual("java.lang.Object", Types.getFullTypeName(new Object()));
                });

                runner.test("with String", (Test test) ->
                {
                    test.assertEqual("java.lang.String", Types.getFullTypeName("abc"));
                });
            });

            runner.testGroup("getFullTypeName(Class<?>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> Types.getFullTypeName((Class<?>)null));
                });

                runner.test("with Object.getClass()", (Test test) ->
                {
                    test.assertEqual("java.lang.Object", Types.getFullTypeName(new Object().getClass()));
                });

                runner.test("with Object.class", (Test test) ->
                {
                    test.assertEqual("java.lang.Object", Types.getFullTypeName(Object.class));
                });

                runner.test("with String", (Test test) ->
                {
                    test.assertEqual("java.lang.String", Types.getFullTypeName("abc".getClass()));
                });
            });
        });
    }
}