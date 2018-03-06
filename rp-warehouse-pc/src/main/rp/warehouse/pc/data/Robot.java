package rp.warehouse.pc.data;

import rp.util.Rate;
import rp.warehouse.pc.communication.Communication;
import rp.warehouse.pc.communication.Protocol;
import rp.warehouse.pc.route.RoutePlan;
import org.apache.log4j.Logger;
import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ExecutorService;

public class Robot implements Runnable {

    // Communications
    private final String ID;            // Communication ID
    private final String name;          // Communication name
    private final ExecutorService pool;
    private Communication comms;  // Communication used to connect each robot to the a nxt brick

    // Route information
    private Queue<Integer> route;       // Queue of directions for the current task
    private int lastInstruction = -1;     // The current Instruction being done by robot (For WMI)
    private RobotLocation location;     // Current location of the robot

    // Job information
    private Queue<Task> tasks;          // The queue of Tasks which need to be done
    private Item currentItem;           // Current Item

    // Robot Information
    private final static float WEIGHTLIMIT = 50.0f;
    private float currentWeightOfCargo = 0.0f;
    private boolean dropOffCheck = false;
    private boolean running = true;
    private boolean dropOffDone = false;
    private boolean pickUpDone = false;

    private static final Logger logger = Logger.getLogger(Robot.class);

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
    public Robot(String ID, String name, Queue<Task> newTasks, ExecutorService pool, RobotLocation startingLocation) {
        this.ID = ID;
        this.name = name;
        this.pool = pool;
        this.tasks = newTasks;
        this.currentItem = tasks.poll().item;
        this.location = startingLocation;
        logger.trace("Robot class: " + ID + " " + name + " Has been created" );
        
        plan();

    }

    // Runs indefinitely and sends commands to Communication
    public void run() {
        try {
            init();
        } catch (IOException e) {
            logger.error("Couldn't connect to: " + name);
            running = false;
        }
        logger.trace("Robot " + ID + " " + name + "was successfully connected");
        Rate r = new Rate(20);
        while (running) {
            sendInstruction();
            r.sleep();
            
        }
    }
    
    private void sendInstruction(){
        updateLocation();
        comms.sendMovement(getCurrentInstruction());
        updateCurrentItem();

    }

    /**
     * Updates robots location
     */
    private void updateLocation() {
        if (lastInstruction!=-1) {
            int x, y;
            x = location.getX();
            y = location.getY();
            int directionPointing = 0;
            switch (lastInstruction) {
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
        }
    }

    /**
     * Used to update the current item
     */
    private void updateCurrentItem() {

        if (route.peek() == null) {
            while(!dropOffDone && !pickUpDone) {
                
            }
            dropOffDone = false;
            pickUpDone = false;
            currentItem = tasks.poll().item;// Ask Megan to change it
        }
        
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
     * @return - True, there is still space for more cargo or the cargo is full. 
     *              False, too many items being picked up
     */
    public boolean pickUp(int numberOfItems) {
        if (route.peek()==null && !dropOffCheck) {
            float newWeight = currentWeightOfCargo + (currentItem.getWeight() * numberOfItems);
            if ( newWeight> WEIGHTLIMIT) {
                route = RoutePlan.planDropOff(this);
                dropOffCheck = true;
                return false;
            }else if (newWeight == WEIGHTLIMIT) {
                currentWeightOfCargo = newWeight;
                // Go back to drop off
                route = RoutePlan.planDropOff(this);
                dropOffCheck = true;
                return true;
            }
            currentWeightOfCargo = newWeight;
            plan();
            
            pickUpDone = true;
            return true;
        }else {
            return false;
        }
    }
    
    /**
     * For Communication when performing drop of at the station
     * @return - 
     */
    public boolean dropOff() {

        if (route.peek()!=null || !dropOffCheck) {
            return false;
        }else {
            if (tasks.peek()!=null) {
                plan();
            }
            currentWeightOfCargo=0;
            dropOffCheck = false;
            dropOffDone = true;
            return true;
        }

    }
    
    private void plan() {
        if (currentItem!=null) {
            route = RoutePlan.plan(this, new Location(1,1));//currentItem.getLocation;
        }else {
            
        }
    }
    
    private void init() throws IOException {
        this.comms = new Communication(ID, name, this);
        pool.execute(comms);
    }
    
    /**
     * For Warehouse MI 
     * @return Queue<Integer> of directions
     */
    public Queue<Integer> getRoute(){
        return route;
    }
    
    public String getID() {
        return ID;
    }

    public RobotLocation getLocation() {
        return location;
    }
    

    /**
     * For: Job Assignment
     * @param newTasks
     *            - the new queue of Jobs for this robot to complete
     */
    public void setJobs(Queue<Task> newTasks) {
        tasks = newTasks;
    }
    
    private int getCurrentInstruction() {
        lastInstruction = route.poll(); 
        // Check Job ID (Discard cancelled jobs "items" )
        return lastInstruction;
    }

    
}
