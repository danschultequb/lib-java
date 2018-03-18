package qub;

public class JSONWriteStreamTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(JSONWriteStream.class, () ->
        {
            runner.test("constructor()", test ->
            {
                final InMemoryLineWriteStream inMemoryWriteStream = new InMemoryLineWriteStream();
                final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);
                test.assertFalse(writeStream.isDisposed());
            });

            runner.test("close()", test ->
            {
                final InMemoryLineWriteStream inMemoryWriteStream = new InMemoryLineWriteStream();
                final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);

                writeStream.close();
                test.assertTrue(writeStream.isDisposed());
                test.assertTrue(inMemoryWriteStream.isDisposed());

                writeStream.close();
                test.assertTrue(writeStream.isDisposed());
                test.assertTrue(inMemoryWriteStream.isDisposed());
            });

            runner.testGroup("writeBoolean(boolean)", () ->
            {
                runner.test("with false", test ->
                {
                    final InMemoryLineWriteStream inMemoryWriteStream = new InMemoryLineWriteStream();
                    final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);
                    writeStream.writeBoolean(false);
                    test.assertEqual("false", inMemoryWriteStream.getText());
                });

                runner.test("with true", test ->
                {
                    final InMemoryLineWriteStream inMemoryWriteStream = new InMemoryLineWriteStream();
                    final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);
                    writeStream.writeBoolean(true);
                    test.assertEqual("true", inMemoryWriteStream.getText());
                });
            });

            runner.test("writeNull()", test ->
            {
                final InMemoryLineWriteStream inMemoryWriteStream = new InMemoryLineWriteStream();
                final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);
                writeStream.writeNull();
                test.assertEqual("null", inMemoryWriteStream.getText());
            });

            runner.testGroup("writeNumber(double)", () ->
            {
                runner.test("with negative", test ->
                {
                    final InMemoryLineWriteStream inMemoryWriteStream = new InMemoryLineWriteStream();
                    final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);
                    writeStream.writeNumber(-12.3);
                    test.assertEqual("-12.3", inMemoryWriteStream.getText());
                });

                runner.test("with negative zero", test ->
                {
                    final InMemoryLineWriteStream inMemoryWriteStream = new InMemoryLineWriteStream();
                    final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);
                    writeStream.writeNumber(-0.0);
                    test.assertEqual("-0.0", inMemoryWriteStream.getText());
                });

                runner.test("with positive zero", test ->
                {
                    final InMemoryLineWriteStream inMemoryWriteStream = new InMemoryLineWriteStream();
                    final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);
                    writeStream.writeNumber(0.0);
                    test.assertEqual("0.0", inMemoryWriteStream.getText());
                });

                runner.test("with positive", test ->
                {
                    final InMemoryLineWriteStream inMemoryWriteStream = new InMemoryLineWriteStream();
                    final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);
                    writeStream.writeNumber(1234567.8);
                    test.assertEqual("1234567.8", inMemoryWriteStream.getText());
                });
            });

            runner.testGroup("writeNumber(long)", () ->
            {
                runner.test("with negative", test ->
                {
                    final InMemoryLineWriteStream inMemoryWriteStream = new InMemoryLineWriteStream();
                    final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);
                    writeStream.writeNumber(-123);
                    test.assertEqual("-123", inMemoryWriteStream.getText());
                });

                runner.test("with negative zero", test ->
                {
                    final InMemoryLineWriteStream inMemoryWriteStream = new InMemoryLineWriteStream();
                    final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);
                    writeStream.writeNumber(-0);
                    test.assertEqual("0", inMemoryWriteStream.getText());
                });

                runner.test("with positive zero", test ->
                {
                    final InMemoryLineWriteStream inMemoryWriteStream = new InMemoryLineWriteStream();
                    final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);
                    writeStream.writeNumber(0);
                    test.assertEqual("0", inMemoryWriteStream.getText());
                });

                runner.test("with positive", test ->
                {
                    final InMemoryLineWriteStream inMemoryWriteStream = new InMemoryLineWriteStream();
                    final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);
                    writeStream.writeNumber(1234567);
                    test.assertEqual("1234567", inMemoryWriteStream.getText());
                });
            });

            runner.testGroup("writeQuotedString()", () ->
            {
                runner.test("with null", test ->
                {
                    final InMemoryLineWriteStream inMemoryWriteStream = new InMemoryLineWriteStream();
                    final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);
                    writeStream.writeQuotedString(null);
                    test.assertEqual("\"\"", inMemoryWriteStream.getText());
                });

                runner.test("with empty", test ->
                {
                    final InMemoryLineWriteStream inMemoryWriteStream = new InMemoryLineWriteStream();
                    final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);
                    writeStream.writeQuotedString("");
                    test.assertEqual("\"\"", inMemoryWriteStream.getText());
                });

                runner.test("with non-empty", test ->
                {
                    final InMemoryLineWriteStream inMemoryWriteStream = new InMemoryLineWriteStream();
                    final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);
                    writeStream.writeQuotedString("test");
                    test.assertEqual("\"test\"", inMemoryWriteStream.getText());
                });
            });

            runner.testGroup("writeObject()", () ->
            {
                runner.test("with no action", test ->
                {
                    final InMemoryLineWriteStream inMemoryWriteStream = new InMemoryLineWriteStream();
                    final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);
                    writeStream.writeObject();
                    test.assertEqual("{}", inMemoryWriteStream.getText());
                });

                runner.test("with null action", test ->
                {
                    final InMemoryLineWriteStream inMemoryWriteStream = new InMemoryLineWriteStream();
                    final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);
                    writeStream.writeObject(null);
                    test.assertEqual("{}", inMemoryWriteStream.getText());
                });

                runner.test("with empty action", test ->
                {
                    final InMemoryLineWriteStream inMemoryWriteStream = new InMemoryLineWriteStream();
                    final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);
                    writeStream.writeObject((JSONObjectWriteStream objectWriteStream) -> {});
                    test.assertEqual("{}", inMemoryWriteStream.getText());
                });

                runner.test("with null property", test ->
                {
                    final InMemoryLineWriteStream inMemoryWriteStream = new InMemoryLineWriteStream();
                    final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);
                    writeStream.writeObject((JSONObjectWriteStream objectWriteStream) ->
                    {
                        objectWriteStream.writeNullProperty("apples");
                    });
                    test.assertEqual("{\"apples\":null}", inMemoryWriteStream.getText());
                });

                runner.test("with boolean properties", test ->
                {
                    final InMemoryLineWriteStream inMemoryWriteStream = new InMemoryLineWriteStream();
                    final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);
                    writeStream.writeObject((JSONObjectWriteStream objectWriteStream) ->
                    {
                        objectWriteStream.writeBooleanProperty("apples", false);
                        objectWriteStream.writeBooleanProperty("oranges", true);
                    });
                    test.assertEqual("{\"apples\":false,\"oranges\":true}", inMemoryWriteStream.getText());
                });

                runner.test("with quoted string property", test ->
                {
                    final InMemoryLineWriteStream inMemoryWriteStream = new InMemoryLineWriteStream();
                    final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);
                    writeStream.writeObject((JSONObjectWriteStream objectWriteStream) ->
                    {
                        objectWriteStream.writeQuotedStringProperty("apples", "oranges");
                    });
                    test.assertEqual("{\"apples\":\"oranges\"}", inMemoryWriteStream.getText());
                });

                runner.test("with integer property", test ->
                {
                    final InMemoryLineWriteStream inMemoryWriteStream = new InMemoryLineWriteStream();
                    final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);
                    writeStream.writeObject((JSONObjectWriteStream objectWriteStream) ->
                    {
                        objectWriteStream.writeNumberProperty("apples", 15);
                    });
                    test.assertEqual("{\"apples\":15}", inMemoryWriteStream.getText());
                });

                runner.test("with double property", test ->
                {
                    final InMemoryLineWriteStream inMemoryWriteStream = new InMemoryLineWriteStream();
                    final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);
                    writeStream.writeObject((JSONObjectWriteStream objectWriteStream) ->
                    {
                        objectWriteStream.writeNumberProperty("apples", 15.5);
                    });
                    test.assertEqual("{\"apples\":15.5}", inMemoryWriteStream.getText());
                });

                runner.test("with object property", test ->
                {
                    final InMemoryLineWriteStream inMemoryWriteStream = new InMemoryLineWriteStream();
                    final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);
                    writeStream.writeObject((JSONObjectWriteStream objectWriteStream) ->
                    {
                        objectWriteStream.writeObjectProperty("apples", (JSONObjectWriteStream objectWriteStream2) -> {});
                    });
                    test.assertEqual("{\"apples\":{}}", inMemoryWriteStream.getText());
                });

                runner.test("with array property", test ->
                {
                    final InMemoryLineWriteStream inMemoryWriteStream = new InMemoryLineWriteStream();
                    final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);
                    writeStream.writeObject((JSONObjectWriteStream objectWriteStream) ->
                    {
                        objectWriteStream.writeArrayProperty("apples", (JSONArrayWriteStream arrayWriteStream) -> {});
                    });
                    test.assertEqual("{\"apples\":[]}", inMemoryWriteStream.getText());
                });
            });

            runner.testGroup("writeArray()", () ->
            {
                runner.test("with no action", test ->
                {
                    final InMemoryLineWriteStream inMemoryWriteStream = new InMemoryLineWriteStream();
                    final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);
                    writeStream.writeArray();
                    test.assertEqual("[]", inMemoryWriteStream.getText());
                });

                runner.test("with null action", test ->
                {
                    final InMemoryLineWriteStream inMemoryWriteStream = new InMemoryLineWriteStream();
                    final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);
                    writeStream.writeArray(null);
                    test.assertEqual("[]", inMemoryWriteStream.getText());
                });

                runner.test("with empty action", test ->
                {
                    final InMemoryLineWriteStream inMemoryWriteStream = new InMemoryLineWriteStream();
                    final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);
                    writeStream.writeArray((JSONArrayWriteStream arrayWriteStream) -> {});
                    test.assertEqual("[]", inMemoryWriteStream.getText());
                });

                runner.test("with null element", test ->
                {
                    final InMemoryLineWriteStream inMemoryWriteStream = new InMemoryLineWriteStream();
                    final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);
                    writeStream.writeArray((JSONArrayWriteStream arrayWriteStream) ->
                    {
                        arrayWriteStream.writeNull();
                    });
                    test.assertEqual("[null]", inMemoryWriteStream.getText());
                });

                runner.test("with boolean elements", test ->
                {
                    final InMemoryLineWriteStream inMemoryWriteStream = new InMemoryLineWriteStream();
                    final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);
                    writeStream.writeArray((JSONArrayWriteStream arrayWriteStream) ->
                    {
                        arrayWriteStream.writeBoolean(false);
                        arrayWriteStream.writeBoolean(true);
                    });
                    test.assertEqual("[false,true]", inMemoryWriteStream.getText());
                });

                runner.test("with quoted string element", test ->
                {
                    final InMemoryLineWriteStream inMemoryWriteStream = new InMemoryLineWriteStream();
                    final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);
                    writeStream.writeArray((JSONArrayWriteStream arrayWriteStream) ->
                    {
                        arrayWriteStream.writeQuotedString("oranges");
                    });
                    test.assertEqual("[\"oranges\"]", inMemoryWriteStream.getText());
                });

                runner.test("with integer element", test ->
                {
                    final InMemoryLineWriteStream inMemoryWriteStream = new InMemoryLineWriteStream();
                    final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);
                    writeStream.writeArray((JSONArrayWriteStream arrayWriteStream) ->
                    {
                        arrayWriteStream.writeNumber(15);
                    });
                    test.assertEqual("[15]", inMemoryWriteStream.getText());
                });

                runner.test("with double element", test ->
                {
                    final InMemoryLineWriteStream inMemoryWriteStream = new InMemoryLineWriteStream();
                    final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);
                    writeStream.writeArray((JSONArrayWriteStream arrayWriteStream) ->
                    {
                        arrayWriteStream.writeNumber(15.5);
                    });
                    test.assertEqual("[15.5]", inMemoryWriteStream.getText());
                });

                runner.test("with object element", test ->
                {
                    final InMemoryLineWriteStream inMemoryWriteStream = new InMemoryLineWriteStream();
                    final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);
                    writeStream.writeArray((JSONArrayWriteStream arrayWriteStream) ->
                    {
                        arrayWriteStream.writeObject((JSONObjectWriteStream objectWriteStream) -> {});
                    });
                    test.assertEqual("[{}]", inMemoryWriteStream.getText());
                });

                runner.test("with array element", test ->
                {
                    final InMemoryLineWriteStream inMemoryWriteStream = new InMemoryLineWriteStream();
                    final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);
                    writeStream.writeArray((JSONArrayWriteStream arrayWriteStream) ->
                    {
                        arrayWriteStream.writeArray((JSONArrayWriteStream arrayWriteStream2) -> {});
                    });
                    test.assertEqual("[[]]", inMemoryWriteStream.getText());
                });
            });
        });
    }
}
