//has an arratList which holds all the jobs

package rp.warehouse.pc.input;

import java.util.ArrayList;

public class Jobs {

    private ArrayList<Job> jobs = new ArrayList<Job>();

    public ArrayList<Job> getJobs() {
        return jobs;
    }

    public void addJob(Job job) {
        jobs.add(job);
    }

    public Job getJob(String jobName) {
        Job job = null;
        //iterate over array list
        for (int i = 0; i < jobs.size(); i++) {
            if(jobs.get(i).equals(jobName)) {
                job = jobs.get(i);
            }
        }
        return job;
    }

}

