package qub;

/**
 * A Clock implementation that allows time to be manually controlled.
 */
public class ManualClock implements Clock
{
    private final AsyncScheduler asyncRunner;
    private final List<PausedTask> pausedTasks;
    private DateTime currentDateTime;

    private ManualClock(DateTime currentDateTime, AsyncScheduler asyncRunner)
    {
        PreCondition.assertNotNull(currentDateTime, "currentDateTime");

        this.asyncRunner = asyncRunner;
        this.pausedTasks = List.create();
        this.currentDateTime = currentDateTime;
    }

    public static ManualClock create()
    {
        return ManualClock.create(DateTime.epoch);
    }

    public static ManualClock create(DateTime currentDateTime)
    {
        return new ManualClock(currentDateTime, null);
    }

    public static ManualClock create(AsyncScheduler asyncRunner)
    {
        return ManualClock.create(DateTime.epoch, asyncRunner);
    }

    public static ManualClock create(DateTime currentDateTime, AsyncScheduler asyncRunner)
    {
        PreCondition.assertNotNull(currentDateTime, "currentDateTime");
        PreCondition.assertNotNull(asyncRunner, "asyncRunner");

        return new ManualClock(currentDateTime, asyncRunner);
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
    public Result<Void> scheduleAt(DateTime dateTime, Action0 action)
    {
        PreCondition.assertNotNull(dateTime, "dateTime");
        PreCondition.assertNotNull(action, "action");
        PreCondition.assertNotNull(this.asyncRunner, "this.asyncRunner");

        AsyncTask<Void> result;
        if (dateTime.lessThanOrEqualTo(getCurrentDateTime()))
        {
            result = this.asyncRunner.schedule(action);
        }
        else
        {
            result = this.asyncRunner.create(action);

            final PausedTask pausedTask = new PausedTask(result, dateTime);

            int insertIndex = -1;
            final int pausedTaskCount = this.pausedTasks.getCount();
            for (int i = 0; i < pausedTaskCount; ++i)
            {
                final PausedTask existingPausedTask = this.pausedTasks.get(i);
                if (pausedTask.isScheduledBefore(existingPausedTask))
                {
                    insertIndex = i;
                    break;
                }
            }

            if (insertIndex == -1)
            {
                this.pausedTasks.add(pausedTask);
            }
            else
            {
                this.pausedTasks.insert(insertIndex, pausedTask);
            }
        }
        return result;
    }

    /**
     * Advance this clock forward by the provided duration.
     * @param duration The duration to advance this Clock forward by.
     */
    public void advance(Duration duration)
    {
        PreCondition.assertNotNull(duration, "duration");

        this.currentDateTime = this.currentDateTime.plus(duration);
        while (this.pausedTasks.any() && this.pausedTasks.first().getScheduledAt().lessThanOrEqualTo(this.currentDateTime))
        {
            this.pausedTasks.removeFirst().schedulePausedAction();
        }
    }

    private static class PausedTask
    {
        private final PausedAsyncTask<?> pausedAction;
        private final DateTime scheduleAt;

        PausedTask(PausedAsyncTask<?> pausedAction, DateTime scheduleAt)
        {
            this.pausedAction = pausedAction;
            this.scheduleAt = scheduleAt;
        }

        void schedulePausedAction()
        {
            this.pausedAction.schedule();
        }

        boolean isScheduledBefore(PausedTask pausedTask)
        {
            return this.scheduleAt.lessThan(pausedTask.scheduleAt);
        }

        DateTime getScheduledAt()
        {
            return this.scheduleAt;
        }
    }
}

