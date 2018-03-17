package rp.warehouse.pc.data.robot;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ExecutorService;

import rp.warehouse.pc.communication.Communication;
import rp.warehouse.pc.data.Item;
import rp.warehouse.pc.data.Task;

public abstract class AbstractRobotData implements Runnable {
    
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
  private int RATE = 20;
    
    public AbstractRobotData(String ID, String name, Queue<Task> newTasks, ExecutorService pool, RobotLocation startingLocation) {
        this.ID = ID;
        this.name = name;
        this.tasks = newTasks;
        cancelledJobs = new HashMap<String, Boolean>();
        // TODO Auto-generated constructor stub
    }
    
    /**
     * For Warehouse MI
     * 
     * @return Queue<Integer> of directions
     */
    public LinkedList<Integer> getRoute() {
        LinkedList<Integer> copy = (LinkedList<Integer>) route.clone();
        return copy;
    }

    public String getID() {
        String copy = ID;
        return copy;
    }

    public Task getTask() {
        Task copy = currentTask;
        return copy;
    }

    public RobotLocation getLocation() {
        RobotLocation copy = location;
        return copy;
    }

    protected String getDirectionString(int direction) {
        // Works out the String representation of the command
        return (direction <= 4 ? (direction == 3 ? "North" : "East") : (direction == 5 ? "South" : "West"));
    }
    
    protected void updateLocation() {
        //robotUtils.updateLocation(lastInstruction);
    }
    


}
