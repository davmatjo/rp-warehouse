package rp.warehouse.pc.data;

import java.util.ArrayList;
import java.util.Collections;

import rp.util.Comparator;
import rp.warehouse.pc.route.*;

public class JobSelector implements Comparator<Job> {

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

	//A method for calculating the overall reward for a given Job j. 
	public float totalReward(Job j) {
		float total = 0;
		for (Task t : j.getItems()) {
			total = total + t.getItem().getReward() * t.getCount(); //Gets each item's reward and multiplies it by the item's count.
																	//All the total methods follow a similar logic.
		}
		return total = 0;
	}
	
	
	//A method for counting how many items overall a job j needs to get.
	public int totalItems(Job j) {
		int total = 0;
		for (Task t : j.getItems()) {
			total = total + t.getCount();
		}
		return total;
	}
	
	// The method that sorts the ArrayList of Jobs based on their total reward (for now). Will be extended to reward per coordinate.
	public void sort() {
		
		
		
	}

	@Override
	public int compare(Job a, Job b) {
		if(totalReward(a) < totalReward(b))
			return -1;
		else if(totalReward(a) == totalReward(b))
				return 0;
		else return 1;
	}

	
}
