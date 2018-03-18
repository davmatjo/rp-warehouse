package rp.warehouse.pc;

import org.apache.log4j.Logger;
import rp.warehouse.pc.communication.Protocol;
import rp.warehouse.pc.data.*;
import rp.warehouse.pc.management.LoadingFrame;
import rp.warehouse.pc.route.RobotsControl;

import java.io.FileNotFoundException;
import java.util.*;

public class Main {
    private static final Logger logger = Logger.getLogger(Main.class);

    public static void main(String[] args) throws FileNotFoundException {
        Runtime.getRuntime().addShutdownHook(new Thread( () -> {
            logger.info("|-----------------------------[Application Closed]----------------------------------|");
        }));
//        Queue<Task> myQ = new LinkedList<>();
//        Queue<Task> myQ2 = new LinkedList<>();
//        Queue<Task> myQ3 = new LinkedList<>();
//        Item myItem = new Item(1f, 1f);
//        Item myItem2 = new Item(1f, 1f);
//        Item myItem3 = new Item(1f, 1f);
//        myItem.setLocation(new Location(5, 5));
//        myItem2.setLocation(new Location(1, 1));
//        myItem3.setLocation(new Location(3, 2));
//        myQ.offer(new Task(myItem, 1, ""));
//        myQ.offer(new Task(myItem2, 1, ""));
//        myQ.offer(new Task(myItem3, 1, ""));
//        myQ2.offer(new Task(myItem2, 1, ""));
//        myQ3.offer(new Task(myItem3, 1, ""));
//
//        ArrayList<Queue<Task>> myL = new ArrayList<>();
//        myL.add(myQ);
//        myL.add(myQ2);
//        myL.add(myQ3);
//
//        RobotsControl.addRobots(myL);
        new LoadingFrame();
        new JobInput();
        logger.debug("Main thread ending");
    }
}
