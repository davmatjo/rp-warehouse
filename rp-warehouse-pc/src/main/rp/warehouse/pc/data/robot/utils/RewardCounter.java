package rp.warehouse.pc.data.robot.utils;
/**
 * Used to keep record of the points earned
 * @author roman
 *
 */
public class RewardCounter {

    private static float pointsEarned = 0.0f;
    private static int numberOfJobsDone = 0;
    private static int numberOfJobsCancelled = 0;
    
    public static int getJobsDone() {
        return numberOfJobsDone;
    }
    public static int getnumberJobsCancelled() {
        return numberOfJobsCancelled;
    }
    public static float getPointsEarned() {
        return pointsEarned;
    }
    public static void addJobCancelled(String id) {
        
        numberOfJobsDone ++;
    }
    public static void addReward(float reward) {
        pointsEarned += reward;
    }
    
    @Override
    public String toString() {
        return "";
    }
}
