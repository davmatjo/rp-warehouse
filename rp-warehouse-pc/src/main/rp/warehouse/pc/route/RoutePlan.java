package rp.warehouse.pc.route;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.apache.log4j.Logger;

import rp.warehouse.pc.communication.Protocol;
import rp.warehouse.pc.data.Location;
import rp.warehouse.pc.data.Robot;
import rp.warehouse.pc.data.RobotLocation;
import rp.warehouse.pc.data.Warehouse;

/**
 * Used to plan the route to the goal location.
 * 
 * The route plan commands are sent to route execution as a queue of integers, where different integers represent north, east, south, and west.
 * 
 * @author alihejazi
 *
 */

public class RoutePlan {
	private Robot robot;
	private Location goalLocation;
	//private ArrayList<Location> blocked = Warehouse.getBlockedLocations();
	
	int currentX;
	int currentY;
	int currentDirection;
	
	ArrayList<RoutePlanLocation> toVisitList = new ArrayList<RoutePlanLocation>();
	ArrayList<Location> visitedList = new ArrayList<Location>();
	boolean goalNotFound = true;
	static Queue<Integer> plan = new LinkedList<Integer>();  
	
	/**
	 * The overloaded constructor for the RoutePlan class
	 * @param robot the robot is passed so that we can access its current coordinates to plan its route
	 * @param goalLocation the goal location is passed so that we can plan a route to it
	 */
	public RoutePlan(Robot robot, Location goalLocation) {
		this.robot = robot;
		this.goalLocation = goalLocation;
		
		Location currentRobotLocation = this.robot.getLocation();
		currentX = currentRobotLocation.getX();
		currentY = currentRobotLocation.getY();
		
		run();
	}
	
	/**
	 * A static method returning a queue of integers, representing commands for route execution to understand
	 * @param robot the robot is passed so that we can pass it the overloaded constructor to access its current coordinates to plan its route
	 * @param goalLocation the goal location is passed so that we can plan a route to it
	 * @return we return 'plan' - a queue of integer route instructions for RouteExecution to understand
	 */
	public static Queue<Integer> plan(Robot robot, Location goalLocation) {
		RoutePlan routePlan = new RoutePlan(robot, goalLocation);
		Queue<Integer> plan = RoutePlan.plan;
		RoutePlan.plan = new LinkedList<Integer>();
		return plan;
	}
	
	/**
	 * A method to return a queue of integers (commands) on how to reach the next drop off point.
	 * @param robot The robot is passed so that we know its current location, in order to plan the route for the next drop off point
	 * @return we return 'plan' - a queue of integer route instructions for RouteExecution to understand
	 */
	public static Queue<Integer> planDropOff(Robot robot) {
		return null;
	}
	
	/**
	 * A method to return the array list of visited locations
	 * @return we return the array list of visited locations
	 */
	public ArrayList<Location> getVisitedList() {
		return visitedList;
	}
	
	/**
	 * This method basically calls all the methods methods to plan all the next coordinated to visit to arrive at the goal location
	 * These locations are converted into commands that RouteExecution understands
	 */
	public void run() {
		
		while (goalNotFound) {
			
			RoutePlanLocation northLocation = new RoutePlanLocation(currentX, currentY + 1);	
			RoutePlanLocation eastLocation = new RoutePlanLocation(currentX + 1, currentY);
			RoutePlanLocation southLocation = new RoutePlanLocation(currentX, currentY - 1);
			RoutePlanLocation westLocation = new RoutePlanLocation(currentX - 1, currentY);	
			
			//now, only the VALID locations will be added to 'toVisitList'
			addToList(northLocation);
			addToList(eastLocation);
			addToList(southLocation);
			addToList(westLocation);
			
			//now, we visit the cheapest-path-cost location; we store it in 'visitedList'
			visitCheapestLocationBasedOnPathCost(currentX, currentY);			
		}
		
		//now, we're out of the while loop - the goal has been found.
		//the locations to get to the goal have now been stored in order in the 'visitedList' array list
		
	}
	
	
	/**
	 * A method to add each of the north, east, south, and west locations to the 'toVisitList' array list, if it is valid.
	 * This method also checks if the location passed is the goal location, in which case we break the loop in the run method.
	 * However, this is done after checking the validity of the location, so that it is added to the 'toVisitList' array list
	 * before we break the loop.
	 * @param location
	 */
	public void addToList(RoutePlanLocation location) {
	
		//check the location given to see if it is valid before proceeding.
		//if the location given is the goal, it is obviously valid, and we'll enter the if statement below.
		if (isValid(location)) {
			location.setPathCost(goalLocation);
			toVisitList.add(location);	
			
		}
		
		/*this goal check is made AFTER the validity check above, so that if the location sent is the goal location,
		we have it added to the toVisit array list first, and THEN we may exit. ;)*/
		if (isGoal(location)) {
			//exit the loop
			goalNotFound = false;
		}
	}
	
