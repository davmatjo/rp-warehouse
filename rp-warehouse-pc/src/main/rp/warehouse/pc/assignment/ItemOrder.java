package rp.warehouse.pc.assignment;

import java.util.Queue;
import rp.warehouse.pc.data.Task;

public class ItemOrder {

	private int pathCost;
	private Queue<Task> order;

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

}
