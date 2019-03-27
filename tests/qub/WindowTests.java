package qub;

public class WindowTests
{
    public static void test(TestRunner runner, Function1<Test,Window> windowCreator)
    {
        runner.testGroup(Window.class, () ->
        {
            runner.testGroup("dispose()", () ->
            {
                runner.test("when not open and with no content", (Test test) ->
                {
                    try (final Window window = windowCreator.run(test))
                    {
                        test.assertSuccess(true, window.dispose());
                        test.assertFalse(window.isOpen());
                        test.assertTrue(window.isDisposed());
                    }
                });

                runner.test("when open and with no content", (Test test) ->
                {
                    try (final Window window = windowCreator.run(test))
                    {
                        window.open();
                        test.assertTrue(window.isOpen());

                        test.assertSuccess(true, window.dispose());

                        test.assertFalse(window.isOpen());
                        test.assertTrue(window.isDisposed());
                    }
                });

                runner.test("when not open and with content", (Test test) ->
                {
                    try (final Window window = windowCreator.run(test))
                    {
                        final UIText text = new UIText("test");
                        window.setContent(text);
                        test.assertSame(window, text.getParent());
                        test.assertSame(window, text.getParentWindow());

                        test.assertSuccess(true, window.dispose());

                        test.assertNull(window.getContent());
                        test.assertNull(text.getParent());
                        test.assertNull(text.getParentWindow());
                    }
                });
            });

            runner.testGroup("open()", () ->
            {
                runner.test("when not open and not disposed", (Test test) ->
                {
                    try (final Window window = windowCreator.run(test))
                    {
                        window.open();
                        test.assertTrue(window.isOpen());
                    }
                });

                runner.test("when open but not disposed", (Test test) ->
                {
                    try (final Window window = windowCreator.run(test))
                    {
                        window.open();
                        test.assertTrue(window.isOpen());

                        test.assertThrows(window::open, new PreConditionFailure("isOpen() cannot be true."));
                        test.assertTrue(window.isOpen());
                    }
                });

                runner.test("when not open but disposed", (Test test) ->
                {
                    try (final Window window = windowCreator.run(test))
                    {
                        window.dispose();
                        test.assertFalse(window.isOpen());
                        test.assertTrue(window.isDisposed());

                        test.assertThrows(window::open, new PreConditionFailure("isDisposed() cannot be true."));
                        test.assertFalse(window.isOpen());
                        test.assertTrue(window.isDisposed());
                    }
                });

                runner.test("when open and disposed", (Test test) ->
                {
                    try (final Window window = windowCreator.run(test))
                    {
                        window.open();
                        window.dispose();
                        test.assertFalse(window.isOpen());
                        test.assertTrue(window.isDisposed());

                        test.assertThrows(window::open, new PreConditionFailure("isDisposed() cannot be true."));
                        test.assertFalse(window.isOpen());
                        test.assertTrue(window.isDisposed());
                    }
                });
            });

            runner.testGroup("setContent(UIElement)", () ->
            {
                runner.test("create null to null", (Test test) ->
                {
                    try (final Window window = windowCreator.run(test))
                    {
                        window.setContent(null);
                        test.assertNull(window.getContent());
                    }
                });

                runner.test("create null to non-null", (Test test) ->
                {
                    try (final Window window = windowCreator.run(test))
                    {
                        final Value<Integer> parentWindowChangedCalls = Value.create(0);
                        final UIText text = new UIText("abc")
                        {
                            @Override
                            public void parentWindowChanged(Window previousParentWindow, Window newParentWindow)
                            {
                                super.parentWindowChanged(previousParentWindow, newParentWindow);
                                parentWindowChangedCalls.set(parentWindowChangedCalls.get() + 1);
                            }
                        };
                        test.assertEqual(0, parentWindowChangedCalls.get());
                        window.setContent(text);
                        test.assertEqual(1, parentWindowChangedCalls.get());
                        test.assertSame(text, window.getContent());
                        test.assertSame(window, text.getParent());
                        test.assertSame(window, text.getParentWindow());
                    }
                });

                runner.test("create non-null to null", (Test test) ->
                {
                    try (final Window window = windowCreator.run(test))
                    {
                        final Value<Integer> parentWindowChangedCalls = Value.create(0);
                        final UIText text = new UIText("abc")
                        {
                            @Override
                            public void parentWindowChanged(Window previousParentWindow, Window newParentWindow)
                            {
                                super.parentWindowChanged(previousParentWindow, newParentWindow);
                                parentWindowChangedCalls.set(parentWindowChangedCalls.get() + 1);
                            }
                        };
                        test.assertEqual(0, parentWindowChangedCalls.get());
                        window.setContent(text);
                        test.assertEqual(1, parentWindowChangedCalls.get());
                        test.assertSame(text, window.getContent());
                        test.assertSame(window, text.getParent());
                        test.assertSame(window, text.getParentWindow());

                        test.assertEqual(1, parentWindowChangedCalls.get());
                        window.setContent(null);
                        test.assertEqual(2, parentWindowChangedCalls.get());

                        test.assertNull(window.getContent());
                        test.assertNull(text.getParent());
                        test.assertNull(text.getParentWindow());
                    }
                });

                runner.test("create non-null to non-null", (Test test) ->
                {
                    try (final Window window = windowCreator.run(test))
                    {
                        final Value<Integer> parentWindowChangedCalls1 = Value.create(0);
                        final UIText text1 = new UIText("abc")
                        {
                            @Override
                            public void parentWindowChanged(Window previousParentWindow, Window newParentWindow)
                            {
                                super.parentWindowChanged(previousParentWindow, newParentWindow);
                                parentWindowChangedCalls1.set(parentWindowChangedCalls1.get() + 1);
                            }
                        };
                        test.assertEqual(0, parentWindowChangedCalls1.get());
                        window.setContent(text1);
                        test.assertEqual(1, parentWindowChangedCalls1.get());

                        final Value<Integer> parentWindowChangedCalls2 = Value.create(0);
                        final UIText text2 = new UIText("xyz")
                        {
                            @Override
                            public void parentWindowChanged(Window previousParentWindow, Window newParentWindow)
                            {
                                super.parentWindowChanged(previousParentWindow, newParentWindow);
                                parentWindowChangedCalls2.set(parentWindowChangedCalls2.get() + 1);
                            }
                        };
                        test.assertEqual(1, parentWindowChangedCalls1.get());
                        test.assertEqual(0, parentWindowChangedCalls2.get());
                        window.setContent(text2);
                        test.assertEqual(2, parentWindowChangedCalls1.get());
                        test.assertEqual(1, parentWindowChangedCalls2.get());

                        test.assertNull(text1.getParent());
                        test.assertNull(text1.getParentWindow());
                        test.assertSame(text2, window.getContent());
                        test.assertSame(window, text2.getParent());
                        test.assertSame(window, text2.getParentWindow());
                    }
                });

                runner.test("to null when disposed", (Test test) ->
                {
                    try (final Window window = windowCreator.run(test))
                    {
                        window.dispose();

                        window.setContent(null);
                        test.assertNull(window.getContent());
                    }
                });

                runner.test("to non-null when disposed", (Test test) ->
                {
                    try (final Window window = windowCreator.run(test))
                    {
                        window.dispose();
                        final UIText text = new UIText("apples");

                        test.assertThrows(() -> window.setContent(text), new PreConditionFailure("!isDisposed() || uiElement == null cannot be false."));

                        test.assertNull(window.getContent());
                        test.assertNull(text.getParent());
                    }
                });
            });

            runner.testGroup("setPainter(UIPainter)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    try (final Window window = windowCreator.run(test))
                    {
                        test.assertThrows(() -> window.setPainter(null), new PreConditionFailure("painter cannot be null."));
                    }
                });

                runner.test("with non-null", (Test test) ->
                {
                    try (final Window window = windowCreator.run(test))
                    {
                        window.setPainter(new FakePainter());
                    }
                });
            });

