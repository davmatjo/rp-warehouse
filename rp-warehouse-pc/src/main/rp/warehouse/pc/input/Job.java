package rp.warehouse.pc.input;

import rp.warehouse.pc.data.Task;

import java.util.ArrayList;

public class Job {

    //has an item name and the count
    private String name;
    private ArrayList<Task> tasks;

    Job(String name, ArrayList<Task> tasks) {
        this.name = name;
        this.tasks = tasks;
    }

    //method to add an task to the list

    public String getName() {
        return name;
    }

    public ArrayList<Task> getItems() {
        return tasks;
    }

    public int numOfTasks()
        return tasks.size();
    }



}
