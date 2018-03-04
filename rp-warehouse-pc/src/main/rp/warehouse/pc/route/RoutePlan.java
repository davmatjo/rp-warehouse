package rp.warehouse.pc.route;
import java.util.ArrayList;

public class RoutePlan {
	private Robot robot;
	//private Map map;
	private Location goalLocation;
	
	int currentX;
	int currentY;
	int currentDirection;
	
	ArrayList<Location> toVisitList = new ArrayList<Location>();
	ArrayList<Location> visitedList = new ArrayList<Location>();
	boolean goalNotFound = true;
	
	public RoutePlan(Robot robot,/*Map map,*/ Location goalLocation) {
		this.robot = robot;
		//this.map = map;
		this.goalLocation = goalLocation;
		
		Location currentRobotLocation = this.robot.getLocation();
		currentX = currentRobotLocation.getX();
		currentY = currentRobotLocation.getY();
		currentDirection = currentRobotLocation.getDirection();
		
		run();
	}
	
	public void run() {
		
		while (goalNotFound) {
			
			Location northLocation = new Location(currentX, currentY + 1, currentDirection);	
			Location eastLocation = new Location(currentX + 1, currentY, currentDirection);
			Location southLocation = new Location(currentX, currentY - 1, currentDirection);
			Location westLocation = new Location(currentX - 1, currentY, currentDirection);	
			
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
	
	public void addToList(Location location) {
	
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
		
		Location cheapestLocation = toVisitList.get(0);
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
		currentDirection = cheapestLocation.getDirection();
		
		toVisitList = new ArrayList<Location>();
	}
	
}


