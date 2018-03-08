//has an arratList which holds all the jobs

package rp.warehouse.pc.data;

import java.util.ArrayList;

public class Jobs {

    private ArrayList<Job> jobs = new ArrayList<Job>();

    public ArrayList<Job> getJobs() {
        return jobs;
    }

    public void addJob(Job job) {
        jobs.add(job);
    }

}

