package rp.warehouse.pc.localisation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import lejos.geom.Point;
import rp.robotics.mapping.GridMap;
import rp.warehouse.pc.data.RobotLocation;
import rp.warehouse.pc.data.Warehouse;

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
	private final GridMap world = Warehouse.build();
	private Point[] directionPoint = new Point[4];
	private final List<Point> blockedPoints = new ArrayList<Point>();
	private final int MAX_RUNS = 10;
	private int runCounter = 0;
	private final Random random = new Random();

	/**
	 * An implementation of the Localisation interface.
	 * 
	 * @param warehouse
	 *            The LineMap representation of the warehouse.
	 */
	public Localiser() {
		directionPoint[Ranges.FRONT] = new Point(0, 1);
		directionPoint[Ranges.RIGHT] = new Point(1, 0);
		directionPoint[Ranges.BACK] = new Point(0, -1);
		directionPoint[Ranges.LEFT] = new Point(-1, 0);

		// Populate blockedPoints with the Locations from the warehouse.
		Warehouse.getBlockedLocations().forEach(l -> blockedPoints.add(l.toPoint()));

		// Generate the warehouseMap values using world.
		// One more problem with git and I'm making my own version control software.
		for (int x = 0; x < world.getXSize(); x++) {
			for (int y = 0; y < world.getYSize(); y++) {
				// Create a point from the X and Y co-ordinates.
				final Point point = new Point(x, y);
				// Check if the position isn't blocked
				if (!blockedPoints.contains(point)) {
					// Take the UP, RIGHT, DOWN and LEFT readings.
					// Casted to ints to remove floating point (stick to grid).
					final int up = (int) world.rangeToObstacleFromGridPosition(x, y, 0);
					final int right = (int) world.rangeToObstacleFromGridPosition(x, y, 90);
					final int down = (int) world.rangeToObstacleFromGridPosition(x, y, 180);
					final int left = (int) world.rangeToObstacleFromGridPosition(x, y, 270);
					// Create a Ranges object from these readings.
					final Ranges ranges = new Ranges(up, right, down, left);

					// Store them in the warehouse map.
					warehouseMap.put(ranges, point);
				}
			}
		}
	}

	@Override
	public RobotLocation getPosition() {
		// Assuming they all face up initially
		// Get the readings from the sensors (using dummy values now)
		Ranges ranges = new Ranges(1, 2, 4, 2);

		List<Point> possiblePoints = warehouseMap.getPoints(ranges);

		// Run whilst there are multiple points, or the maximum iterations has occurred.
		while (possiblePoints.size() > 1 && runCounter++ < MAX_RUNS) {
			List<Integer> directions = ranges.getAvailableDirections();
			final int direction = directions.get(random.nextInt(directions.size()));
			final Point move = directionPoint[direction];
			if (direction == Ranges.FRONT) {
				// Move, get ranges
			} else if (direction == Ranges.RIGHT) {
				// Move, get ranges
			} else if (direction == Ranges.BACK) {
				// Move, get ranges
			} else {
				// Move, get ranges
			}
			// Dummy range for now
			ranges = new Ranges(0, 2, 5, 2);
			possiblePoints = filterPositions(possiblePoints, warehouseMap.getPoints(ranges), move);
		}

		return new RobotLocation(possiblePoints.get(0), 0);
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
	private List<Point> filterPositions(final List<Point> initial, List<Point> next, final Point change) {
		// Filter the next list by removing all points that couldn't exist given the
		// previous points and the change in position.
		next.removeIf(p -> !initial.contains(p.subtract(change)) || blockedPoints.contains(p));
		return next;
	}

}
