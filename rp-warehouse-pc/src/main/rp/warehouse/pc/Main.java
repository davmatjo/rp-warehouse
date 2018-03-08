package rp.warehouse.pc;

import org.apache.log4j.Logger;
import rp.warehouse.pc.communication.Protocol;
import rp.warehouse.pc.data.Item;
import rp.warehouse.pc.data.Job;
import rp.warehouse.pc.data.Location;
import rp.warehouse.pc.data.Task;
import rp.warehouse.pc.route.RobotsControl;

import java.util.*;

public class Main {
    private static final Logger logger = Logger.getLogger(Main.class);

    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread( () -> {
            logger.info("|-----------------------------[Application Closed]----------------------------------|");
        }));
        Queue<Task> myQ = new LinkedList<>();
        Queue<Task> myQ2 = new LinkedList<>();
        Queue<Task> myQ3 = new LinkedList<>();
        Item myItem = new Item(1f, 1f);
        Item myItem2 = new Item(1f, 1f);
        Item myItem3 = new Item(1f, 1f);
        myItem.setLocation(new Location(5, 5));
        myItem2.setLocation(new Location(5, 5));
        myItem3.setLocation(new Location(5, 5));
        myQ.offer(new Task(myItem, 1));
        myQ2.offer(new Task(myItem2, 1));
        myQ3.offer(new Task(myItem3, 1));

        ArrayList<Queue<Task>> myL = new ArrayList<>();
        myL.add(myQ);
        myL.add(myQ2);
        myL.add(myQ3);

        RobotsControl.addRobots(myL);
        logger.debug("Main thread ending");
    }
}
