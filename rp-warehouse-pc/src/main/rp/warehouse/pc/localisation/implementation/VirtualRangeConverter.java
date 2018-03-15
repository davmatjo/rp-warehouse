package rp.warehouse.pc.localisation.implementation;

import rp.warehouse.pc.localisation.interfaces.RangeConverter;

public class VirtualRangeConverter implements RangeConverter {

	@Override
	public boolean toGrid(float range) {
		return ((range - 0.1d) / 0.3d) > 0;
	}

}
