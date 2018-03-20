package rp.warehouse.pc.localisation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import lejos.geom.Point;
import rp.robotics.mapping.GridMap;
import rp.warehouse.pc.data.Location;
import rp.warehouse.pc.data.Warehouse;

/**
 * A bidirectional map for the warehouse which stores both the ranges and points
 * of every grid coordinate on the map.
 * 
 * @author Kieran
 *
 */
public class WarehouseMap {

	private final HashMap<Ranges, ArrayList<Point>> positions = new HashMap<>();
	private final HashMap<Point, Ranges> ranges = new HashMap<>();
	private final Point up = new Point(0, 1), right = new Point(1, 0), down = new Point(0, -1), left = new Point(-1, 0);
	private static final List<Point> blockedPoints = Warehouse.getBlockedLocations().stream().map(Location::toPoint)
			.collect(Collectors.toList());

	/**
	 * Generates the ranges of from every point within the warehouse.
	 */
	public WarehouseMap() {
		final GridMap world = Warehouse.build();
		// Generate the warehouseMap values using world.
		for (byte x = 0; x < world.getXSize(); x++) {
			for (byte y = 0; y < world.getYSize(); y++) {
				// Create a point from the X and Y co-ordinates.
				final Point point = new Point(x, y);
				// Check if the position isn't blocked
				if (!blockedPoints.contains(point)) {
					// Take the UP, RIGHT, DOWN and LEFT readings.
					final float up = world.rangeToObstacleFromGridPosition(x, y, 90);
					final float right = world.rangeToObstacleFromGridPosition(x, y, 0);
					final float down = world.rangeToObstacleFromGridPosition(x, y, -90);
					final float left = world.rangeToObstacleFromGridPosition(x, y, 180);
					// Create a Ranges object from these readings.
					final Ranges ranges = new Ranges(up, right, down, left, Ranges.virtualConverter);

					// Store them in the warehouse map.
					put(ranges, point);
				}
			}
		}
	}

	/**
	 * Method to add ranges and points to the warehouse map.
	 * 
	 * @param ranges
	 *            The ranges of which occur at a given point.
	 * @param point
	 *            The point of which the ranges occur at.
	 */
	private void put(final Ranges ranges, final Point point) {
		final ArrayList<Point> points = positions.getOrDefault(ranges, new ArrayList<>());
		points.add(point);
		this.positions.put(ranges, points);
		this.ranges.put(point, ranges);
	}

	/**
	 * Method to retrieve the all of the points of which have the given ranges.
	 * 
	 * @param ranges
	 *            The ranges to check for.
	 * @return The points matching the ranges given.
	 * @throws NoIdeaException
	 */
	public ArrayList<Point> getPoints(final Ranges ranges) throws NoIdeaException {
		if (this.positions.containsKey(ranges)) {
			return new ArrayList<>(this.positions.get(ranges));
		} else {
			throw new NoIdeaException(ranges);
		}
	}

	/**
	 * Method to retrieve the ranges at a given specific point.
	 * 
	 * @param point
	 *            The point to retrieve.
	 * @return The ranges of which occur at that point.
	 */
	public Ranges getRanges(final Point point) {
		return this.ranges.get(point);
	}

	/**
	 * Method to retrieve the blocked points within the warehouse.
	 * 
	 * @return List of the blocked points in the warehouse.
	 */
	public static List<Point> getBlockedPoints() {
		return blockedPoints;
	}

	public void updateRangesAroundPositions(final Point point) {
		final GridMap world = Warehouse.build();
		List<Point> pointsAround = new ArrayList<>();
		pointsAround.add(point.add(up));
		pointsAround.add(point.add(right));
		pointsAround.add(point.add(down));
		pointsAround.add(point.add(left));
		// Remove the points from the ones to change if it is outside of the map, or
		// inside blocked locations.
		pointsAround.removeIf(p -> !world.isValidGridPosition((int) p.x, (int) p.y) || blockedPoints.contains(p));
		for (Point p : pointsAround) {
			final Ranges range = ranges.get(p);
			ranges.remove(p);
			positions.get(range).remove(p);
			if (p.y > point.y) {
				range.set((byte) 2, false);
			} else if (p.x > point.x) {
				range.set((byte) 3, false);
			} else if (p.y < point.y) {
				range.set((byte) 0, false);
			} else {
				range.set((byte) 1, false);
			}
			put(range, p);
		}
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		// Iterate over the points of the warehouse.
		final List<Point> points = new ArrayList<Point>(this.ranges.keySet());
		// Sort by X then Y
		points.sort((p1, p2) -> (int) (p1.x == p2.x ? (p1.y - p2.y) : (p1.x - p2.x)));
		final Iterator<Point> iterator = points.iterator();
		while (iterator.hasNext()) {
			final Point point = iterator.next();
			builder.append("(").append(point.x).append(",").append(point.y).append(") -> ").append(getRanges(point));
			// Add a newline unless it's the last element.
			if (iterator.hasNext())
				builder.append("\n");
		}
		// Build the final string.
		return builder.toString();
	}

}
