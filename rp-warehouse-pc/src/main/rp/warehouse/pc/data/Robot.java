package rp.warehouse.pc.data;

import rp.warehouse.pc.communication.Communication;
import rp.warehouse.pc.route.RobotController;

import java.util.Queue;

public class Robot implements Runnable{

    // Communications
    private final String ID; // Communication ID
    private final String name; // Communication name
    private final Communication comms; // Communication used to connect each robot to the a nxt brick

    // Route information
    private Queue<Integer> route; // Queue of directions
                // This should Queue<Queue<Integer>> route
                // Because when job is cancelled it the whole route needs to be deleted
    private Location location; // Current location of the robot
    private int currentItem; 
    private final static int weightLimit = 50;
    private int currentWeightOfCargo = 0;
    private boolean fail = false;

    public Robot(String ID, String name) {
        this.ID = ID;
        this.name = name;
        this.comms = new Communication(ID, name, this);
    }

    public void run() {
        String answer = null;
        while (true) {

            // If nothing left in the currentRoute
            if (getCurrentItem() == -1) {
                switch (answer) {
                case "WAITING":

                    break;
                case "FAIL":
                    System.exit(1);
                    break;
                case "OK":

                    break;

                default:
                    break;
                }
            }
            move();
            // answer= robot.getResponse(); // blocking
        }
    }

    public String getID() {
        return ID;
    }

    public Location getLocation() {
        return location;
    }

    /**
     * When the confirmation of move has been received, the location robot is update
     */
    public void updateLocation() {
        // NEEDS TO BE DONE
    }

    public int getCurrentItem() {
        return currentItem;
    }

    public boolean move() {
        Integer nextMovement = route.poll();
        comms.sendMovement(nextMovement);
        return true;
    }

    public Queue<Integer> getRoute() {
        return route;
    }

    /**
     * When called cancels Job of the current item
     */
    public void cancelJob() {
        // currentItem.getJob()
        // cancelltems.add(getCurrentItem())
    }
}
