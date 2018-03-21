package rp.warehouse.pc;

import org.apache.log4j.Logger;
import rp.warehouse.pc.assignment.Auctioner;
import rp.warehouse.pc.data.robot.utils.RobotLocation;
import rp.warehouse.pc.input.Job;
import rp.warehouse.pc.input.JobInput;
import rp.warehouse.pc.input.Jobs;
import rp.warehouse.pc.management.LoadingView;
import rp.warehouse.pc.selection.JobSelector;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    private static final Logger logger = Logger.getLogger(Main.class);

    public static void main(String[] args) throws FileNotFoundException {
        Runtime.getRuntime().addShutdownHook(new Thread( () -> {
            logger.info("|-----------------------------[Application Closed]----------------------------------|");
        }));

        new LoadingView();
        Jobs jobs = new JobInput("./items.csv", "./jobs.csv", "./locations.csv", "").getJobs();

        ArrayList<Job> jobsList = jobs.getJobs();

        JobSelector jobSelector = new JobSelector(jobsList, 0, false, 0f);
        List<Job> jobs1 = jobSelector.sortPredicted("./prediction.csv", jobsList);
        jobs1.forEach((a)  -> System.out.println(a.getItems()));

//        new Auctioner(jobs.getJobs(), new ArrayList<>(Arrays.asList(new RobotLocation(5, 5, 1), new RobotLocation(5, 5, 1), new RobotLocation(5, 5, 1)))).assign();
        logger.debug("Main thread ending");
    }
}
