package rp.warehouse.pc.localisation;

import lejos.geom.Point;
import lejos.robotics.navigation.Pose;

public class EstimatedPose extends Pose {

	// Currently unused class, may use later.

	private float prob;

	/**
	 * A class to represent an estimated pose of the robot. Extends the default Pose
	 * to include a probability.
	 * 
	 * @param x
	 *            The X coordinate of the robot.
	 * @param y
	 *            The Y coordinate of the robot.
	 * @param heading
	 *            The direction of which the robot is facing.
	 * @param probability
	 *            The probability of this pose being correct.
	 */
	public EstimatedPose(final int x, final int y, final float heading, final float probability) {
		super(x, y, heading);
		this.prob = probability;
	}

	/**
	 * Method to retrieve the probability of this pose being correct.
	 * 
	 * @return The probability of this pose being correct.
	 */
	public float getProbability() {
		return this.prob;
	}

	/**
	 * Method to alter the probability of this pose being correct.
	 * 
	 * @param probability
	 *            The new probability to set.
	 */
	public void setProbability(final float probability) {
		this.prob = probability;
	}

	/**
	 * Method to represent the pose as a point.
	 * 
	 * @return The point from the pose.
	 */
	public Point toPoint() {
		return new Point(getX(), getY());
	}

	@Override
	public String toString() {
		return getX() + ";" + getY() + ";" + getHeading() + ";" + prob;
	}

}
