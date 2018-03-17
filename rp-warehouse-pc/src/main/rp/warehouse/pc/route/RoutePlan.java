package rp.warehouse.pc.route;
import java.util.*;

import org.apache.log4j.Logger;

import rp.warehouse.pc.communication.Protocol;
import rp.warehouse.pc.data.Location;
import rp.warehouse.pc.data.Warehouse;
import rp.warehouse.pc.data.robot.Robot;
import rp.warehouse.pc.data.robot.RobotLocation;

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
	private static final Logger logger = Logger.getLogger(RoutePlan.class);
	private static List<Robot> robotsList = new ArrayList<Robot>();
	private HashSet<Location> blocked = new HashSet<>(Warehouse.getBlockedLocations());
	
	int currentX;
	int currentY;
	int currentDirection;
	
	ArrayList<RoutePlanLocation> toVisitList = new ArrayList<RoutePlanLocation>();
	ArrayList<Location> visitedList = new ArrayList<Location>();
	boolean goalNotFound = true;
	static Queue<Integer> plan = new LinkedList<Integer>();
	final Location originalLocation;
	private HashSet<Location> tempBlocked;
	
	/**
	 * The overloaded constructor for the RoutePlan class
	 * @param robot the robot is passed so that we can access its current coordinates to plan its route
	 * @param goalLocation the goal location is passed so that we can plan a route to it
	 */
	public RoutePlan(Robot robot, Location goalLocation) {
		logger.debug("Entered RoutePlan overloaded constructor.");
		this.robot = robot;
		this.goalLocation = goalLocation;
		
		originalLocation = this.robot.getLocation();
		currentX = originalLocation.getX();
		currentY = originalLocation.getY();
		logger.debug("Calling run method.");
		run();
	}
	
	/**
	 * A static method returning a queue of integers, representing commands for route execution to understand
	 * @param robot the robot is passed so that we can pass it the overloaded constructor to access its current coordinates to plan its route
	 * @param goalLocation the goal location is passed so that we can plan a route to it
	 * @return we return 'plan' - a queue of integer route instructions for RouteExecution to understand
	 */
	public static Queue<Integer> plan(Robot robot, Location goalLocation) {
		logger.debug("Called static plan method.");
		logger.debug("Now creating a RoutePlan object.");
		RoutePlan routePlan = new RoutePlan(robot, goalLocation);
		LinkedList<Integer> plan = new LinkedList<>(RoutePlan.plan);
		
		//reset the route plan for the next route
		RoutePlan.plan = new LinkedList<Integer>();
		List<Integer> cutoff;
		if (plan.size() > 4) {
			cutoff = plan.subList(0, 4);
		} else {
			cutoff = plan;
		}
		//return the route plan
		return new LinkedList<>(cutoff);
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
		logger.debug("Entered run method.");
		
		//Check the original location to see if we're already at the goal, before checking any other locations.
		if (isGoal(originalLocation)) {
			plan.offer(Protocol.PICKUP);
		}
		
		else {
			int tick = 0;
			while (goalNotFound) {

				List<Robot> others = new ArrayList<>(robotsList);
				others.remove(robot);
				tempBlocked = getTempBlockedLocations(others, tick);

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

				tick++;
			}
		}
		
		
		logger.debug("Goal location found. 'plan' is now complete with all commands.");
		
		//now, we're out of the while loop - the goal has been found.
		//the locations to get to the goal have now been stored in order in the 'visitedList' array list
		
	}
	
	
	/**
	 * A method to add the north, east, south, and west locations to the 'toVisitList' array list, if they are valid.
	 * This method also checks if the location passed is the goal location, in which case we break the loop in the run method.
	 * However, this is done after checking the validity of the location, so that it is added to the 'toVisitList' array list
	 * before we break the loop.
	 * @param location
	 */
	public void addToList(RoutePlanLocation location) {
		
		String loc = location.toString();
		logger.trace("Checking the validity of " + loc);
		
		//check the location given to see if it is valid before proceeding.
		//if the location given is the goal, it is obviously valid, and we'll enter the if statement below.
		if (isValid(location)) {
			logger.trace(loc + " is valid. Its path cost is " + location.getPathCost() + ".");
			location.setPathCost(goalLocation);
			toVisitList.add(location);	
			logger.trace("Added " + loc + " to 'toVisitList'.");
			
		}
		
		/*this goal check is made AFTER the validity check above, so that if the location sent is the goal location,
		we have it added to the toVisit array list first, and THEN we may exit. ;)*/
		if (isGoal(location)) {
			logger.trace(loc + " is the goal. Goal found.");
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
		
		boolean locationIsBlocked = blocked.contains(location) || tempBlocked.contains(location);

		
		//PLEASE DO NOT DELETE THIS
		/*for (int i = 0; i < blocked.size(); i ++) {
			if ((blocked.get(i).getX() == x && blocked.get(i).getY() == y)) {
				locationIsBlocked = true;
			}
		}
		
		for (int i = 0; i < visitedList.size(); i++) {
			startingLocation
			if (visitedList.get(i).getX() == x && visitedList.get(i).getY() == y) {
				locationIsBlocked = true;
			}
			
		}*/
		
		
		return x >= 0
				&& y >= 0
				&& x <= MAX_X
				&& y <= MAX_Y
				&& !visitedList.contains(location)
				&& !locationIsBlocked;
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
		
		logger.debug("Visited: " + cheapestLocation);
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

	private static HashSet<Location> getTempBlockedLocations(List<Robot> robots, int tick) {
		HashSet<Location> blocked = new HashSet<>();
		int prevTick = tick - 1;
		int nextTick = tick + 1;

		for (Robot robot : robots) {
			logger.debug("New robot blocked locations");
			LinkedList<Integer> directions = (LinkedList) robot.getRoute();

			int currX = robot.getLocation().getX();
			int currY = robot.getLocation().getY();

			for (int i=0; i<=nextTick; i++) {
				try {
					if (directions.get(i) == Protocol.NORTH) {
						currY += 1;
					} else if (directions.get(i) == Protocol.EAST) {
						currX += 1;
					} else if (directions.get(i) == Protocol.SOUTH) {
						currY -= 1;
					} else if (directions.get(i) == Protocol.WEST) {
						currX -= 1;
					}

					if (i == tick || i == prevTick || i == nextTick) {
						blocked.add(new Location(currX, currY));
					}
				} catch (IndexOutOfBoundsException e) {
					logger.debug("ignoring blocked location");
				}
				if (i == tick || i == prevTick || i == nextTick) {
					blocked.add(new Location(currX, currY));
				}
			}
		}
		return blocked;
	}
	
}