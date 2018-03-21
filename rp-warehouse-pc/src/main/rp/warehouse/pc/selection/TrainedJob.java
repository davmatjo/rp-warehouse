package rp.warehouse.pc.selection;

import java.util.HashMap;
import rp.warehouse.pc.data.*;

public class TrainedJob {
	private String jobID;
	private HashMap<String, Integer> picks = new HashMap<String, Integer>();
	private HashMap<String, Item> itemList;
	private boolean cancelled;
	private boolean predictedCancel;

	public TrainedJob(String id, HashMap<String, Item> il, int cancelled) {
		this.jobID = id;
		this.itemList = il;
		if (cancelled == 1) {
			this.cancelled = true;
		} else {
			this.cancelled = false;
		}
	}

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

	public float totalReward() {
		float total = 0;
		for (String i : picks.keySet()) {
			total += itemList.get(i).getReward() * picks.get(i);
		}
		return total;
	}

	public float totalWeight() {
		float total = 0;
		for (String i : picks.keySet()) {
			total += itemList.get(i).getWeight() * picks.get(i);
		}
		return total;
	}

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
