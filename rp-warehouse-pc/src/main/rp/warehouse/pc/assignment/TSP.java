package rp.warehouse.pc.assignment;

import java.util.LinkedList;
import java.util.Queue;

import rp.warehouse.pc.data.Location;
import rp.warehouse.pc.data.Task;

/**
 * Just a bunch of TSP related methods
 * 
 * @author Dylan
 *
 */
public class TSP {

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
	public ItemOrder insertMinimumEdge(Task item, Queue<Task> currentPicks, Location robotLocation) {
		final LinkedList<Task> current = new LinkedList<Task>(currentPicks);
		
		if (current.isEmpty()) {
			LinkedList<Task> order = new LinkedList<Task>();
			order.add(item);
			return new ItemOrder(getTotalDistance(robotLocation, order), order);
		}

		int lowest = Integer.MAX_VALUE;
		LinkedList<Task> lowestOrder = new LinkedList<Task>();
		for (int i = 0; i < currentPicks.size() + 1; i++) {
			LinkedList<Task> newOrder = new LinkedList<Task>(current);
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
	public int getTotalDistance(Location start, LinkedList<Task> items) {
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

	/**
	 * Calculates distance between two locations
	 * 
	 * @param from
	 * @param to
	 * @return Distance between two locations
	 */
	public int getDistance(Location from, Location to) {
		return Math.abs(from.getX() - to.getX()) + Math.abs(from.getY() - to.getY());
	}
}
