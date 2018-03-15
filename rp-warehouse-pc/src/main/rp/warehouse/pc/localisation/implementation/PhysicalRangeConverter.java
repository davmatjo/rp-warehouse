package rp.warehouse.pc.localisation.implementation;

import rp.warehouse.pc.localisation.interfaces.RangeConverter;

public class PhysicalRangeConverter implements RangeConverter {

	@Override
	public boolean toGrid(float range) {
		return (int) ((range + 5) / 27) > 0;
	}

}
