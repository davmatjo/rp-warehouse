package rp.warehouse.pc.management.providers.main;

public interface WarehouseInfoListener {

    void rewardChanged(float newReward);

    void jobCountChanged(int newJobCount);

    void cancelledJobsChanged(int newCancelledCount);

    void uncompletedJobsChanged(int newUncompletedCount);
}
