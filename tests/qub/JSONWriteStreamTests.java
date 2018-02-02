package qub;

public class JSONWriteStreamTests
{
    public static void test(final TestRunner runner)
    {
        runner.testGroup("JSONWriteStream", new Action0()
        {
            @Override
            public void run()
            {
                runner.test("constructor()", new Action1<qub.Test>()
                {
                    @Override
                    public void run(Test test)
                    {
                        final InMemoryLineWriteStream inMemoryWriteStream = new InMemoryLineWriteStream();
                        final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);
                        test.assertTrue(writeStream.isOpen());
                    }
                });
                
                runner.test("close()", new Action1<Test>()
                {
                    @Override
                    public void run(Test test)
                    {
                        final InMemoryLineWriteStream inMemoryWriteStream = new InMemoryLineWriteStream();
                        final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);

                        writeStream.close();
                        test.assertFalse(writeStream.isOpen());
                        test.assertFalse(inMemoryWriteStream.isOpen());

                        writeStream.close();
                        test.assertFalse(writeStream.isOpen());
                        test.assertFalse(inMemoryWriteStream.isOpen());
                    }
                });
                
                runner.testGroup("writeBoolean(boolean)", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("with false", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final InMemoryLineWriteStream inMemoryWriteStream = new InMemoryLineWriteStream();
                                final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);
                                writeStream.writeBoolean(false);
                                test.assertEqual("false", inMemoryWriteStream.getText());
                            }
                        });

