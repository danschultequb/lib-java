package qub;

public interface UIElementTests
{
    static void test(TestRunner runner, Function1<FakeDesktopProcess,? extends UIElement> creator)
    {
        PreCondition.assertNotNull(runner, "runner");
        PreCondition.assertNotNull(creator, "creator");
        
        runner.testGroup(UIElement.class, () ->
        {
            runner.testGroup("setWidth(Distance)", () ->
            {
                final Action2<Distance,Throwable> setWidthErrorTest = (Distance width, Throwable expected) ->
                {
                    runner.test("with " + width, (Test test) ->
                    {
                        try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                        {
                            final UIElement uiElement = creator.run(process);
                            test.assertThrows(() -> uiElement.setWidth(width), expected);
                        }
                    });
                };
    
                setWidthErrorTest.run(null, new PreConditionFailure("width cannot be null."));
                setWidthErrorTest.run(Distance.inches(-1), new PreConditionFailure("width (-1.0 Inches) must be greater than or equal to 0.0 Inches."));
    
                final Action1<Distance> setWidthTest = (Distance width) ->
                {
                    runner.test("with " + width, (Test test) ->
                    {
                        try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                        {
                            final UIElement uiElement = creator.run(process);

                            final UIElement setWidthResult = uiElement.setWidth(width);
                            test.assertSame(uiElement, setWidthResult);

                            test.assertEqual(width, uiElement.getWidth());
                        }
                    });
                };
    
                setWidthTest.run(Distance.inches(1));
                setWidthTest.run(Distance.inches(5));
            });

            runner.testGroup("setWidthInPixels(int)", () ->
            {
                final Action2<Integer,Throwable> setWidthInPixelsErrorTest = (Integer widthInPixels, Throwable expected) ->
                {
                    runner.test("with " + widthInPixels, (Test test) ->
                    {
                        try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                        {
                            final UIElement uiElement = creator.run(process);
                            test.assertThrows(() -> uiElement.setWidthInPixels(widthInPixels), expected);
                        }
                    });
                };

                setWidthInPixelsErrorTest.run(-1, new PreConditionFailure("widthInPixels (-1) must be greater than or equal to 0."));

                final Action1<Integer> setWidthInPixelsTest = (Integer widthInPixels) ->
                {
                    runner.test("with " + widthInPixels, (Test test) ->
                    {
                        try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                        {
                            final UIElement uiElement = creator.run(process);

                            final UIElement setWidthResult = uiElement.setWidthInPixels(widthInPixels);
                            test.assertSame(uiElement, setWidthResult);

                            test.assertEqual(widthInPixels, uiElement.getWidthInPixels());
                        }
                    });
                };

                setWidthInPixelsTest.run(0);
                setWidthInPixelsTest.run(1);
                setWidthInPixelsTest.run(100);
            });

            runner.test("getDynamicWidth()", (Test test) ->
            {
                try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                {
                    final UIElement uiElement = creator.run(process);

                    try (final DynamicDistance dynamicWidth = uiElement.getDynamicWidth())
                    {
                        test.assertNotNull(dynamicWidth);
                        test.assertEqual(uiElement.getWidth(), dynamicWidth.get());

                        final IntegerValue counter = IntegerValue.create(0);
                        final SpinGate gate = SpinGate.create(process.getClock());
                        dynamicWidth.onChanged(() ->
                        {
                            counter.increment();
                            gate.open();
                        });

                        uiElement.setWidth(Distance.inches(4));
                        gate.passThrough(() -> process.getMainAsyncRunner().schedule(Action0.empty).await());
                        test.assertEqual(Distance.inches(4), dynamicWidth.get());
                        test.assertEqual(1, counter.get());
                    }
                }
            });
    
            runner.testGroup("setHeight(Distance)", () ->
            {
                final Action2<Distance,Throwable> setHeightErrorTest = (Distance height, Throwable expected) ->
                {
                    runner.test("with " + height, (Test test) ->
                    {
                        try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                        {
                            final UIElement uiElement = creator.run(process);
                            test.assertThrows(() -> uiElement.setHeight(height), expected);
                        }
                    });
                };
    
                setHeightErrorTest.run(null, new PreConditionFailure("height cannot be null."));
                setHeightErrorTest.run(Distance.inches(-1), new PreConditionFailure("height (-1.0 Inches) must be greater than or equal to 0.0 Inches."));
    
                final Action1<Distance> setHeightTest = (Distance height) ->
                {
                    runner.test("with " + height, (Test test) ->
                    {
                        try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                        {
                            final UIElement uiElement = creator.run(process);

                            final UIElement setHeightResult = uiElement.setHeight(height);
                            test.assertSame(uiElement, setHeightResult);

                            test.assertEqual(height, uiElement.getHeight());
                        }
                    });
                };
    
                setHeightTest.run(Distance.inches(1));
                setHeightTest.run(Distance.inches(5));
            });

            runner.testGroup("setHeightInPixels(int)", () ->
            {
                final Action2<Integer,Throwable> setHeightInPixelsErrorTest = (Integer heightInPixels, Throwable expected) ->
                {
                    runner.test("with " + heightInPixels, (Test test) ->
                    {
                        try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                        {
                            final UIElement uiElement = creator.run(process);
                            test.assertThrows(() -> uiElement.setHeightInPixels(heightInPixels), expected);
                        }
                    });
                };

                setHeightInPixelsErrorTest.run(-1, new PreConditionFailure("heightInPixels (-1) must be greater than or equal to 0."));

                final Action1<Integer> setHeightInPixelsTest = (Integer heightInPixels) ->
                {
                    runner.test("with " + heightInPixels, (Test test) ->
                    {
                        try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                        {
                            final UIElement uiElement = creator.run(process);

                            final UIElement setHeightInPixelsResult = uiElement.setHeightInPixels(heightInPixels);
                            test.assertSame(uiElement, setHeightInPixelsResult);

                            test.assertEqual(heightInPixels, uiElement.getHeightInPixels());
                        }
                    });
                };

                setHeightInPixelsTest.run(0);
                setHeightInPixelsTest.run(1);
                setHeightInPixelsTest.run(100);
            });

            runner.test("getDynamicHeight()", (Test test) ->
            {
                try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                {
                    final UIElement uiElement = creator.run(process);

                    final DynamicDistance dynamicHeight = uiElement.getDynamicHeight();
                    test.assertNotNull(dynamicHeight);
                    test.assertEqual(uiElement.getHeight(), dynamicHeight.get());

                    final IntegerValue counter = IntegerValue.create(0);
                    final SpinGate gate = SpinGate.create(process.getClock());
                    dynamicHeight.onChanged(() ->
                    {
                        counter.increment();
                        gate.open();
                    });

                    uiElement.setHeight(Distance.inches(4));
                    gate.passThrough(() -> process.getMainAsyncRunner().schedule(Action0.empty).await());
                    test.assertEqual(Distance.inches(4), dynamicHeight.get());
                    test.assertEqual(1, counter.get());
                }
            });
    
            runner.testGroup("setSize(Size2D)", () ->
            {
                final Action2<Size2D,Throwable> setSizeErrorTest = (Size2D size, Throwable expected) ->
                {
                    runner.test("with " + size, (Test test) ->
                    {
                        try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                        {
                            final UIElement uiElement = creator.run(process);
                            test.assertThrows(() -> uiElement.setSize(size), expected);
                        }
                    });
                };
    
                setSizeErrorTest.run(null, new PreConditionFailure("size cannot be null."));
    
                final Action1<Size2D> setSizeTest = (Size2D size) ->
                {
                    runner.test("with " + size, (Test test) ->
                    {
                        try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                        {
                            final UIElement uiElement = creator.run(process);
                            final UIElement setSizeResult = uiElement.setSize(size);
                            test.assertSame(uiElement, setSizeResult);
                            test.assertEqual(size, uiElement.getSize());
                        }
                    });
                };
    
                setSizeTest.run(Size2D.create(Distance.zero, Distance.zero));
                setSizeTest.run(Size2D.create(Distance.zero, Distance.inches(1)));
                setSizeTest.run(Size2D.create(Distance.inches(1), Distance.zero));
                setSizeTest.run(Size2D.create(Distance.inches(2), Distance.inches(3)));
            });
    
            runner.testGroup("setSize(Distance,Distance)", () ->
            {
                final Action3<Distance,Distance,Throwable> setSizeErrorTest = (Distance width, Distance height, Throwable expected) ->
                {
                    runner.test("with " + English.andList(width, height), (Test test) ->
                    {
                        try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                        {
                            final UIElement uiElement = creator.run(process);
                            test.assertThrows(() -> uiElement.setSize(width, height), expected);
                        }
                    });
                };
    
                setSizeErrorTest.run(null, Distance.inches(1), new PreConditionFailure("width cannot be null."));
                setSizeErrorTest.run(Distance.inches(-1), Distance.inches(1), new PreConditionFailure("width (-1.0 Inches) must be greater than or equal to 0.0 Inches."));
                setSizeErrorTest.run(Distance.zero, null, new PreConditionFailure("height cannot be null."));
                setSizeErrorTest.run(Distance.zero, Distance.inches(-1), new PreConditionFailure("height (-1.0 Inches) must be greater than or equal to 0.0 Inches."));
    
                final Action2<Distance,Distance> setSizeTest = (Distance width, Distance height) ->
                {
                    runner.test("with " + English.andList(width, height), (Test test) ->
                    {
                        try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                        {
                            final UIElement uiElement = creator.run(process);
                            final UIElement setSizeResult = uiElement.setSize(width, height);
                            test.assertSame(uiElement, setSizeResult);
                            test.assertEqual(width, uiElement.getWidth());
                            test.assertEqual(height, uiElement.getHeight());
                        }
                    });
                };
    
                setSizeTest.run(Distance.zero, Distance.zero);
                setSizeTest.run(Distance.zero, Distance.inches(2));
                setSizeTest.run(Distance.inches(1.5), Distance.zero);
                setSizeTest.run(Distance.inches(5), Distance.inches(4));
            });

            runner.testGroup("setSizeInPixels(int,int)", () ->
            {
                final Action3<Integer,Integer,Throwable> setSizeInPixelsErrorTest = (Integer widthInPixels, Integer heightInPixels, Throwable expected) ->
                {
                    runner.test("with " + English.andList(widthInPixels, heightInPixels), (Test test) ->
                    {
                        try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                        {
                            final UIElement uiElement = creator.run(process);
                            test.assertThrows(() -> uiElement.setSizeInPixels(widthInPixels, heightInPixels), expected);
                        }
                    });
                };

                setSizeInPixelsErrorTest.run(-1, 1, new PreConditionFailure("widthInPixels (-1) must be greater than or equal to 0."));
                setSizeInPixelsErrorTest.run(0, -1, new PreConditionFailure("heightInPixels (-1) must be greater than or equal to 0."));

                final Action2<Integer,Integer> setSizeInPixelsTest = (Integer widthInPixels, Integer heightInPixels) ->
                {
                    runner.test("with " + English.andList(widthInPixels, heightInPixels), (Test test) ->
                    {
                        try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                        {
                            final UIElement uiElement = creator.run(process);
                            final UIElement setSizeResult = uiElement.setSizeInPixels(widthInPixels, heightInPixels);
                            test.assertSame(uiElement, setSizeResult);
                            test.assertEqual(widthInPixels, uiElement.getWidthInPixels());
                            test.assertEqual(heightInPixels, uiElement.getHeightInPixels());
                        }
                    });
                };

                setSizeInPixelsTest.run(0, 0);
                setSizeInPixelsTest.run(0, 2);
                setSizeInPixelsTest.run(1, 0);
                setSizeInPixelsTest.run(5, 4);
            });

            runner.testGroup("onSizeChanged(Action0)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final UIElement uiElement = creator.run(process);
                        test.assertThrows(() -> uiElement.onSizeChanged(null),
                            new PreConditionFailure("callback cannot be null."));
                    }
                });

                runner.test("with non-null", runner.skip(false), (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final UIElement uiElement = creator.run(process);
                        final long currentThreadId = CurrentThread.getId();
                        final LongValue eventThreadId = LongValue.create();

                        final IntegerValue value = IntegerValue.create(0);
                        final Disposable subscription = uiElement.onSizeChanged(() ->
                        {
                            eventThreadId.set(CurrentThread.getId());
                            value.increment();
                        });
                        test.assertNotNull(subscription);
                        test.assertFalse(subscription.isDisposed());
                        test.assertFalse(eventThreadId.hasValue());
                        test.assertEqual(0, value.get());

                        uiElement.setSize(Distance.inches(10), Distance.inches(12));

                        test.assertEqual(currentThreadId, eventThreadId.get());
                        test.assertEqual(1, value.get());

                        test.assertTrue(subscription.dispose().await());
                        test.assertEqual(1, value.get());

                        eventThreadId.clear();
                        value.set(0);

                        uiElement.setSize(Distance.inches(9), Distance.inches(11));

                        test.assertFalse(eventThreadId.hasValue());
                        test.assertEqual(0, value.get());
                    }
                });
            });

            runner.test("getPadding()", (Test test) ->
            {
                try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                {
                    final UIElement uiElement = creator.run(process);
                    final UIPadding padding = uiElement.getPadding();
                    test.assertNotNull(padding);
                    test.assertEqual(padding, uiElement.getPadding());
                }
            });

            runner.testGroup("setPadding(UIPadding)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final UIElement uiElement = creator.run(process);
                        final UIPadding padding = uiElement.getPadding();
                        test.assertThrows(() -> uiElement.setPadding(null),
                            new PreConditionFailure("padding cannot be null."));
                        test.assertEqual(padding, uiElement.getPadding());
                    }
                });

                runner.test("with non-null", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final UIElement uiElement = creator.run(process);
                        final UIPadding padding = UIPadding.create(Distance.inches(1), Distance.inches(2), Distance.inches(3), Distance.inches(4));
                        final UIElement setPaddingResult = uiElement.setPadding(padding);
                        test.assertSame(uiElement, setPaddingResult);
                        test.assertEqual(padding, uiElement.getPadding());
                    }
                });
            });

            runner.testGroup("setPaddingInPixels(UIPaddingInPixels)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final UIElement uiElement = creator.run(process);
                        final UIPaddingInPixels padding = uiElement.getPaddingInPixels();
                        test.assertThrows(() -> uiElement.setPaddingInPixels(null),
                            new PreConditionFailure("padding cannot be null."));
                        test.assertEqual(padding, uiElement.getPaddingInPixels());
                    }
                });

                runner.test("with non-null", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final UIElement uiElement = creator.run(process);
                        final UIPaddingInPixels padding = UIPaddingInPixels.create(1, 2, 3, 4);
                        final UIElement setPaddingResult = uiElement.setPaddingInPixels(padding);
                        test.assertSame(uiElement, setPaddingResult);
                        test.assertEqual(padding, uiElement.getPaddingInPixels());
                    }
                });
            });

            runner.testGroup("onPaddingChanged(Action0)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final UIElement uiElement = creator.run(process);
                        test.assertThrows(() -> uiElement.onPaddingChanged((Action0)null),
                            new PreConditionFailure("callback cannot be null."));
                    }
                });

                runner.test("when padding set to equal padding", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final UIElement uiElement = creator.run(process);
                        final IntegerValue changes = IntegerValue.create(0);
                        uiElement.onPaddingChanged(changes::increment);

                        uiElement.setPadding(uiElement.getPadding());

                        test.assertEqual(0, changes.get());
                    }
                });

                runner.test("when padding set to different padding", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final UIElement uiElement = creator.run(process);
                        final IntegerValue changes = IntegerValue.create(0);
                        uiElement.onPaddingChanged(changes::increment);

                        uiElement.setPadding(UIPadding.create(Distance.inches(1)));

                        test.assertEqual(1, changes.get());
                    }
                });
            });

            runner.testGroup("getContentSpaceSize()", () ->
            {
                runner.test("with no padding", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final UIElement uiElement = creator.run(process);
                        uiElement.setPadding(UIPadding.zero);

                        final Size2D contentSpaceSize = uiElement.getContentSpaceSize();
                        final Size2D size = uiElement.getSize();
                        test.assertEqual(size, contentSpaceSize);
                    }
                });

                runner.test("with padding", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final UIElement uiElement = creator.run(process);
                        final UIPadding padding = UIPadding.create(Distance.inches(0.01));
                        uiElement.setPadding(padding);

                        final Size2D contentSpaceSize = uiElement.getContentSpaceSize();
                        final Distance width = uiElement.getWidth();
                        final Distance expectedWidth = width.greaterThan(padding.getWidth()) ? width.minus(padding.getWidth()) : Distance.zero;
                        final Distance height = uiElement.getHeight();
                        final Distance expectedHeight = height.greaterThan(padding.getHeight()) ? height.minus(padding.getHeight()) : Distance.zero;
                        final Size2D expectedSize = Size2D.create(expectedWidth, expectedHeight);
                        test.assertEqual(expectedSize, contentSpaceSize, Size2D.create(Distance.inches(0.00001), Distance.inches(0.00001)));
                    }
                });
            });

            runner.testGroup("getContentSpaceWidth()", () ->
            {
                runner.test("with no padding", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final UIElement uiElement = creator.run(process);
                        uiElement.setPadding(UIPadding.zero);

                        final Distance contentSpaceWidth = uiElement.getContentSpaceWidth();
                        final Distance width = uiElement.getWidth();
                        test.assertEqual(width, contentSpaceWidth);
                    }
                });

                runner.test("with padding", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final UIElement uiElement = creator.run(process);
                        final UIPadding padding = UIPadding.create(Distance.inches(0.01));
                        uiElement.setPadding(padding);

                        final Distance contentSpaceWidth = uiElement.getContentSpaceWidth();
                        final Distance width = uiElement.getWidth();
                        final Distance expectedWidth = width.greaterThan(padding.getWidth()) ? width.minus(padding.getWidth()) : Distance.zero;
                        test.assertEqual(expectedWidth, contentSpaceWidth, Distance.inches(0.00001));
                    }
                });
            });

            runner.test("getDynamicContentSpaceWidth()", (Test test) ->
            {
                try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                {
                    final UIElement uiElement = creator.run(process);
                    uiElement.setWidth(Distance.inches(2));
                    uiElement.setPadding(UIPadding.zero);

                    try (final DynamicDistance dynamicContentSpaceWidth = uiElement.getDynamicContentSpaceWidth())
                    {
                        test.assertNotNull(dynamicContentSpaceWidth);
                        test.assertEqual(uiElement.getContentSpaceWidth(), dynamicContentSpaceWidth.get());

                        final IntegerValue counter = IntegerValue.create(0);
                        final SpinGate gate = SpinGate.create(process.getClock());
                        dynamicContentSpaceWidth.onChanged(() ->
                        {
                            counter.increment();
                            gate.open();
                        });

                        uiElement.setWidth(Distance.inches(2.5));
                        gate.passThrough(() -> process.getMainAsyncRunner().schedule(Action0.empty).await());
                        test.assertEqual(Distance.inches(2.5), dynamicContentSpaceWidth.get());
                        test.assertEqual(1, counter.get());

                        gate.close();

                        uiElement.setPadding(UIPadding.create(Distance.inches(0.25)));
                        gate.passThrough(() -> process.getMainAsyncRunner().schedule(Action0.empty).await());
                        test.assertEqual(Distance.inches(2), dynamicContentSpaceWidth.get());
                        test.assertEqual(2, counter.get());
                    }
                }
            });

            runner.testGroup("getContentSpaceWidthInPixels()", () ->
            {
                runner.test("with no padding", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final UIElement uiElement = creator.run(process);
                        uiElement.setPadding(UIPadding.zero);

                        final int contentSpaceWidth = uiElement.getContentSpaceWidthInPixels();
                        final int width = uiElement.getWidthInPixels();
                        test.assertEqual(width, contentSpaceWidth);
                    }
                });

                runner.test("with padding", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final UIElement uiElement = creator.run(process);
                        final UIPaddingInPixels padding = UIPaddingInPixels.create(2);
                        uiElement.setPaddingInPixels(padding);

                        final int contentSpaceWidth = uiElement.getContentSpaceWidthInPixels();
                        final int width = uiElement.getWidthInPixels();
                        final int expectedWidth = padding.getWidth() < width ? width - padding.getWidth() : 0;
                        test.assertEqual(expectedWidth, contentSpaceWidth);
                    }
                });
            });

            runner.testGroup("getContentSpaceHeight()", () ->
            {
                runner.test("with no padding", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final UIElement uiElement = creator.run(process);
                        uiElement.setPadding(UIPadding.zero);

                        final Distance contentSpaceHeight = uiElement.getContentSpaceHeight();
                        final Distance width = uiElement.getHeight();
                        test.assertEqual(width, contentSpaceHeight);
                    }
                });

                runner.test("with padding", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final UIElement uiElement = creator.run(process);
                        final UIPadding padding = UIPadding.create(Distance.inches(0.01));
                        uiElement.setPadding(padding);

                        final Distance contentSpaceHeight = uiElement.getContentSpaceHeight();
                        final Distance height = uiElement.getHeight();
                        final Distance expectedHeight = height.greaterThan(padding.getHeight()) ? height.minus(padding.getHeight()) : Distance.zero;
                        test.assertEqual(expectedHeight, contentSpaceHeight, Distance.inches(0.00001));
                    }
                });
            });

            runner.test("getDynamicContentSpaceHeight()", (Test test) ->
            {
                try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                {
                    final UIElement uiElement = creator.run(process);
                    uiElement.setHeight(Distance.inches(2));
                    uiElement.setPadding(UIPadding.zero);

                    try (final DynamicDistance dynamicContentSpaceHeight = uiElement.getDynamicContentSpaceHeight())
                    {
                        test.assertNotNull(dynamicContentSpaceHeight);
                        test.assertEqual(uiElement.getContentSpaceHeight(), dynamicContentSpaceHeight.get());

                        final IntegerValue counter = IntegerValue.create(0);
                        final SpinGate gate = SpinGate.create(process.getClock());
                        dynamicContentSpaceHeight.onChanged(() ->
                        {
                            counter.increment();
                            gate.open();
                        });

                        uiElement.setHeight(Distance.inches(2.5));
                        gate.passThrough(() -> process.getMainAsyncRunner().schedule(Action0.empty).await());
                        test.assertEqual(Distance.inches(2.5), dynamicContentSpaceHeight.get());
                        test.assertEqual(1, counter.get());

                        gate.close();

                        uiElement.setPadding(UIPadding.create(Distance.inches(0.25)));
                        gate.passThrough(() -> process.getMainAsyncRunner().schedule(Action0.empty).await());
                        test.assertEqual(Distance.inches(2), dynamicContentSpaceHeight.get());
                        test.assertEqual(2, counter.get());
                    }
                }
            });

            runner.testGroup("getContentSpaceHeightInPixels()", () ->
            {
                runner.test("with no padding", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final UIElement uiElement = creator.run(process);
                        uiElement.setPadding(UIPadding.zero);

                        final int contentSpaceHeight = uiElement.getContentSpaceHeightInPixels();
                        final int width = uiElement.getHeightInPixels();
                        test.assertEqual(width, contentSpaceHeight);
                    }
                });

                runner.test("with padding", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final UIElement uiElement = creator.run(process);
                        final UIPaddingInPixels padding = UIPaddingInPixels.create(2);
                        uiElement.setPaddingInPixels(padding);

                        final int contentSpaceHeight = uiElement.getContentSpaceHeightInPixels();
                        final int height = uiElement.getHeightInPixels();
                        final int expectedHeight = padding.getHeight() < height ? height - padding.getHeight() : 0;
                        test.assertEqual(expectedHeight, contentSpaceHeight);
                    }
                });
            });

            runner.testGroup("setBackgroundColor(Color)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final UIElement uiElement = creator.run(process);
                        final Color initialColor = uiElement.getBackgroundColor();
                        test.assertThrows(() -> uiElement.setBackgroundColor(null),
                            new PreConditionFailure("backgroundColor cannot be null."));
                        test.assertEqual(initialColor, uiElement.getBackgroundColor());
                    }
                });

                runner.test("with non-null", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final UIElement uiElement = creator.run(process);
                        final UIElement setBackgroundColorResult = uiElement.setBackgroundColor(Color.blue);
                        test.assertSame(uiElement, setBackgroundColorResult);
                        test.assertEqual(Color.blue, uiElement.getBackgroundColor());
                    }
                });
            });
        });
    }
}
