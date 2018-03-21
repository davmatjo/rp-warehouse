package rp.warehouse.pc.selection;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import org.apache.log4j.Logger;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;

import rp.warehouse.pc.assignment.SimpleAssigner;
import rp.warehouse.pc.data.Task;
import rp.warehouse.pc.input.Job;

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
		logger.debug("Sorting jobs based on total reward.");
		Collections.sort(jobs, (a, b) -> (int)totalReward(b) / b.numOfTasks() - (int)totalReward(a) / a.numOfTasks());
	}
	
	public void sortByReward(ArrayList<Job> j) {
		logger.debug("Sorting jobs based on total reward.");
		Collections.sort(j, (a, b) -> (int)totalReward(b) / b.numOfTasks() - (int)totalReward(a) / a.numOfTasks());
	}

	public ArrayList<Job> sortPredicted(String pfile) {
		BufferedReader reader;
		ArrayList<Job> validJobs = new ArrayList<>(); //An ArrayList for the jobs that won't be potentially cancelled.
		ArrayList<Job> cancelledJobs = new ArrayList<>(); //An ArrayList for the jobs that will be potentially cancelled.		
		HashMap<String, Integer> predictions = new LinkedHashMap<>(); //Predictions from WEKA put into a HashMap of ids and values.
		
		try {
			reader = new BufferedReader(new FileReader(pfile));
			String line;
			logger.debug("Started reading from prediction file...");
			while ((line = reader.readLine()) != null) {
				String[] data = line.split(",");
				data[1] = data[1].substring(0, 1);
				logger.debug("Putting results from WEKA into a HashMap.");
				predictions.put(data[0], Integer.parseInt(data[1]));				
			}
			logger.debug("Splitting jobs into arrays called on cancellation...");
			int index = 0;
			for(Integer i: predictions.values()) {
				if(i == 0)
					validJobs.add(jobs.get(index));
				else 
					cancelledJobs.add(jobs.get(index));
				index++;
			}
			logger.debug("Sorting both arrays based on total reward and concatenating");
			sortByReward(validJobs);
			sortByReward(cancelledJobs);			
			validJobs.addAll(cancelledJobs);
			
			reader.close();
			return validJobs;
			
		} catch (FileNotFoundException e) {
			System.out.println("File does not exist");
			return null;
		} catch (IOException e) {
			System.out.println("IO Failed");
			return null;
		} 
	}
}
