package rp.warehouse.pc.data.robot;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ExecutorService;

import org.apache.log4j.Logger;

import rp.util.Rate;
import rp.warehouse.pc.communication.Communication;
import rp.warehouse.pc.data.Item;
import rp.warehouse.pc.data.Task;
import rp.warehouse.pc.data.robot.utils.RobotUtils;
import rp.warehouse.pc.localisation.implementation.Localiser;
import rp.warehouse.pc.route.RoutePlan;

public class Robot implements Runnable{

    // Communications
    private final String ID;                            // Communication ID
    private final String name;                          // Communication name
    private Communication comms;                        // Communication used to connect each robot to the a nxt brick

    // Route information
    private LinkedList<Integer> route;                  // Queue of directions for the current task
    private int lastInstruction = -1;                   // The current Instruction being done by robot (For WMI)
    private RobotLocation location;                     // Current location of the robot

    // Job information
    private Queue<Task> tasks;                          // The queue of Tasks which need to be done
    private Item currentItem;                           // Current Item
    private Task currentTask;
    private static Map<String, Boolean> cancelledJobs;  // Stores ID's of cancelled Jobs

    // Robot Information
    private final static float WEIGHTLIMIT = 50.0f;     // The maximum load robot can carry
    private float currentWeightOfCargo = 0.0f;
    private boolean dropOffCheck = false;               // Indicates if drop off needs to happen
    private boolean running = true;
    private boolean pickUpDone = false;
    private boolean cancel = false;
    private int RATE = 20;

    RobotUtils robotUtils;

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
    public Robot(String ID, String name, Queue<Task> newTasks, ExecutorService pool, RobotLocation startingLocation)
            throws IOException {
        //super( ID, name, newTasks, pool, startingLocation);
        this.ID = ID;
        this.name = name;
        this.tasks = newTasks;
        cancelledJobs = new HashMap<String, Boolean>();
        updateTask();
        logger.debug(name + " : Has " + tasks.size() + " tasks");

        // Communications set up
        this.comms = new Communication(ID, name, this);
        pool.execute(comms);

        // Localisation
        Localiser loc = new Localiser(comms);
        this.location = startingLocation;// loc.getPosition();
        robotUtils = new RobotUtils(location, name);

        logger.trace("Robot class: " + ID + " " + name + " Has been created");
        plan(false);

    }

    // Runs indefinitely and sends commands to Communication
    public void run() {
        logger.info("Robot " + ID + " " + name + "was successfully connected");
        Rate r = new Rate(RATE);

        while (running) {
            logger.debug(name + ": " + "Sending Next instruction");
            sendInstruction();
            logger.debug(name + ": " + "Instruction executed");
            logger.debug(name + " : Has " + tasks.size() + " tasks");
            r.sleep();

        }
    }

    private void sendInstruction() {
        comms.sendMovement(getCurrentInstruction());
        updateCurrentItem();

    }



    /**
     * Used to update the current item
     */
    private void updateCurrentItem() {
        //updateLocation();
        robotUtils.updateLocation(lastInstruction);
        if (cancelledJobs.containsKey(currentTask.getJobID())) {
            plan(true);
            nextItemWeightCheck();
            cancel = false;
        } else if (route.isEmpty() || cancel) {
            if (location.equals(currentItem.getLocation())) {
                logger.debug(name + ": " + "Waiting for " + ((dropOffCheck) ? "Drop Off" : "Pick Up"));
                Rate r = new Rate(RATE);

                // Loops until the right number of items is entered
                while (!pickUpDone) {
                    pickUp(comms.sendLoadingRequest(currentTask.getCount()));
                    r.sleep();
                }

                // Only needs the button to be pressed once
                if (dropOffCheck) {
                    comms.sendLoadingRequest(currentTask.getCount());
                    dropOff();
                }
                logger.debug(name + ": " + "Item update completed");

                dropOffCheck = false;
                pickUpDone = false;
                cancel = false;
            } else {
                cancel = false;
                plan(false);
            }
        }

    }

    /**
     * For: Communication Cancels Job of the current item
     */
    public void cancelJob() {
        cancel = true;
        logger.debug(name + ": " + "Starting Job cancellation");
        // Adds Job ID to the map of cancelled jobs
        cancelledJobs.put(currentTask.getJobID(), true);

        // When in pick up mode
        plan(true);
    }

