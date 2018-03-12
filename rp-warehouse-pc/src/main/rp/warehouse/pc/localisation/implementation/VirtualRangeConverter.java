package rp.warehouse.pc.localisation.implementation;

import rp.warehouse.pc.localisation.interfaces.RangeConverter;

public class VirtualRangeConverter implements RangeConverter {

	@Override
	public byte toGrid(float range) {
		final byte gridRange = (byte) ((range - 0.1d) / 0.3d);
		return gridRange < 0 ? 0 : gridRange;
	}

}
