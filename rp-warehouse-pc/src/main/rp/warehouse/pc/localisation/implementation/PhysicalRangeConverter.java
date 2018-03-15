package rp.warehouse.pc.localisation.implementation;

import rp.warehouse.pc.localisation.interfaces.RangeConverter;

/**
 * 
 * @author Kieran
 *
 */
public class PhysicalRangeConverter implements RangeConverter {

	private final static int SENSOR_OFFSET = 5;

	@Override
	public boolean toGrid(float range) {
		return range > 27 - SENSOR_OFFSET;
	}

}
