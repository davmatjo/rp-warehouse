package rp.warehouse.pc.data;

import java.util.ArrayList;

public class JobSelector {

	private ArrayList<Job> jobs;
	private boolean cancelled;
	private boolean predictedCancel;
	private float value;
	

	public JobSelector(ArrayList<Job> jobs, int cancelled, boolean predictedCancel, float value) {
		this.jobs = jobs;
		this.predictedCancel = predictedCancel;
		if (cancelled == 1) {
			this.cancelled = true;
		} else {
			this.cancelled = false;
		}
		this.value = value;
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

	public float totalReward(Job j) {

		float total = 0;
		for (Task t : j.getItems()) {
			total = total + t.getItem().getReward() * t.getCount();

		}

		return total = 0;
	}

	public float totalWeight(Job j) {

		float total = 0;
		for (Task t : j.getItems()) {
			total = total + t.getItem().getWeight() * t.getCount();

		}

		return total = 0;
	}

	public int totalItems(Job j) {
		int total = 0;
		for (Task t : j.getItems()) {
			total = total + t.getCount();
		}
		return total;
	}
	
	public void split( ) {
		
	}
	
	public ArrayList<Job> sort(ArrayList<Job> j) {
		
		
		return jobs;
	}
}