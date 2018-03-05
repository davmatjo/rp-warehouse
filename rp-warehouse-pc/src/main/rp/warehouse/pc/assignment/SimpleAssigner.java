package rp.warehouse.pc.assignment;

import java.util.ArrayList;
import java.util.Queue;
import rp.warehouse.pc.data.Robot;
import rp.warehouse.pc.data.Task;
import rp.warehouse.pc.data.Item;
import rp.warehouse.pc.route.RobotsControl;

/**
 * A simple job assigner
 * Splits items between robots evenly
 * 
 * @author Dylan
 *
 */
public class SimpleAssigner {

	private ArrayList<Job> jobs;
	
	public SimpleAssigner(ArrayList<Job> jobs) {
		this.jobs = jobs;
	}
	
	public void assign() {
	    ArrayList<Queue<Task>> assignedItems = new ArrayList<Queue<Task>>();
	    for (int i = 0; i < 3; i++){
		assignedItems.add(new Queue<Task>());
	    }

	    while (!jobs.isEmpty()) {
			
		Job job = jobs.get(0);
		jobs.remove(0);		
				
	       	ArrayList<Task> unassignedItems = job.getItems();
       		
		int i = 0;
       		while (!unassignedItems.isEmpty()) {
       		    Task nextItem = unassignedItems.get(0);
       		    unassignedItems.remove(0);

       		    assignedItems.get(i).add(nextItem);
					
       		    i++;
       		    if (i >= assignedItems.size()) {
       	       		i = 0;
       		    }
       		}
				
				
	    }
	    RobotsControl.addRobots(assignedItems);
		
	}	
}
