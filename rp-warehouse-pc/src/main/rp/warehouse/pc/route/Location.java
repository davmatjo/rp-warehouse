package rp.warehouse.pc.route;

public class Location {
    private int x;
    private int y;
    private int direction;
    private int pathCost;
    
    public Location(){}

    public Location(int x, int y, int direction) {
        this.x = x;
        this.y = y;
        this.direction = direction;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getDirection() {
        return direction;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }
    
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
    		return x + ", " + y + " - " + direction;
    }
}
