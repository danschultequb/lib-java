package qub;

public class JSONWriteStreamTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(JSONWriteStream.class, () ->
        {
            runner.test("constructor()", test ->
            {
                final InMemoryLineStream inMemoryWriteStream = new InMemoryLineStream();
                final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);
                test.assertFalse(writeStream.isDisposed());
            });

            runner.test("close()", test ->
            {
                final InMemoryLineStream inMemoryWriteStream = new InMemoryLineStream();
                final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);

                try
                {
                    writeStream.close();
                    test.assertTrue(writeStream.isDisposed());
                    test.assertTrue(inMemoryWriteStream.isDisposed());

                    writeStream.close();
                    test.assertTrue(writeStream.isDisposed());
                    test.assertTrue(inMemoryWriteStream.isDisposed());
                }
                catch (Exception e)
                {
                    test.fail(e);
                }
            });

            runner.testGroup("writeBoolean(boolean)", () ->
            {
                runner.test("with false", test ->
                {
                    final InMemoryLineStream inMemoryWriteStream = new InMemoryLineStream();
                    final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);
                    writeStream.writeBoolean(false);
                    test.assertSuccess("false", inMemoryWriteStream.getText());
                });

                runner.test("with true", test ->
                {
                    final InMemoryLineStream inMemoryWriteStream = new InMemoryLineStream();
                    final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);
                    writeStream.writeBoolean(true);
                    test.assertSuccess("true", inMemoryWriteStream.getText());
                });
            });

            runner.test("writeNull()", test ->
            {
                final InMemoryLineStream inMemoryWriteStream = new InMemoryLineStream();
                final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);
                writeStream.writeNull();
                test.assertSuccess("null", inMemoryWriteStream.getText());
            });

            runner.testGroup("writeNumber(double)", () ->
            {
                runner.test("with negative", test ->
                {
                    final InMemoryLineStream inMemoryWriteStream = new InMemoryLineStream();
                    final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);
                    writeStream.writeNumber(-12.3);
                    test.assertSuccess("-12.3", inMemoryWriteStream.getText());
                });

                runner.test("with negative zero", test ->
                {
                    final InMemoryLineStream inMemoryWriteStream = new InMemoryLineStream();
                    final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);
                    writeStream.writeNumber(-0.0);
                    test.assertSuccess("-0.0", inMemoryWriteStream.getText());
                });

                runner.test("with positive zero", test ->
                {
                    final InMemoryLineStream inMemoryWriteStream = new InMemoryLineStream();
                    final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);
                    writeStream.writeNumber(0.0);
                    test.assertSuccess("0.0", inMemoryWriteStream.getText());
                });

                runner.test("with positive", test ->
                {
                    final InMemoryLineStream inMemoryWriteStream = new InMemoryLineStream();
                    final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);
                    writeStream.writeNumber(1234567.8);
                    test.assertSuccess("1234567.8", inMemoryWriteStream.getText());
                });
            });

            runner.testGroup("writeNumber(long)", () ->
            {
                runner.test("with negative", test ->
                {
                    final InMemoryLineStream inMemoryWriteStream = new InMemoryLineStream();
                    final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);
                    writeStream.writeNumber(-123);
                    test.assertSuccess("-123", inMemoryWriteStream.getText());
                });

                runner.test("with negative zero", test ->
                {
                    final InMemoryLineStream inMemoryWriteStream = new InMemoryLineStream();
                    final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);
                    writeStream.writeNumber(-0);
                    test.assertSuccess("0", inMemoryWriteStream.getText());
                });

                runner.test("with positive zero", test ->
                {
                    final InMemoryLineStream inMemoryWriteStream = new InMemoryLineStream();
                    final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);
                    writeStream.writeNumber(0);
                    test.assertSuccess("0", inMemoryWriteStream.getText());
                });

                runner.test("with positive", test ->
                {
                    final InMemoryLineStream inMemoryWriteStream = new InMemoryLineStream();
                    final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);
                    writeStream.writeNumber(1234567);
                    test.assertSuccess("1234567", inMemoryWriteStream.getText());
                });
            });

            runner.testGroup("writeQuotedString()", () ->
            {
                runner.test("with null", test ->
                {
                    final InMemoryLineStream inMemoryWriteStream = new InMemoryLineStream();
                    final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);
                    writeStream.writeQuotedString(null);
                    test.assertSuccess("\"\"", inMemoryWriteStream.getText());
                });

                runner.test("with empty", test ->
                {
                    final InMemoryLineStream inMemoryWriteStream = new InMemoryLineStream();
                    final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);
                    writeStream.writeQuotedString("");
                    test.assertSuccess("\"\"", inMemoryWriteStream.getText());
                });

                runner.test("with non-empty", test ->
                {
                    final InMemoryLineStream inMemoryWriteStream = new InMemoryLineStream();
                    final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);
                    writeStream.writeQuotedString("test");
                    test.assertSuccess("\"test\"", inMemoryWriteStream.getText());
                });
            });

            runner.testGroup("writeObject()", () ->
            {
                runner.test("with no action", test ->
                {
                    final InMemoryLineStream inMemoryWriteStream = new InMemoryLineStream();
                    final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);
                    writeStream.writeObject();
                    test.assertSuccess("{}", inMemoryWriteStream.getText());
                });

                runner.test("with null action", test ->
                {
                    final InMemoryLineStream inMemoryWriteStream = new InMemoryLineStream();
                    final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);
                    writeStream.writeObject(null);
                    test.assertSuccess("{}", inMemoryWriteStream.getText());
                });

                runner.test("with empty action", test ->
                {
                    final InMemoryLineStream inMemoryWriteStream = new InMemoryLineStream();
                    final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);
                    writeStream.writeObject((JSONObjectWriteStream objectWriteStream) -> {});
                    test.assertSuccess("{}", inMemoryWriteStream.getText());
                });

                runner.test("with null property", test ->
                {
                    final InMemoryLineStream inMemoryWriteStream = new InMemoryLineStream();
                    final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);
                    writeStream.writeObject((JSONObjectWriteStream objectWriteStream) ->
                    {
                        objectWriteStream.writeNullProperty("apples");
                    });
                    test.assertSuccess("{\"apples\":null}", inMemoryWriteStream.getText());
                });

                runner.test("with boolean properties", test ->
                {
                    final InMemoryLineStream inMemoryWriteStream = new InMemoryLineStream();
                    final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);
                    writeStream.writeObject((JSONObjectWriteStream objectWriteStream) ->
                    {
                        objectWriteStream.writeBooleanProperty("apples", false);
                        objectWriteStream.writeBooleanProperty("oranges", true);
                    });
                    test.assertSuccess("{\"apples\":false,\"oranges\":true}", inMemoryWriteStream.getText());
                });

                runner.test("with quoted string property", test ->
                {
                    final InMemoryLineStream inMemoryWriteStream = new InMemoryLineStream();
                    final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);
                    writeStream.writeObject((JSONObjectWriteStream objectWriteStream) ->
                    {
                        objectWriteStream.writeQuotedStringProperty("apples", "oranges");
                    });
                    test.assertSuccess("{\"apples\":\"oranges\"}", inMemoryWriteStream.getText());
                });

                runner.test("with integer property", test ->
                {
                    final InMemoryLineStream inMemoryWriteStream = new InMemoryLineStream();
                    final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);
                    writeStream.writeObject((JSONObjectWriteStream objectWriteStream) ->
                    {
                        objectWriteStream.writeNumberProperty("apples", 15);
                    });
                    test.assertSuccess("{\"apples\":15}", inMemoryWriteStream.getText());
                });

                runner.test("with double property", test ->
                {
                    final InMemoryLineStream inMemoryWriteStream = new InMemoryLineStream();
                    final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);
                    writeStream.writeObject((JSONObjectWriteStream objectWriteStream) ->
                    {
                        objectWriteStream.writeNumberProperty("apples", 15.5);
                    });
                    test.assertSuccess("{\"apples\":15.5}", inMemoryWriteStream.getText());
                });

                runner.test("with object property", test ->
                {
                    final InMemoryLineStream inMemoryWriteStream = new InMemoryLineStream();
                    final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);
                    writeStream.writeObject((JSONObjectWriteStream objectWriteStream) ->
                    {
                        objectWriteStream.writeObjectProperty("apples", (JSONObjectWriteStream objectWriteStream2) -> {});
                    });
                    test.assertSuccess("{\"apples\":{}}", inMemoryWriteStream.getText());
                });

                runner.test("with array property", test ->
                {
                    final InMemoryLineStream inMemoryWriteStream = new InMemoryLineStream();
                    final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);
                    writeStream.writeObject((JSONObjectWriteStream objectWriteStream) ->
                    {
                        objectWriteStream.writeArrayProperty("apples", (JSONArrayWriteStream arrayWriteStream) -> {});
                    });
                    test.assertSuccess("{\"apples\":[]}", inMemoryWriteStream.getText());
                });
            });

            runner.testGroup("writeArray()", () ->
            {
                runner.test("with no action", test ->
                {
                    final InMemoryLineStream inMemoryWriteStream = new InMemoryLineStream();
                    final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);
                    writeStream.writeArray();
                    test.assertSuccess("[]", inMemoryWriteStream.getText());
                });

                runner.test("with null action", test ->
                {
                    final InMemoryLineStream inMemoryWriteStream = new InMemoryLineStream();
                    final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);
                    writeStream.writeArray(null);
                    test.assertSuccess("[]", inMemoryWriteStream.getText());
                });

                runner.test("with empty action", test ->
                {
                    final InMemoryLineStream inMemoryWriteStream = new InMemoryLineStream();
                    final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);
                    writeStream.writeArray((JSONArrayWriteStream arrayWriteStream) -> {});
                    test.assertSuccess("[]", inMemoryWriteStream.getText());
                });

                runner.test("with null element", test ->
                {
                    final InMemoryLineStream inMemoryWriteStream = new InMemoryLineStream();
                    final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);
                    writeStream.writeArray((JSONArrayWriteStream arrayWriteStream) ->
                    {
                        arrayWriteStream.writeNull();
                    });
                    test.assertSuccess("[null]", inMemoryWriteStream.getText());
                });

                runner.test("with boolean elements", test ->
                {
                    final InMemoryLineStream inMemoryWriteStream = new InMemoryLineStream();
                    final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);
                    writeStream.writeArray((JSONArrayWriteStream arrayWriteStream) ->
                    {
                        arrayWriteStream.writeBoolean(false);
                        arrayWriteStream.writeBoolean(true);
                    });
                    test.assertSuccess("[false,true]", inMemoryWriteStream.getText());
                });

                runner.test("with quoted string element", test ->
                {
                    final InMemoryLineStream inMemoryWriteStream = new InMemoryLineStream();
                    final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);
                    writeStream.writeArray((JSONArrayWriteStream arrayWriteStream) ->
                    {
                        arrayWriteStream.writeQuotedString("oranges");
                    });
                    test.assertSuccess("[\"oranges\"]", inMemoryWriteStream.getText());
                });

                runner.test("with integer element", test ->
                {
                    final InMemoryLineStream inMemoryWriteStream = new InMemoryLineStream();
                    final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);
                    writeStream.writeArray((JSONArrayWriteStream arrayWriteStream) ->
                    {
                        arrayWriteStream.writeNumber(15);
                    });
                    test.assertSuccess("[15]", inMemoryWriteStream.getText());
                });

                runner.test("with double element", test ->
                {
                    final InMemoryLineStream inMemoryWriteStream = new InMemoryLineStream();
                    final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);
                    writeStream.writeArray((JSONArrayWriteStream arrayWriteStream) ->
                    {
                        arrayWriteStream.writeNumber(15.5);
                    });
                    test.assertSuccess("[15.5]", inMemoryWriteStream.getText());
                });

                runner.test("with object element", test ->
                {
                    final InMemoryLineStream inMemoryWriteStream = new InMemoryLineStream();
                    final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);
                    writeStream.writeArray((JSONArrayWriteStream arrayWriteStream) ->
                    {
                        arrayWriteStream.writeObject((JSONObjectWriteStream objectWriteStream) -> {});
                    });
                    test.assertSuccess("[{}]", inMemoryWriteStream.getText());
                });

                runner.test("with array element", test ->
                {
                    final InMemoryLineStream inMemoryWriteStream = new InMemoryLineStream();
                    final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);
                    writeStream.writeArray((JSONArrayWriteStream arrayWriteStream) ->
                    {
                        arrayWriteStream.writeArray((JSONArrayWriteStream arrayWriteStream2) -> {});
                    });
                    test.assertSuccess("[[]]", inMemoryWriteStream.getText());
                });
            });
        });
    }
}
