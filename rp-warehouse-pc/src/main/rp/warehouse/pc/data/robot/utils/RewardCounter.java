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
    private static final Map<String, Integer> uncompletedJobReference = new HashMap<>();
    private static final Map<String, Boolean> cancelledJobReference = new HashMap<>();
    private static final Map<String, Boolean> completedJobReference = new HashMap<>();

    private static float pointsEarned = 0.0f;
    private static int numberOfJobsDone = 0;
    private static int numberOfJobsCancelled = 0;
    
    public void setJobs(ArrayList<Job> jobList) {
        for (Job job : jobList) {
           jobReference.put(job.getName(), job);
        }
    }
    
    public static int getJobsDone() {
        return completedJobReference.size();
    }
    public static int getnumberJobsCancelled() {
        return cancelledJobReference.size();
    }
    public static float getPointsEarned() {
        return pointsEarned;
    }
    public synchronized static void addCancelledJob(Task task) {
        cancelledJobReference.put(task.getJobID(), true);
    }
    
    public static boolean checkIfCancelled(Task task) {
        
        return cancelledJobReference.containsKey(task.getJobID());
    }
    public synchronized static void addReward(float reward) {
        pointsEarned += reward;
    }
    public synchronized static void addCompletedJob(Task task) {
        String jobId= task.getJobID();
        if (!checkIfCancelled(task) && jobReference.containsKey(jobId)) {
            if(uncompletedJobReference.containsKey(jobId)) {
//                if(jobReference.get(jobId)) {
//                    
//                }
            }else {
                uncompletedJobReference.put(jobId, 1);
            }
            
        }else {
            return;
        }
        
        
    }
    
    @Override
    public String toString() {
        return "";
    }
}
