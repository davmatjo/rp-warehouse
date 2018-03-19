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

    @Override
    public Pose getPose() {
        return currentPose;
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
            Route route = robot.getRoute();
            boolean interpolate;

            try {
                interpolate = !(route.peek() == Protocol.WAITING
                        || route.peek() == Protocol.DROPOFF
                        || route.peek() == Protocol.PICKUP);

            } catch (NullPointerException e) {
                interpolate = true;
            }

            if (!robot.getLocation().equals(previous)) {
                previous = robot.getLocation();

                RobotLocation currentLocation = robot.getLocation();
                switch (currentLocation.getDirection()) {
                    case Protocol.NORTH:
                        currentLocation.setY(currentLocation.getY() - 1);
                        break;
                    case Protocol.EAST:
                        currentLocation.setX(currentLocation.getX() - 1);
                        break;
                    case Protocol.SOUTH:
                        currentLocation.setY(currentLocation.getY() + 1);
                        break;
                    case Protocol.WEST:
                        currentLocation.setX(currentLocation.getX() + 1);
                }

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
    }

    private void interpolate() {
        switch ((int) currentPose.getHeading()) {
            case 90:
                currentPose = new Pose(currentPose.getX() + INTERPOLATION, currentPose.getY(), currentPose.getHeading());
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
        r.sleep();
    }
}
