package rp.warehouse.pc.management.providers;

import lejos.robotics.localization.PoseProvider;
import lejos.robotics.navigation.Pose;
import rp.util.Rate;
import rp.warehouse.pc.communication.Protocol;
import rp.warehouse.pc.data.robot.Robot;
import rp.warehouse.pc.data.robot.RobotLocation;

import java.util.concurrent.BlockingQueue;

public class RobotPoseProvider implements PoseProvider, Runnable {
    private Robot robot;
    private final Object lock = new Object();
    private Pose currentPose;
    private static final float INTERPOLATION = 0.01f;

    public RobotPoseProvider(Robot robot) {
        this.robot = robot;
        currentPose = robot.getLocation().toPose();
    }

    @Override
    public Pose getPose() {
        return robot.getLocation().toPose();
    }

    @Override
    public void setPose(Pose aPose) {
        throw new RuntimeException("Cannot set pose here");
    }

    @Override
    public void run() {
        Rate r = new Rate(8);
        RobotLocation previous = robot.getLocation();
        int heading;
        while (true) {
            if (!robot.getLocation().equals(previous)) {
                previous = robot.getLocation();
                currentPose = robot.getLocation().toPose();

                heading = robot.getLocation().getDirection();

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
            switch ((int)currentPose.getHeading()) {
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
}
