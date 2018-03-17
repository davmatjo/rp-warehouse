package rp.warehouse.pc.localisation.implementation;

import java.util.HashSet;
import java.util.List;
import java.util.Random;

import org.apache.log4j.Logger;

import lejos.geom.Point;
import rp.warehouse.pc.communication.Communication;
import rp.warehouse.pc.communication.Protocol;
import rp.warehouse.pc.data.RobotLocation;
import rp.warehouse.pc.localisation.NoIdeaException;
import rp.warehouse.pc.localisation.Ranges;
import rp.warehouse.pc.localisation.WarehouseMap;
import rp.warehouse.pc.localisation.interfaces.Localisation;

/**
 * An implementation of the localisation interface. Used to actually calculate
 * the location.
 *
 * @author Kieran
 */
public class Localiser implements Localisation {

	// Currently assumes that all robots are facing upwards relative to the map.

	private static final Logger logger = Logger.getLogger(Localiser.class);
	private final WarehouseMap warehouseMap = new WarehouseMap();
	private final Point[] directionPoint = new Point[4];
	private final List<Point> blockedPoints = WarehouseMap.getBlockedPoints();
	private final byte MAX_RUNS = 100;
	private byte runCounter = 0;
	private final Random random = new Random();
	private Byte previousDirection = null;
	private final Communication comms;
	private Point relativePoint = new Point(0, 0);
	private final HashSet<Point> relativeVisitedPoints = new HashSet<Point>();

	/**
	 * An implementation of the Localisation interface.
	 */
	public Localiser(Communication comms) {
		relativeVisitedPoints.add(new Point(0, 0));
		directionPoint[Ranges.UP] = new Point(0, 1);
		directionPoint[Ranges.RIGHT] = new Point(1, 0);
		directionPoint[Ranges.DOWN] = new Point(0, -1);
		directionPoint[Ranges.LEFT] = new Point(-1, 0);
		this.comms = comms;
	}

	@Override
	public RobotLocation getPosition() throws NoIdeaException {
		// Assuming they all face up initially
		// Get the readings from the sensors (using dummy values now)
		Ranges ranges = comms.getRanges();

		List<Point> possiblePoints = warehouseMap.getPoints(ranges);
		logger.info("Possible points: " + possiblePoints);

		// Run whilst there are multiple points, or the maximum iterations has occurred.
		while (possiblePoints.size() > 1 || runCounter++ < MAX_RUNS) {
			List<Byte> directions = ranges.getAvailableDirections();
			if (runCounter > 1) {
				directions.remove(directions.indexOf(Ranges.getOpposite(previousDirection)));
			}
			// Filter if it can go somewhere
			if (directions.size() > 1) {
				// Remove all directions that would lead to visiting the same point again.
				directions.removeIf(d -> relativeVisitedPoints.contains(relativePoint.add(directionPoint[d])));
			}
			logger.info("Available directions: " + directions);
			// Choose a random direction from the list of available directions.
			final byte direction = directions.get(random.nextInt(directions.size()));
			previousDirection = direction;
			final Point move = directionPoint[direction];
			logger.info("Chosen move: " + move);
			if (direction == Ranges.UP) {
				comms.sendMovement(Protocol.NORTH);
			} else if (direction == Ranges.RIGHT) {
				comms.sendMovement(Protocol.EAST);
			} else if (direction == Ranges.DOWN) {
				comms.sendMovement(Protocol.SOUTH);
			} else {
				comms.sendMovement(Protocol.WEST);
			}
			// Update relative position
			relativePoint = relativePoint.add(move);
			relativeVisitedPoints.add(relativePoint);
			logger.info("Previous direction: " + previousDirection);
			logger.info("Reversal rotation amount: " + direction);
			// Update ranges
			ranges = comms.getRanges();
			logger.info("Received ranges: " + ranges);
			// Rotate ranges
			ranges = Ranges.rotate(ranges, direction);
			logger.info("Rotated ranges: " + ranges);
			possiblePoints = filterPositions(possiblePoints, warehouseMap.getPoints(ranges), move);
			logger.info("Filtered positions: " + possiblePoints);
		}

		if (possiblePoints.isEmpty()) {
			throw new NoIdeaException(ranges);
		}

		// list of possible locations.
		return new RobotLocation(possiblePoints.get(0), Protocol.NORTH);
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
		logger.info("-- Filtering");
		logger.info("Initial ranges: " + initial);
		logger.info("Next ranges: " + next);
		// Filter the next list by removing all points that couldn't exist given the
		// previous points and the change in position.
		next.removeIf(p -> !initial.contains(p.subtract(change)) || blockedPoints.contains(p));
		return next;
	}

}
