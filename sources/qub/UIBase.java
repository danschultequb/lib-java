package qub;

public abstract class UIBase
{
    private final Display display;
    private final AsyncRunner mainAsyncRunner;

    protected UIBase(Display display, AsyncRunner mainAsyncRunner)
    {
        PreCondition.assertNotNull(display, "display");
        PreCondition.assertNotNull(mainAsyncRunner, "mainAsyncRunner");

        this.display = display;
        this.mainAsyncRunner = mainAsyncRunner;
    }

    public Result<Void> scheduleAsyncTask(Action0 action)
    {
        PreCondition.assertNotNull(action, "action");

        return this.mainAsyncRunner.schedule(action);
    }

    public PausedAsyncTask<Void> createPausedAsyncTask(Action0 action)
    {
        PreCondition.assertNotNull(action, "action");

        return this.mainAsyncRunner.create(action);
    }

    public PausedAsyncTask<Void> createPausedAsyncTask()
    {
        return this.createPausedAsyncTask(() -> {});
    }

    public double convertHorizontalDistanceToPixels(Distance horizontalDistance)
    {
        return this.display.convertHorizontalDistanceToPixels(horizontalDistance);
    }

    public Distance convertHorizontalPixelsToDistance(double horizontalPixels)
    {
        return this.display.convertHorizontalPixelsToDistance(horizontalPixels);
    }

    public double convertVerticalDistanceToPixels(Distance verticalDistance)
    {
        return this.display.convertVerticalDistanceToPixels(verticalDistance);
    }

    public Distance convertVerticalPixelsToDistance(double verticalPixels)
    {
        return this.display.convertVerticalPixelsToDistance(verticalPixels);
    }

    public Size2D convertPixelsToSize2D(double horizontalPixels, double verticalPixels)
    {
        return this.display.convertPixelsToSize2D(horizontalPixels, verticalPixels);
    }

    public Point2D convertPixelsToPoint2D(double horizontalPixels, double verticalPixels)
    {
        return this.display.convertPixelsToPoint2D(horizontalPixels, verticalPixels);
    }
}
