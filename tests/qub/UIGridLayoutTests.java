package qub;

public interface UIGridLayoutTests
{
    static void test(TestRunner runner, Function1<Test,? extends UIGridLayout> creator)
    {
        PreCondition.assertNotNull(runner, "runner");
        PreCondition.assertNotNull(creator, "creator");
        
        runner.testGroup(UIGridLayout.class, () ->
        {
            UIElementTests.test(runner, creator);
            
            runner.testGroup("setWidth(Distance)", () ->
            {
                runner.test("should return " + Types.getTypeName(UIGridLayout.class), (Test test) ->
                {
                    final UIGridLayout gridLayout = creator.run(test);
                    final UIGridLayout setWidthResult = gridLayout.setWidth(Distance.inches(1));
                    test.assertSame(gridLayout, setWidthResult);
                });
            });

            runner.testGroup("setHeight(Distance)", () ->
            {
                runner.test("should return " + Types.getTypeName(UIGridLayout.class), (Test test) ->
                {
                    final UIGridLayout gridLayout = creator.run(test);
                    final UIGridLayout setHeightResult = gridLayout.setHeight(Distance.inches(1));
                    test.assertSame(gridLayout, setHeightResult);
                });
            });

            runner.testGroup("setSize(Size2D)", () ->
            {
                runner.test("should return " + Types.getTypeName(UIGridLayout.class), (Test test) ->
                {
                    final UIGridLayout gridLayout = creator.run(test);
                    final UIGridLayout setHeightResult = gridLayout.setSize(Size2D.create(Distance.inches(2), Distance.inches(3)));
                    test.assertSame(gridLayout, setHeightResult);
                });
            });

            runner.testGroup("setSize(Distance,Distance)", () ->
            {
                runner.test("should return " + Types.getTypeName(UIGridLayout.class), (Test test) ->
                {
                    final UIGridLayout gridLayout = creator.run(test);
                    final UIGridLayout setHeightResult = gridLayout.setSize(Distance.inches(2), Distance.inches(3));
                    test.assertSame(gridLayout, setHeightResult);
                });
            });

            runner.testGroup("set(int,int,UIElement)", () ->
            {
                final Action3<Integer,Integer,Throwable> setErrorTest = (Integer x, Integer y, Throwable expected) ->
                {
                    runner.test("with " + English.andList(x, y), (Test test) ->
                    {
                        final UIGridLayout gridLayout = creator.run(test);
                        final UIElement element = creator.run(test);
                        test.assertThrows(() -> gridLayout.setUIElement(x, y, element), expected);
                    });
                };

                setErrorTest.run(-1, 0, new PreConditionFailure("x (-1) must be between 0 and 19."));
                setErrorTest.run(20, 0, new PreConditionFailure("x (20) must be between 0 and 19."));
                setErrorTest.run(0, -1, new PreConditionFailure("y (-1) must be between 0 and 29."));
                setErrorTest.run(0, 30, new PreConditionFailure("y (30) must be between 0 and 29."));

                runner.test("with null element", (Test test) ->
                {
                    final UIGridLayout gridLayout = creator.run(test);
                    final int x = 0;
                    final int y = 0;
                    final UIElement element = null;
                    test.assertThrows(() -> gridLayout.setUIElement(x, y, element),
                        new PreConditionFailure("element cannot be null."));
                    test.assertThrows(() -> gridLayout.getUIElement(x, y).await(),
                        new NotFoundException("No element exists at (x:0, y:0)."));
                });

                final Action2<Integer,Integer> setTest = (Integer x, Integer y) ->
                {
                    runner.test("with " + English.andList(x, y), (Test test) ->
                    {
                        final UIGridLayout gridLayout = creator.run(test);
                        final UIElement element = creator.run(test);
                        final UIGridLayout setResult = gridLayout.setUIElement(x, y, element);
                        test.assertSame(gridLayout, setResult);
                        test.assertSame(element, gridLayout.getUIElement(x, y).await());
                    });
                };

                setTest.run(0, 0);
                setTest.run(1, 0);
                setTest.run(0, 1);
                setTest.run(3, 4);
            });

            runner.testGroup("get(int,int)", () ->
            {
                final Action3<Integer,Integer,Throwable> getErrorTest = (Integer x, Integer y, Throwable expected) ->
                {
                    runner.test("with " + English.andList(x, y), (Test test) ->
                    {
                        final UIGridLayout gridLayout = creator.run(test);
                        test.assertThrows(() -> gridLayout.getUIElement(x, y).await(), expected);
                    });
                };

                getErrorTest.run(-1, 0, new PreConditionFailure("x (-1) must be greater than or equal to 0."));
                getErrorTest.run(0, -1, new PreConditionFailure("y (-1) must be greater than or equal to 0."));
                getErrorTest.run(0, 0, new NotFoundException("No element exists at (x:0, y:0)."));
                getErrorTest.run(5, 10, new NotFoundException("No element exists at (x:5, y:10)."));
            });
        });
    }
}
