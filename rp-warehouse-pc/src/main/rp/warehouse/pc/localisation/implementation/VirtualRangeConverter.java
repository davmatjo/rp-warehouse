package rp.warehouse.pc.localisation.implementation;

import rp.warehouse.pc.localisation.interfaces.RangeConverter;

/**
 * An implementation of a range converter for the <b>virtual</b> world - the
 * same virtual world that the warehouse map is created within. Local units are
 * ambiguous.
 * 
 * @author Kieran
 *
 */
public class VirtualRangeConverter implements RangeConverter {

	@Override
	public boolean toGrid(float range) {
		// Grid points are 0.3 units apart.
		return range > 0.3;
	}

}