	/**
	 * A method to check if the location checked is valid or not. If valid, we may consider it for the next move, as we put it in the
	 * 'toVisitList' array list.
	 * @param location The location being tested on its validity.
	 * @return We return true if the location is valid, and false otherwise.
	 */
	public boolean isValid(RoutePlanLocation location) {
		
		int x = location.getX();
		int y = location.getY();
		
		final int MAX_X = 11;
		final int MAX_Y = 7;
		
		boolean locationIsBlocked = false;

		
		//PLEASE DO NOT DELETE THIS
		/*for (int i = 0; i < blocked.size(); i ++) {
			if ((blocked.get(i).getX() == x && blocked.get(i).getY() == y)) {
				locationIsBlocked = true;
			}
		}
		
		for (int i = 0; i < visitedList.size(); i++) {
			
			if (visitedList.get(i).getX() == x && visitedList.get(i).getY() == y) {
				locationIsBlocked = true;
			}
			
		}*/
		
		
		if (x >= 0
			&& y >= 0
			&& x <= MAX_X
			&& y <= MAX_Y
			&& visitedList.contains(location) == false
			&& locationIsBlocked == false)
			{	
				//might need Bluetooth to check for robots in the path when doing multi-route planning
				return true;
			}
		
		else return false;	
	}
	
	/**
	 * A method to check if the location passed is the goal location.
	 * @param location The location being checked.
	 * @return We return true if this location is the goal location, and false otherwise.
	 */
	public boolean isGoal(Location location) {
		int x = location.getX();
		int y = location.getY();
		
		int goalX = goalLocation.getX();
		int goalY = goalLocation.getY();
		//direction at goal does not matter, so I don't check it
		
		if (x == goalX && y == goalY) return true;
		
		else return false;
	}
	
	
	/**
	 * A method to move to the cheapest-cost valid location available.
	 * @param prevX This is the previous location's x coordinate
	 * @param prevY This is the previous location's y coordinate
	 */
	public void visitCheapestLocationBasedOnPathCost(int prevX, int prevY) {
		
		RoutePlanLocation cheapestLocation = toVisitList.get(0);
		int cheapestPathCost = toVisitList.get(0).getPathCost();
		
		int length = toVisitList.size();
		
		for (int i = 1; i < length; i++) {
			
			int pathCost = toVisitList.get(i).getPathCost();
			
			if (pathCost < cheapestPathCost) {
				cheapestPathCost = pathCost;
				cheapestLocation = toVisitList.get(i);
			}
			
		}
		
		System.out.println("Visited: " + cheapestLocation);
		visitedList.add(cheapestLocation);
		//cheapestLocation.setHasBeenVisited(true);
		//blocked.add(cheapestLocation);
		
		//update the location so now we're at the new location we're moving to
		currentX = cheapestLocation.getX();
		currentY = cheapestLocation.getY();
		
		
		//now, we convert the Location to a command that RouteExecution can understand
		
		if (currentX - prevX == 1) {
			plan.offer(Protocol.EAST);
		} else if (currentX - prevX == -1) {
			plan.offer(Protocol.WEST);
		} else if (currentY - prevY == 1) {
			plan.offer(Protocol.NORTH);
		} else if (currentY - prevY == -1) {
			plan.offer(Protocol.SOUTH);
		}
		
		//reset the array list
		toVisitList = new ArrayList<RoutePlanLocation>();
	}
	
}