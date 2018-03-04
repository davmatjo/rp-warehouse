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
				}
			}
		}
		
		
	}
	
	private Bid getBid(Job job, Queue<Task> currentItems, int robot) {
		Item bidItem = null;
		int bidVal = Integer.MAX_VALUE;

		for (Task task : job.getItems()) {
			int newCost = getCost(task.item, currentItems);
			if (newCost < bidVal) {
				bidItem = task.item;
				bidVal = newCost;
			}

		}
		return new Bid(bidItem, bidVal, robot);
	}
	
	private int getCost(Item item, ArrayList<Item> currentPicks, Location robotLocation, Location endLocation) {
		
	}
	
	private int getDistance() {
		
	}
	
	private Job getNextJob() {
		Job next = jobs.get(0);
		jobs.remove(0);

		return next;
	}
	
}
