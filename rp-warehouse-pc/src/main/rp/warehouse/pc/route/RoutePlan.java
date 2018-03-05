package rp.warehouse.pc.route;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import rp.warehouse.pc.communication.Protocol;
import rp.warehouse.pc.data.Location;
import rp.warehouse.pc.data.Robot;

public class RoutePlan {
	private Robot robot;
	//private Map map;
	private Location goalLocation;
	
	int currentX;
	int currentY;
	int currentDirection;
	
	ArrayList<RoutePlanLocation> toVisitList = new ArrayList<RoutePlanLocation>();
	ArrayList<Location> visitedList = new ArrayList<Location>();
	boolean goalNotFound = true;

	
	public RoutePlan(Robot robot,/*Map map,*/ Location goalLocation) {
		this.robot = robot;
		//this.map = map;
		this.goalLocation = goalLocation;
		
		Location currentRobotLocation = this.robot.getLocation();
		currentX = currentRobotLocation.getX();
		currentY = currentRobotLocation.getY();
		//currentDirection = currentRobotLocation.getDirection();
		
		run();
	}
	
	public static Queue<Integer> plan(Robot robot, Location goalLocation) {
		RoutePlan routePlan = new RoutePlan(robot, goalLocation);
		List<Location> locations = routePlan.getVisitedList();
		int prevX = locations.get(0).getX();
		int prevY = locations.get(0).getY();
		locations.remove(0);
		
		Queue<Integer> plan = new LinkedList<Integer>();
		for (Location l : locations) {
			int currX = l.getX();
			int currY = l.getY();
			
			if (currX - prevX == 1) {
				plan.offer(Protocol.EAST);
			} else if (currX - prevX == -1) {
				plan.offer(Protocol.WEST);
			} else if (currY - prevY == 1) {
				plan.offer(Protocol.NORTH);
			} else if (currY - prevY == -1) {
				plan.offer(Protocol.SOUTH);
			}
			
			prevX = currX;
			prevY = currY;
		}
		return plan;
	}
	
	public static Queue<Integer> planDropOff(Robot robot) {
		return null;
	}
	
	public ArrayList<Location> getVisitedList() {
		return visitedList;
	}
	
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
			visitCheapestLocationBasedOnPathCost();			
		}
		
		//now, we're out of the while loop - the goal has been found.
		//the locations to get to the goal are now stored in order in the 'visitedList' array list
		
		//now, we need to convert the list of Locations to the goal to commands that RouteExecution can understand.
		
		//send the commandsList to RouteExecution.
	}
	
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
	
	public boolean isValid(Location location) {
		
		int x = location.getX();
		int y = location.getY();
		
		//assuming the grid size
		final int MAX_X = 6;
		final int MAX_Y = 6;

		
		if (x >= 0 && y >= 0 && x <= MAX_X && y <= MAX_Y && visitedList.contains(location) == false) {
			
			//now check for obstacles (can't do now because I don't have the map)
			//might even need Bluetooth to check for robots in the path
			
			return true;
		}
		
		else return false;	
	}
	
	public boolean isGoal(Location location) {
		int x = location.getX();
		int y = location.getY();
		
		int goalX = goalLocation.getX();
		int goalY = goalLocation.getY();
		//direction at goal does not matter, so I don't check it
		
		if (x == goalX && y == goalY) return true;
		
		else return false;
	}
	
	public void visitCheapestLocationBasedOnPathCost() {
		
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
		
		//update the location so now we're at the new location we're moving to
		currentX = cheapestLocation.getX();
		currentY = cheapestLocation.getY();
		//currentDirection = cheapestLocation.getDirection();
		
		toVisitList = new ArrayList<RoutePlanLocation>();
	}
	
}


