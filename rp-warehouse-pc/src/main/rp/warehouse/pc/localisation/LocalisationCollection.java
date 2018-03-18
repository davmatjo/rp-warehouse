package rp.warehouse.pc.localisation;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import lejos.geom.Point;
import rp.warehouse.pc.localisation.implementation.Localiser;

/**
 * 
 * @author Kieran
 *
 */
public class LocalisationCollection {

	private final static Point[] directionPoint = new Point[] { new Point(0, 1), new Point(1, 0), new Point(0, -1),
			new Point(-1, 0) };
	private final static WarehouseMap map = new WarehouseMap();
	private final static List<Point> blockedPoints = WarehouseMap.getBlockedPoints();
	private static final Logger logger = Logger.getLogger(Localiser.class);

	private final byte startingDirection;
	private List<Point> possibleLocations = new ArrayList<Point>();
	private byte heading;

	public LocalisationCollection(final byte direction) {
		this.startingDirection = direction;
		this.heading = direction;
	}

	public void update(final byte direction, final Ranges ranges) {
		heading = (byte) ((heading + direction) % 4);
		final Point move = directionPoint[heading];
		try {
			List<Point> possiblePoints = map.getPoints(Ranges.rotate(ranges, heading));
			possibleLocations = filterPositions(possibleLocations, possiblePoints, move);
		} catch (NoIdeaException e) {
			logger.info("(" + startingDirection + "): No more directions");
			e.printStackTrace();
		}
	}

	public boolean isComplete() {
		return possibleLocations.size() == 1;
	}

	public boolean notComplete() {
		return possibleLocations.size() > 1;
	}

	public Point getPoint() {
		return possibleLocations.get(0);
	}

	public byte getHeading() {
		return heading;
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
		logger.info("-- (" + startingDirection + ") Filtering");
		logger.info("(" + startingDirection + ") Initial ranges: " + initial);
		logger.info("(" + startingDirection + ") Next ranges: " + next);
		// Filter the next list by removing all points that couldn't exist given the
		// previous points and the change in position.
		next.removeIf(p -> !initial.contains(p.subtract(change)) || blockedPoints.contains(p));
		return next;
	}

}
