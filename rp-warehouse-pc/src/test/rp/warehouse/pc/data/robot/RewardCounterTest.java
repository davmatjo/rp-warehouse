package rp.warehouse.pc.data.robot;

import java.util.ArrayList;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import rp.warehouse.pc.data.Item;
import rp.warehouse.pc.data.Task;
import rp.warehouse.pc.data.robot.utils.RewardCounter;
import rp.warehouse.pc.input.Job;
import rp.warehouse.pc.input.Jobs;

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
        RewardCounter.setJobs(jobs);
        RewardCounter.addCompletedJob(task1);
        Assertions.assertTrue(RewardCounter.getPointsEarned() == 0.0f);
        RewardCounter.addCompletedJob(task2);
        Assertions.assertTrue(RewardCounter.getPointsEarned() == 14.0f);
    }

}
