package rp.warehouse.pc.localisation.interfaces;

import rp.warehouse.pc.data.robot.utils.RobotLocation;
import rp.warehouse.pc.localisation.NoIdeaException;

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
	 * @throws NoIdeaException
	 */
	public RobotLocation getPosition() throws NoIdeaException;

}
