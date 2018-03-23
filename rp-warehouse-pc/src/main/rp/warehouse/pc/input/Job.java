package rp.warehouse.pc.input;

import rp.warehouse.pc.data.Task;

import java.util.ArrayList;

public class Job {

    //has an item name and the count
    private String name;
    private boolean cancelled = false;
    private ArrayList<Task> tasks;

    /**
     * Constructor
     * @param name
     * @param tasks
     */
    public Job(String name, ArrayList<Task> tasks) {

        this.name = name;
        this.tasks = tasks;

    }

    /**
     * Constructor
     * @param name
     * @param tasks
     * @param cancelled
     */
    public Job(String name, ArrayList<Task> tasks, boolean cancelled) {

        this.name = name;
        this.tasks = tasks;
        this.cancelled = cancelled;

    }


    /**
     * returns the name of the job
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * returns the Items in the job
     * @return
     */
    public ArrayList<Task> getItems() {
        return tasks;
    }

    /**
     * returns the amount of tasks in a job
     * @return
     */
    public int numOfTasks() {
        return tasks.size();
    }

    /**
     * adds a task to a job
     * @param task
     */
    public void addTask(Task task) {
        tasks.add(task);
    }

    /**
     * sets whether a job was cancelled or not
     * @param cancelled
     */
    public void setCancelled (int cancelled) {
        if (cancelled == 0) {
            this.cancelled = false;
        } else
            this.cancelled = true;

    }


}
