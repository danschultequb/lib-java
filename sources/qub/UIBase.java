package qub;

public abstract class UIBase
{
    private final Display display;
    private final AsyncRunner asyncRunner;

    protected UIBase(Display display, AsyncRunner asyncRunner)
    {
        PreCondition.assertNotNull(display, "display");
        PreCondition.assertNotNull(asyncRunner, "asyncRunner");

        this.display = display;
        this.asyncRunner = asyncRunner;
    }

    public Result<Void> scheduleAsyncTask(Action0 action)
    {
        PreCondition.assertNotNull(action, "action");

        return this.asyncRunner.schedule(action);
    }

    public PausedAsyncTask<Void> createPausedAsyncTask(Action0 action)
    {
        PreCondition.assertNotNull(action, "action");

        return this.asyncRunner.create(action);
    }

    public PausedAsyncTask<Void> createPausedAsyncTask()
    {
        return this.createPausedAsyncTask(() -> {});
    }

    public double convertHorizontalDistanceToPixels(Distance horizontalDistance)
    {
        PreCondition.assertNotNull(horizontalDistance, "horizontalDistance");

        final double result = this.display.convertHorizontalDistanceToPixels(horizontalDistance);

        PostCondition.assertGreaterThanOrEqualTo(result, 0, "result");

        return result;
    }

    public Distance convertHorizontalPixelsToDistance(double horizontalPixels)
    {
        PreCondition.assertGreaterThanOrEqualTo(horizontalPixels, 0, "horizontalPixels");

        final Distance result = this.display.convertHorizontalPixelsToDistance(horizontalPixels);

        PostCondition.assertGreaterThanOrEqualTo(result, Distance.zero, "result");

        return result;
    }

    public double convertVerticalDistanceToPixels(Distance verticalDistance)
    {
        PreCondition.assertNotNull(verticalDistance, "verticalDistance");

        final double result = this.display.convertVerticalDistanceToPixels(verticalDistance);

        PostCondition.assertGreaterThanOrEqualTo(result, 0, "result");

        return result;
    }

    public Distance convertVerticalPixelsToDistance(double verticalPixels)
    {
        PreCondition.assertGreaterThanOrEqualTo(verticalPixels, 0, "verticalPixels");

        final Distance result = this.display.convertVerticalPixelsToDistance(verticalPixels);

        PostCondition.assertGreaterThanOrEqualTo(result, Distance.zero, "result");

        return result;
    }
}
