package rp.warehouse.pc.data.robot.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import rp.warehouse.pc.data.Task;
import rp.warehouse.pc.input.Job;

/**
 * Used to keep record of the points earned
 * @author roman
 *
 */
public class RewardCounter {
    
    private static final Map<String, Job> jobReference = new HashMap<>();
    private static final Map<String, Job> completedJobReference = new HashMap<>();
    private static final Map<String, Boolean> cancelledJobReference = new HashMap<>();

    private static float pointsEarned = 0.0f;
    private static int numberOfJobsDone = 0;
    private static int numberOfJobsCancelled = 0;
    
    public void setJobs(ArrayList<Job> jobList) {
        for (Job job : jobList) {
           jobReference.put(job.getName(), job);
        }
    }
    
    public static int getJobsDone() {
        return numberOfJobsDone;
    }
    public static int getnumberJobsCancelled() {
        return numberOfJobsCancelled;
    }
    public static float getPointsEarned() {
        return pointsEarned;
    }
    public static void addCancelledJob(Task task) {
        cancelledJobReference.put(task.getJobID(), true);
    }
    
    public static boolean checkIfCancelled(Task task) {
        
        return cancelledJobReference.containsKey(task.getJobID());
    }
    public static void addReward(float reward) {
        pointsEarned += reward;
    }
    public static void addCompletedJob(Task task) {
        
        
    }
    
    @Override
    public String toString() {
        return "";
    }
}
