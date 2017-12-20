package qub;

import org.junit.Test;

import static org.junit.Assert.*;

public class TestConsoleTests
{
    @Test
    public void testGroupWithNullAction0()
    {
        final TestConsole testConsole = new TestConsole();
        final InMemoryCharacterWriteStream output = new InMemoryCharacterWriteStream();
        testConsole.setOutput(output);

        testConsole.testGroup("Group A", (Action0)null);

        assertEquals("Group A\n", output.getText());
        assertEquals(0, testConsole.getTestsRun());
        assertEquals(0, testConsole.getTestsPassed());
        assertEquals(0, testConsole.getTestsFailed());
    }

    @Test
    public void testGroupWithNullAction1()
    {
        final TestConsole testConsole = new TestConsole();
        final InMemoryCharacterWriteStream output = new InMemoryCharacterWriteStream();
        testConsole.setOutput(output);

        testConsole.testGroup("Group A", (Action1<TestConsole>)null);

        assertEquals("Group A\n", output.getText());
        assertEquals(0, testConsole.getTestsRun());
        assertEquals(0, testConsole.getTestsPassed());
        assertEquals(0, testConsole.getTestsFailed());
    }

    @Test
    public void testGroupWithNonNullAction0()
    {
        final TestConsole testConsole = new TestConsole();
        final InMemoryCharacterWriteStream output = new InMemoryCharacterWriteStream();
        testConsole.setOutput(output);

        final Value<Boolean> flag = new Value<>(false);
        testConsole.testGroup("Group A", new Action0()
        {
            @Override
            public void run()
            {
                flag.set(true);
            }
        });

        assertEquals("Group A\n", output.getText());
        assertTrue(flag.get());
        assertEquals(0, testConsole.getTestsRun());
        assertEquals(0, testConsole.getTestsPassed());
        assertEquals(0, testConsole.getTestsFailed());
    }

    @Test
    public void testWithNullAction0()
    {
        final TestConsole testConsole = new TestConsole();
        final InMemoryCharacterWriteStream output = new InMemoryCharacterWriteStream();
        testConsole.setOutput(output);

        testConsole.test("Test Test", (Action0)null);

        assertEquals("Test Test\n", output.getText());
        assertEquals(0, testConsole.getTestsRun());
        assertEquals(0, testConsole.getTestsPassed());
        assertEquals(0, testConsole.getTestsFailed());
    }

    @Test
    public void testWithNullAction1()
    {
        final TestConsole testConsole = new TestConsole();
        final InMemoryCharacterWriteStream output = new InMemoryCharacterWriteStream();
        testConsole.setOutput(output);

        testConsole.test("Test Test", (Action1<TestConsole>)null);

        assertEquals("Test Test\n", output.getText());
        assertEquals(0, testConsole.getTestsRun());
        assertEquals(0, testConsole.getTestsPassed());
        assertEquals(0, testConsole.getTestsFailed());
    }

    @Test
    public void testWithNonNullAction0()
    {
        final TestConsole testConsole = new TestConsole();
        final InMemoryCharacterWriteStream output = new InMemoryCharacterWriteStream();
        testConsole.setOutput(output);

        final Value<Boolean> flag = new Value<>(false);
        testConsole.test("Test Test", new Action0()
        {
            @Override
            public void run()
            {
                flag.set(true);
            }
        });

        assertEquals("Test Test\n", output.getText());
        assertTrue(flag.get());
        assertEquals(1, testConsole.getTestsRun());
        assertEquals(1, testConsole.getTestsPassed());
        assertEquals(0, testConsole.getTestsFailed());
    }

    @Test
    public void testWithNonNullAction1()
    {
        final TestConsole testConsole = new TestConsole();
        final InMemoryCharacterWriteStream output = new InMemoryCharacterWriteStream();
        testConsole.setOutput(output);

        final Value<Boolean> flag = new Value<>();
        testConsole.test("Test Test", new Action1<TestConsole>()
        {
            @Override
            public void run(TestConsole arg1)
            {
                flag.set(true);
            }
        });

        assertEquals("Test Test\n", output.getText());
        assertTrue(flag.get());
        assertEquals(1, testConsole.getTestsRun());
        assertEquals(1, testConsole.getTestsPassed());
        assertEquals(0, testConsole.getTestsFailed());
    }

    @Test
    public void testWithAssertionFailure()
    {
        final TestConsole testConsole = new TestConsole();
        final InMemoryCharacterWriteStream output = new InMemoryCharacterWriteStream();
        testConsole.setOutput(output);

        testConsole.test("Test 1", new Action1<TestConsole>()
        {
            @Override
            public void run(TestConsole test)
            {
                test.assertTrue(false);
            }
        });

        assertEquals(1, testConsole.getTestsRun());
        assertEquals(0, testConsole.getTestsPassed());
        assertEquals(1, testConsole.getTestsFailed());

        final String outputText = output.getText();
        final String expectedText =
            "Test 1\n" +
            "  Test Failure:\n" +
            "    Expected: true\n" +
            "    Actual:   false\n" +
            "  Stack Trace:\n" +
            "    at qub.TestConsole.assertTrue(TestConsole.java:";
        assertTrue(outputText.startsWith(expectedText));
    }

