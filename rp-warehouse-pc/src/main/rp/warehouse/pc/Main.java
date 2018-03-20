package rp.warehouse.pc;

import org.apache.log4j.Logger;
import rp.warehouse.pc.assignment.Auctioner;
import rp.warehouse.pc.data.robot.utils.RobotLocation;
import rp.warehouse.pc.input.Job;
import rp.warehouse.pc.input.JobInput;
import rp.warehouse.pc.input.Jobs;
import rp.warehouse.pc.management.LoadingView;

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

        List<Job> jobsList = jobs.getJobs();
//        System.out.println(jobsList.stream().limit(20).mapToDouble((a) ->
//                a.getItems().stream().mapToDouble((b) -> (double) b.count * ((double) b.item.getReward())).sum()).sum());


        new Auctioner(jobs.getJobs(), new ArrayList<>(Arrays.asList(new RobotLocation(5, 5, 1)))).assign();
        logger.debug("Main thread ending");
    }
}
