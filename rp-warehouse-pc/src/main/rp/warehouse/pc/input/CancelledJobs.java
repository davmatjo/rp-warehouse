//arraylist to hold all the cancelled jobs

package rp.warehouse.pc.input;

import rp.warehouse.pc.input.Job;

import java.util.ArrayList;

public class CancelledJobs {

    ArrayList<Job> cancelledJobs = new ArrayList<Job>();

    public ArrayList<Job> getCancelledJobs() {
        return cancelledJobs;
    }

    public void addJob(Job job) {

        cancelledJobs.add(job);

    }
}
