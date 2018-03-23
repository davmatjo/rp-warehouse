package rp.warehouse.pc.management.providers.main;

/**
 * Listener for information about the general state of the warehouse
 */
public interface WarehouseInfoListener {

    /**
     * Called when the total reward of the warehouse has changed
     * @param newReward new reward
     */
    void rewardChanged(float newReward);

    /**
     * Called when the number of jobs has changed
     * @param newJobCount new job count
     */
    void jobCountChanged(int newJobCount);

    /**
     * Called when the number of cancelled jobs has changed
     * @param newCancelledCount new number of cancelled jobs
     */
    void cancelledJobsChanged(int newCancelledCount);

    /**
     * Called when the number of uncompleted jobs has changed
     * @param newUncompletedCount new number of uncompleted jobs
     */
    void uncompletedJobsChanged(int newUncompletedCount);
}
