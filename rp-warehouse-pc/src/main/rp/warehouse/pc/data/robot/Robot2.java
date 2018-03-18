/*
package rp.warehouse.pc.data.robot;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ExecutorService;

import rp.warehouse.pc.communication.Communication;
import rp.warehouse.pc.communication.Protocol;
import rp.warehouse.pc.data.Item;
import rp.warehouse.pc.data.Task;
import rp.warehouse.pc.data.robot.utils.RobotUtils;
import rp.warehouse.pc.localisation.implementation.Localiser;
import rp.warehouse.pc.route.Route;
import rp.warehouse.pc.route.RoutePlan;

public class Robot2 implements Runnable {
    
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
    
    private boolean nextItem = false;
    private boolean needPickUpPlan = false;
    private boolean pickUpDone = false;

    RobotUtils robotUtils;
    public Robot2(String ID, String name, Queue<Task> newTasks, ExecutorService pool, RobotLocation startingLocation) {
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

        while(true) {
         
            // check for cancellations
            // route = null;
            
            if(route == null) {
                switch (status) {
                case Status.PICKING_UP:
                    needPickUpPlan = true;
                    break;
                case Status.DROPPING_OFF:
                    needPickUpPlan = false ;
                    break;
                case Status.NOTHING:
                    needPickUpPlan = true;
                    break;

                default:
                    break;
                }
                
            } else if(route.peek() == Protocol.PICKUP) {
                while(!pickUpDone) {
                    pickUp(comms.sendLoadingRequest(currentTask.getCount()));
                }
            } else if(route.peek() == 10) {
                comms.sendLoadingRequest(0);
               dropOff();
                
            } else if (route.peek() == Protocol.WAITING) {
                //rate.sleep();
                
            } else {
                comms.sendMovement(route.poll());
                
            }
            
            
            updateTasks();
            
            // Planning
            planning();

        }

    }
    
    private void pickUp(int numberOfItems){
        if (numberOfItems == currentTask.getCount()) {
            // check if cancelled
            float newWeight = currentWeightOfCargo + currentItem.getWeight() * currentTask.getCount();
            if (newWeight > WEIGHTLIMIT) {
                //drop off
                needPickUpPlan = false;
                //come back
                nextItem = false;
            } else if (newWeight == WEIGHTLIMIT) {
                currentWeightOfCargo = newWeight;
                //drop off
                needPickUpPlan = false;
                // don't come back
                nextItem = true;
            } else if ((!tasks.isEmpty()) && WEIGHTLIMIT < currentWeightOfCargo
                    + (tasks.peek().getCount() * tasks.peek().getItem().getWeight())) {
                currentWeightOfCargo = newWeight;
                // drop off
                needPickUpPlan = false;
                // come back
                nextItem = false;
            } else {
                currentWeightOfCargo = newWeight;
                // plan next item
                needPickUpPlan = true;
                nextItem = true;
            } 
            pickUpDone = true;
        }
        
        
    }
    
    private void dropOff() {
        // empty cargo
        currentWeightOfCargo = 0;
        
        // plan
        needPickUpPlan = true;
        nextItem = false;
    }
    
    public void cancell() {
        cancelledJobs.put(currentTask.getJobID(), true);
    }
    private void updateTasks() {
        if (tasks.isEmpty()) {
            System.exit(0);
        }
        
        if(nextItem && status != Status.DROPPING_OFF) {
            currentTask = tasks.poll();
            currentItem = currentTask.getItem();
        }
        
        while (cancelledJobs.containsKey(currentTask.getJobID())) {
            this.currentTask = tasks.poll();
            this.currentItem = currentTask.getItem();
        }
        
    }
    private void planning() {
        
        if (needPickUpPlan) {
            // plan for pick up of current item
            route = RoutePlan.plan(this, currentItem.getLocation());
        }else {
            // plan drop off for current item
            route = RoutePlan.planDropOff(this);
        }
    }

}
*/
