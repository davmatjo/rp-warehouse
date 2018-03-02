package rp.warehouse.nxt;

import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import rp.config.WheeledRobotConfiguration;

public class RobotParameters {
        private static final float WHEEL_DIAMETER = 0.056f;
        private static final float TRACK_WIDTH = 0.11235f;
        private static final float ROBOT_LENGTH = 0.13f;
        private static final NXTRegulatedMotor LEFT_WHEEL = Motor.B;
        private static final NXTRegulatedMotor RIGHT_WHEEL = Motor.C;

        public static final WheeledRobotConfiguration TEAM_24_BOT = new WheeledRobotConfiguration(
                WHEEL_DIAMETER, TRACK_WIDTH, ROBOT_LENGTH, LEFT_WHEEL, RIGHT_WHEEL);

}
