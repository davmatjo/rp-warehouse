package rp.warehouse.pc.localisation;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import lejos.geom.Point;
import rp.warehouse.pc.communication.Protocol;
import rp.warehouse.pc.data.robot.utils.RobotLocation;
import rp.warehouse.pc.localisation.implementation.Localiser;

public class TestLocaliserAssumption {
	private final byte[] opposite = new byte[] { 0, 3, 2, 1 };
	private final byte NORTH = Ranges.UP, EAST = Ranges.RIGHT, SOUTH = Ranges.DOWN, WEST = Ranges.LEFT;
	private final WarehouseMap map = new WarehouseMap();

	@Test
	public void topCornerToBottom() {
		// Actual direction: south
		LocaliserAssumption north = new LocaliserAssumption(NORTH, map);
		LocaliserAssumption east = new LocaliserAssumption(EAST, map);
		LocaliserAssumption south = new LocaliserAssumption(SOUTH, map);
		LocaliserAssumption west = new LocaliserAssumption(WEST, map);
		final Ranges r1 = getRanges(0, 7, SOUTH);
		// Start
		start(r1, north, east, south, west);
		// Move forward
		final byte d2 = Ranges.UP;
		final Ranges r2 = getRanges(0, 6, SOUTH);
		update(d2, r2, north, east, south, west);
		for (int i = 0; i < 5; i++) {
			// Move forward
			final byte d3 = Ranges.UP;
			final Ranges r3 = getRanges(0, 5 - i, SOUTH);
			update(d3, r3, north, east, south, west);
		}
		// Move forward
		final byte d4 = Ranges.UP;
		final Ranges r4 = getRanges(0, 0, SOUTH);
		update(d4, r4, north, east, south, west);

		final RobotLocation loc = getLocation(north, east, south, west);
		final RobotLocation exp = new RobotLocation(0, 0, Protocol.SOUTH);
		Assertions.assertAll(() -> Assertions.assertFalse(needsToRun(north, east, south, west)),
				() -> Assertions.assertEquals(exp, loc));
	}

	@Test
	public void topCornerArcRoundToSecondAisle() {
		// Actual direction: west
		LocaliserAssumption north = new LocaliserAssumption(NORTH, map);
		LocaliserAssumption east = new LocaliserAssumption(EAST, map);
		LocaliserAssumption south = new LocaliserAssumption(SOUTH, map);
		LocaliserAssumption west = new LocaliserAssumption(WEST, map);
		// Start
		final Ranges r1 = getRanges(0, 7, WEST);
		start(r1, north, east, south, west);
		// Turn backwards
		final byte d2 = Ranges.DOWN;
		final Ranges r2 = getRanges(1, 7, EAST);
		update(d2, r2, north, east, south, west);
		// Go forwards
		final byte d3 = Ranges.UP;
		final Ranges r3 = getRanges(2, 7, EAST);
		update(d3, r3, north, east, south, west);
		// Turn right
		final byte d4 = Ranges.RIGHT;
		final Ranges r4 = getRanges(2, 6, SOUTH);
		update(d4, r4, north, east, south, west);
		// Go forwards
		final byte d5 = Ranges.UP;
		final Ranges r5 = getRanges(2, 5, SOUTH);
		update(d5, r5, north, east, south, west);

		final RobotLocation loc = getLocation(north, east, south, west);
		final RobotLocation exp = new RobotLocation(2, 5, Protocol.SOUTH);
		Assertions.assertAll(() -> Assertions.assertFalse(needsToRun(north, east, south, west)),
				() -> Assertions.assertEquals(exp, loc));
	}

	@Test
	public void topOfLastAisleToTopCorner() {
		// Actual direction: south
		LocaliserAssumption north = new LocaliserAssumption(NORTH, map);
		LocaliserAssumption east = new LocaliserAssumption(EAST, map);
		LocaliserAssumption south = new LocaliserAssumption(SOUTH, map);
		LocaliserAssumption west = new LocaliserAssumption(WEST, map);
		// Start
		final Ranges r1 = getRanges(11, 5, SOUTH);
		start(r1, north, east, south, west);
		// Turn backwards
		final byte d2 = Ranges.DOWN;
		final Ranges r2 = getRanges(11, 6, NORTH);
		update(d2, r2, north, east, south, west);
		// Go forwards
		final byte d3 = Ranges.UP;
		final Ranges r3 = getRanges(11, 7, NORTH);
		update(d3, r3, north, east, south, west);

		final RobotLocation loc = getLocation(north, east, south, west);
		final RobotLocation exp = new RobotLocation(11, 7, Protocol.NORTH);
		Assertions.assertAll(() -> Assertions.assertFalse(needsToRun(north, east, south, west)),
				() -> Assertions.assertEquals(exp, loc));
	}

	@Test
	public void needsToRunAfterOneMove() {
		// Actual direction: north
		LocaliserAssumption north = new LocaliserAssumption(NORTH, map);
		LocaliserAssumption east = new LocaliserAssumption(EAST, map);
		LocaliserAssumption south = new LocaliserAssumption(SOUTH, map);
		LocaliserAssumption west = new LocaliserAssumption(WEST, map);
		// Start
		final Ranges r1 = getRanges(5, 2, NORTH);
		start(r1, north, east, south, west);
		// Turn right
		final byte d2 = Ranges.RIGHT;
		final Ranges r2 = getRanges(6, 2, EAST);
		update(d2, r2, north, east, south, west);

		Assertions.assertTrue(needsToRun(north, east, south, west));
	}

	@Test
	public void preLocalisedRobot() {
		// Actual direction: east
		final RobotLocation preLocalised = new RobotLocation(0, 7, Protocol.WEST);

		final WarehouseMap map = new WarehouseMap();
		map.updateRangesAroundPositions(preLocalised.toPoint());

		LocaliserAssumption north = new LocaliserAssumption(NORTH, map);
		LocaliserAssumption east = new LocaliserAssumption(EAST, map);
		LocaliserAssumption south = new LocaliserAssumption(SOUTH, map);
		LocaliserAssumption west = new LocaliserAssumption(WEST, map);
		// Start
		final Ranges r1 = getRanges(2, 1, NORTH);
		start(r1, north, east, south, west);
		// Go forward
		final byte d2 = Ranges.UP;
		final Ranges r2 = getRanges(2, 2, NORTH);
		update(d2, r2, north, east, south, west);

		System.out.println("N: " + north.stream().collect(Collectors.toList()));
		System.out.println("E: " + east.stream().collect(Collectors.toList()));
		System.out.println("S: " + south.stream().collect(Collectors.toList()));
		System.out.println("W: " + west.stream().collect(Collectors.toList()));
	}

	private Ranges getRanges(int x, int y, int heading) {
		return Ranges.rotate(map.getRanges(new Point(x, y)), opposite[heading]);
	}

	private void start(Ranges r, LocaliserAssumption... assumptions) {
		Stream.of(assumptions).forEach(l -> l.start(r));
	}

	private void update(byte d, Ranges r, LocaliserAssumption... assumptions) {
		Stream.of(assumptions).forEach(l -> l.update(d, r));
	}

	private RobotLocation getLocation(LocaliserAssumption... assumptions) {
		return Stream.of(assumptions).filter(LocaliserAssumption::isComplete)
				.map(l -> new RobotLocation(l.getPoint(), Localiser.directionProtocol[l.getHeading()])).findFirst()
				.get();
	}

	private boolean needsToRun(LocaliserAssumption... assumptions) {
		return Stream.of(assumptions).mapToInt(LocaliserAssumption::getNumberOfPoints).sum() != 1;
	}

}
