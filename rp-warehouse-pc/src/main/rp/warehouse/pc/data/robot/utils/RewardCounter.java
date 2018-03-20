package rp.warehouse.pc.data.robot.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import rp.warehouse.pc.data.Task;
import rp.warehouse.pc.data.robot.Robot;
import rp.warehouse.pc.input.Job;
import rp.warehouse.pc.input.Jobs;
import rp.warehouse.pc.management.providers.main.WarehouseInfoListener;

/**
 * Used to keep record of the points earned
 * @author roman
 *
 */
public class RewardCounter {
    
    private static final Map<String, Job> jobReference = new HashMap<>();
    private static final Map<String, Integer> uncompletedJobReference = new HashMap<>();
    private static final Map<String, Boolean> cancelledJobReference = new HashMap<>();
    private static final Map<String, Boolean> completedJobReference = new HashMap<>();
    private static final List<WarehouseInfoListener> listeners = new ArrayList<>();
    
    private static final Logger logger = Logger.getLogger(RewardCounter.class);

    private static float pointsEarned = 0.0f;
    
    public static void setJobs(Jobs jobs) {
        ArrayList<Job> jobList =jobs.getJobs();
        for (Job job : jobList) {
           jobReference.put(job.getName(), job);
        }
    }
    
    private static int getJobsDone() {
        return completedJobReference.size();
    }
    
    private static int getNumberJobsCancelled() {
        return cancelledJobReference.size();
    }
    
    public static float getPointsEarned() {
        return pointsEarned;
    }
    
    public static float getNumberUncompletedJobs() {
        return uncompletedJobReference.size();
    }
    
    public synchronized static void addCancelledJob(Task task) {
        cancelledJobReference.put(task.getJobID(), true);
        for (WarehouseInfoListener listener : listeners) {
            listener.cancelledJobsChanged(getNumberJobsCancelled());
        }
    }
    
    public static boolean checkIfCancelled(Task task) {
        
        return cancelledJobReference.containsKey(task.getJobID());
    }

    private synchronized static void addReward(float reward) {
        pointsEarned += reward;
        for (WarehouseInfoListener listener : listeners) {
            listener.rewardChanged(getPointsEarned());
        }
    }

    public synchronized static void addCompletedJob(Task task) {
        String jobId= task.getJobID();
        if (!checkIfCancelled(task) && jobReference.containsKey(jobId)) {
            if(uncompletedJobReference.containsKey(jobId)) {
                
                int numberOfTaskDone = uncompletedJobReference.get(jobId);
                if(jobReference.get(jobId).numOfTasks() > uncompletedJobReference.get(jobId)) {
                    uncompletedJobReference.put(jobId, numberOfTaskDone+1);
                } 
                
            }else {
                uncompletedJobReference.put(jobId, 1);
            }
            
            if (jobReference.get(jobId).numOfTasks() == uncompletedJobReference.get(jobId)) {
                
                uncompletedJobReference.remove(jobId);
                
                completedJobReference.put(jobId, true);
                Job rewardToClaim = jobReference.get(jobId);
                ArrayList<Task> tasks = rewardToClaim.getItems();
                logger.debug("Job " + jobId + " completed");
                for (Task taskElement: tasks) {
                    float earnedRewards = taskElement.getItem().getReward() * taskElement.getCount();
                    addReward(earnedRewards);
                    logger.debug("Earned " + earnedRewards + " points for Item(s) " + taskElement.getItem());
                }
            }
            
        } else {
            logger.debug("Completed task with Job ID: " + jobId + "  Is refused");
        }

        for (WarehouseInfoListener listener : listeners) {
            listener.jobCountChanged(getJobsDone());
            listener.cancelledJobsChanged(getNumberJobsCancelled());
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
