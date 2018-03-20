package rp.warehouse.pc.data.robot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import lejos.util.Delay;
import org.apache.log4j.Logger;

import rp.util.Rate;
import rp.warehouse.pc.communication.Communication;
import rp.warehouse.pc.communication.Protocol;
import rp.warehouse.pc.data.Item;
import rp.warehouse.pc.data.Task;
import rp.warehouse.pc.data.robot.utils.RewardCounter;
import rp.warehouse.pc.data.robot.utils.RobotLocation;
import rp.warehouse.pc.data.robot.utils.RobotUtils;
import rp.warehouse.pc.data.robot.utils.Status;
import rp.warehouse.pc.localisation.implementation.Localiser;
import rp.warehouse.pc.route.Route;
import rp.warehouse.pc.route.RoutePlan;

/**
 *
 * @author roman
 *
 */
public class Robot implements Runnable {

    // Communications
    private final String ID; // Communication ID
    private final String name; // Communication name
    private Communication comms; // Communication used to connect each robot to the a nxt brick

    // Route information
    private Route route; // Queue of directions for the current task
    private int lastInstruction = -1; // The current Instruction being done by robot (For WMI)
    private RobotLocation location; // Current location of the robot
    Localiser loc;

    // Job information
    private final Queue<Task> tasks; // The queue of Tasks which need to be done
    private Item currentItem; // Current Item
    private Task currentTask; // Current Task which contains one Item
    // private final static Map<String, Boolean> cancelledJobs = new HashMap<String,
    // Boolean>(); // Stores ID's of cancelled Jobs

    // Robot Information
    private final static float WEIGHTLIMIT = 50.0f; // The maximum load robot can carry
    private float currentWeightOfCargo = 0.0f;
    private final int RATE = 20; // Rate of sleep
    private int status = Status.NOTHING; // Current Status of the robot
    private final List<Task> tasksInTheCargo = new ArrayList<>(); // List of Tasks currently picked up
    private boolean getNextItem = false;

    // Utilities
    RobotUtils robotUtils; // Used to perform updates of location

    private static final Logger logger = Logger.getLogger(Robot.class);

    public Robot(String ID, String name, Queue<Task> newTasks, Communication comms, RobotLocation startingLocation)
            throws IOException {
        // Initialisation
        this.ID = ID;
        this.name = name;
        this.tasks = newTasks;
        this.currentTask = tasks.poll();
        this.currentItem = currentTask.getItem();

        // Communications set up
        this.comms = comms;
        comms.setDirection(startingLocation.getDirection());

        this.location = startingLocation;
        robotUtils = new RobotUtils(location, name);

        logger.info(name + ": Created");
    }

    @Override
    public void run() {
        logger.info(name + ": Started running");
        Rate r = new Rate(RATE);

        // Runs indefinitely
        while (true) {
            r.sleep();
            // logger.debug(name + "Rewards " + RewardCounter.getPointsEarned());
            // Updates the current task and item
            // And checks if the Job was cancelled
            updateTasks();

            if (route == null || route.isEmpty()) {
                // Plans again when runs out of route

                switch (status) {
                case Status.PICKING_UP:
                    planning(true);
                    break;
                case Status.DROPPING_OFF:
                    planning(false);
                    break;
                case Status.NOTHING:
                    planning(true);
                    break;

                default:
                    break;
                }

            } else if (route.peek() == Protocol.PICKUP) {
                // When pick up location was reached
                status = Status.WAITING_FOR_PICKUP;
                logger.debug(name + ": Waiting for Pick Up");

                // Loops until right number of items was entered
                while (!pickUp(comms.sendLoadingRequest(currentTask.getCount()))) {
                    r.sleep();
                    Delay.msDelay(200);
                }

                route = null;
            } else if (route.peek() == Protocol.DROPOFF) {
                // When drop off location was reached
                status = Status.WAITING_FOR_DROPOFF;
                logger.debug(name + ": Waiting for Drop Off");

                // Waits for the button to be pressed to drop off
                comms.sendLoadingRequest(0);
                dropOff();

                route = null;
            } else if (route.peek() == Protocol.WAITING) {
                // When the location is occupied
                r.sleep();

                route = null;
            } else {
                // Sends an instruction
                logger.info(name + ": Sending next instruction");

                // Updates the last Instruction and location (Location is one ahead)
                lastInstruction = route.poll();
                robotUtils.updateLocation(lastInstruction);
                comms.sendMovement(lastInstruction);

            }

        }

    }

