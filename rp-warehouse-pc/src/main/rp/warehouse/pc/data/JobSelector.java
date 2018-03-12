package rp.warehouse.pc.data;

import java.util.ArrayList;
import org.apache.log4j.Logger;
import java.util.Collections;

import rp.warehouse.pc.route.*;

public class JobSelector {

	private ArrayList<Job> jobs;
	private boolean cancelled;
	private boolean predictedCancel;
	private float value;
	private final static Logger logger = Logger.getLogger(JobSelector.class);
	
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
			logger.trace("Calculating total reward of given job.");
			total = total + t.getItem().getReward() * t.getCount(); 
			logger.trace("Logic: item reward multiplied by item count.");
		}
		return total = 0;
	}
	
	
	public int totalItems(Job j) {
		int total = 0;
		for (Task t : j.getItems()) {
			logger.trace("Calculating count of items given job has.");
			total = total + t.getCount();
		}
		return total;
	}
	
	public void sortByReward() {
		logger.trace("Sorting jobs based on total reward.");
		Collections.sort(jobs, (a, b) -> (int)totalReward(b) - (int)totalReward(a));
	}
}
