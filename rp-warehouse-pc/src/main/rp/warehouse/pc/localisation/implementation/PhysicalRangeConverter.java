package rp.warehouse.pc.localisation.implementation;

import rp.warehouse.pc.localisation.interfaces.RangeConverter;

public class PhysicalRangeConverter implements RangeConverter {

	@Override
	public byte toGrid(float range) {
		final byte gridRange = (byte) ((range + 5) / 27);
		return gridRange;
	}

}
