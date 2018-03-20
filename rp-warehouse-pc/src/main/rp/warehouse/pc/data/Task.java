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

    public Task(Task task) {
        this.item = new Item(task.item);
        this.count = task.count;
        this.jobID = task.jobID;
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

    @Override
    public String toString() {
        return "Job " + jobID + " | " + count + " of item " + item.toString();
    }
}