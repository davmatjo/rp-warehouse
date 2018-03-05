package rp.warehouse.pc.assignment;

public class Bid {

	private Item item;
	private int value;
	private int owner;
	
	public Bid(Item item, int value, int owner) {
		this.item = pick;
		this.value = value;
		this.owner = owner;
	}
	
	public Item getItem() {
		return item;
	}
	
	public int getValue() {
		return value;
	}
	
	public int getOwner() {
		return owner;
	}

}
