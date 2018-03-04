package rp.warehouse.pc.route;

import java.util.ArrayList;
import java.util.Queue;

import rp.warehouse.pc.data.Item;
import rp.warehouse.pc.data.Location;
import rp.warehouse.pc.data.Robot;
import rp.warehouse.pc.data.Task;

/**
 * Used to link different part of the system together
 * 
 * Job Assignment   - Can use this class to initially create Array of Robots
 * 
 * Route planning   - Can use to access robot classes to plan
 * 
 * Warehouse MI     - Can use this class to get Robot classes to get all the
 * required data
 * 
 * @author roman
 *
 */
public class RobotsControl {
    private static ArrayList<Robot> robots = new ArrayList<Robot>();

    /**
     * For: Job Selection When the the items have been split between robots (number
     * of robots should be passed to you by Job Selection) pass Queue of items for
     * each of the robot.
     * 
     * If there are 3 robots, there should be 3 queues in the ArrayList and the size
     * of the Array should be 3
     * 
     * If there is only 1, then there should be just one queue in the ArrayList and
     * the size of the array should be 1
     * 
     */
    public static void addRobots(ArrayList<Queue<Task>> listOfItems) {

        int i = 0;
        for (Queue<Task> items : listOfItems) {
            robots.add(new Robot(i + "", createName(), items));

            // Not sure if that's the right way to do it
            Thread t = new Thread(robots.get(i));
            t.start();

            i++;
        }
    }

    private static String createName() {
        return "Bob";
    }

    /**
     * For: Route Planning and Warehouse to get robot classes in the for of array
     * 
     * 
     * @return ArrayList of Robots
     */
    public static ArrayList<Robot> getRobots() {

        return robots;
    }

}
