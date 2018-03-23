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

/**
 * The class that holds all the methods that do the sorting of the job
 * arraylist.
 * 
 * @author nikollevunlieva
 *
 */

public class JobSelector {

	private ArrayList<Job> jobs;
	private boolean cancelled;
	private final static Logger logger = Logger.getLogger(JobSelector.class);
	
	public JobSelector(ArrayList<Job> jobs, int cancelled, boolean predictedCancel, float value) {
		this.jobs = jobs;
	
	}

	public boolean isCancelled() {
		return this.cancelled;
	}
	
	/**
	 * A method to calculate the total reward of a given job.
	 * 
	 * @param j
	 *            job object
	 * 
	 * @return total reward
	 */

	public float totalReward(Job j) {
		float total = 0;
		for (Task t : j.getItems()) {
			logger.trace("Calculating total reward of given job.");
			total = total + t.getItem().getReward() * t.getCount(); 
			logger.trace("Logic: item reward multiplied by item count.");
		}
		return total = 0;
	}
	
	/**
	 * A method to calculate the total number of items a job has
	 * 
	 * @param j
	 *            job object
	 * @return number of items
	 */
	
	public int totalItems(Job j) {
		int total = 0;
		for (Task t : j.getItems()) {
			logger.trace("Calculating count of items given job has.");
			total = total + t.getCount();
		}
		return total;
	}
	
	/**
	 * A method that sorts the arraylist based on total reward divided by number of
	 * tasks
	 */
	
	public void sortByReward() {
		logger.debug("Sorting jobs based on total reward.");
		Collections.sort(jobs, (a, b) -> (int)totalReward(b) / b.numOfTasks() - (int)totalReward(a) / a.numOfTasks());
	}
	
	/**
	 * A method that sorts an arraylist of jobs based on total reward but if 2 jobs
	 * have the same reward the lighter one takes priority over the heavier
	 * 
	 * @param j
	 *            any arraylist of jobs
	 */
	
	public void sortByReward(ArrayList<Job> j) {
		logger.debug("Sorting jobs based on total reward.");
		Collections.sort(j, (a, b) -> (int)totalReward(b) / b.numOfTasks() - (int)totalReward(a) / a.numOfTasks());
	}

	/**
	 * 
	 * @param pfile the predictions file produced by WEKA
	 * @return sorted arraylist of jobs where the potentially cancelled are at the bottom
	 */
	
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
			sortByReward(jobs);
			sortByReward(cancelledJobs);
			jobs.addAll(cancelledJobs);
			
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
