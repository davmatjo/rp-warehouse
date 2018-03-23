//has an arratList which holds all the jobs

package rp.warehouse.pc.input;

import java.util.ArrayList;

/**
 * @author Megan
 */
public class Jobs {

    private ArrayList<Job> jobs = new ArrayList<>();

    /**
     * returns the arrayList of jobs
     * @return
     */
    public ArrayList<Job> getJobs() {
        return jobs;
    }

    /**
     * adds a job to the jobs arraylist
     * @param job
     */
    public void addJob(Job job) {
        jobs.add(job);
    }

    /**
     * returns the job item from the arraylist of jobs
     * @param jobName
     * @return
     */
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

