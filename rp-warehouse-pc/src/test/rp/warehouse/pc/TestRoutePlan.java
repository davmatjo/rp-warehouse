package rp.warehouse.pc;

import static org.junit.Assert.*;


import org.junit.Test;

import rp.warehouse.pc.route.Location;
import rp.warehouse.pc.route.Robot;
import rp.warehouse.pc.route.RoutePlan;

public class TestRoutePlan {

	@Test
	public void test() {
		Robot robot = new Robot("Ali");
		Location goal = new Location(4,6,1);
		RoutePlan rt = new RoutePlan(robot, goal);
		
		//not a proper test lol, but still shows how my code works
	}

}
