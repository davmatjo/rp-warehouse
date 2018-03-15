package rp.warehouse.nxt.motion;

import lejos.nxt.Button;
import lejos.nxt.LightSensor;
import lejos.nxt.SensorPort;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.util.Delay;
import rp.config.WheeledRobotConfiguration;
import rp.systems.WheeledRobotSystem;
import rp.util.Rate;

public class MotionController implements Movement {

	// temporary values until calibration is added
	private int leftLineLimit;
	private int rightLineLimit;
	private DifferentialPilot pilot;
	private LightSensor leftSensor;
	private LightSensor rightSensor;
	private Direction previousDirection;

	public MotionController(WheeledRobotConfiguration educatorBot, SensorPort port1, SensorPort port2) {
		this.pilot = new WheeledRobotSystem(educatorBot).getPilot();
		this.leftSensor = new LightSensor(port1);
		this.rightSensor = new LightSensor(port2);
		this.previousDirection = Direction.NORTH;
		calibrateSensors();
		leftLineLimit = leftSensor.getLightValue() - 15;
		rightLineLimit = rightSensor.getLightValue() - 15;

	}

	@Override
	public boolean move(Direction direction) {

		int rotation = 0;

		// find out which way to turn based on the new direction and the direction the
		// robot is facing
		switch (direction) {
		case NORTH:
			switch (previousDirection) {
			case NORTH:
				break;
			case EAST:
				rotation = -90;
				break;
			case SOUTH:
				rotation = 180;
				break;
			case WEST:
				rotation = 90;
				break;
			}
			break;
		case EAST:
			switch (previousDirection) {
			case NORTH:
				rotation = 90;
				break;
			case EAST:
				break;
			case SOUTH:
				rotation = -90;
				break;
			case WEST:
				rotation = 180;
				break;
			}
			break;
		case SOUTH:
			switch (previousDirection) {
			case NORTH:
				rotation = 180;
				break;
			case EAST:
				rotation = 90;
				break;
			case SOUTH:
				break;
			case WEST:
				rotation = -90;
				break;
			}
			break;
		case WEST:
			switch (previousDirection) {
			case NORTH:
				rotation = -90;
				break;
			case EAST:
				rotation = 180;
				break;
			case SOUTH:
				rotation = 90;
				break;
			case WEST:
				break;
			}
			break;
		default:
			return false;
		}

		previousDirection = direction;
		return travel(rotation);
	}

	private boolean travel(int rotation) {
		boolean junction = false;

		pilot.rotate(rotation);
		pilot.setTravelSpeed(0.12);
		pilot.forward();

		Rate r = new Rate(20);

		while (!junction) {

			int leftValue = leftSensor.getLightValue();
			int rightValue = rightSensor.getLightValue();

			// checks if a junction has been reached
			if (leftValue < leftLineLimit && rightValue < rightLineLimit) {
				pilot.stop();
				junction = true;
			}
			// check is robot has gone off the line and adjust
			else if (leftValue < leftLineLimit) {
				pilot.steer(42);
			} else if (rightValue < rightLineLimit) {
				pilot.steer(-42);
				;
			} else {
				pilot.steer(0);
			}

			r.sleep();
		}
		// returns true once it has reached a junction
		pilot.travel(0.08);
		return true;
	}

	// calibrates the sensors on startup.
	private void calibrateSensors() {

		System.out.println("Put both sensors on a black line and press a button.");
		Delay.msDelay(300);
		leftSensor.calibrateHigh();
		rightSensor.calibrateHigh();

		System.out.println("Sensors have been calibrated!");
	}

	public void rotate() {
		pilot.rotate(90);
	}
}
