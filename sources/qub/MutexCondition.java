package qub;

public interface MutexCondition
{
    /**
     * Release this condition's mutex and wait to be signaled that this condition is satisfied.
     */
    Result<Boolean> await();

    /**
     * Signal any awaiting threads that this condition is now satisfied.
     */
    void signalAll();
}
