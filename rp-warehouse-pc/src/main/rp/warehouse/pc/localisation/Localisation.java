package rp.warehouse.pc.localisation;

import rp.warehouse.pc.data.RobotLocation;

public interface Localisation {

	/**
	 * Method to get the estimated position with the highest probability.
	 * 
	 * @return The estimated position of the robot.
	 */
	public RobotLocation getPosition();

}
