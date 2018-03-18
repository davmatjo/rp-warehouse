package rp.warehouse.pc.localisation.implementation;

import rp.warehouse.pc.localisation.interfaces.RangeConverter;

/**
 * An implementation of a range converter for the <b>physical</b> world - the actual
 * world in which the robots will be used. Local units are in cm.
 * 
 * @author Kieran
 *
 */
public class PhysicalRangeConverter implements RangeConverter {

	// Offset of the sensor from the robot in cm.
	private final static int SENSOR_OFFSET = 5;

	@Override
	public boolean toGrid(float range) {
		// Grid points are 27cm apart.
		return range > 27 - SENSOR_OFFSET;
	}

}