    /**
     * Called when picking up items
     * 
     * @param numberOfItems
     *            - number of items that user wants to pick up
     * @return - return true if successfully picked up and false otherwise
     */
    private boolean pickUp(int numberOfItems) {
        if (RewardCounter.checkIfCancelled(currentTask)) {
            // If the Job was cancelled
            logger.debug(name + ": Canceled Job");
            status = Status.PICKING_UP;
            return true;

        } else if (numberOfItems == currentTask.getCount()) {
            logger.info(name + ": Pick up accepted");

            float newWeight = currentWeightOfCargo + currentItem.getWeight() * currentTask.getCount();

            if (newWeight > WEIGHTLIMIT) {
                logger.warn(name + ": Not enough spacem going to drop off. Should not happen");
                // drop off come back for the item
                status = Status.DROPPING_OFF;

            } else if (newWeight == WEIGHTLIMIT) {
                logger.debug(name + ": Picked up, cargo is full");
                currentWeightOfCargo = newWeight;
                // drop off
                status = Status.DROPPING_OFF;

                // Place task to the cargo
                tasksInTheCargo.add(currentTask);

                // Get next item
                getNextItem = true;

            } else {
                logger.debug(name + ": Picked up, all good");
                currentWeightOfCargo = newWeight;
                // Plan next item
                status = Status.PICKING_UP;

                // Place task to the cargo
                tasksInTheCargo.add(currentTask);

                // Get next item
                getNextItem = true;
                if (tasks.isEmpty()) {
                    status = Status.DROPPING_OFF;
                }
            }
            logger.debug(name + ": Current weight " + currentWeightOfCargo);
            return true;

        }
        logger.info(name + ": Pick up rejected, try other numbers");
        return false;

    }

    /**
     * When called clears the cargo
     */
    private void dropOff() {
        logger.debug(name + ": Dropped off");

        // empty cargo
        currentWeightOfCargo = 0;
        for (Task task : tasksInTheCargo) {
            RewardCounter.addCompletedJob(task);
            // RewardCounter.addReward(task.getItem().getReward());
        }
        tasksInTheCargo.clear();

        // plan
        status = Status.PICKING_UP;
    }

    /**
     * When called the current Job is cancelled
     */
    public void cancelJob() {
        // Adds Job to the list of cancelledJobs
        RewardCounter.addCancelledJob(currentTask);
        // cancelledJobs.put(currentTask.getJobID(), true);
        logger.debug(name + ": Cancelled current Job");
    }

    /**
     * Checks for cancellation, empty tasks and if can fit current task
     */
    private void updateTasks() {
        // Only shuts down when dropped off all the items
        if (tasks.isEmpty() && currentWeightOfCargo == 0) {
            logger.info(name + ": I am Done");
            Thread.currentThread().interrupt();
            System.exit(0);
        } else if (getNextItem && !tasks.isEmpty()) {
            currentTask = tasks.poll();
            currentItem = currentTask.getItem();
            getNextItem = false;
        }
        while (RewardCounter.checkIfCancelled(currentTask)) {
            logger.debug(name + ": Job " + currentTask.jobID + " , Item " + currentItem.getClass() + " was canceled");
            this.currentTask = tasks.poll();
            this.currentItem = currentTask.getItem();
            route = null;
        }
        float newWeight = currentWeightOfCargo + currentItem.getWeight() * currentTask.getCount();
        if (newWeight > WEIGHTLIMIT && status == Status.PICKING_UP) {
            logger.debug(name + ": Not enough space for next item going to drop off.");
            // drop off come back for the item
            status = Status.DROPPING_OFF;
        }
    }

    private void planning(boolean pickUp) {
        if (pickUp) {
            logger.debug(name + ": Planning for Pick up. For Point: " + currentItem.getLocation());
            // plan for pick up of current item
            route = RoutePlan.plan(this, currentItem.getLocation());
        } else {
            logger.debug(name + ": Planning for Drop off");
            // plan drop off for current item
            route = RoutePlan.planDropOff(this);
        }
    }

    /**
     * @return - returns copy of the route
     */
    public Route getRoute() {
        return route == null ? null : new Route(route);
    }

    /**
     * @return - returns ID of the robot
     */
    public String getID() {
        return ID;
    }

    /**
     * @return - returns name of the robot
     */
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

    public static String getDirectionString(int direction) {
        // Works out the String representation of the command
        return (direction <= 4 ? (direction == 3 ? "North" : "East") : (direction == 5 ? "South" : "West"));
    }

    @Override
    public String toString() {
        return "Cargo weight: " + currentWeightOfCargo + "\nEstimated items remaining: " + tasks.size() + "\nStatus: "
                + Status.getWord(status);
    }

}
