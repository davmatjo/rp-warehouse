package rp.warehouse.pc.assignment;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import org.apache.log4j.Logger;

import rp.warehouse.pc.data.Item;
import rp.warehouse.pc.data.Job;
import rp.warehouse.pc.data.Location;
import rp.warehouse.pc.data.Robot;
import rp.warehouse.pc.data.Task;

/**
 * Assigns items in a job to robots through auctioning
 * 
 * Mostly coded but I got no idea if any of it works WIP
 * 
 * To do: 
 * - The 'if the current job is finished' if statement 
 * - Integration stuff
 * 
 * @author Dylan
 *
 */
public class Auctioner {

	private ArrayList<Job> jobs;
	private ArrayList<Robot> robots;
	private final ArrayList<Location> dropLocations;
	
	private static final Logger logger = Logger.getLogger(Auctioner.class);

	/**
	 * Constructor
	 * 
	 * @param jobs
	 *            List of jobs (ordered by utility)
	 * @param robots
	 *            List of robots
	 * @param dropLocations
	 *            List of the drop locations on the map
	 */
	public Auctioner(ArrayList<Job> jobs, ArrayList<Robot> robots, ArrayList<Location> dropLocations) {
		this.jobs = jobs;
		this.robots = robots;
		this.dropLocations = dropLocations;
	}

	/**
	 * Will Auction off the items in a job 
	 * Each robot bids on the item closest to their location 
	 * Winner is the bid with the lowest cost, winner is assigned their chosen item 
	 * Repeat until all the items in the job are assigned
	 */
	public void assign() {

		Job job = jobs.get(0);
		jobs.remove(0);
		logger.debug("Assigning next job");

		ArrayList<Queue<Task>> assignedItems = new ArrayList<Queue<Task>>();
		ArrayList<Task> unassignedItems = job.getItems();

		Location dropLocation = getBestDropLocation(unassignedItems);
		logger.debug("Job drop location chosen: " + dropLocation.getX() + ", " + dropLocation.getY());

		while (!unassignedItems.isEmpty()) {
			ArrayList<Bid> bids = new ArrayList<Bid>();
			for (int i = 0; i < assignedItems.size(); i++) {
				bids.add(getBid(job, assignedItems.get(i), i, dropLocation));
			}
			Bid winner = bids.get(0);
			for (Bid bid : bids) {
				if (bid.getItemOrder().getPathCost() < winner.getItemOrder().getPathCost()) {
					winner = bid;
				}
			}
			assignedItems.get(winner.getOwner()).add(winner.getItem());
			unassignedItems.remove(winner.getItem());
			logger.trace("Item assigned to robot " + robots.get(winner.getOwner()).getID());

		}
		logger.debug("All items assigned");
		// set robots off
		// also robots need to receive the drop location

	}

	/**
	 * Chooses the closest item in the job to a robot and 'bids' on it
	 * 
	 * @param job
	 *            The job
	 * @param currentItems
	 *            The items the robot has won
	 * @param robot
	 *            The robot
	 * @param dropLocation
	 *            The chosen drop location
	 * @return The robot's bid
	 */
	private Bid getBid(Job job, Queue<Task> currentItems, int robot, Location dropLocation) {
		Task bidItem = null;
		int bidVal = Integer.MAX_VALUE;
		ItemOrder bidOrder = null;

		for (Task task : job.getItems()) {
			ItemOrder newOrder = getLowestCost(task, currentItems, robots.get(robot).getLocation(), dropLocation);
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
	 * @param dropLocation
	 *            The chosen drop location
	 * @return The lowest cost order with considered item
	 */
	private ItemOrder getLowestCost(Task item, Queue<Task> currentPicks, Location robotLocation, Location dropLocation) {
		final LinkedList<Task> current = new LinkedList<Task>(currentPicks);

		int lowest = Integer.MAX_VALUE;
		LinkedList<Task> lowestOrder = current;
		for (int i = 1; i < (currentPicks.size() - 1); i++) {
			LinkedList<Task> newOrder = current;
			newOrder.add(i, item);
			int cost = getTotalDistance(robotLocation, newOrder, dropLocation);
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
	 * @param end
	 *            The end location
	 * @return The total path cost
	 */
	private int getTotalDistance(Location start, LinkedList<Task> items, Location end) {
		LinkedList<Location> locations = new LinkedList<Location>();
		locations.add(start);
		for (Task item : items) {
			locations.add(item.getItem().getLocation());
		}
		locations.add(end);

		int cost = 0;
		for (int i = 0; i < (locations.size() - 1); i++) {
			cost += getDistance(locations.get(i), locations.get(i + 1));
		}
		return cost;
	}

	/**
	 * Calculates distance between two locations
	 * 
	 * Yo this just gets the manhattan distance which is no good
	 * 
	 * @param from
	 * @param to
	 * @return
	 */
	private int getDistance(Location from, Location to) {
		return Math.abs(from.getX() - to.getX()) + Math.abs(from.getY() - to.getY());
	}

	/**
	 * Calculates the best drop off location given the position of all the items
	 * in a job
	 * 
	 * @param items
	 *            List of items in the job
	 * @return Location of the chosen drop off point
	 */
	private Location getBestDropLocation(ArrayList<Task> items) {

		Location best = null;
		int bestTotal = Integer.MAX_VALUE;
		for (Location dropLocation : dropLocations) {
			int totalDistance = 0;
			for (Task item : items) {
				Location itemLocation = item.getItem().getLocation();
				totalDistance += getDistance(itemLocation, dropLocation);
			}
			if (totalDistance < bestTotal) {
				best = dropLocation;
				bestTotal = totalDistance;
			}
		}
		return best;
	}

}
