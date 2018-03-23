package rp.warehouse.pc.selection;

import java.util.HashMap;
import rp.warehouse.pc.data.*;

/**
 * Used in order to match the format when creating the ARFF. Has more methods
 * and fields than the initial Job class which we decided to not polute the
 * original class with
 * 
 * @author nikollevunlieva
 *
 */

public class TrainedJob {
	private String jobID;
	private HashMap<String, Integer> picks = new HashMap<String, Integer>();
	private HashMap<String, Item> itemList;
	private boolean cancelled;

	public TrainedJob(String id, HashMap<String, Item> il, int cancelled) {
		this.jobID = id;
		this.itemList = il;
		if (cancelled == 1) {
			this.cancelled = true;
		} else {
			this.cancelled = false;
		}
	}

	/**
	 * A method that adds picks to the pick map
	 * @param item item name
	 * @param count number of items 
	 */
	
	public void addPick(String item, int count) {
		if (picks.containsKey(item)) {
			picks.replace(item, count);
		} else {
			picks.put(item, count);
		}
	}

	public HashMap<String, Integer> getPicks() {
		return picks;
	}

	public void setPicks(HashMap<String, Integer> picks) {
		this.picks = picks;
	}

	/**
	 *  A method that returns the total reward of the job extracted from the picks map
	 * @return the total reward 
	 */
	
	public float totalReward() {
		float total = 0;
		for (String i : picks.keySet()) {
			total += itemList.get(i).getReward() * picks.get(i);
		}
		return total;
	}

	/**
	 *  A method that returns the total weight of picks of the job extracted from the picks map
	 * @return the total weight
	 */
	
	public float totalWeight() {
		float total = 0;
		for (String i : picks.keySet()) {
			total += itemList.get(i).getWeight() * picks.get(i);
		}
		return total;
	}

	/**
	 *  A method that returns the total number of items of the job extracted from the picks map
	 * @return the total number of items 
	 */
	
	public int totalItems() {
		int total = 0;
		for (Integer i : picks.values()) {
			total += i;
		}
		return total;
	}

	public boolean isCancelled() {
		return this.cancelled;
	}

}
