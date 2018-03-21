package rp.warehouse.pc.localisation.implementation;

import rp.warehouse.pc.localisation.interfaces.RangeConverter;

/**
 * An implementation of a range converter for the <b>physical</b> world - the
 * actual world in which the robots will be used. Local units are in cm.
 * 
 * @author Kieran
 *
 */
public class PhysicalRangeConverter implements RangeConverter {

	// Offset of the sensor from the robot in cm.
	private final static float SENSOR_OFFSET = 5;
	private final static float ADDITIONAL_THRESHOLD = 2;

	/**
	 * Converts the given physical reading to a value suitable for the Ranges class.
	 * 
	 * @param range
	 *            the physical range reading.
	 * @return the suitable value for the Ranges class from the physical reading.
	 */
	@Override
	public boolean toGrid(float range) {
		// Grid points are 27cm apart.
		return range > (27 + ADDITIONAL_THRESHOLD) - SENSOR_OFFSET;
	}

}
