package rp.warehouse.pc.assignment;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import org.apache.log4j.Logger;
import rp.warehouse.pc.data.Job;
import rp.warehouse.pc.data.Location;
import rp.warehouse.pc.data.RobotLocation;
import rp.warehouse.pc.data.Task;
import rp.warehouse.pc.route.RobotsControl;

// It all works I think
// This is the big boy you want to make to replace SimpleAssigner
/**
 * Assigns items in a job to robots through auctioning
 * 
 * @author Dylan
 *
 */
public class Auctioner {

	private ArrayList<Job> jobs;
	private ArrayList<RobotLocation> robots;

	private static final Logger logger = Logger.getLogger(Auctioner.class);

	/**
	 * @param jobs
	 *            List of ordered jobs
	 * @param robots
	 *            List of robot locations
	 */
	public Auctioner(ArrayList<Job> jobs, ArrayList<RobotLocation> robots) {
		this.jobs = jobs;
		this.robots = robots;
	}

	/**
	 * Will Auction off the items in a job. Each robot bids on the item closest to
	 * their location. Winner is the bid with the lowest cost, winner is assigned
	 * their chosen item. Repeat until all the items in the job are assigned
	 */
	public void assign() {
		ArrayList<Queue<Task>> assignedItems = new ArrayList<Queue<Task>>();
		for (int i = 0; i < robots.size(); i++) {
			assignedItems.add(new LinkedList<Task>());
		}
		
		while (!jobs.isEmpty()) {
			Job job = jobs.get(0);
			jobs.remove(0);
			logger.debug("Assigning next job");
			
			ArrayList<Task> unassignedItems = job.getItems();

			while (!unassignedItems.isEmpty()) {
				ArrayList<Bid> bids = new ArrayList<Bid>();
				for (int i = 0; i < assignedItems.size(); i++) {
					bids.add(getBid(job, assignedItems.get(i), i));
				}
				
				Bid winner = bids.get(0);
				for (Bid bid : bids) {
					if (bid.getItemOrder().getPathCost() < winner.getItemOrder().getPathCost()) {
						winner = bid;
					}
				}
				
				assignedItems.get(winner.getOwner()).add(winner.getItem());
				unassignedItems.remove(winner.getItem());
				logger.trace("Item assigned to robot " + robots.get(winner.getOwner()));
			}
			logger.trace("All items assigned");
		}
		logger.debug("All jobs assigned");
		
		RobotsControl.addRobots(assignedItems);

	}

	/**
	 * Chooses the closest item in the job to a robot and 'bids' on it
	 * 
	 * @param job
	 *            The job
	 * @param currentItems
	 *            The items the robot has won
	 * @param robot
	 *            The robot bidding
	 * @return The robot's bid
	 */
	private Bid getBid(Job job, Queue<Task> currentItems, int robot) {
		Task bidItem = null;
		int bidVal = Integer.MAX_VALUE;
		ItemOrder bidOrder = null;

		for (Task task : job.getItems()) {
			ItemOrder newOrder = getLowestCost(task, currentItems, robots.get(robot));
			int newCost = newOrder.getPathCost();
			if (newCost < bidVal) {
				bidItem = task;
				bidVal = newCost;
				bidOrder = newOrder;
			}
		}
		return new Bid(bidItem, robot, bidOrder);
	}

	/**
	 * Gets the item order which gives the lowest cost
	 * 
	 * @param item
	 *            The item being considered
	 * @param currentPicks
	 *            The items the robot has won
	 * @param robotLocation
	 *            The robot's current location
	 * @return The lowest cost order with considered item
	 */
	private ItemOrder getLowestCost(Task item, Queue<Task> currentPicks, Location robotLocation) {
		final LinkedList<Task> current = new LinkedList<Task>(currentPicks);

		int lowest = Integer.MAX_VALUE;
		LinkedList<Task> lowestOrder = current;
		for (int i = 1; i < (currentPicks.size() - 1); i++) {
			LinkedList<Task> newOrder = current;
			newOrder.add(i, item);
			int cost = getTotalDistance(robotLocation, newOrder);
			if (cost < lowest) {
				lowest = cost;
				lowestOrder = newOrder;
			}
		}
		return new ItemOrder(lowest, lowestOrder);
	}

	/**
	 * Calculates the total path cost given an order of items
	 * 
	 * @param start
	 *            The start location
	 * @param items
	 *            The items (in order of visiting)
	 * @return The total path cost
	 */
	private int getTotalDistance(Location start, LinkedList<Task> items) {
		LinkedList<Location> locations = new LinkedList<Location>();
		locations.add(start);
		for (Task item : items) {
			locations.add(item.getItem().getLocation());
		}

		int cost = 0;
		for (int i = 0; i < (locations.size() - 1); i++) {
			cost += getDistance(locations.get(i), locations.get(i + 1));
		}
		return cost;
	}
	
	// no
	/**
	 * Calculates manhattan distance between two locations
	 * 
	 * @param from
	 * @param to
	 * @return Distance between two locations
	 */
	private int getDistance(Location from, Location to) {
		return Math.abs(from.getX() - to.getX()) + Math.abs(from.getY() - to.getY());
	}

}
