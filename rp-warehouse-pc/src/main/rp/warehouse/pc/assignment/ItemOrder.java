package rp.warehouse.pc.assignment;

import java.util.Queue;
import rp.warehouse.pc.data.Task;

/**
 * Order of items for a robot
 * 
 * @author Dylan
 *
 */
class ItemOrder {

	private int pathCost;
	private Queue<Task> order;
	
	/**
	 * @param pathCost The cost of the order
	 * @param order The order
	 */
	public ItemOrder(int pathCost, Queue<Task> order) {
		this.pathCost = pathCost;
		this.order = order;
	}

	public Queue<Task> getOrder() {
		return order;
	}

	public int getPathCost() {
		return pathCost;
	}
	
	public String toString() {
		String s = "";
		for (Task task : order) {
			s += "(" + task.getItem().getLocation().getX() + ", " + task.getItem().getLocation().getY() + ") ";		
		}
		return s;
	}

}
