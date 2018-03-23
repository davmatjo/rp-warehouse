package rp.warehouse.pc.data.robot.utils;

import lejos.geom.Point;
import lejos.robotics.navigation.Pose;
import rp.robotics.navigation.GridPose;
import rp.robotics.navigation.Heading;
import rp.warehouse.pc.communication.Protocol;
import rp.warehouse.pc.data.Location;
import rp.warehouse.pc.data.Warehouse;

/**
 * This is a location class that also contains a direction, it contains facilities for moving in the current direction
 * @author dxj786
 */
public class RobotLocation extends Location {
    private int direction;

    /**
     * Creates a robot location based on x, y and direction parameters
     * @param x x co-ordinate
     * @param y y co-ordinate
     * @param direction direction facing
     * @see Protocol for direction
     */
    public RobotLocation(int x, int y, int direction) {
        super(x, y);
        this.direction = direction;
    }

    /**
     * Creates a robot location from another RobotLocation
     * @param l location to copy
     */
    public RobotLocation(RobotLocation l) {
        super(l);
        this.direction = l.getDirection();
    }

    /**
     * Creates a RobotLocation from a point and a direction
     * @param point point to use
     * @param direction direction facing
     * @see Protocol for direction
     */
    public RobotLocation(Point point, int direction) {
        super(point);
        this.direction = direction;
    }

    /**
     * Moves this location forward in the direction facing
     */
    public void forward() {
        switch (direction) {
            case Protocol.NORTH:
                setY(getY() + 1);
                break;
            case Protocol.EAST:
                setX(getX() + 1);
                break;
            case Protocol.SOUTH:
                setY(getY() - 1);
                break;
            case Protocol.WEST:
                setX(getX() - 1);
        }
    }

    /**
     * Moves this location backward in the location facing
     */
    public void backward() {
        switch (direction) {
            case Protocol.NORTH:
                setY(getY() - 1);
                break;
            case Protocol.EAST:
                setX(getX() - 1);
                break;
            case Protocol.SOUTH:
                setY(getY() + 1);
                break;
            case Protocol.WEST:
                setX(getX() + 1);
        }
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    private Heading toHeading() {
        switch (direction) {
            case Protocol.NORTH:
                return Heading.PLUS_Y;
            case Protocol.EAST:
                return Heading.PLUS_X;
            case Protocol.SOUTH:
                return Heading.MINUS_Y;
            case Protocol.WEST:
                return Heading.MINUS_X;
        }
        return null;
    }

    /**
     * @return This location as a pose, normalised to the gridMap
     * @see rp.robotics.mapping.GridMap
     * @see GridPose
     */
    @Override
    public Pose toPose() {
        return Warehouse.build().toPose(new GridPose(getX(), getY(), toHeading()));
    }

    /**
     * @return This location as a point, normalised to the gridMap
     * @see rp.robotics.mapping.GridMap
     * @see Point
     */
    public Point toGridPoint() {
        return Warehouse.build().toPose(new GridPose(getX(), getY(), toHeading())).getLocation();
    }

    @Override
    public String toString() {
        return super.toString() + ", D: " + this.direction;
    }

    /**
     * Checks for equality
     * @param o An object, this can only be equal if it is a Location or RobotLocation
     * @return whether the given object is equal
     */
    @Override
    public boolean equals(Object o) {
        if (o.getClass() == Location.class) {
            return super.equals(o);
        }
        RobotLocation l = (RobotLocation) o;
        return this.getX() == l.getX() && this.getY() == l.getY() && this.getDirection() == l.getDirection();
    }
}
