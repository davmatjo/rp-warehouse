package rp.warehouse.pc.route;

import rp.warehouse.pc.data.Location;

public class RoutePlanLocation extends Location{
	
	private boolean hasBeenVisited;

	public RoutePlanLocation(int x, int y) {
		super(x, y);
		// TODO Auto-generated constructor stub
		
		hasBeenVisited = false;
		
	}
	
	private int pathCost;
	//private boolean hasBeenVisited = false;
	
	public void setPathCost(Location goalLocation) {
		int xDiff = Math.abs(goalLocation.getX() - getX());
		int yDiff = Math.abs(goalLocation.getY() - getY());
		
		int costFromOneCoordinateToOtherCoordinate = 1;
		
		pathCost = costFromOneCoordinateToOtherCoordinate + xDiff + yDiff;
		
}


	public int getPathCost() {
			return pathCost;
	}
	
	public String toString() {
			return getX() + ", " + getY();
	}


	public boolean hasBeenVisited() {
		return hasBeenVisited;
	}


	public void setHasBeenVisited(boolean hasBeenVisited) {
		this.hasBeenVisited = hasBeenVisited;
	}
	
	


}