    /**
     * For: Communication specify amount loaded for the current item
     *
     * @return - True, there is still space for more cargo or the cargo is full.
     *         False, too many items being picked up
     */
    private boolean pickUp(int numberOfItems) {
        logger.trace(name + ": " + "Starting pickUp");

        if (readyForPickUp(numberOfItems)) {
            logger.info(name + ": " + "Pick up valid");

            float newWeight = currentWeightOfCargo + (currentItem.getWeight() * numberOfItems);
            // This might happens when Task was cancelled
            if (newWeight > WEIGHTLIMIT) {
                logger.error(name + ": " + "To much cargo, going to drop off. Should not happen");
                planToDropOff(false);
                return false;
            } else if (newWeight == WEIGHTLIMIT) {
                logger.debug(name + ": " + "Picked up Item(s), cargo is full. Going to drop off");
                currentWeightOfCargo = newWeight;
                planToDropOff(true);
                return true;
            }

            currentWeightOfCargo = newWeight;
            logger.info(name + ": " + "Picked up " + numberOfItems + " Item(s), continuing with tasks");
            logger.info(name + ": " + "current weight of cargo " + currentWeightOfCargo);

            nextItemWeightCheck();

            pickUpDone = true;
            return true;
        } else {
            logger.warn(name + ": " + "Wrong number, Please try again");
            return false;
        }
    }

    private boolean readyForPickUp(int numberOfItems) {
        return route.isEmpty() && !dropOffCheck && numberOfItems == currentTask.getCount();
    }

    /**
     * For Communication when performing drop of at the station
     *
     * @return -
     */
    public boolean dropOff() {
        logger.debug(name + ": " + "Starting DropOff");
        logger.info(name + ": " + "current weight of cargo " + currentWeightOfCargo);
        if (!route.isEmpty() || !dropOffCheck) {
            logger.warn(name + ": " + "Drop off refused");
            return false;
        } else {
            logger.info(name + ": " + "Drop off valid");
            logger.debug(name + ": " + "Planning to get current item");
            plan(false);
            currentWeightOfCargo = 0;
            dropOffCheck = false;
            return true;
        }

    }

    private void nextItemWeightCheck() {
        float weightForNextItem = currentWeightOfCargo + (tasks.peek().getItem().getWeight() * tasks.peek().getCount());

        if (weightForNextItem > WEIGHTLIMIT) {
            logger.info(name + ": " + "Will not be able to fit the next item, going to drop off");
            planToDropOff(false);
        } else {
            plan(true);
        }
    }

    private void updateTask() {
        if (tasks.isEmpty()) {
            logger.info(name + ": I am done");
            System.exit(0);
        }

        while (cancelledJobs.containsKey(tasks.peek().getJobID())) {
            this.currentTask = tasks.poll();
        }
        this.currentTask = tasks.poll();
        this.currentItem = currentTask.getItem();
    }

    private void planToDropOff(boolean getNextItem) {
        if (getNextItem)updateTask();

        route = (LinkedList<Integer>) RoutePlan.planDropOff(this);
        dropOffCheck = true;
    }

    private void plan(boolean getNextItem) {
        logger.info(name + ": " + "Starting plan");
        if (getNextItem) updateTask();

        if (currentItem != null) {
            route = (LinkedList<Integer>) RoutePlan.plan(this, currentItem.getLocation());
        } else {
            logger.error(name + ": " + "No current Item, I thing I am done");
            updateTask();
            plan(true);

        }
    }

    private int getCurrentInstruction() {
        logger.info(name + ": " + " getting Current Instruction");
        logger.info("Route is Empty: " + route.isEmpty());
        
        lastInstruction = route.poll();
        
        logger.info(name + ": " + "Executing command " + getDirectionString(lastInstruction));
        return lastInstruction;
    }

    /**
     * For Warehouse MI
     * 
     * @return Queue<Integer> of directions
     */
    public LinkedList<Integer> getRoute() {
        return new LinkedList<>(route);
    }

    public String getID() {
        String copy = ID;
        return copy;
    }

    public String getName() {
        return name;
    }

    public Task getTask() {
        Task copy = currentTask;
        return copy;
    }

    public RobotLocation getLocation() {
        return new RobotLocation(location);
    }

    private String getDirectionString(int direction) {
        // Works out the String representation of the command
        return (direction <= 4 ? (direction == 3 ? "North" : "East") : (direction == 5 ? "South" : "West"));
    }

}

// Music to listen to while coding
// https://www.youtube.com/watch?v=L5gVFYmDWCk