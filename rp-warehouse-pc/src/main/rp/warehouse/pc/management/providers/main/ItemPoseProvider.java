package rp.warehouse.pc.management.providers.main;

import lejos.robotics.localization.PoseProvider;
import lejos.robotics.navigation.Pose;
import rp.warehouse.pc.data.robot.Robot;

/**
 * Provides the positions of items being picked up
 * @author dxj786
 */
public class ItemPoseProvider implements PoseProvider {
    private Robot robot;

    /**
     * create an item pose provider listening on a given robot
     * @param robot robot to watch
     */
    public ItemPoseProvider(Robot robot) {
        this.robot = robot;
    }

    @Override
    public Pose getPose() {
        return robot.getTask().getItem().getLocation().toPose();
    }

    @Override
    public void setPose(Pose aPose) {
        throw new RuntimeException("Cannot set pose here");
    }
}
