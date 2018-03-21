package rp.warehouse.pc.route;

import rp.warehouse.pc.data.Location;
import rp.warehouse.pc.data.Warehouse;

import java.util.HashSet;

/**
 * Used to create a RoutePlanLocation, which extends a generic Location from rp.warehouse.pc.data.Location
 * <p>
 * The only difference between RoutePlanLocation and Location is that RoutePlanLocation has a path cost, which is used for A* search.
 *
 * @author alihejazi
 */
public class RoutePlanLocation extends Location {

    //private boolean hasBeenVisited;

    private int pathCost;
    //private boolean hasBeenVisited = false;
    private HashSet<Location> blocked = new HashSet<>(Warehouse.getBlockedLocations());
    /**
     * The overloaded constructor to create a RoutePlanLocation, which extends Location in rp.warehouse.pc.data.Location
     *
     * @param x the x coordinate of the RoutePlanLocation
     * @param y the y coordinate of the RoutePlanLocation
     */
    public RoutePlanLocation(int x, int y) {
        super(x, y);
        //hasBeenVisited = false;
    }

    /**
     * the getter method for the path cost variable
     *
     * @return we return the path cost
     */
    public int getPathCost() {
        return pathCost;
    }

    /**
     * A method to set the path cost for this RoutePlanLocation, based on the estimated number of steps we assume it requires to reach the goal if we go to this location.
     *
     * @param goalLocation the goal location that we want to plan a route to/
     */
    public void setPathCost(Location goalLocation) {

        int goalX = goalLocation.getX();
        int goalY = goalLocation.getY();

		/*for (int i = getX(); i < goalX; i++) {
			//check if any of the xCoordinates are blocked
			RoutePlanLocation loc = new RoutePlanLocation(i, getY());

			if (blocked.contains(loc)) {

			}
		}*/


        int xDiff = Math.abs(goalLocation.getX() - getX());
        int yDiff = Math.abs(goalLocation.getY() - getY());

        int costFromOneCoordinateToOtherCoordinate = 1;

        pathCost = costFromOneCoordinateToOtherCoordinate + xDiff + yDiff;

    }

    /**
     * The overloaded toString() method, which we use to print out the coordinates when testing.
     */
    public String toString() {
        return getX() + ", " + getY();
    }

	/*public boolean hasBeenVisited() {
		return hasBeenVisited;
	}

	public void setHasBeenVisited(boolean hasBeenVisited) {
		this.hasBeenVisited = hasBeenVisited;
	}*/


}
