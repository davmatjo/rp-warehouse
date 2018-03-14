package rp.warehouse.pc.assignment;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import org.apache.log4j.Logger;

import rp.warehouse.pc.data.Job;
import rp.warehouse.pc.data.Robot;
import rp.warehouse.pc.data.Task;
import rp.warehouse.pc.data.Item;
import rp.warehouse.pc.data.Job;
import rp.warehouse.pc.route.RobotsControl;

/**
 * A simple job assigner Splits items between robots evenly
 * 
 * @author Dylan
 *
 */
public class SimpleAssigner {

	private ArrayList<Job> jobs;
	
	private static final Logger logger = Logger.getLogger(SimpleAssigner.class);

	public SimpleAssigner(ArrayList<Job> jobs) {
		this.jobs = jobs;
		assign();
	}

	public void assign() {
		ArrayList<Queue<Task>> assignedItems = new ArrayList<Queue<Task>>();
		for (int i = 0; i < 3; i++) {
			assignedItems.add(new LinkedList<Task>());
		}

		while (!jobs.isEmpty()) {

			Job job = jobs.get(0);
			jobs.remove(0);
			logger.debug("Next job");

			ArrayList<Task> unassignedItems = job.getItems();

			int i = 0;
			while (!unassignedItems.isEmpty()) {
				Task nextItem = unassignedItems.get(0);
				unassignedItems.remove(0);
				

				assignedItems.get(i).add(nextItem);
				logger.trace("Item assigned to robot " + i);

				i++;
				if (i >= assignedItems.size())
					i = 0;
			}
			logger.trace("All items from job assigned");

		}
		logger.debug("All jobs assigned");
		RobotsControl.addRobots(assignedItems);

	}
}
