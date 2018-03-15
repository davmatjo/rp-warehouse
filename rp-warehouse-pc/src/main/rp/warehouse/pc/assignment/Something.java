package rp.warehouse.pc.assignment;

import java.util.ArrayList;

import rp.warehouse.pc.data.Item;
import rp.warehouse.pc.data.Job;
import rp.warehouse.pc.data.Location;
import rp.warehouse.pc.data.Task;

public class Something {
	
	public static void main(String[] args) {
		Location[] itemLo = {new Location(1, 3), new Location(9, 4), new Location(5, 1), new Location(1, 1), new Location(6, 6), new Location(4, 4)};
		ArrayList<Item> items = new ArrayList<Item>();
		for (Location loc : itemLo) {
			Item item = new Item (0f, 0f);
			item.setLocation(loc);
			items.add(item);
		}
		
		ArrayList<Task> tasks1 = new ArrayList<Task>();
		ArrayList<Task> tasks2 = new ArrayList<Task>();
		tasks1.add(new Task(items.get(0), 0, ""));
		tasks1.add(new Task(items.get(1), 0, ""));
		tasks1.add(new Task(items.get(2), 0, ""));
		tasks2.add(new Task(items.get(3), 0, ""));
		tasks2.add(new Task(items.get(4), 0, ""));
		tasks2.add(new Task(items.get(5), 0, ""));

		
		Job job1 = new Job("1", tasks1);
		Job job2 = new Job("2", tasks2);
		ArrayList<Job> jobs = new ArrayList<Job>();
		jobs.add(job1);
		jobs.add(job2);
		
		ArrayList<Location> robo = new ArrayList<Location>();
		robo.add(new Location(0, 0));
		robo.add(new Location(1, 76));
		robo.add(new Location(2, 0));
		
		Auctioner a = new Auctioner(jobs, robo);
		a.assign();
		
	}
	
}