            runner.testGroup("repaint()", () ->
            {
                runner.test("before open with no content", (Test test) ->
                {
                    try (final Window window = windowCreator.run(test))
                    {
                        final FakePainter painter = new FakePainter();
                        window.setPainter(painter);

                        window.repaint();

                        test.assertFalse(painter.getActions().any());
                    }
                });

                runner.test("before open with content", (Test test) ->
                {
                    try (final Window window = windowCreator.run(test))
                    {
                        final FakePainter painter = new FakePainter();
                        window.setPainter(painter);
                        window.setContent(new UIText("ABC"));

                        window.repaint();

                        test.assertFalse(painter.getActions().any());
                    }
                });

                runner.test("after open with no content", (Test test) ->
                {
                    try (final Window window = windowCreator.run(test))
                    {
                        final FakePainter painter = new FakePainter();
                        window.setPainter(painter);

                        window.open();
                        test.assertFalse(painter.getActions().any());

                        window.repaint();
                        test.assertFalse(painter.getActions().any());
                    }
                });
            });

            runner.testGroup("setWidth(Distance)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    try (final Window window = windowCreator.run(test))
                    {
                        test.assertThrows(() -> window.setWidth(null), new PreConditionFailure("width cannot be null."));
                    }
                });

                runner.test("with negative", (Test test) ->
                {
                    try (final Window window = windowCreator.run(test))
                    {
                        test.assertThrows(() -> window.setWidth(Distance.inches(-1)), new PreConditionFailure("width (-1.0 Inches) must be greater than or equal to 0.0 Inches."));
                    }
                });

                runner.test("with zero", runner.skip(), (Test test) ->
                {
                    try (final Window window = windowCreator.run(test))
                    {
                        window.open();
                        window.setWidth(Distance.zero);
                        test.assertOneOf(new Distance[] { Distance.zero, Distance.inches(2.6875) }, window.getWidth());
                    }
                });

                runner.test("with positive with useDisplayScaling", (Test test) ->
                {
                    try (final Window window = windowCreator.run(test))
                    {
                        window.open();
                        window.setWidth(Distance.inches(5));
                        test.assertEqual(Distance.inches(5), window.getWidth());
                    }
                });

                runner.test("with positive larger than the screen width", runner.skip(), (Test test) ->
                {
                    try (final Window window = windowCreator.run(test))
                    {
                        window.open();
                        window.setWidth(Distance.miles(3));
                        test.assertOneOf(new Distance[] { Distance.miles(3), Distance.inches(13.489583333333334) }, window.getWidth());
                    }
                });
            });

            runner.testGroup("setHeight(Distance)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    try (final Window window = windowCreator.run(test))
                    {
                        test.assertThrows(() -> window.setHeight(null), new PreConditionFailure("height cannot be null."));
                    }
                });

                runner.test("with negative", (Test test) ->
                {
                    try (final Window window = windowCreator.run(test))
                    {
                        test.assertThrows(() -> window.setHeight(Distance.inches(-1)), new PreConditionFailure("height (-1.0 Inches) must be greater than or equal to 0.0 Inches."));
                    }
                });

                runner.test("with zero", runner.skip(), (Test test) ->
                {
                    try (final Window window = windowCreator.run(test))
                    {
                        window.open();
                        window.setHeight(Distance.zero);
                        test.assertOneOf(new Distance[] { Distance.zero, Distance.inches(0.7395833333333334) }, window.getHeight());
                    }
                });

                runner.test("with positive", (Test test) ->
                {
                    try (final Window window = windowCreator.run(test))
                    {
                        window.open();
                        window.setHeight(Distance.inches(3));
                        test.assertEqual(Distance.inches(3), window.getHeight());
                    }
                });

                runner.test("with positive larger than the screen height", runner.skip(), (Test test) ->
                {
                    try (final Window window = windowCreator.run(test))
                    {
                        window.open();
                        window.setHeight(Distance.miles(3));
                        test.assertOneOf(new Distance[] { Distance.miles(3), Distance.inches(7.65625) }, window.getHeight());
                    }
                });
            });
        });
    }
}
