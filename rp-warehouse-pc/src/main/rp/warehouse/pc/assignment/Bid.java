package rp.warehouse.pc.assignment;

import rp.warehouse.pc.data.Task;

public class Bid {

	private Task item;
	private int owner;
	private ItemOrder itemOrder;
	
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

}
