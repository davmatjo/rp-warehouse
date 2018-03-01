package rp.warehouse.pc.localisation;

import lejos.geom.Point;

public interface Localisation {

	/**
	 * Method to get the estimated position with the highest probability.
	 * 
	 * @return The estimated position of the robot.
	 */
	public Point getPosition();

}
