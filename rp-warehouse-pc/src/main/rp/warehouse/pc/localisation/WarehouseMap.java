package rp.warehouse.pc.localisation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import lejos.geom.Point;

/**
 * A bidirectional map for the warehouse which stores both the ranges and points
 * of every grid coordinate on the map.
 * 
 * @author Kieran
 *
 */
public class WarehouseMap {

	private final HashMap<Ranges, HashSet<Point>> positions = new HashMap<Ranges, HashSet<Point>>();
	private final HashMap<Point, Ranges> ranges = new HashMap<Point, Ranges>();

	/**
	 * Method to add ranges and points to the warehouse map.
	 * 
	 * @param ranges
	 *            The ranges of which occur at a given point.
	 * @param point
	 *            The point of which the ranges occur at.
	 */
	public void put(final Ranges ranges, final Point point) {
		final HashSet<Point> points = positions.getOrDefault(ranges, new HashSet<Point>());
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
	 */
	public List<Point> getPoints(final Ranges ranges) {
		return new ArrayList<Point>(this.positions.get(ranges));
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

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		// Iterate over the points of the warehouse.
		final Iterator<Point> iterator = this.ranges.keySet().iterator();
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
