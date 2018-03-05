package rp.warehouse.pc.assignment;

import java.util.ArrayList;
import java.util.Queue;


public class Auctioner extends Thread {

	private ArrayList<Job> jobs;
	private ArrayList<Robot> robots;
	
	public Auctioner(ArrayList<Job> jobs, ArrayList<Robot> robots) {
		this.jobs = jobs;
		this.robots = robots;
	}
	
	public void run() {
		while (true) {
			if (true) { // if all robots have finished job
				Job job = getNextJob();
				
				ArrayList<Queue<Task>> assignedItems = new ArrayList<Queue<Task>>();
				ArrayList<Task> unassignedItems = job.getItems();
				
				while (!unassignedItems.isEmpty()) {
					ArrayList<Bid> bids = new ArrayList<Bid>();
					for (int i = 0; i < assignedItems.size(); i++) {
						bids.add(getBid());
					}
					// get lowest bid
					// insert task into winner's queue in the optimal position
					// remove task from unassigned
				}
				// set robots off
			}
		}
		
	}
	
	private Bid getBid(Job job, Queue<Task> currentItems, int robot) {
		Item bidItem = null;
		int bidVal = Integer.MAX_VALUE;

		for (Task task : job.getItems()) {
		        int newCost = getLowestCost(task.item, currentItems); // add locations
			if (newCost < bidVal) {
				bidItem = task.item;
				bidVal = newCost;
			}

		}
		return new Bid(bidItem, bidVal, robot);
	}
	
	private int getLowestCost(Item item, ArrayList<Item> currentPicks, Location robotLocation, Location endLocation) {
	    // try every position of inserting the item
	    // start -> pick1 -> pick2 -> ... -> end
	    // insert in every place from after start to before end, calc cost for each
	    // thisPath = variation of list with item inserted
	    // thisCost = getTotalDistance(robotLocation, thisPath, endLocation);
	    // return lowest cost (or something else for easy insert when item is won?)
	}
	
        private int getTotalDistance(Location start, ArrayList<Item> items, Location end) {
	      
	}
	
        private Job getNextJob() {
		Job next = jobs.get(0);
		jobs.remove(0);

		return next;
		// move this to run
	}
	
}
