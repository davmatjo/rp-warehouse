package rp.warehouse.pc.input;

import rp.warehouse.pc.data.Task;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Job that contains a number of tasks
 * @author Megan
 */
public class Job {

    //has an item name and the count
    private String name;
    private boolean cancelled = false;
    private ArrayList<Task> tasks;

    /**
     * Constructor
     * @param name job ID
     * @param tasks tasks this job contains
     */
    public Job(String name, ArrayList<Task> tasks) {

        this.name = name;
        this.tasks = tasks;

    }

    /**
     * Constructor
     * @param name job ID
     * @param tasks tasks this job contains
     * @param cancelled whether this job is cancelled
     */
    public Job(String name, ArrayList<Task> tasks, boolean cancelled) {

        this.name = name;
        this.tasks = tasks;
        this.cancelled = cancelled;

    }

    /**
     * Creates a job from another job
     * @param job job to copy
     */
    public Job(Job job) {
        this.name = job.name;
        this.cancelled = job.cancelled;
        this.tasks = job.tasks.stream().map(Task::new).collect(Collectors.toCollection(ArrayList::new));
    }


    /**
     * @return the name of the job
     */
    public String getName() {
        return name;
    }

    /**
     * @return the Items in the job
     */
    public ArrayList<Task> getItems() {
        return tasks;
    }

    /**
     * @return the amount of tasks in a job
     */
    public int numOfTasks() {
        return tasks.size();
    }

    /**
     * adds a task to a job
     * @param task task to add
     */
    public void addTask(Task task) {
        tasks.add(task);
    }

    /**
     * sets whether a job was cancelled or not
     * @param cancelled whether this job was cancelled
     */
    public void setCancelled(int cancelled) {
        this.cancelled = cancelled != 0;

    }


}
