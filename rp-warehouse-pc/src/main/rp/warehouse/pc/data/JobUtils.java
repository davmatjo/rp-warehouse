package rp.warehouse.pc.data;

import java.util.ArrayList;

import rp.util.HashMap;

public class JobUtils extends Job implements Comparable<JobUtils> {

	private HashMap<String, Integer> tasks = new HashMap<String, Integer>();
	private HashMap<String, Item> itemList;
	private boolean cancelled;
	private boolean predictedCancel;
	private float value;
	
	public JobUtils(String name, HashMap<String, Item> itemList,
			int cancelled, boolean predictedCancel) {
		super(name);
		this.itemList = itemList;
		this.predictedCancel = predictedCancel;
		if (cancelled == 1) {
			this.cancelled = true;
		} else {
			this.cancelled = false;
		}
	}

	public void setPrediction(boolean prediction) {
		this.predictedCancel = prediction;
	}
	
	public void setCancelled(boolean c) {
		this.cancelled = c;
	}

	public boolean isCancelled() {
		return this.cancelled;
	}
	
	public void addTask(String item, int count) {
		if (tasks.containsKey(item)) {
			tasks.replace(item, count);
		} else {
			tasks.put(item, count);
		}
	}
	
	public HashMap<String, Integer> getTasks() {
		return tasks;
	}
	
	public float totalReward() {
		float total = 0;
		for (String i : tasks.keySet()) {
			total += itemList.get(i).getReward() * tasks.get(i);
		}
		return total;
	}

	public float totalWeight() {
		float total = 0;
		for (String i : tasks.keySet()) {
			total += itemList.get(i).getWeight() * tasks.get(i);
		}
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
		this.calcValue();
		return this.value;
	}

	public void calcValue() {
		float value = this.totalReward();
		
		this.value = value;
	}
	
	@Override
	public int compareTo(JobUtils j) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	
}
