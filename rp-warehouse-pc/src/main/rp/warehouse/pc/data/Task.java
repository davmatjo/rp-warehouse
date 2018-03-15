package rp.warehouse.pc.data;

import rp.warehouse.pc.data.Item;

public class Task {

    public final Item item;
    public final int count;
    public final String jobID;

    public Task(Item item, int count, String jobID) {
        this.item = item;
        this.count = count;
        this.jobID = jobID;
    }

    public Item getItem() {
        return item;
    }

    public int getCount() {
        return count;
    }

    public String getJobID() {
        return jobID;
    }
}