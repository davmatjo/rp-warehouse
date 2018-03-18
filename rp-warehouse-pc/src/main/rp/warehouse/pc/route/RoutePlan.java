package rp.warehouse.pc.route;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

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
	private static List<Robot> robotsList = new ArrayList<Robot>();
	
	/**
	 * A static method returning a queue of integers, representing commands for route execution to understand
	 * @param robot the robot is passed so that we can pass it the overloaded constructor to access its current coordinates to plan its route
	 * @param goalLocation the goal location is passed so that we can plan a route to it
	 * @return we return 'plan' - a queue of integer route instructions for RouteExecution to understand
	 */
	public static Queue<Integer> plan(Robot robot, Location goalLocation) {
		logger.debug("Called static plan method.");
		logger.debug("Now creating a RoutePlan object.");
		
		Node start = new Node(robot.getLocation().getX(), robot.getLocation().getY());
		Node end = new Node(goalLocation.getX(), goalLocation.getY());
		
		return start.plan(start, end);
		
	}
	
	/**
	 * A method to return a queue of integers (commands) on how to reach the next drop off point.
	 * @param robot The robot is passed so that we know its current location, in order to plan the route for the next drop off point
	 * @return we return 'plan' - a queue of integer route instructions for RouteExecution to understand
	 */
	public static Queue<Integer> planDropOff(Robot robot) {
		
		Location dropOff = new Location(0, 0);
		
		return plan(robot, dropOff);
	}
	
	/**
	 * A method to add the list of robots
	 * @param robots the list of robots to be added
	 */
	public static void setRobots(List<Robot> robots) {
		robotsList = robots;
	}
	
}
