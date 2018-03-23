package rp.warehouse.pc.data.input;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import rp.warehouse.pc.input.Items;
import rp.warehouse.pc.input.JobInput;
import rp.warehouse.pc.input.Jobs;

import java.io.FileNotFoundException;

public class JobInputTest {

    String itemsLocation = "./test/rp/warehouse/pc/data/input/itemsTest.csv";
    String locationsLocation = "./test/rp/warehouse/pc/data/input/locationsTest.csv";
    String jobsLocation = "./test/rp/warehouse/pc/data/input/jobsTest.csv";
    String cancellationsLocation = "./test/rp/warehouse/pc/data/input/cancellationsTest.csv";

    @Test
    public void shouldBeThreeJobs() throws FileNotFoundException {
        JobInput jobInput = new JobInput(itemsLocation, locationsLocation, jobsLocation, cancellationsLocation);
        Jobs jobs = jobInput.getJobs();
        Assertions.assertEquals(3, jobs.getJobs().size());

    }

    @Test
    public void firstJobCalled10000() throws FileNotFoundException {
        JobInput jobInput = new JobInput(itemsLocation, locationsLocation, jobsLocation, cancellationsLocation);
        Jobs jobs = jobInput.getJobs();
        String firstJobName = jobs.getJobs().get(0).getName();
        Assertions.assertEquals("10000", firstJobName);

    }

    @Test
    public void firstJobHasTwoTasks() throws FileNotFoundException {
        JobInput jobInput = new JobInput(itemsLocation, locationsLocation, jobsLocation, cancellationsLocation);
        Jobs jobs = jobInput.getJobs();
        int numOfTasks = jobs.getJobs().get(0).numOfTasks();
        Assertions.assertEquals(2, numOfTasks);

    }

    @Test
    public void firstJobTaskItemName() throws FileNotFoundException {
        JobInput jobInput = new JobInput(itemsLocation, locationsLocation, jobsLocation, cancellationsLocation);
        Jobs jobs = jobInput.getJobs();
        String firstJobName = jobs.getJobs().get(0).getName();
        Assertions.assertEquals("10000", firstJobName);

    }








}
