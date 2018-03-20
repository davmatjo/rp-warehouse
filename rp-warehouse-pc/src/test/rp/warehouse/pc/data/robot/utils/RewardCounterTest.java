package rp.warehouse.pc.data.robot.utils;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Before;
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
    private Jobs jobs;
    private Task task1;
    private Task task2;

    @Before
    public void setUp() {
        jobs = new Jobs();
        ArrayList<Task> tasks = new ArrayList<>();
        Item item = new Item("top", 6.0f, 3.0f);
        Item item1 = new Item("Legs", 2.0f, 1.0f);
        task1 = new Task(item, 1, "table");
        task2 = new Task(item1, 4, "table");
        tasks.add(task1);
        tasks.add(task2);
        Job job = new Job("table", tasks);
        jobs.addJob(job);

    }

    @Test
    public void rewardTestInitiallyZero() {
        Assertions.assertEquals(0, RewardCounter.getPointsEarned());
        Assertions.assertEquals(0, RewardCounter.getNumberUncompletedJobs());
        Assertions.assertEquals(0, RewardCounter.getJobsDone());
        Assertions.assertEquals(0, RewardCounter.getJobsDone());
        Assertions.assertEquals(0, RewardCounter.getNumberJobsCancelled());

    }

    @Test
    public void rewardAddingPoints() {

        RewardCounter.resetJobs();
        RewardCounter.setJobs(jobs);

        Assertions.assertEquals(0, RewardCounter.getNumberUncompletedJobs());
        Assertions.assertEquals(0, RewardCounter.getJobsDone());

        RewardCounter.addCompletedJob(task1);

        Assertions.assertEquals(0.0f, RewardCounter.getPointsEarned());
        Assertions.assertEquals(1.0f, RewardCounter.getNumberUncompletedJobs());

        RewardCounter.addCompletedJob(task2);

        Assertions.assertEquals(14.0f, RewardCounter.getPointsEarned());
        Assertions.assertEquals(0, RewardCounter.getNumberUncompletedJobs());
        Assertions.assertEquals(1, RewardCounter.getJobsDone());

    }

    @Test
    public void cancelledAdd() {
        RewardCounter.resetJobs();
        RewardCounter.setJobs(jobs);

        Assertions.assertEquals(0, RewardCounter.getNumberUncompletedJobs());
        Assertions.assertEquals(0, RewardCounter.getJobsDone());

        RewardCounter.addCompletedJob(task1);

        Assertions.assertEquals(0.0f, RewardCounter.getPointsEarned());
        Assertions.assertEquals(1, RewardCounter.getNumberUncompletedJobs());

        RewardCounter.addCancelledJob(task1);

        RewardCounter.addCompletedJob(task2);

        Assertions.assertEquals(0, RewardCounter.getPointsEarned());
        Assertions.assertEquals(1, RewardCounter.getNumberUncompletedJobs());
        Assertions.assertEquals(0, RewardCounter.getJobsDone());
        Assertions.assertEquals(1, RewardCounter.getNumberJobsCancelled());

    }

}
