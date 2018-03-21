package rp.warehouse.pc.selection;

import java.io.*;
import java.util.*;

import rp.warehouse.pc.data.Item;
import rp.warehouse.pc.data.Task;
import rp.warehouse.pc.input.Job;

public class JobReading {
	public static HashMap<String, TrainedJob> readTraining(String file, HashMap<String, Item> il) {
		
		BufferedReader reader;
		String coma = ",";
		HashMap<String, TrainedJob> jobs = new HashMap<String, TrainedJob>();

		try {
			reader = new BufferedReader(new FileReader(file));
			String line;
			while ((line = reader.readLine()) != null) {
				String[] job = line.split(coma);
				String jobID = job[0];
				TrainedJob j = new TrainedJob(jobID, il, 0);
				int taskCount = (job.length - 1)/2;
				for (int i = 0; i < taskCount; i++) {
					j.addPick(job[(2*i)+1], Integer.parseInt(job[(2*i)+2]));
				}
				jobs.put(jobID, j);
			}
			reader.close();
			return jobs;
		} catch (FileNotFoundException e) {
			System.out.println("File does not exist");
			return null;
		} catch (IOException e) {
			System.out.println("IO Failed");
			return null;
		} 
	}
	public static HashMap<String, TrainedJob> readTraining(String jobfile, String cancfile, HashMap<String, Item> il) {
		
		BufferedReader jobreader;
		BufferedReader cancreader;
		
		String coma = ",";
		HashMap<String, TrainedJob> jobs = new HashMap<String, TrainedJob>();
		
		try {
			jobreader = new BufferedReader(new FileReader(jobfile));
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
		

