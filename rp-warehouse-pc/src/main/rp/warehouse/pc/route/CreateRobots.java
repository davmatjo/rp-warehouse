package rp.warehouse.pc.route;

import java.util.ArrayList;
import java.util.Queue;

import rp.warehouse.pc.data.Item;
import rp.warehouse.pc.data.Robot;
import rp.warehouse.pc.data.Task;
/**
 * 
 * @author roman
 *
 */
public class CreateRobots {
    ArrayList<Robot> robots = new ArrayList<Robot>();

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
    public CreateRobots(ArrayList<Queue<Task>> listOfItems) {

        int i = 0;
        for (Queue<Task> items : listOfItems) {
            robots.add(new Robot(i + "", createName(), items));
            i++;
        }
    }

    private String createName() {
        return "Bob";
    }

}
