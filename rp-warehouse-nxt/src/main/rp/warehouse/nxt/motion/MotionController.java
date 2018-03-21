package rp.warehouse.nxt.motion;

import lejos.nxt.*;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.util.Delay;
import rp.config.WheeledRobotConfiguration;
import rp.systems.WheeledRobotSystem;
import rp.util.Rate;
import rp.warehouse.nxt.communication.Protocol;

/**
 * @author Marcos Manning
 * 
 * This class moves the robot based on directions and gives feedback once the action is complete.
 * It also ensures the robot follows the line properly and detects junctions. 
 */

public class MotionController implements Movement {

	private double leftLineLimit = 0;
	private double rightLineLimit= 0;
	private DifferentialPilot pilot;
	private LightSensor leftSensor;
	private LightSensor rightSensor;
	private Direction previousDirection;

	/**
	 * Constructor
	 * @param educatorBot robot configuration
	 * @param port1 left light sensor
	 * @param port2 right light sensor
	 */
	public MotionController(WheeledRobotConfiguration educatorBot, SensorPort port1, SensorPort port2) {
		this.pilot = new WheeledRobotSystem(educatorBot).getPilot();
		this.pilot.setTravelSpeed(0.18);
		this.leftSensor = new LightSensor(port1);
		this.rightSensor = new LightSensor(port2);
		this.previousDirection = Direction.NORTH;
		calibrateSensors();
	}

	/**
	 * Sets the facing direction
	 * @param direction
	 */
	public void setDirection(int direction) {
		switch (direction) {
			case Protocol.NORTH:
				previousDirection = Direction.NORTH;
				break;
			case Protocol.EAST:
				previousDirection = Direction.EAST;
				break;
			case Protocol.SOUTH:
				previousDirection = Direction.SOUTH;
				break;
			case Protocol.WEST:
				previousDirection = Direction.WEST;
				break;
		}
	}

	@Override
	/**
	 * Moves the robot in the specified based on the direction it was previously facing.
	 * 
	 * @param direction the direction to move in
	 * @return True if the action is completed, False if something went wrong
	 */
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

	/**
	 * Helper for move function.
	 * Does line checking and junction detection.
	 * @param rotation amount to rotate
	 * @return True if the movement is completed, False if not
	 */
	private boolean travel(int rotation) {
		boolean junction = false;

		pilot.rotate(rotation);
		pilot.forward();


		while (!junction) {

			double leftValue = leftSensor.getLightValue();
			double rightValue = rightSensor.getLightValue();

			// checks if a junction has been reached
			if (leftValue < leftLineLimit && rightValue < rightLineLimit) {
				junction = true;
			}
			// check is robot has gone off the line and adjust
			else if (leftValue < leftLineLimit) {
				pilot.steer(42);
			} else if (rightValue < rightLineLimit) {
				pilot.steer(-42);
			} else {
				pilot.steer(0);
			}

		}
		// returns true once it has reached a junction
		pilot.travel(0.08);
		pilot.stop();
		return true;
	}

	/**
	 * Calibrates the sensor on startup.
	 */
	private void calibrateSensors() {

		//get average value for sensors on the line
		int rightDark = 0;
		int leftDark = 0;
		for (int i = 0; i < 3; i++) {
			System.out.println("Put both sensors on a black line and press a button.");
			Button.waitForAnyPress();
			rightDark += rightSensor.getLightValue();
			leftDark += leftSensor.getLightValue();
		}
		rightDark = rightDark/3;
		leftDark = leftDark/3;
		System.out.println("Dark value finshed");
		
		//get average value for sensor not on the line
		int rightLight= 0;
		int leftLight = 0;
		for (int i = 0; i < 3; i++) {
			System.out.println("Put both sensors in the light and press a button.");
			Button.waitForAnyPress();
			rightLight += rightSensor.getLightValue();
			leftLight += leftSensor.getLightValue();
		}
		rightLight = rightLight/3;
		leftLight = leftLight/3;
		System.out.println("Light value finshed");

		//sets limit as midpoint of the two values
		rightLineLimit = (rightDark + rightLight)*0.5;
		leftLineLimit = (leftDark + leftLight)*0.5;
		LCD.drawString("l " + leftLineLimit + " r " + rightLineLimit, 0, 0);
	}

	/**
	 * Rotates the robot at right angles. Used by localisation.
	 */
	public void rotate() {
		pilot.rotate(90);
	}
}
