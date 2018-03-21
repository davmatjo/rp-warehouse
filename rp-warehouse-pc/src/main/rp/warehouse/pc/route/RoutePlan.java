package rp.warehouse.pc.route;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import rp.warehouse.pc.data.Location;
import rp.warehouse.pc.data.robot.Robot;


/**
 * Used to plan the route to the goal location.
 * 
 * The route plan commands are sent to route execution as a queue of integers, where different integers represent north, east, south, and west.
 * 
 * @author alihejazi
 *
 */

public class RoutePlan {
	
	private static final Logger logger = Logger.getLogger(RoutePlan.class);
	private static final Location dropoff = new Location(4, 7);
	private static final Location dropoff2 = new Location(7, 7);
	private static List<Robot> robotsList = new ArrayList<Robot>();
	
	/**
	 * A static method returning a queue of integers, representing commands for route execution to understand
	 * @param robot the robot is passed so that we can pass it the overloaded constructor to access its current coordinates to plan its route
	 * @param goalLocation the goal location is passed so that we can plan a route to it
	 * @return we return 'plan' - a queue of integer route instructions for RouteExecution to understand
	 */
	public synchronized static Route plan(Robot robot, Location goalLocation) {
		return planRoute(robot, goalLocation, true);
	}

	private static Route planRoute(Robot robot, Location goalLocation, boolean pickup) {
		Node start = new Node(robot.getLocation().getX(), robot.getLocation().getY(), robot);
		Node end = new Node(goalLocation.getX(), goalLocation.getY(), robot);

		return start.plan(start, end, pickup);
	}
	
	/**
	 * A method to return a queue of integers (commands) on how to reach the next drop off point.
	 * @param robot The robot is passed so that we know its current location, in order to plan the route for the next drop off point
	 * @return we return 'plan' - a queue of integer route instructions for RouteExecution to understand
	 */
	public synchronized static Route planDropOff(Robot robot) {
		
		Route plan1 = planRoute(robot, dropoff, false);
		Route plan2 = planRoute(robot, dropoff2, false);
		
		return plan1.size() > plan2.size() ? plan2 : plan1;
	}
	
	/**
	 * A method to add the list of robots
	 * @param robots the list of robots to be added
	 */
	public static void setRobots(List<Robot> robots) {
		Node.setRobots(robots);
	}
	
}
