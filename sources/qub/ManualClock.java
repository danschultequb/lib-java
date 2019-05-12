package qub;

/**
 * A Clock implementation that allows time to be manually controlled.
 */
public class ManualClock implements Clock
{
    private final AsyncScheduler asyncRunner;
    private final List<PausedTask> pausedTasks;
    private DateTime currentDateTime;

    /**
     * Create a new ManualClock object that starts at the provided current date and time.
     */
    public ManualClock(DateTime currentDateTime, AsyncScheduler asyncRunner)
    {
        PreCondition.assertNotNull(currentDateTime, "currentDateTime");
        PreCondition.assertNotNull(asyncRunner, "asyncRunner");

        this.asyncRunner = asyncRunner;
        this.pausedTasks = new ArrayList<>();
        this.currentDateTime = currentDateTime;
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

        AsyncTask<Void> result;
        if (dateTime.lessThanOrEqualTo(getCurrentDateTime()))
        {
            result = asyncRunner.schedule(action);
        }
        else
        {
            result = asyncRunner.create(action);

            final PausedTask pausedTask = new PausedTask(result, dateTime);

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

    /**
     * Advance this clock forward by the provided duration.
     * @param duration The duration to advance this Clock forward by.
     */
    public void advance(Duration duration)
    {
        PreCondition.assertNotNull(duration, "duration");

        currentDateTime = currentDateTime.plus(duration);
        while (pausedTasks.any() && pausedTasks.first().getScheduledAt().lessThanOrEqualTo(currentDateTime))
        {
            pausedTasks.removeFirst().schedulePausedAction();
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

