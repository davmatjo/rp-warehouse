package rp.warehouse.pc.localisation.implementation;

import rp.warehouse.pc.localisation.interfaces.RangeConverter;

/**
 * 
 * @author Kieran
 *
 */
public class VirtualRangeConverter implements RangeConverter {

	@Override
	public boolean toGrid(float range) {
		return range > 0.3;
	}

}
