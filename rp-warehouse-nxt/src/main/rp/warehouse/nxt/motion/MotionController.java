package rp.warehouse.nxt.motion;

import lejos.nxt.Button;
import lejos.nxt.LightSensor;
import lejos.nxt.SensorPort;
import lejos.robotics.navigation.DifferentialPilot;
import rp.config.WheeledRobotConfiguration;
import rp.systems.WheeledRobotSystem;
import rp.util.Rate;

public class MotionController implements Movement {

	//temporary values until calibration is added
	private int LEFT_LINE_LIMIT;
	private int RIGHT_LINE_LIMIT;
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
	}

	@Override
	public boolean move(Direction direction) {

		int rotation = 0;

		//find out which way to turn based on the new direction and the direction the robot is facing
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
		pilot.setTravelSpeed(0.1);
		pilot.forward();
		
		
		Rate r = new Rate(20);
		
		while (!junction) {
			
			int leftValue = leftSensor.getNormalizedLightValue();
			int rightValue = rightSensor.getNormalizedLightValue();
			
			//checks if a junction has been reached 
			if (leftValue < LEFT_LINE_LIMIT && rightValue < RIGHT_LINE_LIMIT) {
				pilot.stop();
				junction = true;
			}
			//check is robot has gone off the line and adjust
			else if (leftValue < LEFT_LINE_LIMIT) {
				pilot.steer(50);
			}
			else if (rightValue <  RIGHT_LINE_LIMIT) {
				pilot.steer(-50);;
			} else {
			    pilot.steer(0);
            }

			r.sleep();
		}
		//returns true once it has reached a junction
		pilot.travel(0.08);
		return true;
	}

	private void calibrateSensors() {
		
		System.out.println("Put both sensors on a black line and press a button");
		
		Button.waitForAnyPress();
		
		LEFT_LINE_LIMIT = leftSensor.getNormalizedLightValue();
		RIGHT_LINE_LIMIT = rightSensor.getNormalizedLightValue();
	}
	
	
	
	
	
	
}
