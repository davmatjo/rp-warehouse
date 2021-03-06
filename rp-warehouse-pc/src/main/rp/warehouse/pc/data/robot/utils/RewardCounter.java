package rp.warehouse.pc.data.robot.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import rp.warehouse.pc.data.Task;
import rp.warehouse.pc.input.Job;
import rp.warehouse.pc.input.Jobs;
import rp.warehouse.pc.management.providers.main.WarehouseInfoListener;

/**
 * Used to keep record of the points earned and Jobs
 * 
 * @author roman
 *
 */
public class RewardCounter {

    private static final Map<String, Job> jobReference = new HashMap<>();
    private static final Map<String, Integer> uncompletedJobReference = new HashMap<>();
    private static final Set<String> cancelledJobReference = new HashSet<>();
    private static final Set<String> completedJobReference = new HashSet<>();
    private static final List<WarehouseInfoListener> listeners = new ArrayList<>();

    private static final Logger logger = Logger.getLogger(RewardCounter.class);

    private static float pointsEarned = 0.0f;

    /**
     * Erases all the data fro this class
     */
    public static void resetJobs() {
        jobReference.clear();
        uncompletedJobReference.clear();
        cancelledJobReference.clear();
        completedJobReference.clear();
    }

    /**
     * Sets the initial set of Jobs for reference
     * 
     * @param jobs
     */
    public static void setJobs(Jobs jobs) {
        ArrayList<Job> jobList = jobs.getJobs();
        for (Job job : jobList) {
            jobReference.put(job.getName(), new Job(job));
        }
    }

    /**
     * @return - returns the number of Jobs Completed
     */
    public static int getJobsDone() {
        return completedJobReference.size();
    }

    /**
     * @return - returns the number of Jobs Cancelled
     */
    public static int getNumberJobsCancelled() {
        return cancelledJobReference.size();
    }

    /**
     * @return - returns the number of points earned for completed jobs
     */
    public static float getPointsEarned() {
        return pointsEarned;
    }

    /**
     * @return - returns the number of jobs which are currently undone
     */
    public static int getNumberUncompletedJobs() {
        return uncompletedJobReference.size();
    }

    /**
     * @param task
     *            - adds the job to be cancelled
     */
    public synchronized static void addCancelledJob(Task task) {
        logger.debug("Job " + task.getJobID() + " cancelled");
        cancelledJobReference.add(task.getJobID());
        uncompletedJobReference.remove(task.getJobID());
        for (WarehouseInfoListener listener : listeners) {
            listener.cancelledJobsChanged(getNumberJobsCancelled());
        }
    }

    /**
     * @param task
     *            - Task which is checked if it is cancelled
     * @return - True present, false not present 
     */
    public static boolean checkIfCancelled(Task task) {

        return cancelledJobReference.contains(task.getJobID());
    }

    private synchronized static void addReward(float reward) {
        pointsEarned += reward;
        for (WarehouseInfoListener listener : listeners) {
            listener.rewardChanged(getPointsEarned());
        }
    }

    /**
     * @param task
     *            - adds Task which was competed
     */
    public synchronized static void addCompletedJob(Task task) {
        String jobId = task.getJobID();
        if (!checkIfCancelled(task) && jobReference.containsKey(jobId)) {
            logger.debug("Job " + jobId + " is valid");
            
            if (uncompletedJobReference.containsKey(jobId)) {

                int numberOfTaskDone = uncompletedJobReference.get(jobId);
                logger.debug("Number of tasks done for this Job " + numberOfTaskDone);
                if (jobReference.get(jobId).numOfTasks() > uncompletedJobReference.get(jobId)) {
                    uncompletedJobReference.put(jobId, numberOfTaskDone + 1);
                }

            } else {
                uncompletedJobReference.put(jobId, 1);
            }

            if (jobReference.get(jobId).numOfTasks() == uncompletedJobReference.get(jobId)) {

                uncompletedJobReference.remove(jobId);

                completedJobReference.add(jobId);
                Job rewardToClaim = jobReference.get(jobId);
                ArrayList<Task> tasks = rewardToClaim.getItems();
                logger.debug("Job " + jobId + " completed");
                for (Task taskElement : tasks) {
                    float earnedRewards = taskElement.getItem().getReward() * taskElement.getCount();
                    addReward(earnedRewards);
                    logger.debug("Earned " + earnedRewards + " points for Item(s) " + taskElement.getItem());
                }
                logger.debug("Total points " + getPointsEarned());
            }

        } else {
            logger.debug("Completed task with Job ID: " + jobId + "  Is refused");
        }

        for (WarehouseInfoListener listener : listeners) {
            listener.jobCountChanged(getJobsDone());
            listener.cancelledJobsChanged(getNumberJobsCancelled());
            listener.uncompletedJobsChanged(getNumberUncompletedJobs());
        }

    }

    public static void addListener(WarehouseInfoListener listener) {
        listeners.add(listener);
    }

    @Override
    public String toString() {
        return "";
    }
}
