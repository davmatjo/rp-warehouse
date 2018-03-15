package rp.warehouse.pc.localisation.estimations;

import lejos.geom.Point;
import rp.warehouse.pc.data.RobotLocation;

/**
 * 
 * @author Kieran
 *
 */
public class EstimatedRobotLocation extends RobotLocation {

	private double probability;

	public EstimatedRobotLocation(int x, int y, int direction, double probability) {
		super(x, y, direction);
		this.probability = probability;
	}

	public EstimatedRobotLocation(Point point, int direction, double probability) {
		super(point, direction);
		this.probability = probability;
	}

	public double getProbability() {
		return this.probability;
	}

	public void setProbability(double probability) {
		this.probability = probability;
	}

}
