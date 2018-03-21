//has an arratList which holds all the jobs

package rp.warehouse.pc.input;

import java.util.ArrayList;

public class Jobs {

    private ArrayList<Job> jobs = new ArrayList<>();

    public ArrayList<Job> getJobs() {
        return jobs;
    }

    public void addJob(Job job) {
        jobs.add(job);
    }

    public Job getJob(String jobName) {
        Job job = null;
        //iterate over array list
        for (Job job1 : jobs) {
            if (job1.equals(jobName)) {
                job = job1;
            }
        }
        return job;
    }

}

