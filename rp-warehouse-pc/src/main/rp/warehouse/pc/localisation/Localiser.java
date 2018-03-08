package rp.warehouse.pc.localisation;

import java.util.List;
import java.util.Random;

import lejos.geom.Point;
import rp.warehouse.pc.communication.Communication;
import rp.warehouse.pc.data.RobotLocation;

/**
 * An implementation of the localisation interface. Used to actually calculate
 * the location.
 * 
 * @author Kieran
 *
 */
public class Localiser implements Localisation {

	// Currently assumes that all robots are facing upwards relative to the map.

	private final WarehouseMap warehouseMap = new WarehouseMap();
	private final Point[] directionPoint = new Point[4];
	private final List<Point> blockedPoints = WarehouseMap.getBlockedPoints();
	private final byte MAX_RUNS = 10;
	private byte runCounter = 0;
	private final Random random = new Random();
	private Byte previousDirection = null;

	/**
	 * An implementation of the Localisation interface.
	 */
	public Localiser(Communication comms) {
		directionPoint[Ranges.UP] = new Point(0, 1);
		directionPoint[Ranges.RIGHT] = new Point(1, 0);
		directionPoint[Ranges.DOWN] = new Point(0, -1);
		directionPoint[Ranges.LEFT] = new Point(-1, 0);
	}

	@Override
	public RobotLocation getPosition() {
		// Assuming they all face up initially
		// Get the readings from the sensors (using dummy values now)
		Point testPoint = new Point(0, 2);
		Ranges ranges = warehouseMap.getRanges(testPoint);

		List<Point> possiblePoints = warehouseMap.getPoints(ranges);

		// Run whilst there are multiple points, or the maximum iterations has occurred.
		while (possiblePoints.size() > 1 && runCounter++ < MAX_RUNS) {
			List<Byte> directions = ranges.getAvailableDirections();
			if (runCounter > 1) {
				directions.remove(directions.indexOf(Ranges.getOpposite(previousDirection)));
			}
			// Choose a random direction from the list of available directions.
			final byte direction = directions.get(random.nextInt(directions.size()));
			previousDirection = direction;
			final Point move = directionPoint[direction];
			if (direction == Ranges.UP) {
				// Move, get ranges
			} else if (direction == Ranges.RIGHT) {
				// Move, get ranges
			} else if (direction == Ranges.DOWN) {
				// Move, get ranges
			} else {
				// Move, get ranges
			}
			testPoint = testPoint.add(move);
			ranges = warehouseMap.getRanges(testPoint);
			possiblePoints = filterPositions(possiblePoints, warehouseMap.getPoints(ranges), move);
		}
		// Create the location of the robot using the first possible location from the
		// list of possible locations.
		return new RobotLocation(possiblePoints.get(0), 90);
	}

	/**
	 * Method to filter initial positions given new positions and a movement. Used
	 * to narrow down the possibility of location.
	 * 
	 * @param initial
	 *            The initial possible positions recorded.
	 * @param next
	 *            The new possible positions recorded.
	 * @param change
	 *            The change in position from <b>initial</b> to <b>next</b>.
	 * @return The new list of possible positions of the robot.
	 */
	private List<Point> filterPositions(final List<Point> initial, final List<Point> next, final Point change) {
		// Filter the next list by removing all points that couldn't exist given the
		// previous points and the change in position.
		next.removeIf(p -> !initial.contains(p.subtract(change)) || blockedPoints.contains(p));
		return next;
	}

}
