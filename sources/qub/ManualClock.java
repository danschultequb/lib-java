package qub;

/**
 * A Clock implementation that allows time to be manually controlled.
 */
public class ManualClock implements Clock
{
    private final AsyncRunner mainAsyncRunner;
    private final List<PausedTask> pausedTasks;
    private DateTime currentDateTime;

    private ManualClock(AsyncRunner mainAsyncRunner, DateTime currentDateTime)
    {
        this.mainAsyncRunner = mainAsyncRunner;
        this.pausedTasks = new ArrayList<>();
        this.currentDateTime = currentDateTime;
    }

    /**
     * Create a new ManualClock object that starts at the provided current date and time.
     */
    public static Result<ManualClock> create(AsyncRunner mainAsyncRunner, DateTime currentDateTime)
    {
        Result<ManualClock> result = Result.notNull(mainAsyncRunner, "mainAsyncRunner");
        if (result == null)
        {
            result = Result.notNull(currentDateTime, "currentDateTime");
            if (result == null)
            {
                result = Result.success(new ManualClock(mainAsyncRunner, currentDateTime));
            }
        }
        return result;
    }

    /**
     * Get the number of tasks that are waiting for the clock to advance to their start times before
     * they will be scheduled.
     * @return The number of tasks that are waiting.
     */
    public int getPausedTaskCount()
    {
        return pausedTasks.getCount();
    }

    @Override
    public DateTime getCurrentDateTime()
    {
        return currentDateTime;
    }

    @Override
    public AsyncAction scheduleAt(DateTime dateTime, Action0 action)
    {
        AsyncAction result;
        if (dateTime.lessThanOrEqualTo(getCurrentDateTime()))
        {
            result = mainAsyncRunner.schedule(action);
        }
        else
        {
            final BasicAsyncAction pausedAsyncAction = new BasicAsyncAction(new Value<>(mainAsyncRunner), action);
            result = pausedAsyncAction;

            final PausedTask pausedTask = new PausedTask(pausedAsyncAction, dateTime);

            int insertIndex = -1;
            final int pausedTaskCount = pausedTasks.getCount();
            for (int i = 0; i < pausedTaskCount; ++i)
            {
                final PausedTask existingPausedTask = pausedTasks.get(i);
                if (pausedTask.isScheduledBefore(existingPausedTask))
                {
                    insertIndex = i;
                    break;
                }
            }

            if (insertIndex == -1)
            {
                pausedTasks.add(pausedTask);
            }
            else
            {
                pausedTasks.insert(insertIndex, pausedTask);
            }
        }
        return result;
    }

    @Override
    public AsyncAction scheduleAfter(Duration duration, Action0 action)
    {
        final DateTime dateTime = getCurrentDateTime().plus(duration);
        return scheduleAt(dateTime, action);
    }

    /**
     * Advance this clock forward by the provided duration.
     * @param duration The duration to advance this Clock forward by.
     */
    public void advance(Duration duration)
    {
        currentDateTime = currentDateTime.plus(duration);
        while (pausedTasks.any() && pausedTasks.first().getScheduledAt().lessThanOrEqualTo(currentDateTime))
        {
            pausedTasks.removeFirst().schedulePausedAction();
        }
    }

    private static class PausedTask
    {
        private final BasicAsyncAction pausedAction;
        private final DateTime scheduleAt;

        PausedTask(BasicAsyncAction pausedAction, DateTime scheduleAt)
        {
            this.pausedAction = pausedAction;
            this.scheduleAt = scheduleAt;
        }

        void schedulePausedAction()
        {
            pausedAction.schedule();
        }

        boolean isScheduledBefore(PausedTask pausedTask)
        {
            return scheduleAt.lessThan(pausedTask.scheduleAt);
        }

        DateTime getScheduledAt()
        {
            return scheduleAt;
        }
    }
}

