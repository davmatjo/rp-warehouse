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
public class SimpleAssigner extends Thread {

	private ArrayList<Job> jobs;
	
	public SimpleAssigner(ArrayList<Job> jobs) {
		this.jobs = jobs;
	}
	
	public void run() {
		while (true) {
			if (true) { // if all robots have finished job
				Job job = getNextJob();
				
				ArrayList<Queue<Task>> assignedItems = new ArrayList<Queue<Task>>();
				ArrayList<Task> unassignedItems = job.getItems();
				
				int i = 0;
				while (!unassignedItems.isEmpty()) {
					Task nextItem = getNextItem(unassignedItems);
					assignedItems.get(i).add(nextItem);
					
					i++;
					if (i >= assignedItems.size()) {
						i = 0;
					}
				}
				
				RobotsControl.addRobots(assignedItems);
			}
		}
		
		
	}
	
	private Task getNextItem(ArrayList<Task> unassignedItems) {
		Task next = unassignedItems.get(0);
		unassignedItems.remove(0);
		return next;
	}
	
}