package rp.warehouse.pc.data;

import rp.warehouse.pc.data.Item;

public class Task {

    public final Item item;
    public final int count;
    public final String jobID;

    /**
     * Construcotr
     * @param item
     * @param count
     * @param jobID
     */
    public Task(Item item, int count, String jobID) {
        this.item = item;
        this.count = count;
        this.jobID = jobID;
    }

    /**
     * returns the item in the task
     * @return
     */
    public Item getItem() {
        return item;
    }

    /**
     * returns the count of the item
     * @return
     */
    public int getCount() {
        return count;
    }

    /**
     * returns the jo of the job in the taskbID of the job the task is in
     * @return
     */
    public String getJobID() {
        return jobID;
    }

    @Override
    /**
     * Returns the task as a string
     */
    public String toString() {
        return "Job " + jobID + " | " + count + " of item " + item.toString();
    }
}