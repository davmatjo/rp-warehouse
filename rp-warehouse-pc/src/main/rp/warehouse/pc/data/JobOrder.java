package rp.warehouse.pc.data;

import java.util.HashMap;

public class JobOrder implements Comparable<Job> {

	private HashMap<String, Integer> picks = new HashMap<String, Integer>();
	private float value;

	public void addPick(String item, int count) {
		if (picks.containsKey(item)) {
			picks.replace(item, count);
		} else {
			picks.put(item, count);
		}
	}

	public float totalReward() {
		float total = 0;
		return total;
	}

	public float totalWeight() {
		float total = 0;
		return total;
	}

	public int totalItems() {
		int total = 0;
		for (Integer i : picks.values()) {
			total += i;
		}
		return total;
	}
	
	public float getValue() {
		return value;
	}

	public void setValue(float value) {
		this.value = value;
	}

	@Override
	public int compareTo(Job j) {
		// TODO Auto-generated method stub
		return 0;
	}

}
