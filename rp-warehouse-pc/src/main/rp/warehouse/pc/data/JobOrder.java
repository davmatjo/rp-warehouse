package rp.warehouse.pc.data;

import java.util.ArrayList;
import java.util.HashMap;

public class JobOrder implements Comparable<Job> {

	private HashMap<String, Integer> tasks = new HashMap<String, Integer>();
	private float value;
	private boolean cancelled;
	private boolean predictedCancel;
	private ArrayList<Job> jobs;

	public JobOrder(HashMap<String, Integer> tasks, float value, boolean cancelled, boolean predictedCancel,
			ArrayList<Job> jobs) {
		this.tasks = tasks;
		this.value = value;
		this.cancelled = cancelled;
		this.predictedCancel = predictedCancel;
		this.jobs = jobs;
	}

	public void addPick(String item, int count) {
		if (tasks.containsKey(item)) {
			tasks.replace(item, count);
		} else {
			tasks.put(item, count);
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
		for (Integer i : tasks.values()) {
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
