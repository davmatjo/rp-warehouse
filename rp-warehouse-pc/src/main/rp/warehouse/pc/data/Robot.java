package rp.warehouse.pc.data;

import rp.warehouse.pc.communication.Communication;
import rp.warehouse.pc.communication.Protocol;
import rp.warehouse.pc.route.RobotsControl;

import java.util.Queue;

public class Robot implements Runnable {

    // Communications
    private final String ID;            // Communication ID
    private final String name;          // Communication name
    private final Communication comms;  // Communication used to connect each robot to the a nxt brick

    // Route information
    private Queue<Integer> route;       // Queue of directions
    private int currentDirection;
    private Location location;          // Current location of the robot

    // Job information
    private Queue<Task> tasks;
    private Item currentItem;
    
    // Robot Information
    private final static int weightLimit = 50;
    private int currentWeightOfCargo = 0;
    private boolean fail = false;

    // Plan
    // Instance of Planning, which could be called 
    // up to plan when, job is cancelled

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

    
    /**
     * Runs indefinitely and sends commands to Communication 
     * 
     */
    public void run() {
        
        while (true) {
            
            // How do i show that its a drop off or pick up?

            
            // Checks if there are anymore items
            if (currentItem==null) {
                // Send STOP  
                comms.sendMovement(getNextInstruction());// Is this blocking?
            }
            
            // Checks if location of the robot matches
            // with the location of the item
            else if(route.peek()==null) {
                // Send STOP and WAIT for PICKUP or DROP OFF
                comms.sendMovement(getNextInstruction());
            }
            
            // If there are still instructions present
            else{
                comms.sendMovement(getNextInstruction());// Is this blocking?
                updateLocation();
                updateCurrentItem();
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
    @SuppressWarnings("unused")    // Using those commands in the run methods now
    private void getNextDirection() {


    }

    private int getNextInstruction() {
        // What it there are no more instruction
        // Check Job ID (Discard cancelled jobs "items" )
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
        case Protocol.NORTH:
            y += 1;
            break;
        case Protocol.EAST:
            x += 1;
            break;
        case Protocol.SOUTH:
            y -= 1;
            break;
        case Protocol.WEST:
            x -= 1;
            break;

        default:
            break;
        }
        location.setX(x);
        location.setY(y);
        location.setDirection(currentDirection);
        Location itemLocation;// =currentItem.getLocation();
        
        // Probably no need for this
        // can just check if there are no more instructions for this item
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
    
    /**
     * For: Communication specify amount loaded for the current item
     * 
     * Only one item can be loaded at the time
     * Every time user presses button on pickup one item is loaded
     * 
     * @return - True, there is still space for more cargo. False, there is no more space left
     */
    public boolean pickUp() {
        if (currentWeightOfCargo+1 > weightLimit) {
            return false;
        }
        currentWeightOfCargo++;
        return true;
    }
    /**
     * For Communication when performing drop of at the station
     * 
     * The whole cargo is dropped off at once
     * When user presses button to unload the robot,the whole cargo is dropped off at once
     * 
     * @return - True, Cargo was dropped off. False, empty
     */
    public boolean dropOff() {
        if (currentWeightOfCargo==0) {
            return false;
        }
        currentWeightOfCargo=0;
        return true;
    }
    
}
