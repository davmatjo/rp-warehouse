package rp.warehouse.pc.data.robot;

import lejos.geom.Point;
import lejos.robotics.localization.PoseProvider;
import lejos.robotics.navigation.Pose;
import rp.robotics.navigation.GridPose;
import rp.robotics.navigation.Heading;
import rp.warehouse.pc.communication.Protocol;
import rp.warehouse.pc.data.Location;
import rp.warehouse.pc.data.Warehouse;

public class RobotLocation extends Location {
	private int direction;

	public RobotLocation(int x, int y, int direction) {
		super(x, y);
		this.direction = direction;
	}

	public RobotLocation(Point point, int direction) {
		super(point);
		this.direction = direction;
	}

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

	private Heading toHeading() {
		switch (direction) {
			case Protocol.NORTH:
				return Heading.PLUS_Y;
			case Protocol.EAST:
				return Heading.PLUS_X;
			case Protocol.SOUTH:
				return Heading.MINUS_Y;
			case Protocol.WEST:
				return Heading.MINUS_X;
		}
		return null;
	}

	@Override
	public Pose toPose() {
		return Warehouse.build().toPose(new GridPose(getX(), getY(), toHeading()));
	}

	@Override
	public String toString() {
		return super.toString() + ", D: " + this.direction;
	}
}
