package rp.warehouse.pc.selection;

import rp.warehouse.pc.data.Item;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

/**
 * Used to read the jobs.csv appropriately in order to create the ARFF file.
 * 
 * There's 2 methods - one for reading the actual jobs.csv given at the
 * demonstration and one for reading the training_jobs.csv and cancellations.csv
 * and putting them together.
 * 
 * @author nikollevunlieva
 *
 */

public class JobReading {
	public static HashMap<String, TrainedJob> readTraining(String jobfile, HashMap<String, Item> il) {
		
		BufferedReader jobreader;
		String coma = ",";
		HashMap<String, TrainedJob> jobs = new HashMap<String, TrainedJob>();

		try {
			jobreader = new BufferedReader(new FileReader(jobfile));
			String line;
			while ((line = jobreader.readLine()) != null) {
				String[] job = line.split(coma);
				String jobID = job[0];
				TrainedJob j = new TrainedJob(jobID, il, 0);
				int taskCount = (job.length - 1)/2;
				for (int i = 0; i < taskCount; i++) {
					j.addPick(job[(2*i)+1], Integer.parseInt(job[(2*i)+2]));
				}
				jobs.put(jobID, j);
			}
			jobreader.close();
			return jobs;
		} catch (FileNotFoundException e) {
			System.out.println("File does not exist");
			return null;
		} catch (IOException e) {
			System.out.println("IO Failed");
			return null;
		} 
	}
	public static HashMap<String, TrainedJob> readTraining(String jfile, String cancfile, HashMap<String, Item> il) {
		
		BufferedReader jobreader;
		BufferedReader cancreader;
		
		String coma = ",";
		HashMap<String, TrainedJob> jobs = new HashMap<String, TrainedJob>();
		
		try {
			jobreader = new BufferedReader(new FileReader(jfile));
			cancreader = new BufferedReader(new FileReader(cancfile));
			
			String jobline;
			String cancline;
			
			while ((jobline = jobreader.readLine()) != null) {
				cancline = cancreader.readLine();
				String[] job = jobline.split(coma);
				int cancelled = Integer.parseInt((cancline.split(","))[1]);				
				String jobId = job[0];
				TrainedJob j = new TrainedJob(jobId, il, cancelled);
				int taskCount = (job.length - 1)/2;
				for (int i = 0; i < taskCount; i++) {
					j.addPick(job[(2*i)+1], Integer.parseInt(job[(2*i)+2]));
				}
				jobs.put(jobId, j);
			}
			jobreader.close();
			cancreader.close();
			return jobs;
			
		} 	catch (FileNotFoundException e) {
				System.out.println("File does not exist");
				return null;
			} catch (IOException e) {
				System.out.println("IO Failed");
				return null;				
			}
		}
	}
		

