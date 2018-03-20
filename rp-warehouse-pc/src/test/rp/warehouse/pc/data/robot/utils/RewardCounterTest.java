package rp.warehouse.pc.data.robot.utils;


import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import rp.warehouse.pc.data.Item;
import rp.warehouse.pc.data.Task;
import rp.warehouse.pc.data.robot.utils.RewardCounter;
import rp.warehouse.pc.input.Job;
import rp.warehouse.pc.input.Jobs;
/**
 * 
 * @author roman
 *
 */
public class RewardCounterTest {
    
    @Test
    public void rewardTestInitiallyZero() {
        Assertions.assertTrue(RewardCounter.getPointsEarned() == 0);
        
    }
    
    @Test
    public void rewardAddingPoints() {
        Jobs jobs = new Jobs();
        ArrayList<Task> tasks = new ArrayList<>();
        Item item = new Item("top", 6.0f, 3.0f);
        Item item1 = new Item("Legs", 2.0f, 1.0f);
        Task task1 = new Task(item, 1, "table");
        Task task2 = new Task(item1, 4, "table");
        tasks.add(task1);
        tasks.add(task2);
        Job job = new Job("table", tasks);
        jobs.addJob(job);
        RewardCounter.resetJobs();
        RewardCounter.setJobs(jobs);
        
        Assertions.assertEquals(0 , RewardCounter.getNumberUncompletedJobs());
        Assertions.assertEquals(0 , RewardCounter.getJobsDone());
        
        RewardCounter.addCompletedJob(task1);
       
        
        Assertions.assertEquals(0.0f , RewardCounter.getPointsEarned());
        Assertions.assertEquals(1.0f , RewardCounter.getNumberUncompletedJobs());
        
        RewardCounter.addCompletedJob(task2);
        
        Assertions.assertEquals( 14.0f  ,RewardCounter.getPointsEarned());
        Assertions.assertEquals(0, RewardCounter.getNumberUncompletedJobs());
        Assertions.assertEquals(1 ,RewardCounter.getJobsDone());
        
    }
    
    @Test
    public void cancelledAdd() {
        Jobs jobs = new Jobs();
        ArrayList<Task> tasks = new ArrayList<>();
        Item item = new Item("top", 6.0f, 3.0f);
        Item item1 = new Item("Legs", 2.0f, 1.0f);
        Task task1 = new Task(item, 1, "table");
        Task task2 = new Task(item1, 4, "table");
        tasks.add(task1);
        tasks.add(task2);
        Job job = new Job("table", tasks);
        jobs.addJob(job);
        RewardCounter.resetJobs();
        RewardCounter.setJobs(jobs);
        
        Assertions.assertTrue(RewardCounter.getNumberUncompletedJobs() == 0);
        Assertions.assertTrue(RewardCounter.getJobsDone() == 0);
        
        RewardCounter.addCompletedJob(task1);
       
        
        Assertions.assertTrue(RewardCounter.getPointsEarned() == 0.0f);
        Assertions.assertTrue(RewardCounter.getNumberUncompletedJobs() == 1);
        
        RewardCounter.addCancelledJob(task1);
        
        RewardCounter.addCompletedJob(task2);
        
        Assertions.assertTrue(RewardCounter.getPointsEarned() == 0);
        Assertions.assertTrue(RewardCounter.getNumberUncompletedJobs() == 1);
        Assertions.assertTrue(RewardCounter.getJobsDone() == 0);
        Assertions.assertTrue(RewardCounter.getNumberJobsCancelled() == 1);
        
    }

}
