package rp.warehouse.pc.management;

import lejos.robotics.localization.PoseProvider;
import lejos.robotics.navigation.Pose;
import rp.warehouse.pc.data.Robot;

public class RobotPoseProvider implements PoseProvider {
    private Robot robot;

    public RobotPoseProvider(Robot robot) {
        this.robot = robot;
    }

    @Override
    public Pose getPose() {
        return robot.getLocation().toPose();
    }

    @Override
    public void setPose(Pose aPose) {
        throw new RuntimeException("Cannot set pose here");
    }
}
