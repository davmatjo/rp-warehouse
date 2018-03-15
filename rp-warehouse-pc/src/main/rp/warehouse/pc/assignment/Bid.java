package rp.warehouse.pc.assignment;

import rp.warehouse.pc.data.Task;

/**
 * A robot's bid on an item in a job
 * 
 * @author Dylan
 *
 */
class Bid {

	private Task item;
	private int owner;
	private ItemOrder itemOrder;

	/**
	 * @param item
	 *            The item the robot has chosen
	 * @param owner
	 *            The robot
	 * @param itemOrder
	 *            The resultant item order for the robot if this bid wins
	 */
	public Bid(Task item, int owner, ItemOrder itemOrder) {
		this.item = item;
		this.owner = owner;
		this.itemOrder = itemOrder;
	}

	public Task getItem() {
		return item;
	}

	public int getOwner() {
		return owner;
	}

	public ItemOrder getItemOrder() {
		return itemOrder;
	}
	
	public String toString() {
		return "Bid: "
				+ "\nItem at: " + "(" + item.getItem().getLocation().getX() + ", " + item.getItem().getLocation().getY() + ")" 
				+ "\nRobot: " + owner 
				+ "\nItem order: " + itemOrder.toString();
	}

}