                        runner.test("with true", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final InMemoryLineWriteStream inMemoryWriteStream = new InMemoryLineWriteStream();
                                final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);
                                writeStream.writeBoolean(true);
                                test.assertEqual("true", inMemoryWriteStream.getText());
                            }
                        });
                    }
                });

                runner.test("writeNull()", new Action1<Test>()
                {
                    @Override
                    public void run(Test test)
                    {
                        final InMemoryLineWriteStream inMemoryWriteStream = new InMemoryLineWriteStream();
                        final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);
                        writeStream.writeNull();
                        test.assertEqual("null", inMemoryWriteStream.getText());
                    }
                });

                runner.testGroup("writeNumber(double)", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("with negative", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final InMemoryLineWriteStream inMemoryWriteStream = new InMemoryLineWriteStream();
                                final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);
                                writeStream.writeNumber(-12.3);
                                test.assertEqual("-12.3", inMemoryWriteStream.getText());
                            }
                        });

                        runner.test("with negative zero", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final InMemoryLineWriteStream inMemoryWriteStream = new InMemoryLineWriteStream();
                                final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);
                                writeStream.writeNumber(-0.0);
                                test.assertEqual("-0.0", inMemoryWriteStream.getText());
                            }
                        });

                        runner.test("with positive zero", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final InMemoryLineWriteStream inMemoryWriteStream = new InMemoryLineWriteStream();
                                final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);
                                writeStream.writeNumber(0.0);
                                test.assertEqual("0.0", inMemoryWriteStream.getText());
                            }
                        });

                        runner.test("with positive", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final InMemoryLineWriteStream inMemoryWriteStream = new InMemoryLineWriteStream();
                                final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);
                                writeStream.writeNumber(1234567.8);
                                test.assertEqual("1234567.8", inMemoryWriteStream.getText());
                            }
                        });
                    }
                });

                runner.testGroup("writeNumber(long)", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("with negative", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final InMemoryLineWriteStream inMemoryWriteStream = new InMemoryLineWriteStream();
                                final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);
                                writeStream.writeNumber(-123);
                                test.assertEqual("-123", inMemoryWriteStream.getText());
                            }
                        });

                        runner.test("with negative zero", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final InMemoryLineWriteStream inMemoryWriteStream = new InMemoryLineWriteStream();
                                final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);
                                writeStream.writeNumber(-0);
                                test.assertEqual("0", inMemoryWriteStream.getText());
                            }
                        });

                        runner.test("with positive zero", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final InMemoryLineWriteStream inMemoryWriteStream = new InMemoryLineWriteStream();
                                final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);
                                writeStream.writeNumber(0);
                                test.assertEqual("0", inMemoryWriteStream.getText());
                            }
                        });

                        runner.test("with positive", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final InMemoryLineWriteStream inMemoryWriteStream = new InMemoryLineWriteStream();
                                final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);
                                writeStream.writeNumber(1234567);
                                test.assertEqual("1234567", inMemoryWriteStream.getText());
                            }
                        });
                    }
                });

                runner.testGroup("writeQuotedString()", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("with null", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final InMemoryLineWriteStream inMemoryWriteStream = new InMemoryLineWriteStream();
                                final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);
                                writeStream.writeQuotedString(null);
                                test.assertEqual("\"\"", inMemoryWriteStream.getText());
                            }
                        });

                        runner.test("with empty", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final InMemoryLineWriteStream inMemoryWriteStream = new InMemoryLineWriteStream();
                                final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);
                                writeStream.writeQuotedString("");
                                test.assertEqual("\"\"", inMemoryWriteStream.getText());
                            }
                        });

                        runner.test("with non-empty", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final InMemoryLineWriteStream inMemoryWriteStream = new InMemoryLineWriteStream();
                                final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);
                                writeStream.writeQuotedString("test");
                                test.assertEqual("\"test\"", inMemoryWriteStream.getText());
                            }
                        });
                    }
                });

                runner.testGroup("writeObject()", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("with no action", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final InMemoryLineWriteStream inMemoryWriteStream = new InMemoryLineWriteStream();
                                final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);
                                writeStream.writeObject();
                                test.assertEqual("{}", inMemoryWriteStream.getText());
                            }
                        });

                        runner.test("with null action", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final InMemoryLineWriteStream inMemoryWriteStream = new InMemoryLineWriteStream();
                                final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);
                                writeStream.writeObject(null);
                                test.assertEqual("{}", inMemoryWriteStream.getText());
                            }
                        });

                        runner.test("with empty action", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final InMemoryLineWriteStream inMemoryWriteStream = new InMemoryLineWriteStream();
                                final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);
                                writeStream.writeObject(new Action1<JSONObjectWriteStream>()
                                {
                                    @Override
                                    public void run(JSONObjectWriteStream objectWriteStream)
                                    {
                                    }
                                });
                                test.assertEqual("{}", inMemoryWriteStream.getText());
                            }
                        });

                        runner.test("with null property", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final InMemoryLineWriteStream inMemoryWriteStream = new InMemoryLineWriteStream();
                                final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);
                                writeStream.writeObject(new Action1<JSONObjectWriteStream>()
                                {
                                    @Override
                                    public void run(JSONObjectWriteStream objectWriteStream)
                                    {
                                        objectWriteStream.writeNullProperty("apples");
                                    }
                                });
                                test.assertEqual("{\"apples\":null}", inMemoryWriteStream.getText());
                            }
                        });

                        runner.test("with boolean properties", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final InMemoryLineWriteStream inMemoryWriteStream = new InMemoryLineWriteStream();
                                final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);
                                writeStream.writeObject(new Action1<JSONObjectWriteStream>()
                                {
                                    @Override
                                    public void run(JSONObjectWriteStream objectWriteStream)
                                    {
                                        objectWriteStream.writeBooleanProperty("apples", false);
                                        objectWriteStream.writeBooleanProperty("oranges", true);
                                    }
                                });
                                test.assertEqual("{\"apples\":false,\"oranges\":true}", inMemoryWriteStream.getText());
                            }
                        });

                        runner.test("with quoted string property", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final InMemoryLineWriteStream inMemoryWriteStream = new InMemoryLineWriteStream();
                                final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);
                                writeStream.writeObject(new Action1<JSONObjectWriteStream>()
                                {
                                    @Override
                                    public void run(JSONObjectWriteStream objectWriteStream)
                                    {
                                        objectWriteStream.writeQuotedStringProperty("apples", "oranges");
                                    }
                                });
                                test.assertEqual("{\"apples\":\"oranges\"}", inMemoryWriteStream.getText());
                            }
                        });

                        runner.test("with integer property", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final InMemoryLineWriteStream inMemoryWriteStream = new InMemoryLineWriteStream();
                                final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);
                                writeStream.writeObject(new Action1<JSONObjectWriteStream>()
                                {
                                    @Override
                                    public void run(JSONObjectWriteStream objectWriteStream)
                                    {
                                        objectWriteStream.writeNumberProperty("apples", 15);
                                    }
                                });
                                test.assertEqual("{\"apples\":15}", inMemoryWriteStream.getText());
                            }
                        });

                        runner.test("with double property", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final InMemoryLineWriteStream inMemoryWriteStream = new InMemoryLineWriteStream();
                                final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);
                                writeStream.writeObject(new Action1<JSONObjectWriteStream>()
                                {
                                    @Override
                                    public void run(JSONObjectWriteStream objectWriteStream)
                                    {
                                        objectWriteStream.writeNumberProperty("apples", 15.5);
                                    }
                                });
                                test.assertEqual("{\"apples\":15.5}", inMemoryWriteStream.getText());
                            }
                        });

                        runner.test("with object property", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final InMemoryLineWriteStream inMemoryWriteStream = new InMemoryLineWriteStream();
                                final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);
                                writeStream.writeObject(new Action1<JSONObjectWriteStream>()
                                {
                                    @Override
                                    public void run(JSONObjectWriteStream objectWriteStream)
                                    {
                                        objectWriteStream.writeObjectProperty("apples", new Action1<JSONObjectWriteStream>()
                                        {
                                            @Override
                                            public void run(JSONObjectWriteStream arg1)
                                            {
                                            }
                                        });
                                    }
                                });
                                test.assertEqual("{\"apples\":{}}", inMemoryWriteStream.getText());
                            }
                        });

                        runner.test("with array property", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final InMemoryLineWriteStream inMemoryWriteStream = new InMemoryLineWriteStream();
                                final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);
                                writeStream.writeObject(new Action1<JSONObjectWriteStream>()
                                {
                                    @Override
                                    public void run(JSONObjectWriteStream objectWriteStream)
                                    {
                                        objectWriteStream.writeArrayProperty("apples", new Action1<JSONArrayWriteStream>()
                                        {
                                            @Override
                                            public void run(JSONArrayWriteStream arg1)
                                            {
                                            }
                                        });
                                    }
                                });
                                test.assertEqual("{\"apples\":[]}", inMemoryWriteStream.getText());
                            }
                        });
                    }
                });

                runner.testGroup("writeArray()", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("with no action", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final InMemoryLineWriteStream inMemoryWriteStream = new InMemoryLineWriteStream();
                                final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);
                                writeStream.writeArray();
                                test.assertEqual("[]", inMemoryWriteStream.getText());
                            }
                        });

                        runner.test("with null action", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final InMemoryLineWriteStream inMemoryWriteStream = new InMemoryLineWriteStream();
                                final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);
                                writeStream.writeArray(null);
                                test.assertEqual("[]", inMemoryWriteStream.getText());
                            }
                        });

                        runner.test("with empty action", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final InMemoryLineWriteStream inMemoryWriteStream = new InMemoryLineWriteStream();
                                final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);
                                writeStream.writeArray(new Action1<JSONArrayWriteStream>()
                                {
                                    @Override
                                    public void run(JSONArrayWriteStream arrayWriteStream)
                                    {
                                    }
                                });
                                test.assertEqual("[]", inMemoryWriteStream.getText());
                            }
                        });

                        runner.test("with null element", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final InMemoryLineWriteStream inMemoryWriteStream = new InMemoryLineWriteStream();
                                final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);
                                writeStream.writeArray(new Action1<JSONArrayWriteStream>()
                                {
                                    @Override
                                    public void run(JSONArrayWriteStream arrayWriteStream)
                                    {
                                        arrayWriteStream.writeNull();
                                    }
                                });
                                test.assertEqual("[null]", inMemoryWriteStream.getText());
                            }
                        });

                        runner.test("with boolean elements", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final InMemoryLineWriteStream inMemoryWriteStream = new InMemoryLineWriteStream();
                                final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);
                                writeStream.writeArray(new Action1<JSONArrayWriteStream>()
                                {
                                    @Override
                                    public void run(JSONArrayWriteStream arrayWriteStream)
                                    {
                                        arrayWriteStream.writeBoolean(false);
                                        arrayWriteStream.writeBoolean(true);
                                    }
                                });
                                test.assertEqual("[false,true]", inMemoryWriteStream.getText());
                            }
                        });

                        runner.test("with quoted string element", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final InMemoryLineWriteStream inMemoryWriteStream = new InMemoryLineWriteStream();
                                final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);
                                writeStream.writeArray(new Action1<JSONArrayWriteStream>()
                                {
                                    @Override
                                    public void run(JSONArrayWriteStream arrayWriteStream)
                                    {
                                        arrayWriteStream.writeQuotedString("oranges");
                                    }
                                });
                                test.assertEqual("[\"oranges\"]", inMemoryWriteStream.getText());
                            }
                        });

                        runner.test("with integer element", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final InMemoryLineWriteStream inMemoryWriteStream = new InMemoryLineWriteStream();
                                final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);
                                writeStream.writeArray(new Action1<JSONArrayWriteStream>()
                                {
                                    @Override
                                    public void run(JSONArrayWriteStream arrayWriteStream)
                                    {
                                        arrayWriteStream.writeNumber(15);
                                    }
                                });
                                test.assertEqual("[15]", inMemoryWriteStream.getText());
                            }
                        });

                        runner.test("with double element", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final InMemoryLineWriteStream inMemoryWriteStream = new InMemoryLineWriteStream();
                                final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);
                                writeStream.writeArray(new Action1<JSONArrayWriteStream>()
                                {
                                    @Override
                                    public void run(JSONArrayWriteStream arrayWriteStream)
                                    {
                                        arrayWriteStream.writeNumber(15.5);
                                    }
                                });
                                test.assertEqual("[15.5]", inMemoryWriteStream.getText());
                            }
                        });

                        runner.test("with object element", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final InMemoryLineWriteStream inMemoryWriteStream = new InMemoryLineWriteStream();
                                final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);
                                writeStream.writeArray(new Action1<JSONArrayWriteStream>()
                                {
                                    @Override
                                    public void run(JSONArrayWriteStream arrayWriteStream)
                                    {
                                        arrayWriteStream.writeObject(new Action1<JSONObjectWriteStream>()
                                        {
                                            @Override
                                            public void run(JSONObjectWriteStream arg1)
                                            {
                                            }
                                        });
                                    }
                                });
                                test.assertEqual("[{}]", inMemoryWriteStream.getText());
                            }
                        });

                        runner.test("with array element", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final InMemoryLineWriteStream inMemoryWriteStream = new InMemoryLineWriteStream();
                                final JSONWriteStream writeStream = new JSONWriteStream(inMemoryWriteStream);
                                writeStream.writeArray(new Action1<JSONArrayWriteStream>()
                                {
                                    @Override
                                    public void run(JSONArrayWriteStream arrayWriteStream)
                                    {
                                        arrayWriteStream.writeArray(new Action1<JSONArrayWriteStream>()
                                        {
                                            @Override
                                            public void run(JSONArrayWriteStream arg1)
                                            {
                                            }
                                        });
                                    }
                                });
                                test.assertEqual("[[]]", inMemoryWriteStream.getText());
                            }
                        });
                    }
                });
            }
        });
    }
}
