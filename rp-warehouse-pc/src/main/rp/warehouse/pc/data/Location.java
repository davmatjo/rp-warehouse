package rp.warehouse.pc.data;

import lejos.geom.Point;
import lejos.robotics.navigation.Pose;
import rp.robotics.navigation.GridPose;
import rp.robotics.navigation.Heading;
import rp.warehouse.pc.data.robot.utils.RobotLocation;

/**
 * Location class that stores an x and y co-ordinate
 * @author dxj786
 */
public class Location {
    private int x;
    private int y;

    /**
     * Create a location from a pair of co-ordinates
     * @param x x co-ordinate
     * @param y y co-ordinate
     */
    public Location(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Create a location that is a copy of another location
     * @param l location to copy
     */
    public Location(Location l) {
        this.x = l.x;
        this.y = l.y;
    }

    /**
     * Create a location from a Point
     * @param point point to use
     */
    public Location(Point point) {
        this.x = (int) point.x;
        this.y = (int) point.y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    /**
     * @return this location as a Point
     */
    public Point toPoint() {
        return new Point(x, y);
    }

    /**
     * @return this location as a pose with co-ordinates matching those of the GridMap
     */
    public Pose toPose() {
        return Warehouse.build().toPose(new GridPose(getX(), getY(), Heading.PLUS_Y));
    }

    @Override
    public String toString() {
        return "(" + this.x + ", " + this.y + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (o.getClass() == Location.class) {
            Location l = (Location) o;
            return this.x == l.x && this.y == l.y;
        } else if (o.getClass() == RobotLocation.class) {
            RobotLocation r = (RobotLocation) o;
            return this.x == r.getX() && this.y == r.getY();
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return x * 31 + y;
    }
}
