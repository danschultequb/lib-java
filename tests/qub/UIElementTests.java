package qub;

public interface UIElementTests
{
    static void test(TestRunner runner, Function1<Test,? extends UIElement> creator)
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
                        final UIElement uiElement = creator.run(test);
                        test.assertThrows(() -> uiElement.setWidth(width), expected);
                    });
                };
    
                setWidthErrorTest.run(null, new PreConditionFailure("width cannot be null."));
                setWidthErrorTest.run(Distance.inches(-1), new PreConditionFailure("width (-1.0 Inches) must be greater than or equal to 0.0 Inches."));
    
                final Action1<Distance> setWidthTest = (Distance width) ->
                {
                    runner.test("with " + width, (Test test) ->
                    {
                        final UIElement uiElement = creator.run(test);
    
                        final UIElement setWidthResult = uiElement.setWidth(width);
                        test.assertSame(uiElement, setWidthResult);
    
                        test.assertEqual(width, uiElement.getWidth());
                    });
                };
    
                setWidthTest.run(Distance.inches(1));
                setWidthTest.run(Distance.inches(5));
            });
    
            runner.testGroup("setHeight(Distance)", () ->
            {
                final Action2<Distance,Throwable> setHeightErrorTest = (Distance height, Throwable expected) ->
                {
                    runner.test("with " + height, (Test test) ->
                    {
                        final UIElement uiElement = creator.run(test);
                        test.assertThrows(() -> uiElement.setHeight(height), expected);
                    });
                };
    
                setHeightErrorTest.run(null, new PreConditionFailure("height cannot be null."));
                setHeightErrorTest.run(Distance.inches(-1), new PreConditionFailure("height (-1.0 Inches) must be greater than or equal to 0.0 Inches."));
    
                final Action1<Distance> setHeightTest = (Distance height) ->
                {
                    runner.test("with " + height, (Test test) ->
                    {
                        final UIElement uiElement = creator.run(test);
    
                        final UIElement setHeightResult = uiElement.setHeight(height);
                        test.assertSame(uiElement, setHeightResult);
    
                        test.assertEqual(height, uiElement.getHeight());
                    });
                };
    
                setHeightTest.run(Distance.inches(1));
                setHeightTest.run(Distance.inches(5));
            });
    
            runner.testGroup("setSize(Size2D)", () ->
            {
                final Action2<Size2D,Throwable> setSizeErrorTest = (Size2D size, Throwable expected) ->
                {
                    runner.test("with " + size, (Test test) ->
                    {
                        final UIElement uiElement = creator.run(test);
                        test.assertThrows(() -> uiElement.setSize(size), expected);
                    });
                };
    
                setSizeErrorTest.run(null, new PreConditionFailure("size cannot be null."));
    
                final Action1<Size2D> setSizeTest = (Size2D size) ->
                {
                    runner.test("with " + size, (Test test) ->
                    {
                        final UIElement uiElement = creator.run(test);
                        final UIElement setSizeResult = uiElement.setSize(size);
                        test.assertSame(uiElement, setSizeResult);
                        test.assertEqual(size, uiElement.getSize());
                    });
                };
    
                setSizeTest.run(Size2D.zero);
                setSizeTest.run(new Size2D(Distance.zero, Distance.inches(1)));
                setSizeTest.run(new Size2D(Distance.inches(1), Distance.zero));
                setSizeTest.run(new Size2D(Distance.inches(2), Distance.inches(3)));
            });
    
            runner.testGroup("setSize(Distance,Distance)", () ->
            {
                final Action3<Distance,Distance,Throwable> setSizeErrorTest = (Distance width, Distance height, Throwable expected) ->
                {
                    runner.test("with " + English.andList(width, height), (Test test) ->
                    {
                        final UIElement uiElement = creator.run(test);
                        test.assertThrows(() -> uiElement.setSize(width, height), expected);
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
                        final UIElement uiElement = creator.run(test);
                        final UIElement setSizeResult = uiElement.setSize(width, height);
                        test.assertSame(uiElement, setSizeResult);
                        test.assertEqual(width, uiElement.getWidth());
                        test.assertEqual(height, uiElement.getHeight());
                    });
                };
    
                setSizeTest.run(Distance.zero, Distance.zero);
                setSizeTest.run(Distance.zero, Distance.inches(2));
                setSizeTest.run(Distance.inches(1.5), Distance.zero);
                setSizeTest.run(Distance.inches(5), Distance.inches(4));
            });
        });
    }
}
