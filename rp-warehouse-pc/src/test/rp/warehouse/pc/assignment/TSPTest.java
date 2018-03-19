package rp.warehouse.pc.assignment;

import org.junit.Test;

import rp.warehouse.pc.data.Item;
import rp.warehouse.pc.data.Location;
import rp.warehouse.pc.data.Task;

import java.util.LinkedList;

import org.junit.Assert;
import org.junit.Before;;

public class TSPTest {
	private static final TSP tsp = new TSP(); 
	
	private Location start;
	private LinkedList<Task> testItems;
	
	@Before
	public void setup() {
		start = new Location(0, 0);
		testItems = new LinkedList<Task>();
		testItems.add(new Task(new Item("", 0f, 0f) {{setLocation(new Location(2, 2));}}, 0, ""));
		testItems.add(new Task(new Item("", 0f, 0f) {{setLocation(new Location(0, 3));}}, 0, ""));
		testItems.add(new Task(new Item("", 0f, 0f) {{setLocation(new Location(1, 0));}}, 0, ""));
		testItems.add(new Task(new Item("", 0f, 0f) {{setLocation(new Location(4, 4));}}, 0, ""));
	}

	@Test
	public void returnsCorrectDistance() {
		Location l1 = new Location(0, 0);
		Location l2 = new Location(3, 4);
		Assert.assertEquals(7, tsp.getDistance(l1, l2));
	}
	
	@Test
	public void returnsCorrectTotalDistance() {
		Assert.assertEquals(18, tsp.getTotalDistance(start, testItems));
	}
	
	@Test
	public void insertsAtMinimumEdge() {
		LinkedList<Task> actual = new LinkedList<Task>();
		actual = (LinkedList<Task>) tsp.insertMinimumEdge(testItems.get(0), actual, start).getOrder();
		actual = (LinkedList<Task>) tsp.insertMinimumEdge(testItems.get(1), actual, start).getOrder();
		actual = (LinkedList<Task>) tsp.insertMinimumEdge(testItems.get(2), actual, start).getOrder();
		actual = (LinkedList<Task>) tsp.insertMinimumEdge(testItems.get(3), actual, start).getOrder();
		
		LinkedList<Task> correct = new LinkedList<Task>();
		correct.add(testItems.get(2));
		correct.add(testItems.get(1));
		correct.add(testItems.get(0));
		correct.add(testItems.get(3));
		
		Assert.assertEquals(correct, actual);
	}
}
