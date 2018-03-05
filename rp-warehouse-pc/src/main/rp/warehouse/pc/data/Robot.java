package rp.warehouse.pc.data;

import rp.warehouse.pc.communication.Communication;
import rp.warehouse.pc.communication.Protocol;
import rp.warehouse.pc.route.RobotsControl;
import rp.warehouse.pc.route.RoutePlan;

import java.io.IOException;
import java.util.Queue;

public class Robot implements Runnable {

    // Communications
    private final String ID;            // Communication ID
    private final String name;          // Communication name
    private final Communication comms;  // Communication used to connect each robot to the a nxt brick

    // Route information
    private Queue<Integer> route;       // Queue of directions for the current task
    private int currentInstruction;     // The current Instruction being done by robot
    private RobotLocation location;     // Current location of the robot

    // Job information
    private Queue<Task> tasks;          // The queue of Tasks which need to be done
    private Item currentItem;           // Current Item 
    
    // Robot Information
    private final static float weightLimit = 50.0f;
    private float currentWeightOfCargo = 0.0f;
    private boolean dropOffCheck = false;
    
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
    public Robot(String ID, String name, Queue<Task> newTasks) throws IOException {
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
                // Do nothing
            }
            // Checks if location of the robot matches
            // with the location of the item or drop off
            else if(route.peek()==null) {
                //checks if its dropping off
                if (dropOffCheck) {
                    
                    dropOff();
                    dropOffCheck = false;
                }else {
                    // Stops and waits for the number of items
                    itemCheck:
                    while(true) {
                        int numberOfItems = comms.sendPickupRequest();
                        
                        // Checks that number of items fit 
                        if(pickUp(numberOfItems)) {
                            break itemCheck;
                        }
                    }
                }
                
                if (tasks.peek()!=null) {
                    plan();
                }else {
                    //Do nothing 
                }
            }
            // If there are still instructions present
            else{
                currentInstruction=route.peek();
                comms.sendMovement(getNextInstruction());
                updateLocation();
                updateCurrentItem();
            }

        }
    }

    public String getID() {
        return ID;
    }

    public RobotLocation getLocation() {
        return location;
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
        int directionPointing = 0;
        switch (currentInstruction) {
        case Protocol.NORTH:
            y += 1;
            directionPointing = Protocol.NORTH;
            break;
        case Protocol.EAST:
            x += 1;
            directionPointing = Protocol.EAST;
            break;
        case Protocol.SOUTH:
            y -= 1;
            directionPointing = Protocol.SOUTH;
            break;
        case Protocol.WEST:
            x -= 1;
            directionPointing = Protocol.WEST;
            break;

        default:
            break;
        }
        location.setX(x);
        location.setY(y);
        location.setDirection(directionPointing);
        
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
        // Update 

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
     * @return - True, there is still space for more cargo or the cargo is full. 
     *              False, too many items being picked up
     */
    private boolean pickUp(int numberOfItems) {
        if (currentWeightOfCargo+(currentItem.getWeight()*numberOfItems) > weightLimit) {
            // Go back to drop off
            // route =RoutePlan.planDropOff(this);
            dropOffCheck = true;
            return false;
        }
        currentWeightOfCargo = currentItem.getWeight()*numberOfItems;
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
    private boolean dropOff() {
        if (currentWeightOfCargo==0) {
            return false;
        }
        currentWeightOfCargo=0;
        return true;
    }
    
    private void plan() {
        // Change Location
        route =RoutePlan.plan(this, new Location(1,1));//tasks.peek();
        // which will be static 
    }
    
}
