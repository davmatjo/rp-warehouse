package rp.warehouse.pc.localisation;

import rp.warehouse.pc.data.RobotLocation;

/**
 * An interface for the localisation, simply to get the location of the robot.
 * 
 * @author Kieran
 *
 */
public interface Localisation {

	/**
	 * Method to get the estimated position with the highest probability.
	 * 
	 * @return The estimated position of the robot.
	 */
	public RobotLocation getPosition();

}
