package rp.warehouse.pc.data;

import rp.warehouse.pc.communication.Communication;

import java.util.Queue;

public class Robot implements Runnable {

    // Communications
    private final String ID; // Communication ID
    private final String name; // Communication name
    private final Communication comms; // Communication used to connect each robot to the a nxt brick

    // Route information
    private Queue<Integer> route; // Queue of directions
    private int currentDirection;
    private Location location; // Current location of the robot

    // Job information
    private Queue<Task> tasks;
    private Item currentItem;
    private final static int weightLimit = 50;
    private int currentWeightOfCargo = 0;
    private boolean fail = false;

    // Plan

    /**
     * For: Job Assignment (Created here)
     * 
     * @param ID
     *            - Unique ID (could be a good idea to use Array location for now)
     * @param name
     *            - Robot name
     * @param newTasks
     *            - The queue of task Robot has to complete
     */
    public Robot(String ID, String name, Queue<Task> newTasks) {
        this.ID = ID;
        this.name = name;
        this.comms = new Communication(ID, name, this);
        tasks = newTasks;
    }

    public void run() {
        String answer = null;
        while (true) {

            // answer= robot.getResponse(); // blocking
            // If nothing left in the currentRoute
            // Need some kind of response
            if (true) {
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

        }
    }

    public String getID() {
        return ID;
    }

    public Location getLocation() {
        return location;
    }

    /**
     * For: Communication
     * 
     * When confirmation of completion of the last instruction has been received,
     * Communication should call this method to update Robot location and get next
     * Direction
     */
    public void getNextDirection() {

        updateLocation();

        updateCurrentItem();

        comms.sendMovement(getNextInstruction());
    }

    private int getNextInstruction() {
        // What it there are no more instruction
        // Check Job ID (Discard cancels jobs "items" )
        return route.poll();
    }

    /**
     * For: Route Planning
     * 
     * @param newRoute
     *            - the new queue of directions to get one item
     */
    public void setRoute(Queue<Integer> newRoute) {
        route = newRoute;
    }

    /**
     * For: Job Assignment
     * 
     * @param newTasks
     *            - the new queue of Jobs for this robot to complete
     */
    public void setJobs(Queue<Task> newTasks) {
        tasks = newTasks;
    }

    /**
     * When the confirmation of move has been received, the location robot is
     * updated
     */
    private void updateLocation() {
        int x, y;
        x = location.getX();
        y = location.getY();
        switch (currentDirection) {
        case 3:
            y += 1;
            break;
        case 4:
            x += 1;
            break;
        case 5:
            y -= 1;
            break;
        case 6:
            x -= 1;
            break;

        default:
            break;
        }
        location = new Location(x, y, currentDirection);
        Location itemLocation;// =currentItem.getLocation();
        if (location.getX() == 0) { // ==itemLocation.getX())&&location.getY()==itemLocation.getY()){
            // update task and get a new item
        }
    }

    /**
     * Used to update the current item
     */
    private void updateCurrentItem() {

        if (route.peek() == null) {
            // No more directions
        }
        currentItem = tasks.poll().item;
    }

    /**
     * For: Communication Cancels Job of the current item
     */
    public void cancelJob() {
        // currentItem.getJob()
        // cancelltems.add(getCurrentItem())

        // Clear all current instructions
        // and start a new one
    }
}
