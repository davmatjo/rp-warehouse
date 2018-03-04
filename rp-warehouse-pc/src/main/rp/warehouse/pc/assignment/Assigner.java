package rp.warehouse.pc.assignment;

import java.util.ArrayList;
import rp.warehouse.pc.data.*;

public class Assigner {
	private ArrayList<Job> jobs;
	private Robot[] robots;

	public Assigner(ArrayList<Job> jobs, Robot[] robots) {
		this.jobs = jobs;
		this.robots = robots;
	}

	public Job getJob(Robot robot) {
		Job nextJob = getNextJob();
		nextJob = orderPicks(nextJob, robot);
		return nextJob;
	}

	// TSP return picks in order of pickup
	private Job orderPicks(Job job, Robot robot) {
		// Location[] pickLocations = job.getPickLocations();
		// Location robotLocation = robot.getLocation();
		//
		// int[] startToPicks = getDistanceArray(robotLocation, pickLocations,
		// job.getNumberOfPicks());
		// int[] picksToEnd = getDistanceArray(endLocation, pickLocations,
		// job.getNumberOfPicks());
		// int[][] picksToPicks = new int[job.getNumberOfPicks()][];
		// for (int i = 0; i < job.getNumberOfPicks(); i++) {
		// picksToPicks[i] = getDistanceArray(pickLocations[i], pickLocations,
		// job.getNumberOfPicks());
		// }

		ArrayList<Item> picks = job.getPicks();
		ArrayList<Item> reserve = new ArrayList<Item>();

		Item closestToRobot = getClosest(robot.getLocation(), picks);
		picks.remove(closestToRobot);
		reserve.add(closestToRobot);

		while (!picks.isEmpty()) {
			Item closestToPick = getClosest(reserve, picks);
			picks.remove(closestToPick);
			reserve = insertMinimumEdge(reserve, closestToPick);
		}

		job.setPicks(reserve);
		return job;
	}

	private int getDistance(Location from, Location to) {
		return 0;
	}

	private Job getNextJob() {
		Job next = jobs.get(0);
		jobs.remove(0);

		return next;
	}
	
	private ArrayList<Item> insertMinimumEdge(ArrayList<Item> reserve, Item pick) {
		
	}

	private Item getClosest(Location from, ArrayList<Item> picks) {
		Item closest = picks.get(0);
		for (int i = 0; i < picks.size(); i++) {
			if (getDistance(from, picks.get(i).getLocation()) < getDistance(from, closest)) {
				closest = picks.get(i);
			}
		}
		return closest;
	}
	
	private Item getClosest(ArrayList<Item> reserve, ArrayList<Item> picks) {
		Item closest = picks.get(0);
		int closestDistance = Integer.MAX_VALUE;
		
		for (int h = 0; h < reserve.size(); h++) {
			for (int i = 0; i < picks.size(); i++) {
				if (getDistance(reserve.get(h).getLocation(), picks.get(i).getLocation()) < closestDistance) {
					closest = picks.get(i);
					closestDistance = getDistance(reserve.get(h).getLocation(), picks.get(i).getLocation());
				}
			} 
		}
		return closest;
	}

	// private Item getClosest(int[] from, ArrayList<Item> picks) {
	// int closest = 0;
	// for (int i = 0; i < picks.size(); i++) {
	// if (from[i] < from[closest]) {
	// closest = i;
	// }
	// }
	// return picks.get(closest);
	// }

	// private int[] getDistanceArray(Location from, Location[] to, int size) {
	// int[] result = new int[size];
	//
	// for (int i = 0; i < size; i++) {
	// result[i] = getDistance(from, to[i]);
	// }
	//
	// return result;
	// }
}
