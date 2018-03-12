package rp.warehouse.pc.data;

import rp.util.Rate;
import rp.warehouse.pc.communication.Communication;
import rp.warehouse.pc.communication.Protocol;
import rp.warehouse.pc.localisation.Localiser;
import rp.warehouse.pc.route.RoutePlan;
import org.apache.log4j.Logger;
import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ExecutorService;

public class Robot implements Runnable {

    // Communications
    private final String ID;                    // Communication ID
    private final String name;                  // Communication name
    private Communication comms;                // Communication used to connect each robot to the a nxt brick

    // Route information
    private Queue<Integer> route;               // Queue of directions for the current task
    private int lastInstruction = -1;           // The current Instruction being done by robot (For WMI)
    private RobotLocation location;             // Current location of the robot

    // Job information
    private Queue<Task> tasks;                  // The queue of Tasks which need to be done
    private Item currentItem;                   // Current Item
    private Task currentTask;

    // Robot Information
    private final static float WEIGHTLIMIT = 50.0f;// The maximum load robot can carry
    private float currentWeightOfCargo = 0.0f;  
    private boolean dropOffCheck = false;       // Indicates if drop off needs to happen 
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
    public Robot(String ID, String name, Queue<Task> newTasks, ExecutorService pool, RobotLocation startingLocation) throws IOException {
        this.ID = ID;
        this.name = name;
        this.tasks = newTasks;
        this.currentTask = tasks.poll();
        this.currentItem = currentTask.getItem();
        this.comms = new Communication(ID, name, this);
        pool.execute(comms);
        Localiser loc = new Localiser(comms);
        this.location = startingLocation;// loc.getPosition();
        logger.trace("Robot class: " + ID + " " + name + " Has been created" );
        plan();

    }

    // Runs indefinitely and sends commands to Communication
    public void run() {
        logger.info("Robot " + ID + " " + name + "was successfully connected");
        Rate r = new Rate(20);
        
        while (running) {
            logger.debug(name + ": " +"Sending Next instruction");
            sendInstruction();
            logger.debug(name + ": " +"Instruction executed");
            r.sleep();
            
        }
    }
    
    private void sendInstruction(){
        comms.sendMovement(getCurrentInstruction());
        updateCurrentItem();

    }

    /**
     * Updates robots location
     */
    private void updateLocation() {
        if (lastInstruction!=-1) {
            logger.debug(name + ": " +"Updating location");
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
            logger.info(name + ": " +"Current location X: " + location.getX() + " Y: "+location.getY());
        }
    }

    /**
     * Used to update the current item
     */
    private void updateCurrentItem() {
        updateLocation();
        if (route.peek() == null) {
            logger.debug(name + ": " +"Waiting for " + ((dropOffCheck)? "Drop Off":"Pick Up"));
            Rate r = new Rate(20);
            while (!pickUpDone) {
                pickUp(comms.sendLoadingRequest(currentTask.getCount()));
                r.sleep();
            }
            logger.debug(name + ": " +"Item update completed");
            dropOffDone = false;
            pickUpDone = false;
        }
        
    }

    /**
     * For: Communication Cancels Job of the current item
     */
    public void cancelJob() {
        logger.debug(name + ": " +"Starting Job cancellation");

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
    private boolean pickUp(int numberOfItems) {
        logger.trace(name + ": " +"Starting pickUp");
        if (route.peek() == null && !dropOffCheck  && numberOfItems == currentTask.getCount()) {
            logger.info(name + ": " +"Pick up valid");
            float newWeight = currentWeightOfCargo + (currentItem.getWeight() * numberOfItems);
            if ( newWeight > WEIGHTLIMIT) {
                logger.debug(name + ": " +"To much cargo, going to drop off");
                logger.error(name + ": " +"Should not happen");
                goToDropOff(false); 
                return false;
            }else if (newWeight == WEIGHTLIMIT) {
                logger.debug(name + ": " +"Picked up Item(s), cargo is full. Going to drop off");
                currentWeightOfCargo = newWeight;
                goToDropOff(true); 
                return true;
            }
            logger.info(name + ": " +"Picked up " + numberOfItems + " Item(s), continuing with tasks");
            currentWeightOfCargo = newWeight;
            logger.info(name + ": " +"current weight of cargo " + currentWeightOfCargo);
            this.currentTask = tasks.poll();
            this.currentItem = currentTask.getItem();
            if(currentWeightOfCargo + (currentItem.getWeight() * currentTask.getCount()) > WEIGHTLIMIT) {
                logger.info(name + ": " +"Will not be able to fit the next item, going to drop off");
                
                goToDropOff(false); 
            }else {
                plan();
            } 
          
            
            pickUpDone = true;
            return true;
        }else {
            logger.warn(name + ": " +"Pick up refused");
            return false;
        }
    }
    
    private void goToDropOff(boolean nextItem) {
        if(nextItem) {
            this.currentTask = tasks.poll();
            this.currentItem = currentTask.getItem();
        }
        route = RoutePlan.planDropOff(this);
        dropOffCheck = true;
    }
    
    
    /**
     * For Communication when performing drop of at the station
     * @return - 
     */
    public boolean dropOff() {
        logger.debug(name + ": " +"Starting DropOff");
        if (route.peek()!=null || !dropOffCheck) {
            logger.warn(name + ": " +"Drop off refused");
            return false;
        }else {
            logger.info(name + ": " +"Drof off valid");
            if (tasks.peek()!=null) {// why?
                logger.debug(name + ": " +"Planning to get current item");
                plan();
                lastInstruction = -1;
            }
            currentWeightOfCargo=0;
            dropOffCheck = false;
            dropOffDone = true;
            return true;
        }

    }
    
    private void plan() {
        logger.info(name + ": " +"Starting plan");
        if (currentItem!=null) {
            route = RoutePlan.plan(this, currentItem.getLocation());
        }else {
            logger.error(name + ": " +"No current Item");
            //route = null;
            route.add(3);
            route.add(4);
            route.add(5);
            route.add(6);
            
        }
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
    
    private String getDirectionString(int direction) {
        // Works out the String representation of the command 
        return (direction <= 4 ? (direction == 3 ? "North" : "East"):(direction == 5 ? "South" : "West"));
    }
    
    private int getCurrentInstruction() {
        logger.info(name + ": " +"Starting getCurrentInstraction");

        lastInstruction = route.poll(); 
        // Check Job ID (Discard cancelled jobs "items" )
        logger.info(name + ": " +"Executing command " + getDirectionString(lastInstruction));
        return lastInstruction;
    }

    
}

//Music to listen to while coding
//https://www.youtube.com/watch?v=L5gVFYmDWCk