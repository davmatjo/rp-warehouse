package rp.warehouse.pc.data;

/**
 * Task that contains a number of items
 * @author Megan
 */
public class Task {

    public final Item item;
    public final int count;
    public final String jobID;

    /**
     * Constructor
     * @param item item
     * @param count count
     * @param jobID jobID this task belongs to
     */
    public Task(Item item, int count, String jobID) {
        this.item = item;
        this.count = count;
        this.jobID = jobID;
    }

    /**
     * Creates a new task from a task
     * @param task task to copy
     */
    public Task(Task task) {
        this.item = new Item(task.item);
        this.count = task.count;
        this.jobID = task.jobID;
    }

    /**
     * @return the item in the task
     */
    public Item getItem() {
        return item;
    }

    /**
     * @return the count of the item
     */
    public int getCount() {
        return count;
    }

    /**
     * @return the jobID of the job the task is in
     */
    public String getJobID() {
        return jobID;
    }

    /**
     * @return the task as a string
     */
    @Override
    public String toString() {
        return "Job " + jobID + " | " + count + " of item " + item.toString();
    }
}