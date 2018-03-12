package rp.warehouse.pc.localisation.implementation;

import rp.warehouse.pc.communication.Protocol;
import rp.warehouse.pc.data.RobotLocation;
import rp.warehouse.pc.localisation.interfaces.Localisation;

public class DummyLocaliser implements Localisation {

	private final int x;
	private final int y;

	public DummyLocaliser(final int x, final int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public RobotLocation getPosition() {
		return new RobotLocation(x, y, Protocol.NORTH);
	}

}
