package rp.warehouse.pc.management.providers;

import lejos.robotics.localization.PoseProvider;
import lejos.robotics.navigation.Pose;
import rp.util.Rate;
import rp.warehouse.pc.communication.Protocol;
import rp.warehouse.pc.data.Robot;
import rp.warehouse.pc.data.RobotLocation;

public class RobotPoseProvider implements PoseProvider, Runnable {
    private Robot robot;
    private final Object lock = new Object();
    private Pose currentPose;

    public RobotPoseProvider(Robot robot) {
        this.robot = robot;
        currentPose = robot.getLocation().toPose();
        Thread t = new Thread(this);
        t.setDaemon(true);
        t.start();
    }

    @Override
    public Pose getPose() {
        synchronized (lock) {
            return currentPose;
        }
    }

    @Override
    public void setPose(Pose aPose) {
        throw new RuntimeException("Cannot set pose here");
    }

    @Override
    public void run() {
        Rate r = new Rate(8);
        int interpolater = 0;
        RobotLocation previous = robot.getLocation();
        while (true) {
            synchronized (lock) {
                if (!robot.getLocation().equals(previous)) {
                    interpolater = 0;
                    previous = robot.getLocation();
                    currentPose = robot.getLocation().toPose();

                    int heading = robot.getRoute().peek();

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
                interpolater++;
                switch ((int)currentPose.getHeading()) {
                    case 90:
                        currentPose = new Pose(currentPose.getX() + (float)interpolater / 10f, currentPose.getY(), currentPose.getHeading());
                        break;
                    case -90:
                        currentPose = new Pose(currentPose.getX() - (float)interpolater / 10f, currentPose.getY(), currentPose.getHeading());
                        break;
                    case 180:
                        currentPose = new Pose(currentPose.getX(), currentPose.getY() - (float)interpolater / 10f, currentPose.getHeading());
                        break;
                    case 0:
                        currentPose = new Pose(currentPose.getX(), currentPose.getY() + (float)interpolater / 10f, currentPose.getHeading());
                        break;
                }

            }
            r.sleep();
        }
    }
}