    @Test
    public void testWithUnhandledExceptionWithNullMessage()
    {
        final TestConsole testConsole = new TestConsole();
        final InMemoryCharacterWriteStream output = new InMemoryCharacterWriteStream();
        testConsole.setOutput(output);

        testConsole.test("Test 5", new Action1<TestConsole>()
        {
            @Override
            public void run(TestConsole test)
            {
                throw new NullPointerException(null);
            }
        });

        assertEquals(1, testConsole.getTestsRun());
        assertEquals(0, testConsole.getTestsPassed());
        assertEquals(1, testConsole.getTestsFailed());

        final String outputText = output.getText();
        final String expectedText =
            "Test 5\n" +
            "  Unhandled Exception: java.lang.NullPointerException\n" +
            "  Stack Trace:\n" +
            "    at qub.TestConsoleTests";
        assertTrue(outputText.startsWith(expectedText));
    }

    @Test
    public void testWithUnhandledExceptionWithEmptyMessage()
    {
        final TestConsole testConsole = new TestConsole();
        final InMemoryCharacterWriteStream output = new InMemoryCharacterWriteStream();
        testConsole.setOutput(output);

        testConsole.test("Test 5", new Action1<TestConsole>()
        {
            @Override
            public void run(TestConsole test)
            {
                throw new NullPointerException("");
            }
        });

        assertEquals(1, testConsole.getTestsRun());
        assertEquals(0, testConsole.getTestsPassed());
        assertEquals(1, testConsole.getTestsFailed());

        final String outputText = output.getText();
        final String expectedText =
            "Test 5\n" +
                "  Unhandled Exception: java.lang.NullPointerException\n" +
                "  Stack Trace:\n" +
                "    at qub.TestConsoleTests";
        assertTrue(outputText.startsWith(expectedText));
    }

    @Test
    public void testWithUnhandledExceptionWithNonEmptyMessage()
    {
        final TestConsole testConsole = new TestConsole();
        final InMemoryCharacterWriteStream output = new InMemoryCharacterWriteStream();
        testConsole.setOutput(output);

        testConsole.test("Test 5", new Action1<TestConsole>()
        {
            @Override
            public void run(TestConsole test)
            {
                throw new NullPointerException("abcdef");
            }
        });

        assertEquals(1, testConsole.getTestsRun());
        assertEquals(0, testConsole.getTestsPassed());
        assertEquals(1, testConsole.getTestsFailed());

        final String outputText = output.getText();
        final String expectedText =
            "Test 5\n" +
                "  Unhandled Exception: java.lang.NullPointerException\n" +
                "  Message: abcdef\n" +
                "  Stack Trace:\n" +
                "    at qub.TestConsoleTests";
        assertTrue(outputText.startsWith(expectedText));
    }

    @Test
    public void assertTrueWithFalse()
    {
        final TestConsole testConsole = new TestConsole();
        try
        {
            testConsole.assertTrue(false);
            fail();
        }
        catch (TestAssertionFailure e)
        {
            assertNotNull(e);
            assertArrayEquals(
                new String[]
                {
                    "Expected: true",
                    "Actual:   false"
                },
                e.getMessageLines());
        }
    }

    @Test
    public void assertTrueWithFalseAndMessage()
    {
        final TestConsole testConsole = new TestConsole();
        try
        {
            testConsole.assertTrue(false, "Test message!");
            fail();
        }
        catch (TestAssertionFailure e)
        {
            assertNotNull(e);
            assertArrayEquals(
                new String[]
                    {
                        "Test message!",
                        "Expected: true",
                        "Actual:   false"
                    },
                e.getMessageLines());
        }
    }

    @Test
    public void assertTrueWithTrue()
    {
        final TestConsole testConsole = new TestConsole();
        testConsole.assertTrue(true);
    }

    @Test
    public void assertFalseWithTrue()
    {
        final TestConsole testConsole = new TestConsole();
        try
        {
            testConsole.assertFalse(true);
            fail();
        }
        catch (TestAssertionFailure e)
        {
            assertNotNull(e);
            assertArrayEquals(
                new String[]
                    {
                        "Expected: false",
                        "Actual:   true"
                    },
                e.getMessageLines());
        }
    }

    @Test
    public void assertFalseWithTrueAndMessage()
    {
        final TestConsole testConsole = new TestConsole();
        try
        {
            testConsole.assertFalse(true, "Test message!");
            fail();
        }
        catch (TestAssertionFailure e)
        {
            assertNotNull(e);
            assertArrayEquals(
                new String[]
                    {
                        "Test message!",
                        "Expected: false",
                        "Actual:   true"
                    },
                e.getMessageLines());
        }
    }

    @Test
    public void assertFalseWithFalse()
    {
        final TestConsole testConsole = new TestConsole();
        testConsole.assertFalse(false);
    }

    @Test
    public void writeSummary()
    {
        final TestConsole testConsole = new TestConsole();
        final InMemoryCharacterWriteStream output = new InMemoryCharacterWriteStream();
        testConsole.setOutput(output);

        for (int i = 0; i < 2; ++i)
        {
            testConsole.incrementTestsFailed();
        }

        for (int i = 0; i < 5; ++i)
        {
            testConsole.incrementTestsPassed();
        }

        testConsole.writeSummary();

        assertEquals(
            "Tests Run:    7\n" +
            "Tests Passed: 5\n" +
            "Tests Failed: 2\n",
            output.getText());
    }
}
