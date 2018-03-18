package rp.warehouse.pc.data.robot;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ExecutorService;

import rp.util.Rate;
import rp.warehouse.pc.communication.Communication;
import rp.warehouse.pc.communication.Protocol;
import rp.warehouse.pc.data.Item;
import rp.warehouse.pc.data.Task;
import rp.warehouse.pc.data.robot.utils.RobotUtils;
import rp.warehouse.pc.localisation.implementation.Localiser;
import rp.warehouse.pc.route.Route;
import rp.warehouse.pc.route.RoutePlan;

public class Robot implements Runnable {
    
   // Communications
    private final String ID;                            // Communication ID
    private final String name;                          // Communication name
    private Communication comms;                        // Communication used to connect each robot to the a nxt brick

    // Route information
    private Route route;                  // Queue of directions for the current task
    private int lastInstruction = -1;                   // The current Instruction being done by robot (For WMI)
    private RobotLocation location;                     // Current location of the robot
    Localiser loc;
    
    // Job information
    private Queue<Task> tasks;                          // The queue of Tasks which need to be done
    private Item currentItem;                           // Current Item
    private Task currentTask;
    private static Map<String, Boolean> cancelledJobs;  // Stores ID's of cancelled Jobs

    // Robot Information
    private final static float WEIGHTLIMIT = 50.0f;     // The maximum load robot can carry
    private float currentWeightOfCargo = 0.0f;
    private int RATE = 20;
    private String robotStatus = "No Job";
    private int status = Status.NOTHING;

    RobotUtils robotUtils;
    public Robot(String ID, String name, Queue<Task> newTasks, ExecutorService pool, RobotLocation startingLocation) throws IOException {
        this.ID = ID;
        this.name = name;
        this.tasks = newTasks;
        cancelledJobs = new HashMap<String, Boolean>();
        this.currentTask = tasks.poll();
        this.currentItem = currentTask.getItem();

        // Communications set up
        this.comms = new Communication(ID, name, this);
        pool.execute(comms);

        // Localisation
        loc = new Localiser(comms);
        this.location = startingLocation;// loc.getPosition();
        robotUtils = new RobotUtils(location, name);
        
    }
    @Override
    public void run() {
        // localisation happens here
        Rate r = new Rate(RATE);

        while(true) {
         
            updateTasks();
            
            if(route == null || route.isEmpty()) {
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
                
            } else if(route.peek() == Protocol.PICKUP) {
                while(!pickUp(comms.sendLoadingRequest(currentTask.getCount()))) {
                    r.sleep();
                }
            } else if(route.peek() == Protocol.DROPOFF) {
                route.poll();
                comms.sendLoadingRequest(0);
                dropOff();
                
            } else if (route.peek() == Protocol.WAITING) {
                route.poll();
                r.sleep();
            } else {
                lastInstruction = route.poll();
                robotUtils.updateLocation(lastInstruction);
                comms.sendMovement(lastInstruction);
                
            }

        }

    }
    
    private boolean pickUp(int numberOfItems){
        if (numberOfItems == currentTask.getCount()) {
            // check if cancelled
            float newWeight = currentWeightOfCargo + currentItem.getWeight() * currentTask.getCount();
            if (newWeight > WEIGHTLIMIT) {
                //drop off
                status = Status.DROPPING_OFF;
                //come back
            } else if (newWeight == WEIGHTLIMIT) {
                currentWeightOfCargo = newWeight;
                //drop off
                status = Status.DROPPING_OFF;

                // Get next item
                currentTask = tasks.poll();
                currentItem = currentTask.getItem();
            } else if ((!tasks.isEmpty()) && WEIGHTLIMIT < currentWeightOfCargo
                    + (tasks.peek().getCount() * tasks.peek().getItem().getWeight())) {
                currentWeightOfCargo = newWeight;
                // drop off
                status = Status.DROPPING_OFF;
                // come back
            } else {
                currentWeightOfCargo = newWeight;
                // Plan next item
                status = Status.PICKING_UP;

                // Get next item
                currentTask = tasks.poll();
                currentItem = currentTask.getItem();
            } 
            return true;
        }
        return false;
        
        
    }
    
    private void dropOff() {
        // empty cargo
        currentWeightOfCargo = 0;

        // plan
        status = Status.PICKING_UP;
    }
    
    public void cancelJob() {
        cancelledJobs.put(currentTask.getJobID(), true);
    }

    private void updateTasks() {
        if (tasks.isEmpty()) {
            System.exit(0);
        }
        
        while (cancelledJobs.containsKey(currentTask.getJobID())) {
            this.currentTask = tasks.poll();
            this.currentItem = currentTask.getItem();
            route = null;
        }
    }
    private void planning(boolean pickUp) {
        if (pickUp) {
            // plan for pick up of current item
            route = RoutePlan.plan(this, currentItem.getLocation());
        }else {
            // plan drop off for current item
            route = RoutePlan.planDropOff(this);
        }
    }
    public Route getRoute() {
        return new Route(route);
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

    public static String getDirectionString(int direction) {
        // Works out the String representation of the command
        return (direction <= 4 ? (direction == 3 ? "North" : "East") : (direction == 5 ? "South" : "West"));
    }

    @Override
    public String toString() {
        return "Cargo weight: " + currentWeightOfCargo
                + "\nEstimated items remaining: " + tasks.size()
                + "\nStatus: " + status;
    }

}
