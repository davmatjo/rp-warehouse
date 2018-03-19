package rp.warehouse.pc.management.providers;

import lejos.robotics.localization.PoseProvider;
import lejos.robotics.navigation.Pose;
import rp.util.Rate;
import rp.warehouse.pc.communication.Protocol;
import rp.warehouse.pc.data.robot.Robot;
import rp.warehouse.pc.data.robot.utils.RobotLocation;
import rp.warehouse.pc.route.Route;

public class RobotPoseProvider implements PoseProvider, Runnable {
    private Robot robot;
    private final Object lock = new Object();
    private Pose currentPose;
    private static final float INTERPOLATION = 0.005f;
    private Rate r  = new Rate(16);

    public RobotPoseProvider(Robot robot) {
        this.robot = robot;
        currentPose = robot.getLocation().toPose();
        Thread t = new Thread(this);
        t.setDaemon(true);
        t.start();
    }

    /**
     * Provides the current pose of the robot, interpolated based on time between readings
     * @return Pose of the robot, normalised for the grid
     */
    @Override
    public Pose getPose() {
        synchronized (lock) {
            return currentPose == null ? new Pose() : currentPose;
        }
    }

    @Override
    public void setPose(Pose aPose) {
        throw new RuntimeException("Cannot set pose here");
    }

    @Override
    public void run() {
        RobotLocation previous = robot.getLocation();
        int heading;
        while (true) {

            synchronized (lock) {
                Route route = robot.getRoute();
                boolean interpolate;

                // Check to see if the robot is being told to wait, dropoff or pickup
                try {
                    interpolate = !(route.peek() == Protocol.WAITING
                            || route.peek() == Protocol.DROPOFF
                            || route.peek() == Protocol.PICKUP);

                } catch (NullPointerException e) {
                    // We always interpolate if the robot has no route - this is necessary because the route is
                    // sometimes null.
                    interpolate = true;
                }

                // If the location reading has changed since the last tick, change the robot heading and pose
                if (!robot.getLocation().equals(previous)) {
                    previous = robot.getLocation();

                    RobotLocation currentLocation = robot.getLocation();
                    changeLocationReversed(currentLocation, currentLocation.getDirection());

                    currentPose = currentLocation.toPose();

                    heading = currentLocation.getDirection();

                    switch (heading) {
                        case Protocol.NORTH:
                            currentPose.setHeading(0);
                            break;
                        case Protocol.EAST:
                            currentPose.setHeading(90);
                            break;
                        case Protocol.SOUTH:
                            currentPose.setHeading(180);
                            break;
                        case Protocol.WEST:
                            currentPose.setHeading(-90);
                    }

                }
                if (interpolate) {
                    interpolate();
                } else {
                    currentPose = robot.getLocation().toPose();
                }
            }
            r.sleep();
        }
    }

    /**
     * Moves the pose by a given amount depending on the heading
     */
    private void interpolate() {
        switch ((int) currentPose.getHeading()) {
            case 90:
                if (currentPose.getX() < 85) {
                    currentPose = new Pose(currentPose.getX() + INTERPOLATION, currentPose.getY(), currentPose.getHeading());
                }
                break;
            case -90:
                currentPose = new Pose(currentPose.getX() - INTERPOLATION, currentPose.getY(), currentPose.getHeading());
                break;
            case 180:
                currentPose = new Pose(currentPose.getX(), currentPose.getY() - INTERPOLATION, currentPose.getHeading());
                break;
            case 0:
                currentPose = new Pose(currentPose.getX(), currentPose.getY() + INTERPOLATION, currentPose.getHeading());
                break;
        }
    }

    /**
     * Changes a location based on a direction, but goes back in time
     * @param location location to change
     * @param direction opposite of direction to travel
     */
    private void changeLocationReversed(RobotLocation location, int direction) {
        switch (direction) {
            case Protocol.NORTH:
                location.setY(location.getY() - 1);
                break;
            case Protocol.EAST:
                location.setX(location.getX() - 1);
                break;
            case Protocol.SOUTH:
                location.setY(location.getY() + 1);
                break;
            case Protocol.WEST:
                location.setX(location.getX() + 1);
        }
    }
}
